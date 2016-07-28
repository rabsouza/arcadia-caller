package br.com.battista.arcadia.caller.repository;

import java.text.MessageFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.base.Strings;
import com.googlecode.objectify.Objectify;

import br.com.battista.arcadia.caller.exception.EntityAlreadyExistsException;
import br.com.battista.arcadia.caller.exception.EntityNotFoundException;
import br.com.battista.arcadia.caller.exception.RepositoryException;
import br.com.battista.arcadia.caller.model.BaseEntity;
import br.com.battista.arcadia.caller.model.Campaign;
import br.com.battista.arcadia.caller.model.Guild;
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

    @Autowired
    private GuildRepository guildRepository;

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
                        .order("-updatedAt")
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
            workaroundChangeGuilds(campaign, campaignFind);
            campaignFind.updateEntity();
            log.info("Update the campaign: {}!", campaignFind);

            saveCampaign(campaignFind);
            return campaignFind;
        }

    }

    private void workaroundChangeGuilds(Campaign campaign, Campaign campaignFind) {
        campaignFind.setGuild01(campaign.getGuild01());
        campaignFind.setGuild02(campaign.getGuild02());
        campaignFind.setGuild03(campaign.getGuild03());
        campaignFind.setGuild04(campaign.getGuild04());
        campaignFind.setHeroesGuild01(campaign.getHeroesGuild01());
        campaignFind.setHeroesGuild02(campaign.getHeroesGuild02());
        campaignFind.setHeroesGuild03(campaign.getHeroesGuild03());
        campaignFind.setHeroesGuild04(campaign.getHeroesGuild04());
    }

    private void saveCampaign(Campaign campaign) {
        String created = campaign.getCreated();
        if (!Strings.isNullOrEmpty(created)) {
            log.info("Find the created user by username: {}.", created);
            User userCreated = userRepository.findByUsername(created);
            if (userCreated == null) {
                throw new RepositoryException("Not found created user!");
            }
        } else {
            throw new RepositoryException("Campaign create can not be null!");
        }

        saveSceneriesCampaign(campaign);
        saveGuilds(campaign);

        if (Strings.isNullOrEmpty(campaign.getKey())) {
            log.debug("Set next key in campaign!");
            campaign.setKey(keyCampaignRepository.nextKey());
        }
        saveEntity(campaign);
    }

    private void saveGuilds(Campaign campaign) {
        Guild guild01 = campaign.getHeroesGuild01();
        if (guild01 != null) {
            guildRepository.saveOrUpdateGuild(guild01);
            campaign.setGuild01(guild01.getUser().getUsername());
        }

        Guild guild02 = campaign.getHeroesGuild02();
        if (guild02 != null) {
            guildRepository.saveOrUpdateGuild(guild02);
            campaign.setGuild02(guild02.getUser().getUsername());
        }

        Guild guild03 = campaign.getHeroesGuild03();
        if (guild03 != null) {
            guildRepository.saveOrUpdateGuild(guild03);
            campaign.setGuild03(guild03.getUser().getUsername());
        }

        Guild guild04 = campaign.getHeroesGuild04();
        if (guild04 != null) {
            guildRepository.saveOrUpdateGuild(guild04);
            campaign.setGuild04(guild04.getUser().getUsername());
        }
    }

    private void saveSceneriesCampaign(Campaign campaign) {
        SceneryCampaign scenery1 = campaign.getScenery1();
        if (scenery1 != null) {
            sceneryCampaignRepository.saveOrUpdateSceneryCampaign(scenery1);
        }

        SceneryCampaign scenery2 = campaign.getScenery2();
        if (scenery2 != null) {
            sceneryCampaignRepository.saveOrUpdateSceneryCampaign(scenery2);
        }

        SceneryCampaign scenery3 = campaign.getScenery3();
        if (scenery3 != null) {
            sceneryCampaignRepository.saveOrUpdateSceneryCampaign(scenery3);
        }

        SceneryCampaign scenery4 = campaign.getScenery4();
        if (scenery4 != null) {
            sceneryCampaignRepository.saveOrUpdateSceneryCampaign(scenery4);
        }

        SceneryCampaign scenery5 = campaign.getScenery5();
        if (scenery5 != null) {
            sceneryCampaignRepository.saveOrUpdateSceneryCampaign(scenery5);
        }

        SceneryCampaign scenery6 = campaign.getScenery6();
        if (scenery6 != null) {
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
