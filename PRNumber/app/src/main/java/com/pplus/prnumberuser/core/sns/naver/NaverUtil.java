package com.pplus.prnumberuser.core.sns.naver;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;
import com.pplus.utils.part.logs.LogUtil;
import com.pplus.prnumberuser.R;

/**
 * Created by 김종경 on 2016-08-08.
 */
public class NaverUtil{

    /**
     * client 정보를 넣어준다.
     */
//    private static String OAUTH_CLIENT_ID = "33JxoKJJTfe1MlB3IjCR";
//    private static String OAUTH_CLIENT_SECRET = "TBG6RBWa7e";
//    private static String OAUTH_CLIENT_NAME = "네이버 아이디로 로그인";

    private static OAuthLogin mOAuthLoginModule;
    private static Context mContext;
    private static NaverCallbackListener mNaverCallbackListener;

    public interface NaverCallbackListener{

        public void onSuccess(String token, String content);

        public void onError();
    }

    public static void init(Context context, OAuthLoginButton oAuthLoginButton, NaverCallbackListener naverCallbackListener){

        mContext = context;
        mOAuthLoginModule = OAuthLogin.getInstance();
        mNaverCallbackListener = naverCallbackListener;
        mOAuthLoginModule.init(context, context.getString(R.string.naver_client_id), context.getString(R.string.naver_client_secret), context.getString(R.string.app_name2));
        oAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);
    }

    public static void click(Activity activity, NaverCallbackListener naverCallbackListener){
        mContext = activity;
        mNaverCallbackListener = naverCallbackListener;
        mOAuthLoginModule = OAuthLogin.getInstance();
        mOAuthLoginModule.init(mContext, activity.getString(R.string.naver_client_id), activity.getString(R.string.naver_client_secret), mContext.getString(R.string.app_name2));
        mOAuthLoginModule.startOauthLoginActivity(activity, mOAuthLoginHandler);
    }

    public static void logout(Context context){
        if(mOAuthLoginModule != null){
            mOAuthLoginModule.logoutAndDeleteToken(context);
        }
    }

    /**
     * startOAuthLoginActivity() 호출시 인자로 넘기거나, OAuthLoginButton 에 등록해주면 인증이 종료되는 걸 알 수 있다.
     */
    private static OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler(){

        @Override
        public void run(boolean success){

            if(success) {
                String accessToken = mOAuthLoginModule.getAccessToken(mContext);
                String refreshToken = mOAuthLoginModule.getRefreshToken(mContext);
                long expiresAt = mOAuthLoginModule.getExpiresAt(mContext);
                String tokenType = mOAuthLoginModule.getTokenType(mContext);

                new RequestApiTask().execute();

            } else {
                String errorCode = mOAuthLoginModule.getLastErrorCode(mContext).getCode();
                String errorDesc = mOAuthLoginModule.getLastErrorDesc(mContext);
                LogUtil.e("NAVER", "errorCode : {}, errorDesc : {}", errorCode, errorDesc);
                if(mNaverCallbackListener != null) {
                    mNaverCallbackListener.onError();
                }
            }
        }
    };

    private static class RequestApiTask extends AsyncTask<Void, Void, String>{

        @Override
        protected void onPreExecute(){

        }

        @Override
        protected String doInBackground(Void... params){

            String url = "https://openapi.naver.com/v1/nid/me";
            String at = mOAuthLoginModule.getAccessToken(mContext);
            return mOAuthLoginModule.requestApi(mContext, at, url);
        }

        protected void onPostExecute(String content){

            Log.e("result", content);
            if(mNaverCallbackListener != null) {
                mNaverCallbackListener.onSuccess(mOAuthLoginModule.getAccessToken(mContext), content);
            }
        }
    }
}
