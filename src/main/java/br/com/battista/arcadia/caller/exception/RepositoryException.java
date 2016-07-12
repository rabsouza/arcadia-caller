package br.com.battista.arcadia.caller.exception;

import java.io.Serializable;

public class RepositoryException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;

    public RepositoryException(String s) {
        super(s);
    }

    public RepositoryException(String s, Throwable throwable) {
        super(s, throwable);
    }

}
