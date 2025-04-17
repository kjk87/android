package com.root37.buflexz.apps.luckydraw.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.root37.buflexz.Const
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.apps.common.ui.base.BaseFragment
import com.root37.buflexz.apps.luckydraw.data.LuckyDrawPurchaseAdapter
import com.root37.buflexz.core.network.ApiBuilder
import com.root37.buflexz.core.network.model.dto.LuckyDrawPurchase
import com.root37.buflexz.core.network.model.response.ListResultResponse
import com.root37.buflexz.core.network.model.response.NewResultResponse
import com.root37.buflexz.databinding.FragmentMyLuckyDrawPurchaseBinding
import retrofit2.Call

class MyLuckyDrawPurchaseFragment : BaseFragment<BaseActivity>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

            //            param1 = it.getString(ARG_PARAM1)
            //            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var _binding: FragmentMyLuckyDrawPurchaseBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentMyLuckyDrawPurchaseBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun getPID(): String? {
        return ""
    }

    var mAdapter: LuckyDrawPurchaseAdapter? = null
    private lateinit var mLayoutManager: LinearLayoutManager
    private var mTotalCount = 0
    private var mLockListView = false
    private var mPaging = 1

    override fun init() {
        mLayoutManager = LinearLayoutManager(requireActivity())
        mAdapter = LuckyDrawPurchaseAdapter()
        binding.recyclerMyLuckyDrawPurchase.adapter = mAdapter
        binding.recyclerMyLuckyDrawPurchase.layoutManager = mLayoutManager

        binding.recyclerMyLuckyDrawPurchase.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        mPaging = 1
        getList(mPaging)
    }

    private fun getList(page: Int) {
        val params = HashMap<String, String>()
        params["paging[page]"] = page.toString()
        params["paging[limit]"] = "20"
        showProgress("")
        mLockListView = true
        ApiBuilder.create().getLuckyDrawPurchaseList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<LuckyDrawPurchase>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<LuckyDrawPurchase>>>?, response: NewResultResponse<ListResultResponse<LuckyDrawPurchase>>?) {
                hideProgress()
                if (response?.result != null) {
                    if (page == 1) {
                        mAdapter!!.clear()

                        mTotalCount = response.result!!.total!!
                        if (mTotalCount == 0) {
                            binding.layoutMyLuckyDrawPurchaseNotExist.visibility = View.VISIBLE
                        } else {
                            binding.layoutMyLuckyDrawPurchaseNotExist.visibility = View.GONE
                        }

                    }

                    mLockListView = false

                    if (response.result!!.list != null) {
                        val dataList = response.result!!.list!!
                        mAdapter!!.addAll(dataList)
                    }

                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<LuckyDrawPurchase>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<LuckyDrawPurchase>>?) {
                hideProgress()
            }
        }).build().call()
    }

    companion object {

        @JvmStatic
        fun newInstance() = MyLuckyDrawPurchaseFragment().apply {
            arguments = Bundle().apply {}
        }
    }
}