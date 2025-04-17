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
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.CategoryFirst
import com.pplus.luckybol.core.network.model.dto.ProductPrice
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.network.model.response.SubResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.FragmentProductShipListBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*

class ProductShipListFragment : BaseFragment<BaseActivity>() {

    private var _binding: FragmentProductShipListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentProductShipListBinding.inflate(inflater, container, false)
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
    private var mAdapter: ProductShipAdapter? = null
    private var mLayoutManager: GridLayoutManager? = null
    private var category: CategoryFirst? = null
    private var mIsLast = false
    private var mSort = "seqNo,desc"

    override fun init() {

        mLayoutManager = GridLayoutManager(activity, 2)
        binding.recyclerProductShipList.layoutManager = mLayoutManager!!
        mAdapter = ProductShipAdapter()
        binding.recyclerProductShipList.adapter = mAdapter
        binding.recyclerProductShipList.addItemDecoration(CustomItemOffsetDecoration(requireActivity(), R.dimen.height_60))
        binding.recyclerProductShipList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        mAdapter!!.setOnItemClickListener(object : ProductShipAdapter.OnItemClickListener {

            override fun onItemClick(position: Int, view: View) {

//                if (!PplusCommonUtil.loginCheck(activity!!)) {
//                    return
//                }

                val intent = Intent(activity, ProductShipDetailActivity::class.java)
                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
                startActivity(intent)
            }

            override fun changeLike() {
                if (LoginInfoManager.getInstance().isMember && parentFragment is MainShipTypeFragment) {
                    (parentFragment as MainShipTypeFragment).getProductLikeCount()
                }
            }
        })

        binding.textProductShipListSort.setOnClickListener {
            val builder = AlertBuilder.Builder()
            builder.setContents(getString(R.string.word_sort_recent), getString(R.string.word_sort_discount), getString(R.string.word_sort_bol))
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                    when (event_alert.getValue()) {
                        1 -> {
                            mSort = "seqNo,desc"
                            binding.textProductShipListSort.text = getString(R.string.word_sort_recent)
                        }
                        2 -> {
                            mSort = "discount_ratio,desc"
                            binding.textProductShipListSort.text = getString(R.string.word_sort_discount)
                        }
                        3 -> {
                            mSort = "price,desc"
                            binding.textProductShipListSort.text = getString(R.string.word_sort_bol)
                        }
                    }
                    mPaging = 0
                    listCall(mPaging)

                }
            }).builder().show(activity)
        }
        binding.swipeHotDealShipTypeList.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                val random = Random()
                setData()
                binding.swipeHotDealShipTypeList.isRefreshing = false
            }
        })


        binding.layoutProductShipListLoading.visibility = View.VISIBLE

        mSort = "seqNo,desc"
        binding.textProductShipListSort.text = getString(R.string.word_sort_recent)
        if(getParentActivity() is ProductShipSearchResultActivity){
            binding.textProductShipListNotExist.text = getString(R.string.msg_not_exist_search_product)
        }else{
            binding.textProductShipListNotExist.text = getString(R.string.msg_not_exist_product)
        }

        setData()
    }

    fun setData() {
        mPaging = 0
        listCall(mPaging)
    }

    private fun listCall(page: Int) {

        mLockListView = true
        val params = HashMap<String, String>()
        if(getParentActivity() is ProductShipSearchResultActivity){
            val search = (getParentActivity() as ProductShipSearchResultActivity).mSearch
            if(StringUtils.isNotEmpty(search)){
                params["search"] = search
            }
        }else{
            if(category!!.seqNo != -1L){
                params["first"] = category!!.seqNo.toString()
            }else{
                params["pick"] = "true"
            }
        }

        params["page"] = page.toString()
        params["sort"] = "pick,desc,$mSort"
//        showProgress("")
        ApiBuilder.create().getProductPriceShipType(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<ProductPrice>>> {
            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<ProductPrice>>>?,
                                    response: NewResultResponse<SubResultResponse<ProductPrice>>?) {
                if (!isAdded) {
                    return
                }

                binding.layoutProductShipListLoading.visibility = View.GONE
                if (response != null) {
                    mIsLast = response.data!!.last!!
                    if (response.data!!.first!!) {
                        mTotalCount = response.data!!.totalElements!!
                        binding.textProductShipListCount.text = PplusCommonUtil.fromHtml(getString(R.string.html_total_count2, FormatUtil.getMoneyType(mTotalCount.toString())))
                        mAdapter!!.clear()
                        if(mTotalCount == 0){
                            binding.layoutProductShipListNotExist.visibility = View.VISIBLE
                        }else{
                            binding.layoutProductShipListNotExist.visibility = View.GONE
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
                binding.layoutProductShipListLoading.visibility = View.GONE
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
            category = it.getParcelable(Const.DATA)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(category: CategoryFirst) =
                ProductShipListFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(Const.DATA, category)
//                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
