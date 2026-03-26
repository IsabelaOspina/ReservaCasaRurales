package org.example.reservacasarurales.Mapper;

import org.example.reservacasarurales.DTOs.Request.DormitorioRequest;
import org.example.reservacasarurales.DTOs.Response.DormitorioResponse;
import org.example.reservacasarurales.Entity.Dormitorio;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DormitorioMapper {
    Dormitorio toEntity(DormitorioRequest request);
    DormitorioResponse toResponse(Dormitorio dormitorio);
    List<DormitorioResponse> toResponseList(List<Dormitorio> dormitorios);

}
