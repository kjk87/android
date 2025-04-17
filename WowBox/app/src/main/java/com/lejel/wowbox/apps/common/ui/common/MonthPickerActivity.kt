package com.lejel.wowbox.apps.common.ui.common

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lejel.wowbox.Const
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.pplus.utils.part.utils.time.DateFormatUtils
import com.lejel.wowbox.databinding.ActivityMonthPickerBinding
import java.util.*

class MonthPickerActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityMonthPickerBinding

    override fun getLayoutView(): View {
        binding = ActivityMonthPickerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        // Set scroller enabled
        binding.datePickerYear.isScrollerEnabled = true
        // Set wrap selector wheel
        binding.datePickerYear.wrapSelectorWheel = true
        binding.datePickerYear.maxValue = Calendar.getInstance().get(Calendar.YEAR)

        // Set scroller enabled
        binding.datePickerMonth.isScrollerEnabled = true
        // Set wrap selector wheel
        binding.datePickerMonth.wrapSelectorWheel = true


        binding.datePickerConfirm.setOnClickListener {
            val year = binding.datePickerYear.value.toString()
            val month = DateFormatUtils.formatTime(binding.datePickerMonth.value)
            val data = Intent()
            data.putExtra(Const.YEAR, year)
            data.putExtra(Const.MONTH, month)
            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }
}
