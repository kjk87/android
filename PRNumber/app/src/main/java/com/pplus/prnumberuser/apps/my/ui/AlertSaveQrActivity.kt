package com.pplus.prnumberuser.apps.my.ui

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
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call

class AlertSaveQrActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAlertSaveQrBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertSaveQrBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        if(StringUtils.isNotEmpty(LoginInfoManager.getInstance().user.qrImage)){
            Glide.with(this).load(LoginInfoManager.getInstance().user.qrImage).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_qr_default).error(R.drawable.img_qr_default)).into(binding.imageAlertSaveQr)
        }else{
            val dataString = "prnumberbiz://qr?memberSeqNo=${LoginInfoManager.getInstance().user.no}&type=point"
            makeQr(dataString)
        }

        binding.textAlertSaveQrDesc.setText(R.string.msg_alert_save_qr_desc)

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

                    LoginInfoManager.getInstance().user.qrImage = url
                    LoginInfoManager.getInstance().save()
                    Glide.with(this@AlertSaveQrActivity).load(LoginInfoManager.getInstance().user.qrImage).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_qr_default).error(R.drawable.img_qr_default)).into(binding.imageAlertSaveQr)
                    val params = HashMap<String, String>()
                    params["qrImage"] = url
                    ApiBuilder.create().updateQrImage(params).build().call()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<String>>?, t: Throwable?, response: NewResultResponse<String>?) {
                hideProgress()
            }
        }).build().call()
    }

}
