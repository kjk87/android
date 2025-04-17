package com.pplus.luckybol.apps.product.ui

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.builder.AlertBuilder
import com.pplus.luckybol.apps.common.builder.OnAlertResultListener
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.common.ui.base.BaseFragment
import com.pplus.luckybol.apps.product.data.ProductLikeShippingAdapter
import com.pplus.luckybol.core.code.common.EnumData
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.ProductLike
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.network.model.response.SubResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.FragmentGoodsLikeBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import retrofit2.Call
import java.util.*

class ProductLikeShipFragment : BaseFragment<BaseActivity>() {

    private var _binding: FragmentGoodsLikeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentGoodsLikeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private var mTotalCount: Int = 0
    private var mLockListView = false
    private var mPaging = 1
    private var mAdapter: ProductLikeShippingAdapter? = null
    private var mLayoutManager: GridLayoutManager? = null
    private var mIsLast = false
    private var mSort = "product_price_seq_no,${EnumData.GoodsSort.desc}"

    override fun init() {

        mLayoutManager = GridLayoutManager(activity, 2)
        binding.recyclerGoodsLike.layoutManager = mLayoutManager
        mAdapter = ProductLikeShippingAdapter()
        binding.recyclerGoodsLike.adapter = mAdapter

        (binding.recyclerGoodsLike.layoutParams as RelativeLayout.LayoutParams).marginStart = resources.getDimensionPixelSize(R.dimen.width_38)
        (binding.recyclerGoodsLike.layoutParams as RelativeLayout.LayoutParams).marginEnd = resources.getDimensionPixelSize(R.dimen.width_38)
        binding.recyclerGoodsLike.addItemDecoration(CustomItemOffsetDecoration(requireActivity(), R.dimen.height_60))
        //        layout_buy_history_top.visibility = View.VISIBLE
        binding.recyclerGoodsLike.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        mAdapter!!.setOnItemClickListener(object : ProductLikeShippingAdapter.OnItemClickListener {

            override fun onItemClick(position: Int) {

                val intent = Intent(activity, ProductShipDetailActivity::class.java)
                intent.putExtra(Const.DATA, mAdapter!!.getItem(position).productPrice)
                startActivity(intent)
            }
        })

        mAdapter!!.setOnItemDeleteListener(object : ProductLikeShippingAdapter.OnItemDeleteListener {
            override fun onItemDelete() {
                mPaging = 0
                listCall(mPaging)
            }
        })

        binding.textGoodsLikeSort.setOnClickListener {
            val builder = AlertBuilder.Builder()
            builder.setContents(getString(R.string.word_sort_new_reg), getString(R.string.word_sort_expired))
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                    when (event_alert.getValue()) {
                        1 -> {
                            mSort = "goodsSeqNo,${EnumData.GoodsSort.desc}"
                            binding.textGoodsLikeSort.text = getString(R.string.word_sort_new_reg)
                        }
                        2 -> {
                            mSort = "expireDatetime,${EnumData.GoodsSort.asc}"
                            binding.textGoodsLikeSort.text = getString(R.string.word_sort_expired)
                        }
                    }
                    mPaging = 0
                    listCall(mPaging)

                }
            }).builder().show(activity)
        }

        mPaging = 0
        listCall(mPaging)
    }

    private fun listCall(page: Int) {

        mLockListView = true
        val params = HashMap<String, String>()

        params["sort"] = mSort
        params["page"] = page.toString()

        showProgress("")
        ApiBuilder.create().getProductLikeShippingList(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<ProductLike>>> {

            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<ProductLike>>>?, response: NewResultResponse<SubResultResponse<ProductLike>>?) {
                hideProgress()

                if (response?.data != null) {
                    mIsLast = response.data!!.last!!
                    if (response.data!!.first!!) {
                        mTotalCount = response.data!!.totalElements!!
                        binding.textGoodsLikeCount.text = PplusCommonUtil.fromHtml(getString(R.string.html_total_count2, FormatUtil.getMoneyType(mTotalCount.toString())))
                        if(mTotalCount > 0){
                            binding.layoutGoodsLikeNotExist.visibility = View.GONE
                        }else{
                            binding.layoutGoodsLikeNotExist.visibility = View.VISIBLE
                        }

                        mAdapter!!.clear()
                    }

                    mLockListView = false

                    val dataList = response.data!!.content!!
                    mAdapter!!.addAll(dataList)
                }

            }

            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<ProductLike>>>?, t: Throwable?, response: NewResultResponse<SubResultResponse<ProductLike>>?) {
                hideProgress()
                mLockListView = false
            }
        }).build().call()
    }

    private inner class CustomItemOffsetDecoration(private val mItemOffset: Int) : RecyclerView.ItemDecoration() {

        constructor(context: Context, @DimenRes lastOffsetId: Int) : this(context.resources.getDimensionPixelSize(lastOffsetId)) {}

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

            super.getItemOffsets(outRect, view, parent, state)

            val position = parent.getChildAdapterPosition(view)
            if (position <= 1) {
                outRect.set(0, mItemOffset, 0, mItemOffset)
            } else {
                outRect.set(0, 0, 0, mItemOffset)
            }
        }
    }

    override fun getPID(): String {
        return ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
                ProductLikeShipFragment().apply {
                    arguments = Bundle().apply {
                        //                        putString(ARG_PARAM1, param1)
//                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
