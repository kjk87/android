//package com.pplus.prnumberuser.apps.my.ui
//
//import android.app.Activity
//import android.os.Bundle
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import kotlinx.android.synthetic.main.activity_set_screen_lock.*
//
//class AlertSetScreenLockActivity : BaseActivity() {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutView(): Int {
//        return R.layout.activity_set_screen_lock
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//        image_set_screen_lock_close.setOnClickListener {
//            setResult(Activity.RESULT_CANCELED)
//            finish()
//        }
//
//        image_set_screen_lock_confirm.setOnClickListener {
//            setResult(Activity.RESULT_OK)
//            finish()
//        }
//
//    }
//
//}
