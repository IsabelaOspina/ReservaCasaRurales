package org.example.reservacasarurales.Mapper;

import org.example.reservacasarurales.DTOs.Request.CasaRuralRequest;
import org.example.reservacasarurales.DTOs.Response.CasaRuralResponse;
import org.example.reservacasarurales.Entity.CasaRural;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {FotoMapper.class})
public interface CasaRuralMapper {

    CasaRuralResponse toResponse(CasaRural casa);

    @Mapping(target = "fotos", ignore = true)
    CasaRural toEntity(CasaRuralRequest request);
}