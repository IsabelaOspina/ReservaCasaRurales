package org.example.reservacasarurales.DTOs.Request;

import lombok.Data;

@Data
public class PropietarioRequest {
    private String username;
    private String password;
    private String nombre;
    private String correoElectronico;
    private String telefonoContacto;
    private String numeroCuenta;
    private String banco;

}
