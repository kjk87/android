package com.lejel.wowbox.apps.my

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.databinding.ActivityAlertMobileNumberBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call

class AlertMobileNumberActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAlertMobileNumberBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertMobileNumberBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        binding.editAlertMobileNumber.setSingleLine()

        binding.imageAlertMobileNumberClose.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        binding.textAlertMobileNumberConfirm.setOnClickListener {
            val mobileNumber = binding.editAlertMobileNumber.text.toString().trim()
            if(StringUtils.isEmpty(mobileNumber)){
                showAlert(R.string.hint_input_mobile_number)
                return@setOnClickListener
            }

            if(!FormatUtil.isPhoneNumber(mobileNumber)){
                showAlert(R.string.msg_invalid_phone_number)
                return@setOnClickListener
            }

            val params = HashMap<String, String>()
            params["mobileNumber"] = mobileNumber
            showProgress("")
            ApiBuilder.create().updateMobileNumber(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                    hideProgress()
                    LoginInfoManager.getInstance().member!!.mobileNumber = mobileNumber
                    LoginInfoManager.getInstance().save()
                    showAlert(R.string.msg_completed)
                    setResult(RESULT_OK)
                    finish()
                }

                override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                    hideProgress()
                }
            }).build().call()
        }

    }
}
