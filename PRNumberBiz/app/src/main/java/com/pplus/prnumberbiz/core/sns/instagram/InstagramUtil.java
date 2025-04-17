package com.pplus.prnumberbiz.core.sns.instagram;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 김종경 on 2016-06-14.
 */
public class InstagramUtil{

    public static final String CLIENT_ID = "d9ea81725dfb4523b8bb5e36b74a57fe";
    public static final String CLIENT_SECRET = "678feb63bf0c44e685bd315b43b31e11";
    public static final String CALLBACK_URL = "http://www.j2n.co.kr";

    private static final String BASE_URL = "https://api.instagram.com";
    private static final String AUTH_URL = "https://api.instagram.com/oauth/authorize/";

    public interface InstagramListener{

        public void onResponse(Call<JsonObject> call, Response<JsonObject> response);

        public void onFailure(Call<JsonObject> call, Throwable t);

        public void onError(String error);
    }

    private Context mContext;
    private InstagramListener mListener;

    public InstagramUtil(Context context){

        this.mContext = context;
    }

    /**
     * 로그인
     */
    public void signIn(InstagramListener listener){

        this.mListener = listener;
        String authURL = AUTH_URL + "?client_id=" + CLIENT_ID + "&redirect_uri=" + CALLBACK_URL + "&response_type=code&display=touch&scope=basic";
        new InstagramDialog(mContext, authURL, new InstagramDialog.OAuthDialogListener(){

            @Override
            public void onComplete(String accessToken){

                Log.e("accessToken", accessToken);
                OkHttpClient client = new OkHttpClient();

                Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(client).build();
                InstagramApi api = retrofit.create(InstagramApi.class);

                Map<String, Object> params = new HashMap<>();
                params.put("client_id", CLIENT_ID);
                params.put("client_secret", CLIENT_SECRET);
                params.put("grant_type", "authorization_code");
                params.put("redirect_uri", CALLBACK_URL);
                params.put("code", accessToken);

                Call<JsonObject> call = api.accessToken(params);
                call.enqueue(new Callback<JsonObject>(){

                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response){

                        if(mListener != null) {
                            mListener.onResponse(call, response);
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t){

                        if(mListener != null) {
                            mListener.onFailure(call, t);
                        }
                    }
                });

            }

            @Override
            public void onError(String error){

                if(mListener != null) {
                    mListener.onError(error);
                }
            }
        }).show();
    }
}
