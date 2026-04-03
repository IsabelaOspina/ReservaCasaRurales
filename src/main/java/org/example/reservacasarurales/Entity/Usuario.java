package org.example.reservacasarurales.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "usuario",nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "contraseña",nullable = false)
    private String password;

    @Column(name = "correo_electronico", nullable = false, unique = true, length = 100)
    private String correoElectronico;

    @Enumerated(EnumType.STRING)
    private Rol rol;

}
