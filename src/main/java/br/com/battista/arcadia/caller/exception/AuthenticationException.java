package br.com.battista.arcadia.caller.exception;

import java.io.Serializable;

public class AuthenticationException extends Exception implements Serializable {

    private static final long serialVersionUID = 1L;

    public AuthenticationException(String s) {
        super(s);
    }

    public AuthenticationException(String s, Throwable throwable) {
        super(s, throwable);
    }

}
