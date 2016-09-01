package br.com.battista.arcadia.caller.service;


import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.battista.arcadia.caller.model.Scenery;
import br.com.battista.arcadia.caller.model.enuns.LocationSceneryEnum;
import br.com.battista.arcadia.caller.repository.SceneryRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SceneryService {

    @Autowired
    private SceneryRepository sceneryRepository;

    public List<Scenery> getAllSceneries(Locale locale) {
        log.info("Find all sceneries!");
        return sceneryRepository.findAll(locale);
    }

    public List<Scenery> getByLocation(LocationSceneryEnum locationScenery, Locale locale) {
        log.info("Find sceneries by location: {}!", locationScenery);
        return sceneryRepository.findByLocation(locationScenery, locale);
    }

    public Scenery getSceneryByName(String name) {
        log.info("Find the scenery by name: {}!", name);
        return sceneryRepository.findByName(name);
    }

    public Scenery saveScenery(Scenery scenery) {
        log.info("Create new scenery!");
        return sceneryRepository.saveOrUpdateScenery(scenery);
    }

}
