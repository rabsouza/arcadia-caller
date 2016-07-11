package br.com.battista.arcadia.caller.service;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.*;
import org.junit.rules.*;
import org.junit.runner.*;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.appengine.repackaged.com.google.common.collect.Lists;

import br.com.battista.arcadia.caller.constants.EntityConstant;
import br.com.battista.arcadia.caller.constants.ProfileAppConstant;
import br.com.battista.arcadia.caller.exception.RepositoryException;
import br.com.battista.arcadia.caller.exception.ValidatorException;
import br.com.battista.arcadia.caller.model.User;
import br.com.battista.arcadia.caller.repository.UserRepository;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    private final String username = "abc";
    private final String mail = "abc@abc.com";
    private final ProfileAppConstant profile = ProfileAppConstant.APP;

    @Rule
    public ExpectedException rule = ExpectedException.none();

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void shouldGetAllUsers() {
        User user = User.builder().username(username).mail(mail).profile(profile).build();
        when(userRepository.findAll()).thenReturn(Lists.newArrayList(user));

        List<User> users = userService.getAllUsers();
        assertNotNull(users);
        assertThat(users, hasSize(1));
        assertThat(users.iterator().next().getUsername(), equalTo(username));

    }

    @Test
    public void shouldGetUserByUsername() {
        User user = User.builder().id(1l).username(username).mail(mail).profile(profile).build();
        user.initEntity();
        when(userRepository.findByUsername(Matchers.anyString())).thenReturn(user);

        User userFind = userService.getUserByUsername(username);
        assertNotNull(userFind);
        assertNotNull(userFind.getPk());
        assertNotNull(userFind.getCreatedAt());
        assertThat(userFind.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));
    }

    @Test
    public void shouldSaveUserWhenUserValid() {
        User user = User.builder().id(1l).username(username).mail(mail).profile(profile).build();
        user.initEntity();
        when(userRepository.saveOrUpdateUser((User) org.mockito.Matchers.any())).thenReturn(user);

        User savedUser = userService.saveUser(user);
        assertNotNull(savedUser);
        assertNotNull(savedUser.getCreatedAt());
        assertThat(savedUser.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));
        assertNotNull(savedUser.getId());
    }

    @Test
    public void shouldReturnExceptionWhenUserInvalid() {
        doThrow(ValidatorException.class).when(userRepository).saveOrUpdateUser((User) org.mockito.Matchers.any());

        rule.expect(ValidatorException.class);

        userService.saveUser(new User());
    }

    @Test
    public void shouldReturnExceptionWhenUserNull() {
        doThrow(RepositoryException.class).when(userRepository).saveOrUpdateUser((User) org.mockito.Matchers.any());

        rule.expect(RepositoryException.class);

        userService.saveUser(null);
    }

}