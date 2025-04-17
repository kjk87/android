package com.pplus.luckybol.apps.buff.ui

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.builder.AlertBuilder
import com.pplus.luckybol.apps.common.builder.OnAlertResultListener
import com.pplus.luckybol.apps.common.builder.data.AlertData
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.BuffMember
import com.pplus.luckybol.core.network.model.dto.BuffParam
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.databinding.ActivityForcedExitReasonBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call

class ForcedExitReasonActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityForcedExitReasonBinding

    override fun getLayoutView(): View {
        binding = ActivityForcedExitReasonBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    lateinit var mExitList: List<BuffMember>

    override fun initializeView(savedInstanceState: Bundle?) {
        mExitList = intent.getParcelableArrayListExtra(Const.DATA)!!

        binding.editForcedExitReason.addTextChangedListener {
            if(it.toString().trim().isEmpty()){
                binding.textForcedExitReason.setBackgroundColor(ResourceUtil.getColor(this, R.color.color_c0c6cc))
                binding.textForcedExitReason.isEnabled = false
            }else{
                binding.textForcedExitReason.setBackgroundColor(ResourceUtil.getColor(this, R.color.color_fc5c57))
                binding.textForcedExitReason.isEnabled = true
            }
        }

        binding.textForcedExitReason.setOnClickListener {
            val reason = binding.editForcedExitReason.text.toString().trim()
            if(StringUtils.isEmpty(reason)){
                showAlert(R.string.msg_input_forced_exit)
                return@setOnClickListener
            }

            val builder = AlertBuilder.Builder()
            builder.setTitle(getString(R.string.word_alert_forced_exit_title))
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
            builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_forced_exit), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
            builder.setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {}

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                    when (event_alert) {
                        AlertBuilder.EVENT_ALERT.RIGHT -> {

                            val exitList = arrayListOf<Long>()
                            for(buffMember in mExitList){
                                exitList.add(buffMember.seqNo!!)
                            }

                            val buffParam = BuffParam()
                            buffParam.buffSeqNo = mExitList[0].buffSeqNo
                            buffParam.exitList = exitList
                            buffParam.reason = reason
                            showProgress("")
                            ApiBuilder.create().forcedExitBuff(buffParam).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                                override fun onResponse(call: Call<NewResultResponse<Any>>?,
                                                        response: NewResultResponse<Any>?) {
                                    hideProgress()
                                    showAlert(R.string.msg_complete_forced_exit)
                                    setResult(RESULT_OK)
                                    finish()
                                }

                                override fun onFailure(call: Call<NewResultResponse<Any>>?,
                                                       t: Throwable?,
                                                       response: NewResultResponse<Any>?) {
                                    hideProgress()
                                }
                            }).build().call()
                        }
                        else -> {}
                    }
                }
            }).builder().show(this)
        }

        binding.textForcedExitReason.setBackgroundColor(ResourceUtil.getColor(this, R.color.color_c0c6cc))
        binding.textForcedExitReason.isEnabled = false
        binding.textForcedExitReason.text = getString(R.string.format_buff_forced_exit, mExitList.size.toString())
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_input_forced_exit_reason), ToolbarOption.ToolbarMenu.LEFT)

        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
                else -> {}
            }
        }
    }
}