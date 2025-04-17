package com.root37.buflexz.apps.main.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.pref.PreferenceUtil
import com.root37.buflexz.Const
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.apps.community.ui.CommunityApplyActivity
import com.root37.buflexz.apps.faq.ui.FaqActivity
import com.root37.buflexz.apps.notice.ui.NoticeActivity
import com.root37.buflexz.apps.partner.ui.PartnerReqActivity
import com.root37.buflexz.core.network.ApiBuilder
import com.root37.buflexz.core.network.model.dto.Partner
import com.root37.buflexz.core.network.model.dto.Popup
import com.root37.buflexz.core.network.model.response.NewResultResponse
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.ActivityAlertMainPopupBinding
import retrofit2.Call

class AlertMainPopupActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAlertMainPopupBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertMainPopupBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val popup = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, Popup::class.java)!!
        binding.webviewAlertMainPopup.loadDataWithBaseURL("", popup.contents!!, "text/html; charset=utf-8", "utf-8", "")

        binding.webviewAlertMainPopup.setOnClickListener {

            when(popup.moveType){
                "inner"->{
                    when(popup.innerType){
                        "partner" -> {

                            if (!PplusCommonUtil.loginCheck(this, null)) {
                                return@setOnClickListener
                            }

                            showProgress("")
                            ApiBuilder.create().getPartner().setCallback(object : PplusCallback<NewResultResponse<Partner>> {
                                override fun onResponse(call: Call<NewResultResponse<Partner>>?, response: NewResultResponse<Partner>?) {
                                    hideProgress()
                                    if (response?.result == null || response.result!!.status != "active") {
                                        val intent = Intent(this@AlertMainPopupActivity, PartnerReqActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                        startActivity(intent)

                                    } else {
                                        showAlert(R.string.msg_already_active_partner)
                                        finish()
                                    }
                                }

                                override fun onFailure(call: Call<NewResultResponse<Partner>>?, t: Throwable?, response: NewResultResponse<Partner>?) {
                                    hideProgress()
                                    finish()
                                }
                            }).build().call()
                        }
                        "telegram"->{
                            if (!PplusCommonUtil.loginCheck(this, null)) {
                                return@setOnClickListener
                            }

                            val intent = Intent(this, CommunityApplyActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            startActivity(intent)
                        }
                        "main" -> {
                            finish()
                        }

                        "notice" -> {
                            val intent = Intent(this, NoticeActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            startActivity(intent)
                        }

                        "faq" -> {
                            val intent = Intent(this, FaqActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            startActivity(intent)
                        }
                    }

                }
                "outer"->{
                    PplusCommonUtil.openChromeWebView(this, popup.outerUrl!!)
                }
                "none"->{
                    finish()
                }
            }
        }

        binding.textAlertMainPopupClose.setOnClickListener {
            finish()
        }

        binding.textAlertMainPopupDoNotAgain.setOnClickListener {
            PreferenceUtil.getDefaultPreference(this).put(Const.POPUP+popup.seqNo, false)
            finish()
        }

        if(popup.closeType == 2){
            binding.textAlertMainPopupDoNotAgain.visibility = View.VISIBLE
        }else{
            binding.textAlertMainPopupDoNotAgain.visibility = View.GONE
        }
    }

    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        DOT.onStartPage(this)
        finish()
    }

}
