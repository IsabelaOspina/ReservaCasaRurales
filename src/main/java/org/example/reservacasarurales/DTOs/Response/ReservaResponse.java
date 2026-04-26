package org.example.reservacasarurales.DTOs.Response;

import java.time.LocalDate;


import lombok.Data;

@Data
public class ReservaResponse {

    private Long id;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int noches;
    private String estado;

    private LocalDate fechaLimitePago;
    private LocalDate fechaCreacion;

    private Long casaId;
    private Long paqueteId;

}
