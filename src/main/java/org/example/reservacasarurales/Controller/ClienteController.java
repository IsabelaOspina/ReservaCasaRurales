package org.example.reservacasarurales.Controller;

import org.example.reservacasarurales.DTOs.Request.ClienteRequest;
import org.example.reservacasarurales.DTOs.Response.ClienteResponse;
import org.example.reservacasarurales.Entity.Cliente;
import org.example.reservacasarurales.Mapper.ClienteMapper;
import org.example.reservacasarurales.Service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteMapper clienteMapper;

    @PostMapping
    public ResponseEntity<ClienteResponse> crear(@RequestBody ClienteRequest request) {
        Cliente nuevoCliente = clienteService.guardar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteMapper.toResponse(nuevoCliente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> editar(@PathVariable Long id, @RequestBody ClienteRequest request) {
        Cliente clienteActualizado = clienteService.editar(id, request);
        return ResponseEntity.ok(clienteMapper.toResponse(clienteActualizado));
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listar() {
        List<Cliente> clientes = clienteService.listar();
        return ResponseEntity.ok(clienteMapper.toResponseList(clientes));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> buscarPorId(@PathVariable Long id) {
        Cliente cliente = clienteService.buscarPorId(id);
        return ResponseEntity.ok(clienteMapper.toResponse(cliente));
    }
}

