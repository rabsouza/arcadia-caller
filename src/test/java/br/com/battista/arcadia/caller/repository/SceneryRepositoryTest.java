package br.com.battista.arcadia.caller.repository;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.*;
import org.junit.rules.*;
import org.junit.runner.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.battista.arcadia.caller.constants.EntityConstant;
import br.com.battista.arcadia.caller.exception.RepositoryException;
import br.com.battista.arcadia.caller.exception.ValidatorException;
import br.com.battista.arcadia.caller.model.BaseEntity;
import br.com.battista.arcadia.caller.model.Scenery;
import br.com.battista.arcadia.caller.validator.EntityValidator;

@RunWith(MockitoJUnitRunner.class)
public class SceneryRepositoryTest extends BaseRepositoryConfig {

    private final String name = "scenery01";
    private final String reward = "reward";
    private final String title = "title";

    @Rule
    public ExpectedException rule = ExpectedException.none();
    @InjectMocks
    private SceneryRepository sceneryRepository;
    @Mock
    private EntityValidator entityValidator;

    @Test
    public void shouldEmptySceneriesWhenEmptyDataBase() {
        List<Scenery> sceneries = sceneryRepository.findAll();
        assertNotNull(sceneries);
        assertThat(sceneries, hasSize(0));
    }

    @Test
    public void shouldReturnSceneriesWhenFindAllSceneries() {
        Scenery scenery = Scenery.builder().name(name).reward(reward).title(title).build();
        objectifyRepository.save().entity(scenery).now();

        List<Scenery> sceneries = sceneryRepository.findAll();
        assertNotNull(sceneries);
        assertThat(sceneries, hasSize(1));
        assertThat(sceneries.iterator().next().getName(), equalTo(name));
    }

    @Test
    public void shouldSaveSceneryWhenValidScenery() {
        Scenery scenery = Scenery.builder().name(name).reward(reward).title(title).build();

        Scenery savedScenery = sceneryRepository.saveOrUpdateScenery(scenery);
        assertNotNull(savedScenery);
        assertNotNull(savedScenery.getPk());
        assertNotNull(savedScenery.getCreatedAt());
        assertThat(savedScenery.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));
    }

    @Test
    public void shouldFindByNameWhenValidSceneryAndValidName() {
        Scenery scenery = Scenery.builder().name(name).reward(reward).title(title).build();

        Scenery savedScenery = sceneryRepository.saveOrUpdateScenery(scenery);
        assertNotNull(savedScenery);
        assertNotNull(savedScenery.getPk());
        assertNotNull(savedScenery.getCreatedAt());
        assertThat(savedScenery.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));

        Scenery sceneryFind = sceneryRepository.findByName(scenery.getName());
        assertNotNull(sceneryFind);
        assertThat(sceneryFind.getPk(), equalTo(savedScenery.getPk()));
        assertThat(sceneryFind.getVersion(), equalTo(savedScenery.getVersion()));
        assertThat(sceneryFind.getName(), equalTo(savedScenery.getName()));
    }

    @Test
    public void shouldReturnNullWhenFindByNameWithValidSceneryAndInvalidName() {
        Scenery scenery = Scenery.builder().name(name).reward(reward).title(title).build();

        Scenery savedScenery = sceneryRepository.saveOrUpdateScenery(scenery);
        assertNotNull(savedScenery);
        assertNotNull(savedScenery.getPk());
        assertNotNull(savedScenery.getCreatedAt());
        assertThat(savedScenery.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));

        Scenery sceneryFind = sceneryRepository.findByName("abcd");
        assertNull(sceneryFind);
    }

    @Test
    public void shouldThrowExceptionWhenSaveSceneryWithInvalidName() {
        Scenery scenery = Scenery.builder().name("abc").reward(reward).title(title).build();

        doThrow(ValidatorException.class).when(entityValidator).validate((BaseEntity) anyObject());

        rule.expect(ValidatorException.class);

        sceneryRepository.saveOrUpdateScenery(scenery);
    }

    @Test
    public void shouldThrowExceptionWhenFindByNameWithNullScenery() {
        rule.expect(RepositoryException.class);
        rule.expectMessage(containsString("not be null!"));

        sceneryRepository.findByName(null);
    }

    @Test
    public void shouldThrowExceptionWhenFindByNameWithEmptyScenery() {
        rule.expect(RepositoryException.class);
        rule.expectMessage(containsString("not be null!"));

        sceneryRepository.findByName("");
    }

    @Test
    public void shouldThrowExceptionWhenSaveNullScenery() {
        rule.expect(RepositoryException.class);
        rule.expectMessage(containsString("not be null!"));

        sceneryRepository.saveOrUpdateScenery(null);
    }

    @Test
    public void shouldThrowExceptionWhenSaveInvalidScenery() {
        doThrow(ValidatorException.class).when(entityValidator).validate((BaseEntity) anyObject());

        rule.expect(ValidatorException.class);

        sceneryRepository.saveOrUpdateScenery(new Scenery());
    }

}