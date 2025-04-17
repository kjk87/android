package com.lejel.wowbox.apps.main.ui

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.firebase.analytics.FirebaseAnalytics
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.attendance.data.AttendanceAdapter
import com.lejel.wowbox.apps.attendance.ui.AlertAttendanceCompleteActivity
import com.lejel.wowbox.apps.common.builder.AlertBuilder
import com.lejel.wowbox.apps.common.builder.OnAlertResultListener
import com.lejel.wowbox.apps.common.builder.data.AlertData
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.apps.common.ui.base.BaseFragment
import com.lejel.wowbox.apps.event.ui.EventResultActivity
import com.lejel.wowbox.apps.invite.ui.InviteActivity
import com.lejel.wowbox.apps.login.LoginActivity2
import com.lejel.wowbox.apps.luckybox.data.LuckyBoxLoadingView
import com.lejel.wowbox.apps.wallet.ui.WalletMakeActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.Event
import com.lejel.wowbox.core.network.model.dto.EventJoinParam
import com.lejel.wowbox.core.network.model.dto.EventJoinResult
import com.lejel.wowbox.core.network.model.response.ListResultResponse
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.AdmobUtil
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.FragmentMainRewardBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import com.pplus.utils.part.utils.time.DateFormatUtils
import retrofit2.Call
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainRewardFragment : BaseFragment<MainActivity>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { //            param1 = it.getString(ARG_PARAM1)
            //            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var _binding: FragmentMainRewardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentMainRewardBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun getPID(): String? {
        return ""
    }

    private lateinit var mAdapter: AttendanceAdapter

    override fun init() {

        val layoutManager = GridLayoutManager(requireActivity(), 7)
//        binding.recyclerMainRewardAttendance.addItemDecoration(SpacesItemDecoration(resources.getDimensionPixelSize(R.dimen.width_15)))
        binding.recyclerMainRewardAttendance.layoutManager = layoutManager
        mAdapter = AttendanceAdapter()
        binding.recyclerMainRewardAttendance.adapter = mAdapter

        binding.imageMainRewardInvite.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), defaultLauncher)) {
                return@setOnClickListener
            }

            val intent = Intent(requireActivity(), InviteActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        binding.imageMainRewardPointMall.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), defaultLauncher)) {
                return@setOnClickListener
            }
            PplusCommonUtil.wowMallJoin()

            val url = "https://wowboxmall.com/shop/step_wowbox.php"
            PplusCommonUtil.openChromeWebView(requireActivity(), url)
        }

        binding.textMainRewardAttendance.visibility = View.GONE
        binding.textMainRewardAttendanceDay.visibility = View.GONE
        binding.textMainRewardAttendanceDayDesc.visibility = View.GONE

        binding.textMainRewardAttendance.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), defaultLauncher)) {
                return@setOnClickListener
            }

            showProgress("")
            ApiBuilder.create().attendance().setCallback(object : PplusCallback<NewResultResponse<Float>>{
                override fun onResponse(call: Call<NewResultResponse<Float>>?, response: NewResultResponse<Float>?) {
                    setEvent(requireActivity(), "attendance")
                    hideProgress()
                    if(response?.result != null){
                        if(LoginInfoManager.getInstance().member!!.attendanceCount == null){
                            LoginInfoManager.getInstance().member!!.attendanceCount = 0
                        }
                        val amount = response.result!!
                        val intent = Intent(requireActivity(), AlertAttendanceCompleteActivity::class.java)
                        intent.putExtra(Const.AMOUNT, amount.toInt().toString())
                        intent.putExtra(Const.TYPE, "point")
                        intent.putExtra(Const.DAY, LoginInfoManager.getInstance().member!!.attendanceCount!!+1)
                        attendanceCompleteLauncher.launch(intent)

                        reloadSession()
                    }

                }

                override fun onFailure(call: Call<NewResultResponse<Float>>?, t: Throwable?, response: NewResultResponse<Float>?) {
                    hideProgress()
                    showAlert(R.string.msg_not_eanble_attendance_time)

                }
            }).build().call()
        }

        loginCheck()
        getImmediatelyEvent()
    }

    private fun loginCheck() {
        if (LoginInfoManager.getInstance().isMember()) {
            binding.textMainRewardLogin.visibility = View.GONE
            reloadSession()
        } else {
            binding.textMainRewardLogin.visibility = View.VISIBLE
            binding.textMainRewardLogin.setOnClickListener {
                val intent = Intent(requireActivity(), LoginActivity2::class.java)
                defaultLauncher.launch(intent)
            }
            binding.textMainRewardAttendance.visibility = View.VISIBLE
            binding.textMainRewardAttendanceDay.visibility = View.GONE
            binding.textMainRewardAttendanceDayDesc.visibility = View.GONE
        }
    }

    private fun reloadSession(){
//        showProgress("")
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {
                if(!isAdded){
                    return
                }
//                hideProgress()
                mAdapter.notifyDataSetChanged()
                if(StringUtils.isNotEmpty(LoginInfoManager.getInstance().member!!.attendanceDate)){
                    val lastDate = PplusCommonUtil.setTimeZoneOffset(DateFormatUtils.PPLUS_DATE_FORMAT.parse(LoginInfoManager.getInstance().member!!.attendanceDate))
                    val calendar = Calendar.getInstance()
                    calendar.add(Calendar.DAY_OF_MONTH, -1)
                    if(calendar.time > lastDate){
                        binding.textMainRewardAttendance.visibility = View.VISIBLE
                        binding.textMainRewardAttendanceDay.visibility = View.GONE
                        binding.textMainRewardAttendanceDayDesc.visibility = View.GONE
                    }else{
                        binding.textMainRewardAttendance.visibility = View.GONE
                        binding.textMainRewardAttendanceDay.visibility = View.VISIBLE
                        binding.textMainRewardAttendanceDayDesc.visibility = View.VISIBLE
                        binding.textMainRewardAttendanceDay.text = getString(R.string.format_days, LoginInfoManager.getInstance().member!!.attendanceCount)
                    }
                }else{
                    binding.textMainRewardAttendance.visibility = View.VISIBLE
                    binding.textMainRewardAttendanceDay.visibility = View.GONE
                    binding.textMainRewardAttendanceDayDesc.visibility = View.GONE
                }
            }
        })
    }

    private fun showAdmob(){
        if (AdmobUtil.getInstance(requireActivity()).mAdmobInterstitialAd != null) {
            AdmobUtil.getInstance(requireActivity()).mAdmobInterstitialAd!!.fullScreenContentCallback = object : FullScreenContentCallback(){
                override fun onAdClicked() {
                    LogUtil.e(LOG_TAG, "onAdShowedFullScreenContent")
                }

                override fun onAdDismissedFullScreenContent() {
                    LogUtil.e(LOG_TAG, "onAdDismissedFullScreenContent")
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    LogUtil.e(LOG_TAG, "onAdFailedToShowFullScreenContent")
                }

                override fun onAdImpression() {
                    LogUtil.e(LOG_TAG, "onAdImpression")
                    setEvent(requireActivity(), FirebaseAnalytics.Event.AD_IMPRESSION)
                }

                override fun onAdShowedFullScreenContent() {
                    LogUtil.e(LOG_TAG, "onAdShowedFullScreenContent")
                }
            }
            AdmobUtil.getInstance(requireActivity()).mAdmobInterstitialAd!!.show(requireActivity())
            AdmobUtil.getInstance(requireActivity()).mIsLoaded = false
            AdmobUtil.getInstance(requireActivity()).initAdMob()
        }else{
            AdmobUtil.getInstance(requireActivity()).mIsLoaded = false
            AdmobUtil.getInstance(requireActivity()).initAdMob()
        }
    }

    val defaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        loginCheck()
        getImmediatelyEvent()
    }

    val attendanceCompleteLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        showAdmob()
    }

    var countTimer: CountDownTimer? = null

    private fun getImmediatelyEvent() {
        val params = HashMap<String, String>()
        params["primaryType"] = "join"
        params["winAnnounceType"] = "immediately"
        params["paging[page]"] = "1"
        params["paging[limit]"] = "1"
        ApiBuilder.create().getEventList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<Event>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<Event>>>?, response: NewResultResponse<ListResultResponse<Event>>?) {
                if (!isAdded) {
                    return
                }

                if (response?.result != null) {
                    if (response.result!!.list != null && response.result!!.list!!.isNotEmpty()) {
                        binding.layoutMainRewardEvent.layoutEvent.visibility = View.VISIBLE
                        val event = response.result!!.list!![0]
                        setEvent(event)
                    }else{
                        binding.layoutMainRewardEvent.layoutEvent.visibility = View.GONE
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<Event>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<Event>>?) {
            }

        }).build().call()
    }

    private fun setEvent(event:Event){

        (binding.layoutMainRewardEvent.layoutEvent.layoutParams as RelativeLayout.LayoutParams).height = resources.getDimensionPixelSize(R.dimen.height_525)

        val currentMillis = System.currentTimeMillis()
        val endMillis = DateFormatUtils.PPLUS_DATE_FORMAT.parse(event.endDatetime).time

        var isWinAnnounce = false

        var isClose = false
        var isOpen = false

        if (currentMillis > endMillis) {
            binding.layoutMainRewardEvent.layoutEventClose.visibility = View.GONE

            isClose = true
        } else {
            binding.layoutMainRewardEvent.layoutEventClose.visibility = View.GONE
            isClose = false
        }

        Glide.with(requireActivity()).load(event.bannerImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(binding.layoutMainRewardEvent.imageEvent)

        countTimer?.cancel()

        if (event.eventTime!!.isEmpty() || isClose || isWinAnnounce) {
            isOpen = event.eventTime!!.isEmpty()
        } else {
            binding.layoutMainRewardEvent.textEventTime.visibility = View.VISIBLE
            isOpen = true
            //                holder.text_time.visibility = View.GONE
            //                holder.text_proceeding.visibility = View.VISIBLE
            binding.layoutMainRewardEvent.textEventProceeding.visibility = View.GONE

            val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            val currentTime = sdf.format(Date(currentMillis)).split(":")
            val currentSecond = (currentTime[0].toInt() * 60 * 60) + (currentTime[1].toInt() * 60) + currentTime[2].toInt()
            var endDisplayMillis = 0L
            for (time in event.eventTime!!) {
                val startSecond = (time.startTime!!.substring(0, 2).toInt() * 60 * 60) + (time.startTime!!.substring(2).toInt() * 60)
                val endSecond = (time.endTime!!.substring(0, 2).toInt() * 60 * 60) + (time.endTime!!.substring(2).toInt() * 60)

                if (currentSecond in startSecond..endSecond) {
                    endDisplayMillis = endSecond * 1000L
                    break
                }
            }

            if (endDisplayMillis > 0) {
                val remainMillis = endDisplayMillis - (currentSecond * 1000)

                if (remainMillis > 0) {
                    countTimer = object : CountDownTimer(remainMillis, 1000) {

                        override fun onTick(millisUntilFinished: Long) {

                            val hours = (millisUntilFinished / (1000 * 60 * 60) % 24).toInt()
                            val minutes = (millisUntilFinished / (1000 * 60) % 60).toInt()
                            val seconds = (millisUntilFinished / 1000).toInt() % 60

                            val strH = DateFormatUtils.formatTime(hours)
                            val strM = DateFormatUtils.formatTime(minutes)
                            val strS = DateFormatUtils.formatTime(seconds)

                            if (hours > 0) {
                                binding.layoutMainRewardEvent.textEventTime.text = PplusCommonUtil.fromHtml(getString(R.string.html_event_time, "$strH:$strM:$strS"))
                                binding.layoutMainRewardEvent.textEventTime.text = "$strH:$strM:$strS"
                            } else {
                                if (minutes > 0) {
                                    binding.layoutMainRewardEvent.textEventTime.text = PplusCommonUtil.fromHtml(getString(R.string.html_event_time, "$strM:$strS"))
                                    binding.layoutMainRewardEvent.textEventTime.text = "$strM:$strS"
                                } else {
                                    binding.layoutMainRewardEvent.textEventTime.text = PplusCommonUtil.fromHtml(getString(R.string.html_event_time, strS))
                                    binding.layoutMainRewardEvent.textEventTime.text = strS
                                }
                            }
                        }

                        override fun onFinish() {

                            try {
                                setEvent(event)
                            } catch (e: Exception) {

                            }
                        }
                    }
                    countTimer?.start()
                } else {
                    binding.layoutMainRewardEvent.textEventTime.visibility = View.GONE
                    isOpen = false
                    binding.layoutMainRewardEvent.textEventProceeding.visibility = View.GONE
                }
            } else {
                binding.layoutMainRewardEvent.textEventTime.visibility = View.GONE
                isOpen = false
                binding.layoutMainRewardEvent.textEventProceeding.visibility = View.GONE
            }

        }

        binding.layoutMainRewardEvent.root.setOnClickListener {
            if (isClose) {
                PplusCommonUtil.showEventAlert(requireActivity(), 0, event, defaultLauncher)

            } else {

                if (!PplusCommonUtil.loginCheck(requireActivity(), defaultLauncher)) {
                    return@setOnClickListener
                }

                joinEvent(event)
            }
        }
    }

    fun joinEvent(event: Event) {
        val params = EventJoinParam()
        params.event = event
        showProgress(getString(R.string.msg_event_join_progress))

        ApiBuilder.create().joinEvent(params).setCallback(object : PplusCallback<NewResultResponse<EventJoinResult>> {
            override fun onResponse(call: Call<NewResultResponse<EventJoinResult>>?,
                                    response: NewResultResponse<EventJoinResult>?) {

                hideProgress()
                if (!isAdded) {
                    return
                }
                setEvent(requireActivity(), "eventJoin")

                if (response?.result != null) {
                    val result = response.result!!

                    if (event.winAnnounceType.equals("immediately")) {
                        val mLoading = LuckyBoxLoadingView()
                        mLoading.isCancelable = false
                        //                        mLoading.setText(getString(R.string.msg_checking_event_result))
                        var delayTime = 2000L
                        try {
                            mLoading.show(parentFragmentManager, "")
                        } catch (e: Exception) {
                            LogUtil.e(LOG_TAG, e.toString())
                        }

                        val handler = Handler(Looper.myLooper()!!)
                        handler.postDelayed({

                            try {
                                mLoading.dismiss()
                            } catch (e: Exception) {

                            }

                            val intent = Intent(requireActivity(), EventResultActivity::class.java)
                            intent.putExtra(Const.EVENT_JOIN, result.join)
                            intent.putExtra(Const.EVENT, event)
                            if(result.win != null){
                                intent.putExtra(Const.EVENT_WIN, result.win)
                            }
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            defaultLauncher.launch(intent)
                        }, delayTime)

                    }
                }

            }

            override fun onFailure(call: Call<NewResultResponse<EventJoinResult>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<EventJoinResult>?) {

                hideProgress()
                if (!isAdded) {
                    return
                }

                if (response != null) {
                    PplusCommonUtil.showEventAlert(activity!!, response.code, event, defaultLauncher)
                }

            }
        }).build().call()
    }

//    private fun setData(){
//        binding.textMainRewardTicket1.text = "3"
//        binding.textMainRewardTicket2.text = "5"
//        binding.textMainRewardTicket3.text = "10"
//
//        binding.textMainRewardRemainTime.visibility = View.GONE
//        binding.textMainRewardAdPlay.isClickable = false
//        binding.textMainRewardAdPlay.setTextColor(ResourceUtil.getColor(requireActivity(), R.color.color_cdcdcd))
//
//        if (LoginInfoManager.getInstance().isMember()) {
//            val member = LoginInfoManager.getInstance().member!!
//
//            if (member.rewardTicket == null && member.rewardPoint == null && member.rewardPoint2 == null && StringUtils.isEmpty(member.rewardDate)) {
//                binding.imageMainRewardComplete1.visibility = View.GONE
//                binding.textMainRewardStep1.visibility = View.VISIBLE
//                binding.textMainRewardAdPlay.isClickable = true
//                binding.textMainRewardAdPlay.setTextColor(ResourceUtil.getColor(requireActivity(), R.color.color_ea5506))
//                binding.textMainRewardAdPlay.setOnClickListener {
//                    callAd(1)
//                }
//
//                binding.imageMainRewardComplete2.visibility = View.GONE
//                binding.textMainRewardStep2.visibility = View.VISIBLE
//
//                binding.imageMainRewardComplete3.visibility = View.GONE
//                binding.textMainRewardStep3.visibility = View.VISIBLE
//            } else {
//
//                val joinAbleTermMillis: Long
//                val termsMillis: Long
//
//                if (member.rewardTicket != null && member.rewardPoint != null && member.rewardPoint2 != null && StringUtils.isNotEmpty(member.rewardDate)) {
//                    val lastJoinTime = PplusCommonUtil.setTimeZoneOffset(DateFormatUtils.PPLUS_DATE_FORMAT.parse(member.rewardDate)).time
//                    joinAbleTermMillis = 3 * 60 * 60 * 1000
//                    termsMillis = System.currentTimeMillis() - lastJoinTime
//
//                    if (termsMillis > joinAbleTermMillis) {
//
//                        binding.imageMainRewardComplete1.visibility = View.GONE
//                        binding.textMainRewardStep1.visibility = View.VISIBLE
//                        binding.textMainRewardAdPlay.isClickable = true
//                        binding.textMainRewardAdPlay.setText(R.string.msg_ad_view_reward)
//                        binding.textMainRewardAdPlay.setTextColor(ResourceUtil.getColor(requireActivity(), R.color.color_ea5506))
//                        binding.textMainRewardAdPlay.setOnClickListener {
//                            val termsMillis = System.currentTimeMillis() - lastJoinTime
//                            if (termsMillis > joinAbleTermMillis) {
//                                callAd(1)
//                            }
//                        }
//
//                        binding.imageMainRewardComplete2.visibility = View.GONE
//                        binding.textMainRewardStep2.visibility = View.VISIBLE
//
//                        binding.imageMainRewardComplete3.visibility = View.GONE
//                        binding.textMainRewardStep3.visibility = View.VISIBLE
//                    } else {
//                        binding.textMainRewardAdPlay.isClickable = false
//                        binding.textMainRewardAdPlay.setText(R.string.msg_enable_after_reset)
//                        binding.textMainRewardAdPlay.setTextColor(ResourceUtil.getColor(requireActivity(), R.color.color_cdcdcd))
//
//                        binding.imageMainRewardComplete1.visibility = View.VISIBLE
//                        binding.textMainRewardStep1.visibility = View.GONE
//
//                        binding.imageMainRewardComplete2.visibility = View.VISIBLE
//                        binding.textMainRewardStep2.visibility = View.GONE
//
//                        binding.imageMainRewardComplete3.visibility = View.VISIBLE
//                        binding.textMainRewardStep3.visibility = View.GONE
//                    }
//                } else {
//
//                    binding.imageMainRewardComplete1.visibility = View.VISIBLE
//                    binding.textMainRewardStep1.visibility = View.GONE
//
//                    val lastJoinTime = PreferenceUtil.getDefaultPreference(requireActivity()).get(Const.AD_MINING_TIME, 0L)
//                    joinAbleTermMillis = 10 * 60 * 1000
//                    termsMillis = System.currentTimeMillis() - lastJoinTime
//
//                    if (member.rewardTicket != null && member.rewardPoint == null) {
//
//                        binding.imageMainRewardComplete2.visibility = View.GONE
//                        binding.textMainRewardStep2.visibility = View.VISIBLE
//
//                        if (termsMillis > joinAbleTermMillis) {
//                            binding.textMainRewardAdPlay.isClickable = true
//                            binding.textMainRewardAdPlay.setText(R.string.msg_ad_view_reward)
//                            binding.textMainRewardAdPlay.setTextColor(ResourceUtil.getColor(requireActivity(), R.color.color_ea5506))
//                            binding.textMainRewardAdPlay.setOnClickListener {
//                                val termsMillis = System.currentTimeMillis() - lastJoinTime
//                                if (termsMillis > joinAbleTermMillis) {
//                                    callAd(2)
//                                }
//                            }
//                        }
//
//                        binding.imageMainRewardComplete3.visibility = View.GONE
//                        binding.textMainRewardStep3.visibility = View.VISIBLE
//
//                    } else if (member.rewardTicket != null && member.rewardPoint != null && member.rewardPoint2 == null) {
//
//                        binding.imageMainRewardComplete2.visibility = View.VISIBLE
//                        binding.textMainRewardStep2.visibility = View.GONE
//
//                        binding.imageMainRewardComplete3.visibility = View.GONE
//                        binding.textMainRewardStep3.visibility = View.VISIBLE
//
//                        if (termsMillis > joinAbleTermMillis) {
//                            binding.textMainRewardAdPlay.isClickable = true
//                            binding.textMainRewardAdPlay.setText(R.string.msg_ad_view_reward)
//                            binding.textMainRewardAdPlay.setTextColor(ResourceUtil.getColor(requireActivity(), R.color.color_ea5506))
//                            binding.textMainRewardAdPlay.setOnClickListener {
//                                val termsMillis = System.currentTimeMillis() - lastJoinTime
//                                if (termsMillis > joinAbleTermMillis) {
//                                    callAd(3)
//                                }
//                            }
//                        }
//                    }else{
//
//                    }
//                }
//
//                if (joinAbleTermMillis > termsMillis) {
//                    binding.textMainRewardRemainTime.visibility = View.VISIBLE
//                    val countTimer = object : CountDownTimer(joinAbleTermMillis - termsMillis, 1000) {
//
//                        override fun onTick(millisUntilFinished: Long) {
//
//                            val hours = (millisUntilFinished / (1000 * 60 * 60) % 24).toInt()
//                            val minutes = (millisUntilFinished / (1000 * 60) % 60).toInt()
//                            val seconds = (millisUntilFinished / 1000).toInt() % 60
//
//                            val strH = DateFormatUtils.formatTime(hours)
//                            val strM = DateFormatUtils.formatTime(minutes)
//                            val strS = DateFormatUtils.formatTime(seconds)
//
//                            if (hours > 0) {
//                                binding.textMainRewardRemainTime.text = "$strH:$strM:$strS"
//                            } else {
//                                if (minutes > 0) {
//                                    binding.textMainRewardRemainTime.text = "$strM:$strS"
//                                } else {
//                                    binding.textMainRewardRemainTime.text = strS
//                                }
//                            }
//                        }
//
//                        override fun onFinish() {
//                            try {
//                                setData()
//
//                            } catch (e: Exception) {
//
//                            }
//                        }
//                    }
//                    countTimer.start()
//                }
//            }
//        } else {
//            binding.imageMainRewardComplete1.visibility = View.GONE
//            binding.textMainRewardStep1.visibility = View.VISIBLE
//            binding.textMainRewardAdPlay.isClickable = true
//            binding.textMainRewardAdPlay.setText(R.string.msg_ad_view_reward)
//            binding.textMainRewardAdPlay.setTextColor(ResourceUtil.getColor(requireActivity(), R.color.color_ea5506))
//            binding.textMainRewardAdPlay.setOnClickListener {
//                PplusCommonUtil.loginCheck(requireActivity(), defaultLauncher)
//            }
//
//            binding.imageMainRewardComplete2.visibility = View.GONE
//            binding.textMainRewardStep2.visibility = View.VISIBLE
//
//            binding.imageMainRewardComplete3.visibility = View.GONE
//            binding.textMainRewardStep3.visibility = View.VISIBLE
//        }
//    }

//    var mIsRewarded = false
//    var mRewardedAmount = 0F
//    private fun callAd(depth: Int) {
//
//        if (!AdmobUtil.getInstance(requireActivity()).mIsRewardedLoaded) {
//            ToastUtil.showAlert(requireActivity(), R.string.msg_loading_ad)
//            return
//        }
//
//        AdmobUtil.getInstance(requireActivity()).mRewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
//            override fun onAdImpression() {
//                setEvent(requireActivity(), FirebaseAnalytics.Event.AD_IMPRESSION)
//            }
//
//            override fun onAdDismissedFullScreenContent() {
//                super.onAdDismissedFullScreenContent()
//                if (mIsRewarded) {
//                    PreferenceUtil.getDefaultPreference(requireActivity()).put(Const.AD_MINING_TIME, System.currentTimeMillis())
//                    mIsRewarded = false
//                    val intent = Intent(requireActivity(), AlertAdMiningCompleteActivity::class.java)
//                    intent.putExtra(Const.AMOUNT, mRewardedAmount.toInt().toString())
//                    var type = ""
//                    when (depth) {
//                        1 -> {
//                            type = "lottery"
//                        }
//                        2 -> {
//                            type = "lottery"
//                        }
//                        3 -> {
//                            type = "lottery"
//                        }
//                    }
//                    intent.putExtra(Const.TYPE, type)
//                    intent.putExtra(Const.DEPTH, depth)
//                    startActivity(intent)
//                    PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
//
//                        override fun reload() {
//                            setData()
//                        }
//                    })
//                }
//            }
//        }
//
//        AdmobUtil.getInstance(requireActivity()).mRewardedAd?.let { ad ->
//            ad.show(requireActivity(), OnUserEarnedRewardListener { rewardItem -> // Handle the reward.
//                val rewardAmount = rewardItem.amount
//                val rewardType = rewardItem.type
//
//                AdmobUtil.getInstance(requireActivity()).mIsRewardedLoaded = false
//                AdmobUtil.getInstance(requireActivity()).initRewardAd()
//
//                val params = HashMap<String, String>()
//                params["depth"] = depth.toString()
//                showProgress("")
//                ApiBuilder.create().reward(params).setCallback(object : PplusCallback<NewResultResponse<Float>> {
//                    override fun onResponse(call: Call<NewResultResponse<Float>>?, response: NewResultResponse<Float>?) {
//                        hideProgress()
//                        if (response?.result != null) {
//                            setEvent(requireActivity(), "reward")
//                            mIsRewarded = true
//                            mRewardedAmount = response.result!!
//
//                        } else {
//                            mIsRewarded = false
//                        }
//                    }
//
//                    override fun onFailure(call: Call<NewResultResponse<Float>>?, t: Throwable?, response: NewResultResponse<Float>?) {
//                        hideProgress()
//                        mIsRewarded = false
//                        if (response?.code == 666) {
//
//                        }
//                    }
//                }).build().call()
//
//            })
//        } ?: run {
//            ToastUtil.showAlert(requireActivity(), R.string.msg_loading_ad)
//            AdmobUtil.getInstance(requireActivity()).mIsRewardedLoaded = false
//            AdmobUtil.getInstance(requireActivity()).initRewardAd()
//        }
//    }

    private fun alertWalletMake() {
        val builder = AlertBuilder.Builder()
        builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
        builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_make_wallet), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
        builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.msg_make_wallet))
        builder.setOnAlertResultListener(object : OnAlertResultListener {

            override fun onCancel() {

            }

            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                when (event_alert) {
                    AlertBuilder.EVENT_ALERT.RIGHT -> {
                        val intent = Intent(context, WalletMakeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        defaultLauncher.launch(intent)
                    }

                    else -> {

                    }
                }
            }
        })
        builder.builder().show(requireActivity())
    }

    companion object {

        @JvmStatic
        fun newInstance() = MainRewardFragment().apply {
            arguments = Bundle().apply { //                putString(ARG_PARAM1, param1)
                //                putString(ARG_PARAM2, param2)
            }
        }
    }
}