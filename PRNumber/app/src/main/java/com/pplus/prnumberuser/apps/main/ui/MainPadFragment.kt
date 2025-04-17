package com.pplus.prnumberuser.apps.main.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.AbsListView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.BusProviderData
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
import com.pplus.prnumberuser.apps.common.ui.custom.dial.DialKey
import com.pplus.prnumberuser.apps.common.ui.custom.dial.DialPadView
import com.pplus.prnumberuser.apps.event.data.EventLoadingView
import com.pplus.prnumberuser.apps.event.ui.*
import com.pplus.prnumberuser.apps.main.data.PageNumberAdapter
import com.pplus.prnumberuser.apps.page.ui.Alert3rdPartyInfoTermsActivity
import com.pplus.prnumberuser.apps.page.ui.NumberGroupPageActivity
import com.pplus.prnumberuser.apps.product.ui.NumberGroupProductActivity
import com.pplus.prnumberuser.core.code.common.EnumData
import com.pplus.prnumberuser.core.code.common.EventType
import com.pplus.prnumberuser.core.code.common.SortCode
import com.pplus.prnumberuser.core.location.LocationUtil
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.*
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.FragmentMainPadBinding
import com.pplus.utils.BusProvider
import com.pplus.utils.part.info.DeviceUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import com.squareup.otto.Subscribe
import retrofit2.Call
import java.util.*

class MainPadFragment : BaseFragment<BaseActivity>() {

    private var _binding: FragmentMainPadBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentMainPadBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private var mAdapter: PageNumberAdapter? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var mLockListView = false
    private var mPaging = 1
    private var mSortCode = SortCode.plusCount
    private var mTotalCount: Int = 0
    private var mNumberSb = StringBuilder()
    private var mPosition = 0
    var mPage: Page? = null

    override fun init() {

        binding.layoutMainPadDescription.layoutParams.height = (DeviceUtil.DISPLAY.SCREEN_HEIGHT_PIXELS * 0.416).toInt()
//        layout_main_pad_description.layoutParams.height = (DeviceUtil.DISPLAY.SCREEN_HEIGHT_PIXELS * 0.345).toInt()
        mLayoutManager = LinearLayoutManager(activity)
        binding.recyclerMainPad.layoutManager = mLayoutManager
        mAdapter = PageNumberAdapter(requireActivity())
        binding.recyclerMainPad.adapter = mAdapter
//        recycler_main_pad.addItemDecoration(CustomItemOffsetDecoration(activity!!, R.dimen.height_60, R.dimen.height_60))


        binding.recyclerMainPad.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            internal var pastVisibleItems: Int = 0
            internal var visibleItemCount: Int = 0
            internal var totalItemCount: Int = 0

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING && !animating && binding.layoutMainNumberPad.isShown) {
                    LogUtil.e(LOG_TAG, "scrollOutAnimation : onScrollStateChanged")
                    scrollOutAnimation()
                }

            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                super.onScrolled(recyclerView, dx, dy)

//                val scrollY = recyclerView!!.computeVerticalScrollOffset()
//
//                val oldScrollY = recyclerView.computeVerticalScrollOffset() - dy
//
//                if (scrollY > oldScrollY && !animating && view_main_number_pad.isShown) {
//
//                    scrollOutAnimation()
//                }

                visibleItemCount = mLayoutManager!!.childCount
                totalItemCount = mLayoutManager!!.itemCount
                pastVisibleItems = mLayoutManager!!.findFirstVisibleItemPosition()
                if (!mLockListView) {
                    if (totalItemCount < mTotalCount && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
                        mPaging++
                        listCall(mPaging)
                    }
                }
            }
        })

        mAdapter!!.setOnItemClickListener(object : PageNumberAdapter.OnItemClickListener {

            override fun onItemClick(position: Int, view: View) {

                val location = IntArray(2)
                view.getLocationOnScreen(location)
                val x = location[0] + view.width / 2
                val y = location[1] + view.height / 2

                mPosition = position
                mPage = mAdapter!!.getItem(position)

                PplusCommonUtil.goPage(activity!!, mAdapter!!.getItem(position), x, y)

                mNumberSb = StringBuilder()
                mAdapter!!.clear()
                binding.recyclerMainPad.visibility = View.GONE
//                pager_main_pad.visibility = View.VISIBLE
//                text_main_pad_desc.visibility = View.VISIBLE
                binding.textMainPadNumber.text = mNumberSb.toString()
            }
        })

        binding.imageMainPadUpDown.setOnClickListener {
            if (!animating) {
                if (!binding.layoutMainNumberPad.isShown) {
                    scrollInAnimation()
                } else {
                    scrollOutAnimation()
                }
            }
        }

        binding.imageMainPadDelete.setOnClickListener {

            if (mNumberSb.isNotEmpty()) {
                mNumberSb.setLength(mNumberSb.length - 1);
                binding.textMainPadNumber.text = mNumberSb.toString()

            }
        }

        binding.imageMainPadDelete.setOnLongClickListener {
            mNumberSb = StringBuilder()
            binding.textMainPadNumber.text = mNumberSb.toString()
            false
        }

        binding.imagePadBack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.imagePadNBook.setOnClickListener {
            val intent = Intent(activity, NBookActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

//        layout_pad_search.setOnClickListener {
//
//            if (!PplusCommonUtil.loginCheck(activity!!)) {
//                return@setOnClickListener
//            }
//
//            val intent = Intent(activity, SearchActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//
//        }

//        val textList = listOf(getString(R.string.msg_pad_search1), getString(R.string.msg_pad_search2))
//        text_main_pad_text.setTexts(textList.toTypedArray())

//        val pagerAdapter = MainPadPagerAdapter(activity!!)
//        pager_main_pad.adapter = pagerAdapter
//        pager_main_pad.interval = 3000
//        pager_main_pad.startAutoScroll()
//
//        pagerAdapter.setOnItemClickListener(object : MainPadPagerAdapter.OnItemClickListener {
//            override fun onItemClick(number: String) {
//                mNumberSb = StringBuilder()
//                mNumberSb.append(number)
//                moveNumber()
//            }
//
//        })

        val handler = Handler()
        handler.postDelayed({

            if (!isAdded) {
                return@postDelayed
            }

            binding.viewMainNumberPad.setOnKeyClickListener(object : DialPadView.OnKeyClickListener {
                override fun onClick(key: DialKey?, view: View?) {
                    if (!isAdded) {
                        return
                    }

                    if (!PplusCommonUtil.loginCheck(requireActivity(), null)) {
                        return
                    }

                    if (key!!.number == "#") {

                        moveNumber()
                    } else if (key.number != "*") {
                        if (mNumberSb.length < 12) {

                            mNumberSb.append(key.number)
                            binding.textMainPadNumber.text = mNumberSb.toString()
                        }
                    }
//                    else if (key.number == "*") {
//                        val intent = Intent(activity, NBookActivity::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                        startActivity(intent)
//                    }
                }

                override fun onLongClick(key: DialKey?, view: View?) {

                    if (!PplusCommonUtil.loginCheck(requireActivity(), null)) {
                        return
                    }

                    if (key!!.number != "*" && key.number != "#") {
                        if (mNumberSb.length < 12) {
                            mNumberSb.append(key.number)
                            moveNumber()

                        }
                    }
                }
            })

        }, 500)

//        image_main_pad_plus_news.setOnClickListener {
//            if (!PplusCommonUtil.loginCheck(activity!!)) {
//                return@setOnClickListener
//            }
//
//            val intent = Intent(activity, PlusFeedActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }

//        image_main_pad_make_biz.setOnClickListener {
//
//            if (!LoginInfoManager.getInstance().isMember || LoginInfoManager.getInstance().user.page == null) {
//                val intent = Intent(activity, BizIntroduceActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                startActivity(intent)
//            } else {
//                PplusCommonUtil.runOtherApp("com.pplus.prnumberbiz")
//            }
//        }

        binding.textMainPadNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (LoginInfoManager.getInstance().isMember) {
                    if (mNumberSb.length > 4) {
                        getCount()
                    } else {
                        mAdapter!!.clear()
                        binding.recyclerMainPad.visibility = View.GONE
//                        pager_main_pad?.visibility = View.VISIBLE
//                        text_main_pad_desc?.visibility = View.VISIBLE
                    }
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })


        mNumberSb = StringBuilder()
        if (StringUtils.isNotEmpty(number)) {
            mNumberSb.append(number)
            binding.textMainPadNumber.text = mNumberSb.toString()
        }

//        if (key == Const.OUT_GOING) {
//
//            if (mNumberSb.length >= 9) {
//                getPage(null)
//            } else {
//                getEvent()
//            }
//        }

    }

    private fun resetNumber(){
        mNumberSb = StringBuilder()
        binding.textMainPadNumber.text = ""
    }

    private fun moveNumber() {


        if (mNumberSb.isNotEmpty()) {

            getVirtualNumberManage()

//            when (mNumberSb.toString()) {
//                "1234"->{
//                    if(Const.API_URL.startsWith("https://stage")){
//                        PplusCommonUtil.openChromeWebView(activity!!, "https://stg-biz.prnumber.com/")
//                    }else{
//                        PplusCommonUtil.openChromeWebView(activity!!, "https://biz.prnumber.com/")
//                    }
//                }
//                "4444"->{
//                    val intent = Intent(activity, MobileGiftActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                    activity?.startActivity(intent)
//                    resetNumber()
//                }
//                "1253" -> {
//                    val intent = Intent(activity, ThemeActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                    activity?.startActivity(intent)
//                    resetNumber()
//                }
//                "7979"->{
//                    if (!PplusCommonUtil.loginCheck(activity!!)) {
//                        return
//                    }
//                    val intent = Intent(activity, InviteActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                    startActivity(intent)
//                    resetNumber()
//                }
//                "7777" -> {
//                    val intent = Intent(activity, PlayActivity::class.java)
//                    intent.putExtra(Const.KEY, 2)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                    activity?.startActivity(intent)
//                    resetNumber()
//                }
//                else -> {
//                    getVirtualNumberManage()
//
//                }
//            }
        }
    }

    private inner class CustomItemOffsetDecoration(private val mItemOffset: Int, private val mTopOffset: Int) : RecyclerView.ItemDecoration() {

        constructor(context: Context, @DimenRes itemOffsetId: Int, @DimenRes topOffsetId: Int) : this(context.resources.getDimensionPixelSize(itemOffsetId), context.resources.getDimensionPixelSize(topOffsetId)) {}

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

            super.getItemOffsets(outRect, view, parent, state)

            val position = parent.getChildAdapterPosition(view)
            if (position == 0) {
                outRect.set(0, mTopOffset, 0, mItemOffset)
            } else {
                outRect.set(0, 0, 0, mItemOffset)
            }

        }
    }

    private fun getVirtualNumberManage(){
        val params = HashMap<String, String>()
        params["virtualNumber"] = mNumberSb.toString()
        showProgress("")
        ApiBuilder.create().getVirtualNumberManage(params).setCallback(object : PplusCallback<NewResultResponse<VirtualNumberManage>> {
            override fun onResponse(
                call: Call<NewResultResponse<VirtualNumberManage>>?,
                response: NewResultResponse<VirtualNumberManage>?
            ) {
                hideProgress()

                if (!isAdded) {
                    return
                }

                if (response?.data != null) {
                    val item = response.data
                    when(item.type){
                        EnumData.VirtualNumberManageType.page.name->{
                            if(item.itemList != null && item.itemList!!.isNotEmpty() && item.itemList!![0].page != null){
                                PplusCommonUtil.goPage(activity!!, item.itemList!![0].page!!, 0, 0)
                            }else{
                                showAlert(R.string.msg_not_exist_pr_number)
                            }

                        }
                        EnumData.VirtualNumberManageType.event.name->{
                            if(StringUtils.isNotEmpty(item.eventCode)){
                                getEvent(item.eventCode!!)
                            }else{
                                showAlert(R.string.msg_not_exist_pr_number)
                            }
                        }

                        EnumData.VirtualNumberManageType.link.name->{
                            if (StringUtils.isNotEmpty(item.url)) {
                                PplusCommonUtil.openChromeWebView(activity!!, item.url!!)
                            }else{
                                showAlert(R.string.msg_not_exist_pr_number)
                            }
                        }
                        EnumData.VirtualNumberManageType.pages.name->{
                            val intent = Intent(activity, NumberGroupPageActivity::class.java)
                            intent.putExtra(Const.DATA, item)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            activity?.startActivity(intent)
                        }
                        EnumData.VirtualNumberManageType.products.name->{
                            val intent = Intent(activity, NumberGroupProductActivity::class.java)
                            intent.putExtra(Const.DATA, item)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            activity?.startActivity(intent)
                        }
                    }
                }else{
                    showAlert(R.string.msg_not_exist_pr_number)
                }
            }

            override fun onFailure(
                call: Call<NewResultResponse<VirtualNumberManage>>?,
                t: Throwable?,
                response: NewResultResponse<VirtualNumberManage>?
            ) {
                hideProgress()
                showAlert(R.string.msg_not_exist_pr_number)
            }
        }).build().call()
    }

    private fun getEvent(code:String) {
        val params = HashMap<String, String>()
        params["code"] = code
        showProgress("")
        ApiBuilder.create().getEventByCode(params).setCallback(object : PplusCallback<NewResultResponse<Event>> {
            override fun onResponse(call: Call<NewResultResponse<Event>>?, response: NewResultResponse<Event>?) {

                hideProgress()

                if (!isAdded) {
                    return
                }

                if (response!!.data != null) {
                    val event = response.data

                    if (event.status != "active") {
                        val url = event.moveTargetString
                        if (StringUtils.isNotEmpty(url)) {
                            PplusCommonUtil.openChromeWebView(activity!!, url!!)
                        }
                        return
                    }

                    if (event!!.primaryType.equals(EventType.PrimaryType.insert.name)) {

                        if(event.isPlus != null && event.isPlus!! && event.pageSeqNo != null && event.agreement2 != null && event.agreement2!! == 1){
                            val params = HashMap<String, String>()
                            params["pageSeqNo"] = event.pageSeqNo.toString()
                            showProgress("")
                            ApiBuilder.create().getOnlyPlus(params).setCallback(object : PplusCallback<NewResultResponse<Plus>> {
                                override fun onResponse(call: Call<NewResultResponse<Plus>>?, response: NewResultResponse<Plus>?) {
                                    hideProgress()
                                    if (!isAdded) {
                                        return
                                    }
                                    if(response?.data == null || response.data.agreement == null || !response.data.agreement!!){
                                        val intent = Intent(activity, Alert3rdPartyInfoTermsActivity::class.java)
                                        intent.putExtra(Const.EVENT, event)
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                        eventAgreeLauncher.launch(intent)
                                    }else{
                                        joinEvent(event)
                                    }
                                }

                                override fun onFailure(call: Call<NewResultResponse<Plus>>?, t: Throwable?, response: NewResultResponse<Plus>?) {
                                    hideProgress()
                                }
                            }).build().call()
                        }else{
                            joinEvent(event)
                        }

                    } else {
                        if (event.primaryType.equals(EventType.PrimaryType.move.name)) {
                            val intent = Intent(activity, EventMoveDetailActivity::class.java)
                            intent.putExtra(Const.DATA, event)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            startActivity(intent)
                        } else {
                            if (StringUtils.isNotEmpty(event.eventLink)) {
                                val intent = Intent(activity, EventDetailActivity::class.java)
                                intent.putExtra(Const.DATA, event)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                startActivity(intent)
                            } else {

                                if(event.isPlus != null && event.isPlus!! && event.pageSeqNo != null && event.agreement2 != null && event.agreement2!! == 1){
                                    val params = HashMap<String, String>()
                                    params["pageSeqNo"] = event.pageSeqNo.toString()
                                    showProgress("")
                                    ApiBuilder.create().getOnlyPlus(params).setCallback(object : PplusCallback<NewResultResponse<Plus>> {
                                        override fun onResponse(call: Call<NewResultResponse<Plus>>?, response: NewResultResponse<Plus>?) {
                                            hideProgress()
                                            if (!isAdded) {
                                                return
                                            }
                                            if(response?.data == null || response.data.agreement == null || !response.data.agreement!!){
                                                val intent = Intent(activity, Alert3rdPartyInfoTermsActivity::class.java)
                                                intent.putExtra(Const.EVENT, event)
                                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                                eventAgreeLauncher.launch(intent)
                                            }else{
                                                joinEvent(event)
                                            }
                                        }

                                        override fun onFailure(call: Call<NewResultResponse<Plus>>?, t: Throwable?, response: NewResultResponse<Plus>?) {
                                            hideProgress()
                                        }
                                    }).build().call()
                                }else{
                                    joinEvent(event)
                                }
                            }
                        }
                    }

                } else {
                    showAlert(R.string.msg_not_exist_pr_number)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Event>>?, t: Throwable?, response: NewResultResponse<Event>?) {

                hideProgress()
                showAlert(R.string.msg_not_exist_pr_number)
            }
        }).build().call()
    }

    fun joinEvent(event: Event) {
        val params = HashMap<String, String>()
        params["no"] = event.no.toString()
        showProgress("")

        ApiBuilder.create().joinEvent(params).setCallback(object : PplusCallback<NewResultResponse<EventResult>> {
            override fun onResponse(call: Call<NewResultResponse<EventResult>>?, response: NewResultResponse<EventResult>?) {

                hideProgress()
                if (!isAdded) {
                    return
                }
                if (response!!.data != null) {
                    if (event.winAnnounceType.equals(EventType.WinAnnounceType.immediately.name)) {
                        val mLoading = EventLoadingView()
                        mLoading.isCancelable = false
                        mLoading.setText(getString(R.string.msg_checking_event_result))
                        var delayTime = 2000L
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
                            startActivity(intent)
                        }, delayTime)

                    } else {
                        val intent = Intent(activity, EventResultActivity::class.java)
                        intent.putExtra(Const.EVENT_RESULT, response.data)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        startActivity(intent)
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<EventResult>>?, t: Throwable?, response: NewResultResponse<EventResult>?) {

                hideProgress()
                if (!isAdded) {
                    return
                }

                if (response != null) {
                    PplusCommonUtil.showEventAlert(activity!!, response.resultCode, event)
                }
            }
        }).build().call()
    }

    fun getPage() {
        val params = HashMap<String, String>()
        params["search"] = mNumberSb.toString()
        showProgress("")
        ApiBuilder.create().getPageByNumber(params).setCallback(object : PplusCallback<NewResultResponse<Page>> {
            override fun onResponse(call: Call<NewResultResponse<Page>>?, response: NewResultResponse<Page>?) {
                hideProgress()
                if (!isAdded) {
                    return
                }

                if (response?.data != null) {
//                    layout_main_pad_page?.visibility = View.VISIBLE
//                    text_main_pad_desc?.visibility = View.GONE
//                    setPageView(response.data)
//                    layout_main_pad_page?.setOnClickListener {
//
//                        mNumberSb = StringBuilder()
//                        binding.textMainPadNumber.text = ""
//
//                        val location = IntArray(2)
//                        it.getLocationOnScreen(location)
//                        val x = location[0] + it.width / 2
//                        val y = location[1] + it.height / 2
//                        PplusCommonUtil.goPage(activity!!, response.data!!, x, y)
//                    }

                    resetNumber()
                    PplusCommonUtil.goPage(activity!!, response.data!!, 0, 0)
                } else {
                    showAlert(R.string.msg_not_exist_pr_number)
//                    layout_main_pad_page?.visibility = View.GONE
//                    text_main_pad_desc?.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Page>>?, t: Throwable?, response: NewResultResponse<Page>?) {
                hideProgress()
                showAlert(R.string.msg_not_exist_pr_number)
//                layout_main_pad_page?.visibility = View.GONE
//                text_main_pad_desc?.visibility = View.VISIBLE
            }
        }).build().call()
    }

    private fun getCount() {

        val params = HashMap<String, String>()
        if (mNumberSb.isNotEmpty()) {
            params["search"] = mNumberSb.toString()
        }
        params["onlyPoint"] = "false"
        mLockListView = true
        ApiBuilder.create().getPageCount(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {

            override fun onResponse(call: Call<NewResultResponse<Int>>, response: NewResultResponse<Int>) {
                if (!isAdded) {
                    return
                }

                mTotalCount = response.data

                if (mTotalCount > 0) {
//                    pager_main_pad?.visibility = View.GONE
//                    text_main_pad_desc?.visibility = View.GONE
                    binding.recyclerMainPad.visibility = View.VISIBLE
                } else {
//                    pager_main_pad?.visibility = View.VISIBLE
//                    text_main_pad_desc?.visibility = View.VISIBLE
                    binding.recyclerMainPad.visibility = View.GONE
                }
                mPaging = 1
                listCall(mPaging)
            }

            override fun onFailure(call: Call<NewResultResponse<Int>>, t: Throwable, response: NewResultResponse<Int>) {

            }
        }).build().call()
    }

    private fun listCall(page: Int) {

        val params = HashMap<String, String>()
        if (mNumberSb.isNotEmpty()) {
            params["search"] = mNumberSb.toString()
        }

        if (LocationUtil.specifyLocationData != null) {
            params["latitude"] = LocationUtil.specifyLocationData!!.getLatitude().toString()
            params["longitude"] = LocationUtil.specifyLocationData!!.getLongitude().toString()
            mSortCode = SortCode.distance
        }

        params["align"] = mSortCode.name
        mLockListView = true
        params["pg"] = "" + page
        params["onlyPoint"] = "false"
//        showProgress("")
        ApiBuilder.create().getPageList(params).setCallback(object : PplusCallback<NewResultResponse<Page>> {

            override fun onResponse(call: Call<NewResultResponse<Page>>, response: NewResultResponse<Page>) {
                if (!isAdded) {
                    return
                }
                mLockListView = false
//                hideProgress()
                if (page == 1) {
                    mAdapter!!.clear()
                    mAdapter!!.setSearchNumber(mNumberSb.toString())
                }
                mAdapter!!.addAll(response.datas)
            }

            override fun onFailure(call: Call<NewResultResponse<Page>>, t: Throwable, response: NewResultResponse<Page>) {

                mLockListView = false
//                hideProgress()
            }
        }).build().call()
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

    //    fun close(): Boolean {
//        scrollOutAnimation(true, false)
//        return layout_main_pad_bottom.isShown
//    }

    var animating = false

    private fun scrollOutAnimation() {

        val scrollOutAnimator = ObjectAnimator.ofFloat(binding.layoutMainPadBottom, "translationY", 0f,
            binding.layoutMainNumberPad.height.toFloat()).apply {
            duration = 300
            interpolator = AccelerateDecelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    animating = true
                }

                override fun onAnimationCancel(animation: Animator?) {
                    animating = true
                }

                override fun onAnimationEnd(animation: Animator?) {
                    animating = false
                    if (!isAdded) {
                        return
                    }

                    binding.imageMainPadUpDown.visibility = View.VISIBLE
                    binding.imageMainPadUpDown.setImageResource(R.drawable.btn_number_up)
                    binding.layoutMainNumberPad.visibility = View.GONE
                    val ani = ObjectAnimator.ofFloat(binding.layoutMainPadBottom, "translationY",
                            0f, 0f)
                    ani.start()


//                    if (isClose) {
//                        if (parentActivity is AppMainActivity2) {
//                            if(isLeft){
//                                (parentActivity as AppMainActivity2).setEventFragment()
//                            }else{
//                                (parentActivity as AppMainActivity2).setPlusGoodsFragment()
//                            }
//
//
//                        }
//                    }
                }
            })
        }

        if (!animating) {
//            unlockbar_main_pad.img_center.visibility = View.INVISIBLE
//            unlockbar_main_pad.img_thumb.visibility = View.INVISIBLE
//            if(isLeft){
//                (parentActivity as AppMainActivity2).centerToLeftAnimation()
//            }else{
//                (parentActivity as AppMainActivity2).centerToRightAnimation()
//            }
            scrollOutAnimator.start()
        }
    }

    fun scrollInAnimation() {
        val scrollInAnimator = ObjectAnimator.ofFloat(binding.layoutMainPadBottom, "translationY",
            binding.layoutMainNumberPad.height.toFloat(), 0f).apply {
            duration = 300
            interpolator = AccelerateDecelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    if (!isAdded) {
                        return
                    }

                    binding.layoutMainNumberPad.visibility = View.VISIBLE
                    animating = true
                }

                override fun onAnimationCancel(animation: Animator?) {
                    animating = true
                }

                override fun onAnimationEnd(animation: Animator?) {
                    LogUtil.e(LOG_TAG, "onAnimationEnd : scroll in")

                    if (!isAdded) {
                        return
                    }
                    binding.imageMainPadUpDown.visibility = View.GONE
                    binding.imageMainPadUpDown.setImageResource(R.drawable.btn_number_down)
//                    parentActivity.layout_main_floating.visibility = View.GONE
//                    unlockbar_main_pad?.img_center?.visibility = View.VISIBLE
//                    unlockbar_main_pad?.img_thumb?.visibility = View.VISIBLE

                    animating = false
                }
            })
        }

        if (!animating) {

            scrollInAnimator.start()
        }
    }

    override fun getPID(): String {
        return "Main_number"
    }

    var key: String? = null
    var number: String? = null

    @Subscribe
    fun setPlus(data: BusProviderData) {
        if (data.type == BusProviderData.BUS_MAIN && data.subData is Page) {
            mAdapter!!.setBusPlus(data)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        BusProvider.getInstance().unregister(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BusProvider.getInstance().register(this)
        arguments?.let {
            key = it.getString(Const.KEY)
            number = it.getString(Const.NUMBER)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(key: String?, number: String?) =
                MainPadFragment().apply {
                    arguments = Bundle().apply {
                        putString(Const.KEY, key)
                        putString(Const.NUMBER, number)
                    }
                }
    }
}
