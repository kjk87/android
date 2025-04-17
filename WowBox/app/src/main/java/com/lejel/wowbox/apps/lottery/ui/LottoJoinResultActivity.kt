package com.lejel.wowbox.apps.lottery.ui

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.firebase.analytics.FirebaseAnalytics
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.component.MyBounceInterpolator
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.core.network.model.dto.LotteryJoin
import com.lejel.wowbox.core.util.AdmobUtil
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityLottoJoinResultBinding
import com.lejel.wowbox.databinding.ItemLottoBinding
import com.pplus.utils.part.logs.LogUtil


class LottoJoinResultActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityLottoJoinResultBinding

    override fun getLayoutView(): View {
        binding = ActivityLottoJoinResultBinding.inflate(layoutInflater)
        return binding.root
    }

    var isAdShow = false

    override fun initializeView(savedInstanceState: Bundle?) {

        val lotteryJoin = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, LotteryJoin::class.java)!!

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        })


        isAdShow = true

        val numberList = arrayListOf<Int>()
        numberList.add(lotteryJoin.no1!!)
        numberList.add(lotteryJoin.no2!!)
        numberList.add(lotteryJoin.no3!!)
        numberList.add(lotteryJoin.no4!!)
        numberList.add(lotteryJoin.no5!!)
        numberList.add(lotteryJoin.no6!!)

        binding.layoutLottoJoinResultNumber.removeAllViews()
        for ((i, number) in numberList.withIndex()) {
            val lottoBinding = ItemLottoBinding.inflate(LayoutInflater.from(this), LinearLayout(this), false)
            lottoBinding.textLottoNumber.text = number.toString()
            lottoBinding.textLottoNumber.layoutParams.width = resources.getDimensionPixelSize(R.dimen.width_120)
            lottoBinding.textLottoNumber.layoutParams.height = resources.getDimensionPixelSize(R.dimen.width_120)

            if (number in 1..10) {
                lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.bg_circle_ffc046)
            } else if (number in 11..20) {
                lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.bg_circle_457eef)
            } else if (number in 21..30) {
                lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.bg_circle_ff4e4e)
            } else if (number in 31..40) {
                lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.bg_circle_ad7aff)
            } else {
                lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.bg_circle_5ecb69)
            }

            binding.layoutLottoJoinResultNumber.addView(lottoBinding.root)
            if (i < numberList.size - 1) {
                (lottoBinding.root.layoutParams as LinearLayout.LayoutParams).marginEnd = resources.getDimensionPixelSize(R.dimen.width_6)
            }
        }

        binding.layoutEventResult.setOnClickListener {
            if (isAdShow) {
                if (!AdmobUtil.getInstance(this).mIsLoaded) {
                    return@setOnClickListener
                }
                showAdmob()
            } else {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }

        if (isAdShow && !AdmobUtil.getInstance(this).mIsLoaded) {
            AdmobUtil.getInstance(this).listener = object : AdmobUtil.OnLoadListener {
                override fun onLoaded() {
                    hideProgress()
                    setEventResult()
                }
            }
            showProgress(getString(R.string.msg_lottery_join_progress))
            AdmobUtil.getInstance(this).initAdMob()
        } else {
            setEventResult()
        }
    }

    private fun setEventResult() {

        val anim = AnimationUtils.loadAnimation(this, R.anim.bounce)
        anim.duration = 1000
        val interpolator = MyBounceInterpolator(0.2, 20.0)
        anim.interpolator = interpolator
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
            }

            override fun onAnimationStart(animation: Animation?) {

            }
        })
        binding.layoutLottoJoinResult.post {
            binding.layoutLottoJoinResult.startAnimation(anim)
        }
    }

    fun showAdmob() {
        if (AdmobUtil.getInstance(this).mAdmobInterstitialAd != null) {
            AdmobUtil.getInstance(this).mAdmobInterstitialAd!!.fullScreenContentCallback = object : FullScreenContentCallback(){
                override fun onAdClicked() {
                    LogUtil.e(LOG_TAG, "onAdShowedFullScreenContent")
                }

                override fun onAdDismissedFullScreenContent() {
                    LogUtil.e(LOG_TAG, "onAdDismissedFullScreenContent")
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    LogUtil.e(LOG_TAG, "onAdFailedToShowFullScreenContent")
                }

                override fun onAdImpression() {
                    LogUtil.e(LOG_TAG, "onAdImpression")
                    setEvent(FirebaseAnalytics.Event.AD_IMPRESSION)
                }

                override fun onAdShowedFullScreenContent() {
                    LogUtil.e(LOG_TAG, "onAdShowedFullScreenContent")
                }
            }
            AdmobUtil.getInstance(this).mAdmobInterstitialAd!!.show(this)
            AdmobUtil.getInstance(this).mIsLoaded = false
            AdmobUtil.getInstance(this).initAdMob()
            setResult(Activity.RESULT_OK)
            finish()
        } else {
            AdmobUtil.getInstance(this).mIsLoaded = false
            AdmobUtil.getInstance(this).initAdMob()
            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}
