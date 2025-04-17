package com.pplus.prnumberbiz.apps.common.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.igaworks.IgawReceiver;
import com.pple.pplus.utils.part.logs.LogUtil;
import com.pple.pplus.utils.part.pref.Preference;
import com.pple.pplus.utils.part.pref.PreferenceUtil;
import com.pple.pplus.utils.part.utils.StringUtils;
import com.pplus.prnumberbiz.Const;

/**
 * Created by imac on 2017. 8. 21..
 */

public class InstallReferrerReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent){
        IgawReceiver igawReceiver = new IgawReceiver();
        igawReceiver.onReceive(context, intent);

        String referrer = intent.getStringExtra("referrer");
        if(StringUtils.isNotEmpty(referrer)){
            String[] referrers = referrer.split("&");
            String recommendKey = "";
            for (String referrerValue : referrers) {
                String keyValue[] = referrerValue.split("=");
                if(keyValue[0].equals("recommendKey")){
                    recommendKey = keyValue[1];
                    break;
                }
            }
            if(StringUtils.isNotEmpty(recommendKey)){
                PreferenceUtil.getDefaultPreference(context).put(Const.RECOMMEND, recommendKey);
                LogUtil.e("recommendKey", "recommendKey : {}", recommendKey);
            }
        }
    }
}
