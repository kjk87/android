package com.pplus.prnumberbiz.apps.nfc.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import com.pple.pplus.utils.part.apps.resource.ResourceUtil
import com.pple.pplus.utils.part.format.FormatUtil
import com.pple.pplus.utils.part.logs.LogUtil
import com.pple.pplus.utils.part.utils.NumberUtils
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Buy
import com.pplus.prnumberbiz.core.network.model.dto.Lpng
import com.pplus.prnumberbiz.core.network.model.dto.LpngRes
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import kotlinx.android.synthetic.main.activity_nfcpay.*
import network.common.PplusCallback
import retrofit2.Call


class NFCPayActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_nfcpay
    }

    var mPrice = 0
    var mInstallment = ""

    override fun initializeView(savedInstanceState: Bundle?) {

        edit_nfcpay.setTextIsSelectable(true)
        edit_nfcpay.setSingleLine()

        val ic: InputConnection = edit_nfcpay.onCreateInputConnection(EditorInfo())
        dial_nfcpay.setInputConnection(ic)

        edit_nfcpay.addTextChangedListener(textWatcher)

        text_nfcpay_request.setOnClickListener {
            mPrice = edit_nfcpay.text.toString().replace(",", "").toInt()
            if (mPrice > 0) {

                if(mPrice < 1000){
                    showAlert(R.string.msg_enable_pg_over_1000)
                    return@setOnClickListener
                }

                if (mPrice >= 50000) {
                    val intent = Intent(this, NFCInstallmentActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivityForResult(intent, Const.REQ_INSTALLMENT)
                } else {

                    getOrderId()
                }

            }
        }

    }

    private fun getOrderId() {
        showProgress("")
        ApiBuilder.create().lpngOrderId.setCallback(object : PplusCallback<NewResultResponse<String>> {
            override fun onResponse(call: Call<NewResultResponse<String>>?, response: NewResultResponse<String>?) {

                hideProgress()
                if (response != null) {
                    buy(response.data)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<String>>?, t: Throwable?, response: NewResultResponse<String>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun buy(orderId: String) {
        val buy = Buy()
        buy.orderId = orderId
        buy.pg = "NFC"
        buy.title = getString(R.string.word_app_card_pay)
        buy.price = mPrice.toLong()
        buy.orderType = EnumData.OrderType.nfc.type
        buy.pageSeqNo = LoginInfoManager.getInstance().user.page!!.no
        buy.buyerName = getString(R.string.word_app_card_pay)
        buy.buyerTel = LoginInfoManager.getInstance().user.mobile
        showProgress("")
        ApiBuilder.create().postBuyBiz(buy).setCallback(object : PplusCallback<NewResultResponse<Buy>> {
            override fun onResponse(call: Call<NewResultResponse<Buy>>?, response: NewResultResponse<Buy>?) {
                hideProgress()
                if (response != null && response.data != null) {

                    if (response.data.seqNo != null) {
                        lpngPay(orderId)
                        return
                    }
                }
                showAlert(R.string.msg_cancel_pg)
            }

            override fun onFailure(call: Call<NewResultResponse<Buy>>?, t: Throwable?, response: NewResultResponse<Buy>?) {
                hideProgress()
                showAlert(R.string.msg_cancel_pg)
            }
        }).build().call()
    }

    private fun lpngPay(orderId: String) {
        val lpng = Lpng()
        lpng.shopcode = LoginInfoManager.getInstance().user.page!!.shopCode
        lpng.SERVICECODE = "LPNG"
        lpng.order_req_amt = mPrice.toString()
        lpng.comp_orderno = orderId
        lpng.order_goodsname = getString(R.string.word_app_card_pay)
        lpng.order_name = getString(R.string.word_app_card_pay)
        lpng.order_hp = LoginInfoManager.getInstance().user.mobile
        if (StringUtils.isNotEmpty(mInstallment)) {
            lpng.req_install = mInstallment
        }
        showProgress("")
        ApiBuilder.create().postBuyLpngTag(lpng).setCallback(object : PplusCallback<NewResultResponse<LpngRes>> {
            override fun onResponse(call: Call<NewResultResponse<LpngRes>>?, response: NewResultResponse<LpngRes>?) {
                hideProgress()
                if (response != null) {
                    val lpngRes = response.data
                    if (lpngRes.orderstatus == "10") {
                        lpngRes.order_req_amt = mPrice.toString()
                        val intent = Intent(this@NFCPayActivity, NFCPayWaitActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        intent.putExtra(Const.LPNG, lpngRes)
                        startActivityForResult(intent, Const.REQ_PAY)
                    }
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

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

            if (s!!.isNotEmpty()) {
                text_nfcpay_hint.visibility = View.GONE
                text_nfcpay_money_unit.visibility = View.VISIBLE
                text_nfcpay_request.setBackgroundColor(ResourceUtil.getColor(this@NFCPayActivity, R.color.color_579ffb))
            } else {
                text_nfcpay_hint.visibility = View.VISIBLE
                text_nfcpay_money_unit.visibility = View.GONE
                text_nfcpay_request.setBackgroundColor(ResourceUtil.getColor(this@NFCPayActivity, R.color.color_b7b7b7))
            }

            edit_nfcpay.removeTextChangedListener(this)
            val cursorPos = edit_nfcpay.selectionStart
            val beforeLength = edit_nfcpay.length()
            edit_nfcpay.setText(FormatUtil.getMoneyType(s.toString().replace(",", "")))
            val afterLength = edit_nfcpay.length()
            LogUtil.e(LOG_TAG, "afterLength : {} beforeLength : {}",afterLength, beforeLength )

            if(NumberUtils.isNumber( edit_nfcpay.text.toString()) && edit_nfcpay.text.toString().toInt() == 0){
                edit_nfcpay.setSelection(1)
            }else{
                if (afterLength > beforeLength) {
                    edit_nfcpay.setSelection(cursorPos + 1)
                } else if (afterLength < beforeLength) {
                    edit_nfcpay.setSelection(cursorPos - 1)
                } else {
                    edit_nfcpay.setSelection(cursorPos)
                }
            }


            edit_nfcpay.addTextChangedListener(this)

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Const.REQ_INSTALLMENT -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        mInstallment = data.getStringExtra(Const.INSTALLMENT)
                        getOrderId()
                    }

                }
            }
            Const.REQ_PAY->{
                if(resultCode == Activity.RESULT_OK){
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        }
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.msg_pay_nfc), ToolbarOption.ToolbarMenu.RIGHT)

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
