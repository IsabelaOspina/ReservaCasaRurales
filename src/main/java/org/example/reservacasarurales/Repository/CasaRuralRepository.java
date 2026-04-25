package org.example.reservacasarurales.Repository;

import org.example.reservacasarurales.Entity.CasaRural;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CasaRuralRepository extends JpaRepository<CasaRural, Long> {

    @Query("SELECT c.codigoCasa FROM CasaRural c WHERE c.poblacion = :poblacion")
    List<Long> findCodigosByPoblacion(String poblacion);

    Optional<CasaRural> findByCodigoCasa(Long codigoCasa);

    boolean existsByCodigoCasaAndPropietarioIdPropietario(
            Long codigoCasa,
            Long idPropietario
    );


}

