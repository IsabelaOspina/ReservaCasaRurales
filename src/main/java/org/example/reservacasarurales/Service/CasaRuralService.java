package org.example.reservacasarurales.Service;

import org.example.reservacasarurales.DTOs.Request.CasaRuralRequest;
import org.example.reservacasarurales.DTOs.Response.CasaRuralResponse;
import org.example.reservacasarurales.Entity.CasaRural;
import org.example.reservacasarurales.Entity.Foto;
import org.example.reservacasarurales.Entity.Propietario;
import org.example.reservacasarurales.Entity.TipoCama;
import org.example.reservacasarurales.Entity.Dormitorio;
import org.example.reservacasarurales.Mapper.CasaRuralMapper;
import org.example.reservacasarurales.Repository.CasaRuralRepository;
import org.example.reservacasarurales.Repository.DormitorioRepository;
import org.example.reservacasarurales.Repository.PropietarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public CasaRuralResponse registrarCasaRural(CasaRuralRequest casaRuralRequest, Long idPropietario) {
        Propietario propietario = propietarioRepository.findByIdPropietario(idPropietario)
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

            dormitorioRepository.save(dormitorio);
        }

        return casaRuralMapper.toResponse(saved);
    }

}
