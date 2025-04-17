package com.lejel.wowbox.apps.main.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.ump.ConsentForm
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import com.google.firebase.analytics.FirebaseAnalytics
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.pref.PreferenceUtil
import com.pplus.utils.part.utils.time.DateFormatUtils
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.apps.common.ui.RandomNumberSetActivity
import com.lejel.wowbox.apps.common.ui.base.BaseFragment
import com.lejel.wowbox.apps.lottery.ui.AlertLottoErrorActivity
import com.lejel.wowbox.apps.lottery.ui.LotteryResultActivity
import com.lejel.wowbox.apps.lottery.ui.LottoJoinResultActivity
import com.lejel.wowbox.apps.lottery.ui.MyLotteryJoinListActivity
import com.lejel.wowbox.apps.main.data.MainLotteryProfileImageAdapter
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.Lottery
import com.lejel.wowbox.core.network.model.dto.LotteryJoin
import com.lejel.wowbox.core.network.model.dto.MemberProfileImage
import com.lejel.wowbox.core.network.model.response.ListResultResponse
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.AdmobUtil
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.FragmentMainLotteryBinding
import retrofit2.Call
import java.text.SimpleDateFormat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainLotteryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainLotteryFragment : BaseFragment<MainActivity>() { //    private var param1: String? = null
    //    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) //        arguments?.let {
        //            param1 = it.getString(ARG_PARAM1)
        //            param2 = it.getString(ARG_PARAM2)
        //        }
    }

    private var _binding: FragmentMainLotteryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentMainLotteryBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun getPID(): String? {
        return ""
    }

    private var mLottery: Lottery? = null
    private var mLottoCount = 0
    private var mProfileImageAdapter: MainLotteryProfileImageAdapter? = null

    override fun init() {

        binding.textMainLottoJoin.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), defaultLauncher)) {
                return@setOnClickListener
            }

            if (mLottery == null) {
                return@setOnClickListener
            }

//            val consentInformation = UserMessagingPlatform.getConsentInformation(requireActivity())
//            if (!consentInformation.canRequestAds()) { // Create a ConsentRequestParameters object.
//                val params = ConsentRequestParameters.Builder().build()
//
//                consentInformation.requestConsentInfoUpdate(requireActivity(), params, {
//                    UserMessagingPlatform.loadAndShowConsentFormIfRequired(requireActivity(), ConsentForm.OnConsentFormDismissedListener { loadAndShowError -> // Consent gathering failed.
//                        LogUtil.e(LOG_TAG, String.format("%s: %s", loadAndShowError?.errorCode, loadAndShowError?.message))
//
//                        // Consent has been gathered.
//                        if (consentInformation.canRequestAds()) {
//                            AdmobUtil.getInstance(requireActivity()).initAdMob()
//                        }
//
//                    })
//                }, { requestConsentError -> // Consent gathering failed.
//                    LogUtil.e(LOG_TAG, String.format("%s: %s", requestConsentError.errorCode, requestConsentError.message))
//                })
//                return@setOnClickListener
//            }

//            if (!AdmobUtil.getInstance(requireActivity()).mIsLoaded) {
//                showAlert(R.string.msg_loading_ad)
//                return@setOnClickListener
//            }

            val lastJoinTime = PreferenceUtil.getDefaultPreference(requireActivity()).get(Const.LOTTO_JOIN_TIME, 0L)
            val currentMillis = System.currentTimeMillis()
            val terms = (currentMillis - lastJoinTime) / 1000
            if (terms > 30) {
                mLottoCount = PreferenceUtil.getDefaultPreference(requireActivity()).get(Const.LOTTO_COUNT, 0)
                if (mLottoCount >= 10) {
                    val intent = Intent(requireActivity(), RandomNumberSetActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    randomNumberLauncher.launch(intent)
                } else {
                    join(true)
//                    showAd(true)
                }
            } else {
                val remainSeconds = 30 - terms
                val intent = Intent(requireActivity(), AlertLottoErrorActivity::class.java)
                intent.putExtra(Const.KEY, "time")
                intent.putExtra(Const.REMAIN_SECOND, remainSeconds.toInt())
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                defaultLauncher.launch(intent)
            }
        }

        binding.layoutMainLotteryJoinHistory.setOnClickListener {

            if (!PplusCommonUtil.loginCheck(requireActivity(), defaultLauncher)) {
                return@setOnClickListener
            }

            val intent = Intent(requireActivity(), MyLotteryJoinListActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

//        binding.layoutMainLotteryJoinHistory2.setOnClickListener {
//
//            if (!PplusCommonUtil.loginCheck(requireActivity(), defaultLauncher)) {
//                return@setOnClickListener
//            }
//
//            val intent = Intent(requireActivity(), MyLotteryJoinListActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            defaultLauncher.launch(intent)
//        }

        binding.textMainLotteryWinnerCount.setOnClickListener {
            val intent = Intent(requireActivity(), LotteryResultActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        binding.layoutMainLotteryWinHistory.setOnClickListener {
            val intent = Intent(requireActivity(), LotteryResultActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }


        binding.recyclerMainLotteryRandomProfile.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        mProfileImageAdapter = MainLotteryProfileImageAdapter()
        binding.recyclerMainLotteryRandomProfile.adapter = mProfileImageAdapter

        mProfileImageAdapter!!.listener = object : MainLotteryProfileImageAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(requireActivity(), LotteryResultActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                defaultLauncher.launch(intent)
            }
        }

        binding.textLotteryGuideUrl.setOnClickListener {
            PplusCommonUtil.openChromeWebView(requireActivity(), "https://m.dhlottery.co.kr/gameResult.do?method=byWin")
        }

        binding.textLotteryGuideQna1Title.text = PplusCommonUtil.fromHtml(getString(R.string.html_lottery_qna1_title))
        binding.textLotteryGuideQna1Contents.text = PplusCommonUtil.fromHtml(getString(R.string.html_lottery_qna1_desc))
        binding.textLotteryGuideQna2Title.text = PplusCommonUtil.fromHtml(getString(R.string.html_lottery_qna2_title))
        binding.textLotteryGuideQna2Contents.text = PplusCommonUtil.fromHtml(getString(R.string.html_lottery_qna2_desc))
        binding.textLotteryGuideQna3Title.text = PplusCommonUtil.fromHtml(getString(R.string.html_lottery_qna3_title))
        binding.textLotteryGuideQna3Contents.text = PplusCommonUtil.fromHtml(getString(R.string.html_lottery_qna3_desc))

        loginCheck()
        getLottery()
    }

    private fun loginCheck() {
        if (LoginInfoManager.getInstance().isMember()) {
//            binding.layoutMainLotteryMember.visibility = View.VISIBLE
//            binding.textMainLotteryLogin.visibility = View.GONE
//            Glide.with(this).load(LoginInfoManager.getInstance().member!!.profile).apply(RequestOptions().centerCrop().placeholder(R.drawable.ic_profile_default).error(R.drawable.ic_profile_default)).into(binding.imageMainLotteryProfile)
//            binding.textMainLotteryNickname.text = LoginInfoManager.getInstance().member!!.nickname

        } else {
//            binding.layoutMainLotteryMember.visibility = View.GONE
//            binding.textMainLotteryLogin.visibility = View.VISIBLE
//            binding.textMainLotteryLogin.setOnClickListener {
//                val intent = Intent(requireActivity(), LoginActivity::class.java)
//                defaultLauncher.launch(intent)
//            }
        }
    }

//    private fun showAd(addCount: Boolean){
//        if (AdmobUtil.getInstance(requireActivity()).mAdmobInterstitialAd != null) {
//            AdmobUtil.getInstance(requireActivity()).mAdmobInterstitialAd!!.fullScreenContentCallback = object : FullScreenContentCallback(){
//                override fun onAdClicked() {
//                    LogUtil.e(LOG_TAG, "onAdShowedFullScreenContent")
//                }
//
//                override fun onAdDismissedFullScreenContent() {
//                    LogUtil.e(LOG_TAG, "onAdDismissedFullScreenContent")
//                    join(addCount)
//                }
//
//                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
//                    LogUtil.e(LOG_TAG, "onAdFailedToShowFullScreenContent")
//                    join(addCount)
//                }
//
//                override fun onAdImpression() {
//                    LogUtil.e(LOG_TAG, "onAdImpression")
//                    setEvent(requireActivity(), FirebaseAnalytics.Event.AD_IMPRESSION)
//                }
//
//                override fun onAdShowedFullScreenContent() {
//                    LogUtil.e(LOG_TAG, "onAdShowedFullScreenContent")
//                }
//            }
//            AdmobUtil.getInstance(requireActivity()).mAdmobInterstitialAd!!.show(requireActivity())
//            AdmobUtil.getInstance(requireActivity()).mIsLoaded = false
//            AdmobUtil.getInstance(requireActivity()).initAdMob()
//        }else{
//            AdmobUtil.getInstance(requireActivity()).mIsLoaded = false
//            AdmobUtil.getInstance(requireActivity()).initAdMob()
//            join(addCount)
//        }
//    }

    private fun join(addCount: Boolean) {

        val params = HashMap<String, String>()
        params["joinType"] = "advertise"
        showProgress("")
        ApiBuilder.create().lotteryJoin(params).setCallback(object : PplusCallback<NewResultResponse<LotteryJoin>> {
            override fun onResponse(call: Call<NewResultResponse<LotteryJoin>>?, response: NewResultResponse<LotteryJoin>?) {
                hideProgress()

                setEvent(requireActivity(), "lottery_join")
                if (!isAdded) {
                    return
                }
                if(response?.result != null){
                    if (addCount) {
                        PreferenceUtil.getDefaultPreference(requireActivity()).put(Const.LOTTO_COUNT, mLottoCount + 1)
                    }

                    PreferenceUtil.getDefaultPreference(requireActivity()).put(Const.LOTTO_JOIN_TIME, System.currentTimeMillis())

                    val intent = Intent(requireActivity(), LottoJoinResultActivity::class.java)
                    intent.putExtra(Const.DATA, response.result)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    defaultLauncher.launch(intent)
                }


            }

            override fun onFailure(call: Call<NewResultResponse<LotteryJoin>>?, t: Throwable?, response: NewResultResponse<LotteryJoin>?) {
                hideProgress()
                if (!isAdded) {
                    return
                }
            }
        }).build().call()
    }

    private fun getLottery() {
        ApiBuilder.create().getLottery().setCallback(object : PplusCallback<NewResultResponse<Lottery>> {
            override fun onResponse(call: Call<NewResultResponse<Lottery>>?, response: NewResultResponse<Lottery>?) {
                if (!isAdded) {
                    return
                }
                if (response?.result != null) {
                    mLottery = response.result

                    binding.textMainLotteryRound.text = getString(R.string.format_lottery_round, mLottery!!.lotteryRound.toString())
                    binding.textMainLotteryAnnounceDate.text = "${getString(R.string.msg_lotto_announce_date)} ${SimpleDateFormat(getString(R.string.msg_lotto_announce_date_format)).format(PplusCommonUtil.setTimeZoneOffset(DateFormatUtils.PPLUS_DATE_FORMAT.parse(mLottery!!.announceDatetime)))}"
                    Glide.with(requireActivity()).load(mLottery!!.bannerImage).apply(RequestOptions().centerCrop()).into(binding.imageMainLotteryBanner)

                    if (mLottery!!.lotteryRound!! > 1102) {
                        binding.layoutMainBeforeLotteryRound.visibility = View.VISIBLE
                        binding.textMainBeforeLotteryRound.text = getString(R.string.format_lottery_round, (mLottery!!.lotteryRound!! - 1).toString())
                    } else {
                        binding.layoutMainBeforeLotteryRound.visibility = View.GONE
                    }


                    val endMillis = PplusCommonUtil.setTimeZoneOffset(DateFormatUtils.PPLUS_DATE_FORMAT.parse(mLottery!!.eventEndDatetime)).time

                    if (endMillis > 0) {
                        binding.layoutMainLotteryRemainTime.visibility = View.VISIBLE
                        val remainMillis = endMillis - System.currentTimeMillis()

                        if (remainMillis > 0) {
                            val countTimer = object : CountDownTimer(remainMillis, 1000) {

                                override fun onTick(millisUntilFinished: Long) {

                                    if (!isAdded) {
                                        return
                                    }

                                    val days = (millisUntilFinished / (1000 * 60 * 60) / 24).toInt()
                                    val hours = (millisUntilFinished / (1000 * 60 * 60) % 24).toInt()
                                    val minutes = (millisUntilFinished / (1000 * 60) % 60).toInt()
                                    val seconds = (millisUntilFinished / 1000).toInt() % 60

                                    val strH = DateFormatUtils.formatTime(hours)
                                    val strM = DateFormatUtils.formatTime(minutes)
                                    val strS = DateFormatUtils.formatTime(seconds)

                                    binding.textMainLotteryRemainDays.text = DateFormatUtils.formatTime(days)
                                    binding.textMainLotteryRemainHours.text = strH
                                    binding.textMainLotteryRemainMins.text = strM
                                    binding.textMainLotteryRemainSeconds.text = strS
                                }

                                override fun onFinish() {
                                    if (!isAdded) {
                                        return
                                    }
                                    getLottery()
                                }
                            }
                            countTimer.start()
                        } else {
                            binding.layoutMainLotteryRemainTime.visibility = View.GONE
                        }
                    } else {
                        binding.layoutMainLotteryRemainTime.visibility = View.GONE
                    }

                    if (LoginInfoManager.getInstance().isMember()) {
                        binding.layoutMainLotteryJoinHistory.visibility = View.VISIBLE
                        getJoinCount()
                    } else {
                        binding.layoutMainLotteryJoinHistory.visibility = View.GONE
                    }

                    if(response.result != null){
                        val lottery = response.result!!

                        if(lottery.firstType == "point"){
                            binding.textLotteryGuide1stPrice.text = getString(R.string.format_point_unit, FormatUtil.getMoneyType(lottery.firstMoney.toString()))
                        }else if(lottery.firstType == "cash"){
                            binding.textLotteryGuide1stPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(lottery.firstMoney.toString()))
                        }else{
                            binding.textLotteryGuide1stPrice.text = getString(R.string.format_lottery_unit, FormatUtil.getMoneyType(lottery.firstMoney.toString()))
                        }

                        if(lottery.secondType == "point"){
                            binding.textLotteryGuide2ndPrice.text = getString(R.string.format_point_unit, FormatUtil.getMoneyType(lottery.secondMoney.toString()))
                        }else if(lottery.secondType == "cash"){
                            binding.textLotteryGuide2ndPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(lottery.secondMoney.toString()))
                        }else{
                            binding.textLotteryGuide2ndPrice.text = getString(R.string.format_lottery_unit, FormatUtil.getMoneyType(lottery.secondMoney.toString()))
                        }

                        if(lottery.thirdType == "point"){
                            binding.textLotteryGuide3rdPrice.text = getString(R.string.format_point_unit, FormatUtil.getMoneyType(lottery.thirdMoney.toString()))
                        }else if(lottery.thirdType == "cash"){
                            binding.textLotteryGuide3rdPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(lottery.thirdMoney.toString()))
                        }else{
                            binding.textLotteryGuide3rdPrice.text = getString(R.string.format_lottery_unit, FormatUtil.getMoneyType(lottery.thirdMoney.toString()))
                        }

                        if(lottery.forthType == "point"){
                            binding.textLotteryGuide4thPrice.text = getString(R.string.format_point_unit, FormatUtil.getMoneyType(lottery.forthMoney.toString()))
                        }else if(lottery.forthType == "cash"){
                            binding.textLotteryGuide1stPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(lottery.forthMoney.toString()))
                        }else{
                            binding.textLotteryGuide4thPrice.text = getString(R.string.format_lottery_unit, FormatUtil.getMoneyType(lottery.forthMoney.toString()))
                        }

                        if(lottery.fifthType == "point"){
                            binding.textLotteryGuide5thPrice.text = getString(R.string.format_point_unit, FormatUtil.getMoneyType(lottery.fifthMoney.toString()))
                        }else if(lottery.fifthType == "cash"){
                            binding.textLotteryGuide1stPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(lottery.fifthMoney.toString()))
                        }else{
                            binding.textLotteryGuide5thPrice.text = getString(R.string.format_lottery_unit, FormatUtil.getMoneyType(lottery.fifthMoney.toString()))
                        }
                    }
                }

                if (mLottery!!.lotteryRound!! > 1102) {
                    getWinCount()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Lottery>>?, t: Throwable?, response: NewResultResponse<Lottery>?) {
            }
        }).build().call()
    }

    private fun getJoinCount(){
        val params = HashMap<String, String>()
        params["lotteryRound"] = mLottery!!.lotteryRound.toString()
        ApiBuilder.create().getLotteryJoinCount(params).setCallback(object : PplusCallback<NewResultResponse<Int>>{
            override fun onResponse(call: Call<NewResultResponse<Int>>?, response: NewResultResponse<Int>?) {
                if (!isAdded) {
                    return
                }
                if (response?.result != null) {
                    binding.textMainLotteryJoinCount.text = FormatUtil.getMoneyType(response.result.toString())
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Int>>?, t: Throwable?, response: NewResultResponse<Int>?) {

            }
        }).build().call()
    }

    private fun getWinCount() {
        ApiBuilder.create().getLotteryWinCount(mLottery!!.lotteryRound!! - 1).setCallback(object : PplusCallback<NewResultResponse<Int>> {
            override fun onResponse(call: Call<NewResultResponse<Int>>?, response: NewResultResponse<Int>?) {
                if (!isAdded) {
                    return
                }
                if (response?.result != null) {
                    binding.textMainLotteryWinnerCount.text = FormatUtil.getMoneyType(response.result.toString())
                }
                getProfileImage()
            }

            override fun onFailure(call: Call<NewResultResponse<Int>>?, t: Throwable?, response: NewResultResponse<Int>?) {
            }
        }).build().call()
    }

    private fun getProfileImage() {
        ApiBuilder.create().getMemberProfileImageList().setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<MemberProfileImage>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<MemberProfileImage>>>?, response: NewResultResponse<ListResultResponse<MemberProfileImage>>?) {
                if (!isAdded) {
                    return
                }
                mProfileImageAdapter!!.clear()
                if (response?.result != null && response.result!!.list != null) {
                    var list = (response.result!!.list!! as MutableList)
                    list.shuffle()
                    if (list.size > 4) {
                        list = list.subList(0, 4)
                    }
                    mProfileImageAdapter!!.addAll(list)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<MemberProfileImage>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<MemberProfileImage>>?) {

            }
        }).build().call()
    }

    val defaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        loginCheck()
        getLottery()
    }

    val randomNumberLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            PreferenceUtil.getDefaultPreference(requireActivity()).put(Const.LOTTO_COUNT, 0)
            join(false)
//            showAd(false)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainLotteryFragment().apply {
            arguments = Bundle().apply { //                putString(ARG_PARAM1, param1)
                //                putString(ARG_PARAM2, param2)
            }
        }
    }
}