package com.pplus.prnumberbiz.apps.pages.ui


import android.content.Intent
import android.os.Bundle
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.pages.data.TemplatePagerAdapter
import com.pplus.prnumberbiz.core.code.custom.AttachmentTargetTypeCode
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Attachment
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import kotlinx.android.synthetic.main.activity_template.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.*

class TemplateActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_template
    }

    private var mAdapter: TemplatePagerAdapter? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        mAdapter = TemplatePagerAdapter(this)
        pager_template.adapter = mAdapter
        pager_template.clipToPadding = false
        pager_template.setPadding(resources.getDimensionPixelSize(R.dimen.width_180), 0, resources.getDimensionPixelSize(R.dimen.width_180), 0)

        getDefaultImage()
    }

    private fun getDefaultImage() {

        val params = HashMap<String, String>()
        params["targetType"] = AttachmentTargetTypeCode.pageBackground.name
        showProgress("")
        ApiBuilder.create().getDefaultImageList(params).setCallback(object : PplusCallback<NewResultResponse<Attachment>> {

            override fun onResponse(call: Call<NewResultResponse<Attachment>>, response: NewResultResponse<Attachment>) {

                hideProgress()
                mAdapter!!.dataList = response.datas
            }

            override fun onFailure(call: Call<NewResultResponse<Attachment>>, t: Throwable, response: NewResultResponse<Attachment>) {

                hideProgress()
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_default_picture), ToolbarOption.ToolbarMenu.LEFT)
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
                    val attachment = mAdapter!!.selectData
                    if (attachment == null) {
                        showAlert(R.string.msg_select_image)
                        return@OnToolbarListener
                    }
                    val data = Intent()
                    data.putExtra(Const.DATA, attachment)
                    setResult(RESULT_OK, data)
                    finish()
                }
            }
        }

    }

}
