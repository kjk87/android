package com.lejel.wowbox.apps.luckybox.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.builder.AlertBuilder
import com.lejel.wowbox.apps.common.builder.OnAlertResultListener
import com.lejel.wowbox.apps.common.builder.data.AlertData
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.core.network.model.dto.LuckyBoxDeliveryPurchase
import com.lejel.wowbox.core.network.model.dto.LuckyBoxPurchase
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityLuckyBoxPgBinding
import com.pplus.utils.part.logs.LogUtil
import java.net.URISyntaxException


class LuckyBoxPgActivity : BaseActivity(), ImplToolbar {
    private lateinit var binding: ActivityLuckyBoxPgBinding

    override fun getLayoutView(): View {
        binding = ActivityLuckyBoxPgBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val builder = AlertBuilder.Builder()
                builder.setTitle(getString(R.string.word_notice_alert))
                builder.addContents(AlertData.MessageData(getString(R.string.msg_question_pay_back), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
                builder.setOnAlertResultListener(object : OnAlertResultListener {

                    override fun onCancel() {

                    }

                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                        when (event_alert) {
                            AlertBuilder.EVENT_ALERT.RIGHT -> finish()
                            else -> {}
                        }
                    }
                }).builder().show(this@LuckyBoxPgActivity)
            }
        })

        val key = intent.getStringExtra(Const.KEY)
        var luckyBoxPurchase:LuckyBoxPurchase? = null
        var luckyBoxDeliveryPurchase:LuckyBoxDeliveryPurchase? = null
        when(key){
            "luckybox"->{
                setTitle(getString(R.string.word_buy_wowbox))
                luckyBoxPurchase = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, LuckyBoxPurchase::class.java)
            }
            "delivery"->{
                setTitle(getString(R.string.word_pay_delviery_fee))
                luckyBoxDeliveryPurchase = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, LuckyBoxDeliveryPurchase::class.java)
            }

        }

        binding.webviewLuckyBoxPg.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                LogUtil.e(LOG_TAG, url)
                hideProgress()
                if(url!!.contains("xendit/success")){
                    when(key){
                        "luckybox"->{

                            val intent = Intent(this@LuckyBoxPgActivity, LuckyBoxPayCompleteActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            intent.putExtra(Const.DATA, luckyBoxPurchase)
                            resultLauncher.launch(intent)
                        }
                        "delivery"->{

                            val intent = Intent(this@LuckyBoxPgActivity, LuckyBoxReqShippingCompleteActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            intent.putExtra(Const.DATA, luckyBoxDeliveryPurchase)
                            resultLauncher.launch(intent)
                        }
                    }
                }
            }

            override fun shouldOverrideUrlLoading(view: WebView?,
                                                  request: WebResourceRequest?): Boolean {
                val url = request?.url.toString()
                if (url.startsWith("intent://")) {
                    try {
                        val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                        val existPackage =
                            packageManager.getLaunchIntentForPackage(intent.getPackage()!!)
                        if (existPackage != null) {
                            startActivity(intent)
                        } else {
                            val marketIntent = Intent(Intent.ACTION_VIEW)
                            marketIntent.data =
                                Uri.parse("market://details?id=" + intent.getPackage()!!)
                            startActivity(marketIntent)
                        }
                        return true
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                } else if (url.startsWith("market://")) {
                    try {
                        val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                        if (intent != null) {
                            startActivity(intent)
                        }
                        return true
                    } catch (e: URISyntaxException) {
                        e.printStackTrace()
                    }
                }
                view?.loadUrl(url)
                return false
            }
        }

        binding.webviewLuckyBoxPg.settings.javaScriptEnabled = true
        binding.webviewLuckyBoxPg.settings.javaScriptCanOpenWindowsAutomatically = true
        binding.webviewLuckyBoxPg.settings.cacheMode = WebSettings.LOAD_DEFAULT
        binding.webviewLuckyBoxPg.settings.loadWithOverviewMode = true
        binding.webviewLuckyBoxPg.settings.useWideViewPort = true
        binding.webviewLuckyBoxPg.settings.allowContentAccess = true
        binding.webviewLuckyBoxPg.settings.domStorageEnabled = true
        binding.webviewLuckyBoxPg.settings.allowFileAccess = true
        binding.webviewLuckyBoxPg.settings.setSupportMultipleWindows(true)
        binding.webviewLuckyBoxPg.settings.mixedContentMode = 0
        binding.webviewLuckyBoxPg.setLayerType(View.LAYER_TYPE_HARDWARE, null)

        showProgress("")
        when(key){
            "luckybox"->{
                binding.webviewLuckyBoxPg.loadUrl(luckyBoxPurchase!!.invoiceUrl!!)
            }
            "delivery"->{
                binding.webviewLuckyBoxPg.loadUrl(luckyBoxDeliveryPurchase!!.invoiceUrl!!)
            }

        }
    }

    val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        setResult(RESULT_OK)
        finish()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_buy_wowbox), ToolbarOption.ToolbarMenu.LEFT)
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