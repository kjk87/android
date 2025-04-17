package com.pplus.luckybol.apps.buff.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.buff.data.ReceiveAdapter
import com.pplus.luckybol.apps.common.builder.AlertBuilder
import com.pplus.luckybol.apps.common.builder.OnAlertResultListener
import com.pplus.luckybol.apps.common.builder.data.AlertData
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.BuffRequest
import com.pplus.luckybol.core.network.model.dto.BuffRequestResult
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.databinding.ActivityBuffReceiveBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import retrofit2.Call

class BuffReceiveActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityBuffReceiveBinding

    override fun getLayoutView(): View {
        binding = ActivityBuffReceiveBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    private lateinit var mAdapter: ReceiveAdapter
    private lateinit var mLayoutManager: LinearLayoutManager

    override fun initializeView(savedInstanceState: Bundle?) {
        mAdapter = ReceiveAdapter()
        mLayoutManager = LinearLayoutManager(this)
        binding.recyclerBuffReceive.layoutManager = mLayoutManager
        binding.recyclerBuffReceive.adapter = mAdapter

        mAdapter.listener = object : ReceiveAdapter.OnItemClickListener {
            override fun onConsent(position: Int) {
                val item = mAdapter.getItem(position)

                val builder = AlertBuilder.Builder()
                builder.setTitle(item.buff!!.title)
                builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
                builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_consent_buff_desc), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_consent_buff))
                builder.setOnAlertResultListener(object : OnAlertResultListener {

                    override fun onCancel() {}

                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                        when (event_alert) {
                            AlertBuilder.EVENT_ALERT.RIGHT -> {
                                changeBuffRequest(item, "consent")
                            }
                            else -> {}
                        }
                    }
                }).builder().show(this@BuffReceiveActivity)
            }

            override fun onReject(position: Int) {
                val item = mAdapter.getItem(position)

                val builder = AlertBuilder.Builder()
                builder.setTitle(item.buff!!.title)
                builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
                builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_reject_buff_desc), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_delete))
                builder.setOnAlertResultListener(object : OnAlertResultListener {

                    override fun onCancel() {}

                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                        when (event_alert) {
                            AlertBuilder.EVENT_ALERT.RIGHT -> {
                                changeBuffRequest(item, "reject")
                            }
                            else -> {}
                        }
                    }
                }).builder().show(this@BuffReceiveActivity)
            }
        }
        listCall()
    }

    private fun changeBuffRequest(buffRequest: BuffRequest, status: String) {
        val params = HashMap<String, String>()
        params["buffRequestSeqNo"] = buffRequest.seqNo.toString()
        params["status"] = status
        showProgress("")
        ApiBuilder.create().changeBuffRequest(params).setCallback(object : PplusCallback<NewResultResponse<BuffRequestResult>> {
            override fun onResponse(call: Call<NewResultResponse<BuffRequestResult>>?,
                                    response: NewResultResponse<BuffRequestResult>?) {
                hideProgress()
                when(status){
                    "consent"->{
                        val builder = AlertBuilder.Builder()
//                        builder.setTitle(buffRequest.buff!!.title)
                        builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
                        builder.addContents(AlertData.MessageData(getString(R.string.html_consent_buff, buffRequest.buff!!.title), AlertBuilder.MESSAGE_TYPE.HTML, 2))
                        builder.builder().show(this@BuffReceiveActivity)
                        finish()
                    }
                    else->{
                        listCall()
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<BuffRequestResult>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<BuffRequestResult>?) {
                hideProgress()
                if (response?.resultCode == 504) {
                    showAlert(R.string.msg_already_exist_buff)
                }else if (response?.resultCode == 662) {
                    showAlert(R.string.msg_exceed_buff_capacity)
                }else if (response?.resultCode == 516) {
                    val remainSecond = response.data!!.remainSecond
                    val intent = Intent(this@BuffReceiveActivity, AlertBuffTimeLimitActivity::class.java)
                    intent.putExtra(Const.REMAIN_SECOND, remainSecond)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                }
            }
        }).build().call()
    }

    private fun listCall() {

        showProgress("")
        ApiBuilder.create().getRequestList().setCallback(object : PplusCallback<NewResultResponse<BuffRequest>> {
            override fun onResponse(call: Call<NewResultResponse<BuffRequest>>?,
                                    response: NewResultResponse<BuffRequest>?) {
                hideProgress()
                if (response?.datas != null) {
                    val totalCount = response.datas!!.size
                    if (totalCount > 0) {
                        binding.layoutBuffReceiveNotExist.visibility = View.GONE
                    } else {
                        binding.layoutBuffReceiveNotExist.visibility = View.VISIBLE
                    }

                    binding.textBuffReceiveCount.text = getString(R.string.format_receive_buff_count, FormatUtil.getMoneyType(totalCount.toString()))

                    mAdapter.setDataList(response.datas!! as MutableList<BuffRequest>)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<BuffRequest>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<BuffRequest>?) {
                hideProgress()
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_receive_buff), ToolbarOption.ToolbarMenu.LEFT)

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