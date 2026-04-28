package org.example.reservacasarurales.DTOs.Response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class NotificacionResponse {
    private String mensaje;
    private String telefono;
    private LocalDate fechaLimitePago;
}
