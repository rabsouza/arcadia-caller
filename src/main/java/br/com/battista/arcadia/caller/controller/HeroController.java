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

import br.com.battista.arcadia.caller.constants.RestControllerConstant;
import br.com.battista.arcadia.caller.exception.AuthenticationException;
import br.com.battista.arcadia.caller.model.Hero;
import br.com.battista.arcadia.caller.service.AuthenticationService;
import br.com.battista.arcadia.caller.service.HeroService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("api/v1/hero")
public class HeroController {

    @Autowired
    private HeroService heroService;

    @Autowired
    private AuthenticationService authenticationService;

    @RequestMapping(value = "/", method = RequestMethod.GET,
            produces = RestControllerConstant.PRODUCES)
    @ResponseBody
    public ResponseEntity<List<Hero>> getAll(@RequestHeader("token") String token) throws AuthenticationException {
        authenticationService.authetication(token);

        log.info("Retrieve all heros!");
        List<Hero> heros = heroService.getAllHeroes();

        if (heros == null || heros.isEmpty()) {
            log.warn("No heros found!");
            return buildResponseErro(HttpStatus.NO_CONTENT);
        } else {
            log.info("Found {} heros!", heros.size());
            return buildResponseSuccess(heros, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.POST,
            produces = RestControllerConstant.PRODUCES, consumes = RestControllerConstant.CONSUMES)
    @ResponseBody
    public ResponseEntity<Hero> save(@RequestHeader("token") String token, @RequestBody Hero hero) throws AuthenticationException {
        authenticationService.authetication(token);

        if (hero == null) {
            log.warn("Hero can not be null!");
            return buildResponseErro("Hero is required!");
        }

        log.info("Save the hero[{}]!", hero);
        Hero newHero = heroService.saveHero(hero);
        log.debug("Save the hero and generate to id: {}!", newHero.getId());
        return buildResponseSuccess(newHero, HttpStatus.OK);
    }

}