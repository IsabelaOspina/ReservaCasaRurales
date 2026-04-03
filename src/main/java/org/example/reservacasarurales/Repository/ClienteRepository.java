package org.example.reservacasarurales.Repository;

import org.example.reservacasarurales.Entity.Cliente;
import org.example.reservacasarurales.Entity.Propietario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByIdCliente(Long idCliente);
    List<Cliente> findByNombreContainingIgnoreCase(String nombre);
    Optional<Cliente> findByUsuarioCorreoElectronico(String correoElectronico);

}
