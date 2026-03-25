package org.example.reservacasarurales.Repository;

import org.example.reservacasarurales.Entity.PaqueteAlquiler;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaqueteRepository extends JpaRepository<PaqueteAlquiler, Long> {

    List<PaqueteAlquiler> findByCasaRuralCodigoCasa(Long codigoCasa);
}
