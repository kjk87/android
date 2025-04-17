package com.pplus.luckybol.apps.common.ui.common

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pplus.luckybol.Const
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.databinding.ActivityDatePickerBinding
import com.pplus.utils.part.utils.time.DateFormatUtils
import java.util.*

class DatePickerActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityDatePickerBinding

    override fun getLayoutView(): View {
        binding = ActivityDatePickerBinding.inflate(layoutInflater)
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
        // Set scroller enabled
        binding.datePickerDate.isScrollerEnabled = true
        // Set wrap selector wheel
        binding.datePickerDate.wrapSelectorWheel = true

        binding.datePickerConfirm.setOnClickListener {
            val birthday = "${binding.datePickerYear.value.toString()}${DateFormatUtils.formatTime(binding.datePickerMonth.value)}${DateFormatUtils.formatTime(binding.datePickerDate.value)}"
            val data = Intent()
            data.putExtra(Const.BIRTH_DAY, birthday)
            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }
}
