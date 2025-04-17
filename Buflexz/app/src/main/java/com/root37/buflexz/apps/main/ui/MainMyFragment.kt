package com.root37.buflexz.apps.main.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.RelativeLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.JsonObject
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import com.pplus.utils.part.utils.time.DateFormatUtils
import com.root37.buflexz.Const
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.mgmt.LoginInfoManager
import com.root37.buflexz.apps.common.mgmt.NationManager
import com.root37.buflexz.apps.common.ui.base.BaseFragment
import com.root37.buflexz.apps.community.ui.CommunityApplyActivity
import com.root37.buflexz.apps.faq.ui.FaqActivity
import com.root37.buflexz.apps.giftcard.ui.GiftCardBrandActivity
import com.root37.buflexz.apps.giftcard.ui.GiftCardPurchaseHistoryActivity
import com.root37.buflexz.apps.inquire.ui.InquireListActivity
import com.root37.buflexz.apps.invite.ui.InviteActivity
import com.root37.buflexz.apps.login.LoginActivity
import com.root37.buflexz.apps.lottery.ui.LotteryGuideActivity
import com.root37.buflexz.apps.lottery.ui.LotteryResultActivity
import com.root37.buflexz.apps.lottery.ui.MyLotteryJoinListActivity
import com.root37.buflexz.apps.luckyball.ui.BallConfigActivity
import com.root37.buflexz.apps.luckyball.ui.LuckyBallExchangeActivity
import com.root37.buflexz.apps.luckydraw.ui.MyLuckyDrawHistoryActivity
import com.root37.buflexz.apps.main.data.HomeBannerAdapter
import com.root37.buflexz.apps.my.ui.BannerListActivity
import com.root37.buflexz.apps.my.ui.NotificationBoxActivity
import com.root37.buflexz.apps.my.ui.ProfileConfigActivity
import com.root37.buflexz.apps.my.ui.SettingActivity
import com.root37.buflexz.apps.notice.ui.NoticeActivity
import com.root37.buflexz.apps.partner.ui.PartnerCommissionConfigActivity
import com.root37.buflexz.apps.partner.ui.PartnerReqActivity
import com.root37.buflexz.apps.partner.ui.ProfitPartnerConfigActivity
import com.root37.buflexz.apps.point.ui.PointConfigActivity
import com.root37.buflexz.apps.product.ui.ProductCategoryActivity
import com.root37.buflexz.apps.product.ui.ProductPurchaseHistoryActivity
import com.root37.buflexz.apps.wallet.ui.WalletActivity
import com.root37.buflexz.apps.wallet.ui.WalletMakeActivity
import com.root37.buflexz.core.WalletSecureUtil
import com.root37.buflexz.core.network.ApiBuilder
import com.root37.buflexz.core.network.model.dto.Banner
import com.root37.buflexz.core.network.model.dto.Partner
import com.root37.buflexz.core.network.model.dto.WalletRes
import com.root37.buflexz.core.network.model.response.ListResultResponse
import com.root37.buflexz.core.network.model.response.NewResultResponse
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.FragmentMainMyBinding
import com.root37.buflexz.databinding.PopupGuideBinding
import retrofit2.Call
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * A simple [Fragment] subclass.
 * Use the [MainMyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainMyFragment : BaseFragment<MainActivity>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) //        arguments?.let {
        //            param1 = it.getString(ARG_PARAM1)
        //            param2 = it.getString(ARG_PARAM2)
        //        }
    }

    private var _binding: FragmentMainMyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentMainMyBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun getPID(): String? {
        return ""
    }

    lateinit var mBannerAdapter: HomeBannerAdapter

    var mPartner: Partner? = null

    override fun init() {

        binding.layoutMainMyNotExistBuffWallet.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), defaultLauncher)) {
                return@setOnClickListener
            }

            val intent = Intent(requireActivity(), WalletMakeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)

        }

        binding.textMainPartnerCommissionConfig.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), defaultLauncher)) {
                return@setOnClickListener
            }

            val intent = Intent(requireActivity(), PartnerCommissionConfigActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        binding.textMainPartnerCalculateConfig.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), defaultLauncher)) {
                return@setOnClickListener
            }

            val intent = Intent(requireActivity(), ProfitPartnerConfigActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        binding.textMainMyBuffTitle.setOnClickListener {
            val popupBinding = PopupGuideBinding.inflate(layoutInflater)
            val popup = PopupWindow(popupBinding.root, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT) //팝업 뷰 터치 되도록
            popup.isTouchable = true //팝업 뷰 포커스도 주고
            popup.isFocusable = true //팝업 뷰 이외에도 터치되게 (터치시 팝업 닫기 위한 코드)
            popup.isOutsideTouchable = true

            popupBinding.textPopupGuideTitle.setText(R.string.word_buff_en)
            popupBinding.textPopupGuideDesc.setText(R.string.msg_buff_coin_desc)

            popupBinding.imagePopupGuideClose.setOnClickListener {
                popup.dismiss()
            }
            popup.contentView = popupBinding.root
            popup.showAsDropDown(it)
        }

        binding.textMainMyPointTitle.setOnClickListener {
            val popupBinding = PopupGuideBinding.inflate(layoutInflater)
            val popup = PopupWindow(popupBinding.root, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT) //팝업 뷰 터치 되도록
            popup.isTouchable = true //팝업 뷰 포커스도 주고
            popup.isFocusable = true //팝업 뷰 이외에도 터치되게 (터치시 팝업 닫기 위한 코드)
            popup.isOutsideTouchable = true

            popupBinding.textPopupGuideTitle.setText(R.string.word_point)
            popupBinding.textPopupGuideDesc.setText(R.string.msg_point_desc)

            popupBinding.imagePopupGuideClose.setOnClickListener {
                popup.dismiss()
            }
            popup.contentView = popupBinding.root
            popup.showAsDropDown(it)
        }

        binding.textMainMyPoint.setOnClickListener {
            val intent = Intent(requireActivity(), PointConfigActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        binding.layoutMainMyGiftShop.setOnClickListener {
            val intent = Intent(requireActivity(), GiftCardBrandActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        binding.layoutMainMyGoodsShop.setOnClickListener {
            val intent = Intent(requireActivity(), ProductCategoryActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        binding.textMainMyLuckyballTitle.setOnClickListener {
            val popupBinding = PopupGuideBinding.inflate(layoutInflater)
            val popup = PopupWindow(popupBinding.root, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT) //팝업 뷰 터치 되도록
            popup.isTouchable = true //팝업 뷰 포커스도 주고
            popup.isFocusable = true //팝업 뷰 이외에도 터치되게 (터치시 팝업 닫기 위한 코드)
            popup.isOutsideTouchable = true

            popupBinding.textPopupGuideTitle.setText(R.string.word_luckyball)
            popupBinding.textPopupGuideDesc.setText(R.string.msg_luckyball_desc)

            popupBinding.imagePopupGuideClose.setOnClickListener {
                popup.dismiss()
            }
            popup.contentView = popupBinding.root
            popup.showAsDropDown(it)
        }

        binding.textMainMyLuckyball.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), defaultLauncher)) {
                return@setOnClickListener
            }

            val intent = Intent(requireActivity(), BallConfigActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        binding.layoutMainMyLuckyballExchange.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), defaultLauncher)) {
                return@setOnClickListener
            }

            val intent = Intent(requireActivity(), LuckyBallExchangeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        mBannerAdapter = HomeBannerAdapter()
        binding.pagerMainMyBanner.adapter = mBannerAdapter

        binding.textMainMyBannerTotal.setOnClickListener {
            val intent = Intent(requireActivity(), BannerListActivity::class.java)
            intent.putExtra(Const.PARTNER, mPartner)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        mBannerAdapter.listener = object : HomeBannerAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {

                val item = mBannerAdapter.getItem(position)
                when (item.moveType) {
                    "inner" -> {
                        when (item.innerType) {
                            "partner" -> {

                                if (!PplusCommonUtil.loginCheck(requireActivity(), defaultLauncher)) {
                                    return
                                }

                                if (mPartner == null || mPartner!!.status != "active") {
                                    val intent = Intent(requireActivity(), PartnerReqActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    defaultLauncher.launch(intent)
                                } else {
                                    showAlert(R.string.msg_already_active_partner)
                                }
                            }

                            "telegram" -> {
                                if (!PplusCommonUtil.loginCheck(requireActivity(), defaultLauncher)) {
                                    return
                                }

                                val intent = Intent(requireActivity(), CommunityApplyActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                defaultLauncher.launch(intent)
                            }

                            "main" -> {
                                getParentActivity().setLotteryFragment()
                            }

                            "notice" -> {
                                val intent = Intent(requireActivity(), NoticeActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                defaultLauncher.launch(intent)
                            }

                            "faq" -> {
                                val intent = Intent(requireActivity(), FaqActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                defaultLauncher.launch(intent)
                            }
                        }

                    }

                    "outer" -> {
                        PplusCommonUtil.openChromeWebView(requireActivity(), item.outerUrl!!)
                    }
                }
            }
        }

        val handler = Handler(Looper.myLooper()!!)
        binding.pagerMainMyBanner.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                handler.removeMessages(0)
                val runnable = Runnable {
                    if (!isAdded) {
                        return@Runnable
                    }

                    val size = (binding.pagerMainMyBanner.adapter?.itemCount ?: 0)
                    if (binding.pagerMainMyBanner.currentItem < size - 1) {
                        binding.pagerMainMyBanner.setCurrentItem(binding.pagerMainMyBanner.currentItem + 1, true)
                    } else {
                        binding.pagerMainMyBanner.setCurrentItem(0, true)
                    }
                }

                handler.postDelayed(runnable, 2500)
            }
        })

        binding.layoutMainMyLotteryJoinHistory.setOnClickListener {

            if (!PplusCommonUtil.loginCheck(requireActivity(), defaultLauncher)) {
                return@setOnClickListener
            }

            val intent = Intent(requireActivity(), MyLotteryJoinListActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        binding.layoutMainMyLotteryWinHistory.setOnClickListener {
            val intent = Intent(requireActivity(), LotteryResultActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }
        binding.layoutMainMyLotteryUseMethod.setOnClickListener {
            val intent = Intent(requireActivity(), LotteryGuideActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        binding.layoutMainMyInvite.setOnClickListener {

            if (!PplusCommonUtil.loginCheck(requireActivity(), defaultLauncher)) {
                return@setOnClickListener
            }

            val intent = Intent(requireActivity(), InviteActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        binding.layoutMainMyInquiry.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), defaultLauncher)) {
                return@setOnClickListener
            }

            val intent = Intent(requireActivity(), InquireListActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        binding.layoutMainMyFaq.setOnClickListener {
            val intent = Intent(requireActivity(), FaqActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        binding.layoutMainMySetting.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), defaultLauncher)) {
                return@setOnClickListener
            }

            val intent = Intent(requireActivity(), SettingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        //        binding.layoutMainMyCommunityApply.setOnClickListener {
        //            if (!PplusCommonUtil.loginCheck(requireActivity(), defaultLauncher)) {
        //                return@setOnClickListener
        //            }
        //
        //            val intent = Intent(requireActivity(), CommunityApplyActivity::class.java)
        //            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        //            defaultLauncher.launch(intent)
        //        }

        binding.layoutMainMyLuckyDrawJoin.setOnClickListener {
            getParentActivity().setLuckyDrawFragment()
        }

        binding.layoutMainMyLuckyDrawJoinHistory.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), defaultLauncher)) {
                return@setOnClickListener
            }

            val intent = Intent(requireActivity(), MyLuckyDrawHistoryActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        binding.layoutMainMyLuckyDrawJoinHistory2.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), defaultLauncher)) {
                return@setOnClickListener
            }

            val intent = Intent(requireActivity(), MyLuckyDrawHistoryActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        binding.layoutMainMyGiftShopHistory.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), defaultLauncher)) {
                return@setOnClickListener
            }

            val intent = Intent(requireActivity(), GiftCardPurchaseHistoryActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        binding.layoutMainMyGoodsShopHistory.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), defaultLauncher)) {
                return@setOnClickListener
            }

            val intent = Intent(requireActivity(), ProductPurchaseHistoryActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        binding.layoutMainMyNotice.setOnClickListener {
            val intent = Intent(requireActivity(), NoticeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        loginCheck()
        getBannerList()
    }

    private fun loginCheck() {
        if (LoginInfoManager.getInstance().isMember()) {
            binding.layoutMainMyMember.visibility = View.VISIBLE
            binding.textMainMyLogin.visibility = View.GONE
            binding.imageMainMyNotificationBox.visibility = View.VISIBLE
            binding.layoutMainMyBuff.visibility = View.VISIBLE

            binding.layoutMainMyMember.setOnClickListener {
                val intent = Intent(requireActivity(), ProfileConfigActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                profileConfigLauncher.launch(intent)
            }

            binding.imageMainMyNotificationBox.setOnClickListener {
                val intent = Intent(requireActivity(), NotificationBoxActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                defaultLauncher.launch(intent)
            }

            reloadSession()
            getPartner()
        } else {
            binding.layoutMainMyMember.visibility = View.GONE
            binding.textMainMyLogin.visibility = View.VISIBLE
            binding.imageMainMyNotificationBox.visibility = View.GONE
            binding.layoutMainMyBuff.visibility = View.GONE

            binding.textMainMyLogin.setOnClickListener {
                val intent = Intent(requireActivity(), LoginActivity::class.java)
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
                        binding.layoutMainMyNotExistBuffWallet.visibility = View.VISIBLE
                        binding.layoutMainMyExistBuffWallet.visibility = View.GONE
                        if(Const.API_URL.startsWith("https://api")){
                            walletSignUp()
                        }

                    } else {
                        binding.layoutMainMyNotExistBuffWallet.visibility = View.GONE
                        binding.layoutMainMyExistBuffWallet.visibility = View.VISIBLE
                        getBuffCoinBalance()
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<WalletRes>>?, t: Throwable?, response: NewResultResponse<WalletRes>?) {
            }
        }).build().call()
    }

    private fun walletSignUp() {
        showProgress("")
        ApiBuilder.create().walletSignUp().setCallback(object : PplusCallback<NewResultResponse<WalletRes>> {
            override fun onResponse(call: Call<NewResultResponse<WalletRes>>?,
                                    response: NewResultResponse<WalletRes>?) {
                if (!isAdded) {
                    return
                }
                if (response?.result != null && response.result!!.result == "SUCCESS") {
                    binding.layoutMainMyNotExistBuffWallet.visibility = View.GONE
                    binding.layoutMainMyExistBuffWallet.visibility = View.VISIBLE
                    getBuffCoinBalance()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<WalletRes>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<WalletRes>?) {
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
                    val krw = response.result!!["krw"]
                    val totalKrw = response.result!!["totalKrw"]
                    binding.textMainMyBuff.text = FormatUtil.getCoinType(buffCoin.toString())

                    binding.layoutMainMyExistBuffWallet.setOnClickListener {
                        val intent = Intent(requireActivity(), WalletActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        defaultLauncher.launch(intent)
                    }

//                    binding.textMainPointCoinKrw.text = getString(R.string.msg_buff_coin_krw, FormatUtil.getMoneyType(totalKrw.toString()), FormatUtil.getMoneyType(krw.toString()))


                }
            }

            override fun onFailure(call: Call<NewResultResponse<Map<String, Any>>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<Map<String, Any>>?) {
            }
        }).build().call()
    }

    private fun getPartner() {
        ApiBuilder.create().getPartner().setCallback(object : PplusCallback<NewResultResponse<Partner>> {
            override fun onResponse(call: Call<NewResultResponse<Partner>>?, response: NewResultResponse<Partner>?) {

                if (!isAdded) {
                    return
                }

                if (response?.result != null) {
                    mPartner = response.result
                    if (mPartner!!.status == "active") {
                        binding.layoutMainMyPartner.visibility = View.VISIBLE
                        getTotalProfit()
                    } else {
                        binding.layoutMainMyPartner.visibility = View.GONE
                    }

                } else {
                    binding.layoutMainMyPartner.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Partner>>?, t: Throwable?, response: NewResultResponse<Partner>?) {
            }
        }).build().call()
    }

    private fun getTotalProfit() {
        ApiBuilder.create().getTotalProfit().setCallback(object : PplusCallback<NewResultResponse<Double>> {
            override fun onResponse(call: Call<NewResultResponse<Double>>?, response: NewResultResponse<Double>?) {

                if (!isAdded) {
                    return
                }

                if (response?.result != null) {
                    binding.textMainMyPartnerTotalCommission.text = FormatUtil.getMoneyTypeFloat(response.result.toString())
                } else {
                    binding.textMainMyPartnerTotalCommission.text = "0"
                }

                getCommissionThisMonth()
            }

            override fun onFailure(call: Call<NewResultResponse<Double>>?, t: Throwable?, response: NewResultResponse<Double>?) {
            }
        }).build().call()
    }

    private fun getCommissionThisMonth() {

        val output = SimpleDateFormat("yyyy-MM-dd")

        val cal = Calendar.getInstance()
        val end = "${output.format(cal.time)} 23:59:59"

        cal.set(Calendar.DAY_OF_MONTH, 1)
        val start = "${output.format(cal.time)} 00:00:00"
        LogUtil.e(LOG_TAG, start + " " + end)
        val params = HashMap<String, String>()
        params["startDate"] = DateFormatUtils.PPLUS_DATE_FORMAT.format(PplusCommonUtil.setServerTimeZone(DateFormatUtils.PPLUS_DATE_FORMAT.parse(start)))
        params["endDate"] = DateFormatUtils.PPLUS_DATE_FORMAT.format(PplusCommonUtil.setServerTimeZone(DateFormatUtils.PPLUS_DATE_FORMAT.parse(end)))
        ApiBuilder.create().getCommission(params).setCallback(object : PplusCallback<NewResultResponse<Double>> {
            override fun onResponse(call: Call<NewResultResponse<Double>>?, response: NewResultResponse<Double>?) {
                if (!isAdded) {
                    return
                }
                if (response?.result != null) {
                    binding.textMainMyPartnerThisMonthCommission.text = FormatUtil.getMoneyTypeFloat(response.result.toString())
                } else {
                    binding.textMainMyPartnerThisMonthCommission.text = "0"
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Double>>?, t: Throwable?, response: NewResultResponse<Double>?) {
            }
        }).build().call()
    }

    private fun getBannerList() {
        val params = HashMap<String, String>()
        params["aos"] = "1"
        params["type"] = "home"
        params["nation"] = Locale.getDefault().country
        ApiBuilder.create().getBannerList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<Banner>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<Banner>>>?, response: NewResultResponse<ListResultResponse<Banner>>?) {
                if (!isAdded) {
                    return
                }

                if (response?.result != null && response.result!!.list != null) {
                    val bannerList = response.result!!.list!!
                    if (bannerList.isEmpty()) {
                        binding.textMainMyBannerTotal.visibility = View.GONE
                        binding.layoutMainMyBanner.visibility = View.GONE
                    } else {
                        binding.layoutMainMyBanner.visibility = View.VISIBLE
                        binding.textMainMyBannerTotal.visibility = View.VISIBLE
                        binding.textMainMyBannerPage.text = "1/${bannerList.size}"
                        binding.pagerMainMyBanner.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                            override fun onPageSelected(position: Int) {
                                binding.textMainMyBannerPage.text = "${position + 1}/${bannerList.size}"
                            }
                        })
                        mBannerAdapter.setDataList(bannerList as MutableList<Banner>)
                        binding.textMainMyBannerPage.text = "${binding.pagerMainMyBanner.currentItem + 1}/${bannerList.size}"
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<Banner>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<Banner>>?) {

            }
        }).build().call()
    }

    private fun reloadSession() {
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {
                if (!isAdded) {
                    return
                }

                Glide.with(this@MainMyFragment).load(LoginInfoManager.getInstance().member!!.profile).apply(RequestOptions().centerCrop().placeholder(R.drawable.ic_profile_default).error(R.drawable.ic_profile_default)).into(binding.imageMainMyProfile)
                binding.textMainMyNickname.text = LoginInfoManager.getInstance().member!!.nickname
                Glide.with(requireActivity()).load(Uri.parse("file:///android_asset/flags/${LoginInfoManager.getInstance().member!!.nation!!.uppercase()}.png")).into(binding.imageMainMyFlag)

                val nation = NationManager.getInstance().nationMap!![LoginInfoManager.getInstance().member!!.nation]
                if (nation!!.code == "KR") {
                    binding.textMainMyNation.text = nation.name
                } else {
                    binding.textMainMyNation.text = nation.nameEn
                }

                binding.textMainMyPoint.text = getString(R.string.format_point_unit, FormatUtil.getMoneyType(LoginInfoManager.getInstance().member!!.point!!.toInt().toString()))
                binding.textMainMyLuckyball.text = getString(R.string.format_ball_unit, FormatUtil.getMoneyType(LoginInfoManager.getInstance().member!!.ball!!.toInt().toString()))

                if (StringUtils.isNotEmpty(LoginInfoManager.getInstance().member!!.authEmail)) {
                    duplicateUser(LoginInfoManager.getInstance().member!!.authEmail!!)
                } else {
                    binding.layoutMainMyNotExistBuffWallet.visibility = View.VISIBLE
                    binding.layoutMainMyExistBuffWallet.visibility = View.GONE
                }
            }
        })
    }

    val profileConfigLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        reloadSession()
    }


    val defaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        loginCheck()
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainMyFragment().apply {
            arguments = Bundle().apply { //                putString(ARG_PARAM1, param1)
                //                putString(ARG_PARAM2, param2)
            }
        }
    }
}