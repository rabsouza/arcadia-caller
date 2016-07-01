package br.com.battista.arcadia.caller.utils;

import org.junit.*;

/**
 * Created by rabsouza on 30/06/16.
 */
public class MD5HashingUtilsTest {
    @Test
    public void generateHash() throws Exception {
        System.out.println(MD5HashingUtils.generateHash("rafaelbs@ciandt.com".toLowerCase()));
    }

}