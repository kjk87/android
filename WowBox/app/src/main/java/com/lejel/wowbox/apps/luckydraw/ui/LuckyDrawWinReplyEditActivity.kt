package com.lejel.wowbox.apps.luckydraw.ui

import android.os.Bundle
import android.view.View
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.utils.StringUtils
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.LuckyDrawWinReply
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityLuckyDrawWinReplyEditBinding
import retrofit2.Call

class LuckyDrawWinReplyEditActivity : BaseActivity(), ImplToolbar {
    private lateinit var binding: ActivityLuckyDrawWinReplyEditBinding

    override fun getLayoutView(): View {
        binding = ActivityLuckyDrawWinReplyEditBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val luckyDrawWinReply = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, LuckyDrawWinReply::class.java)!!
        binding.editLuckyDrawWinReplyEdit.setText(luckyDrawWinReply.reply)

        binding.textLuckyDrawWinReplyEditComplete.setOnClickListener {
            val reply = binding.editLuckyDrawWinReplyEdit.text.toString().trim()

            if (StringUtils.isEmpty(reply)) {
                showAlert(R.string.msg_input_reply)
                return@setOnClickListener
            }

            val params = HashMap<String, String>()
            params["reply"] = reply
            params["seqNo"] = luckyDrawWinReply.seqNo.toString()

            showProgress("")
            ApiBuilder.create().updateLuckyDrawWinReply(params).setCallback(object : PplusCallback<NewResultResponse<LuckyDrawWinReply>> {
                override fun onResponse(call: Call<NewResultResponse<LuckyDrawWinReply>>?, response: NewResultResponse<LuckyDrawWinReply>?) {
                    hideProgress()
                    setResult(RESULT_OK)
                    finish()
                }

                override fun onFailure(call: Call<NewResultResponse<LuckyDrawWinReply>>?, t: Throwable?, response: NewResultResponse<LuckyDrawWinReply>?) {
                    hideProgress()
                    if(response?.code == 589){
                        showAlert(R.string.msg_can_not_use_cursing)
                    }
                }
            }).build().call()
        }
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_reply), ToolbarOption.ToolbarMenu.LEFT)
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