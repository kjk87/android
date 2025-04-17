package com.pple.pplus.utils.part.net;

import com.pple.pplus.utils.part.logs.LogUtil;

import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.pple.pplus.utils.Config;
import com.pple.pplus.utils.part.utils.SystemUtils;

/**
 * Created by 안명훈 on 16. 6. 29..
 */
public class NetworkUtil {

    private static String LOG_TAG = NetworkUtil.class.getSimpleName();

    /**
     * <ol>
     *     네트워크 서비스를 이용하기위해 Sha256 으로 디바이스 넘버를 암호화한 데이터를 반환함
     * </ol>
     * @param deviceNumber 디바이스 연락처
     * */
    public static String getSha256Hmac(String deviceNumber) {

        try {

            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(Config.SERVICE_HMAC_SECRET_KEY.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);

            byte[] sha256_result = sha256_HMAC.doFinal(deviceNumber.getBytes());
            /**
             * jUnit test 완료.
             * input  01012345678
             * secret 123456789
             * output 4c770f779fe27477aed3e125ed0615e6bb6be18f9130c82632fa5723fc650fea (hex)
             * output THcPd5/idHeu0+El7QYV5rtr4Y+RMMgmMvpXI/xlD+o= (base64)
             * */
//            return Base64.encode(sha256_result);
            return SystemUtils.byteArrayToHex(sha256_result);
        } catch (Exception e) {

            LogUtil.e(LOG_TAG , "HMAC SHA256 을 생성하는데 실패하였습니다.");
        }

        return null;
    }

    /**
     * @return deviceID - > LuhnNuber
     * */
    public static String getLuhnNuber(String number) {

        number = getPrefix(number);

        int remainder = 16 - number.length();
        if (remainder > 0) {
            Random random = new Random();
            for (int i = 0; i < remainder; i++) {
                number += random.nextInt(10);//0~9
            }
        }

        return number + getCheckSum(number);
    }

    private static String getPrefix(String number) {
        int length = number.length();
        String rand;
        if (length < 10) {
            Random random = new Random();
            rand = String.valueOf(random.nextInt(8) + 2) + length;//2~9
        } else {
            rand = String.valueOf(length);
        }

        return rand + number;
    }

    private static String getCheckSum(String number) {
        int checkSum = 0;
        String reversedNumber = new StringBuffer(number).reverse().toString();
        for (int i = 0; i < number.length(); i++) {
            int value = 0;
            if (i == 0 || i % 2 == 0) {
                value = Integer.parseInt(reversedNumber.substring(i, i + 1)) * 2;
                if (value > 9) {
                    value = value - 9;
                }
            } else {
                value = Integer.parseInt(reversedNumber.substring(i, i + 1));
            }
            checkSum += value;
        }

        return String.valueOf(checkSum % 10);

    }
}
