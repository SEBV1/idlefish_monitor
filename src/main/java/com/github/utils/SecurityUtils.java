package com.github.utils;

import org.apache.commons.codec.digest.MessageDigestAlgorithms;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class SecurityUtils {
    public static final int DEFAULT_WUA_FLAG = 0;
    public static final int GENERAL_WUA_FLAG = 4;
    public static final int MINI_WUA_FLAG = 8;

    public static String getMd5(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
            messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
            byte[] digest = messageDigest.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                StringBuilder sb2 = new StringBuilder(Integer.toHexString(b & 255));
                while (sb2.length() < 2) {
                    sb2.insert(0, "0");
                }
                sb.append((CharSequence) sb2);
            }
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }
}