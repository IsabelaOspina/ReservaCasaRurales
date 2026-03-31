package org.example.reservacasarurales.Service;

import java.time.LocalDate;
import java.util.List;

import org.example.reservacasarurales.DTOs.Request.DisponibilidadRequest;
import org.example.reservacasarurales.DTOs.Request.ReservaRequest;
import org.example.reservacasarurales.DTOs.Response.DisponibilidadResponse;
import org.example.reservacasarurales.DTOs.Response.ReservaResponse;
import org.example.reservacasarurales.Entity.CasaRural;
import org.example.reservacasarurales.Entity.Dormitorio;
import org.example.reservacasarurales.Entity.PaqueteAlquiler;
import org.example.reservacasarurales.Entity.Reserva;
import org.example.reservacasarurales.Mapper.ReservaMapper;
import org.example.reservacasarurales.Repository.CasaRuralRepository;
import org.example.reservacasarurales.Repository.DormitorioRepository;
import org.example.reservacasarurales.Repository.PaqueteRepository;
import org.example.reservacasarurales.Repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    
    // CREAR RESERVA (HU013, HU014, HU017)
    
    public ReservaResponse crearReserva(ReservaRequest request) {

        CasaRural casa = casaRepository.findById(request.getCasaId())
                .orElseThrow(() -> new RuntimeException("Casa no encontrada"));

        PaqueteAlquiler paquete = paqueteRepository.findById(request.getPaqueteId())
                .orElseThrow(() -> new RuntimeException("Paquete no encontrado"));

        LocalDate fechaInicio = request.getFechaInicio();
        LocalDate fechaFin = fechaInicio.plusDays(request.getNoches());

        // Validar disponibilidad general
        validarDisponibilidadCasa(request.getCasaId(), fechaInicio, fechaFin);

        // Validar dormitorios (HU014)
        if (request.getDormitoriosIds() != null && !request.getDormitoriosIds().isEmpty()) {
            validarDisponibilidadDormitorios(
                    request.getDormitoriosIds(),
                    fechaInicio,
                    request.getNoches()
            );
        }

        Reserva reserva = mapper.toEntity(request, casa, paquete);

        
        if (request.getDormitoriosIds() != null && !request.getDormitoriosIds().isEmpty()) {
            List<Dormitorio> dormitorios = dormitorioRepository.findAllById(request.getDormitoriosIds());

            if (dormitorios.size() != request.getDormitoriosIds().size()) {
                throw new RuntimeException("Uno o más dormitorios no existen");
            }

            reserva.setDormitorios(dormitorios);
        }

        
        reserva.setFechaInicio(fechaInicio);
        reserva.setFechaFin(fechaFin);
        reserva.setConfirmada(false);
        reserva.setFechaCreacion(LocalDate.now());
        reserva.setFechaLimitePago(LocalDate.now().plusDays(3)); // HU017

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
}