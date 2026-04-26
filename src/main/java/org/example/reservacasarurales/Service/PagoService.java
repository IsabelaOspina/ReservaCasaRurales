package org.example.reservacasarurales.Service;

import java.time.LocalDate;
import java.util.List;

import org.example.reservacasarurales.DTOs.Request.PagoRequest;
import org.example.reservacasarurales.DTOs.Response.PagoInfoResponse;
import org.example.reservacasarurales.DTOs.Response.PagoResponse;
import org.example.reservacasarurales.Entity.*;
import org.example.reservacasarurales.Mapper.PagoMapper;
import org.example.reservacasarurales.Repository.ClienteRepository;
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

    @Autowired
    private ClienteRepository clienteRepository;


    //HU007
    @PreAuthorize("hasRole('CLIENTE')")
    public PagoResponse registrarPago(PagoRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        Usuario usuario = (Usuario) authentication.getPrincipal();

        String correo = usuario.getCorreoElectronico();

        Cliente cliente = clienteRepository
                .findByUsuarioCorreoElectronico(correo)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Reserva reserva = reservaRepository.findById(request.getReservaId())
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));


        if (!reserva.getCliente().getIdCliente().equals(cliente.getIdCliente())) {
            throw new RuntimeException("No tienes permiso para pagar esta reserva");
        }


        if (reserva.getEstado() == EstadoReserva.CANCELADA) {
            throw new RuntimeException("No se puede pagar una reserva cancelada");
        }

        if (LocalDate.now().isAfter(reserva.getFechaLimitePago())) {
            throw new RuntimeException("La reserva expiró");
        }

        double total = reserva.getPaquete().getPrecio() * reserva.getNoches();
        double anticipoMinimo = total * 0.2;

        double totalPagado = calcularTotalPagado(reserva.getId());
        double nuevoTotalPagado = totalPagado + request.getMonto();


        if (totalPagado == 0 && request.getMonto() < anticipoMinimo) {
            throw new RuntimeException("Debe registrar al menos el 20% en el primer pago");
        }


        if (nuevoTotalPagado > total) {
            throw new RuntimeException("El pago excede el total de la reserva");
        }

        Pago pago = mapper.toEntity(request, reserva);
        pago.setConfirmado(true);


        if (nuevoTotalPagado >= anticipoMinimo
                && reserva.getEstado() == EstadoReserva.PENDIENTE) {

            reserva.setEstado(EstadoReserva.CONFIRMADA);
        }

        if (nuevoTotalPagado == total) {
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

    @PreAuthorize("hasRole('CLIENTE')")
    public PagoInfoResponse obtenerInfoPago(Long reservaId) {

        Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

        Usuario usuario = (Usuario) authentication.getPrincipal();

        String correo = usuario.getCorreoElectronico();

        
        Cliente cliente = clienteRepository
                .findByUsuarioCorreoElectronico(correo)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        
        if (!reserva.getCliente().getIdCliente().equals(cliente.getIdCliente())) {
            throw new RuntimeException("No tienes permiso para ver esta reserva");
        }

        double total = reserva.getPaquete().getPrecio() * reserva.getNoches();
        double pagado = calcularTotalPagado(reservaId);
        double restante = total - pagado;

        PagoInfoResponse response = new PagoInfoResponse();
        response.setTotal(total);
        response.setAnticipo(total * 0.2);
        response.setRestante(restante);

        // obtener propietario desde la reserva
        Propietario propietario = reserva.getCasaRural().getPropietario();

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