package com.lejel.wowbox.apps.luckybox.ui

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.utils.StringUtils
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.LuckyBoxReply
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityLuckyBoxReplyEditBinding
import retrofit2.Call

class LuckyBoxReplyEditActivity : BaseActivity(), ImplToolbar {


    private lateinit var mLuckyBoxReply: LuckyBoxReply

    override fun getPID(): String {

        return ""
    }

    private lateinit var binding: ActivityLuckyBoxReplyEditBinding

    override fun getLayoutView(): View {
        binding = ActivityLuckyBoxReplyEditBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        mLuckyBoxReply = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, LuckyBoxReply::class.java)!!

        Glide.with(this).load(Const.API_URL+"profile/${mLuckyBoxReply.userKey}").apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop()).into(binding.imageEventReplyEditProfileImage)

        binding.textEventReplyEditName.text = mLuckyBoxReply.memberTotal!!.nickname

        binding.editEventReplyEditContents.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun afterTextChanged(editable: Editable) {

//                textRightTop!!.text = getString(R.string.format_count_per, editable.length, 50)
            }
        })

        binding.editEventReplyEditContents.setText(mLuckyBoxReply.reply)
//        edit_event_reply_edit_contents.setSelection(mEventReply!!.reply!!.length)

        binding.textEventReplyEditCancel.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.textEventReplyEditComplete.setOnClickListener {

            val contents = binding.editEventReplyEditContents.text.toString().trim()

            if(StringUtils.isEmpty(contents)){
                showAlert(R.string.msg_input_reply)
                return@setOnClickListener
            }

            if (mLuckyBoxReply.reply!! == contents) {
                onBackPressedDispatcher.onBackPressed()
                return@setOnClickListener
            }

            mLuckyBoxReply.reply = contents
            updateEventReply()
        }
    }

    private fun updateEventReply() {
        showProgress("")
        ApiBuilder.create().updateLuckyBoxReply(mLuckyBoxReply.seqNo!!, mLuckyBoxReply).setCallback(object : PplusCallback<NewResultResponse<Any>> {
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
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressedDispatcher.onBackPressed()
                    }

                    else -> {}
                }
            }
        }
    }
}
