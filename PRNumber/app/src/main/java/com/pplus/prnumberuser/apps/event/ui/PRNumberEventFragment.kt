//package com.pplus.prnumberuser.apps.event.ui
//
//
//import android.app.Activity.RESULT_OK
//import android.content.Context
//import android.content.Intent
//import android.graphics.Rect
//import android.os.Bundle
//import android.os.Handler
//import android.os.SystemClock
//import android.view.View
//import androidx.annotation.DimenRes
//import androidx.fragment.app.Fragment
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.pplus.networks.common.PplusCallback
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
//import com.pplus.prnumberuser.apps.common.ui.custom.BottomItemOffsetDecoration
//import com.pplus.prnumberuser.apps.event.data.EventAdapter
//import com.pplus.prnumberuser.apps.event.data.EventLoadingView
//import com.pplus.prnumberuser.apps.main.ui.PadActivity
//import com.pplus.prnumberuser.apps.page.ui.Alert3rdPartyInfoTermsActivity
//import com.pplus.prnumberuser.apps.recommend.ui.RecommendActivity
//import com.pplus.prnumberuser.core.code.common.EventType
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Event
//import com.pplus.prnumberuser.core.network.model.dto.EventResult
//import com.pplus.prnumberuser.core.network.model.dto.Plus
//import com.pplus.prnumberuser.core.network.model.dto.User
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.utils.part.utils.time.DateFormatUtils
//import kotlinx.android.synthetic.main.fragment_pr_number_event.*
//import retrofit2.Call
//import java.util.*
//
//
///**
// * A simple [Fragment] subclass.
// * Use the [PRNumberEventFragment.newInstance] factory method to
// * create an instance of this fragment.
// */
//class PRNumberEventFragment : BaseFragment<BaseActivity>() {
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
//        return R.layout.fragment_pr_number_event
//    }
//
//    override fun initializeView(container: View?) {
//
//    }
//
//    override fun init() {
//
//        layout_pr_number_event_pad.setOnClickListener {
//            val intent = Intent(activity, PadActivity::class.java)
//            intent.putExtra(Const.KEY, Const.PAD)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
//        mLayoutManager = LinearLayoutManager(activity!!)
//        recycler_pr_number_event.layoutManager = mLayoutManager
//        mAdapter = EventAdapter(activity!!, true)
//        recycler_pr_number_event.adapter = mAdapter
////        recycler_pr_number_event.addItemDecoration(CustomItemOffsetDecoration(activity!!, R.dimen.height_30, R.dimen.height_30))
//        recycler_pr_number_event.addItemDecoration(BottomItemOffsetDecoration(activity!!, R.dimen.height_30))
//
////        recycler_pr_number_event.addOnScrollListener(RecyclerScaleScrollListener(layout_pr_number_event_get_ticket))
//        recycler_pr_number_event.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
//        image_pr_number_event_back.setOnClickListener {
//            activity?.onBackPressed()
//        }
//
//        layout_pr_number_event_get_ticket.setOnClickListener {
//            if (!PplusCommonUtil.loginCheck(activity!!)) {
//                return@setOnClickListener
//            }
//
//            val intent = Intent(activity, RecommendActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
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
//                    if (!PplusCommonUtil.loginCheck(activity!!)) {
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
//                                val intent = Intent(activity, PadActivity::class.java)
////                                val intent = Intent(activity, AppMainActivity2::class.java)
//                                intent.putExtra(Const.KEY, Const.PAD)
//                                intent.putExtra(Const.NUMBER, item.virtualNumber)
//                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                                startActivity(intent)
////                                activity?.overridePendingTransition(R.anim.flip_horizontal_in, R.anim.flip_horizontal_out)
//
//                            } else if (item.primaryType.equals(EventType.PrimaryType.join.name)) {
//
//                                if(item.isPlus != null && item.isPlus!! && item.pageSeqNo != null){
//                                    val params = HashMap<String, String>()
//                                    params["pageSeqNo"] = item.pageSeqNo.toString()
//                                    showProgress("")
//                                    ApiBuilder.create().getOnlyPlus(params).setCallback(object : PplusCallback<NewResultResponse<Plus>> {
//                                        override fun onResponse(call: Call<NewResultResponse<Plus>>?, response: NewResultResponse<Plus>?) {
//                                            hideProgress()
//                                            if (!isAdded) {
//                                                return
//                                            }
//                                            if(response?.data == null || response.data.agreement == null || !response.data.agreement!!){
//                                                val intent = Intent(activity, Alert3rdPartyInfoTermsActivity::class.java)
//                                                intent.putExtra(Const.EVENT, item)
//                                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                                                activity?.startActivityForResult(intent, Const.REQ_EVENT_AGREE)
//                                            }
//                                        }
//
//                                        override fun onFailure(call: Call<NewResultResponse<Plus>>?, t: Throwable?, response: NewResultResponse<Plus>?) {
//                                            hideProgress()
//                                        }
//                                    }).build().call()
//                                }else{
//                                    joinEvent(item)
//                                }
//
//                            }
//                        }
//                    }
//                }
//
//            }
//        })
//
////        swipe_refresh_pr_number_event.setOnRefreshListener(object : androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener {
////            override fun onRefresh() {
////                getCount()
////                swipe_refresh_pr_number_event.isRefreshing = false
////            }
////        })
//
//        if (LoginInfoManager.getInstance().isMember) {
//            setRetentionBol()
//        } else {
//            text_pr_number_event_event_ticket?.text = "0"
//        }
//        mGroupNo = 2
//        getCount()
//    }
//
//    private inner class CustomItemOffsetDecoration(private val mTopOffset: Int, private val mItemOffset: Int) : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {
//
//        constructor(context: Context, @DimenRes topOffsetId: Int, @DimenRes lastOffsetId: Int) : this(context.resources.getDimensionPixelSize(topOffsetId), context.resources.getDimensionPixelSize(lastOffsetId)) {}
//
//        override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
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
//                            mLoading.show(parentFragmentManager, "")
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
//
//    private fun getCount() {
//        val params = HashMap<String, String>()
//        params["no"] = "2"
//        params["appType"] = Const.APP_TYPE
////        showProgress("")
//        ApiBuilder.create().getEventCountByGroup(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {
//
//            override fun onResponse(call: Call<NewResultResponse<Int>>, response: NewResultResponse<Int>) {
////                hideProgress()
//                if (!isAdded) {
//                    return
//                }
//
//                mTotalCount = response.data
//
////                if (mTotalCount == 0) {
////                    layout_pr_number_event_not_exist.visibility = View.VISIBLE
////                } else {
////                    layout_pr_number_event_not_exist.visibility = View.GONE
////                }
//
//                mPage = 1
//                mAdapter?.clear()
//                listCall(mPage)
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Int>>, t: Throwable, response: NewResultResponse<Int>) {
////                hideProgress()
//                if (!isAdded) {
//                    return
//                }
//            }
//        }).build().call()
//    }
//
//    private fun listCall(page: Int) {
//        val params = HashMap<String, String>()
//        params["no"] = "2"
//        params["pg"] = page.toString()
//        params["appType"] = Const.APP_TYPE
//        mLockListView = true
////        showProgress("")
//        ApiBuilder.create().getEventListByGroup(params).setCallback(object : PplusCallback<NewResultResponse<Event>> {
//
//            override fun onResponse(call: Call<NewResultResponse<Event>>, response: NewResultResponse<Event>) {
////                hideProgress()
//                if (!isAdded) {
//                    return
//                }
//
//                mLockListView = false
//                mAdapter?.addAll(response.datas)
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Event>>, t: Throwable, response: NewResultResponse<Event>) {
////                hideProgress()
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
//
//        when (requestCode) {
//            Const.REQ_SIGN_IN -> {
//                if (resultCode == RESULT_OK) {
//                    setRetentionBol()
//                }
//            }
//            Const.REQ_EVENT_AGREE -> {
//                if (resultCode == RESULT_OK && data != null) {
//
//                    val event = data.getParcelableExtra<Event>(Const.DATA)
//                    joinEvent(event)
//
//                }
//            }
//            Const.REQ_VERIFICATION -> {
//                if (resultCode == RESULT_OK) {
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
//            else -> {
//                if (LoginInfoManager.getInstance().isMember) {
//                    setRetentionBol()
//                }
//                getEvent()
//            }
//        }
//    }
//
//    private fun setRetentionBol() {
//        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
//
//            override fun reload() {
//                val ticketCount = LoginInfoManager.getInstance().user.eventTicketCount
//                if (ticketCount == null || ticketCount == 0) {
//                    text_pr_number_event_event_ticket?.text = "0"
//                } else if (ticketCount > 99) {
//                    text_pr_number_event_event_ticket?.text = "99+"
//                } else {
//                    text_pr_number_event_event_ticket?.text = ticketCount.toString()
//                }
//
////                text_main_pr_event_retention_bol?.text = PplusCommonUtil.fromHtml(getString(R.string.html_cash_unit22, FormatUtil.getMoneyType(LoginInfoManager.getInstance().user.totalBol.toString())))
//            }
//        })
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
//        return "Main(brand_event)"
//    }
//
//    companion object {
//
//        fun newInstance(): PRNumberEventFragment {
//
//            val fragment = PRNumberEventFragment()
//            val args = Bundle()
////            args.putSerializable(Const.TYPE, type)
//            fragment.arguments = args
//            return fragment
//        }
//    }
//
//}// Required empty public constructor
