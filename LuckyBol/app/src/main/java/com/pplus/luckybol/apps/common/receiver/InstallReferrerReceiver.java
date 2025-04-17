//package com.pplus.luckybol.apps.common.receiver;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//
//import com.pplus.utils.part.logs.LogUtil;
//
///**
// * Created by imac on 2017. 8. 21..
// */
//
//public class InstallReferrerReceiver extends BroadcastReceiver{
//
//    @Override
//    public void onReceive(Context context, Intent intent){
//
//        String referrer = intent.getStringExtra("referrer");
//        LogUtil.e("InstallReferrerReceiver", "InstallReferrerReceiver : "+referrer);
////        if(StringUtils.isNotEmpty(referrer)){
////            String[] referrers = referrer.split("&");
////
////            String participateID = "";
////            for (String referrerValue : referrers) {
////                String keyValue[] = referrerValue.split("=");
////                if(keyValue[0].equals("participateID")){
////                    participateID = keyValue[1];
////                    break;
////                }
////            }
////
////            if(StringUtils.isNotEmpty(participateID)){
////                ZzalIntentService.Companion.startActionZZal(context, participateID);
////                LogUtil.e("participateID", "participateID : {}", participateID);
////            }
////
////            String adType = "";
////            for (String referrerValue : referrers) {
////                String keyValue[] = referrerValue.split("=");
////                if(keyValue[0].equals("adType")){
////                    adType = keyValue[1];
////                    break;
////                }
////            }
////
////            if(StringUtils.isNotEmpty(adType)){
////                ZzalIntentService.Companion.startActionAdType(context, adType);
////                LogUtil.e("adType", "adType : {}", adType);
////            }
////
////            String recommendKey = "";
////            for (String referrerValue : referrers) {
////                String keyValue[] = referrerValue.split("=");
////                if(keyValue[0].equals("recommendKey")){
////                    recommendKey = keyValue[1];
////                    break;
////                }
////            }
////            if(StringUtils.isNotEmpty(recommendKey)){
////                PreferenceUtil.getDefaultPreference(context).put(Const.RECOMMEND, recommendKey);
////                LogUtil.e("recommendKey", "recommendKey : {}", recommendKey);
////            }
////        }
//
//    }
//}
