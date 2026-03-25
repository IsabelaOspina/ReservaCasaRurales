package org.example.reservacasarurales.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "paquetes_alquiler")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaqueteAlquiler {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paquete")
    private Long idPaquete;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Column(nullable = false)
    private double precio;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_alquiler", nullable = false)
    private TipoAlquiler tipoAlquiler;

    @ManyToOne
    @JoinColumn(name = "id_casa", nullable = false)
    private CasaRural casaRural;

}
