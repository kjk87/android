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
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
//import com.pplus.prnumberuser.apps.main.data.MainHotDealAdapter
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Goods
//import com.pplus.prnumberuser.core.network.model.dto.Page
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.network.model.response.SubResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.activity_event_goods.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.util.*
//
//class PageGoodsFragment : BaseFragment<BaseActivity>() {
//
//    override fun getLayoutResourceId(): Int {
//        return R.layout.activity_event_goods
//    }
//
//    override fun initializeView(container: View?) {
//
//    }
//
//    private var mTotalCount: Int = 0
//    private var mLockListView = false
//    private var mPaging = 1
//    private var mAdapter: MainHotDealAdapter? = null
//    private var mLayoutManager: LinearLayoutManager? = null
//    private var mIsLast = false
//    private var mPage: Page? = null
//
//    override fun init() {
//
//        mLayoutManager = LinearLayoutManager(activity)
//        recycler_event_goods.layoutManager = mLayoutManager!!
//        mAdapter = MainHotDealAdapter()
//        recycler_event_goods.adapter = mAdapter
//
//        recycler_event_goods.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
//        mAdapter!!.setOnItemClickListener(object : MainHotDealAdapter.OnItemClickListener {
//            override fun onItemClick(position: Int, view: View) {
////                val intent = Intent(activity, GoodsDetailActivity2::class.java)
////                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
////                startActivityForResult(intent, Const.REQ_GOODS_DETAIL)
//
//                val intent = Intent(activity, GoodsDetailShipTypeActivity::class.java)
//                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
//                activity?.startActivityForResult(intent, Const.REQ_GOODS_DETAIL)
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
//        params["pageSeqNo"] = mPage!!.no.toString()
//        params["isPlus"] = "true"
//        params["sort"] = "${EnumData.GoodsSort.seqNo.name},${EnumData.BuyGoodsSort.desc.name}"
//        params["page"] = page.toString()
//        showProgress("")
//        ApiBuilder.create().getGoodsShipType(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<Goods>>> {
//
//            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<Goods>>>?, response: NewResultResponse<SubResultResponse<Goods>>?) {
//                hideProgress()
//                if(!isAdded){
//                    return
//                }
//                if (response != null) {
//
//                    mIsLast = response.data.last!!
//                    if (response.data.first!!) {
//                        mTotalCount = response.data.totalElements!!
//                        mAdapter!!.clear()
//
//                        text_event_goods_count?.text = PplusCommonUtil.fromHtml(getString(R.string.html_total_count2, FormatUtil.getMoneyType(mTotalCount.toString())))
//                        if (mTotalCount == 0) {
//                            layout_event_goods_not_exist?.visibility = View.VISIBLE
//                        } else {
//                            layout_event_goods_not_exist?.visibility = View.GONE
//                        }
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
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            Const.REQ_GOODS_DETAIL -> {
//                if (resultCode == Activity.RESULT_OK) {
//                    mPaging = 0
//                    listCall(mPaging)
//                }
//            }
//        }
//    }
//
//    override fun getPID(): String {
//        return "page product"
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            mPage = it.getParcelable(Const.PAGE)
//            //            param1 = it.getString(ARG_PARAM1)
////            param2 = it.getString(ARG_PARAM2)
//        }
//    }
//
//    companion object {
//
//        @JvmStatic
//        fun newInstance(page: Page) =
//                PageGoodsFragment().apply {
//                    arguments = Bundle().apply {
//                        putParcelable(Const.PAGE, page)
////                        putString(ARG_PARAM2, param2)
//                    }
//                }
//    }
//}
