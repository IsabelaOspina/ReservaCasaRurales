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

    @Column(name = "usuario",nullable = false, unique = true, length = 50)
    private String usuario;

    @Column(name = "contraseña",nullable = false)
    private String contraseña;

    @Column(name = "correo_electronico", nullable = false, unique = true, length = 100)
    private String correoElectronico;

    @Column(name = "telefono_contacto", length = 20)
    private String telefonoContacto;

    @Column(name = "numero_cuenta", length = 30)
    private String numeroCuenta;

    @OneToMany(mappedBy = "propietario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CasaRural> casas = new ArrayList<>();


}
