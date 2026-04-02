package org.example.reservacasarurales.DTOs.Response;

import lombok.Data;

import java.util.List;

@Data
public class CasaRuralResponse {
    private Long codigoCasa;
    private String poblacion;
    private String descripcion;
    private int numeroDormitorios;
    private int numeroBanos;
    private int numeroCocinas;
    private int numeroComedores;
    private int plazasGaraje;
    private List<FotoResponse> fotos;
}
