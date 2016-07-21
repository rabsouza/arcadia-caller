package br.com.battista.arcadia.caller.service;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;

import org.junit.*;
import org.junit.rules.*;
import org.junit.runner.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.appengine.repackaged.com.google.common.collect.Lists;

import br.com.battista.arcadia.caller.constants.EntityConstant;
import br.com.battista.arcadia.caller.constants.ProfileAppConstant;
import br.com.battista.arcadia.caller.exception.EntityNotFoundException;
import br.com.battista.arcadia.caller.exception.RepositoryException;
import br.com.battista.arcadia.caller.exception.ValidatorException;
import br.com.battista.arcadia.caller.model.Campaign;
import br.com.battista.arcadia.caller.model.User;
import br.com.battista.arcadia.caller.repository.CampaignRepository;

@RunWith(MockitoJUnitRunner.class)
public class CampaignServiceTest {

    private final String username = "abc0_";
    private final String mail = "abc@abc.com";
    private final ProfileAppConstant profile = ProfileAppConstant.APP;
    private final String key = "AQ-CB-1";
    private final String key2 = "AQ-CB-2";
    @Rule
    public ExpectedException rule = ExpectedException.none();
    private User user = null;
    @InjectMocks
    private CampaignService campaignService;

    @Mock
    private CampaignRepository campaignRepository;

    @Before
    public void setup() {
        user = User.builder().username(username).mail(mail).profile(profile).build();
        user.initEntity();
    }

    @Test
    public void shouldGetAllCampaigns() {
        Campaign campaign = Campaign.builder().key(key).when(new Date()).created(user).build();
        when(campaignRepository.findAll()).thenReturn(Lists.newArrayList(campaign));

        List<Campaign> campaigns = campaignService.getAllCampaigns();
        assertNotNull(campaigns);
        assertThat(campaigns, hasSize(1));
        assertThat(campaigns.iterator().next().getKey(), equalTo(key));

    }

    @Test
    public void shouldGetCampaignByKey() {
        Campaign campaign = Campaign.builder().id(1l).key(key).when(new Date()).created(user).build();
        campaign.initEntity();
        when(campaignRepository.findByKey(anyString())).thenReturn(campaign);

        Campaign campaignFind = campaignService.getCampaignByKey(key);
        assertNotNull(campaignFind);
        assertNotNull(campaignFind.getPk());
        assertNotNull(campaignFind.getCreatedAt());
        assertThat(campaignFind.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));
    }

    @Test
    public void shouldGetCampaignByUser() {
        Campaign campaign = Campaign.builder().id(1l).key(key).when(new Date()).created(user).build();
        campaign.initEntity();
        when(campaignRepository.findByUser((User) any())).thenReturn(Lists.<Campaign>newArrayList(campaign));

        List<Campaign> campaigns = campaignService.getCampaignByUser(user);
        assertNotNull(campaigns);
        assertThat(campaigns, hasSize(1));
        Campaign campaignFind = campaigns.iterator().next();
        assertNotNull(campaignFind);
        assertNotNull(campaignFind.getPk());
        assertNotNull(campaignFind.getCreatedAt());
        assertThat(campaignFind.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));
    }

    @Test
    public void shouldSaveCampaignWhenCampaignValid() {
        Campaign campaign = Campaign.builder().id(1l).key(key).when(new Date()).created(user).build();
        campaign.initEntity();
        when(campaignRepository.saveOrUpdateCampaign((Campaign) any())).thenReturn(campaign);

        Campaign savedCampaign = campaignService.saveCampaign(campaign);
        assertNotNull(savedCampaign);
        assertNotNull(savedCampaign.getCreatedAt());
        assertThat(savedCampaign.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));
        assertNotNull(savedCampaign.getId());
    }

    @Test
    public void shouldUpdateCampaignWhenCampaignValid() {
        Campaign campaign = Campaign.builder().id(1l).key(key).when(new Date()).created(user).build();
        campaign.initEntity();
        campaign.updateEntity();
        when(campaignRepository.saveOrUpdateCampaign((Campaign) any())).thenReturn(campaign);

        Campaign savedCampaign = campaignService.updateCampaign(campaign);
        assertNotNull(savedCampaign);
        assertNotNull(savedCampaign.getCreatedAt());
        assertThat(savedCampaign.getVersion(), not(equalTo(EntityConstant.DEFAULT_VERSION)));
        assertNotNull(savedCampaign.getId());
    }

    @Test
    public void shouldDeleteCampaignWhenCampaignValid() {
        Campaign campaign = Campaign.builder().key(key).when(new Date()).created(user).build();
        campaign.initEntity();
        doNothing().when(campaignRepository).deleteByKey((Campaign) any());

        campaignService.deleteCampaign(campaign);
    }

    @Test
    public void shouldReturnExceptionWhenDeleteInvalidCampaign() {
        rule.expect(EntityNotFoundException.class);

        Campaign campaign = Campaign.builder().key(key).when(new Date()).created(user).build();
        campaign.initEntity();
        doThrow(EntityNotFoundException.class).when(campaignRepository).deleteByKey((Campaign) any());

        campaignService.deleteCampaign(campaign);
    }

    @Test
    public void shouldReturnExceptionWhenDeleteNullCampaign() {
        rule.expect(RepositoryException.class);

        doThrow(RepositoryException.class).when(campaignRepository).deleteByKey((Campaign) any());

        campaignService.deleteCampaign(null);
    }

    @Test
    public void shouldReturnExceptionWhenCampaignInvalid() {
        doThrow(ValidatorException.class).when(campaignRepository).saveOrUpdateCampaign((Campaign) any());

        rule.expect(ValidatorException.class);

        campaignService.saveCampaign(new Campaign());
    }

    @Test
    public void shouldReturnExceptionWhenCampaignNull() {
        doThrow(RepositoryException.class).when(campaignRepository).saveOrUpdateCampaign((Campaign) any());

        rule.expect(RepositoryException.class);

        campaignService.saveCampaign(null);
    }

}