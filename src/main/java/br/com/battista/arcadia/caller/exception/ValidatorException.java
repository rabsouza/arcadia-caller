package br.com.battista.arcadia.caller.exception;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import com.google.common.collect.Lists;

import br.com.battista.arcadia.caller.model.BaseEntity;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValidatorException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;

    private final transient Set<ConstraintViolation<BaseEntity>> violations = new LinkedHashSet<>();

    public ValidatorException(Set<ConstraintViolation<BaseEntity>> violations, String s) {
        super(s);
        this.violations.addAll(violations);
    }

    public ValidatorException(Set<ConstraintViolation<BaseEntity>> violations, String s, Throwable throwable) {
        super(s, throwable);
        this.violations.addAll(violations);
    }

    public String[] builderMessagesValidator() {
        List<String> violationMessages = Lists.newArrayList();

        for (ConstraintViolation violation : violations) {
            String message = MessageFormat.format("{0}: {1}!", violation.getPropertyPath(), violation.getMessage());
            log.error(message);
            violationMessages.add(message);
        }
        return violationMessages.toArray(new String[0]);
    }

}
