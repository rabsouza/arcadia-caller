package br.com.battista.arcadia.caller.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.googlecode.objectify.ObjectifyService;

import br.com.battista.arcadia.caller.model.Card;
import br.com.battista.arcadia.caller.model.Guild;
import br.com.battista.arcadia.caller.model.Hero;
import br.com.battista.arcadia.caller.model.Scenery;
import br.com.battista.arcadia.caller.model.User;

/**
 * OfyHelper, a ServletContextListener, is setup in web.xml to run before a JSP is run.  This is
 * required to let JSP's access Ofy.
 **/
public class OfyHelper implements ServletContextListener {
    public static void register() {
        ObjectifyService.register(User.class);
        ObjectifyService.register(Card.class);
        ObjectifyService.register(Hero.class);
        ObjectifyService.register(Guild.class);
        ObjectifyService.register(Scenery.class);
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        // This will be invoked as part of a warmup request, or the first user
        // request if no warmup request was invoked.
        register();
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        // App Engine does not currently invoke this method.
    }
}
