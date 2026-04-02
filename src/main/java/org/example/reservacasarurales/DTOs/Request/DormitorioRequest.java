package org.example.reservacasarurales.DTOs.Request;

import lombok.Data;
import org.example.reservacasarurales.Entity.TipoCama;

@Data
public class DormitorioRequest {
    private int numeroCamas;
    private TipoCama tipoCama;
    private boolean tieneBano;
}
