package com.pplus.luckybol.core.network.apis;

import com.pplus.luckybol.core.network.model.dto.MobonResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by j2n on 2016. 7. 25..
 * <p>
 * <pre>
 *     Application의 api interface를 정의합니다.
 * </pre>
 */
public interface IMobonApi {

    @GET("API/ver2.1/JSON/sNo/{s}/AD")
    Call<MobonResult> requestShopping(@Path("s") String s, @Query("adGubun") String adGubun, @Query("cnt") int cnt, @Query("cate") String cate);

    @GET("adbnMobileBanner")
    Call<MobonResult> requestBanner(@Query("s") String s, @Query("cntsr") int cntsr, @Query("cntad") int cntad, @Query("bntype") String bntype);
}
