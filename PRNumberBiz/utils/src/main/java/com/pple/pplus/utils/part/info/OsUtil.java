package com.pple.pplus.utils.part.info;

import android.os.Build;

/**
 * Created by 안명훈 on 16. 7. 4..
 */
public class OsUtil {

    /**
     * 2.3
     * */
    public static boolean isGingerbread(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD) {
            return true;
        }
        return false;
    }

    /**
     * 2.3.3
     * */
    public static boolean isGingerbread_MR1(){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
            return true;
        }
        return false;
    }

    /**
     * 4.0
     * */
    public static boolean isIceCreamSandwich(){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return true;
        }
        return false;
    }

    /**
     * 4.1
     * */
    public static boolean isJellyBean(){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return true;
        }
        return false;
    }

    /**
     * 4.2
     * */
    public static boolean isJellyBean_MR1(){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return true;
        }
        return false;
    }

    /**
     * 4.3
     * */
    public static boolean isJellyBean_MR2(){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return true;
        }
        return false;
    }

    /**
     * 4.4
     * */
    public static boolean isKitkat(){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return true;
        }
        return false;
    }

    /**
     * 4.4 WATCH
     * */
    public static boolean isKitkatWatch(){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            return true;
        }
        return false;
    }

    /**
     * 6.0
     * */
    public static boolean isLollipop(){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return true;
        }
        return false;
    }

    /**
     * 6.0
     * */
    public static boolean isMarshmallow(){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return true;
        }
        return false;
    }

    public static boolean isNougat(){
        //TODO 아직 미출시 OS 누가 안드로이드 7.0
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return true;
        }
        return false;
    }

    public static boolean isOreo(){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return true;
        }
        return false;
    }
}
