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

import br.com.battista.arcadia.caller.model.User;

/**
 * Created by rabsouza on 26/06/16.
 */
public abstract class BaseRepositoryConfig {

    private LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
    protected Objectify objectifyRepository;

    private Validator validator;

    @Before
    public void setUp() {
        ObjectifyFactory objectifyFactory = new ObjectifyFactory();
        objectifyFactory.register(User.class);
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
