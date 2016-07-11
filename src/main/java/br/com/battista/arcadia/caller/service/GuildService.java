package br.com.battista.arcadia.caller.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.battista.arcadia.caller.model.Guild;
import br.com.battista.arcadia.caller.repository.GuildRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GuildService {

    @Autowired
    private GuildRepository guildRepository;

    public List<Guild> getAllGuilds() {
        log.info("Find all guilds!");
        return guildRepository.findAll();
    }

    public Guild getGuildByName(String name) {
        log.info("Find the guild by name: {}!", name);
        return guildRepository.findByName(name);
    }

    public Guild getGuildByMail(String mail) {
        log.info("Find the guild by mail: {}!", mail);
        return guildRepository.findByMail(mail);
    }

    public Guild saveGuild(Guild guild) {
        log.info("Create new guild!");
        return guildRepository.saveOrUpdateGuild(guild);
    }

}
