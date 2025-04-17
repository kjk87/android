package com.pplus.prnumberbiz.apps.cash.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.pple.pplus.utils.part.format.FormatUtil
import com.pple.pplus.utils.part.logs.LogUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pple.pplus.utils.part.utils.time.DateFormatUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.cash.data.CashAdapter
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Cash
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import com.pplus.prnumberbiz.core.util.ToastUtil
import kotlinx.android.synthetic.main.activity_cash_config.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.*

class CashConfigActivity2 : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return "Main_menu_cash"
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_cash_config
    }

    enum class CashTab {
        charge, use
    }

    private var mCashTab: CashTab? = null
    private var mPage = 1
    private var mLockListView = true
    private var mTotalCount = 0
    private var startCalendar: Calendar? = null
    private var endCalendar:Calendar? = null
    private var mInputStart: Boolean = false
    private var mInputEnd:Boolean = false
    private var mSort: String? = null
    private var mStart:String? = null
    private var mEnd:String? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var mAdapter: CashAdapter? = null

    override fun initializeView(savedInstanceState: Bundle?) {

        image_cash_config_detail.setOnClickListener {
            val intent = Intent(this, CashConfigDetailActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        text_cash_config_charge.setOnClickListener {
            if(LoginInfoManager.getInstance().user.page!!.agent != null){
                val intent = Intent(this, CashChargeAlertActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }else{
//                val intent = Intent(this, CashBillingActivity::class.java)
                val intent = Intent(this, CashChargeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivityForResult(intent, Const.REQ_CASH_CHANGE)
            }

        }

        layout_cash_config_charge_tab.setOnClickListener {
            if (mCashTab != CashTab.charge) {
                selectedView(layout_cash_config_charge_tab, layout_cash_config_use_tab)
                selectedTextView(text_cash_config_charge_tab, text_cash_config_use_tab)
                mCashTab = CashTab.charge
                setData()
            }
        }

        layout_cash_config_use_tab.setOnClickListener {
            if (mCashTab != CashTab.use) {
                selectedView(layout_cash_config_use_tab, layout_cash_config_charge_tab)
                selectedTextView(text_cash_config_use_tab, text_cash_config_charge_tab)
                mCashTab = CashTab.use
                setData()
            }
        }

        text_cash_config_sort.setOnClickListener {
            val builder = AlertBuilder.Builder()
            builder.setContents(getString(R.string.word_sort_recent), getString(R.string.word_sort_price), getString(R.string.word_sort_past))
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                    when (event_alert.getValue()) {
                        1 -> {
                            text_cash_config_sort.setText(R.string.word_sort_recent)
                            mSort = "new"
                        }
                        2 -> {
                            text_cash_config_sort.setText(R.string.word_sort_price)
                            mSort = "amount"
                        }
                        3 -> {
                            text_cash_config_sort.setText(R.string.word_sort_past)
                            mSort = "old"
                        }
                    }
                    getCount()
                }
            }).builder().show(this)
        }

        text_cash_config_start_day.setOnClickListener {
            DatePickerDialog(this, DatePickerDialog.OnDateSetListener { datePicker, i, i1, i2 ->
                startCalendar!!.set(i, i1, i2)

                if (mInputEnd) {
                    if (startCalendar!!.compareTo(endCalendar) > 0) {
                        ToastUtil.show(this, R.string.msg_input_startday_before_endday)
                        return@OnDateSetListener
                    }
                }
                mInputStart = true

                text_cash_config_start_day.text = getString(R.string.format_date, i.toString(), DateFormatUtils.formatTime(i1 + 1), DateFormatUtils.formatTime(i2))
                mStart = i.toString() + "-" + DateFormatUtils.formatTime(i1 + 1) + "-" + DateFormatUtils.formatTime(i2) + " 00:00:00"
            }, startCalendar!!.get(Calendar.YEAR), startCalendar!!.get(Calendar.MONTH), startCalendar!!.get(Calendar.DAY_OF_MONTH)).show()
        }

        text_cash_config_end_day.setOnClickListener {
            DatePickerDialog(this, DatePickerDialog.OnDateSetListener { datePicker, i, i1, i2 ->
                endCalendar!!.set(i, i1, i2)
                if (mInputStart) {
                    if (startCalendar!!.compareTo(endCalendar) > 0) {
                        ToastUtil.show(this, R.string.msg_input_endday_after_startday)
                        return@OnDateSetListener
                    }
                }

                mInputEnd = true

                text_cash_config_end_day.text = getString(R.string.format_date, i.toString(), DateFormatUtils.formatTime(i1 + 1), DateFormatUtils.formatTime(i2))
                mEnd = i.toString() + "-" + DateFormatUtils.formatTime(i1 + 1) + "-" + DateFormatUtils.formatTime(i2) + " 23:59:59"
            }, endCalendar!!.get(Calendar.YEAR), endCalendar!!.get(Calendar.MONTH), endCalendar!!.get(Calendar.DAY_OF_MONTH)).show()
        }

        text_cash_config_search.setOnClickListener {
            getCount()
        }

        mLayoutManager = LinearLayoutManager(this)
        recycler_cash_config.layoutManager = mLayoutManager
        mAdapter = CashAdapter(this)
        recycler_cash_config.adapter = mAdapter

        recycler_cash_config.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            var pastVisibleItems: Int = 0
            var visibleItemCount: Int = 0
            var totalItemCount: Int = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                super.onScrolled(recyclerView, dx, dy)
                visibleItemCount = mLayoutManager!!.childCount
                totalItemCount = mLayoutManager!!.itemCount
                pastVisibleItems = mLayoutManager!!.findFirstVisibleItemPosition()
                if (!mLockListView) {
                    if (totalItemCount < mTotalCount && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
                        mPage++
                        listCall(mPage)
                    }
                }
            }
        })

        mAdapter!!.setOnItemClickListener(object : CashAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(this@CashConfigActivity2, CashHistoryDetailActivity::class.java)
                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            }
        })

        startCalendar = Calendar.getInstance()
        startCalendar!!.add(Calendar.MONTH, -3)
        endCalendar = Calendar.getInstance()
        initDate()

        selectedView(layout_cash_config_use_tab, layout_cash_config_charge_tab)
        selectedTextView(text_cash_config_use_tab, text_cash_config_charge_tab)
        mCashTab = CashTab.use
        mSort = "new"

        getCash()
        setData()
    }

    private fun setData(){
        when (mCashTab) {

            CashTab.charge -> {
                text_cash_config_title1.setText(R.string.word_charge_date)
                text_cash_config_title2.setText(R.string.word_charge_method)
                text_cash_config_title3.setText(R.string.word_charge_price)
                text_cash_config_not_exist.setText(R.string.msg_not_exist_cash_charge_history)
            }
            CashTab.use -> {
                text_cash_config_title1.setText(R.string.word_use_date)
                text_cash_config_title2.setText(R.string.word_use_place)
                text_cash_config_title3.setText(R.string.word_use_price)
                text_cash_config_not_exist.setText(R.string.msg_not_exist_cash_use_history)
            }
        }
        getCount()
    }

    private fun initDate() {
        mEnd = endCalendar!!.get(Calendar.YEAR).toString() + "-" + DateFormatUtils.formatTime(endCalendar!!.get(Calendar.MONTH) + 1) + "-" + DateFormatUtils.formatTime(endCalendar!!.get(Calendar.DAY_OF_MONTH)) + " 23:59:59"
        text_cash_config_end_day.text = getString(R.string.format_date, endCalendar!!.get(Calendar.YEAR).toString(), DateFormatUtils.formatTime(endCalendar!!.get(Calendar.MONTH) + 1), DateFormatUtils.formatTime(endCalendar!!.get(Calendar.DAY_OF_MONTH)))

        mStart = startCalendar!!.get(Calendar.YEAR).toString() + "-" + DateFormatUtils.formatTime(startCalendar!!.get(Calendar.MONTH) + 1) + "-" + DateFormatUtils.formatTime(startCalendar!!.get(Calendar.DAY_OF_MONTH)) + " 00:00:00"
        text_cash_config_start_day.text = getString(R.string.format_date, startCalendar!!.get(Calendar.YEAR).toString(), DateFormatUtils.formatTime(startCalendar!!.get(Calendar.MONTH) + 1), DateFormatUtils.formatTime(startCalendar!!.get(Calendar.DAY_OF_MONTH)))
    }

    private fun getCount() {
        val params = HashMap<String, String>()
        when (mCashTab) {

            CashTab.charge -> {
                val builder = StringBuilder()
                builder.append(EnumData.CashType.buy.name).append(",")
                builder.append(EnumData.CashType.refundAdvertise.name).append(",")
                builder.append(EnumData.CashType.recvAdmin.name)
                builder.append(EnumData.CashType.cancelSendMsg.name).append(",")
                builder.append(EnumData.CashType.refundMsgFail.name).append(",")
                params["filter"] = builder.toString()
            }
            CashTab.use -> {
                val builder = StringBuilder()
                builder.append(EnumData.CashType.useTargetPush.name).append(",")
                builder.append(EnumData.CashType.usePush.name).append(",")
                builder.append(EnumData.CashType.useSms.name).append(",")
                builder.append(EnumData.CashType.useLBS.name).append(",")
                builder.append(EnumData.CashType.useAdKeyword.name).append(",")
                builder.append(EnumData.CashType.useAdvertise.name).append(",")
                builder.append(EnumData.CashType.refundAdmin.name).append(",")
                builder.append(EnumData.CashType.buyBol.name)
                params["filter"] = builder.toString()

            }
        }
        if (StringUtils.isNotEmpty(mStart)) {
            params["start"] = mStart!!
        }

        if (StringUtils.isNotEmpty(mEnd)) {
            params["end"] = mEnd!!
        }

        ApiBuilder.create().getCashHistoryTotalAmount(params).setCallback(object : PplusCallback<NewResultResponse<String>> {

            override fun onResponse(call: Call<NewResultResponse<String>>, response: NewResultResponse<String>) {

                LogUtil.e(LOG_TAG, "amount : {}", response.data)
                if (text_cash_config_total_amount != null) {
                    when (mCashTab) {

                        CashTab.charge -> text_cash_config_total_amount.text = PplusCommonUtil.fromHtml(getString(R.string.html_total_cash_amount, FormatUtil.getMoneyType(response.data), getString(R.string.word_charge)))
                        CashTab.use -> text_cash_config_total_amount.text = PplusCommonUtil.fromHtml(getString(R.string.html_total_cash_amount2, FormatUtil.getMoneyType(response.data), getString(R.string.word_use)))
                    }
                }

            }

            override fun onFailure(call: Call<NewResultResponse<String>>, t: Throwable, response: NewResultResponse<String>) {

            }
        }).build().call()

        ApiBuilder.create().getCashHistoryCount(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {

            override fun onResponse(call: Call<NewResultResponse<Int>>, response: NewResultResponse<Int>) {

                mTotalCount = response.data

                mAdapter!!.clear()
                if (mTotalCount > 0) {
                    layout_cash_history.visibility = View.VISIBLE
                    text_cash_config_not_exist.visibility = View.GONE
                } else {
                    layout_cash_history.visibility = View.GONE
                    text_cash_config_not_exist.visibility = View.VISIBLE
                }

                mPage = 1
                listCall(mPage)
            }

            override fun onFailure(call: Call<NewResultResponse<Int>>, t: Throwable, response: NewResultResponse<Int>) {

            }
        }).build().call()
    }

    private fun listCall(page: Int) {
        val params = HashMap<String, String>()
        when (mCashTab) {

            CashTab.charge -> {
                val builder = StringBuilder()
                builder.append(EnumData.CashType.buy.name).append(",")
                builder.append(EnumData.CashType.refundAdvertise.name).append(",")
                builder.append(EnumData.CashType.cancelSendMsg.name).append(",")
                builder.append(EnumData.CashType.refundMsgFail.name).append(",")
                builder.append(EnumData.CashType.recvAdmin.name)
                params["filter"] = builder.toString()
            }
            CashTab.use -> {
                val builder = StringBuilder()
                builder.append(EnumData.CashType.useTargetPush.name).append(",")
                builder.append(EnumData.CashType.usePush.name).append(",")
                builder.append(EnumData.CashType.useSms.name).append(",")
                builder.append(EnumData.CashType.useLBS.name).append(",")
                builder.append(EnumData.CashType.useAdKeyword.name).append(",")
                builder.append(EnumData.CashType.useAdvertise.name).append(",")
                builder.append(EnumData.CashType.refundAdmin.name).append(",")
                builder.append(EnumData.CashType.buyBol.name)
                params["filter"] = builder.toString()
            }
        }
        if (StringUtils.isNotEmpty(mStart)) {
            params["start"] = mStart!!
        }

        if (StringUtils.isNotEmpty(mEnd)) {
            params["end"] = mEnd!!
        }

        params["align"] = mSort!!
        params["pg"] = "" + page
        mLockListView = true
        showProgress("")
        ApiBuilder.create().getCashHistoryList(params).setCallback(object : PplusCallback<NewResultResponse<Cash>> {

            override fun onResponse(call: Call<NewResultResponse<Cash>>, response: NewResultResponse<Cash>) {

                hideProgress()

                mLockListView = false
                mAdapter!!.addAll(response.datas)
            }

            override fun onFailure(call: Call<NewResultResponse<Cash>>, t: Throwable, response: NewResultResponse<Cash>) {

                hideProgress()
            }
        }).build().call()
    }

    private fun getCash(){
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {
                text_cash_config_retention_cash.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(LoginInfoManager.getInstance().user.totalCash.toString()))
            }
        })
    }

    private fun selectedView(view1: View, view2: View) {

        view1.isSelected = true
        view2.isSelected = false
    }

    private fun selectedTextView(textView1: TextView, textView2: TextView) {

        textView1.setTypeface(Typeface.SERIF, Typeface.BOLD)
        textView2.setTypeface(Typeface.SERIF, Typeface.NORMAL)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Const.REQ_CASH_CHANGE -> {
//                if (data != null && StringUtils.isNotEmpty(data.getStringExtra(Const.PAYMETHOD))) {
//                    val payMethod = data.getStringExtra(Const.PAYMETHOD)
//                    if (payMethod.equals("vbank", ignoreCase = true)) {
//                        val intent = Intent(this, PayPendingListActivity::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                        startActivityForResult(intent, Const.REQ_CASH_CHANGE)
//                    }
//                }
                getCash()
                getCount()
            }
        }
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_config_cash), ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {
        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
            }
        }
    }
}
