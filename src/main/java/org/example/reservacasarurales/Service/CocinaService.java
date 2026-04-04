package org.example.reservacasarurales.Service;

import org.example.reservacasarurales.DTOs.Request.CocinaRequest;
import org.example.reservacasarurales.DTOs.Response.CocinaResponse;
import org.example.reservacasarurales.Entity.CasaRural;
import org.example.reservacasarurales.Entity.Cocina;
import org.example.reservacasarurales.Entity.Propietario;
import org.example.reservacasarurales.Entity.Usuario;
import org.example.reservacasarurales.Exception.MaxCocinasException;
import org.example.reservacasarurales.Mapper.CocinaMapper;
import org.example.reservacasarurales.Repository.CasaRuralRepository;
import org.example.reservacasarurales.Repository.CocinaRepository;
import org.example.reservacasarurales.Repository.PropietarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CocinaService {
    @Autowired
    private CocinaRepository cocinaRepository;

    @Autowired
    private CasaRuralRepository casaRuralRepository;

    @Autowired
    private CocinaMapper cocinaMapper;

    @Autowired
    private PropietarioRepository propietarioRepository;

    //HU010
    @PreAuthorize("hasRole('PROPIETARIO')")
    public CocinaResponse registrarCocina(Long codigoCasa, CocinaRequest request) {

        // obtener usuario autenticado
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        Usuario usuario = (Usuario) authentication.getPrincipal();

        String correo = usuario.getCorreoElectronico();

        Propietario propietario = propietarioRepository
                .findByUsuarioCorreoElectronico(correo)
                .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));

        // buscar casa
        CasaRural casa = casaRuralRepository.findByCodigoCasa(codigoCasa)
                .orElseThrow(() -> new RuntimeException("Casa rural no encontrada"));

        // validar que la casa pertenezca al propietario
        if (!casa.getPropietario().getIdPropietario()
                .equals(propietario.getIdPropietario())) {

            throw new RuntimeException("No tienes permiso para modificar esta casa");
        }

        // validar cantidad de cocinas
        int cocinasActuales = casa.getCocinas().size();
        if (cocinasActuales >= casa.getNumeroCocinas()) {
            throw new MaxCocinasException(casa.getNumeroCocinas());
        }

        Cocina cocina = cocinaMapper.toEntity(request);
        cocina.setCasaRural(casa);

        Cocina saved = cocinaRepository.save(cocina);

        return cocinaMapper.toResponse(saved);
    }


    public List<CocinaResponse> listarCocinas(Long codigoCasa) {
        CasaRural casa = casaRuralRepository.findByCodigoCasa(codigoCasa)
                .orElseThrow(() -> new RuntimeException("Casa rural no encontrada"));

        return cocinaRepository.findByCasaRuralCodigoCasa(codigoCasa)
                .stream()
                .map(cocinaMapper::toResponse)
                .toList();
    }


}
