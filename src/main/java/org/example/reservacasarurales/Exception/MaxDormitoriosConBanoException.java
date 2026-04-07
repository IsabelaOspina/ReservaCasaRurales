package org.example.reservacasarurales.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MaxDormitoriosConBanoException extends RuntimeException{

    public MaxDormitoriosConBanoException(int maxBanos) {
        super("No se pueden crear más dormitorios con baño. La casa solo tiene "
                + maxBanos + " baños registrados.");
    }
}