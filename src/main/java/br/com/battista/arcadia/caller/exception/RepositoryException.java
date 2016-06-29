package br.com.battista.arcadia.caller.exception;

public class RepositoryException extends RuntimeException {

    public RepositoryException() {
    }

    public RepositoryException(String s) {
        super(s);
    }

    public RepositoryException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public RepositoryException(Throwable throwable) {
        super(throwable);
    }

}
