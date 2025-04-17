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
import android.view.inputmethod.EditorInfo
import com.pple.pplus.utils.part.format.FormatUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment
import com.pplus.prnumberbiz.apps.group.ui.GroupNameEditActivity
import com.pplus.prnumberbiz.apps.main.data.CustomerAdapter
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Customer
import com.pplus.prnumberbiz.core.network.model.dto.Group
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import kotlinx.android.synthetic.main.fragment_customer_list.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the mapFragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [CustomerListFragment.newInstance] factory method to
 * create an instance of this mapFragment.
 *
 */
class CustomerListFragment : BaseFragment<BaseActivity>() {

    override fun getPID(): String {
        return ""
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.fragment_customer_list
    }

    private var mPage = 1
    private var mTotalCount = 0
    private var mLockListView = true
    private var mSearch: String? = null
    private var mAdapter: CustomerAdapter? = null
    private var mLayoutManager: androidx.recyclerview.widget.LinearLayoutManager? = null

    override fun initializeView(container: View?) {

    }

    override fun init() {

        edit_customer_search.setSingleLine()
        edit_customer_search.imeOptions = EditorInfo.IME_ACTION_SEARCH
        edit_customer_search.setOnEditorActionListener { v, actionId, event ->

            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    search()
                }
            }

            return@setOnEditorActionListener true

        }
        image_customer_search.setOnClickListener {
            search()
        }

        mLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
        mAdapter = CustomerAdapter(activity!!)
        recycler_customer.layoutManager = mLayoutManager
        recycler_customer.adapter = mAdapter
        if (recycler_customer.itemDecorationCount == 0) {
            recycler_customer.addItemDecoration(CustomItemOffsetDecoration(activity!!, R.dimen.height_60, R.dimen.height_60))
        }

        recycler_customer.addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {

            internal var pastVisibleItems: Int = 0
            internal var visibleItemCount: Int = 0
            internal var totalItemCount: Int = 0

            override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {

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

        mAdapter!!.setOnDeleteListener(object : CustomerAdapter.ItemDeleteListener{
            override fun onItemDelete(position: Int) {
                mTotalCount--
                text_customer_total_count.text = PplusCommonUtil.fromHtml(getString(R.string.html_total_count4, FormatUtil.getMoneyType(mTotalCount.toString())))
            }
        })

        text_customer_not_exist.visibility = View.GONE
//        layout_customer.visibility = View.GONE

        if (group!!.isDefaultGroup) {
            text_customer_group_config.visibility = View.GONE
            text_customer_not_exist.setText(R.string.msg_customer_all_not_exist)
        } else {
            text_customer_not_exist.setText(R.string.msg_customer_group_not_exist)
            text_customer_group_config.visibility = View.VISIBLE
        }

        text_customer_group_config.setOnClickListener {
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
                                    builder.addContents(AlertData.MessageData(getString(R.string.format_msg_delete_group1, getString(R.string.word_contact_customer)), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
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
                                                    ApiBuilder.create().deleteGroup(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {

                                                        override fun onResponse(call: Call<NewResultResponse<Any>>, response: NewResultResponse<Any>) {
                                                            hideProgress()
                                                            if(!isAdded){
                                                                return
                                                            }

                                                            if(parentFragment is CustomerFragment){
                                                                (parentFragment as CustomerFragment).mCurrentPosition = 0
                                                                (parentFragment as CustomerFragment).getGroupAll()
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
                                    if(parentFragment is CustomerFragment){
                                        groupList = (parentFragment as CustomerFragment).mGroupList
                                    }

                                    val intent = Intent(activity, GroupNameEditActivity::class.java)
                                    intent.putExtra(Const.DATA, group)
                                    intent.putParcelableArrayListExtra(Const.GROUP, groupList as ArrayList<Group>)
                                    intent.putExtra(Const.KEY, EnumData.CustomerType.customer)
                                    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    activity!!.startActivityForResult(intent, Const.REQ_GROUP_CONFIG)
                                }
                                3, 4->{
                                    val intent = Intent(activity, SelectCustomerConfigActivity::class.java)
                                    intent.putExtra(Const.GROUP, group)
                                    when (event_alert.getValue()) {
                                        3 -> intent.putExtra(Const.KEY, Const.CUSTOMER_GROUP_ADD)
                                        4 -> intent.putExtra(Const.KEY, Const.CUSTOMER_GROUP_DEL)
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
        text_customer_not_exist.setOnClickListener {
            if (group!!.isDefaultGroup) {
                val intent = Intent(activity, CustomerDirectRegActivity::class.java)
                intent.putExtra(Const.KEY, EnumData.CustomerType.customer)
                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                activity!!.startActivityForResult(intent, Const.REQ_CUSTOMER)
            } else {
                val intent = Intent(activity, SelectCustomerConfigActivity::class.java)
                intent.putExtra(Const.GROUP, group)
                intent.putExtra(Const.KEY, Const.CUSTOMER_GROUP_ADD)
                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                activity!!.startActivityForResult(intent, Const.REQ_GROUP_CONFIG)
            }
        }

        getCount()
    }

    private fun search() {
        mSearch = edit_customer_search.text.toString().trim()
        if (StringUtils.isEmpty(mSearch)) {
            showAlert(R.string.msg_input_searchWord)
            return
        }
        getCount()
    }

    private fun getCount() {

        val params = HashMap<String, String>()
        params["no"] = group!!.no.toString()
        if (StringUtils.isNotEmpty(mSearch)) {
            params["search"] = mSearch!!
        }

        ApiBuilder.create().getCustomerCount(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {

            override fun onResponse(call: Call<NewResultResponse<Int>>, response: NewResultResponse<Int>) {
                if (!isAdded) {
                    return
                }
                mTotalCount = response.data

                if (mTotalCount > 0) {
                    text_customer_not_exist.visibility = View.GONE
//                    layout_customer.visibility = View.VISIBLE
                } else {
                    text_customer_not_exist.visibility = View.VISIBLE
//                    layout_customer.visibility = View.GONE
                }

                text_customer_total_count.text = PplusCommonUtil.fromHtml(getString(R.string.html_total_count4, FormatUtil.getMoneyType(mTotalCount.toString())))
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
        ApiBuilder.create().getCustomerList(params).setCallback(object : PplusCallback<NewResultResponse<Customer>> {

            override fun onResponse(call: Call<NewResultResponse<Customer>>, response: NewResultResponse<Customer>) {

                hideProgress()
                if (!isAdded) {
                    return
                }

                mLockListView = false

                mAdapter!!.addAll(response.datas)
            }

            override fun onFailure(call: Call<NewResultResponse<Customer>>, t: Throwable, response: NewResultResponse<Customer>) {

                hideProgress()
            }
        }).build().call()

    }

    private inner class CustomItemOffsetDecoration(private val mItemOffset: Int, private val mTopOffset: Int) : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {

        constructor(context: Context, @DimenRes itemOffsetId: Int, @DimenRes topOffsetId: Int) : this(context.resources.getDimensionPixelSize(itemOffsetId), context.resources.getDimensionPixelSize(topOffsetId)) {}

        override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {

            super.getItemOffsets(outRect, view, parent, state)

            val position = parent.getChildAdapterPosition(view)
            if (position == 0) {
                outRect.set(0, mTopOffset, 0, mItemOffset)
            } else {
                outRect.set(0, 0, 0, mItemOffset)
            }

        }
    }

    private var group: Group? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            group = it.getParcelable(ARG_PARAM1)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(group: Group) =
                CustomerListFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(ARG_PARAM1, group)
                    }
                }
    }
}
