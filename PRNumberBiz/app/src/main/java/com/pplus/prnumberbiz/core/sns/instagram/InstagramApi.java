package com.pplus.prnumberbiz.core.sns.instagram;

import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


/**
 * Created by J2N on 16. 6. 20..
 */
public interface InstagramApi{

    @FormUrlEncoded
    @POST("/oauth/access_token")
        // 회원가입
    Call<JsonObject> accessToken(@FieldMap Map<String, Object> fieldsMap);

}
