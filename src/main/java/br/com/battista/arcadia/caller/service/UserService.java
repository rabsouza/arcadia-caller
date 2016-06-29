package br.com.battista.arcadia.caller.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.battista.arcadia.caller.model.User;
import br.com.battista.arcadia.caller.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        log.info("Find all users!");
        return userRepository.findAll();
    }

    public User saveUser(User user) {
        log.info("Create new user!");
        return userRepository.saveOrUpdateUser(user);
    }

}
