package com.pplus.luckybol.apps.main.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.viewpager2.widget.ViewPager2
import com.buzzvil.buzzad.benefit.BuzzAdBenefit
import com.buzzvil.buzzad.benefit.presentation.feed.BuzzAdFeed
import com.buzzvil.buzzad.benefit.privacy.PrivacyPolicyEventListener
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.bol.ui.CashExchangeActivity
import com.pplus.luckybol.apps.common.mgmt.CountryConfigManager
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.common.ui.base.BaseFragment
import com.pplus.luckybol.apps.event.ui.*
import com.pplus.luckybol.apps.main.data.HomeBannerAdapter
import com.pplus.luckybol.apps.mobilegift.ui.MobileGiftActivity
import com.pplus.luckybol.apps.my.ui.BolChargeStationActivity
import com.pplus.luckybol.apps.point.ui.PointHistoryActivity
import com.pplus.luckybol.apps.product.ui.ProductShipDetailActivity
import com.pplus.luckybol.apps.product.ui.ShoppingGroupProductActivity
import com.pplus.luckybol.apps.recommend.ui.InviteActivity
import com.pplus.luckybol.apps.setting.ui.NoticeDetailActivity
import com.pplus.luckybol.core.code.common.MoveType1Code
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.*
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.FragmentHomeBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import retrofit2.Call

class HomeFragment : BaseFragment<BaseActivity>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { //            param1 = it.getString(ARG_PARAM1)
            //            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun getPID(): String? {
        return ""
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    lateinit var mBannerAdapter: HomeBannerAdapter

    override fun init() {
        mBannerAdapter = HomeBannerAdapter()

        binding.pagerHomeBanner.adapter = mBannerAdapter

        mBannerAdapter.listener = object : HomeBannerAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {

                val item = mBannerAdapter.getItem(position)

                when (item.moveType) {
                    MoveType1Code.inner.name -> {
                        when (item.innerType) {
                            "online" -> {
                                val productPrice = ProductPrice()
                                productPrice.seqNo = item.moveTarget!!.toLong()

                                val intent = Intent(requireActivity(), ProductShipDetailActivity::class.java)
                                intent.putExtra(Const.DATA, productPrice)
                                signInLauncher.launch(intent)
                            }
                            "eventA" -> {
                                val intent = Intent(requireActivity(), EventActivity::class.java)
                                intent.putExtra(Const.GROUP, 1)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                signInLauncher.launch(intent)
                            }
                            "eventB" -> {
                                val intent = Intent(requireActivity(), EventActivity::class.java)
                                intent.putExtra(Const.GROUP, 2)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                signInLauncher.launch(intent)
                            }
                            "playA" -> {
                                val intent = Intent(requireActivity(), EventActivity::class.java)
                                intent.putExtra(Const.GROUP, 3)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                signInLauncher.launch(intent)
                            }
                            "event" -> {
                                val event = Event()
                                event.no = item.moveTarget!!.toLong()
                                val intent = Intent(requireActivity(), EventDetailActivity::class.java)
                                intent.putExtra(Const.DATA, event)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                signInLauncher.launch(intent)
                            }
                            "lotto" -> {
                                val event = Event()
                                event.no = item.moveTarget!!.toLong()
                                val intent = Intent(requireActivity(), LuckyLottoDetailActivity::class.java)
                                intent.putExtra(Const.DATA, event)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                signInLauncher.launch(intent)
                            }
                            "notice" -> {
                                val notice = Notice()
                                notice.no = item.moveTarget!!.toLong()
                                val intent = Intent(requireActivity(), NoticeDetailActivity::class.java)
                                intent.putExtra(Const.NOTICE, notice)
                                signInLauncher.launch(intent)
                            }
                            "shoppingGroup"->{
                                val shoppingGroup = ShoppingGroup()
                                shoppingGroup.seqNo = item.moveTarget!!.toLong()
                                val intent = Intent(requireActivity(), ShoppingGroupProductActivity::class.java)
                                intent.putExtra(Const.DATA, shoppingGroup)
                                signInLauncher.launch(intent)
                            }
                        }

                    }
                    MoveType1Code.outer.name -> {
                        PplusCommonUtil.openChromeWebView(requireActivity(), item.moveTarget!!)
                    }
                }
            }
        }

        binding.layoutHomeEvent1.setOnClickListener {
            val intent = Intent(requireActivity(), EventActivity::class.java)
            intent.putExtra(Const.GROUP, 1)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            signInLauncher.launch(intent)
        }

        binding.layoutHomeEvent2.setOnClickListener {
            val intent = Intent(requireActivity(), EventActivity::class.java)
            intent.putExtra(Const.GROUP, 2)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            signInLauncher.launch(intent)
        }

        binding.layoutHomePlayA.setOnClickListener {
            val intent = Intent(requireActivity(), EventActivity::class.java)
            intent.putExtra(Const.GROUP, 3)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            signInLauncher.launch(intent)
        }

        binding.layoutHomeWinner.setOnClickListener {
            val intent = Intent(requireActivity(), EventReviewActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            signInLauncher.launch(intent)
        }
        val recommendBol = FormatUtil.getMoneyTypeFloat(CountryConfigManager.getInstance().config.properties.recommendBol.toString())
        binding.textHomeInviteDesc.text = getString(R.string.format_msg_invite_desc, recommendBol)
        binding.layoutHomeInvite.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
                return@setOnClickListener
            }
            val intent = Intent(activity, InviteActivity::class.java)
            intent.putExtra(Const.KEY, "main")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            cashChangeLauncher.launch(intent)
        }

        binding.layoutHomeReferral.setOnClickListener{
            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
                return@setOnClickListener
            }
            val intent = Intent(activity, InviteActivity::class.java)
            intent.putExtra(Const.KEY, "referral")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            cashChangeLauncher.launch(intent)
        }

        binding.layoutHomeAttendance.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
                return@setOnClickListener
            }

            val intent = Intent(activity, AttendanceActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            cashChangeLauncher.launch(intent)
        }

        binding.layoutHomeNumberEvent.setOnClickListener {
            val intent = Intent(activity, NumberEventActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            signInLauncher.launch(intent)
        }

        binding.layoutHomeRandomPlay.setOnClickListener {
            val intent = Intent(activity, RandomPlayActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            signInLauncher.launch(intent)
        }

        binding.layoutHomeBuzvil.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
                return@setOnClickListener
            }

            if (BuzzAdBenefit.getPrivacyPolicyManager()!!.isConsentGranted()) {
                setAnalytics(requireActivity(), "Home_Buzzvil feed")
                BuzzAdFeed.Builder().build().show(requireActivity())
            } else {
                BuzzAdBenefit.getPrivacyPolicyManager()?.showConsentUI(requireActivity(), object : PrivacyPolicyEventListener {
                    override fun onUpdated(accepted: Boolean) {
                        if (accepted) {
                            PplusCommonUtil.setBuzvilProfileData()
                            setAnalytics(activity!!, "Home_Buzzvil feed")
                            BuzzAdFeed.Builder().build().show(requireActivity())
                        }

                    }
                })
            }
        }

        binding.layoutHomeChargeStation.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
                return@setOnClickListener
            }
            val intent = Intent(activity, BolChargeStationActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            cashChangeLauncher.launch(intent)
        }

        binding.layoutHomeGiftishow.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
                return@setOnClickListener
            }
            val intent = Intent(activity, MobileGiftActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            cashChangeLauncher.launch(intent)
        }

        binding.layoutHomeCashExchange.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
                return@setOnClickListener
            }
            val intent = Intent(activity, CashExchangeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            cashChangeLauncher.launch(intent)
        }

        binding.layoutHomeTelegram.setOnClickListener {
            val intent = Intent(activity, SnsEventActivity::class.java)
            intent.putExtra(Const.KEY, Const.TELEGRAM)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.layoutHomeKakaoChannel.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("kakaoplus://plusfriend/home/@버프"))
                startActivity(intent)
            } catch (e: Exception) {
                PplusCommonUtil.openChromeWebView(requireActivity(), "https://pf.kakao.com/_Psibxj")
            }
        }

        binding.textHomeRetentionPoint.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
                return@setOnClickListener
            }
            val intent = Intent(activity, PointHistoryActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            cashChangeLauncher.launch(intent)
        }

        getHomeBanner()
    }

    private fun getHomeBanner() {
        val params = HashMap<String, String>()
        params["platform"] = "aos"
        params["type"] = "home"
        ApiBuilder.create().getBannerList(params).setCallback(object : PplusCallback<NewResultResponse<Banner>> {

            override fun onResponse(call: Call<NewResultResponse<Banner>>?,
                                    response: NewResultResponse<Banner>?) {
                if (response?.datas != null) {
                    if (!isAdded) {
                        return
                    }

                    val bannerList = response.datas!!

                    if (bannerList.isEmpty()) {
                        binding.layoutHomeBanner.visibility = View.GONE
                    } else {
                        binding.layoutHomeBanner.visibility = View.VISIBLE
                        binding.textHomeBannerPage.text = "1/${bannerList.size}"
                        binding.pagerHomeBanner.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                            override fun onPageSelected(position: Int) {
                                binding.textHomeBannerPage.text = "${position + 1}/${bannerList.size}"
                            }
                        })
                        mBannerAdapter.setDataList(bannerList as MutableList<Banner>)
                        binding.textHomeBannerPage.text = "${binding.pagerHomeBanner.currentItem + 1}/${bannerList.size}"
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Banner>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<Banner>?) {

            }
        }).build().call()
    }

    val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            if (LoginInfoManager.getInstance().isMember) {
//                binding.textHomeRetentionBol.visibility = View.VISIBLE
                binding.textHomeRetentionPoint.visibility = View.VISIBLE
                setRetentionBol()
            } else {
//                binding.textHomeRetentionBol.visibility = View.GONE
                binding.textHomeRetentionPoint.visibility = View.GONE
            }
        }
    }

    val cashChangeLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (LoginInfoManager.getInstance().isMember) {
            setRetentionBol()
        }
    }

    fun setRetentionBol() {
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {
//                binding.textHomeRetentionBol.text = getString(R.string.format_bol2, FormatUtil.getMoneyTypeFloat(LoginInfoManager.getInstance().user.totalBol.toString()))
                binding.textHomeRetentionPoint.text = getString(R.string.format_cash, FormatUtil.getMoneyTypeFloat(LoginInfoManager.getInstance().user.point.toString()))
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment().apply {
            arguments = Bundle().apply { //                putString(ARG_PARAM1, param1)
                //                putString(ARG_PARAM2, param2)
            }
        }
    }
}