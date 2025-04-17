package com.pplus.luckybol.apps.buff.ui

import android.os.Bundle
import android.view.View
import com.pplus.luckybol.Const
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.NotificationBox
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.databinding.ActivityAlertForcedExitBinding
import com.pplus.networks.common.PplusCallback
import retrofit2.Call

class AlertForcedExitActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAlertForcedExitBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertForcedExitBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val notificationBox = intent.getParcelableExtra<NotificationBox>(Const.DATA)!!

        binding.textAlertForcedExitConfirm.setOnClickListener {
            finish()
        }

        binding.textAlertForcedExitReason.text = notificationBox.contents

        val params = HashMap<String, String>()
        params["notificationBoxSeqNo"] = notificationBox.seqNo.toString()
        ApiBuilder.create().notificationBoxRead(params).setCallback(object : PplusCallback<NewResultResponse<Any>>{
            override fun onResponse(call: Call<NewResultResponse<Any>>?,
                                    response: NewResultResponse<Any>?) {

            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<Any>?) {

            }
        }).build().call()
    }


}
