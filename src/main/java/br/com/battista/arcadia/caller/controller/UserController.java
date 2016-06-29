package br.com.battista.arcadia.caller.controller;

import static br.com.battista.arcadia.caller.builder.ResponseEntityBuilder.buildResponseErro;
import static br.com.battista.arcadia.caller.builder.ResponseEntityBuilder.buildResponseSuccess;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.battista.arcadia.caller.constants.RestControllerConstant;
import br.com.battista.arcadia.caller.model.User;
import br.com.battista.arcadia.caller.service.UserService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/", method = RequestMethod.GET,
            produces = RestControllerConstant.PRODUCES)
    @ResponseBody
    public ResponseEntity<List<User>> getAll() {
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
    public ResponseEntity<User> save(@RequestBody User user) {
        if (user == null) {
            log.warn("User can not be null!");
            return buildResponseErro(HttpStatus.NOT_MODIFIED);
        }

        log.warn("Save to user[{}]!", user);
        User newUser = userService.saveUser(user);
        log.debug("Save user and generate to id: {}!", newUser.getId());
        return buildResponseSuccess(newUser, HttpStatus.OK);
    }

}
