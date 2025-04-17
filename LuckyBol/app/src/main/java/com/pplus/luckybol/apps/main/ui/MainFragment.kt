package com.pplus.luckybol.apps.main.ui


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.buzzvil.buzzad.benefit.BuzzAdBenefit
import com.buzzvil.buzzad.benefit.presentation.feed.BuzzAdFeed
import com.buzzvil.buzzad.benefit.privacy.PrivacyPolicyEventListener
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.bol.ui.BolConfigActivity
import com.pplus.luckybol.apps.common.mgmt.CountryConfigManager
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.common.ui.base.BaseFragment
import com.pplus.luckybol.apps.event.ui.*
import com.pplus.luckybol.apps.my.ui.BolChargeStationActivity
import com.pplus.luckybol.apps.my.ui.MyWinHistoryActivity
import com.pplus.luckybol.apps.product.ui.ProductShipActivity
import com.pplus.luckybol.apps.recommend.ui.InviteActivity
import com.pplus.luckybol.apps.signin.ui.SnsLoginActivity
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.FragmentMainBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import retrofit2.Call
import java.util.*

class MainFragment : BaseFragment<BaseActivity>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //            mTab = it.getString(Const.TAB)
            //            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun init() {

        binding.layoutMain12Event.setOnClickListener {
            val intent = Intent(activity, Event12Activity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            cashChangeLauncher.launch(intent)
        }

//        binding.layoutMainFree.setOnClickListener {
//            val intent = Intent(activity, PlayActivity::class.java)
//            intent.putExtra(Const.KEY, 4)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            cashChangeLauncher.launch(intent)
//        }

        binding.layoutMainPlay.setOnClickListener {
            val intent = Intent(activity, PlayActivity::class.java)
            intent.putExtra(Const.KEY, 1)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            cashChangeLauncher.launch(intent)
        }

        //        layout_main_random_play.setOnClickListener {
        //            val intent = Intent(activity, RandomPlayActivity::class.java)
        //            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        //            cashChangeLauncher.launch(intent)
        //        }

        binding.layoutMainShopping.setOnClickListener {
            val intent = Intent(activity, ProductShipActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            cashChangeLauncher.launch(intent)
        }

        binding.layoutMainTelegram.setOnClickListener {
            val intent = Intent(activity, SnsEventActivity::class.java)
            intent.putExtra(Const.KEY, Const.TELEGRAM)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            cashChangeLauncher.launch(intent)
        }

        binding.layoutMainTwitter.setOnClickListener {
            val intent = Intent(activity, SnsEventActivity::class.java)
            intent.putExtra(Const.KEY, Const.TWITTER)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            cashChangeLauncher.launch(intent)
        }

        binding.layoutMainImmediatelySave.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
                return@setOnClickListener
            }
            val intent = Intent(activity, BolChargeStationActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            cashChangeLauncher.launch(intent)
        }

        binding.layoutMainLuckyTime.setOnClickListener {
            val intent = Intent(activity, EventActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            cashChangeLauncher.launch(intent)
        }

        //        layout_main_ad_save.setOnClickListener {
        //            if (!PplusCommonUtil.loginCheck(activity!!)) {
        //                return@setOnClickListener
        //            }
        //            val intent = Intent(activity, RewardAdActivity::class.java)
        //            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        //            cashChangeLauncher.launch(intent)
        //        }

        binding.layoutMainAttendanceSave.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
                return@setOnClickListener
            }
            val intent = Intent(activity, AttendanceActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            cashChangeLauncher.launch(intent)
        }

//        binding.layoutMainLottoSave.setOnClickListener {
//            val intent = Intent(activity, LottoActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            cashChangeLauncher.launch(intent)
//        }

        binding.layoutMainNumberEvent.setOnClickListener {
            val intent = Intent(activity, NumberEventActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            cashChangeLauncher.launch(intent)
        }

        binding.layoutMainInvite.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
                return@setOnClickListener
            }
            val intent = Intent(activity, InviteActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.textMainTitle.text = PplusCommonUtil.fromHtml(getString(R.string.html_save_bol_title))


        binding.layoutMainBuzzvil.setOnClickListener {
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

        binding.layoutMainShare.setOnClickListener {
            val intent = Intent(activity, ShareEventActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.layoutMainRadomPlay.setOnClickListener {
            val intent = Intent(activity, RandomPlayActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

//        binding.layoutMainKakaoInquiry.setOnClickListener {
//            //kakaoplus://plusfriend/home/@luckybol
//            try {
//                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("kakaoplus://plusfriend/home/@luckybol"))
//                startActivity(intent)
//            } catch (e: Exception) {
//                PplusCommonUtil.openChromeWebView(requireActivity(), "http://pf.kakao.com/_xcXKrK")
//            }
//
//        }

        //        layout_main_sponsor_inquiry.setOnClickListener {
        //            PplusCommonUtil.openChromeWebView(activity!!, "https://docs.google.com/forms/d/e/1FAIpQLSeMwaRqu-fPqg6oAeqSJp1bWkUxl6lLEwWE-_gDfdNZ6NKaHg/viewform")
        //        }

        binding.textMainLogin.setOnClickListener {
            val intent = Intent(activity, SnsLoginActivity::class.java)
            signInLauncher.launch(intent)
        }

        binding.textMainRetentionBol.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
                return@setOnClickListener
            }
            val intent = Intent(activity, BolConfigActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }



        //        layout_main_life_save.setOnClickListener {
        //            val intent = Intent(activity, LifeSaveActivity::class.java)
        //            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        //            cashChangeLauncher.launch(intent)
        //        }

        val recommendPoint = FormatUtil.getMoneyTypeFloat(CountryConfigManager.getInstance().config.properties.recommendBol.toString())
        binding.textMainInviteDesc.text = getString(R.string.format_msg_invite_desc, recommendPoint)

        if (LoginInfoManager.getInstance().isMember) {
            binding.layoutMainRetentionBol.visibility = View.VISIBLE
            binding.textMainLogin.visibility = View.GONE
//            binding.viewMainWinnerBar.visibility = View.VISIBLE
//            binding.layoutMainMyWin.visibility = View.VISIBLE
            setRetentionBol()
        } else {
            binding.layoutMainRetentionBol.visibility = View.GONE
            binding.textMainLogin.visibility = View.VISIBLE
//            binding.viewMainWinnerBar.visibility = View.GONE
//            binding.layoutMainMyWin.visibility = View.GONE
        }

//        getEventWinCount()
        //        getEventWinCountToday()
    }

//    private fun getNewWinCount() {
//
//        ApiBuilder.create().newWinCountByUser.setCallback(object : PplusCallback<NewResultResponse<Int>> {
//            override fun onResponse(call: Call<NewResultResponse<Int>>?,
//                                    response: NewResultResponse<Int>?) {
//                if (!isAdded) {
//                    return
//                }
//                val count = response?.data
//                if (count != null && count > 0) {
//                    binding.imageWinHistoryNew.visibility = View.VISIBLE
//                } else {
//                    binding.imageWinHistoryNew.visibility = View.GONE
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Int>>?,
//                                   t: Throwable?,
//                                   response: NewResultResponse<Int>?) {
//                binding.imageWinHistoryNew.visibility = View.GONE
//            }
//        }).build().call()
//    }

    //    private fun getMyEventWinCount(){
    //        ApiBuilder.create().winCountOnlyPresentByMemberSeqNo.setCallback(object : PplusCallback<NewResultResponse<Int>> {
    //
    //            override fun onResponse(call: Call<NewResultResponse<Int>>,
    //                                    response: NewResultResponse<Int>) {
    //                if (!isAdded) {
    //                    return
    //                }
    //                val count = response.data
    //                text_main_my_win_count?.text = PplusCommonUtil.fromHtml(getString(R.string.html_main_my_win_count, FormatUtil.getMoneyType(count.toString())))
    //            }
    //
    //            override fun onFailure(call: Call<NewResultResponse<Int>>,
    //                                   t: Throwable,
    //                                   response: NewResultResponse<Int>) {
    //            }
    //        }).build().call()
    //    }

    //    private fun getEventWinCountToday(){
    //        ApiBuilder.create().winCountOnlyPresentToday.setCallback(object : PplusCallback<NewResultResponse<Int>> {
    //
    //            override fun onResponse(call: Call<NewResultResponse<Int>>,
    //                                    response: NewResultResponse<Int>) {
    //                if (!isAdded) {
    //                    return
    //                }
    //                val count = response.data
    //                text_main_today_winner_count?.text = PplusCommonUtil.fromHtml(getString(R.string.html_main_today_winner_count, FormatUtil.getMoneyType(count.toString())))
    //            }
    //
    //            override fun onFailure(call: Call<NewResultResponse<Int>>,
    //                                   t: Throwable,
    //                                   response: NewResultResponse<Int>) {
    //            }
    //        }).build().call()
    //    }

//    private fun getEventWinCount() {
//        val params = HashMap<String, String>()
//        params["appType"] = Const.APP_TYPE
//        ApiBuilder.create().getWinCountOnlyPresent(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {
//
//            override fun onResponse(call: Call<NewResultResponse<Int>>,
//                                    response: NewResultResponse<Int>) {
//                if (!isAdded) {
//                    return
//                }
//                val count = response.data
//                binding.textMainWinnerCount.text = PplusCommonUtil.fromHtml(getString(R.string.html_main_event_win_count, FormatUtil.getMoneyType(count.toString())))
//
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Int>>,
//                                   t: Throwable,
//                                   response: NewResultResponse<Int>) {
//            }
//        }).build().call()
//    }

    public fun setRetentionBol() {
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {
                if (!isAdded) {
                    return
                }

                binding.textMainRetentionBol.text = PplusCommonUtil.fromHtml(getString(R.string.html_bol_unit3, FormatUtil.getMoneyTypeFloat(LoginInfoManager.getInstance().user.totalBol.toString())))
            }
        })
//        getNewWinCount()
        //        getMyEventWinCount()
    }

    val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            if (LoginInfoManager.getInstance().isMember) {
                binding.layoutMainRetentionBol.visibility = View.VISIBLE
                binding.textMainLogin.visibility = View.GONE
//                binding.viewMainWinnerBar.visibility = View.VISIBLE
//                binding.layoutMainMyWin.visibility = View.VISIBLE
                setRetentionBol()
            } else {
                binding.layoutMainRetentionBol.visibility = View.GONE
                binding.textMainLogin.visibility = View.VISIBLE
//                binding.viewMainWinnerBar.visibility = View.GONE
//                binding.layoutMainMyWin.visibility = View.GONE
            }
        }
//        getEventWinCount()
    }

    val cashChangeLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (LoginInfoManager.getInstance().isMember) {
            setRetentionBol()
        }
//        getEventWinCount()
    }

    override fun getPID(): String {
        return "Main_Home"
    }

    companion object {

        @JvmStatic
        fun newInstance() = MainFragment().apply {
            arguments = Bundle().apply {
                //                        putString(Const.TAB, type)
                //                        putString(ARG_PARAM2, param2)
            }
        }
    }
}
