package com.pplus.prnumberuser.apps.product.ui

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
import com.pplus.prnumberuser.apps.common.ui.custom.BottomItemOffsetDecoration
import com.pplus.prnumberuser.apps.product.data.StoreSubscriptionAdapter
import com.pplus.prnumberuser.apps.subscription.ui.SubscriptionDetailActivity
import com.pplus.prnumberuser.core.code.common.EnumData
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.LocationData
import com.pplus.prnumberuser.core.network.model.dto.Page
import com.pplus.prnumberuser.core.network.model.dto.ProductPrice
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.network.model.response.SubResultResponse
import com.pplus.prnumberuser.databinding.FragmentStoreSubscriptionProductBinding
import com.pplus.utils.part.format.FormatUtil
import retrofit2.Call
import java.util.*

class StoreMoneyProductFragment : BaseFragment<BaseActivity>() {


    private var _binding: FragmentStoreSubscriptionProductBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentStoreSubscriptionProductBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private lateinit var mPage: Page

    private var mTotalCount: Int = 0
    private var mLockListView = false
    private var mPaging = 1
    private var mAdapter: StoreSubscriptionAdapter? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var mLocationData: LocationData? = null
    private var mIsLast = false

    override fun init() {

        mLayoutManager = LinearLayoutManager(activity)
        binding.recyclerStoreSubscriptionProduct.layoutManager = mLayoutManager!!
        mAdapter = StoreSubscriptionAdapter()
        binding.recyclerStoreSubscriptionProduct.adapter = mAdapter
        binding.recyclerStoreSubscriptionProduct.addItemDecoration(BottomItemOffsetDecoration(requireActivity(), R.dimen.height_48))

//        recycler_main_goods_plus.addOnScrollListener(RecyclerScaleScrollListener(parentActivity.layout_main_floating))
        binding.recyclerStoreSubscriptionProduct.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        mAdapter!!.setOnItemClickListener(object : StoreSubscriptionAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                val intent = Intent(activity, SubscriptionDetailActivity::class.java)
                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }

        })


        mPaging = 0
        listCall(mPaging)

    }


    private fun listCall(page: Int) {

        mLockListView = true
        val params = HashMap<String, String>()
        params["pageSeqNo"] = mPage.no.toString()
        params["page"] = page.toString()
        params["sort"] = "${EnumData.BuyGoodsSort.seqNo.name},${EnumData.BuyGoodsSort.desc.name}"
//        showProgress("")
        ApiBuilder.create().getProductPriceListMoneyTypeByPageSeqNoOnlyNormal(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<ProductPrice>>> {

            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<ProductPrice>>>?, response: NewResultResponse<SubResultResponse<ProductPrice>>?) {
                if (!isAdded) {
                    return
                }
                if (response != null) {

                    mIsLast = response.data.last!!
                    if (response.data.first!!) {
                        mTotalCount = response.data.totalElements!!
                        binding.textStoreSubscriptionProductCount.text = getString(R.string.format_money_product, FormatUtil.getMoneyType(mTotalCount.toString()))
                        mAdapter!!.clear()
                        if (mTotalCount == 0) {
                            binding.layoutStoreSubscriptionProductNotExist.visibility = View.VISIBLE
                        } else {
                            binding.layoutStoreSubscriptionProductNotExist.visibility = View.GONE
                        }
                    }

                    mLockListView = false
                    mAdapter!!.addAll(response.data.content!!)
                }

            }

            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<ProductPrice>>>?, t: Throwable?, response: NewResultResponse<SubResultResponse<ProductPrice>>?) {
//                hideProgress()
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

    override fun getPID(): String {
        return "Main_page product"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mPage = it.getParcelable(Const.PAGE)!!
            //            param2 = it.getString(ARG_PARAM2)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(page:Page) =
                StoreMoneyProductFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(Const.PAGE, page)
                    }
                }
    }
}
