package org.example.reservacasarurales.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "propietarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Propietario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_propietario")
    private Long idPropietario;

    @Column(name = "nombre_propietario",nullable = false, length = 100)
    private String nombre;

    @Column(name = "numero_cuenta", length = 30)
    private String numeroCuenta;

    @Column(name = "banco", length = 50)
    private String banco;

    @OneToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private Usuario usuario;

    @OneToMany(mappedBy = "propietario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CasaRural> casas = new ArrayList<>();


}
