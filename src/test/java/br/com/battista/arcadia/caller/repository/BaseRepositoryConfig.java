package br.com.battista.arcadia.caller.repository;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.*;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;

import br.com.battista.arcadia.caller.model.Card;
import br.com.battista.arcadia.caller.model.Guild;
import br.com.battista.arcadia.caller.model.Hero;
import br.com.battista.arcadia.caller.model.Scenery;
import br.com.battista.arcadia.caller.model.User;

public abstract class BaseRepositoryConfig {

    protected Objectify objectifyRepository;
    private LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
    private Validator validator;

    @Before
    public void setUp() {
        ObjectifyFactory objectifyFactory = new ObjectifyFactory();

        objectifyFactory.register(User.class);
        objectifyFactory.register(Hero.class);
        objectifyFactory.register(Card.class);
        objectifyFactory.register(Guild.class);
        objectifyFactory.register(Scenery.class);

        objectifyRepository = spy(objectifyFactory.begin());

        validator = spy(Validation.buildDefaultValidatorFactory().getValidator());

        helper.setUp();
        initMocks(this);
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }

}
