package com.pplus.prnumberuser.apps.product.ui

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
import com.pplus.prnumberuser.apps.product.data.PurchaseProductShippingHistoryAdapter
import com.pplus.prnumberuser.core.code.common.EnumData
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.PurchaseProduct
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.network.model.response.SubResultResponse
import com.pplus.prnumberuser.databinding.FragmentBuyHistoryBinding
import retrofit2.Call
import java.util.*

class PurchaseProductShippingHistoryFragment : BaseFragment<BaseActivity>() {


    private var _binding: FragmentBuyHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentBuyHistoryBinding.inflate(inflater, container, false)
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
    private var mAdapter: PurchaseProductShippingHistoryAdapter? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var mIsLast = false

    override fun init() {

        mLayoutManager = LinearLayoutManager(activity)
        binding.recyclerBuyHistory.layoutManager = mLayoutManager
        mAdapter = PurchaseProductShippingHistoryAdapter()
        mAdapter!!.mFragment = this
        binding.recyclerBuyHistory.adapter = mAdapter
//        recycler_main_page.addItemDecoration(CustomItemOffsetDecoration(activity!!, R.dimen.height_60, R.dimen.height_60))

        binding.recyclerBuyHistory.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        mAdapter!!.setOnItemClickListener(object : PurchaseProductShippingHistoryAdapter.OnItemClickListener {

            override fun onItemClick(position: Int) {

                val intent = Intent(activity, PurchaseProductShippingHistoryDetailActivity::class.java)
                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
                launcher.launch(intent)
            }
        })

        binding.textBuyHistoryNotExist.setText(R.string.msg_not_exist_buy_goods)
        mPaging = 0
        listCall(mPaging)
    }

    private fun listCall(page: Int) {

        mLockListView = true
        val params = HashMap<String, String>()
        params["page"] = page.toString()

        params["sort"] = "${EnumData.BuyGoodsSort.seqNo.name},${EnumData.BuyGoodsSort.desc.name}"
        showProgress("")
        ApiBuilder.create().getPurchaseProductListByMemberSeqNo(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<PurchaseProduct>>> {
            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<PurchaseProduct>>>?, response: NewResultResponse<SubResultResponse<PurchaseProduct>>?) {
                hideProgress()

                if(!isAdded){
                    return
                }

                if (response != null) {

                    mIsLast = response.data.last!!

                    if (response.data.first!!) {
                        mTotalCount = response.data.totalElements!!
                        mAdapter!!.clear()
                        if (mTotalCount > 0) {
                            binding.layoutBuyHistoryNotExist.visibility = View.GONE
                        } else {
                            binding.layoutBuyHistoryNotExist.visibility = View.VISIBLE
                        }
                    }

                    mLockListView = false

                    val dataList = response.data.content!!
                    mAdapter!!.addAll(dataList)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<PurchaseProduct>>>?, t: Throwable?, response: NewResultResponse<SubResultResponse<PurchaseProduct>>?) {
                hideProgress()
                if(!isAdded){
                    return
                }
                mLockListView = false
            }
        }).build().call()
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

    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        mPaging = 0
        listCall(mPaging)
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
                PurchaseProductShippingHistoryFragment().apply {
                    arguments = Bundle().apply {
//                        putInt(Const.TAB, tab)
//                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
