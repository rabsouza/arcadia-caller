package br.com.battista.arcadia.caller.utils;

import static br.com.battista.arcadia.caller.utils.MD5HashingUtils.generateHash;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.*;

public class MD5HashingUtilsTest {
    @Test
    public void shouldReturnEmptyValueWhenValueNull() throws Exception {
        assertThat(generateHash(null), equalTo(""));
    }

    @Test
    public void shouldReturnEmptyValueWhenValueEmpty() throws Exception {
        assertThat(generateHash(""), equalTo(""));
    }

    @Test
    public void shouldReturnMD5ValueWhenValueValid() throws Exception {
        assertThat(generateHash("abc"), not(isEmptyOrNullString()));
    }
}