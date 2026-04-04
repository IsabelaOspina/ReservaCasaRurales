package org.example.reservacasarurales.Service;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import org.example.reservacasarurales.DTOs.Request.DisponibilidadRequest;
import org.example.reservacasarurales.DTOs.Request.ReservaRequest;
import org.example.reservacasarurales.DTOs.Response.DisponibilidadResponse;
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
    
    // CREAR RESERVA (HU013, HU014, HU017)

    @PreAuthorize("hasRole('CLIENTE')")
    public ReservaResponse crearReserva(ReservaRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        Usuario usuario = (Usuario) authentication.getPrincipal();

        String correo = usuario.getCorreoElectronico();

        System.out.println("Correo autenticado: " + correo);

        Cliente cliente = clienteRepository
                .findByUsuarioCorreoElectronico(correo)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        CasaRural casa = casaRepository.findById(request.getCasaId())
                .orElseThrow(() -> new RuntimeException("Casa no encontrada"));

        PaqueteAlquiler paquete = paqueteRepository.findById(request.getPaqueteId())
                .orElseThrow(() -> new RuntimeException("Paquete no encontrado"));

        LocalDate fechaInicio = request.getFechaInicio();
        LocalDate fechaFin = fechaInicio.plusDays(request.getNoches());

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

        // asignar cliente autenticado
        reserva.setCliente(cliente);

        // asignar teléfono (HU15)
        if (request.getTelefonoContacto() != null) {
            reserva.setTelefonoContacto(request.getTelefonoContacto());
        } else {
            reserva.setTelefonoContacto(cliente.getTelefonoContacto());
        }


        if (request.getDormitoriosIds() != null && !request.getDormitoriosIds().isEmpty()) {

            List<Dormitorio> dormitorios =
                    dormitorioRepository.findAllById(request.getDormitoriosIds());

            if (dormitorios.size() != request.getDormitoriosIds().size()) {
                throw new RuntimeException("Uno o más dormitorios no existen");
            }

            reserva.setDormitorios(dormitorios);
        }

        verificarYActualizarPaqueteCompleto(paquete.getIdPaquete(), casa.getCodigoCasa());

        reserva.setFechaInicio(fechaInicio);
        reserva.setFechaFin(fechaFin);
        reserva.setConfirmada(false);
        reserva.setFechaCreacion(LocalDate.now());
        reserva.setFechaLimitePago(LocalDate.now().plusDays(3)); //HU017

        return mapper.toResponse(reservaRepository.save(reserva));


    }

    
    // DISPONIBILIDAD (HU020)


    public DisponibilidadResponse verificarDisponibilidad(DisponibilidadRequest request) {

        CasaRural casa = casaRepository.findById(request.getCasaId())
                .orElseThrow(() -> new RuntimeException("La casa rural no existe"));

        LocalDate inicio = request.getFechaInicio();
        LocalDate fin = inicio.plusDays(request.getNoches());

        List<Reserva> conflictos = reservaRepository.buscarReservasEnConflicto(
                request.getCasaId(),
                inicio,
                fin
        );

        boolean disponible = conflictos.isEmpty();

        return new DisponibilidadResponse(
                disponible,
                disponible ? "Disponible" : "No disponible en esas fechas"
        );
    }

    
    // VALIDAR CASA
    
    private void validarDisponibilidadCasa(Long casaId, LocalDate inicio, LocalDate fin) {

        List<Reserva> conflictos = reservaRepository.buscarReservasEnConflicto(
                casaId,
                inicio,
                fin
        );

        if (!conflictos.isEmpty()) {
            throw new RuntimeException("La casa ya está reservada en esas fechas");
        }
    }

    
    // VALIDAR DORMITORIOS
    
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

    // HU08 - Listar reservas pendientes con días transcurridos
    @PreAuthorize("hasRole('PROPIETARIO')")
    public List<Map<String, Object>> listarReservasPendientes() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal();

        Propietario propietario = propietarioRepository
                .findByUsuarioCorreoElectronico(usuario.getCorreoElectronico())
                .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));

        List<Reserva> reservasPendientes = reservaRepository.findAll().stream()
                .filter(r -> !r.isConfirmada())
                .filter(r -> r.getCasaRural().getPropietario().getIdPropietario()
                        .equals(propietario.getIdPropietario()))
                .collect(Collectors.toList());

        List<Map<String, Object>> resultado = new ArrayList<>();
        for (Reserva r : reservasPendientes) {
            Map<String, Object> map = new HashMap<>();
            map.put("idReserva", r.getId());
            map.put("fechaInicio", r.getFechaInicio());
            map.put("fechaFin", r.getFechaFin());
            map.put("noches", r.getNoches());
            map.put("fechaLimitePago", r.getFechaLimitePago());
            long diasTranscurridos = ChronoUnit.DAYS.between(r.getFechaLimitePago(), LocalDate.now());
            map.put("diasTranscurridos", diasTranscurridos > 0 ? diasTranscurridos : 0);
            resultado.add(map);
        }
        return resultado;
    }

    // HU23 - Listar reservas expiradas (plazo 3 días vencido)
    @PreAuthorize("hasRole('PROPIETARIO')")
    public List<ReservaResponse> listarReservasExpiradas() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal();

        Propietario propietario = propietarioRepository
                .findByUsuarioCorreoElectronico(usuario.getCorreoElectronico())
                .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));

        LocalDate hoy = LocalDate.now();

        return reservaRepository.findAll().stream()
                .filter(r -> !r.isConfirmada() && r.getFechaLimitePago().isBefore(hoy))
                .filter(r -> r.getCasaRural().getPropietario().getIdPropietario()
                        .equals(propietario.getIdPropietario()))
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    // HU20/HU28 - Calendario de disponibilidad
    public Map<LocalDate, String> obtenerCalendario(Long casaId, LocalDate desde, LocalDate hasta) {
        Map<LocalDate, String> calendario = new LinkedHashMap<>();

        List<Reserva> reservas = reservaRepository.buscarReservasEnConflicto(casaId, desde, hasta);

        for (LocalDate fecha = desde; !fecha.isAfter(hasta); fecha = fecha.plusDays(1)) {
            LocalDate finalFecha = fecha;
            boolean ocupado = reservas.stream()
                    .anyMatch(r -> !finalFecha.isBefore(r.getFechaInicio()) &&
                            !finalFecha.isAfter(r.getFechaFin()));
            calendario.put(fecha, ocupado ? "RESERVADO" : "LIBRE");
        }
        return calendario;
    }

    private void verificarYActualizarPaqueteCompleto(Long paqueteId, Long codigoCasa) {
        PaqueteAlquiler paquete = paqueteRepository.findById(paqueteId)
                .orElseThrow(() -> new RuntimeException("Paquete no encontrado"));

        // Solo aplica para paquetes de tipo POR_HABITACIONES
        if (paquete.getTipoAlquiler() == TipoAlquiler.POR_HABITACIONES) {
            int totalDormitorios = paquete.getCasaRural().getDormitorios().size();
            long reservasConfirmadas = reservaRepository.findAll().stream()
                    .filter(r -> r.getPaquete() != null && r.getPaquete().getIdPaquete().equals(paqueteId))
                    .filter(Reserva::isConfirmada)
                    .count();

            if (reservasConfirmadas >= totalDormitorios) {
                paquete.setTipoAlquiler(TipoAlquiler.COMPLETO);
                paqueteRepository.save(paquete);
            }
        }


    }
}