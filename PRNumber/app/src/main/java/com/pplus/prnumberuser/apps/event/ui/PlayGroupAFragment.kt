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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.bol.ui.BolConfigActivity
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.ui.BolChargeAlertActivity
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
import com.pplus.prnumberuser.apps.event.data.EventLoadingView
import com.pplus.prnumberuser.apps.event.data.PlayAdapter
import com.pplus.prnumberuser.apps.signin.ui.SnsLoginActivity
import com.pplus.prnumberuser.core.code.common.EventType
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.Event
import com.pplus.prnumberuser.core.network.model.dto.EventResult
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.FragmentPlayBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import com.pplus.utils.part.utils.time.DateFormatUtils
import retrofit2.Call
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [PlayGroupAFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlayGroupAFragment : BaseFragment<BaseActivity>() {

    private var mPage: Int = 0
    private var mTotalCount = 0
    //    private var mLayoutManager: LinearLayoutManager? = null
    private var mGridLayoutManager: GridLayoutManager? = null
    private var mLockListView = true
    private var mAdapter: PlayAdapter? = null
    private var mSelectPos: Int = 0
    private var mSelectEvent: Event? = null
    private var mSearch: EventType.WinAnnounceType? = null
    private var mGroupSeqNo = 1

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        mGroupSeqNo = arguments?.getInt(Const.GROUP)!!
    }

    private var _binding: FragmentPlayBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentPlayBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun init() {

        binding.imagePlayGroupDBack.setOnClickListener {
            activity?.onBackPressed()
        }

//        if (PreferenceUtil.getDefaultPreference(activity).get(Const.PLAY_GUIDE, true)) {
//            val intent = Intent(activity, EventGuideAlertActivity::class.java)
//            intent.putExtra(Const.KEY, Const.PLAY_GUIDE)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }

//        mLayoutManager = LinearLayoutManager(activity!!)
        mGridLayoutManager = GridLayoutManager(activity, 2)

//        mGridLayoutManager!!.setSpanSizeLookup(object : SpanSizeLookup() {
//            override fun getSpanSize(position: Int): Int {
//                return when (mAdapter!!.getItemViewType(position)) {
//                    mAdapter!!.TYPE_HEADER -> 2
//                    mAdapter!!.TYPE_ITEM -> 1
//                    else -> 1
//                }
//            }
//        })

        binding.recyclerPlay.layoutManager = mGridLayoutManager
//        recycler_play.layoutManager = mLayoutManager
        mAdapter = PlayAdapter()
        binding.recyclerPlay.adapter = mAdapter
        if (binding.recyclerPlay.itemDecorationCount == 0) {
            binding.recyclerPlay.addItemDecoration(CustomItemOffsetDecoration(requireActivity(), R.dimen.height_40))
        }

        binding.recyclerPlay.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            internal var pastVisibleItems: Int = 0
            internal var visibleItemCount: Int = 0
            internal var totalItemCount: Int = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                super.onScrolled(recyclerView, dx, dy)
                //                visibleItemCount = mLayoutManager!!.childCount
                //                totalItemCount = mLayoutManager!!.itemCount
                //                pastVisibleItems = mLayoutManager!!.findFirstVisibleItemPosition()
                visibleItemCount = mGridLayoutManager!!.childCount
                totalItemCount = mGridLayoutManager!!.itemCount
                pastVisibleItems = mGridLayoutManager!!.findFirstVisibleItemPosition()
                if (!mLockListView) {
                    if (totalItemCount < mTotalCount && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
                        mPage++
                        listCall(mPage)
                    }
                }
            }
        })

        mAdapter!!.setOnItemClickListener(object : PlayAdapter.OnItemClickListener {

            private val MIN_CLICK_INTERVAL: Long = 1000 //in millis
            private var lastClickTime: Long = 0

//            override fun onHeaderClick(item:Event) {
////                if (item.winAnnounceType.equals(EventType.WinAnnounceType.limit.name)) {
////                    if (item.joinCount!! >= item.maxJoinCount!!) {
////
////                        if(item.status == "announce"){
////                            val intent = Intent(activity, RandomPlayWinActivity::class.java)
////                            intent.putExtra(Const.DATA, item)
////                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
////                            activity!!.startActivityForResult(intent, Const.REQ_DETAIL)
////                        }else{
////                            PplusCommonUtil.showEventAlert(activity!!, 0, item)
////                        }
////                        return
////                    }
////                }
////
////                val intent = Intent(activity, RandomPlayDetailActivity::class.java)
////                intent.putExtra(Const.DATA, item)
////                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
////                activity?.startActivityForResult(intent, Const.REQ_RANDOM_DETAIL)
//            }

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

                    if (item.primaryType.equals(EventType.PrimaryType.move.name)) {
                        val intent = Intent(activity, EventMoveDetailActivity::class.java)
                        intent.putExtra(Const.DATA, item)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        defaultLauncher.launch(intent)
                    } else {
                        if (StringUtils.isNotEmpty(item.eventLink)) {
                            val intent = Intent(activity, EventDetailActivity::class.java)
                            item.personalContents = ""
                            intent.putExtra(Const.DATA, item)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            defaultLauncher.launch(intent)
                        } else {
                            if (item.reward == null || item.reward!! >= 0) {
                                joinEvent(item)
                            }else{
                                if (LoginInfoManager.getInstance().user.totalBol >= Math.abs(item.reward!!)) {
                                    val intent = Intent(activity, PlayAlertActivity::class.java)
                                    intent.putExtra(Const.POSITION, 0)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    joinAlertLauncher.launch(intent)
                                } else {
                                    val intent = Intent(activity, BolChargeAlertActivity::class.java)
                                    intent.putExtra(Const.EVENT, item)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    cashChangeLauncher.launch(intent)
                                }
                            }
                        }
                    }
                }
            }
        })

        binding.swipeRefreshPlay.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                getCount()
                binding.swipeRefreshPlay.isRefreshing = false
            }
        })


        binding.textPlayRetentionBol.setOnClickListener {
            val intent = Intent(activity, BolConfigActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.textPlayLogin.setOnClickListener {
            val intent = Intent(activity, SnsLoginActivity::class.java)
            signInLauncher.launch(intent)
        }

        if (LoginInfoManager.getInstance().isMember) {
            setRetentionBol()
            binding.textPlayRetentionBol.visibility = View.VISIBLE
            binding.textPlayLogin.visibility = View.GONE
        }else{
            binding.textPlayRetentionBol.visibility = View.GONE
            binding.textPlayLogin.visibility = View.VISIBLE
        }

        binding.layoutPlayLoading.visibility = View.VISIBLE
//        winListCall()
        getCount()

    }

//    private fun winListCall() {
//        //        text_play_win_list.visibility = View.GONE
//        val params = HashMap<String, String>()
//        params["filter"] = EventType.PrimaryType.goodluck.name
//        params["pg"] = "1"
//        params["sz"] = "20"
//
//        ApiBuilder.create().getWinList(params).setCallback(object : PplusCallback<NewResultResponse<EventWin>> {
//
//            override fun onResponse(call: Call<NewResultResponse<EventWin>>,
//                                    response: NewResultResponse<EventWin>) {
//
//                if (!isAdded) {
//                    return
//                }
//
//                try {
//                    val eventWinList = response.datas
//
//                    if (eventWinList.isEmpty()) {
//                        text_play_list?.visibility = View.GONE
//                    } else {
//                        text_play_list?.visibility = View.VISIBLE
//                    }
//
//                    val stringBuilder = StringBuilder()
//                    stringBuilder.append("   ")
//                    for (eventWin in eventWinList) {
//
//                        val date = SimpleDateFormat(DateFormatUtils.PPLUS_DATE_FORMAT.pattern).parse(eventWin.winDate)
//                        val time = SimpleDateFormat("HH:mm").format(date)
//
//                        val text = getString(R.string.format_goodluck_win_list, time, eventWin.user!!.nickname, eventWin.gift!!.title)
//                        stringBuilder.append(text)
//                        stringBuilder.append("   ")
//                    }
//                    text_play_list?.setSingleLine()
//                    text_play_list?.text = stringBuilder.toString()
//                    text_play_list?.isSelected = true
//                } catch (e: Exception) {
//
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<EventWin>>,
//                                   t: Throwable,
//                                   response: NewResultResponse<EventWin>) {
//            }
//        }).build().call()
//    }

    private fun setRetentionBol() {
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {

                binding.textPlayRetentionBol.text = PplusCommonUtil.fromHtml(getString(R.string.html_cash_unit2, FormatUtil.getMoneyType(LoginInfoManager.getInstance().user.totalBol.toString())))
            }
        })
    }

    private inner class CustomItemOffsetDecoration(private val mItemOffset: Int) : RecyclerView.ItemDecoration() {

        constructor(context: Context, @DimenRes lastOffsetId: Int) : this(context.resources.getDimensionPixelSize(lastOffsetId)) {}

        override fun getItemOffsets(outRect: Rect,
                                    view: View,
                                    parent: RecyclerView,
                                    state: RecyclerView.State) {

            super.getItemOffsets(outRect, view, parent, state)

            val position = parent.getChildAdapterPosition(view)

//            if(position == 0){
//                outRect.set(0, mItemOffset, 0, mItemOffset)
//            }else {
//                outRect.set(0, 0, 0, mItemOffset)
//            }

            outRect.set(0, mItemOffset, 0, 0)
        }
    }

    fun joinEvent(event: Event) {
        val params = HashMap<String, String>()
        params["no"] = event.no.toString()
        showProgress("")


        val request = if(event.reward != null && event.reward!! < 0){
            ApiBuilder.create().serializableJoinEvent(params)
        }else{
            ApiBuilder.create().joinEvent(params)
        }

        request.setCallback(object : PplusCallback<NewResultResponse<EventResult>> {
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

                if(response?.data != null && StringUtils.isNotEmpty(response.data.joinDate)){
                    PplusCommonUtil.showEventAlert(activity!!, response.resultCode, event, response.data.joinDate, response.data.joinTerm)
                }else{
                    PplusCommonUtil.showEventAlert(activity!!, response?.resultCode!!, event)
                }
                getEvent()

            }
        }).build().call()
    }

    private fun getCount() {
        val params = HashMap<String, String>()
        params["filter"] = EventType.PrimaryType.goodluck.name
        if (mSearch != null) {
            params["search"] = mSearch!!.name
        }
        params["platform"] = "aos"
        params["appType"] = Const.APP_TYPE
        params["groupSeqNo"] = mGroupSeqNo.toString()
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
                    binding.layoutPlayLoading.visibility = View.GONE
                    binding.layoutPlayNotExist.visibility = View.VISIBLE
                } else {
                    binding.layoutPlayNotExist.visibility = View.GONE
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
        params["filter"] = EventType.PrimaryType.goodluck.name
        if (mSearch != null) {
            params["search"] = mSearch!!.name
        }
        params["pg"] = page.toString()
        params["platform"] = "aos"
        params["appType"] = Const.APP_TYPE
        params["groupSeqNo"] = mGroupSeqNo.toString()
//        showProgress("")
        mLockListView = true
        ApiBuilder.create().getEventList(params).setCallback(object : PplusCallback<NewResultResponse<Event>> {

            override fun onResponse(call: Call<NewResultResponse<Event>>,
                                    response: NewResultResponse<Event>) {

                //                hideProgress()
                if (!isAdded) {
                    return
                }
                binding.layoutPlayLoading.visibility = View.GONE
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
            setRetentionBol()
            binding.textPlayRetentionBol.visibility = View.VISIBLE
            binding.textPlayLogin.visibility = View.GONE
        }else{
            binding.textPlayRetentionBol.visibility = View.GONE
            binding.textPlayLogin.visibility = View.VISIBLE
        }
    }

    val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data
            checkSignIn()
        }
    }

    val joinAlertLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data
            if (data != null) {
                val event = data.getParcelableExtra<Event>(Const.DATA)
                if (LoginInfoManager.getInstance().user.totalBol >= Math.abs(event!!.reward!!)) {
                    joinEvent(event)
                } else {
                    val intent = Intent(activity, BolChargeAlertActivity::class.java)
                    intent.putExtra(Const.EVENT, event)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    cashChangeLauncher.launch(intent)
                }
            }
        }else{
            getEvent()
        }
    }

    val cashChangeLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        setRetentionBol()
    }


    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data

            if (data != null) {
                val event = data.getParcelableExtra<Event>(Const.DATA)
                showProgress("")
                PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
                    override fun reload() {
                        hideProgress()
                        if (!isAdded) {
                            return
                        }

                        if (LoginInfoManager.getInstance().user.totalBol >= Math.abs(event!!.reward!!)) {
                            joinEvent(event)
                        }
                    }
                })
            } else {
                setRetentionBol()
                getEvent()
            }
        }else{
            setRetentionBol()
            getEvent()
        }
    }

    var defaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        getEvent()
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
        return "Main_playeventlist"
    }

    companion object {

        fun newInstance(groupSeqNo:Int): PlayGroupAFragment {

            val fragment = PlayGroupAFragment()
            val args = Bundle()
            args.putSerializable(Const.GROUP, groupSeqNo)
            fragment.arguments = args
            return fragment
        }
    }

}// Required empty public constructor
