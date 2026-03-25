package org.example.reservacasarurales.DTOs.Response;

import lombok.Data;

@Data
public class LoginResponse {
    private String token; // JWT
    private String usuario;
    private String mensaje; // "Login correcto"

}
