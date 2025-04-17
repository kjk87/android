//package com.pplus.prnumberuser.apps.alert
//
//import android.content.Intent
//import android.os.Bundle
//import androidx.activity.result.contract.ActivityResultContracts
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.setting.ui.ProfileConfigActivity
//import com.pplus.utils.part.pref.PreferenceUtil
//import kotlinx.android.synthetic.main.activity_alert_profile_set.*
//
//class AlertProfileSetActivity : BaseActivity() {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutView(): Int {
//        return R.layout.activity_alert_profile_set
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//        image_profile_set.setOnClickListener {
//            val intent = Intent(this, ProfileConfigActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            profileSetLauncher.launch(intent)
//        }
//
//        text_alert_profile_set_close.setOnClickListener {
//            finish()
//        }
//
//        text_alert_profile_set_do_not_again.setOnClickListener {
//            PreferenceUtil.getDefaultPreference(this).put(Const.GUIDE_PROFILE_SET, false)
//            finish()
//        }
//    }
//
//    val profileSetLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        finish()
//    }
//}
