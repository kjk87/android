package com.pplus.prnumberuser.apps.menu.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.menu.data.OrderPurchaseHistoryAdapter
import com.pplus.prnumberuser.core.code.common.EnumData
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.OrderPurchase
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.network.model.response.SubResultResponse
import com.pplus.prnumberuser.databinding.ActivityOrderPurchaseHistoryBinding
import retrofit2.Call
import java.util.*

class OrderPurchaseHistoryActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityOrderPurchaseHistoryBinding

    override fun getLayoutView(): View {
        binding = ActivityOrderPurchaseHistoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    private var mTotalCount: Int = 0
    private var mLockListView = false
    private var mPaging = 0
    private var mAdapter: OrderPurchaseHistoryAdapter? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var mIsLast = false

    override fun initializeView(savedInstanceState: Bundle?) {
        mLayoutManager = LinearLayoutManager(this)
        binding.recyclerOrderPurchaseHistory.layoutManager = mLayoutManager
        mAdapter = OrderPurchaseHistoryAdapter()
        binding.recyclerOrderPurchaseHistory.adapter = mAdapter
        //        recycler_main_page.addItemDecoration(CustomItemOffsetDecoration(activity!!, R.dimen.height_60, R.dimen.height_60))

        binding.recyclerOrderPurchaseHistory.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        mAdapter!!.setOnItemClickListener(object : OrderPurchaseHistoryAdapter.OnItemClickListener {

            override fun onItemClick(position: Int) {
                val item = mAdapter!!.getItem(position)
                val intent = Intent(this@OrderPurchaseHistoryActivity, OrderPurchaseHistoryDetailActivity::class.java)
                intent.putExtra(Const.DATA, item)
                launcher.launch(intent)

            }
        })

        mPaging = 0
        listCall(mPaging)
    }

    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        mPaging = 0
        listCall(mPaging)
    }

    private fun listCall(page: Int) {

        mLockListView = true
        val params = HashMap<String, String>()
        params["page"] = page.toString()

        params["sort"] = "${EnumData.BuyGoodsSort.seqNo.name},${EnumData.BuyGoodsSort.desc.name}"
        showProgress("")
        ApiBuilder.create().getOrderPurchaseListByMemberSeqNo(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<OrderPurchase>>> {
            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<OrderPurchase>>>?, response: NewResultResponse<SubResultResponse<OrderPurchase>>?) {
                hideProgress()

                if (response != null) {

                    mIsLast = response.data.last!!

                    if (response.data.first!!) {
                        mTotalCount = response.data.totalElements!!
                        mAdapter!!.clear()
                        if (mTotalCount > 0) {
                            binding.layoutOrderPurchaseHistoryNotExist.visibility = View.GONE
                        } else {
                            binding.layoutOrderPurchaseHistoryNotExist.visibility = View.VISIBLE
                        }
                    }

                    mLockListView = false

                    val dataList = response.data.content!!
                    mAdapter!!.addAll(dataList)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<OrderPurchase>>>?, t: Throwable?, response: NewResultResponse<SubResultResponse<OrderPurchase>>?) {
                hideProgress()
                mLockListView = false
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_order_history), ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressed()
                    }
                }
            }
        }
    }
}