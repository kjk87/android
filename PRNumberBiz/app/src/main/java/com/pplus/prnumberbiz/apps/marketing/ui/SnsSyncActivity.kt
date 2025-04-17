package com.pplus.prnumberbiz.apps.marketing.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.common.ui.custom.SafeSwitchCompat
import com.pplus.prnumberbiz.core.code.common.SnsTypeCode
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.No
import com.pplus.prnumberbiz.core.network.model.dto.Page
import com.pplus.prnumberbiz.core.network.model.dto.Sns
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import kotlinx.android.synthetic.main.activity_sns_sync.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.*

class SnsSyncActivity : BaseActivity(), SafeSwitchCompat.OnSafeCheckedListener {
    override fun getPID(): String {
        return "Main_menu_keyword/sns"
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_sns_sync
    }

    private var mPage: Page? = null

    override fun initializeView(savedInstanceState: Bundle?) {

        mPage = LoginInfoManager.getInstance().user.page

        text_sync_sns_title.setOnClickListener {
            onBackPressed()
        }


        switch_sync_twitter.onSafeCheckedListener = this
        switch_sync_facebook.onSafeCheckedListener = this
        switch_sync_instagram.onSafeCheckedListener = this
        switch_sync_kakao.onSafeCheckedListener = this
        switch_sync_blog.onSafeCheckedListener = this
        switch_sync_youtube.onSafeCheckedListener = this
        switch_sync_kakaochannel.onSafeCheckedListener = this
        switch_sync_homepage.onSafeCheckedListener = this

        getSnsLink()
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (buttonView!!.tag != null && StringUtils.isNotEmpty((buttonView.tag as Sns).url)) {
            deletePageSns((buttonView.tag as Sns).type, buttonView)
        } else {
            (buttonView as SafeSwitchCompat).setSafeCheck(false, SafeSwitchCompat.IGNORE)
            when (buttonView) {
                switch_sync_twitter -> {
                    val intent = Intent(this, SnsUrlActivity::class.java)
                    intent.putExtra(Const.KEY, SnsTypeCode.twitter.name)
                    startActivityForResult(intent, Const.REQ_SYNC_SNS)
                }
                switch_sync_facebook -> {
                    val intent = Intent(this, SnsUrlActivity::class.java)
                    intent.putExtra(Const.KEY, SnsTypeCode.facebook.name)
                    startActivityForResult(intent, Const.REQ_SYNC_SNS)
                }
                switch_sync_instagram -> {
                    val intent = Intent(this, SnsUrlActivity::class.java)
                    intent.putExtra(Const.KEY, SnsTypeCode.instagram.name)
                    startActivityForResult(intent, Const.REQ_SYNC_SNS)
                }
                switch_sync_kakao -> {
                    val intent = Intent(this, SnsUrlActivity::class.java)
                    intent.putExtra(Const.KEY, SnsTypeCode.kakao.name)
                    startActivityForResult(intent, Const.REQ_SYNC_SNS)
                }
                switch_sync_blog -> {
                    val intent = Intent(this, SnsUrlActivity::class.java)
                    intent.putExtra(Const.KEY, SnsTypeCode.blog.name)
                    startActivityForResult(intent, Const.REQ_SYNC_SNS)
                }
                switch_sync_homepage -> {
                    val intent = Intent(this, SnsUrlActivity::class.java)
                    intent.putExtra(Const.KEY, SnsTypeCode.homepage.name)
                    startActivityForResult(intent, Const.REQ_SYNC_SNS)
                }
                switch_sync_youtube -> {
                    val intent = Intent(this, SnsUrlActivity::class.java)
                    intent.putExtra(Const.KEY, SnsTypeCode.youtube.name)
                    startActivityForResult(intent, Const.REQ_SYNC_SNS)
                }
                switch_sync_kakaochannel -> {
                    val intent = Intent(this, SnsUrlActivity::class.java)
                    intent.putExtra(Const.KEY, SnsTypeCode.kakaochannel.name)
                    startActivityForResult(intent, Const.REQ_SYNC_SNS)
                }
            }
        }
    }

    override fun onAlwaysCalledListener(buttonView: CompoundButton?, isChecked: Boolean) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            Const.REQ_SYNC_SNS -> if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    val type = data.getStringExtra(Const.KEY)
                    val url = data.getStringExtra(Const.URL)
                    updatePageSns(type, url, true)
                }
            }
        }
    }

    private fun getSnsLink() {

        val params = HashMap<String, String>()
        params["no"] = "" + LoginInfoManager.getInstance().user.page!!.no!!

        ApiBuilder.create().getSnsLinkAll(params).setCallback(object : PplusCallback<NewResultResponse<Sns>> {

            override fun onResponse(call: Call<NewResultResponse<Sns>>, response: NewResultResponse<Sns>) {

                initSNS(response.datas)
            }

            override fun onFailure(call: Call<NewResultResponse<Sns>>, t: Throwable, response: NewResultResponse<Sns>) {

            }
        }).build().call()
    }

    private fun initSNS(snsList: List<Sns>?) {

        if (snsList != null && !snsList.isEmpty()) {
            for (sns in snsList) {
                setSns(sns)
            }
        }
    }

    private fun setSns(sns: Sns?) {

        if (sns != null) {
            when (SnsTypeCode.valueOf(sns.type)) {

                SnsTypeCode.twitter -> switch_sync_twitter.tag = sns
                SnsTypeCode.facebook -> switch_sync_facebook.tag = sns
                SnsTypeCode.instagram -> switch_sync_instagram.tag = sns
                SnsTypeCode.kakao -> switch_sync_kakao.tag = sns
                SnsTypeCode.blog -> switch_sync_blog.tag = sns
                SnsTypeCode.youtube -> switch_sync_youtube.tag = sns
                SnsTypeCode.kakaochannel -> switch_sync_kakaochannel.tag = sns
                SnsTypeCode.homepage -> switch_sync_homepage.tag = sns
            }
            if (StringUtils.isEmpty(sns.url)) {
                when (SnsTypeCode.valueOf(sns.type)) {

                    SnsTypeCode.twitter -> {
                        switch_sync_twitter.setSafeCheck(false, SafeSwitchCompat.IGNORE)
                        layout_sync_twitter_url.visibility = View.GONE
                    }
                    SnsTypeCode.facebook -> {
                        switch_sync_facebook.setSafeCheck(false, SafeSwitchCompat.IGNORE)
                        layout_sync_facebook_url.visibility = View.GONE
                    }
                    SnsTypeCode.instagram -> {
                        switch_sync_instagram.setSafeCheck(false, SafeSwitchCompat.IGNORE)
                        layout_sync_instagram_url.visibility = View.GONE
                    }
                    SnsTypeCode.kakao -> {
                        switch_sync_kakao.setSafeCheck(false, SafeSwitchCompat.IGNORE)
                        layout_sync_kakao_url.visibility = View.GONE
                    }
                    SnsTypeCode.blog -> {
                        switch_sync_blog.setSafeCheck(false, SafeSwitchCompat.IGNORE)
                        layout_sync_blog_url.visibility = View.GONE
                    }
                    SnsTypeCode.youtube -> {
                        switch_sync_youtube.setSafeCheck(false, SafeSwitchCompat.IGNORE)
                        layout_sync_youtube_url.visibility = View.GONE
                    }
                    SnsTypeCode.kakaochannel -> {
                        switch_sync_kakaochannel.setSafeCheck(false, SafeSwitchCompat.IGNORE)
                        layout_sync_kakaochannel_url.visibility = View.GONE
                    }
                    SnsTypeCode.homepage -> {
                        switch_sync_homepage.setSafeCheck(false, SafeSwitchCompat.IGNORE)
                        layout_sync_homepage_url.visibility = View.GONE
                    }
                }
            } else {
                when (SnsTypeCode.valueOf(sns.type)) {

                    SnsTypeCode.twitter -> {
                        switch_sync_twitter.setSafeCheck(true, SafeSwitchCompat.IGNORE)
                        layout_sync_twitter_url.visibility = View.VISIBLE
                        text_sync_twitter_url.text = sns.url
                        text_sync_twitter_url_modify.setOnClickListener {
                            val intent = Intent(this, SnsUrlActivity::class.java)
                            intent.putExtra(Const.DATA, sns.url)
                            intent.putExtra(Const.KEY, SnsTypeCode.facebook.name)
                            startActivityForResult(intent, Const.REQ_SYNC_SNS)
                        }
                    }
                    SnsTypeCode.facebook -> {
                        switch_sync_facebook.setSafeCheck(true, SafeSwitchCompat.IGNORE)
                        layout_sync_facebook_url.visibility = View.VISIBLE
                        text_sync_facebook_url.text = sns.url
                        text_sync_facebook_url_modify.setOnClickListener {
                            val intent = Intent(this, SnsUrlActivity::class.java)
                            intent.putExtra(Const.DATA, sns.url)
                            intent.putExtra(Const.KEY, SnsTypeCode.facebook.name)
                            startActivityForResult(intent, Const.REQ_SYNC_SNS)
                        }
                    }
                    SnsTypeCode.instagram -> {
                        switch_sync_instagram.setSafeCheck(true, SafeSwitchCompat.IGNORE)
                        layout_sync_instagram_url.visibility = View.VISIBLE
                        text_sync_instagram_url.text = sns.url
                        text_sync_instagram_url_modify.setOnClickListener {
                            val intent = Intent(this, SnsUrlActivity::class.java)
                            intent.putExtra(Const.DATA, sns.url)
                            intent.putExtra(Const.KEY, SnsTypeCode.instagram.name)
                            startActivityForResult(intent, Const.REQ_SYNC_SNS)
                        }
                    }
                    SnsTypeCode.kakao -> {
                        switch_sync_kakao.setSafeCheck(true, SafeSwitchCompat.IGNORE)
                        layout_sync_kakao_url.visibility = View.VISIBLE
                        text_sync_kakao_url.text = sns.url
                        text_sync_kakao_url_modify.setOnClickListener {
                            val intent = Intent(this, SnsUrlActivity::class.java)
                            intent.putExtra(Const.DATA, sns.url)
                            intent.putExtra(Const.KEY, SnsTypeCode.kakao.name)
                            startActivityForResult(intent, Const.REQ_SYNC_SNS)
                        }
                    }
                    SnsTypeCode.blog -> {
                        switch_sync_blog.setSafeCheck(true, SafeSwitchCompat.IGNORE)
                        layout_sync_blog_url.visibility = View.VISIBLE
                        text_sync_blog_url.text = sns.url
                        text_sync_blog_url_modify.setOnClickListener {
                            val intent = Intent(this, SnsUrlActivity::class.java)
                            intent.putExtra(Const.DATA, sns.url)
                            intent.putExtra(Const.KEY, SnsTypeCode.blog.name)
                            startActivityForResult(intent, Const.REQ_SYNC_SNS)
                        }
                    }
                    SnsTypeCode.youtube -> {
                        switch_sync_youtube.setSafeCheck(true, SafeSwitchCompat.IGNORE)
                        layout_sync_youtube_url.visibility = View.VISIBLE
                        text_sync_youtube_url.text = sns.url
                        text_sync_youtube_url_modify.setOnClickListener {
                            val intent = Intent(this, SnsUrlActivity::class.java)
                            intent.putExtra(Const.DATA, sns.url)
                            intent.putExtra(Const.KEY, SnsTypeCode.youtube.name)
                            startActivityForResult(intent, Const.REQ_SYNC_SNS)
                        }
                    }
                    SnsTypeCode.kakaochannel -> {
                        switch_sync_kakaochannel.setSafeCheck(true, SafeSwitchCompat.IGNORE)
                        layout_sync_kakaochannel_url.visibility = View.VISIBLE
                        text_sync_kakaochannel_url.text = sns.url
                        text_sync_kakaochannel_url_modify.setOnClickListener {
                            val intent = Intent(this, SnsUrlActivity::class.java)
                            intent.putExtra(Const.DATA, sns.url)
                            intent.putExtra(Const.KEY, SnsTypeCode.kakaochannel.name)
                            startActivityForResult(intent, Const.REQ_SYNC_SNS)
                        }
                    }
                    SnsTypeCode.homepage -> {
                        switch_sync_homepage.setSafeCheck(true, SafeSwitchCompat.IGNORE)
                        layout_sync_homepage_url.visibility = View.VISIBLE
                        text_sync_homepage_url.text = sns.url
                        text_sync_homepage_url_modify.setOnClickListener {
                            val intent = Intent(this, SnsUrlActivity::class.java)
                            intent.putExtra(Const.DATA, sns.url)
                            intent.putExtra(Const.KEY, SnsTypeCode.homepage.name)
                            startActivityForResult(intent, Const.REQ_SYNC_SNS)
                        }
                    }
                }
            }

        }
    }

    //set sns
    private fun updatePageSns(type: String, url: String, linkage: Boolean) {

        val params = Sns()
        params.page = No(LoginInfoManager.getInstance().user.page!!.no)
        params.isLinkage = linkage
        params.type = type
        params.url = url
        showProgress("")
        ApiBuilder.create().saveSnsLink(params).setCallback(object : PplusCallback<NewResultResponse<Sns>> {

            override fun onResponse(call: Call<NewResultResponse<Sns>>, response: NewResultResponse<Sns>) {

                hideProgress()
                if (isFinishing) {
                    return
                }

                setSns(response.data)
            }

            override fun onFailure(call: Call<NewResultResponse<Sns>>, t: Throwable, response: NewResultResponse<Sns>) {

                hideProgress()
            }
        }).build().call()
    }

    //delete sns
    private fun deletePageSns(type: String, buttonView: CompoundButton) {

        val builder = AlertBuilder.Builder()
        builder.setTitle(getString(R.string.word_notice_alert))
        builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_delete_sns), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
        builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
        builder.setOnAlertResultListener(object : OnAlertResultListener {

            override fun onCancel() {
                (buttonView as SafeSwitchCompat).setSafeCheck(true, SafeSwitchCompat.IGNORE)
            }

            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                when (event_alert) {
                    AlertBuilder.EVENT_ALERT.RIGHT -> {
                        val params = HashMap<String, String>()
                        params["page.no"] = "" + LoginInfoManager.getInstance().user.page!!.no!!
                        params["type"] = type
                        showProgress("")
                        ApiBuilder.create().deleteSnsLinkByType(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {

                            override fun onResponse(call: Call<NewResultResponse<Any>>, response: NewResultResponse<Any>) {

                                hideProgress()
                                if (isFinishing) {
                                    return
                                }

                                val sns = Sns()
                                sns.type = type
                                sns.url = null
                                setSns(sns)
                            }

                            override fun onFailure(call: Call<NewResultResponse<Any>>, t: Throwable, response: NewResultResponse<Any>) {

                                hideProgress()
                            }
                        }).build().call()
                    }
                    else->{
                        (buttonView as SafeSwitchCompat).setSafeCheck(true, SafeSwitchCompat.IGNORE)
                    }
                }
            }
        }).builder().show(this)

    }

//    override fun getToolbarOption(): ToolbarOption {
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_keyword_reg_sns_sync), ToolbarOption.ToolbarMenu.LEFT)
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
}
