//package com.pplus.prnumberuser.receiver
//
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import com.pplus.utils.part.logs.LogUtil
//import com.pplus.utils.part.pref.PreferenceUtil
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.main.ui.LockScreenActivity
//import com.pplus.prnumberuser.service.ScreenService
//
//class RestartReceiver : BroadcastReceiver() {
//
//    override fun onReceive(context: Context, intent: Intent) {
//        /**
//         * 특정 시간 잠금화면
//         */
//
//        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
//            val i = Intent(context, ScreenService::class.java)
//            context.startService(i);
//
//        }else if(intent.action == "com.pplus.prnumberuser.ScreenService"){
//            val i = Intent(context, ScreenService::class.java)
//            context.startService(i);
//        }
//
//    }
//}
