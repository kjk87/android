package com.pplus.prnumberuser.apps.product.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.BusinessLicense
import com.pplus.prnumberuser.core.network.model.dto.ProductPrice
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.databinding.ActivityProductSellerInfoBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*

class ProductSellerInfoActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityProductSellerInfoBinding

    override fun getLayoutView(): View {
        binding = ActivityProductSellerInfoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val productPrice = intent.getParcelableExtra<ProductPrice>(Const.PRODUCT_PRICE)
        if (productPrice!!.product!!.imageList != null && productPrice.product!!.imageList!!.isNotEmpty()) {
            Glide.with(this).load(productPrice.product!!.imageList!![0].image).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(binding.imageProductSellerInfoProductImage)
        }

        binding.textProductSellerInfoProductName.text = productPrice.product!!.name
        binding.textProductSellerInfoProductPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(productPrice.price!!.toInt().toString()))

        binding.textProductSellerInfoPageName.text = productPrice.page!!.name
        binding.textProductSellerInfoPageTel.text = FormatUtil.getPhoneNumber(productPrice.page!!.phone!!.replace("biz##", ""))
        binding.textProductSellerInfoPageTel.setOnClickListener {
            if(StringUtils.isNotEmpty(productPrice.page!!.phone)){
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${productPrice.page!!.phone!!.replace("biz##", "")}"))
                startActivity(intent)
            }
        }

        binding.textProductSellerInfoCatchprase.text = productPrice.page!!.catchphrase
        if(StringUtils.isNotEmpty(productPrice.page!!.email)){
            binding.layoutProductSellerInfoEmail.visibility = View.VISIBLE
            binding.textProductSellerInfoEmail.text = productPrice.page!!.email
        }else{
            binding.layoutProductSellerInfoEmail.visibility = View.GONE
        }


        val params = HashMap<String, String>()
        params["pageSeqNo"] = productPrice.page!!.seqNo.toString()
        showProgress("")
        ApiBuilder.create().getBusinessLicense(params).setCallback(object : PplusCallback<NewResultResponse<BusinessLicense>> {
            override fun onResponse(call: Call<NewResultResponse<BusinessLicense>>?, response: NewResultResponse<BusinessLicense>?) {
                hideProgress()
                if (response?.data != null) {
                    val businessLicense = response.data!!

                    binding.textProductSellerInfoCompanyName.text = businessLicense.companyName
                    binding.textProductSellerInfoCeo.text = businessLicense.ceo
                    binding.textProductSellerInfoCompanyNumber.text = businessLicense.corporateNumber
                    binding.textProductSellerInfoCompanyAddress.text = businessLicense.companyAddress
                }
            }

            override fun onFailure(call: Call<NewResultResponse<BusinessLicense>>?, t: Throwable?, response: NewResultResponse<BusinessLicense>?) {
                hideProgress()
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_seller_info), ToolbarOption.ToolbarMenu.LEFT)
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