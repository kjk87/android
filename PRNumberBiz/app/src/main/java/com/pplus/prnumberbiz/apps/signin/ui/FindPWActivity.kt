package com.pplus.prnumberbiz.apps.signin.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.signup.ui.VerificationMeActivity
import com.pplus.prnumberbiz.core.network.model.dto.Verification

class FindPWActivity : BaseActivity(), ImplToolbar {

    private var loginId: String? = null

    override fun getPID(): String? {

        return null
    }

    override fun getLayoutRes(): Int {

        return R.layout.activity_find_pw
    }

    fun setLoginId(loginId: String) {

        this.loginId = loginId
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        findStep1()

    }

    fun findStep1() {

        val fragment = FindPWStep1Fragment.newInstance()

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.find_pw_container, fragment, fragment.javaClass.simpleName)
        ft.commit()
    }

    fun verification(id: String, mobile: String) {

        setLoginId(id)
        val intent = Intent(this, VerificationMeActivity::class.java)
        intent.putExtra(Const.MOBILE_NUMBER, mobile)
        intent.putExtra(Const.KEY, Const.VERIFICATION)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivityForResult(intent, Const.REQ_FIND_PW)

    }

    fun changePassword(verification: Verification) {

        val intent = Intent(this, ChangePWActivity::class.java)
        intent.putExtra(Const.VERIFICATION, verification)
        intent.putExtra(Const.LOGIN_ID, loginId)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivityForResult(intent, Const.REQ_CHANGE_PW)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Const.REQ_FIND_PW -> if (data != null) {
                    val verification = data.getParcelableExtra<Verification>(Const.VERIFICATION)
                    changePassword(verification)
                }
                Const.REQ_CHANGE_PW -> {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        }
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar("", ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
            }
        }
    }
}
