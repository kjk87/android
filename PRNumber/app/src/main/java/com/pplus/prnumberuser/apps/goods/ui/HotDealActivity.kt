//package com.pplus.prnumberuser.apps.goods.ui
//
//import android.os.Bundle
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.main.ui.MainHotDealFragment
//
//class HotDealActivity : BaseActivity() {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_hot_deal
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//        val ft = supportFragmentManager.beginTransaction()
//        ft.replace(R.id.hot_deal_container, MainHotDealFragment.newInstance(), MainHotDealFragment::class.java.simpleName)
//        ft.commit()
//    }
//}