package br.com.battista.arcadia.caller.exception;

import java.text.MessageFormat;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import com.google.common.collect.Lists;

import br.com.battista.arcadia.caller.model.BaseEntity;

public class ValidatorException extends RuntimeException {

    private final Set<ConstraintViolation<BaseEntity>> violations = new LinkedHashSet<>();

    public ValidatorException(Set<ConstraintViolation<BaseEntity>> violations) {
        this.violations.addAll(violations);
    }

    public ValidatorException(Set<ConstraintViolation<BaseEntity>> violations, String s) {
        super(s);
        this.violations.addAll(violations);
    }

    public ValidatorException(Set<ConstraintViolation<BaseEntity>> violations, String s, Throwable throwable) {
        super(s, throwable);
        this.violations.addAll(violations);
    }

    public ValidatorException(Set<ConstraintViolation<BaseEntity>> violations, Throwable throwable) {
        super(throwable);
        this.violations.addAll(violations);
    }

    public String[] builderMessagesValidator() {
        List<String> violationMessages = Lists.newArrayList();

        for (ConstraintViolation violation : violations) {
            violationMessages.add(MessageFormat.format("{0}: {1}!", violation.getPropertyPath(), violation.getMessage()));
        }
        return violationMessages.toArray(new String[0]);
    }

}
