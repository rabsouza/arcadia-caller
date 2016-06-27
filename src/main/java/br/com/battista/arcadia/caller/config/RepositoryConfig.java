package br.com.battista.arcadia.caller.config;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.context.annotation.*;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

@Configuration
@ComponentScan(basePackages = {"br.com.battista.arcadia.caller.repository"})
public class RepositoryConfig {

    @Bean
    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "prototype")
    public Objectify configObjectifyRepository() {
        return ObjectifyService.ofy();
    }

    @Bean
    public Validator configValidator(){
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        return validatorFactory.getValidator();
    }

}
