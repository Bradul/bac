package org.example.services;

import java.util.regex.Pattern;

public class ValidatorService {
    public static boolean validateEmail(String email) {
        return Pattern.matches("^[a-zA-Z][a-zA-Z0-9-_]*@[a-zA-Z][a-zA-Z0-9-_]*.[a-zA-Z][a-zA-Z0-9-_]{1,4}", email);
    }

    public static boolean validatePassword(String password) {
        return password.length() >= 8;
    }
}
