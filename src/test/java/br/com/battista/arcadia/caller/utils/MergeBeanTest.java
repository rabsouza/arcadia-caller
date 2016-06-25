package br.com.battista.arcadia.caller.utils;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import br.com.battista.arcadia.caller.model.Offer;

/**
 * Created by rabsouza on 12/04/16.
 */
public class MergeBeanTest {

    @org.junit.Test
    public void shouldMergeBeanBToBeanA() throws Exception {
        Offer a = new Offer();
        a.setName("A");

        Offer b = new Offer();
        b.setName("B");
        b.setCategory("B");

        new MergeBean().merge(a, b);

        assertThat(a.getName(), equalTo(b.getName()));
        assertThat(a.getCategory(), equalTo(b.getCategory()));
    }

    @org.junit.Test
    public void shouldMergeBeanAToBeanB() throws Exception {
        Offer a = new Offer();
        a.setName("A");

        Offer b = new Offer();
        b.setName("B");
        b.setCategory("B");

        new MergeBean().merge(b, a);

        assertThat(b.getName(), equalTo(a.getName()));
        assertThat(b.getCategory(), equalTo("B"));
    }
}
