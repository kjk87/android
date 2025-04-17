package com.pplus.prnumberuser.core.debug;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.pplus.utils.part.pref.PreferenceUtil;
import com.pplus.prnumberuser.apps.common.mgmt.DebugUtilManager;
import com.pplus.prnumberuser.core.util.DebugConfig;

/**
 * Handler receiving the message transmitted from Messsenger
 */

public class DebugHandler extends Handler implements DebugConstant {

    private static final String TAG = DebugHandler.class.getSimpleName();
    Context mContext;
    DebugManager.onServiceConnectInterface serviceConnectInterface;

    DebugHandler(Context context, DebugManager.onServiceConnectInterface serviceConnectInterface) {
        this.mContext = context;
        this.serviceConnectInterface = serviceConnectInterface;
    }

    @Override
    public void handleMessage(Message msg) {
        Bundle bundle = msg.getData();
        Log.d(TAG, "bundle = " + bundle.toString());
        switch (msg.what) {
            case RECV_DEBUG_DATA:
                String bundleString = bundle.getString(DebugConstant.DEBUG_SHARED_NAME);
                PreferenceUtil.getDefaultPreference(mContext).put(DebugConstant.DEBUG_SHARED_NAME, bundleString);
                Log.d(TAG, "bundleString = " + bundleString);
                DebugUtilManager.DebugValue mDebugValue = new Gson().fromJson(bundleString, DebugUtilManager.DebugValue.class);
                if(mDebugValue != null)
                    Log.d(TAG, "mDebugValue = " + mDebugValue.toString());
                DebugUtilManager.getInstance().setDebugValue(mDebugValue);
                DebugConfig.init(mContext);
                if(serviceConnectInterface != null) {
                    serviceConnectInterface.onServiceConnect();
                }
                break;
            default:
                super.handleMessage(msg);
        }
    }
}
