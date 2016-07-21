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
import br.com.battista.arcadia.caller.model.User;
import br.com.battista.arcadia.caller.model.enuns.GroupCardEnum;
import br.com.battista.arcadia.caller.model.enuns.TypeCardEnum;
import br.com.battista.arcadia.caller.repository.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class CardControllerTest extends BaseControllerConfig {

    private final String name = "card01";
    private final TypeCardEnum type = TypeCardEnum.UPGRADE;
    private final GroupCardEnum group = GroupCardEnum.NONE;
    private final String username = "abc0_";
    private final String mail = "abc@abc.com";
    private final ProfileAppConstant profile = ProfileAppConstant.ADMIN;
    @Rule
    public ExpectedException rule = ExpectedException.none();
    private String token = null;
    @Autowired
    private CardController cardController;

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

        cardController.save(savedUser.getToken(), null);
    }

    @Test
    public void shouldReturnExceptionWhenInvalidTokenToActionSave() throws AuthenticationException {
        rule.expect(AuthenticationException.class);

        cardController.save("abc", null);
    }

    @Test
    public void shouldReturnBadRequestWhenCardNullToActionSave() throws AuthenticationException {
        ResponseEntity<Card> responseEntity = cardController.save(token, null);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void shouldReturnExceptionWhenInvalidCardToActionSave() throws AuthenticationException {
        rule.expect(ValidatorException.class);

        Card card = Card.builder().name(name).build();
        cardController.save(token, card);
    }

    @Test
    public void shouldReturnSuccessWhenValidCardToActionSave() throws AuthenticationException {
        Card card = Card.builder().name(name).type(type).group(group).build();

        ResponseEntity<Card> responseEntity = cardController.save(token, card);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.CREATED));
        Card body = responseEntity.getBody();
        assertNotNull(body);
        assertNotNull(body.getPk());
        assertThat(body.getName(), equalTo(name));
        assertThat(body.getType(), equalTo(type));
        assertThat(body.getGroup(), equalTo(group));
        assertThat(body.getVersion(), equalTo(DEFAULT_VERSION));
    }

    @Test
    public void shouldReturnExceptionWhenInvalidTokenToActionGetAll() throws AuthenticationException {
        rule.expect(AuthenticationException.class);

        cardController.getAll("abc");
    }

    @Test
    public void shouldReturnNotContentWhenNoCardsFounds() throws AuthenticationException {
        ResponseEntity<List<Card>> responseEntity = cardController.getAll(token);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.NO_CONTENT));
        assertNull(responseEntity.getBody());
    }

    @Test
    public void shouldReturnSuccessWhenExistsCardToActionGetAll() throws AuthenticationException {
        Card card = Card.builder().name(name).type(type).group(group).build();

        cardController.save(token, card);

        ResponseEntity<List<Card>> responseEntity = cardController.getAll(token);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
        List<Card> body = responseEntity.getBody();
        assertNotNull(body);
        assertThat(body, hasSize(1));
        assertThat(body, hasItem(card));
    }

}