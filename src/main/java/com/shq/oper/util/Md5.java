package com.shq.oper.util;

import org.springframework.util.Assert;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

/**
 * @author: ltg
 * @date: Created in 11:52 2018/7/2
 */
public class Md5 {

    public static String encrypt(Object... var0) {
        try {
            MessageDigest var1 = MessageDigest.getInstance("MD5");
            byte[] var2 = var1.digest(getBytes(var0));
            return salt(var2);
        } catch (Exception var3) {
            throw new RuntimeException("加密失败", var3);
        }
    }


    private static String salt(byte[] var0) {
        StringBuffer var1 = new StringBuffer();
        byte[] var2 = var0;
        int var3 = var0.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            byte var5 = var2[var4];
            int var6 = var5 & 255;
            String var7 = Integer.toHexString(var6);
            if (var7.length() == 1) {
                var1.append("0");
            }

            var1.append(var7);
        }

        return var1.toString();
    }

    private static byte[] getBytes(Object... var0) throws UnsupportedEncodingException {

        String var1 = var0[0].toString();
        StringBuilder var2 = new StringBuilder(var1);

        for(int var3 = 1; var3 < var0.length; ++var3) {
            var2.append(var0[var3]);
        }

        return var2.toString().getBytes("UTF-8");
    }
}
