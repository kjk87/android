package com.pplus.prnumberuser.apps.page.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.navi.NaviClient
import com.kakao.sdk.navi.model.CoordType
import com.kakao.sdk.navi.model.Location
import com.kakao.sdk.navi.model.NaviOption
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.core.location.LocationUtil
import com.pplus.prnumberuser.core.network.model.dto.Page
import com.pplus.prnumberuser.core.network.model.dto.Page2
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.core.util.ToastUtil
import com.pplus.prnumberuser.databinding.ActivityLocationPageBinding
import com.pplus.prnumberuser.databinding.ItemCustomBalloonBinding
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class LocationPageActivity : BaseActivity(), ImplToolbar, MapView.MapViewEventListener {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityLocationPageBinding

    override fun getLayoutView(): View {
        binding = ActivityLocationPageBinding.inflate(layoutInflater)
        return binding.root
    }

    private var mMapView: MapView? = null
    private var mPage: Page? = null
    private var mPage2: Page2? = null

    private var isZoomOnce = false

    override fun initializeView(savedInstanceState: Bundle?) {

        mMapView = MapView(this)
        binding.layoutLocationPageMap.addView(mMapView)
//        mMapView!!.setDaumMapApiKey(getString(R.string.kakao_app_key))
        mMapView!!.setMapViewEventListener(this)
        mMapView!!.removeAllPOIItems()
        //        mMapView.setPOIItemEventListener(this);
        mPage = intent.getParcelableExtra(Const.PAGE)
        mPage2 = intent.getParcelableExtra(Const.PAGE2)

        var address = ""
        if (mPage != null) {
            address = mPage!!.address!!.roadBase!!

            if (StringUtils.isNotEmpty(mPage!!.address!!.roadDetail)) {
                address = address + " " + mPage!!.address!!.roadDetail
            }
        }else if (mPage2 != null) {
            address = mPage2!!.roadAddress!!

            if (StringUtils.isNotEmpty(mPage2!!.roadDetailAddress)) {
                address = address + " " + mPage2!!.roadDetailAddress
            }
        }

        val customMarker = MapPOIItem()

        if(mPage != null){
            val mapPoint = MapPoint.mapPointWithGeoCoord(mPage!!.latitude!!, mPage!!.longitude!!)
            customMarker.mapPoint = mapPoint
            customMarker.itemName = mPage!!.name
        }else if (mPage2 != null) {
            val mapPoint = MapPoint.mapPointWithGeoCoord(mPage2!!.latitude!!, mPage2!!.longitude!!)
            customMarker.mapPoint = mapPoint
            customMarker.itemName = mPage2!!.name
        }
        customMarker.tag = 0
        customMarker.markerType = MapPOIItem.MarkerType.CustomImage // 마커타입을 커스텀 마커로 지정.
        customMarker.customImageResourceId = R.drawable.ic_map_pin // 마커 이미지.
        customMarker.isCustomImageAutoscale = false // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
        customMarker.setCustomImageAnchor(0.5f, 0.5f) // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.

        val balloonBinding = ItemCustomBalloonBinding.inflate(layoutInflater)
        balloonBinding.textCustomBalloon.text = address
        customMarker.customCalloutBalloon = balloonBinding.root

        val items = arrayOfNulls<MapPOIItem>(1)
        items[0] = customMarker
        mMapView!!.addPOIItems(items)
//        mMapView.setMapCenterPoint(mapPoint, false);
//        mMapView.setZoomLevel(1, false);
        mMapView!!.fitMapViewAreaToShowAllPOIItems()
        mMapView!!.selectPOIItem(customMarker, true)



        binding.imageLocationMy.setOnClickListener {
            mMapView!!.setCurrentLocationEventListener(null)
            mMapView!!.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
            mMapView!!.setCurrentLocationEventListener(object : MapView.CurrentLocationEventListener {

                override fun onCurrentLocationUpdate(mapView: MapView, mapPoint: MapPoint, v: Float) {
                    LogUtil.e(LOG_TAG, "onCurrentLocationUpdate")
                    hideProgress()
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

            if (!LocationUtil.isLocationEnabled(this)) {
                PplusCommonUtil.alertGpsOff(this)
            }else{
                mMapView!!.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
                showProgress(getString(R.string.msg_getting_current_location))
            }

        }

        binding.layoutLocationFindRoad.setOnClickListener {
            if (existDaummapApp()) {
                if(mPage != null){
                    val uri = Uri.parse("daummaps://route?sp=${mPage!!.latitude}&ep=${mPage!!.longitude}&by=CAR");
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }else if(mPage2 != null){
                    val uri = Uri.parse("daummaps://route?sp=${mPage2!!.latitude}&ep=${mPage2!!.longitude}&by=CAR");
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }

            } else {
                val intent = Intent(Intent.ACTION_VIEW).setData(Uri.parse("market://details?id=net.daum.android.map"))
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }

        }

        binding.layoutLocationCallTaxi.setOnClickListener {
            if(mPage != null){
                val uri = Uri.parse("https://t.kakao.com/launch?type=taxi&dest_lat=${mPage!!.latitude}&dest_lng=${mPage!!.longitude}&ref=pplus");
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }else if(mPage2 != null){
                val uri = Uri.parse("https://t.kakao.com/launch?type=taxi&dest_lat=${mPage2!!.latitude}&dest_lng=${mPage2!!.longitude}&ref=pplus");
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }

        }

        binding.layoutLocationNavigation.setOnClickListener {
            if (NaviClient.instance.isKakaoNaviInstalled(this)) {
                LogUtil.e("kakonavi", "카카오내비 앱으로 길안내 가능")
                startActivity(
                    if(mPage != null){
                        NaviClient.instance.navigateIntent(

                            Location(mPage!!.name!!, mPage!!.longitude.toString(), mPage!!.latitude.toString()),
                            NaviOption(coordType = CoordType.WGS84)
                        )
                    }else{
                        NaviClient.instance.navigateIntent(

                            Location(mPage2!!.name!!, mPage2!!.longitude.toString(), mPage2!!.latitude.toString()),
                            NaviOption(coordType = CoordType.WGS84)
                        )
                    }

                )
            } else {
                LogUtil.e("kakonavi", "카카오내비 미설치: 웹 길안내 사용 권장")

                // 웹 브라우저에서 길안내
                // 카카오내비가 설치되지 않은 곳에서 활용할 수 있습니다.

                LogUtil.e("kakonavi", "카카오내비 미설치: 웹 길안내 사용 권장")
                val uri =if(mPage != null){
                    NaviClient.instance.navigateWebUrl(
                        Location(mPage!!.name!!, mPage!!.longitude.toString(), mPage!!.latitude.toString()),
                        NaviOption(coordType = CoordType.WGS84)
                    )
                }else{
                    NaviClient.instance.navigateWebUrl(
                        Location(mPage2!!.name!!, mPage2!!.longitude.toString(), mPage2!!.latitude.toString()),
                        NaviOption(coordType = CoordType.WGS84)
                    )
                }


                // CustomTabs로 길안내
                KakaoCustomTabsClient.openWithDefault(this, uri)
            }
        }

        binding.layoutLocationCopyAddress.setOnClickListener {
            if(mPage != null){
                if (mPage!!.address != null && StringUtils.isNotEmpty(mPage!!.address!!.roadBase)) {
                    var detailAddress = ""
                    if (StringUtils.isNotEmpty(mPage!!.address!!.roadDetail)) {
                        detailAddress = mPage!!.address!!.roadDetail!!
                    }

                    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

                    val clip = ClipData.newPlainText("address", "${mPage!!.address!!.roadBase} $detailAddress")
                    clipboard.setPrimaryClip(clip)
                    ToastUtil.show(this, R.string.msg_copied_clipboard)
                } else {
                    showAlert(getString(R.string.msg_not_exist_address))
                }
            }else if(mPage2 != null){
                if (StringUtils.isNotEmpty(mPage2!!.roadAddress)) {
                    var detailAddress = ""
                    if (StringUtils.isNotEmpty(mPage2!!.roadDetailAddress)) {
                        detailAddress = mPage2!!.roadDetailAddress!!
                    }

                    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

                    val clip = ClipData.newPlainText("address", "${mPage2!!.roadAddress} $detailAddress")
                    clipboard.setPrimaryClip(clip)
                    ToastUtil.show(this, R.string.msg_copied_clipboard)
                } else {
                    showAlert(getString(R.string.msg_not_exist_address))
                }
            }

        }
    }

    fun existDaummapApp(): Boolean {
        val pm = packageManager

        try {
            return (pm.getPackageInfo("net.daum.android.map", PackageManager.GET_SIGNATURES) != null)
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }
    }

    override fun onMapViewDoubleTapped(p0: MapView?, p1: MapPoint?) {

    }

    override fun onMapViewInitialized(p0: MapView?) {
        MapView.setMapTilePersistentCacheEnabled(true)
    }

    override fun onMapViewDragStarted(p0: MapView?, p1: MapPoint?) {

    }

    override fun onMapViewMoveFinished(p0: MapView?, p1: MapPoint?) {
        hideProgress()
    }

    override fun onMapViewCenterPointMoved(p0: MapView?, p1: MapPoint?) {

    }

    override fun onMapViewDragEnded(p0: MapView?, p1: MapPoint?) {

    }

    override fun onMapViewSingleTapped(p0: MapView?, p1: MapPoint?) {

    }

    override fun onMapViewZoomLevelChanged(mapView: MapView?, p1: Int) {
        if (!isZoomOnce) {
            isZoomOnce = true
            mapView!!.setZoomLevelFloat(mapView.zoomLevel + 0.5f, true)
        }
    }

    override fun onMapViewLongPressed(p0: MapView?, p1: MapPoint?) {

    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_location), ToolbarOption.ToolbarMenu.RIGHT)
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

    override fun onBackPressed() {

        super.onBackPressed()
        mMapView!!.removeAllPOIItems()
        binding.layoutLocationPageMap.removeAllViews()
    }
}
