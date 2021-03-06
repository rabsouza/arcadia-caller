package br.com.battista.arcadia.caller.repository;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.appengine.repackaged.com.google.api.client.util.Strings;
import com.googlecode.objectify.Objectify;

import br.com.battista.arcadia.caller.exception.RepositoryException;
import br.com.battista.arcadia.caller.model.BaseEntity;
import br.com.battista.arcadia.caller.model.Card;
import br.com.battista.arcadia.caller.model.Scenery;
import br.com.battista.arcadia.caller.model.enuns.LocationSceneryEnum;
import br.com.battista.arcadia.caller.validator.EntityValidator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class SceneryRepository {

    private static final String FIELD_LOCATION = "location";

    @Autowired
    private EntityValidator entityValidator;

    @Autowired
    private Objectify objectifyRepository;

    @Autowired
    private CardRepository cardRepository;

    public List<Scenery> findAll(Locale locale) {
        log.info("Find all sceneries!");

        return objectifyRepository.load()
                       .type(Scenery.class)
                       //.filter("locale", locale == null ? null : locale.getLanguage())
                       .order(FIELD_LOCATION)
                       .order("name")
                       .list();

    }

    public List<Scenery> findByLocation(LocationSceneryEnum locationScenery, Locale locale) {
        log.info("Find all sceneries!");

        return objectifyRepository.load()
                       .type(Scenery.class)
                       .filter(FIELD_LOCATION, locationScenery)
                       //.filter("locale", locale == null ? null : locale.getLanguage())
                       .order(FIELD_LOCATION)
                       .order("name")
                       .list();

    }

    public Scenery findByName(String name) {
        if (Strings.isNullOrEmpty(name)) {
            throw new RepositoryException("Name can not be null!");
        }
        log.info("Find scenery by name: {}!", name);

        return objectifyRepository
                       .load()
                       .type(Scenery.class)
                       .filter("name", name)
                       .first()
                       .now();

    }

    public Scenery saveOrUpdateScenery(Scenery scenery) {
        if (scenery == null) {
            throw new RepositoryException("Scenery entity can not be null!");
        }
        entityValidator.validate(scenery);

        scenery.initEntity();
        Card wonReward = scenery.getWonReward();

        if (wonReward != null) {
            cardRepository.saveOrUpdateCard(wonReward);
        }
        log.info("Save the scenery: {}!", scenery);

        saveEntity(scenery);

        return scenery;
    }

    private void saveEntity(BaseEntity entity) {
        objectifyRepository.save()
                .entity(entity)
                .now();
    }

}
