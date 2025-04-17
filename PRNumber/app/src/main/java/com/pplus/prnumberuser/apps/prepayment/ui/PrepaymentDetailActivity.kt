package com.pplus.prnumberuser.apps.prepayment.ui

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.pplus.prnumberuser.core.location.LocationUtil
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.LocationData
import com.pplus.prnumberuser.core.network.model.dto.Prepayment
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivityPrepaymentDetailBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.logs.LogUtil
import retrofit2.Call

class PrepaymentDetailActivity : BaseActivity(), ImplToolbar {
    private lateinit var binding: ActivityPrepaymentDetailBinding

    override fun getLayoutView(): View {
        binding = ActivityPrepaymentDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    var mPrepayment: Prepayment? = null

    override fun initializeView(savedInstanceState: Bundle?) {

        mLocationListener = object :LocationListener{
            override fun onLocation(result : ActivityResult) {
                hideProgress()
            }
        }

        mPrepayment = intent.getParcelableExtra(Const.DATA)
        getPrepayment()
    }

    private fun getPrepayment(){
        val params = HashMap<String, String>()

        params["seqNo"] = mPrepayment!!.seqNo.toString()

        showProgress("")
        ApiBuilder.create().getPrepayment(params).setCallback(object : PplusCallback<NewResultResponse<Prepayment>> {
            override fun onResponse(call: Call<NewResultResponse<Prepayment>>?, response: NewResultResponse<Prepayment>?) {
                hideProgress()
                if (response?.data != null) {
                    mPrepayment = response.data
                    val page = mPrepayment!!.page
                    Glide.with(this@PrepaymentDetailActivity).load(page!!.thumbnail).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(binding.imagePrepaymentDetailThumbnail)
                    binding.textPrepaymentDetailPageName.text = page.name

                    setPrepayment(mPrepayment!!)

                    binding.textSubscriptionDetailBuy.setOnClickListener {
                        if (!PplusCommonUtil.loginCheck(this@PrepaymentDetailActivity, null)) {
                            return@setOnClickListener
                        }

                        checkLocation()
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Prepayment>>?, t: Throwable?, response: NewResultResponse<Prepayment>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun setPrepayment(item:Prepayment){

        binding.itemMainPrepayment.textMainPrepaymentPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.price!!.toInt().toString()))
        binding.itemMainPrepayment.textMainPrepaymentTotalPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType((item.price!! + item.addPrice!!).toInt().toString()))
        binding.textPrepaymentDetailTotalPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType((item.price!! + item.addPrice!!).toInt().toString()))
        binding.textPrepaymentDetailExpireDays.text = getString(R.string.format_prepayment_expire_date, "1${getString(R.string.word_year)}")
        binding.textPrepaymentDetailNote.text = item.notice

        //        holder.text_discount.visibility = View.GONE
        if (item.discount != null && item.discount!!.toInt() > 0) {
            binding.itemMainPrepayment.textMainPrepaymentDiscount.visibility = View.VISIBLE
            binding.itemMainPrepayment.textMainPrepaymentDiscount.text = PplusCommonUtil.fromHtml(getString(R.string.format_add_price_discount, item.discount!!.toInt().toString()))
        } else {
            binding.itemMainPrepayment.textMainPrepaymentDiscount.visibility = View.GONE
        }
    }

    private fun checkLocation(){
        if(!LocationUtil.isLocationEnabled(this)){
            val builder = AlertBuilder.Builder()
            builder.setTitle(getString(R.string.word_notice_alert))
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
            builder.addContents(AlertData.MessageData(getString(R.string.msg_not_find_location), AlertBuilder.MESSAGE_TYPE.HTML, 3));
            builder.setLeftText(getString(R.string.word_confirm)).setRightText(getString(R.string.msg_setting))
            builder.setOnAlertResultListener(object : OnAlertResultListener {
                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT?) {
                    when (event_alert) {
                        AlertBuilder.EVENT_ALERT.RIGHT -> {
                            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                            startActivity(intent)
                        }
                    }
                }
            }).builder().show(this)
        }else{
            showProgress("")
            LocationUtil.startLocationOnly(this, object : LocationUtil.LocationResultListener{
                override fun result(location: Location?) {
                    LogUtil.e(LOG_TAG, "onLocationChanged")
                    locationLog()
                    hideProgress()
                    if (location != null) {
                        LogUtil.e("getCurrentLocation", "lat : {}, lon : {}", location.latitude, location.longitude)
                        val locationData = LocationData()
                        locationData.latitude = location.latitude
                        locationData.longitude = location.longitude
                        val distance = PplusCommonUtil.calDistance(location.latitude, location.longitude, mPrepayment!!.page!!.latitude!!, mPrepayment!!.page!!.longitude!!)
                        if(distance < 100){
                            val intent = Intent(this@PrepaymentDetailActivity, PrepaymentPublishActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            intent.putExtra(Const.DATA, mPrepayment)
                            publishLauncher.launch(intent)
                        }else{
                            showAlert(getString(R.string.format_can_not_publish_prepayment_distance, "100"))
                        }
                    } else {
                        //qrcode화면
                        val builder = AlertBuilder.Builder()
                        builder.setTitle(getString(R.string.word_notice_alert))
                        builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
                        builder.addContents(AlertData.MessageData(getString(R.string.msg_not_find_location), AlertBuilder.MESSAGE_TYPE.HTML, 3));
                        builder.setLeftText(getString(R.string.word_confirm)).setRightText(getString(R.string.msg_setting))
                        builder.setOnAlertResultListener(object : OnAlertResultListener {
                            override fun onCancel() {

                            }

                            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT?) {
                                when (event_alert) {
                                    AlertBuilder.EVENT_ALERT.RIGHT -> {
                                        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                                        startActivity(intent)
                                    }
                                }
                            }
                        }).builder().show(this@PrepaymentDetailActivity)
                    }
                }
            })
        }
    }

    val publishLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        setResult(RESULT_OK)
        finish()
    }

    private fun locationLog(){
        val params = java.util.HashMap<String, String>()
        params["deviceId"] = PplusCommonUtil.getDeviceID()
        params["platform"] = "aos"
        params["serviceLog"] = "금액권 발급을 위한 현재위치조회"
        ApiBuilder.create().locationServiceLogSave(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {

            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {

            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_subscription), ToolbarOption.ToolbarMenu.LEFT)
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