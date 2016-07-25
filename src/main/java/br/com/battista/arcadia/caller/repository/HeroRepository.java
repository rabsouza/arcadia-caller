package br.com.battista.arcadia.caller.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.appengine.repackaged.com.google.api.client.util.Strings;
import com.googlecode.objectify.Objectify;

import br.com.battista.arcadia.caller.exception.RepositoryException;
import br.com.battista.arcadia.caller.model.Hero;
import br.com.battista.arcadia.caller.validator.EntityValidator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class HeroRepository {

    @Autowired
    private EntityValidator entityValidator;

    @Autowired
    private Objectify objectifyRepository;

    public List<Hero> findAll() {
        log.info("Find all heroes!");

        return objectifyRepository.load()
                       .type(Hero.class)
                       .order("-updatedAt")
                       .list();

    }

    public Hero findByName(String name) {
        if (Strings.isNullOrEmpty(name)) {
            throw new RepositoryException("Name can not be null!");
        }
        log.info("Find hero by name: {}!", name);

        return objectifyRepository
                       .load()
                       .type(Hero.class)
                       .filter("name", name)
                       .first()
                       .now();

    }

    public Hero saveOrUpdateHero(Hero hero) {
        if (hero == null) {
            throw new RepositoryException("Hero entity can not be null!");
        }
        entityValidator.validate(hero);

        hero.initEntity();
        log.info("Save the hero: {}!", hero);

        objectifyRepository.save()
                .entity(hero)
                .now();

        return hero;
    }

}
