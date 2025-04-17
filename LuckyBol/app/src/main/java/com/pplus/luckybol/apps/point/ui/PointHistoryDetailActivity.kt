package com.pplus.luckybol.apps.point.ui

import android.os.Bundle
import android.view.View
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.PointHistory
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.databinding.ActivityBolHistoryDetailBinding
import com.pplus.luckybol.databinding.ItemPointDetailBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.time.DateFormatUtils
import retrofit2.Call
import java.text.SimpleDateFormat
import java.util.*

class PointHistoryDetailActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityBolHistoryDetailBinding

    override fun getLayoutView(): View {
        binding = ActivityBolHistoryDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    var mPointHistory: PointHistory? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        mPointHistory = intent.getParcelableExtra(Const.DATA)

        binding.textBolHistoryMobileGiftHistoryDescription.visibility = View.GONE
        getHistory()
    }

    fun getHistory() {
        val params = HashMap<String, String>()
        params["seqNo"] = mPointHistory!!.seqNo.toString()
        showProgress("")
        ApiBuilder.create().getPointHistory(params).setCallback(object : PplusCallback<NewResultResponse<PointHistory>> {
            override fun onResponse(call: Call<NewResultResponse<PointHistory>>?, response: NewResultResponse<PointHistory>?) {
                hideProgress()
                if (isFinishing) {
                    return
                }

                if(response?.data != null){
                    mPointHistory = response.data
                    setData()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<PointHistory>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<PointHistory>?) {

            }
        }).build().call()
    }

    fun setData() {
        when (mPointHistory!!.type) {

            "used" -> {
                //사용
                setTitle(getString(R.string.word_use_history_detail))
                binding.textBolHistoryDetailTitle1.setText(R.string.word_use_cash)
                binding.textBolHistoryDetailTitle2.setText(R.string.word_use_date)
                binding.textBolHistoryDetailContents1.setTextColor(ResourceUtil.getColor(this, R.color.color_6e7780))
            }
            "charge" -> {
                //적립
                setTitle(getString(R.string.word_charge_history_detail))
                binding.textBolHistoryDetailTitle1.setText(R.string.word_save_cash)
                binding.textBolHistoryDetailTitle2.setText(R.string.word_earn_date)
                binding.textBolHistoryDetailContents1.setTextColor(ResourceUtil.getColor(this, R.color.color_373c42))
            }
        }

        binding.textBolHistoryDetailContents1.text = getString(R.string.format_money_unit, FormatUtil.getMoneyTypeFloat(mPointHistory!!.point.toString()))
        try {
            val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mPointHistory!!.regDatetime)

            val output = SimpleDateFormat("yyyy.MM.dd")
            binding.textBolHistoryDetailContents2.text = output.format(d)

        } catch (e: Exception) {
            binding.textBolHistoryDetailContents2.text = ""
        }

        val itemPointDetailBinding = ItemPointDetailBinding.inflate(layoutInflater)
        when (mPointHistory!!.type) {

            "used" -> {
                itemPointDetailBinding.textPointDetailTitle.text = getString(R.string.word_use_type)
            }
            "charge" -> {
                itemPointDetailBinding.textPointDetailTitle.text = getString(R.string.word_reward_type)
            }
        }

        itemPointDetailBinding.textPointDetailContents.text = mPointHistory!!.subject
        binding.layoutBolHistoryItem.addView(itemPointDetailBinding.root)

        val entries = mPointHistory!!.historyProp?.entries

        if (entries != null) {
            for (entry in entries) {
                val itemPointDetailBinding = ItemPointDetailBinding.inflate(layoutInflater)
                itemPointDetailBinding.textPointDetailTitle.text = entry.key
                itemPointDetailBinding.textPointDetailContents.text = entry.value
                binding.layoutBolHistoryItem.addView(itemPointDetailBinding.root)
            }
        }
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_point_config), ToolbarOption.ToolbarMenu.LEFT)
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
