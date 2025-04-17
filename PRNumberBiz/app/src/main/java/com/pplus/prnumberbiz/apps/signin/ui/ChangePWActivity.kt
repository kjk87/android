package com.pplus.prnumberbiz.apps.signin.ui

import android.app.Activity
import android.os.Bundle
import android.text.InputType
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Verification
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import kotlinx.android.synthetic.main.activity_change_pw.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.*

class ChangePWActivity : BaseActivity(), ImplToolbar {

    override fun getPID(): String {

        return ""
    }

    override fun getLayoutRes(): Int {

        return R.layout.activity_change_pw
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        edit_change_pw_password.setSingleLine()
        edit_change_pw_password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        text_change_pw_confirm.setOnClickListener {
            val password = edit_change_pw_password.text.toString().trim()
            if (StringUtils.isEmpty(password)) {
                showAlert(R.string.msg_input_password)
                return@setOnClickListener
            }

            if (password.length < 4) {
                edit_change_pw_password.requestFocus()
                showAlert(getString(R.string.to_password) + " " + getString(R.string.format_msg_input_over, 4))
                return@setOnClickListener
            }

            val loginId = intent.getStringExtra(Const.LOGIN_ID)
            val verification = intent.getParcelableExtra<Verification>(Const.VERIFICATION)
            changePw(password, loginId, verification)
        }
    }

    private fun changePw(password: String, loginId: String, verification: Verification) {

        val params = HashMap<String, String>()
        params["loginId"] = loginId
        params["number"] = verification.number
        params["password"] = password
        params["token"] = verification.token
        showProgress("")
        ApiBuilder.create().changePasswordByVerification(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {

            override fun onResponse(call: Call<NewResultResponse<Any>>, response: NewResultResponse<Any>) {
                hideProgress()
                if (LoginInfoManager.getInstance().isMember) {
                    showAlert(R.string.msg_changed_password_in_login, 1)
                    LoginInfoManager.getInstance().user.password = PplusCommonUtil.encryption(params["password"]!!)
                    LoginInfoManager.getInstance().save()
                } else {
                    showAlert(R.string.msg_changed_password, 3)
                }

                setResult(Activity.RESULT_OK)
                finish()
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>, t: Throwable, response: NewResultResponse<Any>) {
                hideProgress()
            }
        }).build().call()
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
