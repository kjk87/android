package com.pplus.prnumberuser.apps.signin.ui

import android.os.Bundle
import android.view.View
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.databinding.ActivityLoginBinding
import com.pplus.utils.part.pref.PreferenceUtil

class LoginActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityLoginBinding

    override fun getLayoutView(): View {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val key = intent.getStringExtra(Const.KEY)

        PreferenceUtil.getDefaultPreference(this).put(Const.TUTORIAL, false)

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.login_container, LoginFragment.newInstance(), LoginFragment::class.java.simpleName)
        ft.commit()
    }

}
