package org.example.reservacasarurales.Mapper;

import org.example.reservacasarurales.DTOs.Request.PagoRequest;
import org.example.reservacasarurales.DTOs.Response.PagoResponse;
import org.example.reservacasarurales.Entity.Pago;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PagoMapper {
    Pago toEntity(PagoRequest request);
    PagoResponse toResponse(Pago pago);

}
