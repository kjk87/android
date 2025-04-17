package com.pplus.prnumberbiz.apps.signin.ui

import android.os.Bundle
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity

class SnsLoginActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_login
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.login_container, SnsLoginFragment.newInstance(), SnsLoginFragment::class.java.simpleName)
        ft.commit()
    }

}
