package com.pplus.prnumberuser.apps.delivery.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption.ToolbarMenu
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.core.location.LocationUtil
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.ApiController
import com.pplus.prnumberuser.core.network.model.dto.DeliveryAddress
import com.pplus.prnumberuser.core.network.model.dto.ResultAddress
import com.pplus.prnumberuser.core.network.model.dto.SearchAddressJuso
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.core.util.PplusCommonUtil.Companion.alertGpsOff
import com.pplus.prnumberuser.databinding.ActivityDeliveryAddressFindMapBinding
import com.pplus.utils.part.logs.LogUtil
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import net.daum.mf.map.api.MapView.CurrentLocationEventListener
import net.daum.mf.map.api.MapView.CurrentLocationTrackingMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class DeliveryAddressFindMapActivity : BaseActivity(), ImplToolbar, MapView.MapViewEventListener{
    override fun getPID(): String? {
        return ""
    }

    private lateinit var mMapView: MapView

    private lateinit var binding: ActivityDeliveryAddressFindMapBinding
    override fun getLayoutView(): View {
        binding = ActivityDeliveryAddressFindMapBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun locationLog(){
        val params = HashMap<String, String>()
        params["deviceId"] = PplusCommonUtil.getDeviceID()
        params["platform"] = "aos"
        params["serviceLog"] = "배달주소 현재위치 기반 설정을 위한 현재위치조회"
        ApiBuilder.create().locationServiceLogSave(params).build().call()
    }

//    private var mReverseGeoCoder: MapReverseGeoCoder? = null
    private var mTotalCount = 0
    private var mPage = 0
    private var mLockListView = false
    private var mIsFindingAddress = false
    override fun initializeView(savedInstanceState: Bundle?) {
        mMapView = MapView(this)
        binding.layoutMapView.addView(mMapView)

        binding.editDeliveryAddressFindAddressDetail.setSingleLine()

//        mMapView.setDaumMapApiKey(getString(R.string.kakao_app_key));
        mMapView.requestFocus()
        mMapView.setMapViewEventListener(this)
        mMapView.setZoomLevel(1, false)
        mIsFindingAddress = false

        val x = intent.getDoubleExtra(Const.X, 0.0)
        val y = intent.getDoubleExtra(Const.Y, 0.0)

        if(x != 0.0 && y != 0.0){
            val mapPoint = MapPoint.mapPointWithGeoCoord(y, x)
            LogUtil.e(LOG_TAG, "Connected")
            mMapView.setMapCenterPoint(mapPoint, false)
        }
//        else if (LocationUtil.isLocationEnabled(this)) {
//            mMapView.currentLocationTrackingMode = CurrentLocationTrackingMode.TrackingModeOff
//            mMapView.currentLocationTrackingMode = CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
////            val location = LocationUtil.getCurrentLocation(this)
////            if (location != null) {
////                val mapPoint = MapPoint.mapPointWithGeoCoord(location.latitude, location.longitude)
////                LogUtil.e(LOG_TAG, "Connected")
////                mMapView.setMapCenterPoint(mapPoint, false)
////            }
//        }
        else if (LocationUtil.getLastLocation(this) != null) {
            val locationData = LocationUtil.getLastLocation(this)
            val mapPoint = MapPoint.mapPointWithGeoCoord(locationData!!.latitude, locationData.longitude)
            mMapView.setMapCenterPoint(mapPoint, false)
            locationLog()
//            setAddress(locationData.latitude, locationData.longitude)
        } else {
            val mapPoint = MapPoint.mapPointWithGeoCoord(LocationUtil.DEFAULT_LATITUDE, LocationUtil.DEFAULT_LONGITUDE)
            mMapView.setMapCenterPoint(mapPoint, false)
//            setAddress(LocationUtil.DEFAULT_LATITUDE, LocationUtil.DEFAULT_LONGITUDE)
        }

        mMapView.setCurrentLocationEventListener(object : CurrentLocationEventListener {
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
        binding.imageMyLocation.setOnClickListener {
            if (!LocationUtil.isLocationEnabled(this@DeliveryAddressFindMapActivity)) {
                alertGpsOff(this@DeliveryAddressFindMapActivity)
            } else {
                mMapView.currentLocationTrackingMode = CurrentLocationTrackingMode.TrackingModeOff
                mMapView.currentLocationTrackingMode = CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
                locationLog()
                showProgress(getString(R.string.msg_getting_current_location))
            }
        }

        binding.textDeliveryAddressFindSet.setOnClickListener {
            if(mJuso != null){
                val deliveryAddress = DeliveryAddress()
//                deliveryAddress.address = mJuso!!.roadAddr
                deliveryAddress.address = mJuso!!.roadAddrPart1
                deliveryAddress.jibunAddress = mJuso!!.jibunAddr
                deliveryAddress.sido = mJuso!!.siNm
                deliveryAddress.sigungu = mJuso!!.sggNm
                deliveryAddress.dongli = mJuso!!.emdNm
                deliveryAddress.roadName = mJuso!!.rn
                deliveryAddress.buildNo = mJuso!!.buldMnnm
                deliveryAddress.zipNo = mJuso!!.zipNo
                deliveryAddress.latitude = mMapView.mapCenterPoint.mapPointGeoCoord.latitude
                deliveryAddress.longitude = mMapView.mapCenterPoint.mapPointGeoCoord.longitude
                deliveryAddress.addressDetail = binding.editDeliveryAddressFindAddressDetail.text.toString().trim()
                val data = Intent()
                data.putExtra(Const.DATA, deliveryAddress)
                setResult(RESULT_OK, data)
                finish()
            }
        }
    }

    /*
    * 맵뷰리스터
    * */
    override fun onMapViewInitialized(mapView: MapView) {
        MapView.setMapTilePersistentCacheEnabled(true)
        LogUtil.e(LOG_TAG, "onMapViewInitialized")
    }

    override fun onMapViewCenterPointMoved(mapView: MapView, mapPoint: MapPoint) {
        LogUtil.e(LOG_TAG, "onMapViewCenterPointMoved")
    }

    override fun onMapViewZoomLevelChanged(mapView: MapView, i: Int) {
        LogUtil.e(LOG_TAG, "onMapViewZoomLevelChanged : {}", i)
    }

    override fun onMapViewSingleTapped(mapView: MapView, mapPoint: MapPoint) {
        LogUtil.e(LOG_TAG, "onMapViewSingleTapped")
    }

    override fun onMapViewDoubleTapped(mapView: MapView, mapPoint: MapPoint) {
        LogUtil.e(LOG_TAG, "onMapViewDoubleTapped")
    }

    override fun onMapViewLongPressed(mapView: MapView, mapPoint: MapPoint) {
        LogUtil.e(LOG_TAG, "onMapViewLongPressed")
    }

    override fun onMapViewDragStarted(mapView: MapView, mapPoint: MapPoint) {
        LogUtil.e(LOG_TAG, "onMapViewDragStarted")
    }

    override fun onMapViewDragEnded(mapView: MapView, mapPoint: MapPoint) {
        LogUtil.e(LOG_TAG, "onMapViewDragEnded")
    }

    var mJuso : SearchAddressJuso? = null

    override fun onMapViewMoveFinished(mapView: MapView, mapPoint: MapPoint) {
        LogUtil.e(LOG_TAG, "onMapViewMoveFinished")
        hideProgress()
//        mReverseGeoCoder = MapReverseGeoCoder(getString(R.string.kakao_app_key), mapView.mapCenterPoint, this, this)
        LogUtil.e(LOG_TAG, "lat : {} lon : {}", mapView.mapCenterPoint.mapPointGeoCoord.latitude, mapView.mapCenterPoint.mapPointGeoCoord.longitude)

        setAddress(mapView.mapCenterPoint.mapPointGeoCoord.latitude, mapView.mapCenterPoint.mapPointGeoCoord.longitude)
//        mReverseGeoCoder!!.startFindingAddress()

    }

    private fun setAddress(latitude:Double, longitude:Double){
        val params = HashMap<String, String>()
        params["x"] = longitude.toString()
        params["y"] = latitude.toString()
        mIsFindingAddress = true
        ApiController.getPRNumberApi().getCoord2Address(params).enqueue(object : Callback<ResultAddress> {

            override fun onResponse(call: Call<ResultAddress>, response: Response<ResultAddress>) {
                mIsFindingAddress = false
                LogUtil.e(LOG_TAG, "onResponse")
                if(response.body()!!.results != null){
                    val juso = response.body()!!.results!!.juso
                    if(juso != null && juso.isNotEmpty()){
                        binding.textAddress.text = juso[0].roadAddrPart1
                        binding.textDeliveryAddressFindAddress.text = juso[0].roadAddrPart1
                        mJuso = juso[0]
                        mJuso!!.jibunAddr = response.body()!!.oldAddress
                    }else{
                        binding.textAddress.text = ""
                        binding.textDeliveryAddressFindAddress.text = ""
                        mJuso = null
                    }
                }
            }

            override fun onFailure(call: Call<ResultAddress>, t: Throwable) {
            }
        })
    }

    /*
    * 리버스 지오커더 리스너
    * */
//    override fun onReverseGeoCoderFoundAddress(mapReverseGeoCoder: MapReverseGeoCoder, s: String) {
//        LogUtil.e(LOG_TAG, "foundAddress : {}", s)
//        mIsFindingAddress = false
//        binding.textAddress.text = s
//    }
//
//    override fun onReverseGeoCoderFailedToFindAddress(mapReverseGeoCoder: MapReverseGeoCoder) {}

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        val key = intent.getStringExtra(Const.KEY)
        toolbarOption.initializeDefaultToolbar(getString(R.string.msg_find_current_location), ToolbarMenu.RIGHT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {
        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarMenu.RIGHT -> if (tag == 1) {
                        onBackPressed()
                    }
                }
            }
        }
    }
}