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
import br.com.battista.arcadia.caller.model.Hero;
import br.com.battista.arcadia.caller.repository.HeroRepository;

@RunWith(MockitoJUnitRunner.class)
public class HeroServiceTest {

    private final String name = "hero01";
    private final int defense = 2;
    private final int life = 4;

    @Rule
    public ExpectedException rule = ExpectedException.none();

    @InjectMocks
    private HeroService heroService;

    @Mock
    private HeroRepository heroRepository;

    @Test
    public void shouldGetAllHeros() {
        Hero hero = Hero.builder().name(name).defense(defense).life(life).build();
        when(heroRepository.findAll()).thenReturn(Lists.newArrayList(hero));

        List<Hero> heros = heroService.getAllHeroes();
        assertNotNull(heros);
        assertThat(heros, hasSize(1));
        assertThat(heros.iterator().next().getName(), equalTo(name));

    }

    @Test
    public void shouldGetHeroByName() {
        Hero hero = Hero.builder().id(1l).name(name).defense(defense).life(life).build();
        hero.initEntity();
        when(heroRepository.findByName(Matchers.anyString())).thenReturn(hero);

        Hero heroFind = heroService.getHeroByName(name);
        assertNotNull(heroFind);
        assertNotNull(heroFind.getPk());
        assertNotNull(heroFind.getCreatedAt());
        assertThat(heroFind.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));
    }

    @Test
    public void shouldSaveHeroWhenHeroValid() {
        Hero hero = Hero.builder().id(1l).name(name).defense(defense).life(life).build();
        hero.initEntity();
        when(heroRepository.saveOrUpdateHero((Hero) Matchers.any())).thenReturn(hero);

        Hero savedHero = heroService.saveHero(hero);
        assertNotNull(savedHero);
        assertNotNull(savedHero.getCreatedAt());
        assertThat(savedHero.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));
        assertNotNull(savedHero.getId());
    }

    @Test
    public void shouldReturnExceptionWhenHeroInvalid() {
        doThrow(ValidatorException.class).when(heroRepository).saveOrUpdateHero((Hero) Matchers.any());

        rule.expect(ValidatorException.class);

        heroService.saveHero(new Hero());
    }

    @Test
    public void shouldReturnExceptionWhenHeroNull() {
        doThrow(RepositoryException.class).when(heroRepository).saveOrUpdateHero((Hero) Matchers.any());

        rule.expect(RepositoryException.class);

        heroService.saveHero(null);
    }

}