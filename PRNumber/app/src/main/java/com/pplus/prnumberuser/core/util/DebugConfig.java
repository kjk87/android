package com.pplus.prnumberuser.core.util;

import android.content.Context;

import com.google.gson.Gson;
import com.pplus.utils.Config;
import com.pplus.utils.part.logs.LogUtil;
import com.pplus.utils.part.pref.PreferenceUtil;
import com.pplus.utils.part.utils.StringUtils;
import com.pplus.prnumberuser.PRNumberApplication;
import com.pplus.prnumberuser.apps.common.mgmt.DebugUtilManager;
import com.pplus.prnumberuser.core.debug.DebugConstant;

import com.pplus.networks.common.NetworkConfig;
import com.pplus.networks.common.NetworkInfo;

/**
 * Created by 김종경 on 2016-11-28.
 */

public class DebugConfig{

    private static boolean isDebugServer = false;
    private static boolean isDebugMode = false;
    private static boolean isStagingServer = false; // 스테이징 서버 접속

    private static boolean isInitData = false; // Data 셋팅 여부

    public static boolean isDebugServer(){

        return isDebugServer;
    }

    public static boolean isDebugMode(){

        return isDebugMode;
    }
    public static void setDebugMode(boolean debug){
        isDebugMode = debug;
    }
    public static boolean isStagingServer(){

        return isStagingServer;
    }

    public static void init(Context context){

        //DebugUtilManager.getInstance().loadDebugData(context);
//        isDebugServer = DebugUtilManager.getInstance().getDebugValue().isDebugServer();
//        isDebugMode = DebugUtilManager.getInstance().getDebugValue().isDebugMode();
//        isStagingServer = DebugUtilManager.getInstance().getDebugValue().isStagingServer();

        if(isDebugMode) {
            Config.setLogEnable(Config.CONFIG.ON);
        }

        isInitData = true;

        NetworkConfig.setNetworkInfo(new NetworkInfo(){

            @Override
            public NetworkConfig.SWITCH isDebug(){

                if(!isInitData) {
                    setData();
                }

                if(isDebugMode) {
                    return NetworkConfig.SWITCH.ON;
                } else {
                    return NetworkConfig.SWITCH.OFF;
                }

            }

            @Override
            public NetworkConfig.DEVELOP_MODE getServiceMode(){

                if(!isInitData) {
                    setData();
                }

                if(isDebugServer) {
                    return NetworkConfig.DEVELOP_MODE.DEV;
                }
                return NetworkConfig.DEVELOP_MODE.REAL;
            }

            @Override
            public String BaseRealServerUrl(){

                if(!isInitData) {
                    setData();
                }

                String debugUrl = DebugUtilManager.getInstance().getDebugValue().getServerUrl();
                if(StringUtils.isNotEmpty(debugUrl)) {
                    LogUtil.e("DebugConfig", "url : {}", debugUrl);
                    return debugUrl;
                }
                if(isStagingServer) {
                    return NetworkConfig.API_STAGING_POINT_URL;
                }
                return NetworkConfig.API_END_REAL_POINT_URL;
            }

            @Override
            public String BaseDevServerUrl(){

                if(!isInitData) {
                    setData();
                }

                String debugUrl = DebugUtilManager.getInstance().getDebugValue().getServerUrl();
                if(StringUtils.isNotEmpty(debugUrl)) {
                    return debugUrl;
                }
                if(isStagingServer) {
                    return NetworkConfig.API_STAGING_POINT_URL;
                }
                return NetworkConfig.API_END_DEV_POINT_URL;
            }
        });
    }

    private static void setData(){
        String data = PreferenceUtil.getDefaultPreference(PRNumberApplication.getContext()).getString(DebugConstant.DEBUG_SHARED_NAME);
        if(data != null) {
            DebugUtilManager.DebugValue mDebugValue = new Gson().fromJson(data, DebugUtilManager.DebugValue.class);
            DebugUtilManager.getInstance().setDebugValue(mDebugValue);
        }
        isDebugServer = DebugUtilManager.getInstance().getDebugValue().isDebugServer();
        isDebugMode = DebugUtilManager.getInstance().getDebugValue().isDebugMode();
        isStagingServer = DebugUtilManager.getInstance().getDebugValue().isStagingServer();
    }
}
