package org.example.reservacasarurales.DTOs.Response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PagoInfoResponse {

    @JsonProperty("Total a pagar")
    private double total;

    @JsonProperty("Anticipo necesario para reservar la casa")
    private double anticipo;

    @JsonProperty("Saldo restante por pagar")
    private double restante;
    private String numeroCuenta;
    private String banco;
}