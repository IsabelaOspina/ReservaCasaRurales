package org.example.reservacasarurales.DTOs.Response;

import lombok.Data;
import org.example.reservacasarurales.Entity.TipoCama;

@Data
public class DormitorioResponse {
    private Long idDormitorio;
    private int numeroCamas;
    private String nombre;
    private TipoCama tipoCama;
    private boolean tieneBano;
    private Long codigoCasa;

}
