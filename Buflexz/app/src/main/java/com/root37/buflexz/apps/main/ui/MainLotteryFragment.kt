package com.root37.buflexz.apps.main.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
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
import com.google.android.ump.ConsentForm
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.pref.PreferenceUtil
import com.pplus.utils.part.utils.time.DateFormatUtils
import com.root37.buflexz.Const
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.mgmt.LoginInfoManager
import com.root37.buflexz.apps.common.mgmt.NationManager
import com.root37.buflexz.apps.common.ui.RandomNumberSetActivity
import com.root37.buflexz.apps.common.ui.base.BaseFragment
import com.root37.buflexz.apps.invite.ui.InviteActivity
import com.root37.buflexz.apps.login.LoginActivity
import com.root37.buflexz.apps.lottery.ui.AlertLottoErrorActivity
import com.root37.buflexz.apps.lottery.ui.LotteryGuideActivity
import com.root37.buflexz.apps.lottery.ui.LotteryResultActivity
import com.root37.buflexz.apps.lottery.ui.LottoJoinResultActivity
import com.root37.buflexz.apps.lottery.ui.MyLotteryJoinListActivity
import com.root37.buflexz.apps.main.data.MainLotteryProfileImageAdapter
import com.root37.buflexz.core.network.ApiBuilder
import com.root37.buflexz.core.network.model.dto.Lottery
import com.root37.buflexz.core.network.model.dto.MemberProfileImage
import com.root37.buflexz.core.network.model.response.ListResultResponse
import com.root37.buflexz.core.network.model.response.NewResultResponse
import com.root37.buflexz.core.util.AdmobUtil
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.FragmentMainLotteryBinding
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

            val consentInformation = UserMessagingPlatform.getConsentInformation(requireActivity())
            if (!consentInformation.canRequestAds()) { // Create a ConsentRequestParameters object.
                val params = ConsentRequestParameters.Builder().build()

                consentInformation.requestConsentInfoUpdate(requireActivity(), params, ConsentInformation.OnConsentInfoUpdateSuccessListener {
                    UserMessagingPlatform.loadAndShowConsentFormIfRequired(requireActivity(), ConsentForm.OnConsentFormDismissedListener { loadAndShowError -> // Consent gathering failed.
                        LogUtil.e(LOG_TAG, String.format("%s: %s", loadAndShowError?.errorCode, loadAndShowError?.message))

                        // Consent has been gathered.
                        if (consentInformation.canRequestAds()) {
                            AdmobUtil.getInstance(requireActivity()).initAdMob()
                        }

                    })
                }, ConsentInformation.OnConsentInfoUpdateFailureListener { requestConsentError -> // Consent gathering failed.
                    LogUtil.e(LOG_TAG, String.format("%s: %s", requestConsentError.errorCode, requestConsentError.message))
                })
                return@setOnClickListener
            }

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

        binding.layoutMainLotteryGuide.setOnClickListener {
            val intent = Intent(requireActivity(), LotteryGuideActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        binding.layoutMainLotteryUseMethod.setOnClickListener {
            val intent = Intent(requireActivity(), LotteryGuideActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        binding.layoutMainLotteryInvite.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), defaultLauncher)) {
                return@setOnClickListener
            }

            val intent = Intent(requireActivity(), InviteActivity::class.java)
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
        loginCheck()
        getLottery()
    }

    private fun loginCheck() {
        if (LoginInfoManager.getInstance().isMember()) {
            binding.layoutMainLotteryMember.visibility = View.VISIBLE
            binding.textMainLotteryLogin.visibility = View.GONE
            Glide.with(this).load(LoginInfoManager.getInstance().member!!.profile).apply(RequestOptions().centerCrop().placeholder(R.drawable.ic_profile_default).error(R.drawable.ic_profile_default)).into(binding.imageMainLotteryProfile)
            binding.textMainLotteryNickname.text = LoginInfoManager.getInstance().member!!.nickname
            Glide.with(requireActivity()).load(Uri.parse("file:///android_asset/flags/${LoginInfoManager.getInstance().member!!.nation!!.uppercase()}.png")).into(binding.imageMainLotteryFlag)

            val nation = NationManager.getInstance().nationMap!![LoginInfoManager.getInstance().member!!.nation]
            if (nation!!.code == "KR") {
                binding.textMainLotteryNation.text = nation.name
            } else {
                binding.textMainLotteryNation.text = nation.nameEn
            } //            reloadSession()
        } else {
            binding.layoutMainLotteryMember.visibility = View.GONE
            binding.textMainLotteryLogin.visibility = View.VISIBLE
            binding.textMainLotteryLogin.setOnClickListener {
                val intent = Intent(requireActivity(), LoginActivity::class.java)
                defaultLauncher.launch(intent)
            }
        }
    }

    private fun join(addCount: Boolean) {
        val params = HashMap<String, String>()
        params["joinType"] = "advertise"
        showProgress("")
        ApiBuilder.create().lotteryJoin(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()
                if (!isAdded) {
                    return
                }

                if (addCount) {
                    PreferenceUtil.getDefaultPreference(requireActivity()).put(Const.LOTTO_COUNT, mLottoCount + 1)
                }

                PreferenceUtil.getDefaultPreference(requireActivity()).put(Const.LOTTO_JOIN_TIME, System.currentTimeMillis())

                val intent = Intent(requireActivity(), LottoJoinResultActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                defaultLauncher.launch(intent)
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
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