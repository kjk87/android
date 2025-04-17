package com.lejel.wowbox.apps.luckybox.ui

import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebViewClient
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityLuckyBoxProductInfoBinding
import com.pplus.networks.common.PplusCallback
import com.lejel.wowbox.core.network.model.dto.Product
import retrofit2.Call

class LuckyBoxProductInfoActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityLuckyBoxProductInfoBinding

    override fun getLayoutView(): View {
        binding = ActivityLuckyBoxProductInfoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        val product = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, Product::class.java)

        binding.webviewLuckyBoxProductInfo.webChromeClient = WebChromeClient()
        binding.webviewLuckyBoxProductInfo.webViewClient = WebViewClient()
        binding.webviewLuckyBoxProductInfo.settings.javaScriptEnabled = true
        binding.webviewLuckyBoxProductInfo.settings.javaScriptCanOpenWindowsAutomatically = true
        binding.webviewLuckyBoxProductInfo.settings.cacheMode = WebSettings.LOAD_DEFAULT
        binding.webviewLuckyBoxProductInfo.settings.loadWithOverviewMode = true
        binding.webviewLuckyBoxProductInfo.settings.useWideViewPort = true
        binding.webviewLuckyBoxProductInfo.settings.allowContentAccess = true
        binding.webviewLuckyBoxProductInfo.settings.domStorageEnabled = true
        binding.webviewLuckyBoxProductInfo.settings.builtInZoomControls = true
        binding.webviewLuckyBoxProductInfo.settings.displayZoomControls = false
        binding.webviewLuckyBoxProductInfo.settings.allowFileAccess = true
        binding.webviewLuckyBoxProductInfo.settings.setSupportMultipleWindows(true)
        binding.webviewLuckyBoxProductInfo.settings.mixedContentMode = 0
        getData(product!!.seqNo!!)
    }

    private fun getData(seqNo:Long) {
        showProgress("")
        ApiBuilder.create().getProduct(seqNo).setCallback(object : PplusCallback<NewResultResponse<Product>> {
            override fun onResponse(call: Call<NewResultResponse<Product>>?,
                                    response: NewResultResponse<Product>?) {
                hideProgress()
                if(response?.result != null){
                    val product = response.result!!
                    binding.webviewLuckyBoxProductInfo.loadData("<div style='width:100%;text-align:center'>${product.contents}</div>", "text/html", "utf-8")
                }

            }

            override fun onFailure(call: Call<NewResultResponse<Product>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<Product>?) {
                hideProgress()
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_product_info), ToolbarOption.ToolbarMenu.LEFT)
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