package br.com.battista.arcadia.caller.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.googlecode.objectify.Objectify;

import br.com.battista.arcadia.caller.exception.RepositoryException;
import br.com.battista.arcadia.caller.model.Card;
import br.com.battista.arcadia.caller.model.Hero;
import br.com.battista.arcadia.caller.model.HeroGuild;
import br.com.battista.arcadia.caller.validator.EntityValidator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class HeroGuildRepository {

    @Autowired
    private EntityValidator entityValidator;

    @Autowired
    private Objectify objectifyRepository;

    @Autowired
    private HeroRepository heroRepository;

    @Autowired
    private CardRepository cardRepository;

    public List<HeroGuild> findAll() {
        log.info("Find all heroGuilds!");

        return objectifyRepository.load()
                       .type(HeroGuild.class)
                       .order("-updatedAt")
                       .list();

    }

    public HeroGuild saveOrUpdateHeroGuild(HeroGuild heroGuild) {
        if (heroGuild == null) {
            throw new RepositoryException("HeroGuild entity can not be null!");
        }
        entityValidator.validate(heroGuild);

        if (heroGuild.getId() == null || heroGuild.getVersion() == null) {
            heroGuild.initEntity();
            log.info("Save the heroGuild: {}!", heroGuild);
            saveHeroGuild(heroGuild);
        } else {
            heroGuild.updateEntity();
            log.info("Update the heroGuild: {}!", heroGuild);
            saveHeroGuild(heroGuild);
        }

        return heroGuild;
    }

    private void saveHeroGuild(HeroGuild heroGuild) {
        Hero hero = heroGuild.getHero();
        if (hero != null) {
            Hero heroFind = heroRepository.findByName(hero.getName());
            heroGuild.setHero(heroFind);
        }

        saveAllCards(heroGuild);

        objectifyRepository.save()
                .entity(heroGuild)
                .now();
    }

    private void saveAllCards(HeroGuild heroGuild) {
        Card card = heroGuild.getCard1();
        if (card != null) {
            Card cardFind = cardRepository.findByName(card.getName());
            heroGuild.setCard1(cardFind);
        }

        card = heroGuild.getCard2();
        if (card != null) {
            Card cardFind = cardRepository.findByName(card.getName());
            heroGuild.setCard2(cardFind);
        }

        card = heroGuild.getCard3();
        if (card != null) {
            Card cardFind = cardRepository.findByName(card.getName());
            heroGuild.setCard3(cardFind);
        }

        card = heroGuild.getCard4();
        if (card != null) {
            Card cardFind = cardRepository.findByName(card.getName());
            heroGuild.setCard4(cardFind);
        }

        card = heroGuild.getCurseCard();
        if (card != null) {
            Card cardFind = cardRepository.findByName(card.getName());
            heroGuild.setCurseCard(cardFind);
        }
    }

}
