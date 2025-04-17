//package com.pplus.luckybol.apps.my.ui
//
//import android.app.Activity
//import android.content.Intent
//import android.os.Bundle
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import android.view.View
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.luckybol.Const
//import com.pplus.luckybol.R
//import com.pplus.luckybol.apps.common.builder.AlertBuilder
//import com.pplus.luckybol.apps.common.builder.OnAlertResultListener
//import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
//import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
//import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
//import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
//import com.pplus.luckybol.apps.common.ui.base.BaseActivity
//import com.pplus.luckybol.apps.goods.data.MyGoodsReviewAdapter
//import com.pplus.luckybol.apps.goods.ui.GoodsReviewWriteActivity
//import com.pplus.luckybol.core.code.common.EnumData
//import com.pplus.luckybol.core.network.ApiBuilder
//import com.pplus.luckybol.core.network.model.dto.GoodsReview
//import com.pplus.luckybol.core.network.model.response.NewResultResponse
//import com.pplus.luckybol.core.network.model.response.SubResultResponse
//import com.pplus.luckybol.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.activity_my_review.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.util.*
//
//class MyReviewActivity : BaseActivity(), ImplToolbar {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.fragment_my_review
//    }
//
//    var mAdapter: MyGoodsReviewAdapter? = null
//    private var mPaging: Int = 0
//    private var mTotalCount = 0
//    private var mLayoutManager: LinearLayoutManager? = null
//    private var mLockListView = true
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//        mLayoutManager = LinearLayoutManager(this)
//        recycler_my_review.layoutManager = mLayoutManager!!
//        mAdapter = MyGoodsReviewAdapter(this)
//        recycler_my_review.adapter = mAdapter
//
//        recycler_my_review.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
//        mAdapter!!.setOnItemClickListener(object : MyGoodsReviewAdapter.OnItemClickListener{
//            override fun onItemClick(position: Int) {
//
//                val goodsReview = mAdapter!!.getItem(position)
//
//                if(goodsReview.member!!.seqNo == LoginInfoManager.getInstance().user.no){
//                    val builder = AlertBuilder.Builder()
//                    builder.setContents(getString(R.string.word_modified), getString(R.string.word_delete))
//                    builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(object : OnAlertResultListener {
//
//                        override fun onCancel() {
//
//                        }
//
//                        override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//                            when (event_alert.getValue()) {
//                                1 -> {
//                                    val intent = Intent(this@MyReviewActivity, GoodsReviewWriteActivity::class.java)
//                                    intent.putExtra(Const.MODE, EnumData.MODE.UPDATE)
//                                    intent.putExtra(Const.DATA, goodsReview)
//                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                                    startActivityForResult(intent, Const.REQ_MODIFY)
//                                }
//
//                                2 -> {
//                                    val params = HashMap<String, String>()
//                                    params["seqNo"] = goodsReview.seqNo.toString()
//
//                                    showProgress("")
//                                    ApiBuilder.create().deleteGoodsReview(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
//                                        override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
//                                            hideProgress()
//                                            showAlert(R.string.msg_deleted)
//                                            mPaging = 0
//                                            listCall(mPaging)
//                                        }
//
//                                        override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
//                                            hideProgress()
//                                        }
//                                    }).build().call()
//                                }
//                            }
//                        }
//                    }).builder().show(this@MyReviewActivity)
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
//        params["memberSeqNo"] = LoginInfoManager.getInstance().user.no.toString()
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
//                        text_my_review_total_count.text = PplusCommonUtil.fromHtml(getString(R.string.html_my_review, FormatUtil.getMoneyType(mTotalCount.toString())))
//                        if (mTotalCount == 0) {
//                            layout_my_review_not_exist.visibility = View.VISIBLE
//                        } else {
//                            layout_my_review_not_exist.visibility = View.GONE
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
//            Const.REQ_DETAIL, Const.REQ_MODIFY -> {
//                if(resultCode == Activity.RESULT_OK){
//                    mPaging = 0
//                    listCall(mPaging)
//                }
//
//            }
//        }
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_my_review), ToolbarOption.ToolbarMenu.LEFT)
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
