package com.pplus.prnumberuser.apps.setting.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.bol.ui.BolConfigActivity
import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberuser.apps.common.builder.data.AlertData
import com.pplus.prnumberuser.apps.common.mgmt.DeliveryAddressManager
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.common.ui.custom.SafeSwitchCompat
import com.pplus.prnumberuser.apps.delivery.ui.DeliveryAddressSetActivity
import com.pplus.prnumberuser.apps.event.ui.EventDetailActivity
import com.pplus.prnumberuser.apps.event.ui.EventImpressionActivity
import com.pplus.prnumberuser.apps.event.ui.EventMoveDetailActivity
import com.pplus.prnumberuser.apps.menu.ui.OrderPurchaseHistoryDetailActivity
import com.pplus.prnumberuser.apps.menu.ui.TicketPurchaseHistoryDetailActivity
import com.pplus.prnumberuser.apps.page.ui.PageDeliveryMenuActivity
import com.pplus.prnumberuser.apps.product.ui.ProductShipDetailActivity
import com.pplus.prnumberuser.apps.product.ui.PurchaseHistoryActivity
import com.pplus.prnumberuser.apps.setting.data.NotificationBoxAdapter
import com.pplus.prnumberuser.core.code.common.EventType
import com.pplus.prnumberuser.core.code.common.MoveType2Code
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.*
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.network.model.response.SubResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivityAlarmContainerBinding
import com.pplus.prnumberuser.databinding.ItemTopSwitchBinding
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*

class AlarmContainerActivity : BaseActivity(), ImplToolbar {

    private var mLayoutManager: LinearLayoutManager? = null
    private var mAdapter: NotificationBoxAdapter? = null

    private var mTotalCount: Int = 0
    private var mLockListView = false
    private var mPaging = 1
    private var mIsLast = false

    override fun getPID(): String? {
        return ""
    }

    private lateinit var binding: ActivityAlarmContainerBinding

    override fun getLayoutView(): View {
        binding = ActivityAlarmContainerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        mLayoutManager = LinearLayoutManager(this)
        mLayoutManager!!.orientation = LinearLayoutManager.VERTICAL
        binding.recyclerAlarm.layoutManager = mLayoutManager
        mAdapter = NotificationBoxAdapter()
        binding.recyclerAlarm.adapter = mAdapter

        mAdapter!!.listener = object : NotificationBoxAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val item = mAdapter!!.getItem(position)


                if (item.moveType1 == "inner") {
                    if (item.moveType2 == null) {
                        return
                    }
                    val intent: Intent // 스키마 호출
                    when (item.moveType2) {
                        MoveType2Code.main.name -> {
                        }
                        MoveType2Code.pageDetail.name -> {

                            val page = Page()
                            page.no = item.moveSeqNo
                            PplusCommonUtil.goPage(this@AlarmContainerActivity, page, 0, 0)
                        }
                        MoveType2Code.productDetail.name -> {
                            intent = Intent(this@AlarmContainerActivity, ProductShipDetailActivity::class.java)
                            val productPrice = ProductPrice()
                            productPrice.seqNo = item.moveSeqNo
                            intent.putExtra(Const.DATA, productPrice)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            startActivity(intent)
                        }
                        MoveType2Code.buyCancel.name -> {
                            intent = Intent(this@AlarmContainerActivity, PurchaseHistoryActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            startActivity(intent)
                        }
                        MoveType2Code.bolHistory.name -> {
                            intent = Intent(this@AlarmContainerActivity, BolConfigActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            startActivity(intent)
                        }
                        MoveType2Code.eventWin.name -> {
                            intent = Intent(this@AlarmContainerActivity, EventImpressionActivity::class.java)
                            val event = Event()
                            event.no = item.moveSeqNo
                            intent.putExtra(Const.DATA, event)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            startActivity(intent)
                        }
                        MoveType2Code.eventDetail.name -> {

                            val params = HashMap<String, String>()
                            params["no"] = "" + item.moveSeqNo

                            ApiBuilder.create().getEvent(params).setCallback(object : PplusCallback<NewResultResponse<Event>> {

                                override fun onResponse(call: Call<NewResultResponse<Event>>, response: NewResultResponse<Event>) {

                                    val event = response.data
                                    if (event != null) {
                                        if (StringUtils.isNotEmpty(event.primaryType) && event.primaryType == EventType.PrimaryType.move.name) {
                                            val intent = Intent(this@AlarmContainerActivity, EventMoveDetailActivity::class.java)
                                            intent.putExtra(Const.DATA, event)
                                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                            startActivity(intent)
                                        } else {
                                            val intent = Intent(this@AlarmContainerActivity, EventDetailActivity::class.java)
                                            intent.putExtra(Const.DATA, event)
                                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                            startActivity(intent)
                                        }
                                    }
                                }

                                override fun onFailure(call: Call<NewResultResponse<Event>>, t: Throwable, response: NewResultResponse<Event>) {

                                }
                            }).build().call()
                        }
                        MoveType2Code.noticeDetail.name -> {
                            intent = Intent(this@AlarmContainerActivity, NoticeDetailActivity::class.java)
                            val notice = Notice()
                            notice.no = item.moveSeqNo
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            intent.putExtra(Const.NOTICE, notice)
                            startActivity(intent)
                        }

                        MoveType2Code.bolDetail.name -> {
                            intent = Intent(this@AlarmContainerActivity, BolConfigActivity::class.java)
                            val bol = Bol()
                            bol.no = item.moveSeqNo
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            intent.putExtra(Const.DATA, bol)
                            startActivity(intent)
                        }
                        MoveType2Code.orderDetail.name -> {
                            if (LoginInfoManager.getInstance().isMember) {
                                val intent = Intent(this@AlarmContainerActivity, OrderPurchaseHistoryDetailActivity::class.java)
                                val orderPurchase = OrderPurchase()
                                orderPurchase.seqNo = item.moveSeqNo
                                intent.putExtra(Const.DATA, orderPurchase)
                                startActivity(intent)
                            }
                        }
                        MoveType2Code.ticketDetail.name -> {
                            if (LoginInfoManager.getInstance().isMember) {
                                val intent = Intent(this@AlarmContainerActivity, TicketPurchaseHistoryDetailActivity::class.java)
                                val orderPurchase = OrderPurchase()
                                orderPurchase.seqNo = item.moveSeqNo
                                intent.putExtra(Const.DATA, orderPurchase)
                                startActivity(intent)
                            }
                        }
                        MoveType2Code.menu.name -> {
                            val deliveryAddressList = DeliveryAddressManager.getInstance().deliveryAddressList
                            if(deliveryAddressList != null && deliveryAddressList.isNotEmpty()){
                                val intent = Intent(this@AlarmContainerActivity, PageDeliveryMenuActivity::class.java)
                                val page = Page2()
                                page.seqNo = item.moveSeqNo
                                intent.putExtra(Const.DATA, page)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                startActivity(intent)
                            }else{
                                val intent = Intent(this@AlarmContainerActivity, DeliveryAddressSetActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                startActivity(intent)
                            }
                        }
                    }
                } else {
                    if (StringUtils.isNotEmpty(item.moveString)) {
                        PplusCommonUtil.openChromeWebView(this@AlarmContainerActivity, item.moveString!!)
                    }
                }
            }
        }

        binding.recyclerAlarm.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            internal var pastVisibleItems: Int = 0
            internal var visibleItemCount: Int = 0
            internal var totalItemCount: Int = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                super.onScrolled(recyclerView, dx, dy)

                visibleItemCount = mLayoutManager!!.childCount
                totalItemCount = mLayoutManager!!.itemCount
                pastVisibleItems = mLayoutManager!!.findFirstVisibleItemPosition()
                if (!mLockListView) {
                    if (!mIsLast && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
                        mPaging++
                        listCall(mPaging)
                    }
                }
            }
        })

        mPaging = 0
        listCall(mPaging)
    }

    private fun listCall(page: Int) {

        mLockListView = true
        val params = HashMap<String, String>()
        params["page"] = page.toString()
        showProgress("")
        mLockListView = true
        ApiBuilder.create().getNotificationBoxList(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<NotificationBox>>> {

            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<NotificationBox>>>?, response: NewResultResponse<SubResultResponse<NotificationBox>>?) {
                mLockListView = false
                hideProgress()
                if (response?.data != null) {

                    mIsLast = response.data.last!!
                    if (response.data.first!!) {
                        mTotalCount = response.data.totalElements!!
                        mAdapter!!.clear()
                        if (mTotalCount == 0) {
                            binding.textAlarmContainerNotExist.visibility = View.VISIBLE
                        } else {
                            binding.textAlarmContainerNotExist.visibility = View.GONE
                        }
                    }

                    mLockListView = false
                    mAdapter!!.addAll(response.data.content!!)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<NotificationBox>>>?, t: Throwable?, response: NewResultResponse<SubResultResponse<NotificationBox>>?) {
                mLockListView = false
                hideProgress()
            }
        }).build().call()
    }

    fun delete(seqNo: Long) {

        val params = HashMap<String, String>()
        params["seqNo"] = seqNo.toString()
        showProgress("")
        ApiBuilder.create().notificationBoxDelete(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {

            override fun onResponse(call: Call<NewResultResponse<Any>>, response: NewResultResponse<Any>) {

                hideProgress()
                showAlert(R.string.msg_delete_complete)
                mPaging = 0
                listCall(mPaging)
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>, t: Throwable, response: NewResultResponse<Any>) {

                hideProgress()
            }
        }).build().call()
    }


    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_alarm_container), ToolbarOption.ToolbarMenu.LEFT)
        val topSwitchBinding = ItemTopSwitchBinding.inflate(layoutInflater)
        if (LoginInfoManager.getInstance().user.device!!.installedApp != null) {
            topSwitchBinding.switchTopRight.setSafeCheck(LoginInfoManager.getInstance().user.device!!.installedApp.isPushActivate, SafeSwitchCompat.IGNORE)
        } else {
            topSwitchBinding.switchTopRight.setSafeCheck(false, SafeSwitchCompat.IGNORE)
        }

        topSwitchBinding.switchTopRight.onSafeCheckedListener = object : SafeSwitchCompat.OnSafeCheckedListener {

            override fun onAlwaysCalledListener(buttonView: CompoundButton, isChecked: Boolean) {

            }

            override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {

                if (isChecked) {
                    val pushMask = "1111111111111111"
                    pushUpdate(pushMask, true)
                } else {
                    val builder = AlertBuilder.Builder()
                    builder.setTitle(getString(R.string.word_notice_alert))
                    builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_alarm_off), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
                    builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
                    builder.setOnAlertResultListener(object : OnAlertResultListener {

                        override fun onCancel() {
                            topSwitchBinding.switchTopRight.setSafeCheck(true, SafeSwitchCompat.IGNORE)
                        }

                        override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                            when (event_alert) {
                                AlertBuilder.EVENT_ALERT.RIGHT -> {
                                    val pushMask = "0000000000000000"
                                    pushUpdate(pushMask, false)
                                }
                                AlertBuilder.EVENT_ALERT.LEFT -> topSwitchBinding.switchTopRight.setSafeCheck(true, SafeSwitchCompat.IGNORE)
                            }

                        }
                    }).builder().show(this@AlarmContainerActivity)
                }
            }
        }
        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, topSwitchBinding.root, 0)
        return toolbarOption
    }

    private fun pushUpdate(pushMask: String, pushActivate: Boolean) {

        val params = HashMap<String, String>()
        params["pushActivate"] = pushActivate.toString()
        params["pushMask"] = pushMask

        showProgress("")
        ApiBuilder.create().updatePushConfig(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {

            override fun onResponse(call: Call<NewResultResponse<Any>>, response: NewResultResponse<Any>) {

                hideProgress()
                LoginInfoManager.getInstance().user.device!!.installedApp.isPushActivate = pushActivate
                LoginInfoManager.getInstance().user.device!!.installedApp.pushMask = pushMask
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>, t: Throwable, response: NewResultResponse<Any>) {

                hideProgress()
            }
        }).build().call()
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
