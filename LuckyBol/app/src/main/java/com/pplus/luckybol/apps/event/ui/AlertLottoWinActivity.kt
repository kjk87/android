package com.pplus.luckybol.apps.event.ui

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.network.model.dto.EventWin
import com.pplus.luckybol.databinding.ActivityAlertLottoWinBinding
import com.pplus.utils.part.logs.LogUtil

class AlertLottoWinActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAlertLottoWinBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertLottoWinBinding.inflate(layoutInflater)
        return binding.root
    }

    lateinit var mEventWin: EventWin

    override fun initializeView(savedInstanceState: Bundle?) {
        mEventWin = intent.getParcelableExtra(Const.DATA)!!
        val winnerCount = intent.getIntExtra(Const.WINNER_COUNT, 0)

        binding.textAlertLottoWinDesc.visibility = View.GONE
        binding.layoutLottoWinGift.visibility = View.GONE

        Glide.with(this).load(mEventWin.gift!!.giftImageUrl).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_lotto_event_gift).error(R.drawable.img_lotto_event_gift)).into(binding.imageAlertLottoWinGift)
        binding.textAlertLottoWinGiftTitle.text = mEventWin.giftTitle
        binding.textAlertLottoWinWriteImpression.setOnClickListener {
            val intent = Intent(this, EventWinImpressionActivity::class.java)
            intent.putExtra(Const.EVENT_WIN, mEventWin)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            impressLauncher.launch(intent)
        }

        if(winnerCount > 1){
            binding.textAlertLottoWinDesc.text = getString(R.string.format_alert_lotto_win_multi_desc, winnerCount.toString())
        }else{
            binding.textAlertLottoWinDesc.text = getString(R.string.msg_alert_lotto_win_alone_desc)
        }

        val handerl = Handler(Looper.myLooper()!!)
        handerl.postDelayed({
            val fadeInAnim = AnimationUtils.loadAnimation(this@AlertLottoWinActivity, android.R.anim.fade_in)
            binding.layoutLottoWinGift.visibility = View.VISIBLE
            binding.layoutLottoWinGift.startAnimation(fadeInAnim)
            binding.textAlertLottoWinDesc.visibility = View.VISIBLE
            binding.textAlertLottoWinDesc.startAnimation(fadeInAnim)
        }, 1500)

        binding.lottieLottoWin.addAnimatorListener(object : Animator.AnimatorListener{
            override fun onAnimationStart(animation: Animator?, isReverse: Boolean) {

            }

            override fun onAnimationStart(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?, isReverse: Boolean) {
                super.onAnimationEnd(animation, isReverse)
                LogUtil.e(LOG_TAG, "onAnimationEnd1")
            }

            override fun onAnimationEnd(animation: Animator?) {
                LogUtil.e(LOG_TAG, "onAnimationEnd")
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationRepeat(animation: Animator?) {

            }
        })
    }

    val impressLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        finish()
    }
}
