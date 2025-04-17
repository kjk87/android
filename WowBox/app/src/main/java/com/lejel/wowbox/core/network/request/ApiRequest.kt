package com.lejel.wowbox.core.network.request

import com.lejel.wowbox.core.network.apis.INewApi
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.pplus.networks.common.PplusCallback
import retrofit2.Call

/**
 * Created by j2n on 2016. 7. 26..
 *
 *
 * API 요청에 대한 빌더 생성 클래스
 *
 *
 * <pre>
 *
 * 1. 콜백 객체 세팅
 * 2. 파라미터 객체 세팅
 * 3. 리턴 객체 설정
 *
</pre> *
 */
class ApiRequest<T> protected constructor(builder: Builder<T>) {
    private val LOG_TAG = this.javaClass.simpleName

    /**
     * Api interface..
     */
    private val api: INewApi

    /**
     * 콜백 객체
     */
    private val callback: PplusCallback<NewResultResponse<T>>?

    /**
     * 요청에 대한 테그
     */
    private val tag: Any?

    /**
     * 실제 요청 콜 객체
     */
    private val requestCall: Call<NewResultResponse<T>>?

    /**
     * Api 요청을 합니다.
     */
    fun call(): Boolean {
        if (callback == null) {
            Throwable("callback not found.. exception")
        }
        if (requestCall == null) {
            Throwable("requestCall not found.. exception")
        }
        api.requestCall(tag, requestCall!!, callback!!)
        return true
    }

    /**
     * <pre>
     * request api 관련하여 빌더 패턴을 적용함
     * Tag는 필수값이 아님
    </pre> *
     */
    class Builder<T> : IApiDefaultRequest<T> {
        /**
         * 실제 요청 콜 객체
         */
        var requestCall: Call<NewResultResponse<T>>? = null

        /**
         * 콜백 객체
         */
        var callback: PplusCallback<NewResultResponse<T>>? = null

        /**
         * 요청에 대한 테그
         */
        var tag: Any? = null
        override fun setCallback(callback: PplusCallback<NewResultResponse<T>>): Builder<T> {
            this.callback = callback
            return this
        }

        override fun setTag(tag: Any): Builder<T> {
            this.tag = tag
            return this
        }

        override fun build(): ApiRequest<T> {
            return ApiRequest(this)
        }

        override fun setRequestCall(requestCall: Call<NewResultResponse<T>>): Builder<T> {
            this.requestCall = requestCall
            return this
        }
    }

    init {
        api = ApiRequestController.getInstance()
        callback = builder.callback
        tag = builder.tag
        requestCall = builder.requestCall
    }
}