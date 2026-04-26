package org.example.reservacasarurales.Service;

import java.time.LocalDate;
import java.util.List;

import org.example.reservacasarurales.Entity.EstadoReserva;
import org.example.reservacasarurales.Entity.Reserva;
import org.example.reservacasarurales.Repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReservaScheduler {

    @Autowired
    private ReservaRepository reservaRepository;

    // Se ejecuta cada hora para cancelar reservas pendientes cuya fecha límite de pago ya pasó
    @Scheduled(fixedRate = 3600000)
    public void cancelarReservasExpiradas() {

        List<Reserva> expiradas = reservaRepository
                .findByEstadoAndFechaLimitePagoBefore(EstadoReserva.PENDIENTE, LocalDate.now());

        for (Reserva reserva : expiradas) {
            reserva.setEstado(EstadoReserva.CANCELADA);
            reservaRepository.save(reserva);
            System.out.println("Reserva #" + reserva.getId()
                    + " cancelada automáticamente por no pagar en el plazo de 3 días.");
        }
    }
}
