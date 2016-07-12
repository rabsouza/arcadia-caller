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
import br.com.battista.arcadia.caller.model.User;
import br.com.battista.arcadia.caller.repository.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class UserControllerTest extends BaseControllerConfig {

    private final String mail = "teste@teste.com";
    private final String username = "teste";
    private final String invalidToken = "12345";
    private String token;
    private final ProfileAppConstant profile = ProfileAppConstant.ADMIN;

    @Rule
    public ExpectedException rule = ExpectedException.none();

    @Autowired
    private UserController userController;

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

        userController.save(invalidToken, null);
    }

    @Test
    public void shouldReturnExceptionWhenInvalidUserToActionSave() throws AuthenticationException {
        rule.expect(ValidatorException.class);

        User user = User.builder().username(username).build();
        userController.save(token, user);
    }

    @Test
    public void shouldReturnExceptionWhenNullUserToActionSave() throws AuthenticationException {
        ResponseEntity<User> responseEntity = userController.save(token, null);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void shouldReturnSuccessWhenValidUserToActionSave() throws AuthenticationException {
        User user = User.builder().username(username).mail(mail).profile(profile).build();

        ResponseEntity<User> responseEntity = userController.save(token, user);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
        User body = responseEntity.getBody();
        assertNotNull(body);
        assertNotNull(body.getPk());
        assertThat(body.getUsername(), equalTo(username));
        assertThat(body.getMail(), equalTo(mail));
        assertThat(body.getVersion(), equalTo(DEFAULT_VERSION));
    }

    @Test
    public void shouldReturnExceptionWhenInvalidProfileToActionGetAll() throws AuthenticationException {
        rule.expect(AuthenticationException.class);

        userController.getAll(invalidToken);
    }

    @Test
    public void shouldReturnSuccessWhenExistsUserToActionGetAll() throws AuthenticationException {
        User user = User.builder().username(username).mail(mail).profile(profile).build();

        userController.save(token, user);

        ResponseEntity<List<User>> responseEntity = userController.getAll(token);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
        List<User> body = responseEntity.getBody();
        assertNotNull(body);
        assertThat(body, hasSize(2));
        assertThat(body, hasItem(user));
    }

}