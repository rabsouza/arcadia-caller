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

import br.com.battista.arcadia.caller.constants.ProfileAppConstant;
import br.com.battista.arcadia.caller.exception.AuthenticationException;
import br.com.battista.arcadia.caller.model.User;
import br.com.battista.arcadia.caller.repository.UserRepository;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceTest {

    private final String username = "abc";
    private final ProfileAppConstant profile = ProfileAppConstant.APP;
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
        User user = User.builder().id(1l).username(username).mail(mail).profile(profile).build();
        user.initEntity();

        when(userRepository.findByToken(anyString())).thenReturn(user);

        authenticationService.authetication(token);
        verify(userRepository, times(1)).findByToken(anyString());
    }

    @Test
    public void shouldAutheticationUserWhenValidTokenAndInvalidProfile() throws AuthenticationException {
        rule.expect(AuthenticationException.class);
        rule.expectMessage(containsString("Invalid Profile to action."));

        User user = User.builder().id(1l).username(username).mail(mail).profile(profile).build();
        user.initEntity();

        when(userRepository.findByToken(anyString())).thenReturn(user);

        authenticationService.authetication(token, ProfileAppConstant.ADMIN);
    }

    @Test
    public void shouldAutheticationUserWhenValidTokenAndValidProfile() throws AuthenticationException {
        User user = User.builder().id(1l).username(username).mail(mail).profile(profile).build();
        user.initEntity();

        when(userRepository.findByToken(anyString())).thenReturn(user);

        authenticationService.authetication(token, ProfileAppConstant.APP);
        verify(userRepository, times(1)).findByToken(anyString());
    }

    @Test
    public void shouldAutheticationUserWhenValidTokenAndAllProfile() throws AuthenticationException {
        User user = User.builder().id(1l).username(username).mail(mail).profile(profile).build();
        user.initEntity();

        when(userRepository.findByToken(anyString())).thenReturn(user);

        authenticationService.authetication(token, ProfileAppConstant.APP, ProfileAppConstant.ADMIN);
        verify(userRepository, times(1)).findByToken(anyString());
    }

}