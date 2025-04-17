//package com.pplus.prnumberuser.apps.goods.ui
//
//import android.app.Activity
//import android.content.Intent
//import android.os.Bundle
//import android.view.View
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
//import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.goods.data.GoodsReviewAdapter
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Goods
//import com.pplus.prnumberuser.core.network.model.dto.GoodsPrice
//import com.pplus.prnumberuser.core.network.model.dto.GoodsReview
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.network.model.response.SubResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.activity_goods_review.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.util.*
//
//class GoodsReviewActivity : BaseActivity(), ImplToolbar {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutView(): Int {
//        return R.layout.activity_goods_review
//    }
//
//    private var mAdapter: GoodsReviewAdapter? = null
//    var mGoodsPrice: GoodsPrice? = null
//    var mGoods: Goods? = null
//    private var mPaging: Int = 0
//    private var mTotalCount = 0
//    private var mLayoutManager: LinearLayoutManager? = null
//    private var mLockListView = true
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//        mGoods = intent.getParcelableExtra(Const.GOODS)
//        mGoodsPrice = intent.getParcelableExtra(Const.GOODS_PRICE)
//
////        if (mGoods!!.avgEval != null) {
////            val avgEval = String.format("%.1f", mGoods!!.avgEval)
////            text_goods_review_grade.text = avgEval
////            grade_bar_goods_review_total.build(avgEval)
////        } else {
////            text_goods_review_grade.text = "0.0"
////            grade_bar_goods_review_total.build("0.0")
////        }
//
//        mLayoutManager = LinearLayoutManager(this)
//        recycler_goods_review.layoutManager = mLayoutManager
//        mAdapter = GoodsReviewAdapter(this)
//        recycler_goods_review.adapter = mAdapter
//
//        recycler_goods_review.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
//                    if (totalItemCount < mTotalCount && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
//                        mPaging++
//                        listCall(mPaging)
//                    }
//                }
//            }
//        })
//
//        mAdapter!!.setOnItemClickListener(object : GoodsReviewAdapter.OnItemClickListener{
//            override fun onItemClick(position: Int) {
//
//                val goodsReview = mAdapter!!.getItem(position)
//
//                if(goodsReview.member!!.seqNo == LoginInfoManager.getInstance().user.no){
//                    val contents = arrayOf(getString(R.string.word_modified), getString(R.string.word_delete))
//                    val builder = AlertBuilder.Builder()
//                    builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER)
//                    builder.setContents(*contents)
//                    builder.setLeftText(getString(R.string.word_cancel))
//                    builder.setOnAlertResultListener(object : OnAlertResultListener {
//
//                        override fun onCancel() {
//
//                        }
//
//                        override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//                            when(event_alert){
//                                AlertBuilder.EVENT_ALERT.LIST->{
//                                    when (event_alert.getValue()) {
//                                        1 -> {
//                                            val intent = Intent(this@GoodsReviewActivity, GoodsReviewWriteActivity::class.java)
//                                            intent.putExtra(Const.MODE, EnumData.MODE.UPDATE)
//                                            intent.putExtra(Const.DATA, goodsReview)
//                                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                                            startActivityForResult(intent, Const.REQ_MODIFY)
//                                        }
//                                        2 -> {
//                                            val params = HashMap<String, String>()
//                                            params["seqNo"] = goodsReview.seqNo.toString()
//
//                                            showProgress("")
//                                            ApiBuilder.create().deleteGoodsReview(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
//                                                override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
//                                                    hideProgress()
//                                                    showAlert(R.string.msg_deleted)
//                                                    mPaging = 0
//                                                    listCall(mPaging)
//                                                }
//
//                                                override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
//                                                    hideProgress()
//                                                }
//                                            }).build().call()
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }).builder().show(this@GoodsReviewActivity)
//
//                }
//
//
//            }
//        })
//
//        mPaging = 0
//        listCall(mPaging)
//    }
//
//    private fun listCall(page: Int) {
//
//        mLockListView = true
//        val params = HashMap<String, String>()
//
//        if(mGoods != null){
//            params["goodsSeqNo"] = mGoods!!.seqNo.toString()
//        }else{
//            params["goodsPriceSeqNo"] = mGoodsPrice!!.seqNo.toString()
//        }
//
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
//                        mAdapter!!.clear()
//                        text_goods_review_count.text = PplusCommonUtil.fromHtml(getString(R.string.html_review, FormatUtil.getMoneyType(mTotalCount.toString())))
//                        if (mTotalCount == 0) {
//                            layout_goods_review_not_exist.visibility = View.VISIBLE
//                        } else {
//                            layout_goods_review_not_exist.visibility = View.GONE
//                        }
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
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            Const.REQ_MODIFY -> {
//                if(resultCode == Activity.RESULT_OK){
//
//                    mPaging = 0
//                    listCall(mPaging)
//
////                    val params = HashMap<String, String>()
////                    params["seqNo"] = mGoodsPrice!!.goods!!.seqNo.toString()
////                    ApiBuilder.create().getOneGoods(params).setCallback(object : PplusCallback<NewResultResponse<Goods>> {
////                        override fun onResponse(call: Call<NewResultResponse<Goods>>?, response: NewResultResponse<Goods>?) {
////                            hideProgress()
////                            if (response != null && response.data != null) {
////                                mGoods = response.data
////
//////                                if (mGoods!!.avgEval != null) {
//////                                    val avgEval = String.format("%.1f", mGoods!!.avgEval)
//////                                    text_goods_review_grade.text = avgEval
//////                                    grade_bar_goods_review_total.build(avgEval)
//////                                } else {
//////                                    text_goods_review_grade.text = "0.0"
//////                                    grade_bar_goods_review_total.build("0.0")
//////                                }
////                            }
////
////
////                        }
////
////                        override fun onFailure(call: Call<NewResultResponse<Goods>>?, t: Throwable?, response: NewResultResponse<Goods>?) {
////                            hideProgress()
////                        }
////                    }).build().call()
//                }
//            }
//        }
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_goods_review), ToolbarOption.ToolbarMenu.LEFT)
////        val textRightTop = TextView(ContextThemeWrapper(this, R.style.buttonStyle))
////        textRightTop.setText(R.string.word_review_write)
////        textRightTop.isClickable = true
////        textRightTop.gravity = Gravity.CENTER
////        textRightTop.setPadding(0, 0, resources.getDimensionPixelSize(R.dimen.width_66), 0)
////        textRightTop.setTextColor(ResourceUtil.getColorStateList(this, R.color.color_579ffb))
////        textRightTop.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(R.dimen.textSize_45pt).toFloat())
////        textRightTop.setSingleLine()
////        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, textRightTop, 0)
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
//                ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
////                    val intent = Intent(this, GoodsReviewWriteActivity::class.java)
////                    intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
////                    intent.putExtra(Const.GOODS, mGoods)
////                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
////                    startActivityForResult(intent, Const.REQ_REVIEW)
//                }
//            }
//        }
//    }
//}
