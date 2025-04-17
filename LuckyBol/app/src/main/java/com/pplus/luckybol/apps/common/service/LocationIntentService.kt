//package com.pplus.luckybol.apps.common.service
//
//import android.app.IntentService
//import android.content.Intent
//import android.content.Context
//import android.location.Location
//import android.support.v4.app.NotificationCompat.getExtras
//import android.os.Bundle
//import com.pplus.utils.part.logs.LogUtil
//import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
//import com.pplus.luckybol.core.network.ApiController
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import java.util.HashMap
//
//
///**
// * An [IntentService] subclass for handling asynchronous task requests in
// * a service on a separate handler thread.
// *
// *
// * TODO: Customize class - update intent actions, extra parameters and static
// * helper methods.
// */
//class LocationIntentService : IntentService("LocationIntentService") {
//
//    override fun onHandleIntent(intent: Intent?) {
//
//        if (intent != null) {
//            val bundle = intent.extras
//            if (bundle != null) {
//                val location = bundle.getParcelable<Location>(LOCATION_PARAM)
//                if (location != null) {
//                    if(LoginInfoManager.getInstance().isMember){
//                        val params = HashMap<String, String>()
//
//                        params["id"] = LoginInfoManager.getInstance().user.no.toString() + " : " + LoginInfoManager.getInstance().user.loginId
//                        params["lat"] = "" + location.latitude
//                        params["lon"] = "" + location.longitude
//
//                        ApiController.getLocationApi().requestInsertLocation(params).enqueue(object : Callback<Any> {
//
//                            override fun onResponse(call: Call<Any>, response: Response<Any>) {
//
//                            }
//
//                            override fun onFailure(call: Call<Any>, t: Throwable) {
//
//                            }
//                        })
//                    }
//
//                } else {
////                    LogUtil.e(LOG_TAG, "location null")
//                }
//
//            }
//        }
//    }
//
//    /**
//     * Handle action Foo in the provided background thread with the provided
//     * parameters.
//     */
//    private fun handleActionFoo(param1: String, param2: String) {
//        // TODO: Handle action Foo
//        throw UnsupportedOperationException("Not yet implemented")
//    }
//
//    /**
//     * Handle action Baz in the provided background thread with the provided
//     * parameters.
//     */
//    private fun handleActionBaz(param1: String, param2: String) {
//        // TODO: Handle action Baz
//        throw UnsupportedOperationException("Not yet implemented")
//    }
//
//    companion object {
//
//
//        // TODO: Rename parameters
//        private val LOCATION_PARAM = "com.google.android.location.LOCATION"
//        private val LOG_TAG = "LocationIntentService"
//
//    }
//}
