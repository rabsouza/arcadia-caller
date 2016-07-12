package br.com.battista.arcadia.caller.exception;

import java.io.Serializable;

public class AppException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;

    public AppException(String s) {
        super(s);
    }

    public AppException(String s, Throwable throwable) {
        super(s, throwable);
    }

}
