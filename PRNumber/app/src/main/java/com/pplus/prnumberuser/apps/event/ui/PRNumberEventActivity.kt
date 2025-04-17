//package com.pplus.prnumberuser.apps.event.ui
//
//import android.os.Bundle
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//
//class PRNumberEventActivity : BaseActivity() {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_pr_number_event
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//        setEventFragment()
//    }
//
//    private fun setEventFragment() {
//        val ft = supportFragmentManager.beginTransaction()
//        ft.replace(R.id.pr_number_event_container, PRNumberEventFragment.newInstance(), PRNumberEventFragment::class.java.simpleName)
//        ft.commit()
//    }
//
//}
