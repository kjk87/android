package com.pplus.prnumberbiz.apps.pages.ui


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.MotionEvent
import android.view.View
import com.pple.pplus.utils.part.format.FormatUtil
import com.pple.pplus.utils.part.logs.LogUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment
import com.pplus.prnumberbiz.apps.marketing.ui.SnsSyncActivity
import com.pplus.prnumberbiz.apps.post.ui.ReviewActivity
import com.pplus.prnumberbiz.apps.shop.ui.LocationShopActivity
import com.pplus.prnumberbiz.core.code.common.SnsTypeCode
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Page
import com.pplus.prnumberbiz.core.network.model.dto.Sns
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import com.pplus.prnumberbiz.core.util.PplusNumberUtil
import kotlinx.android.synthetic.main.fragment_page_info.*
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import network.common.PplusCallback
import retrofit2.Call
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [PageInfoFragment.newInstance] factory method to
 * create an instance of this mapFragment.
 */
class PageInfoFragment : BaseFragment<BaseActivity>() {

    private var mPage: Page? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mPage = arguments!!.getParcelable(Const.PAGE)
        }
    }

    override fun getPID(): String {
        return ""
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.fragment_page_info
    }

    override fun initializeView(container: View?) {

    }


    var animationRun = false

    override fun init() {

        text_page_info_name.text = mPage!!.name

        if (mPage!!.numberList != null && mPage!!.numberList!!.isNotEmpty()) {
            text_page_info_number.text = PplusNumberUtil.getPrNumberFormat(mPage!!.numberList!![0].number)
        }

        image_page_info_facebook.setOnClickListener(onSnsClickListener)
        image_page_info_kakao.setOnClickListener(onSnsClickListener)
        image_page_info_twitter.setOnClickListener(onSnsClickListener)
        image_page_info_instagram.setOnClickListener(onSnsClickListener)
        image_page_info_blog.setOnClickListener(onSnsClickListener)
        image_page_info_homepage.setOnClickListener(onSnsClickListener)

        text_page_info_sync_sns.setOnClickListener {
            val intent = Intent(activity, SnsSyncActivity::class.java)
            intent.putExtra(Const.FIRST, false)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            activity!!.startActivityForResult(intent, Const.REQ_SYNC_SNS)
        }

        if (StringUtils.isNotEmpty(mPage!!.introduction)) {
            layout_page_info_introduction.visibility = View.VISIBLE
            text_page_info_introduction.text = mPage!!.introduction
        } else {
            layout_page_info_introduction.visibility = View.GONE
        }

        if (StringUtils.isNotEmpty(mPage!!.phone)) {
            layout_page_info_phone_number.visibility = View.VISIBLE
            text_page_info_phone_number.text = FormatUtil.getPhoneNumber(mPage!!.phone)
        } else {
            layout_page_info_phone_number.visibility = View.GONE
        }

        if (mPage!!.address == null || StringUtils.isEmpty(mPage!!.address!!.roadBase)) {
            layout_page_info_address.visibility = View.GONE
        } else {
            layout_page_info_address.visibility = View.VISIBLE
            var detailAddress = ""
            if (StringUtils.isNotEmpty(mPage!!.address!!.roadDetail)) {
                detailAddress = mPage!!.address!!.roadDetail
            }
            text_page_info_address.text = "${mPage!!.address!!.roadBase} $detailAddress"
        }

        if (mPage!!.properties == null) {
            layout_page_info_open_hours.visibility = View.GONE
        } else {

            if (StringUtils.isNotEmpty(mPage!!.properties!!.openingHours)) {
                layout_page_info_open_hours.visibility = View.VISIBLE
                text_page_info_opening_hours.text = mPage!!.properties!!.openingHours
            } else {
                layout_page_info_open_hours.visibility = View.GONE
            }
        }

        text_page_info_review.setOnClickListener {
            val intent = Intent(activity, ReviewActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        getSnsLink()
        text_page_info_review.visibility = View.GONE
        getReviewCount()
    }

    private fun getReviewCount() {
        val params = HashMap<String, String>()
        params["boardNo"] = "" + mPage!!.reviewBoard!!.getNo()!!
        ApiBuilder.create().getBoardPostCount(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {

            override fun onResponse(call: Call<NewResultResponse<Int>>, response: NewResultResponse<Int>) {
                val reviewCount = response.data
                if (reviewCount > 0) {
                    text_page_info_review.visibility = View.VISIBLE
                    text_page_info_review.text = getString(R.string.format_review, FormatUtil.getMoneyType(reviewCount.toString()))
                } else {
                    text_page_info_review.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Int>>, t: Throwable, response: NewResultResponse<Int>) {

            }
        }).build().call()
    }

    private fun getSnsLink() {

        val params = HashMap<String, String>()
        params["no"] = "" + mPage!!.no!!

        ApiBuilder.create().getSnsLinkAll(params).setCallback(object : PplusCallback<NewResultResponse<Sns>> {

            override fun onResponse(call: Call<NewResultResponse<Sns>>, response: NewResultResponse<Sns>) {

                if (!isAdded) {
                    return
                }
                if (response.datas.isEmpty()) {
                    layout_page_info_sns.visibility = View.GONE
                } else {
                    layout_page_info_sns.visibility = View.VISIBLE
                }

                initSNS(response.datas)
            }

            override fun onFailure(call: Call<NewResultResponse<Sns>>, t: Throwable, response: NewResultResponse<Sns>) {

            }
        }).build().call()
    }

    private fun initSNS(snsList: List<Sns>?) {

        image_page_info_twitter.visibility = View.GONE
        image_page_info_facebook.visibility = View.GONE
        image_page_info_instagram.visibility = View.GONE
        image_page_info_kakao.visibility = View.GONE
        image_page_info_blog.visibility = View.GONE
        image_page_info_homepage.visibility = View.GONE

        if (snsList != null && !snsList.isEmpty()) {
            for (sns in snsList) {
                setSns(sns)
            }
        }
    }

    private fun setSns(sns: Sns?) {

        if (sns != null) {
            if (StringUtils.isNotEmpty(sns.url)) {
                when (SnsTypeCode.valueOf(sns.type)) {

                    SnsTypeCode.twitter -> {
                        image_page_info_twitter.visibility = View.VISIBLE
                        image_page_info_twitter.tag = sns
                    }
                    SnsTypeCode.facebook -> {
                        image_page_info_facebook.visibility = View.VISIBLE
                        image_page_info_facebook.tag = sns
                    }
                    SnsTypeCode.instagram -> {
                        image_page_info_instagram.visibility = View.VISIBLE
                        image_page_info_instagram.tag = sns
                    }
                    SnsTypeCode.kakao -> {
                        image_page_info_kakao.visibility = View.VISIBLE
                        image_page_info_kakao.tag = sns
                    }
                    SnsTypeCode.blog -> {
                        image_page_info_blog.visibility = View.VISIBLE
                        image_page_info_blog.tag = sns
                    }
                    SnsTypeCode.homepage -> {
                        image_page_info_homepage.visibility = View.VISIBLE
                        image_page_info_homepage.tag = sns
                    }
                }
            }
        }
    }

    private val onSnsClickListener = View.OnClickListener { v ->
        val sns = v.tag as Sns
        snsEvent(sns)
    }

    private fun snsEvent(sns: Sns) {
        // SNS 페이지 이동
        if (StringUtils.isNotEmpty(sns.url)) {
            // 계정으로 이동
            startActivity(PplusCommonUtil.getOpenSnsIntent(activity!!, SnsTypeCode.valueOf(sns.type), sns.url, sns.isLinkage))
        }
    }

    override fun onResume() {
        super.onResume()
        setMapView()
    }

    var mMapView: MapView? = null

    private fun setMapView() {
        container_page_info_map.removeAllViews()
        if (mPage!!.latitude != null && mPage!!.longitude != null) {

            image_page_info_map_full.setOnClickListener {
                container_page_info_map.removeAllViews()
                val intent = Intent(activity, LocationShopActivity::class.java)
                intent.putExtra(Const.PAGE, mPage)
                activity!!.startActivityForResult(intent, Const.REQ_LOCATION_CODE)
            }

            layout_page_info_map.visibility = View.VISIBLE
            container_page_info_map.removeAllViews()
            mMapView = MapView(activity)
            mMapView!!.setDaumMapApiKey(getString(R.string.kakao_app_key))
            mMapView!!.setMapViewEventListener(object : MapView.MapViewEventListener {
                override fun onMapViewDoubleTapped(p0: MapView?, p1: MapPoint?) {

                }

                override fun onMapViewInitialized(p0: MapView?) {
                    LogUtil.e(LOG_TAG, "onMapViewInitialized")
                    scroll_page_info.scrollY = 0
                }

                override fun onMapViewDragStarted(p0: MapView?, p1: MapPoint?) {

                }

                override fun onMapViewMoveFinished(p0: MapView?, p1: MapPoint?) {

                }

                override fun onMapViewCenterPointMoved(p0: MapView?, p1: MapPoint?) {

                }

                override fun onMapViewDragEnded(p0: MapView?, p1: MapPoint?) {

                }

                override fun onMapViewSingleTapped(p0: MapView?, p1: MapPoint?) {
                }

                override fun onMapViewZoomLevelChanged(p0: MapView?, p1: Int) {

                }

                override fun onMapViewLongPressed(p0: MapView?, p1: MapPoint?) {

                }
            })

            mMapView!!.setOnTouchListener(object : View.OnTouchListener{
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    return true
                }
            })
            val mapPoint = MapPoint.mapPointWithGeoCoord(mPage!!.latitude!!, mPage!!.longitude!!)
            mMapView!!.setMapCenterPoint(mapPoint, false)
            container_page_info_map.addView(mMapView)
//            mMapView!!.setPOIItemEventListener(this)
            mMapView!!.removeAllPOIItems()
            mMapView!!.setDefaultCurrentLocationMarker()

            val customMarker = MapPOIItem()
            customMarker.itemName = mPage!!.name
            customMarker.tag = 0
            customMarker.mapPoint = mapPoint
            customMarker.markerType = MapPOIItem.MarkerType.CustomImage // 마커타입을 커스텀 마커로 지정.
            customMarker.customImageResourceId = R.drawable.ic_location // 마커 이미지.
            customMarker.isCustomImageAutoscale = false // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
            customMarker.setCustomImageAnchor(0.5f, 0.5f) // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.
            mMapView!!.addPOIItem(customMarker)

        } else {
            layout_page_info_map.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Const.REQ_SYNC_SNS -> {
                getSnsLink()
            }
        }
    }

    companion object {

        fun newInstance(page: Page): PageInfoFragment {

            val fragment = PageInfoFragment()
            val args = Bundle()
            args.putParcelable(Const.PAGE, page)
            fragment.arguments = args
            return fragment
        }
    }

}// Required empty public constructor
