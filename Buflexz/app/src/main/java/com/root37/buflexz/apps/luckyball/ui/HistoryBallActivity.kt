package com.root37.buflexz.apps.luckyball.ui

import android.os.Bundle
import android.view.View
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.time.DateFormatUtils
import com.root37.buflexz.Const
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.toolbar.ImplToolbar
import com.root37.buflexz.apps.common.toolbar.OnToolbarListener
import com.root37.buflexz.apps.common.toolbar.ToolbarOption
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.core.network.model.dto.HistoryBall
import com.root37.buflexz.core.network.model.dto.HistoryPoint
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.ActivityHistoryPointBinding
import java.text.SimpleDateFormat

class HistoryBallActivity : BaseActivity(), ImplToolbar {
    private lateinit var binding: ActivityHistoryPointBinding

    override fun getLayoutView(): View {
        binding = ActivityHistoryPointBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    lateinit var mHistoryBall: HistoryBall

    override fun initializeView(savedInstanceState: Bundle?) {
        mHistoryBall = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, HistoryBall::class.java)!!

        when(mHistoryBall.type){
            "used"->{
                setTitle(getString(R.string.word_ball_used_history_detail))
                binding.textHistoryPointPointTitle.text = getString(R.string.word_use_ball)
                binding.textHistoryPointCommentTitle.text = getString(R.string.word_use_type)
                binding.textHistoryPointDateTitle.text = getString(R.string.word_use_date)
            }
            "retrieve"->{
                setTitle(getString(R.string.word_ball_retrieve_history_detail))
                binding.textHistoryPointPointTitle.text = getString(R.string.word_retrieve_ball)
                binding.textHistoryPointCommentTitle.text = getString(R.string.word_retrieve_type)
                binding.textHistoryPointDateTitle.text = getString(R.string.word_retrieve_date)
            }
            "charge"->{
                setTitle(getString(R.string.word_ball_charge_history_detail))
                binding.textHistoryPointPointTitle.text = getString(R.string.word_charge_ball)
                binding.textHistoryPointCommentTitle.text = getString(R.string.word_charge_type)
                binding.textHistoryPointDateTitle.text = getString(R.string.word_charge_date)
            }
            "provide"->{
                setTitle(getString(R.string.word_ball_provide_history_detail))
                binding.textHistoryPointPointTitle.text = getString(R.string.word_provide_ball)
                binding.textHistoryPointCommentTitle.text = getString(R.string.word_provide_type)
                binding.textHistoryPointDateTitle.text = getString(R.string.word_provide_date)
            }
        }

        binding.textHistoryPointPoint.text = getString(R.string.format_point_unit, FormatUtil.getMoneyTypeFloat(mHistoryBall.ball.toString()))
        binding.textHistoryPointComment.text = mHistoryBall.comment

        val output = SimpleDateFormat(getString(R.string.word_date_format2))
        val regDate = output.format(PplusCommonUtil.setTimeZoneOffset(DateFormatUtils.PPLUS_DATE_FORMAT.parse(mHistoryBall.regDatetime)))
        binding.textHistoryPointDate.text = regDate
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_luckyball_config), ToolbarOption.ToolbarMenu.LEFT)
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