package com.lejel.wowbox.apps.event.ui


import android.animation.Animator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.app.Activity
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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.builder.AlertBuilder
import com.lejel.wowbox.apps.common.builder.OnAlertResultListener
import com.lejel.wowbox.apps.common.builder.data.AlertData
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.common.ui.base.BaseFragment
import com.lejel.wowbox.apps.community.ui.CommunityApplyActivity
import com.lejel.wowbox.apps.event.data.PlayAdapter
import com.lejel.wowbox.apps.faq.ui.FaqActivity
import com.lejel.wowbox.apps.login.LoginActivity2
import com.lejel.wowbox.apps.luckyball.ui.AlertLuckyBallLackActivity
import com.lejel.wowbox.apps.luckyball.ui.BallConfigActivity
import com.lejel.wowbox.apps.luckybox.data.LuckyBoxLoadingView
import com.lejel.wowbox.apps.luckybox.ui.LuckyBoxGuideActivity
import com.lejel.wowbox.apps.main.data.HomeBannerAdapter
import com.lejel.wowbox.apps.my.ui.UpdateMobileNumberActivity
import com.lejel.wowbox.apps.notice.ui.NoticeActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.Banner
import com.lejel.wowbox.core.network.model.dto.Event
import com.lejel.wowbox.core.network.model.dto.EventJoinParam
import com.lejel.wowbox.core.network.model.dto.EventJoinResult
import com.lejel.wowbox.core.network.model.response.ListResultResponse
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.FragmentPlayBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import com.pplus.utils.part.utils.time.DateFormatUtils
import retrofit2.Call
import java.util.Locale


/**
 * A simple [Fragment] subclass.
 * Use the [PlayFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlayFragment : BaseFragment<BaseActivity>() {


    private var mPage: Int = 0
    private var mTotalCount = 0
    private var mLayoutManager: GridLayoutManager? = null
    private var mLockListView = true
    private var mAdapter: PlayAdapter? = null
    private var mSelectPos: Int = -1
    private var mSelectEvent: Event? = null //    private var mGroupNo = 1

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (arguments != null) { //            mGroupNo = requireArguments().getInt(Const.GROUP)
        }
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

    override fun init() {

        mBannerAdapter = HomeBannerAdapter()
        binding.pagerPlayBanner.adapter = mBannerAdapter

        mBannerAdapter.listener = object : HomeBannerAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {

                val item = mBannerAdapter.getItem(position)
                when (item.moveType) {
                    "inner" -> {
                        when (item.innerType) {
                            "telegram" -> {
                                if (!PplusCommonUtil.loginCheck(requireActivity(), defaultLauncher)) {
                                    return
                                }

                                val intent = Intent(requireActivity(), CommunityApplyActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                defaultLauncher.launch(intent)
                            }
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
        binding.pagerPlayBanner.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {

                binding.textPlayBannerPage.text = "${position+1}/${mBannerAdapter.itemCount}"
                handler.removeMessages(0)
                val runnable = Runnable {
                    if (!isAdded) {
                        return@Runnable
                    }

                    val size = (binding.pagerPlayBanner.adapter?.itemCount ?: 0)
                    if (binding.pagerPlayBanner.currentItem < size - 1) {
                        binding.pagerPlayBanner.setCurrentItemWithDuration(binding.pagerPlayBanner.currentItem + 1, 1000, false)
                    } else {
                        binding.pagerPlayBanner.setCurrentItemWithDuration(0, 1000, false)
                    }
                }

                handler.postDelayed(runnable, 2500)
            }

        })

        mLayoutManager = GridLayoutManager(requireActivity(), 2)
        binding.recyclerPlay.layoutManager = mLayoutManager
        mAdapter = PlayAdapter()
        mAdapter!!.launcher = resultLauncher
        binding.recyclerPlay.adapter = mAdapter
        if (binding.recyclerPlay.itemDecorationCount == 0) {
            binding.recyclerPlay.addItemDecoration(CustomItemOffsetDecoration(requireActivity(), R.dimen.height_60))
        }
        binding.recyclerPlay.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        mAdapter!!.setOnItemClickListener(object : PlayAdapter.OnItemClickListener {

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

                    if (item.winAnnounceType.equals("special")) {
                        val currentMillis = System.currentTimeMillis()
                        val winAnnounceDate = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.winAnnounceDatetime).time
                        if (currentMillis > winAnnounceDate) {

                            val intent = Intent(activity, EventImpressionActivity::class.java)
                            intent.putExtra(Const.DATA, item)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            resultLauncher.launch(intent)
                            return
                        }
                    } else if (item.winAnnounceType.equals("limit") && item.joinCount!! >= item.maxJoinCount!!) {
                        if (item.winAnnounceDatetime != null) {
                            val currentMillis = System.currentTimeMillis()
                            val winAnnounceDate = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.winAnnounceDatetime).time
                            if (currentMillis > winAnnounceDate) {
                                val intent = Intent(activity, EventImpressionActivity::class.java)
                                intent.putExtra(Const.DATA, item)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                resultLauncher.launch(intent)
                            }
                        }
                        return
                    } else if (item.winAnnounceType.equals("immediately")) {
                        if (item.winnerCount!! >= item.totalGiftCount!!) {
                            val intent = Intent(activity, EventImpressionActivity::class.java)
                            intent.putExtra(Const.DATA, item)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            resultLauncher.launch(intent)
                            return
                        }
                    }

                    if (!PplusCommonUtil.loginCheck(activity!!, defaultLauncher)) {
                        return
                    }

                    if (LoginInfoManager.getInstance().member!!.verifiedMobile == null || !LoginInfoManager.getInstance().member!!.verifiedMobile!!) {

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

                    if (item.primaryType.equals("goodluck")) {

                        if (item.reward == null || item.reward!! >= 0) {
                            joinEvent(item)
                        } else {
                            if (LoginInfoManager.getInstance().member!!.ball!! >= Math.abs(item.reward!!)) {
                                val intent = Intent(activity, PlayAlertActivity::class.java)
                                intent.putExtra(Const.DATA, item)
                                intent.putExtra(Const.POSITION, 0)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                joinAlertLauncher.launch(intent)
                            } else {
                                val lackBall = Math.abs(item.reward!!) - LoginInfoManager.getInstance().member!!.ball!!
                                val intent = Intent(requireActivity(), AlertLuckyBallLackActivity::class.java)
                                intent.putExtra(Const.DATA, lackBall)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                resultLauncher.launch(intent)
                            }
                        }
                    }
                }
            }

            override fun onPercentClick(position: Int) {
                if(!PplusCommonUtil.loginCheck(requireActivity(), resultLauncher)){
                    return
                }
                val item = mAdapter!!.getItem(position)

                mSelectPos = position
                mSelectEvent = item

                val intent = Intent(requireActivity(), PlayPercentActivity::class.java)
                intent.putExtra(Const.EVENT, item)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                resultLauncher.launch(intent)
            }
        })
        binding.swipeRefreshPlay.setOnRefreshListener(object : androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                if (!isAdded) {
                    return
                }
                mPage = 1
                listCall(mPage)
                binding.swipeRefreshPlay.isRefreshing = false
            }
        })

        binding.textPlayWinHistory.setOnClickListener {
            val intent = Intent(requireActivity(), EventWinActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        Glide.with(this).load(R.drawable.ic_guide_luckyball).into(binding.imagePlayGuideLuckyball)
        Glide.with(this).load(R.drawable.ic_guide_play).into(binding.imagePlayGuidePlay)
        Glide.with(this).load(R.drawable.ic_guide_get_luckyball).into(binding.imagePlayGuideGetLuckyBall)



        binding.layoutLuckyBallGuidePlay.setOnClickListener {
            val intent = Intent(requireActivity(), LuckyBallGuideActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        binding.layoutPlayGuidePlay.setOnClickListener {
            val intent = Intent(requireActivity(), PlayGuideActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        binding.layoutPlayGuideGetLuckyBall.setOnClickListener {
            val intent = Intent(requireActivity(), GetLuckyBallGuideActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        binding.layoutPlayRetentionBall.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), defaultLauncher)) {
                return@setOnClickListener
            }

            val intent = Intent(requireActivity(), BallConfigActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        loginCheck()

        binding.layoutPlayLoading.visibility = View.VISIBLE

        getBannerList()
        mPage = 1
        listCall(mPage)
    }

    private fun getBannerList() {
        val params = HashMap<String, String>()
        params["aos"] = "1"
        params["type"] = "goodluck"
        params["nation"] = Locale.getDefault().country
        ApiBuilder.create().getBannerList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<Banner>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<Banner>>>?, response: NewResultResponse<ListResultResponse<Banner>>?) {

                if(!isAdded){
                    return
                }

                if (response?.result != null && response.result!!.list != null && response.result!!.list!!.isNotEmpty()) {
                    binding.layoutPlayBanner.visibility = View.VISIBLE
                    val bannerList = response.result!!.list!!
                    mBannerAdapter.setDataList(bannerList as MutableList<Banner>)
                    if(mBannerAdapter.itemCount > 1){
                        binding.textPlayBannerPage.visibility = View.VISIBLE
                        binding.textPlayBannerPage.text = "1/${mBannerAdapter.itemCount}"
                    }else{
                        binding.textPlayBannerPage.visibility = View.GONE
                    }
                }else{
                    binding.layoutPlayBanner.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<Banner>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<Banner>>?) {
                if(!isAdded){
                    return
                }
            }
        }).build().call()
    }

    private inner class CustomItemOffsetDecoration(private val mItemOffset: Int) : RecyclerView.ItemDecoration() {

        constructor(context: Context, @DimenRes lastOffsetId: Int) : this(context.resources.getDimensionPixelSize(lastOffsetId)) {
        }

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

            super.getItemOffsets(outRect, view, parent, state)

            val position = parent.getChildAdapterPosition(view)

            if (position <= 1) {
                outRect.set(0, mItemOffset, 0, 0)
            } else {
                outRect.set(0, 0, 0, 0)
            }

            //            outRect.set(0, mItemOffset, 0, 0)
        }
    }

    fun joinEvent(event: Event) {
        val params = EventJoinParam()
        params.event = event
        showProgress(getString(R.string.msg_event_join_progress))

        ApiBuilder.create().joinEvent(params).setCallback(object : PplusCallback<NewResultResponse<EventJoinResult>> {
            override fun onResponse(call: Call<NewResultResponse<EventJoinResult>>?, response: NewResultResponse<EventJoinResult>?) {

                hideProgress()
                if (!isAdded) {
                    return
                }
                setEvent(requireActivity(), "eventJoin")

                if (response?.result != null) {
                    val result = response.result!!

                    if (event.winAnnounceType.equals("immediately")) {
                        val mLoading = LuckyBoxLoadingView()
                        mLoading.isCancelable = false //                        mLoading.setText(getString(R.string.msg_checking_event_result))
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
                            if (result.win != null) {
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

            override fun onFailure(call: Call<NewResultResponse<EventJoinResult>>?, t: Throwable?, response: NewResultResponse<EventJoinResult>?) {

                hideProgress()
                if (!isAdded) {
                    return
                }

                if (response?.result != null && StringUtils.isNotEmpty(response.result!!.joinDate)) {
                    PplusCommonUtil.showEventAlert(activity!!, response.code, event, response.result!!.joinDate, response.result!!.joinTerm, response.result!!.remainSecond, resultLauncher)
                } else {
                    PplusCommonUtil.showEventAlert(activity!!, response?.code!!, event, resultLauncher)
                }

            }
        }).build().call()
    }

    private fun listCall(page: Int) {
        val params = HashMap<String, String>()
        params["primaryType"] = "goodluck"
        params["paging[page]"] = page.toString()
        params["paging[limit]"] = "20" //        showProgress("")
        mLockListView = true
        ApiBuilder.create().getEventList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<Event>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<Event>>>?, response: NewResultResponse<ListResultResponse<Event>>?) { //                hideProgress()
                if (!isAdded) {
                    return
                }
                binding.layoutPlayLoading.visibility = View.GONE
                if (response?.result != null) {
                    if (page == 1) {
                        mAdapter!!.clear()

                        mTotalCount = response.result!!.total!!
                        if (mTotalCount == 0) {
                            binding.layoutPlayNotExist.visibility = View.VISIBLE
                        } else {
                            binding.layoutPlayNotExist.visibility = View.GONE
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
                if (!isAdded) {
                    return
                }
                binding.layoutPlayLoading.visibility = View.GONE
            }

        }).build().call()
    }

    val joinAlertLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            if (result.data != null) {
                val event = PplusCommonUtil.getParcelableExtra(result.data!!, Const.DATA, Event::class.java)!!
                val position = result.data!!.getIntExtra(Const.POSITION, 0)
                if (position == 0) {
                    if (LoginInfoManager.getInstance().member!!.ball!! >= Math.abs(event.reward!!)) {
                        joinEvent(event)
                    } else {
                        val lackBall = Math.abs(event.reward!!) - LoginInfoManager.getInstance().member!!.ball!!
                        val intent = Intent(requireActivity(), AlertLuckyBallLackActivity::class.java)
                        intent.putExtra(Const.DATA, lackBall)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        resultLauncher.launch(intent)
                    }
                }
            }

        } else {
            getEvent()
        }
    }

    val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (!isAdded) {
            return@registerForActivityResult
        }
        loginCheck()
        getEvent()
    }

    private fun getEvent() {
        if (mSelectEvent == null) {
            return
        }

        showProgress("")
        ApiBuilder.create().getEvent(mSelectEvent!!.seqNo!!).setCallback(object : PplusCallback<NewResultResponse<Event>> {
            override fun onResponse(call: Call<NewResultResponse<Event>>?, response: NewResultResponse<Event>?) {
                hideProgress()
                if (!isAdded) {
                    return
                }

                if (response?.result != null) {
                    mAdapter!!.replaceData(mSelectPos, response.result!!)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Event>>?, t: Throwable?, response: NewResultResponse<Event>?) {

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
            binding.textMainPlayLogin.visibility = View.GONE
            binding.layoutPlayRetentionBall.visibility = View.VISIBLE
            reloadSession()
        } else {
            binding.textMainPlayLogin.visibility = View.VISIBLE
            binding.layoutPlayRetentionBall.visibility = View.GONE
            binding.textMainPlayLogin.setOnClickListener {
                val intent = Intent(requireActivity(), LoginActivity2::class.java)
                defaultLauncher.launch(intent)
            }
        }
    }

    val defaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        loginCheck()
    }

    private fun reloadSession() {
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {
                if (!isAdded) {
                    return
                }
                binding.textPlayRetentionBall.text = FormatUtil.getMoneyType(LoginInfoManager.getInstance().member!!.ball!!.toInt().toString())
            }
        })
    }

    override fun getPID(): String {
        return ""
    }

    companion object {

        @JvmStatic
        fun newInstance() = PlayFragment().apply {
            arguments = Bundle().apply {}
        }
    }

} // Required empty public constructor
