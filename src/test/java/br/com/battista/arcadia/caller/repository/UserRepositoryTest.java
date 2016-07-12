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
import br.com.battista.arcadia.caller.constants.ProfileAppConstant;
import br.com.battista.arcadia.caller.exception.EntityAlreadyExistsException;
import br.com.battista.arcadia.caller.exception.EntityNotFoundException;
import br.com.battista.arcadia.caller.exception.RepositoryException;
import br.com.battista.arcadia.caller.exception.ValidatorException;
import br.com.battista.arcadia.caller.model.BaseEntity;
import br.com.battista.arcadia.caller.model.User;
import br.com.battista.arcadia.caller.validator.EntityValidator;

@RunWith(MockitoJUnitRunner.class)
public class UserRepositoryTest extends BaseRepositoryConfig {

    private final String username = "abc0_";
    private final String mail = "abc@abc.com";
    private final ProfileAppConstant profile = ProfileAppConstant.APP;
    private final String mail02 = "mail02@abc.com";

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
        User user = User.builder().username(username).build();
        objectifyRepository.save().entity(user).now();

        List<User> users = userRepository.findAll();
        assertNotNull(users);
        assertThat(users, hasSize(1));
        assertThat(users.iterator().next().getUsername(), equalTo(username));
    }

    @Test
    public void shouldSaveUserWhenValidUser() {
        User user = User.builder().username(username).mail(mail).profile(profile).build();

        User savedUser = userRepository.saveOrUpdateUser(user);
        assertNotNull(savedUser);
        assertNotNull(savedUser.getPk());
        assertNotNull(savedUser.getCreatedAt());
        assertThat(savedUser.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));
    }

    @Test
    public void shouldFindByTokenWhenValidUserAndValidToken() {
        User user = User.builder().username(username).mail(mail).profile(profile).build();

        User savedUser = userRepository.saveOrUpdateUser(user);
        assertNotNull(savedUser);
        assertNotNull(savedUser.getPk());
        assertNotNull(savedUser.getCreatedAt());
        assertThat(savedUser.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));

        User userFind = userRepository.findByToken(user.getToken());
        assertNotNull(userFind);
        assertThat(userFind.getPk(), equalTo(savedUser.getPk()));
        assertThat(userFind.getVersion(), equalTo(savedUser.getVersion()));
        assertThat(userFind.getToken(), equalTo(savedUser.getToken()));
    }

    @Test
    public void shouldReturnNullWhenFindByTokenWithValidUserAndInvalidToken() {
        User user = User.builder().username(username).mail(mail).profile(profile).build();

        User savedUser = userRepository.saveOrUpdateUser(user);
        assertNotNull(savedUser);
        assertNotNull(savedUser.getPk());
        assertNotNull(savedUser.getCreatedAt());
        assertThat(savedUser.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));

        User userFind = userRepository.findByToken("abc");
        assertNull(userFind);
    }

    @Test
    public void shouldFindByUsernameWhenValidUserAndValidUsername() {
        User user = User.builder().username(username).mail(mail).profile(profile).build();

        User savedUser = userRepository.saveOrUpdateUser(user);
        assertNotNull(savedUser);
        assertNotNull(savedUser.getPk());
        assertNotNull(savedUser.getCreatedAt());
        assertThat(savedUser.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));

        User userFind = userRepository.findByUsername(user.getUsername());
        assertNotNull(userFind);
        assertThat(userFind.getPk(), equalTo(savedUser.getPk()));
        assertThat(userFind.getVersion(), equalTo(savedUser.getVersion()));
        assertThat(userFind.getUsername(), equalTo(savedUser.getUsername()));
    }

    @Test
    public void shouldReturnNullWhenFindByUsernameWithValidUserAndInvalidUsername() {
        User user = User.builder().username(username).mail(mail).profile(profile).build();

        User savedUser = userRepository.saveOrUpdateUser(user);
        assertNotNull(savedUser);
        assertNotNull(savedUser.getPk());
        assertNotNull(savedUser.getCreatedAt());
        assertThat(savedUser.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));

        User userFind = userRepository.findByUsername("abcd");
        assertNull(userFind);
    }

    @Test
    public void shouldThrowExceptionWhenFindByUsernameWithNullUser() {
        rule.expect(RepositoryException.class);
        rule.expectMessage(containsString("not be null!"));

        userRepository.findByUsername(null);
    }

    @Test
    public void shouldThrowExceptionWhenFindByUsernameWithEmptyUser() {
        rule.expect(RepositoryException.class);
        rule.expectMessage(containsString("not be null!"));

        userRepository.findByUsername("");
    }

    @Test
    public void shouldThrowExceptionWhenFindByTokenWithNullUser() {
        rule.expect(RepositoryException.class);
        rule.expectMessage(containsString("not be null!"));

        userRepository.findByToken(null);
    }

    @Test
    public void shouldThrowExceptionWhenFindByTokenWithEmptyUser() {
        rule.expect(RepositoryException.class);
        rule.expectMessage(containsString("not be null!"));

        userRepository.findByToken("");
    }

    @Test
    public void shouldThrowExceptionWhenSaveNullUser() {
        rule.expect(RepositoryException.class);
        rule.expectMessage(containsString("not be null!"));

        userRepository.saveOrUpdateUser(null);
    }

    @Test
    public void shouldThrowExceptionWhenSaveInvalidUser() {
        doThrow(ValidatorException.class).when(entityValidator).validate((BaseEntity) anyObject());

        rule.expect(ValidatorException.class);

        userRepository.saveOrUpdateUser(new User());
    }

    @Test
    public void shouldUpdateUserWhenValidUserAndValidUsername() {
        User user = User.builder().username(username).mail(mail).profile(profile).build();

        User savedUser = userRepository.saveOrUpdateUser(user);
        assertNotNull(savedUser);
        assertNotNull(savedUser.getPk());
        assertNotNull(savedUser.getCreatedAt());
        assertThat(savedUser.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));

        User userFind = userRepository.findByUsername(user.getUsername());
        assertNotNull(userFind);
        assertThat(userFind.getPk(), equalTo(savedUser.getPk()));
        assertThat(userFind.getVersion(), equalTo(savedUser.getVersion()));
        assertThat(userFind.getUsername(), equalTo(savedUser.getUsername()));

        User user02 = User.builder().username(username).mail(mail02).profile(profile).build();
        user02.initEntity();
        User updatedUser = userRepository.saveOrUpdateUser(user02);
        assertThat(updatedUser.getPk(), equalTo(userFind.getPk()));
        assertThat(updatedUser.getVersion(), equalTo(new Long(2L)));
        assertThat(updatedUser.getUsername(), equalTo(userFind.getUsername()));
        assertThat(updatedUser.getMail(),  equalTo(mail02));
    }

    @Test
    public void shouldReturnExceptionWhenUpdateUserWithDifferentVersion() {
        rule.expect(EntityAlreadyExistsException.class);

        User user = User.builder().username(username).mail(mail).profile(profile).build();

        User savedUser = userRepository.saveOrUpdateUser(user);
        assertNotNull(savedUser);
        assertNotNull(savedUser.getPk());
        assertNotNull(savedUser.getCreatedAt());
        assertThat(savedUser.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));

        User userFind = userRepository.findByUsername(user.getUsername());
        assertNotNull(userFind);
        assertThat(userFind.getPk(), equalTo(savedUser.getPk()));
        assertThat(userFind.getVersion(), equalTo(savedUser.getVersion()));
        assertThat(userFind.getUsername(), equalTo(savedUser.getUsername()));

        User user02 = User.builder().username(username).mail(mail02).profile(profile).build();
        user02.initEntity();
        user02.updateEntity();
        userRepository.saveOrUpdateUser(user02);
    }

    @Test
    public void shouldDeleteUserWhenValidUserAndValidUsername() {
        User user = User.builder().username(username).mail(mail).profile(profile).build();

        User savedUser = userRepository.saveOrUpdateUser(user);
        assertNotNull(savedUser);
        assertNotNull(savedUser.getPk());
        assertNotNull(savedUser.getCreatedAt());
        assertThat(savedUser.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));

        User userFind = userRepository.findByUsername(user.getUsername());
        assertNotNull(userFind);
        assertThat(userFind.getPk(), equalTo(savedUser.getPk()));
        assertThat(userFind.getVersion(), equalTo(savedUser.getVersion()));
        assertThat(userFind.getUsername(), equalTo(savedUser.getUsername()));

        User user02 = User.builder().username(username).mail(mail).profile(profile).build();
        userRepository.deleteByUsername(user02);
    }

    @Test
    public void shouldReturnExceptionWhenDeleteInvalidUsername() {
        rule.expect(EntityNotFoundException.class);

        User user = User.builder().username(username).mail(mail).profile(profile).build();

        User savedUser = userRepository.saveOrUpdateUser(user);
        assertNotNull(savedUser);
        assertNotNull(savedUser.getPk());
        assertNotNull(savedUser.getCreatedAt());
        assertThat(savedUser.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));

        User userFind = userRepository.findByUsername(user.getUsername());
        assertNotNull(userFind);
        assertThat(userFind.getPk(), equalTo(savedUser.getPk()));
        assertThat(userFind.getVersion(), equalTo(savedUser.getVersion()));
        assertThat(userFind.getUsername(), equalTo(savedUser.getUsername()));

        User user02 = User.builder().username("abc").mail(mail).profile(profile).build();
        userRepository.deleteByUsername(user02);
    }

    @Test
    public void shouldThrowExceptionWhenDeleteNullUser() {
        rule.expect(RepositoryException.class);
        rule.expectMessage(containsString("not be null!"));

        userRepository.deleteByUsername(null);
    }

}