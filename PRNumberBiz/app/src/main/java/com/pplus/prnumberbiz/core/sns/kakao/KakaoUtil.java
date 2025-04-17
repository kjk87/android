package com.pplus.prnumberbiz.core.sns.kakao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.kakao.auth.AccessTokenCallback;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.KakaoSDK;
import com.kakao.auth.Session;
import com.kakao.auth.authorization.accesstoken.AccessToken;
import com.kakao.kakaostory.KakaoStoryService;
import com.kakao.kakaostory.callback.StoryResponseCallback;
import com.kakao.kakaostory.response.ProfileResponse;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 김종경 on 2016-07-06.
 */

public class KakaoUtil{

    public interface KakaoStoryListener{

        public void success(ProfileResponse result);

        public void failure();
    }

    private static KakaoStoryListener mListener;

    private static volatile Activity currentActivity = null;


    public static Activity getCurrentActivity(){

        return currentActivity;
    }


    /**
     * Activity가 올라올때마다 Activity의 onCreate에서 호출해줘야한다.
     */
    public static void setCurrentActivity(Activity currentActivity){

        KakaoUtil.currentActivity = currentActivity;
    }

    /**
     * Application에서 초기화
     */
    public static void init(Context context){

        KakaoSDK.init(new KakaoSDKAdapter(context));
    }

    public static void me(Activity activity, final MeV2ResponseCallback callback){
        List<String> neededScopes = new ArrayList<>();
        neededScopes.add("account_email");
        Session.getCurrentSession().updateScopes(activity, neededScopes, new AccessTokenCallback() {
            @Override
            public void onAccessTokenReceived(AccessToken accessToken) {
                List<String> keys = new ArrayList<>();
                keys.add("properties.nickname");
                keys.add("kakao_account.email");


                UserManagement.getInstance().me(keys, callback);
            }

            @Override
            public void onAccessTokenFailure(ErrorResult errorResult) {

            }
        });
    }

    public static void logout() {
        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
            }
        });
    }

    /**
     * 카카오스토리 유저확인
     */
    public static void requestIsStoryUser(KakaoStoryListener listener){
        mListener = listener;

        KakaoStoryService.getInstance().requestIsStoryUser(new StoryResponseCallback<Boolean>(){

            @Override
            public void onNotKakaoStoryUser(){
                if(mListener != null){
                    mListener.failure();
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult){
                if(mListener != null){
                    mListener.failure();
                }
            }

            @Override
            public void onNotSignedUp(){
                if(mListener != null){
                    mListener.failure();
                }
            }

            @Override
            public void onSuccess(Boolean result){

                if(result) {
                    requestProfile();
                } else {
                    if(mListener != null){
                        mListener.failure();
                    }
                }
            }
        });
    }

    /**
     * 프로필 요청
     */
    public static void requestProfile(){

        KakaoStoryService.getInstance().requestProfile(new StoryResponseCallback<ProfileResponse>(){

            @Override
            public void onNotKakaoStoryUser(){
                if(mListener != null){
                    mListener.failure();
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult){
                if(mListener != null){
                    mListener.failure();
                }
            }

            @Override
            public void onNotSignedUp(){
                if(mListener != null){
                    mListener.failure();
                }
            }

            @Override
            public void onSuccess(ProfileResponse result){

                if(result != null) {
                    if(mListener != null){
                        mListener.success(result);
                    }
                }

            }
        });

    }

    /**
     * Activity onActivity 호출
     */
    public static void handleActivityResult(int requestCode, int resultCode, Intent data){

        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
    }

    /**
     * onCreate에서 등록
     */
    public static void addCallback(ISessionCallback callback){

        Session.getCurrentSession().addCallback(callback);
    }

    /**
     * onDestroy에서 등록
     */
    public static void remvoeCallback(ISessionCallback callback){

        Session.getCurrentSession().removeCallback(callback);
    }
}
