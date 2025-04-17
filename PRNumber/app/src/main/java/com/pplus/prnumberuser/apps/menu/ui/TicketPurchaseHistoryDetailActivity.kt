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
import com.pplus.prnumberuser.apps.page.ui.PageVisitMenuActivity
import com.pplus.prnumberuser.core.code.common.EnumData
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.Cart
import com.pplus.prnumberuser.core.network.model.dto.CartOption
import com.pplus.prnumberuser.core.network.model.dto.OrderPurchase
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivityTicketPurchaseHistoryDetailBinding
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import com.pplus.utils.part.utils.time.DateFormatUtils
import retrofit2.Call
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class TicketPurchaseHistoryDetailActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityTicketPurchaseHistoryDetailBinding

    override fun getLayoutView(): View {
        binding = ActivityTicketPurchaseHistoryDetailBinding.inflate(layoutInflater)
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

        binding.textTicketPurchaseHistoryDetailPageName.text = mOrderPurchase!!.page!!.name

        binding.textTicketPurchaseHistoryDetailPageName.setOnClickListener {
            val intent = Intent(this, PageVisitMenuActivity::class.java)
            intent.putExtra(Const.DATA, mOrderPurchase!!.page)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.textTicketPurchaseHistoryDetailCancel.visibility = View.GONE
        binding.textTicketPurchaseHistoryDetailReOrder.visibility = View.GONE
        binding.textTicketPurchaseHistoryDetailReview.visibility = View.GONE
        binding.layoutTicketPurchaseHistoryDetailSavePoint.visibility = View.VISIBLE

        val output = SimpleDateFormat("yyyy.MM.dd HH:mm")

        binding.textTicketPurchaseHistoryDetailPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(mOrderPurchase!!.price!!.toInt().toString()))

        when(mOrderPurchase!!.payMethod){
            "card"->{
                binding.textTicketPurchaseHistoryDetailPayMethod.text = getString(R.string.word_credit_card)
            }
            "ftlink"->{
                binding.textTicketPurchaseHistoryDetailPayMethod.text = getString(R.string.word_easy_pay)
            }
        }

        binding.textTicketPurchaseHistoryDetailPhone.text = FormatUtil.getPhoneNumber(mOrderPurchase!!.phone)

        binding.textTicketPurchaseHistoryDetailSavePoint.text = PplusCommonUtil.fromHtml(getString(R.string.html_will_saved_point_desc, mOrderPurchase!!.savedPoint!!.toInt().toString()))

        binding.textTicketPurchaseHistoryDetailBuyDate.text = output.format(DateFormatUtils.PPLUS_DATE_FORMAT.parse(mOrderPurchase!!.regDatetime))

        val expireOutput = SimpleDateFormat("yyyy-MM-dd")
        binding.textTicketPurchaseHistoryDetailExpireDate.text = getString(R.string.format_until, expireOutput.format(DateFormatUtils.PPLUS_DATE_FORMAT.parse(mOrderPurchase!!.expireDatetime)))
        binding.textTicketPurchaseHistoryDetailUse.visibility = View.GONE

        when (mOrderPurchase!!.status) {
            EnumData.OrderPurchaseStatus.PAY.status, EnumData.OrderPurchaseStatus.AFTER_PAY.status -> {
                when (mOrderPurchase!!.statusTicket) {
                    4-> {
                        binding.textTicketPurchaseHistoryDetailStatus.setText(R.string.msg_req_ing_use)
                    }
                    else ->{
                        binding.textTicketPurchaseHistoryDetailStatus.setText(R.string.word_use_ready)
                    }
                }
                binding.textTicketPurchaseHistoryDetailStatus.setTextColor(ResourceUtil.getColor(this, R.color.color_373c42))

                binding.textTicketPurchaseHistoryDetailCancel.visibility = View.VISIBLE
                binding.textTicketPurchaseHistoryDetailCancel.setOnClickListener {
                    val intent = Intent(this, AlertCancelOrderActivity::class.java)
                    intent.putExtra(Const.DATA, mOrderPurchase!!)
                    launcher.launch(intent)
                }

                when (mOrderPurchase!!.statusTicket) { // 티켓 0:접수대기, 1:접수완료, 2:취소, 3:기간만료, 4:사용요청, 99:사용완료
                    4->{
                        binding.textTicketPurchaseHistoryDetailUse.visibility = View.VISIBLE
                        binding.textTicketPurchaseHistoryDetailUse.setText(R.string.msg_req_ing_use)
                        binding.textTicketPurchaseHistoryDetailUse.setBackgroundColor(ResourceUtil.getColor(this, R.color.color_8c8c8f))
                        binding.textTicketPurchaseHistoryDetailUse.setOnClickListener {
                            showAlert(R.string.msg_exist_request_log)
                        }
                    }
                    else->{
                        binding.textTicketPurchaseHistoryDetailUse.visibility = View.VISIBLE
                        binding.textTicketPurchaseHistoryDetailUse.setOnClickListener {
                            val params = HashMap<String, String>()
                            params["orderPurchaseSeqNo"] = mOrderPurchase!!.seqNo.toString()
                            showProgress("")
                            ApiBuilder.create().useTicket(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                                override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                                    hideProgress()
                                    val intent = Intent(this@TicketPurchaseHistoryDetailActivity, TicketUseActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    launcher.launch(intent)
                                }

                                override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                                    hideProgress()
                                    if (response?.resultCode == 662) {
                                        showAlert(R.string.msg_already_use_ticket)
                                    }else if (response?.resultCode == 661) {
                                        showAlert(R.string.msg_exist_request_log)
                                    }
                                }
                            }).build().call()
                        }
                    }
                }

            }

            EnumData.OrderPurchaseStatus.CANCEL_REQ.status -> {
                binding.textTicketPurchaseHistoryDetailStatus.setText(R.string.word_cancel_request)
                binding.layoutTicketPurchaseHistoryDetailSavePoint.visibility = View.GONE
            }
            EnumData.OrderPurchaseStatus.CANCEL_COMPLETE.status -> {
                binding.layoutTicketPurchaseHistoryDetailExpireDate.visibility = View.GONE
                binding.textTicketPurchaseHistoryDetailStatus.setText(R.string.word_buy_cancel)
                binding.textTicketPurchaseHistoryDetailStatus.setTextColor(ResourceUtil.getColor(this, R.color.color_ff4026))

                binding.layoutTicketPurchaseHistoryDetailCancel.visibility = View.VISIBLE
                var d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mOrderPurchase!!.cancelDatetime)
                binding.textTicketPurchaseHistoryDetailCancelDate.text = output.format(d)
                if(StringUtils.isNotEmpty(mOrderPurchase!!.cancelMemo)){
                    binding.layoutTicketPurchaseHistoryDetailCancelMemo.visibility = View.VISIBLE
                    binding.textTicketPurchaseHistoryDetailCancelMemo.text = mOrderPurchase!!.cancelMemo
                }else{
                    binding.layoutTicketPurchaseHistoryDetailCancelMemo.visibility = View.GONE
                }

            }
            EnumData.OrderPurchaseStatus.COMPLETE.status -> {
                binding.textTicketPurchaseHistoryDetailSavePoint.text = PplusCommonUtil.fromHtml(getString(R.string.html_saved_point_desc, mOrderPurchase!!.savedPoint!!.toInt().toString()))

                when (mOrderPurchase!!.statusTicket) { // 티켓 0:접수대기, 1:접수완료, 2:취소, 3:기간만료, 4:사용요청, 99:사용완료

                    3 -> {
                        binding.textTicketPurchaseHistoryDetailStatus.setText(R.string.word_expire)
                        binding.textTicketPurchaseHistoryDetailStatus.setTextColor(ResourceUtil.getColor(this, R.color.color_ff4026))

                    }
                    99 -> {
                        binding.textTicketPurchaseHistoryDetailStatus.setText(R.string.word_use_complete)
                        binding.textTicketPurchaseHistoryDetailStatus.setTextColor(ResourceUtil.getColor(this, R.color.color_4694fb))

                        binding.textTicketPurchaseHistoryDetailReOrder.visibility = View.VISIBLE

                        binding.textTicketPurchaseHistoryDetailReOrder.setOnClickListener {

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
                            binding.textTicketPurchaseHistoryDetailReview.visibility = View.GONE
                        } else {
                            binding.textTicketPurchaseHistoryDetailReview.visibility = View.VISIBLE
                            binding.textTicketPurchaseHistoryDetailReview.setText(R.string.word_review_write)

                            binding.textTicketPurchaseHistoryDetailReview.setOnClickListener {
                                val intent = Intent(this, OrderMenuReviewRegActivity::class.java)
                                intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
                                intent.putExtra(Const.ORDER_PURCHASE, mOrderPurchase)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                launcher.launch(intent)
                            }

                        }
                    }
                }

            }
        }

        binding.recyclerTicketPurchaseHistoryDetailMenu.layoutManager = LinearLayoutManager(this)
        val adapter = OrderPurchaseMenuAdapter()
        binding.recyclerTicketPurchaseHistoryDetailMenu.adapter = adapter
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