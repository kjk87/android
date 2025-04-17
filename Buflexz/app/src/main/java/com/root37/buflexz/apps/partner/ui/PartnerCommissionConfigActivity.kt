package com.root37.buflexz.apps.partner.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.time.DateFormatUtils
import com.root37.buflexz.Const
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.builder.AlertBuilder
import com.root37.buflexz.apps.common.builder.OnAlertResultListener
import com.root37.buflexz.apps.common.toolbar.ImplToolbar
import com.root37.buflexz.apps.common.toolbar.OnToolbarListener
import com.root37.buflexz.apps.common.toolbar.ToolbarOption
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.apps.partner.data.HistoryCommissionAdapter
import com.root37.buflexz.core.network.ApiBuilder
import com.root37.buflexz.core.network.model.dto.HistoryCommission
import com.root37.buflexz.core.network.model.response.ListResultResponse
import com.root37.buflexz.core.network.model.response.NewResultResponse
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.core.util.ToastUtil
import com.root37.buflexz.databinding.ActivityPartnerCommissionConfigBinding
import retrofit2.Call
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class PartnerCommissionConfigActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityPartnerCommissionConfigBinding

    override fun getLayoutView(): View {
        binding = ActivityPartnerCommissionConfigBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    private var mTotalCount = 0
    var mAdapter: HistoryCommissionAdapter? = null
    private lateinit var mLayoutManager: LinearLayoutManager
    private var mLockListView = false
    private var mPaging = 1
    private var mDir = "ASC"
    private var mStart = ""
    private var mEnd = ""

    override fun initializeView(savedInstanceState: Bundle?) {
        mLayoutManager = LinearLayoutManager(this)
        binding.recyclerPartnerCommissionConfig.layoutManager = mLayoutManager
        mAdapter = HistoryCommissionAdapter()
        binding.recyclerPartnerCommissionConfig.adapter = mAdapter

        binding.recyclerPartnerCommissionConfig.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            internal var pastVisibleItems: Int = 0
            internal var visibleItemCount: Int = 0
            internal var totalItemCount: Int = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                super.onScrolled(recyclerView, dx, dy)

                visibleItemCount = mLayoutManager.childCount
                totalItemCount = mLayoutManager.itemCount
                pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition()
                if (!mLockListView) {
                    if (totalItemCount < mTotalCount && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
                        mPaging++
                        getList(mPaging)
                    }
                }
            }
        })

        binding.layoutContactUs.textContactUsEmailCopy.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            val clip = ClipData.newPlainText(getString(R.string.word_contact_us_en), getString(R.string.cs_email))
            clipboard.setPrimaryClip(clip)
            ToastUtil.show(this, R.string.msg_copied_clipboard)
        }

        val durationFormat = SimpleDateFormat("yyyy-MM-dd")
        val displayFormat = SimpleDateFormat(getString(R.string.word_date_format2))

        binding.layoutPartnerCommissionConfigDate.setOnClickListener {
            val builder = AlertBuilder.Builder()
            builder.setTitle(getString(R.string.word_alert_date_title))
            builder.setContents(getString(R.string.word_today), getString(R.string.word_yesterday), getString(R.string.format_last_day, "7"), getString(R.string.format_last_day, "30"))
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                    when (event_alert.value) {
                        1 -> {
                            binding.textPartnerCommissionConfigDateTitle.setText(R.string.word_today)
                            binding.textPartnerCommissionConfigDate.text = displayFormat.format(Date())

                            val day = durationFormat.format(Date())
                            mStart = "${day} 00:00:00"
                            mEnd = "${day} 23:59:59"
                        }
                        2 -> {
                            binding.textPartnerCommissionConfigDateTitle.setText(R.string.word_yesterday)
                            val cal = Calendar.getInstance()
                            cal.add(Calendar.DAY_OF_MONTH, -1)
                            binding.textPartnerCommissionConfigDate.text = displayFormat.format(cal.time)

                            val day = durationFormat.format(cal.time)
                            mStart = "${day} 00:00:00"
                            mEnd = "${day} 23:59:59"
                        }
                        3 -> {
                            binding.textPartnerCommissionConfigDateTitle.text = getString(R.string.format_last_day, "7")
                            val cal = Calendar.getInstance()
                            val disPlayEnd = displayFormat.format(cal.time)
                            mEnd = "${durationFormat.format(cal.time)} 23:59:59"
                            cal.add(Calendar.DAY_OF_MONTH, -7)
                            val disPlayStart = displayFormat.format(cal.time)
                            mStart = "${durationFormat.format(cal.time)} 00:00:00"

                            binding.textPartnerCommissionConfigDate.text = "${disPlayStart} ~ ${disPlayEnd}"
                        }
                        4 -> {
                            binding.textPartnerCommissionConfigDateTitle.text = getString(R.string.format_last_day, "30")
                            val cal = Calendar.getInstance()
                            val disPlayEnd = displayFormat.format(cal.time)
                            mEnd = "${durationFormat.format(cal.time)} 23:59:59"
                            cal.add(Calendar.DAY_OF_MONTH, -30)
                            val disPlayStart = displayFormat.format(cal.time)
                            mStart = "${durationFormat.format(cal.time)} 00:00:00"
                            binding.textPartnerCommissionConfigDate.text = "${disPlayStart} ~ ${disPlayEnd}"
                        }

                    }
                    getCommission()


                }
            }).builder().show(this)
        }

        binding.textPartnerCommissionConfigSort.setOnClickListener {
            val builder = AlertBuilder.Builder()
            builder.setContents(getString(R.string.word_sort_recent), getString(R.string.word_sort_past))
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                    when (event_alert.value) {
                        1 -> {
                            binding.textPartnerCommissionConfigSort.setText(R.string.word_sort_recent)
                            mDir = "ASC"
                        }
                        2 -> {
                            binding.textPartnerCommissionConfigSort.setText(R.string.word_sort_past)
                            mDir = "DESC"
                        }
                    }
                    mPaging = 1
                    getList(mPaging)

                }
            }).builder().show(this)
        }

        val day = durationFormat.format(Date())
        binding.textPartnerCommissionConfigDateTitle.setText(R.string.word_today)
        binding.textPartnerCommissionConfigDate.text = day
        mStart = "${day} 00:00:00"
        mEnd = "${day} 23:59:59"

        getTotalProfit()
    }

    private fun getTotalProfit(){
        showProgress("")
        ApiBuilder.create().getTotalProfit().setCallback(object : PplusCallback<NewResultResponse<Double>>{
            override fun onResponse(call: Call<NewResultResponse<Double>>?, response: NewResultResponse<Double>?) {
                hideProgress()
                if(response?.result != null){
                    binding.textPartnerCommissionConfigTotalPrice.text = FormatUtil.getMoneyTypeFloat(response.result.toString())
                }else{
                    binding.textPartnerCommissionConfigTotalPrice.text = "0"
                }

                getCommission()
            }

            override fun onFailure(call: Call<NewResultResponse<Double>>?, t: Throwable?, response: NewResultResponse<Double>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun getCommission(){
        val params = HashMap<String, String>()
        params["startDate"] = DateFormatUtils.PPLUS_DATE_FORMAT.format(PplusCommonUtil.setServerTimeZone(DateFormatUtils.PPLUS_DATE_FORMAT.parse(mStart)))
        params["endDate"] = DateFormatUtils.PPLUS_DATE_FORMAT.format(PplusCommonUtil.setServerTimeZone(DateFormatUtils.PPLUS_DATE_FORMAT.parse(mEnd)))
        showProgress("")
        ApiBuilder.create().getCommission(params).setCallback(object : PplusCallback<NewResultResponse<Double>>{
            override fun onResponse(call: Call<NewResultResponse<Double>>?, response: NewResultResponse<Double>?) {
                hideProgress()
                if(response?.result != null){
                    binding.textPartnerCommissionConfigDurationPrice.text = FormatUtil.getMoneyTypeFloat(response.result.toString())
                }else{
                    binding.textPartnerCommissionConfigDurationPrice.text = "0"
                }

                mPaging = 1
                getList(mPaging)
            }

            override fun onFailure(call: Call<NewResultResponse<Double>>?, t: Throwable?, response: NewResultResponse<Double>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun getList(page: Int) {
        val params = HashMap<String, String>()
        params["startDate"] = DateFormatUtils.PPLUS_DATE_FORMAT.format(PplusCommonUtil.setServerTimeZone(DateFormatUtils.PPLUS_DATE_FORMAT.parse(mStart)))
        params["endDate"] = DateFormatUtils.PPLUS_DATE_FORMAT.format(PplusCommonUtil.setServerTimeZone(DateFormatUtils.PPLUS_DATE_FORMAT.parse(mEnd)))
        params["paging[page]"] = page.toString()
        params["paging[limit]"] = "20"
        params["order[][column]"] = "seqNo"
        params["order[][dir]"] = mDir
        showProgress("")
        mLockListView = true
        ApiBuilder.create().getHistoryCommissionList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<HistoryCommission>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<HistoryCommission>>>?, response: NewResultResponse<ListResultResponse<HistoryCommission>>?) {
                hideProgress()
                if (response?.result != null) {
                    if (page == 1) {
                        mAdapter!!.clear()

                        mTotalCount = response.result!!.total!!
                        if (mTotalCount > 0) {
                            binding.layoutPartnerCommissionConfigNotExist.visibility = View.GONE
                            binding.layoutPartnerCommissionConfigExist.visibility = View.VISIBLE
                        } else {
                            binding.layoutPartnerCommissionConfigNotExist.visibility = View.VISIBLE
                            binding.layoutPartnerCommissionConfigExist.visibility = View.GONE
                        }
                    }

                    mLockListView = false

                    if (response.result!!.list != null) {
                        val dataList = response.result!!.list!!
                        mAdapter!!.addAll(dataList)
                    }

                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<HistoryCommission>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<HistoryCommission>>?) {
                hideProgress()
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_partner_commission_config), ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressedDispatcher.onBackPressed()
                    }

                    else -> {}
                }
            }
        }
    }
}