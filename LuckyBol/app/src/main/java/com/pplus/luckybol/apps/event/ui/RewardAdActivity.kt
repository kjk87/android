//package com.pplus.luckybol.apps.event.ui
//
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import androidx.annotation.NonNull
//import com.google.android.gms.ads.AdError
//import com.google.android.gms.ads.AdRequest
//import com.google.android.gms.ads.LoadAdError
//import com.google.android.gms.ads.rewarded.RewardItem
//import com.google.android.gms.ads.rewarded.RewardedAd
//import com.google.android.gms.ads.rewarded.RewardedAdCallback
//import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
//import com.pplus.luckybol.Const
//import com.pplus.luckybol.R
//import com.pplus.luckybol.apps.bol.ui.BolConfigActivity
//import com.pplus.luckybol.apps.common.mgmt.CountryConfigManager
//import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
//import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
//import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
//import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
//import com.pplus.luckybol.apps.common.ui.base.BaseActivity
//import com.pplus.luckybol.core.network.ApiBuilder
//import com.pplus.luckybol.core.network.model.dto.AdRewardPossible
//import com.pplus.luckybol.core.network.model.response.NewResultResponse
//import com.pplus.luckybol.core.util.PplusCommonUtil
//import com.pplus.luckybol.core.util.ToastUtil
//import com.pplus.networks.common.PplusCallback
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.logs.LogUtil
//import kotlinx.android.synthetic.main.activity_alert_point_exchange.*
//import kotlinx.android.synthetic.main.activity_reward_ad.*
//import retrofit2.Call
//
//class RewardAdActivity : BaseActivity(), ImplToolbar {
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_reward_ad
//    }
//
//    override fun getPID(): String? {
//        return ""
//    }
//
//    var mRewardedAd: RewardedAd? = null
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//
//        text_reward_ad_title.text = PplusCommonUtil.fromHtml(getString(R.string.html_reward_ad_title))
//
//
//        text_reward_ad_retention_bol.setOnClickListener {
//            val intent = Intent(this, BolConfigActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
//        text_reward_ad_view.setOnClickListener {
//            showProgress("")
//
//            ApiBuilder.create().checkAdRewardPossible().setCallback(object : PplusCallback<NewResultResponse<AdRewardPossible>> {
//                override fun onResponse(call: Call<NewResultResponse<AdRewardPossible>>?,
//                                        response: NewResultResponse<AdRewardPossible>?) {
//                    load()
//
//                }
//
//                override fun onFailure(call: Call<NewResultResponse<AdRewardPossible>>?,
//                                       t: Throwable?,
//                                       response: NewResultResponse<AdRewardPossible>?) {
//                    hideProgress()
//
//                    val intent = Intent(this@RewardAdActivity, AdRewardAlertActivity::class.java)
//                    intent.putExtra(Const.KEY, response?.resultCode)
//                    if (response?.resultCode == 516) {
//                        intent.putExtra(Const.DATA, response.data)
//                    }
//                    startActivity(intent)
//                    reload()
//                }
//            }).build().call()
//
//        }
//
//        val maxAdRewardCount = FormatUtil.getMoneyType(CountryConfigManager.getInstance().config.properties.maxAdRewardCount.toString())
//        text_reward_ad_desc.text = getString(R.string.format_reward_ad_desc, maxAdRewardCount)
//        reload()
//    }
//
//    private fun load(){
//        mRewardedAd = RewardedAd(this, Const.ADMOB_REWARD_ID)
//        val adLoadCallback = object : RewardedAdLoadCallback() {
//            override fun onRewardedAdLoaded() {
//                super.onRewardedAdLoaded()
//                LogUtil.e(LOG_TAG, "onRewardedAdLoaded")
//                hideProgress()
//                showAd()
//            }
//
//
//            override fun onRewardedAdFailedToLoad(p0: LoadAdError?) {
//                super.onRewardedAdFailedToLoad(p0)
//                Log.e(LOG_TAG, "onRewardedAdFailedToLoad : "+p0.toString())
//                hideProgress()
//                showAlert(R.string.msg_ad_load_failed)
//            }
//        }
//        mRewardedAd!!.loadAd(AdRequest.Builder().build(), adLoadCallback)
//    }
//
//    private fun showAd() {
//        if (mRewardedAd!!.isLoaded) {
//            val adCallback = object : RewardedAdCallback() {
//                override fun onRewardedAdOpened() {
//                    // Ad opened.
//                }
//
//                override fun onRewardedAdClosed() {
//                    // Ad closed.
//                }
//
//                override fun onUserEarnedReward(@NonNull reward: RewardItem) {
//                    // User earned reward.
//                    ApiBuilder.create().adReward().setCallback(object : PplusCallback<NewResultResponse<Int>> {
//                        override fun onResponse(call: Call<NewResultResponse<Int>>?,
//                                                response: NewResultResponse<Int>?) {
//
//                            if (response?.data != null) {
//                                ToastUtil.show(this@RewardAdActivity, getString(R.string.format_alert_reward_ad_title, response.data.toString()))
//                                //                                        val intent = Intent(this@RewardAdActivity, AlertBolSaveActivity::class.java)
//                                //                                        intent.putExtra(Const.TYPE, Const.REWARD_AD)
//                                //                                        intent.putExtra(Const.AMOUNT, response.data)
//                                //                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                                //                                        startActivityForResult(intent, Const.REQ_CASH_CHANGE)
//                            }
//
//                            reload()
//                        }
//
//                        override fun onFailure(call: Call<NewResultResponse<Int>>?,
//                                               t: Throwable?,
//                                               response: NewResultResponse<Int>?) {
//
//                        }
//                    }).build().call()
//                }
//
//                override fun onRewardedAdFailedToShow(adError: AdError) {
//                    // Ad failed to display.
//                    Log.e(LOG_TAG, "onRewardedAdFailedToShow : " + adError.toString())
//                    hideProgress()
//                    showAlert(R.string.msg_ad_load_failed)
//                }
//            }
//            mRewardedAd!!.show(this@RewardAdActivity, adCallback)
//        }
//    }
//
//    private fun reload() {
//        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
//            override fun reload() {
//                val maxAdRewardCount = FormatUtil.getMoneyType(CountryConfigManager.getInstance().config.properties.maxAdRewardCount.toString())
//                val rewardCount = LoginInfoManager.getInstance().user.adRewardCount
//                text_reward_ad_view_count?.text = PplusCommonUtil.fromHtml(getString(R.string.html_reward_ad_count, rewardCount.toString(), maxAdRewardCount))
//                text_reward_ad_retention_bol?.text = PplusCommonUtil.fromHtml(getString(R.string.html_bol_unit3, FormatUtil.getMoneyType(LoginInfoManager.getInstance().user.totalBol.toString())))
//            }
//        })
//    }
//
//
//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_ad_save), ToolbarOption.ToolbarMenu.LEFT)
//        return toolbarOption
//    }
//
//    override fun getOnToolbarClickListener(): OnToolbarListener {
//
//        return OnToolbarListener { v, toolbarMenu, tag ->
//            when (toolbarMenu) {
//                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
//                    onBackPressed()
//                }
//            }
//        }
//    }
//}