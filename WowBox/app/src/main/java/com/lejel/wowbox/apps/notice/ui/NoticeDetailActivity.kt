package com.lejel.wowbox.apps.notice.ui

import android.os.Bundle
import android.view.View
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.utils.time.DateFormatUtils
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.Notice
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityNoticeDetailBinding
import retrofit2.Call
import java.text.SimpleDateFormat

class NoticeDetailActivity : BaseActivity(), ImplToolbar {
    private lateinit var binding: ActivityNoticeDetailBinding

    override fun getLayoutView(): View {
        binding = ActivityNoticeDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        var notice = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, Notice::class.java)!!

        showProgress("")
        ApiBuilder.create().getNotice(notice.seqNo!!).setCallback(object : PplusCallback<NewResultResponse<Notice>> {
            override fun onResponse(call: Call<NewResultResponse<Notice>>?, response: NewResultResponse<Notice>?) {
                hideProgress()
                if (response?.result != null) {
                    notice = response.result!!
                    binding.textNoticeTitle.text = notice.title

                    val format = SimpleDateFormat(getString(R.string.word_date_format2))
                    binding.textNoticeDate.text = format.format(PplusCommonUtil.setTimeZoneOffset(DateFormatUtils.PPLUS_DATE_FORMAT.parse(notice.regDatetime)))
                    binding.webviewNoticeDetail.loadDataWithBaseURL(null, notice.contents!!, "text/html; charset=utf-8", "utf-8", null)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Notice>>?, t: Throwable?, response: NewResultResponse<Notice>?) {
                hideProgress()
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_notice), ToolbarOption.ToolbarMenu.LEFT)
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