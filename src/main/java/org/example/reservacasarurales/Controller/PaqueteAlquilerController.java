package org.example.reservacasarurales.Controller;

import lombok.RequiredArgsConstructor;
import org.example.reservacasarurales.DTOs.Request.DividirPaqueteRequest;
import org.example.reservacasarurales.DTOs.Request.PaqueteAlquilerRequest;
import org.example.reservacasarurales.DTOs.Response.OcupacionPaqueteResponse;
import org.example.reservacasarurales.DTOs.Response.PaqueteAlquilerResponse;
import org.example.reservacasarurales.Repository.ReservaRepository;
import org.example.reservacasarurales.Service.PaqueteAlquilerService;
import org.example.reservacasarurales.Service.ReservaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/paquetes")
@RequiredArgsConstructor
public class PaqueteAlquilerController {

    private final PaqueteAlquilerService paqueteService;
    private final ReservaRepository reservaRepository;

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

    // CALENDARIO DE OCUPACIÓN
    @GetMapping("/casa/{codigoCasa}/ocupacion")
    public ResponseEntity<List<OcupacionPaqueteResponse>> obtenerOcupacion(
            @PathVariable Long codigoCasa) {

        return ResponseEntity.ok(
                paqueteService.obtenerOcupacionPorCasa(codigoCasa)
        );
    }

    // DIVIDIR PAQUETE
    @PostMapping("/{idPaquete}/dividir")
    @PreAuthorize("hasRole('PROPIETARIO')")
    public ResponseEntity<List<PaqueteAlquilerResponse>> dividirPaquete(
            @PathVariable Long idPaquete,
            @RequestBody DividirPaqueteRequest request) {

        return ResponseEntity.ok(
                paqueteService.dividirPaquete(idPaquete, request)
        );
    }

    @GetMapping("/{idPaquete}/casa-entera-disponible")
    public ResponseEntity<Boolean> casaEnteraDisponible(@PathVariable Long idPaquete) {
        boolean tieneHabitaciones = reservaRepository.existsByPaqueteIdAndDormitoriosIsNotNull(idPaquete);
        return ResponseEntity.ok(!tieneHabitaciones);
    }
}