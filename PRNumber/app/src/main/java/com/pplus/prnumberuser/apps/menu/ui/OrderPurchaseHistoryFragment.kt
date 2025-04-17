package com.pplus.prnumberuser.apps.menu.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
import com.pplus.prnumberuser.apps.menu.data.OrderPurchaseHistoryAdapter
import com.pplus.prnumberuser.core.code.common.EnumData
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.OrderPurchase
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.network.model.response.SubResultResponse
import com.pplus.prnumberuser.databinding.ActivityOrderPurchaseHistoryBinding
import retrofit2.Call
import java.util.*

class OrderPurchaseHistoryFragment : BaseFragment<BaseActivity>() {

    private var _binding: ActivityOrderPurchaseHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = ActivityOrderPurchaseHistoryBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private var mTotalCount: Int = 0
    private var mLockListView = false
    private var mPaging = 0
    private var mAdapter: OrderPurchaseHistoryAdapter? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var mIsLast = false

    override fun init() {

        mLayoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerOrderPurchaseHistory.layoutManager = mLayoutManager
        mAdapter = OrderPurchaseHistoryAdapter()
        mAdapter!!.launcher = launcher
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
                val intent = Intent(requireActivity(), OrderPurchaseHistoryDetailActivity::class.java)
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

    override fun getPID(): String {
        return ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            mTab = it.getInt(Const.TAB)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
                OrderPurchaseHistoryFragment().apply {
                    arguments = Bundle().apply {
//                        putInt(Const.TAB, tab)
//                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
