package org.example.reservacasarurales.DTOs.Response;

import lombok.Data;
import org.example.reservacasarurales.Entity.TipoAlquiler;

import java.time.LocalDate;

@Data
public class PaqueteAlquilerResponse {
    private Long idPaquete;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private double precio;
    private TipoAlquiler tipoAlquiler;
}
