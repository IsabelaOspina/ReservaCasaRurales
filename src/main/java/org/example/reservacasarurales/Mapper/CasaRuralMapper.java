package org.example.reservacasarurales.Mapper;

import org.example.reservacasarurales.DTOs.Request.CasaRuralRequest;
import org.example.reservacasarurales.DTOs.Response.CasaRuralResponse;
import org.example.reservacasarurales.Entity.CasaRural;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {FotoMapper.class})
public interface CasaRuralMapper {

    CasaRuralResponse toResponse(CasaRural casa);

    CasaRural toEntity(CasaRuralRequest request);
}


