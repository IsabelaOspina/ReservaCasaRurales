package org.example.reservacasarurales.Mapper;

import org.example.reservacasarurales.DTOs.Request.PropietarioRequest;
import org.example.reservacasarurales.DTOs.Response.PropietarioResponse;
import org.example.reservacasarurales.Entity.Propietario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PropietarioMapper {

    @Mapping(target = "idPropietario", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "casas", ignore = true)
    Propietario toEntity(PropietarioRequest request);

    @Mapping(target = "idUsuario", source = "usuario.idUsuario")
    PropietarioResponse toResponse(Propietario propietario);
}