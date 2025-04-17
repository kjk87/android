package com.pplus.prnumberuser.apps.product.ui

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.core.network.model.dto.ProductPrice
import com.pplus.prnumberuser.databinding.ActivityProductOriginBinding
import com.pplus.utils.part.format.FormatUtil

class ProductOriginActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityProductOriginBinding

    override fun getLayoutView(): View {
        binding = ActivityProductOriginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val productPrice = intent.getParcelableExtra<ProductPrice>(Const.PRODUCT_PRICE)
        if (productPrice!!.product!!.imageList != null && productPrice.product!!.imageList!!.isNotEmpty()) {
            Glide.with(this).load(productPrice.product!!.imageList!![0].image).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(binding.imageProductOriginProductImage)
        }

        binding.textProductOriginProductName.text = productPrice.product!!.name
        binding.textProductOriginProductPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(productPrice.price!!.toInt().toString()))

        binding.textProductOrigin.text = productPrice.product!!.origin

    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_origin_place), ToolbarOption.ToolbarMenu.LEFT)
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