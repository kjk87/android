package com.pplus.prnumberuser.apps.setting.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption.ToolbarMenu
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.Verification
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.databinding.ActivityVerificationBinding
import retrofit2.Call
import java.util.*

class VerificationActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityVerificationBinding

    override fun getLayoutView(): View {
        binding = ActivityVerificationBinding.inflate(layoutInflater)
        return binding.root
    }

    private var verification: Verification? = null
    private var mobileNumber: String? = null
    private var mKey: String? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        mKey = intent.getStringExtra(Const.KEY)

        binding.textChangeMobileAuth.setOnClickListener {
            mobileNumber = binding.editChangeMobileMobileNumber.text.toString()
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
        params["appType"] = Const.APP_TYPE
        showProgress("")
        ApiBuilder.create().verification(params).setCallback(object : PplusCallback<NewResultResponse<Verification>>{
            override fun onResponse(call: Call<NewResultResponse<Verification>>?, response: NewResultResponse<Verification>?) {
                hideProgress()
                if(response != null){
                    verification = response.data
                    binding.textChangeMobileAuth.setText(R.string.word_reAuth)
                    binding.editChangeMobileNumber.requestFocus()
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
        }).build().call()
    }

    private fun confirm(number: String) {
        val params = HashMap<String, String>()
        params["number"] = number
        params["token"] = verification!!.token
        params["mobile"] = mobileNumber!!
        params["media"] = "sms"
        if ((mKey == Const.MOBILE_NUMBER)) {
            params["type"] = "changeMobile"
        } else if ((mKey == Const.LEAVE)) {
            params["type"] = "leave"
        } else if ((mKey == Const.CANCEL_LEAVE)) {
            params["type"] = "cancelLeave"
        }
        params["appType"] = Const.APP_TYPE
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
        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarMenu.LEFT -> if ((tag == 1)) {
                        onBackPressed()
                    }
                    ToolbarMenu.RIGHT -> if ((tag == 1)) {
                        if (verification == null) {
                            showAlert(R.string.msg_request_mobile_verification)
                            return
                        }
                        val number = binding.editChangeMobileNumber.text.toString().trim()
                        if (number.trim().isEmpty()) {
                            showAlert(R.string.hint_auth_number)
                            return
                        }
                        confirm(number)
                    }
                }
            }
        }
    }
}