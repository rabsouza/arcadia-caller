package br.com.battista.arcadia.caller.repository;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNotNull;
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