package br.com.battista.arcadia.caller.repository;

import static org.mockito.Mockito.spy;
import static org.mockito.MockitoAnnotations.initMocks;

import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.After;
import org.junit.Before;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;

import br.com.battista.arcadia.caller.model.Campaign;
import br.com.battista.arcadia.caller.model.Card;
import br.com.battista.arcadia.caller.model.Guild;
import br.com.battista.arcadia.caller.model.Hero;
import br.com.battista.arcadia.caller.model.HeroGuild;
import br.com.battista.arcadia.caller.model.KeyCampaign;
import br.com.battista.arcadia.caller.model.Scenery;
import br.com.battista.arcadia.caller.model.SceneryCampaign;
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
        objectifyFactory.register(HeroGuild.class);
        objectifyFactory.register(Card.class);
        objectifyFactory.register(Guild.class);
        objectifyFactory.register(Scenery.class);
        objectifyFactory.register(Campaign.class);
        objectifyFactory.register(SceneryCampaign.class);
        objectifyFactory.register(KeyCampaign.class);

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
