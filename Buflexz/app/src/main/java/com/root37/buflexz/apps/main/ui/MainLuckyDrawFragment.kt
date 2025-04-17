package com.root37.buflexz.apps.main.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.root37.buflexz.Const
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.mgmt.LoginInfoManager
import com.root37.buflexz.apps.common.mgmt.NationManager
import com.root37.buflexz.apps.common.ui.base.BaseFragment
import com.root37.buflexz.apps.login.LoginActivity
import com.root37.buflexz.apps.luckydraw.ui.LuckyDrawCompleteListActivity
import com.root37.buflexz.apps.luckydraw.ui.LuckyDrawGuideActivity
import com.root37.buflexz.apps.luckydraw.ui.MyLuckyDrawHistoryActivity
import com.root37.buflexz.apps.main.data.LuckyDrawAdapter
import com.root37.buflexz.core.network.ApiBuilder
import com.root37.buflexz.core.network.model.dto.LuckyDraw
import com.root37.buflexz.core.network.model.response.ListResultResponse
import com.root37.buflexz.core.network.model.response.NewResultResponse
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.FragmentMainLuckyDrawBinding
import retrofit2.Call


/**
 * A simple [Fragment] subclass.
 * Use the [MainLuckyDrawFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainLuckyDrawFragment : BaseFragment<MainActivity>() { //    private var param1: String? = null
    //    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) //        arguments?.let {
        //            param1 = it.getString(ARG_PARAM1)
        //            param2 = it.getString(ARG_PARAM2)
        //        }
    }

    private var _binding: FragmentMainLuckyDrawBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentMainLuckyDrawBinding.inflate(inflater, container, false)
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
        mLayoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerMainLuckyDraw.adapter = mAdapter
        binding.recyclerMainLuckyDraw.layoutManager = mLayoutManager

        binding.recyclerMainLuckyDraw.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        binding.layoutMainLuckyDrawComplete.setOnClickListener {
            val intent = Intent(requireActivity(), LuckyDrawCompleteListActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        binding.layoutMainLuckyDrawGuide.setOnClickListener {
            val intent = Intent(requireActivity(), LuckyDrawGuideActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        loginCheck()

        mPaging = 1
        getList(mPaging)
    }

    private fun loginCheck() {
        if (LoginInfoManager.getInstance().isMember()) {
            binding.layoutMainLuckyDrawMember.visibility = View.VISIBLE
            binding.textMainLuckyDrawLogin.visibility = View.GONE
            Glide.with(this).load(LoginInfoManager.getInstance().member!!.profile).apply(RequestOptions().centerCrop().placeholder(R.drawable.ic_profile_default).error(R.drawable.ic_profile_default)).into(binding.imageMainLuckyDrawProfile)
            binding.textMainLuckyDrawNickname.text = LoginInfoManager.getInstance().member!!.nickname
            Glide.with(requireActivity()).load(Uri.parse("file:///android_asset/flags/${LoginInfoManager.getInstance().member!!.nation!!.uppercase()}.png")).into(binding.imageMainLuckyDrawFlag)

            val nation = NationManager.getInstance().nationMap!![LoginInfoManager.getInstance().member!!.nation]
            if (nation!!.code == "KR") {
                binding.textMainLuckyDrawNation.text = nation.name
            } else {
                binding.textMainLuckyDrawNation.text = nation.nameEn
            }

            binding.layoutMainLuckyDrawMyPurchaseHistory.visibility = View.VISIBLE
            binding.layoutMainLuckyDrawMyPurchaseHistory.setOnClickListener {
                val intent = Intent(requireActivity(), MyLuckyDrawHistoryActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                defaultLauncher.launch(intent)
            }

            reloadSession()
        } else {
            binding.layoutMainLuckyDrawMember.visibility = View.GONE
            binding.textMainLuckyDrawLogin.visibility = View.VISIBLE
            binding.textMainLuckyDrawLogin.setOnClickListener {
                val intent = Intent(requireActivity(), LoginActivity::class.java)
                defaultLauncher.launch(intent)
            }
            binding.layoutMainLuckyDrawMyPurchaseHistory.visibility = View.GONE
        }
    }

    private fun reloadSession() {
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {
                if (!isAdded) {
                    return
                }

                binding.textMainLuckyDrawRetentionBall.text = getString(R.string.format_ball_unit, FormatUtil.getMoneyType(LoginInfoManager.getInstance().member!!.ball!!.toInt().toString()))
            }
        })
    }

    private fun getList(page: Int) {
        val params = HashMap<String, String>()
        params["paging[page]"] = page.toString()
        params["paging[limit]"] = "20"

        mLockListView = true
        ApiBuilder.create().getLuckyDrawList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<LuckyDraw>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<LuckyDraw>>>?, response: NewResultResponse<ListResultResponse<LuckyDraw>>?) {
                if (!isAdded) {
                    return
                }
                if (response?.result != null) {
                    if (page == 1) {
                        mAdapter!!.clear()

                        mTotalCount = response.result!!.total!!
                        if (mTotalCount == 0) {
                            binding.layoutMainLuckyDrawNotExist.visibility = View.VISIBLE
                        } else {
                            binding.layoutMainLuckyDrawNotExist.visibility = View.GONE
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
        loginCheck()
        mPaging = 1
        getList(mPaging)
    }

    companion object {

        @JvmStatic
        fun newInstance() = MainLuckyDrawFragment().apply {
            arguments = Bundle().apply { //                putString(ARG_PARAM1, param1)
                //                putString(ARG_PARAM2, param2)
            }
        }
    }
}