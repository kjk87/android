//package com.pplus.prnumberuser.apps.main.ui
//
//import android.app.KeyguardManager
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import android.content.IntentFilter
//import android.os.Build
//import android.os.Bundle
//import android.view.WindowManager
//import androidx.appcompat.app.AppCompatActivity
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.logs.LogUtil
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Event
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.activity_lock_screen.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.text.SimpleDateFormat
//import java.util.*
//
//
//class LockScreenActivity : AppCompatActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
//            setShowWhenLocked(true)
//            setTurnScreenOn(true)
//            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
//            keyguardManager.requestDismissKeyguard(this, null)
//        } else {
//            this.window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
//                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
//                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
//        }
//
//        setContentView(R.layout.activity_lock_screen)
//
//        val intentFilter = IntentFilter()
//        intentFilter.addAction(Intent.ACTION_TIME_TICK)
//        intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED)
//        intentFilter.addAction(Intent.ACTION_TIME_CHANGED)
//
//        registerReceiver(mTimeChangedReceiver, intentFilter)
//
//        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
//            override fun reload() {
//
//                text_lock_screen_ticket?.text = PplusCommonUtil.fromHtml(getString(R.string.html_cash_unit2, FormatUtil.getMoneyType(LoginInfoManager.getInstance().user.eventTicketCount.toString())))
//                listCall()
//            }
//        })
//        setTime()
////        listCall()
//    }
//
//
//    val mTimeChangedReceiver: BroadcastReceiver = object : BroadcastReceiver() {
//        override fun onReceive(context: Context, intent: Intent) {
//            val action = intent.action
//            LogUtil.e("TIMERECEIVER", action)
//            setTime()
//        }
//    }
//
//    private fun setTime() {
//        val currentMillis = System.currentTimeMillis()
//        val output = SimpleDateFormat("hh:mm", Locale.getDefault())
//        val output2 = SimpleDateFormat(getString(R.string.format_screen_lock_day), Locale.getDefault())
//
//        text_lock_screen_time.text = output.format(Date(currentMillis))
//        text_lock_screen_day.text = output2.format(Date(currentMillis))
//    }
//
//    private fun listCall() {
//        val params = HashMap<String, String>()
//        params["no"] = "1"
//        params["pg"] = "1"
//        ApiBuilder.create().getEventListByGroup(params).setCallback(object : PplusCallback<NewResultResponse<Event>> {
//
//            override fun onResponse(call: Call<NewResultResponse<Event>>, response: NewResultResponse<Event>) {
//
//                val list = response.datas
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Event>>, t: Throwable, response: NewResultResponse<Event>) {
//
//            }
//        }).build().call()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        unregisterReceiver(mTimeChangedReceiver)
//    }
//}
