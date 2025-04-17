package com.lejel.wowbox.apps.luckydraw.ui

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.builder.AlertBuilder
import com.lejel.wowbox.apps.common.builder.OnAlertResultListener
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.luckydraw.data.LuckyDrawWinReplyAdapter
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.LuckyDrawWin
import com.lejel.wowbox.core.network.model.dto.LuckyDrawWinReply
import com.lejel.wowbox.core.network.model.response.ListResultResponse
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityLuckyDrawWinReplyBinding
import retrofit2.Call

class LuckyDrawWinReplyActivity : BaseActivity(), ImplToolbar {
    private lateinit var binding: ActivityLuckyDrawWinReplyBinding

    override fun getLayoutView(): View {
        binding = ActivityLuckyDrawWinReplyBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    var mAdapter: LuckyDrawWinReplyAdapter? = null
    private lateinit var mLayoutManager: LinearLayoutManager
    private var mTotalCount = 0
    private var mLockListView = false
    private var mPaging = 1

    private lateinit var mLuckyDrawWin: LuckyDrawWin

    override fun initializeView(savedInstanceState: Bundle?) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(Activity.OVERRIDE_TRANSITION_OPEN, R.anim.view_up, R.anim.fix)
            overrideActivityTransition(Activity.OVERRIDE_TRANSITION_CLOSE, R.anim.fix, R.anim.view_down)
        }

        mLuckyDrawWin = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, LuckyDrawWin::class.java)!!

        binding.editLuckyDrawWinReply.setSingleLine()

        binding.textLuckyDrawWinReplyReg.setOnClickListener {
            val reply = binding.editLuckyDrawWinReply.text.toString().trim()
            if (StringUtils.isEmpty(reply)) {
                return@setOnClickListener
            }

            val params = HashMap<String, String>()
            params["reply"] = reply
            params["luckyDrawWinSeqNo"] = mLuckyDrawWin.seqNo.toString()

            showProgress("")
            ApiBuilder.create().postLuckyDrawWinReply(params).setCallback(object : PplusCallback<NewResultResponse<LuckyDrawWinReply>> {
                override fun onResponse(call: Call<NewResultResponse<LuckyDrawWinReply>>?, response: NewResultResponse<LuckyDrawWinReply>?) {
                    hideProgress()
                    binding.editLuckyDrawWinReply.setText("")
                    mPaging = 1
                    getList(mPaging)
                }

                override fun onFailure(call: Call<NewResultResponse<LuckyDrawWinReply>>?, t: Throwable?, response: NewResultResponse<LuckyDrawWinReply>?) {
                    hideProgress()
                    if(response?.code == 589){
                        showAlert(R.string.msg_can_not_use_cursing)
                    }
                }
            }).build().call()
        }

        mLayoutManager = LinearLayoutManager(this)
        mAdapter = LuckyDrawWinReplyAdapter()
        mAdapter!!.launcher = defaultLauncher
        binding.recyclerLuckyDrawWinReply.adapter = mAdapter
        binding.recyclerLuckyDrawWinReply.layoutManager = mLayoutManager

        mAdapter!!.listener = object : LuckyDrawWinReplyAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                if (LoginInfoManager.getInstance().isMember()) {
                    val data = mAdapter!!.getItem(position)
                    if (data.userKey == LoginInfoManager.getInstance().member!!.userKey) {
                        val builder = AlertBuilder.Builder()
                        builder.setContents(getString(R.string.word_modify), getString(R.string.word_delete))
                        builder.setRightText(getString(R.string.word_cancel))
                        builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_BOTTOM).setOnAlertResultListener(object : OnAlertResultListener {

                            override fun onCancel() {

                            }

                            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                                when (event_alert.value) {
                                    1 -> {
                                        val intent = Intent(this@LuckyDrawWinReplyActivity, LuckyDrawWinReplyEditActivity::class.java)
                                        intent.putExtra(Const.DATA, data)
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                        defaultLauncher.launch(intent)
                                    }

                                    2 -> {
                                        delete(data.seqNo!!)
                                    }
                                }

                            }
                        }).builder().show(this@LuckyDrawWinReplyActivity)
                    }
                }
            }
        }

        binding.recyclerLuckyDrawWinReply.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        mPaging = 1
        getList(mPaging)
    }

    private fun getList(page: Int) {
        val params = HashMap<String, String>()
        params["paging[page]"] = page.toString()
        params["paging[limit]"] = "20"
        params["luckyDrawWinSeqNo"] = mLuckyDrawWin.seqNo.toString()
        showProgress("")
        mLockListView = true
        ApiBuilder.create().getLuckyDrawWinReplyList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<LuckyDrawWinReply>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<LuckyDrawWinReply>>>?, response: NewResultResponse<ListResultResponse<LuckyDrawWinReply>>?) {
                hideProgress()
                if (response?.result != null) {
                    if (page == 1) {
                        mAdapter!!.clear()
                        mTotalCount = response.result!!.total!!
                        binding.textLuckyDrawWinReplyCount.text = getString(R.string.format_reply_count, FormatUtil.getMoneyType(mTotalCount.toString()))
                    }

                    if (mTotalCount > 0) {
                        binding.layoutLuckyDrawWinReplyNotExist.visibility = View.GONE
                    } else {
                        binding.layoutLuckyDrawWinReplyNotExist.visibility = View.VISIBLE
                    }

                    mLockListView = false

                    if (response.result!!.list != null) {
                        val dataList = response.result!!.list!!
                        mAdapter!!.addAll(dataList)
                    }

                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<LuckyDrawWinReply>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<LuckyDrawWinReply>>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun delete(seqNo: Long) {
        val params = HashMap<String, String>()
        params["seqNo"] = seqNo.toString()
        showProgress("")
        mLockListView = true
        ApiBuilder.create().deleteLuckyDrawWinReply(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()
                mPaging = 1
                getList(mPaging)
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {

            }
        }).build().call()
    }

    val defaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        mPaging = 1
        getList(mPaging)
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_reply), ToolbarOption.ToolbarMenu.LEFT)
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