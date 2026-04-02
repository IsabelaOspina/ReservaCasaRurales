package org.example.reservacasarurales.DTOs.Request;

import lombok.Data;

@Data
public class RegistroRequest {
    private String nombre;
    private String usuario;
    private String password;
    private String correoElectronico;
    private String telefonoContacto;
    private String numeroCuenta;
}
