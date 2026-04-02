package org.example.reservacasarurales.Service;

import java.time.LocalDate;
import java.util.List;

import org.example.reservacasarurales.DTOs.Request.PagoRequest;
import org.example.reservacasarurales.DTOs.Response.PagoInfoResponse;
import org.example.reservacasarurales.DTOs.Response.PagoResponse;
import org.example.reservacasarurales.Entity.Pago;
import org.example.reservacasarurales.Entity.Reserva;
import org.example.reservacasarurales.Mapper.PagoMapper;
import org.example.reservacasarurales.Repository.PagoRepository;
import org.example.reservacasarurales.Repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private PagoMapper mapper;

    public PagoResponse registrarPago(PagoRequest request) {

        Reserva reserva = reservaRepository.findById(request.getReservaId())
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        if (LocalDate.now().isAfter(reserva.getFechaLimitePago())) {
            throw new RuntimeException("La reserva expiró");
        }

        double total = reserva.getPaquete().getPrecio() * reserva.getNoches();
        double anticipoMinimo = total * 0.2;

        double totalPagado = calcularTotalPagado(reserva.getId());

    
        if (totalPagado == 0 && request.getMonto() < anticipoMinimo) {
            throw new RuntimeException("Debe pagar al menos el 20% en el primer pago");
        }

    
        if (totalPagado + request.getMonto() > total) {
            throw new RuntimeException("El pago excede el total de la reserva");
        }

        Pago pago = mapper.toEntity(request, reserva);
        pago.setConfirmado(true);

    
        if (totalPagado + request.getMonto() >= anticipoMinimo) {
            reserva.setConfirmada(true);
        }

        
        if (totalPagado + request.getMonto() == total) {
            System.out.println("RESERVA PAGADA COMPLETAMENTE");
        }

        reservaRepository.save(reserva);

        return mapper.toResponse(pagoRepository.save(pago));
    }

    public List<PagoResponse> obtenerPagosPorReserva(Long reservaId) {

        List<Pago> pagos = pagoRepository.findByReservaId(reservaId);

        if (pagos.isEmpty()) {
            throw new RuntimeException("No hay pagos para esta reserva");
        }

        return pagos.stream()
                .map(mapper::toResponse)
                .toList();
    }

    public PagoInfoResponse obtenerInfoPago(Long reservaId) {

        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        double total = reserva.getPaquete().getPrecio() * reserva.getNoches();
        double pagado = calcularTotalPagado(reservaId);
        double restante = total - pagado;

        PagoInfoResponse response = new PagoInfoResponse();
        response.setTotal(total);
        response.setAnticipo(total * 0.2);
        response.setRestante(restante);
        response.setNumeroCuenta("123-456-789");
        response.setBanco("Bancolombia");

        return response;
    }

    public double calcularTotalPagado(Long reservaId) {
        return pagoRepository.findByReservaId(reservaId)
                .stream()
                .mapToDouble(Pago::getMonto)
                .sum();
    }

    public double calcularRestante(Long reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        double total = reserva.getPaquete().getPrecio() * reserva.getNoches();
        double pagado = calcularTotalPagado(reservaId);

        return total - pagado;
    }
}