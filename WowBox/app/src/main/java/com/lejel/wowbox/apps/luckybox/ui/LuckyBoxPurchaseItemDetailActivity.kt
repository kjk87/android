package com.lejel.wowbox.apps.luckybox.ui

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.LuckyBoxPurchaseItem
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityLuckyBoxPurchaseItemDetailBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import com.pplus.utils.part.utils.time.DateFormatUtils
import retrofit2.Call
import java.text.SimpleDateFormat

class LuckyBoxPurchaseItemDetailActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityLuckyBoxPurchaseItemDetailBinding

    override fun getLayoutView(): View {
        binding = ActivityLuckyBoxPurchaseItemDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    var mLuckyBoxPurchaseItem: LuckyBoxPurchaseItem? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        mLuckyBoxPurchaseItem = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, LuckyBoxPurchaseItem::class.java)

        //        text_purchase_product_shipping_history_detail_desc.text = PplusCommonUtil.fromHtml(getString(R.string.html_purchase_point_desc))
        getLuckyBoxPurchaseItem()
    }

    private fun getLuckyBoxPurchaseItem() {
        showProgress("")
        ApiBuilder.create().getLuckyBoxPurchaseItem(mLuckyBoxPurchaseItem!!.seqNo!!).setCallback(object : PplusCallback<NewResultResponse<LuckyBoxPurchaseItem>> {
            override fun onResponse(call: Call<NewResultResponse<LuckyBoxPurchaseItem>>?,
                                    response: NewResultResponse<LuckyBoxPurchaseItem>?) {
                hideProgress()
                if (response?.result != null) {
                    mLuckyBoxPurchaseItem = response.result
                    setData()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<LuckyBoxPurchaseItem>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<LuckyBoxPurchaseItem>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun setData() {
        val delivery = mLuckyBoxPurchaseItem!!.luckyboxDelivery!!
        var date = ""
        val output = SimpleDateFormat("yyyy.MM.dd HH:mm")
        binding.textLuckyBoxPurchaseItemDetailDeliveryStartDate.visibility = View.GONE
        binding.textBoxPurchaseItemDetailSearchDelivery.visibility = View.GONE
        binding.layoutLuckyBoxPurchaseItemDetailShippingCompany.visibility = View.GONE
        when (mLuckyBoxPurchaseItem!!.deliveryStatus) { // 1:배송대기(배송신청), 2:배송중, 3:배송완료
            0 -> {
                binding.textLuckyBoxPurchaseItemDetailDeliveryStatus.setText(R.string.word_preparing_goods)

                if (StringUtils.isNotEmpty(mLuckyBoxPurchaseItem!!.luckyboxDeliveryPurchase!!.paymentDatetime)) {
                    binding.textLuckyBoxPurchaseItemDetailDeliveryStartDate.visibility = View.VISIBLE
                    val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mLuckyBoxPurchaseItem!!.luckyboxDeliveryPurchase!!.paymentDatetime)
                    binding.textLuckyBoxPurchaseItemDetailDeliveryStartDate.text = output.format(d)
                }
            }
            1 -> {
                binding.textLuckyBoxPurchaseItemDetailDeliveryStatus.setText(R.string.word_shipping_ready)

                if (StringUtils.isNotEmpty(mLuckyBoxPurchaseItem!!.luckyboxDeliveryPurchase!!.paymentDatetime)) {
                    binding.textLuckyBoxPurchaseItemDetailDeliveryStartDate.visibility = View.VISIBLE
                    val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mLuckyBoxPurchaseItem!!.luckyboxDeliveryPurchase!!.paymentDatetime)
                    binding.textLuckyBoxPurchaseItemDetailDeliveryStartDate.text = output.format(d)
                }
            }
            2 -> {
                binding.textLuckyBoxPurchaseItemDetailDeliveryStartDate.visibility = View.VISIBLE
                binding.textLuckyBoxPurchaseItemDetailDeliveryStatus.setText(R.string.word_shipping)
                binding.layoutLuckyBoxPurchaseItemDetailShippingCompany.visibility = View.VISIBLE
                binding.textLuckyBoxPurchaseItemDetailShippingCompany.text = delivery.shippingCompany
                binding.textLuckyBoxPurchaseItemDetailShippingNumber.text = delivery.transportNumber
//                binding.textBoxPurchaseItemDetailSearchDelivery.visibility = View.VISIBLE
//                binding.textBoxPurchaseItemDetailSearchDelivery.setOnClickListener {
//                    PplusCommonUtil.openChromeWebView(this, "https://m.search.naver.com/search.naver?query=${delivery.shippingCompany}+${delivery.transportNumber}")
//                }
                if (StringUtils.isNotEmpty(delivery.deliveryStartDatetime)) {
                    val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(delivery.deliveryStartDatetime)
                    binding.textLuckyBoxPurchaseItemDetailDeliveryStartDate.text = output.format(d)
                }
            }
            3 -> {
                binding.textLuckyBoxPurchaseItemDetailDeliveryStartDate.visibility = View.VISIBLE
                binding.textLuckyBoxPurchaseItemDetailDeliveryStatus.setText(R.string.word_shipping_complete)
                if (StringUtils.isNotEmpty(delivery.deliveryCompleteDatetime)) {
                    val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(delivery.deliveryCompleteDatetime)
                    binding.textLuckyBoxPurchaseItemDetailDeliveryStartDate.text = output.format(d)
                }

            }
        }

        Glide.with(this).load(mLuckyBoxPurchaseItem!!.productImage).apply(RequestOptions().fitCenter()).into(binding.imageLuckyBoxPurchaseItemDetailProductImage)
        binding.textLuckyBoxPurchaseItemDetailProductName.text = mLuckyBoxPurchaseItem!!.productName
        binding.textLuckyBoxPurchaseItemDetailProductOption.text = mLuckyBoxPurchaseItem!!.optionName
        binding.textLuckyBoxPurchaseItemDetailProductPrice.text = getString(R.string.format_origin_price, FormatUtil.getMoneyTypeFloat(mLuckyBoxPurchaseItem!!.productPrice.toString()))


        if (StringUtils.isNotEmpty(mLuckyBoxPurchaseItem!!.luckyboxDeliveryPurchase!!.paymentDatetime)) {
            val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mLuckyBoxPurchaseItem!!.luckyboxDeliveryPurchase!!.paymentDatetime)
            date = output.format(d)
        }
        binding.textLuckyBoxPurchaseItemDetailPayDate.text = date
        binding.textLuckyBoxPurchaseItemDetailPayMethod.text = mLuckyBoxPurchaseItem!!.luckyboxDeliveryPurchase!!.paymentMethod
        when (mLuckyBoxPurchaseItem!!.luckyboxDeliveryPurchase!!.paymentMethod) {
            "point" -> {
                binding.textLuckyBoxPurchaseItemDetailPayMethod.text = getString(R.string.word_point_pay)
            }
            else->{
                binding.textLuckyBoxPurchaseItemDetailPayMethod.text = mLuckyBoxPurchaseItem!!.luckyboxDeliveryPurchase!!.paymentMethod
            }
        }


        val deliveryFee = mLuckyBoxPurchaseItem!!.deliveryFee

        if (deliveryFee != null && deliveryFee > 0) {
            binding.textLuckyBoxPurchaseItemDetailDeliveryFee.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(deliveryFee.toInt().toString()))
        } else {
            binding.textLuckyBoxPurchaseItemDetailDeliveryFee.text = getString(R.string.word_free_ship)
        }

        if(mLuckyBoxPurchaseItem!!.luckyboxDeliveryPurchase!!.usePoint != null && mLuckyBoxPurchaseItem!!.luckyboxDeliveryPurchase!!.usePoint!! > 0){
            binding.layoutLuckyBoxPurchaseItemDetailUsePoint.visibility = View.VISIBLE
            binding.textLuckyBoxPurchaseItemDetailUsePoint.text = "- ${getString(R.string.format_money_unit, FormatUtil.getMoneyTypeFloat(mLuckyBoxPurchaseItem!!.luckyboxDeliveryPurchase!!.usePoint.toString()))}"
        }else{
            binding.layoutLuckyBoxPurchaseItemDetailUsePoint.visibility = View.GONE
        }

        binding.textLuckyBoxPurchaseItemDetailTotalPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyTypeFloat(mLuckyBoxPurchaseItem!!.luckyboxDeliveryPurchase!!.pgPrice.toString()))

        binding.textLuckyBoxPurchaseItemDetailReceiverName.text = delivery.receiverName
        binding.textLuckyBoxPurchaseItemDetailReceiverFamilyName.text = delivery.receiverFamilyName
        binding.textLuckyBoxPurchaseItemDetailReceiverAddress.text = delivery.receiverAddress
        binding.textLuckyBoxPurchaseItemDetailReceiverAddress2.text = delivery.receiverAddress2
        binding.textLuckyBoxPurchaseItemDetailReceiverProvinsi.text = delivery.receiverProvinsi
        binding.textLuckyBoxPurchaseItemDetailReceiverKabkota.text = delivery.receiverKabkota
        binding.textLuckyBoxPurchaseItemDetailReceiverKecamatan.text = delivery.receiverKecamatan
        binding.textLuckyBoxPurchaseItemDetailReceiverPostCode.text = delivery.receiverPostCode
        binding.textLuckyBoxPurchaseItemDetailReceiverTel.text = delivery.receiverTel

    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_shipping_history_detail), ToolbarOption.ToolbarMenu.LEFT)
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
