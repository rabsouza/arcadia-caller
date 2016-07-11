package br.com.battista.arcadia.caller.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.appengine.repackaged.com.google.api.client.util.Strings;
import com.googlecode.objectify.Objectify;

import br.com.battista.arcadia.caller.exception.RepositoryException;
import br.com.battista.arcadia.caller.model.Guild;
import br.com.battista.arcadia.caller.validator.EntityValidator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class GuildRepository {

    @Autowired
    private EntityValidator entityValidator;

    @Autowired
    private Objectify objectifyRepository;

    public List<Guild> findAll() {
        log.info("Find all guilds!");

        return objectifyRepository.load()
                       .type(Guild.class)
                       .order("-updatedAt")
                       .list();

    }

    public Guild findByName(String name) {
        if (Strings.isNullOrEmpty(name)) {
            throw new RepositoryException("Name can not be null!");
        }
        log.info("Find guild by name: {}!", name);

        return objectifyRepository
                       .load()
                       .type(Guild.class)
                       .filter("name", name.toUpperCase())
                       .first()
                       .now();

    }

    public Guild findByMail(String mail) {
        if (Strings.isNullOrEmpty(mail)) {
            throw new RepositoryException("Mail can not be null!");
        }
        log.info("Find guild by mail: {}!", mail);

        return objectifyRepository
                       .load()
                       .type(Guild.class)
                       .filter("user.mail", mail)
                       .first()
                       .now();

    }

    public Guild saveOrUpdateGuild(Guild guild) {
        if (guild == null) {
            throw new RepositoryException("Guild entity can not be null!");
        }
        entityValidator.validate(guild);

        guild.initEntity();
        log.info("Save to guild: {}!", guild);

        objectifyRepository.save()
                .entity(guild)
                .now();

        return guild;
    }

}
