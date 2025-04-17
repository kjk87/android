//package com.pplus.prnumberuser.apps.post.ui
//
//import android.content.Context
//import android.content.Intent
//import android.graphics.Rect
//import android.os.Bundle
//import androidx.annotation.DimenRes
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.OrientationHelper
//import androidx.recyclerview.widget.RecyclerView
//import android.view.View
//import androidx.activity.result.contract.ActivityResultContracts
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.post.data.PostHeaderCommentAdapter
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Comment
//import com.pplus.prnumberuser.core.network.model.dto.Post
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import kotlinx.android.synthetic.main.activity_post_detail.*
//import kotlinx.android.synthetic.main.layout_input_reply.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.util.*
//
//class PostDetailActivity : BaseActivity(), ImplToolbar {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_post_detail
//    }
//
//    var mPost: Post? = null
//    var mAdapter: PostHeaderCommentAdapter? = null
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//        mPost = intent.getParcelableExtra(Const.DATA)
//
//        mAdapter = PostHeaderCommentAdapter(this)
//        recycler_post_detail_reply.layoutManager = LinearLayoutManager(this)
//        recycler_post_detail_reply.adapter = mAdapter
//        recycler_post_detail_reply.addItemDecoration(CustomItemOffsetDecoration(this, R.dimen.height_100))
//
//        text_input_reply.setOnClickListener {
//            val comment = edit_reply.text.toString().trim()
//
//            if (StringUtils.isEmpty(comment)) {
//                return@setOnClickListener
//            }
//
//            showProgress("")
//            val params = Comment()
//            params.comment = comment
//
//            params.post = mPost
//
//            ApiBuilder.create().insertComment(params).setCallback(object : PplusCallback<NewResultResponse<Comment>> {
//
//                override fun onResponse(call: Call<NewResultResponse<Comment>>, response: NewResultResponse<Comment>) {
//
//                    hideProgress()
//                    edit_reply.setText("")
//                    getComment()
//                }
//
//                override fun onFailure(call: Call<NewResultResponse<Comment>>, t: Throwable, response: NewResultResponse<Comment>) {
//
//                    hideProgress()
//                }
//            }).build().call()
//
//        }
//
//        getPost()
//    }
//
//    private inner class CustomItemOffsetDecoration(private val mLastOffset: Int) : RecyclerView.ItemDecoration() {
//
//        constructor(context: Context, @DimenRes lastOffsetId: Int) : this(context.resources.getDimensionPixelSize(lastOffsetId)) {}
//
//        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
//
//            super.getItemOffsets(outRect, view, parent, state)
//
//            val position = parent.getChildAdapterPosition(view)
//            if (mAdapter!!.itemCount > 0 && position == mAdapter!!.itemCount - 1) {
//                outRect.bottom = mLastOffset
//            }
//
//        }
//    }
//
//    private fun getPost() {
//
//        showProgress("")
//        ApiBuilder.create().getPost(mPost!!.no!!).setCallback(object : PplusCallback<NewResultResponse<Post>> {
//
//            override fun onResponse(call: Call<NewResultResponse<Post>>, response: NewResultResponse<Post>) {
//
//                mPost = response.data
//                mAdapter!!.setPost(mPost!!)
//                getComment()
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Post>>, t: Throwable, response: NewResultResponse<Post>) {
//
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    private fun getComment() {
//
//        showProgress("")
//        ApiBuilder.create().getCommentAll(mPost!!.no!!).setCallback(object : PplusCallback<NewResultResponse<Comment>> {
//
//            override fun onResponse(call: Call<NewResultResponse<Comment>>, response: NewResultResponse<Comment>) {
//
//                hideProgress()
//                if (response.datas != null) {
//
//                    val commentList = response.datas
//                    val pCommentList = ArrayList<Comment>()
//                    val cCommentList = HashMap<Long, ArrayList<Comment>>()
//                    for (comment in commentList) {
//
//                        comment.post = mPost
//                        if (comment.depth == 1) {
//                            pCommentList.add(comment)
//                        } else {
//                            if (cCommentList[comment.parent.no] == null) {
//                                val childList = ArrayList<Comment>()
//                                childList.add(comment)
//                                cCommentList[comment.parent.no] = childList
//                            } else {
//                                cCommentList[comment.parent.no]!!.add(comment)
//                            }
//                        }
//                    }
//
//                    mAdapter!!.clear()
//                    mAdapter!!.setCommentCount(response.datas.size)
//                    mAdapter!!.setcCommentList(cCommentList)
//                    mAdapter!!.addAll(pCommentList)
//                }
//
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Comment>>, t: Throwable, response: NewResultResponse<Comment>) {
//
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    val replyLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        getComment()
//    }
//
//    fun deleteComment(no: Long?) {
//
//        showProgress("")
//        ApiBuilder.create().deleteComment(no!!).setCallback(object : PplusCallback<NewResultResponse<Any>> {
//
//            override fun onResponse(call: Call<NewResultResponse<Any>>, response: NewResultResponse<Any>) {
//
//                hideProgress()
//                getComment()
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Any>>, t: Throwable, response: NewResultResponse<Any>) {
//
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_news), ToolbarOption.ToolbarMenu.LEFT)
//
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
