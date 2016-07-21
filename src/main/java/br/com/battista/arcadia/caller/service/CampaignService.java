package br.com.battista.arcadia.caller.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.battista.arcadia.caller.model.Campaign;
import br.com.battista.arcadia.caller.model.User;
import br.com.battista.arcadia.caller.repository.CampaignRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CampaignService {

    @Autowired
    private CampaignRepository campaignRepository;

    public List<Campaign> getAllCampaigns() {
        log.info("Find all campaigns!");
        return campaignRepository.findAll();
    }

    public Campaign getCampaignByKey(String key) {
        log.info("Find the campaign by key: {}!", key);
        return campaignRepository.findByKey(key);
    }

    public List<Campaign> getCampaignByUser(User user) {
        log.info("Find the campaign by user: {}!", user);
        return campaignRepository.findByUser(user);
    }

    public Campaign saveCampaign(Campaign campaign) {
        log.info("Create new campaign!");
        return campaignRepository.saveOrUpdateCampaign(campaign);
    }

    public Campaign updateCampaign(Campaign campaign) {
        log.info("Update campaign!");
        return campaignRepository.saveOrUpdateCampaign(campaign);
    }

    public void deleteCampaign(Campaign campaign) {
        log.info("Delete campaign!");
        campaignRepository.deleteByKey(campaign);
    }

}
