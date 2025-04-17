package com.lejel.wowbox.apps.event.ui

import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.event.data.GetLuckyBallGuideAdapter
import com.lejel.wowbox.core.network.model.dto.Event
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityGetLuckyBallGuideBinding
import com.lejel.wowbox.databinding.ActivityPlayPercentBinding
import com.pplus.utils.part.format.FormatUtil
import com.yy.mobile.rollingtextview.CharOrder
import com.yy.mobile.rollingtextview.strategy.Strategy

class PlayPercentActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityPlayPercentBinding

    override fun getLayoutView(): View {
        binding = ActivityPlayPercentBinding.inflate(layoutInflater)
        return binding.root
    }

    lateinit var mEvent:Event

    override fun initializeView(savedInstanceState: Bundle?) {
        mEvent = PplusCommonUtil.getParcelableExtra(intent, Const.EVENT, Event::class.java)!!

        binding.textPlayPercentConfirm.setOnClickListener {

            if(LoginInfoManager.getInstance().member!!.ball == null || LoginInfoManager.getInstance().member!!.ball!! <= 0){
                showAlert(R.string.msg_can_not_cal_win_percent_none_ball)
                return@setOnClickListener
            }

            binding.textPlayPercent.setText("0")
            var percent = ((LoginInfoManager.getInstance().member!!.ball!!.toFloat() / mEvent.maxJoinCount!!.toFloat())*100f).toInt()
            if(percent > 100){
                percent = 100
            }
            binding.textPlayPercent.addCharOrder(CharOrder.Number)
            binding.textPlayPercent.animationDuration = 2000L
            binding.textPlayPercent.charStrategy = Strategy.CarryBitAnimation()
            binding.textPlayPercent.animationInterpolator = AccelerateDecelerateInterpolator()
            binding.textPlayPercent.setText(percent.toString())
        }

        reloadSession()
    }

    private fun reloadSession() {
        showProgress("")
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {
                hideProgress()
                binding.textPlayPercentRetentionBall.text = FormatUtil.getMoneyType(LoginInfoManager.getInstance().member!!.ball!!.toInt().toString())
            }
        })
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_win_rate2), ToolbarOption.ToolbarMenu.LEFT)
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