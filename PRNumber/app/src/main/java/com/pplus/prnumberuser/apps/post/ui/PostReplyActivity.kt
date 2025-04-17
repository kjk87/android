//package com.pplus.prnumberuser.apps.post.ui
//
//import android.app.Activity
//import android.content.Intent
//import android.os.Bundle
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.OrientationHelper
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.post.data.ReplyAdapter
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Comment
//import com.pplus.prnumberuser.core.network.model.dto.Post
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.activity_post_detail.*
//import kotlinx.android.synthetic.main.layout_input_reply.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.util.*
//
//class PostReplyActivity : BaseActivity(), ImplToolbar {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutView(): Int {
//        return R.layout.activity_post_detail
//    }
//
//    var mPost: Post? = null
//    var mAdapter: ReplyAdapter? = null
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//        mPost = intent.getParcelableExtra(Const.DATA)
//
//        mAdapter = ReplyAdapter(this, mPost!!)
//        recycler_post_detail_reply.layoutManager = LinearLayoutManager(this)
//        recycler_post_detail_reply.adapter = mAdapter
//
//        text_input_reply.setOnClickListener {
//
//            if(!PplusCommonUtil.loginCheck(this, null)){
//                return@setOnClickListener
//            }
//
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
//        val data = Intent()
//        data.putExtra(Const.DATA, mPost)
//        data.putExtra(Const.POSITION, intent.getIntExtra(Const.POSITION, 0))
//        setResult(Activity.RESULT_OK, data)
//
//        getComment()
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
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_reply), ToolbarOption.ToolbarMenu.LEFT)
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
