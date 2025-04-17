package com.pplus.prnumberbiz.core.sns.google;

import android.app.Activity;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import android.widget.Toast;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.pplus.prnumberbiz.R;

/**
 * Created by 김종경 on 2016-06-30.
 */

public class GoogleUtil {

    private static final int RC_SIGN_IN = 9000;

    /**
     * GoogleSignInResult 리스너
     */
    public interface GoogleSignListener{

        public void handleSignInResult(GoogleSignInResult result);
    }

    private static GoogleApiClient mGoogleApiClient;

    private static GoogleSignListener mListener;

    private static void validateServerClientID(Activity activity){

        String serverClientId = activity.getString(R.string.default_web_client_id);
        String suffix = ".apps.googleusercontent.com";
        if(!serverClientId.trim().endsWith(suffix)) {
            String message = "Invalid server client ID in strings.xml, must end with " + suffix;
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 초기화, listener등록 후 sign in 시도
     **/
    public static void init(FragmentActivity activity, GoogleSignListener listener){

        mListener = listener;

        validateServerClientID(activity);

        if(mGoogleApiClient == null || !mGoogleApiClient.isConnected()){
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(activity.getString(R.string.default_web_client_id)).requestServerAuthCode(activity.getString(R.string.default_web_client_id), false).requestEmail().build();
            mGoogleApiClient = new GoogleApiClient.Builder(activity).enableAutoManage(activity /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener(){

                @Override
                public void onConnectionFailed(@NonNull ConnectionResult connectionResult){

                }
            } ).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        }

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public static void signOut(){
        if(mGoogleApiClient.isConnected()){
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        }
    }

    /**
     * onActivityResult 함수에서 호출
     **/
    public static void onActivityResult(int requestCode, int resultCode, Intent data){

        switch (requestCode) {
            case RC_SIGN_IN:
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if(mListener != null) {
                    mListener.handleSignInResult(result);
                }
                break;
        }
    }
}
