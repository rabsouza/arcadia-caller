package br.com.battista.arcadia.caller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
public class MessageCustomerService implements MessageSourceAware {

    @Autowired
    private MessageSource messageSource;

    @Override
    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String s, Object... objects) {
        return messageSource.getMessage(s, objects, LocaleContextHolder.getLocale());
    }
}
