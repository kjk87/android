package com.pplus.luckybol.apps.common.mgmt;

import android.content.Context;
import android.os.Bundle;

import com.pplus.luckybol.LuckyBolApplication;
import com.pplus.luckybol.core.debug.DebugConstant;
import com.pplus.luckybol.core.debug.DebugManager;

import java.io.Serializable;

/**
 * Created by ksh on 2016-10-17.
 */

public class DebugUtilManager{

    private static DebugUtilManager mDebugUtilManager;
    //public static final Uri PPLUS_URI = Uri.parse("content://kr.co.j2n.pplusutil.PplusProvider/pplus_debug");

    public static final String DEBUG_APP_NAME = "kr.co.j2n.pplusutil";
    public static final String DEBUG_SHARED_NAME = "pplus_debug";
    public static final String DEBUG_SHARED_MODE = "debug_mode";
    public static final String DEBUG_SHARED_SERVER_MODE = "debug_server_mode";
    public static final String DEBUG_SHARED_STAGING_MODE = "staging_mode";
    public static final String DEBUG_SHARED_TOKEN = "token";
    public static final String DEBUG_SERVER_URL = "";

    private DebugValue mDebugValue;

    public static DebugUtilManager getInstance(){

        if(mDebugUtilManager == null) {
            mDebugUtilManager = new DebugUtilManager();
        }
        return mDebugUtilManager;
    }

    public DebugUtilManager(){

        mDebugValue = new DebugValue();
    }

    public static class DebugValue implements Serializable{

        private boolean debugMode = false;
        private boolean debugServer = false;
        private boolean stagingServer = false;
        private String serverUrl = "";

        public boolean isDebugMode(){

            return debugMode;
        }

        public void setDebugMode(boolean debugMode){

            this.debugMode = debugMode;
        }

        public boolean isDebugServer(){

            return debugServer;
        }

        public void setDebugServer(boolean debugServer){

            this.debugServer = debugServer;
        }

        public boolean isStagingServer(){

            return stagingServer;
        }

        public void setStagingServer(boolean stagingServer){

            this.stagingServer = stagingServer;
        }

        public String getServerUrl(){

            return serverUrl;
        }

        public void setServerUrl(String serverUrl){

            this.serverUrl = serverUrl;
        }

        @Override
        public String toString(){

            return "DebugValue{" +
                    "debugMode=" + debugMode +
                    ", debugServer=" + debugServer +
                    ", stagingServer=" + stagingServer +
                    ", serverUrl='" + serverUrl + '\'' +
                    '}';
        }
    }

    /**
     * Debug 정보를 가져옴.
     *
     * @param context
     *
     * @return
     */
/*
    public void loadDebugData(Context context){

        try {

            LogUtil.e("DEBUG_UTIL", "loadDebugData");
            Cursor cursor = context.getContentResolver().query(PPLUS_URI, null, "debug_app_name = ?", new String[]{"com.pplus"}, null);

            if(cursor == null || !cursor.moveToFirst()) {
                LogUtil.e("DEBUG_UTIL", "cursor null");
                return;
            }

            mDebugValue = new DebugValue();
            mDebugValue.setDebugMode(cursor.getInt(cursor.getColumnIndex("debug_mode")) == 1);
            mDebugValue.setDebugServer(cursor.getInt(cursor.getColumnIndex("debug_server_mode")) == 1);
            mDebugValue.setStagingServer(cursor.getInt(cursor.getColumnIndex("staging_mode")) == 1);
            mDebugValue.setServerUrl(cursor.getString(cursor.getColumnIndex("server_url")));
        } catch (Exception e) {
            LogUtil.e("DEBUG_UTIL", "error : {}", e.toString());
            return;
        }

    }
*/

    public void updateToken(Context context, String token){

/*
        try {
            ContentValues values = new ContentValues();
            values.put("token", token);
            context.getContentResolver().update(PPLUS_URI, values, "debug_app_name = ?", new String[]{"com.pplus"});
        } catch (Exception e) {

        }
*/

        Bundle bundle = new Bundle();
        bundle.putString(DebugConstant.DEBUG_TOKEN, token);
        DebugManager.with(LuckyBolApplication.getContext(), null).send(DebugConstant.SEND_TOKEN_DATA, bundle);
/*
        DebugManager.with(getApplicationContext(), null).setServiceConnectInterface(new DebugManager.onServiceConnectInterface(){

            @Override
            public void onServiceConnect(){
                DebugManager.with(getApplicationContext(), null).stopBindService();
            }
        }).setBundleData(bundle, DebugConstant.SEND_TOKEN_DATA).startBindService();
*/

    }

    public void setDebugValue(DebugValue mDebugValue){

        this.mDebugValue = mDebugValue;
    }

    public DebugValue getDebugValue(){

        return mDebugValue;
    }



    //    public String getDebugSharedData(Context context, String strKey, String isDefaultValue){
    //
    //        try {
    //            Context ctx = null;
    //
    //            if(context == null) {
    //                return null;
    //            }
    //
    //            try {
    //                ctx = context.createPackageContext(DEBUG_APP_NAME, 0);
    //            } catch (PackageManager.NameNotFoundException e1) {
    //                //e1.printStackTrace();
    //            }
    //
    //            SharedPreferences prefs = null;
    //
    //            if(ctx != null)
    //                prefs = ctx.getSharedPreferences(DEBUG_SHARED_NAME, Context.MODE_WORLD_WRITEABLE | Context.MODE_WORLD_READABLE);
    //
    //            if(prefs == null) {
    //                return null;
    //            }
    //
    //            String objReturn = null;
    //
    //            objReturn = prefs.getString(strKey, null);
    //
    //            return objReturn;
    //        } catch (Exception e) {
    //            return null;
    //        }
    //
    //    }

    //    public boolean setDebugSharedData(Context context, String key, String value){
    //
    //        try {
    //            if(context == null) {
    //                return false;
    //            }
    //
    //            SharedPreferences prefs = null;
    //
    //            prefs = context.getSharedPreferences(DEBUG_SHARED_NAME, Context.MODE_WORLD_WRITEABLE | Context.MODE_WORLD_READABLE);
    //
    //            if(prefs == null) {
    //                return false;
    //            }
    //
    //            SharedPreferences.Editor editor = prefs.edit();
    //            editor.putString(key, value);
    //            return editor.commit();
    //        } catch (Exception e) {
    //            return false;
    //        }
    //    }


}
