package org.example.reservacasarurales.Exception;

import java.util.List;

public class InvalidDataException extends RuntimeException {
    private List<String> errors;

    public InvalidDataException(String message) {
        super(message);
    }

    public InvalidDataException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }



}
