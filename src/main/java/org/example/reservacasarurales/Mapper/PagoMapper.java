package org.example.reservacasarurales.Mapper;

import java.time.LocalDate;

import org.example.reservacasarurales.DTOs.Request.PagoRequest;
import org.example.reservacasarurales.DTOs.Response.PagoResponse;
import org.example.reservacasarurales.Entity.Pago;
import org.example.reservacasarurales.Entity.Reserva;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public class PagoMapper {

    public Pago toEntity(PagoRequest request, Reserva reserva) {
        Pago p = new Pago();

        p.setMonto(request.getMonto());

        
        if (request.getFechaPago() != null) {
            p.setFechaPago(request.getFechaPago());
        } else {
            p.setFechaPago(LocalDate.now());
        }

        
        p.setMetodoPago(request.getMetodoPago());

        
        p.setConfirmado(request.isConfirmado());

        p.setReserva(reserva);

        return p;
    }

    public PagoResponse toResponse(Pago p) {
        PagoResponse res = new PagoResponse();

        res.setIdPago(p.getIdPago());
        res.setMonto(p.getMonto());
        res.setConfirmado(p.isConfirmado());

        
        res.setMetodoPago(p.getMetodoPago() != null ? p.getMetodoPago() : null);
        res.setFechaPago(p.getFechaPago() != null ? p.getFechaPago() : null);

        return res;
    }
}