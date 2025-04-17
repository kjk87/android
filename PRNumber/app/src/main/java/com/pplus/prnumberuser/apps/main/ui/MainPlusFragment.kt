package com.pplus.prnumberuser.apps.main.ui

import android.app.Activity
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
import com.pplus.prnumberuser.apps.common.ui.custom.BottomItemOffsetDecoration
import com.pplus.prnumberuser.apps.main.data.MainPlusAdapter
import com.pplus.prnumberuser.apps.my.ui.MyPlusActivity
import com.pplus.prnumberuser.apps.search.ui.SearchActivity
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.LocationData
import com.pplus.prnumberuser.core.network.model.dto.Plus2
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.network.model.response.SubResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.FragmentMainPlusBinding
import retrofit2.Call
import java.util.*

class MainPlusFragment : BaseFragment<BaseActivity>() {

    private var mTotalCount: Int = 0
    private var mLockListView = false
    private var mPaging = 1
    private var mAdapter: MainPlusAdapter? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var mLocationData: LocationData? = null
    private var mIsLast = false

    val searchLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data
            mPaging = 0
            listCall(mPaging)
        }
    }

    private var _binding: FragmentMainPlusBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentMainPlusBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun init() {

//        layout_main_plus_search.setOnClickListener {
//            val intent = Intent(activity, SearchActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            activity?.startActivityForResult(intent, Const.REQ_SEARCH)
//        }

        binding.textMainPlusFind.setOnClickListener {
            val intent = Intent(activity, SearchActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            searchLauncher.launch(intent)
        }

        mLayoutManager = LinearLayoutManager(activity)
        binding.recyclerMainPlus.layoutManager = mLayoutManager!!
        mAdapter = MainPlusAdapter()
        binding.recyclerMainPlus.adapter = mAdapter
        binding.recyclerMainPlus.addItemDecoration(BottomItemOffsetDecoration(requireActivity(), R.dimen.height_120))

//        recycler_main_goods_plus.addOnScrollListener(RecyclerScaleScrollListener(parentActivity.layout_main_floating))
        binding.recyclerMainPlus.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        mAdapter!!.setOnItemClickListener(object : MainPlusAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {

            }
        })
        binding.layoutMainPlusLoading.visibility  = View.VISIBLE

        binding.textMainPlusPlusPage.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(),null)) {
                return@setOnClickListener
            }
            val intent = Intent(activity, MyPlusActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        mPaging = 0
        listCall(mPaging)
    }

    private fun listCall(page: Int) {

        mLockListView = true
        val params = HashMap<String, String>()
//        params["sort"] = "${EnumData.BuyGoodsSort.seqNo.name},${EnumData.BuyGoodsSort.desc.name}"
        params["page"] = page.toString()
//        showProgress("")
        ApiBuilder.create().getPlusListWithNews(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<Plus2>>> {

            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<Plus2>>>?, response: NewResultResponse<SubResultResponse<Plus2>>?) {
                if (!isAdded) {
                    return
                }
                binding.layoutMainPlusLoading.visibility  =View.GONE
                if (response != null) {

                    mIsLast = response.data.last!!
                    if (response.data.first!!) {
                        mTotalCount = response.data.totalElements!!
                        mAdapter!!.clear()
                        if (mTotalCount == 0) {
                            binding.layoutMainPlusNotExist.visibility = View.VISIBLE
                            binding.layoutMainPlusExist.visibility = View.GONE
                        } else {
                            binding.layoutMainPlusNotExist.visibility = View.GONE
                            binding.layoutMainPlusExist.visibility = View.VISIBLE
                        }
                    }

                    mLockListView = false
                    mAdapter!!.addAll(response.data.content!!)
                }

            }

            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<Plus2>>>?, t: Throwable?, response: NewResultResponse<SubResultResponse<Plus2>>?) {
//                hideProgress()
                binding.layoutMainPlusLoading.visibility  =View.GONE
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
        return "Main_plus product"
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
                MainPlusFragment().apply {
                    arguments = Bundle().apply {
                        //                        putString(ARG_PARAM1, param1)
//                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
