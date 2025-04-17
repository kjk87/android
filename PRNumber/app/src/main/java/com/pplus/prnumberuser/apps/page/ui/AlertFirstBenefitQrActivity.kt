package com.pplus.prnumberuser.apps.page.ui

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.databinding.ActivityAlertSaveQrBinding
import retrofit2.Call

class AlertFirstBenefitQrActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAlertSaveQrBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertSaveQrBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        binding.textAlertSaveQrDesc.setText(R.string.msg_alert_first_benefit_qr_desc)

        val dataString = "prnumberbiz://qr?memberSeqNo=${LoginInfoManager.getInstance().user.no}&type=benefit"
        makeQr(dataString)

        binding.imageAlertSaveQrClose.setOnClickListener {
            onBackPressed()
        }

    }

    private fun makeQr(dataString: String) {
        val params = HashMap<String, String>()
        params["dataString"] = dataString
        showProgress("")
        ApiBuilder.create().makeQrCode(params).setCallback(object : PplusCallback<NewResultResponse<String>> {
            override fun onResponse(call: Call<NewResultResponse<String>>?, response: NewResultResponse<String>?) {
                hideProgress()
                if (response?.data != null) {
                    val url = response.data!!
                    Glide.with(this@AlertFirstBenefitQrActivity).load(url).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_qr_default).error(R.drawable.img_qr_default)).into(binding.imageAlertSaveQr)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<String>>?, t: Throwable?, response: NewResultResponse<String>?) {
                hideProgress()
            }
        }).build().call()
    }

}
