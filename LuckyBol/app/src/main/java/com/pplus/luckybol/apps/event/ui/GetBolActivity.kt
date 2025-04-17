//package com.pplus.luckybol.apps.event.ui
//
//import android.os.Bundle
//import com.pplus.luckybol.R
//import com.pplus.luckybol.apps.common.ui.base.BaseActivity
//import com.pplus.luckybol.apps.main.ui.MainHomeFragment2
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
