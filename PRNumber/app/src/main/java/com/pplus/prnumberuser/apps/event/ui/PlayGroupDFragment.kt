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
import com.pplus.prnumberuser.databinding.FragmentPlayGroupDBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import com.pplus.utils.part.utils.time.DateFormatUtils
import retrofit2.Call
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [PlayGroupDFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlayGroupDFragment : BaseFragment<BaseActivity>() {

    private var mPage: Int = 0
    private var mTotalCount = 0
    //    private var mLayoutManager: LinearLayoutManager? = null
    private var mGridLayoutManager: GridLayoutManager? = null
    private var mLockListView = true
    private var mAdapter: PlayAdapter? = null
    private var mSelectPos: Int = 0
    private var mSelectEvent: Event? = null
    private var mSearch: EventType.WinAnnounceType? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (arguments != null) {
//            type = arguments.getSerializable(Const.TYPE) as EnumData.AdsType
        }
    }


    private var _binding: FragmentPlayGroupDBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentPlayGroupDBinding.inflate(inflater, container, false)
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

        binding.recyclerPlayGroupD.layoutManager = mGridLayoutManager
//        recycler_play.layoutManager = mLayoutManager
        mAdapter = PlayAdapter()
        binding.recyclerPlayGroupD.adapter = mAdapter
        if (binding.recyclerPlayGroupD.itemDecorationCount == 0) {
            binding.recyclerPlayGroupD.addItemDecoration(CustomItemOffsetDecoration(requireActivity(), R.dimen.height_40))
        }

        binding.recyclerPlayGroupD.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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
                            detailLauncher.launch(intent)
                            return
                        }
                    } else if (item.winAnnounceType.equals(EventType.WinAnnounceType.limit.name)) {
                        if (item.joinCount!! >= item.maxJoinCount!!) {
                            val intent = Intent(activity, EventImpressionActivity::class.java)
                            intent.putExtra(Const.DATA, item)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            detailLauncher.launch(intent)
                            return
                        }
                    } else if (item.winAnnounceType.equals(EventType.WinAnnounceType.immediately.name)) {
                        if (item.winnerCount!! >= item.totalGiftCount!!) {
                            val intent = Intent(activity, EventImpressionActivity::class.java)
                            intent.putExtra(Const.DATA, item)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            detailLauncher.launch(intent)
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
                        detailLauncher.launch(intent)
                    } else {
                        if (StringUtils.isNotEmpty(item.eventLink)) {
                            val intent = Intent(activity, EventDetailActivity::class.java)
                            intent.putExtra(Const.DATA, item)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            detailLauncher.launch(intent)
                        } else {
                            if (item.reward == null || item.reward!! >= 0) {
                                joinEvent(item)
                            }else{
                                if (LoginInfoManager.getInstance().user.totalBol >= Math.abs(item.reward!!)) {
                                    val intent = Intent(activity, PlayAlertActivity::class.java)
                                    intent.putExtra(Const.DATA, item)
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

        binding.swipeRefreshPlayGroupD.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                getCount()
                binding.swipeRefreshPlayGroupD.isRefreshing = false
            }
        })


        binding.textPlayGroupDRetentionBol.setOnClickListener {
            val intent = Intent(activity, BolConfigActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.textPlayGroupDLogin.setOnClickListener {
            val intent = Intent(activity, SnsLoginActivity::class.java)
            signInLauncher.launch(intent)
        }

        checkSignIn()
//        setBuzzvil()
        binding.layoutPlayGroupDLoading.visibility = View.VISIBLE
        getCount()

    }

//    private fun setBuzzvil(){
//        if(LoginInfoManager.getInstance().isMember && BuzzAdBenefit.getPrivacyPolicyManager()!!.isConsentGranted()){
//            collapsing_toolbar_play_group_d.visibility = View.VISIBLE
//            val loader = NativeAdLoader(getString(R.string.buzvil_native_id_free))
//            loader.loadAd(object : NativeAdLoader.OnAdLoadedListener {
//                override fun onLoadError(adError: AdError) {
//                    LogUtil.e("NativeAdLoader", "Failed to load a native ad.", adError)
//                }
//
//                override fun onAdLoaded(nativeAd: NativeAd) {
//
//                    if(!isAdded){
//                        return
//                    }
//                    try{
//                        val ad = nativeAd.ad
//                        val creativeType = if (ad.creative == null) null else ad.creative!!.getType()
//                        if(cta_view_buzzvil == null){
//                            return
//                        }
//                        val ctaPresenter = CtaPresenter(cta_view_buzzvil)
//                        ctaPresenter?.bind(nativeAd)
//
//                        // 1) Assign Ad Properties.
//                        // 1) Assign Ad Properties.
//                        if (media_view != null) {
//                            media_view?.setCreative(ad.creative)
//                            media_view?.setVideoEventListener(object : VideoEventListener { // Override and implement methods
//                                override fun onVideoStarted() {
//
//                                }
//
//                                override fun onResume() {
//
//                                }
//
//                                override fun onPause() {
//
//                                }
//
//                                override fun onReplay() {
//                                }
//
//                                override fun onLanding() {
//                                }
//
//                                override fun onError(p0: VideoErrorStatus, p1: String?) {
//
//                                }
//
//                                override fun onVideoEnded() {
//
//                                }
//                            })
//                        }
//
//                        text_buzzvil_title?.text = ad.title
//
//                        Glide.with(activity!!).load(ad.iconUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(image_buzzvil_icon)
//
//                        text_buzzvil_desc?.text = ad.description
//
//                        // 2) Create a list of clickable views.
//
//                        // 2) Create a list of clickable views.
//                        val clickableViews: MutableList<View> = ArrayList()
//                        clickableViews.add(cta_view_buzzvil)
//                        clickableViews.add(media_view)
//                        clickableViews.add(text_buzzvil_title)
//                        clickableViews.add(text_buzzvil_desc)
//
//                        // 3) Register ClickableViews, MediaView and NativeAd to NativeAdView.
//
//                        // 3) Register ClickableViews, MediaView and NativeAd to NativeAdView.
//                        native_ad_view.setClickableViews(clickableViews)
//                        native_ad_view.setMediaView(media_view)
//                        native_ad_view.setNativeAd(nativeAd)
//
//                        // 4) Add OnNativeAdEventListener to NativeAdView and implement participated UI.
//                        // 4) Add OnNativeAdEventListener to NativeAdView and implement participated UI.
//                        native_ad_view.addOnNativeAdEventListener(object : NativeAdView.OnNativeAdEventListener {
//                            override fun onImpressed(view: NativeAdView, nativeAd: NativeAd) {}
//                            override fun onClicked(view: NativeAdView, nativeAd: NativeAd) {
//                                // Action형 광고에 대한 CTA 처리
//                                ctaPresenter.bind(nativeAd)
//                            }
//
//                            override fun onRewardRequested(view: NativeAdView, nativeAd: NativeAd) {
//                                // 퍼블리셔 기획에 따라 리워드 로딩 이미지를 보여주는 등의 처리
//                            }
//
//                            override fun onRewarded(view: NativeAdView,
//                                                    nativeAd: NativeAd,
//                                                    @Nullable rewardResult: RewardResult?) {
//                                // 리워드 적립의 결과 (RewardResult) SUCCESS, ALREADY_PARTICIPATED, MISSING_REWARD 등에 따라서 적절한 유저 커뮤니케이션 처리
//                            }
//
//                            override fun onParticipated(view: NativeAdView, nativeAd: NativeAd) {
//                                // 퍼블리셔 기획에 따라 UI 처리
//                                ctaPresenter.bind(nativeAd)
//                            }
//                        })
//                    }catch (e:Exception){
//                        Log.e(LOG_TAG, e.toString())
//                    }
//
//                }
//            })
//        }else{
//            collapsing_toolbar_play_group_d.visibility = View.GONE
//        }
//    }

    private fun setRetentionBol() {
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {

                binding.textPlayGroupDRetentionBol.text = PplusCommonUtil.fromHtml(getString(R.string.html_cash_unit2, FormatUtil.getMoneyType(LoginInfoManager.getInstance().user.totalBol.toString())))
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
        params["groupSeqNo"] = "4"
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
                    binding.layoutPlayGroupDLoading.visibility = View.GONE
                    binding.layoutPlayGroupDNotExist.visibility = View.VISIBLE
                } else {
                    binding.layoutPlayGroupDNotExist.visibility = View.GONE
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
        params["groupSeqNo"] = "4"
//        showProgress("")
        mLockListView = true
        ApiBuilder.create().getEventList(params).setCallback(object : PplusCallback<NewResultResponse<Event>> {

            override fun onResponse(call: Call<NewResultResponse<Event>>,
                                    response: NewResultResponse<Event>) {

                //                hideProgress()
                if (!isAdded) {
                    return
                }
                binding.layoutPlayGroupDLoading.visibility = View.GONE
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
            binding.textPlayGroupDRetentionBol.visibility = View.VISIBLE
            binding.textPlayGroupDLogin.visibility = View.GONE
        }else{
            binding.textPlayGroupDRetentionBol.visibility = View.GONE
            binding.textPlayGroupDLogin.visibility = View.VISIBLE
        }
    }

    val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data
            checkSignIn()
        }
    }

    val cashChangeLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        setRetentionBol()
    }

    val joinAlertLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            if (data != null) {
                val event = data.getParcelableExtra<Event>(Const.DATA)
                val position = data.getIntExtra(Const.POSITION, 0)
                if(position == 0){
                    if (LoginInfoManager.getInstance().user.totalBol >= Math.abs(event!!.reward!!)) {
                        joinEvent(event)
                    } else {
                        val intent = Intent(activity, BolChargeAlertActivity::class.java)
                        intent.putExtra(Const.EVENT, event)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        cashChangeLauncher.launch(intent)
                    }
                }
            }

        } else {
            getEvent()
        }
    }

    val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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
                        } else {
                            val intent = Intent(activity, BolChargeAlertActivity::class.java)
                            intent.putExtra(Const.EVENT, event)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            cashChangeLauncher.launch(intent)
                        }
                    }
                })
            } else {
                setRetentionBol()
                getEvent()
            }
        }else {
            setRetentionBol()
            getEvent()
        }
    }

    val detailLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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
        return "Home_Free event"
    }

    companion object {

        fun newInstance(): PlayGroupDFragment {

            val fragment = PlayGroupDFragment()
            val args = Bundle()
//            args.putSerializable(Const.TYPE, type)
            fragment.arguments = args
            return fragment
        }
    }

}// Required empty public constructor
