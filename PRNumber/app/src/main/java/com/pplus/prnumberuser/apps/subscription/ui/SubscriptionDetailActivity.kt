package com.pplus.prnumberuser.apps.subscription.ui

import android.content.Intent
import android.graphics.Paint
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.ActivityResult
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
import com.pplus.prnumberuser.apps.page.ui.LocationPageActivity
import com.pplus.prnumberuser.apps.stamp.ui.StampSubscriptionActivity
import com.pplus.prnumberuser.core.location.LocationUtil
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.LocationData
import com.pplus.prnumberuser.core.network.model.dto.ProductPrice
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivitySubscriptionDetailBinding
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call

class SubscriptionDetailActivity : BaseActivity(), ImplToolbar {
    private lateinit var binding: ActivitySubscriptionDetailBinding

    override fun getLayoutView(): View {
        binding = ActivitySubscriptionDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    var mProductPrice: ProductPrice? = null

    override fun initializeView(savedInstanceState: Bundle?) {

        mLocationListener = object :LocationListener{
            override fun onLocation(result : ActivityResult) {
                hideProgress()
            }
        }

        mProductPrice = intent.getParcelableExtra(Const.DATA)
        getProductPrice()
    }

    private fun getProductPrice(){
        val params = HashMap<String, String>()

        params["seqNo"] = mProductPrice!!.seqNo.toString()

        showProgress("")
        ApiBuilder.create().getProductPrice(params).setCallback(object : PplusCallback<NewResultResponse<ProductPrice>> {
            override fun onResponse(call: Call<NewResultResponse<ProductPrice>>?, response: NewResultResponse<ProductPrice>?) {
                hideProgress()
                if (response?.data != null) {
                    mProductPrice = response.data
                    val page = mProductPrice!!.page
                    Glide.with(this@SubscriptionDetailActivity).load(page!!.thumbnail).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(binding.imageSubscriptionDetailThumbnail)
                    binding.textSubscriptionDetailPageName.text = page.name

                    binding.imageSubscriptionDetailStore.setOnClickListener {
                        val location = IntArray(2)
                        it.getLocationOnScreen(location)
                        val x = location[0] + it.width / 2
                        val y = location[1] + it.height / 2
                        PplusCommonUtil.goPage(this@SubscriptionDetailActivity, page, x, y)
                    }

                    binding.imageSubscriptionDetailPlace.setOnClickListener {
                        val intent = Intent(this@SubscriptionDetailActivity, LocationPageActivity::class.java)
                        intent.putExtra(Const.PAGE2, page)
                        startActivity(intent)
                    }

                    binding.imageSubscriptionDetailCall.setOnClickListener {
                        if (StringUtils.isNotEmpty(page.phone)) {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + page.phone!!))
                            startActivity(intent)
                        }
                    }

                    if(mProductPrice!!.isSubscription != null && mProductPrice!!.isSubscription!!){
                        setTitle(getString(R.string.word_subscription))
                        binding.textSubscriptionDetailItemTitle.text = getString(R.string.word_subscription)
                        binding.textSubscriptionDetailBuy.text = getString(R.string.msg_buy_subscription)
                        binding.textSubscriptionDetailBuy.setBackgroundResource(R.drawable.bg_579ffb_radius_15)
                        binding.textSubscriptionDetailBuy.setTextColor(ResourceUtil.getColor(this@SubscriptionDetailActivity, R.color.white))
                    }else{
                        setTitle(getString(R.string.word_money_product))
                        binding.textSubscriptionDetailItemTitle.text = getString(R.string.word_money_product)
                        binding.textSubscriptionDetailBuy.text = getString(R.string.msg_buy_prepayment)
                        binding.textSubscriptionDetailBuy.setBackgroundResource(R.drawable.bg_ffcf5c_radius_15)
                        binding.textSubscriptionDetailBuy.setTextColor(ResourceUtil.getColor(this@SubscriptionDetailActivity, R.color.color_4a3606))
                    }

                    setSubscription(mProductPrice!!)

                    binding.textSubscriptionDetailBuy.setOnClickListener {
                        if (!PplusCommonUtil.loginCheck(this@SubscriptionDetailActivity, null)) {
                            return@setOnClickListener
                        }

                        if(StringUtils.isNotEmpty(page.echossId)){
                            val intent = Intent(this@SubscriptionDetailActivity, StampSubscriptionActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            intent.putExtra(Const.PAGE, page)
                            intent.putExtra(Const.PRODUCT_PRICE, mProductPrice)
                            startActivity(intent)
                        }else{
                            checkLocation()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<ProductPrice>>?, t: Throwable?, response: NewResultResponse<ProductPrice>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun setSubscription(item:ProductPrice){

        if(item.isSubscription != null && item.isSubscription!!){
            binding.itemMainSubscription.layoutMainSubscriptionEnd.setBackgroundResource(R.drawable.bg_4694fb_right_radius_30)
            binding.itemMainSubscription.textMainSubscriptionDiscountRatio.setTextColor(ResourceUtil.getColor(this, R.color.white))
            binding.itemMainSubscription.textMainSubscriptionDesc.setTextColor(ResourceUtil.getColor(this, R.color.color_999999))
            binding.itemMainSubscription.textMainSubscriptionDesc.text = PplusCommonUtil.fromHtml(getString(R.string.html_subscription_count, item.times.toString()))
        }else{
            binding.itemMainSubscription.layoutMainSubscriptionEnd.setBackgroundResource(R.drawable.bg_ffcf5c_right_radius_30)
            binding.itemMainSubscription.textMainSubscriptionDiscountRatio.setTextColor(ResourceUtil.getColor(this, R.color.color_4a3606))
            binding.itemMainSubscription.textMainSubscriptionDesc.setTextColor(ResourceUtil.getColor(this, R.color.color_4a3606))
            binding.itemMainSubscription.textMainSubscriptionDesc.text = getString(R.string.word_remain_money_manage_type)
        }

        binding.itemMainSubscription.textMainSubscriptionName.text = item.product!!.name

        binding.itemMainSubscription.textMainSubscriptionOriginPrice.paintFlags = binding.itemMainSubscription.textMainSubscriptionOriginPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        if (item.originPrice != null && item.originPrice!! > 0) {

            if (item.originPrice!! <= item.price!!) {
                binding.itemMainSubscription.textMainSubscriptionOriginPrice.visibility = View.GONE
            } else {
                binding.itemMainSubscription.textMainSubscriptionOriginPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.originPrice.toString()))
                binding.itemMainSubscription.textMainSubscriptionOriginPrice.visibility = View.VISIBLE
            }

        } else {
            binding.itemMainSubscription.textMainSubscriptionOriginPrice.visibility = View.GONE
        }

        //        holder.text_discount.visibility = View.GONE
        if (item.discountRatio != null && item.discountRatio!!.toInt() > 0) {
            binding.itemMainSubscription.textMainSubscriptionDiscountRatio.visibility = View.VISIBLE
            binding.itemMainSubscription.textMainSubscriptionDiscountRatio.text = PplusCommonUtil.fromHtml(getString(R.string.html_percent_unit2, item.discountRatio!!.toInt().toString()))
        } else {
            binding.itemMainSubscription.textMainSubscriptionDiscountRatio.visibility = View.GONE
        }
        binding.itemMainSubscription.textMainSubscriptionSalePrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.price.toString()))

        if(item.isSubscription != null && item.isSubscription!!){
            binding.layoutSubscriptionDetailTimes.visibility = View.VISIBLE
            binding.layoutSubscriptionDetailUsePrice.visibility = View.GONE
            binding.textSubscriptionDetailTimes.text = getString(R.string.format_subscription_unit2, FormatUtil.getMoneyType(item.times.toString()))
            binding.textSubscriptionDetailContentsTitle.text = getString(R.string.word_use_condition)
            binding.textSubscriptionDetailMethodTitle.text = getString(R.string.word_subscription_download_method)

            binding.textSubscriptionDetailMethod1.text = getString(R.string.msg_subscription_publish_method_desc1)
            binding.textSubscriptionDetailMethod1.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.img_subscription_method1,0,0)

            binding.textSubscriptionDetailMethod2.text = getString(R.string.msg_subscription_publish_method_desc2)
            binding.textSubscriptionDetailMethod2.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.img_subscription_method2,0,0)

            binding.textSubscriptionDetailMethod3.text = getString(R.string.msg_subscription_publish_method_desc3)
            binding.textSubscriptionDetailMethod3.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.img_subscription_method3,0,0)
        }else{
            binding.layoutSubscriptionDetailTimes.visibility = View.GONE
            binding.layoutSubscriptionDetailUsePrice.visibility = View.VISIBLE
            binding.textSubscriptionDetailOriginPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.originPrice.toString()))
            binding.textSubscriptionDetailContentsTitle.text = getString(R.string.word_use_guide)
            binding.textSubscriptionDetailMethodTitle.text = getString(R.string.word_money_product_download_method)

            binding.textSubscriptionDetailMethod1.text = getString(R.string.msg_money_product_publish_method_desc1)
            binding.textSubscriptionDetailMethod1.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.img_money_product_method1,0,0)

            binding.textSubscriptionDetailMethod2.text = getString(R.string.msg_money_product_publish_method_desc2)
            binding.textSubscriptionDetailMethod2.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.img_money_product_method2,0,0)

            binding.textSubscriptionDetailMethod3.text = getString(R.string.msg_money_product_publish_method_desc3)
            binding.textSubscriptionDetailMethod3.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.img_money_product_method3,0,0)
        }

        if(item.remainDays == 365){
            binding.textSubscriptionDetailRemainDays.text = PplusCommonUtil.fromHtml(getString(R.string.html_remain_years, "1"))
        }else{
            binding.textSubscriptionDetailRemainDays.text = PplusCommonUtil.fromHtml(getString(R.string.html_remain_days, item.remainDays.toString()))
        }

        binding.textSubscriptionDetailContents.text = item.product!!.contents
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
                        val distance = PplusCommonUtil.calDistance(location.latitude, location.longitude, mProductPrice!!.page!!.latitude!!, mProductPrice!!.page!!.longitude!!)
                        if(distance < 100){
                            val intent = Intent(this@SubscriptionDetailActivity, CheckBuySubscriptionAuthCodeActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            intent.putExtra(Const.PRODUCT_PRICE, mProductPrice)
                            startActivity(intent)
                            overridePendingTransition(0,0)
                        }else{

                            //qrcode화면
                            val builder = AlertBuilder.Builder()
                            builder.setTitle(getString(R.string.word_notice_alert))
                            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
                            builder.addContents(AlertData.MessageData(getString(R.string.html_alert_location_over_subscription), AlertBuilder.MESSAGE_TYPE.HTML, 2))
                            builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_qr_buy_subscription), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
                            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.msg_buy_by_qr))
                            builder.setOnAlertResultListener(object : OnAlertResultListener {
                                override fun onCancel() {

                                }

                                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT?) {
                                    when (event_alert) {
                                        AlertBuilder.EVENT_ALERT.RIGHT -> {
                                            val intent = Intent(this@SubscriptionDetailActivity, AlertBuySubscriptionQrActivity::class.java)
                                            intent.putExtra(Const.PRODUCT_PRICE, mProductPrice)
                                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                            startActivity(intent)
                                            overridePendingTransition(R.anim.view_up, R.anim.fix)
                                        }
                                    }
                                }
                            }).builder().show(this@SubscriptionDetailActivity)

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
                                        val intent = Intent(this@SubscriptionDetailActivity, AlertBuySubscriptionQrActivity::class.java)
                                        intent.putExtra(Const.PRODUCT_PRICE, mProductPrice)
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                        startActivity(intent)
                                        overridePendingTransition(R.anim.view_up, R.anim.fix)
                                    }
                                }
                            }
                        }).builder().show(this@SubscriptionDetailActivity)
                    }
                }
            })
        }
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