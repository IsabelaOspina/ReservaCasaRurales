package org.example.reservacasarurales.Mapper;

import org.example.reservacasarurales.DTOs.Request.PaqueteAlquilerRequest;
import org.example.reservacasarurales.DTOs.Response.PaqueteAlquilerResponse;
import org.example.reservacasarurales.Entity.PaqueteAlquiler;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaqueteMapper {
    PaqueteAlquiler toEntity(PaqueteAlquilerRequest request);
    PaqueteAlquilerResponse toResponse(PaqueteAlquiler paquete);
}
