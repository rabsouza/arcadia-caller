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
import br.com.battista.arcadia.caller.model.Card;
import br.com.battista.arcadia.caller.model.Scenery;
import br.com.battista.arcadia.caller.model.User;
import br.com.battista.arcadia.caller.model.enuns.GroupCardEnum;
import br.com.battista.arcadia.caller.model.enuns.LocationSceneryEnum;
import br.com.battista.arcadia.caller.model.enuns.TypeCardEnum;
import br.com.battista.arcadia.caller.repository.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class SceneryControllerTest extends BaseControllerConfig {

    private final String name = "scenery01";
    private final Card reward = Card.builder().name("wonReward").key("key01").type(TypeCardEnum.NONE).group(GroupCardEnum.NONE).build();
    private final String title = "wonTitle";
    private final LocationSceneryEnum location = LocationSceneryEnum.NONE;
    private final String username = "abc0_";
    private final String mail = "abc@abc.com";
    private final ProfileAppConstant profile = ProfileAppConstant.ADMIN;
    @Rule
    public ExpectedException rule = ExpectedException.none();
    private String token = null;
    @Autowired
    private SceneryController sceneryController;

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

        sceneryController.save(savedUser.getToken(), null);
    }

    @Test
    public void shouldReturnExceptionWhenInvalidTokenToActionSave() throws AuthenticationException {
        rule.expect(AuthenticationException.class);

        sceneryController.save("abc", null);
    }

    @Test
    public void shouldReturnBadRequestWhenSceneryNullToActionSave() throws AuthenticationException {
        ResponseEntity<Scenery> responseEntity = sceneryController.save(token, null);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void shouldReturnExceptionWhenInvalidSceneryToActionSave() throws AuthenticationException {
        rule.expect(ValidatorException.class);

        Scenery scenery = Scenery.builder().wonTitle(title).wonReward(reward).build();
        sceneryController.save(token, scenery);
    }

    @Test
    public void shouldReturnSuccessWhenValidSceneryToActionSave() throws AuthenticationException {
        Scenery scenery = Scenery.builder().name(name).location(location).wonTitle(title).wonReward(reward).build();

        ResponseEntity<Scenery> responseEntity = sceneryController.save(token, scenery);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.CREATED));
        Scenery body = responseEntity.getBody();
        assertNotNull(body);
        assertNotNull(body.getPk());
        assertThat(body.getName(), equalTo(name));
        assertThat(body.getWonReward(), equalTo(reward));
        assertThat(body.getWonTitle(), equalTo(title));
        assertThat(body.getVersion(), equalTo(DEFAULT_VERSION));
        assertThat(body.getWonReward().getVersion(), equalTo(DEFAULT_VERSION));
    }

    @Test
    public void shouldReturnExceptionWhenInvalidTokenToActionGetAll() throws AuthenticationException {
        rule.expect(AuthenticationException.class);

        sceneryController.getAll("abc");
    }

    @Test
    public void shouldReturnNotContentWhenNoScenerysFounds() throws AuthenticationException {
        ResponseEntity<List<Scenery>> responseEntity = sceneryController.getAll(token);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.NO_CONTENT));
        assertNull(responseEntity.getBody());
    }

    @Test
    public void shouldReturnSuccessWhenExistsSceneryToActionGetAll() throws AuthenticationException {
        Scenery scenery = Scenery.builder().name(name).location(location).wonTitle(title).wonReward(reward).build();

        sceneryController.save(token, scenery);

        ResponseEntity<List<Scenery>> responseEntity = sceneryController.getAll(token);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
        List<Scenery> body = responseEntity.getBody();
        assertNotNull(body);
        assertThat(body, hasSize(1));
        assertThat(body, hasItem(scenery));
    }

}