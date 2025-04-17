//package com.pplus.prnumberuser.service
//
//import android.app.Service
//import android.content.Intent
//import android.content.IntentFilter
//import android.os.IBinder
//import com.pplus.utils.part.logs.LogUtil
//import com.pplus.prnumberuser.receiver.ScreenReceiver
//
//class ScreenService : Service() {
//    lateinit var receiver: ScreenReceiver
//
//    override fun onBind(intent: Intent): IBinder {
//        TODO("Return the communication channel to the service.")
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//        LogUtil.e("ScreenService", "onCreate")
//        receiver = ScreenReceiver()
//        val filter = IntentFilter(Intent.ACTION_SCREEN_OFF)
//        filter.addAction(Intent.ACTION_SCREEN_ON)
//        registerReceiver(receiver, filter)
//
//    }
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        if (intent != null) {
//
//            if (intent.action == null) {
//
//                if (receiver == null) {
//
//                    receiver = ScreenReceiver()
//
//                    val filter = IntentFilter(Intent.ACTION_SCREEN_OFF)
//                    filter.addAction(Intent.ACTION_SCREEN_ON)
//                    registerReceiver(receiver, filter)
//
//                }
//
//            }
//
//        }
//
//        return START_STICKY
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        unregisterReceiver(receiver)
//        val sendIntent = Intent("com.pplus.prnumberuser.ScreenService")
//        sendBroadcast(sendIntent)
//    }
//}
