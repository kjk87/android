package com.pplus.prnumberbiz.apps.setting.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption.ToolbarMenu
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Verification
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import kotlinx.android.synthetic.main.activity_verification.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.*

class VerificationActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_verification
    }

    private var verification: Verification? = null
    private var mobileNumber: String? = null
    private var mKey: String? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        mKey = intent.getStringExtra(Const.KEY)

        text_change_mobile_auth.setOnClickListener {
            mobileNumber = edit_change_mobile_mobileNumber.text.toString()
            if (mobileNumber!!.trim().isEmpty()) {
                showAlert(R.string.msg_input_mobile_number)
                return@setOnClickListener
            }
            request(mobileNumber!!)
        }
    }

    private fun request(mobile: String) {
        val params: MutableMap<String, String> = HashMap()
        params["media"] = "sms"
        params["mobile"] = mobile
        if ((mKey == Const.MOBILE_NUMBER)) {
            params["type"] = "changeMobile"
        } else if ((mKey == Const.LEAVE)) {
            params["type"] = "leave"
        } else if ((mKey == Const.CANCEL_LEAVE)) {
            params["type"] = "cancelLeave"
        }
        //        params.put("type", "findId");
        showProgress("")
        ApiBuilder.create().verification(params).setCallback(object : PplusCallback<NewResultResponse<Verification>>{
            override fun onResponse(call: Call<NewResultResponse<Verification>>?, response: NewResultResponse<Verification>?) {
                hideProgress()
                if(response != null){
                    verification = response.data
                    text_change_mobile_auth?.setText(R.string.word_reAuth)
                    edit_change_mobile_number?.requestFocus()
                    showAlert(R.string.msg_request_sms)
                }

            }

            override fun onFailure(call: Call<NewResultResponse<Verification>>?, t: Throwable?, response: NewResultResponse<Verification>?) {
                hideProgress()
                if(response != null){
                    if (response.resultCode == 504) {
                        showAlert(R.string.msg_exist_mobile)
                    } else {
                        showAlert(R.string.msg_can_not_use_mobile)
                    }
                }

            }
        })
    }

    private fun confirm(number: String) {
        val params: MutableMap<String, String?> = HashMap()
        params["number"] = number
        params["token"] = verification!!.token
        params["mobile"] = mobileNumber
        params["media"] = "sms"
        if ((mKey == Const.MOBILE_NUMBER)) {
            params["type"] = "changeMobile"
        } else if ((mKey == Const.LEAVE)) {
            params["type"] = "leave"
        } else if ((mKey == Const.CANCEL_LEAVE)) {
            params["type"] = "cancelLeave"
        }
        showProgress("")
        ApiBuilder.create().confirmVerification(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {

            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()
                verification!!.number = number
                val data = Intent()
                data.putExtra(Const.VERIFICATION, verification)
                data.putExtra(Const.MOBILE_NUMBER, mobileNumber)
                setResult(Activity.RESULT_OK, data)
                finish()
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                hideProgress()
                showAlert(R.string.msg_failed_verification)
            }

        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {
        val key = intent.getStringExtra(Const.KEY)
        var title: String? = null
        val toolbarOption = ToolbarOption(this)
        if ((key == Const.MOBILE_NUMBER)) {
            title = getString(R.string.word_change_number)
        } else if ((key == Const.LEAVE)) {
            title = getString(R.string.word_auth_leave)
        }
        toolbarOption.initializeDefaultToolbar(title, ToolbarMenu.LEFT)
        toolbarOption.setToolbarMenu(ToolbarMenu.RIGHT, getString(R.string.word_confirm))
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {
        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarMenu.LEFT -> if ((tag == 1)) {
                    onBackPressed()
                }
                ToolbarMenu.RIGHT -> if ((tag == 1)) {
                    if (verification == null) {
                        showAlert(R.string.msg_request_mobile_verification)
                        return@OnToolbarListener
                    }
                    val number = edit_change_mobile_number?.text.toString().trim()
                    if (number.trim().isEmpty()) {
                        showAlert(R.string.hint_auth_number)
                        return@OnToolbarListener
                    }
                    confirm(number)
                }
            }
        }
    }
}