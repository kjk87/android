package com.lejel.wowbox.core.network.request

import com.pplus.networks.common.PplusCallback
import com.lejel.wowbox.core.network.model.response.NewResultResponse

/**
 * Created by j2n on 2016. 9. 12..
 */
interface IApiRequest<T> {
    fun setCallback(callback: PplusCallback<NewResultResponse<T>>): IApiRequest<T>
    fun setTag(tag: Any): IApiRequest<T>
    fun build(): ApiRequest<T>
}