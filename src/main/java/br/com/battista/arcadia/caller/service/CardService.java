package br.com.battista.arcadia.caller.service;


import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.battista.arcadia.caller.model.Card;
import br.com.battista.arcadia.caller.repository.CardRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    public List<Card> getAllCards(Locale locale) {
        log.info("Find all cards!");
        return cardRepository.findAll(locale);
    }

    public Card getCardByName(String name) {
        log.info("Find the user by name: {}!", name);
        return cardRepository.findByName(name);
    }

    public Card saveCard(Card card) {
        log.info("Create new card!");
        return cardRepository.saveOrUpdateCard(card);
    }

}
