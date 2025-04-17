//package com.pplus.prnumberuser.apps.main.ui
//
//
//import android.content.Context
//import android.content.Intent
//import android.graphics.Rect
//import android.os.Bundle
//import android.view.View
//import androidx.annotation.DimenRes
//import com.pplus.networks.common.PplusCallback
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
//import com.pplus.prnumberuser.apps.main.data.FriendAdapter
//import com.pplus.prnumberuser.apps.recommend.ui.InviteActivity
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Friend
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.logs.LogUtil
//import kotlinx.android.synthetic.main.fragment_main_friend.*
//import retrofit2.Call
//import java.util.*
//
//class MainFriendFragment : BaseFragment<BaseActivity>() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            //            param1 = it.getString(ARG_PARAM1)
////            param2 = it.getString(ARG_PARAM2)
//        }
//    }
//
//    override fun getLayoutResourceId(): Int {
//        return R.layout.fragment_main_friend
//    }
//
//    override fun initializeView(container: View?) {
//
//    }
//
//    private var mAdapter: FriendAdapter? = null
//    private var mPaging: Int = 0
//    private var mTotalCount = 0
//    private var mLayoutManager: androidx.recyclerview.widget.LinearLayoutManager? = null
//    private var mLockListView = true
//
//    override fun init() {
//
//        mLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity!!, androidx.recyclerview.widget.OrientationHelper.VERTICAL, false)
//        recycler_main_friend.layoutManager = mLayoutManager
//        mAdapter = FriendAdapter(activity!!)
//        recycler_main_friend.adapter = mAdapter
////        recycler_main_friend.addItemDecoration(CustomItemOffsetDecoration(activity!!, R.dimen.height_60, R.dimen.height_60))
//        recycler_main_friend.addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
//
//            internal var pastVisibleItems: Int = 0
//            internal var visibleItemCount: Int = 0
//            internal var totalItemCount: Int = 0
//
//            override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
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
////        text_main_friend_start_seller.setOnClickListener {
////            val intent = Intent(Intent.ACTION_VIEW)
////            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
////            intent.data = Uri.parse("https://web.prnumber.com/seller/index.html")
////            startActivity(intent)
////        }
//
//        text_main_friend_invite.setOnClickListener {
//            val intent = Intent(activity, InviteActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
//        getCount()
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
//        }
//    }
//
//    private fun getCount() {
//
//        ApiBuilder.create().friendCount.setCallback(object : PplusCallback<NewResultResponse<Int>> {
//
//            override fun onResponse(call: Call<NewResultResponse<Int>>, response: NewResultResponse<Int>) {
//                if (!isAdded) {
//                    return
//                }
//                mTotalCount = response.data
//
//                text_main_friend_count.text = PplusCommonUtil.fromHtml(getString(R.string.html_friend_count, FormatUtil.getMoneyType(mTotalCount.toString())))
//
//                if (mTotalCount == 0) {
//                    layout_main_friend_not_exist.visibility = View.VISIBLE
//                } else {
//                    layout_main_friend_not_exist.visibility = View.GONE
//                }
//
//                mPaging = 1
//                listCall(mPaging)
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Int>>, t: Throwable, response: NewResultResponse<Int>) {
//
//            }
//        }).build().call()
//    }
//
//    private fun listCall(page: Int) {
//
//        val params = HashMap<String, String>()
//        params["pg"] = "" + page
////        showProgress("")
//        mLockListView = true
//        ApiBuilder.create().getFriendList(params).setCallback(object : PplusCallback<NewResultResponse<Friend>> {
//
//            override fun onResponse(call: Call<NewResultResponse<Friend>>, response: NewResultResponse<Friend>) {
////                hideProgress()
//                if (!isAdded) {
//                    return
//                }
//
//                mLockListView = false
//                LogUtil.e(LOG_TAG, "count = {}", response.datas.size)
//                mAdapter!!.addAll(response.datas)
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Friend>>, t: Throwable, response: NewResultResponse<Friend>) {
////                hideProgress()
//                mLockListView = false
//            }
//        }).build().call()
//    }
//
//    override fun getPID(): String {
//        return ""
//    }
//
//    companion object {
//
//        @JvmStatic
//        fun newInstance() =
//                MainFriendFragment().apply {
//                    arguments = Bundle().apply {
//                        //                        putString(ARG_PARAM1, param1)
////                        putString(ARG_PARAM2, param2)
//                    }
//                }
//    }
//}
