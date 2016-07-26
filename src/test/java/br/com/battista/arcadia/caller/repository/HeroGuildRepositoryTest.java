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
import br.com.battista.arcadia.caller.model.Hero;
import br.com.battista.arcadia.caller.model.HeroGuild;
import br.com.battista.arcadia.caller.validator.EntityValidator;

@RunWith(MockitoJUnitRunner.class)
public class HeroGuildRepositoryTest extends BaseRepositoryConfig {

    private final String nameHero = "hero01";
    private final int defense = 2;
    private final int life = 4;
    private final Hero hero = Hero.builder().name(nameHero).defense(defense).life(life).build();

    @Rule
    public ExpectedException rule = ExpectedException.none();

    @InjectMocks
    private HeroGuildRepository heroGuildRepository;

    @Mock
    private EntityValidator entityValidator;

    @Mock
    private HeroRepository heroRepository;

    @Mock
    private CardRepository cardRepository;

    @Test
    public void shouldEmptyHeroGuildsWhenEmptyDataBase() {
        List<HeroGuild> heroGuilds = heroGuildRepository.findAll();
        assertNotNull(heroGuilds);
        assertThat(heroGuilds, hasSize(0));
    }

    @Test
    public void shouldReturnHeroGuildsWhenFindAllHeroGuilds() {
        HeroGuild heroGuild = HeroGuild.builder().hero(hero).build();
        objectifyRepository.save().entity(heroGuild).now();

        List<HeroGuild> heroGuilds = heroGuildRepository.findAll();
        assertNotNull(heroGuilds);
        assertThat(heroGuilds, hasSize(1));
        assertThat(heroGuilds.iterator().next().getHero().getName(), equalTo(nameHero));
    }

    @Test
    public void shouldSaveHeroGuildWhenValidHeroGuild() {
        HeroGuild heroGuild = HeroGuild.builder().hero(hero).build();

        HeroGuild savedHeroGuild = heroGuildRepository.saveOrUpdateHeroGuild(heroGuild);
        assertNotNull(savedHeroGuild);
        assertNotNull(savedHeroGuild.getPk());
        assertNotNull(savedHeroGuild.getCreatedAt());
        assertThat(savedHeroGuild.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));
    }


    @Test
    public void shouldThrowExceptionWhenSaveHeroGuildWithInvalidName() {
        HeroGuild heroGuild = HeroGuild.builder().hero(hero).build();

        doThrow(ValidatorException.class).when(entityValidator).validate((BaseEntity) anyObject());

        rule.expect(ValidatorException.class);

        heroGuildRepository.saveOrUpdateHeroGuild(heroGuild);
    }

    @Test
    public void shouldThrowExceptionWhenSaveNullHeroGuild() {
        rule.expect(RepositoryException.class);
        rule.expectMessage(containsString("not be null!"));

        heroGuildRepository.saveOrUpdateHeroGuild(null);
    }

    @Test
    public void shouldThrowExceptionWhenSaveInvalidHeroGuild() {
        doThrow(ValidatorException.class).when(entityValidator).validate((BaseEntity) anyObject());

        rule.expect(ValidatorException.class);

        heroGuildRepository.saveOrUpdateHeroGuild(new HeroGuild());
    }

}