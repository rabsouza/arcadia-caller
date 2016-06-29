package br.com.battista.arcadia.caller.controller;

import static br.com.battista.arcadia.caller.builder.ResponseEntityBuilder.buildResponseErro;

import java.util.Map;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.battista.arcadia.caller.exception.RepositoryException;
import br.com.battista.arcadia.caller.exception.ValidatorException;
import lombok.extern.slf4j.Slf4j;


@ControllerAdvice
@Slf4j
public class AppControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleUncaughtException(Exception e) {
        log.error(e.getLocalizedMessage(), e);
        return buildResponseErro(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RepositoryException.class)
    public ResponseEntity<Map<String, Object>> handleRepositoryException(RepositoryException e) {
        log.error(e.getLocalizedMessage(), e);
        return buildResponseErro(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolationException(ConstraintViolationException e) {
        log.error(e.getLocalizedMessage(), e);
        return buildResponseErro(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error(e.getLocalizedMessage(), e);
        return buildResponseErro(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidatorException.class)
    public ResponseEntity<Map<String, Object>> handleValidatorException(ValidatorException e) {
        log.error(e.getLocalizedMessage(), e);
        return buildResponseErro(e, e.builderMessagesValidator(), HttpStatus.BAD_REQUEST);
    }

}
