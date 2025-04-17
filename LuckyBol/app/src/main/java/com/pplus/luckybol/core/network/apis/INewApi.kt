package com.pplus.luckybol.core.network.apis

import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.networks.common.PplusCallback
import retrofit2.Call

/**
 * Created by j2n on 2016. 7. 25..
 *
 *
 * <pre>
 * Application의 api interface를 정의합니다.
</pre> *
 */
interface INewApi {
    fun <T> requestCall(tag: Any?,
                        requestCall: Call<NewResultResponse<T>>,
                        responseCallback: PplusCallback<NewResultResponse<T>>)
}