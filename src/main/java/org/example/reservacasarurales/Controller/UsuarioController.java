package org.example.reservacasarurales.Controller;

import lombok.RequiredArgsConstructor;
import org.example.reservacasarurales.DTOs.Request.ClienteRequest;
import org.example.reservacasarurales.DTOs.Request.LoginRequest;
import org.example.reservacasarurales.DTOs.Request.PropietarioRequest;
import org.example.reservacasarurales.DTOs.Request.UsuarioRequest;
import org.example.reservacasarurales.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request){

        String token = usuarioService.login(
                request.getCorreoElectronico(),
                request.getPassword()
        );

        return ResponseEntity.ok("token:" + token);
    }


    // REGISTRAR CLIENTE
    @PostMapping("/registro-cliente")
    public ResponseEntity<String> registrarCliente(
            @RequestBody ClienteRequest clienteDTO){

        usuarioService.registrarCliente(clienteDTO);

        return ResponseEntity.ok("Cliente registrado correctamente");
    }


    // REGISTRAR PROPIETARIO
    @PostMapping("/registro-propietario")
    public ResponseEntity<String> registrarPropietario(
            @RequestBody PropietarioRequest propietarioDTO){

        usuarioService.registrarPropietario(propietarioDTO);

        return ResponseEntity.ok("Propietario registrado correctamente");
    }
}
