package com.pplus.prnumberbiz.apps.pages.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.pple.pplus.utils.part.utils.time.DateFormatUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_open_time_picker.*

class OpenTimePickerActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_open_time_picker
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        // Set scroller enabled
        open_time_picker_hour.isScrollerEnabled = true
        // Set wrap selector wheel
        open_time_picker_hour.wrapSelectorWheel = true
        // Set scroller enabled
        open_time_picker_min.isScrollerEnabled = true
        // Set wrap selector wheel
        open_time_picker_min.wrapSelectorWheel = true



        open_time_picker_confirm.setOnClickListener {
            val date = "${DateFormatUtils.formatTime(open_time_picker_hour.value)}:${DateFormatUtils.formatTime(open_time_picker_min.value)}"
            intent.putExtra(Const.DATA, date)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}
