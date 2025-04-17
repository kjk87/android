package com.lejel.wowbox.apps.faq.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.common.ui.base.BaseFragment
import com.lejel.wowbox.apps.faq.data.FaqAdapter
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.Faq
import com.lejel.wowbox.core.network.model.dto.FaqCategory
import com.lejel.wowbox.core.network.model.response.ListResultResponse
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.FragmentFaqBinding
import retrofit2.Call
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FaqFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FaqFragment : BaseFragment<BaseActivity>() {

    private var mFaqCategory: FaqCategory? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mFaqCategory = PplusCommonUtil.getParcelable(it, Const.CATEGORY, FaqCategory::class.java)
        }
    }

    private var _binding: FragmentFaqBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentFaqBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun getPID(): String? {
        return ""
    }

    private var mTotalCount = 0
    var mAdapter: FaqAdapter? = null
    private lateinit var mLayoutManager: LinearLayoutManager
    private var mLockListView = false
    private var mPaging = 1

    override fun init() {
        mAdapter = FaqAdapter()
        mLayoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerFaq.adapter = mAdapter
        binding.recyclerFaq.layoutManager = mLayoutManager

        binding.recyclerFaq.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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
        if (mFaqCategory!!.seqNo != null) {
            params["category"] = mFaqCategory!!.seqNo.toString()
        }
        val country = Locale.getDefault().country
        params["nation"] = country.lowercase()
        params["aos"] = "1"

        mLockListView = true
        ApiBuilder.create().getFaqList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<Faq>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<Faq>>>?, response: NewResultResponse<ListResultResponse<Faq>>?) {
                hideProgress()
                if (response?.result != null) {
                    if (page == 1) {
                        mAdapter!!.clear()

                        mTotalCount = response.result!!.total!!
                        binding.textFaqCount.text = getString(R.string.format_faq_count, mFaqCategory!!.name, FormatUtil.getMoneyType(mTotalCount.toString()))

                    }

                    mLockListView = false

                    if (response.result!!.list != null) {
                        val dataList = response.result!!.list!!
                        mAdapter!!.addAll(dataList)
                    }

                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<Faq>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<Faq>>?) {
                hideProgress()
            }
        }).build().call()
    }

    companion object {

        @JvmStatic
        fun newInstance(faqCategory: FaqCategory) = FaqFragment().apply {
            arguments = Bundle().apply {
                putParcelable(Const.CATEGORY, faqCategory)
            }
        }
    }
}