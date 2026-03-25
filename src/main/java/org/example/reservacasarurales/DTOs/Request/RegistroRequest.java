package org.example.reservacasarurales.DTOs.Request;

import lombok.Data;

@Data
public class RegistroRequest {
    private String nombre;
    private String usuario;
    private String contraseña;
    private String correo_electronico;
    private String telefonoContacto;
    private String numeroCuenta;
}
