//package com.pplus.prnumberuser.apps.main.ui
//
//import android.content.Intent
//import android.os.Bundle
//import android.view.View
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.pplus.networks.common.PplusCallback
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.main.data.UserFriendAdapter
//import com.pplus.prnumberuser.apps.recommend.ui.InviteActivity
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Friend
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.utils.part.format.FormatUtil
//import kotlinx.android.synthetic.main.activity_user_friend.*
//import retrofit2.Call
//import java.util.*
//
//class UserFriendActivity : BaseActivity(), ImplToolbar {
//    override fun getPID(): String {
//        return "Main_mypage_friend"
//    }
//
//    override fun getLayoutView(): Int {
//        return R.layout.activity_user_friend
//    }
//
//    private var mAdapter: UserFriendAdapter? = null
//    private var mPaging: Int = 0
//    private var mTotalCount = 0
//    private var mLayoutManager: androidx.recyclerview.widget.LinearLayoutManager? = null
//    private var mLockListView = true
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//        text_user_friend_invite.setOnClickListener {
//            val intent = Intent(this, InviteActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
//        mLayoutManager = LinearLayoutManager(this)
//        recycler_user_friend.layoutManager = mLayoutManager
//        mAdapter = UserFriendAdapter(this)
//        recycler_user_friend.adapter = mAdapter
//        recycler_user_friend.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
//        getCount()
//    }
//
//    private fun getCount() {
//
//        ApiBuilder.create().userFriendCount.setCallback(object : PplusCallback<NewResultResponse<Int>> {
//
//            override fun onResponse(call: Call<NewResultResponse<Int>>, response: NewResultResponse<Int>) {
//
//                mTotalCount = response.data
//
//                text_user_friend_count.text = PplusCommonUtil.fromHtml(getString(R.string.html_friend_count, FormatUtil.getMoneyType(mTotalCount.toString())))
//
//                if (mTotalCount == 0) {
//                    layout_user_friend_not_exist.visibility = View.VISIBLE
//                } else {
//                    layout_user_friend_not_exist.visibility = View.GONE
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
//        showProgress("")
//        mLockListView = true
//        ApiBuilder.create().getUserFriendList(params).setCallback(object : PplusCallback<NewResultResponse<Friend>> {
//
//            override fun onResponse(call: Call<NewResultResponse<Friend>>, response: NewResultResponse<Friend>) {
//                hideProgress()
//                mLockListView = false
//                mAdapter!!.addAll(response.datas)
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Friend>>, t: Throwable, response: NewResultResponse<Friend>) {
//                hideProgress()
//                mLockListView = false
//            }
//        }).build().call()
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_friend), ToolbarOption.ToolbarMenu.LEFT)
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
