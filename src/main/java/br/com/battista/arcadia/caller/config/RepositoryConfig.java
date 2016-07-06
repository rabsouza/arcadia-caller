package br.com.battista.arcadia.caller.config;

import javax.validation.Validation;
import javax.validation.Validator;

import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

@Configuration
public class RepositoryConfig {

    @Bean
    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "prototype")
    public Objectify configObjectifyRepository() {
        return ObjectifyService.ofy();
    }

    @Bean(name = "hibernateValidator")
    public Validator configValidator() {
        return Validation.byProvider(HibernateValidator.class)
                       .configure()
                       .failFast(true)
                       .buildValidatorFactory()
                       .getValidator();
    }

}
