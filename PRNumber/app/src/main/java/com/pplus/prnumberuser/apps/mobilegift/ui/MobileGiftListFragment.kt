//package com.pplus.prnumberuser.apps.mobilegift.ui
//
//import android.content.Context
//import android.content.Intent
//import android.graphics.Rect
//import android.os.Bundle
//import android.view.View
//import androidx.annotation.DimenRes
//import androidx.recyclerview.widget.GridLayoutManager
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import androidx.recyclerview.widget.RecyclerView.ItemDecoration
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
//import com.pplus.prnumberuser.apps.common.builder.AlertBuilder.EVENT_ALERT
//import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
//import com.pplus.prnumberuser.apps.mobilegift.data.MobileGiftAdapter
//import com.pplus.prnumberuser.core.code.common.EnumData.MobileGiftSortType
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.MobileGift
//import com.pplus.prnumberuser.core.network.model.dto.MobileGiftCategory
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.fragment_mobile_gift_list.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.util.*
//
//class MobileGiftListFragment : BaseFragment<BaseActivity?>() {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutResourceId(): Int {
//        return R.layout.fragment_mobile_gift_list
//    }
//
//    private var mAdapter: MobileGiftAdapter? = null
//    var mLayoutManager: LinearLayoutManager? = null
//    private var mPage = 1
//    private var mTotalCount = 0
//    private var mLockListView = true
//    private var mCategory: MobileGiftCategory? = null
//    private var mSort: MobileGiftSortType? = null
//    private var mPos = 0
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        if (arguments != null) {
//            mCategory = arguments!!.getParcelable(Const.CATEGORY)
//            mPos = arguments!!.getInt(Const.POSITION)
//        }
//    }
//
//
//    override fun initializeView(container: View) {
//
//    }
//
//    override fun init() {
//        mLayoutManager = LinearLayoutManager(activity)
//        recycler_mobile_gift_list.layoutManager = mLayoutManager
////        recycler_mobile_gift_list.addItemDecoration(ItemOffsetDecoration(activity!!, R.dimen.height_16))
//        mAdapter = MobileGiftAdapter()
//        recycler_mobile_gift_list.adapter = mAdapter
//        text_mobile_gift_total_count?.text = getString(R.string.format_count_unit, "0")
////        text_mobile_gift_sort.setOnClickListener {
////            val builder = AlertBuilder.Builder()
////            builder.setContents(getString(R.string.word_sort_recent), getString(R.string.word_sort_high_price), getString(R.string.word_sort_low_price))
////            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(object : OnAlertResultListener {
////                override fun onCancel() {}
////                override fun onResult(event_alert: EVENT_ALERT) {
////                    when (event_alert.getValue()) {
////                        1 -> {
////                            text_mobile_gift_sort.text = getString(R.string.word_sort_recent)
////                            mSort = MobileGiftSortType.recent
////                        }
////                        2 -> {
////                            text_mobile_gift_sort.text = getString(R.string.word_sort_high_price)
////                            mSort = MobileGiftSortType.highPrice
////                        }
////                        3 -> {
////                            text_mobile_gift_sort.text = getString(R.string.word_sort_low_price)
////                            mSort = MobileGiftSortType.lowPrice
////                        }
////                    }
////                    getCount()
////                }
////            }).builder().show(activity)
////        }
//        recycler_mobile_gift_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            var pastVisibleItems = 0
//            var visibleItemCount = 0
//            var totalItemCount = 0
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                visibleItemCount = mLayoutManager!!.childCount
//                totalItemCount = mLayoutManager!!.itemCount
//                pastVisibleItems = mLayoutManager!!.findFirstVisibleItemPosition()
//                if (!mLockListView) {
//                    if (totalItemCount < mTotalCount && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
//                        mPage++
//                        listCall(mPage)
//                    }
//                }
//            }
//        })
//        mAdapter!!.setOnItemClickListener(object : MobileGiftAdapter.OnItemClickListener{
//            override fun onItemClick(position: Int) {
//                val intent = Intent(activity, MobileGiftDetailActivity::class.java)
//                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
//                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//                startActivity(intent)
//            }
//        })
//        mSort = MobileGiftSortType.recent
////        text_mobile_gift_sort.setText(R.string.word_sort_low_price)
//
//        getCount()
//    }
//
//    private fun getCount() {
//        val params: MutableMap<String, String> = HashMap()
//        if (mCategory!!.no != null) {
//            params["no"] = "" + mCategory!!.no
//        }
//        ApiBuilder.create().getMobileGiftCount(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {
//            override fun onResponse(call: Call<NewResultResponse<Int>>?, response: NewResultResponse<Int>?) {
//                if (!isAdded) {
//                    return
//                }
//                if (response != null) {
//                    mTotalCount = response.data
//                    text_mobile_gift_total_count?.text = PplusCommonUtil.fromHtml(getString(R.string.html_total_count2, FormatUtil.getMoneyType(mTotalCount.toString())))
//                    mPage = 1
//                    mAdapter!!.clear()
//                    listCall(mPage)
//                }
//
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Int>>?, t: Throwable?, response: NewResultResponse<Int>?) {
//
//            }
//        }).build().call()
//    }
//
//    private fun listCall(page: Int) {
//        val params: MutableMap<String, String> = HashMap()
//        if (mCategory!!.no != null) {
//            params["no"] = "" + mCategory!!.no
//        }
//        params["align"] = mSort!!.name
//        params["pg"] = "" + page
//        mLockListView = true
////        if (page != 1 || mPos == 0) {
////            showProgress("")
////        }
//        ApiBuilder.create().getMobileGiftList(params).setCallback(object : PplusCallback<NewResultResponse<MobileGift>> {
//            override fun onResponse(call: Call<NewResultResponse<MobileGift>>?, response: NewResultResponse<MobileGift>?) {
////                hideProgress()
//                if (!isAdded) {
//                    return
//                }
//                mLockListView = false
//                if (response != null) {
//                    mAdapter!!.addAll(response.datas)
//                }
//
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<MobileGift>>?, t: Throwable?, response: NewResultResponse<MobileGift>?) {
//                mLockListView = false
////                hideProgress()
//            }
//        }).build().call()
//    }
//
//    private inner class ItemOffsetDecoration(private val mItemOffset: Int, private val mRightOffset: Int) : ItemDecoration() {
//
//        constructor(context: Context, @DimenRes itemOffsetId: Int) : this(context.resources.getDimensionPixelSize(itemOffsetId), context.resources.getDimensionPixelSize(R.dimen.width_8)) {}
//
//        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
//            super.getItemOffsets(outRect, view, parent, state)
//            val position = parent.getChildAdapterPosition(view)
//            if (position < 2) {
//                outRect[mRightOffset, mItemOffset, mRightOffset] = mRightOffset
//            } else {
//                outRect[mRightOffset, mRightOffset, mRightOffset] = mRightOffset
//            }
//        }
//
//    }
//
//    companion object {
//        fun newInstance(category: MobileGiftCategory?, position: Int): MobileGiftListFragment {
//            val fragment = MobileGiftListFragment()
//            val args = Bundle()
//            args.putParcelable(Const.CATEGORY, category)
//            args.putInt(Const.POSITION, position)
//            fragment.arguments = args
//            return fragment
//        }
//    }
//}