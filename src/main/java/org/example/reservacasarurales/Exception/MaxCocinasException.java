package org.example.reservacasarurales.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MaxCocinasException extends RuntimeException {
    public MaxCocinasException(int max) {
        super("No se pueden registrar más cocinas de las permitidas (" + max + ")");
    }
}
