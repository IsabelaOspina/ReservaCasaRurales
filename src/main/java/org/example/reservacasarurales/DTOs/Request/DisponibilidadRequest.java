package org.example.reservacasarurales.DTOs.Request;

import java.time.LocalDate;

import lombok.Data;

@Data
public class DisponibilidadRequest {

    private Long casaId;
    private Long paqueteId;
    private LocalDate fechaInicio;
    private int noches;
}
