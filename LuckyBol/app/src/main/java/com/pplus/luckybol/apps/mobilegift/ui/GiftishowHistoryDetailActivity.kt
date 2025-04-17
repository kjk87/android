package com.pplus.luckybol.apps.mobilegift.ui

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.builder.AlertBuilder
import com.pplus.luckybol.apps.common.builder.AlertBuilder.EVENT_ALERT
import com.pplus.luckybol.apps.common.builder.OnAlertResultListener
import com.pplus.luckybol.apps.common.builder.data.AlertData.MessageData
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption.ToolbarMenu
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.mobilegift.data.GiftishowHistoryTargetAdapter
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.GiftishowBuy
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.databinding.ActivityMobileGiftHistoryDetailBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*
import kotlin.collections.HashMap

class GiftishowHistoryDetailActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String? {
        return null
    }

    private lateinit var binding: ActivityMobileGiftHistoryDetailBinding

    override fun getLayoutView(): View {
        binding = ActivityMobileGiftHistoryDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    private var mGiftishowBuy: GiftishowBuy? = null
    override fun initializeView(savedInstanceState: Bundle?) {
        mGiftishowBuy = intent.getParcelableExtra(Const.DATA)

        Glide.with(this).load(mGiftishowBuy!!.giftishow!!.goodsImgB).apply(RequestOptions().centerCrop().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(binding.imageMobileGiftHistoryDetail)
        binding.textMobileGiftHistoryDetailName.text = mGiftishowBuy!!.giftishow!!.goodsName
        binding.textMobileGiftHistoryDetailPrice.text = getString(R.string.format_product_price, FormatUtil.getMoneyType(mGiftishowBuy!!.unitPrice.toString()))

        if (mGiftishowBuy!!.totalCount!! > 1) {
            binding.textMobileGiftHistoryDetailReceiver.text = getString(R.string.format_send_target, getString(R.string.format_other, mGiftishowBuy!!.targetList!![0].name, mGiftishowBuy!!.totalCount!! - 1))
        } else {
            if (StringUtils.isNotEmpty(mGiftishowBuy!!.targetList!![0].name)) {
                binding.textMobileGiftHistoryDetailReceiver.text = getString(R.string.format_send_target, mGiftishowBuy!!.targetList!![0].name)
            } else {
                binding.textMobileGiftHistoryDetailReceiver.text = getString(R.string.format_send_target, getString(R.string.word_me))
            }
        }

        binding.textMobileGiftHistoryDetailCount.text = getString(R.string.format_count_unit, FormatUtil.getMoneyType(mGiftishowBuy!!.totalCount.toString()))
        binding.textMobileGiftHistoryDetailTotalPrice.text = getString(R.string.format_point_unit, FormatUtil.getMoneyType(mGiftishowBuy!!.price.toString()))
        if (StringUtils.isEmpty(mGiftishowBuy!!.msg)) {
            binding.textMobileGiftHistoryDetailSendMsg.visibility = View.GONE
            binding.layoutMobileGiftHistoryDetailSendMsgTitle.visibility = View.GONE
        } else {
            binding.textMobileGiftHistoryDetailSendMsg.visibility = View.VISIBLE
            binding.layoutMobileGiftHistoryDetailSendMsgTitle.visibility = View.VISIBLE
            binding.textMobileGiftHistoryDetailSendMsg.text = mGiftishowBuy!!.msg
        }

        binding.textMobileGiftHistoryDetailCancel.visibility = View.GONE

        binding.imageMobileGiftHistoryDetail.setOnClickListener {
            val intent = Intent(this, GiftishowDetailActivity::class.java)
            intent.putExtra(Const.DATA, mGiftishowBuy!!.giftishow)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            setResult(RESULT_OK)
        }

        text_top_right!!.visibility = View.GONE

        val adapter = GiftishowHistoryTargetAdapter()
        adapter.addAll(mGiftishowBuy!!.targetList)
        binding.recyclerMobileGiftHistoryDetailTarget.layoutManager = LinearLayoutManager(this)
        binding.recyclerMobileGiftHistoryDetailTarget.adapter = adapter
        checkStatus()
    }

    private fun checkStatus() {
        val params = HashMap<String, String>()
        params["trId"] = mGiftishowBuy!!.targetList!![0].trId!!
        showProgress("")
        ApiBuilder.create().checkGiftishowStatus(params).setCallback(object : PplusCallback<NewResultResponse<String>> {
            override fun onResponse(call: Call<NewResultResponse<String>>?,
                                    response: NewResultResponse<String>?) {
                hideProgress()
                if (response?.data != null && response.data == "01") {
                    text_top_right!!.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<NewResultResponse<String>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<String>?) {
                hideProgress()
                hideProgress()
            }
        }).build().call()
    }

    private fun resend() {
        val params: MutableMap<String, String> = HashMap()
        params["giftshowBuySeqNo"] = "" + mGiftishowBuy!!.seqNo.toString()
        showProgress("")
        ApiBuilder.create().resendGiftishowStatus(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?,
                                    response: NewResultResponse<Any>?) {
                hideProgress()
                showAlert(R.string.msg_complete_mobile_gift_resend)
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<Any>?) {
                hideProgress()
            }
        }).build().call()
    }

    private var text_top_right: TextView? = null
    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_change_history), ToolbarMenu.LEFT)
        text_top_right = TextView(ContextThemeWrapper(this, R.style.buttonStyle))
        text_top_right!!.setText(R.string.word_resend)
        text_top_right!!.isClickable = true
        text_top_right!!.gravity = Gravity.CENTER
        text_top_right!!.setPadding(0, 0, resources.getDimensionPixelSize(R.dimen.width_66), 0)
        text_top_right!!.setTextColor(ResourceUtil.getColorStateList(this, R.color.color_fc5c57))
        text_top_right!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(R.dimen.textSize_45pt).toFloat())
        text_top_right!!.setSingleLine()
        toolbarOption.setToolbarMenu(ToolbarMenu.RIGHT, text_top_right, 0)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {
        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
                ToolbarMenu.RIGHT -> if (tag == 1) {
                    val builder = AlertBuilder.Builder()
                    builder.setTitle(getString(R.string.word_notice_alert))
                    builder.addContents(MessageData(getString(R.string.msg_question_mobile_gift_resend), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                    builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
                    builder.setOnAlertResultListener(object : OnAlertResultListener {
                        override fun onCancel() {}
                        override fun onResult(event_alert: EVENT_ALERT) {
                            when (event_alert) {
                                EVENT_ALERT.RIGHT -> resend()
                                else -> {}
                            }
                        }
                    }).builder().show(this@GiftishowHistoryDetailActivity)
                }
            }
        }
    }
}