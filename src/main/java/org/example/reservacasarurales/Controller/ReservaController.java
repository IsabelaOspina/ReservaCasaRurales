package org.example.reservacasarurales.Controller;

import org.example.reservacasarurales.DTOs.Request.DisponibilidadRequest;
import org.example.reservacasarurales.DTOs.Request.ReservaRequest;
import org.example.reservacasarurales.DTOs.Response.DisponibilidadResponse;
import org.example.reservacasarurales.DTOs.Response.ReservaResponse;
import org.example.reservacasarurales.Service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @PostMapping
    public ResponseEntity<ReservaResponse> crear(@RequestBody ReservaRequest request) {
        return ResponseEntity.ok(reservaService.crearReserva(request));
    }

    @PostMapping("/disponibilidad")
    public ResponseEntity<DisponibilidadResponse> verificarDisponibilidad(
            @RequestBody DisponibilidadRequest request) {

        return ResponseEntity.ok(reservaService.verificarDisponibilidad(request));
    }

    // - Listar reservas pendientes con días transcurridos
    @GetMapping("/pendientes")
    public ResponseEntity<?> listarReservasPendientes() {
        return ResponseEntity.ok(reservaService.listarReservasPendientes());
    }

    // HU23 - Listar reservas expiradas (plazo 3 días vencido)
    @GetMapping("/expiradas")
    public ResponseEntity<List<ReservaResponse>> listarReservasExpiradas() {
        return ResponseEntity.ok(reservaService.listarReservasExpiradas());
    }

    // HU20/HU28 - Calendario de disponibilidad
    @GetMapping("/calendario/{casaId}")
    public ResponseEntity<Map<LocalDate, String>> obtenerCalendario(
            @PathVariable Long casaId,
            @RequestParam LocalDate desde,
            @RequestParam LocalDate hasta) {
        return ResponseEntity.ok(reservaService.obtenerCalendario(casaId, desde, hasta));
    }
}
