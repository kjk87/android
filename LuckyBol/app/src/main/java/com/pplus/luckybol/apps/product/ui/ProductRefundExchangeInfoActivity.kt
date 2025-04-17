package com.pplus.luckybol.apps.product.ui

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
import com.pplus.luckybol.core.network.model.dto.ProductPrice
import com.pplus.luckybol.databinding.ActivityProductRefundExchangeInfoBinding
import com.pplus.utils.part.format.FormatUtil

class ProductRefundExchangeInfoActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityProductRefundExchangeInfoBinding

    override fun getLayoutView(): View {
        binding = ActivityProductRefundExchangeInfoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val productPrice = intent.getParcelableExtra<ProductPrice>(Const.PRODUCT_PRICE)!!
        if (productPrice.product!!.imageList != null && productPrice.product!!.imageList!!.isNotEmpty()) {
            Glide.with(this).load(productPrice.product!!.imageList!![0].image).apply(RequestOptions().centerCrop().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(binding.imageProductRefundExchangeInfoProductImage)
        }

        binding.textProductRefundExchangeInfoProductName.text = productPrice.product!!.name
        binding.textProductRefundExchangeInfoProductPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(productPrice.price!!.toInt().toString()))

        binding.textProductRefundExchangeInfoPageName.text = productPrice.page!!.name
        binding.textProductRefundExchangeInfoPageTel.text = FormatUtil.getPhoneNumber(productPrice.productDelivery!!.asTel!!.replace("-", ""))
        binding.textProductRefundExchangeInfo.text = getString(R.string.format_seller_refund_exchange_info, productPrice.page!!.name)
        binding.textProductRefundExchangeInfoShippingCompany.text = getString(R.string.format_seller_shipping_company, productPrice.productDelivery!!.shippingCompany)
        binding.textProductRefundExchangeInfoRefundDeliveryFee.text = getString(R.string.format_seller_refund_delivery_fee, FormatUtil.getMoneyType(productPrice.productDelivery!!.deliveryReturnFee!!.toInt().toString()), FormatUtil.getMoneyType((productPrice.productDelivery!!.deliveryReturnFee!!*2).toInt().toString()))
        binding.textProductRefundExchangeInfoExchangeDeliveryFee.text = getString(R.string.format_seller_exchange_delivery_fee, FormatUtil.getMoneyType(productPrice.productDelivery!!.deliveryExchangeFee!!.toInt().toString()))

        if(productPrice.productDelivery!!.returnAddress != null){
            val address = productPrice.productDelivery!!.returnAddress!!.addr1 + productPrice.productDelivery!!.returnAddress!!.addr2
            binding.textProductRefundExchangeInfoRefundAddress.text = getString(R.string.format_seller_refund_address, address, productPrice.productDelivery!!.returnAddress!!.postcode)
        }


    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_refund_exchange_info), ToolbarOption.ToolbarMenu.LEFT)
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