package org.example.reservacasarurales;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ReservaCasaRuralesApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReservaCasaRuralesApplication.class, args);
    }

}
