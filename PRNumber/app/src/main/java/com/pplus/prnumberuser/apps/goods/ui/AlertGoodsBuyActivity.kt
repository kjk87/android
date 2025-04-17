//package com.pplus.prnumberuser.apps.goods.ui
//
//import android.app.Activity
//import android.content.Intent
//import android.os.Bundle
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import kotlinx.android.synthetic.main.activity_alert_goods_buy.*
//import kotlinx.android.synthetic.main.activity_alert_goods_like_download_complete.*
//
//class AlertGoodsBuyActivity : BaseActivity() {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutView(): Int {
//        return R.layout.activity_alert_goods_buy
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//        image_alert_goods_buy_close.setOnClickListener {
//            setResult(Activity.RESULT_CANCELED)
//            finish()
//        }
//
//        image_alert_goods_buy_confirm.setOnClickListener {
//            setResult(Activity.RESULT_OK)
//            finish()
//        }
//
//    }
//
//}
