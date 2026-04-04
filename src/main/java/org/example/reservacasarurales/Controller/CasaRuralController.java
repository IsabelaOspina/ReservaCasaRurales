package org.example.reservacasarurales.Controller;

import org.example.reservacasarurales.DTOs.Request.CasaRuralRequest;
import org.example.reservacasarurales.DTOs.Response.CasaRuralResponse;
import org.example.reservacasarurales.DTOs.Response.FotoResponse;
import org.example.reservacasarurales.Service.CasaRuralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/casa_rural")
public class CasaRuralController {

    @Autowired
    private CasaRuralService casaRuralService;

    //HU003
    @PostMapping("/registrar/{propietarioId}")
    public ResponseEntity<?> registrarCasa(
            @PathVariable Long propietarioId,
            @RequestBody CasaRuralRequest request) {
        try {
            CasaRuralResponse response = casaRuralService.registrarCasaRural(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            // Error de validación → 400
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            // Propietario no encontrado → 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }

    }

    @DeleteMapping("/{codigoCasa}")
    public ResponseEntity<?> eliminarCasa(@PathVariable Long codigoCasa) {
        try {
            casaRuralService.eliminarCasa(codigoCasa);
            return ResponseEntity.ok(Map.of("mensaje", "Casa eliminada correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // HU14 - Listar todas las casas
    @GetMapping
    public ResponseEntity<List<CasaRuralResponse>> listarTodas() {
        return ResponseEntity.ok(casaRuralService.listarTodas());
    }

    // HU18 - Buscar casas por población
    @GetMapping("/poblacion/{poblacion}")
    public ResponseEntity<List<CasaRuralResponse>> listarPorPoblacion(@PathVariable String poblacion) {
        return ResponseEntity.ok(casaRuralService.listarPorPoblacion(poblacion));
    }

    // HU22 - Listar fotos de una casa (galería)
    @GetMapping("/{codigoCasa}/fotos")
    public ResponseEntity<List<FotoResponse>> listarFotos(@PathVariable Long codigoCasa) {
        return ResponseEntity.ok(casaRuralService.listarFotos(codigoCasa));
    }


}
