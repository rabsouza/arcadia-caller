package br.com.battista.arcadia.caller.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.googlecode.objectify.Objectify;

import br.com.battista.arcadia.caller.exception.RepositoryException;
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
            objectifyRepository.save()
                            .entity(heroGuild)
                            .now();
        } else {
            heroGuild.updateEntity();
            log.info("Update the heroGuild: {}!", heroGuild);
            objectifyRepository.save()
                            .entity(heroGuild)
                            .now();
        }

        return heroGuild;
    }

}
