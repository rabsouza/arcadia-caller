package br.com.battista.arcadia.caller.exception;

public class AppException extends RuntimeException {

    public AppException() {
    }

    public AppException(String s) {
        super(s);
    }

    public AppException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public AppException(Throwable throwable) {
        super(throwable);
    }

    public AppException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
