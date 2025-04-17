package com.pplus.luckybol.apps.buff.ui

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.buff.data.BuffPostReplyAdapter
import com.pplus.luckybol.apps.common.builder.AlertBuilder
import com.pplus.luckybol.apps.common.builder.OnAlertResultListener
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.event.ui.EventReplyEditActivity
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.BuffPost
import com.pplus.luckybol.core.network.model.dto.BuffPostReply
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.network.model.response.SubResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.core.util.ToastUtil
import com.pplus.luckybol.databinding.ActivityBuffPostReplyBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call

class BuffPostReplyActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityBuffPostReplyBinding

    override fun getLayoutView(): View {
        binding = ActivityBuffPostReplyBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    private lateinit var mBuffPost: BuffPost
    private lateinit var mAdapter: BuffPostReplyAdapter
    private lateinit var mLayoutManager: LinearLayoutManager
    private var mIsLast = false
    private var mLockListView = false
    private var mPaging = 0

    override fun initializeView(savedInstanceState: Bundle?) {
        mBuffPost = intent.getParcelableExtra(Const.DATA)!!

        mAdapter = BuffPostReplyAdapter()
        mLayoutManager = LinearLayoutManager(this)
        binding.recyclerBuffPostReply.layoutManager = mLayoutManager
        binding.recyclerBuffPostReply.adapter = mAdapter
        binding.recyclerBuffPostReply.addItemDecoration(CustomItemOffsetDecoration(this, R.dimen.height_200))

        binding.recyclerBuffPostReply.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            internal var pastVisibleItems: Int = 0
            internal var visibleItemCount: Int = 0
            internal var totalItemCount: Int = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                super.onScrolled(recyclerView, dx, dy)

                visibleItemCount = mLayoutManager.childCount
                totalItemCount = mLayoutManager.itemCount
                pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition()
                if (!mLockListView) {
                    if (!mIsLast && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
                        mPaging++
                        listCall(mPaging)
                    }
                }
            }
        })

        mAdapter.setOnItemClickListener(object : BuffPostReplyAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {

                if(!LoginInfoManager.getInstance().isMember){
                    return
                }

                val item = mAdapter.getItem(position)
                if (item.memberSeqNo == LoginInfoManager.getInstance().user.no) {
                    val builder = AlertBuilder.Builder()
                    builder.setContents(getString(R.string.word_modified), getString(R.string.word_delete))
                    builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(object : OnAlertResultListener {

                        override fun onCancel() {

                        }

                        override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                            when (event_alert.getValue()) {
                                1 -> {
                                    val intent = Intent(this@BuffPostReplyActivity, EventReplyEditActivity::class.java)
                                    intent.putExtra(Const.EVENT_REPLY, item)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    modifyLauncher.launch(intent)
                                }

                                2 -> {
                                    delete(item.seqNo!!)
                                }
                            }
                        }
                    }).builder().show(this@BuffPostReplyActivity)
                }
            }
        })

        binding.editBuffPostReply.setSingleLine()
        binding.editBuffPostReplyInsert.setOnClickListener {

            if (!PplusCommonUtil.loginCheck(this, null)) {
                return@setOnClickListener
            }

            val reply = binding.editBuffPostReply.text.toString().trim()

            if (StringUtils.isEmpty(reply)) {
                return@setOnClickListener
            }

            val params = HashMap<String, String>()
            params["buffPostSeqNo"] = mBuffPost.seqNo.toString()
            params["reply"] = reply

            showProgress("")
            ApiBuilder.create().insertBuffPostReply(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                override fun onResponse(call: Call<NewResultResponse<Any>>?,
                                        response: NewResultResponse<Any>?) {
                    hideProgress()
                    binding.editBuffPostReply.setText("")
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

    private inner class CustomItemOffsetDecoration(private val mLastOffset: Int) : RecyclerView.ItemDecoration() {

        constructor(context: Context,
                    @DimenRes lastOffsetId: Int) : this(context.resources.getDimensionPixelSize(lastOffsetId)) {
        }

        override fun getItemOffsets(outRect: Rect,
                                    view: View,
                                    parent: RecyclerView,
                                    state: RecyclerView.State) {

            super.getItemOffsets(outRect, view, parent, state)

            val position = parent.getChildAdapterPosition(view)
            if (mAdapter.itemCount > 0 && position == mAdapter.itemCount - 1) {
                outRect.bottom = mLastOffset
            }
        }
    }

    private fun listCall(page: Int) {
        mLockListView = true
        val params = HashMap<String, String>()
        params["page"] = page.toString()
        showProgress("")
        ApiBuilder.create().getBuffPostReplyList(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<BuffPostReply>>> {
            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<BuffPostReply>>>?,
                                    response: NewResultResponse<SubResultResponse<BuffPostReply>>?) {

                hideProgress()
                if (response?.data != null) {
                    mIsLast = response.data!!.last!!
                    if (response.data!!.first!!) {
                        mAdapter.clear()

                        val totalCount = response.data!!.totalElements!!

                        if (totalCount > 0) {
                            binding.layoutBuffPostReplyNotExist.visibility = View.GONE
                        } else {
                            binding.layoutBuffPostReplyNotExist.visibility = View.VISIBLE
                        }

                        binding.textBuffPostReplyCount.text = getString(R.string.format_reply_count, FormatUtil.getMoneyType(totalCount.toString()))
                    }
                    mLockListView = false

                    val dataList = response.data!!.content!!
                    mAdapter.addAll(dataList)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<BuffPostReply>>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<SubResultResponse<BuffPostReply>>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun delete(seqNo:Long){
        val params = HashMap<String, String>()
        params["buffPostReplySeqNo"] = seqNo.toString()
        showProgress("")
        ApiBuilder.create().deleteBuffPostReply(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?,
                                    response: NewResultResponse<Any>?) {
                hideProgress()
                ToastUtil.show(this@BuffPostReplyActivity, R.string.msg_delete_comment)
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

    val modifyLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        mPaging = 0
        listCall(mPaging)
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_reg_reply), ToolbarOption.ToolbarMenu.LEFT)

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