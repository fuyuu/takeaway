package com.hope.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密工具类
 */
public class MD5Utils {
    private static final String MD5_ALGORITHM = "MD5";
    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', 
                                             '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * MD5加密方法
     * @param input 待加密字符串
     * @return 32位小写MD5值
     */
    public static String md5(String input) {
        if (input == null) {
            return null;
        }

        try {
            MessageDigest messageDigest = MessageDigest.getInstance(MD5_ALGORITHM);
            messageDigest.update(input.getBytes());
            byte[] digest = messageDigest.digest();
            return byteArrayToHexString(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }

    /**
     * 字节数组转16进制字符串
     */
    private static String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(HEX_DIGITS[(b >> 4) & 0x0f]);
            sb.append(HEX_DIGITS[b & 0x0f]);
        }
        return sb.toString();
    }

    /**
     * 加盐MD5加密
     * @param input 原始字符串
     * @param salt 盐值
     * @return 加盐MD5
     */
    public static String md5WithSalt(String input, String salt) {
        if (salt == null) {
            return md5(input);
        }
        return md5(input + salt);
    }

    // 测试
    public static void main(String[] args) {
        String password = "123456";
        System.out.println("原始密码: " + password);
        System.out.println("MD5加密: " + md5(password));
        System.out.println("加盐MD5: " + md5WithSalt(password, "abc"));
    }
}