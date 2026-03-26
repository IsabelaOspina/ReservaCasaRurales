package org.example.reservacasarurales.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "casas_rurales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CasaRural {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo_casa")
    private Long codigoCasa;

    @Column(nullable = false, length = 100)
    private String poblacion;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "num_dormitorios")
    private int numeroDormitorios;

    @Column(name = "num_banos")
    private int numeroBanos;

    @Column(name = "num_cocinas")
    private int numeroCocinas;

    @Column(name = "num_comedores")
    private int numeroComedores;

    @Column(name = "plazas_garaje")
    private int plazasGaraje;

    @ManyToOne
    @JoinColumn(name = "propietario_id", nullable = false)
    private Propietario propietario;

    @OneToMany(mappedBy = "casaRural", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Dormitorio> dormitorios = new ArrayList<>();


    @OneToMany(mappedBy = "casaRural", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cocina> cocinas = new ArrayList<>();

    @OneToMany(mappedBy = "casaRural", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Foto> fotos = new ArrayList<>();

}
