package com.pplus.prnumberbiz.apps.coupon.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pple.pplus.utils.part.apps.resource.ResourceUtil
import com.pple.pplus.utils.part.format.FormatUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pple.pplus.utils.part.utils.time.DateFormatUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.BuyGoods
import com.pplus.prnumberbiz.core.network.model.dto.Count
import com.pplus.prnumberbiz.core.network.model.dto.Goods
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.network.model.response.SubResultResponse
import kotlinx.android.synthetic.main.activity_coupon_config_detail.*
import network.common.PplusCallback
import retrofit2.Call
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class CouponConfigDetailActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_coupon_config_detail
    }

    var mCoupon: Goods? = null
    override fun initializeView(savedInstanceState: Bundle?) {
        mCoupon = intent.getParcelableExtra(Const.DATA)
        setData()
    }

    private fun setData() {
        getGoodsLikeCount()
        getUseCount()
        val params = HashMap<String, String>()
        params["seqNo"] = mCoupon!!.seqNo.toString()
        showProgress("")

        ApiBuilder.create().getOneGoods(params).setCallback(object : PplusCallback<NewResultResponse<Goods>> {
            override fun onResponse(call: Call<NewResultResponse<Goods>>?, response: NewResultResponse<Goods>?) {
                hideProgress()
                if (response != null && response.data != null) {
                    mCoupon = response.data


                    if (LoginInfoManager.getInstance().user.page!!.profileImage != null) {
                        Glide.with(this@CouponConfigDetailActivity).load(LoginInfoManager.getInstance().user.page!!.profileImage!!.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_page_profile_circle_default).error(R.drawable.img_page_profile_circle_default)).into(image_coupon_config_detail_page_image)
                    } else {
                        image_coupon_config_detail_page_image.setImageResource(R.drawable.img_page_profile_circle_default)
                    }
                    if (mCoupon!!.represent!!) {
                        text_coupon_config_detail_represent_coupon.visibility = View.VISIBLE
                    } else {
                        text_coupon_config_detail_represent_coupon.visibility = View.GONE
                    }

                    text_coupon_config_detail_name.text = mCoupon!!.name
                    text_coupon_config_detail_name2.text = mCoupon!!.name

                    text_coupon_config_detail_origin_price.paintFlags = text_coupon_config_detail_origin_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    val originPrice = mCoupon!!.originPrice!!
                    text_coupon_config_detail_origin_price.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(originPrice.toString()))
                    text_coupon_config_detail_origin_price2.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(originPrice.toString()))

                    text_coupon_config_detail_re_reg.setOnClickListener {
                        val intent = Intent(this@CouponConfigDetailActivity, CouponRegActivity::class.java)
                        intent.putExtra(Const.RE_REG_DATA, mCoupon)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        startActivityForResult(intent, Const.REQ_REG)
                    }

                    var ing = false
                    if (mCoupon!!.status == EnumData.GoodsStatus.soldout.status) {

                        layout_coupon_config_detail_status.visibility = View.VISIBLE
                        text_coupon_config_detail_re_reg.visibility = View.VISIBLE
                        text_coupon_config_detail_re_reg.setText(R.string.msg_re_reg_coupon)
                        text_coupon_config_detail_status.setText(R.string.word_sold_out)
                        text_coupon_config_detail_status.setTextColor(ResourceUtil.getColor(this@CouponConfigDetailActivity, R.color.white))
                    } else if (mCoupon!!.status == EnumData.GoodsStatus.finish.status) {

                        layout_coupon_config_detail_status.visibility = View.VISIBLE
                        text_coupon_config_detail_re_reg.visibility = View.VISIBLE
                        text_coupon_config_detail_re_reg.setText(R.string.msg_re_reg_coupon)
                        text_coupon_config_detail_status.setText(R.string.word_sold_finish)
                        text_coupon_config_detail_status.setTextColor(ResourceUtil.getColor(this@CouponConfigDetailActivity, R.color.color_ff4646))
                        if (StringUtils.isNotEmpty(mCoupon!!.expireDatetime)) {
                            val format = SimpleDateFormat(DateFormatUtils.PPLUS_DATE_FORMAT.pattern, Locale.getDefault())
                            var date = Date()
                            try {
                                date = format.parse(mCoupon!!.expireDatetime)
                            } catch (e: ParseException) {
                                e.printStackTrace()
                            }

                            val currentMillis = System.currentTimeMillis()
                            if (date.time <= currentMillis) {
                                text_coupon_config_detail_status.text = getString(R.string.word_sold_finish) + "\n" + getString(R.string.word_expired)
                            }
                        }
                    } else if (mCoupon!!.status == EnumData.GoodsStatus.stop.status) {
                        layout_coupon_config_detail_status.visibility = View.VISIBLE
                        text_coupon_config_detail_re_reg.visibility = View.VISIBLE
                        text_coupon_config_detail_re_reg.setText(R.string.msg_resume_coupon)
                        text_coupon_config_detail_status.setText(R.string.word_sold_stop)
                        text_coupon_config_detail_status.setTextColor(ResourceUtil.getColor(this@CouponConfigDetailActivity, R.color.color_ff4646))

                        text_coupon_config_detail_re_reg.setOnClickListener {
                            putGoodsStatus(EnumData.GoodsStatus.ing.status)
                        }
                    } else {

                        //"expireDatetime":"2018-10-18 15:00:59"

                        if (StringUtils.isNotEmpty(mCoupon!!.expireDatetime)) {
                            val format = SimpleDateFormat(DateFormatUtils.PPLUS_DATE_FORMAT.pattern, Locale.getDefault())
                            var date = Date()
                            try {
                                date = format.parse(mCoupon!!.expireDatetime)
                            } catch (e: ParseException) {
                                e.printStackTrace()
                            }

                            val currentMillis = System.currentTimeMillis()
                            if (date.time <= currentMillis) {
                                layout_coupon_config_detail_status.visibility = View.VISIBLE
                                text_coupon_config_detail_re_reg.visibility = View.VISIBLE
                                text_coupon_config_detail_status.text = getString(R.string.word_sold_finish) + "\n" + getString(R.string.word_expired)
                                text_coupon_config_detail_status.setTextColor(ResourceUtil.getColor(this@CouponConfigDetailActivity, R.color.white))
                            } else {
                                layout_coupon_config_detail_status.visibility = View.GONE
                                text_coupon_config_detail_re_reg.visibility = View.GONE
                                ing = true
                            }
                        } else {
                            layout_coupon_config_detail_status.visibility = View.GONE
                            text_coupon_config_detail_re_reg.visibility = View.GONE
                            ing = true
                        }
                    }

                    if (ing) {
                        text_coupon_config_detail_name.setTextColor(ResourceUtil.getColor(this@CouponConfigDetailActivity, R.color.color_232323))
                        text_coupon_config_detail_sale_price.setTextColor(ResourceUtil.getColor(this@CouponConfigDetailActivity, R.color.color_ff4646))
                    } else {
                        text_coupon_config_detail_name.setTextColor(ResourceUtil.getColor(this@CouponConfigDetailActivity, R.color.color_b7b7b7))
                        text_coupon_config_detail_sale_price.setTextColor(ResourceUtil.getColor(this@CouponConfigDetailActivity, R.color.color_b7b7b7))
                    }
                    text_coupon_config_detail_sale_price.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(mCoupon!!.price.toString()))
                    text_coupon_config_detail_sale_price2.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(mCoupon!!.price.toString()))

                    if (StringUtils.isNotEmpty(mCoupon!!.expireDatetime)) {
                        text_coupon_config_detail_expire_date.visibility = View.VISIBLE
                        layout_coupon_config_detail_expire_date.visibility = View.VISIBLE
                        text_coupon_config_detail_expire_date.text = SimpleDateFormat("yyyy.MM.dd").format(DateFormatUtils.PPLUS_DATE_FORMAT.parse(mCoupon!!.expireDatetime)) + " " + getString(R.string.word_until)
                        text_coupon_config_detail_expire_date2.text = SimpleDateFormat("yyyy.MM.dd").format(DateFormatUtils.PPLUS_DATE_FORMAT.parse(mCoupon!!.expireDatetime)) + " " + getString(R.string.word_until)
                    } else {
                        text_coupon_config_detail_expire_date.visibility = View.GONE
                        layout_coupon_config_detail_expire_date.visibility = View.GONE
                    }

                    if (mCoupon!!.rewardLuckybol != null) {
                        layout_coupon_config_detail_save_point.visibility = View.VISIBLE
                        text_coupon_config_detail_save_point.text = getString(R.string.format_point_unit, FormatUtil.getMoneyType(mCoupon!!.rewardLuckybol.toString()))
                    } else {
                        layout_coupon_config_detail_save_point.visibility = View.GONE
                    }

                    if (StringUtils.isNotEmpty(mCoupon!!.timeOption)) {
                        layout_coupon_config_detail_use_time.visibility = View.VISIBLE
                        text_coupon_config_detail_use_time.text = mCoupon!!.timeOption
                    } else {
                        layout_coupon_config_detail_use_time.visibility = View.GONE
                    }
                    if (StringUtils.isNotEmpty(mCoupon!!.serviceCondition)) {
                        layout_coupon_config_detail_use_condition.visibility = View.VISIBLE
                        text_coupon_config_detail_use_condition.text = mCoupon!!.serviceCondition
                    } else {
                        layout_coupon_config_detail_use_condition.visibility = View.GONE
                    }

                    text_coupon_config_detail_sale_count.text = getString(R.string.format_count2, FormatUtil.getMoneyType(mCoupon!!.soldCount.toString()))
                    text_coupon_config_detail_reg_date.text = SimpleDateFormat("yyyy.MM.dd").format(DateFormatUtils.PPLUS_DATE_FORMAT.parse(mCoupon!!.regDatetime))
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Goods>>?, t: Throwable?, response: NewResultResponse<Goods>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun putGoodsStatus(status : Int){
        val params = HashMap<String, String>()
        params["seqNo"] = mCoupon!!.seqNo.toString()
        params["status"] = status.toString()
        showProgress("")
        ApiBuilder.create().putGoodsStatus(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()
                mCoupon!!.status = params["status"]!!.toInt()
                setResult(Activity.RESULT_OK)
                setData()
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun getGoodsLikeCount() {
        val params = HashMap<String, String>()

        params["pageSeqNo"] = LoginInfoManager.getInstance().user.page!!.no.toString()
        params["goodsSeqNo"] = mCoupon!!.seqNo.toString()
        ApiBuilder.create().getGoodsLikeCount(params).setCallback(object : PplusCallback<NewResultResponse<Count>> {
            override fun onResponse(call: Call<NewResultResponse<Count>>?, response: NewResultResponse<Count>?) {

                if (response != null && response.data != null) {
                    text_coupon_config_detail_download.text = getString(R.string.format_count2, FormatUtil.getMoneyType(response.data.count.toString()))
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Count>>?, t: Throwable?, response: NewResultResponse<Count>?) {

            }
        }).build().call()
    }

    private fun getUseCount() {
        val params = HashMap<String, String>()

        params["goodsSeqNo"] = mCoupon!!.seqNo.toString()
        params["process"] = "3"
        ApiBuilder.create().getBuyGoodsCount(params).setCallback(object : PplusCallback<NewResultResponse<Count>> {
            override fun onResponse(call: Call<NewResultResponse<Count>>?, response: NewResultResponse<Count>?) {
                if (response != null) {
                    val count = response.data.count
                    text_coupon_config_detail_use_count.text = getString(R.string.format_count2, FormatUtil.getMoneyType(count.toString()))
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Count>>?, t: Throwable?, response: NewResultResponse<Count>?) {

            }
        }).build().call()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            Const.REQ_REG->{
                if(resultCode == Activity.RESULT_OK){
                    setData()
                }

            }
        }
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_coupon_config), ToolbarOption.ToolbarMenu.LEFT)
        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, R.drawable.ic_post_set_edit)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
                ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
                    val builder = AlertBuilder.Builder()
                    builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER)

                    val contentsList = arrayListOf<String>()

                    if(mCoupon!!.represent == null || !mCoupon!!.represent!!){
                        builder.addContents(AlertData.MessageData(getString(R.string.word_set_represent_coupon), AlertBuilder.MESSAGE_TYPE.TEXT, 1))
                        contentsList.add(getString(R.string.word_set_represent_coupon))
                    }
                    builder.addContents(AlertData.MessageData(getString(R.string.word_sale_history), AlertBuilder.MESSAGE_TYPE.TEXT, 1))
                    contentsList.add(getString(R.string.word_sale_history))
                    if(mCoupon!!.status != EnumData.GoodsStatus.soldout.status){
                        builder.addContents(AlertData.MessageData(getString(R.string.word_sold_out), AlertBuilder.MESSAGE_TYPE.TEXT, 1))
                        contentsList.add(getString(R.string.word_sold_out))
                    }

                    when (mCoupon!!.status) {
                        EnumData.GoodsStatus.ing.status -> {
                            builder.addContents(AlertData.MessageData(getString(R.string.word_sold_stop), AlertBuilder.MESSAGE_TYPE.TEXT, 1))
                            contentsList.add(getString(R.string.word_sold_stop))
                        }
                        EnumData.GoodsStatus.stop.status -> {
                            builder.addContents(AlertData.MessageData(getString(R.string.word_sold_resume), AlertBuilder.MESSAGE_TYPE.TEXT, 1))
                            contentsList.add(getString(R.string.word_sold_resume))
                        }
                    }
                    builder.addContents(AlertData.MessageData(getString(R.string.word_modified), AlertBuilder.MESSAGE_TYPE.TEXT, 1))
                    builder.addContents(AlertData.MessageData(getString(R.string.word_delete), AlertBuilder.MESSAGE_TYPE.TEXT, 1))
                    contentsList.add(getString(R.string.word_modified))
                    contentsList.add(getString(R.string.word_delete))

                    builder.setLeftText(getString(R.string.word_cancel))
                    builder.setOnAlertResultListener(object : OnAlertResultListener {
                        override fun onCancel() {

                        }

                        override fun onResult(event_alert: AlertBuilder.EVENT_ALERT?) {
                            when (event_alert) {

                                AlertBuilder.EVENT_ALERT.LIST -> {
                                    when(contentsList[event_alert.value-1]){
                                        getString(R.string.word_set_represent_coupon)->{
                                            setMainGoods()
                                        }
                                        getString(R.string.word_sale_history)->{
                                            val intent = Intent(this@CouponConfigDetailActivity, CouponSaleHistoryActivity::class.java)
                                            intent.putExtra(Const.DATA, mCoupon!!)
                                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                            startActivity(intent)
                                        }
                                        getString(R.string.word_sold_out)->{
                                            if(mCoupon!!.represent != null && mCoupon!!.represent!!){
                                                showAlert(R.string.msg_can_not_change_represent_coupon)
                                                return
                                            }

                                            val builder = AlertBuilder.Builder()
                                            builder.setTitle(getString(R.string.word_notice_alert))
                                            builder.addContents(AlertData.MessageData(getString(R.string.msg_question_soldout_coupon), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                                            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
                                            builder.setOnAlertResultListener(object : OnAlertResultListener {

                                                override fun onCancel() {

                                                }

                                                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                                                    when (event_alert) {
                                                        AlertBuilder.EVENT_ALERT.RIGHT -> {
                                                            putGoodsStatus(EnumData.GoodsStatus.soldout.status)
                                                        }
                                                    }
                                                }
                                            }).builder().show(this@CouponConfigDetailActivity)

                                        }
                                        getString(R.string.word_sold_stop)->{
                                            if(mCoupon!!.represent != null && mCoupon!!.represent!!){
                                                showAlert(R.string.msg_can_not_change_represent_coupon)
                                                return
                                            }
                                            putGoodsStatus(EnumData.GoodsStatus.stop.status)
                                        }
                                        getString(R.string.word_sold_resume)->{
                                            putGoodsStatus(EnumData.GoodsStatus.ing.status)
                                        }
                                        getString(R.string.word_modified)->{
                                            val intent = Intent(this@CouponConfigDetailActivity, CouponRegActivity::class.java)
                                            intent.putExtra(Const.DATA, mCoupon!!)
                                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                            startActivityForResult(intent, Const.REQ_REG)
                                        }
                                        getString(R.string.word_delete)->{
                                            if(mCoupon!!.represent != null && mCoupon!!.represent!!){
                                                showAlert(R.string.msg_can_not_delete_represent_coupon)
                                                return
                                            }
                                            delete()
                                        }
                                    }
                                }
                            }
                        }
                    }).builder().show(this@CouponConfigDetailActivity)
                }
            }
        }

    }

    private fun setMainGoods(){
        val params = HashMap<String, String>()
        params["no"] = LoginInfoManager.getInstance().user.page!!.no.toString()
        params["mainGoodsSeqNo"] = mCoupon!!.seqNo.toString()

        showProgress("")
        ApiBuilder.create().putMainGoods(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()

                showAlert(R.string.msg_set_main_coupon)
                LoginInfoManager.getInstance().user.page!!.mainGoodsSeqNo = mCoupon!!.seqNo
                LoginInfoManager.getInstance().save()
                setData()
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun delete() {

        val builder = AlertBuilder.Builder()
        builder.setTitle(getString(R.string.word_notice_alert))
        builder.addContents(AlertData.MessageData(getString(R.string.msg_question_delete_coupon), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
        builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
        builder.setOnAlertResultListener(object : OnAlertResultListener {

            override fun onCancel() {

            }

            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                when (event_alert) {
                    AlertBuilder.EVENT_ALERT.RIGHT -> {
                        val params = HashMap<String, String>()
                        params["seqNo"] = mCoupon!!.seqNo.toString()
                        showProgress("")
                        ApiBuilder.create().deleteGoods(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                                hideProgress()
                                showAlert(R.string.msg_deleted_coupon)
                                setResult(Activity.RESULT_OK)
                                finish()
                            }

                            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                                hideProgress()
                                showAlert(R.string.msg_can_not_delete_history_coupon)
                            }
                        }).build().call()
                    }
                }
            }
        }).builder().show(this)
    }
}
