package br.com.battista.arcadia.caller.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.battista.arcadia.caller.constants.RestControllerConstant;

public class BaseController {

    public ResponseEntity buildResponseSuccess(Object body, HttpStatus httpStatus) {
        return new ResponseEntity(body, httpStatus);
    }

    public ResponseEntity buildResponseSuccess(HttpStatus httpStatus) {
        return new ResponseEntity(httpStatus);
    }

    public ResponseEntity buildResponseErro(HttpStatus httpStatus) {
        return new ResponseEntity(httpStatus);
    }

    public ResponseEntity buildResponseErro(String cause) {
        Map<String, String> body = new HashMap<String, String>();
        body.put(RestControllerConstant.BODY_ERRO, cause);
        return new ResponseEntity(body, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity buildResponseErro(Throwable cause, HttpStatus httpStatus) {
        if (cause == null) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Map<String, String> body = new HashMap<String, String>();
        body.put(RestControllerConstant.BODY_ERRO, cause.getLocalizedMessage());
        return new ResponseEntity(body, httpStatus);
    }

}
