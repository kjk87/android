package com.lejel.wowbox.apps.main.ui

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.apps.common.ui.base.BaseFragment
import com.lejel.wowbox.apps.login.LoginActivity2
import com.lejel.wowbox.apps.main.data.MainPointMallAdapter
import com.lejel.wowbox.apps.point.ui.PointConfigActivity
import com.lejel.wowbox.apps.point.ui.PointExchangeActivity
import com.lejel.wowbox.apps.withdraw.ui.WithdrawListActivity
import com.lejel.wowbox.apps.withdraw.ui.WithdrawPointActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.PointMallCategory
import com.lejel.wowbox.core.network.model.response.ListResultResponse
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.FragmentMainPointMallBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import retrofit2.Call


class MainPointMallFragment : BaseFragment<MainActivity>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { //            param1 = it.getString(ARG_PARAM1)
            //            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var _binding: FragmentMainPointMallBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentMainPointMallBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun getPID(): String? {
        return ""
    }

    private lateinit var mAdapter: MainPointMallAdapter

    override fun init() {

        mAdapter = MainPointMallAdapter()
        mAdapter.listener = object : MainPointMallAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                setEvent(requireActivity(), "wowmall")
                if (!PplusCommonUtil.loginCheck(requireActivity(), defaultLauncher)) {
                    return
                }
                PplusCommonUtil.wowMallJoin()

                val item = mAdapter.getItem(position)

                if (item.url == "home") {
                    val url = "https://wowboxmall.com"
                    PplusCommonUtil.openChromeWebView(requireActivity(), url)
                } else {
                    PplusCommonUtil.openChromeWebView(requireActivity(), item.url!!)
                }

                //                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.url))
                //                startActivity(intent)
            }
        }
        binding.recyclerMainPointMall.layoutManager = GridLayoutManager(requireActivity(), 3)
        binding.recyclerMainPointMall.addItemDecoration(SpacesItemDecoration(resources.getDimensionPixelSize(R.dimen.width_30)))
        binding.recyclerMainPointMall.adapter = mAdapter

        binding.textMainPointMallPoint.setOnClickListener {
            if (LoginInfoManager.getInstance().isMember()) {
                val intent = Intent(requireActivity(), PointConfigActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                defaultLauncher.launch(intent)
            } else {
                val intent = Intent(requireActivity(), LoginActivity2::class.java)
                defaultLauncher.launch(intent)
            }
        }

        binding.imageMainPointMall.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), defaultLauncher)) {
                return@setOnClickListener
            }
            PplusCommonUtil.wowMallJoin()
            setEvent(requireActivity(), "wowmall")
            val url = "https://wowboxmall.com"
            PplusCommonUtil.openChromeWebView(requireActivity(), url)
        }

        binding.textMainPointMallPointExchange.setOnClickListener {

            if (!PplusCommonUtil.loginCheck(requireActivity(), defaultLauncher)) {
                return@setOnClickListener
            }

            PplusCommonUtil.wowMallJoin()

            val intent = Intent(requireActivity(), PointExchangeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        binding.textMainPointMallPointExchange2.setOnClickListener {

            if (!PplusCommonUtil.loginCheck(requireActivity(), defaultLauncher)) {
                return@setOnClickListener
            }

            val intent = Intent(requireActivity(), PointExchangeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        binding.textMainPointMallWithdraw.setOnClickListener {

            if (!PplusCommonUtil.loginCheck(requireActivity(), defaultLauncher)) {
                return@setOnClickListener
            }

            val intent = Intent(requireActivity(), WithdrawPointActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        binding.textMainPointMallWithdrawHistory.setOnClickListener {

            if (!PplusCommonUtil.loginCheck(requireActivity(), defaultLauncher)) {
                return@setOnClickListener
            }

            val intent = Intent(requireActivity(), WithdrawListActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

//        binding.textMainPointMallPointConfig.setOnClickListener {
//
//            if (!PplusCommonUtil.loginCheck(requireActivity(), defaultLauncher)) {
//                return@setOnClickListener
//            }
//
//            val intent = Intent(requireActivity(), PointConfigActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            defaultLauncher.launch(intent)
//        }

        binding.layoutMainPointMallLoading.visibility = View.VISIBLE

        loginCheck()
        getList()
    }

    internal inner class SpacesItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val itemPosition = (view.layoutParams as RecyclerView.LayoutParams).absoluteAdapterPosition
            if (itemPosition % 4 == 3) {
                outRect.right = 0
            } else {
                outRect.right = space
            }
            outRect.bottom = space // Add top margin only for the first item to avoid double space between items
        }
    }

    private fun getList() {
        ApiBuilder.create().getPointMallCategoryList().setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<PointMallCategory>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<PointMallCategory>>>?, response: NewResultResponse<ListResultResponse<PointMallCategory>>?) {

                if (!isAdded) {
                    return
                }

                binding.layoutMainPointMallLoading.visibility = View.GONE

                if (response?.result != null && response.result!!.list != null) {

                    mAdapter.setDataList(response.result!!.list as MutableList<PointMallCategory>)

                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<PointMallCategory>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<PointMallCategory>>?) {
                if (!isAdded) {
                    return
                }

                binding.layoutMainPointMallLoading.visibility = View.GONE
            }
        }).build().call()
    }

    private fun loginCheck() {
        if (LoginInfoManager.getInstance().isMember()) {
            binding.textPointMallLogin.visibility = View.GONE
            reloadSession()
        } else {
            binding.textMainPointMallPoint.setText(R.string.word_login_join)
            binding.textPointMallLogin.visibility = View.VISIBLE
            binding.textPointMallLogin.setOnClickListener {
                val intent = Intent(requireActivity(), LoginActivity2::class.java)
                defaultLauncher.launch(intent)
            }
        }
    }

    private fun reloadSession() {
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {
                if (!isAdded) {
                    return
                }
                binding.textMainPointMallPoint.text = getString(R.string.format_point_unit, FormatUtil.getMoneyType(LoginInfoManager.getInstance().member!!.point!!.toInt().toString()))
            }
        })
    }

    val defaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        loginCheck()
    }

    companion object {

        @JvmStatic
        fun newInstance() = MainPointMallFragment().apply {
            arguments = Bundle().apply { //                putString(ARG_PARAM1, param1)
                //                putString(ARG_PARAM2, param2)
            }
        }
    }
}