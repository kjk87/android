package com.lejel.wowbox.core.network.request

import com.pplus.networks.common.PplusCallback
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import retrofit2.Call

/**
 * Created by j2n on 2016. 9. 12..
 */
interface IApiDefaultRequest<T> : IApiRequest<T> {
    override fun setCallback(callback: PplusCallback<NewResultResponse<T>>): IApiDefaultRequest<T>
    override fun setTag(tag: Any): IApiDefaultRequest<T>
    fun setRequestCall(requestCall: Call<NewResultResponse<T>>): IApiDefaultRequest<T>
    override fun build(): ApiRequest<T>
}