package br.com.battista.arcadia.caller.validator;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import br.com.battista.arcadia.caller.exception.ValidatorException;
import br.com.battista.arcadia.caller.model.BaseEntity;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EntityValidator {

    @Autowired
    @Qualifier("hibernateValidator")
    private Validator validator;

    public void validate(BaseEntity entity) {
        if (entity == null || entity.getClass() == null) {
            log.warn("Validating a null entity!");
        }

        Set<ConstraintViolation<BaseEntity>> violations = validator.validate(entity);
        if (CollectionUtils.isNotEmpty(violations) && entity != null && entity.getClass() != null) {
            String message = String.format("Constraint validation error to entity: %s",
                    entity.getClass().getSimpleName());
            log.error(message);
            throw new ValidatorException(violations, message);
        }
    }
}
