package br.com.battista.arcadia.caller.controller;

import static br.com.battista.arcadia.caller.builder.ResponseEntityBuilder.buildResponseErro;
import static br.com.battista.arcadia.caller.builder.ResponseEntityBuilder.buildResponseSuccess;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.battista.arcadia.caller.constants.MessagePropertiesConstant;
import br.com.battista.arcadia.caller.constants.ProfileAppConstant;
import br.com.battista.arcadia.caller.constants.RestControllerConstant;
import br.com.battista.arcadia.caller.exception.AuthenticationException;
import br.com.battista.arcadia.caller.model.User;
import br.com.battista.arcadia.caller.service.AuthenticationService;
import br.com.battista.arcadia.caller.service.MessageCustomerService;
import br.com.battista.arcadia.caller.service.UserService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("api/v1/user")
public class UserController {

    public static final String USER_CAN_NOT_BE_NULL = "User can not be null!";
    public static final String USER_IS_REQUIRED = "User";

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private MessageCustomerService messageSource;

    @RequestMapping(value = "/", method = RequestMethod.GET,
            produces = RestControllerConstant.PRODUCES)
    @ResponseBody
    public ResponseEntity<List<User>> getAll(@RequestHeader("token") String token) throws AuthenticationException {
        authenticationService.authetication(token, ProfileAppConstant.ADMIN);

        log.info("Retrieve all users!");
        List<User> users = userService.getAllUsers();

        if (users == null || users.isEmpty()) {
            log.warn("No users found!");
            return buildResponseErro(HttpStatus.NO_CONTENT);
        } else {
            log.info("Found {} users!", users.size());
            return buildResponseSuccess(users, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.POST,
            produces = RestControllerConstant.PRODUCES, consumes = RestControllerConstant.CONSUMES)
    @ResponseBody
    public ResponseEntity<User> save(@RequestHeader("token") String token, @RequestBody User user) throws AuthenticationException {
        authenticationService.authetication(token, ProfileAppConstant.ADMIN);

        if (user == null) {
            log.warn(USER_CAN_NOT_BE_NULL);
            return buildResponseErro(messageSource.getMessage(MessagePropertiesConstant.MESSAGE_FIELD_IS_REQUIRED, USER_IS_REQUIRED));
        }

        log.info("Save the user[{}]!", user);
        User newUser = userService.saveUser(user);
        log.debug("Save the user and generate to id: {}!", newUser.getId());
        return buildResponseSuccess(newUser, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT,
            produces = RestControllerConstant.PRODUCES, consumes = RestControllerConstant.CONSUMES)
    @ResponseBody
    public ResponseEntity<User> update(@RequestHeader("token") String token, @RequestBody User user) throws AuthenticationException {
        authenticationService.authetication(token, ProfileAppConstant.ADMIN);

        if (user == null) {
            log.warn(USER_CAN_NOT_BE_NULL);
            return buildResponseErro(messageSource.getMessage(MessagePropertiesConstant.MESSAGE_FIELD_IS_REQUIRED, USER_IS_REQUIRED));
        }

        log.info("Update the user[{}]!", user);
        User updatedUser = userService.updateUser(user);
        return buildResponseSuccess(updatedUser, HttpStatus.OK);
    }

    @RequestMapping(value = "/", method = RequestMethod.DELETE,
            consumes = RestControllerConstant.CONSUMES)
    @ResponseBody
    public ResponseEntity delete(@RequestHeader("token") String token, @RequestBody User user) throws AuthenticationException {
        authenticationService.authetication(token, ProfileAppConstant.ADMIN);

        if (user == null) {
            log.warn(USER_CAN_NOT_BE_NULL);
            return buildResponseErro(messageSource.getMessage(MessagePropertiesConstant.MESSAGE_FIELD_IS_REQUIRED, USER_IS_REQUIRED));
        }

        log.info("Delete the user[{}]!", user);
        userService.deleteUser(user);
        return buildResponseSuccess(HttpStatus.OK);
    }


}
