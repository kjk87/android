package com.root37.buflexz.apps.product.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.info.DeviceUtil
import com.root37.buflexz.Const
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.mgmt.LoginInfoManager
import com.root37.buflexz.apps.common.toolbar.ImplToolbar
import com.root37.buflexz.apps.common.toolbar.OnToolbarListener
import com.root37.buflexz.apps.common.toolbar.ToolbarOption
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.apps.product.data.ProductImageAdapter
import com.root37.buflexz.core.network.ApiBuilder
import com.root37.buflexz.core.network.model.dto.Product
import com.root37.buflexz.core.network.model.response.NewResultResponse
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.ActivityProductBinding
import retrofit2.Call

class ProductActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityProductBinding

    override fun getLayoutView(): View {
        binding = ActivityProductBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    lateinit var mProduct: Product
    var mImageAdapter: ProductImageAdapter? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        mProduct = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, Product::class.java)!!
        mImageAdapter = ProductImageAdapter()
        binding.pagerProductImage.adapter = mImageAdapter
        binding.pagerProductImage.layoutParams.height = DeviceUtil.DISPLAY.SCREEN_WIDTH_PIXELS

        binding.textProductPurchase.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(this, null)) {
                return@setOnClickListener
            }

            if (mProduct.price!! > LoginInfoManager.getInstance().member!!.point!!) {
                showAlert(R.string.msg_lack_point)
                return@setOnClickListener
            }

            if (LoginInfoManager.getInstance().member!!.nation!!.lowercase() == "kr") {
                val intent = Intent(this, ProductPurchaseKrActivity::class.java)
                intent.putExtra(Const.DATA, mProduct)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                defaultLauncher.launch(intent)
            } else {
                val intent = Intent(this, ProductPurchaseActivity::class.java)
                intent.putExtra(Const.DATA, mProduct)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                defaultLauncher.launch(intent)
            }
        }
        getData()
    }

    private fun getData() {
        showProgress("")
        ApiBuilder.create().getProduct(mProduct.seqNo!!).setCallback(object : PplusCallback<NewResultResponse<Product>> {
            override fun onResponse(call: Call<NewResultResponse<Product>>?, response: NewResultResponse<Product>?) {
                hideProgress()
                if (response?.result != null) {
                    mProduct = response.result!!
                    binding.textProductTitle.text = mProduct.title
                    binding.textProductPrice.text = getString(R.string.format_point_unit, FormatUtil.getMoneyTypeFloat(mProduct.price.toString()))

                    val imageList = mProduct.imageList as MutableList
                    mImageAdapter!!.setDataList(imageList)
                } else {
                    finish()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Product>>?, t: Throwable?, response: NewResultResponse<Product>?) {
                hideProgress()
            }
        }).build().call()
    }

    private val defaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (LoginInfoManager.getInstance().isMember()) {
            reloadSession()
        }
    }

    private fun reloadSession() {
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {
            }
        })
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_product_detail), ToolbarOption.ToolbarMenu.LEFT)
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