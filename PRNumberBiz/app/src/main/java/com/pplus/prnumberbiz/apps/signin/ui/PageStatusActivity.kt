package com.pplus.prnumberbiz.apps.signin.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.goods.ui.SellerApplyActivity
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import kotlinx.android.synthetic.main.activity_page_status.*

class PageStatusActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_page_status
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val page = LoginInfoManager.getInstance().user.page!!
        if (page.status == EnumData.PageStatus.pending.name || page.status == EnumData.PageStatus.redemand.name) { //승인대기
            text_page_status_re_apply.visibility = View.GONE
            layout_page_status_reject.visibility = View.GONE
            layout_page_status_pending.visibility = View.VISIBLE
        } else if (page.status == "return") {//승인거절
            text_page_status_re_apply.visibility = View.VISIBLE
            layout_page_status_reject.visibility = View.VISIBLE
            layout_page_status_pending.visibility = View.GONE

            text_page_status_reason.text = page.reason
            text_page_status_re_apply.setOnClickListener {
                val intent = Intent(this, SellerApplyActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivityForResult(intent, Const.REQ_APPLY)
            }
        }

        text_page_status_log_out.setOnClickListener {
            val builder = AlertBuilder.Builder()
            builder.setTitle(getString(R.string.word_notice_alert))
            builder.addContents(AlertData.MessageData(getString(R.string.msg_question_logout), AlertBuilder.MESSAGE_TYPE.TEXT, 1))
            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
            builder.setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                    when (event_alert) {
                        AlertBuilder.EVENT_ALERT.RIGHT -> PplusCommonUtil.logOutAndRestart()
                    }
                }
            }).builder().show(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Const.REQ_APPLY -> {
                if (resultCode == Activity.RESULT_OK) {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        }
    }
}
