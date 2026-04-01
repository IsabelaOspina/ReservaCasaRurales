package org.example.reservacasarurales.Mapper;

import org.example.reservacasarurales.DTOs.Response.ClienteResponse;
import org.example.reservacasarurales.Entity.Cliente;
import org.hibernate.annotations.CollectionTypeRegistration;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClienteMapper {
    public ClienteResponse toResponse(Cliente cliente) {
        if (cliente == null) {
            return null;
        }

        return new ClienteResponse(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getEmail()
        );
    }

    public List<ClienteResponse> toResponseList(List<Cliente> clientes) {
        if (clientes == null) {
            return null;
        }

        return clientes.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

}
