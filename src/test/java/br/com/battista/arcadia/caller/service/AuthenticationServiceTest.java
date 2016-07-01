package br.com.battista.arcadia.caller.service;

import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import org.junit.*;
import org.junit.rules.*;
import org.junit.runner.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.battista.arcadia.caller.exception.AuthenticationException;
import br.com.battista.arcadia.caller.model.User;
import br.com.battista.arcadia.caller.repository.UserRepository;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceTest {

    private final String userName = "abc";
    private final String mail = "abc@abc.com";
    private final String token = "123456";

    @Rule
    public ExpectedException rule = ExpectedException.none();

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private UserRepository userRepository;

    @Before
    public void setup() {
        authenticationService.setup();
    }

    @Test
    public void shouldReturnExceptionWhenNullToken() throws AuthenticationException {
        rule.expect(AuthenticationException.class);
        rule.expectMessage(containsString("Header param 'token' can not be null!"));

        authenticationService.authetication(null);

    }

    @Test
    public void shouldReturnExceptionWhenEmptyToken() throws AuthenticationException {
        rule.expect(AuthenticationException.class);
        rule.expectMessage(containsString("Header param 'token' can not be null!"));

        authenticationService.authetication("");

    }

    @Test
    public void shouldReturnExceptionWhenInvalidToken() throws AuthenticationException {
        rule.expect(AuthenticationException.class);
        rule.expectMessage(containsString("Invalid token."));

        authenticationService.authetication(token);

    }

    @Test
    public void shouldAutheticationUserWhenValidToken() throws AuthenticationException {
        User user = User.builder().id(1l).user(userName).mail(mail).build();
        user.initEntity();

        when(userRepository.findByToken(anyString())).thenReturn(user);

        authenticationService.authetication(token);
        verify(userRepository.findByToken(anyString()), times(1));
    }

}