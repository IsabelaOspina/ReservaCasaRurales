package org.example.reservacasarurales.DTOs.Response;

import java.time.LocalDate;

import lombok.Data;

@Data
public class PeriodoOcupadoResponse {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estado;
}
