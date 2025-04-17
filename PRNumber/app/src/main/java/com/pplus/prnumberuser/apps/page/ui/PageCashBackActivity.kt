//package com.pplus.prnumberuser.apps.page.ui
//
//import android.content.Intent
//import android.location.Location
//import android.net.Uri
//import android.os.Bundle
//import android.provider.Settings
//import androidx.activity.result.ActivityResult
//import com.bumptech.glide.Glide
//import com.bumptech.glide.request.RequestOptions
//import com.pplus.networks.common.PplusCallback
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
//import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
//import com.pplus.prnumberuser.apps.common.builder.data.AlertData
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.my.ui.AlertSaveQrActivity
//import com.pplus.prnumberuser.apps.my.ui.CheckSavePointAuthCodeActivity
//import com.pplus.prnumberuser.apps.stamp.ui.StampActivity
//import com.pplus.prnumberuser.core.location.LocationUtil
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.LocationData
//import com.pplus.prnumberuser.core.network.model.dto.Page
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.logs.LogUtil
//import com.pplus.utils.part.utils.StringUtils
//import kotlinx.android.synthetic.main.activity_page_cash_back.*
//import retrofit2.Call
//import java.util.*
//import kotlin.collections.HashMap
//import kotlin.collections.set
//
//class PageCashBackActivity : BaseActivity(), ImplToolbar {
//
//    override fun getLayoutView(): Int {
//        return R.layout.activity_page_cash_back
//    }
//
//    override fun getPID(): String? {
//        return ""
//    }
//    var mPage: Page? = null
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//        mPage = intent.getParcelableExtra(Const.DATA)
//
//        mLocationListener = object :LocationListener{
//            override fun onLocation(result : ActivityResult) {
//                hideProgress()
//            }
//        }
//
//        getPage()
//    }
//
//    private fun getPage(){
//        val params = HashMap<String, String>()
//        params["no"] = mPage!!.no.toString()
//        showProgress("")
//        ApiBuilder.create().getPage(params).setCallback(object : PplusCallback<NewResultResponse<Page>> {
//            override fun onResponse(call: Call<NewResultResponse<Page>>?, response: NewResultResponse<Page>?) {
//                hideProgress()
//                if (response?.data != null) {
//                    mPage = response.data!!
//                    Glide.with(this@PageCashBackActivity).load(mPage!!.thumbnail).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(image_page_cash_back_thumbnail)
//                    text_page_cash_back_page_name.text = mPage!!.name
//
//                    image_page_cash_back_store.setOnClickListener {
//                        val location = IntArray(2)
//                        it.getLocationOnScreen(location)
//                        val x = location[0] + it.width / 2
//                        val y = location[1] + it.height / 2
//                        PplusCommonUtil.goPage(this@PageCashBackActivity, mPage!!, x, y)
//                    }
//
//                    image_page_cash_back_place.setOnClickListener {
//                        val intent = Intent(this@PageCashBackActivity, LocationPageActivity::class.java)
//                        intent.putExtra(Const.PAGE, mPage)
//                        startActivity(intent)
//                    }
//
//                    image_page_cash_back_call.setOnClickListener {
//                        if (StringUtils.isNotEmpty(mPage!!.phone)) {
//                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mPage!!.phone!!))
//                            startActivity(intent)
//                        }
//                    }
//
//                    text_page_cash_back_point.text = PplusCommonUtil.fromHtml(getString(R.string.html_save_cash, FormatUtil.getMoneyType(mPage!!.visitPoint.toString())))
//
//                    var minPrice = 6000
//                    if(mPage!!.visitMinPrice != null && mPage!!.visitMinPrice!! > 0){
//                        minPrice = mPage!!.visitMinPrice!!
//                    }
//
//                    text_page_cash_back_point_min_price_desc.text = PplusCommonUtil.fromHtml(getString(R.string.html_visit_save_desc, mPage!!.name, FormatUtil.getMoneyType(minPrice.toString())))
//
//                    text_page_cash_back_visit_save.setOnClickListener {
//                        if(StringUtils.isNotEmpty(mPage!!.echossId)){
//                            val intent = Intent(this@PageCashBackActivity, StampActivity::class.java)
//                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                            intent.putExtra(Const.PAGE, mPage)
//                            intent.putExtra(Const.TYPE, "visit")
//                            startActivity(intent)
//                        }else{
//                            checkLocation()
//                        }
//
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Page>>?, t: Throwable?, response: NewResultResponse<Page>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    private fun checkLocation(){
//        if(!LocationUtil.isLocationEnabled(this)){
//            val builder = AlertBuilder.Builder()
//            builder.setTitle(getString(R.string.word_notice_alert))
//            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
//            builder.addContents(AlertData.MessageData(getString(R.string.msg_on_locationService), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
//            builder.setBackgroundClickable(false)
//            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_setting))
//            builder.setOnAlertResultListener(object : OnAlertResultListener {
//
//
//                override fun onCancel() {
//
//                }
//
//                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//
//                    var intent: Intent? = null
//                    when (event_alert) {
//                        AlertBuilder.EVENT_ALERT.LEFT -> {
//                            finish()
//                        }
//                        AlertBuilder.EVENT_ALERT.RIGHT -> {
//                            intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//                            locationLauncher.launch(intent)
//                        }
//                    }
//                }
//            }).builder().show(this)
//        }else{
//            showProgress("")
//            LocationUtil.startLocationOnly(this, object : LocationUtil.LocationResultListener{
//                override fun result(location: Location?) {
//                    LogUtil.e(LOG_TAG, "onLocationChanged")
//
//                    hideProgress()
//                    if (location != null) {
//                        LogUtil.e("getCurrentLocation", "lat : {}, lon : {}", location.latitude, location.longitude)
//                        val locationData = LocationData()
//                        locationData.latitude = location.latitude
//                        locationData.longitude = location.longitude
//                        val distance = PplusCommonUtil.calDistance(location.latitude, location.longitude, mPage!!.latitude!!, mPage!!.longitude!!)
//                        if(distance < 100){
//                            val intent = Intent(this@PageCashBackActivity, CheckSavePointAuthCodeActivity::class.java)
//                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                            intent.putExtra(Const.PAGE, mPage)
//                            intent.putExtra(Const.TYPE, "visit")
//                            startActivity(intent)
//                            overridePendingTransition(0,0)
//                        }else{
//
//                            //qrcode화면
//                            val builder = AlertBuilder.Builder()
//                            builder.setTitle(getString(R.string.word_notice_alert))
//                            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
//                            builder.addContents(AlertData.MessageData(getString(R.string.html_alert_location_over), AlertBuilder.MESSAGE_TYPE.HTML, 2));
//
//                            builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_qr_cash_back), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
//                            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.msg_save_by_qr))
//                            builder.setOnAlertResultListener(object : OnAlertResultListener {
//                                override fun onCancel() {
//
//                                }
//
//                                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT?) {
//                                    when (event_alert) {
//                                        AlertBuilder.EVENT_ALERT.RIGHT -> {
//                                            val intent = Intent(this@PageCashBackActivity, AlertSaveQrActivity::class.java)
//                                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                                            startActivity(intent)
//                                            overridePendingTransition(R.anim.view_up, R.anim.fix)
//                                        }
//                                    }
//                                }
//                            }).builder().show(this@PageCashBackActivity)
//
//                        }
//                    } else {
//                        //qrcode화면
//                        val builder = AlertBuilder.Builder()
//                        builder.setTitle(getString(R.string.word_notice_alert))
//                        builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
//                        builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_not_find_location), AlertBuilder.MESSAGE_TYPE.HTML, 2));
//                        builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_qr_cash_back), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
//                        builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.msg_save_by_qr))
//                        builder.setOnAlertResultListener(object : OnAlertResultListener {
//                            override fun onCancel() {
//
//                            }
//
//                            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT?) {
//                                when (event_alert) {
//                                    AlertBuilder.EVENT_ALERT.RIGHT -> {
//                                        val intent = Intent(this@PageCashBackActivity, AlertSaveQrActivity::class.java)
//                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                                        startActivity(intent)
//                                        overridePendingTransition(R.anim.view_up, R.anim.fix)
//                                    }
//                                }
//                            }
//                        }).builder().show(this@PageCashBackActivity)
//                    }
//                }
//            })
//        }
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_my_around_cash_back), ToolbarOption.ToolbarMenu.LEFT)
//
//        return toolbarOption
//    }
//
//    override fun getOnToolbarClickListener(): OnToolbarListener {
//
//        return OnToolbarListener { v, toolbarMenu, tag ->
//            when (toolbarMenu) {
//                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
//                    onBackPressed()
//                }
//            }
//        }
//    }
//}