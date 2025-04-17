//package com.pplus.prnumberuser.apps.goods.ui
//
//import android.content.Context
//import android.content.Intent
//import android.graphics.Rect
//import android.os.Bundle
//import androidx.annotation.DimenRes
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import android.view.View
//import android.widget.CompoundButton
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
//import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
//import com.pplus.prnumberuser.apps.common.builder.data.AlertData
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
//import com.pplus.prnumberuser.apps.goods.data.GoodsCartAdapter
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Cart
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.network.model.response.SubResultResponse
//import com.pplus.prnumberuser.core.util.ToastUtil
//import kotlinx.android.synthetic.main.fragment_cart.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.util.HashMap
//import kotlin.collections.ArrayList
//import kotlin.collections.set
//
//class CartFragment : BaseFragment<BaseActivity>() {
//
//    override fun getLayoutResourceId(): Int {
//        return R.layout.fragment_cart
//    }
//
//    override fun initializeView(container: View?) {
//
//    }
//
//    private var mTotalCount: Int = 0
//    private var mLockListView = false
//    private var mPaging = 1
//    private var mAdapter: GoodsCartAdapter? = null
//    private var mLayoutManager: androidx.recyclerview.widget.LinearLayoutManager? = null
//    private var mSelectList = ArrayList<Cart>()
//    private var mIsFirst = true
//
//    override fun init() {
//
//        mLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
//        recycler_cart.layoutManager = mLayoutManager
//        mAdapter = GoodsCartAdapter(activity!!)
//        recycler_cart.adapter = mAdapter
////        recycler_main_page.addItemDecoration(CustomItemOffsetDecoration(activity!!, R.dimen.height_60, R.dimen.height_60))
//
////        recycler_cart.addOnScrollListener(object : RecyclerView.OnScrollListener() {
////
////            internal var pastVisibleItems: Int = 0
////            internal var visibleItemCount: Int = 0
////            internal var totalItemCount: Int = 0
////
////            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
////
////                super.onScrolled(recyclerView, dx, dy)
////
////                visibleItemCount = mLayoutManager!!.childCount
////                totalItemCount = mLayoutManager!!.itemCount
////                pastVisibleItems = mLayoutManager!!.findFirstVisibleItemPosition()
////                if (!mLockListView) {
////                    if (totalItemCount < mTotalCount && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
////                        mPaging++
////                        listCall(mPaging)
////                    }
////                }
////            }
////        })
//
//        mAdapter!!.setOnItemClickListener(object : GoodsCartAdapter.OnItemClickListener {
//
//            override fun onItemClick(position: Int) {
//
//                val intent = Intent(activity, GoodsDetailActivity2::class.java)
//                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
//                activity!!.startActivityForResult(intent, Const.REQ_GOODS_DETAIL)
//            }
//        })
//
//        mAdapter!!.setOnItemCheckListener(object : GoodsCartAdapter.OnItemCheckListener {
//            override fun onItemCheck() {
//                val dataList = mAdapter!!.mDataList
//                var isTotal = true
//                mSelectList = ArrayList<Cart>()
//                for (i in 0 until dataList!!.size) {
//                    if (dataList[i].check) {
//                        mSelectList.add(dataList[i])
//                    } else {
//                        isTotal = false
//                    }
//                }
//
//                check_cart_total_select.setOnCheckedChangeListener(null)
//                check_cart_total_select.isChecked = isTotal
//                check_cart_total_select.setOnCheckedChangeListener(checkedListener)
//                setSelectData()
//
//            }
//        })
//
//        check_cart_total_select.isChecked = true
//        check_cart_total_select.setOnCheckedChangeListener(checkedListener)
//
//        text_cart_delete.setOnClickListener {
//            if (mSelectList.isEmpty()) {
//                showAlert(R.string.msg_select_delete_goods)
//                return@setOnClickListener
//            }
//            val builder = AlertBuilder.Builder()
//            builder.setTitle(getString(R.string.word_notice_alert))
//            builder.addContents(AlertData.MessageData(getString(R.string.msg_question_delete_select_goods), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
//            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
//            builder.setOnAlertResultListener(object : OnAlertResultListener {
//
//                override fun onCancel() {
//
//                }
//
//                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//
//                    when (event_alert) {
//                        AlertBuilder.EVENT_ALERT.RIGHT -> {
//                            val params = HashMap<String, String>()
//
//                            var no = ""
//                            for (i in 0 until mSelectList.size) {
//                                if (i != 0) {
//                                    no += ","
//                                }
//                                no += "${mSelectList[i].seqNo}"
//                            }
//                            params["seqNoList"] = no
//                            ApiBuilder.create().deleteCart(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
//                                override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
//                                    ToastUtil.show(activity, R.string.msg_deleted)
//                                    mSelectList = arrayListOf()
//                                    listCall()
//                                }
//
//                                override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
//
//                                }
//                            }).build().call()
//                        }
//                    }
//
//                }
//            }).builder().show(activity)
//
//        }
//
//        text_cart_pay.setOnClickListener {
//            if (mSelectList.isEmpty()) {
//                showAlert(R.string.msg_select_pay_goods)
//                return@setOnClickListener
//            }
//
//            val intent = Intent(activity, HotdealBuyActivity::class.java)
//            intent.putExtra(Const.KEY, Const.CART)
//            intent.putParcelableArrayListExtra(Const.CART, mSelectList)
//            activity!!.startActivityForResult(intent, Const.REQ_ORDER)
//        }
//        mIsFirst = true
//        listCall()
//    }
//
//    val checkedListener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
//        mAdapter!!.setAllCheck(isChecked)
//    }
//
//    private fun setSelectData() {
//        var price = 0L
//        for (cart in mSelectList) {
//            price += (cart.goods!!.price!! * cart.count!!)
//        }
//
//        text_cart_status.text = getString(R.string.format_cart_status, mSelectList.size.toString(), FormatUtil.getMoneyType(price.toString()))
//    }
//
//    private fun listCall() {
//
//        mLockListView = true
//        val params = HashMap<String, String>()
//
//        params["memberSeqNo"] = LoginInfoManager.getInstance().user.no.toString()
//        params["page"] = "0"
//        params["size"] = "100"
//        params["sort"] = "${EnumData.BuyGoodsSort.seqNo.name},${EnumData.BuyGoodsSort.desc.name}"
//
//        showProgress("")
//        ApiBuilder.create().getCart(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<Cart>>> {
//            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<Cart>>>?, response: NewResultResponse<SubResultResponse<Cart>>?) {
//                hideProgress()
//
//                if (response != null) {
//
//                    if (response.data.first!!) {
//                        mTotalCount = response.data.totalElements!!
//                        mAdapter!!.clear()
//                    }
//
//                    mLockListView = false
//
//                    val dataList = response.data.content!!
//
//                    if(dataList.isNotEmpty()){
//                        layout_cart_not_exist.visibility = View.GONE
//                    }else{
//                        layout_cart_not_exist.visibility = View.VISIBLE
//                    }
//
//                    if(mIsFirst){
//                        mSelectList = ArrayList<Cart>()
//                        for (i in 0 until dataList.size) {
//                            dataList[i].check = true
//                            mSelectList.add(dataList[i])
//                        }
//                        mIsFirst = false
//                    }
//
//                    setSelectData()
//                    mAdapter!!.addAll(dataList)
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<Cart>>>?, t: Throwable?, response: NewResultResponse<SubResultResponse<Cart>>?) {
//                hideProgress()
//                mLockListView = false
//            }
//        }).build().call()
//    }
//
//    private inner class CustomItemOffsetDecoration(private val mItemOffset: Int, private val mTopOffset: Int) : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {
//
//        constructor(context: Context, @DimenRes itemOffsetId: Int, @DimenRes topOffsetId: Int) : this(context.resources.getDimensionPixelSize(itemOffsetId), context.resources.getDimensionPixelSize(topOffsetId)) {}
//
//        override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
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
//
//        }
//    }
//
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            //            param1 = it.getString(ARG_PARAM1)
////            param2 = it.getString(ARG_PARAM2)
//        }
//    }
//
//    companion object {
//
//        @JvmStatic
//        fun newInstance() =
//                CartFragment().apply {
//                    arguments = Bundle().apply {
//                        //                        putString(ARG_PARAM1, param1)
////                        putString(ARG_PARAM2, param2)
//                    }
//                }
//    }
//}
