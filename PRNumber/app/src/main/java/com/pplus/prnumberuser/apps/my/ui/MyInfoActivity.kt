//package com.pplus.prnumberuser.apps.my.ui
//
//import android.content.Intent
//import android.os.Bundle
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.main.ui.MyInfoFragment
//import com.pplus.prnumberuser.apps.product.ui.PurchaseHistoryActivity
//import com.pplus.utils.part.utils.StringUtils
//
//class MyInfoActivity : BaseActivity() {
//
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutView(): Int {
//        return R.layout.activity_my_info
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//        val ft = supportFragmentManager.beginTransaction()
//        ft.replace(R.id.my_info_container, MyInfoFragment.newInstance(), MyInfoFragment::class.java.simpleName)
//        ft.commit()
//
//        val key = intent.getStringExtra(Const.KEY)
//        if (StringUtils.isNotEmpty(key) && key == Const.GOODS_HISTORY) {
//            val intent = Intent(this, PurchaseHistoryActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
//    }
//}
