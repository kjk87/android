package com.pple.pplus.utils.part.info;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.pple.pplus.utils.part.logs.LogUtil;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import com.pple.pplus.utils.part.pref.PreferenceUtil;

import static com.pple.pplus.utils.part.info.DeviceUtil.DISPLAY.DENSITY;
import static com.pple.pplus.utils.part.info.DeviceUtil.DISPLAY.DENSITY_DEFAULT;
import static com.pple.pplus.utils.part.info.DeviceUtil.DISPLAY.DENSITY_DPI;
import static com.pple.pplus.utils.part.info.DeviceUtil.DISPLAY.DENSITY_HIGH;
import static com.pple.pplus.utils.part.info.DeviceUtil.DISPLAY.DENSITY_LOW;
import static com.pple.pplus.utils.part.info.DeviceUtil.DISPLAY.DENSITY_MEDIUM;
import static com.pple.pplus.utils.part.info.DeviceUtil.DISPLAY.SCALED_DENSITY;
import static com.pple.pplus.utils.part.info.DeviceUtil.DISPLAY.SCREEN_HEIGHT_PIXELS;
import static com.pple.pplus.utils.part.info.DeviceUtil.DISPLAY.SCREEN_WIDTH_PIXELS;
import static com.pple.pplus.utils.part.info.DeviceUtil.DISPLAY.X_DPI;
import static com.pple.pplus.utils.part.info.DeviceUtil.DISPLAY.Y_DPI;

/**
 * Created by 안명훈 on 16. 6. 28..
 * <p/>
 * <ol> 디바이스 관련된 정보를 반환하도록 합니다.<br>
 * <p/>
 * <p/>
 * </ol>
 */
public class DeviceUtil{

    private static String LOG_TAG = DeviceUtil.class.getSimpleName();

    public static class DEVICE{

        // DEVICE..
        public static String DEVICE = Build.DEVICE;

        public static String MODEL = Build.MODEL;

        public static String PRODUCT = Build.PRODUCT;

        public static String BOARD = Build.BOARD;

        public static String BRAND = Build.BRAND;

        public static String TAGS = Build.TAGS;

        public static String toLog(){

            return "DEVICE{" +
                    "BOARD='" + BOARD + '\'' +
                    ", DEVICE='" + DEVICE + '\'' +
                    ", MODEL='" + MODEL + '\'' +
                    ", PRODUCT='" + PRODUCT + '\'' +
                    ", BRAND='" + BRAND + '\'' +
                    ", TAGS='" + TAGS + '\'' +
                    '}';
        }
    }

    public static class OS{

        // OS
        public static int SDK_VERSION = Build.VERSION.SDK_INT;
        @Deprecated
        public static String API_VERSION = Build.VERSION.SDK;

        public static String RELEAS_VERSION = Build.VERSION.RELEASE;

        public static String INCREMENTAL = Build.VERSION.INCREMENTAL;

        public static String DISPLAY = Build.DISPLAY;

        public static String FINGERPRINT = Build.FINGERPRINT;

        public static String BUILD_ID = Build.ID;

        public static Long BUILD_TIME = Build.TIME;

        public static String BUILD_TYPE = Build.TYPE;

        public static String BUILD_USER = Build.USER;

        public static String toLog(){

            return "OS{" +
                    "API_VERSION='" + API_VERSION + '\'' +
                    ", SDK_VERSION=" + SDK_VERSION +
                    ", RELEAS_VERSION='" + RELEAS_VERSION + '\'' +
                    ", INCREMENTAL='" + INCREMENTAL + '\'' +
                    ", DISPLAY='" + DISPLAY + '\'' +
                    ", FINGERPRINT='" + FINGERPRINT + '\'' +
                    ", BUILD_ID='" + BUILD_ID + '\'' +
                    ", BUILD_TIME=" + BUILD_TIME +
                    ", BUILD_TYPE='" + BUILD_TYPE + '\'' +
                    ", BUILD_USER='" + BUILD_USER + '\'' +
                    '}';
        }
    }

    public static class DISPLAY{

        // DISPLAY
        public static float DENSITY;

        public static int DENSITY_DPI;

        public static float SCALED_DENSITY;

        public static float X_DPI;

        public static float Y_DPI;

        public static int DENSITY_DEFAULT;

        public static int DENSITY_LOW;

        public static int DENSITY_MEDIUM;

        public static int DENSITY_HIGH;

        public static int SCREEN_HEIGHT_PIXELS;

        public static int SCREEN_WIDTH_PIXELS;

        public static String toLog(){

            return "DISPLAY{" +
                    "DENSITY=" + DENSITY +
                    ", DENSITY_DPI=" + DENSITY_DPI +
                    ", SCALED_DENSITY=" + SCALED_DENSITY +
                    ", X_DPI=" + X_DPI +
                    ", Y_DPI=" + Y_DPI +
                    ", DENSITY_DEFAULT=" + DENSITY_DEFAULT +
                    ", DENSITY_LOW=" + DENSITY_LOW +
                    ", DENSITY_MEDIUM=" + DENSITY_MEDIUM +
                    ", DENSITY_HIGH=" + DENSITY_HIGH +
                    ", SCREEN_HEIGHT_PIXELS=" + SCREEN_HEIGHT_PIXELS +
                    ", SCREEN_WIDTH_PIXELS=" + SCREEN_WIDTH_PIXELS +
                    '}';
        }

    }

    public static void initialize(Context context){

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        windowManager.getDefaultDisplay().getMetrics(metrics);

        DENSITY = metrics.density;
        DENSITY_DPI = metrics.densityDpi;
        SCALED_DENSITY = metrics.scaledDensity;
        X_DPI = metrics.xdpi;
        Y_DPI = metrics.ydpi;
        DENSITY_DEFAULT = metrics.DENSITY_DEFAULT;
        DENSITY_LOW = metrics.DENSITY_LOW;
        DENSITY_MEDIUM = metrics.DENSITY_MEDIUM;
        DENSITY_HIGH = metrics.DENSITY_HIGH;
        SCREEN_HEIGHT_PIXELS = metrics.heightPixels;
        SCREEN_WIDTH_PIXELS = metrics.widthPixels;
    }

    public static class PRIVACY{

        private static String PROPERTY_DEVICE_ID = "device_uuid";

        public static String getDeviceUUID(final Context context){

            final String id = PreferenceUtil.getDefaultPreference(context).getString(PROPERTY_DEVICE_ID);

            UUID uuid = null;
            if(id != null) {
                uuid = UUID.fromString(id);
            } else {
                final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                try {
                    if(!"9774d56d682e549c".equals(androidId)) {
                        uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
                    } else {
                        final String deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                        uuid = deviceId != null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")) : UUID.randomUUID();
                    }
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }

                PreferenceUtil.getDefaultPreference(context).put(PROPERTY_DEVICE_ID, uuid.toString());
            }

            return uuid.toString();
        }
        /**
         final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

         final String tmDevice, tmSerial, androidId;
         tmDevice = "" + tm.getDeviceId();
         tmSerial = "" + tm.getSimSerialNumber();
         androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

         UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
         String deviceId = deviceUuid.toString
         * */

    }

    /**
     * 디바이스 전화번호를 불러오도록 합니다.
     *
     * @return String value..
     */
    public static String getDeviceNumber(Context context){

        try {
            String serviceName = Context.TELEPHONY_SERVICE;
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(serviceName);

            String number = telephonyManager.getLine1Number();

            number = number.replace("+82", "0");

            return number;
        } catch (Exception e) {
            LogUtil.e(LOG_TAG, "해당 디바이스는 유심이 없습니다. -- {}", e);
        }
        return null;
    }

    /**
     * @return 디바이스의 와이파이 맥 주소를 반환 합니다.
     */
    public static String getMacAddress(Context context){

        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();

        String mac = null;

        try {
            mac = info.getMacAddress();
        } catch (Exception e) {
            LogUtil.e(LOG_TAG, "해당 디바이스 WIFI 모듈에 문제가 발생하였습니다. -- {}", e);
        }

        return mac;
    }

    public static String getAppVersion(Context context){

        // application version
        String versionName = "";
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            LogUtil.e(LOG_TAG, "NameNotFoundException. -- {}", e);
        }

        return versionName;
    }



}
