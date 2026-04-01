package org.example.reservacasarurales.DTOs.Response;

public class ClienteResponse {
    private Long id;
    private String nombre;
    private String email;

    // Constructor
    public ClienteResponse(Long id, String nombre, String email) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
    }

    // Getters (solo lectura, no setters)
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
}
