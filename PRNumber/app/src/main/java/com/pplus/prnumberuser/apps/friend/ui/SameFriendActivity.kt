//package com.pplus.prnumberuser.apps.friend.ui
//
//import android.animation.Animator
//import android.animation.AnimatorListenerAdapter
//import android.content.Context
//import android.graphics.Rect
//import android.os.Bundle
//import androidx.annotation.DimenRes
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import android.view.View
//import android.view.ViewAnimationUtils
//import android.view.ViewTreeObserver
//import com.pplus.utils.part.logs.LogUtil
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.friend.data.SameFriendAdapter
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.User
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import kotlinx.android.synthetic.main.activity_same_friend.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.util.*
//
//class SameFriendActivity : BaseActivity(), ImplToolbar{
//
//
//    private var mAuthor: User? = null
//
//    private var mAdapter: SameFriendAdapter? = null
//    private var mLayoutManager: androidx.recyclerview.widget.LinearLayoutManager? = null
//
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutView(): Int {
//
//        return R.layout.activity_same_friend
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//        mAuthor = intent.getParcelableExtra(Const.DATA)
//
//        mAdapter = SameFriendAdapter(this)
//        mLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
//        recycler_same_friend.layoutManager = mLayoutManager
//        recycler_same_friend.adapter = mAdapter
//        recycler_same_friend.addItemDecoration(CustomItemOffsetDecoration(this, R.dimen.height_60, R.dimen.height_60))
//
//        mAdapter!!.setOnItemClickListener(object : SameFriendAdapter.OnItemClickListener{
//            override fun onItemClick(position: Int, view: View) {
////                val item = mAdapter!!.getItem(position)
////                if (item.page != null) {
////                    val location = IntArray(2)
////                    view.getLocationOnScreen(location)
////                    val x = location[0] + view.width/2
////                    val y = location[1] + view.height/2
////                    PplusCommonUtil.goPage(this@SameFriendActivity, item.page!!, x, y)
////                }
//            }
//        })
//
//        val viewTreeObserver = layout_same_friend_root.viewTreeObserver
//        if (viewTreeObserver.isAlive) {
//            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
//                override fun onGlobalLayout() {
//                    val x = intent.getIntExtra(Const.X, 0)
//                    val y = intent.getIntExtra(Const.Y, 0)
//
//                    revealShow(layout_same_friend_root, x, y)
//                    layout_same_friend_root.viewTreeObserver.removeOnGlobalLayoutListener(this)
//                }
//            })
//        }
//
//        listCall()
//    }
//
//    private fun revealShow(view:View, x:Int, y:Int) {
//
//        val w = view.width
//        val h = view.height
//
//        LogUtil.e(LOG_TAG, "x : {}, y : {}", x, y)
//        LogUtil.e(LOG_TAG, "w : {}, h : {}",w, h)
//
//        val endRadius = (Math.max(w, h) * 1.1);
//
//        val revealAnimator = ViewAnimationUtils.createCircularReveal(view, x, y, 0f, endRadius.toFloat())
//
//        view.visibility = View.VISIBLE
//
//        revealAnimator.addListener(object : AnimatorListenerAdapter() {
//            override fun onAnimationEnd(animation: Animator) {
//                super.onAnimationEnd(animation)
//            }
//        })
//        revealAnimator.duration = 700
//        revealAnimator.start()
//
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
//    private fun listCall() {
//        val params = HashMap<String, String>()
//        params["no"] = "" + mAuthor!!.no!!
//        showProgress("")
//        ApiBuilder.create().getSameFriendAll(params).setCallback(object : PplusCallback<NewResultResponse<User>> {
//
//            override fun onResponse(call: Call<NewResultResponse<User>>, response: NewResultResponse<User>) {
//                hideProgress()
//                if (response.datas.size > 0) {
//                    mAdapter!!.addAll(response.datas)
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<User>>, t: Throwable, response: NewResultResponse<User>) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_who_are_you), ToolbarOption.ToolbarMenu.LEFT)
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
