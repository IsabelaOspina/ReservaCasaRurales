package org.example.reservacasarurales.Service;

import java.time.LocalDate;
import java.util.List;

import org.example.reservacasarurales.DTOs.Request.PagoRequest;
import org.example.reservacasarurales.DTOs.Response.PagoInfoResponse;
import org.example.reservacasarurales.DTOs.Response.PagoResponse;
import org.example.reservacasarurales.Entity.Pago;
import org.example.reservacasarurales.Entity.Propietario;
import org.example.reservacasarurales.Entity.Reserva;
import org.example.reservacasarurales.Entity.Usuario;
import org.example.reservacasarurales.Mapper.PagoMapper;
import org.example.reservacasarurales.Repository.PagoRepository;
import org.example.reservacasarurales.Repository.PropietarioRepository;
import org.example.reservacasarurales.Repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private PagoMapper mapper;

    @Autowired
    private PropietarioRepository propietarioRepository;


    //HU007
    @PreAuthorize("hasRole('PROPIETARIO')")
    public PagoResponse registrarPago(PagoRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String correo = authentication.getName();

        // buscar propietario autenticado
        Propietario propietario = propietarioRepository
                .findByUsuarioCorreoElectronico(correo)
                .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));

        Reserva reserva = reservaRepository.findById(request.getReservaId())
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        // validar que la reserva pertenece a una casa del propietario
        if (!reserva.getCasaRural().getPropietario().getIdPropietario()
                .equals(propietario.getIdPropietario())) {

            throw new RuntimeException("No puedes registrar pagos de reservas de otras casas");
        }

        if (LocalDate.now().isAfter(reserva.getFechaLimitePago())) {
            throw new RuntimeException("La reserva expiró");
        }

        double total = reserva.getPaquete().getPrecio() * reserva.getNoches();
        double anticipoMinimo = total * 0.2;

        double totalPagado = calcularTotalPagado(reserva.getId());

        if (totalPagado == 0 && request.getMonto() < anticipoMinimo) {
            throw new RuntimeException("Debe registrar al menos el 20% en el primer pago");
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

    @PreAuthorize("hasRole('PROPIETARIO')")
    public PagoInfoResponse obtenerInfoPago(Long reservaId) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        Usuario usuario = (Usuario) authentication.getPrincipal();

        String correo = usuario.getCorreoElectronico();

        Propietario propietario = propietarioRepository
                .findByUsuarioCorreoElectronico(correo)
                .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));

        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        // validar que la reserva pertenece a una casa del propietario
        if (!reserva.getCasaRural().getPropietario().getIdPropietario()
                .equals(propietario.getIdPropietario())) {

            throw new RuntimeException("No tienes permiso para ver esta reserva");
        }

        double total = reserva.getPaquete().getPrecio() * reserva.getNoches();
        double pagado = calcularTotalPagado(reservaId);
        double restante = total - pagado;

        PagoInfoResponse response = new PagoInfoResponse();
        response.setTotal(total);
        response.setAnticipo(total * 0.2);
        response.setRestante(restante);

        // datos del propietario
        response.setNumeroCuenta(propietario.getNumeroCuenta());
        response.setBanco(propietario.getBanco());

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