package com.pplus.prnumberbiz.apps.marketing.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.webkit.URLUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.core.code.common.SnsTypeCode
import com.pplus.prnumberbiz.core.util.ToastUtil
import kotlinx.android.synthetic.main.activity_sns_url.*

class SnsUrlActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_sns_url
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val key = intent.getStringExtra(Const.KEY)
        val url = intent.getStringExtra(Const.DATA)
        when (key) {
            SnsTypeCode.blog.name -> {
                text_sns_url_title.text = getString(R.string.format_msg_sns_url_title, getString(R.string.word_blog))
                edit_sns_url.hint = getString(R.string.format_hint_input_kakaochannel_url, getString(R.string.word_blog))
                setTitle(getString(R.string.word_sync_blog))
            }
            SnsTypeCode.homepage.name -> {
                text_sns_url_title.text = getString(R.string.format_msg_sns_url_title, getString(R.string.word_homepage))
                edit_sns_url.hint = getString(R.string.format_hint_input_kakaochannel_url, getString(R.string.word_homepage))
                setTitle(getString(R.string.word_sync_homepage))
            }
            SnsTypeCode.youtube.name -> {
                text_sns_url_title.text = getString(R.string.format_msg_sns_url_title, getString(R.string.word_youtube))
                edit_sns_url.hint = getString(R.string.format_hint_input_kakaochannel_url, getString(R.string.word_youtube))
                setTitle(getString(R.string.word_sync_youtube))
            }
            SnsTypeCode.kakaochannel.name -> {
                text_sns_url_title.text = getString(R.string.format_msg_sns_url_title, getString(R.string.word_kakao_channel))
                edit_sns_url.hint = getString(R.string.format_hint_input_kakaochannel_url, getString(R.string.word_kakao_channel))
                setTitle(getString(R.string.word_sync_kakao_channel))
            }
            SnsTypeCode.twitter.name -> {
                text_sns_url_title.text = getString(R.string.format_msg_sns_url_title, getString(R.string.word_twitter))
                edit_sns_url.hint = getString(R.string.format_hint_input_kakaochannel_url, getString(R.string.word_twitter))
                setTitle(getString(R.string.word_sync_twitter))
            }
            SnsTypeCode.instagram.name -> {
                text_sns_url_title.text = getString(R.string.format_msg_sns_url_title, getString(R.string.word_instagram))
                edit_sns_url.hint = getString(R.string.format_hint_input_kakaochannel_url, getString(R.string.word_instagram))
                setTitle(getString(R.string.word_sync_instagram))
            }
            SnsTypeCode.kakao.name -> {
                text_sns_url_title.text = getString(R.string.format_msg_sns_url_title, getString(R.string.word_kakao_story))
                edit_sns_url.hint = getString(R.string.format_hint_input_kakaochannel_url, getString(R.string.word_kakao_story))
                setTitle(getString(R.string.word_sync_kakaostory))
            }
            SnsTypeCode.facebook.name -> {
                text_sns_url_title.text = getString(R.string.format_msg_sns_url_title, getString(R.string.word_facebook))
                edit_sns_url.hint = getString(R.string.format_hint_input_kakaochannel_url, getString(R.string.word_facebook))
                setTitle(getString(R.string.word_sync_facebook))
            }
        }

        if (StringUtils.isNotEmpty(url)) {
            edit_sns_url.setText(url)
        }
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        val key = intent.getStringExtra(Const.KEY)
        when (key) {
            SnsTypeCode.blog.name -> {
                toolbarOption.initializeDefaultToolbar(getString(R.string.word_sync_blog), ToolbarOption.ToolbarMenu.LEFT)
            }
            SnsTypeCode.homepage.name -> {
                toolbarOption.initializeDefaultToolbar(getString(R.string.word_sync_homepage), ToolbarOption.ToolbarMenu.LEFT)
            }
            SnsTypeCode.youtube.name -> {
                toolbarOption.initializeDefaultToolbar(getString(R.string.word_sync_youtube), ToolbarOption.ToolbarMenu.LEFT)
            }
            SnsTypeCode.kakaochannel.name -> {
                toolbarOption.initializeDefaultToolbar(getString(R.string.word_sync_kakao_channel), ToolbarOption.ToolbarMenu.LEFT)
            }
            SnsTypeCode.twitter.name -> {
                toolbarOption.initializeDefaultToolbar(getString(R.string.word_sync_twitter), ToolbarOption.ToolbarMenu.LEFT)
            }
            SnsTypeCode.instagram.name -> {
                toolbarOption.initializeDefaultToolbar(getString(R.string.word_sync_instagram), ToolbarOption.ToolbarMenu.LEFT)
            }
            SnsTypeCode.kakao.name -> {
                toolbarOption.initializeDefaultToolbar(getString(R.string.word_sync_kakaostory), ToolbarOption.ToolbarMenu.LEFT)
            }
            SnsTypeCode.facebook.name -> {
                toolbarOption.initializeDefaultToolbar(getString(R.string.word_sync_facebook), ToolbarOption.ToolbarMenu.LEFT)
            }
        }

        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, getString(R.string.word_complete))
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
                ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
                    val url = edit_sns_url.text.toString().trim()
                    if (URLUtil.isValidUrl(url)) {
                        val data = Intent()
                        data.putExtra(Const.KEY, intent.getStringExtra(Const.KEY))
                        data.putExtra(Const.URL, url)
                        setResult(Activity.RESULT_OK, data)
                        finish()
                    } else {
                        ToastUtil.show(this, R.string.msg_invalid_url)
                    }
                }
            }
        }

    }
}
