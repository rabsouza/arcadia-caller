package br.com.battista.arcadia.caller.controller;

import static br.com.battista.arcadia.caller.constants.EntityConstant.DEFAULT_VERSION;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.*;
import org.junit.rules.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.com.battista.arcadia.caller.config.AppConfig;
import br.com.battista.arcadia.caller.constants.ProfileAppConstant;
import br.com.battista.arcadia.caller.exception.AuthenticationException;
import br.com.battista.arcadia.caller.exception.ValidatorException;
import br.com.battista.arcadia.caller.model.Hero;
import br.com.battista.arcadia.caller.model.User;
import br.com.battista.arcadia.caller.repository.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class HeroControllerTest extends BaseControllerConfig {

    private final String name = "hero01";
    private final int defense = 2;
    private final int life = 4;
    private String token = null;
    private final String username = "abc0_";
    private final String mail = "abc@abc.com";
    private final ProfileAppConstant profile = ProfileAppConstant.APP;

    @Rule
    public ExpectedException rule = ExpectedException.none();

    @Autowired
    private HeroController heroController;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setup() {

        User user = User.builder().username(username).mail(mail).profile(profile).build();

        User savedUser = userRepository.saveOrUpdateUser(user);
        assertNotNull(savedUser);
        token = savedUser.getToken();
    }

    @Test
    public void shouldReturnExceptionWhenInvalidTokenToActionSave() throws AuthenticationException {
        rule.expect(AuthenticationException.class);

        heroController.save("abc", null);
    }

    @Test
    public void shouldReturnBadRequestWhenHeroNullToActionSave() throws AuthenticationException {
        ResponseEntity<Hero> responseEntity = heroController.save(token, null);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void shouldReturnExceptionWhenInvalidHeroToActionSave() throws AuthenticationException {
        rule.expect(ValidatorException.class);

        Hero hero = Hero.builder().name(name).build();
        heroController.save(token, hero);
    }

    @Test
    public void shouldReturnSuccessWhenValidHeroToActionSave() throws AuthenticationException {
        Hero hero = Hero.builder().name(name).defense(defense).life(life).build();

        ResponseEntity<Hero> responseEntity = heroController.save(token, hero);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
        Hero body = responseEntity.getBody();
        assertNotNull(body);
        assertNotNull(body.getPk());
        assertThat(body.getName(), equalTo(name));
        assertThat(body.getDefense(), equalTo(defense));
        assertThat(body.getLife(), equalTo(life));
        assertThat(body.getVersion(), equalTo(DEFAULT_VERSION));
    }

    @Test
    public void shouldReturnExceptionWhenInvalidTokenToActionGetAll() throws AuthenticationException {
        rule.expect(AuthenticationException.class);

        heroController.getAll("abc");
    }

    @Test
    public void shouldReturnNotContentWhenNoHerosFounds() throws AuthenticationException {
        ResponseEntity<List<Hero>> responseEntity = heroController.getAll(token);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.NO_CONTENT));
        assertNull(responseEntity.getBody());
    }

    @Test
    public void shouldReturnSuccessWhenExistsHeroToActionGetAll() throws AuthenticationException {
        Hero hero = Hero.builder().name(name).defense(defense).life(life).build();

        heroController.save(token, hero);

        ResponseEntity<List<Hero>> responseEntity = heroController.getAll(token);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
        List<Hero> body = responseEntity.getBody();
        assertNotNull(body);
        assertThat(body, hasSize(1));
        assertThat(body, hasItem(hero));
    }

}