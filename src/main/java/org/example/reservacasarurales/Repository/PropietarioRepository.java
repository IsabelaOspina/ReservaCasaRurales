package org.example.reservacasarurales.Repository;


import org.example.reservacasarurales.Entity.Propietario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PropietarioRepository extends JpaRepository<Propietario, Long> {
    Optional<Propietario> findByCorreoElectronico(String correoElectronico);
}
