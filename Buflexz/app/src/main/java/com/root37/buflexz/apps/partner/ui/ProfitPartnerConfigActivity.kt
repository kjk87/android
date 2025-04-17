package com.root37.buflexz.apps.partner.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.resource.ResourceUtil
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import com.pplus.utils.part.utils.time.DateFormatUtils
import com.root37.buflexz.Const
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.toolbar.ImplToolbar
import com.root37.buflexz.apps.common.toolbar.OnToolbarListener
import com.root37.buflexz.apps.common.toolbar.ToolbarOption
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.core.network.ApiBuilder
import com.root37.buflexz.core.network.model.dto.ProfitPartner
import com.root37.buflexz.core.network.model.response.NewResultResponse
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.ActivityProfitPartnerConfigBinding
import retrofit2.Call
import java.text.SimpleDateFormat
import java.util.Calendar

class ProfitPartnerConfigActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityProfitPartnerConfigBinding

    override fun getLayoutView(): View {
        binding = ActivityProfitPartnerConfigBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    private var mMonth = ""
    val cal = Calendar.getInstance()

    override fun initializeView(savedInstanceState: Bundle?) {
        val displayFormat = SimpleDateFormat("yyyy.MM")
        val format = SimpleDateFormat("yyyy-MM")

        val thisMonth = "${format.format(cal.time)}-01"
        cal.add(Calendar.MONTH, -1)


        mMonth = "${format.format(cal.time)}-01"
        binding.textProfitPartnerMonth.text = displayFormat.format(cal.time)


        binding.imageProfitPartnerMonthBefore.setOnClickListener {
            cal.add(Calendar.MONTH, -1)
            mMonth = "${format.format(cal.time)}-01"
            binding.textProfitPartnerMonth.text = displayFormat.format(cal.time)
            getPaterProfit()
        }

        binding.imageProfitPartnerMonthNext.setOnClickListener {
            if(mMonth != thisMonth){
                cal.add(Calendar.MONTH, 1)
                mMonth = "${format.format(cal.time)}-01"
                binding.textProfitPartnerMonth.text = displayFormat.format(cal.time)
                getPaterProfit()
            }
        }

        binding.layoutProfitPartnerConfigNotExist.visibility = View.GONE
        binding.layoutProfitPartnerConfigExist.visibility = View.GONE
        getTotalProfit()
    }

    private fun getTotalProfit(){
        showProgress("")
        ApiBuilder.create().getTotalProfit().setCallback(object : PplusCallback<NewResultResponse<Double>> {
            override fun onResponse(call: Call<NewResultResponse<Double>>?, response: NewResultResponse<Double>?) {
                hideProgress()
                if(response?.result != null){
                    binding.textProfitPartnerConfigTotalPrice.text = FormatUtil.getMoneyTypeFloat(response.result.toString())
                }else{
                    binding.textProfitPartnerConfigTotalPrice.text = "0"
                }

                getPaterProfit()
            }

            override fun onFailure(call: Call<NewResultResponse<Double>>?, t: Throwable?, response: NewResultResponse<Double>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun getPaterProfit(){
        val params = HashMap<String, String>()
        params["calculateMonth"] = mMonth

        showProgress("")
        ApiBuilder.create().getProfitPartner(params).setCallback(object :PplusCallback<NewResultResponse<ProfitPartner>>{
            override fun onResponse(call: Call<NewResultResponse<ProfitPartner>>?, response: NewResultResponse<ProfitPartner>?) {
                hideProgress()
                if(response?.result != null){
                    binding.layoutProfitPartnerConfigNotExist.visibility = View.GONE
                    binding.layoutProfitPartnerConfigExist.visibility = View.VISIBLE
                    val data = response.result!!

                    when(data.status){
                        "transfer"->{
                            binding.textProfitPartnerStatus.setText(R.string.word_profit_transfer)
                            binding.textProfitPartnerStatus.setTextColor(ResourceUtil.getColor(this@ProfitPartnerConfigActivity, R.color.color_dcdcdc))
                        }
                        "complete"->{
                            binding.textProfitPartnerStatus.setText(R.string.word_profit_complete)
                            binding.textProfitPartnerStatus.setTextColor(ResourceUtil.getColor(this@ProfitPartnerConfigActivity, R.color.color_3e78ff))
                        }
                    }

                    if(StringUtils.isNotEmpty(data.changeDatetime)){
                        binding.textProfitPartnerDate.visibility = View.VISIBLE
                        val format = SimpleDateFormat(getString(R.string.word_date_format2))
                        binding.textProfitPartnerDate.text = format.format(PplusCommonUtil.setTimeZoneOffset(DateFormatUtils.PPLUS_DATE_FORMAT.parse(data.changeDatetime)))
                    }else{
                        binding.textProfitPartnerDate.visibility = View.GONE
                    }

                    var totalProfit = 0f
                    val myProfit = data.adProfit!! + data.ballProfit!!
                    totalProfit += myProfit
                    binding.textProfitPartnerConfigMyProfit.text = getString(R.string.format_dollar_unit, FormatUtil.getMoneyTypeFloat(myProfit.toString()))
                    if(data.transferedAdProfit!! > 0 || data.transferedBallProfit!! > 0 || data.transferedBonusProfit!! > 0){
                        binding.layoutProfitPartnerConfigTransferedProfit.visibility = View.VISIBLE
                        val transferedProfit = data.transferedAdProfit!! + data.transferedBallProfit!! + data.transferedBonusProfit!!
                        binding.textProfitPartnerConfigTransferedProfit.text = getString(R.string.format_dollar_unit, FormatUtil.getMoneyTypeFloat(transferedProfit.toString()))

                        totalProfit += transferedProfit
                    }else{
                        binding.layoutProfitPartnerConfigTransferedProfit.visibility = View.GONE
                    }

                    if(data.bonusProfit!! > 0){
                        binding.layoutProfitPartnerConfigMasterProfit.visibility = View.VISIBLE
                        binding.textProfitPartnerConfigMasterProfit.text = getString(R.string.format_dollar_unit, FormatUtil.getMoneyTypeFloat(data.bonusProfit.toString()))
                        totalProfit += data.bonusProfit!!
                        binding.textProfitPartnerConfigMasterProfitDetail.setOnClickListener {
                            val intent = Intent(this@ProfitPartnerConfigActivity, BonusProfitDetailActivity::class.java)
                            intent.putExtra(Const.DATA, data)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            startActivity(intent)
                        }
                    }else{
                        binding.layoutProfitPartnerConfigMasterProfit.visibility = View.GONE
                    }

                    binding.textProfitPartnerConfigTotalProfit.text = getString(R.string.format_dollar_unit, FormatUtil.getMoneyTypeFloat(totalProfit.toString()))
                }else{
                    binding.layoutProfitPartnerConfigNotExist.visibility = View.VISIBLE
                    binding.layoutProfitPartnerConfigExist.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<NewResultResponse<ProfitPartner>>?, t: Throwable?, response: NewResultResponse<ProfitPartner>?) {
                hideProgress()
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_commission_calculate_config), ToolbarOption.ToolbarMenu.LEFT)
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