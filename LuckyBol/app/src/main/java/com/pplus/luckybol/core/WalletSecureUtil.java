package com.pplus.luckybol.core;

import android.util.Base64;

import com.pplus.luckybol.Const;
import com.pplus.utils.part.logs.LogUtil;
import com.pplus.utils.part.utils.StringUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class WalletSecureUtil {

    private static final String devSecretKey = "2r5u8x!A%D*G-KaPdSgVkYp3s6v9y$B?";
    private static final String devIvKey = "mZq4t7w!z$C&F)J@";

    private static final String prodSecretKey = "H@McQfTjWnZr4t7w!z%C*F-JaNdRgUkX";
    private static final String prodIvKey = "6w9z$C&F)J@NcRfU";


    public static String encrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            String secretKey;
            String ivKey;
            if(Const.API_URL.startsWith("https://api")){
                secretKey = prodSecretKey;
                ivKey = prodIvKey;
            }else{
                secretKey = devSecretKey;
                ivKey = devIvKey;
            }

            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivKey.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] encrypted = cipher.doFinal((data).getBytes(StandardCharsets.UTF_8));

            return new String(Base64.encode(encrypted, Base64.NO_WRAP));
        } catch (Exception e) {
            LogUtil.e("input=" + data, e);
            return null;
        }
    }

    public static String urlEncode(String data){
        return Base64.encodeToString(encrypt(data).getBytes(StandardCharsets.UTF_8), Base64.URL_SAFE);
    }

    public static String decrypt(String data) {

        if (StringUtils.isEmpty(data))
            return data;

        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            String secretKey;
            String ivKey;
            if(Const.API_URL.startsWith("https://api")){
                secretKey = prodSecretKey;
                ivKey = prodIvKey;
            }else{
                secretKey = devSecretKey;
                ivKey = devIvKey;
            }

            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivKey.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] encrypted = Base64.decode(data, Base64.NO_WRAP);

            return new String(cipher.doFinal(encrypted), StandardCharsets.UTF_8);
        } catch (Exception e) {
            LogUtil.e("input=" + data, e);
            return null;
        }
    }
}
