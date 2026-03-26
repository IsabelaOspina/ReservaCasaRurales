package org.example.reservacasarurales.Exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) // devuelve 400 en vez de 500
public class MaxDormitoriosException extends RuntimeException {
    public MaxDormitoriosException(int max) {
        super("No se pueden registrar más dormitorios de los permitidos (" + max + ")");
    }
}
