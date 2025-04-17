//package com.pplus.prnumberuser.apps.goods.ui
//
//import android.app.Activity
//import android.content.Context
//import android.content.Intent
//import android.graphics.Rect
//import android.os.Bundle
//import androidx.annotation.DimenRes
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import android.view.View
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
//import com.pplus.prnumberuser.apps.goods.data.BuyHistoryAdapter
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Buy
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.network.model.response.SubResultResponse
//import kotlinx.android.synthetic.main.fragment_buy_history.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.util.*
//
//class BuyHistoryFragment : BaseFragment<BaseActivity>() {
//
//    override fun getLayoutResourceId(): Int {
//        return R.layout.fragment_buy_history
//    }
//
//    override fun initializeView(container: View?) {
//
//    }
//
//    private var mTotalCount: Int = 0
//    private var mLockListView = false
//    private var mPaging = 0
//    private var mAdapter: BuyHistoryAdapter? = null
//    private var mLayoutManager: LinearLayoutManager? = null
//    private var mIsLast = false
//
//    override fun init() {
//
//        mLayoutManager = LinearLayoutManager(activity)
//        recycler_buy_history.layoutManager = mLayoutManager
//        mAdapter = BuyHistoryAdapter(activity!!)
//        recycler_buy_history.adapter = mAdapter
////        recycler_main_page.addItemDecoration(CustomItemOffsetDecoration(activity!!, R.dimen.height_60, R.dimen.height_60))
//
//        recycler_buy_history.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
//        mAdapter!!.setOnItemClickListener(object : BuyHistoryAdapter.OnItemClickListener {
//
//            override fun onItemClick(position: Int) {
//
//                val buy = mAdapter!!.getItem(position)
//                if(buy.buyGoodsList!![0].goods!!.type == "0"){
//                    when (buy.buyGoodsList!![0].process) {
//                        EnumData.BuyGoodsProcess.USE.process -> {
//                            val intent = Intent(activity, OrderHistoryDetailActivity::class.java)
//                            intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
//                            activity!!.startActivityForResult(intent, Const.REQ_GOODS_DETAIL)
//                        }
//                        else->{
//                            val intent = Intent(activity, BuyDetailActivity::class.java)
//                            intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
//                            activity!!.startActivityForResult(intent, Const.REQ_GOODS_DETAIL)
//                        }
//                    }
//                }else{
//                    val intent = Intent(activity, BuyDetailActivity::class.java)
//                    intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
//                    activity!!.startActivityForResult(intent, Const.REQ_GOODS_DETAIL)
//                }
//
//
//            }
//        })
//
//        text_buy_history_not_exist.setText(R.string.msg_not_exist_buy_goods)
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
//        params["page"] = page.toString()
////        params["isHotdeal"] = "false"
////        params["isPlus"] = "false"
//        params["orderType"] = "0"
////        params["payType"] = "online"
//
//        params["sort"] = "${EnumData.BuyGoodsSort.seqNo.name},${EnumData.BuyGoodsSort.desc.name}"
//        showProgress("")
//        ApiBuilder.create().getBuy(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<Buy>>> {
//            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<Buy>>>?, response: NewResultResponse<SubResultResponse<Buy>>?) {
//                hideProgress()
//
//                if(!isAdded){
//                    return
//                }
//
//                if (response != null) {
//
//                    mIsLast = response.data.last!!
//
//                    if (response.data.first!!) {
//                        mTotalCount = response.data.totalElements!!
//                        mAdapter!!.clear()
//                        if (mTotalCount > 0) {
//                            layout_buy_history_not_exist.visibility = View.GONE
//                        } else {
//                            layout_buy_history_not_exist.visibility = View.VISIBLE
//                        }
//                    }
//
//                    mLockListView = false
//
//                    val dataList = response.data.content!!
//                    mAdapter!!.addAll(dataList)
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<Buy>>>?, t: Throwable?, response: NewResultResponse<SubResultResponse<Buy>>?) {
//                hideProgress()
//                if(!isAdded){
//                    return
//                }
//                mLockListView = false
//            }
//        }).build().call()
//    }
//
//    private inner class CustomItemOffsetDecoration(private val mItemOffset: Int, private val mTopOffset: Int) : RecyclerView.ItemDecoration() {
//
//        constructor(context: Context, @DimenRes itemOffsetId: Int, @DimenRes topOffsetId: Int) : this(context.resources.getDimensionPixelSize(itemOffsetId), context.resources.getDimensionPixelSize(topOffsetId)) {}
//
//        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
//
//            super.getItemOffsets(outRect, view, parent, state)
//
//            val position = parent.getChildAdapterPosition(view)
//            if (position == 0) {
//                outRect.set(0, mTopOffset, 0, mItemOffset)
//            } else {
//                outRect.set(0, 0, 0, mItemOffset)
//            }
//
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            Const.REQ_BUY_CANCEL, Const.REQ_REVIEW -> {
//                if (resultCode == Activity.RESULT_OK) {
//                    mPaging = 0
//                    listCall(mPaging)
//                }
//
//            }
//            Const.REQ_GOODS_DETAIL ->{
//                mPaging = 0
//                listCall(mPaging)
//            }
//
//            Const.REQ_USE->{
//                if(resultCode == Activity.RESULT_OK && data != null){
//                    val buy = data.getParcelableExtra<Buy>(Const.DATA)
//                    val pos = data.getIntExtra(Const.POSITION, 0)
//                    use(buy.seqNo!!, pos)
//                }
//            }
//        }
//    }
//
//    private fun use(buySeqNo:Long, pos:Int){
//        val params = HashMap<String, String>()
//        params["buySeqNo"] = buySeqNo.toString()
//        showProgress("")
//        ApiBuilder.create().buyGoodsListUse(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
//            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
//                hideProgress()
//                showAlert(R.string.msg_use_complete)
//                mAdapter!!.getBuy(buySeqNo, pos)
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
////            mTab = it.getInt(Const.TAB)
////            param2 = it.getString(ARG_PARAM2)
//        }
//    }
//
//    companion object {
//
//        @JvmStatic
//        fun newInstance() =
//                BuyHistoryFragment().apply {
//                    arguments = Bundle().apply {
////                        putInt(Const.TAB, tab)
////                        putString(ARG_PARAM2, param2)
//                    }
//                }
//    }
//}
