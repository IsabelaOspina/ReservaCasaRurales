package org.example.reservacasarurales.DTOs.Response;

import lombok.Data;

@Data
public class DisponibilidadResponse {

    private boolean disponible;
    private String mensaje;

    public DisponibilidadResponse(boolean disponible, String mensaje) {
        this.disponible = disponible;
        this.mensaje = mensaje;
    }
}