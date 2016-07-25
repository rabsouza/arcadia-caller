package br.com.battista.arcadia.caller.repository;

import java.text.MessageFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.appengine.repackaged.com.google.api.client.util.Strings;
import com.googlecode.objectify.Objectify;

import br.com.battista.arcadia.caller.exception.EntityAlreadyExistsException;
import br.com.battista.arcadia.caller.exception.EntityNotFoundException;
import br.com.battista.arcadia.caller.exception.RepositoryException;
import br.com.battista.arcadia.caller.model.BaseEntity;
import br.com.battista.arcadia.caller.model.Campaign;
import br.com.battista.arcadia.caller.model.SceneryCampaign;
import br.com.battista.arcadia.caller.model.User;
import br.com.battista.arcadia.caller.utils.MergeBeanUtils;
import br.com.battista.arcadia.caller.validator.EntityValidator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class CampaignRepository {

    @Autowired
    private EntityValidator entityValidator;

    @Autowired
    private Objectify objectifyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KeyCampaignRepository keyCampaignRepository;

    @Autowired
    private SceneryCampaignRepository sceneryCampaignRepository;

    public List<Campaign> findAll() {
        log.info("Find all campaigns!");

        return objectifyRepository.load()
                       .type(Campaign.class)
                       .order("-updatedAt")
                       .list();

    }

    public Campaign findByKey(String key) {
        if (Strings.isNullOrEmpty(key)) {
            log.warn("Key can not be null!");
            return null;
        }
        log.info("Find campaign by key: {}!", key);

        return objectifyRepository
                       .load()
                       .type(Campaign.class)
                       .filter("key", key)
                       .first()
                       .now();

    }

    public List<Campaign> findByUser(User user) {
        if (user == null || Strings.isNullOrEmpty(user.getUsername())) {
            throw new RepositoryException("User or username can not be null!");
        }
        String username = user.getUsername();
        log.info("Find campaign by username {}!", username);

        return objectifyRepository
                       .load()
                       .type(Campaign.class)
                       .filter("created", username)
                       .list();

    }

    public Campaign saveOrUpdateCampaign(Campaign campaign) {
        if (campaign == null) {
            throw new RepositoryException("Campaign entity can not be null!");
        }
        entityValidator.validate(campaign);

        Campaign campaignFind = findByKey(campaign.getKey());
        if (campaignFind == null) {
            campaign.initEntity();
            log.info("Save the campaign: {}!", campaign);

            saveCampaign(campaign);
            return campaign;
        } else if (campaignFind.getVersion() != campaign.getVersion()) {
            String cause = MessageFormat.format("There is already an {0} object registered with different version!" +
                                                        " Please update your object!", campaign.getClass().getSimpleName());
            log.error(cause);

            throw new EntityAlreadyExistsException(cause);
        } else {
            MergeBeanUtils.merge(campaignFind, campaign);
            campaignFind.updateEntity();
            log.info("Update the campaign: {}!", campaignFind);

            saveCampaign(campaignFind);
            return campaignFind;
        }

    }

    private void saveCampaign(Campaign campaign) {
        String created = campaign.getCreated();
        if (!Strings.isNullOrEmpty(created)) {
            log.info("Find the created user by username: {}.", created);
            User userCreated = userRepository.findByUsername(created);
            if (userCreated == null) {
                throw new RepositoryException("Not found created user!!!");
            }
        } else {
            throw new RepositoryException("Create can not be null!!!");
        }

        saveSceneryCampaign(campaign);

        if(com.google.common.base.Strings.isNullOrEmpty(campaign.getKey())) {
            log.debug("Set next key in campaign!");
            campaign.setKey(keyCampaignRepository.nextKey());
        }
        saveEntity(campaign);
    }

    private void saveSceneryCampaign(Campaign campaign) {
        SceneryCampaign scenery1 = campaign.getScenery1();
        if(scenery1 != null){
            sceneryCampaignRepository.saveOrUpdateSceneryCampaign(scenery1);
        }

        SceneryCampaign scenery2 = campaign.getScenery2();
        if(scenery2 != null){
            sceneryCampaignRepository.saveOrUpdateSceneryCampaign(scenery2);
        }

        SceneryCampaign scenery3 = campaign.getScenery3();
        if(scenery3 != null){
            sceneryCampaignRepository.saveOrUpdateSceneryCampaign(scenery3);
        }

        SceneryCampaign scenery4 = campaign.getScenery4();
        if(scenery4 != null){
            sceneryCampaignRepository.saveOrUpdateSceneryCampaign(scenery4);
        }

        SceneryCampaign scenery5 = campaign.getScenery5();
        if(scenery5 != null){
            sceneryCampaignRepository.saveOrUpdateSceneryCampaign(scenery5);
        }

        SceneryCampaign scenery6 = campaign.getScenery6();
        if(scenery6 != null){
            sceneryCampaignRepository.saveOrUpdateSceneryCampaign(scenery6);
        }
    }

    private void saveEntity(BaseEntity entity) {
        objectifyRepository.save()
                        .entity(entity)
                        .now();
    }

    public void deleteByKey(Campaign campaign) {
        if (campaign == null) {
            throw new RepositoryException("Campaign entity can not be null!");
        }

        Campaign campaignFind = findByKey(campaign.getKey());
        if (campaignFind == null) {
            String cause = MessageFormat.format("Not found campaign by key: {0}!", campaign.getKey());
            log.error(cause);

            throw new EntityNotFoundException(cause);
        } else {
            log.info("Delete campaign by id {} and key {}.", campaign.getPk(), campaign.getKey());
            objectifyRepository.delete()
                    .entity(campaignFind);
        }
    }

}
