package com.pplus.prnumberbiz.apps.goods.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import android.view.View
import android.widget.LinearLayout
import com.nightonke.boommenu.Animation.OrderEnum
import com.nightonke.boommenu.BoomButtons.HamButton
import com.nightonke.boommenu.BoomMenuButton
import com.pple.pplus.utils.part.apps.resource.ResourceUtil
import com.pple.pplus.utils.part.format.FormatUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pple.pplus.utils.part.utils.time.DateFormatUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.goods.data.GoodsImagePagerAdapter
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Goods
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import com.pplus.prnumberbiz.core.util.ToastUtil
import kotlinx.android.synthetic.main.activity_plus_goods_detail.*
import kotlinx.android.synthetic.main.item_goods_more.view.*
import network.common.PplusCallback
import retrofit2.Call
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class PlusGoodsDetailActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_plus_goods_detail
    }

    var mGoods: Goods? = null
    var mType = EnumData.GoodsType.plus.name

    override fun initializeView(savedInstanceState: Bundle?) {
        mGoods = intent.getParcelableExtra(Const.DATA)
        mType = intent.getStringExtra(Const.TYPE)
        getGoods()
    }

    private fun getGoods() {
        val params = HashMap<String, String>()

        params["seqNo"] = mGoods!!.seqNo.toString()

        showProgress("")

        ApiBuilder.create().getOneGoods(params).setCallback(object : PplusCallback<NewResultResponse<Goods>> {
            override fun onResponse(call: Call<NewResultResponse<Goods>>?, response: NewResultResponse<Goods>?) {
                hideProgress()
                if (response != null && response.data != null) {
                    mGoods = response.data

                    text_plus_goods_detail_origin_price.paintFlags = text_plus_goods_detail_origin_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                    text_plus_goods_detail_name.text = mGoods!!.name
                    text_plus_goods_detail_contents.text = mGoods!!.description

                    text_plus_goods_detail_origin_price.visibility = View.GONE

                    if (mGoods!!.originPrice != null) {

                        val origin_price = mGoods!!.originPrice!!

                        if (origin_price > mGoods!!.price!!) {
                            text_plus_goods_detail_origin_price.visibility = View.VISIBLE

                            text_plus_goods_detail_origin_price.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(origin_price.toString()))

                        }
                    }

                    if (StringUtils.isNotEmpty(mGoods!!.startTime) && StringUtils.isNotEmpty(mGoods!!.endTime)) {
                        text_plus_goods_detail_use_time.visibility = View.VISIBLE
                        text_plus_goods_detail_use_time.text = getString(R.string.format_use_time, mGoods!!.startTime, mGoods!!.endTime)
                    } else {
                        text_plus_goods_detail_use_time.visibility = View.GONE
                    }

                    if (mGoods!!.rewardPrReviewLink != null && mGoods!!.rewardPrReviewLink!! > 0) {
                        text_plus_goods_detail_reward.visibility = View.VISIBLE
                        text_plus_goods_detail_reward.text = PplusCommonUtil.fromHtml(getString(R.string.html_reward_point, FormatUtil.getMoneyType(mGoods!!.rewardPrReviewLink!!.toString())))

                    } else {
                        text_plus_goods_detail_reward.visibility = View.GONE
                    }

                    text_plus_goods_detail_sale_price.text = FormatUtil.getMoneyType(mGoods!!.price.toString())

                    if (mGoods!!.status == EnumData.GoodsStatus.soldout.status) {
                        text_plus_goods_detail_remain_count.visibility = View.GONE
                    }

                    if (mGoods!!.count != null && mGoods!!.count != -1) {
                        var soldCount = 0
                        if (mGoods!!.soldCount != null) {
                            soldCount = mGoods!!.soldCount!!
                        }
                        text_plus_goods_detail_remain_count.visibility = View.VISIBLE
                        text_plus_goods_detail_remain_count.text = PplusCommonUtil.fromHtml(getString(R.string.html_remain_count, FormatUtil.getMoneyType((mGoods!!.count!! - soldCount).toString())))
                    } else {
                        text_plus_goods_detail_remain_count.visibility = View.GONE
                    }


                    if (mGoods!!.attachments != null && mGoods!!.attachments!!.images != null && mGoods!!.attachments!!.images!!.isNotEmpty()) {
                        layout_plus_goods_detail_image.visibility = View.VISIBLE

                        if (mGoods!!.attachments!!.images!!.size > 1) {
                            indicator_plus_goods_detail.visibility = View.VISIBLE
                        } else {
                            indicator_plus_goods_detail.visibility = View.GONE
                        }

                        val imageAdapter = GoodsImagePagerAdapter(this@PlusGoodsDetailActivity)
                        imageAdapter.dataList = mGoods!!.attachments!!.images!! as ArrayList<String>

                        pager_plus_goods_detail_image.adapter = imageAdapter
                        indicator_plus_goods_detail.visibility = View.VISIBLE
                        indicator_plus_goods_detail.removeAllViews()
                        indicator_plus_goods_detail.build(LinearLayout.HORIZONTAL, mGoods!!.attachments!!.images!!.size)
                        pager_plus_goods_detail_image.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {

                            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                            }

                            override fun onPageSelected(position: Int) {

                                indicator_plus_goods_detail.setCurrentItem(position)
                            }

                            override fun onPageScrollStateChanged(state: Int) {

                            }
                        })

                    } else {
                        layout_plus_goods_detail_image.visibility = View.GONE
                    }

                    setGoodsStatus()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Goods>>?, t: Throwable?, response: NewResultResponse<Goods>?) {
                hideProgress()
            }
        }).build().call()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Const.REQ_MODIFY -> {
                if (resultCode == Activity.RESULT_OK) {
                    getGoods()
                    setResult(Activity.RESULT_OK)
                }
            }
            Const.REQ_REG -> {
                if (resultCode == Activity.RESULT_OK) {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        }
    }

    private fun setGoodsStatus() {
        when (mGoods!!.status) {
            EnumData.GoodsStatus.ing.status -> {
                mViewTopRight!!.visibility = View.VISIBLE
                text_plus_goods_detail_name.setTextColor(ResourceUtil.getColor(this, R.color.color_232323))
                text_plus_goods_detail_sale_price.setTextColor(ResourceUtil.getColor(this, R.color.color_232323))
                text_plus_goods_detail_sale_price_unit.setTextColor(ResourceUtil.getColor(this, R.color.color_232323))
                text_plus_goods_detail_origin_price.setTextColor(ResourceUtil.getColor(this, R.color.color_b7b7b7))
                text_plus_goods_detail_remain_count.setTextColor(ResourceUtil.getColor(this, R.color.color_579ffb))
                text_plus_goods_detail_reward.setTextColor(ResourceUtil.getColor(this, R.color.color_ff4646))
                text_plus_goods_detail_contents.setTextColor(ResourceUtil.getColor(this, R.color.color_737373))

                layout_plus_goods_detail_status.visibility = View.GONE
                if (mType == EnumData.GoodsType.plus.name) {
                    text_plus_goods_detail_btn.visibility = View.VISIBLE
                    text_plus_goods_detail_btn.setBackgroundResource(R.drawable.btn_blue)
                    text_plus_goods_detail_btn.setTextColor(ResourceUtil.getColor(this, R.color.white))
                    text_plus_goods_detail_btn.setText(R.string.msg_send_plus_event)
                    text_plus_goods_detail_btn.setOnClickListener {
                        val builder = AlertBuilder.Builder()
                        builder.setTitle(getString(R.string.word_notice_alert))
                        builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)

                        builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
                        builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_goods_news), AlertBuilder.MESSAGE_TYPE.TEXT, 5))

                        builder.setOnAlertResultListener(object : OnAlertResultListener {
                            override fun onCancel() {

                            }

                            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT?) {
                                when (event_alert) {
                                    AlertBuilder.EVENT_ALERT.RIGHT -> {
                                        val params = HashMap<String, String>()

                                        params["seqNo"] = mGoods!!.seqNo.toString()
                                        ApiBuilder.create().goodsNews(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                                            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                                                showAlert(R.string.msg_sent_goods_news)
                                            }

                                            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                                                showAlert(R.string.msg_alert_error_goods_news, 5)
                                            }
                                        }).build().call()
                                    }
                                }
                            }
                        }).builder().show(this)
                    }
                } else {
                    text_plus_goods_detail_btn.visibility = View.GONE
                }


                if (StringUtils.isNotEmpty(mGoods!!.expireDatetime)) {
                    val format = SimpleDateFormat(DateFormatUtils.PPLUS_DATE_FORMAT.pattern, Locale.getDefault())
                    var date = Date()
                    try {
                        date = format.parse(mGoods!!.expireDatetime)
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }

                    val currentMillis = System.currentTimeMillis()
                    if (date.time <= currentMillis) {
                        text_plus_goods_detail_btn.visibility = View.VISIBLE
                        layout_plus_goods_detail_status.visibility = View.VISIBLE
                        text_plus_goods_detail_status_option.visibility = View.VISIBLE
                        text_plus_goods_detail_status.setText(R.string.word_sold_finish)
                        text_plus_goods_detail_status_option.setText(R.string.word_expired)
                        mViewTopRight!!.visibility = View.GONE

                        if (mType == EnumData.GoodsType.plus.name) {
                            text_plus_goods_detail_btn.setText(R.string.msg_reg_plus_goods)
                            text_plus_goods_detail_btn.setBackgroundResource(R.drawable.btn_blue_line)
                            text_plus_goods_detail_btn.setTextColor(ResourceUtil.getColor(this, R.color.color_579ffb))
                        } else {
                            text_plus_goods_detail_btn.setText(R.string.msg_reg_hotdeal)
                            text_plus_goods_detail_btn.setBackgroundResource(R.drawable.btn_red_line)
                            text_plus_goods_detail_btn.setTextColor(ResourceUtil.getColor(this, R.color.color_ff4646))
                        }

                        text_plus_goods_detail_btn.setOnClickListener {
                            val intent = Intent(this, GoodsRegActivity2::class.java)
                            intent.putExtra(Const.TYPE, mType)
                            intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
                            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                            startActivityForResult(intent, Const.REQ_REG)
                        }
                    }
                }
            }
            EnumData.GoodsStatus.stop.status -> {
                mViewTopRight!!.visibility = View.VISIBLE
                text_plus_goods_detail_name.setTextColor(ResourceUtil.getColor(this, R.color.color_b7b7b7))
                text_plus_goods_detail_sale_price.setTextColor(ResourceUtil.getColor(this, R.color.color_b7b7b7))
                text_plus_goods_detail_sale_price_unit.setTextColor(ResourceUtil.getColor(this, R.color.color_b7b7b7))
                text_plus_goods_detail_origin_price.setTextColor(ResourceUtil.getColor(this, R.color.color_b7b7b7))
                text_plus_goods_detail_remain_count.setTextColor(ResourceUtil.getColor(this, R.color.color_b7b7b7))
                text_plus_goods_detail_reward.setTextColor(ResourceUtil.getColor(this, R.color.color_b7b7b7))
                text_plus_goods_detail_contents.setTextColor(ResourceUtil.getColor(this, R.color.color_b7b7b7))

                layout_plus_goods_detail_status.visibility = View.VISIBLE
                text_plus_goods_detail_status_option.visibility = View.GONE
                text_plus_goods_detail_status.setText(R.string.word_sold_stop)

                text_plus_goods_detail_btn.setBackgroundResource(R.drawable.btn_blue_line)
                text_plus_goods_detail_btn.setTextColor(ResourceUtil.getColor(this, R.color.color_ff4646))
                text_plus_goods_detail_btn.setText(R.string.word_re_sale)
                text_plus_goods_detail_btn.setOnClickListener {
                    putGoodsStatus()
                }
            }
            EnumData.GoodsStatus.soldout.status -> {
                mViewTopRight!!.visibility = View.GONE
                text_plus_goods_detail_name.setTextColor(ResourceUtil.getColor(this, R.color.color_b7b7b7))
                text_plus_goods_detail_sale_price.setTextColor(ResourceUtil.getColor(this, R.color.color_b7b7b7))
                text_plus_goods_detail_sale_price_unit.setTextColor(ResourceUtil.getColor(this, R.color.color_b7b7b7))
                text_plus_goods_detail_origin_price.setTextColor(ResourceUtil.getColor(this, R.color.color_b7b7b7))
                text_plus_goods_detail_remain_count.setTextColor(ResourceUtil.getColor(this, R.color.color_b7b7b7))
                text_plus_goods_detail_reward.setTextColor(ResourceUtil.getColor(this, R.color.color_b7b7b7))
                text_plus_goods_detail_contents.setTextColor(ResourceUtil.getColor(this, R.color.color_b7b7b7))

                layout_plus_goods_detail_status.visibility = View.VISIBLE
                text_plus_goods_detail_status_option.visibility = View.GONE
                text_plus_goods_detail_status.setText(R.string.word_sold_out)


                if (mType == EnumData.GoodsType.plus.name) {
                    text_plus_goods_detail_btn.setText(R.string.msg_reg_plus_goods)
                    text_plus_goods_detail_btn.setBackgroundResource(R.drawable.btn_blue_line)
                    text_plus_goods_detail_btn.setTextColor(ResourceUtil.getColor(this, R.color.color_579ffb))
                } else {
                    text_plus_goods_detail_btn.setText(R.string.msg_reg_hotdeal)
                    text_plus_goods_detail_btn.setBackgroundResource(R.drawable.btn_red_line)
                    text_plus_goods_detail_btn.setTextColor(ResourceUtil.getColor(this, R.color.color_ff4646))
                }
                text_plus_goods_detail_btn.setOnClickListener {
                    val intent = Intent(this, GoodsRegActivity2::class.java)
                    intent.putExtra(Const.TYPE, mType)
                    intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
                    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivityForResult(intent, Const.REQ_REG)
                }
            }
            EnumData.GoodsStatus.finish.status -> {
                mViewTopRight!!.visibility = View.GONE
                text_plus_goods_detail_name.setTextColor(ResourceUtil.getColor(this, R.color.color_b7b7b7))
                text_plus_goods_detail_sale_price.setTextColor(ResourceUtil.getColor(this, R.color.color_b7b7b7))
                text_plus_goods_detail_sale_price_unit.setTextColor(ResourceUtil.getColor(this, R.color.color_b7b7b7))
                text_plus_goods_detail_origin_price.setTextColor(ResourceUtil.getColor(this, R.color.color_b7b7b7))
                text_plus_goods_detail_remain_count.setTextColor(ResourceUtil.getColor(this, R.color.color_b7b7b7))
                text_plus_goods_detail_reward.setTextColor(ResourceUtil.getColor(this, R.color.color_b7b7b7))
                text_plus_goods_detail_contents.setTextColor(ResourceUtil.getColor(this, R.color.color_b7b7b7))

                layout_plus_goods_detail_status.visibility = View.VISIBLE
                text_plus_goods_detail_status_option.visibility = View.GONE
                text_plus_goods_detail_status.setText(R.string.word_sold_finish)

                if (mType == EnumData.GoodsType.plus.name) {
                    text_plus_goods_detail_btn.setText(R.string.msg_reg_plus_goods)
                    text_plus_goods_detail_btn.setBackgroundResource(R.drawable.btn_blue_line)
                    text_plus_goods_detail_btn.setTextColor(ResourceUtil.getColor(this, R.color.color_579ffb))
                } else {
                    text_plus_goods_detail_btn.setText(R.string.msg_reg_hotdeal)
                    text_plus_goods_detail_btn.setBackgroundResource(R.drawable.btn_red_line)
                    text_plus_goods_detail_btn.setTextColor(ResourceUtil.getColor(this, R.color.color_ff4646))
                }
                text_plus_goods_detail_btn.setOnClickListener {
                    val intent = Intent(this, GoodsRegActivity2::class.java)
                    intent.putExtra(Const.TYPE, mType)
                    intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
                    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivityForResult(intent, Const.REQ_REG)
                }
            }

        }
    }

    private fun putGoodsStatus() {
        val builder = AlertBuilder.Builder()
        builder.setTitle(getString(R.string.word_notice_alert))
        builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)

        builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
        if (mGoods!!.status == EnumData.GoodsStatus.ing.status) {

            builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_sale_stop1), AlertBuilder.MESSAGE_TYPE.TEXT, 1))
        } else if (mGoods!!.status == EnumData.GoodsStatus.stop.status) {
            builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_sale_resume1), AlertBuilder.MESSAGE_TYPE.TEXT, 1))
        }
        builder.setOnAlertResultListener(object : OnAlertResultListener {
            override fun onCancel() {

            }

            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT?) {
                when (event_alert) {
                    AlertBuilder.EVENT_ALERT.RIGHT -> {
                        val params = HashMap<String, String>()
                        params["seqNo"] = mGoods!!.seqNo.toString()
                        if (mGoods!!.status == EnumData.GoodsStatus.ing.status) {
                            params["status"] = EnumData.GoodsStatus.stop.status.toString()
                        } else if (mGoods!!.status == EnumData.GoodsStatus.stop.status) {
                            params["status"] = EnumData.GoodsStatus.ing.status.toString()
                        }
                        showProgress("")
                        ApiBuilder.create().putGoodsStatus(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                                hideProgress()
                                mGoods!!.status = params["status"]!!.toInt()
                                setGoodsStatus()

                                setBmb(mGoods!!)
                            }

                            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                                hideProgress()
                            }
                        }).build().call()
                    }
                }
            }
        }).builder().show(this)
    }

    private fun delete() {
        val builder = AlertBuilder.Builder()
        builder.setTitle(getString(R.string.word_notice_alert))
        builder.addContents(AlertData.MessageData(getString(R.string.msg_question_delete_goods), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
        builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
        builder.setOnAlertResultListener(object : OnAlertResultListener {

            override fun onCancel() {

            }

            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                when (event_alert) {
                    AlertBuilder.EVENT_ALERT.RIGHT -> {
                        val params = HashMap<String, String>()
                        params["seqNo"] = mGoods!!.seqNo.toString()
                        showProgress("")
                        ApiBuilder.create().deleteGoods(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                                hideProgress()
                                ToastUtil.show(this@PlusGoodsDetailActivity, getString(R.string.msg_deleted_goods))
                                setResult(Activity.RESULT_OK)
                                finish()
                            }

                            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                                hideProgress()

//                                                                        if (response!!.resultCode == 698) {
//                                                                            showAlert(R.string.msg_can_not_delete_history_goods)
//                                                                        }

                                showAlert(R.string.msg_can_not_delete_history_goods)
                            }
                        }).build().call()
                    }
                }
            }
        }).builder().show(this)
    }

    var mViewTopRight: View? = null

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_goods_detail), ToolbarOption.ToolbarMenu.LEFT)

        val goods = intent.getParcelableExtra<Goods>(Const.DATA)
        mViewTopRight = layoutInflater.inflate(R.layout.item_goods_more, null)

        val bmb = mViewTopRight!!.bmb_goods_more
        setBmb(goods)
        mViewTopRight!!.image_goods_more.setOnClickListener {
            bmb.boom()
        }

        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, mViewTopRight!!, 0)


        return toolbarOption
    }

    private fun setBmb(goods:Goods){
        val bmb = mViewTopRight!!.bmb_goods_more
        bmb.orderEnum = OrderEnum.DEFAULT
        bmb.clearBuilders()
        val rect = Rect(resources.getDimensionPixelSize(R.dimen.width_170), 0, resources.getDimensionPixelSize(R.dimen.width_900), resources.getDimensionPixelSize(R.dimen.height_170))
        when (goods.status) {
            EnumData.GoodsStatus.ing.status -> {

//                bmb.addBuilder(HamButton.Builder()
//                        .normalColor(ResourceUtil.getColor(this, R.color.color_86ccd2))
//                        .normalImageRes(R.drawable.ic_floating_push)
//                        .normalTextRes(R.string.msg_push_send)
//                        .textRect(rect)
//                        .containsSubText(false)
//                        .buttonHeight(resources.getDimensionPixelSize(R.dimen.height_170))
//                        .buttonWidth(resources.getDimensionPixelSize(R.dimen.width_900))
//                        .pieceColorRes(R.color.white)
//                        .listener {
//
//                            val intent = Intent(this, PushSendActivity::class.java)
//                            intent.putExtra(Const.DATA, mGoods)
//                            startActivityForResult(intent, Const.REQ_CASH_CHANGE)
//                        })

//                bmb.addBuilder(HamButton.Builder()
//                        .normalColor(ResourceUtil.getColor(this, R.color.color_579ffb))
//                        .normalImageRes(R.drawable.ic_floating_share)
//                        .normalTextRes(R.string.word_sms)
//                        .textRect(rect)
//                        .containsSubText(false)
//                        .buttonHeight(resources.getDimensionPixelSize(R.dimen.height_170))
//                        .buttonWidth(resources.getDimensionPixelSize(R.dimen.width_900))
//                        .pieceColorRes(R.color.white)
//                        .listener {
//
//                            val contents = mGoods!!.name
//                            val shareText = contents + "\n" + getString(R.string.format_msg_page_url, "goods_detail.php?seqNo=${mGoods!!.seqNo}")
//
//                            val smsUri = Uri.parse("sms:")
//                            val sendIntent = Intent(Intent.ACTION_SENDTO, smsUri)
//                            sendIntent.putExtra("sms_body", shareText)
//                            startActivity(sendIntent)
////                                val intent = Intent(mContext, SmsSendActivity::class.java)
////                                intent.putExtra(Const.DATA, item)
////                                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
////                                (mContext as BaseActivity).startActivityForResult(intent, Const.REQ_CASH_CHANGE)
//                        })

//                bmb.addBuilder(HamButton.Builder()
//                        .normalColor(ResourceUtil.getColor(this, R.color.color_fce000))
//                        .normalImageRes(R.drawable.ic_floating_kakao)
//                        .normalTextRes(R.string.msg_share_kakao)
//                        .textRect(rect)
//                        .containsSubText(false)
//                        .buttonHeight(resources.getDimensionPixelSize(R.dimen.height_170))
//                        .buttonWidth(resources.getDimensionPixelSize(R.dimen.width_900))
//                        .pieceColorRes(R.color.white)
//                        .listener {
//                            val contents = mGoods!!.name
//                            val url = getString(R.string.format_msg_page_url, "goods_detail.php?seqNo=${mGoods!!.seqNo}")
//                            val id = mGoods!!.attachments!!.images!![0]
//
//                            KakaoLinkUtil.getInstance(this).sendKakaoUrl(contents, "${Const.API_URL}attachment/image?id=${id}", url)
//                        })

                bmb.addBuilder(HamButton.Builder()
                        .normalColor(ResourceUtil.getColor(this, R.color.color_ffa7b3))
                        .normalImageRes(R.drawable.ic_floating_stop)
                        .normalTextRes(R.string.word_sold_stop)
                        .buttonHeight(resources.getDimensionPixelSize(R.dimen.height_170))
                        .buttonWidth(resources.getDimensionPixelSize(R.dimen.width_900))
                        .textRect(rect)
                        .containsSubText(false)
                        .pieceColorRes(R.color.white)
                        .listener {
                            putGoodsStatus()

                        })
            }

            EnumData.GoodsStatus.stop.status -> {
                bmb.addBuilder(HamButton.Builder()
                        .normalColor(ResourceUtil.getColor(this, R.color.color_ffa7b3))
                        .normalImageRes(R.drawable.ic_floating_resume)
                        .normalTextRes(R.string.word_sold_resume)
                        .buttonHeight(resources.getDimensionPixelSize(R.dimen.height_170))
                        .buttonWidth(resources.getDimensionPixelSize(R.dimen.width_900))
                        .textRect(rect)
                        .containsSubText(false)
                        .pieceColorRes(R.color.white)
                        .listener {
                            putGoodsStatus()
                        })
            }
        }

        bmb.addBuilder(HamButton.Builder()
                .normalColor(ResourceUtil.getColor(this, R.color.color_47bcc6))
                .normalImageRes(R.drawable.ic_floating_edit)
                .normalTextRes(R.string.word_goods_modified)
                .buttonHeight(resources.getDimensionPixelSize(R.dimen.height_170))
                .buttonWidth(resources.getDimensionPixelSize(R.dimen.width_900))
                .textRect(rect)
                .containsSubText(false)
                .pieceColorRes(R.color.white)
                .listener {
                    val intent = Intent(this, GoodsRegActivity2::class.java)
                    intent.putExtra(Const.TYPE, mType)
                    intent.putExtra(Const.MODE, EnumData.MODE.UPDATE)
                    intent.putExtra(Const.DATA, mGoods)
                    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivityForResult(intent, Const.REQ_MODIFY)
                })

        bmb.addBuilder(HamButton.Builder()
                .normalColor(ResourceUtil.getColor(this, R.color.color_71ce71))
                .normalImageRes(R.drawable.ic_floating_delete)
                .normalTextRes(R.string.word_goods_delete)
                .buttonHeight(resources.getDimensionPixelSize(R.dimen.height_170))
                .buttonWidth(resources.getDimensionPixelSize(R.dimen.width_900))
                .textRect(rect)
                .containsSubText(false)
                .pieceColorRes(R.color.white)
                .listener {
                    delete()
                })


        setButtonPlace(bmb)
        setPiecePlace(bmb)
    }

    private fun setButtonPlace(bmb: BoomMenuButton) {
        val builder = (bmb.builders[0] as HamButton.Builder)
        val h = builder.buttonHeight
        val w = builder.buttonWidth

        val vm = bmb.buttonVerticalMargin
        val vm_0_5 = vm / 2
        val h_0_5 = h / 2

        val halfButtonNumber = bmb.builders.size / 2

        val pointList = arrayListOf<PointF>()

        if (bmb.builders.size % 2 == 0) {
            for (i in halfButtonNumber - 1 downTo 0)
                pointList.add(PointF(0f, -h_0_5 - vm_0_5 - i * (h + vm)))
            for (i in 0 until halfButtonNumber)
                pointList.add(PointF(0f, +h_0_5 + vm_0_5 + i * (h + vm)))
        } else {
            for (i in halfButtonNumber - 1 downTo 0)
                pointList.add(PointF(0f, -h - vm - i * (h + vm)))
            pointList.add(PointF(0f, 0f))
            for (i in 0 until halfButtonNumber)
                pointList.add(PointF(0f, +h + vm + i * (h + vm)))
        }

        bmb.customButtonPlacePositions = pointList
    }

    private fun setPiecePlace(bmb: BoomMenuButton) {
        val h = bmb.hamHeight
        val w = bmb.hamWidth

        val pn = bmb.builders.size
        val pn_0_5 = pn / 2
        val h_0_5 = h / 2
        val vm = bmb.pieceVerticalMargin
        val vm_0_5 = vm / 2

        val pointList = arrayListOf<PointF>()

        if (pn % 2 == 0) {
            for (i in pn_0_5 - 1 downTo 0)
                pointList.add(PointF(0f, -h_0_5 - vm_0_5 - i * (h + vm)))
            for (i in 0 until pn_0_5)
                pointList.add(PointF(0f, +h_0_5 + vm_0_5 + i * (h + vm)))
        } else {
            for (i in pn_0_5 - 1 downTo 0)
                pointList.add(PointF(0f, -h - vm - i * (h + vm)))
            pointList.add(PointF(0f, 0f))
            for (i in 0 until pn_0_5)
                pointList.add(PointF(0f, +h + vm + i * (h + vm)))
        }

        bmb.customPiecePlacePositions = pointList
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
            }
        }
    }
}
