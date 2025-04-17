package com.root37.buflexz.apps.point.ui

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
import com.root37.buflexz.core.network.model.dto.HistoryPoint
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.ActivityHistoryPointBinding
import java.text.SimpleDateFormat

class HistoryPointActivity : BaseActivity(), ImplToolbar {
    private lateinit var binding: ActivityHistoryPointBinding

    override fun getLayoutView(): View {
        binding = ActivityHistoryPointBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    lateinit var mHistoryPoint: HistoryPoint

    override fun initializeView(savedInstanceState: Bundle?) {
        mHistoryPoint = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, HistoryPoint::class.java)!!

        when(mHistoryPoint.type){
            "used"->{
                setTitle(getString(R.string.word_point_used_history_detail))
                binding.textHistoryPointPointTitle.text = getString(R.string.word_use_point)
                binding.textHistoryPointCommentTitle.text = getString(R.string.word_use_type)
                binding.textHistoryPointDateTitle.text = getString(R.string.word_use_date)
            }
            "retrieve"->{
                setTitle(getString(R.string.word_point_retrieve_history_detail))
                binding.textHistoryPointPointTitle.text = getString(R.string.word_retrieve_point)
                binding.textHistoryPointCommentTitle.text = getString(R.string.word_retrieve_type)
                binding.textHistoryPointDateTitle.text = getString(R.string.word_retrieve_date)
            }
            "charge"->{
                setTitle(getString(R.string.word_point_charge_history_detail))
                binding.textHistoryPointPointTitle.text = getString(R.string.word_charge_point)
                binding.textHistoryPointCommentTitle.text = getString(R.string.word_charge_type)
                binding.textHistoryPointDateTitle.text = getString(R.string.word_charge_date)
            }
            "provide"->{
                setTitle(getString(R.string.word_point_provide_history_detail))
                binding.textHistoryPointPointTitle.text = getString(R.string.word_provide_point)
                binding.textHistoryPointCommentTitle.text = getString(R.string.word_provide_type)
                binding.textHistoryPointDateTitle.text = getString(R.string.word_provide_date)
            }
        }

        binding.textHistoryPointPoint.text = getString(R.string.format_point_unit, FormatUtil.getMoneyTypeFloat(mHistoryPoint.point.toString()))
        binding.textHistoryPointComment.text = mHistoryPoint.comment

        val output = SimpleDateFormat(getString(R.string.word_date_format2))
        val regDate = output.format(PplusCommonUtil.setTimeZoneOffset(DateFormatUtils.PPLUS_DATE_FORMAT.parse(mHistoryPoint.regDatetime)))
        binding.textHistoryPointDate.text = regDate
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_point_config), ToolbarOption.ToolbarMenu.LEFT)
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