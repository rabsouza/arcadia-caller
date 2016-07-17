package br.com.battista.arcadia.caller.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Locale;

import org.junit.*;
import org.junit.rules.*;
import org.junit.runner.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

@RunWith(MockitoJUnitRunner.class)
public class MessageCustomerServiceTest {

    private String keyMessage = "a.b.c";
    private String invalidKeyMessage = "a.bbv";
    private String valueMessage = "ABC";

    @Rule
    public ExpectedException rule = ExpectedException.none();

    @InjectMocks
    private MessageCustomerService messageCustomerService;

    @Mock
    private MessageSource messageSource;

    @Test
    public void shouldReturnMessageWhenExistKey() {

        when(messageSource.getMessage(anyString(), new Object[]{any()}, (Locale) any())).thenReturn(valueMessage);

        String message = messageCustomerService.getMessage(keyMessage);
        assertNotNull(message);
        assertThat(message, equalTo(valueMessage));


    }

    @Test
    public void shouldThrowExceptionWhenInvalidKey() {
        rule.expect(NoSuchMessageException.class);

        when(messageSource.getMessage(anyString(), new Object[]{any()}, (Locale) any())).thenThrow(NoSuchMessageException.class);

        messageCustomerService.getMessage(invalidKeyMessage);

    }

}