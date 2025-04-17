package com.pplus.luckybol.apps.main.ui


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.igaworks.adpopcorn.IgawAdpopcorn
import com.igaworks.adpopcorn.interfaces.IAdPOPcornEventListener
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.bol.ui.BolConfigActivity
import com.pplus.luckybol.apps.bol.ui.CashExchangeActivity
import com.pplus.luckybol.apps.card.ui.CardConfigActivity
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.ui.base.BaseFragment
import com.pplus.luckybol.apps.event.ui.LottoHistoryActivity
import com.pplus.luckybol.apps.mobilegift.ui.MobileGiftActivity
import com.pplus.luckybol.apps.my.ui.MyWinHistoryActivity
import com.pplus.luckybol.apps.point.ui.PointHistoryActivity
import com.pplus.luckybol.apps.product.ui.MyReviewActivity
import com.pplus.luckybol.apps.product.ui.ProductLikeActivity
import com.pplus.luckybol.apps.product.ui.PurchaseHistoryActivity
import com.pplus.luckybol.apps.recommend.ui.InviteActivity
import com.pplus.luckybol.apps.setting.ui.AlarmContainerActivity
import com.pplus.luckybol.apps.setting.ui.AppSettingActivity
import com.pplus.luckybol.apps.setting.ui.ProfileConfigActivity
import com.pplus.luckybol.apps.signin.ui.SnsLoginActivity
import com.pplus.luckybol.apps.wallet.ui.WalletActivity
import com.pplus.luckybol.core.Crypt
import com.pplus.luckybol.core.WalletSecureUtil
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.Coin
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.FragmentMyInfoBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [MyInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyInfoFragment : BaseFragment<AppMainActivity>() {

    // TODO: Rename and change types of parameters
    //    private var mParam1: String? = null
    //    private var mParam2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (arguments != null) {
            //            mParam1 = arguments!!.getString(ARG_PARAM1)
        }
    }

    private var _binding: FragmentMyInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentMyInfoBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        //        IgawCommon.startSession(activity)
    }

    override fun onPause() {
        super.onPause()
        //        IgawCommon.endSession()
    }

    override fun init() {

        binding.textMyInfoEventWinHistory.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
                return@setOnClickListener
            }
            val intent = Intent(activity, MyWinHistoryActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        binding.textMyInfoRetentionBol.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
                return@setOnClickListener
            }
            val intent = Intent(activity, BolConfigActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.layoutMyInfoBolPlay.setOnClickListener {
            getParentActivity().setPlayFragment()
        }

        binding.layoutMyInfoLottoWinHistory.setOnClickListener {
            val intent = Intent(activity, LottoHistoryActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        binding.textMyInfoRetentionPoint.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
                return@setOnClickListener
            }
            val intent = Intent(activity, PointHistoryActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.layoutMyInfoGiftishow.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
                return@setOnClickListener
            }
            val intent = Intent(activity, MobileGiftActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        binding.layoutMyInfoCashExchange.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
                return@setOnClickListener
            }
            val intent = Intent(activity, CashExchangeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        binding.textMyInfoPurchaseCount.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
                return@setOnClickListener
            }

            val intent = Intent(activity, PurchaseHistoryActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            signInLauncher.launch(intent)
        }

        binding.layoutMyInfoProductLike.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
                return@setOnClickListener
            }

            val intent = Intent(activity, ProductLikeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.layoutMyInfoPurchaseReview.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
                return@setOnClickListener
            }
            val intent = Intent(activity, MyReviewActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            signInLauncher.launch(intent)
        }

        binding.layoutMyInfoCardConfig.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
                return@setOnClickListener
            }
            val intent = Intent(activity, CardConfigActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.layoutMyInfoSponsorInquiry.setOnClickListener {
            PplusCommonUtil.openChromeWebView(requireActivity(), "https://docs.google.com/forms/d/e/1FAIpQLSeMwaRqu-fPqg6oAeqSJp1bWkUxl6lLEwWE-_gDfdNZ6NKaHg/viewform")
        }

        binding.layoutMyInfoInvite.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
                return@setOnClickListener
            }
            val intent = Intent(activity, InviteActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.layoutMyInfoAlarm.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
                return@setOnClickListener
            }
            val intent = Intent(activity, AlarmContainerActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        binding.layoutMyInfoSetting.setOnClickListener {
            val intent = Intent(activity, AppSettingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            signInLauncher.launch(intent)
        }

        IgawAdpopcorn.setEventListener(activity, object : IAdPOPcornEventListener {

            override fun OnAgreePrivacy() {

            }

            override fun OnClosedOfferWallPage() {
                if (LoginInfoManager.getInstance().isMember) {
                    setPoint()
                }
            }
        })

        binding.textMyInfoNickname.setOnClickListener {
            if (!LoginInfoManager.getInstance().isMember) {
                val intent = Intent(activity, SnsLoginActivity::class.java)
                signInLauncher.launch(intent)
            }
        }

        binding.imageMyInfo.setOnClickListener {
            if (!LoginInfoManager.getInstance().isMember) {
                val intent = Intent(activity, SnsLoginActivity::class.java)
                signInLauncher.launch(intent)
                return@setOnClickListener
            }
            val intent = Intent(activity, ProfileConfigActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            signInLauncher.launch(intent)
        }

        binding.layoutMyInfoPplusInfo.layoutPplusInfoBtn.setOnClickListener {
            if (binding.layoutMyInfoPplusInfo.layoutPplusInfo.visibility == View.GONE) {
                binding.layoutMyInfoPplusInfo.textPplusInfoBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_footer_arrow_up, 0)
                binding.layoutMyInfoPplusInfo.layoutPplusInfo.visibility = View.VISIBLE
            } else if (binding.layoutMyInfoPplusInfo.layoutPplusInfo.visibility == View.VISIBLE) {
                binding.layoutMyInfoPplusInfo.textPplusInfoBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_footer_arrow_down, 0)
                binding.layoutMyInfoPplusInfo.layoutPplusInfo.visibility = View.GONE
            }
        }

        binding.textMyInfoRetentionPoint.setOnClickListener {
            val intent = Intent(activity, PointHistoryActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.layoutMyInfoGiftishow.setOnClickListener {
            val intent = Intent(activity, MobileGiftActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            signInLauncher.launch(intent)
        }

        binding.textMyInfoWallet.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
                return@setOnClickListener
            }

            val intent = Intent(activity, WalletActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.textMyInfoRetentionBol.setSingleLine()
        binding.textMyInfoRetentionCoin.setSingleLine()
        binding.textMyInfoRetentionPoint.setSingleLine()
        binding.textMyInfoPurchaseCount.setSingleLine()

        checkLogin()
    }

    private fun checkLogin() {
        if (LoginInfoManager.getInstance().isMember) {
            binding.scrollMyInfo.visibility = View.VISIBLE
            binding.textMyInfoEventWinHistory.visibility = View.VISIBLE
            binding.textMyInfoNickname.setBackgroundColor(ResourceUtil.getColor(activity, android.R.color.transparent))
            setData()
            setCount()
            checkUser()
        } else {
            binding.scrollMyInfo.visibility = View.GONE
            binding.textMyInfoEventWinHistory.visibility = View.VISIBLE

            binding.textMyInfoNickname.setText(R.string.word_login_join2)
            binding.textMyInfoNickname.setBackgroundResource(R.drawable.underbar_ffffff_transparent)
            binding.imageMyInfo.setImageResource(R.drawable.ic_mypage_profile_default)
        }
    }

    private fun checkUser() {
        ApiBuilder.create().walletDuplicateUser().setCallback(object : PplusCallback<NewResultResponse<String>> {
            override fun onResponse(call: Call<NewResultResponse<String>>?,
                                    response: NewResultResponse<String>?) {
                if(!isAdded){
                    return
                }

                if (response?.data != null && response.data == "SUCCESS") {
                    binding.layoutMyInfoWalletNotExist.visibility = View.VISIBLE
                    binding.layoutMyInfoWalletNotExist.setOnClickListener {
                        walletSignUp()
                    }
                    binding.layoutMyInfoWalletExist.visibility = View.GONE
                } else {
                    binding.layoutMyInfoWalletNotExist.visibility = View.GONE
                    binding.layoutMyInfoWalletExist.visibility = View.VISIBLE
                    walletBalance()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<String>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<String>?) {
            }
        }).build().call()
    }

    private fun walletSignUp() {
        val params = HashMap<String, String>()
        params["password"] = Crypt.encrypt(PplusCommonUtil.decryption(LoginInfoManager.getInstance().user.password!!))
        showProgress("")
        ApiBuilder.create().walletSignUp(params).setCallback(object : PplusCallback<NewResultResponse<String>> {
            override fun onResponse(call: Call<NewResultResponse<String>>?,
                                    response: NewResultResponse<String>?) {
                if(!isAdded){
                    return
                }
                if (response?.data != null && response.data == "SUCCESS") {
                    binding.layoutMyInfoWalletNotExist.visibility = View.GONE
                    binding.layoutMyInfoWalletExist.visibility = View.VISIBLE
                    walletBalance()
                } else {
                    hideProgress()
                    showAlert(R.string.msg_error_wallet)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<String>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<String>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun walletBalance() {
        ApiBuilder.create().walletBalance().setCallback(object : PplusCallback<NewResultResponse<Map<String, Any>>>{
            override fun onResponse(call: Call<NewResultResponse<Map<String, Any>>>?,
                                    response: NewResultResponse<Map<String, Any>>?) {
                hideProgress()
                if(!isAdded){
                    return
                }
                if(response?.data != null){
                    val gson = Gson()
                    val jsonObject = JsonParser.parseString(gson.toJson(response.data)).asJsonObject
                    val buffCoin = jsonObject.get("balances").asJsonObject.get("BUFF").asJsonObject
                    if(buffCoin != null){
                        binding.textMyInfoRetentionCoin.text = getString(R.string.format_count_unit, FormatUtil.getMoneyType(buffCoin.get("balance").asString))
                    }

                }
            }

            override fun onFailure(call: Call<NewResultResponse<Map<String, Any>>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<Map<String, Any>>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun setData() {
        val user = LoginInfoManager.getInstance().user
        if (user.profileImage != null && StringUtils.isNotEmpty(user.profileImage!!.url)) {
            Glide.with(this).load(user.profileImage!!.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.ic_mypage_profile_default).error(R.drawable.ic_mypage_profile_default)).into(binding.imageMyInfo)
        }

        binding.textMyInfoNickname.text = user.nickname
        setPoint()

    }

    private fun setCount() {
        getPurchaseCount()

    }

//    private fun getProductLikeCount() {
//        if (!LoginInfoManager.getInstance().isMember) {
//            binding.textMyInfoGoodsLikeCount.text = PplusCommonUtil.fromHtml(getString(R.string.html_like_count, "0"))
//            return
//        }
//
//        ApiBuilder.create().countProductLike.setCallback(object : PplusCallback<NewResultResponse<Int>> {
//            override fun onResponse(call: Call<NewResultResponse<Int>>?,
//                                    response: NewResultResponse<Int>?) {
//
//                if (!isAdded) {
//                    return
//                }
//
//                if (response?.data != null) {
//                    binding.textMyInfoGoodsLikeCount.text = PplusCommonUtil.fromHtml(getString(R.string.html_like_count, FormatUtil.getMoneyType(response.data.toString())))
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Int>>?,
//                                   t: Throwable?,
//                                   response: NewResultResponse<Int>?) {
//
//            }
//        }).build().call()
//    }

    private fun getPurchaseCount() {

        ApiBuilder.create().countPurchaseProductByMemberSeqNo.setCallback(object : PplusCallback<NewResultResponse<Int>> {
            override fun onResponse(call: Call<NewResultResponse<Int>>?,
                                    response: NewResultResponse<Int>?) {

                if (!isAdded) {
                    return
                }

                if (response?.data != null) {
                    binding.textMyInfoPurchaseCount.text = getString(R.string.format_count_unit2, FormatUtil.getMoneyType(response.data.toString()))
                } else {
                    binding.textMyInfoPurchaseCount.text = getString(R.string.format_count_unit2, FormatUtil.getMoneyType("0"))
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Int>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<Int>?) {
            }
        }).build().call()
    }

//    private fun getReviewCount() {
//
//        ApiBuilder.create().countProductReviewByyMemberSeqNo.setCallback(object : PplusCallback<NewResultResponse<Int>> {
//            override fun onResponse(call: Call<NewResultResponse<Int>>?,
//                                    response: NewResultResponse<Int>?) {
//
//                if (!isAdded) {
//                    return
//                }
//
//                if (response?.data != null) {
//                    binding.textMyInfoReviewCount.text = PplusCommonUtil.fromHtml(getString(R.string.html_purchase_review_count, FormatUtil.getMoneyType(response.data.toString())))
//                } else {
//                    binding.textMyInfoReviewCount.text = PplusCommonUtil.fromHtml(getString(R.string.html_purchase_review_count, "0"))
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Int>>?,
//                                   t: Throwable?,
//                                   response: NewResultResponse<Int>?) {
//            }
//        }).build().call()
//    }

    val defaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        setPoint()
    }

    val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        checkLogin()
    }

    private fun setPoint() {
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
            override fun reload() {
                if (!isAdded) {
                    return
                }

                binding.textMyInfoRetentionBol.text = FormatUtil.getMoneyTypeFloat(LoginInfoManager.getInstance().user.totalBol.toString())
                binding.textMyInfoRetentionPoint.text = FormatUtil.getMoneyTypeFloat(LoginInfoManager.getInstance().user.point.toString())

            }
        })
    }

    override fun getPID(): String {
        return "Main_Mypage"
    }

    companion object {

        fun newInstance(): MyInfoFragment {

            val fragment = MyInfoFragment()
            val args = Bundle()
            //            args.putString(ARG_PARAM1, param1)
            fragment.arguments = args
            return fragment
        }
    }

} // Required empty public constructor
