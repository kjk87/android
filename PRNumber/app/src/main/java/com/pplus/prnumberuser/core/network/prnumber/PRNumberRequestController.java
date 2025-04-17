package com.pplus.prnumberuser.core.network.prnumber;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.pplus.utils.part.logs.LogUtil;
import com.pplus.prnumberuser.PRNumberApplication;
import com.pplus.prnumberuser.R;
import com.pplus.prnumberuser.apps.LauncherScreenActivity;
import com.pplus.prnumberuser.apps.common.builder.AlertBuilder;
import com.pplus.prnumberuser.apps.common.builder.data.AlertData;
import com.pplus.prnumberuser.apps.common.mgmt.DialogManager;
import com.pplus.prnumberuser.core.network.ApiController;
import com.pplus.prnumberuser.core.network.apis.INewApi;
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse;
import com.pplus.prnumberuser.core.util.DebugConfig;
import com.pplus.prnumberuser.core.util.PplusCommonUtil;
import com.pplus.prnumberuser.core.util.ToastUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.pplus.networks.common.NetworkException;
import com.pplus.networks.common.PplusCallback;
import com.pplus.networks.common.PplusforAlertCallback;
import okhttp3.HttpUrl;
import okhttp3.Request;
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
            builder.setTitle(PRNumberApplication.getContext().getString(R.string.word_notice_alert));
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE);
            builder.addContents(new AlertData.MessageData(PRNumberApplication.getContext().getString(R.string.msg_disconnected_network), AlertBuilder.MESSAGE_TYPE.TEXT, 1));
            builder.addContents(new AlertData.MessageData(PRNumberApplication.getContext().getString(R.string.msg_check_network_status), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
            builder.setLeftText(PRNumberApplication.getContext().getString(R.string.word_confirm));
            builder.builder().show(PRNumberApplication.getContext(), true);
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
            String body = "";
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
                    body = buffer.readUtf8();
                    LogUtil.e(LOG_TAG, "request body = {} ", body);
                }


            } catch (IOException e) {
                LogUtil.e(LOG_TAG, e.toString());
            }

            requestCallSet.remove(call.request().url().toString());

            // 요청 성공 실패?
            if(response.isSuccessful()) {

                NewResultResponse resultResponse = response.body();

//                LogUtil.e(LOG_TAG, "resultResponse = {} ", resultResponse.toString());


                if(!body.contains("sessionCheck=false") && resultResponse.getResultCode() == SESSION_EXPIRATION_CODE) {
                    for(Activity activity : PRNumberApplication.getActivityList()) {
                        activity.finish();
                    }
                    Intent intent = new Intent(PRNumberApplication.getContext(), LauncherScreenActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    PRNumberApplication.getContext().startActivity(intent);
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

//                        onError(resultResponse.getResultCode());
                    }
                }

            } else {
                NewResultResponse resultResponse = new NewResultResponse();
                try {

                    String errorBody = response.errorBody().string();
                    LogUtil.e(LOG_TAG, "errorBody : {}", errorBody);
                    JSONObject jsonObject = new JSONObject(errorBody);
                    LogUtil.e(LOG_TAG, "error resultCode : {}", jsonObject.optInt("resultCode"));
                    if(!body.contains("sessionCheck=false") && jsonObject.optInt("resultCode") == SESSION_EXPIRATION_CODE) {
                        for(Activity activity : PRNumberApplication.getActivityList()) {
                            activity.finish();
                        }
                        Intent intent = new Intent(PRNumberApplication.getContext(), LauncherScreenActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        PRNumberApplication.getContext().startActivity(intent);
                    } else {
                        if(this.resultResponseCallback != null) {

                            LogUtil.e(LOG_TAG, "response.raw().code() = {} ", response.raw().code());

                            if(this.resultResponseCallback instanceof PplusforAlertCallback) {

                                ((PplusforAlertCallback) this.resultResponseCallback).onErrorAlert(resultResponse);

                            } else {
                                new Handler(Looper.getMainLooper()).post(new Runnable(){

                                    @Override
                                    public void run(){

                                        ToastUtil.showAlert(PRNumberApplication.getContext(), PRNumberApplication.getContext().getString(R.string.server_error_default), true);
                                        try{
                                            DialogManager.getInstance().hideAll();
                                        }catch (Exception e){

                                        }

                                    }
                                });
                                this.resultResponseCallback.onFailure(call, new NetworkException(), resultResponse);
                            }
                        }
                    }

                } catch (Exception e) {
                    LogUtil.e(LOG_TAG, e.toString());
                    resultResponse.setResultCode(500);
                    if(this.resultResponseCallback != null) {

                        LogUtil.e(LOG_TAG, "response.raw().code() = {} ", response.raw().code());

                        if(this.resultResponseCallback instanceof PplusforAlertCallback) {

                            ((PplusforAlertCallback) this.resultResponseCallback).onErrorAlert(resultResponse);

                        } else {
                            new Handler(Looper.getMainLooper()).post(new Runnable(){

                                @Override
                                public void run(){

                                    ToastUtil.showAlert(PRNumberApplication.getContext(), PRNumberApplication.getContext().getString(R.string.server_error_default), true);

                                    try{
                                        DialogManager.getInstance().hideAll();
                                    }catch (Exception e){

                                    }
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
            NewResultResponse resultResponse = new NewResultResponse();
            resultResponse.setResultCode(500);

            if(resultResponseCallback != null){
                this.resultResponseCallback.onFailure(call, t, resultResponse);

                showDefaultErrorAlert();
            }

        }

        public void showDefaultErrorAlert(){

            new Handler(Looper.getMainLooper()).post(new Runnable(){

                @Override
                public void run(){

                    String value = PRNumberApplication.getContext().getString(R.string.server_error_default);
                    ToastUtil.showAlert(PRNumberApplication.getContext(), value, true);

                    try{
                        DialogManager.getInstance().hideAll();
                    }catch (Exception e){

                    }
                }
            });
        }

        public void onError(int resultCode){

            LogUtil.e(LOG_TAG, "resultCode : {}", resultCode);

            new Handler(Looper.getMainLooper()).post(new Runnable(){

                @Override
                public void run(){

                    //                    ToastUtil.showAlert(PRNumberApplication.getContext(), resultResponse.getMessage(), true);

                    try{
                        DialogManager.getInstance().hideAll();
                    }catch (Exception e){

                    }
                }
            });
        }
    }

    private void exit(){

        System.exit(0);
    }


}
