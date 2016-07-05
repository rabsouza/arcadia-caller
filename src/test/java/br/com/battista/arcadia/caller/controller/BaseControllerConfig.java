package br.com.battista.arcadia.caller.controller;

import org.junit.*;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.ObjectifyService;

import br.com.battista.arcadia.caller.model.User;

/**
 * Created by rabsouza on 26/06/16.
 */
public abstract class BaseControllerConfig {

    private LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @BeforeClass
    public static void startUp(){
        ObjectifyService.begin();
        ObjectifyService.register(User.class);
    }

    @Before
    public void setUp() {
        helper.setUp();
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }

}
