package com.pplus.prnumberbiz.apps.pages.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pple.pplus.utils.part.logs.LogUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.CloseTime
import com.pplus.prnumberbiz.core.network.model.dto.OpenTime
import com.pplus.prnumberbiz.core.network.model.dto.PageManagement
import com.pplus.prnumberbiz.core.network.model.dto.Page
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import kotlinx.android.synthetic.main.activity_operation_info.*
import kotlinx.android.synthetic.main.item_holiday.view.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.*

class OperationInfoActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_operation_info
    }

    var mPage: Page? = null
    var mPageManagement: PageManagement? = null
    var mOpenType = 0

    override fun initializeView(savedInstanceState: Bundle?) {

        text_operation_info_back.setOnClickListener {
            onBackPressed()
        }

        mPage = LoginInfoManager.getInstance().user.page
        if (mPage!!.type == EnumData.PageTypeCode.store.name) {
            layout_operation_info_method.visibility = View.VISIBLE

            check_operation_info_store.setOnCheckedChangeListener { buttonView, isChecked ->
                mPage!!.isShopOrderable = isChecked
            }

            check_operation_info_packing.setOnCheckedChangeListener { buttonView, isChecked ->
                mPage!!.isPackingOrderable = isChecked
            }

            check_operation_info_delivery.setOnCheckedChangeListener { buttonView, isChecked ->
                mPage!!.isDeliveryOrderable = isChecked
                if (isChecked) {
                    layout_operation_info_delivery_round.visibility = View.VISIBLE
                    layout_operation_info_delivery_fee_min_price.visibility = View.VISIBLE
                } else {
                    layout_operation_info_delivery_round.visibility = View.GONE
                    layout_operation_info_delivery_fee_min_price.visibility = View.GONE
                }

            }

            check_operation_info_store.isChecked = mPage!!.isShopOrderable!!
            check_operation_info_packing.isChecked = mPage!!.isPackingOrderable!!
            check_operation_info_delivery.isChecked = mPage!!.isDeliveryOrderable!!
            if(mPage!!.isDeliveryOrderable!!){
                layout_operation_info_delivery_round.visibility = View.VISIBLE
                layout_operation_info_delivery_fee_min_price.visibility = View.VISIBLE
            }else{
                layout_operation_info_delivery_round.visibility = View.GONE
                layout_operation_info_delivery_fee_min_price.visibility = View.GONE
            }

        } else {
            layout_operation_info_method.visibility = View.GONE
            layout_operation_info_delivery_round.visibility = View.GONE
            layout_operation_info_delivery_fee_min_price.visibility = View.GONE
        }

        text_operation_info_operation_time_type.setOnClickListener {
            val builder = AlertBuilder.Builder()
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_BOTTOM)
            val contents = arrayOf(getString(R.string.word_every_day), getString(R.string.word_week_end), getString(R.string.word_each_day))
            builder.setContents(*contents)
            builder.setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                    when (event_alert) {
                        AlertBuilder.EVENT_ALERT.LIST -> when (event_alert.getValue()) {
                            1 -> {
                                text_operation_info_operation_time_type.setText(R.string.word_every_day)
                                layout_operation_info_time_every_day.visibility = View.VISIBLE
                                layout_operation_info_time_week_end.visibility = View.GONE
                                layout_operation_info_time_each_day.visibility = View.GONE
                                mOpenType = 0
                            }
                            2 -> {
                                text_operation_info_operation_time_type.setText(R.string.word_week_end)
                                layout_operation_info_time_every_day.visibility = View.GONE
                                layout_operation_info_time_week_end.visibility = View.VISIBLE
                                layout_operation_info_time_each_day.visibility = View.GONE
                                mOpenType = 1
                            }
                            3->{
                                text_operation_info_operation_time_type.setText(R.string.word_each_day)
                                layout_operation_info_time_every_day.visibility = View.GONE
                                layout_operation_info_time_week_end.visibility = View.GONE
                                layout_operation_info_time_each_day.visibility = View.VISIBLE
                                mOpenType = 2
                            }
                        }
                    }
                }
            }).builder().show(this)
        }

        text_operation_info_parking.setOnClickListener {
            val builder = AlertBuilder.Builder()
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_BOTTOM)
            val contents = arrayOf(getString(R.string.word_parking_enable), getString(R.string.word_parking_disable))
            builder.setContents(*contents)
            builder.setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                    when (event_alert) {
                        AlertBuilder.EVENT_ALERT.LIST -> when (event_alert.getValue()) {
                            1 -> {
                                mPage!!.isParkingAvailable = true
                            }
                            2 -> {
                                mPage!!.isParkingAvailable = false
                            }
                        }
                    }
                    setParkingInfo()
                }
            }).builder().show(this)
        }

        text_operation_info_valet_parking.setOnClickListener {
            val builder = AlertBuilder.Builder()
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_BOTTOM)
            val contents = arrayOf(getString(R.string.word_valet_parking_enable), getString(R.string.word_valet_parking_disable))
            builder.setContents(*contents)
            builder.setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                    when (event_alert) {
                        AlertBuilder.EVENT_ALERT.LIST -> when (event_alert.getValue()) {
                            1 -> {
                                mPage!!.isValetParkingAvailable = true
                            }
                            2 -> {
                                mPage!!.isValetParkingAvailable = false
                            }
                        }
                    }
                    setParkingInfo()
                }
            }).builder().show(this)
        }

        setParkingInfo()

        text_operation_info_delivery_round.setOnClickListener {
            val builder = AlertBuilder.Builder()
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_BOTTOM)
            val contents = arrayOfNulls<String>(10)
            for (i in 1..10) {
                contents[i - 1] = getString(R.string.format_delivery_round, i.toString())
            }
            builder.setContents(*contents)
            builder.setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                    when (event_alert) {
                        AlertBuilder.EVENT_ALERT.LIST -> {
                            val value = event_alert.getValue()
                            mPageManagement!!.deliveryRadius = value.toDouble()
                        }
                    }
                    setDeliveryRound()
                }
            }).builder().show(this)
        }

        text_operation_info_every_day_start_time.setOnClickListener {
            val intent = Intent(this, OpenTimePickerActivity::class.java)
            intent.putExtra(Const.TYPE, 0)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivityForResult(intent, Const.REQ_START_DATE)
        }
        text_operation_info_every_day_end_time.setOnClickListener {
            val intent = Intent(this, OpenTimePickerActivity::class.java)
            intent.putExtra(Const.TYPE, 0)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivityForResult(intent, Const.REQ_END_DATE)
        }

        text_operation_info_week_start_time.setOnClickListener {
            val intent = Intent(this, OpenTimePickerActivity::class.java)
            intent.putExtra(Const.TYPE, 1)
            intent.putExtra(Const.KEY, "week")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivityForResult(intent, Const.REQ_START_DATE)
        }

        text_operation_info_week_end_time.setOnClickListener {
            val intent = Intent(this, OpenTimePickerActivity::class.java)
            intent.putExtra(Const.TYPE, 1)
            intent.putExtra(Const.KEY, "week")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivityForResult(intent, Const.REQ_END_DATE)
        }

        text_operation_info_end_start_time.setOnClickListener {
            val intent = Intent(this, OpenTimePickerActivity::class.java)
            intent.putExtra(Const.TYPE, 1)
            intent.putExtra(Const.KEY, "end")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivityForResult(intent, Const.REQ_START_DATE)
        }

        text_operation_info_end_end_time.setOnClickListener {
            val intent = Intent(this, OpenTimePickerActivity::class.java)
            intent.putExtra(Const.TYPE, 1)
            intent.putExtra(Const.KEY, "end")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivityForResult(intent, Const.REQ_END_DATE)
        }

        text_operation_info_mon_start_time.setOnClickListener {
            val intent = Intent(this, OpenTimePickerActivity::class.java)
            intent.putExtra(Const.TYPE, 2)
            intent.putExtra(Const.KEY, "mon")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivityForResult(intent, Const.REQ_START_DATE)
        }

        text_operation_info_mon_end_time.setOnClickListener {
            val intent = Intent(this, OpenTimePickerActivity::class.java)
            intent.putExtra(Const.TYPE, 2)
            intent.putExtra(Const.KEY, "mon")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivityForResult(intent, Const.REQ_END_DATE)
        }

        text_operation_info_tue_start_time.setOnClickListener {
            val intent = Intent(this, OpenTimePickerActivity::class.java)
            intent.putExtra(Const.TYPE, 2)
            intent.putExtra(Const.KEY, "tue")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivityForResult(intent, Const.REQ_START_DATE)
        }

        text_operation_info_tue_end_time.setOnClickListener {
            val intent = Intent(this, OpenTimePickerActivity::class.java)
            intent.putExtra(Const.TYPE, 2)
            intent.putExtra(Const.KEY, "tue")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivityForResult(intent, Const.REQ_END_DATE)
        }

        text_operation_info_wed_start_time.setOnClickListener {
            val intent = Intent(this, OpenTimePickerActivity::class.java)
            intent.putExtra(Const.TYPE, 2)
            intent.putExtra(Const.KEY, "wed")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivityForResult(intent, Const.REQ_START_DATE)
        }

        text_operation_info_wed_end_time.setOnClickListener {
            val intent = Intent(this, OpenTimePickerActivity::class.java)
            intent.putExtra(Const.TYPE, 2)
            intent.putExtra(Const.KEY, "wed")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivityForResult(intent, Const.REQ_END_DATE)
        }

        text_operation_info_thu_start_time.setOnClickListener {
            val intent = Intent(this, OpenTimePickerActivity::class.java)
            intent.putExtra(Const.TYPE, 2)
            intent.putExtra(Const.KEY, "thu")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivityForResult(intent, Const.REQ_START_DATE)
        }

        text_operation_info_thu_end_time.setOnClickListener {
            val intent = Intent(this, OpenTimePickerActivity::class.java)
            intent.putExtra(Const.TYPE, 2)
            intent.putExtra(Const.KEY, "thu")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivityForResult(intent, Const.REQ_END_DATE)
        }

        text_operation_info_fri_start_time.setOnClickListener {
            val intent = Intent(this, OpenTimePickerActivity::class.java)
            intent.putExtra(Const.TYPE, 2)
            intent.putExtra(Const.KEY, "fri")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivityForResult(intent, Const.REQ_START_DATE)
        }

        text_operation_info_fri_end_time.setOnClickListener {
            val intent = Intent(this, OpenTimePickerActivity::class.java)
            intent.putExtra(Const.TYPE, 2)
            intent.putExtra(Const.KEY, "fri")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivityForResult(intent, Const.REQ_END_DATE)
        }

        text_operation_info_sat_start_time.setOnClickListener {
            val intent = Intent(this, OpenTimePickerActivity::class.java)
            intent.putExtra(Const.TYPE, 2)
            intent.putExtra(Const.KEY, "sat")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivityForResult(intent, Const.REQ_START_DATE)
        }

        text_operation_info_sat_end_time.setOnClickListener {
            val intent = Intent(this, OpenTimePickerActivity::class.java)
            intent.putExtra(Const.TYPE, 2)
            intent.putExtra(Const.KEY, "sat")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivityForResult(intent, Const.REQ_END_DATE)
        }

        text_operation_info_sun_start_time.setOnClickListener {
            val intent = Intent(this, OpenTimePickerActivity::class.java)
            intent.putExtra(Const.TYPE, 2)
            intent.putExtra(Const.KEY, "sun")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivityForResult(intent, Const.REQ_START_DATE)
        }

        text_operation_info_sun_end_time.setOnClickListener {
            val intent = Intent(this, OpenTimePickerActivity::class.java)
            intent.putExtra(Const.TYPE, 2)
            intent.putExtra(Const.KEY, "sun")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivityForResult(intent, Const.REQ_END_DATE)
        }

        text_operation_info_save.setOnClickListener {
            save()
        }

        text_operation_info_save2.setOnClickListener {
            save()
        }

        text_operation_info_add_holiday.setOnClickListener {
            val view_holiday = layoutInflater.inflate(R.layout.item_holiday, null)
            addHolidayView(view_holiday)
        }

        getPageManagement()
    }

    private fun save() {

        mPageManagement!!.pageSeqNo = mPage!!.no

        if (mPage!!.type == EnumData.PageTypeCode.store.name) {
            if (!mPage!!.isShopOrderable!! && !mPage!!.isPackingOrderable!! && !mPage!!.isDeliveryOrderable!!) {
                showAlert(R.string.msg_select_operation_method)
                return
            }

            if (mPage!!.isDeliveryOrderable!!) {
                if (mPageManagement!!.deliveryRadius == null) {
                    mPageManagement!!.deliveryRadius = 2.toDouble()
                }

                val deliveryFee = edit_operation_info_delivery_fee.text.toString().trim()
                if (StringUtils.isNotEmpty(deliveryFee)) {
                    mPageManagement!!.deliveryFee = deliveryFee.toFloat()
                } else {
                    mPageManagement!!.deliveryFee = 0f
                }

                val deliveryMinPrice = edit_operation_info_min_delivery_price.text.toString().trim()
                if (StringUtils.isNotEmpty(deliveryMinPrice)) {
                    mPageManagement!!.deliveryMinPrice = deliveryMinPrice.toFloat()
                } else {
                    mPageManagement!!.deliveryMinPrice = 0f
                }

            }
        }

        mPageManagement!!.originDesc = edit_operation_info_origin_indication.text.toString().trim()

        val openTimeList = arrayListOf<OpenTime>()
        when (mOpenType) {
            0 -> {
                val startTime = text_operation_info_every_day_start_time.text.toString()
                val endTime = text_operation_info_every_day_end_time.text.toString()

                if (StringUtils.isEmpty(startTime)) {
                    showAlert(R.string.msg_select_start_time)
                    return
                }

                if (StringUtils.isEmpty(endTime)) {
                    showAlert(R.string.msg_select_end_time)
                    return
                }

                val openTime = OpenTime()
                openTime.type = mOpenType
                openTime.startTime = startTime
                openTime.endTime = endTime
                openTime.nextDay = isNextDay(startTime, endTime)
                openTimeList.add(openTime)
            }
            1 -> {
                var startTime = text_operation_info_week_start_time.text.toString()
                var endTime = text_operation_info_week_end_time.text.toString()

                if (StringUtils.isEmpty(startTime)) {
                    showAlert(R.string.msg_select_start_time)
                    return
                }

                if (StringUtils.isEmpty(endTime)) {
                    showAlert(R.string.msg_select_end_time)
                    return
                }

                var openTime = OpenTime()
                openTime.type = mOpenType
                openTime.startTime = startTime
                openTime.endTime = endTime
                openTime.nextDay = isNextDay(startTime, endTime)
                openTimeList.add(openTime)

                startTime = text_operation_info_end_start_time.text.toString()
                endTime = text_operation_info_end_end_time.text.toString()

                if (StringUtils.isEmpty(startTime)) {
                    showAlert(R.string.msg_select_start_time)
                    return
                }

                if (StringUtils.isEmpty(endTime)) {
                    showAlert(R.string.msg_select_end_time)
                    return
                }

                openTime = OpenTime()
                openTime.type = mOpenType
                openTime.startTime = startTime
                openTime.endTime = endTime
                openTime.nextDay = isNextDay(startTime, endTime)
                openTimeList.add(openTime)
            }
            2 -> {
                var startTime = text_operation_info_mon_start_time.text.toString()
                var endTime = text_operation_info_mon_end_time.text.toString()

                if (StringUtils.isEmpty(startTime)) {
                    showAlert(R.string.msg_select_start_time)
                    return
                }

                if (StringUtils.isEmpty(endTime)) {
                    showAlert(R.string.msg_select_end_time)
                    return
                }

                var openTime = OpenTime()
                openTime.type = mOpenType
                openTime.startTime = startTime
                openTime.endTime = endTime
                openTime.weekDay = "mon"
                openTime.nextDay = isNextDay(startTime, endTime)
                openTimeList.add(openTime)

                startTime = text_operation_info_tue_start_time.text.toString()
                endTime = text_operation_info_tue_end_time.text.toString()

                if (StringUtils.isEmpty(startTime)) {
                    showAlert(R.string.msg_select_start_time)
                    return
                }

                if (StringUtils.isEmpty(endTime)) {
                    showAlert(R.string.msg_select_end_time)
                    return
                }

                openTime = OpenTime()
                openTime.type = mOpenType
                openTime.startTime = startTime
                openTime.endTime = endTime
                openTime.weekDay = "tue"
                openTime.nextDay = isNextDay(startTime, endTime)
                openTimeList.add(openTime)

                startTime = text_operation_info_wed_start_time.text.toString()
                endTime = text_operation_info_wed_end_time.text.toString()

                if (StringUtils.isEmpty(startTime)) {
                    showAlert(R.string.msg_select_start_time)
                    return
                }

                if (StringUtils.isEmpty(endTime)) {
                    showAlert(R.string.msg_select_end_time)
                    return
                }

                openTime = OpenTime()
                openTime.type = mOpenType
                openTime.startTime = startTime
                openTime.endTime = endTime
                openTime.weekDay = "wed"
                openTime.nextDay = isNextDay(startTime, endTime)
                openTimeList.add(openTime)

                startTime = text_operation_info_thu_start_time.text.toString()
                endTime = text_operation_info_thu_end_time.text.toString()

                if (StringUtils.isEmpty(startTime)) {
                    showAlert(R.string.msg_select_start_time)
                    return
                }

                if (StringUtils.isEmpty(endTime)) {
                    showAlert(R.string.msg_select_end_time)
                    return
                }

                openTime = OpenTime()
                openTime.type = mOpenType
                openTime.startTime = startTime
                openTime.endTime = endTime
                openTime.weekDay = "thu"
                openTime.nextDay = isNextDay(startTime, endTime)
                openTimeList.add(openTime)

                startTime = text_operation_info_fri_start_time.text.toString()
                endTime = text_operation_info_fri_end_time.text.toString()

                if (StringUtils.isEmpty(startTime)) {
                    showAlert(R.string.msg_select_start_time)
                    return
                }

                if (StringUtils.isEmpty(endTime)) {
                    showAlert(R.string.msg_select_end_time)
                    return
                }

                openTime = OpenTime()
                openTime.type = mOpenType
                openTime.startTime = startTime
                openTime.endTime = endTime
                openTime.weekDay = "fri"
                openTime.nextDay = isNextDay(startTime, endTime)
                openTimeList.add(openTime)

                startTime = text_operation_info_sat_start_time.text.toString()
                endTime = text_operation_info_sat_end_time.text.toString()

                if (StringUtils.isEmpty(startTime)) {
                    showAlert(R.string.msg_select_start_time)
                    return
                }

                if (StringUtils.isEmpty(endTime)) {
                    showAlert(R.string.msg_select_end_time)
                    return
                }

                openTime = OpenTime()
                openTime.type = mOpenType
                openTime.startTime = startTime
                openTime.endTime = endTime
                openTime.weekDay = "sat"
                openTime.nextDay = isNextDay(startTime, endTime)
                openTimeList.add(openTime)

                startTime = text_operation_info_sun_start_time.text.toString()
                endTime = text_operation_info_sun_end_time.text.toString()

                if (StringUtils.isEmpty(startTime)) {
                    showAlert(R.string.msg_select_start_time)
                    return
                }

                if (StringUtils.isEmpty(endTime)) {
                    showAlert(R.string.msg_select_end_time)
                    return
                }

                openTime = OpenTime()
                openTime.type = mOpenType
                openTime.startTime = startTime
                openTime.endTime = endTime
                openTime.weekDay = "sun"
                openTime.nextDay = isNextDay(startTime, endTime)
                openTimeList.add(openTime)
            }
        }

        mPageManagement!!.opentimeList = openTimeList

        if (layout_operation_info_holiday.childCount > 0) {
            val closeList = arrayListOf<CloseTime>()

            for(i in 0 until layout_operation_info_holiday.childCount) {
                val view = layout_operation_info_holiday.getChildAt(i)

                val holiday_type = view.text_holiday_type.text.toString()
                val holiday_day_of_week = view.text_holiday_day_of_week.text.toString()

                if (StringUtils.isEmpty(holiday_type) || StringUtils.isEmpty(holiday_day_of_week)) {
                    showAlert(R.string.msg_input_holiday_info)
                    return
                }

                val closeTime = CloseTime()
                when (holiday_type) {
                    getString(R.string.word_every_week) -> {
                        closeTime.everyWeek = 0
                    }
                    getString(R.string.word_1_week) -> {
                        closeTime.everyWeek = 1
                    }
                    getString(R.string.word_2_week) -> {
                        closeTime.everyWeek = 2
                    }
                    getString(R.string.word_3_week) -> {
                        closeTime.everyWeek = 3
                    }
                    getString(R.string.word_4_week) -> {
                        closeTime.everyWeek = 5
                    }
                    getString(R.string.word_5_week) -> {
                        closeTime.everyWeek = 6
                    }
                }
                when (holiday_day_of_week) {
                    getString(R.string.word_mon) -> {
                        closeTime.weekDay = "mon"
                    }
                    getString(R.string.word_tue) -> {
                        closeTime.weekDay = "tue"
                    }
                    getString(R.string.word_wed) -> {
                        closeTime.weekDay = "wed"
                    }
                    getString(R.string.word_thu) -> {
                        closeTime.weekDay = "thu"
                    }
                    getString(R.string.word_fri) -> {
                        closeTime.weekDay = "fri"
                    }
                    getString(R.string.word_sat) -> {
                        closeTime.weekDay = "sat"
                    }
                    getString(R.string.word_sun) -> {
                        closeTime.weekDay = "sun"
                    }
                }
                closeList.add(closeTime)
            }

            mPageManagement!!.closedList = closeList

        }

        showProgress("")
        ApiBuilder.create().postPageManagement(mPageManagement).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()
                showProgress("")
                ApiBuilder.create().updatePage(mPage).setCallback(object : PplusCallback<NewResultResponse<Page>> {
                    override fun onResponse(call: Call<NewResultResponse<Page>>?, response: NewResultResponse<Page>?) {
                        hideProgress()
                        showAlert(R.string.msg_modified)
                        setResult(Activity.RESULT_OK)
                        finish()
                    }

                    override fun onFailure(call: Call<NewResultResponse<Page>>?, t: Throwable?, response: NewResultResponse<Page>?) {
                        hideProgress()
                    }
                }).build().call()
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun isNextDay(startTime: String, endTime: String): Boolean {
        val startHour = startTime.split(":")[0].toInt()
        val startMin = startTime.split(":")[1].toInt()

        val endHour = endTime.split(":")[0].toInt()
        val endMin = endTime.split(":")[1].toInt()

        return (startHour * 60 + startMin) >= (endHour * 60 + endMin)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Const.REQ_START_DATE -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        val type = data.getIntExtra(Const.TYPE, 0)
                        when (type) {
                            0 -> {
                                text_operation_info_every_day_start_time.text = data.getStringExtra(Const.DATA)
                            }
                            1 -> {
                                val key = data.getStringExtra(Const.KEY)
                                when (key) {
                                    "week" -> {
                                        text_operation_info_week_start_time.text = data.getStringExtra(Const.DATA)
                                    }
                                    "end" -> {
                                        text_operation_info_end_start_time.text = data.getStringExtra(Const.DATA)
                                    }
                                }
                            }
                            2 -> {
                                val key = data.getStringExtra(Const.KEY)
                                when (key) {
                                    "mon" -> {
                                        text_operation_info_mon_start_time.text = data.getStringExtra(Const.DATA)
                                    }
                                    "tue" -> {
                                        text_operation_info_tue_start_time.text = data.getStringExtra(Const.DATA)
                                    }
                                    "wed" -> {
                                        text_operation_info_wed_start_time.text = data.getStringExtra(Const.DATA)
                                    }
                                    "thu" -> {
                                        text_operation_info_thu_start_time.text = data.getStringExtra(Const.DATA)
                                    }
                                    "fri" -> {
                                        text_operation_info_fri_start_time.text = data.getStringExtra(Const.DATA)
                                    }
                                    "sat" -> {
                                        text_operation_info_sat_start_time.text = data.getStringExtra(Const.DATA)
                                    }
                                    "sun" -> {
                                        text_operation_info_sun_start_time.text = data.getStringExtra(Const.DATA)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Const.REQ_END_DATE -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        val type = data.getIntExtra(Const.TYPE, 0)
                        when (type) {
                            0 -> {
                                text_operation_info_every_day_end_time.text = data.getStringExtra(Const.DATA)
                            }
                            1 -> {
                                val key = data.getStringExtra(Const.KEY)
                                when (key) {
                                    "week" -> {
                                        text_operation_info_week_end_time.text = data.getStringExtra(Const.DATA)
                                    }
                                    "end" -> {
                                        text_operation_info_end_end_time.text = data.getStringExtra(Const.DATA)
                                    }
                                }
                            }
                            2 -> {
                                val key = data.getStringExtra(Const.KEY)
                                when (key) {
                                    "mon" -> {
                                        text_operation_info_mon_end_time.text = data.getStringExtra(Const.DATA)
                                    }
                                    "tue" -> {
                                        text_operation_info_tue_end_time.text = data.getStringExtra(Const.DATA)
                                    }
                                    "wed" -> {
                                        text_operation_info_wed_end_time.text = data.getStringExtra(Const.DATA)
                                    }
                                    "thu" -> {
                                        text_operation_info_thu_end_time.text = data.getStringExtra(Const.DATA)
                                    }
                                    "fri" -> {
                                        text_operation_info_fri_end_time.text = data.getStringExtra(Const.DATA)
                                    }
                                    "sat" -> {
                                        text_operation_info_sat_end_time.text = data.getStringExtra(Const.DATA)
                                    }
                                    "sun" -> {
                                        text_operation_info_sun_end_time.text = data.getStringExtra(Const.DATA)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getPageManagement() {
        val params = HashMap<String, String>()
        params["no"] = mPage!!.no.toString()
        showProgress("")
        ApiBuilder.create().getPageManagement(params).setCallback(object : PplusCallback<NewResultResponse<PageManagement>> {
            override fun onResponse(call: Call<NewResultResponse<PageManagement>>?, response: NewResultResponse<PageManagement>?) {
                hideProgress()
                if (response != null) {
                    mPageManagement = response.data

                    setDeliveryRound()

                    if (mPageManagement!!.deliveryFee != null) {
                        edit_operation_info_delivery_fee.setText(mPageManagement!!.deliveryFee!!.toInt().toString())
                    }

                    if (mPageManagement!!.deliveryMinPrice != null) {
                        edit_operation_info_min_delivery_price.setText(mPageManagement!!.deliveryMinPrice!!.toInt().toString())
                    }

                    if(StringUtils.isNotEmpty(mPageManagement!!.originDesc)){
                        edit_operation_info_origin_indication.setText(mPageManagement!!.originDesc)
                    }

                    val openTimeList = mPageManagement!!.opentimeList

                    if (openTimeList != null && openTimeList.isNotEmpty()) {
                        mOpenType = openTimeList[0].type!!
                        when (mOpenType) {
                            0 -> {
                                text_operation_info_operation_time_type.setText(R.string.word_every_day)
                                layout_operation_info_time_every_day.visibility = View.VISIBLE
                                layout_operation_info_time_week_end.visibility = View.GONE
                                layout_operation_info_time_each_day.visibility = View.GONE
                                text_operation_info_every_day_start_time.text = openTimeList[0].startTime
                                text_operation_info_every_day_end_time.text = openTimeList[0].endTime
                            }
                            1 -> {
                                text_operation_info_operation_time_type.setText(R.string.word_week_end)
                                layout_operation_info_time_every_day.visibility = View.GONE
                                layout_operation_info_time_week_end.visibility = View.VISIBLE
                                layout_operation_info_time_each_day.visibility = View.GONE
                                text_operation_info_week_start_time.text = openTimeList[0].startTime
                                text_operation_info_week_end_time.text = openTimeList[0].endTime
                                text_operation_info_end_start_time.text = openTimeList[1].startTime
                                text_operation_info_end_end_time.text = openTimeList[1].endTime
                            }
                            2 -> {
                                text_operation_info_operation_time_type.setText(R.string.word_each_day)
                                layout_operation_info_time_every_day.visibility = View.GONE
                                layout_operation_info_time_week_end.visibility = View.GONE
                                layout_operation_info_time_each_day.visibility = View.VISIBLE
                                text_operation_info_mon_start_time.text = openTimeList[0].startTime
                                text_operation_info_mon_end_time.text = openTimeList[0].startTime
                                text_operation_info_tue_start_time.text = openTimeList[1].startTime
                                text_operation_info_tue_end_time.text = openTimeList[1].startTime
                                text_operation_info_wed_start_time.text = openTimeList[2].startTime
                                text_operation_info_wed_end_time.text = openTimeList[2].endTime
                                text_operation_info_thu_start_time.text = openTimeList[3].startTime
                                text_operation_info_thu_end_time.text = openTimeList[3].endTime
                                text_operation_info_fri_start_time.text = openTimeList[4].startTime
                                text_operation_info_fri_end_time.text = openTimeList[4].endTime
                                text_operation_info_sat_start_time.text = openTimeList[5].startTime
                                text_operation_info_sat_end_time.text = openTimeList[5].endTime
                                text_operation_info_sun_start_time.text = openTimeList[6].startTime
                                text_operation_info_sun_end_time.text = openTimeList[6].endTime
                            }
                        }

                    } else {
                        text_operation_info_operation_time_type.setText(R.string.word_every_day)
                        layout_operation_info_time_every_day.visibility = View.VISIBLE
                        layout_operation_info_time_week_end.visibility = View.GONE
                        layout_operation_info_time_each_day.visibility = View.GONE
                        mOpenType = 0
                    }

                    val closeTimeList = mPageManagement!!.closedList

                    layout_operation_info_holiday.removeAllViews()
                    if (closeTimeList != null && closeTimeList.isNotEmpty()) {

                        layout_operation_info_holiday.visibility = View.GONE
                        layout_operation_info_holiday_not_exist.visibility = View.VISIBLE

                        for (i in 0 until closeTimeList.size) {
                            val view_holiday = layoutInflater.inflate(R.layout.item_holiday, null)


                            when (closeTimeList[i].everyWeek) {
                                0 -> {
                                    view_holiday.text_holiday_type.setText(R.string.word_every_week)
                                }
                                1 -> {
                                    view_holiday.text_holiday_type.setText(R.string.word_1_week)
                                }
                                2 -> {
                                    view_holiday.text_holiday_type.setText(R.string.word_2_week)
                                }
                                3 -> {
                                    view_holiday.text_holiday_type.setText(R.string.word_3_week)
                                }
                                4 -> {
                                    view_holiday.text_holiday_type.setText(R.string.word_4_week)
                                }
                                5 -> {
                                    view_holiday.text_holiday_type.setText(R.string.word_5_week)
                                }
                            }

                            when (closeTimeList[i].weekDay) {
                                "mon" -> {
                                    view_holiday.text_holiday_day_of_week.setText(R.string.word_mon)
                                }
                                "tue" -> {
                                    view_holiday.text_holiday_day_of_week.setText(R.string.word_tue)
                                }
                                "wed" -> {
                                    view_holiday.text_holiday_day_of_week.setText(R.string.word_wed)
                                }
                                "thu" -> {
                                    view_holiday.text_holiday_day_of_week.setText(R.string.word_thu)
                                }
                                "fri" -> {
                                    view_holiday.text_holiday_day_of_week.setText(R.string.word_fri)
                                }
                                "sat" -> {
                                    view_holiday.text_holiday_day_of_week.setText(R.string.word_sat)
                                }
                                "sun" -> {
                                    view_holiday.text_holiday_day_of_week.setText(R.string.word_sun)
                                }
                            }
                            addHolidayView(view_holiday)
                        }

                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<PageManagement>>?, t: Throwable?, response: NewResultResponse<PageManagement>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun addHolidayView(view: View) {
        view.text_holiday_type.setOnClickListener {
            val builder = AlertBuilder.Builder()
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_BOTTOM)
            val contents = arrayOf(getString(R.string.word_every_week), getString(R.string.word_1_week), getString(R.string.word_2_week), getString(R.string.word_3_week), getString(R.string.word_4_week), getString(R.string.word_5_week))
            builder.setContents(*contents)
            builder.setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                    when (event_alert) {
                        AlertBuilder.EVENT_ALERT.LIST -> when (event_alert.getValue() - 1) {
                            0 -> {
                                view.text_holiday_type.setText(R.string.word_every_week)
                            }
                            1 -> {
                                view.text_holiday_type.setText(R.string.word_1_week)
                            }
                            2 -> {
                                view.text_holiday_type.setText(R.string.word_2_week)
                            }
                            3 -> {
                                view.text_holiday_type.setText(R.string.word_3_week)
                            }
                            4 -> {
                                view.text_holiday_type.setText(R.string.word_4_week)
                            }
                            5 -> {
                                view.text_holiday_type.setText(R.string.word_5_week)
                            }
                        }
                    }

                }
            }).builder().show(this@OperationInfoActivity)
        }

        view.text_holiday_day_of_week.setOnClickListener {
            val builder = AlertBuilder.Builder()
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_BOTTOM)
            val contents = arrayOf(getString(R.string.word_mon), getString(R.string.word_tue), getString(R.string.word_wed), getString(R.string.word_thu), getString(R.string.word_fri), getString(R.string.word_sat), getString(R.string.word_sun))
            builder.setContents(*contents)
            builder.setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                    when (event_alert) {
                        AlertBuilder.EVENT_ALERT.LIST -> when (event_alert.getValue()) {
                            1 -> {
                                view.text_holiday_day_of_week.setText(R.string.word_mon)
                            }
                            2 -> {
                                view.text_holiday_day_of_week.setText(R.string.word_tue)
                            }
                            3 -> {
                                view.text_holiday_day_of_week.setText(R.string.word_wed)
                            }
                            4 -> {
                                view.text_holiday_day_of_week.setText(R.string.word_thu)
                            }
                            5 -> {
                                view.text_holiday_day_of_week.setText(R.string.word_fri)
                            }
                            6 -> {
                                view.text_holiday_day_of_week.setText(R.string.word_sat)
                            }
                            7 -> {
                                view.text_holiday_day_of_week.setText(R.string.word_sun)
                            }
                        }
                    }

                }
            }).builder().show(this@OperationInfoActivity)
        }

        view.image_holiday_delete.setOnClickListener {
            layout_operation_info_holiday.removeView(view)

            setHolidayLayout()

        }

        layout_operation_info_holiday.addView(view)
        setHolidayLayout()
    }

    private fun setHolidayLayout(){
        LogUtil.e(LOG_TAG, "size: {}", layout_operation_info_holiday.childCount)
        if(layout_operation_info_holiday.childCount == 0){
            layout_operation_info_holiday.visibility = View.GONE
            layout_operation_info_holiday_not_exist.visibility = View.VISIBLE
        }else{
            layout_operation_info_holiday.visibility = View.VISIBLE
            layout_operation_info_holiday_not_exist.visibility = View.GONE
        }
    }

    private fun setDeliveryRound() {
        if (mPageManagement!!.deliveryRadius == null) {
            mPageManagement!!.deliveryRadius = 2.toDouble()
        }

        text_operation_info_delivery_round.text = getString(R.string.format_delivery_round, mPageManagement!!.deliveryRadius!!.toInt().toString())
    }

    private fun setParkingInfo() {
        if (mPage!!.isParkingAvailable!!) {
            text_operation_info_parking.setText(R.string.word_parking_enable)
            layout_operation_info_valet_parking.visibility = View.VISIBLE
            if (mPage!!.isValetParkingAvailable!!) {
                text_operation_info_valet_parking.setText(R.string.word_valet_parking_enable)
            } else {
                text_operation_info_valet_parking.setText(R.string.word_valet_parking_disable)
            }
        } else {
            text_operation_info_parking.setText(R.string.word_parking_disable)
            layout_operation_info_valet_parking.visibility = View.GONE
        }
    }

}
