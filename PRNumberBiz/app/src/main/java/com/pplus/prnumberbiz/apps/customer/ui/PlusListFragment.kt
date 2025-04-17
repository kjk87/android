package com.pplus.prnumberbiz.apps.customer.ui


import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import androidx.annotation.DimenRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.pple.pplus.utils.part.format.FormatUtil
import com.pple.pplus.utils.part.logs.LogUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.Const

import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment
import com.pplus.prnumberbiz.apps.group.ui.GroupNameEditActivity
import com.pplus.prnumberbiz.apps.main.data.PlusAdapter
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Fan
import com.pplus.prnumberbiz.core.network.model.dto.Group
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import kotlinx.android.synthetic.main.fragment_plus_list.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.ArrayList
import java.util.HashMap

// TODO: Rename parameter arguments, choose names that match
// the mapFragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PlusListFragment.newInstance] factory method to
 * create an instance of this mapFragment.
 *
 */
class PlusListFragment : BaseFragment<BaseActivity>() {
    override fun getLayoutResourceId(): Int {
        return R.layout.fragment_plus_list
    }

    override fun initializeView(container: View?) {

    }

    private var mPage = 1
    private var mTotalCount = 0
    private var mLockListView = true
    private var mSearch: String? = null
    private var mAdapter: PlusAdapter? = null
    private var mLayoutManager: LinearLayoutManager? = null

    override fun init() {
        mLayoutManager = LinearLayoutManager(activity)
        mAdapter = PlusAdapter(activity!!)
        recycler_plus.layoutManager = mLayoutManager
        recycler_plus.adapter = mAdapter
        if (recycler_plus.itemDecorationCount == 0) {
            recycler_plus.addItemDecoration(CustomItemOffsetDecoration(activity!!, R.dimen.height_60, R.dimen.height_60))
        }

        recycler_plus.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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
                        mPage++
                        listCall(mPage)
                    }
                }
            }
        })

        text_plus_group_not_exist.visibility = View.GONE
        layout_plus_not_exist.visibility = View.GONE
//        layout_plus.visibility = View.GONE

        if (group!!.isDefaultGroup) {
            text_plus_group_config.visibility = View.GONE
            layout_plus_not_exist.setOnClickListener {

            }
        } else {
            text_plus_group_config.visibility = View.VISIBLE

            text_plus_group_not_exist.setOnClickListener {
                val intent = Intent(activity, SelectCustomerConfigActivity::class.java)
                intent.putExtra(Const.GROUP, group)
                intent.putExtra(Const.KEY, Const.PLUS_GROUP_ADD)
                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                activity!!.startActivityForResult(intent, Const.REQ_GROUP_CONFIG)
            }
        }

        text_plus_group_config.setOnClickListener {
            val builder = AlertBuilder.Builder()
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_BOTTOM)
            builder.setContents(*arrayOf(getString(R.string.word_delete_group), getString(R.string.word_change_groupName), getString(R.string.word_add_group_member), getString(R.string.word_delete_group_member)))
            builder.setLeftText(getString(R.string.word_cancel))
            builder.setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                    when (event_alert) {
                        AlertBuilder.EVENT_ALERT.LIST -> {
                            when (event_alert.getValue()) {
                                1->{
                                    val builder = AlertBuilder.Builder()
                                    builder.setTitle(getString(R.string.word_notice_alert))
                                    builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
                                    builder.addContents(AlertData.MessageData(getString(R.string.format_msg_delete_group1, getString(R.string.word_plus_customer)), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
                                    builder.addContents(AlertData.MessageData(getString(R.string.format_msg_delete_group2, group!!.name), AlertBuilder.MESSAGE_TYPE.TEXT, 1))
                                    builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
                                    builder.setOnAlertResultListener(object : OnAlertResultListener {

                                        override fun onCancel() {

                                        }

                                        override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                                            when (event_alert) {
                                                AlertBuilder.EVENT_ALERT.RIGHT//그룹삭제
                                                -> {
                                                    val params = HashMap<String, String>()
                                                    params["no"] = "" + group!!.no
                                                    showProgress("")
                                                    ApiBuilder.create().deleteFanGroup(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {

                                                        override fun onResponse(call: Call<NewResultResponse<Any>>, response: NewResultResponse<Any>) {
                                                            hideProgress()
                                                            if(!isAdded){
                                                                return
                                                            }

                                                            if(parentFragment is PlusFragment){
                                                                (parentFragment as PlusFragment).mCurrentPosition = 0
                                                                (parentFragment as PlusFragment).getGroupAll()
                                                            }

                                                        }

                                                        override fun onFailure(call: Call<NewResultResponse<Any>>, t: Throwable, response: NewResultResponse<Any>) {

                                                        }
                                                    }).build().call()
                                                }
                                            }
                                        }
                                    }).builder().show(activity)
                                }
                                2->{
                                    var groupList:MutableList<Group>? = null
                                    if(parentFragment is PlusFragment){
                                        groupList = (parentFragment as PlusFragment).mGroupList
                                    }

                                    val intent = Intent(activity, GroupNameEditActivity::class.java)
                                    intent.putExtra(Const.DATA, group)
                                    intent.putParcelableArrayListExtra(Const.GROUP, groupList as ArrayList<Group>)
                                    intent.putExtra(Const.KEY, EnumData.CustomerType.plus)
                                    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    activity!!.startActivityForResult(intent, Const.REQ_GROUP_CONFIG)
                                }
                                3,4->{
                                    val intent = Intent(activity, SelectCustomerConfigActivity::class.java)
                                    intent.putExtra(Const.GROUP, group)
                                    when (event_alert.getValue()) {
                                        3 -> intent.putExtra(Const.KEY, Const.PLUS_GROUP_ADD)
                                        4 -> intent.putExtra(Const.KEY, Const.PLUS_GROUP_DEL)
                                    }
                                    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    activity!!.startActivityForResult(intent, Const.REQ_GROUP_CONFIG)
                                }
                            }

                        }
                    }
                }
            }).builder().show(activity)
        }

        getCount()
    }

    private inner class CustomItemOffsetDecoration(private val mItemOffset: Int, private val mTopOffset: Int) : RecyclerView.ItemDecoration() {

        constructor(context: Context, @DimenRes itemOffsetId: Int, @DimenRes topOffsetId: Int) : this(context.resources.getDimensionPixelSize(itemOffsetId), context.resources.getDimensionPixelSize(topOffsetId)) {}

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

            super.getItemOffsets(outRect, view, parent, state)

            val position = parent.getChildAdapterPosition(view)
            if (position == 0) {
                outRect.set(0, mTopOffset, 0, mItemOffset)
            } else {
                outRect.set(0, 0, 0, mItemOffset)
            }

        }
    }

    private fun getCount() {

        val params = HashMap<String, String>()
        params["no"] = group!!.no.toString()
        if (StringUtils.isNotEmpty(mSearch)) {
            params["search"] = mSearch!!
        }

        ApiBuilder.create().getFanCount(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {

            override fun onResponse(call: Call<NewResultResponse<Int>>, response: NewResultResponse<Int>) {
                if (!isAdded) {
                    return
                }
                mTotalCount = response.data

                if (mTotalCount > 0) {
                    layout_plus_not_exist.visibility = View.GONE
                } else {
                    layout_plus_not_exist.visibility = View.VISIBLE
                }

                LogUtil.e(LOG_TAG, group!!.name)

                if (group!!.isDefaultGroup) {
                    text_plus_total_count.text = getString(R.string.format_total_count, getString(R.string.word_my_plus), FormatUtil.getMoneyType(mTotalCount.toString()))
                } else {
                    text_plus_total_count.text = getString(R.string.format_total_count, group!!.name, FormatUtil.getMoneyType(mTotalCount.toString()))
                }
                mAdapter!!.clear()
                mPage = 1
                listCall(mPage)
            }

            override fun onFailure(call: Call<NewResultResponse<Int>>, t: Throwable, response: NewResultResponse<Int>) {

            }
        }).build().call()
    }

    private fun listCall(page: Int) {

        mLockListView = true
        val params = HashMap<String, String>()
        params["no"] = group!!.no.toString()
        params["pg"] = page.toString()
        if (StringUtils.isNotEmpty(mSearch)) {
            params["search"] = mSearch!!
        }
        showProgress("")
        ApiBuilder.create().getFanList(params).setCallback(object : PplusCallback<NewResultResponse<Fan>> {
            override fun onResponse(call: Call<NewResultResponse<Fan>>?, response: NewResultResponse<Fan>?) {

                hideProgress()
                if (!isAdded) {
                    return
                }

                mLockListView = false

                mAdapter!!.addAll(response!!.datas)
            }

            override fun onFailure(call: Call<NewResultResponse<Fan>>?, t: Throwable?, response: NewResultResponse<Fan>?) {

            }
        }).build().call()

    }

    override fun getPID(): String {
        return ""
    }

    var group: Group? = null

    fun setGroupData(group: Group){
        this.group = group
        init()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        LogUtil.e(LOG_TAG, "onCreate")
        super.onCreate(savedInstanceState)
        arguments?.let {
            group = it.getParcelable(ARG_PARAM1)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(group: Group) =
                PlusListFragment().apply {

                    LogUtil.e(LOG_TAG, "newInstance")
                    arguments = Bundle().apply {
                        putParcelable(ARG_PARAM1, group)
                    }
                }
    }
}
