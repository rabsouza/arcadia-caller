package br.com.battista.arcadia.caller.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import br.com.battista.arcadia.caller.exception.AppException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MD5HashingUtils {

    private MD5HashingUtils(){}

    public static String generateHash(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(value.getBytes());

            byte byteData[] = md.digest();

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new AppException(e);
        }
    }
}