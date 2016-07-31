package br.com.battista.arcadia.caller.controller;

import static br.com.battista.arcadia.caller.builder.ResponseEntityBuilder.buildResponseErro;
import static br.com.battista.arcadia.caller.builder.ResponseEntityBuilder.buildResponseSuccess;
import static br.com.battista.arcadia.caller.constants.ProfileAppConstant.ADMIN;
import static br.com.battista.arcadia.caller.constants.ProfileAppConstant.APP;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.battista.arcadia.caller.constants.RestControllerConstant;
import br.com.battista.arcadia.caller.exception.AuthenticationException;
import br.com.battista.arcadia.caller.model.User;
import br.com.battista.arcadia.caller.model.dto.StatisticUserDto;
import br.com.battista.arcadia.caller.service.AuthenticationService;
import br.com.battista.arcadia.caller.service.StatisticUserService;
import br.com.battista.arcadia.caller.service.UserService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("api/v1/statistic")
public class StatisticUserController {

    @Autowired
    private StatisticUserService statisticUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationService authenticationService;

    @RequestMapping(value = "/{username:.+}", method = RequestMethod.GET,
            produces = RestControllerConstant.PRODUCES)
    @ResponseBody
    public ResponseEntity<StatisticUserDto> getAll(@RequestHeader("token") String token, @PathVariable("username") String username) throws
            AuthenticationException {
        authenticationService.authetication(token, APP, ADMIN);

        log.info("Retrieve user by username!");
        User user = userService.getUserByUsername(username);

        if (user == null) {
            log.warn("User not found!");
            return buildResponseErro(HttpStatus.NOT_FOUND);
        } else {
            StatisticUserDto statisticUserDto = statisticUserService.getAllByUser(user);
            log.info("Retrieve statistic user: {}", statisticUserDto);

            return buildResponseSuccess(statisticUserDto, HttpStatus.OK);
        }
    }


}
