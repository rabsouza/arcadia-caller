package br.com.battista.arcadia.caller.repository;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.anyObject;
import static org.mockito.Mockito.doThrow;

import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.battista.arcadia.caller.constants.EntityConstant;
import br.com.battista.arcadia.caller.exception.RepositoryException;
import br.com.battista.arcadia.caller.exception.ValidatorException;
import br.com.battista.arcadia.caller.model.BaseEntity;
import br.com.battista.arcadia.caller.model.Card;
import br.com.battista.arcadia.caller.model.Scenery;
import br.com.battista.arcadia.caller.model.SceneryCampaign;
import br.com.battista.arcadia.caller.model.enuns.DifficultySceneryEnum;
import br.com.battista.arcadia.caller.model.enuns.GroupCardEnum;
import br.com.battista.arcadia.caller.model.enuns.LocationSceneryEnum;
import br.com.battista.arcadia.caller.model.enuns.TypeCardEnum;
import br.com.battista.arcadia.caller.validator.EntityValidator;

@RunWith(MockitoJUnitRunner.class)
public class SceneryCampaignRepositoryTest extends BaseRepositoryConfig {

    private final String name = "scenery01";
    private final Card reward = Card.builder().name("wonReward").type(TypeCardEnum.NONE).group(GroupCardEnum.NONE).build();
    private final String title = "wonTitle";
    private final LocationSceneryEnum location = LocationSceneryEnum.NONE;
    private final DifficultySceneryEnum difficulty = DifficultySceneryEnum.NONE;
    private final Scenery scenery = Scenery.builder().name(name).difficulty(difficulty).location(location).wonTitle(title).wonReward(reward).build();

    @Rule
    public ExpectedException rule = ExpectedException.none();

    @InjectMocks
    private SceneryCampaignRepository sceneryCampaignRepository;

    @Mock
    private EntityValidator entityValidator;

    @Test
    public void shouldEmptySceneryCampaignsWhenEmptyDataBase() {
        List<SceneryCampaign> sceneryCampaigns = sceneryCampaignRepository.findAll();
        assertNotNull(sceneryCampaigns);
        assertThat(sceneryCampaigns, hasSize(0));
    }

    @Test
    public void shouldReturnSceneryCampaignsWhenFindAllSceneryCampaigns() {
        SceneryCampaign sceneryCampaign = SceneryCampaign.builder().name(name).scenery(scenery).build();
        objectifyRepository.save().entity(sceneryCampaign).now();

        List<SceneryCampaign> sceneryCampaigns = sceneryCampaignRepository.findAll();
        assertNotNull(sceneryCampaigns);
        assertThat(sceneryCampaigns, hasSize(1));
        assertThat(sceneryCampaigns.iterator().next().getName(), equalTo(name));
    }

    @Test
    public void shouldSaveSceneryCampaignWhenValidSceneryCampaign() {
        SceneryCampaign sceneryCampaign = SceneryCampaign.builder().name(name).scenery(scenery).build();

        SceneryCampaign savedSceneryCampaign = sceneryCampaignRepository.saveOrUpdateSceneryCampaign(sceneryCampaign);
        assertNotNull(savedSceneryCampaign);
        assertNotNull(savedSceneryCampaign.getPk());
        assertNotNull(savedSceneryCampaign.getCreatedAt());
        assertThat(savedSceneryCampaign.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));
    }

    @Test
    public void shouldFindByNameWhenValidSceneryCampaignAndValidName() {
        SceneryCampaign sceneryCampaign = SceneryCampaign.builder().name(name).scenery(scenery).build();

        SceneryCampaign savedSceneryCampaign = sceneryCampaignRepository.saveOrUpdateSceneryCampaign(sceneryCampaign);
        assertNotNull(savedSceneryCampaign);
        assertNotNull(savedSceneryCampaign.getPk());
        assertNotNull(savedSceneryCampaign.getCreatedAt());
        assertThat(savedSceneryCampaign.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));

        SceneryCampaign sceneryCampaignFind = sceneryCampaignRepository.findByName(sceneryCampaign.getName());
        assertNotNull(sceneryCampaignFind);
        assertThat(sceneryCampaignFind.getPk(), equalTo(savedSceneryCampaign.getPk()));
        assertThat(sceneryCampaignFind.getVersion(), equalTo(savedSceneryCampaign.getVersion()));
        assertThat(sceneryCampaignFind.getName(), equalTo(savedSceneryCampaign.getName()));
    }

    @Test
    public void shouldReturnNullWhenFindByNameWithValidSceneryCampaignAndInvalidName() {
        SceneryCampaign sceneryCampaign = SceneryCampaign.builder().name(name).scenery(scenery).build();

        SceneryCampaign savedSceneryCampaign = sceneryCampaignRepository.saveOrUpdateSceneryCampaign(sceneryCampaign);
        assertNotNull(savedSceneryCampaign);
        assertNotNull(savedSceneryCampaign.getPk());
        assertNotNull(savedSceneryCampaign.getCreatedAt());
        assertThat(savedSceneryCampaign.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));

        SceneryCampaign sceneryCampaignFind = sceneryCampaignRepository.findByName("abcd");
        assertNull(sceneryCampaignFind);
    }

    @Test
    public void shouldThrowExceptionWhenSaveSceneryCampaignWithInvalidName() {
        SceneryCampaign sceneryCampaign = SceneryCampaign.builder().name("abc").scenery(scenery).build();

        doThrow(ValidatorException.class).when(entityValidator).validate((BaseEntity) anyObject());

        rule.expect(ValidatorException.class);

        sceneryCampaignRepository.saveOrUpdateSceneryCampaign(sceneryCampaign);
    }

    @Test
    public void shouldThrowExceptionWhenFindByNameWithNullSceneryCampaign() {
        rule.expect(RepositoryException.class);
        rule.expectMessage(containsString("not be null!"));

        sceneryCampaignRepository.findByName(null);
    }

    @Test
    public void shouldThrowExceptionWhenFindByNameWithEmptySceneryCampaign() {
        rule.expect(RepositoryException.class);
        rule.expectMessage(containsString("not be null!"));

        sceneryCampaignRepository.findByName("");
    }

    @Test
    public void shouldThrowExceptionWhenSaveNullSceneryCampaign() {
        rule.expect(RepositoryException.class);
        rule.expectMessage(containsString("not be null!"));

        sceneryCampaignRepository.saveOrUpdateSceneryCampaign(null);
    }

    @Test
    public void shouldThrowExceptionWhenSaveInvalidSceneryCampaign() {
        doThrow(ValidatorException.class).when(entityValidator).validate((BaseEntity) anyObject());

        rule.expect(ValidatorException.class);

        sceneryCampaignRepository.saveOrUpdateSceneryCampaign(new SceneryCampaign());
    }

}