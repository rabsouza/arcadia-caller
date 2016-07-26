package br.com.battista.arcadia.caller.repository;

import static br.com.battista.arcadia.caller.model.KeyCampaign.PREFIX_KEY;

import org.hamcrest.Matchers;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class KeyCampaignRepositoryTest extends BaseRepositoryConfig {

    @InjectMocks
    private KeyCampaignRepository keyCampaignRepository;

    @Test
    public void shouldReturnNextKey() {
        String firistKey = keyCampaignRepository.nextKey();
        Assert.assertNotNull(firistKey);
        Assert.assertThat(firistKey, Matchers.equalTo(PREFIX_KEY.concat("1")));
        Assert.assertThat(keyCampaignRepository.nextKey(), Matchers.equalTo(PREFIX_KEY.concat("2")));
        Assert.assertThat(keyCampaignRepository.nextKey(), Matchers.equalTo(PREFIX_KEY.concat("3")));
        Assert.assertThat(keyCampaignRepository.nextKey(), Matchers.equalTo(PREFIX_KEY.concat("4")));
    }

}