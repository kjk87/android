//package com.pplus.prnumberuser.apps.post.ui
//
//import android.app.Activity
//import android.os.Bundle
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import kotlinx.android.synthetic.main.activity_real_impression_alert.*
//
//class RealImpressionAlertActivity : BaseActivity() {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutView(): Int {
//        return R.layout.activity_real_impression_alert
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//        image_real_impression_alert_close.setOnClickListener {
//            setResult(Activity.RESULT_CANCELED)
//            finish()
//        }
//
//        text_real_impression_alert_reg.setOnClickListener {
//            setResult(Activity.RESULT_OK)
//            finish()
//        }
//
//    }
//}
