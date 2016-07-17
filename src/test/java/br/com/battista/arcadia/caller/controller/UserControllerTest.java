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
import br.com.battista.arcadia.caller.exception.EntityAlreadyExistsException;
import br.com.battista.arcadia.caller.exception.EntityNotFoundException;
import br.com.battista.arcadia.caller.exception.ValidatorException;
import br.com.battista.arcadia.caller.model.User;
import br.com.battista.arcadia.caller.repository.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class UserControllerTest extends BaseControllerConfig {

    private final String mail = "teste@teste.com";
    private final String username = "teste";
    private final String invalidToken = "12345";
    private final String mail02 = "mail02@mail.com";
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

        User user = User.builder().username("profile").mail("profile@profile").profile(profile).build();

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
    public void shouldReturnBadRequestWhenValidTokenAndNullUserToActionSave() throws AuthenticationException {
        ResponseEntity<User> responseEntity = userController.save(token, null);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void shouldReturnBadRequestWhenValidTokenAndNullUserToActionUpdate() throws AuthenticationException {
        ResponseEntity<User> responseEntity = userController.update(token, null);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void shouldReturnBadRequestWhenValidTokenAndNullUserToActionDelete() throws AuthenticationException {
        ResponseEntity<User> responseEntity = userController.update(token, null);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertNotNull(responseEntity.getBody());
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

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.CREATED));
        User body = responseEntity.getBody();
        assertNotNull(body);
        assertNotNull(body.getPk());
        assertThat(body.getUsername(), equalTo(username));
        assertThat(body.getMail(), equalTo(mail));
        assertThat(body.getVersion(), equalTo(DEFAULT_VERSION));
    }

    @Test
    public void shouldReturnSuccessWhenValidUserToActionUpdate() throws AuthenticationException {
        User user = User.builder().username(username).mail(mail).profile(profile).build();

        ResponseEntity<User> responseEntity = userController.save(token, user);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.CREATED));
        User body = responseEntity.getBody();
        assertNotNull(body);
        assertNotNull(body.getPk());
        assertThat(body.getUsername(), equalTo(username));
        assertThat(body.getMail(), equalTo(mail));
        assertThat(body.getVersion(), equalTo(DEFAULT_VERSION));

        body.setMail(mail02);
        ResponseEntity<User> responseEntityUpdate = userController.update(token, body);
        assertThat(responseEntityUpdate.getStatusCode(), equalTo(HttpStatus.OK));
        User bodyUpdate = responseEntityUpdate.getBody();
        assertNotNull(bodyUpdate);
        assertNotNull(bodyUpdate.getPk());
        assertThat(bodyUpdate.getUsername(), equalTo(username));
        assertThat(bodyUpdate.getMail(), equalTo(mail02));
        assertThat(bodyUpdate.getVersion(), not(equalTo(DEFAULT_VERSION)));
    }

    @Test
    public void shouldReturnExceptionWhenValidUserAndDifferentVersionToActionUpdate() throws AuthenticationException {
        rule.expect(EntityAlreadyExistsException.class);

        User user = User.builder().username(username).mail(mail).profile(profile).build();

        ResponseEntity<User> responseEntity = userController.save(token, user);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.CREATED));
        User body = responseEntity.getBody();
        assertNotNull(body);
        assertNotNull(body.getPk());
        assertThat(body.getUsername(), equalTo(username));
        assertThat(body.getMail(), equalTo(mail));
        assertThat(body.getVersion(), equalTo(DEFAULT_VERSION));

        User user02 = User.builder().username(username).mail(mail02).profile(profile).build();
        user02.initEntity();
        user02.updateEntity();
        userController.update(token, user02);
    }

    @Test
    public void shouldReturnSuccessWhenValidUserToActionDelete() throws AuthenticationException {
        User user = User.builder().username(username).mail(mail).profile(profile).build();

        ResponseEntity<User> responseEntity = userController.save(token, user);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.CREATED));
        User body = responseEntity.getBody();
        assertNotNull(body);
        assertNotNull(body.getPk());
        assertThat(body.getUsername(), equalTo(username));
        assertThat(body.getMail(), equalTo(mail));
        assertThat(body.getVersion(), equalTo(DEFAULT_VERSION));

        body.setMail(mail02);
        ResponseEntity<User> responseEntityUpdate = userController.delete(token, body);
        assertThat(responseEntityUpdate.getStatusCode(), equalTo(HttpStatus.OK));
        User bodyDelete = responseEntityUpdate.getBody();
        assertNull(bodyDelete);
    }

    @Test
    public void shouldReturnExceptionWhenInvalidUsernameToActionDelete() throws AuthenticationException {
        rule.expect(EntityNotFoundException.class);

        User user = User.builder().username(username).mail(mail).profile(profile).build();

        ResponseEntity<User> responseEntity = userController.save(token, user);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.CREATED));
        User body = responseEntity.getBody();
        assertNotNull(body);
        assertNotNull(body.getPk());
        assertThat(body.getUsername(), equalTo(username));
        assertThat(body.getMail(), equalTo(mail));
        assertThat(body.getVersion(), equalTo(DEFAULT_VERSION));

        User user02 = User.builder().username("user02").mail(mail).profile(profile).build();
        user02.initEntity();
        user02.updateEntity();
        userController.delete(token, user02);
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