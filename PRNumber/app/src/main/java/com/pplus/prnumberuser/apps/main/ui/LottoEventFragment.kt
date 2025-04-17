package com.pplus.prnumberuser.apps.main.ui


import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.bol.ui.TicketConfigActivity
import com.pplus.prnumberuser.apps.common.mgmt.CountryConfigManager
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
import com.pplus.prnumberuser.apps.event.ui.EventGuideAlertActivity
import com.pplus.prnumberuser.apps.event.ui.LottoEventDetailActivity
import com.pplus.prnumberuser.apps.event.ui.LottoEventImpressionActivity
import com.pplus.prnumberuser.apps.event.ui.LottoWinHistoryActivity
import com.pplus.prnumberuser.apps.recommend.ui.RecommendHistoryActivity
import com.pplus.prnumberuser.core.code.common.EventType
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.Event
import com.pplus.prnumberuser.core.network.model.dto.Lotto
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.core.util.ToastUtil
import com.pplus.prnumberuser.databinding.FragmentLottoEventBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.pref.PreferenceUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [LottoEventFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LottoEventFragment : BaseFragment<BaseActivity>() {

    private var mLottoEvent:Event? = null
    private var mLotto:Lotto? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (arguments != null) {
//            type = arguments.getSerializable(Const.TYPE) as EnumData.AdsType
        }
    }

    private var _binding: FragmentLottoEventBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentLottoEventBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    var isWaiting = false
    override fun init() {

        if (PreferenceUtil.getDefaultPreference(activity).get(Const.LOTTO_GUIDE, true)) {
            val intent = Intent(activity, EventGuideAlertActivity::class.java)
            intent.putExtra(Const.KEY, Const.LOTTO_GUIDE)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.imageLottoBack.setOnClickListener {
            activity?.onBackPressed()
        }

        val cal = Calendar.getInstance()
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            val hour = cal.get(Calendar.HOUR_OF_DAY)
            val min = cal.get(Calendar.MINUTE)
            if(hour >= 20 && hour <= 21){
                if(hour == 21 && min > 30){
                    binding.layoutLottoEventWait.visibility = View.GONE
                }else{
                    binding.layoutLottoEventWait.visibility = View.VISIBLE
                    isWaiting = true
                }
            }
        }

        binding.imageLottoEventWinPopupClose.setOnClickListener {
            binding.layoutLottoEventWinPopup.visibility = View.GONE
        }

        binding.textLottoWinHistory.setOnClickListener {

            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
                return@setOnClickListener
            }

            val intent = Intent(activity, LottoWinHistoryActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

//        swipe_refresh_number_event.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
//            override fun onRefresh() {
//                getCount()
//                swipe_refresh_number_event.isRefreshing = false
//            }
//        })


        binding.textLottoEventJoin.setOnClickListener {

            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
                return@setOnClickListener
            }

            if(mLottoEvent != null){
                val intent = Intent(activity, LottoEventDetailActivity::class.java)
                intent.putExtra(Const.DATA, mLottoEvent)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            }
        }

        binding.imageLottoEventWinResult.setOnClickListener {

            if(StringUtils.isNotEmpty(mLotto!!.lottoPrevTimes)){
                val intent = Intent(activity, LottoEventImpressionActivity::class.java)
                intent.putExtra(Const.LOTTO_TIMES, mLotto!!.lottoPrevTimes)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)

            }

        }

        val recommendKey = LoginInfoManager.getInstance().user.recommendKey

        binding.textLottoEventRecommendKey.text = recommendKey

        binding.imageLottoEventRecommendCopy.setOnClickListener {
            val clipboard = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            val clip = ClipData.newPlainText(getString(R.string.word_recommend_code), recommendKey)
            clipboard.setPrimaryClip(clip)
            ToastUtil.show(activity, R.string.msg_copied_clipboard)
        }

        binding.textLottoEventInvite.setOnClickListener {

            share()
        }

        binding.textLottoEventRecommendHistory.setOnClickListener {
            val intent = Intent(activity, RecommendHistoryActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.textLottoEventTicketCount.setOnClickListener {
            val intent = Intent(activity, TicketConfigActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        setRetentionBol()
        getLotto()
    }

    private fun share() {
        val recommendPoint = FormatUtil.getMoneyType(CountryConfigManager.getInstance().config.properties!!.recommendBol.toString())
        val recommendKey = LoginInfoManager.getInstance().user.recommendKey
        val text = "${getString(R.string.format_invite_description, recommendKey)}\n${getString(R.string.format_msg_invite_url, LoginInfoManager.getInstance().user.recommendKey)}"
//        val text = "${getString(R.string.msg_invite_desc)}\n${getString(R.string.msg_invite_url)}"
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, text)
        val chooserIntent = Intent.createChooser(intent, getString(R.string.word_share))
        startActivity(chooserIntent)
    }

    private fun setRetentionBol() {
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {
                binding.textLottoEventTicketCount.text = getString(R.string.format_my_lotto_ticket, LoginInfoManager.getInstance().user.lottoTicketCount.toString())
            }
        })
    }
    private fun getLotto(){
        ApiBuilder.create().lotto.setCallback(object : PplusCallback<NewResultResponse<Lotto>> {
            override fun onResponse(call: Call<NewResultResponse<Lotto>>?, response: NewResultResponse<Lotto>?) {
                if(!isAdded){
                    return
                }

                if(response != null){
                    mLotto = response.data
                    binding.textLottoEventTitle.text = getString(R.string.format_lotto_event_title, mLotto!!.lottoTimes.toString())

                    if(StringUtils.isNotEmpty(mLotto!!.lottoPrevTimes) && !isWaiting && !PreferenceUtil.getDefaultPreference(activity).get(Const.CONFIRM_LOTTO+mLotto!!.lottoPrevTimes, false)){
                        PreferenceUtil.getDefaultPreference(activity).put(Const.CONFIRM_LOTTO+mLotto!!.lottoPrevTimes, true)
                        binding.layoutLottoEventWinPopup.visibility = View.VISIBLE

                    }

                    getLottoEvent()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Lotto>>?, t: Throwable?, response: NewResultResponse<Lotto>?) {

            }
        }).build().call()
    }

    private fun getLottoEvent(){

        val params = HashMap<String, String>()
        params["lottoTimes"] = mLotto!!.lottoTimes!!
        params["primaryType"] = EventType.PrimaryType.lotto.name
        ApiBuilder.create().getEvent(params).setCallback(object : PplusCallback<NewResultResponse<Event>> {
            override fun onResponse(call: Call<NewResultResponse<Event>>?, response: NewResultResponse<Event>?) {
                if(!isAdded){
                    return
                }
                if(response != null){
                    mLottoEvent = response.data
                    if(mLottoEvent == null){
                        return
                    }
                }

            }

            override fun onFailure(call: Call<NewResultResponse<Event>>?, t: Throwable?, response: NewResultResponse<Event>?) {

            }
        }).build().call()
    }

    private fun checkSignIn(){
        getLotto()
        setRetentionBol()
    }

    val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data
            checkSignIn()
        }
    }

    override fun getPID(): String {
        return "Main_point_lotto"
    }

    companion object {

        fun newInstance(): LottoEventFragment {

            val fragment = LottoEventFragment()
            val args = Bundle()
//            args.putSerializable(Const.TYPE, type)
            fragment.arguments = args
            return fragment
        }
    }

}// Required empty public constructor
