package com.pplus.prnumberbiz.apps.goods.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.pple.pplus.utils.part.utils.time.DateFormatUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.core.network.model.dto.DateData
import kotlinx.android.synthetic.main.activity_goods_date_picker.*
import java.util.*

class GoodsDatePickerActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_goods_date_picker
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        // Set scroller enabled
        goods_date_picker_year.isScrollerEnabled = true
        // Set wrap selector wheel
        goods_date_picker_year.wrapSelectorWheel = true
        goods_date_picker_year.minValue = Calendar.getInstance().get(Calendar.YEAR)
        goods_date_picker_year.maxValue = Calendar.getInstance().get(Calendar.YEAR) + 100
        goods_date_picker_year.value = Calendar.getInstance().get(Calendar.YEAR)

        // Set scroller enabled
        goods_date_picker_month.isScrollerEnabled = true
        // Set wrap selector wheel
        goods_date_picker_month.wrapSelectorWheel = true
        // Set scroller enabled
        goods_date_picker_day.isScrollerEnabled = true
        // Set wrap selector wheel
        goods_date_picker_day.wrapSelectorWheel = true

        goods_date_picker_hour.isScrollerEnabled = true
        goods_date_picker_hour.wrapSelectorWheel = true

        goods_date_picker_minute.isScrollerEnabled = true
        goods_date_picker_minute.wrapSelectorWheel = true

        goods_date_picker_confirm.setOnClickListener {

            val dateData = DateData(goods_date_picker_year.value.toString(),
                    DateFormatUtils.formatTime(goods_date_picker_month.value),
                    DateFormatUtils.formatTime(goods_date_picker_day.value),
                    DateFormatUtils.formatTime(goods_date_picker_hour.value),
                    DateFormatUtils.formatTime(goods_date_picker_minute.value))

            val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse("${dateData.year}-${dateData.month}-${dateData.day} ${dateData.hour}:${dateData.min}:59")

            if (System.currentTimeMillis() >= d.time) {

                showAlert(R.string.msg_can_not_set_date_before_current)
                return@setOnClickListener
            }

            val data = Intent()
            data.putExtra(Const.DATA, dateData)
            setResult(Activity.RESULT_OK, data)
            finish()
        }

        image_goods_date_picker_close.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }
}
