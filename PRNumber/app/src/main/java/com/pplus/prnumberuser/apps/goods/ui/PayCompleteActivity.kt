//package com.pplus.prnumberuser.apps.goods.ui
//
//import android.content.Intent
//import android.os.Bundle
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.main.ui.AppMainActivity
//import kotlinx.android.synthetic.main.activity_pay_complete.*
//
//class PayCompleteActivity : BaseActivity() {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutView(): Int {
//        return R.layout.activity_pay_complete
//    }
//
//    var mPageSeqNo: Long? = null
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//        mPageSeqNo = intent.getLongExtra(Const.PAGE_SEQ_NO, -1)
//
//
//
//        text_pay_complete_confirm.setOnClickListener {
//            goMain()
//
//        }
//
//    }
//
//
//    private fun goMain(){
//        val intent = Intent(this, AppMainActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//        startActivity(intent)
//    }
//
//    override fun onBackPressed() {
//
//    }
//}
