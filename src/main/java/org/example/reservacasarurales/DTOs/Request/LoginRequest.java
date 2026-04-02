package org.example.reservacasarurales.DTOs.Request;

import lombok.Data;

@Data
public class LoginRequest {
    private String correoElectronico;
    private String password;

}
