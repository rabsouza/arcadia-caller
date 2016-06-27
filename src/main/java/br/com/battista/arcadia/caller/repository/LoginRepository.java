package br.com.battista.arcadia.caller.repository;

import java.util.List;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.googlecode.objectify.Objectify;

import br.com.battista.arcadia.caller.exception.RepositoryException;
import br.com.battista.arcadia.caller.model.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class LoginRepository {

    @Autowired
    private Validator validator;

    @Autowired
    private Objectify objectifyRepository;

    @Cacheable(value = "users", key = "#user.user")
    public List<User> findAll() {
        log.info("Find all user!");

        return objectifyRepository.load()
                       .type(User.class)
                       .order("-updatedAt")
                       .list();

    }

    @CachePut(value = "users", key = "#user.user")
    public User saveOrUpdateUser(User user){
        if (user == null){
            throw new RepositoryException("User entity can not be null!");
        }
        validator.validate(user);

        user.initEntity();
        log.info(String.format("Save to user: %s!", user));

        objectifyRepository.save()
                .entity(user)
                .now();

        return user;
    }


}
