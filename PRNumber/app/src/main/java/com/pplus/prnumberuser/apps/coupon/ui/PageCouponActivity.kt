//package com.pplus.prnumberuser.apps.coupon.ui
//
//import android.content.Intent
//import android.os.Bundle
//import android.view.View
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.main.data.CouponAdapter
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Goods
//import com.pplus.prnumberuser.core.network.model.dto.GoodsLike
//import com.pplus.prnumberuser.core.network.model.dto.Page
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.network.model.response.SubResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.prnumberuser.core.util.ToastUtil
//import kotlinx.android.synthetic.main.activity_page_coupon.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.util.*
//
//class PageCouponActivity : BaseActivity(), ImplToolbar {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_page_coupon
//    }
//
//    private var mTotalCount: Int = 0
//    private var mLockListView = false
//    private var mPaging = 1
//    private var mAdapter: CouponAdapter? = null
//    private var mLayoutManager: LinearLayoutManager? = null
//    private var mIsLast = false
//    private var mPage: Page? = null
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//        mPage = intent.getParcelableExtra(Const.PAGE)
//
//        mLayoutManager = LinearLayoutManager(this)
//        recycler_page_coupon.layoutManager = mLayoutManager!!
//        mAdapter = CouponAdapter()
//        recycler_page_coupon.adapter = mAdapter
//
//        recycler_page_coupon.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//
//            internal var pastVisibleItems: Int = 0
//            internal var visibleItemCount: Int = 0
//            internal var totalItemCount: Int = 0
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//
//                super.onScrolled(recyclerView, dx, dy)
//
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
//        mAdapter!!.setOnItemClickListener(object : CouponAdapter.OnItemClickListener {
//            override fun onItemClick(position: Int, view: View) {
//                val intent = Intent(this@PageCouponActivity, CouponDetailActivity::class.java)
//                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
//                startActivityForResult(intent, Const.REQ_DETAIL)
//            }
//
//            override fun onCheckDownload(position: Int) {
//                if (!PplusCommonUtil.loginCheck(this@PageCouponActivity)) {
//                    return
//                }
//
//                checkLike(mAdapter!!.getItem(position))
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
//        params["expired"] = "false" //null-All, true-유효기간지난것, false-유효기간남은것
//        params["status"] = EnumData.GoodsStatus.ing.status.toString()
//
//        params["pageSeqNo"] = mPage!!.no.toString()
//        params["isPlus"] = "false"
//        params["isHotdeal"] = "false"
//        params["isCoupon"] = "true"
//        params["sort"] = "${EnumData.GoodsSort.seqNo.name},${EnumData.GoodsSort.desc.name}"
//        params["page"] = page.toString()
//        showProgress("")
//        ApiBuilder.create().getGoods(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<Goods>>> {
//
//            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<Goods>>>?, response: NewResultResponse<SubResultResponse<Goods>>?) {
//                hideProgress()
//                if (response != null) {
//
//                    mIsLast = response.data.last!!
//                    if (response.data.first!!) {
//                        mTotalCount = response.data.totalElements!!
//                        mAdapter!!.clear()
//
//                        text_page_coupon_count.text = PplusCommonUtil.fromHtml(getString(R.string.html_total_count2, FormatUtil.getMoneyType(mTotalCount.toString())))
//                        if(mTotalCount > 0){
//                            layout_page_coupon_not_exist.visibility = View.GONE
//                        }else{
//                            layout_page_coupon_not_exist.visibility = View.VISIBLE
//                        }
//
//                    }
//
//                    mLockListView = false
//                    mAdapter!!.addAll(response.data.content!!)
//                }
//
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<Goods>>>?, t: Throwable?, response: NewResultResponse<SubResultResponse<Goods>>?) {
//                hideProgress()
//                mLockListView = false
//            }
//        }).build().call()
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
//                        ToastUtil.show(this@PageCouponActivity, R.string.msg_already_download_coupon)
//                    }else{
//                        postLike(goods)
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
//                val intent = Intent(this@PageCouponActivity, AlertCouponDownloadCompleteActivity::class.java)
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
//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_coupon), ToolbarOption.ToolbarMenu.LEFT)
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
