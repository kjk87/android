package com.lejel.wowbox.apps.main.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.apps.common.ui.base.BaseFragment
import com.lejel.wowbox.apps.login.LoginActivity2
import com.lejel.wowbox.apps.main.data.MainMiningAdapter
import com.lejel.wowbox.apps.wallet.ui.WalletActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.WalletRes
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.AdmobUtil
import com.lejel.wowbox.databinding.FragmentMainMiningBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call

class MainMiningFragment : BaseFragment<MainActivity>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { //            param1 = it.getString(ARG_PARAM1)
            //            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var _binding: FragmentMainMiningBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentMainMiningBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun getPID(): String? {
        return ""
    }

    private lateinit var mAdapter:MainMiningAdapter

    override fun init() {
        AdmobUtil.getInstance(requireActivity()).initRewardAd()
        binding.pagerMainMining.offscreenPageLimit = 3

        mAdapter = MainMiningAdapter()
        mAdapter.launcher = defaultLauncher
        binding.pagerMainMining.adapter = mAdapter
        binding.pagerMainMining.setPageTransformer(MarginPageTransformer(resources.getDimensionPixelOffset(R.dimen.width_50)))


        binding.pagerMainMining.setPadding(resources.getDimensionPixelOffset(R.dimen.width_94), 0, resources.getDimensionPixelOffset(R.dimen.width_94), 0)

        binding.pagerMainMining.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                binding.textMainMiningPage.text = "${position + 1}/${mAdapter.itemCount}"
            }
        })

        binding.textMainMiningPage.text = "1/${mAdapter.itemCount}"
        loginCheck()
    }

    private fun loginCheck() {
        if (LoginInfoManager.getInstance().isMember()) {
            binding.layoutMainMiningMember.visibility = View.VISIBLE
            binding.textMainMiningLogin.visibility = View.GONE
            Glide.with(this).load(LoginInfoManager.getInstance().member!!.profile).apply(RequestOptions().centerCrop().placeholder(R.drawable.ic_profile_default).error(R.drawable.ic_profile_default)).into(binding.imageMainMiningProfile)
            binding.textMainMiningNickname.text = LoginInfoManager.getInstance().member!!.nickname
            mAdapter.notifyDataSetChanged()

            if(LoginInfoManager.getInstance().member!!.isAuthEmail!! && StringUtils.isNotEmpty(LoginInfoManager.getInstance().member!!.authEmail)){
                duplicateUser(LoginInfoManager.getInstance().member!!.authEmail!!)
            }else{
                binding.textMainMiningRetentionBuff.visibility = View.GONE
            }
        } else {
            binding.layoutMainMiningMember.visibility = View.GONE
            binding.textMainMiningLogin.visibility = View.VISIBLE
            binding.textMainMiningLogin.setOnClickListener {
                val intent = Intent(requireActivity(), LoginActivity2::class.java)
                defaultLauncher.launch(intent)
            }
        }
    }

    private fun duplicateUser(email: String) {
        val params = HashMap<String, String>()
        params["email"] = email
        ApiBuilder.create().walletDuplicateUser(params).setCallback(object : PplusCallback<NewResultResponse<WalletRes>> {
            override fun onResponse(call: Call<NewResultResponse<WalletRes>>?, response: NewResultResponse<WalletRes>?) {

                if (!isAdded) {
                    return
                }

                if (response?.result != null) {
                    if (response.result!!.result == "SUCCESS") { //미가입
                        binding.textMainMiningRetentionBuff.visibility = View.GONE
                    } else {
                        binding.textMainMiningRetentionBuff.visibility = View.VISIBLE
                        getBuffCoinBalance()
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<WalletRes>>?, t: Throwable?, response: NewResultResponse<WalletRes>?) {
            }
        }).build().call()
    }

    private fun getBuffCoinBalance() {
        ApiBuilder.create().getBuffCoinBalance().setCallback(object : PplusCallback<NewResultResponse<Map<String, Any>>> {
            override fun onResponse(call: Call<NewResultResponse<Map<String, Any>>>?,
                                    response: NewResultResponse<Map<String, Any>>?) {
                if (!isAdded) {
                    return
                }
                if (response?.result != null) {
                    LogUtil.e(LOG_TAG, response.result!!.toString())
                    val buffCoin = response.result!!["buff"]
                    val usdt = response.result!!["usdt"]
                    val totalUsdt = response.result!!["totalUsdt"]
                    binding.textMainMiningRetentionBuff.text = FormatUtil.getCoinType(buffCoin.toString())

                    binding.textMainMiningRetentionBuff.setOnClickListener {
                        val intent = Intent(requireActivity(), WalletActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        defaultLauncher.launch(intent)
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Map<String, Any>>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<Map<String, Any>>?) {
            }
        }).build().call()
    }

    val defaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        loginCheck()
    }

    companion object {

        @JvmStatic
        fun newInstance() = MainMiningFragment().apply {
            arguments = Bundle().apply { //                putString(ARG_PARAM1, param1)
                //                putString(ARG_PARAM2, param2)
            }
        }
    }
}