package org.example.reservacasarurales.Mapper;

import org.example.reservacasarurales.DTOs.Request.FotoRequest;
import org.example.reservacasarurales.DTOs.Response.FotoResponse;
import org.example.reservacasarurales.Entity.Foto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FotoMapper {
    Foto toEntity(FotoRequest request);

    List<FotoResponse> toResponseList(List<Foto> fotos);
    // Convierte un String (url) en Foto
    default Foto toEntity(String url) {
        Foto foto = new Foto();
        foto.setUrl(url);
        return foto;
    }

    // Convierte Foto en String (solo url)
    default String toResponse(Foto foto) {
        return foto.getUrl();
    }

}
