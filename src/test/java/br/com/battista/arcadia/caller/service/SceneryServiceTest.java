package br.com.battista.arcadia.caller.service;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.*;
import org.junit.rules.*;
import org.junit.runner.*;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.appengine.repackaged.com.google.common.collect.Lists;

import br.com.battista.arcadia.caller.constants.EntityConstant;
import br.com.battista.arcadia.caller.exception.RepositoryException;
import br.com.battista.arcadia.caller.exception.ValidatorException;
import br.com.battista.arcadia.caller.model.Scenery;
import br.com.battista.arcadia.caller.model.enuns.LocationSceneryEnum;
import br.com.battista.arcadia.caller.repository.SceneryRepository;

@RunWith(MockitoJUnitRunner.class)
public class SceneryServiceTest {

    private final String name = "scenery01";
    private final String reward = "wonReward";
    private final String title = "wonTitle";
    private final LocationSceneryEnum location = LocationSceneryEnum.NONE;

    @Rule
    public ExpectedException rule = ExpectedException.none();

    @InjectMocks
    private SceneryService sceneryService;

    @Mock
    private SceneryRepository sceneryRepository;

    @Test
    public void shouldGetAllSceneries() {
        Scenery scenery = Scenery.builder().name(name).location(location).wonTitle(title).wonReward(reward).build();
        when(sceneryRepository.findAll()).thenReturn(Lists.newArrayList(scenery));

        List<Scenery> sceneries = sceneryService.getAllSceneries();
        assertNotNull(sceneries);
        assertThat(sceneries, hasSize(1));
        assertThat(sceneries.iterator().next().getName(), equalTo(name));

    }

    @Test
    public void shouldGetSceneryByName() {
        Scenery scenery = Scenery.builder().id(1l).name(name).location(location).wonTitle(title).wonReward(reward).build();
        scenery.initEntity();
        when(sceneryRepository.findByName(Matchers.anyString())).thenReturn(scenery);

        Scenery sceneryFind = sceneryService.getSceneryByName(name);
        assertNotNull(sceneryFind);
        assertNotNull(sceneryFind.getPk());
        assertNotNull(sceneryFind.getCreatedAt());
        assertThat(sceneryFind.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));
    }

    @Test
    public void shouldSaveSceneryWhenSceneryValid() {
        Scenery scenery = Scenery.builder().id(1l).name(name).location(location).wonTitle(title).wonReward(reward).build();
        scenery.initEntity();
        when(sceneryRepository.saveOrUpdateScenery((Scenery) Matchers.any())).thenReturn(scenery);

        Scenery savedScenery = sceneryService.saveScenery(scenery);
        assertNotNull(savedScenery);
        assertNotNull(savedScenery.getCreatedAt());
        assertThat(savedScenery.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));
        assertNotNull(savedScenery.getId());
    }

    @Test
    public void shouldReturnExceptionWhenSceneryInvalid() {
        doThrow(ValidatorException.class).when(sceneryRepository).saveOrUpdateScenery((Scenery) Matchers.any());

        rule.expect(ValidatorException.class);

        sceneryService.saveScenery(new Scenery());
    }

    @Test
    public void shouldReturnExceptionWhenSceneryNull() {
        doThrow(RepositoryException.class).when(sceneryRepository).saveOrUpdateScenery((Scenery) Matchers.any());

        rule.expect(RepositoryException.class);

        sceneryService.saveScenery(null);
    }

}