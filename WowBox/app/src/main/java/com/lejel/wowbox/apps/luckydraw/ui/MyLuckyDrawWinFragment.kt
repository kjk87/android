package com.lejel.wowbox.apps.luckydraw.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.lejel.wowbox.Const
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.common.ui.base.BaseFragment
import com.lejel.wowbox.apps.luckydraw.data.MyLuckyDrawWinAdapter
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.LuckyDrawWin
import com.lejel.wowbox.core.network.model.response.ListResultResponse
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.databinding.FragmentMyLuckyDrawWinBinding
import retrofit2.Call

class MyLuckyDrawWinFragment : BaseFragment<BaseActivity>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

            //            param1 = it.getString(ARG_PARAM1)
            //            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var _binding: FragmentMyLuckyDrawWinBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentMyLuckyDrawWinBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun getPID(): String? {
        return ""
    }

    var mAdapter: MyLuckyDrawWinAdapter? = null
    private lateinit var mLayoutManager: LinearLayoutManager
    private var mTotalCount = 0
    private var mLockListView = false
    private var mPaging = 1

    override fun init() {
        mLayoutManager = LinearLayoutManager(requireActivity())
        mAdapter = MyLuckyDrawWinAdapter()
        binding.recyclerMyLuckyDrawWin.adapter = mAdapter
        binding.recyclerMyLuckyDrawWin.layoutManager = mLayoutManager

        binding.recyclerMyLuckyDrawWin.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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
        ApiBuilder.create().getLuckyDrawMyWinListAll(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<LuckyDrawWin>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<LuckyDrawWin>>>?, response: NewResultResponse<ListResultResponse<LuckyDrawWin>>?) {
                hideProgress()
                if (response?.result != null) {
                    if (page == 1) {
                        mAdapter!!.clear()

                        mTotalCount = response.result!!.total!!
                        if (mTotalCount == 0) {
                            binding.layoutMyLuckyDrawWinNotExist.visibility = View.VISIBLE
                        } else {
                            binding.layoutMyLuckyDrawWinNotExist.visibility = View.GONE
                        }

                    }

                    mLockListView = false

                    if (response.result!!.list != null) {
                        val dataList = response.result!!.list!!
                        mAdapter!!.addAll(dataList)
                    }

                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<LuckyDrawWin>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<LuckyDrawWin>>?) {
                hideProgress()
            }
        }).build().call()
    }

    companion object {

        @JvmStatic
        fun newInstance() = MyLuckyDrawWinFragment().apply {
            arguments = Bundle().apply {}
        }
    }
}