package org.example.reservacasarurales.Repository;


import org.example.reservacasarurales.Entity.Cliente;
import org.example.reservacasarurales.Entity.Propietario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PropietarioRepository extends JpaRepository<Propietario, Long> {
    Optional<Propietario> findByIdPropietario(Long idPropietario);

    Optional<Propietario> findByUsuarioCorreoElectronico(String correoElectronico);

    List<Cliente> findByNombreContainingIgnoreCase(String nombre);
}
