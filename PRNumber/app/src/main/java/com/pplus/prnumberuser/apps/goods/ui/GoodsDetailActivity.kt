//package com.pplus.prnumberuser.apps.goods.ui
//
//import android.animation.Animator
//import android.animation.AnimatorListenerAdapter
//import android.animation.ObjectAnimator
//import android.content.Intent
//import android.graphics.Paint
//import android.os.Bundle
//import android.view.View
//import android.view.animation.AccelerateDecelerateInterpolator
//import android.widget.LinearLayout
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.engine.DiskCacheStrategy
//import com.bumptech.glide.request.RequestOptions
//import com.pplus.utils.part.apps.resource.ResourceUtil
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.utils.part.utils.time.DateFormatUtils
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
//import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
//import com.pplus.prnumberuser.apps.common.builder.data.AlertData
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.common.mgmt.SelectedGoodsManager
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.goods.data.GoodsImagePagerAdapter
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.BuyGoods
//import com.pplus.prnumberuser.core.network.model.dto.Goods
//import com.pplus.prnumberuser.core.network.model.dto.GoodsImage
//import com.pplus.prnumberuser.core.network.model.dto.GoodsLike
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.prnumberuser.core.util.ToastUtil
//import kotlinx.android.synthetic.main.activity_goods_detail.*
//import kotlinx.android.synthetic.main.layout_pplus_info.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.text.ParseException
//import java.text.SimpleDateFormat
//import java.util.*
//
//class GoodsDetailActivity : BaseActivity(), ImplToolbar {
//    override fun getPID(): String {
//        return "Main_surrounding_product detail"
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_goods_detail
//    }
//
//    var mGoods: Goods? = null
//    var mCount = 1
//    var mTotalPrice = 0L
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//        mGoods = intent.getParcelableExtra(Const.DATA)
//
//        layout_goods_detail_eval_review.setOnClickListener {
//
//            val intent = Intent(this, GoodsReviewActivity::class.java)
//            intent.putExtra(Const.GOODS, mGoods)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivityForResult(intent, Const.REQ_REVIEW)
//        }
//
//        text_goods_detail_origin_price.paintFlags = text_goods_detail_origin_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
//
//        text_goods_detail_buy.setOnClickListener {
//
//            scrollInAnimation()
//        }
//        text_goods_detail_cart.setOnClickListener {
//            scrollInAnimation()
//        }
//
//        image_goods_detail_option_close.setOnClickListener {
//            scrollOutAnimation()
//        }
//
//        image_goods_detail_option_minus.setOnClickListener {
//            if (mCount > 1) {
//                mCount--
//                changeOption()
//            }
//        }
//
//        image_goods_detail_option_plus.setOnClickListener {
//
//            var maxCount = -1
//            if (mGoods!!.count != -1) {
//
//                var soldCount = 0
//                if (mGoods!!.soldCount != null) {
//                    soldCount = mGoods!!.soldCount!!
//                }
//
//                maxCount = mGoods!!.count!! - soldCount
//            }
//
//            if (maxCount != -1) {
//                if (mCount < maxCount) {
//                    mCount++
//                }
//            } else {
//                mCount++
//            }
//
//            changeOption()
//        }
//
//        if (mGoods!!.isPlus!! || mGoods!!.isHotdeal!!) {
//            text_goods_detail_option_cart.visibility = View.GONE
//        } else {
//            text_goods_detail_option_cart.visibility = View.VISIBLE
//        }
//
//        text_goods_detail_option_cart.setOnClickListener {
//            var buyGoodsList = SelectedGoodsManager.getInstance(mGoods!!.page!!.seqNo!!).load().buyGoodsList
//
//            if (buyGoodsList == null) {
//                buyGoodsList = arrayListOf()
//            }
//
//            var isExist = false
//
//            for (i in 0 until buyGoodsList.size) {
//                if (buyGoodsList[i].goods!!.seqNo == mGoods!!.seqNo) {
//                    buyGoodsList[i].count = buyGoodsList[i].count!! + mCount
//                    isExist = true
//                    break
//                }
//            }
//
//            if (!isExist) {
//                val buyGoods = BuyGoods()
//                buyGoods.goods = mGoods
//                buyGoods.count = mCount
//                buyGoodsList.add(buyGoods)
//            }
//
//
//            SelectedGoodsManager.getInstance(mGoods!!.page!!.seqNo!!).load().buyGoodsList = buyGoodsList
//            SelectedGoodsManager.getInstance(mGoods!!.page!!.seqNo!!).save()
//
//            val builder = AlertBuilder.Builder()
//            builder.setTitle(getString(R.string.word_notice_alert))
//            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
//            builder.addContents(AlertData.MessageData(getString(R.string.msg_saved_cart), AlertBuilder.MESSAGE_TYPE.TEXT, 1))
//            builder.setLeftText(getString(R.string.msg_move_cart)).setRightText(getString(R.string.msg_shopping_continue))
//            builder.setOnAlertResultListener(object : OnAlertResultListener {
//
//                override fun onCancel() {
//
//                }
//
//                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//                    when (event_alert) {
//                        AlertBuilder.EVENT_ALERT.LEFT -> {
//                            val intent = Intent(this@GoodsDetailActivity, CartActivity::class.java)
//                            intent.putExtra(Const.PAGE_SEQ_NO, mGoods!!.page!!.seqNo)
//                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                            startActivity(intent)
//                        }
//                    }
//                }
//            })
//            builder.builder().show(this@GoodsDetailActivity)
//        }
//
//        text_goods_detail_option_immediately_buy.setOnClickListener {
//            if (!PplusCommonUtil.loginCheck(this)) {
//                return@setOnClickListener
//            }
//
//            when (mGoods!!.type) {
//                "1" -> {
//                    val buyGoodsList = ArrayList<BuyGoods>()
//                    val buyGoods = BuyGoods()
//                    buyGoods.goods = mGoods
//                    buyGoods.count = mCount
//                    buyGoodsList.add(buyGoods)
//
//                    val intent = Intent(this, GoodsBuyActivity::class.java)
//                    intent.putParcelableArrayListExtra(Const.BUY_GOODS, buyGoodsList)
//                    startActivityForResult(intent, Const.REQ_ORDER)
//
//                }
//            }
//        }
//
//        image_goods_detail_more.setOnClickListener {
//            val builder = AlertBuilder.Builder()
//            builder.setLeftText(getString(R.string.word_cancel))
//            builder.setContents(getString(R.string.word_share), getString(R.string.msg_report))
//            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_BOTTOM).setOnAlertResultListener(object : OnAlertResultListener {
//
//                override fun onCancel() {
//
//                }
//
//                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//
//                    when (event_alert.getValue()) {
//                        1 -> {
//                            if (mGoods != null) {
//                                share()
//                            }
//                        }
//                        2 -> {
//                            PplusCommonUtil.report(this@GoodsDetailActivity, EnumData.REPORT_TYPE.goods, mGoods!!.seqNo)
//                        }
//                    }
//                }
//            }).builder().show(this@GoodsDetailActivity)
//        }
//
//        text_goods_notice_info.setOnClickListener {
//            val intent = Intent(this, GoodsNoticeInfoActivity::class.java)
//            intent.putExtra(Const.DATA, mGoods)
//            intent.putExtra(Const.KEY, Const.REFUND)
//            startActivity(intent)
//        }
//
//        text_refund_exchange_info.setOnClickListener {
//            val intent = Intent(this, GoodsRefundInfoActivity::class.java)
//            intent.putExtra(Const.DATA, mGoods)
//            intent.putExtra(Const.KEY, Const.REFUND)
//            startActivity(intent)
//        }
//
//        text_buy_warning.setOnClickListener {
//            val intent = Intent(this, GoodsInfoActivity::class.java)
//            intent.putExtra(Const.KEY, Const.WARNING)
//            intent.putExtra(Const.GOODS, mGoods)
//            startActivity(intent)
//        }
//
//        layout_pplus_info_btn.setOnClickListener {
//            if (layout_pplus_info.visibility == View.GONE) {
//                text_pplus_info_btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_footer_arrow_up, 0)
//                layout_pplus_info.visibility = View.VISIBLE
//            } else if (layout_pplus_info.visibility == View.VISIBLE) {
//                text_pplus_info_btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_footer_arrow_down, 0)
//                layout_pplus_info.visibility = View.GONE
//            }
//        }
//
//        layout_goods_detail_review_page.visibility = View.GONE
//
//        setData()
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
////            Const.REQ_ORDER_TYPE -> {
////                if (resultCode == Activity.RESULT_OK) {
////                    if (data != null) {
////                        val type = data.getIntExtra(Const.TYPE, 0)
////                        val intent = Intent(this, HotdealBuyActivity::class.java)
////                        intent.putExtra(Const.KEY, Const.GOODS)
////                        intent.putExtra(Const.GOODS, mGoods)
////                        intent.putExtra(Const.COUNT, mCount)
////                        intent.putExtra(Const.TYPE, type)
////                        startActivityForResult(intent, Const.REQ_ORDER)
////                    }
////                }
////            }
//        }
//    }
//
//    fun share() {
//        var contents = mGoods!!.name
//
//        val shareText = contents + "\n" + getString(R.string.format_msg_page_url, "goods_detail.php?seqNo=${mGoods!!.seqNo}")
//
//        val intent = Intent(Intent.ACTION_SEND)
//        intent.action = Intent.ACTION_SEND
//        intent.type = "text/plain"
//        //        intent.putExtra(Intent.EXTRA_SUBJECT, subjectText);
//        intent.putExtra(Intent.EXTRA_TEXT, shareText)
//
//        val chooserIntent = Intent.createChooser(intent, getString(R.string.word_share))
//        //        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, shareIntentList.toArray(new Parcelable[]{}));
//        startActivity(chooserIntent)
//    }
//
//    private fun changeOption() {
//        text_goods_detail_option_count.text = mCount.toString()
//        mTotalPrice = mGoods!!.price!! * mCount
//        text_goods_detail_option_price.text = PplusCommonUtil.fromHtml(getString(R.string.html_total_price2, FormatUtil.getMoneyType(mTotalPrice.toString())))
//    }
//
//    private fun postLike() {
//
//        val goodsLike = GoodsLike()
//        goodsLike.memberSeqNo = LoginInfoManager.getInstance().user.no
//        goodsLike.goodsSeqNo = mGoods!!.seqNo
//        goodsLike.pageSeqNo = mGoods!!.page!!.seqNo
//        showProgress("")
//        ApiBuilder.create().postGoodsLike(goodsLike).setCallback(object : PplusCallback<NewResultResponse<GoodsLike>> {
//            override fun onResponse(call: Call<NewResultResponse<GoodsLike>>?, response: NewResultResponse<GoodsLike>?) {
//                hideProgress()
//                mIsLike = true
//                layout_goods_detail_like.isSelected = true
//                ToastUtil.show(this@GoodsDetailActivity, R.string.msg_goods_like)
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<GoodsLike>>?, t: Throwable?, response: NewResultResponse<GoodsLike>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    private fun deleteLike() {
//        val goodsLike = GoodsLike()
//        goodsLike.memberSeqNo = LoginInfoManager.getInstance().user.no
//        goodsLike.goodsSeqNo = mGoods!!.seqNo
//        goodsLike.pageSeqNo = mGoods!!.page!!.seqNo
//        showProgress("")
//        ApiBuilder.create().deleteGoodsLike(goodsLike).setCallback(object : PplusCallback<NewResultResponse<Any>> {
//            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
//                hideProgress()
//                mIsLike = false
//                layout_goods_detail_like.isSelected = false
//                ToastUtil.show(this@GoodsDetailActivity, R.string.msg_delete_goods_like)
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//
//    var mIsLike = false
//
//    private fun checkLike() {
//        val params = HashMap<String, String>()
//
//        params["memberSeqNo"] = LoginInfoManager.getInstance().user.no.toString()
//        params["goodsSeqNo"] = mGoods!!.seqNo.toString()
//        params["pageSeqNo"] = mGoods!!.page!!.seqNo.toString()
//
//        showProgress("")
//        mIsLike = false
//        ApiBuilder.create().getGoodsLikeOne(params).setCallback(object : PplusCallback<NewResultResponse<GoodsLike>> {
//            override fun onResponse(call: Call<NewResultResponse<GoodsLike>>?, response: NewResultResponse<GoodsLike>?) {
//                hideProgress()
//
//                if (response != null) {
//                    if (response.data != null && response.data.status == 1) {
//                        layout_goods_detail_like.isSelected = true
//                        mIsLike = true
//                    }
//                }
//
//                layout_goods_detail_like.setOnClickListener {
//                    if (mIsLike) {
//                        deleteLike()
//                    } else {
//                        postLike()
//                    }
//
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<GoodsLike>>?, t: Throwable?, response: NewResultResponse<GoodsLike>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    private fun setData() {
//        val params = HashMap<String, String>()
//
//        params["seqNo"] = mGoods!!.seqNo.toString()
//
//        showProgress("")
//        ApiBuilder.create().getOneGoods(params).setCallback(object : PplusCallback<NewResultResponse<Goods>> {
//            override fun onResponse(call: Call<NewResultResponse<Goods>>?, response: NewResultResponse<Goods>?) {
//                hideProgress()
//                if (response != null && response.data != null) {
//                    mGoods = response.data
////                    checkLike()
//                    if (mGoods!!.page != null) {
//
//                        if (StringUtils.isNotEmpty(mGoods!!.page!!.thumbnail)) {
//                            Glide.with(this@GoodsDetailActivity).load(mGoods!!.page!!.thumbnail).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.img_commerce_profile_default).error(R.drawable.img_commerce_profile_default)).into(image_goods_detail_profile)
//                        } else {
//                            image_goods_detail_profile.setImageResource(R.drawable.img_commerce_profile_default)
//                        }
//
//                        layout_goods_detail_page.setOnClickListener {
//                            val location = IntArray(2)
//                            it.getLocationOnScreen(location)
//                            val x = location[0] + it.width / 2
//                            val y = location[1] + it.height / 2
//
//                            PplusCommonUtil.goPage(this@GoodsDetailActivity, mGoods!!.page!!, x, y)
//                        }
//                        layout_goods_detail_page2.setOnClickListener {
//                            val location = IntArray(2)
//                            it.getLocationOnScreen(location)
//                            val x = location[0] + it.width / 2
//                            val y = location[1] + it.height / 2
//
//                            PplusCommonUtil.goPage(this@GoodsDetailActivity, mGoods!!.page!!, x, y)
//                        }
//                    }
//
//                    if (mGoods!!.isPlus!!) {
//                        if (StringUtils.isNotEmpty(mGoods!!.expireDatetime)) {
//                            val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mGoods!!.expireDatetime)
//                            val output = SimpleDateFormat(getString(R.string.word_format_date))
//                            text_goods_detail_expire_date.text = getString(R.string.format_expire_date2, output.format(d))
//                        }
//
//                    } else if (mGoods!!.isHotdeal!!) {
//                        text_goods_detail_expire_date.text = getString(R.string.format_expire_date2, getString(R.string.word_buy_after_30))
//                    } else if (mGoods!!.type == "1") {
//                        text_goods_detail_expire_date.text = getString(R.string.format_expire_date2, getString(R.string.word_buy_after_30))
//                        text_goods_detail_contents.visibility = View.GONE
//                    }
//
////                    if (StringUtils.isNotEmpty(mGoods!!.startTime) && StringUtils.isNotEmpty(mGoods!!.endTime)) {
////                        text_goods_detail_use_time.visibility = View.VISIBLE
////                        text_goods_detail_use_time.text = getString(R.string.format_use_time, mGoods!!.startTime + " ~ " + mGoods!!.endTime)
////                    } else {
////                        text_goods_detail_use_time.visibility = View.GONE
////                    }
//
//                    text_goods_detail_page_name.text = mGoods!!.page!!.name
//                    var avgEval = "0.0"
//                    if (mGoods!!.avgEval != null) {
//                        avgEval = String.format("%.1f", mGoods!!.avgEval)
//                    }
//                    text_goods_detail_eval_review.text = getString(R.string.format_review, FormatUtil.getMoneyType(mGoods!!.reviewCount.toString()))
//
//                    if (mGoods!!.status == EnumData.GoodsStatus.soldout.status) {
//
//                        layout_goods_detail_status.visibility = View.VISIBLE
//                        text_goods_detail_buy.visibility = View.GONE
//                        text_goods_detail_status.setText(R.string.word_sold_out)
//                        text_goods_detail_status.setTextColor(ResourceUtil.getColor(this@GoodsDetailActivity, R.color.white))
//
//                    } else if (mGoods!!.status == EnumData.GoodsStatus.stop.status) {
//
//                        layout_goods_detail_status.visibility = View.VISIBLE
//                        text_goods_detail_buy.visibility = View.GONE
//                        text_goods_detail_status.setText(R.string.word_sold_stop)
//                        text_goods_detail_status.setTextColor(ResourceUtil.getColor(this@GoodsDetailActivity, R.color.color_ff4646))
//                        text_goods_detail_status.setBackgroundResource(R.drawable.border_color_ff4646_2px)
//                    } else if (mGoods!!.status == EnumData.GoodsStatus.finish.status) {
//
//                        layout_goods_detail_status.visibility = View.VISIBLE
//                        text_goods_detail_buy.visibility = View.GONE
//                        text_goods_detail_status.setText(R.string.word_sold_finish)
//                        text_goods_detail_status.setTextColor(ResourceUtil.getColor(this@GoodsDetailActivity, R.color.color_ff4646))
//                        text_goods_detail_status.setBackgroundResource(R.drawable.border_color_ff4646_2px)
//                    } else {
//
//                        //"expireDatetime":"2018-10-18 15:00:59"
//
//                        if (StringUtils.isNotEmpty(mGoods!!.expireDatetime)) {
//                            val format = SimpleDateFormat(DateFormatUtils.PPLUS_DATE_FORMAT.pattern, Locale.getDefault())
//                            var date = Date()
//                            try {
//                                date = format.parse(mGoods!!.expireDatetime)
//                            } catch (e: ParseException) {
//                                e.printStackTrace()
//                            }
//
//                            val currentMillis = System.currentTimeMillis()
//                            if (date.time <= currentMillis) {
//                                layout_goods_detail_status.visibility = View.VISIBLE
//                                text_goods_detail_buy.visibility = View.GONE
//                                text_goods_detail_status.setText(R.string.word_sold_finish)
//                                text_goods_detail_status.setTextColor(ResourceUtil.getColor(this@GoodsDetailActivity, R.color.color_ff4646))
//                                text_goods_detail_status.setBackgroundResource(R.drawable.border_color_ff4646_2px)
//                            } else {
//                                layout_goods_detail_status.visibility = View.GONE
//                                text_goods_detail_buy.visibility = View.VISIBLE
//                            }
//                        } else {
//                            layout_goods_detail_status.visibility = View.GONE
//                            text_goods_detail_buy.visibility = View.VISIBLE
//                        }
//                    }
//
//                    if (mGoods!!.count != null && mGoods!!.count != -1) {
//                        var soldCount = 0
//                        if (mGoods!!.soldCount != null) {
//                            soldCount = mGoods!!.soldCount!!
//                        }
//                        text_goods_detail_remain_count.visibility = View.VISIBLE
//                        text_goods_detail_remain_count.text = PplusCommonUtil.fromHtml(getString(R.string.html_goods_remain_count, FormatUtil.getMoneyType((mGoods!!.count!! - soldCount).toString())))
//                    } else {
//                        text_goods_detail_remain_count.visibility = View.GONE
//                    }
//
//                    changeOption()
//
//                    text_goods_detail_title.text = mGoods!!.name
//                    text_goods_detail_origin_price.visibility = View.GONE
//
//                    if (mGoods!!.originPrice != null) {
//
//                        val origin_price = mGoods!!.originPrice!!
//
//                        if (origin_price > mGoods!!.price!!) {
//                            text_goods_detail_origin_price.visibility = View.VISIBLE
//
//                            text_goods_detail_origin_price.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(origin_price.toString()))
//                            text_goods_detail_origin_price.paintFlags = text_goods_detail_origin_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
//
//                        }
//                    }
//
//                    text_goods_detail_sale_price.text = FormatUtil.getMoneyType(mGoods!!.price.toString())
//                    text_goods_detail_contents.text = mGoods!!.description
//
//                    if(mGoods!!.page!!.point != null && mGoods!!.page!!.point!! > 0){
//                        text_goods_detail_point.visibility = View.VISIBLE
//                        val point = (mGoods!!.price!!*(mGoods!!.page!!.point!!/100f)).toInt()
//                        text_goods_detail_point.text = PplusCommonUtil.fromHtml(getString(R.string.html_reward_point4, "${FormatUtil.getMoneyType(point.toString())}P"))
//                    }else{
//                        text_goods_detail_point.visibility = View.GONE
//                    }
//
//                    if (mGoods!!.goodsImageList != null && mGoods!!.goodsImageList!!.isNotEmpty()) {
//
////                        val imageList = ArrayList<Long>()
////                        for (element in mGoods!!.attachments!!.images!!) {
////                            imageList.add(element)
////                        }
//
//                        if (mGoods!!.goodsImageList!!.size > 1) {
//                            indicator_detail_goods.visibility = View.VISIBLE
//                        } else {
//                            indicator_detail_goods.visibility = View.GONE
//                        }
//
//                        val imageAdapter = GoodsImagePagerAdapter(this@GoodsDetailActivity)
//                        imageAdapter.dataList = mGoods!!.goodsImageList!! as ArrayList<GoodsImage>
//
//                        pager_goods_detail_image.adapter = imageAdapter
//                        indicator_detail_goods.removeAllViews()
//                        indicator_detail_goods.build(LinearLayout.HORIZONTAL, mGoods!!.goodsImageList!!.size)
//                        pager_goods_detail_image.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
//
//                            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//
//                            }
//
//                            override fun onPageSelected(position: Int) {
//
//                                indicator_detail_goods.setCurrentItem(position)
//                            }
//
//                            override fun onPageScrollStateChanged(state: Int) {
//
//                            }
//                        })
//
//                        imageAdapter.mListener = object : GoodsImagePagerAdapter.OnItemClickListener {
//                            override fun onItemClick(position: Int) {
//                                val intent = Intent(this@GoodsDetailActivity, GoodsDetailViewerActivity::class.java)
//                                intent.putExtra(Const.POSITION, pager_goods_detail_image.currentItem)
//                                intent.putExtra(Const.DATA, imageAdapter.dataList)
//                                startActivity(intent)
//                            }
//
//                        }
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Goods>>?, t: Throwable?, response: NewResultResponse<Goods>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    var animating = false
//
//    private fun scrollOutAnimation() {
//        val scrollOutAnimator = ObjectAnimator.ofFloat(layout_goods_detail_option, "translationY", 0f,
//                layout_goods_detail_option.height.toFloat()).apply {
//            duration = 300
//            interpolator = AccelerateDecelerateInterpolator()
//            addListener(object : AnimatorListenerAdapter() {
//                override fun onAnimationStart(animation: Animator?) {
//                    animating = true
//                }
//
//                override fun onAnimationCancel(animation: Animator?) {
//                    animating = true
//                }
//
//                override fun onAnimationEnd(animation: Animator?) {
//                    animating = false
//                    layout_goods_detail_option.visibility = View.GONE
//                }
//            })
//        }
//
//        if (!animating) {
//            scrollOutAnimator.start()
//        }
//    }
//
//    private fun scrollInAnimation() {
//        val scrollInAnimator = ObjectAnimator.ofFloat(layout_goods_detail_option, "translationY",
//                resources.getDimension(R.dimen.height_840), 0f).apply {
//            duration = 300
//            interpolator = AccelerateDecelerateInterpolator()
//            addListener(object : AnimatorListenerAdapter() {
//                override fun onAnimationStart(animation: Animator?) {
//
//                    layout_goods_detail_option.visibility = View.VISIBLE
//                    animating = true
//                }
//
//                override fun onAnimationCancel(animation: Animator?) {
//                    animating = true
//                }
//
//                override fun onAnimationEnd(animation: Animator?) {
//                    animating = false
//                }
//            })
//        }
//
//        if (!animating) {
//            scrollInAnimator.start()
//        }
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_goods_detail), ToolbarOption.ToolbarMenu.LEFT)
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
