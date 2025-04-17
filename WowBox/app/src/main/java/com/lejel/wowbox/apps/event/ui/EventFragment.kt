package com.lejel.wowbox.apps.event.ui


import android.animation.Animator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DimenRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.builder.AlertBuilder
import com.lejel.wowbox.apps.common.builder.OnAlertResultListener
import com.lejel.wowbox.apps.common.builder.data.AlertData
import com.lejel.wowbox.apps.common.component.RecyclerScaleScrollListener
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.common.ui.base.BaseFragment
import com.lejel.wowbox.apps.event.data.EventAdapter
import com.lejel.wowbox.apps.faq.ui.FaqActivity
import com.lejel.wowbox.apps.login.LoginActivity2
import com.lejel.wowbox.apps.luckyball.ui.BallConfigActivity
import com.lejel.wowbox.apps.luckybox.data.LuckyBoxLoadingView
import com.lejel.wowbox.apps.main.data.HomeBannerAdapter
import com.lejel.wowbox.apps.my.ui.UpdateMobileNumberActivity
import com.lejel.wowbox.apps.notice.ui.NoticeActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.Banner
import com.lejel.wowbox.core.network.model.dto.Count
import com.lejel.wowbox.core.network.model.dto.Event
import com.lejel.wowbox.core.network.model.dto.EventJoinParam
import com.lejel.wowbox.core.network.model.dto.EventJoinResult
import com.lejel.wowbox.core.network.model.response.ListResultResponse
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.FragmentEventBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.time.DateFormatUtils
import retrofit2.Call
import java.util.Locale


/**
 * A simple [Fragment] subclass.
 * Use the [EventFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EventFragment : BaseFragment<BaseActivity>() {


    private var mPage: Int = 0
    private var mTotalCount = 0
    private var mLayoutManager: LinearLayoutManager? = null
    private var mLockListView = true
    private var mAdapter: EventAdapter? = null
    private var mSelectPos: Int = -1
    private var mSelectEvent: Event? = null
//    private var mGroupNo = 1

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (arguments != null) {
//            mGroupNo = requireArguments().getInt(Const.GROUP)
        }
    }

    private var _binding: FragmentEventBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = FragmentEventBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    lateinit var mBannerAdapter: HomeBannerAdapter

    fun ViewPager2.setCurrentItemWithDuration(
        item: Int,
        duration: Long,
        isVertical: Boolean,
        interpolator: TimeInterpolator = AccelerateDecelerateInterpolator(),
        pagePxWidth: Int = width, // Default value taken from getWidth() from ViewPager2 view
        pagePxHeight: Int = height // Default value taken from getWidth() from ViewPager2 view
    ) {
        var pagePx = 0
        if(isVertical){
            pagePx = pagePxHeight
        }else{
            pagePx = pagePxWidth
        }
        val pxToDrag: Int = pagePx * (item - currentItem)
        val animator = ValueAnimator.ofInt(0, pxToDrag)
        var previousValue = 0
        animator.addUpdateListener { valueAnimator ->
            val currentValue = valueAnimator.animatedValue as Int
            val currentPxToDrag = (currentValue - previousValue).toFloat()
            fakeDragBy(-currentPxToDrag)
            previousValue = currentValue
        }
        animator.addListener(object : Animator.AnimatorListener {

            override fun onAnimationStart(animation: Animator) { beginFakeDrag() }
            override fun onAnimationEnd(animation: Animator) { endFakeDrag() }
            override fun onAnimationCancel(animation: Animator) { /* Ignored */ }
            override fun onAnimationRepeat(animation: Animator) { /* Ignored */ }
        })
        animator.interpolator = interpolator
        animator.duration = duration
        animator.start()
    }

    var mScrollListener:RecyclerScaleScrollListener? = null
    override fun init() {

        mBannerAdapter = HomeBannerAdapter()
        binding.pagerEventBanner.adapter = mBannerAdapter

        mBannerAdapter.listener = object : HomeBannerAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {

                val item = mBannerAdapter.getItem(position)
                when (item.moveType) {
                    "inner" -> {
                        when (item.innerType) {
                            "main" -> {
                            }
                            "notice" -> {
                                val intent = Intent(requireActivity(), NoticeActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                defaultLauncher.launch(intent)
                            }
                            "faq" -> {
                                val intent = Intent(requireActivity(), FaqActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                defaultLauncher.launch(intent)
                            }
                        }

                    }

                    "outer" -> {
                        PplusCommonUtil.openChromeWebView(requireActivity(), item.outerUrl!!)
                    }
                }
            }
        }

        val handler = Handler(Looper.myLooper()!!)
        binding.pagerEventBanner.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {

                binding.textEventBannerPage.text = "${position+1}/${mBannerAdapter.itemCount}"
                handler.removeMessages(0)
                val runnable = Runnable {
                    if (!isAdded) {
                        return@Runnable
                    }

                    val size = (binding.pagerEventBanner.adapter?.itemCount ?: 0)
                    if (binding.pagerEventBanner.currentItem < size - 1) {
                        binding.pagerEventBanner.setCurrentItemWithDuration(binding.pagerEventBanner.currentItem + 1, 1000, false)
                    } else {
                        binding.pagerEventBanner.setCurrentItemWithDuration(0, 1000, false)
                    }
                }

                handler.postDelayed(runnable, 2500)
            }

        })

        mScrollListener = RecyclerScaleScrollListener(binding.layoutEventFloating)
        mLayoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerEvent.layoutManager = mLayoutManager
        mAdapter = EventAdapter()
        mAdapter!!.launcher = resultLauncher
        binding.recyclerEvent.adapter = mAdapter
        binding.recyclerEvent.addItemDecoration(CustomItemOffsetDecoration(requireActivity(), R.dimen.height_20, R.dimen.height_10))
        binding.recyclerEvent.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        mAdapter!!.setOnItemClickListener(object : EventAdapter.OnItemClickListener {

            private val MIN_CLICK_INTERVAL: Long = 1000 //in millis
            private var lastClickTime: Long = 0

            override fun onItemClick(position: Int, isOpen: Boolean) {

                val currentTime = SystemClock.elapsedRealtime();
                if (currentTime - lastClickTime > MIN_CLICK_INTERVAL) {
                    lastClickTime = currentTime

                    if (position < 0) {
                        return
                    }

                    val item = mAdapter!!.getItem(position)

                    mSelectPos = position
                    mSelectEvent = item

                    if (item.winAnnounceType.equals("special")) {
                        val currentMillis = System.currentTimeMillis()
                        val winAnnounceDate = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.winAnnounceDatetime).time
                        if (currentMillis > winAnnounceDate) {

                            val intent = Intent(requireActivity(), EventImpressionActivity::class.java)
                            intent.putExtra(Const.DATA, item)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            resultLauncher.launch(intent)
                            return
                        }
                    }

                    if (!PplusCommonUtil.loginCheck(requireActivity(), defaultLauncher)) {
                        return
                    }

                    if(LoginInfoManager.getInstance().member!!.verifiedMobile == null || !LoginInfoManager.getInstance().member!!.verifiedMobile!!){

                        val builder = AlertBuilder.Builder()
                        builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_event_verify_mobile), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
                        builder.setRightText(getString(R.string.word_confirm))
                        builder.setOnAlertResultListener(object : OnAlertResultListener {

                            override fun onCancel() {

                            }

                            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                                when (event_alert) {
                                    AlertBuilder.EVENT_ALERT.SINGLE -> {
                                        val intent = Intent(requireActivity(), UpdateMobileNumberActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                        resultLauncher.launch(intent)
                                    }
                                    else -> {

                                    }
                                }
                            }
                        }).builder().show(requireActivity())
                        return
                    }

                    when(item.detailType){
                        1->{
                            joinEvent(item)
                        }
                        2,3->{
//                            val intent = Intent(requireActivity(), EventDetailActivity::class.java)
//                            intent.putExtra(Const.DATA, item)
//                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                            resultLauncher.launch(intent)
                        }
                    }
                }

            }
        })
        binding.swipeRefreshEvent.setOnRefreshListener(object : androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                if (!isAdded) {
                    return
                }
                mPage = 1
                listCall(mPage)
                binding.swipeRefreshEvent.isRefreshing = false
            }
        })

        binding.layoutEventRetentionBall.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), defaultLauncher)) {
                return@setOnClickListener
            }

            val intent = Intent(requireActivity(), BallConfigActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        binding.layoutEventFloating.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), defaultLauncher)) {
                return@setOnClickListener
            }

            val intent = Intent(requireActivity(), MyWinHistoryActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        loginCheck()

        binding.layoutEventLoading.visibility = View.VISIBLE
        getBannerList()
        mPage = 1
        listCall(mPage)
    }

    private fun getBannerList() {
        val params = HashMap<String, String>()
        params["aos"] = "1"
        params["type"] = "timeEvent"
        params["nation"] = Locale.getDefault().country
        ApiBuilder.create().getBannerList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<Banner>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<Banner>>>?, response: NewResultResponse<ListResultResponse<Banner>>?) {

                if(!isAdded){
                    return
                }

                if (response?.result != null && response.result!!.list != null && response.result!!.list!!.isNotEmpty()) {
                    binding.layoutEventBanner.visibility = View.VISIBLE
                    val bannerList = response.result!!.list!!
                    mBannerAdapter.setDataList(bannerList as MutableList<Banner>)
                    if(mBannerAdapter.itemCount > 1){
                        binding.textEventBannerPage.visibility = View.VISIBLE
                        binding.textEventBannerPage.text = "1/${mBannerAdapter.itemCount}"
                    }else{
                        binding.textEventBannerPage.visibility = View.GONE
                    }
                }else{
                    binding.layoutEventBanner.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<Banner>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<Banner>>?) {
                if(!isAdded){
                    return
                }
            }
        }).build().call()
    }

    private fun getNewWinCountByUser(){
        ApiBuilder.create().getNewWinCountByUser().setCallback(object : PplusCallback<NewResultResponse<Count>>{
            override fun onResponse(call: Call<NewResultResponse<Count>>?, response: NewResultResponse<Count>?) {

                if(!isAdded){
                    return
                }

                if(response?.result != null && response.result!!.count != null && response.result!!.count!! > 0){
                    binding.layoutEventFloating.visibility = View.VISIBLE
                    binding.recyclerEvent.removeOnScrollListener(mScrollListener!!)
                    binding.recyclerEvent.addOnScrollListener(mScrollListener!!)
                }else{
                    binding.layoutEventFloating.visibility = View.GONE
                    binding.recyclerEvent.removeOnScrollListener(mScrollListener!!)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Count>>?, t: Throwable?, response: NewResultResponse<Count>?) {

            }
        }).build().call()
    }

    private inner class CustomItemOffsetDecoration(private val mTopOffset: Int,
                                                   private val mItemOffset: Int) : RecyclerView.ItemDecoration() {

        constructor(context: Context,
                    @DimenRes topOffsetId: Int,
                    @DimenRes lastOffsetId: Int) : this(context.resources.getDimensionPixelSize(topOffsetId), context.resources.getDimensionPixelSize(lastOffsetId)) {
        }

        override fun getItemOffsets(outRect: Rect,
                                    view: View,
                                    parent: RecyclerView,
                                    state: RecyclerView.State) {

            super.getItemOffsets(outRect, view, parent, state)

            val position = parent.getChildAdapterPosition(view)
            if (position == 0) {
                outRect.set(0, mTopOffset, 0, mItemOffset)
            } else {
                outRect.set(0, 0, 0, mItemOffset)
            }
        }
    }

    fun joinEvent(event: Event) {
        val params = EventJoinParam()
        params.event = event
        showProgress(getString(R.string.msg_event_join_progress))

        ApiBuilder.create().joinEvent(params).setCallback(object : PplusCallback<NewResultResponse<EventJoinResult>> {
            override fun onResponse(call: Call<NewResultResponse<EventJoinResult>>?,
                                    response: NewResultResponse<EventJoinResult>?) {

                hideProgress()
                if (!isAdded) {
                    return
                }
                setEvent(requireActivity(), "eventJoin")

                if (response?.result != null) {
                    val result = response.result!!

                    if (event.winAnnounceType.equals("immediately")) {
                        val mLoading = LuckyBoxLoadingView()
                        mLoading.isCancelable = false
//                        mLoading.setText(getString(R.string.msg_checking_event_result))
                        var delayTime = 2000L
                        try {
                            mLoading.show(parentFragmentManager, "")
                        } catch (e: Exception) {
                            LogUtil.e(LOG_TAG, e.toString())
                        }

                        val handler = Handler(Looper.myLooper()!!)
                        handler.postDelayed({

                            try {
                                mLoading.dismiss()
                            } catch (e: Exception) {

                            }

                            val intent = Intent(requireActivity(), EventResultActivity::class.java)
                            intent.putExtra(Const.EVENT_JOIN, result.join)
                            intent.putExtra(Const.EVENT, event)
                            if(result.win != null){
                                intent.putExtra(Const.EVENT_WIN, result.win)
                            }
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            resultLauncher.launch(intent)
                        }, delayTime)

                    } else {
                        val intent = Intent(requireActivity(), EventResultActivity::class.java)
                        intent.putExtra(Const.EVENT_JOIN, result.join)
                        intent.putExtra(Const.EVENT, event)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        resultLauncher.launch(intent)
                    }
                }

            }

            override fun onFailure(call: Call<NewResultResponse<EventJoinResult>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<EventJoinResult>?) {

                hideProgress()
                if (!isAdded) {
                    return
                }

                if (response != null) {
                    PplusCommonUtil.showEventAlert(activity!!, response.code, event, resultLauncher)
                }

            }
        }).build().call()
    }

    private fun listCall(page: Int) {
        val params = HashMap<String, String>()
        params["primaryType"] = "join"
        params["winAnnounceType"] = "special"
        params["paging[page]"] = page.toString()
        params["paging[limit]"] = "20"
//        showProgress("")
        mLockListView = true
        ApiBuilder.create().getEventList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<Event>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<Event>>>?, response: NewResultResponse<ListResultResponse<Event>>?) {
//                hideProgress()
                if (!isAdded) {
                    return
                }
                binding.layoutEventLoading.visibility = View.GONE
                if (response?.result != null) {
                    if (page == 1) {
                        mAdapter!!.clear()

                        mTotalCount = response.result!!.total!!
                        if(mTotalCount == 0){
                            binding.layoutEventNotExist.visibility = View.VISIBLE
                        }else{
                            binding.layoutEventNotExist.visibility = View.GONE
                        }
                    }

                    mLockListView = false

                    if (response.result!!.list != null) {
                        val dataList = response.result!!.list!!
                        mAdapter!!.addAll(dataList)
                    }

                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<Event>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<Event>>?) {
//                hideProgress()
                if (!isAdded) {
                    return
                }
                binding.layoutEventLoading.visibility = View.GONE
            }

        }).build().call()
    }

    val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (!isAdded) {
            return@registerForActivityResult
        }
        getEvent()
    }

    private fun getEvent() {
        if (mSelectEvent == null) {
            return
        }

        showProgress("")
        ApiBuilder.create().getEvent(mSelectEvent!!.seqNo!!).setCallback(object : PplusCallback<NewResultResponse<Event>> {
            override fun onResponse(call: Call<NewResultResponse<Event>>?,
                                    response: NewResultResponse<Event>?) {
                hideProgress()
                if (!isAdded) {
                    return
                }

                if (response?.result != null) {
                    mAdapter!!.replaceData(mSelectPos, response.result!!)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Event>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<Event>?) {

                hideProgress()
                if (!isAdded) {
                    return
                }
            }
        }).build().call()
        mSelectEvent = null
    }

    private fun loginCheck() {
        if (LoginInfoManager.getInstance().isMember()) {
            binding.textEventLogin.visibility = View.GONE
            binding.layoutEventRetentionBall.visibility = View.VISIBLE
            getNewWinCountByUser()
            reloadSession()
        } else {
            binding.textEventLogin.visibility = View.VISIBLE
            binding.layoutEventFloating.visibility = View.GONE
            binding.layoutEventRetentionBall.visibility = View.GONE
            binding.textEventLogin.setOnClickListener {
                val intent = Intent(requireActivity(), LoginActivity2::class.java)
                defaultLauncher.launch(intent)
            }
        }
    }

    private fun reloadSession() {
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {
                if(!isAdded){
                    return
                }
                binding.textEventRetentionBall.text = FormatUtil.getMoneyType(LoginInfoManager.getInstance().member!!.ball!!.toInt().toString())
            }
        })
    }

    val defaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        loginCheck()
    }

    override fun getPID(): String {
        return ""
    }

    companion object {

        @JvmStatic
        fun newInstance() = EventFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }

} // Required empty public constructor
