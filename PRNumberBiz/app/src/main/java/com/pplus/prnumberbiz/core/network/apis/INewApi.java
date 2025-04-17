package com.pplus.prnumberbiz.core.network.apis;

import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;

import network.common.PplusCallback;
import retrofit2.Call;

/**
 * Created by j2n on 2016. 7. 25..
 * <p>
 * <pre>
 *     Application의 api interface를 정의합니다.
 * </pre>
 */
public interface INewApi{

    <T> void requestCall(Object tag, Call<NewResultResponse<T>> requestCall, PplusCallback<NewResultResponse<T>> responseCallback);
}
