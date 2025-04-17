package com.pplus.prnumberuser.apps.search.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.core.location.LocationUtil
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.LocationData
import com.pplus.prnumberuser.core.network.model.dto.Page
import com.pplus.prnumberuser.core.network.model.dto.VirtualNumberManage
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivityLocationAroundNumberGroupPageBinding
import com.pplus.prnumberuser.databinding.ItemCustomBalloonBinding
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import retrofit2.Call
import java.util.*

class LocationAroundNumberGroupPageActivity : BaseActivity(), ImplToolbar, MapView.MapViewEventListener, MapView.POIItemEventListener {
    override fun getPID(): String {
        return "Main_storemap"
    }

    private lateinit var binding: ActivityLocationAroundNumberGroupPageBinding

    override fun getLayoutView(): View {
        binding = ActivityLocationAroundNumberGroupPageBinding.inflate(layoutInflater)
        return binding.root
    }

    var mVirtualNumberManage:VirtualNumberManage? = null

    override fun initializeView(savedInstanceState: Bundle?) {

        mVirtualNumberManage = intent.getParcelableExtra(Const.DATA)

        binding.imageLocationAroundNumberGroupPageMy.setOnClickListener {

            if (!LocationUtil.isLocationEnabled(this)) {
                PplusCommonUtil.alertGpsOff(this)
            } else {
                mMapView.setCurrentLocationEventListener(null)
                mMapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
                mMapView.setCurrentLocationEventListener(object : MapView.CurrentLocationEventListener {

                    override fun onCurrentLocationUpdate(mapView: MapView, mapPoint: MapPoint, v: Float) {
                        LogUtil.e(LOG_TAG, "onCurrentLocationUpdate")
                    }

                    override fun onCurrentLocationDeviceHeadingUpdate(mapView: MapView, v: Float) {
                        hideProgress()
                    }

                    override fun onCurrentLocationUpdateFailed(mapView: MapView) {
                        hideProgress()
                    }

                    override fun onCurrentLocationUpdateCancelled(mapView: MapView) {
                        hideProgress()
                    }
                })

                mMapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
                showProgress(getString(R.string.msg_getting_current_location))
            }
        }
    }

    lateinit var mMapView: MapView
    var mLocationData: LocationData? = null
    private var mTotalCount: Int = 0
    private var mLockListView = false
    private var mPageListArray = ArrayList<ArrayList<Page>>()

    private fun initializeUI() {

        mMapView = MapView(this)
        binding.layoutLocationAroundNumberGroupPage.addView(mMapView)

//        mMapView.setDaumMapApiKey(getString(R.string.kakao_app_key))
        mMapView.setMapViewEventListener(this)
        mMapView.setPOIItemEventListener(this)
        //        mMapView.setCalloutBalloonAdapter(new CustomBalloonAdapter(this));
        mMapView.removeAllPOIItems()


        mLocationData = LocationUtil.specifyLocationData
        if (mLocationData != null) {
            mMapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(mLocationData!!.latitude, mLocationData!!.longitude), false)
        }

    }

    private fun listCall(left: Double, top: Double, right: Double, bottom: Double) {

        val params = HashMap<String, String>()
        params["pg"] = "1"
        params["sz"] = "100"
        params["left"] = left.toString()
        params["top"] = top.toString()
        params["right"] = right.toString()
        params["bottom"] = bottom.toString()
        params["manageSeqNo"] = mVirtualNumberManage!!.seqNo.toString()
        if (mLocationData != null) {
            params["latitude"] = mLocationData!!.latitude.toString()
            params["longitude"] = mLocationData!!.longitude.toString()
        }

        mLockListView = true

//        layout_location_around_page_bottom.visibility = View.GONE
        mPageListArray = ArrayList()
        mMapView.removeAllPOIItems()
//        showProgress("")
        ApiBuilder.create().getPageListByAreaAndManageSeqNo(params).setCallback(object : PplusCallback<NewResultResponse<Page>> {

            override fun onResponse(call: Call<NewResultResponse<Page>>, response: NewResultResponse<Page>) {
                mLockListView = false
//                hideProgress()

                val pageList = response.datas
                var beforePage: Page? = null
                var sameLocationList = ArrayList<Page>()
                if (!pageList.isEmpty()) {
                    for (i in 0..pageList.size - 1) {

                        val page = pageList[i]

                        if (beforePage != null) {
                            if (beforePage.latitude == page.latitude && beforePage.longitude == page.longitude) {
                                sameLocationList.add(page)
                            } else {

                                mPageListArray.add(sameLocationList)
                                sameLocationList = ArrayList<Page>()
                                sameLocationList.add(page)
                            }

                            if (i == pageList.size - 1) {
                                mPageListArray.add(sameLocationList)
                            }

                        } else {
                            sameLocationList = ArrayList<Page>()
                            sameLocationList.add(page)
                            if (pageList.size == 1) {
                                mPageListArray.add(sameLocationList)
                            }
                        }

                        beforePage = page
                    }

                    val items = arrayOfNulls<MapPOIItem>(mPageListArray.size)

                    for (i in 0..mPageListArray.size - 1) {
                        val item = mPageListArray[i][0]
                        val mapPoint = MapPoint.mapPointWithGeoCoord(item.latitude!!, item.longitude!!)
                        val customMarker = MapPOIItem()
                        customMarker.itemName = item.name
                        customMarker.tag = i
                        customMarker.mapPoint = mapPoint
                        customMarker.markerType = MapPOIItem.MarkerType.CustomImage // 마커타입을 커스텀 마커로 지정.

                        customMarker.customImageResourceId = R.drawable.ic_location // 마커 이미지.
                        customMarker.customSelectedImageResourceId = R.drawable.ic_location

                        customMarker.isMoveToCenterOnSelect = false
                        customMarker.isCustomImageAutoscale = false // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
                        customMarker.setCustomImageAnchor(0.5f, 0.5f) // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.

                        val customBalloonBinding = ItemCustomBalloonBinding.inflate(layoutInflater)
                        if (sameLocationList.size > 1) {
                            customBalloonBinding.textCustomBalloon.text = getString(R.string.format_other2, mPageListArray[i][0].name, mPageListArray[i].size - 1)
                        } else {
                            customBalloonBinding.textCustomBalloon.text = mPageListArray[i][0].name
                        }

                        customMarker.customCalloutBalloon = customBalloonBinding.root
                        items[i] = customMarker
                    }

                    mMapView.addPOIItems(items)
                }


            }

            override fun onFailure(call: Call<NewResultResponse<Page>>, t: Throwable, response: NewResultResponse<Page>) {
                hideProgress()
            }
        }).build().call()
    }

    private fun setPageInfo(page: Page) {
        binding.layoutLocationAroundNumberGroupPageBottom.visibility = View.VISIBLE

        binding.maskLocationAroundNumberGroupPage.setMask(R.drawable.img_page_profile_mask)
        binding.imageLocationAroundNumberGroupPageCall.visibility = View.VISIBLE

        binding.layoutLocationAroundNumberGroupPageBottom.setOnClickListener {
            val location = IntArray(2)
            it.getLocationOnScreen(location)
            val x = location[0] + it.width / 2
            val y = location[1] + it.height / 2
            PplusCommonUtil.goPage(this, page, x, y)
        }
        binding.textLocationAroundNumberGroupPageName.text = page.name!!

        if (StringUtils.isNotEmpty(page.thumbnail)) {
            Glide.with(this).load(page.thumbnail).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(binding.imageLocationAroundPage)
        } else {
            binding.imageLocationAroundPage.setImageResource(R.drawable.prnumber_default_img)
        }
        binding.textLocationAroundPageIntroduce.setSingleLine()
        if (StringUtils.isNotEmpty(page.catchphrase)) {
            binding.textLocationAroundPageIntroduce.text = page.catchphrase
        } else {
            binding.textLocationAroundPageIntroduce.text = ""
        }

//        if (page.numberList != null && page.numberList!!.isNotEmpty()) {
//            text_location_around_page_distance.visibility = View.VISIBLE
//            val number = page.numberList!![0].number
//            text_location_around_page_distance.text = PplusNumberUtil.getPrNumberFormat(number)
//        } else {
//            text_location_around_page_distance.visibility = View.GONE
//        }

        val distance = page.distance
        if (distance != null) {
            binding.textLocationAroundNumberGroupPageDistance.visibility = View.VISIBLE
            var strDistance: String? = null
            if (distance > 1) {
                strDistance = String.format("%.2f", distance) + "km"
            } else {
                strDistance = (distance * 1000).toInt().toString() + "m"
            }
            binding.textLocationAroundNumberGroupPageDistance.text = strDistance
        } else {
            binding.textLocationAroundNumberGroupPageDistance.visibility = View.GONE
        }

        if (StringUtils.isNotEmpty(page.phone)) {
            binding.imageLocationAroundNumberGroupPageCall.setOnClickListener {

                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + page.phone!!))
                startActivity(intent)
            }
        } else {
            binding.imageLocationAroundNumberGroupPageCall.visibility = View.GONE
        }

        if (page.point != null && page.point!! > 0) {
            binding.textLocationAroundNumberGroupPagePoint.visibility = View.VISIBLE
            binding.textLocationAroundNumberGroupPagePoint.text = PplusCommonUtil.fromHtml(getString(R.string.html_reward_point4, "${page.point!!}%"))
        } else {
            binding.textLocationAroundNumberGroupPagePoint.visibility = View.GONE
        }
    }

    var beforeTop: Double? = null
    var beforeLeft: Double? = null
    var beforeBottom: Double? = null
    var beforeRight: Double? = null

    fun resetData(reset: Boolean) {
        val topLeft = MapPoint.mapPointWithScreenLocation(binding.layoutLocationAroundNumberGroupPage.x.toDouble(), binding.layoutLocationAroundNumberGroupPage.y.toDouble())
        val bottomRight = MapPoint.mapPointWithScreenLocation((binding.layoutLocationAroundNumberGroupPage.x + binding.layoutLocationAroundNumberGroupPage.width).toDouble(), (binding.layoutLocationAroundNumberGroupPage.y + binding.layoutLocationAroundNumberGroupPage.height).toDouble())

        val left = topLeft.mapPointGeoCoord.longitude
        val top = topLeft.mapPointGeoCoord.latitude
        val right = bottomRight.mapPointGeoCoord.longitude
        val bottom = bottomRight.mapPointGeoCoord.latitude

        if (reset) {
            beforeBottom = bottom
            beforeLeft = left
            beforeTop = top
            beforeRight = right
            listCall(left, top, right, bottom)
        } else {
            if (left == beforeLeft && top == beforeTop && right == beforeRight && bottom == beforeBottom) {
                return
            } else {
                beforeBottom = bottom
                beforeLeft = left
                beforeTop = top
                beforeRight = right
                listCall(left, top, right, bottom)
            }
        }


    }

    override fun onMapViewInitialized(p0: MapView?) {
        MapView.setMapTilePersistentCacheEnabled(true)
    }


    override fun onMapViewCenterPointMoved(p0: MapView?, p1: MapPoint?) {

    }

    override fun onMapViewMoveFinished(mapView: MapView?, p1: MapPoint?) {
        hideProgress()
        if (mMapView.currentLocationTrackingMode == MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading) {
            mMapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithMarkerHeadingWithoutMapMoving
        }
        resetData(false)
    }

    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?, p2: MapPOIItem.CalloutBalloonButtonType?) {

        if (p1!!.tag == -1) {
            return
        }

        val list = mPageListArray[p1.tag]
        if (list.size == 1) {
//            if(!PplusCommonUtil.loginCheck(this@LocationAroundPageActivity)){
//                return
//            }
            setPageInfo(list[0])
        } else {
            val contents = arrayOfNulls<String>(list.size)

            for (i in list.indices) {
                contents[i] = list.get(i).name
            }

            val builder = AlertBuilder.Builder()
            builder.setTitle(getString(R.string.word_select))
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER)
            builder.setContents(*contents)
            builder.setLeftText(getString(R.string.word_cancel))
            builder.setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                    when (event_alert) {

                        AlertBuilder.EVENT_ALERT.LIST -> {
//                            if(!PplusCommonUtil.loginCheck(this@LocationAroundPageActivity)){
//                                return
//                            }
                            setPageInfo(list[event_alert.getValue() - 1])
                        }
                    }
                }
            }).builder().show(this)
        }
    }

    override fun onMapViewDoubleTapped(p0: MapView?, p1: MapPoint?) {}
    override fun onMapViewDragStarted(p0: MapView?, p1: MapPoint?) {}
    override fun onMapViewDragEnded(p0: MapView?, p1: MapPoint?) {}
    override fun onMapViewSingleTapped(p0: MapView?, p1: MapPoint?) {}
    override fun onMapViewZoomLevelChanged(p0: MapView?, p1: Int) {}
    override fun onMapViewLongPressed(p0: MapView?, p1: MapPoint?) {}
    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {}
    override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {}
    override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {}

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.msg_view_map), ToolbarOption.ToolbarMenu.RIGHT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
                        onBackPressed()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        beforeTop = null
        beforeLeft = null
        beforeBottom = null
        beforeRight = null
        initializeUI()
    }

    override fun onPause() {
        super.onPause()
        mMapView.removeAllPOIItems()
        binding.layoutLocationAroundNumberGroupPage.removeAllViews()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        mMapView.removeAllPOIItems()
        binding.layoutLocationAroundNumberGroupPage.removeAllViews()
    }
}
