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
//import android.net.Uri
//import android.os.Bundle
//import android.view.View
//import android.view.ViewAnimationUtils
//import android.view.ViewTreeObserver
//import android.widget.LinearLayout
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.OnMapReadyCallback
//import com.google.android.gms.maps.model.BitmapDescriptorFactory
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.gms.maps.model.MarkerOptions
//import com.google.android.material.appbar.AppBarLayout
//import com.kakao.kakaonavi.KakaoNaviParams
//import com.kakao.kakaonavi.KakaoNaviService
//import com.kakao.kakaonavi.Location
//import com.kakao.kakaonavi.NaviOptions
//import com.kakao.kakaonavi.options.CoordType
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.info.OsUtil
//import com.pplus.utils.part.logs.LogUtil
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
//import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
//import com.pplus.prnumberuser.apps.common.builder.data.AlertData
//import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
//import com.pplus.prnumberuser.apps.menu.ui.CategoryMenuActivity
//import com.pplus.prnumberuser.apps.page.data.PageImagePagerAdapter
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.code.common.SnsTypeCode
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.*
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.network.model.response.SubResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.prnumberuser.core.util.ToastUtil
//import kotlinx.android.synthetic.main.fragment_page2.*
//import kotlinx.android.synthetic.main.layout_pplus_info.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.util.*
//
//class PageFragment2 : BaseFragment<PageActivity>() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            mPage = it.getParcelable(Const.PAGE)!!
////            param2 = it.getString(ARG_PARAM2)
//        }
//    }
//
//    override fun getLayoutResourceId(): Int {
//        return R.layout.fragment_page2
//    }
//
//    override fun initializeView(container: View?) {
//
//    }
//
////    var mIntroPagerAdapter: IntroduceImagePagerAdapter? = null
//    var mPageImagePagerAdapter: PageImagePagerAdapter? = null
//    private lateinit var mPage: Page
//    private var attachmentList: MutableList<Attachment>? = null
//    private var mImageList: MutableList<Attachment>? = null
//    private var mPageImageList: MutableList<PageImage>? = null
//
////    private var mPaging: Int = 0
////    private var mTotalCount = 0
////    private var mLayoutManager: LinearLayoutManager? = null
////    private var mLockListView = true
////    private var mAdapter: PageHeaderAdapter? = null
////    private var mIsLast = false
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
//            val viewTreeObserver = layout_page_parent.viewTreeObserver
//            if (viewTreeObserver.isAlive) {
//                viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
//                    override fun onGlobalLayout() {
//                        revealShow(layout_page_parent, getParentActivity().x, getParentActivity().y)
//                        layout_page_parent.viewTreeObserver.removeOnGlobalLayoutListener(this)
//                    }
//                })
//            }
//        }
//
//        appbar_page.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
//            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
//                if (!isAdded) {
//                    return
//                }
//
//                if (verticalOffset <= -collapsing_page.height + toolbar_page.height) {
//                    //toolbar is collapsed here
//                    //write your code here
//                    text_page_name2.visibility = View.VISIBLE
////                    layout_page_plus.layoutParams.height = resources.getDimensionPixelSize(R.dimen.height_140)
//                } else {
//                    text_page_name2.visibility = View.GONE
////                    layout_page_plus.layoutParams.height = resources.getDimensionPixelSize(R.dimen.height_230)
//                }
//            }
//        })
//
//        mPageImagePagerAdapter = PageImagePagerAdapter(activity!!)
//        pager_page_introduce_image.adapter = mPageImagePagerAdapter
//        mPageImagePagerAdapter!!.setListener(object : PageImagePagerAdapter.OnItemClickListener{
//            override fun onItemClick(position: Int) {
//                val intent = Intent(activity, IntroduceImageDetailActivity::class.java)
//                intent.putExtra(Const.DATA, mPageImagePagerAdapter!!.getDataList() as ArrayList<PageImage>)
//                intent.putExtra(Const.POSITION, position)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                activity!!.startActivityForResult(intent, Const.REQ_INTRODUCE_IMAGE_DETAIL)
//            }
//        })
//
////        mIntroPagerAdapter = IntroduceImagePagerAdapter(activity!!)
////        pager_page_introduce_image.adapter = mIntroPagerAdapter
////        mIntroPagerAdapter!!.setListener(object : IntroduceImagePagerAdapter.OnItemClickListener{
////            override fun onItemClick(position: Int) {
////                if (StringUtils.isNotEmpty(mIntroPagerAdapter!!.getData(position).targetType) && mIntroPagerAdapter!!.getData(position).targetType == "youtube") {
////
////                } else {
////                    val intent = Intent(activity, IntroduceImageDetailActivity::class.java)
////                    intent.putExtra(Const.DATA, mIntroPagerAdapter!!.getDataList() as ArrayList<Attachment>)
////                    intent.putExtra(Const.POSITION, position)
////                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
////                    activity!!.startActivityForResult(intent, Const.REQ_INTRODUCE_IMAGE_DETAIL)
////                }
////            }
////        })
//
//
//        indicator_page_introduce_image.visibility = View.GONE
//        pager_page_introduce_image.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
//
//            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//
//            }
//
//            override fun onPageSelected(position: Int) {
//
//                indicator_page_introduce_image.setCurrentItem(position)
//            }
//
//            override fun onPageScrollStateChanged(state: Int) {
//
//            }
//        })
//
//        layout_page_plus_goods.setOnClickListener {
//            if(!PplusCommonUtil.loginCheck(activity!!)){
//                return@setOnClickListener
//            }
//            if (!mPage.plus!!) {
//
//                val builder = AlertBuilder.Builder()
//                builder.setTitle(getString(R.string.word_notice_alert))
//                builder.addContents(AlertData.MessageData(getString(R.string.format_msg_question_plus2, mPage.name), AlertBuilder.MESSAGE_TYPE.TEXT, 4))
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
//                                        layout_page_plus.visibility = View.GONE
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
//
//                return@setOnClickListener
//            }
//
//            val intent = Intent(activity, PagePlusGoodsActivity::class.java)
//            intent.putExtra(Const.PAGE, mPage)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
//        layout_page_goods.setOnClickListener {
//            val intent = Intent(activity, CategoryMenuActivity::class.java)
//            intent.putExtra(Const.PAGE, mPage)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
//        layout_page_review.setOnClickListener {
//            val intent = Intent(activity, PageReviewActivity::class.java)
//            intent.putExtra(Const.PAGE, mPage)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
//        image_page_back.setOnClickListener {
//            activity?.finish()
//        }
//
//
//        getPage()
//    }
//
////    private inner class CustomItemOffsetDecoration(private val mItemOffset: Int, private val mLastOffset: Int) : RecyclerView.ItemDecoration() {
////
////        constructor(context: Context, @DimenRes itemOffsetId: Int, @DimenRes lastOffsetId: Int) : this(context.resources.getDimensionPixelSize(itemOffsetId), context.resources.getDimensionPixelSize(lastOffsetId)) {}
////
////        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
////
////            super.getItemOffsets(outRect, view, parent, state)
////
////            val position = parent.getChildAdapterPosition(view)
////            outRect.set(0, 0, 0, mItemOffset)
////            if (position == mTotalCount - 1) {
////                outRect.bottom = mLastOffset
////            }
////
////        }
////    }
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
//                if (response!!.data != null) {
//                    mPage = response.data!!
//
//                    getGoodsCount()
//                    getPlusGoodsCount()
//                    getReviewCount()
//
//                    setData()
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Page?>>?, t: Throwable?, response: NewResultResponse<Page?>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    private fun getReviewCount(){
//        val params = HashMap<String, String>()
//        params["pageSeqNo"] = mPage.no.toString()
//        ApiBuilder.create().getGoodsReviewCount(params).setCallback(object : PplusCallback<NewResultResponse<Count>>{
//            override fun onResponse(call: Call<NewResultResponse<Count>>?, response: NewResultResponse<Count>?) {
//                if(response != null){
//                    val count = response.data.count
//                    text_page_review_count?.text = getString(R.string.format_review, FormatUtil.getMoneyType(count.toString()))
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Count>>?, t: Throwable?, response: NewResultResponse<Count>?) {
//
//            }
//        }).build().call()
//    }
//
//    private fun getGoodsCount() {
//        val params = HashMap<String, String>()
//
//        params["expired"] = "false" //null-All, true-유효기간지난것, false-유효기간남은것
//        params["status"] = EnumData.GoodsStatus.ing.status.toString()
//        params["pageSeqNo"] = mPage.no.toString()
//        params["isHotdeal"] = "false"
//        params["isPlus"] = "false"
//        params["page"] = "0"
//        showProgress("")
//        ApiBuilder.create().getGoods(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<Goods>>> {
//
//            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<Goods>>>?, response: NewResultResponse<SubResultResponse<Goods>>?) {
//                hideProgress()
//                if (!isAdded) {
//                    return
//                }
//                if (response != null) {
//                    val goodsCount = response.data.totalElements!!
//                    text_page_goods_count?.text = getString(R.string.format_goods, FormatUtil.getMoneyType(goodsCount.toString()))
//
//                }
//
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<Goods>>>?, t: Throwable?, response: NewResultResponse<SubResultResponse<Goods>>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    private fun getPlusGoodsCount() {
//        val params = HashMap<String, String>()
//
//        params["expired"] = "false" //null-All, true-유효기간지난것, false-유효기간남은것
//        params["status"] = EnumData.GoodsStatus.ing.status.toString()
//        params["pageSeqNo"] = mPage.no.toString()
//        params["isHotdeal"] = "false"
//        params["isPlus"] = "true"
//        params["page"] = "0"
//        showProgress("")
//        ApiBuilder.create().getGoods(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<Goods>>> {
//
//            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<Goods>>>?, response: NewResultResponse<SubResultResponse<Goods>>?) {
//                hideProgress()
//                if (!isAdded) {
//                    return
//                }
//                if (response != null) {
//                    val goodsCount = response.data.totalElements!!
//                    text_page_plus_goods_count?.text = getString(R.string.format_plus_goods, FormatUtil.getMoneyType(goodsCount.toString()))
//
//                }
//
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<Goods>>>?, t: Throwable?, response: NewResultResponse<SubResultResponse<Goods>>?) {
//                hideProgress()
//            }
//        }).build().call()
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
//                image_page_facebook.visibility = View.GONE
//                image_page_twitter.visibility = View.GONE
//                image_page_kakao.visibility = View.GONE
//                image_page_blog.visibility = View.GONE
//
//
//                layout_page_naver.visibility = View.GONE
//                layout_page_instagram.visibility = View.GONE
//                layout_page_homepage.visibility = View.GONE
//                layout_page_youtube.visibility = View.GONE
//                layout_page_kakaochannel.visibility = View.GONE
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
//                                    layout_page_naver.visibility = View.VISIBLE
//                                    layout_page_naver.tag = sns
//                                    layout_page_naver.setOnClickListener(onSnsClickListener)
//                                }
//                                SnsTypeCode.instagram.name -> {
//                                    existSns = true
//                                    layout_page_instagram.visibility = View.VISIBLE
//                                    layout_page_instagram.tag = sns
//                                    layout_page_instagram.setOnClickListener(onSnsClickListener)
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
//                                    layout_page_homepage.visibility = View.VISIBLE
//                                    layout_page_homepage.tag = sns
//                                    layout_page_homepage.setOnClickListener(onSnsClickListener)
//                                }
//                                SnsTypeCode.youtube.name -> {
//                                    existSns = true
//                                    layout_page_youtube.visibility = View.VISIBLE
//                                    layout_page_youtube.tag = sns
//                                    layout_page_youtube.setOnClickListener(onSnsClickListener)
//                                }
//                                SnsTypeCode.kakaoChannel.name -> {
//                                    existSns = true
//                                    layout_page_kakaochannel.visibility = View.VISIBLE
//                                    layout_page_kakaochannel.tag = sns
//                                    layout_page_kakaochannel.setOnClickListener(onSnsClickListener)
//                                }
//                            }
//                        }
//                    }
//                }
//
//                if(existSns){
//                    layout_page_info_sns.visibility = View.VISIBLE
//                }else{
//                    layout_page_info_sns.visibility = View.GONE
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
//    private fun setData() {
//
//        text_page_name2.text = mPage.name
//        text_page_name.text = mPage.name
//
//        LogUtil.e(LOG_TAG, "reviewCount {}, goodscount {}, avgEval : {}", mPage.reviewCount, mPage.goodsCount, mPage.avgEval)
//
//
////        if (mPage.numberList != null && mPage.numberList!!.isNotEmpty()) {
////            text_home_number.text = PplusNumberUtil.getPrNumberFormat(mPage.numberList!![0].number)
////        }
//
//        if (mPage.plus!!) {
//            layout_page_plus.visibility = View.GONE
//        } else {
//            layout_page_plus.visibility = View.VISIBLE
//        }
//
//        layout_page_plus.setOnClickListener {
//            if(!PplusCommonUtil.loginCheck(activity!!)){
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
//                                        layout_page_plus.visibility = View.GONE
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
////        if (StringUtils.isNotEmpty(mPage.phone)) {
////            layout_page_call.isSelected = true
////            layout_page_call.setOnClickListener {
////                val pPlusPermission = PPlusPermission(activity)
////                pPlusPermission.addPermission(Permission.PERMISSION_KEY.CONTACTS)
////                pPlusPermission.setPermissionListener(object : PermissionListener {
////
////                    override fun onPermissionGranted() {
////
////                        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mPage.phone!!))
////                        startActivity(intent)
////                    }
////
////                    override fun onPermissionDenied(deniedPermissions: ArrayList<String>) {
////
////                    }
////                })
////                pPlusPermission.checkPermission()
////            }
////        } else {
////            layout_page_call.isSelected = false
////            layout_page_call.setOnClickListener(null)
////        }
//
//
////        if(!mPage.isShopOrderable!! && !mPage.isPackingOrderable!! && !mPage.isDeliveryOrderable!!){
////            layout_page_order.visibility = View.GONE
////        }else{
////            layout_page_order.visibility = View.GONE
////        }
//
////        mAdapter!!.setPage(mPage)
//
//        if (StringUtils.isNotEmpty(mPage.catchphrase) || StringUtils.isNotEmpty(mPage.introduction)) {
//            layout_page_info_introduction.visibility = View.VISIBLE
//
//            var catchphrase = ""
//            if (StringUtils.isNotEmpty(mPage.catchphrase)) {
//                catchphrase = mPage.catchphrase!!
//            }
//
//            var introduction = ""
//            if (StringUtils.isNotEmpty(mPage.introduction)) {
//                introduction = mPage.introduction!!
//            }
//
//            text_page_info_introduction.text = "$catchphrase\n$introduction"
//        } else {
//            layout_page_info_introduction.visibility = View.GONE
//        }
//
//        if (StringUtils.isNotEmpty(mPage.phone)) {
//            layout_page_info_phone.visibility = View.GONE
//            layout_page_call.visibility = View.VISIBLE
//            view_page_call_bar.visibility = View.VISIBLE
//            text_page_info_phone.text = FormatUtil.getPhoneNumber(mPage.phone)
//            text_page_info_phone.setOnClickListener {
//                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mPage.phone!!))
//                startActivity(intent)
//            }
//
//            text_page_info_call.setOnClickListener {
//                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mPage.phone!!))
//                startActivity(intent)
//            }
//
//            layout_page_call.setOnClickListener{
//                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mPage.phone!!))
//                startActivity(intent)
//            }
//        } else {
//            layout_page_info_phone.visibility = View.GONE
//            layout_page_call.visibility = View.GONE
//            view_page_call_bar.visibility = View.GONE
//        }
//
//        if (StringUtils.isNotEmpty(mPage.openHours)) {
//            layout_page_info_open_hours.visibility = View.VISIBLE
//            text_page_info_opening_hours.text = mPage.openHours
//        } else {
//            layout_page_info_open_hours.visibility = View.GONE
//        }
//
//        if (StringUtils.isNotEmpty(mPage.holiday)) {
//            layout_page_info_holiday.visibility = View.VISIBLE
//            text_page_info_holiday.text = mPage.holiday
//        } else {
//            layout_page_info_holiday.visibility = View.GONE
//        }
//
//        if (mPage.isParkingAvailable!!) {
//            layout_page_info_parking.visibility = View.VISIBLE
//
//            if (mPage.isValetParkingAvailable!!) {
//                text_page_info_parking.text = "${getString(R.string.word_parking_enable)}\n${getString(R.string.word_valet_parking_enable)}"
//            } else {
//                text_page_info_parking.text = "${getString(R.string.word_parking_enable)}\n${getString(R.string.word_valet_parking_disable)}"
//            }
//        } else {
//            text_page_info_parking.text = getString(R.string.word_parking_disable)
//        }
//
//        if (mPage.address == null || StringUtils.isEmpty(mPage.address!!.roadBase)) {
//            layout_page_info_address.visibility = View.GONE
//            layout_page_info_map_option.visibility = View.GONE
//            layout_page_info_map.visibility = View.GONE
//        } else {
//            layout_page_info_address.visibility = View.VISIBLE
//            var detailAddress = ""
//            if (StringUtils.isNotEmpty(mPage.address!!.roadDetail)) {
//                detailAddress = mPage.address!!.roadDetail
//            }
//            text_page_info_address.text = "${mPage.address!!.roadBase} $detailAddress"
//
//            if (mPage.latitude != null && mPage.longitude != null) {
//                layout_page_info_map.visibility = View.VISIBLE
//
//                map_page_info.onCreate(null)
//                map_page_info.getMapAsync(object : OnMapReadyCallback {
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
//                image_page_info_map_full.setOnClickListener {
//                    val intent = Intent(activity, LocationPageActivity::class.java)
//                    intent.putExtra(Const.PAGE, mPage)
//                    activity?.startActivityForResult(intent, Const.REQ_LOCATION_CODE)
//                }
//            } else {
//                layout_page_info_map.visibility = View.GONE
//            }
//        }
//
//        layout_page_info_find_road.setOnClickListener {
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
//        layout_page_info_call_taxi.setOnClickListener {
//            val uri = Uri.parse("https://t.kakao.com/launch?type=taxi&dest_lat=${mPage.latitude}&dest_lng=${mPage.longitude}&ref=pplus")
//            val intent = Intent(Intent.ACTION_VIEW, uri)
//            startActivity(intent)
//        }
//
//        layout_page_info_navigation.setOnClickListener {
//            val destination = Location.newBuilder(mPage.name, mPage.longitude!!, mPage.latitude!!).build()
//            val builder = KakaoNaviParams.newBuilder(destination)
//                    .setNaviOptions(NaviOptions.newBuilder().setCoordType(CoordType.WGS84).build())
//            KakaoNaviService.getInstance().shareDestination(activity, builder.build());
//        }
//
//        layout_page_info_copy_address.setOnClickListener {
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
//        layout_pplus_info_btn.setOnClickListener {
//            if (layout_pplus_info.visibility == View.GONE) {
//                text_pplus_info_btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_footer_arrow_up, 0)
//                layout_pplus_info.visibility = View.VISIBLE
//            } else if (layout_pplus_info.visibility == View.VISIBLE) {
//                text_pplus_info_btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_footer_arrow_down, 0)
//                layout_pplus_info.visibility = View.GONE
//            }
//        }
//
//        getSnsLink()
//        callIntroduceImage()
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
//    private fun callIntroduceImage() {
//
//        val params = HashMap<String, String>()
//        params["no"] = "" + mPage.no!!
////        ApiBuilder.create().getIntroImageAll(params).setCallback(object : PplusCallback<NewResultResponse<Attachment>> {
////
////            override fun onResponse(call: Call<NewResultResponse<Attachment>>, response: NewResultResponse<Attachment>) {
////
////                if (!isAdded) {
////                    return
////                }
////
////                attachmentList = ArrayList<Attachment>()
////                mImageList = response.datas
////                attachmentList!!.addAll(response.datas)
////                if (StringUtils.isNotEmpty(mPage.mainMovieUrl)) {
////                    val mainYoutube = Attachment()
////                    mainYoutube.targetType = "youtube"
////                    mainYoutube.url = mPage.mainMovieUrl
////                    attachmentList!!.add(0, mainYoutube)
////                }
////
////                if (attachmentList!!.size > 1) {
////
////                    indicator_page_introduce_image.removeAllViews()
////                    indicator_page_introduce_image.visibility = View.VISIBLE
////                    indicator_page_introduce_image.build(LinearLayout.HORIZONTAL, attachmentList!!.size)
////                } else {
////                    indicator_page_introduce_image.visibility = View.GONE
////                }
////
////                mIntroPagerAdapter!!.setDataList(attachmentList)
////            }
////
////            override fun onFailure(call: Call<NewResultResponse<Attachment>>, t: Throwable, response: NewResultResponse<Attachment>) {
////
////            }
////        }).build().call()
//
//        ApiBuilder.create().getPageImageAll(params).setCallback(object : PplusCallback<NewResultResponse<PageImage>> {
//            override fun onResponse(call: Call<NewResultResponse<PageImage>>?, response: NewResultResponse<PageImage>?) {
//                if (!isAdded) {
//                    return
//                }
//                if(response != null){
//                    mPageImageList = response.datas
//
//                    if(mPageImageList!!.size > 1){
//                        indicator_page_introduce_image.removeAllViews()
//                        indicator_page_introduce_image.visibility = View.VISIBLE
//                        indicator_page_introduce_image.build(LinearLayout.HORIZONTAL, mPageImageList!!.size)
//                    }else{
//                        indicator_page_introduce_image.visibility = View.GONE
//                    }
//
//                    mPageImagePagerAdapter!!.setDataList(mPageImageList)
//                }
//
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<PageImage>>?, t: Throwable?, response: NewResultResponse<PageImage>?) {
//
//            }
//        }).build().call()
//    }
//
////    private fun listCall(page: Int) {
////
////        mLockListView = true
////        val params = HashMap<String, String>()
////
////        params["pageSeqNo"] = mPage.no.toString()
////        params["sort"] = "${EnumData.BuyGoodsSort.seqNo.name},${EnumData.BuyGoodsSort.desc.name}"
////        params["page"] = page.toString()
////
////        showProgress("")
////        ApiBuilder.create().getGoodsReview(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<GoodsReview>>> {
////
////            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<GoodsReview>>>?, response: NewResultResponse<SubResultResponse<GoodsReview>>?) {
////                if (!isAdded) {
////                    return
////                }
////                hideProgress()
////
////                if (response != null) {
////                    mIsLast = response.data.last!!
////                    if (response.data.first!!) {
////                        mTotalCount = response.data.totalElements!!
////                        mAdapter!!.clear()
////                    }
////
////                    mLockListView = false
////                    mAdapter!!.addAll(response.data.content!!)
////                }
////
////            }
////
////            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<GoodsReview>>>?, t: Throwable?, response: NewResultResponse<SubResultResponse<GoodsReview>>?) {
////                hideProgress()
////                mLockListView = false
////            }
////        }).build().call()
////    }
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
////            Const.REQ_POST -> {
////                mPaging = 0
////                listCall(mPaging)
////            }
////            Const.REQ_GOODS_DETAIL -> {
////                mAdapter!!.notifyItemChanged(0)
////            }
//            Const.REQ_REVIEW -> {
//            }
////            Const.REQ_LOCATION_CODE -> {
////                mAdapter!!.notifyItemChanged(0)
////            }
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
//                PageFragment2().apply {
//                    arguments = Bundle().apply {
//                        putParcelable(Const.PAGE, page)
//                    }
//                }
//    }
//}
