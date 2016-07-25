package br.com.battista.arcadia.caller.repository;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;

import org.junit.*;
import org.junit.rules.*;
import org.junit.runner.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.battista.arcadia.caller.constants.EntityConstant;
import br.com.battista.arcadia.caller.constants.ProfileAppConstant;
import br.com.battista.arcadia.caller.exception.EntityAlreadyExistsException;
import br.com.battista.arcadia.caller.exception.EntityNotFoundException;
import br.com.battista.arcadia.caller.exception.RepositoryException;
import br.com.battista.arcadia.caller.exception.ValidatorException;
import br.com.battista.arcadia.caller.model.BaseEntity;
import br.com.battista.arcadia.caller.model.Campaign;
import br.com.battista.arcadia.caller.model.User;
import br.com.battista.arcadia.caller.validator.EntityValidator;

@RunWith(MockitoJUnitRunner.class)
public class CampaignRepositoryTest extends BaseRepositoryConfig {

    private final String username = "abc0_";
    private final String mail = "abc@abc.com";
    private final ProfileAppConstant profile = ProfileAppConstant.APP;
    private final String key = "AQ-CB-1";
    private final String key2 = "AQ-CB-2";

    @Rule
    public ExpectedException rule = ExpectedException.none();

    private User user = null;

    @InjectMocks
    private CampaignRepository campaignRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EntityValidator entityValidator;

    @Before
    public void setup() {
        user = User.builder().username(username).mail(mail).profile(profile).build();
        user.initEntity();
        objectifyRepository.save()
                .entity(user)
                .now();
    }

    @Test
    public void shouldEmptyCampaignsWhenEmptyDataBase() {
        List<Campaign> campaigns = campaignRepository.findAll();
        assertNotNull(campaigns);
        assertThat(campaigns, hasSize(0));
    }

    @Test
    public void shouldReturnCampaignsWhenFindAllCampaigns() {
        Campaign campaign = Campaign.builder().key(key).when(new Date()).created(user).build();
        objectifyRepository.save().entity(campaign).now();

        List<Campaign> campaigns = campaignRepository.findAll();
        assertNotNull(campaigns);
        assertThat(campaigns, hasSize(1));
        assertThat(campaigns.iterator().next().getKey(), equalTo(key));
    }

    @Test
    public void shouldSaveCampaignWhenValidCampaign() {
        Campaign campaign = Campaign.builder().key(key).when(new Date()).created(user).build();

        Campaign savedCampaign = campaignRepository.saveOrUpdateCampaign(campaign);
        assertNotNull(savedCampaign);
        assertNotNull(savedCampaign.getPk());
        assertNotNull(savedCampaign.getCreatedAt());
        assertThat(savedCampaign.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));
    }

    @Test
    public void shouldFindByNameWhenValidCampaignAndValidKey() {
        Campaign campaign = Campaign.builder().key(key).when(new Date()).created(user).build();

        Campaign savedCampaign = campaignRepository.saveOrUpdateCampaign(campaign);
        assertNotNull(savedCampaign);
        assertNotNull(savedCampaign.getPk());
        assertNotNull(savedCampaign.getCreatedAt());
        assertThat(savedCampaign.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));

        Campaign campaignFind = campaignRepository.findByKey(campaign.getKey());
        assertNotNull(campaignFind);
        assertThat(campaignFind.getPk(), equalTo(savedCampaign.getPk()));
        assertThat(campaignFind.getVersion(), equalTo(savedCampaign.getVersion()));
        assertThat(campaignFind.getKey(), equalTo(savedCampaign.getKey()));
    }

    @Test
    public void shouldReturnNullWhenFindByNameWithValidCampaignAndInvalidName() {
        Campaign campaign = Campaign.builder().key(key).when(new Date()).created(user).build();

        Campaign savedCampaign = campaignRepository.saveOrUpdateCampaign(campaign);
        assertNotNull(savedCampaign);
        assertNotNull(savedCampaign.getPk());
        assertNotNull(savedCampaign.getCreatedAt());
        assertThat(savedCampaign.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));

        Campaign campaignFind = campaignRepository.findByKey("abcd");
        assertNull(campaignFind);
    }

    @Test
    public void shouldFindByNameWhenValidCampaignAndValidUser() {
        Campaign campaign = Campaign.builder().key(key).when(new Date()).created(user).build();

        Campaign savedCampaign = campaignRepository.saveOrUpdateCampaign(campaign);
        assertNotNull(savedCampaign);
        assertNotNull(savedCampaign.getPk());
        assertNotNull(savedCampaign.getCreatedAt());
        assertThat(savedCampaign.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));

        List<Campaign> campaigns = campaignRepository.findByUser(user);
        assertNotNull(campaigns);
        assertThat(campaigns, hasSize(1));
        Campaign campaignFind = campaigns.iterator().next();
        assertThat(campaignFind.getKey(), equalTo(key));
        assertNotNull(campaignFind);
        assertThat(campaignFind.getPk(), equalTo(savedCampaign.getPk()));
        assertThat(campaignFind.getVersion(), equalTo(savedCampaign.getVersion()));
        assertThat(campaignFind.getKey(), equalTo(savedCampaign.getKey()));
    }

    @Test
    public void shouldFindByNameWhenValidCampaignAndInvalidUser() {
        Campaign campaign = Campaign.builder().key(key).when(new Date()).created(user).build();

        Campaign savedCampaign = campaignRepository.saveOrUpdateCampaign(campaign);
        assertNotNull(savedCampaign);
        assertNotNull(savedCampaign.getPk());
        assertNotNull(savedCampaign.getCreatedAt());
        assertThat(savedCampaign.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));

        User newUser = User.builder().username("abc").build();
        List<Campaign> campaigns = campaignRepository.findByUser(newUser);
        assertNotNull(campaigns);
        assertThat(campaigns, hasSize(0));
    }

    @Test
    public void shouldThrowExceptionWhenSaveCampaignWithInvalidName() {
        Campaign campaign = Campaign.builder().key("abc").when(new Date()).created(user).build();

        doThrow(ValidatorException.class).when(entityValidator).validate((BaseEntity) anyObject());

        rule.expect(ValidatorException.class);

        campaignRepository.saveOrUpdateCampaign(campaign);
    }

    @Test
    public void shouldThrowExceptionWhenFindByNameWithNullCampaign() {
        rule.expect(RepositoryException.class);
        rule.expectMessage(containsString("not be null!"));

        campaignRepository.findByKey(null);
    }

    @Test
    public void shouldThrowExceptionWhenFindByNameWithEmptyCampaign() {
        rule.expect(RepositoryException.class);
        rule.expectMessage(containsString("not be null!"));

        campaignRepository.findByKey("");
    }

    @Test
    public void shouldThrowExceptionWhenSaveNullCampaign() {
        rule.expect(RepositoryException.class);
        rule.expectMessage(containsString("not be null!"));

        campaignRepository.saveOrUpdateCampaign(null);
    }

    @Test
    public void shouldThrowExceptionWhenSaveInvalidCampaign() {
        doThrow(ValidatorException.class).when(entityValidator).validate((BaseEntity) anyObject());

        rule.expect(ValidatorException.class);

        campaignRepository.saveOrUpdateCampaign(new Campaign());
    }

    @Test
    public void shouldUpdateCampaignWhenValidCampaignAndValidKey() {
        Campaign campaign = Campaign.builder().key(key).when(new Date()).created(user).build();

        Campaign savedCampaign = campaignRepository.saveOrUpdateCampaign(campaign);
        assertNotNull(savedCampaign);
        assertNotNull(savedCampaign.getPk());
        assertNotNull(savedCampaign.getCreatedAt());
        assertThat(savedCampaign.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));

        Campaign campaignFind = campaignRepository.findByKey(campaign.getKey());
        assertNotNull(campaignFind);
        assertThat(campaignFind.getPk(), equalTo(savedCampaign.getPk()));
        assertThat(campaignFind.getVersion(), equalTo(savedCampaign.getVersion()));
        assertThat(campaignFind.getKey(), equalTo(savedCampaign.getKey()));

        Campaign campaign02 = Campaign.builder().key(key).when(new Date()).created(user).build();
        campaign02.initEntity();
        Campaign updatedCampaign = campaignRepository.saveOrUpdateCampaign(campaign02);
        assertThat(updatedCampaign.getPk(), equalTo(campaignFind.getPk()));
        assertThat(updatedCampaign.getVersion(), equalTo(new Long(2L)));
        assertThat(updatedCampaign.getKey(), equalTo(campaignFind.getKey()));
    }

    @Test
    public void shouldReturnExceptionWhenUpdateCampaignWithDifferentVersion() {
        rule.expect(EntityAlreadyExistsException.class);

        Campaign campaign = Campaign.builder().key(key).when(new Date()).created(user).build();

        Campaign savedCampaign = campaignRepository.saveOrUpdateCampaign(campaign);
        assertNotNull(savedCampaign);
        assertNotNull(savedCampaign.getPk());
        assertNotNull(savedCampaign.getCreatedAt());
        assertThat(savedCampaign.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));

        Campaign campaignFind = campaignRepository.findByKey(campaign.getKey());
        assertNotNull(campaignFind);
        assertThat(campaignFind.getPk(), equalTo(savedCampaign.getPk()));
        assertThat(campaignFind.getVersion(), equalTo(savedCampaign.getVersion()));
        assertThat(campaignFind.getKey(), equalTo(savedCampaign.getKey()));

        Campaign campaign02 = Campaign.builder().key(key).when(new Date()).created(user).build();
        campaign02.initEntity();
        campaign02.updateEntity();
        campaignRepository.saveOrUpdateCampaign(campaign02);
    }

    @Test
    public void shouldDeleteCampaignWhenValidCampaignAndValidKey() {
        Campaign campaign = Campaign.builder().key(key).when(new Date()).created(user).build();

        Campaign savedCampaign = campaignRepository.saveOrUpdateCampaign(campaign);
        assertNotNull(savedCampaign);
        assertNotNull(savedCampaign.getPk());
        assertNotNull(savedCampaign.getCreatedAt());
        assertThat(savedCampaign.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));

        Campaign campaignFind = campaignRepository.findByKey(campaign.getKey());
        assertNotNull(campaignFind);
        assertThat(campaignFind.getPk(), equalTo(savedCampaign.getPk()));
        assertThat(campaignFind.getVersion(), equalTo(savedCampaign.getVersion()));
        assertThat(campaignFind.getKey(), equalTo(savedCampaign.getKey()));

        Campaign campaign02 = Campaign.builder().key(key).when(new Date()).created(user).build();
        campaignRepository.deleteByKey(campaign02);
    }

    @Test
    public void shouldReturnExceptionWhenDeleteInvalidKey() {
        rule.expect(EntityNotFoundException.class);

        Campaign campaign = Campaign.builder().key(key).when(new Date()).created(user).build();

        Campaign savedCampaign = campaignRepository.saveOrUpdateCampaign(campaign);
        assertNotNull(savedCampaign);
        assertNotNull(savedCampaign.getPk());
        assertNotNull(savedCampaign.getCreatedAt());
        assertThat(savedCampaign.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));

        Campaign campaignFind = campaignRepository.findByKey(campaign.getKey());
        assertNotNull(campaignFind);
        assertThat(campaignFind.getPk(), equalTo(savedCampaign.getPk()));
        assertThat(campaignFind.getVersion(), equalTo(savedCampaign.getVersion()));
        assertThat(campaignFind.getKey(), equalTo(savedCampaign.getKey()));

        Campaign campaign02 = Campaign.builder().key(key2).when(new Date()).created(user).build();
        campaignRepository.deleteByKey(campaign02);
    }

    @Test
    public void shouldThrowExceptionWhenDeleteNullCampaign() {
        rule.expect(RepositoryException.class);
        rule.expectMessage(containsString("not be null!"));

        campaignRepository.deleteByKey(null);
    }

}