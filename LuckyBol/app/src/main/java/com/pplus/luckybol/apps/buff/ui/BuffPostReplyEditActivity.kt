package com.pplus.luckybol.apps.buff.ui

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
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
import com.pplus.luckybol.core.network.model.dto.BuffPostReply
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.databinding.ActivityEventReplyEditBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call

class BuffPostReplyEditActivity : BaseActivity(), ImplToolbar {


    private lateinit var mBuffPostReply: BuffPostReply

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
        mBuffPostReply = intent.getParcelableExtra(Const.DATA)!!

        postSeq = intent.getLongExtra(Const.POST, -1)

        if(mBuffPostReply.member!!.profileAttachment != null){
            val glideUrl = GlideUrl("${Const.API_URL}attachment/image?id=${mBuffPostReply.member!!.profileAttachment!!.id}")
            Glide.with(this).load(glideUrl).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.img_commerce_user_profile_default).error(R.drawable.img_commerce_user_profile_default)).into(binding.imageEventReplyEditProfileImage)
        }else{
            binding.imageEventReplyEditProfileImage.setImageResource(R.drawable.img_commerce_user_profile_default)
        }

        binding.textEventReplyEditName.text = mBuffPostReply.member!!.nickname

        binding.editEventReplyEditContents.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun afterTextChanged(editable: Editable) {

//                textRightTop!!.text = getString(R.string.format_count_per, editable.length, 50)
            }
        })

        binding.editEventReplyEditContents.setText(mBuffPostReply.reply)
//        edit_event_reply_edit_contents.setSelection(mBuffPostReply.reply!!.length)

        binding.textEventReplyEditCancel.setOnClickListener {
            onBackPressed()
        }

        binding.textEventReplyEditComplete.setOnClickListener {

            val contents = binding.editEventReplyEditContents.text.toString().trim()

            if(StringUtils.isEmpty(contents)){
                showAlert(R.string.msg_input_reply)
                return@setOnClickListener
            }

            if (mBuffPostReply.reply!! == contents) {
                onBackPressed()
                return@setOnClickListener
            }

            mBuffPostReply.reply = contents
            updateEventReply()
        }
    }

    private fun updateEventReply() {
        val params = HashMap<String, String>()
        params["buffPostReplySeqNo"] = mBuffPostReply.seqNo.toString()
        params["reply"] = mBuffPostReply.reply!!
        showProgress("")
        ApiBuilder.create().modifyBuffPostReply(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
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
                else -> {}
            }
        }

    }
}
