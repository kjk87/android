//package com.pplus.prnumberuser.apps.main.ui
//
//
//import android.app.Activity
//import android.content.Context
//import android.content.Intent
//import android.graphics.Rect
//import android.os.Bundle
//import android.os.Handler
//import android.os.SystemClock
//import android.view.View
//import androidx.annotation.DimenRes
//import androidx.fragment.app.Fragment
//import com.pplus.networks.common.PplusCallback
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.bol.ui.BolConfigActivity
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.common.ui.BolChargeAlertActivity
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
//import com.pplus.prnumberuser.apps.event.data.EventLoadingView
//import com.pplus.prnumberuser.apps.event.data.PlayAdapter
//import com.pplus.prnumberuser.apps.event.ui.*
//import com.pplus.prnumberuser.apps.signin.ui.SnsLoginActivity
//import com.pplus.prnumberuser.core.code.common.EventType
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Event
//import com.pplus.prnumberuser.core.network.model.dto.EventResult
//import com.pplus.prnumberuser.core.network.model.dto.EventWin
//import com.pplus.prnumberuser.core.network.model.dto.User
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.pref.PreferenceUtil
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.utils.part.utils.time.DateFormatUtils
//import com.pplus.utils.part.utils.time.DateFormatUtils.PPLUS_DATE_FORMAT
//import kotlinx.android.synthetic.main.fragment_play.*
//import retrofit2.Call
//import java.text.SimpleDateFormat
//import java.util.*
//
//
///**
// * A simple [Fragment] subclass.
// * Use the [PlayFragment.newInstance] factory method to
// * create an instance of this fragment.
// */
//class PlayFragment : BaseFragment<BaseActivity>() {
//
//    // TODO: Rename and change types of parameters
//
//
//    private var mPage: Int = 0
//    private var mTotalCount = 0
//    //    private var mLayoutManager: LinearLayoutManager? = null
//    private var mGridLayoutManager: androidx.recyclerview.widget.GridLayoutManager? = null
//    private var mLockListView = true
//    private var mAdapter: PlayAdapter? = null
//    private var mSelectPos: Int = 0
//    private var mSelectEvent: Event? = null
//    private var mSearch: EventType.WinAnnounceType? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//
//        super.onCreate(savedInstanceState)
//        if (arguments != null) {
////            type = arguments.getSerializable(Const.TYPE) as EnumData.AdsType
//        }
//    }
//
//    override fun getLayoutResourceId(): Int {
//        return R.layout.fragment_play
//    }
//
//    override fun initializeView(container: View?) {
//
//    }
//
//    override fun init() {
//
//        if (PreferenceUtil.getDefaultPreference(activity).get(Const.PLAY_GUIDE, true)) {
//            val intent = Intent(activity, EventGuideAlertActivity::class.java)
//            intent.putExtra(Const.KEY, Const.PLAY_GUIDE)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
////        mLayoutManager = LinearLayoutManager(activity!!)
//        mGridLayoutManager = androidx.recyclerview.widget.GridLayoutManager(activity, 2)
//        recycler_play.layoutManager = mGridLayoutManager
////        recycler_play.layoutManager = mLayoutManager
//        mAdapter = PlayAdapter(activity!!)
//        recycler_play.adapter = mAdapter
//        if (recycler_play.itemDecorationCount == 0) {
//            recycler_play.addItemDecoration(CustomItemOffsetDecoration(activity!!, R.dimen.height_40))
//        }
//
//        image_play_back.setOnClickListener {
//            activity?.onBackPressed()
//        }
//
//        recycler_play.addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
//
//            internal var pastVisibleItems: Int = 0
//            internal var visibleItemCount: Int = 0
//            internal var totalItemCount: Int = 0
//
//            override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
//
//                super.onScrolled(recyclerView, dx, dy)
////                visibleItemCount = mLayoutManager!!.childCount
////                totalItemCount = mLayoutManager!!.itemCount
////                pastVisibleItems = mLayoutManager!!.findFirstVisibleItemPosition()
//                visibleItemCount = mGridLayoutManager!!.childCount
//                totalItemCount = mGridLayoutManager!!.itemCount
//                pastVisibleItems = mGridLayoutManager!!.findFirstVisibleItemPosition()
//                if (!mLockListView) {
//                    if (totalItemCount < mTotalCount && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
//                        mPage++
//                        listCall(mPage)
//                    }
//                }
//            }
//        })
//
//        mAdapter!!.setOnItemClickListener(object : PlayAdapter.OnItemClickListener {
//
//            private val MIN_CLICK_INTERVAL: Long = 1000 //in millis
//            private var lastClickTime: Long = 0
//
//            override fun onItemClick(position: Int) {
//
//                val currentTime = SystemClock.elapsedRealtime()
//                if (currentTime - lastClickTime > MIN_CLICK_INTERVAL) {
//                    lastClickTime = currentTime
//
//                    if (position < 0) {
//                        return
//                    }
//
//                    val item = mAdapter!!.getItem(position)
//
//                    mSelectPos = position
//                    mSelectEvent = item
//
//                    if (item.winAnnounceType.equals(EventType.WinAnnounceType.special.name)) {
//                        val currentMillis = System.currentTimeMillis()
//                        val winAnnounceDate = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.winAnnounceDate).time
//                        if (currentMillis > winAnnounceDate) {
//
//                            val intent = Intent(activity, EventImpressionActivity::class.java)
//                            intent.putExtra(Const.DATA, item)
//                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                            activity!!.startActivityForResult(intent, Const.REQ_DETAIL)
//                            return
//                        }
//                    } else if (item.winAnnounceType.equals(EventType.WinAnnounceType.limit.name)) {
//                        if (item.joinCount!! >= item.maxJoinCount!!) {
//                            val intent = Intent(activity, EventImpressionActivity::class.java)
//                            intent.putExtra(Const.DATA, item)
//                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                            activity!!.startActivityForResult(intent, Const.REQ_DETAIL)
//                            return
//                        }
//                    } else if (item.winAnnounceType.equals(EventType.WinAnnounceType.immediately.name)) {
//                        if (item.winnerCount!! >= item.totalGiftCount!!) {
//                            val intent = Intent(activity, EventImpressionActivity::class.java)
//                            intent.putExtra(Const.DATA, item)
//                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                            activity!!.startActivityForResult(intent, Const.REQ_DETAIL)
//                            return
//                        }
//                    }
//
//                    if(!PplusCommonUtil.loginCheck(activity!!)){
//                        return
//                    }
//
//                    if (item.primaryType.equals(EventType.PrimaryType.move.name)) {
//                        val intent = Intent(activity, EventMoveDetailActivity::class.java)
//                        intent.putExtra(Const.DATA, item)
//                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                        activity!!.startActivityForResult(intent, Const.REQ_EVENT_DETAIL)
//                    } else {
//                        if (StringUtils.isNotEmpty(item.eventLink)) {
//                            val intent = Intent(activity, EventDetailActivity::class.java)
//                            intent.putExtra(Const.DATA, item)
//                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                            activity!!.startActivityForResult(intent, Const.REQ_EVENT_DETAIL)
//                        } else {
//                            if (item.primaryType.equals(EventType.PrimaryType.insert.name)) {
//
////                                if (parentActivity is AppMainActivity) {
////                                    (parentActivity as AppMainActivity).setPadFragment(Const.PAD, item.virtualNumber)
////                                }
//
//
//                                val intent = Intent(activity, PadActivity::class.java)
////                                val intent = Intent(activity, AppMainActivity2::class.java)
//                                intent.putExtra(Const.KEY, Const.PAD)
//                                intent.putExtra(Const.NUMBER, item.virtualNumber)
//                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                                startActivity(intent)
////                                activity!!.overridePendingTransition(R.anim.flip_horizontal_in, R.anim.flip_horizontal_out)
//                            } else if (item.primaryType.equals(EventType.PrimaryType.join.name)) {
//                                joinEvent(item)
//                            } else if (item.primaryType.equals(EventType.PrimaryType.goodluck.name)) {
//                                if (item.reward != null && item.reward!! < 0) {
//
//                                    if (LoginInfoManager.getInstance().user.totalBol >= Math.abs(item.reward!!)) {
//                                        val intent = Intent(activity, PlayAlertActivity::class.java)
//                                        intent.putExtra(Const.DATA, item)
//                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                                        activity!!.startActivityForResult(intent, Const.REQ_JOIN_ALERT)
//                                    } else {
//                                        val intent = Intent(activity, BolChargeAlertActivity::class.java)
//                                        intent.putExtra(Const.EVENT, item)
//                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                                        startActivity(intent)
//                                    }
//                                } else {
//                                    joinEvent(item)
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        })
//
//        swipe_refresh_play.setOnRefreshListener(object : androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener {
//            override fun onRefresh() {
//                getCount()
//                swipe_refresh_play.isRefreshing = false
//            }
//        })
//
////        text_play_retention_bol.setOnClickListener {
////            val intent = Intent(activity, PointConfigActivity::class.java)
////            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
////            startActivity(intent)
////        }
////
////        setSelect(text_play_first_come, text_play_real_time, text_play_after)
////        mSearch = EventType.WinAnnounceType.limit
////        getBol()
//
//        text_play_retention_bol.setOnClickListener {
//            if (LoginInfoManager.getInstance().isMember) {
//                val intent = Intent(activity, BolConfigActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                startActivity(intent)
//            }else{
//                val intent = Intent(activity, SnsLoginActivity::class.java)
//                activity?.startActivityForResult(intent, Const.REQ_SIGN_IN)
//            }
//
//        }
//
//        if (LoginInfoManager.getInstance().isMember) {
//            setRetentionBol()
//            text_play_retention_bol?.setBackgroundResource(android.R.color.transparent)
//        }else{
//            text_play_retention_bol?.setText(R.string.word_login_join2)
//            text_play_retention_bol?.setBackgroundResource(R.drawable.underbar_579ffb_ffffff)
//        }
//        winListCall()
//        getCount()
//
//    }
//
//    private fun setRetentionBol() {
//        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
//
//            override fun reload() {
//
//                text_play_retention_bol?.text = PplusCommonUtil.fromHtml(getString(R.string.html_cash_unit2, FormatUtil.getMoneyType(LoginInfoManager.getInstance().user.totalBol.toString())))
//            }
//        })
//    }
//
//    private inner class CustomItemOffsetDecoration(private val mItemOffset: Int) : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {
//
//        constructor(context: Context, @DimenRes lastOffsetId: Int) : this(context.resources.getDimensionPixelSize(lastOffsetId)) {}
//
//        override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
//
//            super.getItemOffsets(outRect, view, parent, state)
//
//            val position = parent.getChildAdapterPosition(view)
//            if (position <= 1) {
//                outRect.set(0, mItemOffset, 0, mItemOffset)
//            } else {
//                outRect.set(0, 0, 0, mItemOffset)
//            }
//        }
//    }
//
//    fun joinEvent(event: Event) {
//        val params = HashMap<String, String>()
//        params["no"] = event.no.toString()
//        showProgress("")
//
//        ApiBuilder.create().joinEvent(params).setCallback(object : PplusCallback<NewResultResponse<EventResult>> {
//            override fun onResponse(call: Call<NewResultResponse<EventResult>>?, response: NewResultResponse<EventResult>?) {
//
//                hideProgress()
//                if (!isAdded) {
//                    return
//                }
//
//                if (response!!.data != null) {
//                    if (event.winAnnounceType.equals(EventType.WinAnnounceType.immediately.name)) {
//                        val mLoading = EventLoadingView()
//                        mLoading.isCancelable = false
//                        mLoading.setText(getString(R.string.msg_checking_event_result))
//                        val delayTime = 2000L
//                        mLoading.isCancelable = false
//                        try {
//                            mLoading.show(parentFragmentManager, "")
//                        } catch (e: Exception) {
//
//                        }
//
//                        val handler = Handler()
//                        handler.postDelayed(Runnable {
//
//                            try {
//                                mLoading.dismiss()
//                            } catch (e: Exception) {
//
//                            }
//
//                            val intent = Intent(activity, EventResultActivity::class.java)
//                            intent.putExtra(Const.EVENT_RESULT, response.data)
//                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                            activity!!.startActivityForResult(intent, Const.REQ_RESULT)
//                        }, delayTime)
//
//                    } else {
//                        val intent = Intent(activity, EventResultActivity::class.java)
//                        intent.putExtra(Const.EVENT_RESULT, response.data)
//                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                        activity!!.startActivityForResult(intent, Const.REQ_RESULT)
//                    }
//                }
//
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<EventResult>>?, t: Throwable?, response: NewResultResponse<EventResult>?) {
//
//                hideProgress()
//                if (!isAdded) {
//                    return
//                }
//
//                if (response != null) {
//                    PplusCommonUtil.showEventAlert(activity!!, response.resultCode, event)
//                    getEvent()
//                }
//
//            }
//        }).build().call()
//    }
//
//    private fun winListCall() {
////        text_play_win_list.visibility = View.GONE
//        val params = HashMap<String, String>()
//        params["filter"] = EventType.PrimaryType.goodluck.name
//        params["pg"] = "1"
//        params["sz"] = "20"
//
//        ApiBuilder.create().getWinList(params).setCallback(object : PplusCallback<NewResultResponse<EventWin>> {
//
//            override fun onResponse(call: Call<NewResultResponse<EventWin>>, response: NewResultResponse<EventWin>) {
//
//                if (!isAdded) {
//                    return
//                }
//
//                try {
//                    val eventWinList = response.datas
//
//                    if (eventWinList.isEmpty()) {
//                        text_play_win_list.visibility = View.GONE
//                    } else {
//                        text_play_win_list.visibility = View.VISIBLE
//                    }
//
//                    val stringBuilder = StringBuilder()
//                    stringBuilder.append("   ")
//                    for (eventWin in eventWinList) {
//
//                        val date = SimpleDateFormat(PPLUS_DATE_FORMAT.pattern).parse(eventWin.winDate)
//                        val time = SimpleDateFormat("HH:mm").format(date)
//
//                        val text = getString(R.string.format_goodluck_win_list, time, eventWin.user!!.nickname, eventWin.gift!!.title)
//                        stringBuilder.append(text)
//                        stringBuilder.append("   ")
//                    }
//                    text_play_win_list.setSingleLine()
//                    text_play_win_list.text = stringBuilder.toString()
//                    text_play_win_list.isSelected = true
//                } catch (e: Exception) {
//
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<EventWin>>, t: Throwable, response: NewResultResponse<EventWin>) {
//            }
//        }).build().call()
//    }
//
//    private fun getCount() {
//        val params = HashMap<String, String>()
//        params["filter"] = EventType.PrimaryType.goodluck.name
//        if (mSearch != null) {
//            params["search"] = mSearch!!.name
//        }
//        params["appType"] = Const.APP_TYPE
////        showProgress("")
//        ApiBuilder.create().getEventCount(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {
//
//            override fun onResponse(call: Call<NewResultResponse<Int>>, response: NewResultResponse<Int>) {
////                hideProgress()
//                if (!isAdded) {
//                    return
//                }
//                mTotalCount = response.data
//
//                if (mTotalCount == 0) {
//                    layout_play_not_exist.visibility = View.VISIBLE
//                } else {
//                    layout_play_not_exist.visibility = View.GONE
//                }
//
//                mPage = 1
//                mAdapter?.clear()
//                listCall(mPage)
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Int>>, t: Throwable, response: NewResultResponse<Int>) {
////                hideProgress()
//            }
//        }).build().call()
//    }
//
//    private fun listCall(page: Int) {
//        val params = HashMap<String, String>()
//        params["filter"] = EventType.PrimaryType.goodluck.name
//        if (mSearch != null) {
//            params["search"] = mSearch!!.name
//        }
//        params["pg"] = page.toString()
//        params["appType"] = Const.APP_TYPE
////        showProgress("")
//        mLockListView = true
//        ApiBuilder.create().getEventList(params).setCallback(object : PplusCallback<NewResultResponse<Event>> {
//
//            override fun onResponse(call: Call<NewResultResponse<Event>>, response: NewResultResponse<Event>) {
//
////                hideProgress()
//                if (!isAdded) {
//                    return
//                }
//
//                mLockListView = false
//
//                mAdapter?.addAll(response.datas)
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Event>>, t: Throwable, response: NewResultResponse<Event>) {
////                hideProgress()
//            }
//        }).build().call()
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (!isAdded) {
//            return
//        }
//        when (requestCode) {
//            Const.REQ_SIGN_IN->{
//                if(resultCode == Activity.RESULT_OK){
//                    if (LoginInfoManager.getInstance().isMember) {
//                        setRetentionBol()
//                        text_play_retention_bol?.setBackgroundResource(android.R.color.transparent)
//                    }else{
//                        text_play_retention_bol?.setText(R.string.word_login_join2)
//                        text_play_retention_bol?.setBackgroundResource(R.drawable.underbar_579ffb_ffffff)
//                    }
//                }
//            }
//            Const.REQ_JOIN_ALERT -> {
//                if (resultCode == Activity.RESULT_OK) {
//                    if (data != null) {
//                        val event = data.getParcelableExtra<Event>(Const.DATA)
//                        if (LoginInfoManager.getInstance().user.totalBol >= Math.abs(event.reward!!)) {
//                            joinEvent(event)
//                        } else {
//                            val intent = Intent(activity, BolChargeAlertActivity::class.java)
//                            intent.putExtra(Const.EVENT, event)
//                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                            startActivity(intent)
//                        }
//                    }
//
//                } else {
//                    getEvent()
//                }
//            }
//            Const.REQ_VERIFICATION -> {
//                if (resultCode == Activity.RESULT_OK) {
//                    if (data != null) {
//                        val verifiedData = data.getParcelableExtra<User>(Const.DATA)
//
//                        val user = LoginInfoManager.getInstance().user
//                        user.name = verifiedData.name
//                        user.birthday = verifiedData.birthday
//                        user.mobile = verifiedData.mobile
//                        user.gender = verifiedData.gender
//                        user.verification = verifiedData.verification
//                        showProgress("")
//                        ApiBuilder.create().updateUser(user).setCallback(object : PplusCallback<NewResultResponse<User>> {
//
//                            override fun onResponse(call: Call<NewResultResponse<User>>, response: NewResultResponse<User>) {
//                                hideProgress()
//                                showProgress("")
//                                PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
//                                    override fun reload() {
//                                        hideProgress()
//                                        showAlert(R.string.msg_verified)
//                                    }
//                                })
//                            }
//
//                            override fun onFailure(call: Call<NewResultResponse<User>>, t: Throwable, response: NewResultResponse<User>) {
//                                hideProgress()
//                            }
//                        }).build().call()
//                    }
//                }
//            }
//            Const.REQ_RESULT -> {
//                if (resultCode == Activity.RESULT_OK) {
//                    if (data != null) {
//                        val event = data.getParcelableExtra<Event>(Const.DATA)
//                        showProgress("")
//                        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
//                            override fun reload() {
//                                hideProgress()
//                                if (!isAdded) {
//                                    return
//                                }
////                                parentActivity.text_mobile_gift_retention_bol.text = PplusCommonUtil.fromHtml(getString(R.string.html_cash_unit2, FormatUtil.getMoneyType(LoginInfoManager.getInstance().user.totalBol.toString())))
//
//                                if (LoginInfoManager.getInstance().user.totalBol >= Math.abs(event.reward!!)) {
//                                    joinEvent(event)
//                                } else {
//                                    val intent = Intent(activity, BolChargeAlertActivity::class.java)
//                                    intent.putExtra(Const.EVENT, event)
//                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                                    startActivity(intent)
//                                }
//                            }
//                        })
//                    } else {
//                        setRetentionBol()
//                        getEvent()
//                    }
//                } else {
//                    setRetentionBol()
//                    getEvent()
//                }
//            }
//            else -> {
//                getEvent()
//            }
//        }
//
//
//    }
//
//    private fun getEvent() {
//        if (mSelectEvent == null) {
//            return
//        }
//        val params = HashMap<String, String>()
//        params["no"] = mSelectEvent!!.no.toString()
////        showProgress("")
//        ApiBuilder.create().getEvent(params).setCallback(object : PplusCallback<NewResultResponse<Event>> {
//            override fun onResponse(call: Call<NewResultResponse<Event>>?, response: NewResultResponse<Event>?) {
////                hideProgress()
//                if (!isAdded) {
//                    return
//                }
//                if (response!!.data != null) {
//                    mAdapter!!.replaceData(mSelectPos, response.data)
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Event>>?, t: Throwable?, response: NewResultResponse<Event>?) {
////                hideProgress()
//            }
//        }).build().call()
//        mSelectEvent = null
//    }
//
//    override fun getPID(): String {
//        return "Main(Play_event)"
//    }
//
//    companion object {
//
//        fun newInstance(): PlayFragment {
//
//            val fragment = PlayFragment()
//            val args = Bundle()
////            args.putSerializable(Const.TYPE, type)
//            fragment.arguments = args
//            return fragment
//        }
//    }
//
//}// Required empty public constructor
