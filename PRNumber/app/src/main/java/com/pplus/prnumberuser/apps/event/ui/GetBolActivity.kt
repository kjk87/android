//package com.pplus.prnumberuser.apps.event.ui
//
//import android.os.Bundle
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.main.ui.MainHomeFragment2
//
//class GetBolActivity : BaseActivity() {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_play
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//        val ft = supportFragmentManager.beginTransaction()
//        ft.replace(R.id.play_container, MainHomeFragment2.newInstance(), MainHomeFragment2::class.java.simpleName)
//        ft.commit()
//
//    }
//
//}
