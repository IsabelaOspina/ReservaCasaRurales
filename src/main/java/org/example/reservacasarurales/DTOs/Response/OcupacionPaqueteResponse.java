package org.example.reservacasarurales.DTOs.Response;

import java.time.LocalDate;
import java.util.List;

import org.example.reservacasarurales.Entity.TipoAlquiler;

import lombok.Data;

@Data
public class OcupacionPaqueteResponse {
    private Long idPaquete;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private double precio;
    private TipoAlquiler tipoAlquiler;
    private List<PeriodoOcupadoResponse> periodosOcupados;
}
