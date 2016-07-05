package br.com.battista.arcadia.caller.controller;

import static br.com.battista.arcadia.caller.constants.EntityConstant.DEFAULT_VERSION;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Map;

import org.junit.*;
import org.junit.rules.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.com.battista.arcadia.caller.config.AppConfig;
import br.com.battista.arcadia.caller.exception.AuthenticationException;
import br.com.battista.arcadia.caller.exception.ValidatorException;
import br.com.battista.arcadia.caller.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class LoginControllerTest extends BaseControllerConfig {

    private final String mail = "teste@teste.com";
    private final String username = "teste";

    @Autowired
    private LoginController loginController;
    @Rule
    public ExpectedException rule = ExpectedException.none();


    @Test
    public void shouldReturnBadRequestWhenUserNullToActionSave() throws AuthenticationException {
        ResponseEntity<User> responseEntity = loginController.create(null);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void shouldReturnExceptionWhenInvalidUserToActionSave() throws AuthenticationException {
        rule.expect(ValidatorException.class);

        User user = User.builder().username(username).build();
        loginController.create(user);
    }

    @Test
    public void shouldReturnSuccessWhenValidUserToActionSave() throws AuthenticationException {
        User user = User.builder().username(username).mail(mail).build();

        ResponseEntity<User> responseEntity = loginController.create(user);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
        User body = responseEntity.getBody();
        assertNotNull(body);
        assertNotNull(body.getPk());
        assertThat(body.getUsername(), equalTo(username));
        assertThat(body.getMail(), equalTo(mail));
        assertThat(body.getVersion(), equalTo(DEFAULT_VERSION));
    }

    @Test
    public void shouldReturnBadRequestWhenUsernameNullToActionLogin() throws AuthenticationException {
        ResponseEntity<Map<String, String>> responseEntity = loginController.login(null);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void shouldReturnNotFoundWhenInvalidUsernameToActionLogin() throws AuthenticationException {
        ResponseEntity<Map<String, String>> responseEntity = loginController.login(username);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
        assertNull(responseEntity.getBody());
    }

    @Test
    public void shouldReturnSuccessWhenValidUserToActionLogin() throws AuthenticationException {
        User user = User.builder().username(username).mail(mail).build();

        loginController.create(user);

        ResponseEntity<Map<String, String>> responseEntity = loginController.login(username);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
        Map<String, String> body = responseEntity.getBody();
        assertNotNull(body);
        assertThat(body.get("token"), not(isEmptyOrNullString()));
    }


}