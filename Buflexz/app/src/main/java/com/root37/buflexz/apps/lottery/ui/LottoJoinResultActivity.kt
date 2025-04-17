package com.root37.buflexz.apps.lottery.ui

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.OnBackPressedCallback
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.component.MyBounceInterpolator
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.core.util.AdmobUtil
import com.root37.buflexz.databinding.ActivityLottoJoinResultBinding


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
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        })


        isAdShow = true

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
    }

    private fun setEventResult() {

        binding.textLottoResultConfirm.visibility = View.INVISIBLE

        val fadeInAnim = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        val anim = AnimationUtils.loadAnimation(this, R.anim.bounce)
        anim.duration = 1000
        val interpolator = MyBounceInterpolator(0.2, 20.0)
        anim.interpolator = interpolator
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                binding.textLottoResultConfirm.visibility = View.VISIBLE
                binding.textLottoResultConfirm.startAnimation(fadeInAnim)
            }

            override fun onAnimationStart(animation: Animation?) {

            }
        })
        binding.imageLottoResultCharacter.post {
            binding.imageLottoResultCharacter.startAnimation(anim)
        }
    }

    fun showAdmob() {
        if (AdmobUtil.getInstance(this).mAdmobInterstitialAd != null) {
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
