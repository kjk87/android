package com.root37.buflexz.core.network.request

import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.pplus.networks.common.NetworkException
import com.pplus.networks.common.PplusCallback
import com.pplus.networks.common.PplusforAlertCallback
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import com.root37.buflexz.BuflexzApplication
import com.root37.buflexz.Const
import com.root37.buflexz.R
import com.root37.buflexz.apps.LauncherScreenActivity
import com.root37.buflexz.apps.common.builder.AlertBuilder
import com.root37.buflexz.apps.common.builder.data.AlertData
import com.root37.buflexz.apps.common.mgmt.DialogManager
import com.root37.buflexz.apps.common.mgmt.LoginInfoManager
import com.root37.buflexz.core.network.ApiBuilder
import com.root37.buflexz.core.network.ApiController
import com.root37.buflexz.core.network.apis.INewApi
import com.root37.buflexz.core.network.model.response.NewResultResponse
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.core.util.PplusCommonUtil.Companion.hasNetworkConnection
import com.root37.buflexz.core.util.ToastUtil
import okio.Buffer
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ApiRequestController private constructor() : INewApi {
    private val DEBUG = false
    private val LOG_TAG = ApiRequestController::class.java.simpleName
    private val requestCallSet: MutableSet<String>

    /**
     * 실제 Api를 요청하는 함수!
     */
    override fun <T> requestCall(tag: Any?,
                                 call: Call<NewResultResponse<T>>,
                                 callback: PplusCallback<NewResultResponse<T>>) {
        if (!hasNetworkConnection()) {
            val builder = AlertBuilder.Builder()
            builder.setTitle(BuflexzApplication.context.getString(R.string.word_notice_alert))
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
            builder.addContents(AlertData.MessageData(BuflexzApplication.context.getString(R.string.msg_disconnected_network), AlertBuilder.MESSAGE_TYPE.TEXT, 1))
            builder.addContents(AlertData.MessageData(BuflexzApplication.context.getString(R.string.msg_check_network_status), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
            builder.setLeftText(BuflexzApplication.context.getString(R.string.word_confirm))
            builder.builder().show(BuflexzApplication.context, true)
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
                if (resultResponse.code == SESSION_EXPIRATION_CODE) {
                    refreshSessionKey(call, resultResponse)
                } else {
                    if (resultResponseCallback != null) {
                        if (resultResponse.code == 200) { // 요청 성공시
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
                    if (jsonObject.optInt("resultCode") == SESSION_EXPIRATION_CODE) {
                        resultResponse.code = SESSION_EXPIRATION_CODE
                        refreshSessionKey(call, resultResponse)
                    } else {
                        if (resultResponseCallback != null) {
                            LogUtil.e(LOG_TAG, "response.raw().code() = {} ", response.raw().code)
                            if (resultResponseCallback is PplusforAlertCallback) {
                                resultResponseCallback.onErrorAlert(resultResponse)
                            } else {
                                Handler(Looper.getMainLooper()).post {
                                    ToastUtil.showAlert(BuflexzApplication.context, BuflexzApplication.context.getString(R.string.server_error_default), true)
                                    try {
                                        DialogManager.instance.hideAll()
                                    } catch (e: Exception) {
                                    }
                                }
                                resultResponseCallback.onFailure(call, NetworkException(), resultResponse)
                            }
                        }
                    }
                } catch (e: Exception) {
                    LogUtil.e(LOG_TAG, e.toString())
                    resultResponse.code = 500
                    if (resultResponseCallback != null) {
                        LogUtil.e(LOG_TAG, "response.raw().code() = {} ", response.raw().code)
                        if (resultResponseCallback is PplusforAlertCallback) {
                            resultResponseCallback.onErrorAlert(resultResponse)
                        } else {
                            Handler(Looper.getMainLooper()).post {
                                ToastUtil.showAlert(BuflexzApplication.context, BuflexzApplication.context.getString(R.string.server_error_default), true)
                                try {
                                    DialogManager.instance.hideAll()
                                } catch (e: Exception) {
                                }
                            }
                            resultResponseCallback.onFailure(call, NetworkException(), resultResponse)
                        }
                    } else {
                        onError(resultResponse.code)
                    }
                }
            }
        }

        override fun onFailure(call: Call<NewResultResponse<T>>, t: Throwable) {
            if (Const.DEBUG_MODE) {
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
            resultResponse.code = 500
            if (resultResponseCallback != null) {
                resultResponseCallback.onFailure(call, t, resultResponse)
                showDefaultErrorAlert()
            }
        }

        fun showDefaultErrorAlert() {
            Handler(Looper.getMainLooper()).post {
                val value = BuflexzApplication.context.getString(R.string.server_error_default)
                ToastUtil.showAlert(BuflexzApplication.context, value, true)
                try {
                    DialogManager.instance.hideAll()
                } catch (e: Exception) {
                    LogUtil.e(LOG_TAG, e)
                }
            }
        }

        private fun refreshSessionKey(parentCall: Call<NewResultResponse<T>>, resultResponse: NewResultResponse<T>){
            if(LoginInfoManager.getInstance().isMember() && StringUtils.isNotEmpty(LoginInfoManager.getInstance().member!!.refreshToken)){
                val params = HashMap<String, String>()
                params["refreshToken"] = LoginInfoManager.getInstance().member!!.refreshToken!!
                params["token"] = LoginInfoManager.getInstance().member!!.token!!
                params["device"] = PplusCommonUtil.getDeviceID()
                ApiBuilder.create().refreshToken(params).setCallback(object : PplusCallback<NewResultResponse<String>>{
                    override fun onResponse(call: Call<NewResultResponse<String>>?,
                                            response: NewResultResponse<String>?) {
                        if(response?.result != null){
                            LoginInfoManager.getInstance().member!!.token = response.result
                            LoginInfoManager.getInstance().save()

                            if (resultResponseCallback != null) {
                                LogUtil.e(LOG_TAG, "resultResponseCallback")
                                resultResponseCallback.onFailure(parentCall, NetworkException(), resultResponse)
                            }else{
                                LogUtil.e(LOG_TAG, "resultResponseCallback is null")
                            }
                        }else{
                            for (activity in BuflexzApplication.getActivityList()) {
                                activity.finish()
                            }

                            PplusCommonUtil.logOutAndRestart()
                        }
                    }

                    override fun onFailure(call: Call<NewResultResponse<String>>?,
                                           t: Throwable?,
                                           response: NewResultResponse<String>?) {
                        for (activity in BuflexzApplication.getActivityList()) {
                            activity.finish()
                        }
                        val intent = Intent(BuflexzApplication.context, LauncherScreenActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        BuflexzApplication.context.startActivity(intent)
                    }
                }).build().call()
            }else{
                for (activity in BuflexzApplication.getActivityList()) {
                    activity.finish()
                }
                val intent = Intent(BuflexzApplication.context, LauncherScreenActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                BuflexzApplication.context.startActivity(intent)
            }
        }

        fun onError(resultCode: Int) {
            LogUtil.e(LOG_TAG, "resultCode : {}", resultCode)
            Handler(Looper.getMainLooper()).post { //                    ToastUtil.showAlert(LuckyBolApplication.getContext(), resultResponse.getMessage(), true);
                try {
                    DialogManager.instance.hideAll()
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

        var requestController: ApiRequestController? = null

        @Synchronized
        fun getInstance(): INewApi {
            if (requestController == null) {
                requestController = ApiRequestController()
                ApiController.apiService.updateHeaders()
            }
            return requestController!!
        }

        private const val SESSION_EXPIRATION_CODE = 401
    }

    init {
        requestCallSet = HashSet()
    }
}