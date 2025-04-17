package com.pplus.prnumberuser.core;

import android.util.Base64;

import com.pplus.utils.part.logs.LogUtil;
import com.pplus.utils.part.utils.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class Crypt {

    private final static byte[] keyBytes = {-20, -96, -100, -20, -99, -76, -19, -120, -84, -20, -105, -108, -20, -89, -79, -19};

    private final static IvParameterSpec ivParameterSpec = new IvParameterSpec(keyBytes);

    private final static SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

    public static String encrypt(String str) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

            byte[] encrypted = cipher.doFinal((str + ".vnkwpq#^&").getBytes("UTF-8"));
            return new String(Base64.encode(encrypted, Base64.NO_WRAP));
        } catch (Exception e) {
            LogUtil.e("input=" + str, e);
            return null;
        }
    }

    public static String decrypt(String str) {

        if (StringUtils.isEmpty(str))
            return str;

        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

            //logger.debug("decryptMobileNumber : " +  mobileNumber) ;
            byte[] encrypted = Base64.decode(str, Base64.NO_WRAP);
            //logger.debug("encryptedMobileNumber.length : " +  encrypted.length) ;
            String decrypted = new String(cipher.doFinal(encrypted), "UTF-8");
            int index = decrypted.indexOf('.');

            return decrypted.substring(0, index);
        } catch (Exception e) {
            LogUtil.e("input=" + str, e);
            return null;
        }
    }
}
