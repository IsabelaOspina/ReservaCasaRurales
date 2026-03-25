package org.example.reservacasarurales.Mapper;

import org.example.reservacasarurales.DTOs.Request.FotoRequest;
import org.example.reservacasarurales.DTOs.Response.FotoResponse;
import org.example.reservacasarurales.Entity.Foto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FotoMapper {
    Foto toEntity(FotoRequest request);
    FotoResponse toResponse(Foto foto);
    List<FotoResponse> toResponseList(List<Foto> fotos);
}
