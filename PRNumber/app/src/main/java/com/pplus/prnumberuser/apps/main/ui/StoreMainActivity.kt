//package com.pplus.prnumberuser.apps.main.ui
//
//import android.content.Intent
//import android.support.v7.app.AppCompatActivity
//import android.os.Bundle
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import kotlinx.android.synthetic.main.activity_store_main.*
//
//class StoreMainActivity : BaseActivity() {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_store_main
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//
//        text_store_main_home.setOnClickListener {
//            val intent = Intent(this, AppMainActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//            overridePendingTransition(R.anim.fix, R.anim.fix);
//        }
//
//        layout_store_main_order_tab.setOnClickListener {
//            setHotDealFragment()
//        }
//
//        layout_store_main_market_tab.setOnClickListener {
//            setMarketFragment()
//        }
//
//        onNewIntent(intent)
//    }
//
//    override fun onNewIntent(intent: Intent?) {
//        super.onNewIntent(intent)
//
//        val tab = intent?.getIntExtra(Const.TAB, 0)
//
//        when(tab){
//            0->{
//                setHotDealFragment()
//            }
//            1->{
//                setMarketFragment()
//            }
//        }
//    }
//
//    private fun setHotDealFragment() {
//        layout_store_main_order_tab.isSelected = true
//        layout_store_main_market_tab.isSelected = false
//        val ft = supportFragmentManager.beginTransaction()
//        ft.replace(R.id.store_main_container, MainHotDealFragment.newInstance(), MainHotDealFragment::class.java.simpleName)
//        ft.commitNow()
//    }
//
//    private fun setMarketFragment() {
//        layout_store_main_order_tab.isSelected = false
//        layout_store_main_market_tab.isSelected = true
//        val ft = supportFragmentManager.beginTransaction()
//        ft.replace(R.id.store_main_container, MainHotDealFragment.newInstance(), MainHotDealFragment::class.java.simpleName)
//        ft.commitNow()
//    }
//
//}
