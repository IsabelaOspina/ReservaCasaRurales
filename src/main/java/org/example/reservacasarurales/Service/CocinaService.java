package org.example.reservacasarurales.Service;

import org.example.reservacasarurales.DTOs.Request.CocinaRequest;
import org.example.reservacasarurales.DTOs.Response.CocinaResponse;
import org.example.reservacasarurales.Entity.CasaRural;
import org.example.reservacasarurales.Entity.Cocina;
import org.example.reservacasarurales.Exception.MaxCocinasException;
import org.example.reservacasarurales.Mapper.CocinaMapper;
import org.example.reservacasarurales.Repository.CasaRuralRepository;
import org.example.reservacasarurales.Repository.CocinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    //HU010
    public CocinaResponse registrarCocina(Long codigoCasa, CocinaRequest request) {
        CasaRural casa = casaRuralRepository.findByCodigoCasa(codigoCasa)
                .orElseThrow(() -> new RuntimeException("Casa rural no encontrada"));

        // Validar cantidad de cocinas
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
