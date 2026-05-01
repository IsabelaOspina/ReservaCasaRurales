package org.example.reservacasarurales.Service;

import org.example.reservacasarurales.DTOs.Request.DormitorioRequest;
import org.example.reservacasarurales.DTOs.Response.DormitorioResponse;
import org.example.reservacasarurales.Entity.CasaRural;
import org.example.reservacasarurales.Entity.Dormitorio;
import org.example.reservacasarurales.Entity.Propietario;
import org.example.reservacasarurales.Entity.Usuario;
import org.example.reservacasarurales.Exception.MaxDormitoriosException;
import org.example.reservacasarurales.Mapper.DormitorioMapper;
import org.example.reservacasarurales.Repository.CasaRuralRepository;
import org.example.reservacasarurales.Repository.DormitorioRepository;
import org.example.reservacasarurales.Repository.PropietarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DormitorioService {
    @Autowired
    private DormitorioRepository dormitorioRepository;

    @Autowired
    private CasaRuralRepository casaRuralRepository;

    @Autowired
    private DormitorioMapper dormitorioMapper;

    @Autowired
    private PropietarioRepository propietarioRepository;


    //HU009
    @PreAuthorize("hasRole('PROPIETARIO')")
    public DormitorioResponse registrarDormitorio(Long codigoCasa, DormitorioRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        Usuario usuario = (Usuario) authentication.getPrincipal();

        String correo = usuario.getCorreoElectronico();

        System.out.println("Correo autenticado: " + correo);

        Propietario propietario = propietarioRepository
                .findByUsuarioCorreoElectronico(correo)
                .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));

        // buscar casa
        CasaRural casa = casaRuralRepository.findByCodigoCasa(codigoCasa)
                .orElseThrow(() -> new RuntimeException("Casa rural no encontrada"));

        // verificar que la casa pertenece al propietario
        if (!casa.getPropietario().getIdPropietario()
                .equals(propietario.getIdPropietario())) {

            throw new RuntimeException("No tienes permiso para modificar esta casa");
        }

        // validar número máximo de dormitorios
        int dormitoriosActuales = casa.getDormitorios().size();
        if (dormitoriosActuales >= casa.getNumeroDormitorios()) {
            throw new MaxDormitoriosException(casa.getNumeroDormitorios());
        }

        // contar dormitorios con baño
        long dormitoriosConBano = casa.getDormitorios()
                .stream()
                .filter(Dormitorio::isTieneBano)
                .count();

        // validar baños
        if (request.isTieneBano() && dormitoriosConBano >= casa.getNumeroBanos()) {
            throw new MaxDormitoriosException(casa.getNumeroBanos());
        }

        // crear dormitorio
        Dormitorio dormitorio = dormitorioMapper.toEntity(request);
        dormitorio.setCasaRural(casa);

        Dormitorio saved = dormitorioRepository.save(dormitorio);

        DormitorioResponse response = dormitorioMapper.toResponse(saved);
        response.setCodigoCasa(casa.getCodigoCasa());

        return response;
    }

    public List<DormitorioResponse> listarDormitorios(Long codigoCasa) {
        CasaRural casa = casaRuralRepository.findByCodigoCasa(codigoCasa)
                .orElseThrow(() -> new RuntimeException("Casa rural no encontrada"));

        return dormitorioRepository.findByCasaRuralCodigoCasa(codigoCasa)
                .stream()
                .map(dormitorioMapper::toResponse)
                .toList();
    }


}
