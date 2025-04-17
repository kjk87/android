//package com.pplus.prnumberuser.apps.goods.ui
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
//import android.webkit.WebChromeClient
//import android.webkit.WebSettings
//import android.webkit.WebViewClient
//import android.widget.LinearLayout
//import android.widget.TextView
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
//import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.goods.data.GoodsShipTypeHeaderImageDetailAdapter
//import com.pplus.prnumberuser.apps.goods.data.GoodsShipTypeHeaderReviewAdapter
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.*
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.network.model.response.SubResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.prnumberuser.core.util.ToastUtil
//import com.pplus.networks.common.PplusCallback
//import com.pplus.utils.part.apps.resource.ResourceUtil
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.logs.LogUtil
//import com.pplus.utils.part.utils.StringUtils
//import kotlinx.android.synthetic.main.activity_goods_detail_ship_type.*
//import kotlinx.android.synthetic.main.item_option.view.*
//import kotlinx.android.synthetic.main.item_selected_option.view.*
//import retrofit2.Call
//import java.util.*
//import kotlin.collections.HashMap
//
//class GoodsDetailShipTypeActivity : BaseActivity() {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_goods_detail_ship_type
//    }
//
//    var mGoodsPrice: GoodsPrice? = null
//    var mGoodsOptionTotal: GoodsOptionTotal? = null
//    var mIsLike = false
//    var mCount = 1
//    var mGoodsShipTypeHeaderReviewAdapter : GoodsShipTypeHeaderReviewAdapter? = null
//    private var mPaging: Int = 0
//    private var mTotalCount = 0
//    private var mLayoutManager: LinearLayoutManager? = null
//    private var mLockListView = true
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//        mGoodsPrice = intent.getParcelableExtra(Const.DATA)
//
//        image_goods_detail_ship_type_back.setOnClickListener {
//            onBackPressed()
//        }
//
//        webview_goods_detail_ship_type.webChromeClient = WebChromeClient()
//        webview_goods_detail_ship_type.webViewClient = WebViewClient()
//        webview_goods_detail_ship_type.settings.javaScriptEnabled = true
//        webview_goods_detail_ship_type.settings.javaScriptCanOpenWindowsAutomatically = true
//        webview_goods_detail_ship_type.settings.setAppCacheEnabled(false)
//        webview_goods_detail_ship_type.settings.cacheMode = WebSettings.LOAD_NO_CACHE
//
//        layout_goods_detail_ship_type_bottom.visibility = View.GONE
//
//        layout_goods_detail_ship_type_review.setOnClickListener {
//            val intent = Intent(this, GoodsReviewActivity::class.java)
//            intent.putExtra(Const.GOODS_PRICE, mGoodsPrice)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivityForResult(intent, Const.REQ_REVIEW)
//        }
//
//        image_goods_detail_ship_type_like.setOnClickListener {
//            if(!PplusCommonUtil.loginCheck(this)){
//                return@setOnClickListener
//            }
//
//            if(!mIsLike){
//                postLike()
//            }else{
//                deleteLike()
//            }
//        }
//
//        text_goods_detail_ship_type_show_option.setOnClickListener {
//            scrollInAnimation()
//        }
//
//        image_goods_detail_ship_type_not_option_close.setOnClickListener {
//            scrollOutAnimation()
//        }
//
//        image_goods_detail_ship_type_not_option_minus.setOnClickListener {
//            if(mCount > 1){
//                mCount--
//            }
//            text_goods_detail_ship_type_not_option_count.text = mCount.toString()
//            setTotalPrice()
//        }
//
//        image_goods_detail_ship_type_not_option_plus.setOnClickListener {
//            if(mGoodsPrice!!.goods!!.count != -1){
//                val remainCount = mGoodsPrice!!.goods!!.count!! - mGoodsPrice!!.goods!!.soldCount!!
//                if(mCount < remainCount){
//                    mCount++
//                }
//            }else{
//                mCount++
//            }
//            text_goods_detail_ship_type_not_option_count.text = mCount.toString()
//            setTotalPrice()
//        }
//
//        text_goods_detail_ship_type_buy.setOnClickListener {
//
//            if(!PplusCommonUtil.loginCheck(this)){
//                return@setOnClickListener
//            }
//
//            var totalCount = 0
//            if (mGoodsPrice!!.goods!!.optionType != null && mGoodsPrice!!.goods!!.optionType != 0) {
//                if(mSelectBuyGoodsOptionList.isEmpty()){
//                    ToastUtil.show(this, R.string.msg_select_option)
//                    return@setOnClickListener
//                }
//
//                for(buyGoodsOption in mSelectBuyGoodsOptionList){
//                    totalCount += buyGoodsOption.amount!!
//                }
//            }else{
//                totalCount = mCount
//            }
//
//            if(mGoodsPrice!!.goods!!.buyableCount != null && mGoodsPrice!!.goods!!.buyableCount!! > 0){
//
//                val params = HashMap<String, String>()
//                params["goodsSeqNo"] = mGoodsPrice!!.goods!!.seqNo.toString()
//                showProgress("")
//                ApiBuilder.create().getBuyCountByGoodsSeqNo(params).setCallback(object :PplusCallback<NewResultResponse<Count>>{
//                    override fun onResponse(call: Call<NewResultResponse<Count>>?, response: NewResultResponse<Count>?) {
//                        hideProgress()
//                        if(response?.data != null) {
//                            val buyCount = response.data.count
//                            if (totalCount + buyCount!! > mGoodsPrice!!.goods!!.buyableCount!!) {
//                                ToastUtil.show(this@GoodsDetailShipTypeActivity, R.string.msg_over_buyable_count)
//                                return
//                            } else {
//                                val buyGoodsList = ArrayList<BuyGoods>()
//                                val buyGoods = BuyGoods()
//                                buyGoods.goods = mGoodsPrice!!.goods
//                                buyGoods.goodsPriceData = mGoodsPrice
//                                buyGoods.count = totalCount
//                                if (mGoodsPrice!!.goods!!.optionType != null && mGoodsPrice!!.goods!!.optionType != 0) {
//                                    buyGoods.buyGoodsOptionSelectList = mSelectBuyGoodsOptionList
//                                }
//                                buyGoods.deliveryFee = mGoodsPrice!!.goods!!.deliveryFee
//                                if (buyGoods.deliveryFee == null || buyGoods.deliveryFee!! < 0) {
//                                    buyGoods.deliveryFee = 0
//                                }
//
//                                buyGoodsList.add(buyGoods)
//
//                                val intent = Intent(this@GoodsDetailShipTypeActivity, GoodsBuyShipTypeActivity::class.java)
//                                intent.putParcelableArrayListExtra(Const.BUY_GOODS, buyGoodsList)
//                                startActivityForResult(intent, Const.REQ_ORDER)
//                            }
//                        }
//                    }
//
//                    override fun onFailure(call: Call<NewResultResponse<Count>>?, t: Throwable?, response: NewResultResponse<Count>?) {
//                        hideProgress()
//                    }
//                }).build().call()
//            }else{
//                val buyGoodsList = ArrayList<BuyGoods>()
//                val buyGoods = BuyGoods()
//                buyGoods.goods = mGoodsPrice!!.goods
//                buyGoods.goodsPriceData = mGoodsPrice
//                buyGoods.count = totalCount
//                if (mGoodsPrice!!.goods!!.optionType != null && mGoodsPrice!!.goods!!.optionType != 0) {
//                    buyGoods.buyGoodsOptionSelectList = mSelectBuyGoodsOptionList
//                }
//                buyGoods.deliveryFee = mGoodsPrice!!.goods!!.deliveryFee
//                if(buyGoods.deliveryFee == null){
//                    buyGoods.deliveryFee = 0
//                }
//                buyGoodsList.add(buyGoods)
//
//                val intent = Intent(this@GoodsDetailShipTypeActivity, GoodsBuyShipTypeActivity::class.java)
//                intent.putParcelableArrayListExtra(Const.BUY_GOODS, buyGoodsList)
//                startActivityForResult(intent, Const.REQ_ORDER)
//            }
//        }
//
//        text_goods_detail_ship_type_title.setSingleLine()
//        setData()
//    }
//
//    private fun postLike() {
//
//        val goodsLike = GoodsLike()
//        goodsLike.memberSeqNo = LoginInfoManager.getInstance().user.no
//        goodsLike.goodsSeqNo = mGoodsPrice!!.goods!!.seqNo
//        goodsLike.goodsPriceSeqNo = mGoodsPrice!!.seqNo
//        goodsLike.pageSeqNo = mGoodsPrice!!.goods!!.page!!.seqNo
//        showProgress("")
//        ApiBuilder.create().postGoodsLike(goodsLike).setCallback(object : PplusCallback<NewResultResponse<GoodsLike>> {
//            override fun onResponse(call: Call<NewResultResponse<GoodsLike>>?, response: NewResultResponse<GoodsLike>?) {
//                hideProgress()
//                mIsLike = true
//                image_goods_detail_ship_type_like.isSelected = true
//                val intent = Intent(this@GoodsDetailShipTypeActivity, AlertGoodsLikeCompleteActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                startActivity(intent)
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
//        goodsLike.goodsSeqNo = mGoodsPrice!!.goods!!.seqNo
//        goodsLike.goodsPriceSeqNo = mGoodsPrice!!.seqNo
//        goodsLike.pageSeqNo = mGoodsPrice!!.goods!!.page!!.seqNo
//        showProgress("")
//        ApiBuilder.create().deleteGoodsLike(goodsLike).setCallback(object : PplusCallback<NewResultResponse<Any>> {
//            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
//                hideProgress()
//                mIsLike = false
//                image_goods_detail_ship_type_like.isSelected = false
//                ToastUtil.show(this@GoodsDetailShipTypeActivity, R.string.msg_delete_goods_like)
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    private fun setData() {
//        val params = HashMap<String, String>()
//
//        params["seqNo"] = mGoodsPrice!!.seqNo.toString()
//
//        showProgress("")
//        ApiBuilder.create().getGoodsPrice(params).setCallback(object : PplusCallback<NewResultResponse<GoodsPrice>> {
//            override fun onResponse(call: Call<NewResultResponse<GoodsPrice>>?, response: NewResultResponse<GoodsPrice>?) {
//                hideProgress()
//                if (response?.data != null) {
//                    mGoodsPrice = response.data
//
//                    if (mGoodsPrice!!.goods!!.optionType != null && mGoodsPrice!!.goods!!.optionType != 0) {
//                        scroll_goods_detail_ship_type_option.visibility = View.VISIBLE
//                        view_goods_detail_ship_type_option_bar.visibility = View.VISIBLE
//                        layout_goods_detail_ship_type_not_option.visibility = View.GONE
//                        layout_goods_detail_ship_type_total_price.setBackgroundColor(ResourceUtil.getColor(this@GoodsDetailShipTypeActivity, R.color.color_f0f0f0))
//                        getOption()
//                    }else{
//                        scroll_goods_detail_ship_type_option.visibility = View.GONE
//                        view_goods_detail_ship_type_option_bar.visibility = View.GONE
//                        layout_goods_detail_ship_type_not_option.visibility = View.VISIBLE
//                        layout_goods_detail_ship_type_total_price.setBackgroundColor(ResourceUtil.getColor(this@GoodsDetailShipTypeActivity, R.color.white))
//                        setTotalPrice()
//                    }
//
//                    if (LoginInfoManager.getInstance().isMember) {
//                        checkLike()
//                    }
//                    getReviewCount()
//
//                    text_goods_detail_ship_type_title.text = mGoodsPrice!!.goods!!.name
//                    layout_goods_detail_ship_type_review.visibility = View.VISIBLE
//                    view_goods_detail_ship_type_review_bar.visibility = View.VISIBLE
//                    when(mGoodsPrice!!.goods!!.detailType){
//                        "url"->{
//                            webview_goods_detail_ship_type.visibility = View.VISIBLE
//                            recycler_goods_detail_ship_type.visibility = View.GONE
//                            if(StringUtils.isNotEmpty(mGoodsPrice!!.goods!!.externalUrl)){
//                                webview_goods_detail_ship_type.loadUrl(mGoodsPrice!!.goods!!.externalUrl)
//                            }
//                        }
//                        "image"->{
//                            webview_goods_detail_ship_type.visibility = View.GONE
//                            recycler_goods_detail_ship_type.visibility = View.VISIBLE
//                            recycler_goods_detail_ship_type.layoutManager = LinearLayoutManager(this@GoodsDetailShipTypeActivity)
//                            val detailAdapter = GoodsShipTypeHeaderImageDetailAdapter()
//                            detailAdapter.mGoodsPrice = mGoodsPrice
//                            recycler_goods_detail_ship_type.adapter = detailAdapter
//                            val params = HashMap<String, String>()
//                            params["goodsSeqNo"] = mGoodsPrice!!.goods!!.seqNo.toString()
//                            params["type"] = "detail"
//                            ApiBuilder.create().getGoodsImageList(params).setCallback(object : PplusCallback<NewResultResponse<GoodsImage>>{
//                                override fun onResponse(call: Call<NewResultResponse<GoodsImage>>?, response: NewResultResponse<GoodsImage>?) {
//                                    if(response?.datas != null){
//                                        detailAdapter.setDataList(response.datas)
//                                    }
//                                }
//
//                                override fun onFailure(call: Call<NewResultResponse<GoodsImage>>?, t: Throwable?, response: NewResultResponse<GoodsImage>?) {
//
//                                }
//                            }).build().call()
//
//                        }
//                        else->{
//                            layout_goods_detail_ship_type_review.visibility = View.GONE
//                            view_goods_detail_ship_type_review_bar.visibility = View.GONE
//                            webview_goods_detail_ship_type.visibility = View.GONE
//                            recycler_goods_detail_ship_type.visibility = View.VISIBLE
//                            mGoodsShipTypeHeaderReviewAdapter = GoodsShipTypeHeaderReviewAdapter()
//                            mGoodsShipTypeHeaderReviewAdapter!!.mGoodsPrice = mGoodsPrice
//                            val linearLayoutManager = LinearLayoutManager(this@GoodsDetailShipTypeActivity)
//                            recycler_goods_detail_ship_type.layoutManager = linearLayoutManager
//                            recycler_goods_detail_ship_type.adapter = mGoodsShipTypeHeaderReviewAdapter
//
//                            recycler_goods_detail_ship_type.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//
//                                internal var pastVisibleItems: Int = 0
//                                internal var visibleItemCount: Int = 0
//                                internal var totalItemCount: Int = 0
//
//                                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//
//                                    super.onScrolled(recyclerView, dx, dy)
//                                    visibleItemCount = linearLayoutManager.childCount
//                                    totalItemCount = linearLayoutManager.itemCount
//                                    pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition()
//                                    if (!mLockListView) {
//                                        if (totalItemCount < mTotalCount && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
//                                            mPaging++
//                                            listCall(mPaging)
//                                        }
//                                    }
//                                }
//                            })
//
//                            mGoodsShipTypeHeaderReviewAdapter!!.setOnItemClickListener(object : GoodsShipTypeHeaderReviewAdapter.OnItemClickListener{
//                                override fun onItemClick(position: Int) {
//
//                                    val goodsReview = mGoodsShipTypeHeaderReviewAdapter!!.getItem(position)
//
//                                    if(goodsReview.member!!.seqNo == LoginInfoManager.getInstance().user.no){
//                                        val contents = arrayOf(getString(R.string.word_modified), getString(R.string.word_delete))
//                                        val builder = AlertBuilder.Builder()
//                                        builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER)
//                                        builder.setContents(*contents)
//                                        builder.setLeftText(getString(R.string.word_cancel))
//                                        builder.setOnAlertResultListener(object : OnAlertResultListener {
//
//                                            override fun onCancel() {
//
//                                            }
//
//                                            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//                                                when(event_alert){
//                                                    AlertBuilder.EVENT_ALERT.LIST->{
//                                                        when (event_alert.getValue()) {
//                                                            1 -> {
//                                                                val intent = Intent(this@GoodsDetailShipTypeActivity, GoodsReviewWriteActivity::class.java)
//                                                                intent.putExtra(Const.MODE, EnumData.MODE.UPDATE)
//                                                                intent.putExtra(Const.DATA, goodsReview)
//                                                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                                                                startActivityForResult(intent, Const.REQ_MODIFY)
//                                                            }
//                                                            2 -> {
//                                                                val params = java.util.HashMap<String, String>()
//                                                                params["seqNo"] = goodsReview.seqNo.toString()
//
//                                                                showProgress("")
//                                                                ApiBuilder.create().deleteGoodsReview(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
//                                                                    override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
//                                                                        hideProgress()
//                                                                        showAlert(R.string.msg_deleted)
//                                                                        mPaging = 0
//                                                                        listCall(mPaging)
//                                                                    }
//
//                                                                    override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
//                                                                        hideProgress()
//                                                                    }
//                                                                }).build().call()
//                                                            }
//                                                        }
//                                                    }
//                                                }
//                                            }
//                                        }).builder().show(this@GoodsDetailShipTypeActivity)
//
//                                    }
//                                }
//                            })
//
//                            mPaging = 0
//                            listCall(mPaging)
//                        }
//                    }
//
//
//                    if (mGoodsPrice!!.originPrice != null && mGoodsPrice!!.originPrice!! > 0) {
//
//                        if (mGoodsPrice!!.originPrice!! <= mGoodsPrice!!.price!!) {
//                            text_goods_detail_ship_type_origin_price.visibility = View.GONE
//                        } else {
//                            text_goods_detail_ship_type_origin_price.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(mGoodsPrice!!.originPrice.toString()))
//                            text_goods_detail_ship_type_origin_price.visibility = View.VISIBLE
//                            text_goods_detail_ship_type_origin_price.paintFlags = text_goods_detail_ship_type_origin_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
//                        }
//
//                    } else {
//                        text_goods_detail_ship_type_origin_price.visibility = View.GONE
//                    }
//
//                    if (mGoodsPrice!!.discountRatio != null && mGoodsPrice!!.discountRatio!! > 0) {
//                        text_goods_detail_ship_type_discount_ratio.visibility = View.VISIBLE
//                        text_goods_detail_ship_type_discount_ratio.text = PplusCommonUtil.fromHtml(getString(R.string.html_percent_unit, mGoodsPrice!!.discountRatio!!.toInt().toString()))
//                    } else {
//                        text_goods_detail_ship_type_discount_ratio.visibility = View.GONE
//                    }
//                    text_goods_detail_ship_type_sale_price.text = PplusCommonUtil.fromHtml(getString(R.string.html_money_unit, FormatUtil.getMoneyType(mGoodsPrice!!.price.toString())))
//                    text_goods_detail_ship_type_point.text = PplusCommonUtil.fromHtml(getString(R.string.html_ship_type_save_point, FormatUtil.getMoneyType((mGoodsPrice!!.price!!*0.1f).toInt().toString())))
//                    if(mGoodsPrice!!.goods!!.deliveryFee != null && mGoodsPrice!!.goods!!.deliveryFee!! > 0){
//                        text_goods_detail_ship_type_delivery_fee.text = getString(R.string.format_ship_price, FormatUtil.getMoneyType(mGoodsPrice!!.goods!!.deliveryFee.toString()))
//                        text_goods_detail_ship_type_delivery_fee2.text = FormatUtil.getMoneyType(mGoodsPrice!!.goods!!.deliveryFee.toString())
//                    }else{
//                        text_goods_detail_ship_type_delivery_fee.text = getString(R.string.format_ship_price, getString(R.string.word_free_ship))
//                        text_goods_detail_ship_type_delivery_fee2.text = getString(R.string.word_free_ship)
//                    }
//
//                    if(mGoodsPrice!!.isLuckyball != null && mGoodsPrice!!.isLuckyball!!){
//                        layout_goods_detail_ship_type_point.visibility = View.VISIBLE
//                    }else{
//                        layout_goods_detail_ship_type_point.visibility = View.GONE
//                    }
//
//                }else{
//                    showAlert(R.string.msg_not_exist_goods2)
//                    finish()
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<GoodsPrice>>?, t: Throwable?, response: NewResultResponse<GoodsPrice>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    private fun listCall(page: Int) {
//
//        mLockListView = true
//        val params = java.util.HashMap<String, String>()
//
////        params["goodsSeqNo"] = mGoodsPrice!!.goods!!.seqNo.toString()
//        params["goodsPriceSeqNo"] = mGoodsPrice!!.seqNo.toString()
//        params["sort"] = "${EnumData.BuyGoodsSort.seqNo.name},${EnumData.BuyGoodsSort.desc.name}"
//        params["page"] = page.toString()
//
//        showProgress("")
//        ApiBuilder.create().getGoodsReview(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<GoodsReview>>> {
//
//            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<GoodsReview>>>?, response: NewResultResponse<SubResultResponse<GoodsReview>>?) {
//                hideProgress()
//
//                if (response != null) {
//
//                    if (response.data.first!!) {
//                        mTotalCount = response.data.totalElements!!
//                        mGoodsShipTypeHeaderReviewAdapter!!.clear()
//                        mGoodsShipTypeHeaderReviewAdapter!!.mTotalCount = mTotalCount
//                    }
//
//                    mLockListView = false
//                    mGoodsShipTypeHeaderReviewAdapter!!.addAll(response.data.content!!)
//                }
//
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<GoodsReview>>>?, t: Throwable?, response: NewResultResponse<SubResultResponse<GoodsReview>>?) {
//                hideProgress()
//                mLockListView = false
//            }
//        }).build().call()
//    }
//
//    var mItemList = arrayListOf<GoodsOptionItem>()
//    var mOptionTextList = arrayListOf<TextView>()
//    var mSelectBuyGoodsOptionList = arrayListOf<BuyGoodsOption>()
//
//    private fun getOption() {
//        val params = HashMap<String, String>()
//
//        params["goodsSeqNo"] = mGoodsPrice!!.goods!!.seqNo.toString()
//        showProgress("")
//        ApiBuilder.create().getGoodsOption(params).setCallback(object : PplusCallback<NewResultResponse<GoodsOptionTotal>> {
//            override fun onResponse(call: Call<NewResultResponse<GoodsOptionTotal>>?, response: NewResultResponse<GoodsOptionTotal>?) {
//                hideProgress()
//                if(response?.data != null){
//                    mGoodsOptionTotal = response.data
//                    initOption()
//
//                }else{
//                    scroll_goods_detail_ship_type_option.visibility = View.GONE
//                    view_goods_detail_ship_type_option_bar.visibility = View.GONE
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<GoodsOptionTotal>>?, t: Throwable?, response: NewResultResponse<GoodsOptionTotal>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    private fun initOption(){
//        val goodsOptionList = mGoodsOptionTotal!!.goodsOptionList
//        val goodsOptionItemList = mGoodsOptionTotal!!.goodsOptionItemList
//        val goodsOptionDetailList = mGoodsOptionTotal!!.goodsOptionDetailList
//
//        layout_goods_detail_ship_type_option.removeAllViews()
//        if(goodsOptionList != null && goodsOptionList.isNotEmpty()){
//            mItemList = arrayListOf()
//            mOptionTextList = arrayListOf<TextView>()
//            for (i in goodsOptionList.indices){
//                mItemList.add(GoodsOptionItem())
//                val goodOption = goodsOptionList[i]
//                val view = layoutInflater.inflate(R.layout.item_option, LinearLayout(this@GoodsDetailShipTypeActivity))
//                view.text_option_name.setTextColor(ResourceUtil.getColor(this@GoodsDetailShipTypeActivity, R.color.color_737373))
//                view.text_option_name.text = goodOption.name
//                mOptionTextList.add(view.text_option_name)
//                view.setOnClickListener {
//                    if(i != 0 && mItemList[i-1].seqNo == null){
//                        ToastUtil.show(this@GoodsDetailShipTypeActivity, R.string.msg_select_before_option)
//                        return@setOnClickListener
//                    }
//                    if(i < goodsOptionList.size -1){
//                        val selectedItemList = goodsOptionItemList!!.filter {
//                            it.optionSeqNo == goodOption.seqNo
//                        }
//                        val intent = Intent(this@GoodsDetailShipTypeActivity, AlertGoodsOptionActivity::class.java)
//                        intent.putParcelableArrayListExtra(Const.ITEM, selectedItemList as ArrayList)
//                        intent.putExtra(Const.POSITION, i)
//                        intent.putExtra(Const.NAME, goodOption.name)
//                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                        startActivityForResult(intent, Const.REQ_GOODS_OPTION)
//                    }else{
//                        val selectedDetailList = goodsOptionDetailList!!.filter {
//                            when(goodsOptionList.size){
//                                1->{
//                                    true
//                                }
//                                2->{
//                                    it.depth1ItemSeqNo == mItemList[0].seqNo
//
//                                }
//                                else->{
//                                    it.depth1ItemSeqNo == mItemList[0].seqNo && it.depth2ItemSeqNo == mItemList[1].seqNo
//                                }
//                            }
//                        }
//
//                        val intent = Intent(this@GoodsDetailShipTypeActivity, AlertGoodsOptionActivity::class.java)
//                        intent.putParcelableArrayListExtra(Const.DETAIL, selectedDetailList as ArrayList)
//                        intent.putExtra(Const.POSITION, i)
//                        intent.putExtra(Const.NAME, goodOption.name)
//                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                        startActivityForResult(intent, Const.REQ_GOODS_OPTION_DETAIL)
//                    }
//                }
//                layout_goods_detail_ship_type_option.addView(view)
//            }
//        }else{
//            scroll_goods_detail_ship_type_option.visibility = View.GONE
//            view_goods_detail_ship_type_option_bar.visibility = View.GONE
//        }
//    }
//
//    private fun checkLike() {
//        val params = HashMap<String, String>()
//
//        params["memberSeqNo"] = LoginInfoManager.getInstance().user.no.toString()
//        params["goodsSeqNo"] = mGoodsPrice!!.goods!!.seqNo.toString()
//        params["pageSeqNo"] = mGoodsPrice!!.goods!!.page!!.seqNo.toString()
//        params["goodsPriceSeqNo"] = mGoodsPrice!!.seqNo.toString()
//
//        showProgress("")
//        mIsLike = false
//        image_goods_detail_ship_type_like.isEnabled = false
//        ApiBuilder.create().getGoodsLikeOne(params).setCallback(object : PplusCallback<NewResultResponse<GoodsLike>> {
//            override fun onResponse(call: Call<NewResultResponse<GoodsLike>>?, response: NewResultResponse<GoodsLike>?) {
//                hideProgress()
//                image_goods_detail_ship_type_like.isEnabled = true
//                image_goods_detail_ship_type_like.isSelected = false
//                if (response?.data != null && response.data.status == 1) {
//                    image_goods_detail_ship_type_like.isSelected = true
//                    mIsLike = true
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<GoodsLike>>?, t: Throwable?, response: NewResultResponse<GoodsLike>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    private fun getReviewCount(){
//        val params = HashMap<String, String>()
//        params["goodsPriceSeqNo"] = mGoodsPrice!!.seqNo.toString()
//        ApiBuilder.create().getGoodsReviewCount(params).setCallback(object : PplusCallback<NewResultResponse<Count>>{
//            override fun onResponse(call: Call<NewResultResponse<Count>>?, response: NewResultResponse<Count>?) {
//                if(response != null){
//                    val count = response.data.count
//                    text_goods_detail_ship_type_review_count?.text = FormatUtil.getMoneyType(count.toString())
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Count>>?, t: Throwable?, response: NewResultResponse<Count>?) {
//
//            }
//        }).build().call()
//    }
//
//    var animating = false
//
//    private fun scrollOutAnimation() {
//        val scrollOutAnimator = ObjectAnimator.ofFloat(layout_goods_detail_ship_type_bottom, "translationY", 0f,
//                layout_goods_detail_ship_type_bottom.height.toFloat()).apply {
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
//                    layout_goods_detail_ship_type_bottom.visibility = View.GONE
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
//        val scrollInAnimator = ObjectAnimator.ofFloat(layout_goods_detail_ship_type_bottom, "translationY",
//                resources.getDimension(R.dimen.height_840), 0f).apply {
//            duration = 300
//            interpolator = AccelerateDecelerateInterpolator()
//            addListener(object : AnimatorListenerAdapter() {
//                override fun onAnimationStart(animation: Animator?) {
//
//                    layout_goods_detail_ship_type_bottom.visibility = View.VISIBLE
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
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when(requestCode){
//            Const.REQ_SIGN_IN ->{
//                setData()
//            }
//            Const.REQ_GOODS_OPTION->{
//                if(resultCode == Activity.RESULT_OK && data != null){
//
//                    val item = data.getParcelableExtra<GoodsOptionItem>(Const.DATA)
//                    val position = data.getIntExtra(Const.POSITION, 0)
//                    LogUtil.e(LOG_TAG, "item : {}, position : {}", item.item, position)
//                    mItemList[position] = item
//                    mOptionTextList[position].text = item.item
//                    mOptionTextList[position].setTextColor(ResourceUtil.getColor(this, R.color.color_232323))
//                }
//
//            }
//            Const.REQ_GOODS_OPTION_DETAIL->{
//                if(resultCode == Activity.RESULT_OK && data != null){
//                    val detail = data.getParcelableExtra<GoodsOptionDetail>(Const.DATA)
//
//                    var isContain = false
//                    for(i in mSelectBuyGoodsOptionList.indices){
//                        val selectBuyGoodsOption = mSelectBuyGoodsOptionList[i]
//                        if(selectBuyGoodsOption.goodsOptionDetailSeqNo == detail.seqNo){
//                            isContain = true
//                            val remainCount = detail.amount!! - detail.soldCount!!
//                            if(selectBuyGoodsOption.amount!! < remainCount){
//                                selectBuyGoodsOption.amount = selectBuyGoodsOption.amount!!+1
//                            }
//                            setChangeOptionView(mSelectViewList[i], selectBuyGoodsOption)
//                            break
//                        }
//                    }
//
//                    if(!isContain){
//                        val buyGoodsOption = BuyGoodsOption()
//                        buyGoodsOption.amount = 1
//                        buyGoodsOption.goodsSeqNo = mGoodsPrice!!.goods!!.seqNo
//                        buyGoodsOption.goodsOptionDetailSeqNo = detail.seqNo
//                        buyGoodsOption.price = detail.price!!
//                        if(detail.item1 != null){
//                            buyGoodsOption.depth1 = detail.item1!!.item
//                        }
//                        if(detail.item2 != null){
//                            buyGoodsOption.depth2 = detail.item2!!.item
//                        }
//                        if(detail.item3 != null){
//                            buyGoodsOption.depth3 = detail.item3!!.item
//                        }
//                        mSelectBuyGoodsOptionList.add(buyGoodsOption)
//                        addSelectOption(buyGoodsOption, detail)
//                    }
//
//
//                    layout_goods_detail_ship_type_select_bar.visibility = View.VISIBLE
//                    layout_goods_detail_ship_type_select_item.visibility = View.VISIBLE
//
//                    initOption()
//                }
//            }
//        }
//    }
//
//    var mSelectViewList = arrayListOf<View>()
//
//    private fun addSelectOption(option: BuyGoodsOption, detail: GoodsOptionDetail){
//        val view = layoutInflater.inflate(R.layout.item_selected_option, LinearLayout(this))
//
//        when(mItemList.size){
//            1->{
//                view.text_selected_option.text = "${option.depth1}"
//            }
//            2->{
//                view.text_selected_option.text = "${option.depth1} / ${option.depth2}"
//            }
//            else->{
//                view.text_selected_option.text = "${option.depth1} / ${option.depth2} / ${option.depth3}"
//            }
//        }
//        view.image_selected_option_delete.setOnClickListener {
//            mSelectBuyGoodsOptionList.remove(option)
//            layout_goods_detail_ship_type_select_item.removeView(view)
//            setTotalPrice()
//            checkSelectOption()
//        }
//
//        view.image_selected_option_minus.setOnClickListener {
//            if(option.amount!! > 1){
//                option.amount = option.amount!!-1
//                setChangeOptionView(view, option)
//            }
//        }
//        view.image_selected_option_plus.setOnClickListener {
//            val remainCount = detail.amount!! - detail.soldCount!!
//            if(option.amount!! < remainCount){
//                option.amount = option.amount!!+1
//                setChangeOptionView(view, option)
//            }
//        }
//        setChangeOptionView(view, option)
//        mSelectViewList.add(view)
//        layout_goods_detail_ship_type_select_item.addView(view)
//    }
//
//    private fun setChangeOptionView(view:View, option: BuyGoodsOption){
//        view.text_selected_option_price.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(((mGoodsPrice!!.price!! + option.price!!)*option.amount!!).toString()))
//        view.text_selected_option_count.text = option.amount.toString()
//        setTotalPrice()
//    }
//
//    private fun setTotalPrice(){
//        var totalPrice = 0
//        if (mGoodsPrice!!.goods!!.optionType != null && mGoodsPrice!!.goods!!.optionType != 0) {
//            for(buyGoodsOption in mSelectBuyGoodsOptionList){
//                totalPrice += (mGoodsPrice!!.price!!.toInt() + buyGoodsOption.price!!)*buyGoodsOption.amount!!
//            }
//        }else{
//            totalPrice = mGoodsPrice!!.price!!.toInt() * mCount
//        }
//
//        if(mGoodsPrice!!.goods!!.deliveryFee!! > 0){
//            if(mGoodsPrice!!.goods!!.deliveryMinPrice == null || mGoodsPrice!!.goods!!.deliveryMinPrice!! <= 0){
//                totalPrice += mGoodsPrice!!.goods!!.deliveryFee!!
//            }else{
//                if(mGoodsPrice!!.goods!!.deliveryFee!! > 0 && (mGoodsPrice!!.goods!!.deliveryMinPrice!! > 0 && totalPrice < mGoodsPrice!!.goods!!.deliveryMinPrice!!)){
//                    totalPrice += mGoodsPrice!!.goods!!.deliveryFee!!
//                }
//            }
//        }
//
//
//        text_goods_detail_ship_type_total_price.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(totalPrice.toString()))
//    }
//
//    private fun checkSelectOption(){
//        if(mSelectBuyGoodsOptionList.isEmpty()){
//            layout_goods_detail_ship_type_select_bar.visibility = View.GONE
//            layout_goods_detail_ship_type_select_item.visibility = View.GONE
//        }else{
//            layout_goods_detail_ship_type_select_bar.visibility = View.VISIBLE
//            layout_goods_detail_ship_type_select_item.visibility = View.VISIBLE
//        }
//    }
//
//    override fun onBackPressed() {
//        if(layout_goods_detail_ship_type_bottom.visibility == View.VISIBLE){
//            scrollOutAnimation()
//        }else{
//            super.onBackPressed()
//        }
//    }
//}