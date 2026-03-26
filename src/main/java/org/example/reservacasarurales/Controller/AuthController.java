package org.example.reservacasarurales.Controller;

import org.example.reservacasarurales.DTOs.Request.LoginRequest;
import org.example.reservacasarurales.DTOs.Request.RegistroRequest;
import org.example.reservacasarurales.DTOs.Response.LoginResponse;
import org.example.reservacasarurales.DTOs.Response.RegistroResponse;
import org.example.reservacasarurales.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // HU001
    @PostMapping("/registro")
    public ResponseEntity<RegistroResponse> registrar(@RequestBody RegistroRequest request) {
        RegistroResponse response = authService.registrarPropietario(request);
        return ResponseEntity.ok(response);
    }

    // HU002 - Login
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
    
}