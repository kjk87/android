package com.pplus.luckybol.core.network.apis;

import com.google.gson.JsonArray;
import com.pplus.luckybol.core.network.model.dto.LinkMine;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by j2n on 2016. 7. 25..
 * <p>
 * <pre>
 *     Application의 api interface를 정의합니다.
 * </pre>
 */
public interface ILinkMineApi {

    @GET("media/ads.proc?mda_code=FVRKmZKt1l&app_code=Aua0E8Gg6b&tp=JSON")
    Call<JsonArray> getAdsList();

}
