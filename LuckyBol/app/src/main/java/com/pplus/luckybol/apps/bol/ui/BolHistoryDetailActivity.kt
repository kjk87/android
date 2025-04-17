package com.pplus.luckybol.apps.bol.ui

import android.os.Bundle
import android.view.View
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.code.common.EnumData
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.Bol
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.databinding.ActivityBolHistoryDetailBinding
import com.pplus.luckybol.databinding.ItemPointDetailBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.time.DateFormatUtils
import retrofit2.Call
import java.text.SimpleDateFormat

class BolHistoryDetailActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityBolHistoryDetailBinding

    override fun getLayoutView(): View {
        binding = ActivityBolHistoryDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    var mBol: Bol? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        mBol = intent.getParcelableExtra(Const.DATA)

        binding.textBolHistoryMobileGiftHistoryDescription.visibility = View.GONE
        getHistory()
    }

    fun getHistory() {
        val params = HashMap<String, String>()
        params["no"] = mBol!!.no.toString()
        showProgress("")
        ApiBuilder.create().getBolHistoryWithTarget(params).setCallback(object : PplusCallback<NewResultResponse<Bol>> {
            override fun onResponse(call: Call<NewResultResponse<Bol>>?, response: NewResultResponse<Bol>?) {
                hideProgress()
                if (isFinishing) {
                    return
                }

                mBol = response!!.data
                if(mBol != null){
                    setData()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Bol>>?, t: Throwable?, response: NewResultResponse<Bol>?) {
                hideProgress()
            }
        }).build().call()
    }

    fun setData() {
        when (mBol!!.primaryType) {

            "decrease" -> {
                //사용
                setTitle(getString(R.string.word_use_history_detail))
                binding.textBolHistoryDetailTitle1.setText(R.string.word_use_bol)
                if(mBol!!.secondaryType == EnumData.BolType.exchange.name){
                    binding.textBolHistoryDetailTitle2.setText(R.string.word_req_date)
                }else{
                    binding.textBolHistoryDetailTitle2.setText(R.string.word_use_date)
                }
                binding.textBolHistoryDetailContents1.setTextColor(ResourceUtil.getColor(this, R.color.color_6e7780))
            }
            "increase" -> {
                //적립
                setTitle(getString(R.string.word_charge_history_detail))
                binding.textBolHistoryDetailTitle1.setText(R.string.word_reward_bol)
                binding.textBolHistoryDetailTitle2.setText(R.string.word_earn_date)
                binding.textBolHistoryDetailContents1.setTextColor(ResourceUtil.getColor(this, R.color.color_373c42))
            }
        }

        binding.textBolHistoryDetailContents1.text = getString(R.string.format_bol_unit, FormatUtil.getMoneyTypeFloat(mBol!!.amount))
        try {
            val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mBol!!.regDate)

            val output = SimpleDateFormat("yyyy.MM.dd")
            binding.textBolHistoryDetailContents2.text = output.format(d)

        } catch (e: Exception) {
            binding.textBolHistoryDetailContents2.text = ""
        }

        val entries = mBol!!.properties?.entries

        if (entries != null) {
            for (entry in entries) {
                val itemPointDetailBinding = ItemPointDetailBinding.inflate(layoutInflater)
                itemPointDetailBinding.textPointDetailTitle.text = entry.key
                itemPointDetailBinding.textPointDetailContents.text = entry.value
                binding.layoutBolHistoryItem.addView(itemPointDetailBinding.root)
            }
        }

        when (mBol!!.secondaryType!!) {
            EnumData.BolType.purchaseMobileGift.name -> {
                binding.textBolHistoryMobileGiftHistoryDescription.visibility = View.VISIBLE
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
