package org.example.reservacasarurales.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pagos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Long idPago;

    @Column(name = "monto", nullable = false)
    private double monto;

    @Column(name = "metodo_pago", nullable = false)
    private String metodoPago;

    @Column(name = "fecha_pago", nullable = false)
    private String fechaPago;

    @Column(name = "confirmado", nullable = false)
    private boolean confirmado;

    //falta relacion con reserva
    
}


