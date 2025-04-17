package com.pplus.luckybol.core.network.prnumber

import com.pplus.networks.common.PplusCallback
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import retrofit2.Call

/**
 * Created by j2n on 2016. 9. 12..
 */
interface IPRNumberDefaultRequest<T> : IPRNumberRequest<T> {
    override fun setCallback(callback: PplusCallback<NewResultResponse<T>>): IPRNumberDefaultRequest<T>
    override fun setTag(tag: Any): IPRNumberDefaultRequest<T>
    fun setRequestCall(requestCall: Call<NewResultResponse<T>>): IPRNumberDefaultRequest<T>
    override fun build(): PRNumberRequest<T>
}