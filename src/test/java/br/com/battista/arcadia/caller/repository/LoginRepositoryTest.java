package br.com.battista.arcadia.caller.repository;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.*;
import org.junit.rules.*;
import org.junit.runner.*;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.battista.arcadia.caller.constants.EntityConstant;
import br.com.battista.arcadia.caller.exception.RepositoryException;
import br.com.battista.arcadia.caller.model.User;

@RunWith(MockitoJUnitRunner.class)
public class LoginRepositoryTest extends BaseRepositoryConfigTest {

    private final String userName = "abc";
    private final String mail = "abc@abc.com";

    @Rule
    public ExpectedException rule = ExpectedException.none();

    @InjectMocks
    private LoginRepository loginRepository;

    @Test
    public void shouldEmptyUsersWhenEmptyDataBase() {
        List<User> users = loginRepository.findAll();
        assertNotNull(users);
        assertThat(users, hasSize(0));
    }

    @Test
    public void shouldReturnUsersWhenFindAllUsers() {
        User user = User.builder().user(userName).build();
        objectifyRepository.save().entity(user).now();

        List<User> users = loginRepository.findAll();
        assertNotNull(users);
        assertThat(users, hasSize(1));
        assertThat(users.iterator().next().getUser(), equalTo(userName));
    }

    @Test
    public void shouldSaveUserWhenValidUser() {
        User user = User.builder().user(userName).mail(mail).build();

        User savedUser = loginRepository.saveOrUpdateUser(user);
        assertNotNull(savedUser);
        assertNotNull(savedUser.getCreatedAt());
        assertThat(savedUser.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));
        assertNotNull(savedUser.getId());
    }

    @Test
    public void shouldThrowExceptionWhenNullUser() {
        rule.expect(RepositoryException.class);
        rule.expectMessage(containsString("not be null!"));

        loginRepository.saveOrUpdateUser(null);
    }

    @Test
    public void shouldThrowExceptionWhenInvalidUser() {
        rule.expect(RepositoryException.class);
        rule.expectMessage(containsString("not be null!"));

        loginRepository.saveOrUpdateUser(new User());
    }

}