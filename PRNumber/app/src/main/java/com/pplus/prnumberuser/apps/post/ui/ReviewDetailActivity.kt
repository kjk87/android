//package com.pplus.prnumberuser.apps.post.ui
//
//import android.app.Activity
//import android.content.Intent
//import android.os.Bundle
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.OrientationHelper
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.post.data.ReviewHeaderCommentAdapter
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Comment
//import com.pplus.prnumberuser.core.network.model.dto.Page
//import com.pplus.prnumberuser.core.network.model.dto.Post
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import kotlinx.android.synthetic.main.activity_post_detail.*
//import kotlinx.android.synthetic.main.layout_input_reply.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.util.*
//import kotlin.collections.HashMap
//
//class ReviewDetailActivity : BaseActivity(), ImplToolbar {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutView(): Int {
//        return R.layout.activity_post_detail
//    }
//
//    var mPage: Page? = null
//    var mPost: Post? = null
//    var mAdapter: ReviewHeaderCommentAdapter? = null
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//        mPost = intent.getParcelableExtra(Const.DATA)
//
//        mAdapter = ReviewHeaderCommentAdapter(this);
//        recycler_post_detail_reply.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.OrientationHelper.VERTICAL, false)
//        recycler_post_detail_reply.adapter = mAdapter
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
//            val post = Post()
//            post.no = mPost!!.no
//            params.post = post
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
//        getPage()
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            Const.REQ_MODIFY -> {
//                if (resultCode == Activity.RESULT_OK) {
//                    getPost()
//                }
//            }
//            Const.REQ_REPLY -> {
//                getComment()
//            }
//        }
//    }
//
//    private fun getPage() {
//        val params = HashMap<String, String>()
//        params["postNo"] = mPost!!.no.toString()
//        showProgress("")
//        ApiBuilder.create().getPostPage(params).setCallback(object : PplusCallback<NewResultResponse<Page>> {
//            override fun onResponse(call: Call<NewResultResponse<Page>>?, response: NewResultResponse<Page>?) {
//                hideProgress()
//                mPage = response!!.data
//                mAdapter!!.setPage(mPage)
//                getPost()
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Page>>?, t: Throwable?, response: NewResultResponse<Page>?) {
//                hideProgress()
//                getPost()
//            }
//        }).build().call()
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
//                        val post = Post()
//                        post.no = mPost!!.no
//                        comment.post = post
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
//                    mAdapter!!.setcCommentList(cCommentList)
//                    mAdapter!!.addAll(pCommentList)
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Comment>>, t: Throwable, response: NewResultResponse<Comment>) {
//
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_review_post_detail), ToolbarOption.ToolbarMenu.LEFT)
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
