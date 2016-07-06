package br.com.battista.arcadia.caller.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.google.common.base.Strings;

import br.com.battista.arcadia.caller.exception.AppException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class MD5HashingUtils {

    public static final String MD5 = "MD5";

    private MD5HashingUtils() {
    }

    public static String generateHash(String value) {
        if (Strings.isNullOrEmpty(value)) {
            return "";
        }

        try {
            MessageDigest md = MessageDigest.getInstance(MD5);
            md.update(value.getBytes());

            byte[] byteData = md.digest();

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new AppException("Error generating the MD5 hash key.", e);
        }
    }
}
