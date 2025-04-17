package com.pplus.luckybol.apps.buff.ui

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.buff.data.FriendAdapter
import com.pplus.luckybol.apps.common.builder.AlertBuilder
import com.pplus.luckybol.apps.common.builder.OnAlertResultListener
import com.pplus.luckybol.apps.common.builder.data.AlertData
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.BuffMember
import com.pplus.luckybol.core.network.model.dto.BuffParam
import com.pplus.luckybol.core.network.model.dto.Contact
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.network.model.response.SubResultResponse
import com.pplus.luckybol.databinding.ActivityBuffInviteBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.format.FormatUtil
import retrofit2.Call

class BuffInviteActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityBuffInviteBinding

    override fun getLayoutView(): View {
        binding = ActivityBuffInviteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    private lateinit var mAdapter: FriendAdapter
    private lateinit var mLayoutManager: LinearLayoutManager
    private var mIsLast = false
    private var mLockListView = false
    private var mPaging = 0

    override fun initializeView(savedInstanceState: Bundle?) {
        mAdapter = FriendAdapter()
        mLayoutManager = LinearLayoutManager(this)
        binding.recyclerBuffInviteFriend.layoutManager = mLayoutManager
        binding.recyclerBuffInviteFriend.adapter = mAdapter
        binding.recyclerBuffInviteFriend.addItemDecoration(CustomItemOffsetDecoration(this, R.dimen.height_200))

        binding.recyclerBuffInviteFriend.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        mAdapter.listener = object : FriendAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                if (mAdapter.mCheckList.isEmpty()) {
                    binding.textBuffInvite.setBackgroundColor(ResourceUtil.getColor(this@BuffInviteActivity, R.color.color_c0c6cc))
                    binding.textBuffInvite.text = getString(R.string.msg_buff_invite)
                    binding.textBuffInvite.isEnabled = false
                } else {
                    binding.textBuffInvite.setBackgroundColor(ResourceUtil.getColor(this@BuffInviteActivity, R.color.color_fc5c57))
                    binding.textBuffInvite.text = getString(R.string.format_buff_invite, FormatUtil.getMoneyType(mAdapter.mCheckList.size.toString()))
                    binding.textBuffInvite.isEnabled = true
                }
            }
        }

        binding.textBuffInvite.setOnClickListener {
            if(mAdapter.mCheckList.isEmpty()){
                showAlert(R.string.msg_select_invite_friend)
                return@setOnClickListener
            }

            val builder = AlertBuilder.Builder()
            builder.setTitle(getString(R.string.format_alert_buff_invite_title, mAdapter.mCheckList.size.toString()))
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
            builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_buff_invite_desc), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
            builder.setLeftText(getString(R.string.msg_next_time2)).setRightText(getString(R.string.word_buff_invite))
            builder.setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {}

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                    when (event_alert) {
                        AlertBuilder.EVENT_ALERT.RIGHT -> {
                            val buffInvite = BuffParam()
                            buffInvite.buffSeqNo = mAdapter.mBuff!!.seqNo
                            buffInvite.inviteList = mAdapter.mCheckList
                            showProgress("")
                            ApiBuilder.create().buffInvite(buffInvite).setCallback(object : PplusCallback<NewResultResponse<Any>>{
                                override fun onResponse(call: Call<NewResultResponse<Any>>?,
                                                        response: NewResultResponse<Any>?) {
                                    hideProgress()
                                    showAlert(R.string.msg_buff_invite_complete)
                                    finish()
                                }

                                override fun onFailure(call: Call<NewResultResponse<Any>>?,
                                                       t: Throwable?,
                                                       response: NewResultResponse<Any>?) {
                                    hideProgress()
                                }
                            }).build().call()
                        }
                        else -> {}
                    }
                }
            }).builder().show(this)


        }

        binding.textBuffInvite.setBackgroundColor(ResourceUtil.getColor(this@BuffInviteActivity, R.color.color_c0c6cc))
        binding.textBuffInvite.text = getString(R.string.msg_buff_invite)
        binding.textBuffInvite.isEnabled = false

        getMyBuff()
    }

    private inner class CustomItemOffsetDecoration(private val mLastOffset: Int) : RecyclerView.ItemDecoration() {

        constructor(context: Context, @DimenRes lastOffsetId: Int) : this(context.resources.getDimensionPixelSize(lastOffsetId)) {}

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

            super.getItemOffsets(outRect, view, parent, state)

            val position = parent.getChildAdapterPosition(view)
            if (mAdapter.itemCount > 0 && position == mAdapter.itemCount - 1) {
                outRect.bottom = mLastOffset
            }
        }
    }

    private fun getMyBuff() {
        showProgress("")
        ApiBuilder.create().getBuffMember().setCallback(object : PplusCallback<NewResultResponse<BuffMember>> {
            override fun onResponse(call: Call<NewResultResponse<BuffMember>>?,
                                    response: NewResultResponse<BuffMember>?) {

                hideProgress()
                if (response?.data != null && response.data!!.buff != null) {
                    val buff = response.data!!.buff!!
                    mAdapter.mBuff = buff
                    mPaging = 0
                    listCall(mPaging)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<BuffMember>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<BuffMember>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun listCall(page: Int) {
        mLockListView = true
        val params = HashMap<String, String>()
        params["page"] = page.toString()
        showProgress("")
        ApiBuilder.create().getFriendList(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<Contact>>> {
            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<Contact>>>?,
                                    response: NewResultResponse<SubResultResponse<Contact>>?) {

                hideProgress()
                if (response?.data != null) {
                    mIsLast = response.data!!.last!!
                    if (response.data!!.first!!) {
                        mAdapter.clear()

                        val totalCount = response.data!!.totalElements!!
                        if (totalCount > 0) {
                            binding.layoutBuffInviteFriendNotExist.visibility = View.GONE
                        } else {
                            binding.layoutBuffInviteFriendNotExist.visibility = View.VISIBLE
                        }

                        binding.textBuffInviteFriendCount.text = getString(R.string.format_friend_count, FormatUtil.getMoneyType(totalCount.toString()))
                    }
                    mLockListView = false

                    val dataList = response.data!!.content!!
                    mAdapter.addAll(dataList)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<Contact>>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<SubResultResponse<Contact>>?) {
                hideProgress()
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.msg_buff_invite), ToolbarOption.ToolbarMenu.LEFT)

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