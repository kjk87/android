package com.pplus.prnumberuser.apps.menu.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.menu.data.OrderPurchaseMenuAdapter
import com.pplus.prnumberuser.core.code.common.EnumData
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.Cart
import com.pplus.prnumberuser.core.network.model.dto.CartOption
import com.pplus.prnumberuser.core.network.model.dto.OrderPurchase
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivityOrderPurchaseHistoryDetailBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import com.pplus.utils.part.utils.time.DateFormatUtils
import retrofit2.Call
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class OrderPurchaseHistoryDetailActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityOrderPurchaseHistoryDetailBinding

    override fun getLayoutView(): View {
        binding = ActivityOrderPurchaseHistoryDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    var mOrderPurchase: OrderPurchase? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        mOrderPurchase = intent.getParcelableExtra(Const.DATA)

        getOrderPurchase()
    }

    private fun getOrderPurchase() {
        val params = HashMap<String, String>()
        params["seqNo"] = mOrderPurchase!!.seqNo.toString()
        showProgress("")
        ApiBuilder.create().getOrderPurchase(params).setCallback(object : PplusCallback<NewResultResponse<OrderPurchase>> {
            override fun onResponse(call: Call<NewResultResponse<OrderPurchase>>?, response: NewResultResponse<OrderPurchase>?) {
                hideProgress()
                if (response?.data != null) {
                    mOrderPurchase = response.data
                    setData()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<OrderPurchase>>?, t: Throwable?, response: NewResultResponse<OrderPurchase>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun setData(){

        binding.textOrderPurchaseHistoryDetailPageName.text = mOrderPurchase!!.page!!.name

        binding.layoutOrderPurchaseHistoryDetailConfirmTime.visibility = View.GONE
        binding.layoutOrderPurchaseHistoryDetailVisit.visibility = View.GONE
        binding.layoutOrderPurchaseHistoryDetailStartEnd.visibility = View.GONE
        binding.layoutOrderPurchaseHistoryDetailStartTime.visibility = View.GONE
        binding.layoutOrderPurchaseHistoryDetailCompleteTime.visibility = View.GONE
        binding.layoutOrderPurchaseHistoryDetailDeliveryFee.visibility = View.GONE
        binding.textOrderPurchaseHistoryDetailCancel.visibility = View.GONE
        binding.textOrderPurchaseHistoryDetailReOrder.visibility = View.GONE
        binding.textOrderPurchaseHistoryDetailReview.visibility = View.GONE
        binding.layoutOrderPurchaseHistoryDetailSavePoint.visibility = View.VISIBLE
        binding.layoutOrderPurchaseHistoryDetailRemainTime.visibility = View.GONE

        val output = SimpleDateFormat("yyyy.MM.dd HH:mm")
        var d :Date
        when(mOrderPurchase!!.salesType){
            1->{//방문
                binding.textOrderPurchaseHistoryDetailSalesType.text = getString(R.string.word_visit_order)
                binding.layoutOrderPurchaseHistoryDetailVisit.visibility = View.VISIBLE
                binding.textOrderPurchaseHistoryDetailVisitCount.text = getString(R.string.format_count_unit4, mOrderPurchase!!.visitNumber.toString())
                if(mOrderPurchase!!.isVisitNow != null && mOrderPurchase!!.isVisitNow!!){
                    binding.textOrderPurchaseHistoryDetailVisitTime.text = getString(R.string.word_visit_now)
                }else{
                    if(StringUtils.isNotEmpty(mOrderPurchase!!.visitTime)){
                        d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mOrderPurchase!!.visitTime)
                        binding.textOrderPurchaseHistoryDetailVisitTime.text = output.format(d)
                    }
                }

                binding.layoutOrderPurchaseHistoryDetailAddress.visibility = View.GONE

            }
            2->{//배달
                binding.textOrderPurchaseHistoryDetailSalesType.text = getString(R.string.word_delivery_order)
                binding.layoutOrderPurchaseHistoryDetailDeliveryFee.visibility = View.VISIBLE

                var riderFee = getString(R.string.word_free_delivery_price)
                if(mOrderPurchase!!.riderFee != null && mOrderPurchase!!.riderFee!! > 0f){
                    riderFee = getString(R.string.format_money_unit, FormatUtil.getMoneyType(mOrderPurchase!!.riderFee!!.toInt().toString()))
                }

                binding.textOrderPurchaseHistoryDetailDeliveryFee.text = riderFee
                binding.layoutOrderPurchaseHistoryDetailAddress.visibility = View.VISIBLE
            }
            5->{//포장
                binding.textOrderPurchaseHistoryDetailSalesType.text = getString(R.string.word_package_order)
                binding.layoutOrderPurchaseHistoryDetailAddress.visibility = View.GONE
            }
        }

        binding.textOrderPurchaseHistoryDetailPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(mOrderPurchase!!.price!!.toInt().toString()))

        when(mOrderPurchase!!.payMethod){
            "card"->{
                binding.textOrderPurchaseHistoryDetailPayMethod.text = getString(R.string.word_credit_card)
            }
            "ftlink"->{
                binding.textOrderPurchaseHistoryDetailPayMethod.text = getString(R.string.word_easy_pay)
            }
            "outsideCard"->{
                binding.textOrderPurchaseHistoryDetailPayMethod.text = getString(R.string.word_outside_pay)
            }
            "outsideCash"->{
                binding.textOrderPurchaseHistoryDetailPayMethod.text = getString(R.string.word_outside_pay)
            }
        }

        binding.textOrderPurchaseHistoryDetailPhone.text = mOrderPurchase!!.phone

        binding.textOrderPurchaseHistoryDetailAddress.text = "${mOrderPurchase!!.address}\n${mOrderPurchase!!.addressDetail}"

        if(StringUtils.isNotEmpty(mOrderPurchase!!.memo)){
            binding.textOrderPurchaseHistoryDetailMemo.text = mOrderPurchase!!.memo
        }else{
            binding.textOrderPurchaseHistoryDetailMemo.text = getString(R.string.word_none)
        }
        binding.textOrderPurchaseHistoryDetailSavePoint.text = PplusCommonUtil.fromHtml(getString(R.string.html_will_saved_point_desc, mOrderPurchase!!.savedPoint!!.toInt().toString()))

        d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mOrderPurchase!!.regDatetime)
        binding.textOrderPurchaseHistoryDetailOrderTime.text = output.format(d)

        binding.layoutOrderPurchaseHistoryDetailCancel.visibility = View.GONE

        when (mOrderPurchase!!.status) {
            EnumData.OrderPurchaseStatus.PAY.status, EnumData.OrderPurchaseStatus.AFTER_PAY.status -> {
                binding.textOrderPurchaseHistoryDetailStatus.setText(R.string.word_before_confirm)

                binding.textOrderPurchaseHistoryDetailCancel.visibility = View.VISIBLE
                binding.textOrderPurchaseHistoryDetailCancel.setOnClickListener {
                    val intent = Intent(this, AlertCancelOrderActivity::class.java)
                    intent.putExtra(Const.DATA, mOrderPurchase!!)
                    launcher.launch(intent)
                }
            }
            EnumData.OrderPurchaseStatus.CONFIRM.status -> {
                binding.textOrderPurchaseHistoryDetailStatus.setText(R.string.word_order_confirm)

                if(StringUtils.isNotEmpty(mOrderPurchase!!.receiptDatetime)){
                    binding.layoutOrderPurchaseHistoryDetailConfirmTime.visibility = View.VISIBLE
                    d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mOrderPurchase!!.receiptDatetime)
                    binding.textOrderPurchaseHistoryDetailConfirmTime.text = output.format(d)
                }

                if(mOrderPurchase!!.salesType == 2 && mOrderPurchase!!.statusRider == 4){
                    binding.textOrderPurchaseHistoryDetailStatus.setText(R.string.word_delivery_ing)
                    binding.layoutOrderPurchaseHistoryDetailStartEnd.visibility = View.VISIBLE
                    binding.layoutOrderPurchaseHistoryDetailStartTime.visibility = View.VISIBLE
                    if(StringUtils.isNotEmpty(mOrderPurchase!!.riderStartTime)){
                        d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mOrderPurchase!!.riderStartTime)
                        binding.textOrderPurchaseHistoryDetailDeliveryStartTime.text = output.format(d)
                    }
                }

                if(mOrderPurchase!!.salesType == 2 && mOrderPurchase!!.riderTime != null && StringUtils.isNotEmpty(mOrderPurchase!!.receiptDatetime)){
                    binding.layoutOrderPurchaseHistoryDetailRemainTime.visibility = View.VISIBLE

                    val regDate = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mOrderPurchase!!.receiptDatetime)
                    val expectTime = regDate.time + (mOrderPurchase!!.riderTime!!*60*1000)
                    val currentTime = System.currentTimeMillis()
                    val remainTime = expectTime - currentTime
                    if(remainTime > 0){
                        var remainMinute = remainTime/1000/60
                        if(remainMinute == 0L){
                            remainMinute = 1
                        }
                        binding.textOrderPurchaseHistoryDetailRemainTime.text = getString(R.string.format_remain_minute, remainMinute.toString())

                    }else{
                        binding.textOrderPurchaseHistoryDetailRemainTime.text = getString(R.string.format_remain_minute, "0")
                    }
                }

            }
            EnumData.OrderPurchaseStatus.CANCEL_REQ.status -> {
                binding.textOrderPurchaseHistoryDetailStatus.setText(R.string.word_cancel_request)
                binding.layoutOrderPurchaseHistoryDetailSavePoint.visibility = View.GONE
            }
            EnumData.OrderPurchaseStatus.CANCEL_COMPLETE.status -> {
                binding.textOrderPurchaseHistoryDetailStatus.setText(R.string.word_order_cancel)
                binding.layoutOrderPurchaseHistoryDetailSavePoint.visibility = View.GONE
                binding.layoutOrderPurchaseHistoryDetailCancel.visibility = View.VISIBLE
                d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mOrderPurchase!!.cancelDatetime)
                binding.textOrderPurchaseHistoryDetailCancelDate.text = output.format(d)
                if(StringUtils.isNotEmpty(mOrderPurchase!!.cancelMemo)){
                    binding.textOrderPurchaseHistoryDetailCancelMemo.text = mOrderPurchase!!.cancelMemo
                }else{
                    binding.textOrderPurchaseHistoryDetailCancelMemo.text = getString(R.string.msg_cancel_default_message)
                }
            }
            EnumData.OrderPurchaseStatus.COMPLETE.status -> {
                binding.textOrderPurchaseHistoryDetailSavePoint.text = PplusCommonUtil.fromHtml(getString(R.string.html_saved_point_desc, mOrderPurchase!!.savedPoint!!.toInt().toString()))
                binding.layoutOrderPurchaseHistoryDetailStartEnd.visibility = View.VISIBLE
                binding.layoutOrderPurchaseHistoryDetailCompleteTime.visibility = View.VISIBLE
                d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mOrderPurchase!!.changeStatusDatetime)
                binding.textOrderPurchaseHistoryDetailCompleteTime.text = output.format(d)

                when(mOrderPurchase!!.salesType){
                    1->{//방문
                        binding.textOrderPurchaseHistoryDetailStatus.setText(R.string.word_visit_complete)
                        binding.textOrderPurchaseHistoryDetailCompleteTimeTitle.setText(R.string.word_visit_complete_time)
                    }
                    2->{//배달
                        binding.textOrderPurchaseHistoryDetailStatus.setText(R.string.word_delivery_complete)
                        binding.textOrderPurchaseHistoryDetailCompleteTimeTitle.setText(R.string.word_delivery_complete_time)
                    }
                    5->{//포장
                        binding.textOrderPurchaseHistoryDetailStatus.setText(R.string.word_package_complete)
                        binding.textOrderPurchaseHistoryDetailCompleteTimeTitle.setText(R.string.word_package_complete_time)
                    }
                }

                binding.textOrderPurchaseHistoryDetailReOrder.visibility = View.VISIBLE

                binding.textOrderPurchaseHistoryDetailReOrder.setOnClickListener {

                    val cartList = arrayListOf<Cart>()
                    for (orderPurchaseMenu in mOrderPurchase!!.orderPurchaseMenuList!!) {
                        val cart = Cart()
                        cart.amount = orderPurchaseMenu.amount
                        cart.memberSeqNo = LoginInfoManager.getInstance().user.no
                        cart.pageSeqNo = orderPurchaseMenu.orderMenu!!.pageSeqNo
                        cart.orderMenuSeqNo = orderPurchaseMenu.orderMenu!!.seqNo
                        cart.salesType = 2
                        cart.orderMenu = orderPurchaseMenu.orderMenu
                        val list = arrayListOf<CartOption>()
                        for (orderPurchaseMenuOption in orderPurchaseMenu.orderPurchaseMenuOptionList!!) {

                            val cartOption = CartOption()
                            cartOption.menuOptionDetailSeqNo = orderPurchaseMenuOption.menuOptionDetail!!.seqNo
                            cartOption.menuOptionDetail = orderPurchaseMenuOption.menuOptionDetail
                            cartOption.type = orderPurchaseMenuOption.type
                            list.add(cartOption)
                        }

                        cart.cartOptionList = list
                        cartList.add(cart)
                    }

                    val intent = Intent(this, OrderPurchasePgActivity::class.java)
                    intent.putExtra(Const.TYPE, mOrderPurchase!!.salesType)
                    intent.putParcelableArrayListExtra(Const.DATA, cartList)
                    intent.putExtra(Const.PAGE, mOrderPurchase!!.page)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    launcher.launch(intent)
                }

                if (mOrderPurchase!!.isReviewExist != null && mOrderPurchase!!.isReviewExist!!) {
                    binding.textOrderPurchaseHistoryDetailReview.visibility = View.GONE
                } else {
                    binding.textOrderPurchaseHistoryDetailReview.visibility = View.VISIBLE
                    binding.textOrderPurchaseHistoryDetailReview.setText(R.string.word_review_write)

                    binding.textOrderPurchaseHistoryDetailReview.setOnClickListener {
                        val intent = Intent(this, OrderMenuReviewRegActivity::class.java)
                        intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
                        intent.putExtra(Const.ORDER_PURCHASE, mOrderPurchase)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        launcher.launch(intent)
                    }

                }
            }
        }

        binding.recyclerOrderPurchaseHistoryDetailMenu.layoutManager = LinearLayoutManager(this)
        val adapter = OrderPurchaseMenuAdapter()
        binding.recyclerOrderPurchaseHistoryDetailMenu.adapter = adapter
        adapter.setDataList(mOrderPurchase!!.orderPurchaseMenuList!!.toMutableList())
    }

    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        getOrderPurchase()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_order_history_detail), ToolbarOption.ToolbarMenu.LEFT)
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