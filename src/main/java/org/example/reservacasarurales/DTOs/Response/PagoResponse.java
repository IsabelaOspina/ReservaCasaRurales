package org.example.reservacasarurales.DTOs.Response;

import lombok.Data;
import org.example.reservacasarurales.Entity.MetodoPago;

import java.time.LocalDate;

@Data
public class PagoResponse {
    private Long idPago;
    private double monto;
    private MetodoPago metodoPago;
    private LocalDate fechaPago;
    private boolean confirmado;
}
