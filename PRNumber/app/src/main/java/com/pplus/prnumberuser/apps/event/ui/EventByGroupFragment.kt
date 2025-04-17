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
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.utils.part.utils.time.DateFormatUtils
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
//import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
//import com.pplus.prnumberuser.apps.common.builder.data.AlertData
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
//import com.pplus.prnumberuser.apps.common.ui.custom.BottomItemOffsetDecoration
//import com.pplus.prnumberuser.apps.event.data.EventAdapter
//import com.pplus.prnumberuser.apps.event.data.EventLoadingView
//import com.pplus.prnumberuser.apps.main.ui.PadActivity
//import com.pplus.prnumberuser.apps.page.ui.AlertPlusEventTermsActivity
//import com.pplus.prnumberuser.apps.signup.ui.VerificationMeActivity
//import com.pplus.prnumberuser.core.code.common.EventType
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Event
//import com.pplus.prnumberuser.core.network.model.dto.EventResult
//import com.pplus.prnumberuser.core.network.model.dto.User
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.fragment_event_by_group.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.util.*
//
//
///**
// * A simple [Fragment] subclass.
// * Use the [EventByGroupFragment.newInstance] factory method to
// * create an instance of this fragment.
// */
//class EventByGroupFragment : BaseFragment<BaseActivity>() {
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
//    private var mGroupNo = 1
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//
//        super.onCreate(savedInstanceState)
//        if (arguments != null) {
//            mGroupNo = arguments!!.getInt(Const.GROUP)
//        }
//    }
//
//    override fun getLayoutResourceId(): Int {
//        return R.layout.fragment_event_by_group
//    }
//
//    override fun initializeView(container: View?) {
//
//    }
//
//    override fun init() {
//
//        mLayoutManager = LinearLayoutManager(activity!!)
//        recycler_event_by_group.layoutManager = mLayoutManager
//        mAdapter = EventAdapter(activity!!, true)
//        recycler_event_by_group.adapter = mAdapter
////        recycler_pr_number_event.addItemDecoration(CustomItemOffsetDecoration(activity!!, R.dimen.height_30, R.dimen.height_30))
//        recycler_event_by_group.addItemDecoration(BottomItemOffsetDecoration(activity!!, R.dimen.height_20))
//
////        recycler_event_by_group.addOnScrollListener(RecyclerScaleScrollListener(parentActivity.layout_main_event_floating))
//        recycler_event_by_group.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
//        if(mGroupNo == 1){
//            layout_event_by_group_banner.setBackgroundResource(R.drawable.img_event_bg_1)
//            image_event_by_group_banner.setImageResource(R.drawable.img_event_txt_1)
//        }else if(mGroupNo == 2){
//            layout_event_by_group_banner.setBackgroundResource(R.drawable.img_event_bg_2)
//            image_event_by_group_banner.setImageResource(R.drawable.img_event_txt_2)
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
//                    if(LoginInfoManager.getInstance().user.verification!!.media != "external"){
//                        val builder = AlertBuilder.Builder()
//                        builder.setTitle(getString(R.string.word_notice_alert))
//                        builder.addContents(AlertData.MessageData(getString(R.string.msg_verification_me_for_event1), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
//                        builder.addContents(AlertData.MessageData(getString(R.string.msg_move_verification), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
//                        builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
//                        builder.setOnAlertResultListener(object : OnAlertResultListener {
//                            override fun onCancel() {}
//                            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//                                when (event_alert) {
//                                    AlertBuilder.EVENT_ALERT.RIGHT -> {
//                                        val intent = Intent(activity, VerificationMeActivity::class.java)
//                                        intent.putExtra(Const.KEY, Const.VERIFICATION_ME)
//                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                                        activity?.startActivityForResult(intent, Const.REQ_VERIFICATION)
//                                    }
//                                }
//                            }
//                        }).builder().show(activity)
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
//                                val plusTerms = LoginInfoManager.getInstance().user.plusTerms
//
//                                if (item.pageSeqNo != null) {
//                                    if (plusTerms == null || !plusTerms) {
//                                        val intent = Intent(activity, AlertPlusEventTermsActivity::class.java)
//                                        intent.putExtra(Const.DATA, item)
//                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                                        activity?.startActivityForResult(intent, Const.REQ_EVENT_AGREE)
//                                    } else {
//                                        joinEvent(item)
//                                    }
//                                } else {
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
//        params["no"] = mGroupNo.toString()
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
//        params["no"] = mGroupNo.toString()
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
//                    val user = LoginInfoManager.getInstance().user
//                    user.plusTerms = true
//                    ApiBuilder.create().updateUser(user).setCallback(object : PplusCallback<NewResultResponse<User>> {
//
//                        override fun onResponse(call: Call<NewResultResponse<User>>, response: NewResultResponse<User>) {
//                            hideProgress()
//                            LoginInfoManager.getInstance().save()
//                            val event = data.getParcelableExtra<Event>(Const.DATA)
//                            joinEvent(event)
//                        }
//
//                        override fun onFailure(call: Call<NewResultResponse<User>>, t: Throwable, response: NewResultResponse<User>) {
//                            hideProgress()
//                        }
//                    }).build().call()
//
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
//        fun newInstance(group:Int): EventByGroupFragment {
//
//            val fragment = EventByGroupFragment()
//            val args = Bundle()
//            args.putSerializable(Const.GROUP, group)
//            fragment.arguments = args
//            return fragment
//        }
//    }
//
//}// Required empty public constructor
