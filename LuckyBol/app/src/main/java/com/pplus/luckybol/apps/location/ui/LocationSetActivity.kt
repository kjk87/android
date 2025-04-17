package com.pplus.luckybol.apps.location.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption.ToolbarMenu
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.location.data.SearchLocationAdapter
import com.pplus.luckybol.core.location.LocationUtil
import com.pplus.luckybol.core.network.ApiController
import com.pplus.luckybol.core.network.model.dto.LocationData
import com.pplus.luckybol.core.network.model.response.result.ResultDaumKeyword
import com.pplus.luckybol.core.util.PplusCommonUtil.Companion.alertGpsOff
import com.pplus.luckybol.core.util.PplusCommonUtil.Companion.hideKeyboard
import com.pplus.luckybol.databinding.ActivityLocationSetBinding
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapReverseGeoCoder
import net.daum.mf.map.api.MapView
import net.daum.mf.map.api.MapView.CurrentLocationEventListener
import net.daum.mf.map.api.MapView.CurrentLocationTrackingMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationSetActivity : BaseActivity(), ImplToolbar, MapView.MapViewEventListener, MapReverseGeoCoder.ReverseGeoCodingResultListener {
    override fun getPID(): String? {
        return ""
    }

    private lateinit var mMapView: MapView

    private lateinit var binding: ActivityLocationSetBinding
    override fun getLayoutView(): View {
        binding = ActivityLocationSetBinding.inflate(layoutInflater)
        return binding.root
    }

    private var mReverseGeoCoder: MapReverseGeoCoder? = null
    private var mSearchLocationAdapter: SearchLocationAdapter? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var mKeyword: String? = null
    private var mTotalCount = 0
    private var mPage = 0
    private var mLockListView = false
    private var mIsFindingAddress = false
    override fun initializeView(savedInstanceState: Bundle?) {
        mMapView = MapView(this)
        binding.layoutLocationSetMapView.addView(mMapView)

//        mMapView.setDaumMapApiKey(getString(R.string.kakao_app_key));
        mMapView.requestFocus()
        binding.textLocationSetLocation.setOnClickListener {
            val mapPoint = mMapView.mapCenterPoint
            val locationData = LocationData()
            locationData.latitude = mapPoint.mapPointGeoCoord.latitude
            locationData.longitude = mapPoint.mapPointGeoCoord.longitude
            locationData.address = binding.textLocationSetAddress.text.toString()
            if (!mIsFindingAddress) { //주소찾기 끝났을때
                val key = intent.getStringExtra(Const.KEY)
                if (StringUtils.isNotEmpty(key) && key == Const.PROFILE) {
                    val data = Intent()
                    data.putExtra(Const.LATITUDE, locationData.latitude)
                    data.putExtra(Const.LONGITUDE, locationData.longitude)
                    data.putExtra(Const.ADDRESS, locationData.address)
                    setResult(RESULT_OK, data)
                    finish()
                } else {
                    LocationUtil.specifyLocationData = locationData
                    LocationUtil.setLastLocation(this, locationData.latitude, locationData.longitude)
                    setResult(RESULT_OK)
                    finish()
                }
            } else {
                showAlert(R.string.msg_finding_address)
            }
        }
        mMapView.setMapViewEventListener(this)
        mMapView.setZoomLevel(1, false)
        mIsFindingAddress = false
        if (LocationUtil.specifyLocationData != null) {
            val mapPoint = MapPoint.mapPointWithGeoCoord(LocationUtil.specifyLocationData!!.latitude, LocationUtil.specifyLocationData!!.longitude)
            mMapView.setMapCenterPoint(mapPoint, false)
        } else if (LocationUtil.isLocationEnabled(this)) {
            val location = LocationUtil.getCurrentLocation(this)
            if (location != null) {
                val mapPoint = MapPoint.mapPointWithGeoCoord(location.latitude, location.longitude)
                LogUtil.e(LOG_TAG, "Connected")
                mMapView.setMapCenterPoint(mapPoint, false)
            }
        } else if (LocationUtil.getLastLocation(this) != null) {
            val locationData = LocationUtil.getLastLocation(this)
            val mapPoint = MapPoint.mapPointWithGeoCoord(locationData!!.latitude, locationData.longitude)
            mMapView.setMapCenterPoint(mapPoint, false)
        } else {
            val mapPoint = MapPoint.mapPointWithGeoCoord(LocationUtil.DEFAULT_LATITUDE, LocationUtil.DEFAULT_LONGITUDE)
            mMapView.setMapCenterPoint(mapPoint, false)
        }
        binding.editLocationSetSearch.setSingleLine()
        binding.editLocationSetSearch.imeOptions = EditorInfo.IME_ACTION_SEARCH
        binding.editLocationSetSearch.setOnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    mPage = 1
                    search()
                }
            }
            true
        }
        binding.imageLocationSetSearch.setOnClickListener {
            mPage = 1
            search()
        }
        binding.layoutLocationSetSearchResult.visibility = View.GONE
        mLayoutManager = LinearLayoutManager(this)
        binding.recyclerLocationSetSearchResult.layoutManager = mLayoutManager
        mSearchLocationAdapter = SearchLocationAdapter(this)
        binding.recyclerLocationSetSearchResult.adapter = mSearchLocationAdapter
        mSearchLocationAdapter!!.setOnItemClickListener { position ->
            val item = mSearchLocationAdapter!!.getItem(position)
            binding.layoutLocationSetSearchResult.visibility = View.GONE
            mMapView.requestFocus()
            val mapPoint = MapPoint.mapPointWithGeoCoord(item.y, item.x)
            mMapView.setMapCenterPoint(mapPoint, false)
        }
        binding.recyclerLocationSetSearchResult.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var pastVisibleItems = 0
            var visibleItemCount = 0
            var totalItemCount = 0
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                visibleItemCount = mLayoutManager!!.childCount
                totalItemCount = mLayoutManager!!.itemCount
                pastVisibleItems = mLayoutManager!!.findFirstVisibleItemPosition()
                if (!mLockListView) {
                    if (totalItemCount < mTotalCount && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
                        if (mPage < 3) {
                            mPage++
                            search()
                        }
                    }
                }
            }
        })
        binding.editLocationSetSearch.onFocusChangeListener = OnFocusChangeListener { view, b ->
            if (b) {
                if (binding.layoutLocationSetSearchResult.visibility != View.VISIBLE) {
                    binding.layoutLocationSetSearchResult.visibility = View.VISIBLE
                }
            }
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
        binding.imageLocationSetMy.setOnClickListener {
            if (!LocationUtil.isLocationEnabled(this@LocationSetActivity)) {
                alertGpsOff(this@LocationSetActivity)
            } else {
                mMapView.currentLocationTrackingMode = CurrentLocationTrackingMode.TrackingModeOff
                mMapView.currentLocationTrackingMode = CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
                showProgress(getString(R.string.msg_getting_current_location))
            }
        }
    }

    private fun search() {
        mKeyword = binding.editLocationSetSearch.text.toString().trim()
        if (mKeyword!!.isEmpty()) {
            showAlert(R.string.msg_input_searchWord)
            return
        }
        mLockListView = true
        hideKeyboard(this)
        showProgress(getString(R.string.msg_searching))
        ApiController.mapApi.requestSearchLocationKeyword(mKeyword!!).enqueue(object : Callback<ResultDaumKeyword> {
            override fun onResponse(call: Call<ResultDaumKeyword>, response: Response<ResultDaumKeyword>) {
                hideProgress()
                mLockListView = false
                binding.layoutLocationSetSearchResult.visibility = View.VISIBLE
                mTotalCount = response.body()!!.meta.total_count
                if (mPage == 1) {
                    mSearchLocationAdapter!!.clear()
                }
                if (mTotalCount > 0) {
                    val itemList = response.body()!!.documents
                    mSearchLocationAdapter!!.addAll(itemList)
                }
            }

            override fun onFailure(call: Call<ResultDaumKeyword>, t: Throwable) {
                hideProgress()
                mLockListView = false
            }
        })
    }

    override fun onBackPressed() {
        if (binding.layoutLocationSetSearchResult.visibility == View.VISIBLE) {
            binding.layoutLocationSetSearchResult.visibility = View.GONE
            mMapView.requestFocus()
        } else {
            super.onBackPressed()
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

    override fun onMapViewMoveFinished(mapView: MapView, mapPoint: MapPoint) {
        LogUtil.e(LOG_TAG, "onMapViewMoveFinished")
        hideProgress()
        mReverseGeoCoder = MapReverseGeoCoder(getString(R.string.kakao_app_key), mapView.mapCenterPoint, this, this)
        mReverseGeoCoder!!.startFindingAddress()
        mIsFindingAddress = true
    }

    /*
    * 리버스 지오커더 리스너
    * */
    override fun onReverseGeoCoderFoundAddress(mapReverseGeoCoder: MapReverseGeoCoder, s: String) {
        LogUtil.e(LOG_TAG, "foundAddress : {}", s)
        mIsFindingAddress = false
        binding.textLocationSetAddress.text = s
    }

    override fun onReverseGeoCoderFailedToFindAddress(mapReverseGeoCoder: MapReverseGeoCoder) {}
    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        val key = intent.getStringExtra(Const.KEY)
        if (StringUtils.isNotEmpty(key) && key == Const.PROFILE) {
            toolbarOption.initializeDefaultToolbar(getString(R.string.word_setting_active_round), ToolbarMenu.RIGHT)
        } else {
            toolbarOption.initializeDefaultToolbar(getString(R.string.word_search_location_map), ToolbarMenu.RIGHT)
        }
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {
        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarMenu.RIGHT -> if (tag == 1) {
                        onBackPressed()
                    }
                    else -> {}
                }
            }
        }
    }
}