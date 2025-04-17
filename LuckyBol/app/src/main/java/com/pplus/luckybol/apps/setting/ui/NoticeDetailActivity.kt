package com.pplus.luckybol.apps.setting.ui

import android.os.Bundle
import android.view.View
import android.webkit.*
import com.pplus.networks.common.PplusCallback
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption.ToolbarMenu
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.Faq
import com.pplus.luckybol.core.network.model.dto.Notice
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.databinding.ActivityNoticeDetailBinding
import com.pplus.utils.part.info.OsUtil
import com.pplus.utils.part.logs.LogUtil
import retrofit2.Call
import java.util.*

class NoticeDetailActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String? {
        return ""
    }

    private lateinit var binding: ActivityNoticeDetailBinding

    override fun getLayoutView(): View {
        binding = ActivityNoticeDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    private var mNotice: Notice? = null
    private var mFaq: Faq? = null
    override fun initializeView(savedInstanceState: Bundle?) {
        mNotice = intent.getParcelableExtra(Const.NOTICE)
        mFaq = intent.getParcelableExtra(Const.FAQ)
        val settings = binding.webviewNoticeDetail.settings
        settings.defaultTextEncodingName = "utf-8"
        binding.webviewNoticeDetail.setBackgroundColor(0)
        if (OsUtil.isLollipop()) {
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        binding.webviewNoticeDetail.webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                LogUtil.e(LOG_TAG, "console msg : {}, level : {]", consoleMessage.message(), consoleMessage.messageLevel())
                return super.onConsoleMessage(consoleMessage)
            }
        }
        binding.webviewNoticeDetail.webViewClient = object : WebViewClient() {
            override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                super.onReceivedError(view, request, error)
                LogUtil.e(LOG_TAG, "onReceivedError : {}", error.description)
            }
        }
        if (mNotice != null) {
            setTitle(getString(R.string.word_notice))
            val params: MutableMap<String, String> = HashMap()
            params["no"] = "" + mNotice!!.no
            ApiBuilder.create().getNotice(params).setCallback(object : PplusCallback<NewResultResponse<Notice>> {
                override fun onResponse(call: Call<NewResultResponse<Notice>>?, response: NewResultResponse<Notice>?) {
                    if(response?.data != null){
                        mNotice = response.data
                        binding.textNoticeTitle.text = response.data!!.subject
                        binding.webviewNoticeDetail.loadDataWithBaseURL("", response.data!!.contents!!, "text/html; charset=utf-8", "utf-8", "")
                    }

                }

                override fun onFailure(call: Call<NewResultResponse<Notice>>?, t: Throwable?, response: NewResultResponse<Notice>?) {

                }
            }).build().call()
        } else if (mFaq != null) {
            setTitle(getString(R.string.word_faq_en))
            val params: MutableMap<String, String> = HashMap()
            params["no"] = "" + mFaq!!.no
            ApiBuilder.create().getFaq(params).setCallback(object : PplusCallback<NewResultResponse<Faq>> {
                override fun onResponse(call: Call<NewResultResponse<Faq>>?, response: NewResultResponse<Faq>?) {
                    if(response?.data != null){
                        mFaq = response.data
                        binding.textNoticeTitle.text = response.data!!.subject
                        binding.webviewNoticeDetail.loadDataWithBaseURL("", response.data!!.contents!!, "text/html; charset=utf-8", "utf-8", "")
                    }

                }

                override fun onFailure(call: Call<NewResultResponse<Faq>>?, t: Throwable?, response: NewResultResponse<Faq>?) {
                }
            }).build().call()
        }
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_notice), ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {
        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressed()
                    }
                    else -> {}
                }
            }
        }
    }
}