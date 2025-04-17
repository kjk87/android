package com.pplus.prnumberuser.core.sns.facebook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by 김종경 on 2016-06-29.
 */

public class FaceBookUtil{

    private static final String TAG = "FaceBookUtil";

    /**
     * 콜백 리스너
     */
    public interface FaceBookCallbackListener{

        public void onSuccess(LoginResult loginResult);

        public void onCancel();

        public void onError(FacebookException exception);

        public void onCompleted(JSONObject object, GraphResponse response);
    }

    /**
     * 콜백 변수
     */
    private static CallbackManager callbackManager;

    private static FaceBookCallbackListener mListener;

    /**
     * facebook 초기화, listener 등록
     */
    public static void init(Context context){

        FacebookSdk.sdkInitialize(context);
        callbackManager = CallbackManager.Factory.create();
    }

    /**
     * 콜백 register 등록
     */
    public static void registerCallback(FaceBookCallbackListener listener){

        mListener = listener;
        LoginManager.getInstance().logOut();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>(){

            @Override
            public void onSuccess(LoginResult loginResult){

                if(mListener != null) {
                    mListener.onSuccess(loginResult);
                }
            }

            @Override
            public void onCancel(){

                if(mListener != null) {
                    mListener.onCancel();
                }
            }

            @Override
            public void onError(FacebookException exception){

                if(mListener != null) {
                    mListener.onError(exception);
                }
            }
        });
    }

    /**
     * public_profile 로그인 요청 activity
     **/
    public static void logIn(Activity activity){

        LoginManager.getInstance().logInWithReadPermissions(activity, null);
    }

    /**
     * public_profile 로그인 요청 fragment
     **/
    public static void logIn(Fragment fragment){

        LoginManager.getInstance().logInWithReadPermissions(fragment, null);
    }

    /**
     * publish_actions 로그인 요청
     **/
    public static void logInWithPublishActions(Activity activity){

        LoginManager.getInstance().logInWithPublishPermissions(activity, Arrays.asList("publish_actions"));
    }

    /**
     * public_profile 로그인 요청 fragment
     **/
    public static void logInWithPublicProfile(Fragment fragment){

        LoginManager.getInstance().logInWithReadPermissions(fragment, Arrays.asList("email", "public_profile"));
    }

    /**
     * 로그인 permission 직접 요청 activity
     **/
    public static void logInWithReadPermissions(Activity activity, Collection<String> permissions){

        LoginManager.getInstance().logInWithReadPermissions(activity, permissions);
    }

    /**
     * 로그인 permission 직접 요청 fragment
     **/
    public static void logInWithReadPermissions(Fragment fragment, Collection<String> permissions){

        LoginManager.getInstance().logInWithReadPermissions(fragment, permissions);
    }

    /**
     * 로그인 성공후 프로필 요청 onCompleted로 떨어짐
     **/
    public static void requestProfile(AccessToken token){

        GraphRequest request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback(){

            @Override
            public void onCompleted(JSONObject object, GraphResponse response){

                if(mListener != null) {
                    mListener.onCompleted(object, response);
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,birthday,gender");
        request.setParameters(parameters);
        request.executeAsync();
    }


    /**
     * callbackManager onActivityResult 함수 호출
     **/
    public static void onActivityResult(int requestCode, int resultCode, Intent data){

        if(callbackManager != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
}
