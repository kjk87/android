package com.root37.buflexz.apps.login

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.pplus.networks.common.PplusCallback
import com.root37.buflexz.Const
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.builder.AlertBuilder
import com.root37.buflexz.apps.common.builder.OnAlertResultListener
import com.root37.buflexz.apps.common.builder.data.AlertData
import com.root37.buflexz.apps.common.toolbar.ImplToolbar
import com.root37.buflexz.apps.common.toolbar.OnToolbarListener
import com.root37.buflexz.apps.common.toolbar.ToolbarOption
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.core.network.ApiBuilder
import com.root37.buflexz.core.network.model.dto.Member
import com.root37.buflexz.core.network.model.response.NewResultResponse
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.ActivityWithdrawalAuthBinding
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