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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaqueteAlquilerService {

    private final PaqueteRepository paqueteRepository;
    private final CasaRuralRepository casaRepository;
    private final PaqueteMapper paqueteMapper;
    private final PropietarioRepository propietarioRepository;

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
}