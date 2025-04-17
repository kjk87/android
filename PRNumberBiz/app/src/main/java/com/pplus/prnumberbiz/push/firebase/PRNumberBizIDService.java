//package com.pplus.prnumberbiz.push.firebase;
//
//import android.text.TextUtils;
//
//import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.iid.FirebaseInstanceIdService;
//import com.pple.pplus.utils.part.logs.LogUtil;
//import com.pple.pplus.utils.part.utils.StringUtils;
//import com.pplus.prnumberbiz.apps.common.mgmt.AppInfoManager;
//
///**
// * Created by j2n on 2016. 7. 29..
// */
//public class PRNumberBizIDService extends FirebaseInstanceIdService{
//
//    private static final String TAG = "MyFirebaseIIDService";
//
//    /**
//     * Called if InstanceID token is updated. This may occur if the security of the previous token
//     * had been compromised. NoteSend that this is called when the InstanceID token is initially
//     * generated so this is where you would retrieve the token.
//     */
//    // [START refresh_token]
//    @Override
//    public void onTokenRefresh(){
//        // Get updated InstanceID token.
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        if(StringUtils.isNotEmpty(refreshedToken)) {
//            AppInfoManager.getInstance().setPushToken(refreshedToken);
//        }
//
//    }
//    // [END refresh_token]
//
//    /**
//     * Persist token to third-party servers.
//     * <p>
//     * Modify this method to associate the user's FCM InstanceID token with any server-side account
//     * maintained by your application.
//     *
//     * @param token The new token.
//     */
//    public static void sendRegistrationToServer(final String token){
//
//        String appToken = AppInfoManager.getInstance().getPushToken();
//
//        LogUtil.d(TAG, "appToken = " + appToken + " token = " + token);
//
//        if(TextUtils.isEmpty(appToken) || (!TextUtils.isEmpty(appToken) && !TextUtils.isEmpty(token) && !appToken.equals(token))) {
//
//            LogUtil.d(TAG, "Token is send");
//
////            ParamsPush paramsPush = new ParamsPush();
////            paramsPush.setAppVersion(AppInfoManager.getInstance().getAppVersion());
////            paramsPush.setPushRegistrationId(token);
////
////            ApiBuilder.create().setPush(paramsPush).setCallback(new PplusCallback<ResultResponse<BaseResult>>(){
////
////                @Override
////                public void onResponse(Call<ResultResponse<BaseResult>> call, ResultResponse<BaseResult> response){
////
////                    LogUtil.d(TAG, "response = " + response.toString());
////                    AppInfoManager.getInstance().setPushToken(token);
////                    DebugUtilManager.getInstance().updateToken(PRNumberBizApplication.getContext(), token);
////                }
////
////                @Override
////                public void onFailure(Call<ResultResponse<BaseResult>> call, Throwable t, ResultResponse<BaseResult> response){
////
////                }
////            }).build().call();
//        }
//    }
//
//}
