package com.pplus.prnumberbiz.apps.common.ui.common

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.pple.pplus.utils.part.utils.time.DateFormatUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_date_picker.*
import java.util.*

class DatePickerActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_date_picker
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        // Set scroller enabled
        date_picker_year.isScrollerEnabled = true
        // Set wrap selector wheel
        date_picker_year.wrapSelectorWheel = true



        // Set scroller enabled
        date_picker_month.isScrollerEnabled = true
        // Set wrap selector wheel
        date_picker_month.wrapSelectorWheel = true
        // Set scroller enabled
        date_picker_date.isScrollerEnabled = true
        // Set wrap selector wheel
        date_picker_date.wrapSelectorWheel = true

        val cal = Calendar.getInstance()
        cal.add(Calendar.MONTH, 1)
        val year = cal.get(Calendar.YEAR)

        date_picker_year.minValue = year
        date_picker_year.maxValue = year + 100
        date_picker_year.value = year

        val month = cal.get(Calendar.MONTH)+1

        date_picker_month.value = month

        val day = cal.get(Calendar.DAY_OF_MONTH)
        date_picker_date.value = day

        date_picker_confirm.setOnClickListener {
            val dateStr = "${date_picker_year.value.toString()}-${DateFormatUtils.formatTime(date_picker_month.value)}-${DateFormatUtils.formatTime(date_picker_date.value)}"

            val currentMillis = System.currentTimeMillis()
            val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(dateStr + " 23:59:59")
            if (currentMillis >= d.time) {
                showAlert(R.string.msg_can_not_set_date_before_current)
                return@setOnClickListener
            }

            val cal = Calendar.getInstance()
            cal.add(Calendar.DAY_OF_YEAR, 60)
            val after60 = cal.time.time

            if(d.time > after60){
                showAlert(R.string.msg_can_not_set_after_60)
                return@setOnClickListener
            }

            val data = Intent()
            data.putExtra(Const.DATA, dateStr)
            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }
}
