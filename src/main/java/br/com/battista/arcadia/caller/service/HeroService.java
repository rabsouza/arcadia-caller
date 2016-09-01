package br.com.battista.arcadia.caller.service;


import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.battista.arcadia.caller.model.Hero;
import br.com.battista.arcadia.caller.repository.HeroRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HeroService {

    @Autowired
    private HeroRepository heroRepository;

    public List<Hero> getAllHeroes(Locale locale) {
        log.info("Find all heroes!");
        return heroRepository.findAll(locale);
    }

    public Hero getHeroByName(String name) {
        log.info("Find the hero by name: {}!", name);
        return heroRepository.findByName(name);
    }

    public Hero saveHero(Hero hero) {
        log.info("Create new hero!");
        return heroRepository.saveOrUpdateHero(hero);
    }

}
