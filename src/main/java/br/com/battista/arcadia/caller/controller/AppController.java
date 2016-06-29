package br.com.battista.arcadia.caller.controller;

import static br.com.battista.arcadia.caller.builder.ResponseEntityBuilder.buildResponseSuccess;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.appengine.repackaged.com.google.api.client.util.Maps;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("api/v1/")
@PropertySource("classpath:version.properties")
public class AppController {

    @Autowired
    private Environment env;

    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity ping() {

        log.info("Check app!");
        return buildResponseSuccess(HttpStatus.OK);
    }

    @RequestMapping(value = "/health", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity health() {

        log.info("health app!");

        Map<String, Object> body = Maps.newLinkedHashMap();

        body.put("app.status", "UP");
        body.put("objectify.status", "UP");
        body.put("gae.app.name", env.getProperty("gae.app.name"));
        body.put("gae.app.version", env.getProperty("gae.app.version"));
        body.put("version.app", env.getProperty("version.app"));
        body.put("group.id.app", env.getProperty("group.id.app"));
        body.put("artifact.id.app", env.getProperty("artifact.id.app"));

        return buildResponseSuccess(body, HttpStatus.OK);
    }


}
