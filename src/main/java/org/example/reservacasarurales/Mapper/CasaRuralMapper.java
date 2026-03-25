package org.example.reservacasarurales.Mapper;

import org.example.reservacasarurales.DTOs.Request.CasaRuralRequest;
import org.example.reservacasarurales.DTOs.Response.CasaRuralResponse;
import org.example.reservacasarurales.Entity.CasaRural;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CasaRuralMapper {
    CasaRural toEntity(CasaRuralRequest request);
    CasaRuralResponse toResponse(CasaRural casaRural);
}
