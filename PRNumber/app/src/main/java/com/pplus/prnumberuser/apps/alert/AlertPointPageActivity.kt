//package com.pplus.prnumberuser.apps.alert
//
//import android.content.Intent
//import android.os.Bundle
//import android.view.View
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.page.ui.PointPageGuideActivity
//import com.pplus.prnumberuser.databinding.ActivityAlertPointPageGuideBinding
//import com.pplus.utils.part.pref.PreferenceUtil
//
//class AlertPointPageActivity : BaseActivity() {
//    override fun getPID(): String {
//        return ""
//    }
//
//    private lateinit var binding: ActivityAlertPointPageGuideBinding
//
//    override fun getLayoutView(): View {
//        binding = ActivityAlertPointPageGuideBinding.inflate(layoutInflater)
//        return binding.root
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//        binding.imageAlertPointPageGuide.setOnClickListener {
//            val intent = Intent(this, PointPageGuideActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
//        binding.textAlertPointPageGuideClose.setOnClickListener {
//            finish()
//        }
//
//        binding.textAlertPointPageGuideDoNotAgain.setOnClickListener {
//            PreferenceUtil.getDefaultPreference(this).put(Const.GUIDE_POINT_PAGE, false)
//            finish()
//        }
//    }
//}
