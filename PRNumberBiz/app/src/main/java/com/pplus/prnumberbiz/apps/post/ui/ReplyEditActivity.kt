package com.pplus.prnumberbiz.apps.post.ui

import android.app.Activity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Comment
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import kotlinx.android.synthetic.main.activity_reply_edit.*
import network.common.PplusCallback
import retrofit2.Call

class ReplyEditActivity : BaseActivity(), ImplToolbar {


    private var comment: Comment? = null

    private var postSeq: Long = 0

    override fun getPID(): String {

        return ""
    }

    override fun getLayoutRes(): Int {

        return R.layout.activity_reply_edit
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        comment = intent.getParcelableExtra(Const.REPLY)

        postSeq = intent.getLongExtra(Const.POST, -1)

        if (comment!!.author.profileImage != null) {
            Glide.with(this).load(comment!!.author.profileImage!!.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.ic_gift_profile_default).error(R.drawable.ic_gift_profile_default)).into(image_reply_edit_profileImage)
        }

        text_reply_edit_name.text = comment!!.author.nickname

        edit_reply_edit_contents.setText(comment!!.comment)
        edit_reply_edit_contents.setSelection(comment!!.comment.length)

        text_reply_edit_cancel.setOnClickListener {
            onBackPressed()
        }

        text_reply_edit_complete.setOnClickListener {

            val contents = edit_reply_edit_contents.text.toString().trim()

            if(StringUtils.isEmpty(contents)){
                showAlert(R.string.msg_input_reply)
                return@setOnClickListener
            }

            if (comment!!.comment == contents) {
                onBackPressed()
                return@setOnClickListener
            }

            comment!!.comment = contents
            editComment()
        }
    }

    private fun editComment() {
        showProgress("")
        ApiBuilder.create().updateComment(comment).setCallback(object : PplusCallback<NewResultResponse<Comment>> {
            override fun onResponse(call: Call<NewResultResponse<Comment>>?, response: NewResultResponse<Comment>?) {
                hideProgress()
                setResult(Activity.RESULT_OK)
                finish()
            }

            override fun onFailure(call: Call<NewResultResponse<Comment>>?, t: Throwable?, response: NewResultResponse<Comment>?) {
                hideProgress()
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_reply_edit), ToolbarOption.ToolbarMenu.LEFT)
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
