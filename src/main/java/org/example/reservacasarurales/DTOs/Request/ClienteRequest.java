package org.example.reservacasarurales.DTOs.Request;

public class ClienteRequest {
    private String nombre;
    private String email;
    private String password;
    private String telefono;

    // Constructor vacío
    public ClienteRequest() {}

    // Constructor con parámetros
    public ClienteRequest(String nombre, String email, String password, String telefono) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.telefono = telefono;
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

}
