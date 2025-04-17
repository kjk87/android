package com.pplus.luckybol.core.network.prnumber

import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.pplus.luckybol.LuckyBolApplication
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.LauncherScreenActivity
import com.pplus.luckybol.apps.common.builder.AlertBuilder
import com.pplus.luckybol.apps.common.builder.data.AlertData
import com.pplus.luckybol.apps.common.mgmt.DialogManager
import com.pplus.luckybol.core.network.ApiController.pRNumberService
import com.pplus.luckybol.core.network.apis.INewApi
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.util.DebugConfig
import com.pplus.luckybol.core.util.PplusCommonUtil.Companion.hasNetworkConnection
import com.pplus.luckybol.core.util.ToastUtil
import com.pplus.networks.common.NetworkException
import com.pplus.networks.common.PplusCallback
import com.pplus.networks.common.PplusforAlertCallback
import com.pplus.utils.part.logs.LogUtil
import okio.Buffer
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*

/**
 * Created by j2n on 2016. 7. 25.. Api 테스트 기간중에는 실제로 작동하지않는다.
 *
 *
 * okhttp3.Response rawResp = new okhttp3.Response.Builder().code(200).message("OK").protocol(Protocol.HTTP_1_1).body(body).request(call.request()).build();
 * Response<ResultResponse> response = behaviorDelegate.returning(ApiCalls.response(Response.success(rawResp.body(),
 * rawResp))).requestVersionCheck(versionCheckParams.getParamsToMap()).execute();
</ResultResponse> */
class PRNumberRequestController private constructor() : INewApi {
    private val DEBUG = false
    private val LOG_TAG = PRNumberRequestController::class.java.simpleName
    private val requestCallSet: MutableSet<String>

    /**
     * 실제 Api를 요청하는 함수!
     */
    override fun <T> requestCall(tag: Any?,
                                 call: Call<NewResultResponse<T>>,
                                 callback: PplusCallback<NewResultResponse<T>>) {
        if (!hasNetworkConnection()) {
            val builder = AlertBuilder.Builder()
            builder.setTitle(LuckyBolApplication.getContext().getString(R.string.word_notice_alert))
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
            builder.addContents(AlertData.MessageData(LuckyBolApplication.getContext().getString(R.string.msg_disconnected_network), AlertBuilder.MESSAGE_TYPE.TEXT, 1))
            builder.addContents(AlertData.MessageData(LuckyBolApplication.getContext().getString(R.string.msg_check_network_status), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
            builder.setLeftText(LuckyBolApplication.getContext().getString(R.string.word_confirm))
            builder.builder().show(LuckyBolApplication.getContext(), true)
            return
        }

        //        if(call.request().url().)
        //        if(requestCallSet.contains(call.request().url().toString())) {
        //            LogUtil.e(LOG_TAG, "해당 콜은 요청중 입니다.");
        //            //            resultResponseCallback.onFailure(call, new Exception());
        //            return;
        //        }
        var url = call.request().url.toString()
        if (call.request().method == "GET") {
            url += if (call.request().url.toString().contains("?")) {
                "&timestamp=" + System.currentTimeMillis()
            } else {
                "?timestamp=" + System.currentTimeMillis()
            }
        }
        LogUtil.e(LOG_TAG, "request url 요청합니다.= {} ", url)
        requestCallSet.add(url)
        val requestCallback = RequestCallback(tag, callback)
        call.enqueue(requestCallback)
    }


    /**
     * 코어 콜백 처리를 위해 정의합니다.
     */
    private inner class RequestCallback<T>(private val tag: Any?,
                                           private val resultResponseCallback: PplusCallback<NewResultResponse<T>>?) : Callback<NewResultResponse<T>> {
        override fun onResponse(call: Call<NewResultResponse<T>>,
                                response: Response<NewResultResponse<T>>) {
            var body = ""
            try {

                //                LogUtil.e(LOG_TAG, "Successful = {} ", response.isSuccessful());
                //
                //                LogUtil.e(LOG_TAG, "request Url = {} ", response.raw().request().url().toString());
                //
                //                LogUtil.e(LOG_TAG, "request header = {} ", response.raw().request().header("sessionKey"));
                val copy = response.raw().request.body
                if (copy != null) {
                    val buffer = Buffer()
                    copy.writeTo(buffer)
                    body = buffer.readUtf8()
                    LogUtil.e(LOG_TAG, "request body = {} ", body)
                }
            } catch (e: IOException) {
                LogUtil.e(LOG_TAG, e.toString())
            }
            requestCallSet.remove(call.request().url.toString())

            // 요청 성공 실패?
            if (response.isSuccessful) {
                val resultResponse = response.body()!!

                //                LogUtil.e(LOG_TAG, "resultResponse = {} ", resultResponse.toString());
                if (!body.contains("sessionCheck=false") && resultResponse.resultCode == SESSION_EXPIRATION_CODE) {
                    for (activity in LuckyBolApplication.getActivityList()) {
                        activity.finish()
                    }
                    val intent = Intent(LuckyBolApplication.getContext(), LauncherScreenActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    LuckyBolApplication.getContext().startActivity(intent)
                } else {
                    if (resultResponseCallback != null) {
                        if (resultResponse.resultCode == 200) { // 요청 성공시
                            resultResponseCallback.onResponse(call, resultResponse)
                        } else { // 요청 실패시
                            if (resultResponseCallback is PplusforAlertCallback) {
                                resultResponseCallback.onErrorAlert(resultResponse)
                            } else { //                                onError(resultResponse.getResultCode());
                                resultResponseCallback.onFailure(call, NetworkException(), resultResponse)
                            }
                        }
                    } else {

                        //                        onError(resultResponse.getResultCode());
                    }
                }
            } else {
                val resultResponse = NewResultResponse<T>()
                try {
                    val errorBody = response.errorBody()!!.string()
                    LogUtil.e(LOG_TAG, "errorBody : {}", errorBody)
                    val jsonObject = JSONObject(errorBody)
                    LogUtil.e(LOG_TAG, "error resultCode : {}", jsonObject.optInt("resultCode"))
                    if (!body.contains("sessionCheck=false") && jsonObject.optInt("resultCode") == SESSION_EXPIRATION_CODE) {
                        for (activity in LuckyBolApplication.getActivityList()) {
                            activity.finish()
                        }
                        val intent = Intent(LuckyBolApplication.getContext(), LauncherScreenActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        LuckyBolApplication.getContext().startActivity(intent)
                    } else {
                        if (resultResponseCallback != null) {
                            LogUtil.e(LOG_TAG, "response.raw().code() = {} ", response.raw().code)
                            if (resultResponseCallback is PplusforAlertCallback) {
                                resultResponseCallback.onErrorAlert(resultResponse)
                            } else {
                                Handler(Looper.getMainLooper()).post {
                                    ToastUtil.showAlert(LuckyBolApplication.getContext(), LuckyBolApplication.getContext().getString(R.string.server_error_default), true)
                                    try {
                                        DialogManager.getInstance().hideAll()
                                    } catch (e: Exception) {
                                    }
                                }
                                resultResponseCallback.onFailure(call, NetworkException(), resultResponse)
                            }
                        }
                    }
                } catch (e: Exception) {
                    LogUtil.e(LOG_TAG, e.toString())
                    resultResponse.resultCode = 500
                    if (resultResponseCallback != null) {
                        LogUtil.e(LOG_TAG, "response.raw().code() = {} ", response.raw().code)
                        if (resultResponseCallback is PplusforAlertCallback) {
                            resultResponseCallback.onErrorAlert(resultResponse)
                        } else {
                            Handler(Looper.getMainLooper()).post {
                                ToastUtil.showAlert(LuckyBolApplication.getContext(), LuckyBolApplication.getContext().getString(R.string.server_error_default), true)
                                try {
                                    DialogManager.getInstance().hideAll()
                                } catch (e: Exception) {
                                }
                            }
                            resultResponseCallback.onFailure(call, NetworkException(), resultResponse)
                        }
                    } else {
                        onError(resultResponse.resultCode)
                    }
                }
            }
        }

        override fun onFailure(call: Call<NewResultResponse<T>>, t: Throwable) {
            if (DebugConfig.isDebugMode()) {
                LogUtil.e(LOG_TAG, "Throwable = {}", t.message)
                val copy = call.request().body
                if (copy != null) {
                    val buffer = Buffer()
                    try {
                        copy.writeTo(buffer)
                        LogUtil.e(LOG_TAG, "request body = {} ", buffer.readUtf8())
                    } catch (e: IOException) {
                        LogUtil.e(LOG_TAG, e)
                    }
                }
            }
            requestCallSet.remove(call.request().url.toString())
            val resultResponse = NewResultResponse<T>()
            resultResponse.resultCode = 500
            if (resultResponseCallback != null) {
                resultResponseCallback.onFailure(call, t, resultResponse)
                showDefaultErrorAlert()
            }
        }

        fun showDefaultErrorAlert() {
            Handler(Looper.getMainLooper()).post {
                val value = LuckyBolApplication.getContext().getString(R.string.server_error_default)
                ToastUtil.showAlert(LuckyBolApplication.getContext(), value, true)
                try {
                    DialogManager.getInstance().hideAll()
                } catch (e: Exception) {
                    LogUtil.e(LOG_TAG, e)
                }
            }
        }

        fun onError(resultCode: Int) {
            LogUtil.e(LOG_TAG, "resultCode : {}", resultCode)
            Handler(Looper.getMainLooper()).post { //                    ToastUtil.showAlert(LuckyBolApplication.getContext(), resultResponse.getMessage(), true);
                try {
                    DialogManager.getInstance().hideAll()
                } catch (e: Exception) {
                }
            }
        }
    }

    private fun exit() {
        System.exit(0)
    }

    companion object { // network 관련 로그 출력
        //public static final boolean LOG_ENABLE = true;

        var requestController: PRNumberRequestController? = null

        @Synchronized
        fun getInstance(): INewApi {
            if (requestController == null) {
                requestController = PRNumberRequestController()
                pRNumberService.updateHeaders()
            }
            return requestController!!
        }

        private const val SESSION_EXPIRATION_CODE = 560
    }

    init {
        requestCallSet = HashSet()
    }
}