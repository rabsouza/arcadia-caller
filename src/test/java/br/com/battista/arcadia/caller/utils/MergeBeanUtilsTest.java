package br.com.battista.arcadia.caller.utils;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.*;

import br.com.battista.arcadia.caller.model.User;

public class MergeBeanUtilsTest {

    @Test
    public void shouldMergeBeanBToBeanA() throws Exception {
        User a = User.builder().mail("A").build();

        User b = User.builder().mail("B").token("B").build();

        MergeBeanUtils.merge(a, b);

        assertThat(a.getMail(), equalTo(b.getMail()));
        assertThat(a.getToken(), equalTo(b.getToken()));
    }

    @Test
    public void shouldMergeBeanAToBeanB() throws Exception {
        User a = User.builder().mail("A").build();

        User b = User.builder().mail("B").token("B").build();

        MergeBeanUtils.merge(b, a);

        assertThat(b.getMail(), equalTo(a.getMail()));
        assertThat(b.getToken(), equalTo("B"));
    }


}