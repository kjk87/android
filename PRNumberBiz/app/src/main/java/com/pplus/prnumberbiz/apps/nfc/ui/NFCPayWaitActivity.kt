package com.pplus.prnumberbiz.apps.nfc.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.pple.pplus.utils.part.format.FormatUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Lpng
import com.pplus.prnumberbiz.core.network.model.dto.LpngRes
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import kotlinx.android.synthetic.main.activity_nfcpay_wait.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.HashMap

class NFCPayWaitActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_nfcpay_wait
    }

    var mLpngRes:LpngRes? = null
    var mIsCheck = false

    override fun initializeView(savedInstanceState: Bundle?) {
        mLpngRes = intent.getParcelableExtra<LpngRes>(Const.LPNG)

        text_nfcpay_wait_price.text = PplusCommonUtil.fromHtml(getString(R.string.html_money_unit, FormatUtil.getMoneyType(mLpngRes?.order_req_amt)))

        text_nfcpay_wait_cancel.setOnClickListener {
            cancel()
        }
        mIsCheck = false
        val handler = Handler()
        handler.postDelayed({
            if(!mIsCheck){
                lpngPayCheck()
            }

        }, 10000)

    }

    private fun cancel(){
        val builder = AlertBuilder.Builder()
        builder.setTitle(getString(R.string.word_notice_alert))
        builder.addContents(AlertData.MessageData(getString(R.string.msg_question_cancel_nfc), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
        builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
        builder.setOnAlertResultListener(object : OnAlertResultListener {

            override fun onCancel() {

            }

            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                when (event_alert) {
                    AlertBuilder.EVENT_ALERT.RIGHT -> {
                        val params = HashMap<String, String>()
                        params["orderId"] = mLpngRes?.comp_orderno!!
                        showProgress("")
                        ApiBuilder.create().deleteCancelLpngTag(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                                hideProgress()
                                mIsCheck = true
                                showAlert(R.string.msg_cancel_pg)
                                setResult(Activity.RESULT_CANCELED)
                                finish()
                            }

                            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                                hideProgress()
                                showAlert(R.string.msg_failed)
                            }
                        }).build().call()
                    }
                }
            }
        }).builder().show(this)

    }

    private fun lpngPayCheck() {

        val lpng = Lpng()
        lpng.shopcode = LoginInfoManager.getInstance().user.page!!.shopCode
        lpng.SERVICECODE = "LPNG"
        lpng.orderno = mLpngRes!!.orderno
        lpng.order_req_amt = mLpngRes!!.order_req_amt
        showProgress("")
        ApiBuilder.create().postBuyLpngCheck(lpng).setCallback(object : PplusCallback<NewResultResponse<LpngRes>> {
            override fun onResponse(call: Call<NewResultResponse<LpngRes>>?, response: NewResultResponse<LpngRes>?) {
                hideProgress()
                if (response != null) {
                    val lpngRes = response.data
                    if (lpngRes.orderstatus == "12") {
                        mIsCheck = true
                        val intent = Intent(this@NFCPayWaitActivity, AlertNFCPayCompleteActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        intent.putExtra(Const.LPNG, mLpngRes)
                        startActivityForResult(intent, Const.REQ_PAY_COMPLETE)
                    }
                }
                if(!mIsCheck){
                    val handler = Handler()
                    handler.postDelayed({
                        if(!mIsCheck){
                            lpngPayCheck()
                        }

                    }, 10000)
                }

            }

            override fun onFailure(call: Call<NewResultResponse<LpngRes>>?, t: Throwable?, response: NewResultResponse<LpngRes>?) {
                hideProgress()
                if (response != null && response.data != null && StringUtils.isNotEmpty(response.data.errormsg)) {
                    showAlert(response.data.errormsg)
                }
            }
        }).build().call()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val orderId = intent?.getStringExtra(Const.ORDER_ID)
        payComplete(orderId)
    }

    private fun payComplete(orderId:String?){
        if(StringUtils.isNotEmpty(orderId) && mLpngRes != null){
            if(mLpngRes!!.comp_orderno == orderId){
                mIsCheck = true
                val intent = Intent(this, AlertNFCPayCompleteActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                intent.putExtra(Const.LPNG, mLpngRes)
                startActivityForResult(intent, Const.REQ_PAY_COMPLETE)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mIsCheck = true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            Const.REQ_PAY_COMPLETE->{
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

    override fun onBackPressed() {
        cancel()
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_pay_wait_nfc), ToolbarOption.ToolbarMenu.RIGHT)

        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
                    onBackPressed()
                }
            }
        }
    }
}
