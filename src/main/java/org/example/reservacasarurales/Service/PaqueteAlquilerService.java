package org.example.reservacasarurales.Service;


import lombok.RequiredArgsConstructor;
import org.example.reservacasarurales.DTOs.Request.DividirPaqueteRequest;
import org.example.reservacasarurales.DTOs.Request.PaqueteAlquilerRequest;
import org.example.reservacasarurales.DTOs.Request.SubPaqueteRequest;
import org.example.reservacasarurales.DTOs.Response.OcupacionPaqueteResponse;
import org.example.reservacasarurales.DTOs.Response.PaqueteAlquilerResponse;
import org.example.reservacasarurales.DTOs.Response.PeriodoOcupadoResponse;
import org.example.reservacasarurales.Entity.*;
import org.example.reservacasarurales.Mapper.PaqueteMapper;
import org.example.reservacasarurales.Repository.CasaRuralRepository;
import org.example.reservacasarurales.Repository.PaqueteRepository;
import org.example.reservacasarurales.Repository.PropietarioRepository;
import org.example.reservacasarurales.Repository.ReservaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaqueteAlquilerService {

    private final PaqueteRepository paqueteRepository;
    private final CasaRuralRepository casaRepository;
    private final PaqueteMapper paqueteMapper;
    private final PropietarioRepository propietarioRepository;
    private final ReservaRepository reservaRepository;

    // CREAR PAQUETE
    @PreAuthorize("hasRole('PROPIETARIO')")
    public PaqueteAlquilerResponse crearPaquete(Long codigoCasa, PaqueteAlquilerRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        Usuario usuario = (Usuario) authentication.getPrincipal();

        String correo = usuario.getCorreoElectronico();

        System.out.println("Correo autenticado: " + correo);

        Propietario propietario = propietarioRepository
                .findByUsuarioCorreoElectronico(correo)
                .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));

        // Validar fechas
        if (request.getFechaInicio().isAfter(request.getFechaFin())) {
            throw new RuntimeException("La fecha de inicio no puede ser mayor a la fecha fin");
        }

        CasaRural casa = casaRepository.findByCodigoCasa(codigoCasa)
                .orElseThrow(() -> new RuntimeException("Casa no encontrada"));

        // Mapear
        PaqueteAlquiler paquete = paqueteMapper.toEntity(request);
        paquete.setCasaRural(casa);

        // Guardar
        paqueteRepository.save(paquete);

        return paqueteMapper.toResponse(paquete);
    }

    // ACTUALIZAR PAQUETE
    public PaqueteAlquilerResponse actualizarPaquete(Long idPaquete, PaqueteAlquilerRequest request) {

        PaqueteAlquiler paquete = paqueteRepository.findById(idPaquete)
                .orElseThrow(() -> new RuntimeException("Paquete no encontrado"));

        // Validación fechas
        if (request.getFechaInicio() != null && request.getFechaFin() != null) {
            if (request.getFechaInicio().isAfter(request.getFechaFin())) {
                throw new RuntimeException("La fecha de inicio no puede ser mayor a la fecha fin");
            }
        }

        // Update parcial
        if (request.getFechaInicio() != null) {
            paquete.setFechaInicio(request.getFechaInicio());
        }

        if (request.getFechaFin() != null) {
            paquete.setFechaFin(request.getFechaFin());
        }

        if (request.getPrecio() != null) {
            paquete.setPrecio(request.getPrecio());
        }

        if (request.getTipoAlquiler() != null) {
            paquete.setTipoAlquiler(request.getTipoAlquiler());
        }

        paqueteRepository.save(paquete);

        return paqueteMapper.toResponse(paquete);
    }

    public List<PaqueteAlquilerResponse> listarPaquetesPorCasa(Long codigoCasa) {

        List<PaqueteAlquiler> paquetes = paqueteRepository
                .findByCasaRural_CodigoCasa(codigoCasa);

        return paquetes.stream()
                .map(paqueteMapper::toResponse)
                .toList();
    }

    // CALENDARIO DE OCUPACIÓN POR PAQUETES (HU - CLIENTE)
    public List<OcupacionPaqueteResponse> obtenerOcupacionPorCasa(Long codigoCasa) {

        List<PaqueteAlquiler> paquetes = paqueteRepository
                .findByCasaRural_CodigoCasa(codigoCasa);

        List<OcupacionPaqueteResponse> resultado = new ArrayList<>();

        for (PaqueteAlquiler paquete : paquetes) {

            List<Reserva> reservasActivas = reservaRepository
                    .findByPaqueteIdPaqueteAndEstadoNot(
                            paquete.getIdPaquete(),
                            EstadoReserva.CANCELADA
                    );

            List<PeriodoOcupadoResponse> periodos = reservasActivas.stream()
                    .map(r -> {
                        PeriodoOcupadoResponse p = new PeriodoOcupadoResponse();
                        p.setFechaInicio(r.getFechaInicio());
                        p.setFechaFin(r.getFechaFin());
                        p.setEstado(r.getEstado().name());
                        return p;
                    })
                    .toList();

            OcupacionPaqueteResponse ocupacion = new OcupacionPaqueteResponse();
            ocupacion.setIdPaquete(paquete.getIdPaquete());
            ocupacion.setFechaInicio(paquete.getFechaInicio());
            ocupacion.setFechaFin(paquete.getFechaFin());
            ocupacion.setPrecio(paquete.getPrecio());
            ocupacion.setTipoAlquiler(paquete.getTipoAlquiler());
            ocupacion.setPeriodosOcupados(periodos);

            resultado.add(ocupacion);
        }

        return resultado;
    }

    // DIVIDIR PAQUETE EN PERIODOS MÁS PEQUEÑOS (HU - PROPIETARIO)
    @PreAuthorize("hasRole('PROPIETARIO')")
    @Transactional
    public List<PaqueteAlquilerResponse> dividirPaquete(Long idPaquete, DividirPaqueteRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();

        Propietario propietario = propietarioRepository
                .findByUsuarioCorreoElectronico(usuario.getCorreoElectronico())
                .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));

        PaqueteAlquiler paqueteOriginal = paqueteRepository.findById(idPaquete)
                .orElseThrow(() -> new RuntimeException("Paquete no encontrado"));

        // Validar que el propietario sea el dueño de la casa
        if (!paqueteOriginal.getCasaRural().getPropietario().getIdPropietario()
                .equals(propietario.getIdPropietario())) {
            throw new RuntimeException("No puedes dividir un paquete que no te pertenece");
        }

        // Validar que no haya reservas activas en el paquete
        List<Reserva> reservasActivas = reservaRepository
                .findByPaqueteIdPaqueteAndEstadoNot(idPaquete, EstadoReserva.CANCELADA);

        if (!reservasActivas.isEmpty()) {
            throw new RuntimeException("No se puede dividir un paquete que tiene reservas activas");
        }

        // Validar que se envíen al menos 2 sub-paquetes
        if (request.getSubPaquetes() == null || request.getSubPaquetes().size() < 2) {
            throw new RuntimeException("Debe indicar al menos 2 sub-paquetes para dividir");
        }

        List<SubPaqueteRequest> subs = request.getSubPaquetes();

        for (SubPaqueteRequest sub : subs) {
            // Validar fechas de cada sub-paquete
            if (sub.getFechaInicio() == null || sub.getFechaFin() == null) {
                throw new RuntimeException("Cada sub-paquete debe tener fecha de inicio y fin");
            }

            if (!sub.getFechaInicio().isBefore(sub.getFechaFin())) {
                throw new RuntimeException(
                        "La fecha de inicio debe ser anterior a la fecha fin en cada sub-paquete"
                );
            }

            // Validar que esté dentro del rango del paquete original
            if (sub.getFechaInicio().isBefore(paqueteOriginal.getFechaInicio()) ||
                    sub.getFechaFin().isAfter(paqueteOriginal.getFechaFin())) {
                throw new RuntimeException(
                        "Los sub-paquetes deben estar dentro del rango del paquete original ("
                                + paqueteOriginal.getFechaInicio() + " a "
                                + paqueteOriginal.getFechaFin() + ")"
                );
            }
        }

        // Validar que los sub-paquetes no se solapen entre sí
        for (int i = 0; i < subs.size(); i++) {
            for (int j = i + 1; j < subs.size(); j++) {
                SubPaqueteRequest a = subs.get(i);
                SubPaqueteRequest b = subs.get(j);

                boolean seSolapan = !a.getFechaFin().isBefore(b.getFechaInicio())
                        && !b.getFechaFin().isBefore(a.getFechaInicio());

                if (seSolapan) {
                    throw new RuntimeException("Los sub-paquetes no pueden solaparse entre sí");
                }
            }
        }

        CasaRural casa = paqueteOriginal.getCasaRural();

        // Crear los nuevos paquetes
        List<PaqueteAlquiler> nuevosPaquetes = new ArrayList<>();

        for (SubPaqueteRequest sub : subs) {
            PaqueteAlquiler nuevo = new PaqueteAlquiler();
            nuevo.setFechaInicio(sub.getFechaInicio());
            nuevo.setFechaFin(sub.getFechaFin());
            nuevo.setPrecio(sub.getPrecio());
            nuevo.setTipoAlquiler(
                    sub.getTipoAlquiler() != null
                            ? sub.getTipoAlquiler()
                            : paqueteOriginal.getTipoAlquiler()
            );
            nuevo.setCasaRural(casa);
            nuevosPaquetes.add(nuevo);
        }

        paqueteRepository.saveAll(nuevosPaquetes);

        // Eliminar el paquete original
        paqueteRepository.delete(paqueteOriginal);

        return nuevosPaquetes.stream()
                .map(paqueteMapper::toResponse)
                .toList();
    }
}