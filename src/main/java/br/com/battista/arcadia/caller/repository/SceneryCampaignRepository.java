package br.com.battista.arcadia.caller.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.appengine.repackaged.com.google.api.client.util.Strings;
import com.googlecode.objectify.Objectify;

import br.com.battista.arcadia.caller.exception.RepositoryException;
import br.com.battista.arcadia.caller.model.SceneryCampaign;
import br.com.battista.arcadia.caller.validator.EntityValidator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class SceneryCampaignRepository {

    @Autowired
    private EntityValidator entityValidator;

    @Autowired
    private Objectify objectifyRepository;

    public List<SceneryCampaign> findAll() {
        log.info("Find all sceneryCampaigns!");

        return objectifyRepository.load()
                       .type(SceneryCampaign.class)
                       .order("-updatedAt")
                       .list();

    }

    public SceneryCampaign findByName(String name) {
        if (Strings.isNullOrEmpty(name)) {
            throw new RepositoryException("Name can not be null!");
        }
        log.info("Find sceneryCampaign by name: {}!", name);

        return objectifyRepository
                       .load()
                       .type(SceneryCampaign.class)
                       .filter("name", name)
                       .first()
                       .now();

    }

    public SceneryCampaign saveOrUpdateSceneryCampaign(SceneryCampaign sceneryCampaign) {
        if (sceneryCampaign == null) {
            throw new RepositoryException("SceneryCampaign entity can not be null!");
        }
        entityValidator.validate(sceneryCampaign);

        sceneryCampaign.initEntity();
        log.info("Save to sceneryCampaign: {}!", sceneryCampaign);

        objectifyRepository.save()
                .entity(sceneryCampaign)
                .now();

        return sceneryCampaign;
    }

}
