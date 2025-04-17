package com.pplus.prnumberuser.apps.product.ui


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
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
import com.pplus.prnumberuser.apps.product.data.ProductShipAdapter
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.ProductPrice
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.network.model.response.SubResultResponse
import com.pplus.prnumberuser.databinding.FragmentMainProductShipBinding
import retrofit2.Call
import java.util.*

class MainProductShipFragment : BaseFragment<BaseActivity>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            mPage = it.getParcelable(Const.PAGE)!!
//            param2 = it.getString(ARG_PARAM2)
        }
    }


    private var _binding: FragmentMainProductShipBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentMainProductShipBinding.inflate(inflater, container, false)
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
    private var mAdapter: ProductShipAdapter? = null
    private var mLayoutManager: GridLayoutManager? = null
    private var mIsLast = false
    private var mSort = "seqNo,desc"
    var mSorts = arrayOf("seqNo,desc","seqNo,asc", "discount_ratio,desc", "price,asc", "origin_price,desc")

    override fun init() {
        mLayoutManager = GridLayoutManager(activity, 2)
        binding.recyclerMainProductShip.layoutManager = mLayoutManager!!
        mAdapter = ProductShipAdapter()
        binding.recyclerMainProductShip.adapter = mAdapter
        binding.recyclerMainProductShip.addItemDecoration(CustomItemOffsetDecoration(requireActivity(), R.dimen.height_60))
        binding.recyclerMainProductShip.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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
            override fun changeLike() {

            }

            override fun onItemClick(position: Int, view: View) {

//                if (!PplusCommonUtil.loginCheck(activity!!)) {
//                    return
//                }

                val intent = Intent(activity, ProductShipDetailActivity::class.java)
                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
                activity?.startActivityForResult(intent, Const.REQ_DETAIL)
            }
        })

        binding.swipeMainProductShip.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                setData()
                binding.swipeMainProductShip.isRefreshing = false
            }
        })

        setData()
    }

    private fun setData(){
        val random = Random()
        mSort = mSorts[random.nextInt(mSorts.size)]
        mPaging = 0
        listCall(mPaging)
    }

    private fun listCall(page: Int) {

        mLockListView = true
        val params = HashMap<String, String>()
        params["page"] = page.toString()
        params["sort"] = "pick,desc,$mSort"
        showProgress("")
        ApiBuilder.create().getProductPriceListShipTypeByPageAndDiscount(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<ProductPrice>>> {
            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<ProductPrice>>>?,
                                    response: NewResultResponse<SubResultResponse<ProductPrice>>?) {
                hideProgress()
                if (!isAdded) {
                    return
                }

                if (response?.data != null) {
                    mIsLast = response.data.last!!
                    if (response.data.first!!) {
                        mTotalCount = response.data.totalElements!!
//                        text_main_product_ship_count?.text = getString(R.string.format_goods, FormatUtil.getMoneyType(mTotalCount.toString()))
                        if(mTotalCount > 0){
                            binding.layoutMainProductShipNotExist.visibility = View.GONE
                        }else{
                            binding.layoutMainProductShipNotExist.visibility = View.VISIBLE
                        }

                        mAdapter!!.clear()
                    }

                    mLockListView = false
                    mAdapter!!.addAll(response.data.content!!)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<ProductPrice>>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<SubResultResponse<ProductPrice>>?) {
                if (!isAdded) {
                    return
                }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
        }
    }

    override fun getPID(): String {
        return ""
    }

    companion object {

        @JvmStatic
        fun newInstance() =
                MainProductShipFragment().apply {
                    arguments = Bundle().apply {
//                        putParcelable(Const.PAGE, page)
                    }
                }
    }
}
