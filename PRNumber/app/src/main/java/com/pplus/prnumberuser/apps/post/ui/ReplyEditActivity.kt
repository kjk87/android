//package com.pplus.prnumberuser.apps.post.ui
//
//import android.app.Activity
//import android.os.Bundle
//import android.text.Editable
//import android.text.TextWatcher
//import android.util.TypedValue
//import android.view.ContextThemeWrapper
//import android.view.Gravity
//import android.widget.TextView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.request.RequestOptions
//import com.pplus.utils.part.apps.resource.ResourceUtil
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Comment
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import kotlinx.android.synthetic.main.activity_reply_edit.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//
//class ReplyEditActivity : BaseActivity(), ImplToolbar {
//
//
//    private var comment: Comment? = null
//
//    private var postSeq: Long = 0
//
//    override fun getPID(): String {
//
//        return ""
//    }
//
//    override fun getLayoutView(): Int {
//
//        return R.layout.activity_reply_edit
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//        comment = intent.getParcelableExtra(Const.REPLY)
//
//        postSeq = intent.getLongExtra(Const.POST, -1)
//
//        if (comment!!.author.profileImage != null) {
//            Glide.with(this).load(comment!!.author.profileImage!!.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.ic_gift_profile_default).error(R.drawable.ic_gift_profile_default)).into(image_reply_edit_profileImage)
//        }
//
//        text_reply_edit_name.text = comment!!.author.nickname
//
//        edit_reply_edit_contents.addTextChangedListener(object : TextWatcher {
//
//            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
//
//            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
//
//            override fun afterTextChanged(editable: Editable) {
//
//                textRightTop!!.text = getString(R.string.format_count_per, editable.length, 50)
//            }
//        })
//
//        edit_reply_edit_contents.setText(comment!!.comment)
//        edit_reply_edit_contents.setSelection(comment!!.comment.length)
//
//        text_reply_edit_cancel.setOnClickListener {
//            onBackPressed()
//        }
//
//        text_reply_edit_complete.setOnClickListener {
//
//            val contents = edit_reply_edit_contents.text.toString().trim()
//
//            if(StringUtils.isEmpty(contents)){
//                showAlert(R.string.msg_input_reply)
//                return@setOnClickListener
//            }
//
//            if (comment!!.comment == contents) {
//                onBackPressed()
//                return@setOnClickListener
//            }
//
//            comment!!.comment = contents
//            editComment()
//        }
//    }
//
//    private fun editComment() {
//        showProgress("")
//        ApiBuilder.create().updateComment(comment!!).setCallback(object : PplusCallback<NewResultResponse<Comment>> {
//            override fun onResponse(call: Call<NewResultResponse<Comment>>?, response: NewResultResponse<Comment>?) {
//                hideProgress()
//                setResult(Activity.RESULT_OK)
//                finish()
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Comment>>?, t: Throwable?, response: NewResultResponse<Comment>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    private var textRightTop: TextView? = null
//
//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_reply_edit), ToolbarOption.ToolbarMenu.LEFT)
//        textRightTop = TextView(ContextThemeWrapper(this, R.style.buttonStyle))
//        textRightTop!!.text = getString(R.string.format_count_per, 0, 50)
//        textRightTop!!.isClickable = true
//        textRightTop!!.gravity = Gravity.CENTER
//        textRightTop!!.setPadding(0, 0, resources.getDimensionPixelSize(R.dimen.width_66), 0)
//        textRightTop!!.setTextColor(ResourceUtil.getColorStateList(this, R.color.color_579ffb))
//        textRightTop!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(R.dimen.textSize_45pt).toFloat())
//        textRightTop!!.setSingleLine()
//        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, textRightTop, 0)
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
//
//    }
//}
