package org.example.reservacasarurales.DTOs.Response;

import lombok.Data;

@Data
public class UsuarioResponse {
    private Long idUsuario;
    private String username;
    private String correoElectronico;
    private String rol;
}
