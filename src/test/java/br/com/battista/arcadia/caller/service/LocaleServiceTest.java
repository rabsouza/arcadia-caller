package br.com.battista.arcadia.caller.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.com.battista.arcadia.caller.config.AppConfig;
import br.com.battista.arcadia.caller.constants.LocaleConstant;
import br.com.battista.arcadia.caller.constants.MessageConstant;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class LocaleServiceTest {

    @Autowired
    private LocaleService localeService;

    @Test
    public void shouldReturnDefaultWhenNullLocale() {
        assertThat(localeService.processSupportedLocales(null), equalTo(LocaleConstant.LOCALE_DEFAULT));
    }

    @Test
    public void shouldReturnDefaultWhenEmptyLocale() {
        assertThat(localeService.processSupportedLocales(""), equalTo(LocaleConstant.LOCALE_DEFAULT));
    }

    @Test
    public void shouldReturnDefaultWhenInvalidLocale() {
        assertThat(localeService.processSupportedLocales("123"), equalTo(LocaleConstant.LOCALE_DEFAULT));
    }

    @Test
    public void shouldReturnDefaultWhenNoSupportedLocale() {
        assertThat(localeService.processSupportedLocales("es"), equalTo(LocaleConstant.LOCALE_DEFAULT));
    }

    @Test
    public void shouldReturnLocaleWhenSupportedLocale() {
        assertThat(localeService.processSupportedLocales("pt"), equalTo(MessageConstant.DEFAULT_LOCALE));
    }

}