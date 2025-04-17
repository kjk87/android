package com.pplus.prnumberbiz.apps.goods.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import android.net.Uri
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
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.goods.data.GoodsImagePagerAdapter
import com.pplus.prnumberbiz.apps.push.ui.PushSendActivity
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Goods
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.sns.kakao.KakaoLinkUtil
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import com.pplus.prnumberbiz.core.util.ToastUtil
import kotlinx.android.synthetic.main.activity_goods_detail.*
import kotlinx.android.synthetic.main.item_goods_more.view.*
import network.common.PplusCallback
import retrofit2.Call
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class GoodsDetailActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return "Main_product detail"
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_goods_detail
    }

    var mGoods: Goods? = null
    var mIsOther = false

    override fun initializeView(savedInstanceState: Bundle?) {

        mGoods = intent.getParcelableExtra(Const.DATA)
        mIsOther = intent.getBooleanExtra(Const.OTHER, false)

        if (mIsOther) {
            layout_goods_detail_bottom.visibility = View.GONE
        } else {
            layout_goods_detail_bottom.visibility = View.VISIBLE
            if (LoginInfoManager.getInstance().user.page!!.mainGoodsSeqNo == mGoods!!.seqNo) {
                image_goods_detail_main.visibility = View.VISIBLE
//                text_goods_detail_set_main.setText(R.string.msg_release_main_goods)
            } else {
                image_goods_detail_main.visibility = View.GONE
//                text_goods_detail_set_main.setText(R.string.msg_setting_main_goods)
            }

        }


        text_goods_detail_review_count.setOnClickListener {
            val intent = Intent(this, GoodsReviewActivity::class.java)
            intent.putExtra(Const.GOODS, mGoods)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivityForResult(intent, Const.REQ_REVIEW)
        }

        text_goods_detail_re_reg.setOnClickListener {

        }

        text_goods_detail_share.setOnClickListener {
            if (mGoods != null) {
                share()
            }
        }

        setData()
    }

    private fun setData() {
        val params = HashMap<String, String>()

        params["seqNo"] = mGoods!!.seqNo.toString()

        showProgress("")

        ApiBuilder.create().getOneGoods(params).setCallback(object : PplusCallback<NewResultResponse<Goods>> {
            override fun onResponse(call: Call<NewResultResponse<Goods>>?, response: NewResultResponse<Goods>?) {
                hideProgress()
                if (response != null && response.data != null) {
                    mGoods = response.data

                    text_goods_detail_title.text = mGoods!!.name
                    text_goods_detail_origin_price.visibility = View.GONE
                    text_goods_detail_discount.visibility = View.GONE
                    text_goods_detail_discount_type.visibility = View.GONE
                    if (mGoods!!.originPrice != null) {

                        val origin_price = mGoods!!.originPrice!!

                        if (origin_price > mGoods!!.price!!) {
                            text_goods_detail_origin_price.visibility = View.VISIBLE
                            text_goods_detail_discount.visibility = View.VISIBLE
                            text_goods_detail_discount_type.visibility = View.VISIBLE

                            text_goods_detail_origin_price.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(origin_price.toString()))
                            text_goods_detail_origin_price.paintFlags = text_goods_detail_origin_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                            val percent = (100 - (mGoods!!.price!!.toFloat() / origin_price.toFloat() * 100)).toInt()
                            text_goods_detail_discount.text = FormatUtil.getMoneyType(percent.toString())
                        }
                    }

                    if (mGoods!!.status == EnumData.GoodsStatus.soldout.status) {

                        layout_goods_detail_status.visibility = View.VISIBLE
//                        text_goods_detail_re_reg.visibility = View.VISIBLE
                        text_goods_detail_re_reg.visibility = View.GONE
//                        text_goods_detail_set_main.visibility = View.GONE
                        text_goods_detail_status.setText(R.string.word_sold_out)
                        text_goods_detail_status.setTextColor(ResourceUtil.getColor(this@GoodsDetailActivity, R.color.white))
                        text_goods_detail_status.setBackgroundResource(R.drawable.border_color_ffffff_2px)
                    } else if (mGoods!!.status == EnumData.GoodsStatus.finish.status) {

                        layout_goods_detail_status.visibility = View.VISIBLE
                        text_goods_detail_re_reg.visibility = View.GONE
//                        text_goods_detail_set_main.visibility = View.GONE
                        text_goods_detail_status.setText(R.string.word_sold_finish)
                        text_goods_detail_status.setTextColor(ResourceUtil.getColor(this@GoodsDetailActivity, R.color.color_ff4646))
                        text_goods_detail_status.setBackgroundResource(R.drawable.border_color_ffffff_2px)
                    } else if (mGoods!!.status == EnumData.GoodsStatus.stop.status) {
                        layout_goods_detail_status.visibility = View.VISIBLE
                        text_goods_detail_re_reg.visibility = View.GONE
//                        text_goods_detail_set_main.visibility = View.GONE
                        text_goods_detail_status.setText(R.string.word_sold_stop)
                        text_goods_detail_status.setTextColor(ResourceUtil.getColor(this@GoodsDetailActivity, R.color.color_ff4646))
                        text_goods_detail_status.setBackgroundResource(R.drawable.border_color_ff4646_2px)
                    } else {

                        //"expireDatetime":"2018-10-18 15:00:59"

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
                                layout_goods_detail_status.visibility = View.VISIBLE
                                text_goods_detail_re_reg.visibility = View.GONE
//                                text_goods_detail_set_main.visibility = View.GONE
                                text_goods_detail_status.setText(R.string.word_sold_finish)
                                text_goods_detail_status.setTextColor(ResourceUtil.getColor(this@GoodsDetailActivity, R.color.white))
                                text_goods_detail_status.setBackgroundResource(R.drawable.border_color_ffffff_2px)
                            } else {
                                layout_goods_detail_status.visibility = View.GONE
                                text_goods_detail_re_reg.visibility = View.GONE

                            }
                        } else {
                            layout_goods_detail_status.visibility = View.GONE
                            text_goods_detail_re_reg.visibility = View.GONE
                        }
                    }

                    if (mGoods!!.count != null && mGoods!!.count != -1) {
                        var soldCount = 0
                        if (mGoods!!.soldCount != null) {
                            soldCount = mGoods!!.soldCount!!
                        }
                        text_goods_detail_remain_count.visibility = View.VISIBLE
                        text_goods_detail_remain_count.text = PplusCommonUtil.fromHtml(getString(R.string.html_goods_remain_count, FormatUtil.getMoneyType((mGoods!!.count!! - soldCount).toString())))
                    } else {
                        text_goods_detail_remain_count.visibility = View.GONE
                    }

                    text_goods_detail_sale_price.text = FormatUtil.getMoneyType(mGoods!!.price.toString())
                    text_goods_detail_remain_contents.text = mGoods!!.description
                    text_goods_detail_review_count.text = PplusCommonUtil.fromHtml(getString(R.string.html_goods_review, FormatUtil.getMoneyType(mGoods!!.reviewCount.toString())))

                    if (mGoods!!.attachments != null && mGoods!!.attachments!!.images != null && mGoods!!.attachments!!.images!!.isNotEmpty()) {

//                        val imageList = ArrayList<Long>()
//                        for (element in mGoods!!.attachments!!.images!!) {
//                            imageList.add(element)
//                        }

                        if (mGoods!!.attachments!!.images!!.size > 1) {
                            indicator_detail_goods.visibility = View.VISIBLE
                        } else {
                            indicator_detail_goods.visibility = View.GONE
                        }

                        val imageAdapter = GoodsImagePagerAdapter(this@GoodsDetailActivity)
                        imageAdapter.dataList = mGoods!!.attachments!!.images!! as ArrayList<String>

                        pager_goods_detail_image.adapter = imageAdapter
                        indicator_detail_goods.removeAllViews()
                        indicator_detail_goods.build(LinearLayout.HORIZONTAL, mGoods!!.attachments!!.images!!.size)
                        pager_goods_detail_image.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {

                            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                            }

                            override fun onPageSelected(position: Int) {

                                indicator_detail_goods.setCurrentItem(position)
                            }

                            override fun onPageScrollStateChanged(state: Int) {

                            }
                        })

                        imageAdapter.mListener = object : GoodsImagePagerAdapter.OnItemClickListener {
                            override fun onItemClick(position: Int) {
                                val intent = Intent(this@GoodsDetailActivity, GoodsDetailViewerActivity::class.java)
                                intent.putExtra(Const.POSITION, pager_goods_detail_image.currentItem)
                                intent.putExtra(Const.DATA, imageAdapter.dataList)
                                startActivity(intent)
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Goods>>?, t: Throwable?, response: NewResultResponse<Goods>?) {
                hideProgress()
            }
        }).build().call()
    }

    fun share() {
        var contents = mGoods!!.name

        val shareText = contents + "\n" + getString(R.string.format_msg_page_url, "goods_detail.php?seqNo=${mGoods!!.seqNo}")

        val intent = Intent(Intent.ACTION_SEND)
        intent.action = Intent.ACTION_SEND
        intent.type = "text/plain"
        //        intent.putExtra(Intent.EXTRA_SUBJECT, subjectText);
        intent.putExtra(Intent.EXTRA_TEXT, shareText)

        val chooserIntent = Intent.createChooser(intent, getString(R.string.word_share))
        //        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, shareIntentList.toArray(new Parcelable[]{}));
        startActivity(chooserIntent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Const.REQ_MODIFY -> {
                if (resultCode == Activity.RESULT_OK) {
                    setResult(Activity.RESULT_OK)
                    setData()
                }
            }
        }
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_goods_detail), ToolbarOption.ToolbarMenu.LEFT)

        val isOther = intent.getBooleanExtra(Const.OTHER, false)
        if (!isOther) {
            toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, getString(R.string.word_sale_history), 0, resources.getDimensionPixelSize(R.dimen.width_40))
            val goods = intent.getParcelableExtra<Goods>(Const.DATA)
            val view = layoutInflater.inflate(R.layout.item_goods_more, null)

            val bmb = view.bmb_goods_more
            bmb.orderEnum = OrderEnum.DEFAULT
            bmb.clearBuilders()
            val rect = Rect(resources.getDimensionPixelSize(R.dimen.width_170), 0, resources.getDimensionPixelSize(R.dimen.width_900), resources.getDimensionPixelSize(R.dimen.height_170))
            when (goods!!.status) {
                EnumData.GoodsStatus.ing.status -> {

                    var mainGoodsTitle = getString(R.string.msg_setting_main_goods)
                    var mainGoodsImg = R.drawable.ic_floating_goods
                    if (LoginInfoManager.getInstance().user.page!!.mainGoodsSeqNo == goods.seqNo) {
                        mainGoodsTitle = getString(R.string.msg_release_main_goods)
                        mainGoodsImg = R.drawable.ic_floating_stop
                    }

                    bmb.addBuilder(HamButton.Builder()
                            .normalColor(ResourceUtil.getColor(this, R.color.color_f2cb3f))
                            .normalImageRes(mainGoodsImg)
                            .normalText(mainGoodsTitle)
                            .buttonHeight(resources.getDimensionPixelSize(R.dimen.height_170))
                            .buttonWidth(resources.getDimensionPixelSize(R.dimen.width_900))
                            .textRect(rect)
                            .containsSubText(false)
                            .pieceColorRes(R.color.white)
                            .listener {
                                val builder = AlertBuilder.Builder()
                                builder.setTitle(getString(R.string.word_notice_alert))
                                if (LoginInfoManager.getInstance().user.page!!.mainGoodsSeqNo == mGoods!!.seqNo) {
                                    builder.addContents(AlertData.MessageData(getString(R.string.msg_question_release_main_goods), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                                } else {
                                    builder.addContents(AlertData.MessageData(getString(R.string.msg_question_main_goods), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                                }

                                builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
                                builder.setOnAlertResultListener(object : OnAlertResultListener {

                                    override fun onCancel() {

                                    }

                                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                                        when (event_alert) {
                                            AlertBuilder.EVENT_ALERT.RIGHT -> {
                                                val params = HashMap<String, String>()
                                                params["no"] = LoginInfoManager.getInstance().user.page!!.no.toString()
                                                if (LoginInfoManager.getInstance().user.page!!.mainGoodsSeqNo != mGoods!!.seqNo) {
                                                    params["mainGoodsSeqNo"] = mGoods!!.seqNo.toString()
                                                }

                                                showProgress("")
                                                ApiBuilder.create().putMainGoods(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                                                    override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                                                        hideProgress()

                                                        setResult(Activity.RESULT_OK)
                                                        if (LoginInfoManager.getInstance().user.page!!.mainGoodsSeqNo == mGoods!!.seqNo) {
                                                            showAlert(R.string.msg_released_main_goods)
                                                            LoginInfoManager.getInstance().user.page!!.mainGoodsSeqNo = null
                                                            LoginInfoManager.getInstance().save()
                                                            image_goods_detail_main.visibility = View.GONE
//                                                                                text_goods_detail_set_main.setText(R.string.msg_setting_main_goods)
                                                        } else {
                                                            showAlert(R.string.msg_set_main_goods)
                                                            LoginInfoManager.getInstance().user.page!!.mainGoodsSeqNo = mGoods!!.seqNo
                                                            LoginInfoManager.getInstance().save()
                                                            image_goods_detail_main.visibility = View.VISIBLE
//                                                                                text_goods_detail_set_main.setText(R.string.msg_release_main_goods)
                                                        }

                                                    }

                                                    override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                                                        hideProgress()
                                                    }
                                                }).build().call()
                                            }
                                        }

                                    }
                                }).builder().show(this@GoodsDetailActivity)
                            })

                    bmb.addBuilder(HamButton.Builder()
                            .normalColor(ResourceUtil.getColor(this@GoodsDetailActivity, R.color.color_86ccd2))
                            .normalImageRes(R.drawable.ic_floating_push)
                            .normalTextRes(R.string.msg_push_send)
                            .textRect(rect)
                            .containsSubText(false)
                            .buttonHeight(resources.getDimensionPixelSize(R.dimen.height_170))
                            .buttonWidth(resources.getDimensionPixelSize(R.dimen.width_900))
                            .pieceColorRes(R.color.white)
                            .listener {

                                val intent = Intent(this@GoodsDetailActivity, PushSendActivity::class.java)
                                intent.putExtra(Const.DATA, mGoods)
                                startActivityForResult(intent, Const.REQ_CASH_CHANGE)
                            })

                    bmb.addBuilder(HamButton.Builder()
                            .normalColor(ResourceUtil.getColor(this@GoodsDetailActivity, R.color.color_579ffb))
                            .normalImageRes(R.drawable.ic_floating_share)
                            .normalTextRes(R.string.word_sms)
                            .textRect(rect)
                            .containsSubText(false)
                            .buttonHeight(resources.getDimensionPixelSize(R.dimen.height_170))
                            .buttonWidth(resources.getDimensionPixelSize(R.dimen.width_900))
                            .pieceColorRes(R.color.white)
                            .listener {

                                val contents = mGoods!!.name
                                val shareText = contents + "\n" + getString(R.string.format_msg_page_url, "goods_detail.php?seqNo=${mGoods!!.seqNo}")

                                val smsUri = Uri.parse("sms:")
                                val sendIntent = Intent(Intent.ACTION_SENDTO, smsUri)
                                sendIntent.putExtra("sms_body", shareText)
                                startActivity(sendIntent)
//                                val intent = Intent(mContext, SmsSendActivity::class.java)
//                                intent.putExtra(Const.DATA, item)
//                                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//                                (mContext as BaseActivity).startActivityForResult(intent, Const.REQ_CASH_CHANGE)
                            })

                    bmb.addBuilder(HamButton.Builder()
                            .normalColor(ResourceUtil.getColor(this@GoodsDetailActivity, R.color.color_fce000))
                            .normalImageRes(R.drawable.ic_floating_kakao)
                            .normalTextRes(R.string.msg_share_kakao)
                            .textRect(rect)
                            .containsSubText(false)
                            .buttonHeight(resources.getDimensionPixelSize(R.dimen.height_170))
                            .buttonWidth(resources.getDimensionPixelSize(R.dimen.width_900))
                            .pieceColorRes(R.color.white)
                            .listener {
                                val contents = mGoods!!.name
                                val url = getString(R.string.format_msg_page_url, "goods_detail.php?seqNo=${mGoods!!.seqNo}")
                                val id = mGoods!!.attachments!!.images!![0]

                                KakaoLinkUtil.getInstance(this@GoodsDetailActivity).sendKakaoUrl(contents, "${Const.API_URL}attachment/image?id=${id}", url)
                            })

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
                                if (LoginInfoManager.getInstance().user.page!!.mainGoodsSeqNo != mGoods!!.seqNo) {
                                    setStatus()
                                } else {
                                    ToastUtil.show(this@GoodsDetailActivity, R.string.msg_can_not_stop_goods_main_goods)
                                }

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
                                setStatus()
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
            view.image_goods_more.setOnClickListener {
                bmb.boom()
            }

            toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, view, 0)
        }

        return toolbarOption
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
                ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
                    val intent = Intent(this@GoodsDetailActivity, GoodsSaleHistoryActivity::class.java)
                    intent.putExtra(Const.GOODS, mGoods)
                    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                }
            }
        }
    }

    private fun delete() {
        if (LoginInfoManager.getInstance().user.page!!.mainGoodsSeqNo == mGoods!!.seqNo) {
            showAlert(R.string.msg_can_not_delete_main_goods)
            return
        }
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
                                ToastUtil.show(this@GoodsDetailActivity, getString(R.string.msg_deleted_goods))
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
        }).builder().show(this@GoodsDetailActivity)
    }

    private fun setStatus() {
        val builder = AlertBuilder.Builder()
        builder.setTitle(getString(R.string.word_notice_alert))
        builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)

        builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
        if (mGoods!!.status == EnumData.GoodsStatus.ing.status) {

            if (LoginInfoManager.getInstance().user.page!!.mainGoodsSeqNo == mGoods!!.seqNo) {
                showAlert(R.string.msg_can_not_stop_main_goods)
                return
            }

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
                                if (mGoods!!.status == EnumData.GoodsStatus.ing.status) {
                                } else if (mGoods!!.status == EnumData.GoodsStatus.soldout.status) {
                                }

                                mGoods!!.status = params["status"]!!.toInt()
                                setResult(Activity.RESULT_OK)
                                setData()
                            }

                            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                                hideProgress()
                            }
                        }).build().call()
                    }
                }
            }
        }).builder().show(this@GoodsDetailActivity)
    }
}
