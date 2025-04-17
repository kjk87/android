package com.root37.buflexz.apps.product.ui

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.root37.buflexz.Const
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.apps.common.ui.base.BaseFragment
import com.root37.buflexz.apps.main.ui.MainProductFragment
import com.root37.buflexz.apps.product.data.ProductAdapter
import com.root37.buflexz.core.network.ApiBuilder
import com.root37.buflexz.core.network.model.dto.Product
import com.root37.buflexz.core.network.model.dto.ProductCategory
import com.root37.buflexz.core.network.model.response.ListResultResponse
import com.root37.buflexz.core.network.model.response.NewResultResponse
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.FragmentProductListBinding
import retrofit2.Call

class ProductListFragment : BaseFragment<BaseActivity>() {
    private var mCategory: ProductCategory? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mCategory = PplusCommonUtil.getParcelable(it, Const.CATEGORY, ProductCategory::class.java)
        }
    }

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun getPID(): String? {
        return ""
    }

    private var mTotalCount = 0
    var mAdapter: ProductAdapter? = null
    private lateinit var mLayoutManager: GridLayoutManager
    private var mLockListView = false
    private var mPaging = 1
    private var mDir = "ASC"

    override fun init() {
        mLayoutManager = GridLayoutManager(requireActivity(), 2)
        binding.recyclerProductList.layoutManager = mLayoutManager
        mAdapter = ProductAdapter()
        binding.recyclerProductList.adapter = mAdapter
        binding.recyclerProductList.addItemDecoration(CustomItemOffsetDecoration(requireActivity(), R.dimen.width_33, R.dimen.height_69))

        binding.recyclerProductList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        mAdapter!!.listener = object : ProductAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(requireActivity(), ProductActivity::class.java)
                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                defaultLauncher.launch(intent)
            }
        }

        mPaging = 1
        getList(mPaging)
    }

    private fun getList(page: Int) {
        val params = HashMap<String, String>()
        params["paging[page]"] = page.toString()
        params["paging[limit]"] = "20"
        if (mCategory!!.seqNo != null) {
            params["categorySeqNo"] = mCategory!!.seqNo.toString()
        } //        params["order[][column]"] = "price"
        //        params["order[][dir]"] = mDir
        showProgress("")
        mLockListView = true
        ApiBuilder.create().getProductList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<Product>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<Product>>>?, response: NewResultResponse<ListResultResponse<Product>>?) {
                hideProgress()
                if (response?.result != null) {
                    if (page == 1) {
                        mAdapter!!.clear()

                        mTotalCount = response.result!!.total!!
                        if(mTotalCount == 0){
                            binding.textProductListNotExist.visibility = View.VISIBLE
                        }else{
                            binding.textProductListNotExist.visibility = View.GONE
                        }
                    }

                    mLockListView = false

                    if (response.result!!.list != null) {
                        val dataList = response.result!!.list!!
                        mAdapter!!.addAll(dataList)
                    }

                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<Product>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<Product>>?) {
                hideProgress()
            }
        }).build().call()
    }

    private val defaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        mPaging = 1
        getList(mPaging)
        if (getParentActivity() is ProductCategoryActivity) {
            (getParentActivity() as ProductCategoryActivity).loginCheck()
        } else if (parentFragment is MainProductFragment) {
            (parentFragment as MainProductFragment).loginCheck()
        }
    }

    private inner class CustomItemOffsetDecoration(private val mRightOffset: Int, private val mBottomOffset: Int) : RecyclerView.ItemDecoration() {

        constructor(context: Context, @DimenRes rightOffsetId: Int, @DimenRes bottomOffsetId: Int) : this(context.resources.getDimensionPixelSize(rightOffsetId), context.resources.getDimensionPixelSize(bottomOffsetId)) {
        }

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

            super.getItemOffsets(outRect, view, parent, state)

            val position = parent.getChildAdapterPosition(view)

            if (position % 2 != 1) {
                outRect.set(0, 0, mRightOffset, mBottomOffset)
            } else {
                outRect.set(0, 0, 0, mBottomOffset)
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(category: ProductCategory) = ProductListFragment().apply {
            arguments = Bundle().apply {
                putParcelable(Const.CATEGORY, category)
            }
        }
    }
}