//package com.pplus.prnumberuser.apps.coupon.ui
//
//import android.content.Intent
//import android.os.Bundle
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import kotlinx.android.synthetic.main.activity_alert_cancel_complete.*
//import kotlinx.android.synthetic.main.activity_alert_coupon_download_complete.*
//
//class AlertCouponDownloadCompleteActivity : BaseActivity() {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_alert_coupon_download_complete
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//        image_alert_coupon_download_complete_confirm.setOnClickListener {
//            finish()
//        }
//
//        image_alert_coupon_download_complete_go.setOnClickListener {
//            val intent = Intent(this, CouponContainerActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//            finish()
//        }
//
//    }
//
//}
