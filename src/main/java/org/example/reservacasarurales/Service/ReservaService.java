package org.example.reservacasarurales.Service;

import java.time.LocalDate;
import java.util.List;

import org.example.reservacasarurales.DTOs.Request.DisponibilidadRequest;
import org.example.reservacasarurales.DTOs.Request.ReservaRequest;
import org.example.reservacasarurales.DTOs.Response.DisponibilidadResponse;
import org.example.reservacasarurales.DTOs.Response.NotificacionResponse;
import org.example.reservacasarurales.DTOs.Response.ReservaResponse;
import org.example.reservacasarurales.Entity.*;
import org.example.reservacasarurales.Mapper.ReservaMapper;
import org.example.reservacasarurales.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private CasaRuralRepository casaRepository;

    @Autowired
    private PaqueteRepository paqueteRepository;

    @Autowired
    private DormitorioRepository dormitorioRepository;

    @Autowired
    private ReservaMapper mapper;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PropietarioRepository propietarioRepository;

    @Autowired
    private PagoService pagoService;

    // CREAR RESERVA (HU013, HU014, HU017)
    @PreAuthorize("hasRole('CLIENTE')")
    public ReservaResponse crearReserva(ReservaRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();
        String correo = usuario.getCorreoElectronico();

        System.out.println("Correo autenticado: " + correo);

        Cliente cliente = clienteRepository.findByUsuarioCorreoElectronico(correo)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        CasaRural casa = casaRepository.findById(request.getCasaId())
                .orElseThrow(() -> new RuntimeException("Casa no encontrada"));

        PaqueteAlquiler paquete = paqueteRepository.findById(request.getPaqueteId())
                .orElseThrow(() -> new RuntimeException("Paquete no encontrado"));

        LocalDate fechaInicio = request.getFechaInicio();
        LocalDate fechaFin = fechaInicio.plusDays(request.getNoches());

        // ===== HU-26: VALIDAR CASA ENTERA (solo agregado) =====
        if (request.getDormitoriosIds() == null || request.getDormitoriosIds().isEmpty()) {
            if (reservaRepository.existsByPaqueteIdAndDormitoriosIsNotNull(request.getPaqueteId())) {
                throw new RuntimeException("No se puede reservar la casa entera porque ya hay habitaciones reservadas en este paquete");
            }
        }
        // ===== FIN HU-26 =====

        // VALIDAR VIGENCIA DEL PAQUETE
        if (fechaInicio.isBefore(paquete.getFechaInicio()) ||
                fechaFin.isAfter(paquete.getFechaFin())) {
            throw new RuntimeException("La reserva está fuera del rango de vigencia del paquete de alquiler");
        }

        // validar disponibilidad
        validarDisponibilidadCasa(request.getCasaId(), fechaInicio, fechaFin);

        if (request.getDormitoriosIds() != null && !request.getDormitoriosIds().isEmpty()) {
            validarDisponibilidadDormitorios(
                    request.getDormitoriosIds(),
                    fechaInicio,
                    request.getNoches()
            );
        }

        Reserva reserva = mapper.toEntity(request, casa, paquete);
        reserva.setCliente(cliente);

        if (request.getTelefonoContacto() != null) {
            reserva.setTelefonoContacto(request.getTelefonoContacto());
        } else {
            reserva.setTelefonoContacto(cliente.getTelefonoContacto());
        }

        if (request.getDormitoriosIds() != null && !request.getDormitoriosIds().isEmpty()) {
            List<Dormitorio> dormitorios = dormitorioRepository.findAllById(request.getDormitoriosIds());
            if (dormitorios.size() != request.getDormitoriosIds().size()) {
                throw new RuntimeException("Uno o más dormitorios no existen");
            }
            reserva.setDormitorios(dormitorios);
        }

        reserva.setFechaInicio(fechaInicio);
        reserva.setFechaFin(fechaFin);
        reserva.setFechaCreacion(LocalDate.now());
        reserva.setFechaLimitePago(LocalDate.now().plusDays(3));
        reserva.setEstado(EstadoReserva.PENDIENTE);

        return mapper.toResponse(reservaRepository.save(reserva));
    }

    // DISPONIBILIDAD (HU020)
    public DisponibilidadResponse verificarDisponibilidad(DisponibilidadRequest request) {

        CasaRural casa = casaRepository.findById(request.getCasaId())
                .orElseThrow(() -> new RuntimeException("La casa rural no existe"));

        LocalDate inicio = request.getFechaInicio();
        LocalDate fin = inicio.plusDays(request.getNoches());

        PaqueteAlquiler paquete = paqueteRepository.findById(request.getPaqueteId())
                .orElseThrow(() -> new RuntimeException("Paquete no encontrado"));

        if (inicio.isBefore(paquete.getFechaInicio()) || fin.isAfter(paquete.getFechaFin())) {
            return new DisponibilidadResponse(false, "El paquete de alquiler no está vigente para esas fechas");
        }

        List<Reserva> conflictos = reservaRepository.buscarReservasEnConflicto(request.getCasaId(), inicio, fin);
        boolean disponible = conflictos.isEmpty();

        return new DisponibilidadResponse(disponible, disponible ? "Disponible" : "No disponible en esas fechas");
    }

    private void validarDisponibilidadCasa(Long casaId, LocalDate inicio, LocalDate fin) {
        List<Reserva> conflictos = reservaRepository.buscarReservasEnConflicto(casaId, inicio, fin);
        if (!conflictos.isEmpty()) {
            throw new RuntimeException("La casa ya está reservada en esas fechas");
        }
    }

    public void validarDisponibilidadDormitorios(List<Long> dormitoriosIds,
                                                 LocalDate inicioNueva,
                                                 int noches) {
        List<Reserva> reservas = reservaRepository.findReservasPorDormitorios(dormitoriosIds);
        LocalDate finNueva = inicioNueva.plusDays(noches);
        for (Reserva r : reservas) {
            LocalDate inicioExistente = r.getFechaInicio();
            LocalDate finExistente = r.getFechaFin();
            boolean seCruzan = !(finNueva.isBefore(inicioExistente) || inicioNueva.isAfter(finExistente));
            if (seCruzan) {
                throw new RuntimeException("Dormitorio ya reservado en esas fechas");
            }
        }
    }

    @PreAuthorize("hasRole('PROPIETARIO')")
    public List<ReservaResponse> obtenerReservasPendientes() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal();
        Propietario propietario = propietarioRepository
                .findByUsuarioCorreoElectronico(usuario.getCorreoElectronico())
                .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));
        return reservaRepository.findPendientesByPropietario(propietario.getIdPropietario())
                .stream().map(mapper::toResponse).toList();
    }

    @PreAuthorize("hasRole('PROPIETARIO')")
    public ReservaResponse cancelarReserva(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal();
        Propietario propietario = propietarioRepository
                .findByUsuarioCorreoElectronico(usuario.getCorreoElectronico())
                .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));

        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        if (!reserva.getCasaRural().getPropietario().getIdPropietario().equals(propietario.getIdPropietario())) {
            throw new RuntimeException("No puedes cancelar esta reserva");
        }

        double total = reserva.getPaquete().getPrecio() * reserva.getNoches();
        double anticipoMinimo = total * 0.2;
        double totalPagado = pagoService.calcularTotalPagado(reserva.getId());

        if (totalPagado >= anticipoMinimo) {
            throw new RuntimeException("No se puede cancelar, ya se pagó el 20%");
        }

        if (reserva.getEstado() != EstadoReserva.PENDIENTE) {
            throw new RuntimeException("Solo se pueden cancelar reservas pendientes");
        }

        reserva.setEstado(EstadoReserva.CANCELADA);
        return mapper.toResponse(reservaRepository.save(reserva));
    }

    @PreAuthorize("hasRole('CLIENTE')")
    public List<ReservaResponse> obtenerMisReservas() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal();
        Cliente cliente = clienteRepository
                .findByUsuarioCorreoElectronico(usuario.getCorreoElectronico())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        return reservaRepository.findByClienteIdCliente(cliente.getIdCliente())
                .stream().map(mapper::toResponse).toList();
    }

    @PreAuthorize("hasRole('PROPIETARIO')")
    public List<NotificacionResponse> obtenerNotificaciones() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal();
        Propietario propietario = propietarioRepository
                .findByUsuarioCorreoElectronico(usuario.getCorreoElectronico())
                .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));
        List<Reserva> reservas = reservaRepository.findReservasExpiradas(propietario.getIdPropietario());
        return reservas.stream().map(r -> {
            NotificacionResponse n = new NotificacionResponse();
            n.setMensaje("La reserva #" + r.getId() + " ha expirado");
            n.setTelefono(r.getTelefonoContacto());
            n.setFechaLimitePago(r.getFechaLimitePago());
            return n;
        }).toList();
    }
}