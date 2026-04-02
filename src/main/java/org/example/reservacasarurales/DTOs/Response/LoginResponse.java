package org.example.reservacasarurales.DTOs.Response;

import lombok.Data;

@Data
public class LoginResponse {
    private String token; // JWT

    public LoginResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
