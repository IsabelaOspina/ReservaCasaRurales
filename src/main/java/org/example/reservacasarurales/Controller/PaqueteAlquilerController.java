package org.example.reservacasarurales.Controller;

import lombok.RequiredArgsConstructor;
import org.example.reservacasarurales.DTOs.Request.PaqueteAlquilerRequest;
import org.example.reservacasarurales.DTOs.Request.PaqueteAlquilerRequest;
import org.example.reservacasarurales.DTOs.Response.PaqueteAlquilerResponse;
import org.example.reservacasarurales.Service.PaqueteAlquilerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/paquetes")
@RequiredArgsConstructor
public class PaqueteAlquilerController {

    private final PaqueteAlquilerService paqueteService;

    // CREAR
    @PostMapping("/{codigoCasa}/crear")
    public ResponseEntity<PaqueteAlquilerResponse> crearPaquete(
            @PathVariable Long codigoCasa,
            @RequestBody PaqueteAlquilerRequest request) {

        return ResponseEntity.ok(
                paqueteService.crearPaquete(codigoCasa, request)
        );
    }

    // ACTUALIZAR
    @PutMapping("/{idPaquete}")
    public ResponseEntity<PaqueteAlquilerResponse> actualizarPaquete(
            @PathVariable Long idPaquete,
            @RequestBody PaqueteAlquilerRequest request) {

        return ResponseEntity.ok(
                paqueteService.actualizarPaquete(idPaquete, request)
        );
    }

    // LISTAR
    @GetMapping("/casa/{codigoCasa}")
    public ResponseEntity<List<PaqueteAlquilerResponse>> listarPaquetesPorCasa(
            @PathVariable Long codigoCasa) {

        return ResponseEntity.ok(
                paqueteService.listarPaquetesPorCasa(codigoCasa)
        );
    }

    @PostMapping("/{idPaquete}/dividir")
    public ResponseEntity<?> dividirPaquete(
            @PathVariable Long idPaquete,
            @RequestBody Map<String, LocalDate> fechas) {
        try {
            LocalDate nuevaFechaInicio = fechas.get("fechaInicio");
            LocalDate nuevaFechaFin = fechas.get("fechaFin");
            List<PaqueteAlquilerResponse> nuevosPaquetes = paqueteService.dividirPaquete(idPaquete, nuevaFechaInicio, nuevaFechaFin);
            return ResponseEntity.ok(nuevosPaquetes);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}