package org.example.reservacasarurales.Controller;

import org.example.reservacasarurales.DTOs.Request.CasaRuralRequest;
import org.example.reservacasarurales.DTOs.Response.CasaRuralResponse;
import org.example.reservacasarurales.Service.CasaRuralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/casa_rural")
public class CasaRuralController {

    @Autowired
    private CasaRuralService casaRuralService;

    //HU003
    @PostMapping(value = "/registrar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registrarCasa(@ModelAttribute CasaRuralRequest request) {

        try {

            CasaRuralResponse response = casaRuralService.registrarCasaRural(request);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));

        } catch (RuntimeException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @DeleteMapping("/{codigoCasa}")
    public ResponseEntity<?> eliminarCasa(
            @PathVariable Long codigoCasa){

        try{

            casaRuralService.eliminarCasa(codigoCasa);

            return ResponseEntity.ok(
                    Map.of(
                            "mensaje",
                            "Casa rural eliminada correctamente"
                    )
            );

        }catch(RuntimeException e){

            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(
                            Map.of(
                                    "error",
                                    e.getMessage()
                            )
                    );
        }
    }

    @GetMapping("/{codigoCasa}")
    public ResponseEntity<?> obtenerCasa(@PathVariable Long codigoCasa) {

        try {

            CasaRuralResponse response = casaRuralService.obtenerCasaPorId(codigoCasa);

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<CasaRuralResponse>> listarCasas() {

        List<CasaRuralResponse> casas = casaRuralService.listarCasas();

        return ResponseEntity.ok(casas);
    }



}
