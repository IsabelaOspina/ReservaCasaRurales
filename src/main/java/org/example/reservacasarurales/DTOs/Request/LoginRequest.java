package org.example.reservacasarurales.DTOs.Request;

import lombok.Data;

@Data
public class LoginRequest {
    private String correo_electronico;
    private String contraseña;

}
