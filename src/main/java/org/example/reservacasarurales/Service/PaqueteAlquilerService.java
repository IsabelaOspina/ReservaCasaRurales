package org.example.reservacasarurales.Service;


import lombok.RequiredArgsConstructor;
import org.example.reservacasarurales.DTOs.Request.PaqueteAlquilerRequest;
import org.example.reservacasarurales.DTOs.Request.PaqueteAlquilerRequest;
import org.example.reservacasarurales.DTOs.Response.PaqueteAlquilerResponse;
import org.example.reservacasarurales.Entity.CasaRural;
import org.example.reservacasarurales.Entity.PaqueteAlquiler;
import org.example.reservacasarurales.Entity.Propietario;
import org.example.reservacasarurales.Entity.Usuario;
import org.example.reservacasarurales.Mapper.PaqueteMapper;
import org.example.reservacasarurales.Repository.CasaRuralRepository;
import org.example.reservacasarurales.Repository.PaqueteRepository;
import org.example.reservacasarurales.Repository.PropietarioRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.example.reservacasarurales.Repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaqueteAlquilerService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private PropietarioRepository propietarioRepository;

    private final PaqueteRepository paqueteRepository;
    private final CasaRuralRepository casaRepository;
    private final PaqueteMapper paqueteMapper;

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

        //  Validar que las fechas no coincidan con otro paquete
        boolean hayConflicto = paqueteRepository.findByCasaRural_CodigoCasa(codigoCasa)
                .stream()
                .anyMatch(p -> !(request.getFechaFin().isBefore(p.getFechaInicio()) ||
                        request.getFechaInicio().isAfter(p.getFechaFin())));

        if (hayConflicto) {
            throw new RuntimeException("Las fechas coinciden con otro paquete existente en esta casa");
        }


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

    @PreAuthorize("hasRole('PROPIETARIO')")
    public List<PaqueteAlquilerResponse> dividirPaquete(Long idPaquete, LocalDate nuevaFechaInicio, LocalDate nuevaFechaFin) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal();

        Propietario propietario = propietarioRepository
                .findByUsuarioCorreoElectronico(usuario.getCorreoElectronico())
                .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));

        PaqueteAlquiler paqueteOriginal = paqueteRepository.findById(idPaquete)
                .orElseThrow(() -> new RuntimeException("Paquete no encontrado"));

        if (!paqueteOriginal.getCasaRural().getPropietario().getIdPropietario().equals(propietario.getIdPropietario())) {
            throw new RuntimeException("No tienes permiso para dividir este paquete");
        }

        // Validar que no tenga reservas
        boolean tieneReservas = reservaRepository.findAll().stream()
                .anyMatch(r -> r.getPaquete() != null && r.getPaquete().getIdPaquete().equals(idPaquete));

        if (tieneReservas) {
            throw new RuntimeException("No se puede dividir un paquete que tiene reservas asociadas");
        }

        // Crear primer paquete
        PaqueteAlquiler paquete1 = new PaqueteAlquiler();
        paquete1.setFechaInicio(paqueteOriginal.getFechaInicio());
        paquete1.setFechaFin(nuevaFechaInicio.minusDays(1));
        paquete1.setPrecio(paqueteOriginal.getPrecio());
        paquete1.setTipoAlquiler(paqueteOriginal.getTipoAlquiler());
        paquete1.setCasaRural(paqueteOriginal.getCasaRural());

        // Crear segundo paquete
        PaqueteAlquiler paquete2 = new PaqueteAlquiler();
        paquete2.setFechaInicio(nuevaFechaInicio);
        paquete2.setFechaFin(nuevaFechaFin);
        paquete2.setPrecio(paqueteOriginal.getPrecio());
        paquete2.setTipoAlquiler(paqueteOriginal.getTipoAlquiler());
        paquete2.setCasaRural(paqueteOriginal.getCasaRural());

        // Eliminar original y guardar nuevos
        paqueteRepository.delete(paqueteOriginal);

        List<PaqueteAlquilerResponse> nuevosPaquetes = new ArrayList<>();
        nuevosPaquetes.add(paqueteMapper.toResponse(paqueteRepository.save(paquete1)));
        nuevosPaquetes.add(paqueteMapper.toResponse(paqueteRepository.save(paquete2)));

        return nuevosPaquetes;
    }
}