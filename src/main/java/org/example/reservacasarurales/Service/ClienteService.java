package org.example.reservacasarurales.Service;


import org.example.reservacasarurales.DTOs.Request.ClienteRequest;
import org.example.reservacasarurales.Entity.Cliente;
import org.example.reservacasarurales.Exception.DuplicateResourceException;
import org.example.reservacasarurales.Exception.ResourceNotFoundException;
import org.example.reservacasarurales.Repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.List;

@Service
public class ClienteService {


    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public Cliente guardar(ClienteRequest request) {
        if (clienteRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Ya existe un cliente con el email: " + request.getEmail());
        }

        Cliente cliente = new Cliente();
        cliente.setNombre(request.getNombre());
        cliente.setEmail(request.getEmail());
        cliente.setTelefono(request.getTelefono());

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            cliente.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (cliente.getTelefono() != null && cliente.getTelefono().trim().isEmpty()) {
            cliente.setTelefono(null);
        }

        return clienteRepository.save(cliente);
    }

    @Transactional
    public Cliente editar(Long id, ClienteRequest request) {
        Cliente existente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));

        if (!existente.getEmail().equals(request.getEmail()) &&
                clienteRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Ya existe un cliente con el email: " + request.getEmail());
        }

        existente.setNombre(request.getNombre());
        existente.setEmail(request.getEmail());
        existente.setTelefono(request.getTelefono());

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            existente.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return clienteRepository.save(existente);
    }

    @Transactional(readOnly = true)
    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));
    }

    @Transactional(readOnly = true)
    public Cliente buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con email: " + email));
    }

    @Transactional(readOnly = true)
    public List<Cliente> buscarPorNombre(String nombre) {
        return clienteRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @Transactional
    public void eliminar(Long id) {
        Cliente cliente = buscarPorId(id);
        clienteRepository.delete(cliente);
    }
}
