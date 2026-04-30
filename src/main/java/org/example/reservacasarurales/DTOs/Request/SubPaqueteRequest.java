package org.example.reservacasarurales.DTOs.Request;

import java.time.LocalDate;

import org.example.reservacasarurales.Entity.TipoAlquiler;

import lombok.Data;

@Data
public class SubPaqueteRequest {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private double precio;
    private TipoAlquiler tipoAlquiler;
}
