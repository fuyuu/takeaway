package com.hope.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

//年龄/生日/地址属于个人隐私，返回给前端前要脱敏，存储时敏感字段可加密。 示例工具类（AES 对称加密）：
public final class CryptoUtil {

    // 16/24/32 字节长度都行，这里用 32 字节（256 bit）
    private static final String KEY = "1234567890abcdef1234567890abcdef"; // 放配置中心！
    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";

    public static String encrypt(String raw) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(KEY.getBytes(), "AES"));
            return Base64.getEncoder().encodeToString(cipher.doFinal(raw.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException("AES encrypt error", e);
        }
    }

    public static String decrypt(String enc) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(KEY.getBytes(), "AES"));
            return new String(cipher.doFinal(Base64.getDecoder().decode(enc)));
        } catch (Exception e) {
            throw new RuntimeException("AES decrypt error", e);
        }
    }

    private CryptoUtil() {}
}