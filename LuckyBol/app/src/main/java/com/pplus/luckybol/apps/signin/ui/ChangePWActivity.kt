package com.pplus.luckybol.apps.signin.ui

import android.app.Activity
import android.os.Bundle
import android.text.InputType
import android.view.View
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.Crypt
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.Verification
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.ActivityChangePwBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*

class ChangePWActivity : BaseActivity(), ImplToolbar {

    override fun getPID(): String {

        return ""
    }

    private lateinit var binding: ActivityChangePwBinding

    override fun getLayoutView(): View {
        binding = ActivityChangePwBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        binding.editChangePwPassword.setSingleLine()
        binding.editChangePwPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        binding.textChangePwConfirm.setOnClickListener {
            val password = binding.editChangePwPassword.text.toString().trim()
            if (StringUtils.isEmpty(password)) {
                showAlert(R.string.msg_input_password)
                return@setOnClickListener
            }

            if (password.length < 4) {
                binding.editChangePwPassword.requestFocus()
                showAlert(getString(R.string.to_password) + " " + getString(R.string.format_msg_input_over, 4))
                return@setOnClickListener
            }

            val loginId = intent.getStringExtra(Const.LOGIN_ID)
            val verification = intent.getParcelableExtra<Verification>(Const.VERIFICATION)
            changePw(password, loginId!!, verification!!)
        }
    }

    private fun changePw(password: String, loginId: String, verification: Verification) {

        val params = HashMap<String, String>()
        params["loginId"] = loginId
        params["number"] = verification.number
        params["password"] = Crypt.encrypt(password)
        params["token"] = verification.token
        params["appType"] = Const.APP_TYPE
        params["encrypted"] = "true"
        showProgress("")
        ApiBuilder.create().changePasswordByVerification(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {

            override fun onResponse(call: Call<NewResultResponse<Any>>, response: NewResultResponse<Any>) {
                hideProgress()
                if (LoginInfoManager.getInstance().isMember) {
                    showAlert(R.string.msg_changed_password_in_login, 1)
                    LoginInfoManager.getInstance().user.password = PplusCommonUtil.encryption(Crypt.decrypt(params["password"]!!))
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
                else -> {}
            }
        }

    }
}
