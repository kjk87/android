package com.pplus.prnumberuser.apps.page.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.BusinessLicense
import com.pplus.prnumberuser.core.network.model.dto.Page2
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.databinding.ActivityPageInfoBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*

class PageInfoActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityPageInfoBinding

    override fun getLayoutView(): View {
        binding = ActivityPageInfoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val page = intent.getParcelableExtra<Page2>(Const.DATA)

        setTitle(page!!.name)

        if(StringUtils.isNotEmpty(page.introduction)){
            binding.textPageIntroduction.visibility = View.VISIBLE
            binding.textPageIntroduction.text = page.introduction
        }else{
            binding.textPageIntroduction.visibility = View.GONE
        }


        //1:매일(일~토 7개) 2:평일/주말(일~토 7개) 3:요일별(일~토 7개) 4:24시간영업
        val dayOfWeeks = resources.getStringArray(R.array.day_of_week)
        when(page.businessHoursType){
            1->{
                binding.textPageBusinessHours.text = getString(R.string.format_daily_time, page.pageBusinessHoursList!![0].openTime!!.substring(0, 5), page.pageBusinessHoursList!![0].closeTime!!.substring(0, 5))
            }
            2->{
                val holidayOpenTime = page.pageBusinessHoursList!![0].openTime!!.substring(0, 5)
                val holidayCloseTime = page.pageBusinessHoursList!![0].closeTime!!.substring(0, 5)
                val openTime = page.pageBusinessHoursList!![1].openTime!!.substring(0, 5)
                val closeTime = page.pageBusinessHoursList!![1].closeTime!!.substring(0, 5)
                binding.textPageBusinessHours.text = getString(R.string.format_week_day_end_time, holidayOpenTime, holidayCloseTime, openTime, closeTime)
            }
            3->{
                var businessTime = ""
                for((i, businessHours) in page.pageBusinessHoursList!!.withIndex()){
                    if(i != 0){
                        businessTime += "\n"
                    }
                    businessTime += getString(R.string.format_day_of_week_time, dayOfWeeks[businessHours.day!!-1], businessHours.openTime!!.substring(0, 5), businessHours.closeTime!!.substring(0, 5))
                }
                binding.textPageBusinessHours.text = businessTime
            }
            4->{
                binding.textPageBusinessHours.text = getString(R.string.word_daily_24hour)
            }
        }

        if(page.pageDayOffList != null && page.pageDayOffList!!.isNotEmpty()){
            var dayOff = ""
            for((i, pageDayOff) in page.pageDayOffList!!.withIndex()){
                if(i != 0){
                    dayOff += "\n"
                }
                when(pageDayOff.week){
                    0->{
                        dayOff += getString(R.string.format_weekly_day_off, dayOfWeeks[pageDayOff.day!!])
                    }
                    else->{
                        dayOff += getString(R.string.format_day_off, pageDayOff.week.toString(), dayOfWeeks[pageDayOff.day!!])
                    }
                }
            }

            binding.textPageDayOff.text = dayOff
        }else{
            binding.textPageDayOff.text = getString(R.string.word_none_day_off)
        }

        if(page.pageTimeOffList != null && page.pageTimeOffList!!.isNotEmpty()){
            var timeOff = ""
            for((i, pageTimeOff) in page.pageTimeOffList!!.withIndex()){
                if(i != 0){
                    timeOff += "\n"
                }

                timeOff += getString(R.string.format_time, pageTimeOff.start!!.substring(0, 5), pageTimeOff.end!!.substring(0, 5))

            }

            binding.textPageTimeOff.text = timeOff
        }else{
            binding.textPageTimeOff.text = getString(R.string.word_none)
        }

        binding.textPageAddress.text = "${page.roadAddress} ${page.roadDetailAddress}"
        binding.textPageViewMap.setOnClickListener {
            val intent = Intent(this, LocationPageActivity::class.java)
            intent.putExtra(Const.PAGE2, page)
            startActivity(intent)
        }


        if(StringUtils.isNotEmpty(page.orderInfo)){
            binding.layoutPageOrderInfo.visibility = View.VISIBLE
            binding.textPageOrderInfo.text = page.orderInfo
        }else{
            binding.layoutPageOrderInfo.visibility = View.GONE
        }

        binding.textPageOrderMinPrice.text = getString(R.string.format_over_money_unit, FormatUtil.getMoneyType(page.minOrderPrice!!.toInt().toString()))
        binding.textPageDeliveryPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(page.riderFee!!.toInt().toString()))

        if(page.riderFreePrice != null && page.riderFreePrice!! > 0){
            binding.layoutPageFreeDeliveryInfo.visibility = View.VISIBLE
            binding.textPageFreeDeliveryPrice.text = getString(R.string.format_over_money_unit, FormatUtil.getMoneyType(page.riderFreePrice!!.toInt().toString()))
        }else{
            binding.layoutPageFreeDeliveryInfo.visibility = View.GONE
        }

        binding.textPageOrigin.text = page.origin

        getBusinessLicense(page)
    }

    private fun getBusinessLicense(page:Page2) {
        val params = HashMap<String, String>()
        params["pageSeqNo"] = page.seqNo.toString()
        showProgress("")
        ApiBuilder.create().getBusinessLicense(params).setCallback(object : PplusCallback<NewResultResponse<BusinessLicense>> {
            override fun onResponse(call: Call<NewResultResponse<BusinessLicense>>?, response: NewResultResponse<BusinessLicense>?) {
                hideProgress()
                if (response?.data != null) {
                    val businessLicense = response.data!!

                    binding.textPageCompanyName.text = businessLicense.companyName
                    binding.textPageCompanyNumber.text = businessLicense.corporateNumber
                }
            }

            override fun onFailure(call: Call<NewResultResponse<BusinessLicense>>?, t: Throwable?, response: NewResultResponse<BusinessLicense>?) {
                hideProgress()
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_store_info3), ToolbarOption.ToolbarMenu.LEFT)

        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressed()
                    }
                }
            }
        }
    }
}