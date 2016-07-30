package br.com.battista.arcadia.caller.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.appengine.repackaged.com.google.common.collect.Sets;
import com.google.common.base.Strings;

import br.com.battista.arcadia.caller.exception.RepositoryException;
import br.com.battista.arcadia.caller.model.Campaign;
import br.com.battista.arcadia.caller.model.SceneryCampaign;
import br.com.battista.arcadia.caller.model.User;
import br.com.battista.arcadia.caller.model.dto.StatisticUserDto;
import br.com.battista.arcadia.caller.repository.CampaignRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StatisticUserService {

    @Autowired
    private CampaignRepository campaignRepository;

    public StatisticUserDto getAllByUser(User user) {
        if (user == null || Strings.isNullOrEmpty(user.getUsername())) {
            throw new RepositoryException("User or username can not be null!");
        }

        log.info("Find all campaigns by user and guilds: {}!", user);
        Set<Campaign> campaigns = Sets.newHashSet(campaignRepository.findByUser(user));
        campaigns.addAll(campaignRepository.findByGuilds(user));

        log.info("Found {} campaigns e and process all!", campaigns.size());
        String username = user.getUsername();
        StatisticUserDto statisticUserDto = StatisticUserDto.builder()
                                                    .username(username)
                                                    .urlAvatar(user.getUrlAvatar())
                                                    .mail(user.getMail())
                                                    .build();
        statisticUserDto.initializeStatistic();
        for (Campaign campaign : campaigns) {
            statisticUserDto.incCampaigns();

            checkStatisticCampaign(username, statisticUserDto, campaign);

            String guild = campaign.getGuild01();
            checkStatisticGuild(username, statisticUserDto, guild);
            guild = campaign.getGuild02();
            checkStatisticGuild(username, statisticUserDto, guild);
            guild = campaign.getGuild03();
            checkStatisticGuild(username, statisticUserDto, guild);
            guild = campaign.getGuild04();
            checkStatisticGuild(username, statisticUserDto, guild);

            SceneryCampaign scenery = campaign.getScenery1();
            checkStatisticScenery(statisticUserDto, scenery, username);
            scenery = campaign.getScenery2();
            checkStatisticScenery(statisticUserDto, scenery, username);
            scenery = campaign.getScenery3();
            checkStatisticScenery(statisticUserDto, scenery, username);
            scenery = campaign.getScenery4();
            checkStatisticScenery(statisticUserDto, scenery, username);
            scenery = campaign.getScenery5();
            checkStatisticScenery(statisticUserDto, scenery, username);
            scenery = campaign.getScenery6();
            checkStatisticScenery(statisticUserDto, scenery, username);
        }

        return statisticUserDto;
    }

    private void checkStatisticCampaign(String username, StatisticUserDto statisticUserDto, Campaign campaign) {
        if (campaign.getCompleted()) {
            statisticUserDto.incCompleteds();

            if (campaign.getWinner() != null && campaign.getWinner().contains(username)) {
                statisticUserDto.incCampaignWinners();
            }

            if (campaign.getLeastDeaths() != null && campaign.getLeastDeaths().contains(username)) {
                statisticUserDto.incCampaignLeastDeaths();
            }

            if (campaign.getMostCoins() != null && campaign.getMostCoins().contains(username)) {
                statisticUserDto.incCampaignMostCoins();
            }

            if (campaign.getWonReward() != null && campaign.getWonReward().contains(username)) {
                statisticUserDto.incCampaignWonRewards();
            }

            if (campaign.getWonTitle() != null && campaign.getWonTitle().contains(username)) {
                statisticUserDto.incCampaignWonTitles();
            }
        }
    }

    private void checkStatisticGuild(String username, StatisticUserDto statisticUserDto, String guild) {
        if (username.equalsIgnoreCase(guild)) {
            statisticUserDto.incGuilds();
        }
    }

    private void checkStatisticScenery(StatisticUserDto statisticUserDto, SceneryCampaign scenery, String username) {
        if (scenery != null && scenery.getCompleted()) {
            statisticUserDto.incSceneries();
            if (username.equalsIgnoreCase(scenery.getWinner())) {
                statisticUserDto.incSceneryWinners();
            } else {
                statisticUserDto.incSceneryDefeats();
            }

            if (scenery.getLeastDeaths() != null && scenery.getLeastDeaths().contains(username)) {
                statisticUserDto.incSceneryLeastDeaths();
            }

            if (scenery.getMostCoins() != null && scenery.getMostCoins().contains(username)) {
                statisticUserDto.incSceneryMostCoins();
            }

            if (scenery.getWonReward() != null && scenery.getWonReward().contains(username)) {
                statisticUserDto.incSceneryWonRewards();
            }

            if (scenery.getWonTitle() != null && scenery.getWonTitle().contains(username)) {
                statisticUserDto.incSceneryWonTitles();
            }
        }
    }

}
