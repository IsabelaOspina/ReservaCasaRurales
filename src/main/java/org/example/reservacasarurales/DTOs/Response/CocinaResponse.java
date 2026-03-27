package org.example.reservacasarurales.DTOs.Response;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
public class CocinaResponse {
    private Long idCocina;
    private boolean tieneLavavajillas;
    private boolean tieneLavadora;
    private Long codigoCasa;


}
