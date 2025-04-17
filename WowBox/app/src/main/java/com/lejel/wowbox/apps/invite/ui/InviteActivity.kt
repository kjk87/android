package com.lejel.wowbox.apps.invite.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.firebase.analytics.FirebaseAnalytics
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.ToastUtil
import com.lejel.wowbox.databinding.ActivityInviteBinding
import com.lejel.wowbox.databinding.ItemTopRight2Binding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call

class InviteActivity : BaseActivity(), ImplToolbar {
    private lateinit var binding: ActivityInviteBinding

    override fun getLayoutView(): View {
        binding = ActivityInviteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val key = LoginInfoManager.getInstance().member!!.userKey
        binding.textInviteCopy.setOnClickListener {
            val url:String
            if(StringUtils.isNotEmpty(LoginInfoManager.getInstance().member!!.inviteUrl)){
                url = LoginInfoManager.getInstance().member!!.inviteUrl!!
            }else{
                url = getString(R.string.format_msg_invite_url, LoginInfoManager.getInstance().member!!.userKey)
            }

            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            val clip = ClipData.newPlainText(getString(R.string.word_invite_friend), url)
            clipboard.setPrimaryClip(clip)
            ToastUtil.show(this, R.string.msg_copied_clipboard)

        }

        binding.textInvite.setOnClickListener {
            share()
        }

        if(StringUtils.isEmpty(LoginInfoManager.getInstance().member!!.inviteUrl)){
            showProgress("")
            ApiBuilder.create().makeInviteUrl().setCallback(object : PplusCallback<NewResultResponse<String>>{
                override fun onResponse(call: Call<NewResultResponse<String>>?, response: NewResultResponse<String>?) {
                    hideProgress()
                    if(response?.result != null){
                        LoginInfoManager.getInstance().member!!.inviteUrl = response.result
                        LoginInfoManager.getInstance().save()
                        binding.textInviteUrl.text = LoginInfoManager.getInstance().member!!.inviteUrl
                    }
                }

                override fun onFailure(call: Call<NewResultResponse<String>>?, t: Throwable?, response: NewResultResponse<String>?) {
                    hideProgress()
                }
            }).build().call()
        }else{
            binding.textInviteUrl.text = LoginInfoManager.getInstance().member!!.inviteUrl
        }
    }

    private fun share() {
        setEvent(FirebaseAnalytics.Event.SHARE)
        val recommendKey = LoginInfoManager.getInstance().member!!.userKey
        val url:String
        if(StringUtils.isNotEmpty(LoginInfoManager.getInstance().member!!.inviteUrl)){
            url = LoginInfoManager.getInstance().member!!.inviteUrl!!
        }else{
            url = getString(R.string.format_msg_invite_url, LoginInfoManager.getInstance().member!!.userKey)
        }

        val text = "${getString(R.string.format_invite_description, recommendKey)}\n${url}"
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, text)
        val chooserIntent = Intent.createChooser(intent, getString(R.string.word_share))
        startActivity(chooserIntent)
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_invite_friend), ToolbarOption.ToolbarMenu.LEFT)
        val item = ItemTopRight2Binding.inflate(layoutInflater)
        item.textTopRight.setText(R.string.word_my_invite_list)
        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, item.root, 0)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressedDispatcher.onBackPressed()
                    }
                    ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
                        val intent = Intent(this@InviteActivity, InviteHistoryActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        startActivity(intent)
                    }
                    else -> {}
                }
            }
        }
    }
}