package br.com.battista.arcadia.caller.controller;

import static br.com.battista.arcadia.caller.constants.EntityConstant.DEFAULT_VERSION;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Locale;

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
import br.com.battista.arcadia.caller.model.enuns.GroupHeroEnum;
import br.com.battista.arcadia.caller.repository.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class HeroControllerTest extends BaseControllerConfig {

    private Locale locale = new Locale("pt");

    private final String name = "hero01";
    private final int defense = 2;
    private final int life = 4;
    private final String ability = "ability";
    private final GroupHeroEnum group = GroupHeroEnum.CORE_BOX;
    private final String username = "abc0_";
    private final String mail = "abc@abc.com";
    private final ProfileAppConstant profile = ProfileAppConstant.ADMIN;
    @Rule
    public ExpectedException rule = ExpectedException.none();
    private String token = null;
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
    public void shouldReturnExceptionWhenUnauthorized() throws AuthenticationException {
        User userApp = User.builder().username("usernameApp").mail("app@profile").profile(ProfileAppConstant.APP).build();

        User savedUser = userRepository.saveOrUpdateUser(userApp);
        assertNotNull(savedUser);

        rule.expect(AuthenticationException.class);

        heroController.save(savedUser.getToken(), null);
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

        Hero hero = Hero.builder().locale(locale.getLanguage()).name(name).build();
        heroController.save(token, hero);
    }

    @Test
    public void shouldReturnSuccessWhenValidHeroToActionSave() throws AuthenticationException {
        Hero hero = Hero.builder().locale(locale.getLanguage()).name(name).defense(defense).life(life).ability(ability).group(group).build();

        ResponseEntity<Hero> responseEntity = heroController.save(token, hero);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.CREATED));
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

        heroController.getAll("abc", locale.getLanguage());
    }

    @Test
    public void shouldReturnNotContentWhenNoHerosFounds() throws AuthenticationException {
        ResponseEntity<List<Hero>> responseEntity = heroController.getAll(token, locale.getLanguage());

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.NO_CONTENT));
        assertNull(responseEntity.getBody());
    }

    @Test
    public void shouldReturnSuccessWhenExistsHeroToActionGetAll() throws AuthenticationException {
        Hero hero = Hero.builder().locale(locale.getLanguage()).name(name).defense(defense).life(life).ability(ability).group(group).build();

        heroController.save(token, hero);

        ResponseEntity<List<Hero>> responseEntity = heroController.getAll(token, locale.getLanguage());

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
        List<Hero> body = responseEntity.getBody();
        assertNotNull(body);
        assertThat(body, hasSize(1));
        assertThat(body, hasItem(hero));
    }

}