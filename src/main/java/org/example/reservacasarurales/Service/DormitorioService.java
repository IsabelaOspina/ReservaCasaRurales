package org.example.reservacasarurales.Service;

import org.example.reservacasarurales.DTOs.Request.DormitorioRequest;
import org.example.reservacasarurales.DTOs.Response.DormitorioResponse;
import org.example.reservacasarurales.Entity.CasaRural;
import org.example.reservacasarurales.Entity.Dormitorio;
import org.example.reservacasarurales.Exception.MaxDormitoriosException;
import org.example.reservacasarurales.Mapper.DormitorioMapper;
import org.example.reservacasarurales.Repository.CasaRuralRepository;
import org.example.reservacasarurales.Repository.DormitorioRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    //HU009
    public DormitorioResponse registrarDormitorio(Long codigoCasa, DormitorioRequest request) {
        CasaRural casa = casaRuralRepository.findByCodigoCasa(codigoCasa)
                .orElseThrow(() -> new RuntimeException("Casa rural no encontrada"));

        int dormitoriosActuales = casa.getDormitorios().size();
        if (dormitoriosActuales >= casa.getNumeroDormitorios()) {
            throw new MaxDormitoriosException(casa.getNumeroDormitorios());
        }

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
