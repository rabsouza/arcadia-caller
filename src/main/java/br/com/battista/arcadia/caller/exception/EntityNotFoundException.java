package br.com.battista.arcadia.caller.exception;

import java.io.Serializable;

public class EntityNotFoundException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;

    public EntityNotFoundException(String s) {
        super(s);
    }

    public EntityNotFoundException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public EntityNotFoundException(Throwable throwable) {
        super(throwable);
    }
}
