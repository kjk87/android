//package com.pplus.prnumberuser.apps.main.ui
//
//
//import android.content.Context
//import android.content.Intent
//import android.graphics.Rect
//import android.os.Bundle
//import android.os.Handler
//import android.os.SystemClock
//import android.support.annotation.DimenRes
//import android.support.v4.app.Fragment
//import android.support.v4.widget.SwipeRefreshLayout
//import android.support.v7.widget.LinearLayoutManager
//import android.support.v7.widget.OrientationHelper
//import android.support.v7.widget.RecyclerView
//import android.view.View
//import com.pplus.utils.part.pref.PreferenceUtil
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.utils.part.utils.time.DateFormatUtils
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
//import com.pplus.prnumberuser.apps.event.data.EventAdapter
//import com.pplus.prnumberuser.apps.event.data.EventLoadingView
//import com.pplus.prnumberuser.apps.event.ui.*
//import com.pplus.prnumberuser.core.code.common.EventType
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Event
//import com.pplus.prnumberuser.core.network.model.dto.EventResult
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.fragment_pr_event.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.util.*
//
//
///**
// * A simple [Fragment] subclass.
// * Use the [PREventFragment.newInstance] factory method to
// * create an instance of this fragment.
// */
//class PREventFragment : BaseFragment<BaseActivity>() {
//
//    // TODO: Rename and change types of parameters
//
//
//    private var mPage: Int = 0
//    private var mTotalCount = 0
//    private var mLayoutManager: LinearLayoutManager? = null
//    private var mLockListView = true
//    private var mAdapter: EventAdapter? = null
//    private var mSelectPos: Int = -1
//    private var mSelectEvent: Event? = null
//    private var mGroupNo = 2
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
//        return R.layout.fragment_pr_event
//    }
//
//    override fun initializeView(container: View?) {
//
//    }
//
//    override fun init() {
//
//        if (PreferenceUtil.getDefaultPreference(activity).get(Const.EVENT_GUIDE, true)) {
//            val intent = Intent(activity, EventGuideAlertActivity::class.java)
//            intent.putExtra(Const.KEY, Const.EVENT_GUIDE)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
//        mLayoutManager = LinearLayoutManager(activity!!, OrientationHelper.VERTICAL, false)
//        recycler_pr_event.layoutManager = mLayoutManager
//        mAdapter = EventAdapter(activity!!, true)
//        recycler_pr_event.adapter = mAdapter
//        recycler_pr_event.addItemDecoration(CustomItemOffsetDecoration(activity!!, R.dimen.height_30, R.dimen.height_30))
//
//        recycler_pr_event.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//
//            internal var pastVisibleItems: Int = 0
//            internal var visibleItemCount: Int = 0
//            internal var totalItemCount: Int = 0
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//
//                super.onScrolled(recyclerView, dx, dy)
//                visibleItemCount = mLayoutManager!!.childCount
//                totalItemCount = mLayoutManager!!.itemCount
//                pastVisibleItems = mLayoutManager!!.findFirstVisibleItemPosition()
//                if (!mLockListView) {
//                    if (totalItemCount < mTotalCount && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
//                        mPage++
//                        listCall(mPage)
//                    }
//                }
//            }
//        })
//
//        layout_pr_event_random_number.setOnClickListener {
//            val intent = Intent(activity, PadActivity::class.java)
//            intent.putExtra(Const.KEY, Const.PAD)
////            intent.putExtra(Const.NUMBER, item.virtualNumber)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//            activity!!.overridePendingTransition(R.anim.flip_horizontal_in, R.anim.flip_horizontal_out)
//        }
//
////        recycler_event.addOnScrollListener(RecyclerScaleScrollListener(parentActivity.image_event_pad))
//
//        mAdapter!!.setOnItemClickListener(object : EventAdapter.OnItemClickListener {
//
//            private val MIN_CLICK_INTERVAL: Long = 1000 //in millis
//            private var lastClickTime: Long = 0
//
//            override fun onItemClick(position: Int) {
//
//                val currentTime = SystemClock.elapsedRealtime();
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
//                    }
//
//                    if (item.primaryType.equals(EventType.PrimaryType.move.name)) {
//                        val intent = Intent(activity, EventMoveDetailActivity::class.java)
//                        intent.putExtra(Const.DATA, item)
//                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                        activity!!.startActivityForResult(intent, Const.REQ_EVENT_DETAIL)
//                    } else {
//                        if (StringUtils.isNotEmpty(item.contents)) {
//                            val intent = Intent(activity, EventDetailActivity::class.java)
//                            intent.putExtra(Const.DATA, item)
//                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                            activity!!.startActivityForResult(intent, Const.REQ_EVENT_DETAIL)
//                        } else {
//                            if (item.primaryType.equals(EventType.PrimaryType.insert.name)) {
//                                val intent = Intent(activity, PadActivity::class.java)
////                                val intent = Intent(activity, AppMainActivity2::class.java)
//                                intent.putExtra(Const.KEY, Const.PAD)
//                                intent.putExtra(Const.NUMBER, item.virtualNumber)
//                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                                startActivity(intent)
//                                activity!!.overridePendingTransition(R.anim.flip_horizontal_in, R.anim.flip_horizontal_out)
//
//                            } else if (item.primaryType.equals(EventType.PrimaryType.join.name)) {
//                                joinEvent(item)
//                            }
//                        }
//                    }
//                }
//
//            }
//        })
//
////        text_event_bol_play.setOnClickListener {
////            val intent = Intent(activity, PlayActivity::class.java)
////            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
////            activity?.startActivityForResult(intent, Const.REQ_CASH_CHANGE)
////        }
//
////        text_pr_event_addpopcorn.setOnClickListener {
////            val pPlusPermission = PPlusPermission(activity)
////            pPlusPermission.addPermission(Permission.PERMISSION_KEY.STORAGE)
////            pPlusPermission.setPermissionListener(object : PermissionListener {
////
////                override fun onPermissionGranted() {
////
////                    IgawCommon.setUserId(activity, LoginInfoManager.getInstance().user.no.toString())
////                    val optionMap = HashMap<String, Any>()
////                    optionMap.put(ApStyleManager.CustomStyle.OFFERWALL_TITLE_TEXT, getString(R.string.word_bol_charge_station))
////                    optionMap.put(ApStyleManager.CustomStyle.OFFERWALL_THEME_COLOR, Color.parseColor("#579ffb"))
////                    optionMap.put(ApStyleManager.CustomStyle.TOP_BAR_BG_COLOR, Color.parseColor("#579ffb"))
////                    optionMap.put(ApStyleManager.CustomStyle.BOTTOM_BAR_BG_COLOR, Color.parseColor("#579ffb"))
////                    ApStyleManager.setCustomOfferwallStyle(optionMap)
////                    IgawAdpopcornExtension.setCashRewardAppFlag(activity, true)
////                    IgawAdpopcorn.openOfferWall(activity)
////                    IgawAdbrix.retention("Main_event_luckybol_a_charging")
////                    IgawAdbrix.firstTimeExperience("Main_event_luckybol_a_charging")
////                }
////
////                override fun onPermissionDenied(deniedPermissions: ArrayList<String>) {
////
////                }
////            })
////            pPlusPermission.checkPermission()
////        }
//
////        text_pr_event_win_history.setOnClickListener {
////            val intent = Intent(activity, MyWinHistoryActivity::class.java)
////            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
////            activity?.startActivityForResult(intent, Const.REQ_CASH_CHANGE)
////        }
//
//        swipe_refresh_pr_event.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
//            override fun onRefresh() {
//                getCount()
//                swipe_refresh_pr_event.isRefreshing = false
//            }
//        })
//
////        text_pr_event_retention_bol.setOnClickListener {
////            val intent = Intent(activity, PointConfigActivity::class.java)
////            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
////            startActivity(intent)
////        }
//
//        getRandomPrnumber()
////        setRetentionBol()
//        mGroupNo = 2
//        getCount()
//    }
//
//    private inner class CustomItemOffsetDecoration(private val mTopOffset: Int, private val mItemOffset: Int) : RecyclerView.ItemDecoration() {
//
//        constructor(context: Context, @DimenRes topOffsetId: Int, @DimenRes lastOffsetId: Int) : this(context.resources.getDimensionPixelSize(topOffsetId), context.resources.getDimensionPixelSize(lastOffsetId)) {}
//
//        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
//
//            super.getItemOffsets(outRect, view, parent, state)
//
//            val position = parent.getChildAdapterPosition(view)
//            if (position == 0) {
//                outRect.set(0, mTopOffset, 0, mItemOffset)
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
//                        var delayTime = 2000L
//                        mLoading.isCancelable = false
//                        try {
//                            mLoading.show(fragmentManager, "")
//                        } catch (e: Exception) {
//
//                        }
//
//                        val handler = Handler()
//                        handler.postDelayed({
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
//                }
//
//            }
//        }).build().call()
//    }
//
////    private fun getGroupAll(){
////        ApiBuilder.create().eventGroupAll.setCallback(object  : PplusCallback<NewResultResponse<EventGroup>>{
////            override fun onResponse(call: Call<NewResultResponse<EventGroup>>?, response: NewResultResponse<EventGroup>?) {
////                if(response!!.datas.isNotEmpty()){
////                    header_main_event.visibility = View.VISIBLE
////                }else{
////                    header_main_event.visibility = View.GONE
////                }
////            }
////
////            override fun onFailure(call: Call<NewResultResponse<EventGroup>>?, t: Throwable?, response: NewResultResponse<EventGroup>?) {
////                header_main_event.visibility = View.GONE
////            }
////        }).build().call()
////    }
//
//    private fun getCount() {
//        val params = HashMap<String, String>()
//        params["no"] = mGroupNo.toString()
//        ApiBuilder.create().getEventCountByGroup(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {
//
//            override fun onResponse(call: Call<NewResultResponse<Int>>, response: NewResultResponse<Int>) {
//                if (!isAdded) {
//                    return
//                }
//
//                mTotalCount = response.data
//
//                if (mTotalCount == 0) {
//                    layout_pr_event_not_exist.visibility = View.VISIBLE
//                } else {
//                    layout_pr_event_not_exist.visibility = View.GONE
//                }
//
//                mPage = 1
//                mAdapter?.clear()
//                listCall(mPage)
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Int>>, t: Throwable, response: NewResultResponse<Int>) {
//
//                if (!isAdded) {
//                    return
//                }
//            }
//        }).build().call()
//    }
//
//    private fun listCall(page: Int) {
//        val params = HashMap<String, String>()
//        params["no"] = mGroupNo.toString()
//        params["pg"] = page.toString()
//        mLockListView = true
//        ApiBuilder.create().getEventListByGroup(params).setCallback(object : PplusCallback<NewResultResponse<Event>> {
//
//            override fun onResponse(call: Call<NewResultResponse<Event>>, response: NewResultResponse<Event>) {
//
//                if (!isAdded) {
//                    return
//                }
//
//                mLockListView = false
//                mAdapter?.addAll(response.datas)
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Event>>, t: Throwable, response: NewResultResponse<Event>) {
//
//                if (!isAdded) {
//                    return
//                }
//
//            }
//        }).build().call()
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        setRetentionBol()
//        getEvent()
//    }
//
//    private fun setRetentionBol() {
//        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
//
//            override fun reload() {
//
////                text_pr_event_retention_bol?.text = PplusCommonUtil.fromHtml(getString(R.string.html_cash_unit2, FormatUtil.getMoneyType(LoginInfoManager.getInstance().user.totalBol.toString())))
//            }
//        })
//    }
//
//    private fun getRandomPrnumber(){
//        ApiBuilder.create().randomPrnumber.setCallback(object : PplusCallback<NewResultResponse<String>>{
//            override fun onResponse(call: Call<NewResultResponse<String>>?, response: NewResultResponse<String>?) {
//                if(response != null){
//                    val numberList = response.datas
//                    text_pr_event_random_number.setTexts(numberList.toTypedArray())
//                }
//
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<String>>?, t: Throwable?, response: NewResultResponse<String>?) {
//
//            }
//        }).build().call()
//    }
//
//    private fun getEvent() {
//        if (mSelectEvent == null) {
//            return
//        }
//
//        val params = HashMap<String, String>()
//        params["no"] = mSelectEvent!!.no.toString()
//        showProgress("")
//        ApiBuilder.create().getEvent(params).setCallback(object : PplusCallback<NewResultResponse<Event>> {
//            override fun onResponse(call: Call<NewResultResponse<Event>>?, response: NewResultResponse<Event>?) {
//                hideProgress()
//                if (!isAdded) {
//                    return
//                }
//
//                if (response!!.data != null) {
//                    mAdapter!!.replaceData(mSelectPos, response.data)
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Event>>?, t: Throwable?, response: NewResultResponse<Event>?) {
//
//                hideProgress()
//                if (!isAdded) {
//                    return
//                }
//            }
//        }).build().call()
//        mSelectEvent = null
//    }
//
//    override fun getPID(): String {
//        return "Main_pr_eventlist"
//    }
//
//    companion object {
//
//        fun newInstance(): PREventFragment {
//
//            val fragment = PREventFragment()
//            val args = Bundle()
////            args.putSerializable(Const.TYPE, type)
//            fragment.arguments = args
//            return fragment
//        }
//    }
//
//}// Required empty public constructor
