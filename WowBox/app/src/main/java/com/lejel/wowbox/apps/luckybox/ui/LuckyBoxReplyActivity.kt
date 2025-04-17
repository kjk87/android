package com.lejel.wowbox.apps.luckybox.ui

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.builder.AlertBuilder
import com.lejel.wowbox.apps.common.builder.OnAlertResultListener
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.luckybox.data.LuckyBoxReplyAdapter
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.LuckyBoxPurchaseItem
import com.lejel.wowbox.core.network.model.dto.LuckyBoxReply
import com.lejel.wowbox.core.network.model.dto.LuckyBoxReview
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.core.util.ToastUtil
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import com.lejel.wowbox.core.network.model.response.ListResultResponse
import com.lejel.wowbox.databinding.ActivityLuckyBoxReplyBinding
import retrofit2.Call

class LuckyBoxReplyActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityLuckyBoxReplyBinding

    override fun getLayoutView(): View {
        binding = ActivityLuckyBoxReplyBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    private var mAdapter: LuckyBoxReplyAdapter? = null
    private var mTotalCount: Int = 0
    private var mLockListView = false
    private var mPaging = 1
    private var mLayoutManager: LinearLayoutManager? = null

    var mLuckyBoxReview: LuckyBoxReview? = null
    var mLuckyBoxPurchaseItem: LuckyBoxPurchaseItem? = null

    override fun initializeView(savedInstanceState: Bundle?) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(Activity.OVERRIDE_TRANSITION_OPEN, R.anim.view_up, R.anim.fix)
            overrideActivityTransition(Activity.OVERRIDE_TRANSITION_CLOSE, R.anim.fix, R.anim.view_down)
        }

        mLuckyBoxPurchaseItem = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, LuckyBoxPurchaseItem::class.java)

        if (mLuckyBoxPurchaseItem == null) {
            mLuckyBoxReview = PplusCommonUtil.getParcelableExtra(intent, Const.REVIEW, LuckyBoxReview::class.java)
        }

        val data = Intent()
        data.putExtra(Const.REVIEW, mLuckyBoxReview)
        data.putExtra(Const.DATA, mLuckyBoxPurchaseItem)
        data.putExtra(Const.POSITION, intent.getIntExtra(Const.POSITION, -1))
        setResult(RESULT_OK, data)


        mLayoutManager = LinearLayoutManager(this)
        binding.recyclerEventReply.layoutManager = mLayoutManager!!
        mAdapter = LuckyBoxReplyAdapter()
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
                    if (totalItemCount < mTotalCount && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
                        mPaging++
                        listCall(mPaging)
                    }
                }
            }
        })

        mAdapter!!.setOnItemClickListener(object : LuckyBoxReplyAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {

                if (!LoginInfoManager.getInstance().isMember()) {
                    return
                }

                val item = mAdapter!!.getItem(position)
                if (item.userKey == LoginInfoManager.getInstance().member!!.userKey) {
                    val builder = AlertBuilder.Builder()
                    builder.setContents(getString(R.string.word_modified), getString(R.string.word_delete))
                    builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(object : OnAlertResultListener {

                        override fun onCancel() {

                        }

                        override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                            when (event_alert.value) {
                                1 -> {
                                    val intent = Intent(this@LuckyBoxReplyActivity, LuckyBoxReplyEditActivity::class.java)
                                    intent.putExtra(Const.DATA, item)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    modifyLauncher.launch(intent)
                                }

                                2 -> {
                                    delete(item.seqNo!!)
                                }
                            }
                        }
                    }).builder().show(this@LuckyBoxReplyActivity)
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

            val params = LuckyBoxReply()
            params.reply = reply
            params.userKey = LoginInfoManager.getInstance().member!!.userKey
            if (mLuckyBoxPurchaseItem != null) {
                params.luckyboxPurchaseItemSeqNo = mLuckyBoxPurchaseItem!!.seqNo
            } else {
                params.luckyboxReviewSeqNo = mLuckyBoxReview!!.seqNo
            }

            showProgress("")
            ApiBuilder.create().insertLuckyBoxReply(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                    hideProgress()
                    binding.editEventReply.setText("")
                    mPaging = 1
                    listCall(mPaging)
                }

                override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                    hideProgress()
                }
            }).build().call()
        }

        mPaging = 1
        listCall(mPaging)
    }

    private fun delete(seqNo: Long) {
        showProgress("")
        ApiBuilder.create().deleteLuckyBoxReply(seqNo).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()
                ToastUtil.show(this@LuckyBoxReplyActivity, R.string.msg_delete_comment)
                mPaging = 1
                listCall(mPaging)
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun listCall(page: Int) {

        mLockListView = true
        val params = HashMap<String, String>()

        if (mLuckyBoxPurchaseItem != null) {
            params["luckyboxPurchaseItemSeqNo"] = mLuckyBoxPurchaseItem!!.seqNo.toString()
        }else{
            params["luckyboxReviewSeqNo"] = mLuckyBoxReview!!.seqNo.toString()
        }
        params["paging[page]"] = page.toString()
        params["paging[limit]"] = "20"
        params["order[][column]"] = "seqNo"
        params["order[][dir]"] = "DESC"
        showProgress("")
        ApiBuilder.create().getLuckyBoxReplyList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<LuckyBoxReply>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<LuckyBoxReply>>>?, response: NewResultResponse<ListResultResponse<LuckyBoxReply>>?) {
                hideProgress()

                if (response?.result != null) {
                    if (page == 1) {

                        mTotalCount = response.result!!.total!!
                        binding.textEventReplyCount.text = PplusCommonUtil.fromHtml(getString(R.string.html_total_count2, FormatUtil.getMoneyType(mTotalCount.toString())))

                        if (mTotalCount > 0) {
                            binding.layoutEventReplyNotExist.visibility = View.GONE
                        } else {
                            binding.layoutEventReplyNotExist.visibility = View.VISIBLE
                        }

                        mAdapter!!.clear()
                    }

                    mLockListView = false

                    if (response.result!!.list != null) {
                        val dataList = response.result!!.list!!
                        mAdapter!!.addAll(dataList)
                    }

                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<LuckyBoxReply>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<LuckyBoxReply>>?) {
                hideProgress()
            }
        }).build().call()
    }


    val modifyLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        mPaging = 1
        listCall(mPaging)
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