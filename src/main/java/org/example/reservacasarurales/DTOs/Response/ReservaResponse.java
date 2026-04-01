package org.example.reservacasarurales.DTOs.Response;

import java.time.LocalDate;

public class ReservaResponse {
    private Long id;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String clienteNombre;
    private String estado;

    // Constructor
    public ReservaResponse(Long id, LocalDate fechaInicio, LocalDate fechaFin, String clienteNombre, String estado) {
        this.id = id;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.clienteNombre = clienteNombre;
        this.estado = estado;
    }

    // Getters
    public Long getId() { return id; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public String getClienteNombre() { return clienteNombre; }
    public String getEstado() { return estado; }
}
