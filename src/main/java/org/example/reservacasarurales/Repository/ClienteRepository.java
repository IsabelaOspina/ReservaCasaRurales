package org.example.reservacasarurales.Repository;

import org.example.reservacasarurales.Entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Buscar cliente por email (útil para validaciones)
    Optional<Cliente> findByEmail(String email);

    // Verificar si existe un cliente con ese email
    boolean existsByEmail(String email);

    // Buscar clientes por nombre (ignorando mayúsculas/minúsculas)
    List<Cliente> findByNombreContainingIgnoreCase(String nombre);

    // Buscar clientes que tengan teléfono (no nulo y no vacío)
    @Query("SELECT c FROM Cliente c WHERE c.telefono IS NOT NULL AND c.telefono != ''")
    List<Cliente> findClientesWithTelefono();

    // Buscar cliente por email y teléfono
    Optional<Cliente> findByEmailAndTelefono(String email, String telefono);

    // Contar clientes por email (para validación de duplicados)
    long countByEmail(String email);
}
