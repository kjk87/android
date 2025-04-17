//package com.pplus.luckybol.apps.event.ui
//
//
//import android.app.Activity
//import android.content.Intent
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.fragment.app.Fragment
//import com.pplus.luckybol.Const
//import com.pplus.luckybol.R
//import com.pplus.luckybol.apps.common.ui.base.BaseActivity
//import com.pplus.luckybol.apps.common.ui.base.BaseFragment
//import com.pplus.luckybol.core.code.common.EventType
//import com.pplus.luckybol.core.network.ApiBuilder
//import com.pplus.luckybol.core.network.model.dto.Event
//import com.pplus.luckybol.core.network.model.dto.Lotto
//import com.pplus.luckybol.core.network.model.response.NewResultResponse
//import com.pplus.luckybol.core.util.PplusCommonUtil
//import com.pplus.luckybol.databinding.FragmentLottoEventBinding
//import com.pplus.networks.common.PplusCallback
//import com.pplus.utils.part.pref.PreferenceUtil
//import com.pplus.utils.part.utils.StringUtils
//import retrofit2.Call
//import java.util.*
//
//
///**
// * A simple [Fragment] subclass.
// * Use the [LottoEventFragment.newInstance] factory method to
// * create an instance of this fragment.
// */
//class LottoEventFragment : BaseFragment<BaseActivity>() {
//
//    private var mLottoEvent:Event? = null
//    private var mLotto:Lotto? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//
//        super.onCreate(savedInstanceState)
//        if (arguments != null) {
////            type = arguments.getSerializable(Const.TYPE) as EnumData.AdsType
//        }
//    }
//
//    private var _binding: FragmentLottoEventBinding? = null
//    private val binding get() = _binding!!
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//
//        _binding = FragmentLottoEventBinding.inflate(inflater, container, false)
//        val view = binding.root
//        return view
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//
//    var isWaiting = false
//    override fun init() {
//
////        if (PreferenceUtil.getDefaultPreference(activity).get(Const.LOTTO_GUIDE, true)) {
////            val intent = Intent(activity, EventGuideAlertActivity::class.java)
////            intent.putExtra(Const.KEY, Const.LOTTO_GUIDE)
////            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
////            startActivity(intent)
////        }
//
//        binding.imageLottoBack.setOnClickListener {
//            activity?.onBackPressed()
//        }
//
//        val cal = Calendar.getInstance()
//        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
//            val hour = cal.get(Calendar.HOUR_OF_DAY)
//            val min = cal.get(Calendar.MINUTE)
//            if(hour >= 20 && hour <= 21){
//                if(hour == 21 && min > 30){
//                    binding.layoutLottoEventWait.visibility = View.GONE
//                }else{
//                    binding.layoutLottoEventWait.visibility = View.VISIBLE
//                    isWaiting = true
//                }
//            }
//        }
//
//        binding.imageLottoEventWinPopupClose.setOnClickListener {
//            binding.layoutLottoEventWinPopup.visibility = View.GONE
//        }
//
//        binding.textLottoWinHistory.setOnClickListener {
//
//            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
//                return@setOnClickListener
//            }
//
//            val intent = Intent(activity, LottoWinHistoryActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
////        swipe_refresh_number_event.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
////            override fun onRefresh() {
////                getCount()
////                swipe_refresh_number_event.isRefreshing = false
////            }
////        })
//
//
//        binding.textLottoEventJoin.setOnClickListener {
//
//            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
//                return@setOnClickListener
//            }
//
//            if(mLottoEvent != null){
//                val intent = Intent(activity, LottoEventDetailActivity::class.java)
//                intent.putExtra(Const.DATA, mLottoEvent)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                startActivity(intent)
//            }
//        }
//
//        binding.imageLottoEventWinResult.setOnClickListener {
//
//            if(StringUtils.isNotEmpty(mLotto!!.lottoPrevTimes)){
//                val intent = Intent(activity, LuckyLottoDetailActivity::class.java)
//                intent.putExtra(Const.DATA, mLottoEvent)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                startActivity(intent)
//
//            }
//
//        }
//
//
//
//        getLotto()
//    }
//
//    private fun getLotto(){
//        showProgress("")
//        ApiBuilder.create().lotto.setCallback(object : PplusCallback<NewResultResponse<Lotto>> {
//            override fun onResponse(call: Call<NewResultResponse<Lotto>>?, response: NewResultResponse<Lotto>?) {
//                hideProgress()
//                if(!isAdded){
//                    return
//                }
//
//                if(response != null){
//                    mLotto = response.data
//                    binding.textLottoEventTitle.text = getString(R.string.format_lotto_event_title, mLotto!!.lottoTimes.toString())
//
//                    if(StringUtils.isNotEmpty(mLotto!!.lottoPrevTimes) && !isWaiting && !PreferenceUtil.getDefaultPreference(activity).get(Const.CONFIRM_LOTTO+mLotto!!.lottoPrevTimes, false)){
//                        PreferenceUtil.getDefaultPreference(activity).put(Const.CONFIRM_LOTTO+mLotto!!.lottoPrevTimes, true)
//                        binding.layoutLottoEventWinPopup.visibility = View.VISIBLE
//
//                    }
//
//                    binding.textLottoJoinHistory.setOnClickListener {
//                        val intent = Intent(activity, MyLottoJoinListActivity::class.java)
//                        intent.putExtra(Const.LOTTO_TIMES, mLotto!!.lottoTimes)
//                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                        startActivity(intent)
//                    }
//
//                    getLottoEvent()
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Lotto>>?, t: Throwable?, response: NewResultResponse<Lotto>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    private fun getLottoEvent(){
//
//        val params = HashMap<String, String>()
//        params["lottoTimes"] = mLotto!!.lottoTimes!!
//        params["primaryType"] = EventType.PrimaryType.lotto.name
//        showProgress("")
//        ApiBuilder.create().getEvent(params).setCallback(object : PplusCallback<NewResultResponse<Event>> {
//            override fun onResponse(call: Call<NewResultResponse<Event>>?, response: NewResultResponse<Event>?) {
//                hideProgress()
//                if(!isAdded){
//                    return
//                }
//                if(response != null){
//                    mLottoEvent = response.data
//                    if(mLottoEvent == null){
//                        return
//                    }
//                }
//
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Event>>?, t: Throwable?, response: NewResultResponse<Event>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    private fun setRetentionBol() {
//        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
//
//            override fun reload() {
//
//            }
//        })
//    }
//
//    private fun checkSignIn(){
//        getLotto()
//        setRetentionBol()
//    }
//
//    val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            // There are no request codes
//            val data = result.data
//            checkSignIn()
//        }
//    }
//
//    override fun getPID(): String {
//        return "Main_mypage_lotto"
//    }
//
//    companion object {
//
//        fun newInstance(): LottoEventFragment {
//
//            val fragment = LottoEventFragment()
//            val args = Bundle()
////            args.putSerializable(Const.TYPE, type)
//            fragment.arguments = args
//            return fragment
//        }
//    }
//
//}// Required empty public constructor
