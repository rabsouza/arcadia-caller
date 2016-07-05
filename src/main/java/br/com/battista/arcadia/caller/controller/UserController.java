package br.com.battista.arcadia.caller.controller;

import static br.com.battista.arcadia.caller.builder.ResponseEntityBuilder.buildResponseErro;
import static br.com.battista.arcadia.caller.builder.ResponseEntityBuilder.buildResponseSuccess;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import br.com.battista.arcadia.caller.constants.RestControllerConstant;
import br.com.battista.arcadia.caller.exception.AuthenticationException;
import br.com.battista.arcadia.caller.model.User;
import br.com.battista.arcadia.caller.service.AuthenticationService;
import br.com.battista.arcadia.caller.service.UserService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationService authenticationService;

    @RequestMapping(value = "/", method = RequestMethod.GET,
            produces = RestControllerConstant.PRODUCES)
    @ResponseBody
    public ResponseEntity<List<User>> getAll(@RequestHeader("profile") String profile) throws AuthenticationException {
        authenticationService.validHeader(profile);

        log.info("Retrieve all users!");
        List<User> users = userService.getAllUsers();

        if (users == null || users.isEmpty()) {
            log.warn("No users founds!");
            return buildResponseErro(HttpStatus.NO_CONTENT);
        } else {
            log.info("Found {} users!", users.size());
            return buildResponseSuccess(users, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.POST,
            produces = RestControllerConstant.PRODUCES, consumes = RestControllerConstant.CONSUMES)
    @ResponseBody
    public ResponseEntity<User> save(@RequestHeader("profile") String profile, @RequestBody User user) throws AuthenticationException {
        authenticationService.validHeader(profile);

        if (user == null) {
            log.warn("User can not be null!");
            return buildResponseErro("User is required!");
        }

        log.info("Save the user[{}]!", user);
        User newUser = userService.saveUser(user);
        log.debug("Save the user and generate to id: {}!", newUser.getId());
        return buildResponseSuccess(newUser, HttpStatus.OK);
    }

}
