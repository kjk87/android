package com.pplus.prnumberbiz.apps.pages.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.URLUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Page
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import com.pplus.prnumberbiz.core.util.ToastUtil
import kotlinx.android.synthetic.main.activity_main_youtube.*
import network.common.PplusCallback
import retrofit2.Call

class MainYoutubeActivity : BaseActivity(), ImplToolbar {

    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_main_youtube
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        val page = LoginInfoManager.getInstance().user.page

        if (StringUtils.isNotEmpty(page!!.mainMovieUrl)) {
            edit_main_youtube.setText(page.mainMovieUrl)
        }
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_sync_homepage), ToolbarOption.ToolbarMenu.LEFT)
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
                    val url = edit_main_youtube.text.toString().trim()

                    if(StringUtils.isNotEmpty(url)){
                        if (PplusCommonUtil.getYoutubeVideoId(url) == null) {
                            showAlert(R.string.msg_input_correct_youtube)
                            return@OnToolbarListener
                        }

                    }

                    val page = LoginInfoManager.getInstance().user.page
                    page!!.mainMovieUrl = url

                    showProgress("")
                    ApiBuilder.create().updatePage(page).setCallback(object : PplusCallback<NewResultResponse<Page>> {
                        override fun onResponse(call: Call<NewResultResponse<Page>>?, response: NewResultResponse<Page>?) {
                            hideProgress()
                            LoginInfoManager.getInstance().user.page = response!!.data
                            LoginInfoManager.getInstance().save()

                            showAlert(R.string.msg_saved)
                            setResult(Activity.RESULT_OK)
                            finish()
                        }

                        override fun onFailure(call: Call<NewResultResponse<Page>>?, t: Throwable?, response: NewResultResponse<Page>?) {
                            hideProgress()
                        }
                    }).build().call()
                }
            }
        }

    }
}
