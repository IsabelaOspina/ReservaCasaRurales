package org.example.reservacasarurales.Mapper;

import org.example.reservacasarurales.DTOs.Request.CocinaRequest;
import org.example.reservacasarurales.DTOs.Response.CocinaResponse;
import org.example.reservacasarurales.Entity.Cocina;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CocinaMapper {
    @Mapping(target = "casaRural", ignore = true)
    Cocina toEntity(CocinaRequest request);

    @Mapping(target = "codigoCasa", source = "casaRural.codigoCasa")
    CocinaResponse toResponse(Cocina cocina);

}

