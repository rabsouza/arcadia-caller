package br.com.battista.arcadia.caller.controller;

import static br.com.battista.arcadia.caller.builder.ResponseEntityBuilder.buildResponseErro;
import static br.com.battista.arcadia.caller.builder.ResponseEntityBuilder.buildResponseSuccess;
import static br.com.battista.arcadia.caller.constants.ProfileAppConstant.ADMIN;
import static br.com.battista.arcadia.caller.constants.ProfileAppConstant.APP;
import static br.com.battista.arcadia.caller.constants.RestControllerConstant.ENABLE_CACHED_ACTION;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.appengine.repackaged.com.google.api.client.util.Strings;

import br.com.battista.arcadia.caller.constants.MessagePropertiesConstant;
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
        authenticationService.authetication(token, ADMIN);

        log.info("Retrieve all users!");
        List<User> users = userService.getAllUsers();

        if (users == null || users.isEmpty()) {
            log.warn("No users found!");
            return buildResponseErro(HttpStatus.NO_CONTENT);
        } else {
            log.info("Found {} users!", users.size());
            return buildResponseSuccess(users, HttpStatus.OK, ENABLE_CACHED_ACTION);
        }
    }

    @RequestMapping(value = "/{username:.+}", method = RequestMethod.GET,
            produces = RestControllerConstant.PRODUCES)
    @ResponseBody
    public ResponseEntity<User> getByUsername(@RequestHeader("token") String token, @PathVariable("username") String username) throws
            AuthenticationException {
        authenticationService.authetication(token, ADMIN, APP);

        log.info("Retrieve user by username: {}.", username);
        User user = userService.getUserByUsername(username);

        if (user == null) {
            log.warn("No user found!");
            return buildResponseErro(HttpStatus.NOT_FOUND);
        } else {
            log.info("Found the user: {}.", user);
            return buildResponseSuccess(user, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/exists/{username:.+}", method = RequestMethod.GET,
                    produces = RestControllerConstant.PRODUCES)
    @ResponseBody
    public ResponseEntity<Void> existsUsername(@RequestHeader("token") String token, @PathVariable("username") String username) throws
                    AuthenticationException {
        authenticationService.authetication(token, ADMIN, APP);

        log.info("Check if there is the user by username: {}.", username);
        User user = userService.getUserByUsername(username);

        if (user == null) {
            log.warn("User not found!");
            return buildResponseErro(HttpStatus.NOT_FOUND);
        } else {
            log.info("Found the user: {}.", user);
            return buildResponseSuccess(HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.POST,
            produces = RestControllerConstant.PRODUCES,
            consumes = RestControllerConstant.CONSUMES)
    @ResponseBody
    public ResponseEntity<User> save(@RequestHeader("token") String token, @RequestBody User user) throws AuthenticationException {
        authenticationService.authetication(token, ADMIN);

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
            produces = RestControllerConstant.PRODUCES,
            consumes = RestControllerConstant.CONSUMES)
    @ResponseBody
    public ResponseEntity<User> update(@RequestHeader("token") String token, @RequestBody User user) throws AuthenticationException {
        authenticationService.authetication(token, ADMIN);

        if (user == null) {
            log.warn(USER_CAN_NOT_BE_NULL);
            return buildResponseErro(messageSource.getMessage(MessagePropertiesConstant.MESSAGE_FIELD_IS_REQUIRED, USER_IS_REQUIRED));
        }

        log.info("Update the user[{}]!", user);
        User updatedUser = userService.updateUser(user);
        return buildResponseSuccess(updatedUser, HttpStatus.OK);
    }

    @RequestMapping(value = "/{username:.+}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity delete(@RequestHeader("token") String token, @PathVariable("username") String username) throws AuthenticationException {
        authenticationService.authetication(token, ADMIN);

        if (Strings.isNullOrEmpty(username)) {
            log.warn("Username can not be null!");
            return buildResponseErro(messageSource.getMessage(MessagePropertiesConstant.MESSAGE_FIELD_IS_REQUIRED, "Username can not be null!"));
        }
        User user = User.builder().username(username).build();
        log.info("Delete the user[{}]!", user);
        userService.deleteUser(user);
        return buildResponseSuccess(HttpStatus.OK);
    }


}
