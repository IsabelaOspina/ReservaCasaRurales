package org.example.reservacasarurales.Service;

import org.example.reservacasarurales.DTOs.Request.CasaRuralRequest;
import org.example.reservacasarurales.DTOs.Response.CasaRuralResponse;
import org.example.reservacasarurales.Entity.*;
import org.example.reservacasarurales.Mapper.CasaRuralMapper;
import org.example.reservacasarurales.Repository.CasaRuralRepository;
import org.example.reservacasarurales.Repository.FotoRepository;
import org.example.reservacasarurales.Repository.PropietarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CasaRuralService {

    @Autowired
    private CasaRuralRepository casaRuralRepository;

    @Autowired
    private PropietarioRepository propietarioRepository;

    @Autowired
    private FotoRepository fotoRepository;

    @Autowired
    private CasaRuralMapper casaRuralMapper;

    //HU003
    @PreAuthorize("hasRole('PROPIETARIO')")
    public CasaRuralResponse registrarCasaRural(CasaRuralRequest casaRuralRequest) throws IOException {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        Usuario usuario = (Usuario) authentication.getPrincipal();

        Propietario propietario = propietarioRepository
                .findByUsuarioCorreoElectronico(usuario.getCorreoElectronico())
                .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));

        if (casaRuralRequest.getNumeroCocinas() < 1) {
            throw new IllegalArgumentException("La casa debe tener al menos 1 cocina");
        }
        if (casaRuralRequest.getNumeroDormitorios() < 3) {
            throw new IllegalArgumentException("La casa debe tener al menos 3 dormitorios");
        }
        if (casaRuralRequest.getNumeroBanos() < 2) {
            throw new IllegalArgumentException("La casa debe tener al menos 2 baños");
        }

        CasaRural casa = casaRuralMapper.toEntity(casaRuralRequest);

        if (casa.getFotos() == null) {
            casa.setFotos(new ArrayList<>());
        }

        casa.setPropietario(propietario);

        CasaRural savedCasa = casaRuralRepository.save(casa);

        String rutaUpload = "uploads/";

        if (casaRuralRequest.getFotos() != null) {

            for (int i = 0; i < casaRuralRequest.getFotos().size(); i++) {

                MultipartFile archivo = casaRuralRequest.getFotos().get(i);

                String nombreArchivo = System.currentTimeMillis() + "_" + archivo.getOriginalFilename();

                Path ruta = Paths.get(rutaUpload + nombreArchivo);

                Files.createDirectories(ruta.getParent());
                Files.write(ruta, archivo.getBytes());

                Foto foto = new Foto();
                foto.setUrl("/uploads/" + nombreArchivo);

                if (casaRuralRequest.getDescripcionesFotos() != null &&
                        i < casaRuralRequest.getDescripcionesFotos().size()) {

                    foto.setDescripcion(casaRuralRequest.getDescripcionesFotos().get(i));
                }

                foto.setCasaRural(savedCasa);

                fotoRepository.save(foto);

                savedCasa.getFotos().add(foto);
            }
        }

        return casaRuralMapper.toResponse(savedCasa);
    }

    @PreAuthorize("hasRole('PROPIETARIO')")
    public void eliminarCasa(Long codigoCasa) {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        Usuario usuario = (Usuario) authentication.getPrincipal();

        Propietario propietario = propietarioRepository
                .findByUsuarioCorreoElectronico(usuario.getCorreoElectronico())
                .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));

        if (!casaRuralRepository
                .existsByCodigoCasaAndPropietarioIdPropietario(
                        codigoCasa,
                        propietario.getIdPropietario()
                )) {
            throw new RuntimeException("No tienes permisos para eliminar esta casa o no existe");
        }

        casaRuralRepository.deleteById(codigoCasa);
    }

    // ===== MÉTODOS AGREGADOS =====
    public CasaRuralResponse obtenerCasaPorId(Long codigoCasa) {
        CasaRural casa = casaRuralRepository.findByCodigoCasa(codigoCasa)
                .orElseThrow(() -> new RuntimeException("Casa rural no encontrada"));
        return casaRuralMapper.toResponse(casa);
    }

    public List<CasaRuralResponse> listarCasas() {
        return casaRuralRepository.findAll()
                .stream()
                .map(casaRuralMapper::toResponse)
                .toList();
    }

}