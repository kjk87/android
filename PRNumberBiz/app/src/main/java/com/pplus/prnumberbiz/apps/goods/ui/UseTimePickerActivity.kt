package com.pplus.prnumberbiz.apps.goods.ui

import android.app.Activity
import android.os.Bundle
import com.pple.pplus.utils.part.utils.time.DateFormatUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.shawnlin.numberpicker.NumberPicker
import kotlinx.android.synthetic.main.activity_use_time_picker.*

class UseTimePickerActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_use_time_picker
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        // Set scroller enabled
        use_start_time_picker_hour.isScrollerEnabled = true
        use_end_time_picker_hour.isScrollerEnabled = true
        // Set wrap selector wheel
        use_start_time_picker_hour.wrapSelectorWheel = true
        use_end_time_picker_hour.wrapSelectorWheel = true
        // Set scroller enabled
        use_start_time_picker_min.isScrollerEnabled = true
        use_end_time_picker_min.isScrollerEnabled = true
        // Set wrap selector wheel
        use_start_time_picker_min.wrapSelectorWheel = true
        use_end_time_picker_min.wrapSelectorWheel = true

        val start = "${DateFormatUtils.formatTime(use_start_time_picker_hour.value)}:${DateFormatUtils.formatTime(use_start_time_picker_min.value)}"
        val end = "${DateFormatUtils.formatTime(use_end_time_picker_hour.value)}:${DateFormatUtils.formatTime(use_end_time_picker_min.value)}"
        text_use_time_selected_value.text = getString(R.string.format_use_time, start, end)

        use_start_time_picker_hour.setOnValueChangedListener(changeListener)
        use_end_time_picker_hour.setOnValueChangedListener(changeListener)
        use_start_time_picker_min.setOnValueChangedListener(changeListener)
        use_end_time_picker_min.setOnValueChangedListener(changeListener)

        use_time_picker_confirm.setOnClickListener {
            val start = "${DateFormatUtils.formatTime(use_start_time_picker_hour.value)}:${DateFormatUtils.formatTime(use_start_time_picker_min.value)}"
            val end = "${DateFormatUtils.formatTime(use_end_time_picker_hour.value)}:${DateFormatUtils.formatTime(use_end_time_picker_min.value)}"
            intent.putExtra(Const.START, start)
            intent.putExtra(Const.END, end)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    val changeListener = NumberPicker.OnValueChangeListener{picker, oldVal, newVal ->

        val start = "${DateFormatUtils.formatTime(use_start_time_picker_hour.value)}:${DateFormatUtils.formatTime(use_start_time_picker_min.value)}"
        val end = "${DateFormatUtils.formatTime(use_end_time_picker_hour.value)}:${DateFormatUtils.formatTime(use_end_time_picker_min.value)}"
        text_use_time_selected_value.text = getString(R.string.format_use_time, start, end)
    }
}
