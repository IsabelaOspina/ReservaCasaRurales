package org.example.reservacasarurales.DTOs.Response;

import lombok.Data;

@Data
public class RegistroResponse {
    private Long idPropietario;
    private String nombre;
    private String usuario;
    private String mensaje; // "Registro exitoso"
}
