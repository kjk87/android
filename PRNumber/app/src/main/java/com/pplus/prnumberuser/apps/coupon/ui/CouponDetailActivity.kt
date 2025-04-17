//package com.pplus.prnumberuser.apps.coupon.ui
//
//import android.animation.Animator
//import android.animation.AnimatorListenerAdapter
//import android.animation.ObjectAnimator
//import android.app.Activity
//import android.content.Intent
//import android.graphics.Paint
//import android.os.Bundle
//import android.view.View
//import android.view.animation.AccelerateDecelerateInterpolator
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.engine.DiskCacheStrategy
//import com.bumptech.glide.request.RequestOptions
//import com.pplus.utils.part.apps.resource.ResourceUtil
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.utils.part.utils.time.DateFormatUtils
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Goods
//import com.pplus.prnumberuser.core.network.model.dto.GoodsLike
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.activity_coupon_detail.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.text.ParseException
//import java.text.SimpleDateFormat
//import java.util.*
//
//class CouponDetailActivity : BaseActivity(), ImplToolbar {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_coupon_detail
//    }
//
//    var mCoupon: Goods? = null
//    var mCount = 1
//    override fun initializeView(savedInstanceState: Bundle?) {
//        mCoupon = intent.getParcelableExtra(Const.DATA)
//
//        image_coupon_detail_option_minus.setOnClickListener {
//            if (mCount > 1) {
//                mCount--
//                setCount()
//            }
//        }
//
//        image_coupon_detail_option_plus.setOnClickListener {
//
//            var maxCount = -1
//            if (mCoupon!!.count != -1) {
//
//                var soldCount = 0
//                if (mCoupon!!.soldCount != null) {
//                    soldCount = mCoupon!!.soldCount!!
//                }
//
//                maxCount = mCoupon!!.count!! - soldCount
//            }
//
//            if (maxCount != -1) {
//                if (mCount < maxCount) {
//                    mCount++
//                }
//            } else {
//                mCount++
//            }
//            setCount()
//        }
//
//        text_coupon_detail_download.setOnClickListener {
//            if (!PplusCommonUtil.loginCheck(this)) {
//                return@setOnClickListener
//            }
//
//            postLike(mCoupon!!)
//        }
//
//        text_coupon_detail_buy.setOnClickListener {
//            scrollInAnimation()
//        }
//
//        image_coupon_detail_option_close.setOnClickListener {
//            scrollOutAnimation()
//        }
//
//
//        text_coupon_detail_option_buy.setOnClickListener {
//            if (!PplusCommonUtil.loginCheck(this)) {
//                return@setOnClickListener
//            }
//
//            val intent = Intent(this, CouponBuyActivity::class.java)
//            intent.putExtra(Const.COUPON, mCoupon)
//            intent.putExtra(Const.COUNT, mCount)
//            startActivityForResult(intent, Const.REQ_ORDER)
//        }
//        text_coupon_detail_download.visibility = View.GONE
//        mCount = 1
//        setData()
//    }
//
//    private fun setData() {
//        val params = HashMap<String, String>()
//        params["seqNo"] = mCoupon!!.seqNo.toString()
//        showProgress("")
//
//        ApiBuilder.create().getOneGoods(params).setCallback(object : PplusCallback<NewResultResponse<Goods>> {
//            override fun onResponse(call: Call<NewResultResponse<Goods>>?, response: NewResultResponse<Goods>?) {
//                hideProgress()
//                if (response != null && response.data != null) {
//                    mCoupon = response.data
//                    if(LoginInfoManager.getInstance().isMember){
//                        checkLike(mCoupon!!)
//                    }
//
//                    if (StringUtils.isNotEmpty(mCoupon!!.page!!.thumbnail)) {
//                        Glide.with(this@CouponDetailActivity).load(mCoupon!!.page!!.thumbnail).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(image_coupon_detail_page_image)
//                    } else {
//                        image_coupon_detail_page_image.setImageResource(R.drawable.prnumber_default_img)
//                    }
//
//                    if(mCoupon!!.discountRatio != null && mCoupon!!.discountRatio!! > 0){
//                        text_coupon_detail_discount_ratio.visibility = View.VISIBLE
//                        text_coupon_detail_discount_ratio.text = "${mCoupon!!.discountRatio!!.toInt()}%"
//                    }else{
//                        text_coupon_detail_discount_ratio.visibility = View.GONE
//                    }
//
//                    text_coupon_detail_use_place.text = mCoupon!!.page!!.name
//                    text_coupon_detail_use_place.setOnClickListener {
//                        val location = IntArray(2)
//                        it.getLocationOnScreen(location)
//                        val x = location[0] + it.width / 2
//                        val y = location[1] + it.height / 2
//                        PplusCommonUtil.goPage(this@CouponDetailActivity, mCoupon!!.page!!, x, y)
//                    }
//
//                    text_coupon_detail_name.text = mCoupon!!.name
//
//                    text_coupon_detail_origin_price.paintFlags = text_coupon_detail_origin_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
//                    val originPrice = mCoupon!!.originPrice!!
//                    text_coupon_detail_origin_price.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(originPrice.toString()))
//
//                    var ing = false
//                    if (mCoupon!!.status == EnumData.GoodsStatus.soldout.status) {
//
//                        layout_coupon_detail_status.visibility = View.VISIBLE
//                        text_coupon_detail_status.setText(R.string.word_sold_out)
//                        text_coupon_detail_status.setTextColor(ResourceUtil.getColor(this@CouponDetailActivity, R.color.white))
//                    } else if (mCoupon!!.status == EnumData.GoodsStatus.finish.status) {
//
//                        layout_coupon_detail_status.visibility = View.VISIBLE
//                        text_coupon_detail_status.setText(R.string.word_sold_finish)
//                        text_coupon_detail_status.setTextColor(ResourceUtil.getColor(this@CouponDetailActivity, R.color.color_ff4646))
//                        if (StringUtils.isNotEmpty(mCoupon!!.expireDatetime)) {
//                            val format = SimpleDateFormat(DateFormatUtils.PPLUS_DATE_FORMAT.pattern, Locale.getDefault())
//                            var date = Date()
//                            try {
//                                date = format.parse(mCoupon!!.expireDatetime)
//                            } catch (e: ParseException) {
//                                e.printStackTrace()
//                            }
//
//                            val currentMillis = System.currentTimeMillis()
//                            if (date.time <= currentMillis) {
//                                text_coupon_detail_status.text = getString(R.string.word_sold_finish) + "\n" + getString(R.string.word_expired)
//                            }
//                        }
//                    } else if (mCoupon!!.status == EnumData.GoodsStatus.stop.status) {
//                        layout_coupon_detail_status.visibility = View.VISIBLE
//                        text_coupon_detail_status.setText(R.string.word_sold_stop)
//                        text_coupon_detail_status.setTextColor(ResourceUtil.getColor(this@CouponDetailActivity, R.color.color_ff4646))
//
//                    } else {
//
//                        //"expireDatetime":"2018-10-18 15:00:59"
//
//                        if (StringUtils.isNotEmpty(mCoupon!!.expireDatetime)) {
//                            val format = SimpleDateFormat(DateFormatUtils.PPLUS_DATE_FORMAT.pattern, Locale.getDefault())
//                            var date = Date()
//                            try {
//                                date = format.parse(mCoupon!!.expireDatetime)
//                            } catch (e: ParseException) {
//                                e.printStackTrace()
//                            }
//
//                            val currentMillis = System.currentTimeMillis()
//                            if (date.time <= currentMillis) {
//                                layout_coupon_detail_status.visibility = View.VISIBLE
//                                text_coupon_detail_status.text = getString(R.string.word_sold_finish) + "\n" + getString(R.string.word_expired)
//                                text_coupon_detail_status.setTextColor(ResourceUtil.getColor(this@CouponDetailActivity, R.color.white))
//                            } else {
//                                layout_coupon_detail_status.visibility = View.GONE
//                                ing = true
//                            }
//                        } else {
//                            layout_coupon_detail_status.visibility = View.GONE
//                            ing = true
//                        }
//                    }
//
//                    if (ing) {
//                        text_coupon_detail_name.setTextColor(ResourceUtil.getColor(this@CouponDetailActivity, R.color.color_232323))
//                        text_coupon_detail_sale_price.setTextColor(ResourceUtil.getColor(this@CouponDetailActivity, R.color.color_ff4646))
//                    } else {
//                        text_coupon_detail_name.setTextColor(ResourceUtil.getColor(this@CouponDetailActivity, R.color.color_b7b7b7))
//                        text_coupon_detail_sale_price.setTextColor(ResourceUtil.getColor(this@CouponDetailActivity, R.color.color_b7b7b7))
//                    }
//                    text_coupon_detail_sale_price.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(mCoupon!!.price.toString()))
//
//                    if (StringUtils.isNotEmpty(mCoupon!!.expireDatetime)) {
//                        text_coupon_detail_expire_date.visibility = View.VISIBLE
//                        layout_coupon_detail_expire_date.visibility = View.VISIBLE
//                        text_coupon_detail_expire_date.text = SimpleDateFormat("yyyy.MM.dd").format(DateFormatUtils.PPLUS_DATE_FORMAT.parse(mCoupon!!.expireDatetime)) + " " + getString(R.string.word_until)
//                    } else {
//                        text_coupon_detail_expire_date.visibility = View.GONE
//                        layout_coupon_detail_expire_date.visibility = View.GONE
//                    }
//
////                    if (mCoupon!!.rewardLuckybol != null) {
////                        layout_coupon_detail_save_point.visibility = View.VISIBLE
////                        text_coupon_detail_save_point.text = getString(R.string.format_point_unit, FormatUtil.getMoneyType(mCoupon!!.rewardLuckybol.toString()))
////                    } else {
////                        layout_coupon_detail_save_point.visibility = View.GONE
////                    }
//
//                    if (mCoupon!!.allDays != null) {
//                        if (mCoupon!!.allDays!!) {
//                            text_coupon_detail_use_time.text = getString(R.string.word_every_time_use)
//                        } else {
//                            text_coupon_detail_use_time.text = mCoupon!!.startTime + "~" + mCoupon!!.endTime
//                        }
//                    }else{
//                        text_coupon_detail_use_time.text = getString(R.string.word_every_time_use)
//                    }
//
//                    if (mCoupon!!.allWeeks != null) {
//                        if (mCoupon!!.allWeeks!!) {
//                            text_coupon_detail_dayofweeks.text = getString(R.string.word_every_dayofweek)
//                        } else {
//                            val dayOfWeek = mCoupon!!.dayOfWeeks!!.replace(",", "/").replace("0", getString(R.string.word_mon)).replace("1", getString(R.string.word_tue))
//                                    .replace("2", getString(R.string.word_wed)).replace("3", getString(R.string.word_thu)).replace("4", getString(R.string.word_fri))
//                                    .replace("5", getString(R.string.word_sat)).replace("6", getString(R.string.word_sun))
//
//                            text_coupon_detail_dayofweeks.text = dayOfWeek
//                        }
//                    } else {
//                        text_coupon_detail_dayofweeks.text = getString(R.string.word_every_dayofweek)
//                    }
//
//                    if(mCoupon!!.count != -1){
//                        layout_coupon_detail_count.visibility = View.VISIBLE
//
//                        var soldCount = 0
//                        if (mCoupon!!.soldCount != null) {
//                            soldCount = mCoupon!!.soldCount!!
//                        }
//                        text_coupon_detail_count.text = PplusCommonUtil.fromHtml(getString(R.string.html_goods_remain_count, FormatUtil.getMoneyType((mCoupon!!.count!! - soldCount).toString())))
//                    }else{
//                        layout_coupon_detail_count.visibility = View.GONE
//                    }
//
//                    if (mCoupon!!.page!!.point!! > 0) {
//                        layout_coupon_detail_save_point.visibility = View.VISIBLE
//                        val point = mCoupon!!.price!! * (mCoupon!!.page!!.point!! / 100)
//                        text_coupon_detail_save_point.text = PplusCommonUtil.fromHtml(getString(R.string.html_reward_point, FormatUtil.getMoneyType(point.toInt().toString())))
//
//                    } else {
//                        layout_coupon_detail_save_point.visibility = View.GONE
//                    }
//
//                    if (StringUtils.isNotEmpty(mCoupon!!.serviceCondition)) {
//                        layout_coupon_detail_use_condition.visibility = View.VISIBLE
//                        text_coupon_detail_use_condition.text = mCoupon!!.serviceCondition
//                    } else {
//                        layout_coupon_detail_use_condition.visibility = View.GONE
//                    }
//
////                    image_coupon_detail_page.setOnClickListener {
////                        val location = IntArray(2)
////                        it.getLocationOnScreen(location)
////                        val x = location[0] + it.width / 2
////                        val y = location[1] + it.height / 2
////                        PplusCommonUtil.goPage(this@CouponDetailActivity, mCoupon!!.page!!, x, y)
////                    }
////
////                    image_coupon_detail_tel.setOnClickListener {
////                        val phone = mCoupon!!.page!!.phone
////                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${phone}"))
////                        startActivity(intent)
////                    }
////
////                    image_coupon_detail_map.setOnClickListener {
////                        val intent = Intent(this@CouponDetailActivity, LocationPageActivity::class.java)
////                        intent.putExtra(Const.PAGE2, mCoupon!!.page)
////                        startActivity(intent)
////                    }
//
//
//
//                    setCount()
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Goods>>?, t: Throwable?, response: NewResultResponse<Goods>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    private fun setCount() {
//        text_coupon_detail_option_count.text = mCount.toString()
//        val totalPrice = mCount * mCoupon!!.price!!
//        text_coupon_detail_option_price.text = PplusCommonUtil.fromHtml(getString(R.string.html_total_price2, FormatUtil.getMoneyType(totalPrice.toString())))
//    }
//
//    var animating = false
//
//    private fun scrollOutAnimation() {
//        val scrollOutAnimator = ObjectAnimator.ofFloat(layout_coupon_detail_option, "translationY", 0f,
//                layout_coupon_detail_option.height.toFloat()).apply {
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
//                    layout_coupon_detail_option.visibility = View.GONE
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
//        val scrollInAnimator = ObjectAnimator.ofFloat(layout_coupon_detail_option, "translationY",
//                resources.getDimension(R.dimen.height_840), 0f).apply {
//            duration = 300
//            interpolator = AccelerateDecelerateInterpolator()
//            addListener(object : AnimatorListenerAdapter() {
//                override fun onAnimationStart(animation: Animator?) {
//
//                    layout_coupon_detail_option.visibility = View.VISIBLE
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
//    private fun checkLike(goods: Goods) {
//        val params = HashMap<String, String>()
//
//        params["memberSeqNo"] = LoginInfoManager.getInstance().user.no.toString()
//        params["goodsSeqNo"] = goods.seqNo.toString()
//        params["pageSeqNo"] = goods.page!!.seqNo.toString()
//
//        showProgress("")
//        ApiBuilder.create().getGoodsLikeOne(params).setCallback(object : PplusCallback<NewResultResponse<GoodsLike>> {
//            override fun onResponse(call: Call<NewResultResponse<GoodsLike>>?, response: NewResultResponse<GoodsLike>?) {
//                hideProgress()
//
//                if (response != null) {
//                    if (response.data != null && response.data.status == 1) {
//                        text_coupon_detail_download.visibility = View.GONE
////                        ToastUtil.show(this@CouponDetailActivity, R.string.msg_already_download_coupon)
//                    }else{
//                        text_coupon_detail_download.visibility = View.VISIBLE
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<GoodsLike>>?, t: Throwable?, response: NewResultResponse<GoodsLike>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    private fun postLike(goods: Goods) {
//
//        val goodsLike = GoodsLike()
//        goodsLike.memberSeqNo = LoginInfoManager.getInstance().user.no
//        goodsLike.goodsSeqNo = goods.seqNo
//        goodsLike.pageSeqNo = goods.page!!.seqNo
//        showProgress("")
//        ApiBuilder.create().postGoodsLike(goodsLike).setCallback(object : PplusCallback<NewResultResponse<GoodsLike>> {
//            override fun onResponse(call: Call<NewResultResponse<GoodsLike>>?, response: NewResultResponse<GoodsLike>?) {
//                hideProgress()
//                val intent = Intent(this@CouponDetailActivity, AlertCouponDownloadCompleteActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                startActivity(intent)
//                setResult(Activity.RESULT_OK)
//                finish()
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<GoodsLike>>?, t: Throwable?, response: NewResultResponse<GoodsLike>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when(requestCode){
//            Const.REQ_SIGN_IN->{
//                if(LoginInfoManager.getInstance().isMember){
//                    checkLike(mCoupon!!)
//                }
//            }
//        }
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_coupon_detail), ToolbarOption.ToolbarMenu.LEFT)
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
//
//    }
//
//}
