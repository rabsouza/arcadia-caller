package br.com.battista.arcadia.caller.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.appengine.repackaged.com.google.api.client.util.Strings;
import com.googlecode.objectify.Objectify;

import br.com.battista.arcadia.caller.exception.RepositoryException;
import br.com.battista.arcadia.caller.model.Card;
import br.com.battista.arcadia.caller.validator.EntityValidator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class CardRepository {

    @Autowired
    private EntityValidator entityValidator;

    @Autowired
    private Objectify objectifyRepository;

    public List<Card> findAll() {
        log.info("Find all card!");

        return objectifyRepository.load()
                       .type(Card.class)
                       .order("-updatedAt")
                       .list();

    }

    public Card findByName(String name) {
        if (Strings.isNullOrEmpty(name)) {
            throw new RepositoryException("Name can not be null!");
        }
        log.info("Find card by name: {}!", name);

        return objectifyRepository
                       .load()
                       .type(Card.class)
                       .filter("name", name)
                       .first()
                       .now();

    }

    public Card saveOrUpdateCard(Card card) {
        if (card == null) {
            throw new RepositoryException("Card entity can not be null!");
        }
        entityValidator.validate(card);

        card.initEntity();
        log.info("Save to card: {}!", card);

        objectifyRepository.save()
                .entity(card)
                .now();

        return card;
    }

}
