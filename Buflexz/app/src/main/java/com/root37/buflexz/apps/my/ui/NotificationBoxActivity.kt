package com.root37.buflexz.apps.my.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.root37.buflexz.Const
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.builder.AlertBuilder
import com.root37.buflexz.apps.common.builder.OnAlertResultListener
import com.root37.buflexz.apps.common.builder.data.AlertData
import com.root37.buflexz.apps.common.toolbar.ImplToolbar
import com.root37.buflexz.apps.common.toolbar.OnToolbarListener
import com.root37.buflexz.apps.common.toolbar.ToolbarOption
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.apps.common.ui.custom.BottomItemOffsetDecoration
import com.root37.buflexz.apps.faq.ui.FaqActivity
import com.root37.buflexz.apps.main.ui.MainActivity
import com.root37.buflexz.apps.my.data.NotificationBoxAdapter
import com.root37.buflexz.apps.my.data.SwipeHelperCallback
import com.root37.buflexz.apps.notice.ui.NoticeActivity
import com.root37.buflexz.core.network.ApiBuilder
import com.root37.buflexz.core.network.model.dto.NotificationBox
import com.root37.buflexz.core.network.model.response.ListResultResponse
import com.root37.buflexz.core.network.model.response.NewResultResponse
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.ActivityNotificationBoxBinding
import retrofit2.Call

class NotificationBoxActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityNotificationBoxBinding

    override fun getLayoutView(): View {
        binding = ActivityNotificationBoxBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    private var mTotalCount = 0
    var mAdapter: NotificationBoxAdapter? = null
    private lateinit var mLayoutManager: LinearLayoutManager
    private var mLockListView = false
    private var mPaging = 1

    override fun initializeView(savedInstanceState: Bundle?) { // ItemTouchHelper.Callback 을 리사이클러뷰와 연결


        mLayoutManager = LinearLayoutManager(this)
        binding.recyclerNotificationBox.layoutManager = mLayoutManager
        mAdapter = NotificationBoxAdapter()
        binding.recyclerNotificationBox.adapter = mAdapter

        val swipeHelperCallback = SwipeHelperCallback().apply {
            setClamp(resources.getDimensionPixelSize(R.dimen.width_240).toFloat())
        }
        val itemTouchHelper = ItemTouchHelper(swipeHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerNotificationBox)

        binding.recyclerNotificationBox.addItemDecoration(BottomItemOffsetDecoration(this, R.dimen.height_47))
        binding.recyclerNotificationBox.setOnTouchListener { view, motionEvent ->
            swipeHelperCallback.removePreviousClamp(binding.recyclerNotificationBox)
            false
        }

        binding.recyclerNotificationBox.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        mAdapter!!.listener = object : NotificationBoxAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val item = mAdapter!!.getItem(position)
                read(item.seqNo!!)
                when (item.moveType) {
                    "inner" -> {
                        when (item.innerType) {

                            "main" -> {
                                val intent = Intent(this@NotificationBoxActivity, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                startActivity(intent)
                            }

                            "notice" -> {
                                val intent = Intent(this@NotificationBoxActivity, NoticeActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                startActivity(intent)
                            }

                            "faq" -> {
                                val intent = Intent(this@NotificationBoxActivity, FaqActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                startActivity(intent)
                            }
                        }

                    }

                    "outer" -> {
                        PplusCommonUtil.openChromeWebView(this@NotificationBoxActivity, item.outerUrl!!)
                    }
                }
            }

            override fun onItemDelete(position: Int) {

                val item = mAdapter!!.getItem(position)

                val builder = AlertBuilder.Builder()
                builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_delete_notification_box), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
                builder.setOnAlertResultListener(object : OnAlertResultListener {

                    override fun onCancel() {

                    }

                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                        when (event_alert) {
                            AlertBuilder.EVENT_ALERT.RIGHT -> {
                                val params = HashMap<String, String>()
                                params["seqNo"] = item.seqNo.toString()
                                showProgress("")
                                ApiBuilder.create().notificationBoxDelete(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                                    override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                                        hideProgress()

                                        mPaging = 1
                                        getList(mPaging)
                                    }

                                    override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                                        hideProgress()
                                    }
                                }).build().call()
                            }

                            else -> {

                            }
                        }
                    }
                }).builder().show(this@NotificationBoxActivity)
            }
        }

        mPaging = 1
        getList(mPaging)
    }

    private fun read(seqNo:Long){
        val params = HashMap<String, String>()
        params["seqNo"] = seqNo.toString()
        ApiBuilder.create().notificationBoxRead(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
            }
        }).build().call()
    }

    private fun getList(page: Int) {
        val params = HashMap<String, String>()
        params["paging[page]"] = page.toString()
        params["paging[limit]"] = "20"
        showProgress("")
        mLockListView = true
        ApiBuilder.create().getNotificationBoxList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<NotificationBox>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<NotificationBox>>>?, response: NewResultResponse<ListResultResponse<NotificationBox>>?) {
                hideProgress()
                if (response?.result != null) {
                    if (page == 1) {
                        mAdapter!!.clear()

                        mTotalCount = response.result!!.total!!
                        if (mTotalCount == 0) {
                            binding.layoutNotificationBoxNotExist.visibility = View.VISIBLE
                        } else {
                            binding.layoutNotificationBoxNotExist.visibility = View.GONE
                        }
                    }

                    mLockListView = false

                    if (response.result!!.list != null) {
                        val dataList = response.result!!.list!!
                        mAdapter!!.addAll(dataList)
                    }

                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<NotificationBox>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<NotificationBox>>?) {
                hideProgress()
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_alarm_box), ToolbarOption.ToolbarMenu.LEFT)
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