package org.example.reservacasarurales.Mapper;

import org.example.reservacasarurales.DTOs.Request.CasaRuralRequest;
import org.example.reservacasarurales.DTOs.Response.CasaRuralResponse;
import org.example.reservacasarurales.Entity.CasaRural;
import org.example.reservacasarurales.Entity.Foto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {FotoMapper.class})
public interface CasaRuralMapper {

    @Mapping(target = "fotos", source = "fotos") // usa FotoMapper
    CasaRural toEntity(CasaRuralRequest request);

    CasaRuralResponse toResponse(CasaRural casa);
}

