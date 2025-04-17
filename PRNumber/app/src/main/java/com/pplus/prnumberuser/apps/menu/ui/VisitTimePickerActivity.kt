package com.pplus.prnumberuser.apps.menu.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.databinding.ActivityVisitTimePickerBinding
import java.text.SimpleDateFormat
import java.util.*

class VisitTimePickerActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityVisitTimePickerBinding
    override fun getLayoutView(): View {
        binding = ActivityVisitTimePickerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

//        val currentTime = SimpleDateFormat("HH:mm").format(Date())
//        val currentTimes = currentTime.split(":")
//        val currentHour = currentTimes[0]

        // Set scroller enabled
        binding.dateVisitTimeHour.isScrollerEnabled = true
        // Set wrap selector wheel
        binding.dateVisitTimeHour.wrapSelectorWheel = true


        binding.dateVisitTimeHour.minValue = 0
        binding.dateVisitTimeHour.maxValue = 23

        // Set scroller enabled
        binding.dateVisitTimeMin.isScrollerEnabled = true
        // Set wrap selector wheel
        binding.dateVisitTimeMin.wrapSelectorWheel = true

        val data = arrayOf("00", "10", "20", "30", "40", "50")

        binding.dateVisitTimeMin.minValue = 0
        binding.dateVisitTimeMin.maxValue = data.size-1
        binding.dateVisitTimeMin.displayedValues = data
        binding.dateVisitTimeMin.value = 0


        binding.dateVisitTimeConfirm.setOnClickListener {

            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, binding.dateVisitTimeHour.value)
            calendar.set(Calendar.MINUTE, binding.dateVisitTimeMin.value*10)
            calendar.set(Calendar.SECOND, 0)
            val output = SimpleDateFormat("yyyy.MM.dd HH:mm")

            val data = Intent()
            data.putExtra(Const.DATA, output.format(calendar.time))
            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }
}
