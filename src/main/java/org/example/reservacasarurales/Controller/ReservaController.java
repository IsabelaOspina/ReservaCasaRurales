package org.example.reservacasarurales.Controller;

import org.example.reservacasarurales.DTOs.Request.DisponibilidadRequest;
import org.example.reservacasarurales.DTOs.Request.ReservaRequest;
import org.example.reservacasarurales.DTOs.Response.DisponibilidadResponse;
import org.example.reservacasarurales.DTOs.Response.NotificacionResponse;
import org.example.reservacasarurales.DTOs.Response.ReservaResponse;
import org.example.reservacasarurales.Service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ReservaResponse> crear(@RequestBody ReservaRequest request) {
        return ResponseEntity.ok(reservaService.crearReserva(request));
    }

    @PostMapping("/disponibilidad")
    public ResponseEntity<DisponibilidadResponse> verificarDisponibilidad(
            @RequestBody DisponibilidadRequest request) {

        return ResponseEntity.ok(reservaService.verificarDisponibilidad(request));
    }

    @GetMapping("/pendientes")
    @PreAuthorize("hasRole('PROPIETARIO')")
    public ResponseEntity<List<ReservaResponse>> obtenerPendientes() {
        return ResponseEntity.ok(reservaService.obtenerReservasPendientes());
    }

    @PutMapping("/{id}/cancelar")
    @PreAuthorize("hasRole('PROPIETARIO')")
    public ResponseEntity<ReservaResponse> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.cancelarReserva(id));
    }

    @GetMapping("/mis-reservas")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<ReservaResponse>> obtenerMisReservas() {
        return ResponseEntity.ok(reservaService.obtenerMisReservas());
    }

    @GetMapping("/notificaciones")
    @PreAuthorize("hasRole('PROPIETARIO')")
    public ResponseEntity<List<NotificacionResponse>> obtenerNotificaciones() {
        return ResponseEntity.ok(reservaService.obtenerNotificaciones());
    }
}
