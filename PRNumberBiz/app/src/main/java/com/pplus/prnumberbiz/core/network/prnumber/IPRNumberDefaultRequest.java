package com.pplus.prnumberbiz.core.network.prnumber;

import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;

import network.common.PplusCallback;
import retrofit2.Call;

/**
 * Created by j2n on 2016. 9. 12..
 */
public interface IPRNumberDefaultRequest<T> extends IPRNumberRequest<T>{

    IPRNumberDefaultRequest<T> setCallback(PplusCallback<NewResultResponse<T>> callback);

    IPRNumberDefaultRequest<T> setTag(Object tag);

    IPRNumberDefaultRequest setRequestCall(Call<NewResultResponse<T>> requestCall);

    PRNumberRequest<T> build();
}
