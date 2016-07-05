package br.com.battista.arcadia.caller.repository;

import java.text.MessageFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.appengine.repackaged.com.google.api.client.util.Strings;
import com.googlecode.objectify.Objectify;

import br.com.battista.arcadia.caller.constants.EntityConstant;
import br.com.battista.arcadia.caller.exception.RepositoryException;
import br.com.battista.arcadia.caller.model.User;
import br.com.battista.arcadia.caller.utils.MD5HashingUtils;
import br.com.battista.arcadia.caller.validator.EntityValidator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class UserRepository {

    @Autowired
    private EntityValidator entityValidator;

    @Autowired
    private Objectify objectifyRepository;

    public List<User> findAll() {
        log.info("Find all user!");

        return objectifyRepository.load()
                       .type(User.class)
                       .order("-updatedAt")
                       .list();

    }

    public User findByToken(String token) {
        if (Strings.isNullOrEmpty(token)) {
            throw new RepositoryException("Token can not be null!");
        }
        log.info("Find user by token: {}!", token);

        return objectifyRepository
                       .load()
                       .type(User.class)
                       .filter("token", token)
                       .first()
                       .now();

    }

    public User findByUsername(String username) {
        if (Strings.isNullOrEmpty(username)) {
            throw new RepositoryException("Username can not be null!");
        }
        log.info("Find user by username: {}!", username);

        return objectifyRepository
                       .load()
                       .type(User.class)
                       .filter("username", username)
                       .first()
                       .now();

    }

    public User saveOrUpdateUser(User user) {
        if (user == null) {
            throw new RepositoryException("User entity can not be null!");
        }
        entityValidator.validate(user);
        checkValidUsername(user.getUsername());

        user.initEntity();
        user.setToken(generateToken(user.getMail(), user.getUsername()));
        user.setUrlAvatar(generateUrlGravatar(user.getMail()));
        log.info("Save to user: {}!", user);

        objectifyRepository.save()
                .entity(user)
                .now();

        return user;
    }

    public void checkValidUsername(String username) {
        if (Strings.isNullOrEmpty(username)) {
            throw new RepositoryException("Username can not be null!");
        }
        String regex = "^[A-Za-z0-9_]{5,30}$";
        if (!username.matches(regex)) {
            throw new RepositoryException("Invalid username! Size: [5-30], Chars: [A-Za-z0-9_]");
        }
    }

    private String generateToken(String mail, String username) {
        if (Strings.isNullOrEmpty(mail) || Strings.isNullOrEmpty(username)) {
            throw new RepositoryException("Mail or username can not be null!");
        }
        return MD5HashingUtils.generateHash(username.concat(mail));
    }

    private String generateUrlGravatar(String mail) {
        if (Strings.isNullOrEmpty(mail)) {
            throw new RepositoryException("Mail can not be null!");
        }
        String hash = MD5HashingUtils.generateHash(mail.toLowerCase());
        return MessageFormat.format(EntityConstant.GRAVATAR_URL_TEMPLATE, hash);
    }

}
