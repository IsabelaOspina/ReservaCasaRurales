package org.example.reservacasarurales.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fotos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Foto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_foto")
    private Long idFoto;

    @Column(nullable = false)
    private String url; // ruta en servidor o almacenamiento

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "id_casa", nullable = false)
    private CasaRural casaRural;
}
