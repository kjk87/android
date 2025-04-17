package com.pple.pplus.utils.part.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import com.pple.pplus.utils.part.logs.LogUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by 안명훈 on 16. 6. 28..
 * 모든 유틸성 함수를 처리합니다.
 */
public class SystemUtils{

    private static final String LOG_TAG = SystemUtils.class.getCanonicalName();

    /**
     * app AndroidManifest에 등록된 meta data를 불러오도록함
     *
     * @param key 불러올 정보의 key 값
     * @return metaData
     */
    public static String getMetadata(final Context context, final String key) {
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (ai == null)
                return null;
            else if (ai.metaData == null)
                return null;
            else
                return ai.metaData.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    /**
     * 패키지 정보를 반환함
     */
    public static PackageInfo getPackageInfo(final Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            LogUtil.w(LOG_TAG, "Unable to get PackageInfo {}", e);
        }
        return null;
    }

    /**
     * @return Key Hash 값을 반환함
     */
    public static String getKeyHash(final Context context) {
        PackageInfo packageInfo = getPackageInfo(context);

        if (packageInfo == null)
            return null;

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return android.util.Base64.encodeToString(md.digest(), android.util.Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                LogUtil.w(LOG_TAG, "Unable to get MessageDigest. signature: {} -- {}", signature, e);
            }
        }
        return null;
    }

    /**
     * @return String -> byte[]
     */
    public static byte[] hexToByteArray(String hex) {
        if (hex == null || hex.length() == 0) {
            return null;
        }

        byte[] ba = new byte[hex.length() / 2];
        for (int i = 0; i < ba.length; i++) {
            ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return ba;
    }

// byte[] to hex

    /**
     * @return byte[] -> String
     */
    public static String byteArrayToHex(byte[] ba) {
        if (ba == null || ba.length == 0) {
            return null;
        }

        StringBuffer sb = new StringBuffer(ba.length * 2);
        String hexNumber;
        for (int x = 0; x < ba.length; x++) {
            hexNumber = "0" + Integer.toHexString(0xff & ba[x]);

            sb.append(hexNumber.substring(hexNumber.length() - 2));
        }
        return sb.toString();
    }
}
