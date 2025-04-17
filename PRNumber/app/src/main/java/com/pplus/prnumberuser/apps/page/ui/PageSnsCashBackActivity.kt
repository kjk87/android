package com.pplus.prnumberuser.apps.page.ui

import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberuser.apps.common.builder.data.AlertData
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.my.ui.AlertSaveQrActivity
import com.pplus.prnumberuser.apps.my.ui.CheckSavePointAuthCodeActivity
import com.pplus.prnumberuser.apps.page.data.HashTagValueAdapter
import com.pplus.prnumberuser.apps.stamp.ui.StampActivity
import com.pplus.prnumberuser.core.location.LocationUtil
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.LocationData
import com.pplus.prnumberuser.core.network.model.dto.Page
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivityPageSnsCashBackBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.set

class PageSnsCashBackActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityPageSnsCashBackBinding

    override fun getLayoutView(): View {
        binding = ActivityPageSnsCashBackBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }
    var mPage: Page? = null



    override fun initializeView(savedInstanceState: Bundle?) {
        mPage = intent.getParcelableExtra(Const.DATA)

        mLocationListener = object :LocationListener{
            override fun onLocation(result : ActivityResult) {
                hideProgress()
            }
        }

        getPage()
    }

    private fun getPage(){
        val params = HashMap<String, String>()
        params["no"] = mPage!!.no.toString()
        showProgress("")
        ApiBuilder.create().getPage(params).setCallback(object : PplusCallback<NewResultResponse<Page>> {
            override fun onResponse(call: Call<NewResultResponse<Page>>?, response: NewResultResponse<Page>?) {
                hideProgress()
                if (response?.data != null) {
                    mPage = response.data!!
                    Glide.with(this@PageSnsCashBackActivity).load(mPage!!.thumbnail).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(binding.imagePageSnsCashBackThumbnail)
                    binding.textPageSnsCashBackPageName.text = mPage!!.name

                    binding.imagePageSnsCashBackStore.setOnClickListener {
                        val location = IntArray(2)
                        it.getLocationOnScreen(location)
                        val x = location[0] + it.width / 2
                        val y = location[1] + it.height / 2
                        PplusCommonUtil.goPage(this@PageSnsCashBackActivity, mPage!!, x, y)
                    }

                    binding.imagePageSnsCashBackPlace.setOnClickListener {
                        val intent = Intent(this@PageSnsCashBackActivity, LocationPageActivity::class.java)
                        intent.putExtra(Const.PAGE, mPage)
                        startActivity(intent)
                    }

                    binding.imagePageSnsCashBackCall.setOnClickListener {
                        if (StringUtils.isNotEmpty(mPage!!.phone)) {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mPage!!.phone!!))
                            startActivity(intent)
                        }
                    }


                    if(mPage!!.useSns != null && mPage!!.useSns!!){
                        binding.layoutPageSnsCashBackSnsSave.visibility = View.VISIBLE
                        binding.textPageSnsCashBackSnsPoint.text = PplusCommonUtil.fromHtml(getString(R.string.html_save_cash, FormatUtil.getMoneyType(mPage!!.snsPoint.toString())))
                        binding.textPageSnsCashBackPointSnsDesc.text = PplusCommonUtil.fromHtml(getString(R.string.html_sns_save_desc, mPage!!.name))

                        val keywordList = ArrayList<String>()
                        if (StringUtils.isNotEmpty(mPage!!.hashtag)) {
                            keywordList.addAll(mPage!!.hashtag!!.split(",").toMutableList())
                        }

                        val layoutManager = GridLayoutManager(this@PageSnsCashBackActivity, 30)
                        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {

                            override fun getSpanSize(position: Int): Int {

                                val keyWord = keywordList[position]

                                return if (keyWord.length < 3) {
                                    7
                                } else if (keyWord.length < 4) {
                                    8
                                } else if (keyWord.length < 5) {
                                    9
                                } else if (keyWord.length < 6) {
                                    10
                                } else if (keyWord.length < 7) {
                                    12
                                } else if (keyWord.length < 8) {
                                    13
                                } else if (keyWord.length < 10) {
                                    14
                                } else {
                                    20
                                }
                            }
                        }

                        binding.recyclerPageSnsCashBackPointSnsHashtag.layoutManager = layoutManager
                        val adapter = HashTagValueAdapter()
                        adapter.setDataList(keywordList)
                        binding.recyclerPageSnsCashBackPointSnsHashtag.adapter = adapter

                        binding.textPageSnsCashBackSnsSave.setOnClickListener {
                            if(StringUtils.isNotEmpty(mPage!!.echossId)){
                                val intent = Intent(this@PageSnsCashBackActivity, StampActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                intent.putExtra(Const.PAGE, mPage)
                                intent.putExtra(Const.TYPE, "sns")
                                startActivity(intent)
                            }else{
                                checkLocation()
                            }

                        }
                    }else{
                        binding.layoutPageSnsCashBackSnsSave.visibility = View.GONE
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Page>>?, t: Throwable?, response: NewResultResponse<Page>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun checkLocation(){
        if(!LocationUtil.isLocationEnabled(this)){
            val builder = AlertBuilder.Builder()
            builder.setTitle(getString(R.string.word_notice_alert))
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
            builder.addContents(AlertData.MessageData(getString(R.string.msg_on_locationService), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
            builder.setBackgroundClickable(false)
            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_setting))
            builder.setOnAlertResultListener(object : OnAlertResultListener {


                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                    var intent: Intent? = null
                    when (event_alert) {
                        AlertBuilder.EVENT_ALERT.LEFT -> {
                            finish()
                        }
                        AlertBuilder.EVENT_ALERT.RIGHT -> {
                            intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                            locationLauncher.launch(intent)
                        }
                    }
                }
            }).builder().show(this)
        }else{
            showProgress("")
            LocationUtil.startLocationOnly(this, object : LocationUtil.LocationResultListener{
                override fun result(location: Location?) {
                    LogUtil.e(LOG_TAG, "onLocationChanged")

                    hideProgress()
                    if (location != null) {
                        LogUtil.e("getCurrentLocation", "lat : {}, lon : {}", location.latitude, location.longitude)
                        val locationData = LocationData()
                        locationData.latitude = location.latitude
                        locationData.longitude = location.longitude
                        val distance = PplusCommonUtil.calDistance(location.latitude, location.longitude, mPage!!.latitude!!, mPage!!.longitude!!)
                        if(distance < 100){
                            val intent = Intent(this@PageSnsCashBackActivity, CheckSavePointAuthCodeActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            intent.putExtra(Const.PAGE, mPage)
                            intent.putExtra(Const.TYPE, "sns")
                            startActivity(intent)
                            overridePendingTransition(0,0)
                        }else{

                            //qrcode화면
                            val builder = AlertBuilder.Builder()
                            builder.setTitle(getString(R.string.word_notice_alert))
                            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
                            builder.addContents(AlertData.MessageData(getString(R.string.html_alert_location_over_sns), AlertBuilder.MESSAGE_TYPE.HTML, 2));

                            builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_qr_cash_back), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
                            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.msg_save_by_qr))
                            builder.setOnAlertResultListener(object : OnAlertResultListener {
                                override fun onCancel() {

                                }

                                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT?) {
                                    when (event_alert) {
                                        AlertBuilder.EVENT_ALERT.RIGHT -> {
                                            val intent = Intent(this@PageSnsCashBackActivity, AlertSaveQrActivity::class.java)
                                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                            startActivity(intent)
                                            overridePendingTransition(R.anim.view_up, R.anim.fix)
                                        }
                                    }
                                }
                            }).builder().show(this@PageSnsCashBackActivity)

                        }
                    } else {
                        //qrcode화면
                        val builder = AlertBuilder.Builder()
                        builder.setTitle(getString(R.string.word_notice_alert))
                        builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
                        builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_not_find_location), AlertBuilder.MESSAGE_TYPE.HTML, 2));
                        builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_qr_cash_back), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
                        builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.msg_save_by_qr))
                        builder.setOnAlertResultListener(object : OnAlertResultListener {
                            override fun onCancel() {

                            }

                            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT?) {
                                when (event_alert) {
                                    AlertBuilder.EVENT_ALERT.RIGHT -> {
                                        val intent = Intent(this@PageSnsCashBackActivity, AlertSaveQrActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                        startActivity(intent)
                                        overridePendingTransition(R.anim.view_up, R.anim.fix)
                                    }
                                }
                            }
                        }).builder().show(this@PageSnsCashBackActivity)
                    }
                }
            })
        }
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_my_around_cash_back), ToolbarOption.ToolbarMenu.LEFT)

        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressed()
                    }
                }
            }
        }
    }
}