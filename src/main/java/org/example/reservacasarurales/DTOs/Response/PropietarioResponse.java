package org.example.reservacasarurales.DTOs.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PropietarioResponse {
    private Long idPropietario;
    private String nombre;
    private String correoElectronico;
    private String telefonoContacto;
    private String numeroCuenta;
    private String banco;
    private Long idUsuario;
}
