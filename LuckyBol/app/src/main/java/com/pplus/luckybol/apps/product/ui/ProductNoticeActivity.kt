package com.pplus.luckybol.apps.product.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.product.data.ProductNoticeAdapter
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.ProductNotice
import com.pplus.luckybol.core.network.model.dto.ProductPrice
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.databinding.ActivityProductNoticeBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import retrofit2.Call

class ProductNoticeActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityProductNoticeBinding

    override fun getLayoutView(): View {
        binding = ActivityProductNoticeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val productPrice = intent.getParcelableExtra<ProductPrice>(Const.PRODUCT_PRICE)!!
        if (productPrice.product!!.imageList != null && productPrice.product!!.imageList!!.isNotEmpty()) {
            Glide.with(this).load(productPrice.product!!.imageList!![0].image).apply(RequestOptions().centerCrop().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(binding.imageProductNoticeProductImage)
        }

        binding.textProductNoticeProductName.text = productPrice.product!!.name
        binding.textProductNoticeProductPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(productPrice.price!!.toInt().toString()))
        val adapter = ProductNoticeAdapter()

        binding.recyclerProductNotice.layoutManager = LinearLayoutManager(this)
        binding.recyclerProductNotice.adapter = adapter
        val params = HashMap<String, String>()
        params["productSeqNo"] = productPrice.product!!.seqNo.toString()
        showProgress("")
        ApiBuilder.create().getProductNoticeListByProductSeqNo(params).setCallback(object : PplusCallback<NewResultResponse<ProductNotice>> {
            override fun onResponse(call: Call<NewResultResponse<ProductNotice>>?,
                                    response: NewResultResponse<ProductNotice>?) {
                hideProgress()
                if (response?.datas != null) {
                    adapter.clear()
                    adapter.addAll(response.datas!!)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<ProductNotice>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<ProductNotice>?) {
                hideProgress()

            }
        }).build().call()

    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_goods_notice_info), ToolbarOption.ToolbarMenu.LEFT)
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