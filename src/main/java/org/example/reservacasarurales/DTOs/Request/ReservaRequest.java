package org.example.reservacasarurales.DTOs.Request;

import java.time.LocalDate;

import java.util.List;

import lombok.Data;

@Data
public class ReservaRequest {
    private LocalDate fechaInicio;
    private int noches;
    private Long casaId;
    private Long paqueteId;
    private List<Long> dormitoriosIds;
}

