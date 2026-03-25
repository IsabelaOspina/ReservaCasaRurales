package org.example.reservacasarurales.DTOs.Request;

import lombok.Data;
import org.example.reservacasarurales.Entity.MetodoPago;

import java.time.LocalDate;

@Data
public class PagoRequest {
    private double monto;
    private MetodoPago metodoPago;
    private LocalDate fechaPago;
    private boolean confirmado;
}
