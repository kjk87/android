//package com.pplus.prnumberuser.apps.goods.ui
//
//import android.animation.Animator
//import android.animation.AnimatorListenerAdapter
//import android.animation.ObjectAnimator
//import android.content.Context
//import android.content.Intent
//import android.graphics.Rect
//import android.os.Bundle
//import android.view.View
//import android.view.animation.AccelerateDecelerateInterpolator
//import androidx.annotation.DimenRes
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
//import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
//import com.pplus.prnumberuser.apps.common.builder.data.AlertData
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.common.mgmt.SelectedGoodsManager
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.goods.data.GoodsHeaderReviewAdapter
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.BuyGoods
//import com.pplus.prnumberuser.core.network.model.dto.Goods
//import com.pplus.prnumberuser.core.network.model.dto.GoodsLike
//import com.pplus.prnumberuser.core.network.model.dto.GoodsReview
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.network.model.response.SubResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.prnumberuser.core.util.ToastUtil
//import kotlinx.android.synthetic.main.activity_goods_detail2.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.util.*
//
//class GoodsDetailActivity2 : BaseActivity() {
//    override fun getPID(): String {
//        return "Main_surrounding_product detail"
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_goods_detail2
//    }
//
//    var mGoods: Goods? = null
//    var mCount = 1
//    var mTotalPrice = 0L
//    var mAdapter: GoodsHeaderReviewAdapter? = null
//    private var mPaging: Int = 0
//    private var mTotalCount = 0
//    private var mLayoutManager: LinearLayoutManager? = null
//    private var mLockListView = true
//    private var mIsLast = false
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//        mGoods = intent.getParcelableExtra(Const.DATA)
//
//
//        mAdapter = GoodsHeaderReviewAdapter(this)
//        mLayoutManager = LinearLayoutManager(this)
//        recycler_goods_detail.layoutManager = mLayoutManager
//        recycler_goods_detail.adapter = mAdapter
//        recycler_goods_detail.addItemDecoration(CustomItemOffsetDecoration(this, R.dimen.height_200))
//        recycler_goods_detail.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//
//            internal var pastVisibleItems: Int = 0
//            internal var visibleItemCount: Int = 0
//            internal var totalItemCount: Int = 0
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//
//                super.onScrolled(recyclerView, dx, dy)
//                visibleItemCount = mLayoutManager!!.childCount
//                totalItemCount = mLayoutManager!!.itemCount
//                pastVisibleItems = mLayoutManager!!.findFirstVisibleItemPosition()
//                if (!mLockListView) {
//                    if (!mIsLast && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
//                        mPaging++
//                        listCall(mPaging)
//                    }
//                }
//            }
//        })
//
//        image_goods_detail_back.setOnClickListener {
//            onBackPressed()
//        }
//
//        text_goods_detail_title.text = getString(R.string.word_goods_detail)
//
//        text_goods_detail_buy.setOnClickListener {
//
//            scrollInAnimation()
//        }
//
//        image_goods_detail_like.setOnClickListener {
//
//            if (!PplusCommonUtil.loginCheck(this)) {
//                return@setOnClickListener
//            }
//
//            if (mIsLike) {
//                deleteLike()
//            } else {
//                postLike()
//            }
//        }
////        text_goods_detail_cart.setOnClickListener {
////            scrollInAnimation()
////        }
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
//                            val intent = Intent(this@GoodsDetailActivity2, CartActivity::class.java)
//                            intent.putExtra(Const.PAGE_SEQ_NO, mGoods!!.page!!.seqNo)
//                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                            startActivity(intent)
//                        }
//                    }
//                }
//            })
//            builder.builder().show(this@GoodsDetailActivity2)
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
////        layout_pplus_info_btn.setOnClickListener {
////            if (layout_pplus_info.visibility == View.GONE) {
////                text_pplus_info_btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_footer_arrow_up, 0)
////                layout_pplus_info.visibility = View.VISIBLE
////            } else if (layout_pplus_info.visibility == View.VISIBLE) {
////                text_pplus_info_btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_footer_arrow_down, 0)
////                layout_pplus_info.visibility = View.GONE
////            }
////        }
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
//                image_goods_detail_like.isSelected = true
//                val intent = Intent(this@GoodsDetailActivity2, AlertGoodsLikeCompleteActivity::class.java)
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
//        goodsLike.goodsSeqNo = mGoods!!.seqNo
//        goodsLike.pageSeqNo = mGoods!!.page!!.seqNo
//        showProgress("")
//        ApiBuilder.create().deleteGoodsLike(goodsLike).setCallback(object : PplusCallback<NewResultResponse<Any>> {
//            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
//                hideProgress()
//                mIsLike = false
//                image_goods_detail_like.isSelected = false
//                ToastUtil.show(this@GoodsDetailActivity2, R.string.msg_delete_goods_like)
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
//                image_goods_detail_like.isSelected = false
//                if (response != null) {
//                    if (response.data != null && response.data.status == 1) {
//                        image_goods_detail_like.isSelected = true
//                        mIsLike = true
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
//                    if (LoginInfoManager.getInstance().isMember) {
//                        checkLike()
//                    }
//
//                    if (mGoods!!.isPlus!! || mGoods!!.isHotdeal!!) {
//                        text_goods_detail_option_cart.visibility = View.GONE
//                    } else {
//                        text_goods_detail_option_cart.visibility = View.VISIBLE
//                    }
//
//                    if(StringUtils.isNotEmpty(mGoods!!.externalUrl)){
//                        text_goods_detail_url.visibility = View.VISIBLE
//                        text_goods_detail_url.setOnClickListener {
//                            PplusCommonUtil.openChromeWebView(this@GoodsDetailActivity2, mGoods!!.externalUrl!!)
//                        }
//                    }else{
//                        text_goods_detail_url.visibility = View.GONE
//                    }
//
//                    mAdapter!!.mGoods = mGoods
//                    mAdapter!!.notifyDataSetChanged()
//
//                    changeOption()
//                    mPaging = 0
//                    listCall(mPaging)
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Goods>>?, t: Throwable?, response: NewResultResponse<Goods>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    private fun listCall(page: Int) {
//
//        mLockListView = true
//        val params = HashMap<String, String>()
//
//        params["pageSeqNo"] = mGoods!!.page!!.seqNo.toString()
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
//                    mIsLast = response.data.last!!
//                    if (response.data.first!!) {
//                        mTotalCount = response.data.totalElements!!
//                        mAdapter!!.mTotalCount = mTotalCount
//                        mAdapter!!.clear()
//
////                        if (mTotalCount == 0) {
////                            layout_page_review_not_exist.visibility = View.VISIBLE
////                        } else {
////                            layout_page_review_not_exist.visibility = View.GONE
////                        }
//                    }
//
//                    mLockListView = false
//                    mAdapter!!.addAll(response.data.content!!)
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
//    private inner class CustomItemOffsetDecoration(private val mLastOffset: Int) : RecyclerView.ItemDecoration() {
//
//        constructor(context: Context, @DimenRes lastOffsetId: Int) : this(context.resources.getDimensionPixelSize(lastOffsetId)) {}
//
//        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
//
//            super.getItemOffsets(outRect, view, parent, state)
//
//            val position = parent.getChildAdapterPosition(view)
//            if (mAdapter!!.itemCount > 0 && position == mAdapter!!.itemCount - 1) {
//                outRect.bottom = mLastOffset
//            }
//
//        }
//    }
//
//}
