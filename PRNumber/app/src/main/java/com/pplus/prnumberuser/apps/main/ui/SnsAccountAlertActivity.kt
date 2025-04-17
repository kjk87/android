//package com.pplus.prnumberuser.apps.main.ui
//
//import android.app.Activity
//import android.content.Intent
//import android.os.Bundle
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.core.network.model.dto.Event
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.activity_sns_account_alert.*
//
//class SnsAccountAlertActivity : BaseActivity() {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutView(): Int {
//        return R.layout.activity_sns_account_alert
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
////        image_sns_account_alert_close.setOnClickListener {
////            finish()
////        }
//
//
//        text_sns_account_alert_confirm.setOnClickListener {
//            val intent = Intent(this, ChangeSnsAccountActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//            finish()
//        }
//    }
//
//    override fun onBackPressed() {
//
//    }
//}
