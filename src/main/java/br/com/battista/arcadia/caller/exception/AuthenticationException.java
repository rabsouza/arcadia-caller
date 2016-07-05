package br.com.battista.arcadia.caller.exception;

public class AuthenticationException extends Exception {

    public AuthenticationException(String s) {
        super(s);
    }

    public AuthenticationException(String s, Throwable throwable) {
        super(s, throwable);
    }

}
