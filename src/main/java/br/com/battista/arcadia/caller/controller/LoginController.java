package br.com.battista.arcadia.caller.controller;

import static br.com.battista.arcadia.caller.builder.ResponseEntityBuilder.buildResponseErro;
import static br.com.battista.arcadia.caller.builder.ResponseEntityBuilder.buildResponseSuccess;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.appengine.repackaged.com.google.api.client.util.Maps;
import com.google.common.base.Strings;

import br.com.battista.arcadia.caller.constants.MessagePropertiesConstant;
import br.com.battista.arcadia.caller.constants.RestControllerConstant;
import br.com.battista.arcadia.caller.model.User;
import br.com.battista.arcadia.caller.service.MessageCustomerService;
import br.com.battista.arcadia.caller.service.UserService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("api/v1/login")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageCustomerService messageSource;

    @RequestMapping(value = "/", method = RequestMethod.POST,
            produces = RestControllerConstant.PRODUCES, consumes = RestControllerConstant.CONSUMES)
    @ResponseBody
    public ResponseEntity<User> create(@RequestBody User user) {
        if (user == null) {
            log.warn("User can not be null!");
            return buildResponseErro(messageSource.getMessage(MessagePropertiesConstant.MESSAGE_FIELD_IS_REQUIRED, "User"));
        }

        log.info("Create new username[{}]!", user);
        User newUser = userService.saveUser(user);
        log.debug("Save the username and generate to id: {}!", newUser.getId());
        return buildResponseSuccess(newUser, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{username}", method = RequestMethod.GET,
            produces = RestControllerConstant.PRODUCES)
    @ResponseBody
    public ResponseEntity<Map<String, String>> login(@PathVariable("username") String username) {
        if (username == null) {
            log.warn("Path username can not be null!");
            return buildResponseErro(messageSource.getMessage(MessagePropertiesConstant.MESSAGE_FIELD_IS_REQUIRED, "Path username"));
        }

        log.info("Get token to username: {}.", username);
        User userEntity = userService.getUserByUsername(username);
        if (userEntity == null || Strings.isNullOrEmpty(userEntity.getToken())) {
            log.warn("Token not found!");
            return buildResponseErro(HttpStatus.NOT_FOUND);
        }

        log.info("Token found by username.");
        Map<String, String> response = Maps.newHashMap();
        response.put("token", userEntity.getToken());

        return buildResponseSuccess(response, HttpStatus.OK);
    }
}
