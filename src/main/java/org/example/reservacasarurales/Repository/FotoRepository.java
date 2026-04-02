package org.example.reservacasarurales.Repository;

import org.example.reservacasarurales.Entity.Foto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FotoRepository extends JpaRepository<Foto, Long> {
    List<Foto> findByCasaRuralCodigoCasa(Long codigoCasa);
}
