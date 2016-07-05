package br.com.battista.arcadia.caller.controller;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Map;

import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.com.battista.arcadia.caller.config.AppConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class AppControllerTest extends BaseControllerConfig {

    @Autowired
    private AppController appController;

    @Test
    public void shouldReturnSuccessWhenActionPing() {
        ResponseEntity ping = appController.ping();
        assertThat(ping.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void shouldReturnSuccessWhenActionhealth() {
        ResponseEntity health = appController.health();
        assertThat(health.getStatusCode(), equalTo(HttpStatus.OK));
        Map<String, Object> body = (Map<String, Object>) health.getBody();
        assertNotNull(body);
        assertThat(body, hasEntry("app.status", (Object) "UP"));
        assertThat(body, hasKey("version.app"));
    }

}