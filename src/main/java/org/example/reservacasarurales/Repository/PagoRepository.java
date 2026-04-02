package org.example.reservacasarurales.Repository;

import org.example.reservacasarurales.Entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PagoRepository extends JpaRepository<Pago, Long> {
    List<Pago> findByConfirmado(boolean confirmado);
    List<Pago> findByReservaId(Long reservaId);
}
