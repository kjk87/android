package com.pplus.prnumberbiz.apps.post.ui

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.post.data.ReviewHeaderCommentAdapter
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Comment
import com.pplus.prnumberbiz.core.network.model.dto.Post
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import kotlinx.android.synthetic.main.activity_review_detail.*
import kotlinx.android.synthetic.main.layout_input_reply.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.*

class ReviewDetailActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }


    override fun getLayoutRes(): Int {
        return R.layout.activity_review_detail
    }

    var mPost: Post? = null
    var mNo = 0L
    var mAdapter: ReviewHeaderCommentAdapter? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        mNo = intent.getLongExtra(Const.POST_NO, 0L)

        mAdapter = ReviewHeaderCommentAdapter(this);
        recycler_review_detail_reply.layoutManager = LinearLayoutManager(this, androidx.recyclerview.widget.OrientationHelper.VERTICAL, false)
        recycler_review_detail_reply.adapter = mAdapter

        text_input_reply.setOnClickListener {
            val comment = edit_reply.text.toString().trim()

            if (StringUtils.isEmpty(comment)) {
                return@setOnClickListener
            }

            showProgress("")
            val params = Comment()
            params.comment = comment

            val post = Post()
            post.no = mNo
            params.post = post

            ApiBuilder.create().insertComment(params).setCallback(object : PplusCallback<NewResultResponse<Comment>> {

                override fun onResponse(call: Call<NewResultResponse<Comment>>, response: NewResultResponse<Comment>) {

                    hideProgress()
                    edit_reply.setText("")
                    getComment()
                }

                override fun onFailure(call: Call<NewResultResponse<Comment>>, t: Throwable, response: NewResultResponse<Comment>) {

                    hideProgress()
                }
            }).build().call()

        }

        getPost()
    }

    private fun getPost() {

        showProgress("")
        ApiBuilder.create().getPost(mNo).setCallback(object : PplusCallback<NewResultResponse<Post>> {

            override fun onResponse(call: Call<NewResultResponse<Post>>, response: NewResultResponse<Post>) {

                mPost = response.data
                mAdapter!!.setPost(mPost!!)
                getComment()
            }

            override fun onFailure(call: Call<NewResultResponse<Post>>, t: Throwable, response: NewResultResponse<Post>) {

                hideProgress()
            }
        }).build().call()
    }

    private fun getComment() {

        showProgress("")
        ApiBuilder.create().getCommentAll(mNo).setCallback(object : PplusCallback<NewResultResponse<Comment>> {

            override fun onResponse(call: Call<NewResultResponse<Comment>>, response: NewResultResponse<Comment>) {

                hideProgress()
                if (response.datas != null) {

                    val commentList = response.datas
                    val pCommentList = ArrayList<Comment>()
                    val cCommentList = HashMap<Long, ArrayList<Comment>>()
                    for (comment in commentList) {
                        val post = Post()
                        post.no = mNo
                        comment.post = post
                        if (comment.depth == 1) {
                            pCommentList.add(comment)
                        } else {
                            if (cCommentList[comment.parent.no] == null) {
                                val childList = ArrayList<Comment>()
                                childList.add(comment)
                                cCommentList[comment.parent.no] = childList
                            } else {
                                cCommentList[comment.parent.no]!!.add(comment)
                            }
                        }
                    }

                    mAdapter!!.clear()
                    mAdapter!!.setcCommentList(cCommentList)
                    mAdapter!!.addAll(pCommentList)
                }

            }

            override fun onFailure(call: Call<NewResultResponse<Comment>>, t: Throwable, response: NewResultResponse<Comment>) {

                hideProgress()
            }
        }).build().call()
    }

    fun deleteComment(no: Long?) {

        showProgress("")
        ApiBuilder.create().deleteComment(no).setCallback(object : PplusCallback<NewResultResponse<Any>> {

            override fun onResponse(call: Call<NewResultResponse<Any>>, response: NewResultResponse<Any>) {

                hideProgress()
                getComment()
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>, t: Throwable, response: NewResultResponse<Any>) {

                hideProgress()
            }
        }).build().call()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Const.REQ_REPLY ->{
                getComment()
            }
        }
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_review_post_detail), ToolbarOption.ToolbarMenu.LEFT)

        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
            }
        }
    }
}
