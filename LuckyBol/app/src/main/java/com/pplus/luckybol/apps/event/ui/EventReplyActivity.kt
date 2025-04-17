package com.pplus.luckybol.apps.event.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.builder.AlertBuilder
import com.pplus.luckybol.apps.common.builder.OnAlertResultListener
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.event.data.EventReplyAdapter
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.EventReply
import com.pplus.luckybol.core.network.model.dto.EventReview
import com.pplus.luckybol.core.network.model.dto.EventWin
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.network.model.response.SubResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.core.util.ToastUtil
import com.pplus.luckybol.databinding.ActivityEventReplyBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call

class EventReplyActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityEventReplyBinding

    override fun getLayoutView(): View {
        binding = ActivityEventReplyBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    private var mAdapter: EventReplyAdapter? = null
    private var mIsLast = false
    private var mSort = "seqNo,asc"
    private var mTotalCount: Int = 0
    private var mLockListView = false
    private var mPaging = 0
    private var mLayoutManager: LinearLayoutManager? = null

    var mEventReview: EventReview? = null
    var mEventWin: EventWin? = null

    override fun initializeView(savedInstanceState: Bundle?) {

        mEventWin = intent.getParcelableExtra(Const.EVENT_WIN)

        if (mEventWin == null) {
            mEventReview = intent.getParcelableExtra(Const.EVENT_REVIEW)
        }

        val data = Intent()
        data.putExtra(Const.EVENT_REVIEW, mEventReview)
        data.putExtra(Const.EVENT_WIN, mEventWin)
        data.putExtra(Const.POSITION, intent.getIntExtra(Const.POSITION, -1))
        setResult(RESULT_OK, data)


        mLayoutManager = LinearLayoutManager(this)
        binding.recyclerEventReply.layoutManager = mLayoutManager!!
        mAdapter = EventReplyAdapter()
        binding.recyclerEventReply.adapter = mAdapter

        binding.recyclerEventReply.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        mAdapter!!.setOnItemClickListener(object : EventReplyAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {

                if(!LoginInfoManager.getInstance().isMember){
                    return
                }

                val item = mAdapter!!.getItem(position)
                if (item.memberSeqNo == LoginInfoManager.getInstance().user.no) {
                    val builder = AlertBuilder.Builder()
                    builder.setContents(getString(R.string.word_modified), getString(R.string.word_delete))
                    builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(object : OnAlertResultListener {

                        override fun onCancel() {

                        }

                        override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                            when (event_alert.getValue()) {
                                1 -> {
                                    val intent = Intent(this@EventReplyActivity, EventReplyEditActivity::class.java)
                                    intent.putExtra(Const.EVENT_REPLY, item)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    modifyLauncher.launch(intent)
                                }

                                2 -> {
                                    delete(item.seqNo!!)
                                }
                            }
                        }
                    }).builder().show(this@EventReplyActivity)
                }
            }
        })

        binding.editEventReply.setSingleLine()
        binding.editEventReplyInsert.setOnClickListener {

            if (!PplusCommonUtil.loginCheck(this, null)) {
                return@setOnClickListener
            }

            val reply = binding.editEventReply.text.toString().trim()

            if (StringUtils.isEmpty(reply)) {
                return@setOnClickListener
            }

            val params = EventReply()
            params.reply = reply
            params.memberSeqNo = LoginInfoManager.getInstance().user.no
            if (mEventWin != null) {
                params.eventSeqNo = mEventWin!!.event!!.no
                params.eventWinSeqNo = mEventWin!!.winNo
                params.eventWinId = mEventWin!!.id
            } else {
                params.eventReviewSeqNo = mEventReview!!.seqNo
            }

            showProgress("")
            ApiBuilder.create().insertEventReply(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                override fun onResponse(call: Call<NewResultResponse<Any>>?,
                                        response: NewResultResponse<Any>?) {
                    hideProgress()
                    binding.editEventReply.setText("")
                    mPaging = 0
                    listCall(mPaging)
                }

                override fun onFailure(call: Call<NewResultResponse<Any>>?,
                                       t: Throwable?,
                                       response: NewResultResponse<Any>?) {
                    hideProgress()
                }
            }).build().call()
        }

        mPaging = 0
        listCall(mPaging)
    }

    private fun delete(seqNo:Long){
        val params = HashMap<String, String>()
        params["seqNo"] = seqNo.toString()
        showProgress("")
        ApiBuilder.create().deleteEventReply(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?,
                                    response: NewResultResponse<Any>?) {
                hideProgress()
                ToastUtil.show(this@EventReplyActivity, R.string.msg_delete_comment)
                mPaging = 0
                listCall(mPaging)
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<Any>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun listCall(page: Int) {

        mLockListView = true
        val params = HashMap<String, String>()

        if (mEventWin != null) {
            params["eventWinId"] = mEventWin!!.id.toString()
            params["sort"] = mSort
            params["page"] = page.toString()
            showProgress("")
            ApiBuilder.create().getEventReplyListByEventWinId(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<EventReply>>> {
                override fun onResponse(call: Call<NewResultResponse<SubResultResponse<EventReply>>>?,
                                        response: NewResultResponse<SubResultResponse<EventReply>>?) {
                    hideProgress()
                    if (response?.data != null) {
                        mIsLast = response.data!!.last!!
                        if (response.data!!.first!!) {
                            mTotalCount = response.data!!.totalElements!!
                            binding.textEventReplyCount.text = PplusCommonUtil.fromHtml(getString(R.string.html_total_count2, FormatUtil.getMoneyType(mTotalCount.toString())))

                            if(mTotalCount > 0){
                                binding.layoutEventReplyNotExist.visibility = View.GONE
                            }else{
                                binding.layoutEventReplyNotExist.visibility = View.VISIBLE
                            }

                            mAdapter!!.clear()
                        }

                        mLockListView = false

                        val dataList = response.data!!.content!!
                        mAdapter!!.addAll(dataList)
                    }
                }

                override fun onFailure(call: Call<NewResultResponse<SubResultResponse<EventReply>>>?,
                                       t: Throwable?,
                                       response: NewResultResponse<SubResultResponse<EventReply>>?) {
                    hideProgress()
                }
            }).build().call()
        } else {
            params["eventReviewSeqNo"] = mEventReview!!.seqNo.toString()
            params["sort"] = mSort
            params["page"] = page.toString()
            showProgress("")
            ApiBuilder.create().getEventReplyListByEventReviewSeqNo(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<EventReply>>> {
                override fun onResponse(call: Call<NewResultResponse<SubResultResponse<EventReply>>>?,
                                        response: NewResultResponse<SubResultResponse<EventReply>>?) {
                    hideProgress()
                    if (response?.data != null) {
                        mIsLast = response.data!!.last!!
                        if (response.data!!.first!!) {
                            mTotalCount = response.data!!.totalElements!!
                            binding.textEventReplyCount.text = PplusCommonUtil.fromHtml(getString(R.string.html_total_count2, FormatUtil.getMoneyType(mTotalCount.toString())))

                            mAdapter!!.clear()
                        }

                        mLockListView = false

                        val dataList = response.data!!.content!!
                        mAdapter!!.addAll(dataList)
                    }
                }

                override fun onFailure(call: Call<NewResultResponse<SubResultResponse<EventReply>>>?,
                                       t: Throwable?,
                                       response: NewResultResponse<SubResultResponse<EventReply>>?) {
                    hideProgress()
                }
            }).build().call()
        }

    }

    val modifyLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        mPaging = 0
        listCall(mPaging)
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_reply), ToolbarOption.ToolbarMenu.LEFT)

        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
                else -> {}
            }
        }
    }
}