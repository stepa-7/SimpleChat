package com.stepa7.webchat.security;

import org.mindrot.jbcrypt.BCrypt;

public class SecurityUtil {
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}