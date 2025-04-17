package com.root37.buflexz.apps.partner.ui

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.webkit.URLUtil
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.mgmt.LoginInfoManager
import com.root37.buflexz.apps.common.mgmt.NationManager
import com.root37.buflexz.apps.common.toolbar.ImplToolbar
import com.root37.buflexz.apps.common.toolbar.OnToolbarListener
import com.root37.buflexz.apps.common.toolbar.ToolbarOption
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.core.network.ApiBuilder
import com.root37.buflexz.core.network.model.dto.Partner
import com.root37.buflexz.core.network.model.response.NewResultResponse
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.ActivityPartnerReqBinding
import retrofit2.Call


class PartnerReqActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityPartnerReqBinding

    override fun getLayoutView(): View {
        binding = ActivityPartnerReqBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    private var mPartner:Partner? = null

    override fun initializeView(savedInstanceState: Bundle?) {

        val nation = NationManager.getInstance().nationMap!![LoginInfoManager.getInstance().member!!.nation]!!

        binding.textPartnerReqProcess3Desc.text = PplusCommonUtil.fromHtml(getString(R.string.html_partner_earn_process3_desc, nation.adCommission.toString()))
        binding.textPartnerReqEarnDesc1.text = PplusCommonUtil.fromHtml(getString(R.string.html_partner_earn_1))
        binding.textPartnerReqEarnDesc2.text = PplusCommonUtil.fromHtml(getString(R.string.html_partner_earn_2))
        binding.textPartnerReqEarnPrice.text = getString(R.string.format_dollar_unit, "9,000")
        binding.textPartnerReqNameTitle.text = PplusCommonUtil.fromHtml(getString(R.string.html_partner_name_title))
        binding.textPartnerReqEmailTitle.text = PplusCommonUtil.fromHtml(getString(R.string.html_join_email_title))

        binding.editPartnerReqName.setSingleLine()
        binding.editPartnerReqEmail.setSingleLine()
        binding.editPartnerReqInstarUrl.setSingleLine()
        binding.editPartnerReqYoutubeUrl.setSingleLine()
        binding.editPartnerReqXUrl.setSingleLine()

        binding.textPartnerReq.setOnClickListener {
            if(mPartner == null){
                request()
            }else{
                if(mPartner!!.status == "pending"){
                    resendEmail()
                }
            }

        }

        binding.layoutPartnerReqNone.visibility = View.GONE
        binding.layoutPartnerReqPending.visibility = View.GONE
        getPartner()
    }

    private fun getPartner(){
        showProgress("")
        ApiBuilder.create().getPartner().setCallback(object : PplusCallback<NewResultResponse<Partner>>{
            override fun onResponse(call: Call<NewResultResponse<Partner>>?, response: NewResultResponse<Partner>?) {
                hideProgress()
                if(response?.result != null){
                    mPartner = response.result
                    binding.layoutPartnerReqNone.visibility = View.GONE
                    if(mPartner!!.status == "pending"){
                        binding.layoutPartnerReqPending.visibility = View.VISIBLE
                        binding.textPartnerReq.setText(R.string.word_resend_email)
                    }

                }else{
                    binding.layoutPartnerReqNone.visibility = View.VISIBLE
                    binding.textPartnerReq.setText(R.string.word_req_partner)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Partner>>?, t: Throwable?, response: NewResultResponse<Partner>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun request(){
        val name = binding.editPartnerReqName.text.toString().trim()
        if (StringUtils.isEmpty(name)) {
            showAlert(R.string.msg_input_partner_name)
            return
        }

        val email = binding.editPartnerReqEmail.text.toString().trim()

        if (StringUtils.isEmpty(email)) {
            showAlert(R.string.msg_input_email)
            return
        }

        if (!FormatUtil.isEmailAddress(email)) {
            showAlert(R.string.msg_valid_email)
            return
        }

        if (!binding.checkPartnerReqAgree.isChecked) {
            showAlert(R.string.msg_agree_email)
            return
        }
        val params = Partner()
        params.name = name
        params.email = email

        val instar = binding.editPartnerReqInstarUrl.text.toString().trim()
        if (StringUtils.isNotEmpty(instar)) {
            if(!FormatUtil.isValidUrl(instar)){
                showAlert(R.string.msg_input_valid_url_instar)
                return
            }

            params.instargram = instar
        }

        val youtube = binding.editPartnerReqYoutubeUrl.text.toString().trim()
        if (StringUtils.isNotEmpty(youtube)) {
            if(!FormatUtil.isValidUrl(youtube)){
                showAlert(R.string.msg_input_valid_url_youtube)
                return
            }

            params.youtube = youtube
        }

        val x = binding.editPartnerReqXUrl.text.toString().trim()
        if (StringUtils.isNotEmpty(x)) {
            if(!FormatUtil.isValidUrl(x)){
                showAlert(R.string.msg_input_valid_url_x)
                return
            }

            params.twitter = x
        }

        showProgress("")
        ApiBuilder.create().postPartner(params).setCallback(object : PplusCallback<NewResultResponse<Any>>{
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()
                showAlert(R.string.msg_partner_req_complete)
                getPartner()
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun resendEmail(){
        showProgress("")
        ApiBuilder.create().partnerResendEmail().setCallback(object : PplusCallback<NewResultResponse<Any>>{
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()
                showAlert(R.string.msg_resend_email_complete)
                getPartner()
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                hideProgress()
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_req_partner), ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressedDispatcher.onBackPressed()
                    }

                    else -> {}
                }
            }
        }
    }
}