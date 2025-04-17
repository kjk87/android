package com.pplus.prnumberbiz.apps.coupon.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.pple.pplus.utils.part.utils.StringUtils
import com.pple.pplus.utils.part.utils.time.DateFormatUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.common.ui.common.DatePickerActivity
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Goods
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import kotlinx.android.synthetic.main.activity_coupon_reg.*
import network.common.PplusCallback
import retrofit2.Call
import java.text.SimpleDateFormat

class CouponRegActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_coupon_reg
    }

    var mMode = EnumData.MODE.WRITE
    var mCoupon: Goods? = null
    var mExpireDate = ""

    override fun initializeView(savedInstanceState: Bundle?) {
        mCoupon = intent.getParcelableExtra(Const.DATA)

        image_coupon_reg_back.setOnClickListener {
            onBackPressed()
        }

        edit_coupon_reg_name.setSingleLine()
        edit_coupon_reg_origin_price.setSingleLine()
        edit_coupon_reg_sale_price.setSingleLine()
        edit_coupon_reg_use_time.setSingleLine()
        edit_coupon_reg_use_condition.setSingleLine()
        edit_coupon_reg_point.setSingleLine()

        if (mCoupon != null) {
            mMode = EnumData.MODE.UPDATE
            text_coupon_reg_title.setText(R.string.word_modify_coupon)


            if (StringUtils.isNotEmpty(mCoupon!!.name)) {
                edit_coupon_reg_name.setText(mCoupon!!.name)
            }

            if (mCoupon!!.originPrice != null) {
                edit_coupon_reg_origin_price.setText(mCoupon!!.originPrice.toString())
            }

            if (mCoupon!!.price != null) {
                edit_coupon_reg_sale_price.setText(mCoupon!!.price!!.toString())
            }

            if (mCoupon!!.expireDatetime != null) {
                text_coupon_reg_expire_date.text = SimpleDateFormat("yyyy.MM.dd").format(DateFormatUtils.PPLUS_DATE_FORMAT.parse(mCoupon!!.expireDatetime)) + " " + getString(R.string.word_until)
                mExpireDate = mCoupon!!.expireDatetime!!
            }

            if (StringUtils.isNotEmpty(mCoupon!!.timeOption)) {
                edit_coupon_reg_use_time.setText(mCoupon!!.timeOption)
            }

            if (StringUtils.isNotEmpty(mCoupon!!.serviceCondition)) {
                edit_coupon_reg_use_condition.setText(mCoupon!!.serviceCondition)
            }

            if (mCoupon!!.rewardLuckybol != null) {
                edit_coupon_reg_point.setText(mCoupon!!.rewardLuckybol.toString())
            }


        } else {
            mMode = EnumData.MODE.WRITE
            text_coupon_reg_title.setText(R.string.word_reg_coupon)
            val reRegCoupon = intent.getParcelableExtra<Goods>(Const.RE_REG_DATA)
            if(reRegCoupon != null){
                if (StringUtils.isNotEmpty(reRegCoupon.name)) {
                    edit_coupon_reg_name.setText(reRegCoupon.name)
                }

                if (reRegCoupon.originPrice != null) {
                    edit_coupon_reg_origin_price.setText(reRegCoupon.originPrice.toString())
                }

                if (reRegCoupon.price != null) {
                    edit_coupon_reg_sale_price.setText(reRegCoupon.price!!.toString())
                }

                if (StringUtils.isNotEmpty(reRegCoupon.timeOption)) {
                    edit_coupon_reg_use_time.setText(reRegCoupon.timeOption)
                }

                if (StringUtils.isNotEmpty(reRegCoupon.serviceCondition)) {
                    edit_coupon_reg_use_condition.setText(reRegCoupon.serviceCondition)
                }

                if (reRegCoupon.rewardLuckybol != null) {
                    edit_coupon_reg_point.setText(reRegCoupon.rewardLuckybol.toString())
                }
            }
        }

        text_coupon_reg_expire_date.setOnClickListener {
            val intent = Intent(this, DatePickerActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivityForResult(intent, Const.REQ_EXPIRE_DATE)
        }

        text_coupon_reg.setOnClickListener {
            reg()
        }
        text_coupon_reg2.setOnClickListener {
            reg()
        }

    }

    private fun isEmptyData(): Boolean {

        val name = edit_coupon_reg_name.text.toString().trim()

        if (StringUtils.isEmpty(name)) {
            showAlert(R.string.msg_input_coupon_name)
            return true
        }

        val origin_price = edit_coupon_reg_origin_price.text.toString().trim()

        if (StringUtils.isEmpty(origin_price)) {
            showAlert(R.string.msg_input_goods_origin_price)
            return true
        }

        val price = edit_coupon_reg_sale_price.text.toString().trim()

        if (StringUtils.isEmpty(price)) {
            showAlert(R.string.msg_input_coupon_sale_price)
            return true
        }

        if (origin_price.toInt() < price.toInt()) {
            showAlert(R.string.msg_sale_price_over)
            return true
        }

        if (price.toInt() < 1000) {
            showAlert(R.string.msg_input_over_1000)
            return true
        }

        if (!StringUtils.isNotEmpty(mExpireDate)) {
            showAlert(R.string.msg_input_expire_date)
            return true
        }

        return false
    }

    private fun reg() {
        if (isEmptyData()) {
            return
        }
        val builder = AlertBuilder.Builder()
        builder.setTitle(getString(R.string.word_notice_alert))
        when (mMode) {
            EnumData.MODE.WRITE -> {
                builder.addContents(AlertData.MessageData(getString(R.string.msg_question_coupon_reg), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
            }
            EnumData.MODE.UPDATE -> {
                builder.addContents(AlertData.MessageData(getString(R.string.msg_question_coupon_modify), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
            }
        }

        builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
        builder.setOnAlertResultListener(object : OnAlertResultListener {

            override fun onCancel() {

            }

            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                when (event_alert) {
                    AlertBuilder.EVENT_ALERT.RIGHT -> {

                        postGoods()

                    }
                }
            }
        }).builder().show(this)
    }

    private fun postGoods() {
        val params = Goods()

        params.isHotdeal = false
        params.isPlus = false
        params.isCoupon = true
        params.pageSeqNo = LoginInfoManager.getInstance().user.page!!.no

        val name = edit_coupon_reg_name.text.toString().trim()

        params.name = name
        params.description = name

        val originPrice = edit_coupon_reg_origin_price.text.toString().trim()
        params.originPrice = originPrice.toLong()


        val price = edit_coupon_reg_sale_price.text.toString().trim()

        params.price = price.toLong()

        if (StringUtils.isNotEmpty(mExpireDate)) {

            params.expireDatetime = mExpireDate
        }

        params.count = -1

        if (StringUtils.isNotEmpty(edit_coupon_reg_use_time.text.toString().trim())) {
            val time = edit_coupon_reg_use_time.text.toString().trim()
            params.timeOption = time
        }

        if (StringUtils.isNotEmpty(edit_coupon_reg_use_time.text.toString().trim())) {
            val condition = edit_coupon_reg_use_condition.text.toString().trim()
            params.serviceCondition = condition
        }


        if (StringUtils.isNotEmpty(edit_coupon_reg_point.text.toString().trim())) {
            val point = edit_coupon_reg_point.text.toString().trim().toLong()
            params.rewardLuckybol = point
        }

        params.type = "2"

        when (mMode) {
            EnumData.MODE.WRITE -> {

                showProgress(getString(R.string.msg_registration_of_goods))

                ApiBuilder.create().postGoods(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                    override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                        hideProgress()
                        showAlert(R.string.msg_registed_goods)
                        setResult(Activity.RESULT_OK)
                        finish()
                    }

                    override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                        hideProgress()
                    }
                }).build().call()
            }
            EnumData.MODE.UPDATE -> {
                mCoupon!!.name = params.name
                mCoupon!!.originPrice = params.originPrice
                mCoupon!!.price = params.price
                mCoupon!!.expireDatetime = params.expireDatetime
                mCoupon!!.timeOption = params.timeOption
                mCoupon!!.serviceCondition = params.serviceCondition
                mCoupon!!.rewardLuckybol = params.rewardLuckybol
                mCoupon!!.categorySeqNo =  mCoupon!!.category!!.seqNo
                mCoupon!!.pageSeqNo = mCoupon!!.page!!.seqNo

                showProgress(getString(R.string.msg_modification_of_goods))
                ApiBuilder.create().putGoods(mCoupon).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                    override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {

                        hideProgress()
                        showAlert(R.string.msg_modified_goods)
                        setResult(Activity.RESULT_OK)
                        finish()
                    }

                    override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                        hideProgress()
                    }
                }).build().call()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Const.REQ_EXPIRE_DATE -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        val expireDate = data.getStringExtra(Const.DATA)
                        mExpireDate = expireDate + " 23:59:59"
                        text_coupon_reg_expire_date.text = expireDate.replace("-", ".") + " " + getString(R.string.word_until)
                    }
                }
            }
        }
    }

    private fun isEditingData(): Boolean {
        val name = edit_coupon_reg_name.text.toString().trim()

        if (StringUtils.isNotEmpty(name)) {
            return true
        }

        val originPrice = edit_coupon_reg_origin_price.text.toString().trim()

        if (StringUtils.isNotEmpty(originPrice)) {
            return true
        }

        val salePrice = edit_coupon_reg_sale_price.text.toString().trim()

        if (StringUtils.isNotEmpty(salePrice)) {
            return true
        }

        val expireDate = text_coupon_reg_expire_date.text.toString().trim()

        if (StringUtils.isNotEmpty(expireDate)) {
            return true
        }

        val useTime = edit_coupon_reg_use_time.text.toString().trim()

        if (StringUtils.isNotEmpty(useTime)) {
            return true
        }

        val condition = edit_coupon_reg_use_condition.text.toString().trim()

        if (StringUtils.isNotEmpty(condition)) {
            return true
        }

        val point = edit_coupon_reg_point.text.toString().trim()

        if (StringUtils.isNotEmpty(point)) {
            return true
        }

        return false
    }

    override fun onBackPressed() {
        if (isEditingData()) {
            val builder = AlertBuilder.Builder()
            builder.setTitle(getString(R.string.word_notice_alert))
            builder.addContents(AlertData.MessageData(getString(R.string.msg_question_back), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
            builder.setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                    when (event_alert) {
                        AlertBuilder.EVENT_ALERT.RIGHT -> finish()
                    }
                }
            }).builder().show(this)
        } else {
            super.onBackPressed()
        }

    }
}
