package br.com.battista.arcadia.caller.controller;

import static br.com.battista.arcadia.caller.builder.ResponseEntityBuilder.buildResponseErro;
import static br.com.battista.arcadia.caller.builder.ResponseEntityBuilder.buildResponseSuccess;
import static br.com.battista.arcadia.caller.constants.ProfileAppConstant.ADMIN;
import static br.com.battista.arcadia.caller.constants.ProfileAppConstant.APP;
import static br.com.battista.arcadia.caller.constants.RestControllerConstant.ENABLE_CACHED_ACTION;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.battista.arcadia.caller.constants.MessagePropertiesConstant;
import br.com.battista.arcadia.caller.constants.RestControllerConstant;
import br.com.battista.arcadia.caller.exception.AuthenticationException;
import br.com.battista.arcadia.caller.model.Scenery;
import br.com.battista.arcadia.caller.model.enuns.LocationSceneryEnum;
import br.com.battista.arcadia.caller.service.AuthenticationService;
import br.com.battista.arcadia.caller.service.LocaleService;
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

    @Autowired
    private LocaleService localeService;

    @RequestMapping(value = "/", method = RequestMethod.GET,
            produces = RestControllerConstant.PRODUCES)
    @ResponseBody
    public ResponseEntity<List<Scenery>> getAll(@RequestHeader("token") String token, @RequestHeader("locale") String localeStr) throws AuthenticationException {
        authenticationService.authetication(token, APP, ADMIN);

        log.info("Retrieve all sceneries!");
        Locale locale = localeService.processSupportedLocales(localeStr);
        List<Scenery> sceneries = sceneryService.getAllSceneries(locale);

        if (sceneries == null || sceneries.isEmpty()) {
            log.warn("No sceneries found!");
            return buildResponseErro(HttpStatus.NO_CONTENT);
        } else {
            log.info("Found {} sceneries!", sceneries.size());
            return buildResponseSuccess(sceneries, HttpStatus.OK, ENABLE_CACHED_ACTION);
        }
    }

    @RequestMapping(value = "/location/{location:.+}", method = RequestMethod.GET,
            produces = RestControllerConstant.PRODUCES)
    @ResponseBody
    public ResponseEntity<List<Scenery>> getByLocation(@RequestHeader("token") String token, @RequestHeader("locale") String localeStr, @PathVariable("location") LocationSceneryEnum locationScenery)
            throws AuthenticationException {
        authenticationService.authetication(token, APP, ADMIN);

        log.info("Retrieve sceneries by localtion: {}!", locationScenery);
        Locale locale = localeService.processSupportedLocales(localeStr);
        List<Scenery> sceneries = sceneryService.getByLocation(locationScenery, locale);

        if (sceneries == null || sceneries.isEmpty()) {
            log.warn("No sceneries found!");
            return buildResponseErro(HttpStatus.NO_CONTENT);
        } else {
            log.info("Found {} sceneries!", sceneries.size());
            return buildResponseSuccess(sceneries, HttpStatus.OK, ENABLE_CACHED_ACTION);
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.POST,
            produces = RestControllerConstant.PRODUCES, consumes = RestControllerConstant.CONSUMES)
    @ResponseBody
    public ResponseEntity<Scenery> save(@RequestHeader("token") String token, @RequestBody Scenery scenery) throws AuthenticationException {
        authenticationService.authetication(token, ADMIN);

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
