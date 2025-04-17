//package com.pplus.prnumberuser.apps.alert
//
//import android.os.Bundle
//import com.pplus.utils.part.pref.PreferenceUtil
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import kotlinx.android.synthetic.main.activity_alert_hot_deal_live.*
//
//class AlertHotDealLiveActivity : BaseActivity() {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_alert_hot_deal_live
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//
//        text_alert_hot_deal_live_close.setOnClickListener {
//            finish()
//        }
//
//        text_alert_hot_deal_live_do_not_again.setOnClickListener {
//            PreferenceUtil.getDefaultPreference(this).put(Const.GUIDE_HOT_DEAL_LIVE, false)
//            finish()
//        }
//    }
//}
