package com.lejel.wowbox.apps.cash.ui

import android.os.Bundle
import android.view.View
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.time.DateFormatUtils
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.core.network.model.dto.HistoryCash
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityHistoryPointBinding
import java.text.SimpleDateFormat

class HistoryCashActivity : BaseActivity(), ImplToolbar {
    private lateinit var binding: ActivityHistoryPointBinding

    override fun getLayoutView(): View {
        binding = ActivityHistoryPointBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    lateinit var mHistoryCash: HistoryCash

    override fun initializeView(savedInstanceState: Bundle?) {
        mHistoryCash = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, HistoryCash::class.java)!!

        when(mHistoryCash.type){
            "used"->{
                setTitle(getString(R.string.word_rp_used_history_detail))
                binding.textHistoryPointPointTitle.text = getString(R.string.word_use_rp)
                binding.textHistoryPointCommentTitle.text = getString(R.string.word_use_type)
                binding.textHistoryPointDateTitle.text = getString(R.string.word_use_date)
            }
            "retrieve"->{
                setTitle(getString(R.string.word_rp_retrieve_history_detail))
                binding.textHistoryPointPointTitle.text = getString(R.string.word_retrieve_rp)
                binding.textHistoryPointCommentTitle.text = getString(R.string.word_retrieve_type)
                binding.textHistoryPointDateTitle.text = getString(R.string.word_retrieve_date)
            }
            "charge"->{
                setTitle(getString(R.string.word_rp_charge_history_detail))
                binding.textHistoryPointPointTitle.text = getString(R.string.word_charge_rp)
                binding.textHistoryPointCommentTitle.text = getString(R.string.word_charge_type)
                binding.textHistoryPointDateTitle.text = getString(R.string.word_charge_date)
            }
            "provide"->{
                setTitle(getString(R.string.word_rp_provide_history_detail))
                binding.textHistoryPointPointTitle.text = getString(R.string.word_provide_rp)
                binding.textHistoryPointCommentTitle.text = getString(R.string.word_provide_type)
                binding.textHistoryPointDateTitle.text = getString(R.string.word_provide_date)
            }
        }

        binding.textHistoryPointPoint.text = getString(R.string.format_money_unit, FormatUtil.getMoneyTypeFloat(mHistoryCash.cash.toString()))
        binding.textHistoryPointComment.text = mHistoryCash.comment

        val output = SimpleDateFormat(getString(R.string.word_date_format2))
        val regDate = output.format(PplusCommonUtil.setTimeZoneOffset(DateFormatUtils.PPLUS_DATE_FORMAT.parse(mHistoryCash.regDatetime)))
        binding.textHistoryPointDate.text = regDate
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_rp_config), ToolbarOption.ToolbarMenu.LEFT)
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