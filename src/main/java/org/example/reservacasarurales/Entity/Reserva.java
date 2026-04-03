package org.example.reservacasarurales.Entity;

import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "reservas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")

    private Long id;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "noches", nullable = false)
    private int noches;

    @Column(name = "confirmada")
    private boolean confirmada;

    @Column(name = "fecha_limite_pago")
    private LocalDate fechaLimitePago;

    @Column(name = "telefono_contacto")
    private String telefonoContacto;

    @ManyToOne
    @JoinColumn(name = "id_casa")
    private CasaRural casaRural;

    @ManyToOne
    @JoinColumn(name = "id_paquete")
    private PaqueteAlquiler paquete;

    @ManyToMany
    @JoinTable(
        name = "reserva_dormitorio",
        joinColumns = @JoinColumn(name = "id_reserva"),
        inverseJoinColumns = @JoinColumn(name = "id_dormitorio")
    )
    private List<Dormitorio> dormitorios;
    
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDate fechaCreacion;
    
    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;


    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL)
    private List<Pago> pagos;


    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoReserva estado;



}
