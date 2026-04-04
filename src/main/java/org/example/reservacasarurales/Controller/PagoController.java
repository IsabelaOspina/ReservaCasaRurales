package org.example.reservacasarurales.Controller;

import java.util.List;

import org.example.reservacasarurales.DTOs.Request.PagoRequest;
import org.example.reservacasarurales.DTOs.Response.PagoInfoResponse;
import org.example.reservacasarurales.DTOs.Response.PagoResponse;
import org.example.reservacasarurales.Service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @PostMapping
    public ResponseEntity<PagoResponse> registrar(@RequestBody PagoRequest request) {
        return ResponseEntity.ok(pagoService.registrarPago(request));
    }

    @GetMapping("/info/{reservaId}")
    public ResponseEntity<PagoInfoResponse> obtenerInfo(@PathVariable Long reservaId) {
        return ResponseEntity.ok(pagoService.obtenerInfoPago(reservaId));
    }

    @GetMapping("/{reservaId}")
    public ResponseEntity<List<PagoResponse>> obtenerPagos(@PathVariable Long reservaId) {
        return ResponseEntity.ok(pagoService.obtenerPagosPorReserva(reservaId));
    }
}
