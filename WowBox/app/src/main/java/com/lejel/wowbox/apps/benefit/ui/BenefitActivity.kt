package com.lejel.wowbox.apps.benefit.ui

import android.os.Bundle
import android.view.View
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.Benefit
import com.lejel.wowbox.core.network.model.dto.Config
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.databinding.ActivityBenefitBinding
import retrofit2.Call

class BenefitActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityBenefitBinding

    override fun getLayoutView(): View {
        binding = ActivityBenefitBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        binding.textBenefitDailyTab.setOnClickListener {
            binding.textBenefitDailyTab.isSelected = true
            binding.textBenefitCalculateTab.isSelected = false
            historyBenefit()

        }
        binding.textBenefitCalculateTab.setOnClickListener {
            binding.textBenefitDailyTab.isSelected = false
            binding.textBenefitCalculateTab.isSelected = true
            benefitCalculate()
        }

        getBenefit()
    }

    private fun getBenefit(){
        showProgress("")
        ApiBuilder.create().getBenefit().setCallback(object : PplusCallback<NewResultResponse<Benefit>>{
            override fun onResponse(call: Call<NewResultResponse<Benefit>>?, response: NewResultResponse<Benefit>?) {
                hideProgress()
                if(response?.result != null){
                    val benefit = response.result!!
                    binding.textBenefitTotalBenefit.text = FormatUtil.getMoneyTypeFloat(benefit.totalBenefit.toString())
                    getTotalBuff(benefit.buff!!)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Benefit>>?, t: Throwable?, response: NewResultResponse<Benefit>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun getTotalBuff(buff:Double){
        showProgress("")
        ApiBuilder.create().getConfig("benefitMBuff").setCallback(object : PplusCallback<NewResultResponse<Config>>{
            override fun onResponse(call: Call<NewResultResponse<Config>>?, response: NewResultResponse<Config>?) {
                hideProgress()
                if(response?.result != null){
                    val totalBuff = response.result!!.config!!.toDouble()
                    val percent = (buff / totalBuff)*100
                    binding.textBenefitRetentionBuff.text = "${FormatUtil.getMoneyTypeFloat(buff.toString())}(${FormatUtil.getMoneyTypeFloat(percent.toString())}%)"

                    binding.textBenefitDailyTab.isSelected = true
                    binding.textBenefitCalculateTab.isSelected = false
                    historyBenefit()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Config>>?, t: Throwable?, response: NewResultResponse<Config>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun historyBenefit() {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.benefit_container, HistoryBenefitFragment.newInstance(), HistoryBenefitFragment::class.java.simpleName)
        ft.commit()
    }

    private fun benefitCalculate(){
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.benefit_container, BenefitCalculateFragment.newInstance(), BenefitCalculateFragment::class.java.simpleName)
        ft.commit()
    }


    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar("", ToolbarOption.ToolbarMenu.RIGHT)
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