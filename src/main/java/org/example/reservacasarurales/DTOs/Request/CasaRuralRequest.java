package org.example.reservacasarurales.DTOs.Request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class CasaRuralRequest {
    private String poblacion;
    private String descripcion;
    private int numeroDormitorios;
    private int numeroBanos;
    private int numeroCocinas;
    private int numeroComedores;
    private int plazasGaraje;
    private List<MultipartFile> fotos;
    private List<String> descripcionesFotos;


}
