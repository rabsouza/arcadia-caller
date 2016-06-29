package br.com.battista.arcadia.caller.builder;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.google.appengine.repackaged.com.google.api.client.util.Maps;

import br.com.battista.arcadia.caller.constants.RestControllerConstant;
import br.com.battista.arcadia.caller.exception.ValidatorException;

public class ResponseEntityBuilder {

    private ResponseEntityBuilder() {
    }

    public static ResponseEntity buildResponseSuccess(Object body, HttpStatus httpStatus) {
        return new ResponseEntity(body, httpStatus);
    }

    public static ResponseEntity buildResponseSuccess(HttpStatus httpStatus) {
        return new ResponseEntity(httpStatus);
    }

    public static ResponseEntity buildResponseErro(HttpStatus httpStatus) {
        return new ResponseEntity(httpStatus);
    }

    public static ResponseEntity buildResponseErro(String cause) {
        Map<String, String> body = new HashMap<String, String>();
        body.put(RestControllerConstant.BODY_ERROR, cause);
        return new ResponseEntity(body, HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity buildResponseErro(Throwable cause, HttpStatus httpStatus) {
        if (cause == null) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Map<String, String> body = new HashMap<String, String>();
        body.put(RestControllerConstant.BODY_ERROR, cause.getLocalizedMessage());
        return new ResponseEntity(body, httpStatus);
    }

    public static ResponseEntity buildResponseErro(ValidatorException cause, String[] messages, HttpStatus httpStatus) {
        if (cause == null) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Map<String, Object> body = Maps.newHashMap();
        body.put(RestControllerConstant.BODY_ERROR, cause.getLocalizedMessage());

        Map<String, String> details = Maps.newLinkedHashMap();
        for (String message : messages) {
            details.put(RestControllerConstant.DETAIL_ERROR, message);
        }
        body.put(RestControllerConstant.DETAIL, details);
        return new ResponseEntity(body, httpStatus);
    }

}
