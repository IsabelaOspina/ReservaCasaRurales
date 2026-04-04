package org.example.reservacasarurales.Service;

import org.example.reservacasarurales.DTOs.Request.CasaRuralRequest;
import org.example.reservacasarurales.DTOs.Response.CasaRuralResponse;
import org.example.reservacasarurales.DTOs.Response.FotoResponse;
import org.example.reservacasarurales.Entity.*;
import org.example.reservacasarurales.Mapper.CasaRuralMapper;
import org.example.reservacasarurales.Mapper.FotoMapper;
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
public class CasaRuralService {

    @Autowired
    private CasaRuralRepository casaRuralRepository;

    @Autowired
    private PropietarioRepository propietarioRepository;

    @Autowired
    private CasaRuralMapper casaRuralMapper;

    @Autowired
    private DormitorioRepository dormitorioRepository;

    @Autowired
    private FotoMapper fotoMapper;

    //HU003
    @PreAuthorize("hasRole('PROPIETARIO')")
    public CasaRuralResponse registrarCasaRural(CasaRuralRequest casaRuralRequest) {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        Usuario usuario = (Usuario) authentication.getPrincipal();

        Propietario propietario = propietarioRepository
                .findByUsuarioCorreoElectronico(usuario.getCorreoElectronico())
                .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));

        if (casaRuralRequest.getNumeroCocinas() < 1) {
            throw new IllegalArgumentException("La casa debe tener al menos 1 cocina");
        }
        if (casaRuralRequest.getNumeroDormitorios() < 3) {
            throw new IllegalArgumentException("La casa debe tener al menos 3 dormitorios");
        }
        if (casaRuralRequest.getNumeroBanos() < 2) {
            throw new IllegalArgumentException("La casa debe tener al menos 2 baños");
        }

        CasaRural casa = casaRuralMapper.toEntity(casaRuralRequest);

        casa.setPropietario(propietario);

        if (casa.getFotos() != null) {
            for (Foto foto : casa.getFotos()) {
                foto.setCasaRural(casa);
            }
        }

        CasaRural saved = casaRuralRepository.save(casa);

        for (int i = 1; i <= casaRuralRequest.getNumeroDormitorios(); i++) {
            Dormitorio dormitorio = new Dormitorio();
            dormitorio.setCasaRural(saved);
            dormitorio.setNombre("Habitación " + i);
            dormitorio.setTipoCama(TipoCama.SENCILLA);
            dormitorio.setNumeroCamas(1);
            dormitorio.setTieneBano(false);
            dormitorioRepository.save(dormitorio);
        }


        return casaRuralMapper.toResponse(saved);
    }

    // HU04 - Eliminar casa
    @PreAuthorize("hasRole('PROPIETARIO')")
    public void eliminarCasa(Long codigoCasa) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal();

        Propietario propietario = propietarioRepository
                .findByUsuarioCorreoElectronico(usuario.getCorreoElectronico())
                .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));

        CasaRural casa = casaRuralRepository.findByCodigoCasa(codigoCasa)
                .orElseThrow(() -> new RuntimeException("Casa no encontrada"));

        if (!casa.getPropietario().getIdPropietario().equals(propietario.getIdPropietario())) {
            throw new RuntimeException("No tienes permiso para eliminar esta casa");
        }

        casaRuralRepository.delete(casa);
    }

    // HU14 - Listar todas las casas
    public List<CasaRuralResponse> listarTodas() {
        return casaRuralRepository.findAll()
                .stream()
                .map(casaRuralMapper::toResponse)
                .toList();
    }

    // HU18 - Listar casas por población
    public List<CasaRuralResponse> listarPorPoblacion(String poblacion) {
        return casaRuralRepository.findByPoblacionContainingIgnoreCase(poblacion)
                .stream()
                .map(casaRuralMapper::toResponse)
                .toList();
    }

    // HU22 - Listar fotos de una casa
    public List<FotoResponse> listarFotos(Long codigoCasa) {
        CasaRural casa = casaRuralRepository.findByCodigoCasa(codigoCasa)
                .orElseThrow(() -> new RuntimeException("Casa no encontrada"));

        return casa.getFotos().stream()
                .map(fotoMapper::toResponse)
                .toList();
    }



}
