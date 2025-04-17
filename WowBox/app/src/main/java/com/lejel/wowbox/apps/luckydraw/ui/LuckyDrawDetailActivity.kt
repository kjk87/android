package com.lejel.wowbox.apps.luckydraw.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.activity.result.contract.ActivityResultContracts
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.builder.AlertBuilder
import com.lejel.wowbox.apps.common.builder.OnAlertResultListener
import com.lejel.wowbox.apps.common.builder.data.AlertData
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.my.ui.UpdateMobileNumberActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.LuckyDraw
import com.lejel.wowbox.core.network.model.dto.WalletRes
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityLuckyBoxProductInfoBinding
import com.lejel.wowbox.databinding.ActivityLuckyDrawDetailBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.resource.ResourceUtil
import com.pplus.utils.part.utils.StringUtils
import com.pplus.utils.part.utils.time.DateFormatUtils
import retrofit2.Call
import java.text.SimpleDateFormat

class LuckyDrawDetailActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityLuckyDrawDetailBinding

    override fun getLayoutView(): View {
        binding = ActivityLuckyDrawDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    lateinit var mLuckyDraw: LuckyDraw

    override fun initializeView(savedInstanceState: Bundle?) {

        mLuckyDraw = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, LuckyDraw::class.java)!!

        binding.webviewLuckyDrawDetail.webChromeClient = WebChromeClient()
        binding.webviewLuckyDrawDetail.webViewClient = WebViewClient()
        binding.webviewLuckyDrawDetail.settings.javaScriptEnabled = true
        binding.webviewLuckyDrawDetail.settings.javaScriptCanOpenWindowsAutomatically = true
        binding.webviewLuckyDrawDetail.settings.cacheMode = WebSettings.LOAD_DEFAULT
        binding.webviewLuckyDrawDetail.settings.loadWithOverviewMode = true
        binding.webviewLuckyDrawDetail.settings.useWideViewPort = true
        binding.webviewLuckyDrawDetail.settings.allowContentAccess = true
        binding.webviewLuckyDrawDetail.settings.domStorageEnabled = true
        binding.webviewLuckyDrawDetail.settings.builtInZoomControls = true
        binding.webviewLuckyDrawDetail.settings.displayZoomControls = false
        binding.webviewLuckyDrawDetail.settings.allowFileAccess = true
        binding.webviewLuckyDrawDetail.settings.setSupportMultipleWindows(true)
        binding.webviewLuckyDrawDetail.settings.mixedContentMode = 0
        getData(mLuckyDraw.seqNo!!)
    }

    private fun getData(seqNo:Long) {
        showProgress("")
        ApiBuilder.create().getLuckyDraw(seqNo).setCallback(object : PplusCallback<NewResultResponse<LuckyDraw>> {
            override fun onResponse(call: Call<NewResultResponse<LuckyDraw>>?,
                                    response: NewResultResponse<LuckyDraw>?) {
                hideProgress()
                if(response?.result != null){
                    mLuckyDraw = response.result!!
                    setTitle(mLuckyDraw.title)
                    binding.webviewLuckyDrawDetail.loadData("<div style='width:100%;text-align:center'>${mLuckyDraw.contents}</div>", "text/html", "utf-8")
                    when (mLuckyDraw.status) {
                        "active"->{
                            binding.textLuckyDrawDetailJoin.setBackgroundColor(ResourceUtil.getColor(this@LuckyDrawDetailActivity, R.color.color_ea5506))
                            binding.textLuckyDrawDetailJoin.setTextColor(ResourceUtil.getColor(this@LuckyDrawDetailActivity, R.color.white))
                            binding.textLuckyDrawDetailJoin.text = getString(R.string.msg_join)
                        }
                        "expire" -> {
                            binding.textLuckyDrawDetailJoin.setBackgroundColor(ResourceUtil.getColor(this@LuckyDrawDetailActivity, R.color.color_878787))
                            binding.textLuckyDrawDetailJoin.setTextColor(ResourceUtil.getColor(this@LuckyDrawDetailActivity, R.color.white))
                            binding.textLuckyDrawDetailJoin.text = getString(R.string.msg_checking_win_announce_date)
                        }
                        "pending" -> {
                            if (mLuckyDraw.announceType == "live" && StringUtils.isNotEmpty(mLuckyDraw.liveUrl)) {
                                binding.textLuckyDrawDetailJoin.setBackgroundColor(ResourceUtil.getColor(this@LuckyDrawDetailActivity, R.color.color_ff5e5e))
                                binding.textLuckyDrawDetailJoin.setTextColor(ResourceUtil.getColor(this@LuckyDrawDetailActivity, R.color.white))
                                binding.textLuckyDrawDetailJoin.text = getString(R.string.msg_view_live)
                            }else{
                                binding.textLuckyDrawDetailJoin.setBackgroundColor(ResourceUtil.getColor(this@LuckyDrawDetailActivity, R.color.white))
                                binding.textLuckyDrawDetailJoin.setTextColor(ResourceUtil.getColor(this@LuckyDrawDetailActivity, R.color.color_333333))
                                val winAnnounceFormat = SimpleDateFormat(getString(R.string.word_date_format3))
                                binding.textLuckyDrawDetailJoin.text = getString(R.string.format_lotto_win_announce_date, winAnnounceFormat.format(PplusCommonUtil.setTimeZoneOffset(DateFormatUtils.PPLUS_DATE_FORMAT.parse(mLuckyDraw.winAnnounceDatetime))))
                            }
                        }
                        "complete" -> {
                            binding.textLuckyDrawDetailJoin.setBackgroundColor(ResourceUtil.getColor(this@LuckyDrawDetailActivity, R.color.white))
                            binding.textLuckyDrawDetailJoin.setTextColor(ResourceUtil.getColor(this@LuckyDrawDetailActivity, R.color.color_ea5506))
                            val winAnnounceFormat = SimpleDateFormat(getString(R.string.word_date_format3))
                            binding.textLuckyDrawDetailJoin.text = getString(R.string.msg_view_winner)
                        }
                    }


                    binding.textLuckyDrawDetailJoin.setOnClickListener {
                        when (mLuckyDraw.status) {
                            "active" -> {

                                if (!PplusCommonUtil.loginCheck(this@LuckyDrawDetailActivity, defaultLauncher)) {
                                    return@setOnClickListener
                                }

                                if(LoginInfoManager.getInstance().member!!.verifiedMobile == null || !LoginInfoManager.getInstance().member!!.verifiedMobile!!){

                                    val builder = AlertBuilder.Builder()
                                    builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_event_verify_mobile), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
                                    builder.setRightText(getString(R.string.word_confirm))
                                    builder.setOnAlertResultListener(object : OnAlertResultListener {

                                        override fun onCancel() {

                                        }

                                        override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                                            when (event_alert) {
                                                AlertBuilder.EVENT_ALERT.SINGLE -> {
                                                    val intent = Intent(this@LuckyDrawDetailActivity, UpdateMobileNumberActivity::class.java)
                                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                                    updateMobileLauncher.launch(intent)
                                                }
                                                else -> {

                                                }
                                            }
                                        }
                                    }).builder().show(this@LuckyDrawDetailActivity)
                                    return@setOnClickListener
                                }

                                if(mLuckyDraw.isPrivate != null && mLuckyDraw.isPrivate!!){
                                    val intent = Intent(this@LuckyDrawDetailActivity, LuckyDrawCheckPrivateActivity::class.java)
                                    intent.putExtra(Const.DATA, mLuckyDraw)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    checkPrivateLauncher.launch(intent)
                                    return@setOnClickListener
                                }

                                val intent = Intent(this@LuckyDrawDetailActivity, LuckyDrawJoinActivity::class.java)
                                intent.putExtra(Const.DATA, mLuckyDraw)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                defaultLauncher.launch(intent)
                            }
                            "expire", "pending" -> {
                                if (StringUtils.isNotEmpty(mLuckyDraw.liveUrl)) {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mLuckyDraw.liveUrl))
                                    startActivity(intent)
                                }
                            }
                            "complete" -> {
                                val intent = Intent(this@LuckyDrawDetailActivity, LuckyDrawWinActivity::class.java)
                                intent.putExtra(Const.DATA, mLuckyDraw)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                defaultLauncher.launch(intent)
                            }
                        }
                    }
                }

            }

            override fun onFailure(call: Call<NewResultResponse<LuckyDraw>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<LuckyDraw>?) {
                hideProgress()
            }
        }).build().call()
    }

    val updateMobileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

    }


    val defaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            setResult(RESULT_OK)
            finish()

        }else{
            getData(mLuckyDraw.seqNo!!)
        }

    }

    private val checkPrivateLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val item = PplusCommonUtil.getParcelableExtra(result.data!!, Const.DATA, LuckyDraw::class.java)

            val intent = Intent(this, LuckyDrawJoinActivity::class.java)
            intent.putExtra(Const.DATA, item)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar("", ToolbarOption.ToolbarMenu.LEFT)
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