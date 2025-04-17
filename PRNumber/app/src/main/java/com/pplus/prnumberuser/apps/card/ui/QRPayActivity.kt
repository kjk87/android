package com.pplus.prnumberuser.apps.card.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.CaptureManager
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.alert.AlertInputPriceActivity
import com.pplus.prnumberuser.apps.card.data.CardSelectAdapter
import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberuser.apps.common.builder.data.AlertData
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.*
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivityQrPayBinding
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*

class QRPayActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityQrPayBinding

    override fun getLayoutView(): View {
        binding = ActivityQrPayBinding.inflate(layoutInflater)
        return binding.root
    }

    private var mBuy: Buy? = null
    private var mShopCode: String? = null
    private var mType = ""
    private var mRoomId = ""

    private val callback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult?) {
            if (result?.text == null) {
                return
            }
            mReadText = result.text
            LogUtil.e(LOG_TAG, mReadText)
            if (mReadText.startsWith("pplus://")) {
                val parameter = mReadText.split("?")[1]
                val params = parameter.split("&").toTypedArray() //pplus://qr?title={title}&price={price}&pageSeqNo={pageSeqNo}&shopCode={shopCode}&installment={installment}&type={type}&roomId={roomId}

                mBuy = Buy()
                mType = ""
                mRoomId = ""
                for (paramValue in params) {
                    val keyValue = paramValue.split("=").toTypedArray()
                    when (keyValue[0]) {
                        "title" -> {
                            mBuy!!.title = keyValue[1]
                        }
                        "price" -> {
                            mBuy!!.price = keyValue[1].toLong()
                        }
                        "pageSeqNo" -> {
                            mBuy!!.pageSeqNo = keyValue[1].toLong()
                        }
                        "shopCode" -> {
                            mShopCode = keyValue[1]
                        }
                        "installment" -> {
                            mBuy!!.installment = keyValue[1]
                        }
                        "type" -> {
                            mType = keyValue[1]
                        }
                        "roomId" -> {
                            mRoomId = keyValue[1]
                        }
                    }
                }

                val intent = Intent(this@QRPayActivity, AlertInputPriceActivity::class.java)
                intent.putExtra(Const.PRICE, mBuy!!.price)
                intent.putExtra(Const.INSTALLMENT, mBuy!!.installment)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                priceAlertLauncher.launch(intent)
            } else if (mReadText.startsWith("https://www.plusmember.co.kr")) {
                val parameter = mReadText.split("?")[1]
                val params = parameter.split("&").toTypedArray()
                for (paramValue in params) {
                    val keyValue = paramValue.split("=").toTypedArray()
                    when (keyValue[0]) {
                        "pageSeqNo" -> {
                            val params = HashMap<String, String>()
                            params["no"] = keyValue[1]
                            showProgress("")
                            ApiBuilder.create().getPage(params).setCallback(object : PplusCallback<NewResultResponse<Page>> {
                                override fun onResponse(call: Call<NewResultResponse<Page>>?, response: NewResultResponse<Page>?) {
                                    hideProgress()
                                    if (response?.data != null) {
                                        val intent = Intent(this@QRPayActivity, InputPriceActivity::class.java)
                                        intent.putExtra(Const.PAGE, response.data)
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                        inputPriceLauncher.launch(intent)
                                    }

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

        override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) {
        }
    }

    private var mReadText = ""
    private var capture: CaptureManager? = null
    private var mAdapter: CardSelectAdapter? = null

    override fun initializeView(savedInstanceState: Bundle?) {

        mAdapter = CardSelectAdapter()
        binding.recyclerQrPay.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerQrPay.adapter = mAdapter
        listCall()

        capture = CaptureManager(this, binding.qrScanner)
        capture!!.initializeFromIntent(intent, savedInstanceState)
        capture!!.setShowMissingCameraPermissionDialog(false) //        qr_scanner.decodeSingle(callback)
        binding.qrScanner.decodeContinuous(callback)

        onNewIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val scheme = intent!!.getStringExtra(Const.SCHEMA)
        if (StringUtils.isNotEmpty(scheme)) {

            mReadText = scheme!!
            LogUtil.e(LOG_TAG, mReadText)
            val parameter = mReadText.split("?")[1]
            val params = parameter.split("&").toTypedArray()
            if (mReadText.startsWith("pplus://")) { //pplus://qr?title={title}&price={price}&pageSeqNo={pageSeqNo}&shopCode={shopCode}&installment={installment}&type={type}&roomId={roomId}

                mBuy = Buy()
                mType = ""
                mRoomId = ""
                for (paramValue in params) {
                    val keyValue = paramValue.split("=").toTypedArray()
                    when (keyValue[0]) {
                        "title" -> {
                            mBuy!!.title = keyValue[1]
                        }
                        "price" -> {
                            mBuy!!.price = keyValue[1].toLong()
                        }
                        "pageSeqNo" -> {
                            mBuy!!.pageSeqNo = keyValue[1].toLong()
                        }
                        "shopCode" -> {
                            mShopCode = keyValue[1]
                        }
                        "installment" -> {
                            mBuy!!.installment = keyValue[1]
                        }
                        "type" -> {
                            mType = keyValue[1]
                        }
                        "roomId" -> {
                            mRoomId = keyValue[1]
                        }
                    }
                }

                val intent = Intent(this@QRPayActivity, PayPasswordCheckActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                checkPasswordLauncher.launch(intent)
            }
        }
    }

    private fun listCall() {
        showProgress("")
        ApiBuilder.create().cardList.setCallback(object : PplusCallback<NewResultResponse<Card>> {
            override fun onResponse(call: Call<NewResultResponse<Card>>?, response: NewResultResponse<Card>?) {
                hideProgress()
                if (response?.datas != null && response.datas.isNotEmpty()) {
                    mAdapter!!.mSelectData = response.datas[0]
                    mAdapter!!.setDataList(response.datas)
                    mAdapter!!.add(Card())
                } else {
                    val builder = AlertBuilder.Builder()
                    builder.setTitle(getString(R.string.word_notice_alert))
                    builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_not_exist_card), AlertBuilder.MESSAGE_TYPE.TEXT, 4))
                    builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
                    builder.setOnAlertResultListener(object : OnAlertResultListener {

                        override fun onCancel() {
                        }

                        override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                            when (event_alert) {
                                AlertBuilder.EVENT_ALERT.RIGHT -> {
                                    val intent = Intent(this@QRPayActivity, CardRegActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    cardRegLauncher.launch(intent)
                                }
                            }

                        }
                    }).builder().show(this@QRPayActivity)
                    finish()
                }

            }

            override fun onFailure(call: Call<NewResultResponse<Card>>?, t: Throwable?, response: NewResultResponse<Card>?) {
                hideProgress()
            }
        }).build().call()
    }

    val priceAlertLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) { // There are no request codes
            val data = result.data
            val intent = Intent(this@QRPayActivity, PayPasswordCheckActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            checkPasswordLauncher.launch(intent)
        }
    }

    val cardRegLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            listCall()
        }
    }

    val qrPayLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        setResult(Activity.RESULT_OK)
        finish()
    }

    val inputPriceLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) { // There are no request codes
            val data = result.data
            if (data != null) {
                mBuy = Buy()
                val page = data.getParcelableExtra<Page>(Const.PAGE)
                val price = data.getIntExtra(Const.PRICE, 0)
                val installment = data.getStringExtra(Const.INSTALLMENT)
                mShopCode = page!!.shopCode
                mBuy!!.pageSeqNo = page.no
                mBuy!!.title = "${page.name} ${getString(R.string.word_reprsent_goods)}"
                mBuy!!.price = price.toLong()
                mBuy!!.installment = installment

                val intent = Intent(this@QRPayActivity, PayPasswordCheckActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                checkPasswordLauncher.launch(intent)
            }
        }
    }

    val checkPasswordLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            if (mBuy != null) {
                mBuy!!.memberSeqNo = LoginInfoManager.getInstance().user.no
                mBuy!!.pg = "DAOU"
                mBuy!!.buyerName = LoginInfoManager.getInstance().user.name
                mBuy!!.buyerTel = LoginInfoManager.getInstance().user.mobile

                getOrderId()
            }
        }
    }

    private fun getOrderId() {
        showProgress("")
        ApiBuilder.create().buyOrderId.setCallback(object : PplusCallback<NewResultResponse<String>> {
            override fun onResponse(call: Call<NewResultResponse<String>>?, response: NewResultResponse<String>?) {

                hideProgress()
                if (response != null) { //                    val regex = Regex("^[0-9]{22}\\z")
                    val orderId = response.data
                    buy(orderId)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<String>>?, t: Throwable?, response: NewResultResponse<String>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun buy(orderId: String) {
        mBuy!!.orderId = orderId
        showProgress("")
        ApiBuilder.create().postBuyQr(mBuy!!).setCallback(object : PplusCallback<NewResultResponse<Buy>> {
            override fun onResponse(call: Call<NewResultResponse<Buy>>?, response: NewResultResponse<Buy>?) {
                hideProgress()
                if (response?.data != null) {
                    ftLinkPay()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Buy>>?, t: Throwable?, response: NewResultResponse<Buy>?) {
                hideProgress()
                showAlert(R.string.msg_cancel_pg)
            }
        }).build().call()
    }

    private fun ftLinkPay() {
        val params = FTLink()
        params.shopcode = mShopCode //        params.loginId = LoginInfoManager.getInstance().user.loginId
        params.order_req_amt = mBuy!!.price.toString()
        params.order_hp = mBuy!!.buyerTel
        params.order_name = mBuy!!.buyerName
        params.order_goodsname = mBuy!!.title
        params.req_installment = mBuy!!.installment
        params.comp_memno = LoginInfoManager.getInstance().user.no.toString()
        params.autokey = mAdapter!!.mSelectData!!.autoKey
        params.req_cardcode = mAdapter!!.mSelectData!!.cardCode
        params.comp_orderno = mBuy!!.orderId

        if (StringUtils.isNotEmpty(mType) && StringUtils.isNotEmpty(mRoomId)) {
            params.serverType = mType
            params.roomId = mRoomId
        }

        showProgress("")
        ApiBuilder.create().postBuyFTLinkPay(params).setCallback(object : PplusCallback<NewResultResponse<FTLink>> {
            override fun onResponse(call: Call<NewResultResponse<FTLink>>?, response: NewResultResponse<FTLink>?) {
                hideProgress()
                if (response?.data != null) {
                    orderComplete(response.data)
                }

            }

            override fun onFailure(call: Call<NewResultResponse<FTLink>>?, t: Throwable?, response: NewResultResponse<FTLink>?) {
                hideProgress()
                if (response != null && response.data != null && StringUtils.isNotEmpty(response.data.errMessage)) {
                    showAlert(response.data.errMessage)
                }
            }
        }).build().call()
    }

    private fun orderComplete(ftLink: FTLink) {
        val intent = Intent(this, AlertQRPayCompleteActivity::class.java)
        intent.putExtra(Const.DATA, ftLink)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        qrPayLauncher.launch(intent)
    }

    override fun onResume() {
        super.onResume()
        capture!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        capture!!.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        capture!!.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        capture!!.onSaveInstanceState(outState)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        capture!!.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_qr_pay), ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressed()
                    }
                }
            }
        }
    }

}
