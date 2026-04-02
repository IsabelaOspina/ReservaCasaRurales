package org.example.reservacasarurales.Controller;

import org.example.reservacasarurales.DTOs.Request.CocinaRequest;
import org.example.reservacasarurales.DTOs.Response.CocinaResponse;
import org.example.reservacasarurales.Service.CocinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/{codigoCasa}/cocinas")
public class CocinaController {

    @Autowired
    private  CocinaService cocinaService;

    @PostMapping("/registrar")
    public ResponseEntity<CocinaResponse> registrarCocina(
            @PathVariable Long codigoCasa,
            @RequestBody CocinaRequest request) {
        return ResponseEntity.ok(cocinaService.registrarCocina(codigoCasa, request));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<CocinaResponse>> listarCocinas(@PathVariable Long codigoCasa) {
        return ResponseEntity.ok(cocinaService.listarCocinas(codigoCasa));
    }
}