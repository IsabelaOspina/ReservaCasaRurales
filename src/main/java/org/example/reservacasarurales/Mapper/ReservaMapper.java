package org.example.reservacasarurales.Mapper;


import org.example.reservacasarurales.DTOs.Request.ReservaRequest;
import org.example.reservacasarurales.DTOs.Response.ReservaResponse;
import org.example.reservacasarurales.Entity.CasaRural;
import org.example.reservacasarurales.Entity.PaqueteAlquiler;
import org.example.reservacasarurales.Entity.Reserva;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public class ReservaMapper {

    public Reserva toEntity(ReservaRequest request, CasaRural casa, PaqueteAlquiler paquete) {
        Reserva r = new Reserva();
        r.setFechaInicio(request.getFechaInicio());
        r.setNoches(request.getNoches());
        r.setCasaRural(casa);
        r.setPaquete(paquete);
        return r;
    }

    public ReservaResponse toResponse(Reserva r) {
        ReservaResponse res = new ReservaResponse();

        res.setId(r.getId());
        res.setFechaInicio(r.getFechaInicio());
        res.setFechaFin(r.getFechaFin()); 
        res.setNoches(r.getNoches());
        res.setEstado(r.getEstado().name());

        //  HU017
        res.setFechaLimitePago(r.getFechaLimitePago());

        
        res.setFechaCreacion(r.getFechaCreacion());

        if (r.getCasaRural() != null) {
            res.setCasaId(r.getCasaRural().getCodigoCasa());
        }

        if (r.getPaquete() != null) {
            res.setPaqueteId(r.getPaquete().getIdPaquete());
        }

        return res;
    }
}

