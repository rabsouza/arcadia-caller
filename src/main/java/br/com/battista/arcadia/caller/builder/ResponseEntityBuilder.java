package br.com.battista.arcadia.caller.builder;

import static br.com.battista.arcadia.caller.constants.CacheConstant.HEADER_CACHE_CONTROL_MAX_AGE_VALUE;
import static br.com.battista.arcadia.caller.constants.CacheConstant.HEADER_NO_CACHE_CONTROL;
import static com.google.common.net.HttpHeaders.CACHE_CONTROL;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.google.appengine.repackaged.com.google.api.client.util.Maps;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;

import br.com.battista.arcadia.caller.constants.RestControllerConstant;
import br.com.battista.arcadia.caller.exception.ValidatorException;

public class ResponseEntityBuilder {

    private ResponseEntityBuilder() {
    }

    public static ResponseEntity buildResponseSuccess(Object body, HttpStatus httpStatus) {
        return buildResponseSuccess(body, httpStatus, false);
    }

    public static ResponseEntity buildResponseSuccess(Object body, HttpStatus httpStatus, Boolean cached) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        if (cached) {
            headers.put(CACHE_CONTROL, Lists.newArrayList(HEADER_CACHE_CONTROL_MAX_AGE_VALUE));
        } else {
            headers.put(CACHE_CONTROL, Lists.newArrayList(HEADER_NO_CACHE_CONTROL));
        }
        return new ResponseEntity(body, headers, httpStatus);
    }

    public static ResponseEntity buildResponseSuccess(HttpStatus httpStatus) {
        return new ResponseEntity(httpStatus);
    }

    public static ResponseEntity buildResponseErro(HttpStatus httpStatus) {
        return new ResponseEntity(httpStatus);
    }

    public static ResponseEntity buildResponseErro(String cause) {
        Map<String, String> body = new HashMap<>();
        body.put(RestControllerConstant.BODY_ERROR, cause);
        return new ResponseEntity(body, HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity buildResponseErro(Throwable cause, HttpStatus httpStatus) {
        if (cause == null) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Map<String, String> body = new HashMap<>();
        body.put(RestControllerConstant.BODY_ERROR, Throwables.getRootCause(cause).getLocalizedMessage());
        return new ResponseEntity(body, httpStatus);
    }

    public static ResponseEntity buildResponseErro(ValidatorException cause, String[] messages, HttpStatus httpStatus) {
        if (cause == null) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Map<String, Object> body = Maps.newHashMap();
        body.put(RestControllerConstant.BODY_ERROR, Throwables.getRootCause(cause).getLocalizedMessage());

        Map<String, String> details = Maps.newLinkedHashMap();
        for (String message : messages) {
            details.put(RestControllerConstant.DETAIL_ERROR, message);
        }
        body.put(RestControllerConstant.DETAIL, details);
        return new ResponseEntity(body, httpStatus);
    }

}
