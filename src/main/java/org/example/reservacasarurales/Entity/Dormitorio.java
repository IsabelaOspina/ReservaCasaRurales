package org.example.reservacasarurales.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dormitorios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dormitorio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dormitorio")
    private Long idDormitorio;

    @Column(name = "num_camas")
    private int numeroCamas;

    @Column(name = "nombre")
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cama", nullable = false)
    private TipoCama tipoCama;

    @Column(name = "tiene_bano")
    private boolean tieneBano;

    @ManyToOne
    @JoinColumn(name = "codigoCasa", nullable = false)
    private CasaRural casaRural;
}

