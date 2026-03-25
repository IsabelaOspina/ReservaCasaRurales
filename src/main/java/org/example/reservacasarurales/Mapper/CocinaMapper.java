package org.example.reservacasarurales.Mapper;

import org.example.reservacasarurales.DTOs.Request.CocinaRequest;
import org.example.reservacasarurales.DTOs.Response.CocinaResponse;
import org.example.reservacasarurales.Entity.Cocina;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CocinaMapper {
    Cocina toEntity(CocinaRequest request);
    CocinaResponse toResponse(Cocina cocina);
}

