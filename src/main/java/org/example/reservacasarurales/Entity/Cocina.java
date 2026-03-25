package org.example.reservacasarurales.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cocinas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cocina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cocina")
    private Long idCocina;

    @Column(name = "tiene_lavavajillas")
    private boolean tieneLavavajillas;

    @Column(name = "tiene_lavadora")
    private boolean tieneLavadora;

    @ManyToOne
    @JoinColumn(name = "id_casa", nullable = false)
    private CasaRural casaRural;
}
