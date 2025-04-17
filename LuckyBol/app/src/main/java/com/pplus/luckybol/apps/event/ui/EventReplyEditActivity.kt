package com.pplus.luckybol.apps.event.ui

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.RequestOptions
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.EventReply
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.databinding.ActivityEventReplyEditBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call

class EventReplyEditActivity : BaseActivity(), ImplToolbar {


    private var mEventReply: EventReply? = null

    private var postSeq: Long = 0

    override fun getPID(): String {

        return ""
    }

    private lateinit var binding: ActivityEventReplyEditBinding

    override fun getLayoutView(): View {
        binding = ActivityEventReplyEditBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        mEventReply = intent.getParcelableExtra(Const.EVENT_REPLY)

        postSeq = intent.getLongExtra(Const.POST, -1)

        if(mEventReply!!.member!!.profileAttachment != null){
            val glideUrl = GlideUrl("${Const.API_URL}attachment/image?id=${mEventReply!!.member!!.profileAttachment!!.id}")
            Glide.with(this).load(glideUrl).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.img_commerce_user_profile_default).error(R.drawable.img_commerce_user_profile_default)).into(binding.imageEventReplyEditProfileImage)
        }else{
            binding.imageEventReplyEditProfileImage.setImageResource(R.drawable.img_commerce_user_profile_default)
        }

        binding.textEventReplyEditName.text = mEventReply!!.member!!.nickname

        binding.editEventReplyEditContents.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun afterTextChanged(editable: Editable) {

//                textRightTop!!.text = getString(R.string.format_count_per, editable.length, 50)
            }
        })

        binding.editEventReplyEditContents.setText(mEventReply!!.reply)
//        edit_event_reply_edit_contents.setSelection(mEventReply!!.reply!!.length)

        binding.textEventReplyEditCancel.setOnClickListener {
            onBackPressed()
        }

        binding.textEventReplyEditComplete.setOnClickListener {

            val contents = binding.editEventReplyEditContents.text.toString().trim()

            if(StringUtils.isEmpty(contents)){
                showAlert(R.string.msg_input_reply)
                return@setOnClickListener
            }

            if (mEventReply!!.reply!! == contents) {
                onBackPressed()
                return@setOnClickListener
            }

            mEventReply!!.reply = contents
            updateEventReply()
        }
    }

    private fun updateEventReply() {
        showProgress("")
        ApiBuilder.create().updateEventReply(mEventReply!!).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?,
                                    response: NewResultResponse<Any>?) {
                hideProgress()
                setResult(Activity.RESULT_OK)
                finish()
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<Any>?) {
                hideProgress()
            }
        }).build().call()
    }

    private var textRightTop: TextView? = null

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_reply_edit), ToolbarOption.ToolbarMenu.LEFT)
//        textRightTop = TextView(ContextThemeWrapper(this, R.style.buttonStyle))
//        textRightTop!!.text = getString(R.string.format_count_per, 0, 50)
//        textRightTop!!.isClickable = true
//        textRightTop!!.gravity = Gravity.CENTER
//        textRightTop!!.setPadding(0, 0, resources.getDimensionPixelSize(R.dimen.width_66), 0)
//        textRightTop!!.setTextColor(ResourceUtil.getColorStateList(this, R.color.color_fc5c57))
//        textRightTop!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(R.dimen.textSize_45pt).toFloat())
//        textRightTop!!.setSingleLine()
//        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, textRightTop, 0)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
                else -> {}
            }
        }

    }
}
