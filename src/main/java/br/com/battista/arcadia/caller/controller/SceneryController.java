package br.com.battista.arcadia.caller.controller;

import static br.com.battista.arcadia.caller.builder.ResponseEntityBuilder.buildResponseErro;
import static br.com.battista.arcadia.caller.builder.ResponseEntityBuilder.buildResponseSuccess;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.battista.arcadia.caller.constants.MessagePropertiesConstant;
import br.com.battista.arcadia.caller.constants.RestControllerConstant;
import br.com.battista.arcadia.caller.exception.AuthenticationException;
import br.com.battista.arcadia.caller.model.Scenery;
import br.com.battista.arcadia.caller.service.AuthenticationService;
import br.com.battista.arcadia.caller.service.MessageCustomerService;
import br.com.battista.arcadia.caller.service.SceneryService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("api/v1/scenery")
public class SceneryController {

    @Autowired
    private SceneryService sceneryService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private MessageCustomerService messageSource;

    @RequestMapping(value = "/", method = RequestMethod.GET,
            produces = RestControllerConstant.PRODUCES)
    @ResponseBody
    public ResponseEntity<List<Scenery>> getAll(@RequestHeader("token") String token) throws AuthenticationException {
        authenticationService.authetication(token);

        log.info("Retrieve all sceneries!");
        List<Scenery> sceneries = sceneryService.getAllSceneries();

        if (sceneries == null || sceneries.isEmpty()) {
            log.warn("No sceneries found!");
            return buildResponseErro(HttpStatus.NO_CONTENT);
        } else {
            log.info("Found {} sceneries!", sceneries.size());
            return buildResponseSuccess(sceneries, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.POST,
            produces = RestControllerConstant.PRODUCES, consumes = RestControllerConstant.CONSUMES)
    @ResponseBody
    public ResponseEntity<Scenery> save(@RequestHeader("token") String token, @RequestBody Scenery scenery) throws AuthenticationException {
        authenticationService.authetication(token);

        if (scenery == null) {
            log.warn("Scenery can not be null!");
            return buildResponseErro(messageSource.getMessage(MessagePropertiesConstant.MESSAGE_FIELD_IS_REQUIRED, "Scenery"));
        }

        log.info("Save the scenery[{}]!", scenery);
        Scenery newScenery = sceneryService.saveScenery(scenery);
        log.debug("Save the scenery and generate to id: {}!", newScenery.getId());
        return buildResponseSuccess(newScenery, HttpStatus.CREATED);
    }

}
