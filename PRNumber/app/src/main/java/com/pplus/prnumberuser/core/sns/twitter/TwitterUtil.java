//package com.pplus.prnumberuser.core.sns.twitter;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Build;
//import android.text.TextUtils;
//import android.util.Log;
//
//import com.crashlytics.android.Crashlytics;
//import com.crashlytics.android.core.CrashlyticsCore;
//import com.pplus.utils.part.info.DeviceUtil;
//import com.pplus.prnumberuser.R;
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager;
//import com.twitter.sdk.android.Twitter;
//import com.twitter.sdk.android.core.Callback;
//import com.twitter.sdk.android.core.Result;
//import com.twitter.sdk.android.core.TwitterAuthConfig;
//import com.twitter.sdk.android.core.TwitterException;
//import com.twitter.sdk.android.core.TwitterSession;
//import com.twitter.sdk.android.core.identity.TwitterAuthClient;
//
//import io.fabric.sdk.android.Fabric;
//
///**
// * Created by 김종경 on 2016-06-29.
// */
//
//public class TwitterUtil{
//
//    private static final String TAG = "TwitterUtil";
//
//
//    public interface TwitterAuthListener{
//
//        public void success(Result<TwitterSession> result);
//
//        public void failure(TwitterException e);
//    }
//
//    private static TwitterAuthClient mTwitterAuthClient;
//    private static TwitterAuthListener mListener;
//    private Context mContext;
//
//    public TwitterUtil(Context context){
//
//        this.mContext = context;
//    }
//
//    /**
//     * twitter 초기화
//     */
//    public static void init(Context context){
//        if(!Fabric.isInitialized()) {
//            //twitter
//            TwitterAuthConfig authConfig = new TwitterAuthConfig(context.getString(R.string.twitter_consumer_key), context.getString(R.string.twitter_consumer_secret));
//            Fabric.with(context, new Twitter(authConfig), new CrashlyticsCore(), new Crashlytics());
//
//            // fabric
//            String pNum = null;
//
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                if(LoginInfoManager.getInstance().isMember()) {
//                    pNum = LoginInfoManager.getInstance().getUser().getMobile();
//                    if(TextUtils.isEmpty(pNum)) {
//                        pNum = "6.0";
//                    }
//                } else {
//                    pNum = "6.0";
//                }
//
//            } else {
//                pNum = DeviceUtil.getDeviceNumber(context);
//            }
//            if(LoginInfoManager.getInstance().isMember()) {
//                Crashlytics.getInstance().setUserName(String.valueOf(LoginInfoManager.getInstance().getUser().getNo()));
////                Crashlytics.getInstance().setUserEmail(""+LoginInfoManager.getInstance().getUser().getEmail());
//            }
//
//            Crashlytics.getInstance().setUserIdentifier(pNum);
//
//        }
//
//    }
//
//    /**
//     * twitter 승인
//     */
//    public static void auth(Activity activity, TwitterAuthListener listener){
//
//        mTwitterAuthClient = new TwitterAuthClient();
//        mListener = listener;
//        mTwitterAuthClient.authorize(activity, new Callback<TwitterSession>(){
//
//            @Override
//            public void success(Result<TwitterSession> result){
//
//                if(mListener != null) {
//                    mListener.success(result);
//                }
//            }
//
//            @Override
//            public void failure(TwitterException e){
//
//                Log.e(TAG, e.toString());
//                if(mListener != null) {
//                    mListener.failure(e);
//                }
//            }
//        });
//    }
//
//    /**
//     * onActivityResult 에서 함수 호출
//     **/
//    public static void onActivityResult(int requestCode, int resultCode, Intent data){
//
//        if(mTwitterAuthClient != null) {
//            mTwitterAuthClient.onActivityResult(requestCode, resultCode, data);
//        }
//    }
//}
