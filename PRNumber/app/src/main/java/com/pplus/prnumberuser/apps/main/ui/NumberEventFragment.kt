//package com.pplus.prnumberuser.apps.main.ui
//
//
//import android.content.Context
//import android.content.Intent
//import android.graphics.Rect
//import android.graphics.Typeface
//import android.os.Bundle
//import android.os.SystemClock
//import android.view.View
//import android.widget.TextView
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.annotation.DimenRes
//import androidx.fragment.app.Fragment
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
//import com.pplus.networks.common.PplusCallback
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.bol.ui.BolConfigActivity
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
//import com.pplus.prnumberuser.apps.event.data.NumberEventAdapter
//import com.pplus.prnumberuser.apps.event.ui.EventGuideAlertActivity
//import com.pplus.prnumberuser.apps.event.ui.EventImpressionActivity
//import com.pplus.prnumberuser.apps.event.ui.NumberEventDetailActivity
//import com.pplus.prnumberuser.apps.signin.ui.SnsLoginActivity
//import com.pplus.prnumberuser.core.code.common.EventType
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Event
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.pref.PreferenceUtil
//import com.pplus.utils.part.utils.time.DateFormatUtils
//import kotlinx.android.synthetic.main.fragment_number_event.*
//import retrofit2.Call
//import java.util.*
//
//
///**
// * A simple [Fragment] subclass.
// * Use the [NumberEventFragment.newInstance] factory method to
// * create an instance of this fragment.
// */
//class NumberEventFragment : BaseFragment<BaseActivity>() {
//
//    // TODO: Rename and change types of parameters
//
//
//    private var mPage: Int = 0
//    private var mTotalCount = 0
//    private var mLayoutManager: LinearLayoutManager? = null
//    private var mLockListView = true
//    private var mAdapter: NumberEventAdapter? = null
//    private var mSelectPos: Int = 0
//    private var mSelectEvent: Event? = null
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
//        return R.layout.fragment_number_event
//    }
//
//    override fun initializeView(container: View?) {
//
//    }
//
//    override fun init() {
//
//        if (PreferenceUtil.getDefaultPreference(activity).get(Const.NUMBER_EVENT_GUIDE, true)) {
//            val intent = Intent(activity, EventGuideAlertActivity::class.java)
//            intent.putExtra(Const.KEY, Const.NUMBER_EVENT_GUIDE)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
//        image_number_event_back.setOnClickListener {
//            activity?.onBackPressed()
//        }
//
//        mLayoutManager = LinearLayoutManager(requireActivity())
//        recycler_number_event.layoutManager = mLayoutManager
//        mAdapter = NumberEventAdapter(requireActivity())
//        recycler_number_event.adapter = mAdapter
//        recycler_number_event.addItemDecoration(CustomItemOffsetDecoration(requireActivity(), R.dimen.height_30, R.dimen.height_30))
//
//        recycler_number_event.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
//        mAdapter!!.setOnItemClickListener(object : NumberEventAdapter.OnItemClickListener {
//
//            private val MIN_CLICK_INTERVAL: Long = 1000 //in millis
//            private var lastClickTime: Long = 0
//
//            override fun onItemClick(position: Int, isOpen : Boolean) {
//
//                val currentTime = SystemClock.elapsedRealtime()
//                if (currentTime - lastClickTime > MIN_CLICK_INTERVAL) {
//                    lastClickTime = currentTime
//
//                    if(position < 0){
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
//                            cashChangeLauncher.launch(intent)
//                            return
//                        }
//                    } else if (item.winAnnounceType.equals(EventType.WinAnnounceType.limit.name)) {
//                        if(item.joinCount!! >= item.maxJoinCount!!){
//                            val intent = Intent(activity, EventImpressionActivity::class.java)
//                            intent.putExtra(Const.DATA, item)
//                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                            cashChangeLauncher.launch(intent)
//                            return
//                        }
//                    }else if (item.winAnnounceType.equals(EventType.WinAnnounceType.immediately.name)) {
//                        if(item.winnerCount!! >= item.totalGiftCount!!){
//                            val intent = Intent(activity, EventImpressionActivity::class.java)
//                            intent.putExtra(Const.DATA, item)
//                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                            cashChangeLauncher.launch(intent)
//                            return
//                        }
//                    }
//
//                    if (!PplusCommonUtil.loginCheck(requireActivity(), cashChangeLauncher)) {
//                        return
//                    }
//
//                    if (item.primaryType.equals(EventType.PrimaryType.number.name)) {
//                        if(isOpen){
//                            val intent = Intent(activity, NumberEventDetailActivity::class.java)
//                            intent.putExtra(Const.DATA, item)
//                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                            cashChangeLauncher.launch(intent)
//                        }else{
//                            PplusCommonUtil.showEventAlert(activity!!, 516, item)
//                        }
//                    }
//                }
//            }
//        })
//
//        swipe_refresh_number_event.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
//            override fun onRefresh() {
//                getCount()
//                swipe_refresh_number_event.isRefreshing = false
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
//        text_number_event_retention_bol.setOnClickListener {
//            if (LoginInfoManager.getInstance().isMember) {
//                val intent = Intent(activity, BolConfigActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                startActivity(intent)
//            }else{
//                val intent = Intent(activity, SnsLoginActivity::class.java)
//                cashChangeLauncher.launch(intent)
//            }
//        }
//
//        if (LoginInfoManager.getInstance().isMember) {
//            setRetentionBol()
//        }else{
//            text_number_event_retention_bol?.setText(R.string.msg_play_title)
//        }
//        getCount()
//
//    }
//
//    private fun setRetentionBol() {
//        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
//
//            override fun reload() {
//
//                text_number_event_retention_bol?.text = PplusCommonUtil.fromHtml(getString(R.string.html_cash_unit2, FormatUtil.getMoneyType(LoginInfoManager.getInstance().user.totalBol.toString())))
//            }
//        })
//    }
//
//    private fun setSelect(view1: TextView, view2: TextView, view3: TextView) {
//
//        view1.typeface = Typeface.DEFAULT_BOLD
//        view2.typeface = Typeface.DEFAULT
//        view3.typeface = Typeface.DEFAULT
//        view1.isSelected = true
//        view2.isSelected = false
//        view3.isSelected = false
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
//    private fun getCount() {
//        showProgress("")
//        val params = HashMap<String, String>()
//        params["filter"] = EventType.PrimaryType.number.name
//
//        ApiBuilder.create().getEventCount(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {
//
//            override fun onResponse(call: Call<NewResultResponse<Int>>, response: NewResultResponse<Int>) {
//                hideProgress()
//                if (!isAdded) {
//                    return
//                }
//                mTotalCount = response.data
//
//                if (mTotalCount == 0) {
//                    layout_number_event_not_exist.visibility = View.VISIBLE
//                } else {
//                    layout_number_event_not_exist.visibility = View.GONE
//                }
//
//                mPage = 1
//                mAdapter?.clear()
//                listCall(mPage)
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Int>>, t: Throwable, response: NewResultResponse<Int>) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    private fun listCall(page: Int) {
//        val params = HashMap<String, String>()
//        params["filter"] = EventType.PrimaryType.number.name
//        params["pg"] = page.toString()
//
//        showProgress("")
//        mLockListView = true
//        ApiBuilder.create().getEventList(params).setCallback(object : PplusCallback<NewResultResponse<Event>> {
//
//            override fun onResponse(call: Call<NewResultResponse<Event>>, response: NewResultResponse<Event>) {
//
//                hideProgress()
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
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    val cashChangeLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if(LoginInfoManager.getInstance().isMember){
//            setRetentionBol()
//        }
//        getEvent()
//
//    }
//
//    private fun getEvent() {
//        if (mSelectEvent == null) {
//            return
//        }
//        val params = HashMap<String, String>()
//        params["no"] = mSelectEvent!!.no.toString()
//        showProgress("")
//        ApiBuilder.create().getEvent(params).setCallback(object : PplusCallback<NewResultResponse<Event>> {
//            override fun onResponse(call: Call<NewResultResponse<Event>>?, response: NewResultResponse<Event>?) {
//                hideProgress()
//                if (!isAdded) {
//                    return
//                }
//                if (response!!.data != null) {
//                    mAdapter!!.replaceData(mSelectPos, response.data)
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Event>>?, t: Throwable?, response: NewResultResponse<Event>?) {
//                hideProgress()
//            }
//        }).build().call()
//        mSelectEvent = null
//    }
//
//    override fun getPID(): String {
//        return "Main_bolpoint_number_eventlist"
//    }
//
//    companion object {
//
//        fun newInstance(): NumberEventFragment {
//
//            val fragment = NumberEventFragment()
//            val args = Bundle()
////            args.putSerializable(Const.TYPE, type)
//            fragment.arguments = args
//            return fragment
//        }
//    }
//
//}// Required empty public constructor
