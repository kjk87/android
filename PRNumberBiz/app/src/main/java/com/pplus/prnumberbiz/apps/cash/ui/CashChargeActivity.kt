package com.pplus.prnumberbiz.apps.cash.ui

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import com.pple.pplus.utils.part.format.FormatUtil
import com.pple.pplus.utils.part.logs.LogUtil
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.billing.data.BillingData
import com.pplus.prnumberbiz.apps.billing.data.CashBillingAdapter
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.mgmt.SystemBoardManager
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Buy
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import com.pplus.prnumberbiz.core.util.ToastUtil
import kotlinx.android.synthetic.main.activity_cash_billing.*
import kotlinx.android.synthetic.main.layout_cash_terms.view.*
import kr.co.bootpay.*
import kr.co.bootpay.enums.Method
import kr.co.bootpay.enums.PG
import kr.co.bootpay.listener.*
import kr.co.bootpay.model.BootExtra
import kr.co.bootpay.model.BootUser
import network.common.PplusCallback
import retrofit2.Call
import java.util.*

class CashChargeActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_cash_billing
    }

    private var mAdapter: CashBillingAdapter? = null
    var mBuy = Buy()

    override fun initializeView(savedInstanceState: Bundle?) {
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {

                text_cash_billing_retention_cash.text = FormatUtil.getMoneyType(LoginInfoManager.getInstance().user.totalCash.toString())
            }
        })

        recycler_cash_billing.layoutManager = LinearLayoutManager(this)
        mAdapter = CashBillingAdapter(this)
        recycler_cash_billing.adapter = mAdapter

        val billingDataList = ArrayList<BillingData>()
        billingDataList.add(BillingData("5000", "cash_5000"))
        billingDataList.add(BillingData("10000", "cash_10000"))
        billingDataList.add(BillingData("20000", "cash_20000"))
        billingDataList.add(BillingData("30000", "cash_30000"))
        billingDataList.add(BillingData("50000", "cash_50000"))
        billingDataList.add(BillingData("100000", "cash_100000"))
        mAdapter!!.setDataList(billingDataList)
        mAdapter!!.setSelectData(billingDataList[2])

        mAdapter!!.setOnClickListener { position -> mAdapter!!.selectData = billingDataList[position] }

        if (SystemBoardManager.getInstance().board != null) {
            val list = SystemBoardManager.getInstance().board.cashList

            if (list != null) {
                for (i in list.indices) {
                    val viewTerms = layoutInflater.inflate(R.layout.layout_cash_terms, LinearLayout(this))
                    val title = viewTerms.text_cash_terms_title
                    val contents = viewTerms.text_cash_terms_contents
                    title.text = list[i].subject
                    contents.text = list[i].contents
                    title.setOnClickListener {
                        title.isSelected = !title.isSelected
                        val animation: Animation
                        if (contents.visibility == View.VISIBLE) {
                            animation = AnimationUtils.loadAnimation(this, R.anim.scale_hide)
                            animation.setAnimationListener(object : Animation.AnimationListener {

                                override fun onAnimationStart(animation: Animation) {

                                }

                                override fun onAnimationEnd(animation: Animation) {

                                    contents.visibility = View.GONE
                                }

                                override fun onAnimationRepeat(animation: Animation) {

                                }
                            })
                        } else {
                            contents.visibility = View.VISIBLE

                            animation = AnimationUtils.loadAnimation(this, R.anim.scale_show)
                            animation.setAnimationListener(object : Animation.AnimationListener {

                                override fun onAnimationStart(animation: Animation) {

                                }

                                override fun onAnimationEnd(animation: Animation) {

                                    scroll_cash_billing.smoothScrollTo(0, scroll_cash_billing.bottom)
                                }

                                override fun onAnimationRepeat(animation: Animation) {

                                }
                            })

                        }

                        contents.startAnimation(animation)
                    }
                    layout_cash_billing_terms.addView(viewTerms)
                }
            }
        }

        // 초기설정 - 해당 프로젝트(안드로이드)의 application id 값을 설정합니다. 결제와 통계를 위해 꼭 필요합니다.
        if (Const.API_URL.startsWith("https://api")) {
            BootpayAnalytics.init(this, getString(R.string.boot_pay_id))
        } else if (Const.API_URL.startsWith("http://stage")) {
            BootpayAnalytics.init(this, getString(R.string.boot_pay_id_stage))
        } else {
            BootpayAnalytics.init(this, getString(R.string.boot_pay_id_dev))
        }

        text_cash_billing_charge.setOnClickListener {
            mBuy = Buy()
            mBuy.title = getString(R.string.word_cash_en)
            mBuy.payMethod = "card"
            mBuy.pg = "inicis"
            mBuy.buyerName = LoginInfoManager.getInstance().user.name
            mBuy.buyerTel = LoginInfoManager.getInstance().user.mobile
            mBuy.cash = true
            mBuy.price = mAdapter!!.selectData.amount.toLong()
            getOrderId()
        }
    }

    private fun getOrderId() {
        val params = HashMap<String, String>()
        showProgress("")
        ApiBuilder.create().getBuyOrderId(params).setCallback(object : PplusCallback<NewResultResponse<String>> {
            override fun onResponse(call: Call<NewResultResponse<String>>?, response: NewResultResponse<String>?) {

                hideProgress()
                if (response != null) {
                    openPg(response.data)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<String>>?, t: Throwable?, response: NewResultResponse<String>?) {
                hideProgress()
            }
        }).build().call()
    }

    var mPayMethod = "card"
    private fun openPg(orderId: String) {
        val builder = Bootpay.init(this)
        if (Const.API_URL.startsWith("https://api")) {
            builder.setApplicationId(getString(R.string.boot_pay_id)) // 해당 프로젝트(안드로이드)의 application id 값
        } else if (Const.API_URL.startsWith("http://stage")) {
            builder.setApplicationId(getString(R.string.boot_pay_id_stage)) // 해당 프로젝트(안드로이드)의 application id 값
        } else {
            builder.setApplicationId(getString(R.string.boot_pay_id_dev)) // 해당 프로젝트(안드로이드)의 application id 값
        }

        val bootUser = BootUser()
        bootUser.username = mBuy.buyerName
        bootUser.phone = mBuy.buyerTel
        val bootExtra = BootExtra().setQuotas(intArrayOf(0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12))

        builder.setBootUser(bootUser)
                .setMethod(Method.CARD) // 결제수단
                .setContext(this)
                .setMethod(mPayMethod) // 결제수단
                .setName(getString(R.string.word_cash_en)) // 결제할 상품명
                .setOrderId(orderId) //고유 주문번호로, 생성하신 값을 보내주셔야 합니다.
                //.setAccountExpireAt("2018-09-22") // 가상계좌 입금기간 제한 ( yyyy-mm-dd 포멧으로 입력해주세요. 가상계좌만 적용됩니다. 오늘 날짜보다 더 뒤(미래)여야 합니다 )
        builder.addItem(getString(R.string.word_cash_en), 1, mAdapter!!.selectData.id, mAdapter!!.selectData.amount.toInt()) // 주문정보에 담길 상품정보, 통계를 위해 사용
        builder.setPrice(mBuy.price!!.toInt())

        builder.onConfirm(object : ConfirmListener { // 결제가 진행되기 바로 직전 호출되는 함수로, 주로 재고처리 등의 로직이 수행
            override fun onConfirm(message: String?) {
                mBuy.orderId = orderId
                ApiBuilder.create().postBuyCash(mBuy).setCallback(object : PplusCallback<NewResultResponse<Buy>> {
                    override fun onResponse(call: Call<NewResultResponse<Buy>>?, response: NewResultResponse<Buy>?) {
                        hideProgress()
                        if (response != null && response.data != null) {

                            if (response.data.seqNo != null) {
                                Bootpay.confirm(message)
                                return
                            }
                        }
                        showAlert(R.string.msg_cancel_pg)
                        Bootpay.removePaymentWindow()
                    }

                    override fun onFailure(call: Call<NewResultResponse<Buy>>?, t: Throwable?, response: NewResultResponse<Buy>?) {
                        hideProgress()
                        showAlert(R.string.msg_cancel_pg)
                        Bootpay.removePaymentWindow()
                    }
                }).build().call()

                LogUtil.e(LOG_TAG, "confirm : {}", message)
            }
        }).onDone(object : DoneListener { // 결제완료시 호출, 아이템 지급 등 데이터 동기화 로직을 수행합니다
            override fun onDone(message: String?) {
                LogUtil.e(LOG_TAG, "done : {}", message)
                getBuy(orderId)

            }
        }).onReady(object : ReadyListener {
            override fun onReady(message: String?) {
                LogUtil.e(LOG_TAG, "ready : {}", message)
            }
        }).onCancel(object : CancelListener {
            override fun onCancel(message: String?) {
                LogUtil.e(LOG_TAG, "cancel : {}", message)
                showAlert(R.string.msg_cancel_pg)
//                deleteBuy(buy.seqNo.toString())
            }
        }).onError(object : ErrorListener {
            override fun onError(message: String?) {
                LogUtil.e(LOG_TAG, "error : {}", message)
                showAlert(R.string.msg_error_pg)
//                deleteBuy(buy.seqNo.toString())
            }
        }).onClose(object : CloseListener {
            override fun onClose(message: String?) {
                LogUtil.e(LOG_TAG, "close : {}", message)
            }
        }).request()
    }

    var retryCount = 1
    private fun getBuy(orderId: String) {
        if(retryCount > 5){
            hideProgress()
            return
        }

        showProgress("")
        Thread.sleep(1000)
        val params = HashMap<String, String>()
        params["orderId"] = orderId
        ApiBuilder.create().getOneBuy(params).setCallback(object : PplusCallback<NewResultResponse<Buy>> {
            override fun onResponse(call: Call<NewResultResponse<Buy>>?, response: NewResultResponse<Buy>?) {

                if(response != null && response.data != null && response.data.process != null){
                    if(response.data.process!! > 0){
                        hideProgress()
                        retryCount = 1

                        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

                            override fun reload() {

                                ToastUtil.show(this@CashChargeActivity, R.string.msg_charged_cash)
                                text_cash_billing_retention_cash.text = FormatUtil.getMoneyType(LoginInfoManager.getInstance().user.totalCash.toString())
                            }
                        })

                    }else if(response.data.process!! < 0){
                        hideProgress()
                        showAlert(R.string.msg_error_pg)
                    }else{
                        retryCount++
                        getBuy(orderId)
                    }
                }else{
                    hideProgress()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Buy>>?, t: Throwable?, response: NewResultResponse<Buy>?) {
                hideProgress()
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.msg_charge), ToolbarOption.ToolbarMenu.RIGHT)

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
