package org.example.reservacasarurales.DTOs.Request;

import lombok.Data;

@Data
public class UsuarioRequest {
    private String username;
    private String password;
    private String correoElectronico;
}
