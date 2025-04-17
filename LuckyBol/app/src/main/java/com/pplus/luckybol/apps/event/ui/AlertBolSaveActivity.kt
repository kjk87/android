package com.pplus.luckybol.apps.event.ui

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.databinding.ActivityAlertBolSaveBinding
import com.pplus.utils.part.logs.LogUtil

class AlertBolSaveActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAlertBolSaveBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertBolSaveBinding.inflate(layoutInflater)
        return binding.root
    }

    private var mAdmobInterstitialAd: InterstitialAd? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        val type = intent.getStringExtra(Const.TYPE)
        val amount = intent.getIntExtra(Const.AMOUNT, 0)
        when(type){
            Const.ATTENDANCE->{
                binding.textAlertBolSaveAmount.visibility = View.VISIBLE
                binding.textAlertBolSaveAmount.text = "+${amount}"
                binding.textAlertBolSaveTitle.text = getString(R.string.word_complete_attendance)
                binding.textAlertBolSaveDesc.text = getString(R.string.format_alert_attendance_desc, amount.toString())
            }
            Const.REWARD_AD->{
                binding.textAlertBolSaveAmount.visibility = View.GONE
                binding.textAlertBolSaveTitle.text = getString(R.string.format_alert_reward_ad_title, amount.toString())
                binding.textAlertBolSaveDesc.text = getString(R.string.msg_alert_reward_ad_desc)
            }
        }

        binding.textAlertBolSaveConfirm.setOnClickListener {
//            finish()
            initAdMob()
        }
    }

    fun initAdMob() {

        MobileAds.initialize(this)

        val adRequest = AdRequest.Builder().build()

        showProgress("")
        InterstitialAd.load(this,Const.ADMOB_INTERSTITIAL_ID, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                LogUtil.e(LOG_TAG, "onAdFailedToLoad : {}", adError?.message)
                mAdmobInterstitialAd = null
                hideProgress()
                finish()
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                LogUtil.e(LOG_TAG, "onAdLoaded")
                mAdmobInterstitialAd = interstitialAd
                hideProgress()
                mAdmobInterstitialAd!!.show(this@AlertBolSaveActivity)

                setResult(Activity.RESULT_OK)
                finish()
            }
        })

    }
}
