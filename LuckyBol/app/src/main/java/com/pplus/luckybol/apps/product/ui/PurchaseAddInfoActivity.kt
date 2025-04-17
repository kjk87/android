package com.pplus.luckybol.apps.product.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.code.common.EnumData
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.ProductAuth
import com.pplus.luckybol.core.network.model.dto.ProductInfo
import com.pplus.luckybol.core.network.model.dto.ProductPrice
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.databinding.ActivityPurchaseAddInfoBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call

class PurchaseAddInfoActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityPurchaseAddInfoBinding

    override fun getLayoutView(): View {
        binding = ActivityPurchaseAddInfoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    var mProductPrice:ProductPrice? = null
    override fun initializeView(savedInstanceState: Bundle?) {
        mProductPrice = intent.getParcelableExtra(Const.PRODUCT_PRICE)
        if(mProductPrice!!.product!!.imageList != null && mProductPrice!!.product!!.imageList!!.isNotEmpty()){
            Glide.with(this).load(mProductPrice!!.product!!.imageList!![0].image).apply(RequestOptions().centerCrop().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(binding.imagePurchaseAddInfoProductImage)
        }

        binding.textPurchaseAddInfoProductName.text = mProductPrice!!.product!!.name
        binding.textPurchaseAddInfoProductPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(mProductPrice!!.price!!.toInt().toString()))

        when(mProductPrice!!.productDelivery!!.method){
            1->{
                binding.textPurchaseAddInfoDeliveryMethod.text = getString(R.string.word_delivery)
            }
            2->{
                binding.textPurchaseAddInfoDeliveryMethod.text = getString(R.string.word_direct_delivery)
            }
        }

        when (mProductPrice!!.productDelivery!!.type) { // 1:무료, 2:유료, 3:조건부 무료
            EnumData.DeliveryType.free.type -> {
                binding.textPurchaseAddInfoDeliveryFee.text = getString(R.string.word_free_ship)
                binding.textPurchaseAddInfoDeliveryMinPrice.visibility = View.GONE
            }
            EnumData.DeliveryType.pay.type -> {
                if (mProductPrice!!.productDelivery!!.deliveryFee!! > 0) {
                    binding.textPurchaseAddInfoDeliveryFee.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(mProductPrice!!.productDelivery!!.deliveryFee!!.toInt().toString()))
                    binding.textPurchaseAddInfoDeliveryMinPrice.visibility = View.GONE
                } else {
                    binding.textPurchaseAddInfoDeliveryFee.text = getString(R.string.word_free_ship)
                }
            }
            EnumData.DeliveryType.conditionPay.type -> {
                if (mProductPrice!!.productDelivery!!.deliveryFee!! > 0) {
                    binding.textPurchaseAddInfoDeliveryFee.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(mProductPrice!!.productDelivery!!.deliveryFee!!.toInt().toString()))
                    if (mProductPrice!!.productDelivery!!.deliveryMinPrice != null && mProductPrice!!.productDelivery!!.deliveryMinPrice!! > 0) {
                        binding.textPurchaseAddInfoDeliveryMinPrice.text = getString(R.string.format_delivery_min_price, FormatUtil.getMoneyType(mProductPrice!!.productDelivery!!.deliveryMinPrice!!.toInt().toString()))
                        binding.textPurchaseAddInfoDeliveryMinPrice.visibility = View.VISIBLE
                    } else {
                        binding.textPurchaseAddInfoDeliveryMinPrice.visibility = View.GONE
                    }
                }
            }
        }
        if (mProductPrice!!.productDelivery!!.isAddFee != null && mProductPrice!!.productDelivery!!.isAddFee!!) {
            if (mProductPrice!!.productDelivery!!.deliveryAddFee1 != null && mProductPrice!!.productDelivery!!.deliveryAddFee1!! > 0) {
                binding.textPurchaseAddInfoDeliveryAddFee1.visibility = View.VISIBLE
                binding.textPurchaseAddInfoDeliveryAddFee1.text = getString(R.string.format_delivery_add_fee1, FormatUtil.getMoneyType(mProductPrice!!.productDelivery!!.deliveryAddFee1!!.toInt().toString()))
            } else {
                binding.textPurchaseAddInfoDeliveryAddFee1.visibility = View.GONE
            }

            if (mProductPrice!!.productDelivery!!.deliveryAddFee2 != null && mProductPrice!!.productDelivery!!.deliveryAddFee2!! > 0) {
                binding.textPurchaseAddInfoDeliveryAddFee2.visibility = View.VISIBLE
                binding.textPurchaseAddInfoDeliveryAddFee2.text = getString(R.string.format_delivery_add_fee2, FormatUtil.getMoneyType(mProductPrice!!.productDelivery!!.deliveryAddFee2!!.toInt().toString()))
            } else {
                binding.textPurchaseAddInfoDeliveryAddFee2.visibility = View.GONE
            }
        } else {
            binding.textPurchaseAddInfoDeliveryAddFee1.visibility = View.GONE
            binding.textPurchaseAddInfoDeliveryAddFee2.visibility = View.GONE
        }

        when(mProductPrice!!.productDelivery!!.paymentMethod){
            "before"->{
                binding.textPurchaseAddInfoDeliveryPayMethod.text = getString(R.string.word_before_pay_delivery_fee)
            }
            "after"->{
                binding.textPurchaseAddInfoDeliveryPayMethod.text = getString(R.string.word_after_pay_delivery_fee)
            }
        }

        var params = HashMap<String, String>()
        params["productSeqNo"] = mProductPrice!!.product!!.seqNo.toString()
        ApiBuilder.create().getProductInfoByProductSeqNo(params).setCallback(object : PplusCallback<NewResultResponse<ProductInfo>> {
            override fun onResponse(call: Call<NewResultResponse<ProductInfo>>?,
                                    response: NewResultResponse<ProductInfo>?) {
                if (response?.data != null) {
                    val productInfo = response.data!!

                    if(StringUtils.isNotEmpty(productInfo.model)){
                        binding.textPurchaseAddInfoModelName.text = productInfo.model
                        binding.layoutPurchaseAddInfoModelName.visibility = View.VISIBLE
                    }else{
                        binding.layoutPurchaseAddInfoModelName.visibility = View.GONE
                    }

                    if(StringUtils.isNotEmpty(productInfo.modelCode)){
                        binding.textPurchaseAddInfoModelCode.text = productInfo.modelCode
                        binding.layoutPurchaseAddInfoModelCode.visibility = View.VISIBLE
                    }else{
                        binding.layoutPurchaseAddInfoModelCode.visibility = View.GONE
                    }

                    if(StringUtils.isNotEmpty(productInfo.brand)){
                        binding.textPurchaseAddInfoBrandName.text = productInfo.brand
                        binding.layoutPurchaseAddInfoBrandName.visibility = View.VISIBLE
                    }else{
                        binding.layoutPurchaseAddInfoBrandName.visibility = View.GONE
                    }

                    if(StringUtils.isNotEmpty(productInfo.menufacturer)){
                        binding.textPurchaseAddInfoManufacturerName.text = productInfo.menufacturer
                        binding.layoutPurchaseAddInfoManufacturerName.visibility = View.VISIBLE
                    }else{
                        binding.layoutPurchaseAddInfoManufacturerName.visibility = View.GONE
                    }

                    if(StringUtils.isNotEmpty(productInfo.origin)){
                        binding.textPurchaseAddInfoOrigin.text = productInfo.origin
                        binding.layoutPurchaseAddInfoOrigin.visibility = View.VISIBLE
                    }else{
                        binding.layoutPurchaseAddInfoOrigin.visibility = View.GONE
                    }

                    if(StringUtils.isNotEmpty(productInfo.menufacturedDate)){
                        binding.textPurchaseAddInfoManufactureDate.text = productInfo.menufacturedDate
                        binding.layoutPurchaseAddInfoManufactureDate.visibility = View.VISIBLE
                    }else{
                        binding.layoutPurchaseAddInfoManufactureDate.visibility = View.GONE
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<ProductInfo>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<ProductInfo>?) {

            }
        }).build().call()

        binding.textPurchaseAddInfoAsMent.text = mProductPrice!!.productDelivery!!.asMent
        binding.textPurchaseAddInfoAsTel.text = FormatUtil.getPhoneNumber(mProductPrice!!.productDelivery!!.asTel!!.replace("-", ""))
        binding.textPurchaseAddInfoAsTel.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${mProductPrice!!.productDelivery!!.asTel}"))
            startActivity(intent)
        }

        params = HashMap<String, String>()
        params["productSeqNo"] = mProductPrice!!.product!!.seqNo.toString()
        ApiBuilder.create().getProductAuthByProductSeqNo(params).setCallback(object : PplusCallback<NewResultResponse<ProductAuth>> {
            override fun onResponse(call: Call<NewResultResponse<ProductAuth>>?,
                                    response: NewResultResponse<ProductAuth>?) {
                if(response?.data != null && StringUtils.isNotEmpty(response.data!!.type) && StringUtils.isNotEmpty(response.data!!.authNo)){
                    binding.layoutPurchaseAddInfoAuthInfo.visibility = View.VISIBLE
                    binding.textPurchaseAddInfoAuthInfo.text = "${response.data!!.type}/${response.data!!.authNo}"
                }else{
                    binding.layoutPurchaseAddInfoAuthInfo.visibility = View.GONE
                }

            }

            override fun onFailure(call: Call<NewResultResponse<ProductAuth>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<ProductAuth>?) {

            }
        }).build().call()

    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_purchase_add_info), ToolbarOption.ToolbarMenu.LEFT)
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