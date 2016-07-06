package br.com.battista.arcadia.caller.validator;


import static org.hamcrest.Matchers.*;

import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.*;
import org.junit.rules.*;
import org.junit.runner.*;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.battista.arcadia.caller.constants.ProfileAppConstant;
import br.com.battista.arcadia.caller.exception.ValidatorException;
import br.com.battista.arcadia.caller.model.User;

@RunWith(MockitoJUnitRunner.class)
public class EntityValidatorTest {

    @Rule
    public ExpectedException rule = ExpectedException.none();
    @InjectMocks
    private EntityValidator entityValidator;
    @Spy
    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void shouldErrorValidateWhenValidNullEntity() {
        rule.expect(IllegalArgumentException.class);
        rule.expectMessage(containsString("The object to be validated must not be null."));

        entityValidator.validate(null);
    }

    @Test
    public void shouldErrorValidateWhenInvalidEntity() {
        rule.expect(ValidatorException.class);
        rule.expectMessage(containsString("Constraint validation error to entity"));

        User build = User.builder().build();
        entityValidator.validate(build);
    }

    @Test
    public void shouldPassValidateWhenValidEntity() {
        User build = User.builder().mail("abcdef@abc.com").username("abcdef").profile(ProfileAppConstant.ADMIN).build();

        entityValidator.validate(build);
    }

}