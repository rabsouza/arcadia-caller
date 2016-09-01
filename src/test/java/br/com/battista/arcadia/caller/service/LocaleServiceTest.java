package br.com.battista.arcadia.caller.service;

import static org.junit.Assert.*;

import org.hamcrest.CoreMatchers;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.com.battista.arcadia.caller.config.AppConfig;
import br.com.battista.arcadia.caller.constants.MessageConstant;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class LocaleServiceTest {

    @Autowired
    private LocaleService localeService;

    @Test
    public void shouldReturnNullWhenNullLocale() {
        assertNull(localeService.processSupportedLocales(null));
    }

    @Test
    public void shouldReturnNullWhenEmptyLocale() {
        assertNull(localeService.processSupportedLocales(""));
    }

    @Test
    public void shouldReturnNullWhenInvalidLocale() {
        assertNull(localeService.processSupportedLocales("123"));
    }

    @Test
    public void shouldReturnNullWhenNoSupportedLocale() {
        assertNull(localeService.processSupportedLocales("es"));
    }

    @Test
    public void shouldReturnLocaleWhenSupportedLocale() {
        assertThat(localeService.processSupportedLocales("pt"), CoreMatchers.equalTo(MessageConstant.DEFAULT_LOCALE));
    }

}