package pl.edu.pwr.wordnetloom.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public final class PasswordUtils {

    private PasswordUtils() {
    }

    public static String encryptPassword(final String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (final NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
        md.update(password.getBytes());
        return Base64.getMimeEncoder().encodeToString(md.digest());
    }

}
