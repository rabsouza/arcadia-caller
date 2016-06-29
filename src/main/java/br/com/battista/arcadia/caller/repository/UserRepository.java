package br.com.battista.arcadia.caller.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.appengine.repackaged.com.google.api.client.util.Strings;
import com.googlecode.objectify.Objectify;

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

    public User saveOrUpdateUser(User user) {
        if (user == null) {
            throw new RepositoryException("User entity can not be null!");
        }
        entityValidator.validate(user);

        user.initEntity();
        user.setToken(generateToken(user.getMail(), user.getUser()));
        log.info("Save to user: {}!", user);

        objectifyRepository.save()
                .entity(user)
                .now();

        return user;
    }

    private String generateToken(String mail, String user) {
        if (Strings.isNullOrEmpty(mail) || Strings.isNullOrEmpty(user)) {
            throw new RepositoryException("Mail or user can not be null!");
        }
        return MD5HashingUtils.generateHash(user.concat(mail));
    }


}
