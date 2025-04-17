package com.lejel.wowbox.apps.luckydraw.ui

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.common.ui.base.BaseFragment
import com.lejel.wowbox.apps.main.data.LuckyDrawAdapter
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.LuckyDraw
import com.lejel.wowbox.core.network.model.dto.LuckyDrawGroup
import com.lejel.wowbox.core.network.model.response.ListResultResponse
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.FragmentLuckyDrawByGroupBinding
import retrofit2.Call


/**
 * A simple [Fragment] subclass.
 * Use the [LuckyDrawCompleteByGroupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LuckyDrawCompleteByGroupFragment : BaseFragment<BaseActivity>() { //    private var param1: String? = null
    //    private var param2: String? = null

    private lateinit var mLuckyDrawGroup: LuckyDrawGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mLuckyDrawGroup = PplusCommonUtil.getParcelable(it, Const.DATA, LuckyDrawGroup::class.java)!!
        }
    }

    private var _binding: FragmentLuckyDrawByGroupBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentLuckyDrawByGroupBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun getPID(): String? {
        return ""
    }

    private var mTotalCount = 0
    var mAdapter: LuckyDrawAdapter? = null
    private lateinit var mLayoutManager: LinearLayoutManager
    private var mLockListView = false
    private var mPaging = 1

    override fun init() {
        mAdapter = LuckyDrawAdapter()
        mAdapter!!.launcher = defaultLauncher
        mAdapter!!.checkPrivateLauncher = checkPrivateLauncher
        mLayoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerLuckyDrawByGroup.adapter = mAdapter
        binding.recyclerLuckyDrawByGroup.layoutManager = mLayoutManager

        binding.recyclerLuckyDrawByGroup.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        binding.textLuckyDrawBoyGroupNotExist1.setText(R.string.msg_not_exist_complete_lucky_draw)
        binding.textLuckyDrawBoyGroupNotExist2.visibility = View.GONE

        mPaging = 1
        getList(mPaging)
    }

    private fun getList(page: Int) {
        val params = HashMap<String, String>()
        params["paging[page]"] = page.toString()
        params["paging[limit]"] = "20"
        params["aos"] = "1"
        params["luckyDrawGroupSeqNo"] = mLuckyDrawGroup.seqNo.toString()

        mLockListView = true
        ApiBuilder.create().getLuckyDrawCompleteList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<LuckyDraw>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<LuckyDraw>>>?, response: NewResultResponse<ListResultResponse<LuckyDraw>>?) {
                if (!isAdded) {
                    return
                }
                if (response?.result != null) {
                    if (page == 1) {
                        mAdapter!!.clear()

                        mTotalCount = response.result!!.total!!
                        if (mTotalCount == 0) {
                            binding.layoutLuckyDrawByGroupNotExist.visibility = View.VISIBLE
                        } else {
                            binding.layoutLuckyDrawByGroupNotExist.visibility = View.GONE
                        }
                    }

                    mLockListView = false

                    if (response.result!!.list != null) {
                        val dataList = response.result!!.list!!
                        mAdapter!!.addAll(dataList)
                    }

                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<LuckyDraw>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<LuckyDraw>>?) {
            }
        }).build().call()
    }

    val defaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        mPaging = 1
        getList(mPaging)
    }

    private val checkPrivateLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val item = PplusCommonUtil.getParcelableExtra(result.data!!, Const.DATA, LuckyDraw::class.java)

            val intent = Intent(requireActivity(), LuckyDrawJoinActivity::class.java)
            intent.putExtra(Const.DATA, item)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(luckyDrawGroup: LuckyDrawGroup) = LuckyDrawCompleteByGroupFragment().apply {
            arguments = Bundle().apply {
                putParcelable(Const.DATA, luckyDrawGroup)
            }
        }
    }
}