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
//import com.pplus.utils.part.logs.LogUtil
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
//import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
//import com.pplus.prnumberuser.apps.common.builder.data.AlertData
//import com.pplus.prnumberuser.apps.common.mgmt.SelectedGoodsManager
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
//import com.pplus.prnumberuser.apps.goods.data.PageCartAdapter
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.BuyGoods
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.prnumberuser.core.util.ToastUtil
//import kotlinx.android.synthetic.main.fragment_cart.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//
//class PageCartFragment : BaseFragment<BaseActivity>() {
//
//    override fun getLayoutResourceId(): Int {
//        return R.layout.fragment_cart
//    }
//
//    override fun initializeView(container: View?) {
//
//    }
//
//    private var mAdapter: PageCartAdapter? = null
//    private var mLayoutManager: androidx.recyclerview.widget.LinearLayoutManager? = null
//    private var mSelectList = ArrayList<BuyGoods>()
//    private var mIsFirst = true
//
//    override fun init() {
//
//        mLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
//        recycler_cart.layoutManager = mLayoutManager
//        mAdapter = PageCartAdapter(activity!!, mPageSeqNo)
//        recycler_cart.adapter = mAdapter
//
//        mAdapter!!.setOnItemClickListener(object : PageCartAdapter.OnItemClickListener {
//
//            override fun onItemClick(position: Int) {
//
//                val intent = Intent(activity, GoodsDetailActivity2::class.java)
//                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
//                activity!!.startActivityForResult(intent, Const.REQ_GOODS_DETAIL)
//            }
//        })
//
//        mAdapter!!.setOnItemCheckListener(object : PageCartAdapter.OnItemCheckListener {
//            override fun onItemCheck() {
//                val dataList = mAdapter!!.mDataList
//                var isTotal = true
//                mSelectList = ArrayList<BuyGoods>()
//                for (i in 0 until dataList!!.size) {
//                    if (dataList[i].check!!) {
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
//                            val dataList = mAdapter!!.mDataList
//                            for (i in 0 until mSelectList.size) {
//                                for (j in 0 until dataList!!.size) {
//                                    if(dataList[j].goods!!.seqNo == mSelectList[i].goods!!.seqNo){
//                                        dataList.removeAt(j)
//                                        break
//                                    }
//                                }
//                            }
//                            SelectedGoodsManager.getInstance(mPageSeqNo).load().buyGoodsList = dataList
//                            SelectedGoodsManager.getInstance(mPageSeqNo).save()
//                            ToastUtil.show(activity, R.string.msg_deleted)
//                            mSelectList = arrayListOf()
//                            listCall()
//                        }
//                    }
//
//                }
//            }).builder().show(activity)
//
//        }
//
//        text_cart_pay.setOnClickListener {
//
//            if (!PplusCommonUtil.loginCheck(activity!!)) {
//                return@setOnClickListener
//            }
//
//            if (mSelectList.isEmpty()) {
//                showAlert(R.string.msg_select_pay_goods)
//                return@setOnClickListener
//            }
//
//            val intent = Intent(activity, GoodsBuyActivity::class.java)
//            intent.putParcelableArrayListExtra(Const.BUY_GOODS, mSelectList)
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
//        var dataList = SelectedGoodsManager.getInstance(mPageSeqNo).load().buyGoodsList
//
//        if(dataList == null){
//            dataList = arrayListOf()
//        }
//
//        if (dataList.isNotEmpty()) {
//            layout_cart_not_exist.visibility = View.GONE
//        } else {
//            layout_cart_not_exist.visibility = View.VISIBLE
//        }
//
//        if (mIsFirst) {
//            mSelectList = ArrayList<BuyGoods>()
//            for (i in 0 until dataList.size) {
//                dataList[i].check = true
//                mSelectList.add(dataList[i])
//            }
//            mIsFirst = false
//        }else{
//            for (i in 0 until dataList.size) {
//                dataList[i].check = false
//            }
//
//            for (i in 0 until dataList.size) {
//                for (j in 0 until mSelectList.size) {
//                    if(dataList[i].goods!!.seqNo == mSelectList[j].goods!!.seqNo){
//                        dataList[i].check = true
//                        break
//                    }
//                }
//            }
//        }
//
//        setSelectData()
//        mAdapter!!.setDataList(dataList)
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
//            Const.REQ_ORDER->{
//                listCall()
//            }
//        }
//    }
//
//    override fun getPID(): String {
//        return ""
//    }
//
//    var mPageSeqNo = 0L
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            mPageSeqNo = it.getLong(Const.PAGE_SEQ_NO)
////            param2 = it.getString(ARG_PARAM2)
//        }
//    }
//
//    companion object {
//
//        @JvmStatic
//        fun newInstance(seqNo: Long) =
//                PageCartFragment().apply {
//                    arguments = Bundle().apply {
//                        putLong(Const.PAGE_SEQ_NO, seqNo)
////                        putString(ARG_PARAM2, param2)
//                    }
//                }
//    }
//}
