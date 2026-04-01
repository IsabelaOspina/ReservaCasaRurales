package org.example.reservacasarurales.Repository;

import org.example.reservacasarurales.Entity.EstadoReserva;
import org.example.reservacasarurales.Entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    // Buscar reservas por cliente
    List<Reserva> findByClienteId(Long clienteId);

    // Buscar reservas por estado
    List<Reserva> findByEstado(EstadoReserva estado);

    // Buscar reservas por cliente y estado
    List<Reserva> findByClienteIdAndEstado(Long clienteId, EstadoReserva estado);

    // Buscar reservas en un rango de fechas (que se solapen)
    @Query("SELECT r FROM Reserva r WHERE " +
            "(:fechaInicio BETWEEN r.fechaInicio AND r.fechaFin OR " +
            ":fechaFin BETWEEN r.fechaInicio AND r.fechaFin OR " +
            "r.fechaInicio BETWEEN :fechaInicio AND :fechaFin)")
    List<Reserva> findReservasEnRango(@Param("fechaInicio") LocalDate fechaInicio,
                                      @Param("fechaFin") LocalDate fechaFin);

    // Buscar reservas por fechas específicas (que estén activas en una fecha)
    @Query("SELECT r FROM Reserva r WHERE :fecha BETWEEN r.fechaInicio AND r.fechaFin")
    List<Reserva> findReservasPorFecha(@Param("fecha") LocalDate fecha);

    // Buscar reservas confirmadas en un rango de fechas
    @Query("SELECT r FROM Reserva r WHERE r.estado = 'CONFIRMADA' AND " +
            "(:fechaInicio BETWEEN r.fechaInicio AND r.fechaFin OR " +
            ":fechaFin BETWEEN r.fechaInicio AND r.fechaFin OR " +
            "r.fechaInicio BETWEEN :fechaInicio AND :fechaFin)")
    List<Reserva> findReservasConfirmadasEnRango(@Param("fechaInicio") LocalDate fechaInicio,
                                                 @Param("fechaFin") LocalDate fechaFin);

    // Buscar reservas pendientes que expiraron (para HU17 y HU23)
    @Query("SELECT r FROM Reserva r WHERE r.estado = 'PENDIENTE' AND r.fechaInicio <= :fechaLimite")
    List<Reserva> findReservasPendientesExpiradas(@Param("fechaLimite") LocalDate fechaLimite);

    // Contar reservas activas de un cliente
    long countByClienteIdAndEstadoIn(Long clienteId, List<EstadoReserva> estados);

    // Buscar reservas por fecha de inicio (para ordenar)
    List<Reserva> findAllByOrderByFechaInicioAsc();

    // Verificar si existe una reserva en un rango de fechas para un cliente específico
    boolean existsByClienteIdAndFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(
            Long clienteId, LocalDate fechaFin, LocalDate fechaInicio);
}