package org.example.reservacasarurales.Mapper;


import org.example.reservacasarurales.DTOs.Request.UsuarioRequest;
import org.example.reservacasarurales.DTOs.Response.UsuarioResponse;
import org.example.reservacasarurales.Entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "idUsuario", ignore = true)
    Usuario toEntity(UsuarioRequest request);

    UsuarioResponse toResponse(Usuario usuario);
}
