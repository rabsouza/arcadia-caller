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
import br.com.battista.arcadia.caller.model.Card;
import br.com.battista.arcadia.caller.service.AuthenticationService;
import br.com.battista.arcadia.caller.service.CardService;
import br.com.battista.arcadia.caller.service.MessageCustomerService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("api/v1/card")
public class CardController {

    @Autowired
    private CardService cardService;

    @Autowired
    private MessageCustomerService messageSource;

    @Autowired
    private AuthenticationService authenticationService;

    @RequestMapping(value = "/", method = RequestMethod.GET,
            produces = RestControllerConstant.PRODUCES)
    @ResponseBody
    public ResponseEntity<List<Card>> getAll(@RequestHeader("token") String token) throws AuthenticationException {
        authenticationService.authetication(token, APP, ADMIN);

        log.info("Retrieve all cards!");
        List<Card> cards = cardService.getAllCards();

        if (cards == null || cards.isEmpty()) {
            log.warn("No cards found!");
            return buildResponseErro(HttpStatus.NO_CONTENT);
        } else {
            log.info("Found {} cards!", cards.size());
            return buildResponseSuccess(cards, HttpStatus.OK, ENABLE_CACHED_ACTION);
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.POST,
            produces = RestControllerConstant.PRODUCES, consumes = RestControllerConstant.CONSUMES)
    @ResponseBody
    public ResponseEntity<Card> save(@RequestHeader("token") String token, @RequestBody Card card) throws AuthenticationException {
        authenticationService.authetication(token, ADMIN);

        if (card == null) {
            log.warn("Card can not be null!");
            return buildResponseErro(messageSource.getMessage(MessagePropertiesConstant.MESSAGE_FIELD_IS_REQUIRED, "Card"));
        }

        log.info("Save the card[{}]!", card);
        Card newCard = cardService.saveCard(card);
        log.debug("Save the card and generate to id: {}!", newCard.getId());
        return buildResponseSuccess(newCard, HttpStatus.CREATED);
    }

}
