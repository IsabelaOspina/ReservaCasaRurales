package org.example.reservacasarurales.DTOs.Response;

import lombok.Data;

@Data
public class CocinaResponse {
    private Long idCocina;
    private boolean tieneLavavajillas;
    private boolean tieneLavadora;

}
