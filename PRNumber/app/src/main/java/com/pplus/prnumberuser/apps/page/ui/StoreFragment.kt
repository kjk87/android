//package com.pplus.prnumberuser.apps.page.ui
//
//
//import android.animation.Animator
//import android.animation.AnimatorListenerAdapter
//import android.app.Activity.RESULT_OK
//import android.content.ClipData
//import android.content.ClipboardManager
//import android.content.Context
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.graphics.Rect
//import android.net.Uri
//import android.os.Bundle
//import android.view.View
//import android.view.ViewAnimationUtils
//import android.view.ViewTreeObserver
//import androidx.annotation.DimenRes
//import androidx.recyclerview.widget.GridLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.request.RequestOptions
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.OnMapReadyCallback
//import com.google.android.gms.maps.model.BitmapDescriptorFactory
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.gms.maps.model.MarkerOptions
//import com.google.android.material.appbar.AppBarLayout
//import com.kakao.sdk.common.util.KakaoCustomTabsClient
//import com.kakao.sdk.navi.NaviClient
//import com.kakao.sdk.navi.model.CoordType
//import com.kakao.sdk.navi.model.Location
//import com.kakao.sdk.navi.model.NaviOption
//import com.pplus.networks.common.PplusCallback
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
//import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
//import com.pplus.prnumberuser.apps.common.builder.data.AlertData
//import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
//import com.pplus.prnumberuser.apps.menu.ui.CategoryMenuActivity
//import com.pplus.prnumberuser.apps.product.data.ProductShipAdapter
//import com.pplus.prnumberuser.apps.product.ui.ProductShipDetailActivity
//import com.pplus.prnumberuser.core.code.common.SnsTypeCode
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.*
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.network.model.response.SubResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.prnumberuser.core.util.ToastUtil
//import com.pplus.utils.part.info.OsUtil
//import com.pplus.utils.part.logs.LogUtil
//import com.pplus.utils.part.utils.StringUtils
//import kotlinx.android.synthetic.main.fragment_store.*
//import kotlinx.android.synthetic.main.fragment_store.collapsing_page
//import kotlinx.android.synthetic.main.layout_store_info.*
//import retrofit2.Call
//import java.util.*
//
//class StoreFragment : BaseFragment<PageActivity>() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            mPage = it.getParcelable(Const.PAGE)!!
////            param2 = it.getString(ARG_PARAM2)
//        }
//    }
//
//    override fun getLayoutResourceId(): Int {
//        return R.layout.fragment_store
//    }
//
//    override fun initializeView(container: View?) {
//
//    }
//
////    var mPageImagePagerAdapter: PageImagePagerAdapter? = null
//    private lateinit var mPage: Page
//
//    private var mTotalCount: Int = 0
//    private var mLockListView = false
//    private var mPaging = 0
//    private var mAdapter: ProductShipAdapter? = null
//    private var mLayoutManager: GridLayoutManager? = null
//    private var mIsLast = false
//
//    private fun revealShow(view: View, x: Int, y: Int) {
//
//        val w = view.width
//        val h = view.height
//
//        val endRadius = (Math.max(w, h) * 1.1);
//
//        val revealAnimator = ViewAnimationUtils.createCircularReveal(view, x, y, 0f, endRadius.toFloat())
//
//        view.visibility = View.VISIBLE
//
//        revealAnimator.addListener(object : AnimatorListenerAdapter() {
//            override fun onAnimationEnd(animation: Animator) {
//                super.onAnimationEnd(animation)
//            }
//        })
//        revealAnimator.duration = 700
//        revealAnimator.start()
//
//    }
//
//    override fun init() {
//        LogUtil.e(LOG_TAG, "init")
//        if (OsUtil.isLollipop()) {
//            val viewTreeObserver = layout_store_parent.viewTreeObserver
//            if (viewTreeObserver.isAlive) {
//                viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
//                    override fun onGlobalLayout() {
//                        revealShow(layout_store_parent, getParentActivity().x, getParentActivity().y)
//                        layout_store_parent.viewTreeObserver.removeOnGlobalLayoutListener(this)
//                    }
//                })
//            }
//        }
//
//        appbar_store.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
//            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
//                if (!isAdded) {
//                    return
//                }
//
//                if (verticalOffset <= -collapsing_page.height + toolbar_store.height) {
//                    //toolbar is collapsed here
//                    //write your code here
//                    text_store_name2.visibility = View.VISIBLE
////                    layout_page_plus.layoutParams.height = resources.getDimensionPixelSize(R.dimen.height_140)
//                } else {
//                    text_store_name2.visibility = View.GONE
////                    layout_page_plus.layoutParams.height = resources.getDimensionPixelSize(R.dimen.height_230)
//                }
//            }
//        })
//
//        image_store_back.setOnClickListener {
//            activity?.finish()
//        }
//
//        mLayoutManager = GridLayoutManager(activity, 2)
//        recycler_store_goods.layoutManager = mLayoutManager!!
//        mAdapter = ProductShipAdapter()
//        recycler_store_goods.adapter = mAdapter
//        recycler_store_goods.addItemDecoration(CustomItemOffsetDecoration(activity!!, R.dimen.height_60))
//        recycler_store_goods.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//
//            internal var pastVisibleItems: Int = 0
//            internal var visibleItemCount: Int = 0
//            internal var totalItemCount: Int = 0
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//
//                super.onScrolled(recyclerView, dx, dy)
//
//                visibleItemCount = mLayoutManager!!.childCount
//                totalItemCount = mLayoutManager!!.itemCount
//                pastVisibleItems = mLayoutManager!!.findFirstVisibleItemPosition()
//                if (!mLockListView) {
//                    if (!mIsLast && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
//                        mPaging++
//                        listCall(mPaging)
//                    }
//                }
//            }
//        })
//
//        mAdapter!!.setOnItemClickListener(object : ProductShipAdapter.OnItemClickListener {
//            override fun changeLike() {
//
//            }
//
//            override fun onItemClick(position: Int, view: View) {
//
////                if (!PplusCommonUtil.loginCheck(activity!!)) {
////                    return
////                }
//
//                val intent = Intent(activity, ProductShipDetailActivity::class.java)
//                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
//                activity?.startActivityForResult(intent, Const.REQ_DETAIL)
//            }
//        })
//
//        layout_store_goods_tab.setOnClickListener {
//            recycler_store_goods.visibility = View.VISIBLE
//            scroll_store_info.visibility = View.GONE
//            layout_store_goods_tab.isSelected = true
//            layout_store_info_tab.isSelected = false
//            if(mTotalCount > 0){
//                layout_store_goods_not_exist?.visibility = View.GONE
//            }else{
//                layout_store_goods_not_exist?.visibility = View.VISIBLE
//            }
//        }
//
//        layout_store_info_tab.setOnClickListener {
//            recycler_store_goods.visibility = View.GONE
//            scroll_store_info.visibility = View.VISIBLE
//            layout_store_goods_tab.isSelected = false
//            layout_store_info_tab.isSelected = true
//            layout_store_goods_not_exist?.visibility = View.GONE
//        }
//
//        recycler_store_goods.visibility = View.GONE
//        scroll_store_info.visibility = View.VISIBLE
//        layout_store_goods_tab.isSelected = false
//        layout_store_info_tab.isSelected = true
//        layout_store_goods_not_exist?.visibility = View.GONE
//
//        getPage()
//    }
//
//    private fun getSnsLink() {
//
//        val params = HashMap<String, String>()
//        params["no"] = "" + mPage.no!!
//        showProgress("")
//        ApiBuilder.create().getSnsLinkAll(params).setCallback(object : PplusCallback<NewResultResponse<Sns>> {
//
//            override fun onResponse(call: Call<NewResultResponse<Sns>>, response: NewResultResponse<Sns>) {
//                hideProgress()
//                if (!isAdded) {
//                    return
//                }
//
//                image_store_facebook?.visibility = View.GONE
//                image_store_twitter?.visibility = View.GONE
//                image_store_naver?.visibility = View.GONE
//                image_store_instagram?.visibility = View.GONE
//                image_store_homepage?.visibility = View.GONE
//                image_store_youtube?.visibility = View.GONE
//                image_store_kakaochannel?.visibility = View.GONE
//
//                val snsList = response.datas
//
//                var existSns = false
//
//                if (snsList != null && !snsList.isEmpty()) {
//                    for (sns in snsList) {
//                        if (StringUtils.isNotEmpty(sns.url)) {
//                            when (sns.type) {
//
////                                SnsTypeCode.twitter.name -> {
////                                    image_page_twitter.visibility = View.VISIBLE
////                                    image_page_twitter.tag = sns
////                                    image_page_twitter.setOnClickListener(onSnsClickListener)
////                                }
////                                SnsTypeCode.facebook.name -> {
////                                    image_page_facebook.visibility = View.VISIBLE
////                                    image_page_facebook.tag = sns
////                                    image_page_facebook.setOnClickListener(onSnsClickListener)
////                                }
//                                SnsTypeCode.naver.name -> {
//                                    existSns = true
//                                    image_store_naver.visibility = View.VISIBLE
//                                    image_store_naver.tag = sns
//                                    image_store_naver.setOnClickListener(onSnsClickListener)
//                                }
//                                SnsTypeCode.instagram.name -> {
//                                    existSns = true
//                                    image_store_instagram.visibility = View.VISIBLE
//                                    image_store_instagram.tag = sns
//                                    image_store_instagram.setOnClickListener(onSnsClickListener)
//                                }
////                                SnsTypeCode.kakaoStory.name -> {
////                                    image_page_kakao.visibility = View.VISIBLE
////                                    image_page_kakao.tag = sns
////                                    image_page_kakao.setOnClickListener(onSnsClickListener)
////                                }
////                                SnsTypeCode.blog.name -> {
////                                    image_page_blog.visibility = View.VISIBLE
////                                    image_page_blog.isSelected = true
////                                    image_page_blog.tag = sns
////                                    image_page_blog.setOnClickListener(onSnsClickListener)
////                                }
//                                SnsTypeCode.homepage.name -> {
//                                    existSns = true
//                                    image_store_homepage.visibility = View.VISIBLE
//                                    image_store_homepage.tag = sns
//                                    image_store_homepage.setOnClickListener(onSnsClickListener)
//                                }
//                                SnsTypeCode.youtube.name -> {
//                                    existSns = true
//                                    image_store_youtube.visibility = View.VISIBLE
//                                    image_store_youtube.tag = sns
//                                    image_store_youtube.setOnClickListener(onSnsClickListener)
//                                }
//                                SnsTypeCode.kakaoChannel.name -> {
//                                    existSns = true
//                                    image_store_kakaochannel.visibility = View.VISIBLE
//                                    image_store_kakaochannel.tag = sns
//                                    image_store_kakaochannel.setOnClickListener(onSnsClickListener)
//                                }
//                            }
//                        }
//                    }
//                }
//
//                if(existSns){
//                    layout_store_sns.visibility = View.VISIBLE
//                }else{
//                    layout_store_sns.visibility = View.GONE
//                }
//
////                mPaging = 0
////                listCall(mPaging)
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Sns>>, t: Throwable, response: NewResultResponse<Sns>) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    private val onSnsClickListener = View.OnClickListener { v ->
//        val sns = v.tag as Sns
//        snsEvent(sns)
//    }
//
//    private fun snsEvent(sns: Sns) {
//        // SNS 페이지 이동
//        if (StringUtils.isNotEmpty(sns.url)) {
//            // 계정으로 이동
//            startActivity(PplusCommonUtil.getOpenSnsIntent(activity!!, SnsTypeCode.valueOf(sns.type), sns.url, sns.isLinkage))
//        }
//    }
//
//    private fun listCall(page: Int) {
//
//        mLockListView = true
//        val params = HashMap<String, String>()
//        params["pageSeqNo"] = mPage.no.toString()
//        params["page"] = page.toString()
//        params["sort"] = "seqNo,desc"
//        showProgress("")
//        ApiBuilder.create().getProductPriceListShipTypeByPageSeqNoOnlyNormal(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<ProductPrice>>> {
//            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<ProductPrice>>>?,
//                                    response: NewResultResponse<SubResultResponse<ProductPrice>>?) {
//                hideProgress()
//                if (!isAdded) {
//                    return
//                }
//
//                if (response?.data != null) {
//                    mIsLast = response.data.last!!
//                    if (response.data.first!!) {
//                        mTotalCount = response.data.totalElements!!
//
//                        if(mTotalCount > 0){
//                            layout_store_goods_not_exist?.visibility = View.GONE
//                        }else{
//                            layout_store_goods_not_exist?.visibility = View.VISIBLE
//                        }
//
//                        mAdapter!!.clear()
//                    }
//
//                    mLockListView = false
//                    mAdapter!!.addAll(response.data.content!!)
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<ProductPrice>>>?,
//                                   t: Throwable?,
//                                   response: NewResultResponse<SubResultResponse<ProductPrice>>?) {
//                if (!isAdded) {
//                    return
//                }
//                hideProgress()
//                mLockListView = false
//            }
//
//        }).build().call()
//    }
//
//    private inner class CustomItemOffsetDecoration(private val mItemOffset: Int) : RecyclerView.ItemDecoration() {
//
//        constructor(context: Context, @DimenRes lastOffsetId: Int) : this(context.resources.getDimensionPixelSize(lastOffsetId)) {}
//
//        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
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
//    private fun getPage() {
//
//        val params = HashMap<String, String>()
//        params["no"] = mPage.no.toString()
//        showProgress("")
//        ApiBuilder.create().getPage(params).setCallback(object : PplusCallback<NewResultResponse<Page?>> {
//
//            override fun onResponse(call: Call<NewResultResponse<Page?>>?, response: NewResultResponse<Page?>?) {
//                hideProgress()
//
//                if (!isAdded) {
//                    return
//                }
//
//                if (response?.data != null) {
//                    mPage = response.data!!
//                    setData()
//                }
//
//                getSnsLink()
//
//                mPaging = 0
//                listCall(mPaging)
//
//                getBusinessLicense()
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Page?>>?, t: Throwable?, response: NewResultResponse<Page?>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    private fun getBusinessLicense() {
//        val params = HashMap<String, String>()
//        params["pageSeqNo"] = mPage.no.toString()
//        showProgress("")
//        ApiBuilder.create().getBusinessLicense(params).setCallback(object : PplusCallback<NewResultResponse<BusinessLicense>> {
//            override fun onResponse(call: Call<NewResultResponse<BusinessLicense>>?, response: NewResultResponse<BusinessLicense>?) {
//                hideProgress()
//                if (!isAdded) {
//                    return
//                }
//                if (response?.data != null) {
//                    val businessLicense = response.data!!
//
//                    text_store_company_name?.text = businessLicense.companyName
//                    text_store_ceo?.text = businessLicense.ceo
//                    text_store_company_number?.text = businessLicense.corporateNumber
//                    text_store_company_address?.text = businessLicense.companyAddress
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<BusinessLicense>>?, t: Throwable?, response: NewResultResponse<BusinessLicense>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//
//    private fun setData() {
//
//        text_store_name2?.text = mPage.name
//        text_store_name?.text = mPage.name
//
//
////        if (mPage.numberList != null && mPage.numberList!!.isNotEmpty()) {
////            text_home_number.text = PplusNumberUtil.getPrNumberFormat(mPage.numberList!![0].number)
////        }
//
//        Glide.with(activity!!).load(mPage.thumbnail).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_page_profile_default).error(R.drawable.img_page_profile_default)).into(image_store_profile)
//
//        if (mPage.plus!!) {
//            layout_store_plus.visibility = View.GONE
//        } else {
//            layout_store_plus.visibility = View.VISIBLE
//        }
//
//        layout_store_plus.setOnClickListener {
//            if (!PplusCommonUtil.loginCheck(activity!!)) {
//                return@setOnClickListener
//            }
//            if (!mPage.plus!!) {
//                val builder = AlertBuilder.Builder()
//                builder.setTitle(getString(R.string.word_notice_alert))
//                builder.addContents(AlertData.MessageData(getString(R.string.format_msg_question_plus, mPage.name), AlertBuilder.MESSAGE_TYPE.TEXT, 4))
//                builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
//                builder.setOnAlertResultListener(object : OnAlertResultListener {
//
//                    override fun onCancel() {
//
//                    }
//
//                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//
//                        when (event_alert) {
//                            AlertBuilder.EVENT_ALERT.RIGHT -> {
//                                val params = Plus()
//                                params.no = mPage.no
//                                showProgress("")
//                                ApiBuilder.create().insertPlus(params).setCallback(object : PplusCallback<NewResultResponse<Plus>> {
//
//                                    override fun onResponse(call: Call<NewResultResponse<Plus>>, response: NewResultResponse<Plus>) {
//
//                                        hideProgress()
//                                        ToastUtil.show(activity, getString(R.string.msg_plus_ing))
//                                        mPage.plus = true
//                                        layout_store_plus.visibility = View.GONE
//                                    }
//
//                                    override fun onFailure(call: Call<NewResultResponse<Plus>>, t: Throwable, response: NewResultResponse<Plus>) {
//
//                                        hideProgress()
//                                    }
//                                }).build().call()
//                            }
//                        }
//
//                    }
//                }).builder().show(activity)
//            }
//        }
//
//        text_store_catchphrase.text = mPage.catchphrase
//
//        if (StringUtils.isNotEmpty(mPage.introduction)) {
//            layout_store_introduction.visibility = View.VISIBLE
//
//            text_store_introduction.text = mPage.introduction
//        } else {
//            layout_store_introduction.visibility = View.GONE
//        }
//
//        if (StringUtils.isNotEmpty(mPage.openHours)) {
//            layout_store_open_hours.visibility = View.VISIBLE
//            text_store_opening_hours.text = mPage.openHours
//        } else {
//            layout_store_open_hours.visibility = View.GONE
//        }
//
//        if (StringUtils.isNotEmpty(mPage.holiday)) {
//            layout_store_holiday.visibility = View.VISIBLE
//            text_store_holiday.text = mPage.holiday
//        } else {
//            layout_store_holiday.visibility = View.GONE
//        }
//
//        if (mPage.address == null || StringUtils.isEmpty(mPage.address!!.roadBase)) {
//            layout_store_address.visibility = View.GONE
//            layout_store_map_option.visibility = View.GONE
//            layout_store_map.visibility = View.GONE
//        } else {
//            layout_store_address.visibility = View.VISIBLE
//            var detailAddress = ""
//            if (StringUtils.isNotEmpty(mPage.address!!.roadDetail)) {
//                detailAddress = mPage.address!!.roadDetail
//            }
//            text_store_address.text = "${mPage.address!!.roadBase} $detailAddress"
//
//            if (mPage.latitude != null && mPage.longitude != null) {
//                layout_store_map.visibility = View.VISIBLE
//
//                map_store.onCreate(null)
//                map_store.getMapAsync(object : OnMapReadyCallback {
//                    override fun onMapReady(googMap: GoogleMap) {
//                        LogUtil.e("GOOGLEMAP", "onMapReady")
//                        googMap.uiSettings.isScrollGesturesEnabled = false
//                        googMap.uiSettings.isZoomGesturesEnabled = false
//                        val latLng = LatLng(mPage.latitude!!, mPage.longitude!!)
//                        googMap.addMarker(MarkerOptions().position(latLng).title(mPage.name).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location)))
//                        googMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
//
//                        googMap.setOnMapClickListener {
//                            val intent = Intent(activity, LocationPageActivity::class.java)
//                            intent.putExtra(Const.PAGE, mPage)
//                            activity?.startActivityForResult(intent, Const.REQ_LOCATION_CODE)
//                        }
//                    }
//                })
//
//                image_store_map_full.setOnClickListener {
//                    val intent = Intent(activity, LocationPageActivity::class.java)
//                    intent.putExtra(Const.PAGE, mPage)
//                    activity?.startActivityForResult(intent, Const.REQ_LOCATION_CODE)
//                }
//            } else {
//                layout_store_map.visibility = View.GONE
//            }
//        }
//
//        layout_store_find_road.setOnClickListener {
//            if (existDaummapApp()) {
//                val uri = Uri.parse("daummaps://route?ep=${mPage.latitude},${mPage.longitude}&by=CAR");
//                val intent = Intent(Intent.ACTION_VIEW, uri)
//                startActivity(intent)
//            } else {
//                val intent = Intent(Intent.ACTION_VIEW).setData(Uri.parse("market://details?id=net.daum.android.map"))
//                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                startActivity(intent)
//            }
//
//        }
//
//        layout_store_call_taxi.setOnClickListener {
//            val uri = Uri.parse("https://t.kakao.com/launch?type=taxi&dest_lat=${mPage.latitude}&dest_lng=${mPage.longitude}&ref=pplus")
//            val intent = Intent(Intent.ACTION_VIEW, uri)
//            startActivity(intent)
//        }
//
//        layout_store_navigation.setOnClickListener {
//            if (NaviClient.instance.isKakaoNaviInstalled(activity!!)) {
//                LogUtil.e("kakonavi", "카카오내비 앱으로 길안내 가능")
//                startActivity(
//                        NaviClient.instance.navigateIntent(
//                                Location(mPage!!.name!!, mPage!!.longitude.toString(), mPage!!.latitude.toString()),
//                                NaviOption(coordType = CoordType.WGS84)
//                        )
//                )
//            } else {
//                LogUtil.e("kakonavi", "카카오내비 미설치: 웹 길안내 사용 권장")
//
//                // 웹 브라우저에서 길안내
//                // 카카오내비가 설치되지 않은 곳에서 활용할 수 있습니다.
//                val uri =
//                        NaviClient.instance.navigateWebUrl(
//                                Location(mPage!!.name!!, mPage!!.longitude.toString(), mPage!!.latitude.toString()),
//                                NaviOption(coordType = CoordType.WGS84)
//                        )
//
//                // CustomTabs로 길안내
//                KakaoCustomTabsClient.openWithDefault(activity!!, uri)
//            }
//        }
//
//        layout_store_copy_address.setOnClickListener {
//            if (mPage.address != null && StringUtils.isNotEmpty(mPage.address!!.roadBase)) {
//                var detailAddress = ""
//                if (StringUtils.isNotEmpty(mPage.address!!.roadDetail)) {
//                    detailAddress = mPage.address!!.roadDetail
//                }
//
//                val clipboard = activity!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//
//                val clip = ClipData.newPlainText("address", "${mPage.address!!.roadBase} $detailAddress")
//                clipboard.setPrimaryClip(clip)
//                ToastUtil.show(activity, R.string.msg_copied_clipboard)
//            } else {
//                ToastUtil.showAlert(activity, getString(R.string.msg_not_exist_address))
//            }
//        }
//
//        if (StringUtils.isNotEmpty(mPage.phone)) {
//            text_sore_call.visibility = View.VISIBLE
//            text_sore_call.setOnClickListener {
//                if (StringUtils.isNotEmpty(mPage.phone)) {
//                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mPage.phone!!))
//                    startActivity(intent)
//                }
//            }
//        } else {
//            text_sore_call.visibility = View.GONE
//        }
//
//        if (StringUtils.isNotEmpty(mPage.email)) {
//            layout_store_email.visibility = View.VISIBLE
//            text_store_email.text = mPage.email
//        } else {
//            layout_store_email.visibility = View.GONE
//        }
//
//    }
//
//    private fun existDaummapApp(): Boolean {
//        val pm = activity!!.packageManager
//
//        try {
//            return (pm.getPackageInfo("net.daum.android.map", PackageManager.GET_SIGNING_CERTIFICATES) != null)
//        } catch (e: PackageManager.NameNotFoundException) {
//            return false;
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            Const.REQ_ORDER_TYPE -> {
//                if (resultCode == RESULT_OK) {
//                    if (data != null) {
//                        val type = data.getIntExtra(Const.TYPE, 0)
//                        val intent = Intent(activity, CategoryMenuActivity::class.java)
//                        intent.putExtra(Const.PAGE, mPage)
//                        intent.putExtra(Const.TYPE, type)
//                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                        startActivity(intent)
//                    }
//                }
//            }
//            Const.REQ_REVIEW -> {
//            }
//        }
//    }
//
//    private fun share() {
//
//        val shareText = mPage.catchphrase + "\n" + getString(R.string.format_msg_page_url, "index.php?no=" + mPage.no!!)
//
//        val intent = Intent(Intent.ACTION_SEND)
//        intent.action = Intent.ACTION_SEND
//        intent.type = "text/plain"
//        intent.putExtra(Intent.EXTRA_TEXT, shareText)
//        val chooserIntent = Intent.createChooser(intent, getString(R.string.word_share_page))
//        startActivity(chooserIntent)
//    }
//
//    override fun getPID(): String {
//        return ""
//    }
//
//    companion object {
//
//        @JvmStatic
//        fun newInstance(page: Page) =
//                StoreFragment().apply {
//                    arguments = Bundle().apply {
//                        putParcelable(Const.PAGE, page)
//                    }
//                }
//    }
//}
