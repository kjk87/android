package com.pplus.luckybol.apps.product.ui

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.builder.AlertBuilder
import com.pplus.luckybol.apps.common.builder.OnAlertResultListener
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.common.ui.base.BaseFragment
import com.pplus.luckybol.apps.main.ui.MainShipTypeFragment
import com.pplus.luckybol.apps.product.data.ProductShipAdapter
import com.pplus.luckybol.apps.product.data.ProductShoppingGroupAdapter
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.CategoryFirst
import com.pplus.luckybol.core.network.model.dto.ProductPrice
import com.pplus.luckybol.core.network.model.dto.ShoppingGroup
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.network.model.response.SubResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.FragmentProductShipListBinding
import com.pplus.luckybol.databinding.FragmentShoppingGroupProductBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*

class ShoppingGroupProductFragment : BaseFragment<BaseActivity>() {

    private var _binding: FragmentShoppingGroupProductBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentShoppingGroupProductBinding.inflate(inflater, container, false)
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
    private var mAdapter: ProductShoppingGroupAdapter? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var shoppingGroup: ShoppingGroup? = null
    private var mIsLast = false
    private var mSort = "seqNo,desc"

    override fun init() {

        mLayoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerShoppingGroupProduct.layoutManager = mLayoutManager!!
        mAdapter = ProductShoppingGroupAdapter()
        binding.recyclerShoppingGroupProduct.adapter = mAdapter
        binding.recyclerShoppingGroupProduct.addItemDecoration(CustomItemOffsetDecoration(requireActivity(), R.dimen.height_30))
        binding.recyclerShoppingGroupProduct.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        mAdapter!!.setOnItemClickListener(object : ProductShoppingGroupAdapter.OnItemClickListener {

            override fun onItemClick(position: Int, view: View) {

                val intent = Intent(activity, ProductShipDetailActivity::class.java)
                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
                startActivity(intent)
            }

            override fun changeLike() {
            }
        })

        binding.textShoppingGroupProductSort.setOnClickListener {
            val builder = AlertBuilder.Builder()
            builder.setContents(getString(R.string.word_sort_recent), getString(R.string.word_sort_discount), getString(R.string.word_sort_bol))
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                    when (event_alert.getValue()) {
                        1 -> {
                            mSort = "seqNo,desc"
                            binding.textShoppingGroupProductSort.text = getString(R.string.word_sort_recent)
                        }
                        2 -> {
                            mSort = "discount_ratio,desc"
                            binding.textShoppingGroupProductSort.text = getString(R.string.word_sort_discount)
                        }
                        3 -> {
                            mSort = "price,desc"
                            binding.textShoppingGroupProductSort.text = getString(R.string.word_sort_bol)
                        }
                    }
                    mPaging = 0
                    listCall(mPaging)

                }
            }).builder().show(activity)
        }
        binding.swipeShoppingGroupProduct.setOnRefreshListener {
            mPaging = 0
            listCall(mPaging)
            binding.swipeShoppingGroupProduct.isRefreshing = false
        }


        binding.layoutShoppingGroupProductLoading.visibility = View.VISIBLE
        mSort = "seqNo,desc"
        binding.textShoppingGroupProductSort.text = getString(R.string.word_sort_recent)

        mPaging = 0
        listCall(mPaging)
    }

    private fun listCall(page: Int) {

        mLockListView = true
        val params = HashMap<String, String>()
        params["shoppingGroupSeqNo"] = shoppingGroup!!.seqNo.toString()
        params["page"] = page.toString()
        params["sort"] = mSort
//        showProgress("")
        ApiBuilder.create().getProductPriceListShipTypeByShoppingGroup(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<ProductPrice>>> {
            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<ProductPrice>>>?,
                                    response: NewResultResponse<SubResultResponse<ProductPrice>>?) {
                if (!isAdded) {
                    return
                }

                binding.layoutShoppingGroupProductLoading.visibility = View.GONE
                if (response != null) {
                    mIsLast = response.data!!.last!!
                    if (response.data!!.first!!) {
                        mTotalCount = response.data!!.totalElements!!
                        binding.textShoppingGroupProductCount.text = PplusCommonUtil.fromHtml(getString(R.string.html_total_count2, FormatUtil.getMoneyType(mTotalCount.toString())))
                        mAdapter!!.clear()
                        if(mTotalCount == 0){
                            binding.textShoppingGroupProductNotExist.visibility = View.VISIBLE
                        }else{
                            binding.textShoppingGroupProductNotExist.visibility = View.GONE
                        }
                    }

                    mLockListView = false
                    mAdapter!!.addAll(response.data!!.content!!)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<ProductPrice>>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<SubResultResponse<ProductPrice>>?) {
                if (!isAdded) {
                    return
                }
                mLockListView = false
                binding.layoutShoppingGroupProductLoading.visibility = View.GONE
            }

        }).build().call()
    }

    private inner class CustomItemOffsetDecoration(private val mItemOffset: Int) : RecyclerView.ItemDecoration() {

        constructor(context: Context, @DimenRes lastOffsetId: Int) : this(context.resources.getDimensionPixelSize(lastOffsetId)) {}

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

            super.getItemOffsets(outRect, view, parent, state)

            val position = parent.getChildAdapterPosition(view)
            if (position == 0) {
                outRect.set(0, mItemOffset, 0, 0)
            }
        }
    }

    override fun getPID(): String {
        return ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            shoppingGroup = it.getParcelable(Const.DATA)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(shoppingGroup: ShoppingGroup) =
                ShoppingGroupProductFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(Const.DATA, shoppingGroup)
//                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
