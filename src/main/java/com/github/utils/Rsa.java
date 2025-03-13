package com.github.utils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


public class Rsa {
    private static final String ALGORITHM = "RSA";
    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";
    private static final String rsaPubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC8H6Gp7XP6UvEQzvUgGnt9nPX4exn1aNlmeyloMl6g2rEggeTNMp7I3iLPzQDbt6yedCru971fducKc2DgF/y2CcwAdqaKdxm0dSI2Zs4QLNYbKwWJ65wkgUh8+TJBnk+PGTgoxZ2wzvhJyRGjGhsFvLmZkUYPPxAPSNfjB3+/4wIDAQAB";

    public static String encrypt(String str) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            PublicKey publicKeyFromX509 = getPublicKeyFromX509("RSA");
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKeyFromX509);
            byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
            // 根据密钥长度计算块大小
            int keySize = publicKeyFromX509.getEncoded().length * 8;
            int blockSize = (keySize / 8) - 11; // 减去 PKCS1 填充的长度

            for (int i = 0; i < bytes.length; i += blockSize) {
                int length = Math.min(blockSize, bytes.length - i);
                byte[] encryptedBlock = cipher.doFinal(bytes, i, length);
                byteArrayOutputStream.write(encryptedBlock);
            }

            return new String(Base64.encode(byteArrayOutputStream.toByteArray()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static PublicKey getPublicKeyFromX509(String str) throws Exception {
        KeyFactory keyFactory;
        X509EncodedKeySpec x509EncodedKeySpec = getX509EncodedKeySpec();
        try {
            try {
                keyFactory = KeyFactory.getInstance(str);
            } catch (Throwable unused) {
                keyFactory = KeyFactory.getInstance(str, "BC");
            }
        } catch (Throwable unused2) {
            keyFactory = KeyFactory.getInstance(str);
        }
        if (keyFactory == null) {
            keyFactory = KeyFactory.getInstance(str);
        }
        return keyFactory.generatePublic(x509EncodedKeySpec);
    }

    private static X509EncodedKeySpec getX509EncodedKeySpec() {
        byte[] decode = {48, -127, -97, 48, 13, 6, 9, 42, -122, 72, -122, -9, 13, 1, 1, 1, 5, 0, 3, -127, -115, 0, 48, -127, -119, 2, -127, -127, 0, -68, 31, -95, -87, -19, 115, -6, 82, -15, 16, -50, -11, 32, 26, 123, 125, -100, -11, -8, 123, 25, -11, 104, -39, 102, 123, 41, 104, 50, 94, -96, -38, -79, 32, -127, -28, -51, 50, -98, -56, -34, 34, -49, -51, 0, -37, -73, -84, -98, 116, 42, -18, -9, -67, 95, 118, -25, 10, 115, 96, -32, 23, -4, -74, 9, -52, 0, 118, -90, -118, 119, 25, -76, 117, 34, 54, 102, -50, 16, 44, -42, 27, 43, 5, -119, -21, -100, 36, -127, 72, 124, -7, 50, 65, -98, 79, -113, 25, 56, 40, -59, -99, -80, -50, -8, 73, -55, 17, -93, 26, 27, 5, -68, -71, -103, -111, 70, 15, 63, 16, 15, 72, -41, -29, 7, 127, -65, -29, 2, 3, 1, 0, 1};
        return new X509EncodedKeySpec(decode);
    }

    public static String sign(String str, String str2) {
        try {
            PrivateKey generatePrivate = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64.decode(str2)));
            Signature signature = Signature.getInstance("SHA1WithRSA");
            signature.initSign(generatePrivate);
            signature.update(str.getBytes("utf-8"));
            return Base64.encode(signature.sign());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        String encrypt = Rsa.encrypt("123456");
        System.out.println(encrypt);
    }
}