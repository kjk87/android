package com.pplus.luckybol.apps.event.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.common.ui.base.BaseFragment
import com.pplus.luckybol.apps.event.data.LottoHistoryAdapter
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.Event
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.databinding.FragmentLottoHistoryBinding
import com.pplus.networks.common.PplusCallback
import retrofit2.Call
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [LottoHistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LottoHistoryFragment : BaseFragment<BaseActivity>() {

    // TODO: Rename and change types of parameters


    private var mPage: Int = 0
    private var mTotalCount = 0
    private var mLayoutManager: LinearLayoutManager? = null
    private var mLockListView = true
    private var mAdapter: LottoHistoryAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
    }

    private var _binding: FragmentLottoHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentLottoHistoryBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun init() {

        mLayoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerLottoHistory.layoutManager = mLayoutManager
        mAdapter = LottoHistoryAdapter()
        binding.recyclerLottoHistory.adapter = mAdapter
        binding.recyclerLottoHistory.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            internal var pastVisibleItems: Int = 0
            internal var visibleItemCount: Int = 0
            internal var totalItemCount: Int = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                super.onScrolled(recyclerView, dx, dy)
                visibleItemCount = mLayoutManager!!.childCount
                totalItemCount = mLayoutManager!!.itemCount
                pastVisibleItems = mLayoutManager!!.findFirstVisibleItemPosition()
                if (!mLockListView) {
                    if (totalItemCount < mTotalCount && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
                        mPage++
                        listCall(mPage)
                    }
                }
            }
        })

        getCount()
    }


    private fun getCount() {
        val params = HashMap<String, String>()
        params["platform"] = "aos"
        params["appType"] = Const.APP_TYPE

        showProgress("")
        ApiBuilder.create().getLottoHistoryCount(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {

            override fun onResponse(call: Call<NewResultResponse<Int>>, response: NewResultResponse<Int>) {
                hideProgress()
                if (!isAdded) {
                    return
                }

                mTotalCount = response.data!!

                if (mTotalCount == 0) {
                    binding.layoutLottoHistoryNotExist.visibility = View.VISIBLE
                } else {
                    binding.layoutLottoHistoryNotExist.visibility = View.GONE
                }

                mPage = 1
                mAdapter?.clear()
                listCall(mPage)
            }

            override fun onFailure(call: Call<NewResultResponse<Int>>, t: Throwable, response: NewResultResponse<Int>) {

                hideProgress()
                if (!isAdded) {
                    return
                }
            }
        }).build().call()
    }

    private fun listCall(page: Int) {
        val params = HashMap<String, String>()
        params["pg"] = page.toString()
        params["platform"] = "aos"
        params["appType"] = Const.APP_TYPE
        showProgress("")
        mLockListView = true
        ApiBuilder.create().getLottoHistoryList(params).setCallback(object : PplusCallback<NewResultResponse<Event>> {

            override fun onResponse(call: Call<NewResultResponse<Event>>, response: NewResultResponse<Event>) {

                hideProgress()
                if (!isAdded) {
                    return
                }

                mLockListView = false
                mAdapter?.addAll(response.datas!!)
            }

            override fun onFailure(call: Call<NewResultResponse<Event>>, t: Throwable, response: NewResultResponse<Event>) {

                hideProgress()
                if (!isAdded) {
                    return
                }

            }
        }).build().call()
    }


    override fun getPID(): String {
        return ""
    }

    companion object {

        fun newInstance(): LottoHistoryFragment {

            val fragment = LottoHistoryFragment()
//            val args = Bundle()
//            args.putSerializable(Const.GROUP, group)
//            fragment.arguments = args
            return fragment
        }
    }

}// Required empty public constructor
