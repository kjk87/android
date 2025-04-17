package com.pplus.prnumberuser.apps.event.ui


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DimenRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.bol.ui.BolConfigActivity
import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberuser.apps.common.builder.data.AlertData
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
import com.pplus.prnumberuser.apps.event.data.EventLoadingView
import com.pplus.prnumberuser.apps.event.data.TuneEventAdapter
import com.pplus.prnumberuser.apps.main.ui.PadActivity
import com.pplus.prnumberuser.apps.my.ui.MyWinHistoryActivity
import com.pplus.prnumberuser.apps.page.ui.Alert3rdPartyInfoTermsActivity
import com.pplus.prnumberuser.apps.signin.ui.SnsLoginActivity
import com.pplus.prnumberuser.apps.signup.ui.VerificationMeActivity
import com.pplus.prnumberuser.core.code.common.EventType
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.Event
import com.pplus.prnumberuser.core.network.model.dto.EventResult
import com.pplus.prnumberuser.core.network.model.dto.Plus
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.FragmentTuneGroupABinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import com.pplus.utils.part.utils.time.DateFormatUtils
import retrofit2.Call
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [TuneEventGroupAFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TuneEventGroupAFragment : BaseFragment<BaseActivity>() {

    private var mPage: Int = 0
    private var mTotalCount = 0

    private var mLayoutManager: LinearLayoutManager? = null

    //    private var mGridLayoutManager: GridLayoutManager? = null
    private var mLockListView = true
    private var mAdapter: TuneEventAdapter? = null
    private var mSelectPos: Int = 0
    private var mSelectEvent: Event? = null
    private var mIsToday = true

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
    }


    private var _binding: FragmentTuneGroupABinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentTuneGroupABinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        setRetentionBol()
        getEvent()
    }

    val eventAgreeLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data
            if(data != null){
                val event = data.getParcelableExtra<Event>(Const.DATA)
                joinEvent(event!!)
            }

        }
    }

    val cashChangeLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        setRetentionBol()
    }

    val defaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        getEvent()
    }

    val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data
            checkSignIn()
        }
    }

    override fun init() {

        binding.imageTuneEventBack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.imageTuneGroupAClose.setOnClickListener {
            activity?.onBackPressed()
        }

        mLayoutManager = LinearLayoutManager(requireActivity())

        binding.recyclerTuneGroupA.layoutManager = mLayoutManager
        mAdapter = TuneEventAdapter()
        binding.recyclerTuneGroupA.adapter = mAdapter

//        recycler_tune_group_a.addOnScrollListener(RecyclerScaleScrollListener(image_tune_group_a_win_history))

        binding.textTuneGroupAWinHistory.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
                return@setOnClickListener
            }
            val intent = Intent(activity, MyWinHistoryActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            cashChangeLauncher.launch(intent)
        }

        binding.imageTuneGroupAPad.setOnClickListener {
            val intent = Intent(activity, PadActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            cashChangeLauncher.launch(intent)
        }

        if (binding.recyclerTuneGroupA.itemDecorationCount == 0) {
            binding.recyclerTuneGroupA.addItemDecoration(CustomItemOffsetDecoration(requireActivity(), R.dimen.height_40))
        }

        binding.recyclerTuneGroupA.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            internal var pastVisibleItems: Int = 0
            internal var visibleItemCount: Int = 0
            internal var totalItemCount: Int = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                super.onScrolled(recyclerView, dx, dy)
                visibleItemCount = mLayoutManager!!.childCount
                totalItemCount = mLayoutManager!!.itemCount
                pastVisibleItems = mLayoutManager!!.findFirstVisibleItemPosition()
                if (!mLockListView) {
                    if (totalItemCount < mTotalCount && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
                        mPage++
                        listCall(mPage)
                    }
                }
            }
        })

        mAdapter!!.setOnItemClickListener(object : TuneEventAdapter.OnItemClickListener {

            private val MIN_CLICK_INTERVAL: Long = 1000 //in millis
            private var lastClickTime: Long = 0

            override fun onItemClick(position: Int) {

                val currentTime = SystemClock.elapsedRealtime()
                if (currentTime - lastClickTime > MIN_CLICK_INTERVAL) {
                    lastClickTime = currentTime

                    if (position < 0) {
                        return
                    }

                    val item = mAdapter!!.getItem(position)

                    mSelectPos = position
                    mSelectEvent = item

                    if (item.winAnnounceType.equals(EventType.WinAnnounceType.special.name)) {
                        val currentMillis = System.currentTimeMillis()
                        val winAnnounceDate = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.winAnnounceDate).time
                        if (currentMillis > winAnnounceDate) {

                            val intent = Intent(activity, EventImpressionActivity::class.java)
                            intent.putExtra(Const.DATA, item)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            defaultLauncher.launch(intent)
                            return
                        }
                    } else if (item.winAnnounceType.equals(EventType.WinAnnounceType.limit.name)) {
                        if (item.joinCount!! >= item.maxJoinCount!!) {
                            val intent = Intent(activity, EventImpressionActivity::class.java)
                            intent.putExtra(Const.DATA, item)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            defaultLauncher.launch(intent)
                            return
                        }
                    } else if (item.winAnnounceType.equals(EventType.WinAnnounceType.immediately.name)) {
                        if (item.winnerCount!! >= item.totalGiftCount!!) {
                            val intent = Intent(activity, EventImpressionActivity::class.java)
                            intent.putExtra(Const.DATA, item)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            defaultLauncher.launch(intent)
                            return
                        }
                    }

                    if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
                        return
                    }

                    when(item.detailType){
                        1->{
                            if(item.isPlus != null && item.isPlus!! && item.pageSeqNo != null && item.agreement2 != null && item.agreement2!! == 1){

                                if (LoginInfoManager.getInstance().user.verification!!.media != "external") {
                                    val builder = AlertBuilder.Builder()
                                    builder.setTitle(getString(R.string.word_notice_alert))
                                    builder.addContents(AlertData.MessageData(getString(R.string.msg_verification_me_for_event1), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
                                    builder.addContents(AlertData.MessageData(getString(R.string.msg_move_verification), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                                    builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
                                    builder.setOnAlertResultListener(object : OnAlertResultListener {
                                        override fun onCancel() {}
                                        override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                                            when (event_alert) {
                                                AlertBuilder.EVENT_ALERT.RIGHT -> {
                                                    val intent = Intent(activity, VerificationMeActivity::class.java)
                                                    intent.putExtra(Const.KEY, Const.VERIFICATION_ME)
                                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                                    verificationLauncher.launch(intent)
                                                }
                                            }
                                        }
                                    }).builder().show(activity)
                                    return
                                }

                                val params = HashMap<String, String>()
                                params["pageSeqNo"] = item.pageSeqNo.toString()
                                showProgress("")
                                ApiBuilder.create().getOnlyPlus(params).setCallback(object : PplusCallback<NewResultResponse<Plus>> {
                                    override fun onResponse(call: Call<NewResultResponse<Plus>>?, response: NewResultResponse<Plus>?) {
                                        hideProgress()
                                        if (!isAdded) {
                                            return
                                        }
                                        if(response?.data == null || response.data.agreement == null || !response.data.agreement!!){
                                            val intent = Intent(activity, Alert3rdPartyInfoTermsActivity::class.java)
                                            intent.putExtra(Const.EVENT, item)
                                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                            eventAgreeLauncher.launch(intent)
                                        }else{
                                            joinEvent(item)
                                        }
                                    }

                                    override fun onFailure(call: Call<NewResultResponse<Plus>>?, t: Throwable?, response: NewResultResponse<Plus>?) {
                                        hideProgress()
                                    }
                                }).build().call()
                            }else{
                                joinEvent(item)
                            }
                        }
                        2,3->{
                            val intent = Intent(activity, EventDetailActivity::class.java)
                            intent.putExtra(Const.DATA, item)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            defaultLauncher.launch(intent)
                        }
                    }
                }
            }
        })

        binding.swipeRefreshTuneGroupA.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                getCount()
                binding.swipeRefreshTuneGroupA.isRefreshing = false
            }
        })

        binding.layoutTuneGroupALoading?.visibility = View.VISIBLE

        checkSignIn()

        binding.textTuneGroupATodayTab.isSelected = true
        binding.textTuneGroupAOtherTab.isSelected = false
        mIsToday = true

        binding.textTuneGroupATodayTab.setOnClickListener {
            binding.textTuneGroupATodayTab.isSelected = true
            binding.textTuneGroupAOtherTab.isSelected = false
            mIsToday = true
            getCount()
        }

        binding.textTuneGroupAOtherTab.setOnClickListener {
            binding.textTuneGroupATodayTab.isSelected = false
            binding.textTuneGroupAOtherTab.isSelected = true
            mIsToday = false
            getCount()
        }

        binding.textTuneGroupALogin.setOnClickListener {
            val intent = Intent(activity, SnsLoginActivity::class.java)
            signInLauncher.launch(intent)
        }

        binding.textTuneGroupAPoint.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
                return@setOnClickListener
            }
            val intent = Intent(activity, BolConfigActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        getCount()

    }

    private fun setRetentionBol() {
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {

                binding.textTuneGroupAPoint.text = PplusCommonUtil.fromHtml(getString(R.string.html_cash_unit2, FormatUtil.getMoneyType(LoginInfoManager.getInstance().user.totalBol.toString())))
            }
        })
    }

    private inner class CustomItemOffsetDecoration(private val mItemOffset: Int) : RecyclerView.ItemDecoration() {

        constructor(context: Context, @DimenRes lastOffsetId: Int) : this(context.resources.getDimensionPixelSize(lastOffsetId)) {
        }

        override fun getItemOffsets(outRect: Rect,
                                    view: View,
                                    parent: RecyclerView,
                                    state: RecyclerView.State) {

            super.getItemOffsets(outRect, view, parent, state)

            val position = parent.getChildAdapterPosition(view)

            if (position <= 1) {
                outRect.set(0, mItemOffset, 0, mItemOffset)
            } else {
                outRect.set(0, 0, 0, mItemOffset)
            }

        }
    }

    fun joinEvent(event: Event) {
        val params = HashMap<String, String>()
        params["no"] = event.no.toString()
        showProgress("")

        ApiBuilder.create().joinEvent(params).setCallback(object : PplusCallback<NewResultResponse<EventResult>> {
            override fun onResponse(call: Call<NewResultResponse<EventResult>>?,
                                    response: NewResultResponse<EventResult>?) {

                hideProgress()
                if (!isAdded) {
                    return
                }

                if (response?.data != null) {
                    if (event.winAnnounceType.equals(EventType.WinAnnounceType.immediately.name)) {
                        val mLoading = EventLoadingView()
                        mLoading.isCancelable = false
                        mLoading.setText(getString(R.string.msg_checking_event_result))
                        val delayTime = 2000L
                        mLoading.isCancelable = false
                        try {
                            mLoading.show(parentFragmentManager, "")
                        } catch (e: Exception) {

                        }

                        val handler = Handler()
                        handler.postDelayed(Runnable {

                            try {
                                mLoading.dismiss()
                            } catch (e: Exception) {

                            }

                            val intent = Intent(activity, EventResultActivity::class.java)
                            intent.putExtra(Const.EVENT_RESULT, response.data)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            resultLauncher.launch(intent)
                        }, delayTime)

                    } else {
                        val intent = Intent(activity, EventResultActivity::class.java)
                        intent.putExtra(Const.EVENT_RESULT, response.data)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        resultLauncher.launch(intent)
                    }
                }

            }

            override fun onFailure(call: Call<NewResultResponse<EventResult>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<EventResult>?) {

                hideProgress()
                if (!isAdded) {
                    return
                }

                if (response?.data != null && StringUtils.isNotEmpty(response.data.joinDate)) {
                    PplusCommonUtil.showEventAlert(activity!!, response.resultCode, event, response.data.joinDate, response.data.joinTerm)
                } else {
                    PplusCommonUtil.showEventAlert(activity!!, response?.resultCode!!, event)
                }
                getEvent()

            }
        }).build().call()
    }

    private fun getCount() {
        val params = HashMap<String, String>()
        params["filter"] = EventType.PrimaryType.tune.name
        params["platform"] = "aos"
        params["appType"] = Const.APP_TYPE
        params["groupSeqNo"] = "1"
//        params["isToday"] = mIsToday.toString()
        //        showProgress("")
        ApiBuilder.create().getEventCount(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {

            override fun onResponse(call: Call<NewResultResponse<Int>>,
                                    response: NewResultResponse<Int>) {
                //                hideProgress()
                if (!isAdded) {
                    return
                }
                mTotalCount = response.data

                if (mTotalCount == 0) {
                    binding.layoutTuneGroupALoading.visibility = View.GONE
                    binding.layoutTuneGroupANotExist.visibility = View.VISIBLE
                } else {
                    binding.layoutTuneGroupANotExist.visibility = View.GONE
                }

                mPage = 1
                mAdapter?.clear()
                listCall(mPage)
            }

            override fun onFailure(call: Call<NewResultResponse<Int>>,
                                   t: Throwable,
                                   response: NewResultResponse<Int>) {
                //                hideProgress()
            }
        }).build().call()
    }

    private fun listCall(page: Int) {
        val params = HashMap<String, String>()
        params["filter"] = EventType.PrimaryType.tune.name
        params["pg"] = page.toString()
        params["platform"] = "aos"
        params["appType"] = Const.APP_TYPE
        params["groupSeqNo"] = "1"
//        params["isToday"] = mIsToday.toString()
        //        showProgress("")
        mLockListView = true
        ApiBuilder.create().getEventList(params).setCallback(object : PplusCallback<NewResultResponse<Event>> {

            override fun onResponse(call: Call<NewResultResponse<Event>>,
                                    response: NewResultResponse<Event>) {

                //                hideProgress()
                if (!isAdded) {
                    return
                }
                binding.layoutTuneGroupALoading.visibility = View.GONE
                mLockListView = false

                mAdapter?.addAll(response.datas)
            }

            override fun onFailure(call: Call<NewResultResponse<Event>>,
                                   t: Throwable,
                                   response: NewResultResponse<Event>) {
                //                hideProgress()
            }
        }).build().call()
    }

    private fun checkSignIn(){
        if (LoginInfoManager.getInstance().isMember) {
            binding.textTuneGroupAPoint.visibility = View.VISIBLE
            binding.textTuneGroupALogin.visibility = View.GONE
            setRetentionBol()
        } else {
            binding.textTuneGroupAPoint.visibility = View.GONE
            binding.textTuneGroupALogin.visibility = View.VISIBLE
        }
    }

    private fun getEvent() {
        if (mSelectEvent == null) {
            return
        }
        val params = HashMap<String, String>()
        params["no"] = mSelectEvent!!.no.toString()
        //        showProgress("")
        ApiBuilder.create().getEvent(params).setCallback(object : PplusCallback<NewResultResponse<Event>> {
            override fun onResponse(call: Call<NewResultResponse<Event>>?,
                                    response: NewResultResponse<Event>?) {
                //                hideProgress()
                if (!isAdded) {
                    return
                }
                if (response!!.data != null) {
                    mAdapter!!.replaceData(mSelectPos, response.data)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Event>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<Event>?) {
                //                hideProgress()
            }
        }).build().call()
        mSelectEvent = null
    }

    override fun getPID(): String {
        return "Main_Fit event"
    }

    companion object {

        @JvmStatic
        fun newInstance() = TuneEventGroupAFragment().apply {
            arguments = Bundle().apply {
                //                putInt(Const.TAB, tab)
                //                        putString(ARG_PARAM2, param2)
            }
        }
    }

} // Required empty public constructor
