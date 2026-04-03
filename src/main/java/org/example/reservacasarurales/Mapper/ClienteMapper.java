package org.example.reservacasarurales.Mapper;

import org.example.reservacasarurales.DTOs.Request.ClienteRequest;
import org.example.reservacasarurales.DTOs.Response.ClienteResponse;
import org.example.reservacasarurales.Entity.Cliente;
import org.hibernate.annotations.CollectionTypeRegistration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    @Mapping(target = "idCliente", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "reservas", ignore = true)
    Cliente toEntity(ClienteRequest request);

    @Mapping(target = "idUsuario", source = "usuario.idUsuario")
    ClienteResponse toResponse(Cliente cliente);
}