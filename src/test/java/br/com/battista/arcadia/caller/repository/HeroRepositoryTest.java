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
import br.com.battista.arcadia.caller.validator.EntityValidator;

@RunWith(MockitoJUnitRunner.class)
public class HeroRepositoryTest extends BaseRepositoryConfig {

    private final String name = "hero01";
    private final int defense = 2;
    private final int life = 4;

    @Rule
    public ExpectedException rule = ExpectedException.none();

    @InjectMocks
    private HeroRepository heroRepository;

    @Mock
    private EntityValidator entityValidator;

    @Test
    public void shouldEmptyHerosWhenEmptyDataBase() {
        List<Hero> heros = heroRepository.findAll();
        assertNotNull(heros);
        assertThat(heros, hasSize(0));
    }

    @Test
    public void shouldReturnHerosWhenFindAllHeros() {
        Hero hero = Hero.builder().name(name).defense(defense).life(life).build();
        objectifyRepository.save().entity(hero).now();

        List<Hero> heros = heroRepository.findAll();
        assertNotNull(heros);
        assertThat(heros, hasSize(1));
        assertThat(heros.iterator().next().getName(), equalTo(name));
    }

    @Test
    public void shouldSaveHeroWhenValidHero() {
        Hero hero = Hero.builder().name(name).defense(defense).life(life).build();

        Hero savedHero = heroRepository.saveOrUpdateHero(hero);
        assertNotNull(savedHero);
        assertNotNull(savedHero.getPk());
        assertNotNull(savedHero.getCreatedAt());
        assertThat(savedHero.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));
    }

    @Test
    public void shouldFindByNameWhenValidHeroAndValidName() {
        Hero hero = Hero.builder().name(name).defense(defense).life(life).build();

        Hero savedHero = heroRepository.saveOrUpdateHero(hero);
        assertNotNull(savedHero);
        assertNotNull(savedHero.getPk());
        assertNotNull(savedHero.getCreatedAt());
        assertThat(savedHero.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));

        Hero heroFind = heroRepository.findByName(hero.getName());
        assertNotNull(heroFind);
        assertThat(heroFind.getPk(), equalTo(savedHero.getPk()));
        assertThat(heroFind.getVersion(), equalTo(savedHero.getVersion()));
        assertThat(heroFind.getName(), equalTo(savedHero.getName()));
    }

    @Test
    public void shouldReturnNullWhenFindByNameWithValidHeroAndInvalidName() {
        Hero hero = Hero.builder().name(name).defense(defense).life(life).build();

        Hero savedHero = heroRepository.saveOrUpdateHero(hero);
        assertNotNull(savedHero);
        assertNotNull(savedHero.getPk());
        assertNotNull(savedHero.getCreatedAt());
        assertThat(savedHero.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));

        Hero heroFind = heroRepository.findByName("abcd");
        assertNull(heroFind);
    }

    @Test
    public void shouldThrowExceptionWhenSaveHeroWithInvalidName() {
        Hero hero = Hero.builder().name("abc").defense(defense).life(life).build();

        doThrow(ValidatorException.class).when(entityValidator).validate((BaseEntity) anyObject());

        rule.expect(ValidatorException.class);

        heroRepository.saveOrUpdateHero(hero);
    }

    @Test
    public void shouldThrowExceptionWhenFindByNameWithNullHero() {
        rule.expect(RepositoryException.class);
        rule.expectMessage(containsString("not be null!"));

        heroRepository.findByName(null);
    }

    @Test
    public void shouldThrowExceptionWhenFindByNameWithEmptyHero() {
        rule.expect(RepositoryException.class);
        rule.expectMessage(containsString("not be null!"));

        heroRepository.findByName("");
    }

    @Test
    public void shouldThrowExceptionWhenSaveNullHero() {
        rule.expect(RepositoryException.class);
        rule.expectMessage(containsString("not be null!"));

        heroRepository.saveOrUpdateHero(null);
    }

    @Test
    public void shouldThrowExceptionWhenSaveInvalidHero() {
        doThrow(ValidatorException.class).when(entityValidator).validate((BaseEntity) anyObject());

        rule.expect(ValidatorException.class);

        heroRepository.saveOrUpdateHero(new Hero());
    }

}