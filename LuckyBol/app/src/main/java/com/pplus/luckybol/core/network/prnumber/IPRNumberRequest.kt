package com.pplus.luckybol.core.network.prnumber

import com.pplus.networks.common.PplusCallback
import com.pplus.luckybol.core.network.model.response.NewResultResponse

/**
 * Created by j2n on 2016. 9. 12..
 */
interface IPRNumberRequest<T> {
    fun setCallback(callback: PplusCallback<NewResultResponse<T>>): IPRNumberRequest<T>
    fun setTag(tag: Any): IPRNumberRequest<T>
    fun build(): PRNumberRequest<T>
}