package org.example.reservacasarurales.Controller;


import org.example.reservacasarurales.DTOs.Request.DormitorioRequest;
import org.example.reservacasarurales.DTOs.Response.DormitorioResponse;
import org.example.reservacasarurales.Service.DormitorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/dormitorios")
public class DormitorioController {

    @Autowired
    private DormitorioService dormitorioService;

    @PostMapping("/registrar/{codigoCasa}")
    public ResponseEntity<?> registrarDormitorio(
            @PathVariable Long codigoCasa,
            @RequestBody DormitorioRequest request) {
        try {
            DormitorioResponse response = dormitorioService.registrarDormitorio(codigoCasa, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }
}
