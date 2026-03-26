package org.example.reservacasarurales.Repository;

import org.example.reservacasarurales.Entity.Dormitorio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DormitorioRepository extends JpaRepository<Dormitorio, Long> {
    // Buscar habitaciones por el código de la casa rural
    List<Dormitorio> findByCasaRuralCodigoCasa(Long codigoCasa);

    // Buscar una habitación específica por su código dentro de la casa
    Optional<Dormitorio> findByIdDormitorioAfterAndCasaRuralCodigoCasa(Long idDormitorio, Long codigoCasa);
}

