package br.com.battista.arcadia.caller.controller;

import static br.com.battista.arcadia.caller.builder.ResponseEntityBuilder.buildResponseErro;
import static br.com.battista.arcadia.caller.builder.ResponseEntityBuilder.buildResponseSuccess;
import static br.com.battista.arcadia.caller.constants.ProfileAppConstant.ADMIN;
import static br.com.battista.arcadia.caller.constants.ProfileAppConstant.APP;
import static br.com.battista.arcadia.caller.constants.RestControllerConstant.ENABLE_CACHED_ACTION;

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
import br.com.battista.arcadia.caller.model.Hero;
import br.com.battista.arcadia.caller.service.AuthenticationService;
import br.com.battista.arcadia.caller.service.HeroService;
import br.com.battista.arcadia.caller.service.MessageCustomerService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("api/v1/hero")
public class HeroController {

    @Autowired
    private HeroService heroService;

    @Autowired
    private MessageCustomerService messageSource;

    @Autowired
    private AuthenticationService authenticationService;

    @RequestMapping(value = "/", method = RequestMethod.GET,
            produces = RestControllerConstant.PRODUCES)
    @ResponseBody
    public ResponseEntity<List<Hero>> getAll(@RequestHeader("token") String token) throws AuthenticationException {
        authenticationService.authetication(token, APP, ADMIN);

        log.info("Retrieve all heroes!");
        List<Hero> heroes = heroService.getAllHeroes();

        if (heroes == null || heroes.isEmpty()) {
            log.warn("No heroes found!");
            return buildResponseErro(HttpStatus.NO_CONTENT);
        } else {
            log.info("Found {} heroes!", heroes.size());
            return buildResponseSuccess(heroes, HttpStatus.OK, ENABLE_CACHED_ACTION);
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.POST,
            produces = RestControllerConstant.PRODUCES, consumes = RestControllerConstant.CONSUMES)
    @ResponseBody
    public ResponseEntity<Hero> save(@RequestHeader("token") String token, @RequestBody Hero hero) throws AuthenticationException {
        authenticationService.authetication(token, ADMIN);

        if (hero == null) {
            log.warn("Hero can not be null!");
            return buildResponseErro(messageSource.getMessage(MessagePropertiesConstant.MESSAGE_FIELD_IS_REQUIRED, "Hero"));
        }

        log.info("Save the hero[{}]!", hero);
        Hero newHero = heroService.saveHero(hero);
        log.debug("Save the hero and generate to id: {}!", newHero.getId());
        return buildResponseSuccess(newHero, HttpStatus.CREATED);
    }

}
