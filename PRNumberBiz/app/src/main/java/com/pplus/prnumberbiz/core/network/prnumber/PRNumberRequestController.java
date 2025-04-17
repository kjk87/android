package com.pplus.prnumberbiz.core.network.prnumber;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.pple.pplus.utils.part.logs.LogUtil;
import com.pplus.prnumberbiz.PRNumberBizApplication;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.LauncherScreenActivity;
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder;
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData;
import com.pplus.prnumberbiz.apps.common.mgmt.DialogManager;
import com.pplus.prnumberbiz.core.network.ApiController;
import com.pplus.prnumberbiz.core.network.apis.INewApi;
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
import com.pplus.prnumberbiz.core.util.DebugConfig;
import com.pplus.prnumberbiz.core.util.PplusCommonUtil;
import com.pplus.prnumberbiz.core.util.ToastUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import network.common.NetworkException;
import network.common.PplusCallback;
import network.common.PplusforAlertCallback;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by j2n on 2016. 7. 25.. Api 테스트 기간중에는 실제로 작동하지않는다.
 * <p>
 * okhttp3.Response rawResp = new okhttp3.Response.Builder().code(200).message("OK").protocol(Protocol.HTTP_1_1).body(body).request(call.request()).build();
 * Response<ResultResponse> response = behaviorDelegate.returning(ApiCalls.response(Response.success(rawResp.body(),
 * rawResp))).requestVersionCheck(versionCheckParams.getParamsToMap()).execute();
 */
public class PRNumberRequestController implements INewApi{

    // network 관련 로그 출력
    //public static final boolean LOG_ENABLE = true;

    private static INewApi Instance;
    private final boolean DEBUG = false;

    private final String LOG_TAG = PRNumberRequestController.class.getSimpleName();
    private static final int SESSION_EXPIRATION_CODE = 560;

    private Set<String> requestCallSet;

    private PRNumberRequestController(){

        requestCallSet = new HashSet<>();
    }

    protected synchronized static INewApi getInstance(){

        if(Instance == null) {
            Instance = new PRNumberRequestController();
            ApiController.getPRNumberService().updateHeaders();
        }
        return Instance;
    }

    /**
     * 실제 Api를 요청하는 함수!
     */
    @Override
    public final void requestCall(Object tag, Call call, PplusCallback resultResponseCallback){

        if(!PplusCommonUtil.Companion.hasNetworkConnection()) {
            AlertBuilder.Builder builder = new AlertBuilder.Builder();
            builder.setTitle(PRNumberBizApplication.getContext().getString(R.string.word_notice_alert));
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE);
            builder.addContents(new AlertData.MessageData(PRNumberBizApplication.getContext().getString(R.string.msg_disconnected_network), AlertBuilder.MESSAGE_TYPE.TEXT, 1));
            builder.addContents(new AlertData.MessageData(PRNumberBizApplication.getContext().getString(R.string.msg_check_network_status), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
            builder.setLeftText(PRNumberBizApplication.getContext().getString(R.string.word_confirm));
            builder.builder().show(PRNumberBizApplication.getContext(), true);
            return;
        }

        //        if(call.request().url().)
        //        if(requestCallSet.contains(call.request().url().toString())) {
        //            LogUtil.e(LOG_TAG, "해당 콜은 요청중 입니다.");
        //            //            resultResponseCallback.onFailure(call, new Exception());
        //            return;
        //        }

        String url = call.request().url().toString();

        if(call.request().method().equals("GET")){

            if(call.request().url().toString().contains("?")){
                url += "&timestamp="+System.currentTimeMillis();
            }else{
                url += "?timestamp="+System.currentTimeMillis();
            }
        }
        LogUtil.e(LOG_TAG, "request url 요청합니다.= {} ", url);
        requestCallSet.add(url);

        RequestCallback requestCallback = new RequestCallback(tag, resultResponseCallback);
        call.enqueue(requestCallback);
    }

    /**
     * 코어 콜백 처리를 위해 정의합니다.
     */
    private class RequestCallback<T> implements Callback<NewResultResponse<T>>{

        private PplusCallback<NewResultResponse<T>> resultResponseCallback;

        private Object tag;

        public RequestCallback(Object tag, PplusCallback<NewResultResponse<T>> resultResponseCallback){

            this.resultResponseCallback = resultResponseCallback;
            this.tag = tag;
        }

        @Override
        public void onResponse(Call<NewResultResponse<T>> call, Response<NewResultResponse<T>> response){

            try {

                //                LogUtil.e(LOG_TAG, "Successful = {} ", response.isSuccessful());
                //
                //                LogUtil.e(LOG_TAG, "request Url = {} ", response.raw().request().url().toString());
                //
                //                LogUtil.e(LOG_TAG, "request header = {} ", response.raw().request().header("sessionKey"));

                final okhttp3.RequestBody copy = response.raw().request().body();

                if(copy != null) {
                    final Buffer buffer = new Buffer();
                    copy.writeTo(buffer);
                    LogUtil.e(LOG_TAG, "request body = {} ", buffer.readUtf8());
                }


            } catch (IOException e) {
                LogUtil.e(LOG_TAG, e.toString());
            }

            requestCallSet.remove(call.request().url().toString());

            // 요청 성공 실패?
            if(response.isSuccessful()) {

                NewResultResponse resultResponse = response.body();

                LogUtil.e(LOG_TAG, "resultCode = {} ", resultResponse.getResultCode());

                if(resultResponse.getResultCode() == SESSION_EXPIRATION_CODE) {
                    for(Activity activity : PRNumberBizApplication.getActivityList()) {
                        activity.finish();
                    }
                    Intent intent = new Intent(PRNumberBizApplication.getContext(), LauncherScreenActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    PRNumberBizApplication.getContext().startActivity(intent);

                } else {
                    if(this.resultResponseCallback != null) {

                        if(resultResponse.getResultCode() == 200) {
                            // 요청 성공시
                            this.resultResponseCallback.onResponse(call, resultResponse);
                        } else {

                            // 요청 실패시
                            if(this.resultResponseCallback instanceof PplusforAlertCallback) {
                                ((PplusforAlertCallback) this.resultResponseCallback).onErrorAlert(resultResponse);
                            } else {
                                //                                onError(resultResponse.getResultCode());
                                this.resultResponseCallback.onFailure(call, new NetworkException(), resultResponse);
                            }
                        }

                    } else {

                        onError(resultResponse.getResultCode());
                    }
                }
            } else {
                NewResultResponse resultResponse = new NewResultResponse();
                try {
                    String errorBody = response.errorBody().string();
                    LogUtil.e(LOG_TAG, "errorBody : {}", errorBody);
                    JSONObject jsonObject = new JSONObject(errorBody);
                    LogUtil.e(LOG_TAG, "error resultCode : {}", jsonObject.optInt("resultCode"));
                    if(jsonObject.optInt("resultCode") == SESSION_EXPIRATION_CODE) {
                        for(Activity activity : PRNumberBizApplication.getActivityList()) {
                            activity.finish();
                        }
                        Intent intent = new Intent(PRNumberBizApplication.getContext(), LauncherScreenActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        PRNumberBizApplication.getContext().startActivity(intent);
                    } else {
                        resultResponse.setResultCode(jsonObject.optInt("resultCode"));
                        if(this.resultResponseCallback != null) {

                            LogUtil.e(LOG_TAG, "response.raw().code() = {} ", response.raw().code());

                            if(this.resultResponseCallback instanceof PplusforAlertCallback) {

                                ((PplusforAlertCallback) this.resultResponseCallback).onErrorAlert(resultResponse);

                            } else {
                                new Handler(Looper.getMainLooper()).post(new Runnable(){

                                    @Override
                                    public void run(){

                                        ToastUtil.showAlert(PRNumberBizApplication.getContext(), PRNumberBizApplication.getContext().getString(R.string.server_error_default), true);

                                        DialogManager.getInstance().hideAll();
                                    }
                                });
                                this.resultResponseCallback.onFailure(call, new NetworkException(), resultResponse);
                            }
                        }else{
                            onError(resultResponse.getResultCode());
                        }
                    }


                } catch (Exception e) {
                    LogUtil.e(LOG_TAG, e.toString());
                    resultResponse.setResultCode(500);
                    if(this.resultResponseCallback != null) {

                        if(this.resultResponseCallback instanceof PplusforAlertCallback) {

                            ((PplusforAlertCallback) this.resultResponseCallback).onErrorAlert(resultResponse);

                        } else {
                            new Handler(Looper.getMainLooper()).post(new Runnable(){

                                @Override
                                public void run(){

                                    ToastUtil.showAlert(PRNumberBizApplication.getContext(), PRNumberBizApplication.getContext().getString(R.string.server_error_default), true);

                                    DialogManager.getInstance().hideAll();
                                }
                            });
                            this.resultResponseCallback.onFailure(call, new NetworkException(), resultResponse);
                        }
                    }else{
                        onError(resultResponse.getResultCode());
                    }
                }
            }

        }

        @Override
        public void onFailure(Call<NewResultResponse<T>> call, Throwable t){

            if(DebugConfig.isDebugMode()) {
                LogUtil.e(LOG_TAG, "Throwable = {}", t.getMessage());

                final okhttp3.RequestBody copy = call.request().body();

                if(copy != null) {

                    final Buffer buffer = new Buffer();

                    try {
                        copy.writeTo(buffer);
                        LogUtil.e(LOG_TAG, "request body = {} ", buffer.readUtf8());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            requestCallSet.remove(call.request().url().toString());
            if(resultResponseCallback != null){
                this.resultResponseCallback.onFailure(call, t, new NewResultResponse<T>());

                showDefaultErrorAlert();
            }



        }

        public void showDefaultErrorAlert(){

            new Handler(Looper.getMainLooper()).post(new Runnable(){

                @Override
                public void run(){

                    String value = PRNumberBizApplication.getContext().getString(R.string.server_error_default);
                    ToastUtil.showAlert(PRNumberBizApplication.getContext(), value, true);

                    DialogManager.getInstance().hideAll();
                }
            });
        }

        public void onError(int resultCode){

            LogUtil.e(LOG_TAG, "resultCode : {}", resultCode);

            new Handler(Looper.getMainLooper()).post(new Runnable(){

                @Override
                public void run(){

                    //                    ToastUtil.showAlert(PRNumberBizApplication.getContext(), resultResponse.getMessage(), true);

                    DialogManager.getInstance().hideAll();
                }
            });
        }

    }

    private void exit(){

        System.exit(0);
    }


}
