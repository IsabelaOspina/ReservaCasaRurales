package org.example.reservacasarurales.Repository;

import org.example.reservacasarurales.Entity.Cocina;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CocinaRepository extends JpaRepository<Cocina, Long> {
    List<Cocina> findByCasaRuralCodigoCasa(Long codigoCasa);
}