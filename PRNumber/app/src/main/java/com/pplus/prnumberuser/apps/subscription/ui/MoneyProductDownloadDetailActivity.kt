package com.pplus.prnumberuser.apps.subscription.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.stamp.ui.StampMoneyProductUseActivity
import com.pplus.prnumberuser.apps.subscription.data.MoneyProductLogAdapter
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.ProductPrice
import com.pplus.prnumberuser.core.network.model.dto.SubscriptionDownload
import com.pplus.prnumberuser.core.network.model.dto.SubscriptionLog
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivityMoneyProductDownloadDetailBinding
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import com.pplus.utils.part.utils.time.DateFormatUtils
import retrofit2.Call
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.*

class MoneyProductDownloadDetailActivity : BaseActivity(), ImplToolbar {
    private lateinit var binding: ActivityMoneyProductDownloadDetailBinding

    override fun getLayoutView(): View {
        binding = ActivityMoneyProductDownloadDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    var mSubscriptionDownload: SubscriptionDownload? = null
    var mAdapter : MoneyProductLogAdapter? = null


    override fun initializeView(savedInstanceState: Bundle?) {
        mSubscriptionDownload = intent.getParcelableExtra(Const.DATA)
        mAdapter = MoneyProductLogAdapter()
        binding.recyclerMoneyProductDownloadDetailLog.layoutManager = LinearLayoutManager(this)
        binding.recyclerMoneyProductDownloadDetailLog.adapter = mAdapter

        getSubscriptionDownload()
    }

    private fun getSubscriptionDownload(){
        val params = HashMap<String, String>()
        params["seqNo"] = mSubscriptionDownload!!.seqNo.toString()
        params["sort"] = "seqNo,desc"
        showProgress("")
        ApiBuilder.create().getSubscriptionDownloadBySeqNo(params).setCallback(object : PplusCallback<NewResultResponse<SubscriptionDownload>> {
            override fun onResponse(call: Call<NewResultResponse<SubscriptionDownload>>?, response: NewResultResponse<SubscriptionDownload>?) {
                hideProgress()
                if (response?.data != null) {
                    mSubscriptionDownload = response.data!!
                    setSubscription(mSubscriptionDownload!!.productPrice!!)

                    val currentDate = LocalDate.now()
                    val expireDate = LocalDate.parse(mSubscriptionDownload!!.expireDate)
                    val remainDay = ChronoUnit.DAYS.between(currentDate, expireDate)
                    binding.textMoneyProductDownloadDetailExpireDate.text = PplusCommonUtil.fromHtml(getString(R.string.html_subscription_remain_day, FormatUtil.getMoneyType(mSubscriptionDownload!!.expireDate), FormatUtil.getMoneyType(remainDay.toString())))
                    binding.textMoneyProductDownloadDetailContents.text = mSubscriptionDownload!!.useCondition
                    binding.textMoneyProductDownloadDetailUse.visibility = View.GONE

                    var remainPrice = mSubscriptionDownload!!.havePrice!! - mSubscriptionDownload!!.usePrice!!
                    if(remainPrice < 0){
                        remainPrice = 0
                    }
                    binding.textMoneyProductDownloadDetailRemainPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(remainPrice.toString()))

                    if(remainPrice == 0){
                        binding.layoutMoneyProductDownloadDetailUsePrice.visibility = View.GONE
                        binding.textMoneyProductDownloadDetailRemainPriceAfterUse.visibility = View.GONE
                    }

                    val watcher = object : TextWatcher {
                        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                        override fun afterTextChanged(p0: Editable?) {
                            if(p0!!.isNotEmpty()){
                                var usePrice = p0.toString().toInt()
                                if(usePrice == 0){
                                    binding.editMoneyProductDownloadDetailUsePrice.removeTextChangedListener(this)
                                    binding.editMoneyProductDownloadDetailUsePrice.setText("")
                                    binding.editMoneyProductDownloadDetailUsePrice.addTextChangedListener(this)
                                }else{
                                    val remainPrice = mSubscriptionDownload!!.havePrice!! - mSubscriptionDownload!!.usePrice!!
                                    if(usePrice > remainPrice){
                                        usePrice = remainPrice
                                        binding.editMoneyProductDownloadDetailUsePrice.removeTextChangedListener(this)
                                        binding.editMoneyProductDownloadDetailUsePrice.setText(usePrice.toString())
                                        binding.editMoneyProductDownloadDetailUsePrice.addTextChangedListener(this)
                                    }
                                    binding.textMoneyProductDownloadDetailRemainPriceAfterUse.text = PplusCommonUtil.fromHtml(getString(R.string.html_remain_price_after_use, FormatUtil.getMoneyType((remainPrice - usePrice).toString())))
                                }
                            }else{
                                val remainPrice = mSubscriptionDownload!!.havePrice!! - mSubscriptionDownload!!.usePrice!!
                                binding.textMoneyProductDownloadDetailRemainPriceAfterUse.text = PplusCommonUtil.fromHtml(getString(R.string.html_remain_price_after_use, FormatUtil.getMoneyType((remainPrice).toString())))
                            }
                        }
                    }

                    binding.editMoneyProductDownloadDetailUsePrice.setText("")
                    binding.textMoneyProductDownloadDetailRemainPriceAfterUse.text = PplusCommonUtil.fromHtml(getString(R.string.html_remain_price_after_use, FormatUtil.getMoneyType((remainPrice).toString())))
                    binding.editMoneyProductDownloadDetailUsePrice.addTextChangedListener(watcher)

                    when(mSubscriptionDownload!!.status){ // 1:사용중, 2:사용완료, 3:기간만료
                        1->{
                            binding.textMoneyProductDownloadDetailStatus.visibility = View.GONE
                            binding.layoutMoneyProductDownloadDetailComplete.visibility = View.GONE
                            binding.textMoneyProductDownloadDetailUse.visibility = View.VISIBLE
                            binding.textMoneyProductDownloadDetailUse.setOnClickListener {

                                val usePrice = binding.editMoneyProductDownloadDetailUsePrice.text.toString().trim()
                                if(StringUtils.isEmpty(usePrice) || usePrice.toInt() <= 0){
                                    showAlert(R.string.msg_input_use_price)
                                    return@setOnClickListener
                                }

                                val page = mSubscriptionDownload!!.productPrice!!.page!!

                                if(StringUtils.isNotEmpty(page.echossId)){
                                    val intent = Intent(this@MoneyProductDownloadDetailActivity, StampMoneyProductUseActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    intent.putExtra(Const.SUBSCRIPTION_DOWNLOAD, mSubscriptionDownload)
                                    intent.putExtra(Const.PAGE, page)
                                    intent.putExtra(Const.PRICE, usePrice.toInt())
                                    defaultLauncher.launch(intent)
                                }else{
                                    val intent = Intent(this@MoneyProductDownloadDetailActivity, CheckUseSubscriptionAuthCodeActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    intent.putExtra(Const.SUBSCRIPTION_DOWNLOAD, mSubscriptionDownload)
                                    intent.putExtra(Const.PRICE, usePrice.toInt())
                                    defaultLauncher.launch(intent)
                                }
                            }

                        }
                        2->{
                            binding.layoutMoneyProductDownloadDetailUsePrice.visibility = View.GONE
                            binding.textMoneyProductDownloadDetailRemainPriceAfterUse.visibility = View.GONE
                            binding.textMoneyProductDownloadDetailStatus.visibility = View.VISIBLE
                            binding.textMoneyProductDownloadDetailStatus.setText(R.string.word_use_complete)
                            binding.layoutMoneyProductDownloadDetailComplete.visibility = View.VISIBLE
                            val output = SimpleDateFormat("yyyy-MM-dd")
                            binding.textMoneyProductDownloadDetailCompleteDate.text = output.format(DateFormatUtils.PPLUS_DATE_FORMAT.parse(mSubscriptionDownload!!.completeDatetime))
                        }
                        3->{
                            binding.layoutMoneyProductDownloadDetailUsePrice.visibility = View.GONE
                            binding.textMoneyProductDownloadDetailRemainPriceAfterUse.visibility = View.GONE
                            binding.textMoneyProductDownloadDetailStatus.visibility = View.VISIBLE
                            binding.textMoneyProductDownloadDetailStatus.setText(R.string.word_expire)
                            binding.layoutMoneyProductDownloadDetailComplete.visibility = View.GONE

//                            val output = SimpleDateFormat("yyyy.MM.dd")
//                            holder.text_date.text = output.format(item.expireDate)
                        }
                    }
                }

                getSubscriptionLogList()
            }

            override fun onFailure(call: Call<NewResultResponse<SubscriptionDownload>>?, t: Throwable?, response: NewResultResponse<SubscriptionDownload>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun getSubscriptionLogList(){
        val params = HashMap<String, String>()
        params["subscriptionDownSeqNo"] = mSubscriptionDownload!!.seqNo.toString()
        params["sort"] = "recent"
        showProgress("")
        ApiBuilder.create().getSubscriptionLogListBySubscriptionDownloadSeqNo(params).setCallback(object : PplusCallback<NewResultResponse<SubscriptionLog>> {
            override fun onResponse(call: Call<NewResultResponse<SubscriptionLog>>?, response: NewResultResponse<SubscriptionLog>?) {
                hideProgress()
                if(response?.datas != null){
                    if(response.datas.isEmpty()){
                        binding.textMoneyProductDownloadDetailLogTitle.visibility = View.GONE
                    }else{
                        binding.textMoneyProductDownloadDetailLogTitle.visibility = View.VISIBLE
                    }
                    mAdapter!!.setDataList(response.datas!!)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<SubscriptionLog>>?, t: Throwable?, response: NewResultResponse<SubscriptionLog>?) {
                hideProgress()
            }
        }).build().call()
    }


    private fun setSubscription(item: ProductPrice){

        if(item.isSubscription != null && item.isSubscription!!){
            binding.itemMainSubscription.layoutMainSubscriptionEnd.setBackgroundResource(R.drawable.bg_4694fb_right_radius_30)
            binding.itemMainSubscription.textMainSubscriptionDiscountRatio.setTextColor(ResourceUtil.getColor(this, R.color.white))
            binding.itemMainSubscription.textMainSubscriptionDesc.setTextColor(ResourceUtil.getColor(this, R.color.color_999999))
            binding.itemMainSubscription.textMainSubscriptionDesc.text = PplusCommonUtil.fromHtml(getString(R.string.html_subscription_count, mSubscriptionDownload!!.haveCount.toString()))
        }else{
            binding.itemMainSubscription.layoutMainSubscriptionEnd.setBackgroundResource(R.drawable.bg_ffcf5c_right_radius_30)
            binding.itemMainSubscription.textMainSubscriptionDiscountRatio.setTextColor(ResourceUtil.getColor(this, R.color.color_4a3606))
            binding.itemMainSubscription.textMainSubscriptionDesc.setTextColor(ResourceUtil.getColor(this, R.color.color_4a3606))
            binding.itemMainSubscription.textMainSubscriptionDesc.text = getString(R.string.word_remain_money_manage_type)
        }

        binding.itemMainSubscription.textMainSubscriptionName.text = mSubscriptionDownload!!.name
        binding.textMoneyProductDownloadDetailPageName.text = item.page!!.name

        if (item.originPrice != null && item.originPrice!! > 0) {

            if (item.originPrice!! <= item.price!!) {
                binding.itemMainSubscription.textMainSubscriptionOriginPrice.visibility = View.GONE
            } else {
                binding.itemMainSubscription.textMainSubscriptionOriginPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.originPrice.toString()))
                binding.itemMainSubscription.textMainSubscriptionOriginPrice.visibility = View.VISIBLE
            }

        } else {
            binding.itemMainSubscription.textMainSubscriptionOriginPrice.visibility = View.GONE
        }

        //        holder.text_discount.visibility = View.GONE
        if (item.discountRatio != null && item.discountRatio!!.toInt() > 0) {
            binding.itemMainSubscription.textMainSubscriptionDiscountRatio.visibility = View.VISIBLE
            binding.itemMainSubscription.textMainSubscriptionDiscountRatio.text = PplusCommonUtil.fromHtml(getString(R.string.html_percent_unit2, item.discountRatio!!.toInt().toString()))
        } else {
            binding.itemMainSubscription.textMainSubscriptionDiscountRatio.visibility = View.GONE
        }
        binding.itemMainSubscription.textMainSubscriptionSalePrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.price.toString()))
    }

    val defaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        getSubscriptionDownload()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_retention_prepayment), ToolbarOption.ToolbarMenu.LEFT)
//        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, getString(R.string.word_use_history))
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressed()
                    } //                ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
                    //                    val logIntent = Intent(this@MoneyProductDownloadDetailActivity, MoneyProductUseLogActivity::class.java)
                    //                    logIntent.putExtra(Const.SUBSCRIPTION_DOWNLOAD, mSubscriptionDownload)
                    //                    logIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    //                    startActivity(logIntent)
                    //                }
                }
            }
        }
    }
}