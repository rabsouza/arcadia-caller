package br.com.battista.arcadia.caller.controller;

import static br.com.battista.arcadia.caller.builder.ResponseEntityBuilder.buildResponseErro;
import static br.com.battista.arcadia.caller.builder.ResponseEntityBuilder.buildResponseSuccess;
import static br.com.battista.arcadia.caller.constants.ProfileAppConstant.ADMIN;
import static br.com.battista.arcadia.caller.constants.ProfileAppConstant.APP;
import static br.com.battista.arcadia.caller.constants.RestControllerConstant.ENABLE_CACHED_ACTION;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.battista.arcadia.caller.constants.MessagePropertiesConstant;
import br.com.battista.arcadia.caller.constants.RestControllerConstant;
import br.com.battista.arcadia.caller.exception.AuthenticationException;
import br.com.battista.arcadia.caller.model.Campaign;
import br.com.battista.arcadia.caller.model.User;
import br.com.battista.arcadia.caller.service.AuthenticationService;
import br.com.battista.arcadia.caller.service.CampaignService;
import br.com.battista.arcadia.caller.service.MessageCustomerService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("api/v1/campaign")
public class CampaignController {

    public static final String CAMPAIGN_CAN_NOT_BE_NULL = "Campaign can not be null!";
    public static final String CAMPAIGN_IS_REQUIRED = "Campaign";

    @Autowired
    private CampaignService campaignService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private MessageCustomerService messageSource;

    @RequestMapping(value = "/", method = RequestMethod.GET,
            produces = RestControllerConstant.PRODUCES)
    @ResponseBody
    public ResponseEntity<List<Campaign>> getAll(@RequestHeader("token") String token) throws AuthenticationException {
        authenticationService.authetication(token, ADMIN, APP);

        log.info("Retrieve all campaigns!");
        List<Campaign> campaigns = campaignService.getAllCampaigns();

        if (campaigns == null || campaigns.isEmpty()) {
            log.warn("No campaigns found!");
            return buildResponseErro(HttpStatus.NO_CONTENT);
        } else {
            log.info("Found {} campaigns!", campaigns.size());
            return buildResponseSuccess(campaigns, HttpStatus.OK, ENABLE_CACHED_ACTION);
        }
    }

    @RequestMapping(value = "/{key}", method = RequestMethod.GET,
            produces = RestControllerConstant.PRODUCES)
    @ResponseBody
    public ResponseEntity<Campaign> getByKey(@RequestHeader("token") String token, @PathVariable("key") String key) throws
            AuthenticationException {
        authenticationService.authetication(token, ADMIN, APP);

        log.info("Retrieve campaign by key: {}.", key);
        Campaign campaign = campaignService.getCampaignByKey(key);

        if (campaign == null) {
            log.warn("No campaign found!");
            return buildResponseErro(HttpStatus.NOT_FOUND);
        } else {
            log.info("Found the campaign: {}.", campaign);
            return buildResponseSuccess(campaign, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/{username}", method = RequestMethod.GET,
            produces = RestControllerConstant.PRODUCES)
    @ResponseBody
    public ResponseEntity<List<Campaign>> getByUser(@RequestHeader("token") String token, @PathVariable("username") String username) throws
            AuthenticationException {
        authenticationService.authetication(token, ADMIN, APP);

        log.info("Retrieve campaigns by created: {}.", username);
        User user = User.builder().username(username).build();
        List<Campaign> campaigns = campaignService.getCampaignByUser(user);

        if (campaigns == null || campaigns.isEmpty()) {
            log.warn("No campaigns found!");
            return buildResponseErro(HttpStatus.NO_CONTENT);
        } else {
            log.info("Found {} campaigns!", campaigns.size());
            return buildResponseSuccess(campaigns, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.POST,
            produces = RestControllerConstant.PRODUCES,
            consumes = RestControllerConstant.CONSUMES)
    @ResponseBody
    public ResponseEntity<Campaign> save(@RequestHeader("token") String token, @RequestBody Campaign campaign) throws AuthenticationException {
        authenticationService.authetication(token, ADMIN, APP);

        if (campaign == null) {
            log.warn(CAMPAIGN_CAN_NOT_BE_NULL);
            return buildResponseErro(messageSource.getMessage(MessagePropertiesConstant.MESSAGE_FIELD_IS_REQUIRED, CAMPAIGN_IS_REQUIRED));
        }

        log.info("Save the campaign[{}]!", campaign);
        Campaign newCampaign = campaignService.saveCampaign(campaign);
        log.debug("Save the campaign and generate to id: {}!", newCampaign.getId());
        return buildResponseSuccess(newCampaign, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT,
            produces = RestControllerConstant.PRODUCES,
            consumes = RestControllerConstant.CONSUMES)
    @ResponseBody
    public ResponseEntity<Campaign> update(@RequestHeader("token") String token, @RequestBody Campaign campaign) throws AuthenticationException {
        authenticationService.authetication(token, ADMIN, APP);

        if (campaign == null) {
            log.warn(CAMPAIGN_CAN_NOT_BE_NULL);
            return buildResponseErro(messageSource.getMessage(MessagePropertiesConstant.MESSAGE_FIELD_IS_REQUIRED, CAMPAIGN_IS_REQUIRED));
        }

        log.info("Update the campaign[{}]!", campaign);
        Campaign updatedCampaign = campaignService.updateCampaign(campaign);
        return buildResponseSuccess(updatedCampaign, HttpStatus.OK);
    }

    @RequestMapping(value = "/", method = RequestMethod.DELETE,
            consumes = RestControllerConstant.CONSUMES)
    @ResponseBody
    public ResponseEntity delete(@RequestHeader("token") String token, @RequestBody Campaign campaign) throws AuthenticationException {
        authenticationService.authetication(token, ADMIN);

        if (campaign == null) {
            log.warn(CAMPAIGN_CAN_NOT_BE_NULL);
            return buildResponseErro(messageSource.getMessage(MessagePropertiesConstant.MESSAGE_FIELD_IS_REQUIRED, CAMPAIGN_IS_REQUIRED));
        }

        log.info("Delete the campaign[{}]!", campaign);
        campaignService.deleteCampaign(campaign);
        return buildResponseSuccess(HttpStatus.OK);
    }


}
