package org.example.reservacasarurales.Mapper;

import org.example.reservacasarurales.DTOs.Request.RegistroRequest;
import org.example.reservacasarurales.DTOs.Response.RegistroResponse;
import org.example.reservacasarurales.Entity.Propietario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    Propietario toEntity(RegistroRequest request);
    RegistroResponse toResponse(Propietario propietario);
}
