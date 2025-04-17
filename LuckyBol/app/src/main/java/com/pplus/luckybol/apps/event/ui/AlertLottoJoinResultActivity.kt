package com.pplus.luckybol.apps.event.ui

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.component.MyBounceInterpolator
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.databinding.ActivityAlertLottoJoinResultBinding

class AlertLottoJoinResultActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAlertLottoJoinResultBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertLottoJoinResultBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun initializeView(savedInstanceState: Bundle?) {

        val fadeInAnim = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        val anim = AnimationUtils.loadAnimation(this, R.anim.bounce)
        anim.duration = 1000
        val interpolator = MyBounceInterpolator(0.2, 20.0)
        anim.interpolator = interpolator
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                binding.textAlertLottoJoinResultTitle.visibility = View.VISIBLE
                binding.textAlertLottoJoinResultDesc.visibility = View.VISIBLE
                binding.textAlertLottoJoinResultTitle.startAnimation(fadeInAnim)
                binding.textAlertLottoJoinResultDesc.startAnimation(fadeInAnim)
            }

            override fun onAnimationStart(animation: Animation?) {

            }
        })
        binding.imageAlertLottoJoinResult.post(Runnable {
            binding.imageAlertLottoJoinResult.startAnimation(anim)
        })

        binding.textAlertLottoJoinResultConfirm.setOnClickListener{
            finish()
        }
    }

}
