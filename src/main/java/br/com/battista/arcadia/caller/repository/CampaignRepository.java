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

    public List<Campaign> findAll() {
        log.info("Find all campaigns!");

        return objectifyRepository.load()
                       .type(Campaign.class)
                       .order("-updatedAt")
                       .list();

    }

    public Campaign findByKey(String key) {
        if (Strings.isNullOrEmpty(key)) {
            throw new RepositoryException("Key can not be null!");
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
                       .filter("usernameCreated", username)
                       .list();

    }

    public Campaign saveOrUpdateCampaign(Campaign campaign) {
        if (campaign == null) {
            throw new RepositoryException("Campaign entity can not be null!");
        }
        entityValidator.validate(campaign);
        campaign.setUsernameCreated(campaign.getCreated().getUsername());

        Campaign campaignFind = findByKey(campaign.getKey());
        if (campaignFind == null) {
            campaign.initEntity();
            log.info("Save to campaign: {}!", campaign);

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
            log.info("Update to campaign: {}!", campaignFind);

            saveCampaign(campaignFind);
            return campaignFind;
        }

    }

    private void saveCampaign(Campaign campaign) {

        User created = campaign.getCreated();
        if (created != null) {
            log.info("Save te created user: {}.", created);
            userRepository.saveOrUpdateUser(created);
        }

        saveEntity(campaign);
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
