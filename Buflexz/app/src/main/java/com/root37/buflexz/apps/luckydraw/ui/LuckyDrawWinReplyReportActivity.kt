package com.root37.buflexz.apps.luckydraw.ui

import android.os.Bundle
import android.view.View
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.utils.StringUtils
import com.root37.buflexz.Const
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.mgmt.LoginInfoManager
import com.root37.buflexz.apps.common.toolbar.ImplToolbar
import com.root37.buflexz.apps.common.toolbar.OnToolbarListener
import com.root37.buflexz.apps.common.toolbar.ToolbarOption
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.core.network.ApiBuilder
import com.root37.buflexz.core.network.model.dto.LuckyDrawWinReply
import com.root37.buflexz.core.network.model.response.NewResultResponse
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.ActivityLuckyDrawWinReplyReportBinding
import retrofit2.Call

class LuckyDrawWinReplyReportActivity : BaseActivity(), ImplToolbar {
    private lateinit var binding: ActivityLuckyDrawWinReplyReportBinding

    override fun getLayoutView(): View {
        binding = ActivityLuckyDrawWinReplyReportBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    var mReason = ""

    override fun initializeView(savedInstanceState: Bundle?) {

        val reply = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, LuckyDrawWinReply::class.java)!!

        binding.textLuckyDrawWinReport1.setOnClickListener {
            binding.textLuckyDrawWinReport1.isSelected = true
            binding.textLuckyDrawWinReport2.isSelected = false
            binding.textLuckyDrawWinReport3.isSelected = false
            binding.textLuckyDrawWinReport4.isSelected = false
            binding.textLuckyDrawWinReport5.isSelected = false
            binding.textLuckyDrawWinReport6.isSelected = false
            binding.textLuckyDrawWinReportEtc.isSelected = false
            mReason = binding.textLuckyDrawWinReport1.text.toString()
        }

        binding.textLuckyDrawWinReport2.setOnClickListener {
            binding.textLuckyDrawWinReport1.isSelected = false
            binding.textLuckyDrawWinReport2.isSelected = true
            binding.textLuckyDrawWinReport3.isSelected = false
            binding.textLuckyDrawWinReport4.isSelected = false
            binding.textLuckyDrawWinReport5.isSelected = false
            binding.textLuckyDrawWinReport6.isSelected = false
            binding.textLuckyDrawWinReportEtc.isSelected = false
            mReason = binding.textLuckyDrawWinReport2.text.toString()
        }

        binding.textLuckyDrawWinReport3.setOnClickListener {
            binding.textLuckyDrawWinReport1.isSelected = false
            binding.textLuckyDrawWinReport2.isSelected = false
            binding.textLuckyDrawWinReport3.isSelected = true
            binding.textLuckyDrawWinReport4.isSelected = false
            binding.textLuckyDrawWinReport5.isSelected = false
            binding.textLuckyDrawWinReport6.isSelected = false
            binding.textLuckyDrawWinReportEtc.isSelected = false
            mReason = binding.textLuckyDrawWinReport3.text.toString()
        }
        binding.textLuckyDrawWinReport4.setOnClickListener {
            binding.textLuckyDrawWinReport1.isSelected = false
            binding.textLuckyDrawWinReport2.isSelected = false
            binding.textLuckyDrawWinReport3.isSelected = false
            binding.textLuckyDrawWinReport4.isSelected = true
            binding.textLuckyDrawWinReport5.isSelected = false
            binding.textLuckyDrawWinReport6.isSelected = false
            binding.textLuckyDrawWinReportEtc.isSelected = false
            mReason = binding.textLuckyDrawWinReport4.text.toString()
        }
        binding.textLuckyDrawWinReport5.setOnClickListener {
            binding.textLuckyDrawWinReport1.isSelected = false
            binding.textLuckyDrawWinReport2.isSelected = false
            binding.textLuckyDrawWinReport3.isSelected = false
            binding.textLuckyDrawWinReport4.isSelected = false
            binding.textLuckyDrawWinReport5.isSelected = true
            binding.textLuckyDrawWinReport6.isSelected = false
            binding.textLuckyDrawWinReportEtc.isSelected = false
            mReason = binding.textLuckyDrawWinReport5.text.toString()
        }
        binding.textLuckyDrawWinReport6.setOnClickListener {
            binding.textLuckyDrawWinReport1.isSelected = false
            binding.textLuckyDrawWinReport2.isSelected = false
            binding.textLuckyDrawWinReport3.isSelected = false
            binding.textLuckyDrawWinReport4.isSelected = false
            binding.textLuckyDrawWinReport5.isSelected = false
            binding.textLuckyDrawWinReport6.isSelected = true
            binding.textLuckyDrawWinReportEtc.isSelected = false
            mReason = binding.textLuckyDrawWinReport6.text.toString()
        }
        binding.textLuckyDrawWinReportEtc.setOnClickListener {
            binding.textLuckyDrawWinReport1.isSelected = false
            binding.textLuckyDrawWinReport2.isSelected = false
            binding.textLuckyDrawWinReport3.isSelected = false
            binding.textLuckyDrawWinReport4.isSelected = false
            binding.textLuckyDrawWinReport5.isSelected = false
            binding.textLuckyDrawWinReport6.isSelected = false
            binding.textLuckyDrawWinReportEtc.isSelected = true
        }

        binding.textLuckyDrawWinReport.setOnClickListener {
            if (StringUtils.isEmpty(mReason) && !binding.textLuckyDrawWinReportEtc.isSelected) {
                showAlert(R.string.msg_select_report_reason)
                return@setOnClickListener
            }

            if (binding.textLuckyDrawWinReportEtc.isSelected) {
                mReason = binding.editLuckyDrawWinReportEtc.text.toString().trim()
                if (StringUtils.isEmpty(mReason)) {
                    showAlert(R.string.msg_input_etc_reason)
                    return@setOnClickListener
                }
            }

            val params = HashMap<String, String>()
            params["type"] = "luckyDrawWin"
            params["luckyDrawWinReplySeqNo"] = reply.seqNo.toString()
            params["reason"] = mReason
            showProgress("")
            ApiBuilder.create().replyReport(params).setCallback(object : PplusCallback<NewResultResponse<Any>>{
                override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                    hideProgress()
                    showAlert(R.string.msg_reported_reply)
                    setResult(RESULT_OK)
                    finish()
                }

                override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                    hideProgress()
                }
            }).build().call()
        }

    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.msg_report), ToolbarOption.ToolbarMenu.RIGHT)
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