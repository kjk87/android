package com.pplus.prnumberbiz.apps.pages.ui

import android.os.Bundle
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.main.ui.MyPageFragment

class PageActivity2 : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_page
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.page_parent_container, MyPageFragment.newInstance(), MyPageFragment::class.java.simpleName)
        ft.commitNow()

    }
}
