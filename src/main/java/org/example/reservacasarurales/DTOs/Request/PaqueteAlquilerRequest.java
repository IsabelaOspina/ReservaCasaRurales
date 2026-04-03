package org.example.reservacasarurales.DTOs.Request;

import lombok.Data;
import org.example.reservacasarurales.Entity.TipoAlquiler;

import java.time.LocalDate;

@Data
public class PaqueteAlquilerRequest {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Double precio;
    private TipoAlquiler tipoAlquiler;
}
