package com.hope.util;

import java.util.Random;

public class VerificationCode {
    public static String generateVerificationCode() {
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            code.append(chars[random.nextInt(chars.length)]);
        }
        code.append(random.nextInt(10));
        return code.toString();
    }
}
