package com.pplus.prnumberbiz.core.network.prnumber;

import com.pplus.prnumberbiz.core.network.apis.INewApi;
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;

import network.common.PplusCallback;
import retrofit2.Call;

/**
 * Created by j2n on 2016. 7. 26..
 * <p>
 * API 요청에 대한 빌더 생성 클래스
 * <p>
 * <pre>
 *
 *     1. 콜백 객체 세팅
 *     2. 파라미터 객체 세팅
 *     3. 리턴 객체 설정
 *
 * </pre>
 */
public class PRNumberRequest<T>{

    private final String LOG_TAG = this.getClass().getSimpleName();

    /**
     * Api interface..
     */
    private final INewApi api;

    /**
     * 콜백 객체
     */
    private final PplusCallback<NewResultResponse<T>> callback;

    /**
     * 요청에 대한 테그
     */
    private final Object tag;

    /**
     * 실제 요청 콜 객체
     * */
    private Call<NewResultResponse<T>> requestCall;

    protected PRNumberRequest(Builder builder){

        api = PRNumberRequestController.getInstance();
        callback = builder.callback;
        tag = builder.tag;
        requestCall = builder.requestCall;
    }


    /**
     * Api 요청을 합니다.
     */
    public boolean call(){

        if(this.callback == null) {
            new Throwable("callback not found.. exception");
        }

        if(this.requestCall == null) {
            new Throwable("requestCall not found.. exception");
        }

        api.requestCall(this.tag, this.requestCall, this.callback);

        return true;
    }

    /**
     * <pre>
     * request api 관련하여 빌더 패턴을 적용함
     * Tag는 필수값이 아님
     * </pre>
     */
    public static class Builder<T> implements IPRNumberDefaultRequest<T>{

        /**
         * 실제 요청 콜 객체
         * */
        private Call<NewResultResponse<T>> requestCall;

        /**
         * 콜백 객체
         */
        private PplusCallback<NewResultResponse<T>> callback;
        /**
         * 요청에 대한 테그
         */
        private Object tag;

        public static IPRNumberDefaultRequest create(){

            return new Builder();
        }

        private Builder(){

        }

        public Builder setCallback(PplusCallback<NewResultResponse<T>> callback){

            this.callback = callback;
            return this;
        }

        public Builder setTag(Object tag){

            this.tag = tag;
            return this;
        }

        public PRNumberRequest build(){

            return new PRNumberRequest(this);
        }

        public Builder setRequestCall(Call<NewResultResponse<T>> requestCall){

            this.requestCall = requestCall;
            return this;
        }
    }

}
