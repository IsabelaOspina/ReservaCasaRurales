package org.example.reservacasarurales.Repository;


import java.time.LocalDate;
import java.util.List;

import org.example.reservacasarurales.Entity.EstadoReserva;
import org.example.reservacasarurales.Entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    // HU020 - Validar disponibilidad REAL de la casa
    @Query("""
    SELECT r FROM Reserva r
    WHERE r.casaRural.codigoCasa = :casaId
    AND r.estado <> org.example.reservacasarurales.Entity.EstadoReserva.CANCELADA
    AND (
        (:fechaInicio BETWEEN r.fechaInicio AND r.fechaFin)
        OR (:fechaFin BETWEEN r.fechaInicio AND r.fechaFin)
        OR (r.fechaInicio BETWEEN :fechaInicio AND :fechaFin)
    )
    """)
    List<Reserva> buscarReservasEnConflicto(
            @Param("casaId") Long casaId,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );

    // HU014 - Validar dormitorios
    @Query("""
    SELECT DISTINCT r FROM Reserva r
    JOIN r.dormitorios d
    WHERE d.idDormitorio IN :ids
    AND r.estado <> org.example.reservacasarurales.Entity.EstadoReserva.CANCELADA
    """)
    List<Reserva> findReservasPorDormitorios(@Param("ids") List<Long> ids);


    @Query("""
    SELECT r FROM Reserva r
    WHERE r.estado = 'PENDIENTE'
    AND r.casaRural.propietario.idPropietario = :propietarioId
    """)
    List<Reserva> findPendientesByPropietario(Long propietarioId);

    // Buscar todas las reservas de un cliente
    List<Reserva> findByClienteIdCliente(Long idCliente);

}