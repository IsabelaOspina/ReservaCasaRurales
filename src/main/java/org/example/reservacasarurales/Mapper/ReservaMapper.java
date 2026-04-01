package org.example.reservacasarurales.Mapper;

import org.example.reservacasarurales.DTOs.Response.ReservaResponse;
import org.example.reservacasarurales.Entity.Reserva;

import java.util.List;
import java.util.stream.Collectors;

public class ReservaMapper {

    public ReservaResponse toResponse(Reserva reserva) {
        if (reserva == null) {
            return null;
        }

        String clienteNombre = reserva.getCliente() != null ?
                reserva.getCliente().getNombre() : null;

        String estadoNombre = reserva.getEstado() != null ?
                reserva.getEstado().name() : null;

        return new ReservaResponse(
                reserva.getId(),
                reserva.getFechaInicio(),
                reserva.getFechaFin(),
                clienteNombre,
                estadoNombre
        );
    }

    public List<ReservaResponse> toResponseList(List<Reserva> reservas) {
        if (reservas == null) {
            return null;
        }

        return reservas.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

}
