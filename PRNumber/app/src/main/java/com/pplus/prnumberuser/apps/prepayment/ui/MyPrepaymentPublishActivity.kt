package com.pplus.prnumberuser.apps.prepayment.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.common.ui.custom.BottomItemOffsetDecoration
import com.pplus.prnumberuser.apps.prepayment.data.PageWithPrepaymentPublishAdapter
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.Page2
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.network.model.response.SubResultResponse
import com.pplus.prnumberuser.databinding.ActivityMyPrepaymentPublishBinding
import retrofit2.Call
import java.util.*

class MyPrepaymentPublishActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityMyPrepaymentPublishBinding

    override fun getLayoutView(): View {
        binding = ActivityMyPrepaymentPublishBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    private var mTotalCount: Int = 0
    private var mLockListView = false
    private var mPaging = 1
    private var mAdapter: PageWithPrepaymentPublishAdapter? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var mIsLast = false

    override fun initializeView(savedInstanceState: Bundle?) {
        mLayoutManager = LinearLayoutManager(this)
        binding.recyclerMyPrepaymentPublish.layoutManager = mLayoutManager!!
        mAdapter = PageWithPrepaymentPublishAdapter()
        binding.recyclerMyPrepaymentPublish.adapter = mAdapter
        binding.recyclerMyPrepaymentPublish.addItemDecoration(BottomItemOffsetDecoration(this, R.dimen.height_66))

        //        recycler_main_goods_plus.addOnScrollListener(RecyclerScaleScrollListener(parentActivity.layout_main_floating))
        binding.recyclerMyPrepaymentPublish.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        mPaging = 0
        listCall(mPaging)
    }

    private fun listCall(page: Int) {

        mLockListView = true
        val params = HashMap<String, String>()
        params["page"] = page.toString()
        showProgress("")
        ApiBuilder.create().getPageListWithPageWithPrepaymentPublish(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<Page2>>> {

            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<Page2>>>?, response: NewResultResponse<SubResultResponse<Page2>>?) {
                hideProgress()
                if (response != null) {

                    mIsLast = response.data.last!!
                    if (response.data.first!!) {
                        mTotalCount = response.data.totalElements!!
                        mAdapter!!.clear()
                        if (mTotalCount == 0) {
                            binding.layoutMyPrepaymentPublishNotExist.visibility = View.VISIBLE
                        } else {
                            binding.layoutMyPrepaymentPublishNotExist.visibility = View.GONE
                        }
                    }

                    mLockListView = false
                    mAdapter!!.addAll(response.data.content!!)
                }

            }

            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<Page2>>>?, t: Throwable?, response: NewResultResponse<SubResultResponse<Page2>>?) {
                //                hideProgress()
                hideProgress()
                mLockListView = false
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_retention_prepayment), ToolbarOption.ToolbarMenu.RIGHT)

        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
                        onBackPressed()
                    }
                }
            }
        }
    }
}