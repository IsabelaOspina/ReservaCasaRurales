package org.example.reservacasarurales.Service;

import org.example.reservacasarurales.DTOs.Request.CasaRuralRequest;
import org.example.reservacasarurales.DTOs.Response.CasaRuralResponse;
import org.example.reservacasarurales.Entity.*;
import org.example.reservacasarurales.Mapper.CasaRuralMapper;
import org.example.reservacasarurales.Repository.CasaRuralRepository;
import org.example.reservacasarurales.Repository.DormitorioRepository;
import org.example.reservacasarurales.Repository.PropietarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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

        return casaRuralMapper.toResponse(saved);
    }

}