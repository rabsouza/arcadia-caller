package br.com.battista.arcadia.caller.controller;

import static br.com.battista.arcadia.caller.constants.EntityConstant.DEFAULT_VERSION;
import static br.com.battista.arcadia.caller.model.KeyCampaign.PREFIX_KEY;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.*;
import org.junit.rules.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.com.battista.arcadia.caller.config.AppConfig;
import br.com.battista.arcadia.caller.constants.ProfileAppConstant;
import br.com.battista.arcadia.caller.exception.AuthenticationException;
import br.com.battista.arcadia.caller.exception.EntityAlreadyExistsException;
import br.com.battista.arcadia.caller.exception.EntityNotFoundException;
import br.com.battista.arcadia.caller.exception.ValidatorException;
import br.com.battista.arcadia.caller.model.Campaign;
import br.com.battista.arcadia.caller.model.User;
import br.com.battista.arcadia.caller.repository.CampaignRepository;
import br.com.battista.arcadia.caller.repository.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class CampaignControllerTest extends BaseControllerConfig {

    private final String invalidKey = "error";
    private final String invalidToken = "12345";
    private final ProfileAppConstant profile = ProfileAppConstant.ADMIN;
    @Rule
    public ExpectedException rule = ExpectedException.none();
    private String token;
    private User user = null;
    @Autowired
    private CampaignController campaignController;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setup() {

        user = User.builder().username("profile").mail("profile@profile").profile(profile).build();

        User savedUser = userRepository.saveOrUpdateUser(user);
        assertNotNull(savedUser);
        token = savedUser.getToken();
    }

    @Test
    public void shouldReturnExceptionWhenUnauthorized() throws AuthenticationException {
        User userApp = User.builder().username("usernameApp").mail("app@profile").profile(ProfileAppConstant.APP).build();

        User savedUser = userRepository.saveOrUpdateUser(userApp);
        assertNotNull(savedUser);

        rule.expect(AuthenticationException.class);

        campaignController.delete(savedUser.getToken(), null);
    }

    @Test
    public void shouldReturnExceptionWhenInvalidTokenToActionSave() throws AuthenticationException {
        rule.expect(AuthenticationException.class);

        campaignController.save(invalidToken, null);
    }

    @Test
    public void shouldReturnBadRequestWhenValidTokenAndNullCampaignToActionSave() throws AuthenticationException {
        ResponseEntity<Campaign> responseEntity = campaignController.save(token, null);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void shouldReturnBadRequestWhenValidTokenAndNullCampaignToActionUpdate() throws AuthenticationException {
        ResponseEntity<Campaign> responseEntity = campaignController.update(token, null);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void shouldReturnBadRequestWhenValidTokenAndNullCampaignToActionDelete() throws AuthenticationException {
        ResponseEntity<Campaign> responseEntity = campaignController.update(token, null);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void shouldReturnExceptionWhenInvalidCampaignToActionSave() throws AuthenticationException {
        rule.expect(ValidatorException.class);

        Campaign campaign = Campaign.builder().build();
        campaignController.save(token, campaign);
    }

    @Test
    public void shouldReturnExceptionWhenNullCampaignToActionSave() throws AuthenticationException {
        ResponseEntity<Campaign> responseEntity = campaignController.save(token, null);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void shouldReturnSuccessWhenValidCampaignToActionSave() throws AuthenticationException {
        Campaign campaign = Campaign.builder().when(new Date()).created(user.getUsername()).build();

        ResponseEntity<Campaign> responseEntity = campaignController.save(token, campaign);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.CREATED));
        Campaign body = responseEntity.getBody();
        assertNotNull(body);
        assertNotNull(body.getPk());
        assertThat(body.getKey(), containsString(PREFIX_KEY));
        assertThat(body.getCreated(), equalTo(user.getUsername()));
        assertThat(body.getVersion(), equalTo(DEFAULT_VERSION));
    }

    @Test
    public void shouldReturnSuccessWhenValidCampaignToActionGetAll() throws AuthenticationException {
        Campaign campaign = Campaign.builder().when(new Date()).created(user.getUsername()).build();

        ResponseEntity<Campaign> responseEntity = campaignController.save(token, campaign);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.CREATED));
        Campaign body = responseEntity.getBody();
        assertNotNull(body);
        assertNotNull(body.getPk());
        assertThat(body.getKey(), containsString(PREFIX_KEY));
        assertThat(body.getCreated(), equalTo(user.getUsername()));
        assertThat(body.getVersion(), equalTo(DEFAULT_VERSION));

        ResponseEntity<List<Campaign>> responseFind = campaignController.getAll(token);
        assertThat(responseFind.getStatusCode(), equalTo(HttpStatus.OK));
        List<Campaign> bodyFind = responseFind.getBody();
        assertNotNull(bodyFind);
        assertThat(bodyFind, hasSize(greaterThan(0)));
        assertThat(bodyFind, hasItem(campaign));

    }

    @Test
    public void shouldReturnSuccessWhenValidCampaignToActionGetByKey() throws AuthenticationException {
        Campaign campaign = Campaign.builder().when(new Date()).created(user.getUsername()).build();

        ResponseEntity<Campaign> responseEntity = campaignController.save(token, campaign);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.CREATED));
        Campaign body = responseEntity.getBody();
        assertNotNull(body);
        assertNotNull(body.getPk());
        assertThat(body.getKey(), containsString(PREFIX_KEY));
        assertThat(body.getCreated(), equalTo(user.getUsername()));
        assertThat(body.getVersion(), equalTo(DEFAULT_VERSION));

        ResponseEntity<Campaign> responseFind = campaignController.getByKey(token, campaign.getKey());
        assertThat(responseFind.getStatusCode(), equalTo(HttpStatus.OK));
        Campaign bodyFind = responseFind.getBody();
        assertNotNull(bodyFind);
        assertNotNull(bodyFind.getPk());
        assertThat(bodyFind.getKey(), equalTo(body.getKey()));
        assertThat(bodyFind.getCreated(), equalTo(body.getCreated()));
        assertThat(bodyFind.getVersion(), equalTo(body.getVersion()));

    }

    @Test
    public void shouldReturnNotFoundWhenInvalidKeyToActionGetByKey() throws AuthenticationException {
        Campaign campaign = Campaign.builder().when(new Date()).created(user.getUsername()).build();

        ResponseEntity<Campaign> responseEntity = campaignController.save(token, campaign);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.CREATED));
        Campaign body = responseEntity.getBody();
        assertNotNull(body);
        assertNotNull(body.getPk());
        assertThat(body.getKey(), containsString(PREFIX_KEY));
        assertThat(body.getCreated(), equalTo(user.getUsername()));
        assertThat(body.getVersion(), equalTo(DEFAULT_VERSION));

        ResponseEntity<Campaign> responseFind = campaignController.getByKey(token, invalidKey);
        assertThat(responseFind.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
        assertNull(responseFind.getBody());

    }

    @Test
    public void shouldReturnSuccessWhenValidCampaignToActionGetByUser() throws AuthenticationException {
        Campaign campaign = Campaign.builder().when(new Date()).created(user.getUsername()).build();

        ResponseEntity<Campaign> responseEntity = campaignController.save(token, campaign);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.CREATED));
        Campaign body = responseEntity.getBody();
        assertNotNull(body);
        assertNotNull(body.getPk());
        assertThat(body.getKey(), containsString(PREFIX_KEY));
        assertThat(body.getCreated(), equalTo(user.getUsername()));
        assertThat(body.getVersion(), equalTo(DEFAULT_VERSION));

        ResponseEntity<List<Campaign>> responseFind = campaignController.getByUser(token, campaign.getCreated());
        assertThat(responseFind.getStatusCode(), equalTo(HttpStatus.OK));
        assertNotNull(responseFind.getBody());
        assertThat(responseFind.getBody(), hasSize(greaterThan(0)));
        Campaign bodyFind = responseFind.getBody().iterator().next();
        assertNotNull(bodyFind);
        assertNotNull(bodyFind.getPk());
        assertThat(bodyFind.getKey(), equalTo(body.getKey()));
        assertThat(bodyFind.getCreated(), equalTo(body.getCreated()));
        assertThat(bodyFind.getVersion(), equalTo(body.getVersion()));

    }

    @Test
    public void shouldReturnNoContentWhenInvalidKeyToActionGetByUser() throws AuthenticationException {
        Campaign campaign = Campaign.builder().when(new Date()).created(user.getUsername()).build();

        ResponseEntity<Campaign> responseEntity = campaignController.save(token, campaign);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.CREATED));
        Campaign body = responseEntity.getBody();
        assertNotNull(body);
        assertNotNull(body.getPk());
        assertThat(body.getKey(), containsString(PREFIX_KEY));
        assertThat(body.getCreated(), equalTo(user.getUsername()));
        assertThat(body.getVersion(), equalTo(DEFAULT_VERSION));

        ResponseEntity<List<Campaign>> responseFind = campaignController.getByUser(token, "abc");
        assertThat(responseFind.getStatusCode(), equalTo(HttpStatus.NO_CONTENT));
        assertNull(responseFind.getBody());

    }

    @Test
    public void shouldReturnSuccessWhenValidCampaignToActionUpdate() throws AuthenticationException {
        Campaign campaign = Campaign.builder().when(new Date()).created(user.getUsername()).build();

        ResponseEntity<Campaign> responseEntity = campaignController.save(token, campaign);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.CREATED));
        Campaign body = responseEntity.getBody();
        assertNotNull(body);
        assertNotNull(body.getPk());
        assertThat(body.getKey(), containsString(PREFIX_KEY));
        assertThat(body.getCreated(), equalTo(user.getUsername()));
        assertThat(body.getVersion(), equalTo(DEFAULT_VERSION));

        ResponseEntity<Campaign> responseEntityUpdate = campaignController.update(token, body);
        assertThat(responseEntityUpdate.getStatusCode(), equalTo(HttpStatus.OK));
        Campaign bodyUpdate = responseEntityUpdate.getBody();
        assertNotNull(bodyUpdate);
        assertNotNull(bodyUpdate.getPk());
        assertThat(bodyUpdate.getKey(), containsString(PREFIX_KEY));
        assertThat(bodyUpdate.getCreated(), equalTo(user.getUsername()));
        assertThat(bodyUpdate.getVersion(), not(equalTo(DEFAULT_VERSION)));
    }

    @Test
    public void shouldReturnExceptionWhenValidCampaignAndDifferentVersionToActionUpdate() throws AuthenticationException {
        rule.expect(EntityAlreadyExistsException.class);

        Campaign campaign = Campaign.builder().when(new Date()).created(user.getUsername()).build();

        ResponseEntity<Campaign> responseEntity = campaignController.save(token, campaign);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.CREATED));
        Campaign body = responseEntity.getBody();
        assertNotNull(body);
        assertNotNull(body.getPk());
        assertThat(body.getKey(), containsString(PREFIX_KEY));
        assertThat(body.getCreated(), equalTo(user.getUsername()));
        assertThat(body.getVersion(), equalTo(DEFAULT_VERSION));

        Campaign campaign02 = Campaign.builder().when(new Date()).key(body.getKey()).created(user.getUsername()).build();
        campaign02.initEntity();
        campaign02.updateEntity();
        campaignController.update(token, campaign02);
    }

    @Test
    public void shouldReturnBadRequestWhenNullCampaignToActionDelete() throws AuthenticationException {
        ResponseEntity<Campaign> responseEntity = campaignController.delete(token, null);
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void shouldReturnSuccessWhenValidCampaignToActionDelete() throws AuthenticationException {
        Campaign campaign = Campaign.builder().when(new Date()).created(user.getUsername()).build();

        ResponseEntity<Campaign> responseEntity = campaignController.save(token, campaign);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.CREATED));
        Campaign body = responseEntity.getBody();
        assertNotNull(body);
        assertNotNull(body.getPk());
        assertThat(body.getKey(), containsString(PREFIX_KEY));
        assertThat(body.getCreated(), equalTo(user.getUsername()));
        assertThat(body.getVersion(), equalTo(DEFAULT_VERSION));

        ResponseEntity<Campaign> responseEntityUpdate = campaignController.delete(token, body);
        assertThat(responseEntityUpdate.getStatusCode(), equalTo(HttpStatus.OK));
        Campaign bodyDelete = responseEntityUpdate.getBody();
        assertNull(bodyDelete);
    }

    @Test
    public void shouldReturnExceptionWhenInvalidKeyToActionDelete() throws AuthenticationException {
        rule.expect(EntityNotFoundException.class);

        Campaign campaign = Campaign.builder().when(new Date()).created(user.getUsername()).build();

        ResponseEntity<Campaign> responseEntity = campaignController.save(token, campaign);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.CREATED));
        Campaign body = responseEntity.getBody();
        assertNotNull(body);
        assertNotNull(body.getPk());
        assertThat(body.getKey(), containsString(PREFIX_KEY));
        assertThat(body.getCreated(), equalTo(user.getUsername()));
        assertThat(body.getVersion(), equalTo(DEFAULT_VERSION));

        Campaign campaign02 = Campaign.builder().key(invalidKey).when(new Date()).created(user.getUsername()).build();
        campaign02.initEntity();
        campaign02.updateEntity();
        campaignController.delete(token, campaign02);
    }

    @Test
    public void shouldReturnExceptionWhenInvalidProfileToActionGetAll() throws AuthenticationException {
        rule.expect(AuthenticationException.class);

        campaignController.getAll(invalidToken);
    }

    @Test
    public void shouldReturnSuccessWhenExistsCampaignToActionGetAll() throws AuthenticationException {
        Campaign campaign = Campaign.builder().when(new Date()).created(user.getUsername()).build();

        campaignController.save(token, campaign);

        ResponseEntity<List<Campaign>> responseEntity = campaignController.getAll(token);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
        List<Campaign> body = responseEntity.getBody();
        assertNotNull(body);
        assertThat(body, hasSize(1));
        assertThat(body, hasItem(campaign));
    }

}