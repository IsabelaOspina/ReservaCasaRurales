package org.example.reservacasarurales.DTOs.Request;

import java.util.List;

import lombok.Data;

@Data
public class DividirPaqueteRequest {
    private List<SubPaqueteRequest> subPaquetes;
}
