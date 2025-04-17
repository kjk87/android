package com.pplus.prnumberuser.apps.main.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewTreeObserver
import androidx.activity.result.ActivityResult
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.location.ui.LocationSetActivity
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivityLocationSelectBinding
import com.pplus.utils.part.info.OsUtil
import com.pplus.utils.part.logs.LogUtil


class LocationSelectActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityLocationSelectBinding

    override fun getLayoutView(): View {
        binding = ActivityLocationSelectBinding.inflate(layoutInflater)
        return binding.root
    }

    var animating = false


    override fun initializeView(savedInstanceState: Bundle?) {

        mLocationListener = object :LocationListener{
            override fun onLocation(result : ActivityResult) {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }

        binding.layoutLocationSelectCurrent.setOnClickListener {
            PplusCommonUtil.alertLocation(this, true, object : PplusCommonUtil.Companion.SuccessLocationListener {
                override fun onSuccess() {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            })
        }

        binding.layoutLocationSelectMap.setOnClickListener {
            val intent = Intent(this, LocationSetActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            locationLauncher.launch(intent)
        }

        binding.imageLocationSelectClose.setOnClickListener {
            finish()
        }

        if(OsUtil.isLollipop()){
            val viewTreeObserver = binding.root.viewTreeObserver
            if (viewTreeObserver.isAlive) {
                viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        val x = intent.getIntExtra(Const.X, 0)
                        val y = intent.getIntExtra(Const.Y, 0)

                        revealShow(binding.root, x, y)
                        binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                })
            }
        }
    }

    private fun revealShow(view:View, x:Int, y:Int) {

        val w = view.width
        val h = view.height

        LogUtil.e(LOG_TAG, "x : {}, y : {}", x, y)
        LogUtil.e(LOG_TAG, "w : {}, h : {}",w, h)

        val endRadius = (Math.max(w, h) * 1.1);

        val revealAnimator = ViewAnimationUtils.createCircularReveal(view, x, y, 0f, endRadius.toFloat())

        view.visibility = View.VISIBLE

        revealAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                animating = false
            }
        })
        revealAnimator.duration = 700
        animating = true
        revealAnimator.start()

    }
}
