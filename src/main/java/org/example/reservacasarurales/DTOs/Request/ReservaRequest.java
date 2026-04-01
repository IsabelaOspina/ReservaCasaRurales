package org.example.reservacasarurales.DTOs.Request;

import java.time.LocalDate;

public class ReservaRequest {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Long clienteId;  // Se envía el ID del cliente, no el objeto completo
    private String estado;   // O podrías usar el enum EstadoReserva

    // Constructor vacío
    public ReservaRequest() {}

    // Constructor con parámetros
    public ReservaRequest(LocalDate fechaInicio, LocalDate fechaFin, Long clienteId, String estado) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.clienteId = clienteId;
        this.estado = estado;
    }

    // Getters y Setters
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
