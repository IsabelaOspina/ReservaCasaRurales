package org.example.reservacasarurales.Mapper;

import org.example.reservacasarurales.DTOs.Request.FotoRequest;
import org.example.reservacasarurales.DTOs.Response.FotoResponse;
import org.example.reservacasarurales.Entity.Foto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface FotoMapper {

    // Mapea Foto a FotoResponse
    FotoResponse toResponse(Foto foto);

    // Mapea FotoRequest a Foto
    Foto toEntity(FotoRequest request);
}