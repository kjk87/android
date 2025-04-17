package com.pplus.prnumberbiz.core.util;

import android.content.Context;

import com.pple.pplus.utils.Config;

/**
 * Created by 김종경 on 2016-11-28.
 */

public class DebugConfig{

    private static boolean isDebugMode = false;


    public static boolean isDebugMode(){

        return isDebugMode;
    }

    public static void setDebugMode(boolean debug){

        isDebugMode = debug;
    }

    public static void init(Context context){

        if(isDebugMode) {
            Config.setLogEnable(Config.CONFIG.ON);
        } else {
            Config.setLogEnable(Config.CONFIG.OFF);
        }
    }
}
