package com.pplus.prnumberbiz.core.network.prnumber;

import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;

import network.common.PplusCallback;

/**
 * Created by j2n on 2016. 9. 12..
 */
public interface IPRNumberRequest<T> {

    IPRNumberRequest<T> setCallback(PplusCallback<NewResultResponse<T>> callback);

    IPRNumberRequest<T> setTag(Object tag);

    PRNumberRequest<T> build();
}
