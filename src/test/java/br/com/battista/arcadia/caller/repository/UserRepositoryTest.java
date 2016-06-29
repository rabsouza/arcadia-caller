package br.com.battista.arcadia.caller.repository;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.*;
import org.junit.rules.*;
import org.junit.runner.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.battista.arcadia.caller.constants.EntityConstant;
import br.com.battista.arcadia.caller.exception.RepositoryException;
import br.com.battista.arcadia.caller.exception.ValidatorException;
import br.com.battista.arcadia.caller.model.BaseEntity;
import br.com.battista.arcadia.caller.model.User;
import br.com.battista.arcadia.caller.validator.EntityValidator;

@RunWith(MockitoJUnitRunner.class)
public class UserRepositoryTest extends BaseRepositoryConfig {

    private final String userName = "abc";
    private final String mail = "abc@abc.com";

    @Rule
    public ExpectedException rule = ExpectedException.none();

    @InjectMocks
    private UserRepository userRepository;

    @Mock
    private EntityValidator entityValidator;

    @Test
    public void shouldEmptyUsersWhenEmptyDataBase() {
        List<User> users = userRepository.findAll();
        assertNotNull(users);
        assertThat(users, hasSize(0));
    }

    @Test
    public void shouldReturnUsersWhenFindAllUsers() {
        User user = User.builder().user(userName).build();
        objectifyRepository.save().entity(user).now();

        List<User> users = userRepository.findAll();
        assertNotNull(users);
        assertThat(users, hasSize(1));
        assertThat(users.iterator().next().getUser(), equalTo(userName));
    }

    @Test
    public void shouldSaveUserWhenValidUser() {
        User user = User.builder().user(userName).mail(mail).build();

        User savedUser = userRepository.saveOrUpdateUser(user);
        assertNotNull(savedUser);
        assertNotNull(savedUser.getCreatedAt());
        assertThat(savedUser.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));
        assertNotNull(savedUser.getId());
    }

    @Test
    public void shouldThrowExceptionWhenNullUser() {
        rule.expect(RepositoryException.class);
        rule.expectMessage(containsString("not be null!"));

        userRepository.saveOrUpdateUser(null);
    }

    @Test
    public void shouldThrowExceptionWhenInvalidUser() {
        doThrow(ValidatorException.class).when(entityValidator).validate((BaseEntity) anyObject());

        rule.expect(ValidatorException.class);

        userRepository.saveOrUpdateUser(new User());
    }

}