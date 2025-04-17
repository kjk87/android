//package com.pplus.prnumberuser.service
//
//import android.app.IntentService
//import android.content.Intent
//import android.content.Context
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//
//// TODO: Rename actions, choose action names that describe tasks that this
//// IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
//private const val ACTION_ZZAL = "com.pplus.prnumberuser.service.action.ZZAL"
//private const val ACTION_AD_TYPE = "com.pplus.prnumberuser.service.action.AD_TYPE"
//
//
///**
// * An [IntentService] subclass for handling asynchronous task requests in
// * a service on a separate handler thread.
// * TODO: Customize class - update intent actions, extra parameters and static
// * helper methods.
// */
//class ZzalIntentService : IntentService("ZzalIntentService") {
//
//    override fun onHandleIntent(intent: Intent?) {
//        when (intent?.action) {
//            ACTION_ZZAL -> {
//                val participateID = intent.getStringExtra(Const.PARTICIPATEID)
//                handleActionZZal(participateID)
//            }
//            ACTION_AD_TYPE->{
//                val adType = intent.getStringExtra(Const.AD_TYPE)
//                handleActionAdType(adType)
//            }
//        }
//    }
//
//    private fun handleActionZZal(participateID: String) {
//        val params = HashMap<String, String>()
//        params["id"] = participateID
//        params["type"] = "zzal"
//        ApiBuilder.create().cpeReport(params).setCallback(object : PplusCallback<NewResultResponse<Any>>{
//            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
//
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
//
//            }
//        }).build().call()
//    }
//
//    private fun handleActionAdType(adType: String) {
//        val params = HashMap<String, String>()
//        params["id"] = PplusCommonUtil.getDeviceID()
//        params["type"] = adType
//        ApiBuilder.create().cpeReport(params).setCallback(object : PplusCallback<NewResultResponse<Any>>{
//            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
//
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
//
//            }
//        }).build().call()
//    }
//
//    companion object {
//        /**
//         * Starts this service to perform action Foo with the given parameters. If
//         * the service is already performing a task this action will be queued.
//         *
//         * @see IntentService
//         */
//        // TODO: Customize helper method
//        @JvmStatic
//        fun startActionZZal(context: Context, participateID: String) {
//            val intent = Intent(context, ZzalIntentService::class.java).apply {
//                action = ACTION_ZZAL
//                putExtra(Const.PARTICIPATEID, participateID)
//            }
//            context.startService(intent)
//        }
//
//        @JvmStatic
//        fun startActionAdType(context: Context, adType: String) {
//            val intent = Intent(context, ZzalIntentService::class.java).apply {
//                action = ACTION_AD_TYPE
//                putExtra(Const.AD_TYPE, adType)
//            }
//            context.startService(intent)
//        }
//    }
//}
