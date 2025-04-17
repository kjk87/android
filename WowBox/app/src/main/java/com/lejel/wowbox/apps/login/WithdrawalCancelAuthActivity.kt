package com.lejel.wowbox.apps.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.pplus.networks.common.PplusCallback
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.builder.AlertBuilder
import com.lejel.wowbox.apps.common.builder.OnAlertResultListener
import com.lejel.wowbox.apps.common.builder.data.AlertData
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.verify.ui.SelectVerifyTypeActivity
import com.lejel.wowbox.apps.verify.ui.VerifySmsActivity
import com.lejel.wowbox.apps.verify.ui.VerifyWhatsAppActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.Member
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityWithdrawalAuthBinding
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call

class WithdrawalCancelAuthActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityWithdrawalAuthBinding

    override fun getLayoutView(): View {
        binding = ActivityWithdrawalAuthBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    lateinit var mMember: Member

    override fun initializeView(savedInstanceState: Bundle?) {

        mMember = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, Member::class.java)!!

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val builder = AlertBuilder.Builder()
                builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_cancel_auth), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
                builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
                builder.setOnAlertResultListener(object : OnAlertResultListener {

                    override fun onCancel() {

                    }

                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                        when (event_alert) {
                            AlertBuilder.EVENT_ALERT.RIGHT -> {
                                setResult(RESULT_CANCELED)
                                finish()
                            }

                            else -> {

                            }
                        }
                    }
                }).builder().show(this@WithdrawalCancelAuthActivity)
            }
        })

        if(mMember.verifiedMobile != null && mMember.verifiedMobile!! && StringUtils.isNotEmpty(mMember.mobileNumber)){
            val intent = Intent(this, SelectVerifyTypeActivity::class.java)
            intent.putExtra(Const.MOOBILE_NUMBER, mMember.mobileNumber)
            selectVerifyTypeLauncher.launch(intent)
        }else{
            val params = HashMap<String, String>()
            params["type"] = "withdrawalCancel"
            params["email"] = mMember.email!!
            params["language"] = mMember.language!!
            showProgress("")
            ApiBuilder.create().sendEmailForAuth(params).setCallback(object : PplusCallback<NewResultResponse<String>> {
                override fun onResponse(call: Call<NewResultResponse<String>>?, response: NewResultResponse<String>?) {
                    hideProgress()
                    if (response?.result != null) {
                        confirm(response.result!!)
                    }
                }

                override fun onFailure(call: Call<NewResultResponse<String>>?, t: Throwable?, response: NewResultResponse<String>?) {
                    hideProgress()
                }
            }).build().call()
        }

    }

    val selectVerifyTypeLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val mobileNumber = result.data!!.getStringExtra(Const.MOOBILE_NUMBER)!!
            val verifyType = result.data!!.getStringExtra(Const.VERIFY_TYPE)!!
            when (verifyType) {
                "whatsapp" -> {
                    sendWhatsApp(mobileNumber)
                }
                else -> {
                    sendSms(mobileNumber)
                }
            }
        }
    }

    private fun sendWhatsApp(mobileNumber: String) {
        val params = HashMap<String, String>()
        params["mobileNumber"] = mobileNumber
        params["type"] = "withdrawalCancel"
        showProgress("")
        ApiBuilder.create().sendWhatsapp(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()
                val intent = Intent(this@WithdrawalCancelAuthActivity, VerifyWhatsAppActivity::class.java)
                intent.putExtra(Const.MOOBILE_NUMBER, mobileNumber)
                intent.putExtra(Const.TYPE, "withdrawalCancel")
                intent.putExtra(Const.MEMBER, mMember)
                defaultLauncher.launch(intent)
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun sendSms(mobileNumber: String) {
        val params = HashMap<String, String>()
        params["mobileNumber"] = mobileNumber
        params["type"] = "withdrawalCancel"
        showProgress("")
        ApiBuilder.create().sendSms(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()
                val intent = Intent(this@WithdrawalCancelAuthActivity, VerifySmsActivity::class.java)
                intent.putExtra(Const.MOOBILE_NUMBER, mobileNumber)
                intent.putExtra(Const.TYPE, "withdrawalCancel")
                intent.putExtra(Const.MEMBER, mMember)
                defaultLauncher.launch(intent)
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                hideProgress()
            }
        }).build().call()
    }

    val defaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        finish()
    }

    fun confirm(authNumber: String) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.withdrawal_container, WithdrawalCancelAuthConfirmFragment.newInstance(mMember, authNumber), WithdrawalCancelAuthConfirmFragment::class.java.simpleName)
        ft.commit()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_withdrawal_cancel), ToolbarOption.ToolbarMenu.RIGHT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
                        onBackPressedDispatcher.onBackPressed()
                    }

                    else -> {}
                }
            }
        }
    }
}