//package com.pplus.prnumberuser.apps.search.ui
//
//import android.content.Intent
//import android.os.Bundle
//import android.view.View
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.bumptech.glide.Glide
//import com.bumptech.glide.request.RequestOptions
//import com.pplus.utils.part.apps.permission.Permission
//import com.pplus.utils.part.apps.permission.PermissionListener
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.logs.LogUtil
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
//import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
//import com.pplus.prnumberuser.apps.common.builder.PPlusPermission
//import com.pplus.prnumberuser.apps.common.mgmt.CategoryInfoManager
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.goods.ui.AlertGoodsLikeCompleteActivity
//import com.pplus.prnumberuser.apps.goods.ui.GoodsDetailActivity2
//import com.pplus.prnumberuser.apps.search.data.CategoryLocationAroundAdapter
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.location.LocationUtil
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.*
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.network.model.response.SubResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.prnumberuser.core.util.ToastUtil
//import kotlinx.android.synthetic.main.activity_location_around_page.*
//import kotlinx.android.synthetic.main.item_custom_balloon.view.*
//import net.daum.mf.map.api.MapPOIItem
//import net.daum.mf.map.api.MapPoint
//import net.daum.mf.map.api.MapView
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.util.*
//
//class LocationAroundGoodsActivity : BaseActivity(), ImplToolbar, MapView.MapViewEventListener, MapView.POIItemEventListener {
//    override fun getPID(): String {
//        return "Main_storemap"
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_location_around_page
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//        image_location_around_page_my.setOnClickListener {
//
//            if (!LocationUtil.isLocationEnabled(this)) {
//                PplusCommonUtil.alertGpsOff(this)
//            } else {
//                mMapView.setCurrentLocationEventListener(null)
//                mMapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
//                mMapView.setCurrentLocationEventListener(object : MapView.CurrentLocationEventListener {
//
//                    override fun onCurrentLocationUpdate(mapView: MapView, mapPoint: MapPoint, v: Float) {
//                        LogUtil.e(LOG_TAG, "onCurrentLocationUpdate")
//                    }
//
//                    override fun onCurrentLocationDeviceHeadingUpdate(mapView: MapView, v: Float) {
//                        hideProgress()
//                    }
//
//                    override fun onCurrentLocationUpdateFailed(mapView: MapView) {
//                        hideProgress()
//                    }
//
//                    override fun onCurrentLocationUpdateCancelled(mapView: MapView) {
//                        hideProgress()
//                    }
//                })
//
//                mMapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
//                showProgress(getString(R.string.msg_getting_current_location))
//            }
//
//
//        }
////        mAddedLocationList = ArrayList<ArrayList<Page>>()
//
//        recycler_location_around_page.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//
//        mCatAdapter = CategoryLocationAroundAdapter()
//        recycler_location_around_page.adapter = mCatAdapter
//        val category = CategoryMajor()
//        category.seqNo = -1L
//        category.name = getString(R.string.word_total)
//        mCatAdapter!!.add(category)
//        if (CategoryInfoManager.getInstance().categoryList != null) {
//            mCatAdapter!!.addAll(CategoryInfoManager.getInstance().categoryList)
//        }
//
//        mCatAdapter!!.setOnItemClickListener(object : CategoryLocationAroundAdapter.OnItemClickListener {
//            override fun onItemClick(position: Int) {
//                resetData(false)
//
//            }
//        })
//    }
//
//    lateinit var mMapView: MapView
//    var mLocationData: LocationData? = null
//    private var mLockListView = false
//    private var mGoodsListArray = ArrayList<ArrayList<Goods>>()
//    var mCatAdapter: CategoryLocationAroundAdapter? = null
//
//    private fun initializeUI() {
//
//        mMapView = MapView(this)
//        layout_location_around_page.addView(mMapView)
//
////        mMapView.setDaumMapApiKey(getString(R.string.kakao_app_key))
//        mMapView.setMapViewEventListener(this)
//        mMapView.setPOIItemEventListener(this)
//        //        mMapView.setCalloutBalloonAdapter(new CustomBalloonAdapter(this));
//        mMapView.removeAllPOIItems()
//
//        mLocationData = LocationUtil.specifyLocationData
//        if (mLocationData != null) {
//            mMapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(mLocationData!!.latitude, mLocationData!!.longitude), false)
//        }
//
//    }
//
//    private fun listCall(left: Double, top: Double, right: Double, bottom: Double) {
//
//        val params = HashMap<String, String>()
//        params["pg"] = "0"
//        params["size"] = "50"
//        params["left"] = left.toString()
//        params["top"] = top.toString()
//        params["right"] = right.toString()
//        params["bottom"] = bottom.toString()
//        if (mLocationData != null) {
//            params["latitude"] = mLocationData!!.latitude.toString()
//            params["longitude"] = mLocationData!!.longitude.toString()
//        }
//        params["expired"] = "false" //null-All, true-유효기간지난것, false-유효기간남은것
//        params["status"] = EnumData.GoodsStatus.ing.status.toString()
//        params["isHotdeal"] = "true"
//        params["isPlus"] = "false"
//
//        if (mCatAdapter!!.mSelectData != null && mCatAdapter!!.mSelectData!!.seqNo != null && mCatAdapter!!.mSelectData!!.seqNo != -1L) {
//            params["categoryMajorSeqNo"] = mCatAdapter!!.mSelectData!!.seqNo.toString()
//        }
//        params["page"] = "0"
//        mLockListView = true
//
//        mGoodsListArray = ArrayList()
//        mMapView.removeAllPOIItems()
////        showProgress("")
//        ApiBuilder.create().getGoodsLocationArea(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<Goods>>> {
//
//            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<Goods>>>?, response: NewResultResponse<SubResultResponse<Goods>>?) {
////                hideProgress()
//                mLockListView = false
//                layout_location_around_goods_bottom.visibility = View.GONE
//                if (response != null) {
//
//                    val goodsList = response.data.content!!
//                    var beforeGoods: Goods? = null
//                    var sameLocationList = ArrayList<Goods>()
//                    if (goodsList.isNotEmpty()) {
//                        for (i in 0..goodsList.size - 1) {
//                            val goods = goodsList[i]
//
//                            if (beforeGoods != null) {
//                                if (beforeGoods.page!!.latitude == goods.page!!.latitude && beforeGoods.page!!.longitude == goods.page!!.longitude) {
//                                    sameLocationList.add(goods)
//                                } else {
//
//                                    mGoodsListArray.add(sameLocationList)
//                                    sameLocationList = ArrayList<Goods>()
//                                    sameLocationList.add(goods)
//                                }
//
//                                if (i == goodsList.size - 1) {
//                                    mGoodsListArray.add(sameLocationList)
//                                }
//
//                            } else {
//                                sameLocationList = ArrayList<Goods>()
//                                sameLocationList.add(goods)
//                                if (goodsList.size == 1) {
//                                    mGoodsListArray.add(sameLocationList)
//                                }
//                            }
//
//                            beforeGoods = goods
//                        }
//
//                        val items = arrayOfNulls<MapPOIItem>(mGoodsListArray.size)
//
//                        for (i in 0..mGoodsListArray.size - 1) {
//                            val item = mGoodsListArray[i][0]
//                            val mapPoint = MapPoint.mapPointWithGeoCoord(item.page!!.latitude!!, item.page!!.longitude!!)
//                            val customMarker = MapPOIItem()
//                            customMarker.itemName = item.page!!.name
//                            customMarker.tag = i
//                            customMarker.mapPoint = mapPoint
//                            customMarker.markerType = MapPOIItem.MarkerType.CustomImage // 마커타입을 커스텀 마커로 지정.
//
//                            customMarker.customImageResourceId = R.drawable.ic_location // 마커 이미지.
//                            customMarker.customSelectedImageResourceId = R.drawable.ic_location
//
//                            customMarker.isMoveToCenterOnSelect = false
//                            customMarker.isCustomImageAutoscale = false // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
//                            customMarker.setCustomImageAnchor(0.5f, 0.5f) // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.
//
//                            val view = layoutInflater.inflate(R.layout.item_custom_balloon, null)
//
//                            if (sameLocationList.size > 1) {
//                                view.text_custom_balloon.text = getString(R.string.format_other2, mGoodsListArray[i][0].page!!.name, mGoodsListArray[i].size - 1)
//                            } else {
//                                view.text_custom_balloon.text = mGoodsListArray[i][0].page!!.name
//                            }
//
//                            customMarker.customCalloutBalloon = view
//                            items[i] = customMarker
//                        }
//
//                        mMapView.addPOIItems(items)
//                    }
//                }
//
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<Goods>>>?, t: Throwable?, response: NewResultResponse<SubResultResponse<Goods>>?) {
////                hideProgress()
//                mLockListView = false
//            }
//        }).build().call()
//
//    }
//
//    var mIsLike = false
//    private fun checkLike(goods: Goods) {
//        val params = HashMap<String, String>()
//
//        params["memberSeqNo"] = LoginInfoManager.getInstance().user.no.toString()
//        params["goodsSeqNo"] = goods.seqNo.toString()
//        params["pageSeqNo"] = goods.page!!.seqNo.toString()
//
//        showProgress("")
//        ApiBuilder.create().getGoodsLikeOne(params).setCallback(object : PplusCallback<NewResultResponse<GoodsLike>> {
//            override fun onResponse(call: Call<NewResultResponse<GoodsLike>>?, response: NewResultResponse<GoodsLike>?) {
//                hideProgress()
//
//                if (response != null) {
//                    mIsLike = response.data != null && response.data.status == 1
//                    image_location_around_goods_goods_like.isSelected = mIsLike
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<GoodsLike>>?, t: Throwable?, response: NewResultResponse<GoodsLike>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    private fun deleteLike(goods: Goods) {
//        val goodsLike = GoodsLike()
//        goodsLike.memberSeqNo = LoginInfoManager.getInstance().user.no
//        goodsLike.goodsSeqNo = goods.seqNo
//        goodsLike.pageSeqNo = goods.page!!.seqNo
//        showProgress("")
//        ApiBuilder.create().deleteGoodsLike(goodsLike).setCallback(object : PplusCallback<NewResultResponse<Any>> {
//            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
//                hideProgress()
//                mIsLike = false
//                image_location_around_goods_goods_like.isSelected = false
//                ToastUtil.show(this@LocationAroundGoodsActivity, R.string.msg_delete_goods_like)
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    private fun postLike(goods: Goods) {
//
//        val goodsLike = GoodsLike()
//        goodsLike.memberSeqNo = LoginInfoManager.getInstance().user.no
//        goodsLike.goodsSeqNo = goods.seqNo
//        goodsLike.pageSeqNo = goods.page!!.seqNo
//        showProgress("")
//        ApiBuilder.create().postGoodsLike(goodsLike).setCallback(object : PplusCallback<NewResultResponse<GoodsLike>> {
//            override fun onResponse(call: Call<NewResultResponse<GoodsLike>>?, response: NewResultResponse<GoodsLike>?) {
//                hideProgress()
//                mIsLike = true
//                image_location_around_goods_goods_like.isSelected = true
//                val intent = Intent(this@LocationAroundGoodsActivity, AlertGoodsLikeCompleteActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                startActivity(intent)
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<GoodsLike>>?, t: Throwable?, response: NewResultResponse<GoodsLike>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    private fun setGoodsInfo(goods: Goods) {
//        layout_location_around_goods_bottom.visibility = View.VISIBLE
//
//        layout_location_around_goods_bottom.setOnClickListener {
//            val intent = Intent(this, GoodsDetailActivity2::class.java)
//            intent.putExtra(Const.DATA, goods)
//            startActivityForResult(intent, Const.REQ_DETAIL)
//        }
//        image_location_around_goods_goods_like.setOnClickListener(null)
//        if (LoginInfoManager.getInstance().isMember) {
//            checkLike(goods)
//        }
//
//        image_location_around_goods_goods_like.setOnClickListener {
//            if (!PplusCommonUtil.loginCheck(this@LocationAroundGoodsActivity)) {
//                return@setOnClickListener
//            }
//
//            if (mIsLike) {
//                deleteLike(goods)
//            } else {
//                postLike(goods)
//            }
//        }
//
//        if (goods.goodsImageList != null && goods.goodsImageList!!.isNotEmpty()) {
//            Glide.with(this).load(goods.goodsImageList!![0].image).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(image_location_around_goods)
//
//        } else {
//            image_location_around_goods.setImageResource(R.drawable.prnumber_default_img)
//        }
//        if (goods.count != -1) {
//            var soldCount = 0
//            if (goods.soldCount != null) {
//                soldCount = goods.soldCount!!
//            }
//            text_location_around_goods_remain_count.visibility = View.VISIBLE
//            text_location_around_goods_remain_count.text = PplusCommonUtil.fromHtml(getString(R.string.html_goods_remain_count, FormatUtil.getMoneyType((goods.count!! - soldCount).toString())))
//        } else {
//            text_location_around_goods_remain_count.visibility = View.GONE
//        }
//
//        text_location_around_goods_name.text = goods.name
//
//        if (goods.originPrice != null && goods.originPrice!! > 0) {
//
//            if (goods.originPrice!! <= goods.price!!) {
//                text_location_around_goods_sale_price.visibility = View.GONE
//            } else {
//                text_location_around_goods_sale_price.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(goods.originPrice.toString()))
//                text_location_around_goods_sale_price.visibility = View.VISIBLE
//            }
//
//        } else {
//            text_location_around_goods_sale_price.visibility = View.GONE
//        }
//
////        holder.text_discount.visibility = View.GONE
//        if (goods.discountRatio != null && goods.discountRatio!! > 0) {
//            text_location_around_goods_discount_ratio.visibility = View.VISIBLE
//            text_location_around_goods_discount_ratio.text = PplusCommonUtil.fromHtml(getString(R.string.html_percent_unit, goods.discountRatio!!.toInt().toString()))
//        } else {
//            text_location_around_goods_discount_ratio.visibility = View.GONE
//        }
//        text_location_around_goods_sale_price.text = PplusCommonUtil.fromHtml(getString(R.string.html_money_unit, FormatUtil.getMoneyType(goods.price.toString())))
//
//
//        if (goods.distance != null) {
//            text_location_around_goods_distance.visibility = View.VISIBLE
//            var strDistance: String? = null
//            if (goods.distance!! > 1) {
//                strDistance = String.format("%.2f", goods.distance!!)
//                text_location_around_goods_distance.text = PplusCommonUtil.fromHtml(getString(R.string.html_distance, strDistance, "km"))
//            } else {
//                strDistance = (goods.distance!! * 1000).toInt().toString()
//                text_location_around_goods_distance.text = PplusCommonUtil.fromHtml(getString(R.string.html_distance, strDistance, "m"))
//            }
//        } else {
//            text_location_around_goods_distance.visibility = View.GONE
//        }
//    }
//
//    var beforeTop: Double? = null
//    var beforeLeft: Double? = null
//    var beforeBottom: Double? = null
//    var beforeRight: Double? = null
//    var beforeCategory: CategoryMajor? = null
//
//    fun resetData(reset: Boolean) {
//        val topLeft = MapPoint.mapPointWithScreenLocation(layout_location_around_page.x.toDouble(), layout_location_around_page.y.toDouble())
//        val bottomRight = MapPoint.mapPointWithScreenLocation((layout_location_around_page.x + layout_location_around_page.width).toDouble(), (layout_location_around_page.y + layout_location_around_page.height).toDouble())
//
//        val left = topLeft.mapPointGeoCoord.longitude
//        val top = topLeft.mapPointGeoCoord.latitude
//        val right = bottomRight.mapPointGeoCoord.longitude
//        val bottom = bottomRight.mapPointGeoCoord.latitude
//
//        if (reset) {
//            beforeCategory = mCatAdapter!!.mSelectData
//            beforeBottom = bottom
//            beforeLeft = left
//            beforeTop = top
//            beforeRight = right
//            listCall(left, top, right, bottom)
//        } else {
//            if (left == beforeLeft && top == beforeTop && right == beforeRight && bottom == beforeBottom && beforeCategory == mCatAdapter!!.mSelectData) {
//                return
//            } else {
//                beforeCategory = mCatAdapter!!.mSelectData
//                beforeBottom = bottom
//                beforeLeft = left
//                beforeTop = top
//                beforeRight = right
//                listCall(left, top, right, bottom)
//            }
//        }
//
//
//    }
//
//    override fun onMapViewInitialized(p0: MapView?) {
//        MapView.setMapTilePersistentCacheEnabled(true)
//    }
//
//
//    override fun onMapViewCenterPointMoved(p0: MapView?, p1: MapPoint?) {
//
//    }
//
//    override fun onMapViewMoveFinished(mapView: MapView?, p1: MapPoint?) {
//        hideProgress()
//        if (mMapView.currentLocationTrackingMode == MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading) {
//            mMapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithMarkerHeadingWithoutMapMoving
//        }
//        resetData(false)
//    }
//
//    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?, p2: MapPOIItem.CalloutBalloonButtonType?) {
//
//        if (p1!!.tag == -1) {
//            return
//        }
//
//        val list = mGoodsListArray[p1.tag]
//        if (list.size == 1) {
////            if(!PplusCommonUtil.loginCheck(this@LocationAroundGoodsActivity)){
////                return
////            }
//            setGoodsInfo(list[0])
//        } else {
//            val contents = arrayOfNulls<String>(list.size)
//
//            for (i in list.indices) {
//                contents[i] = list.get(i).name
//            }
//
//            val builder = AlertBuilder.Builder()
//            builder.setTitle(getString(R.string.word_select))
//            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER)
//            builder.setContents(*contents)
//            builder.setLeftText(getString(R.string.word_cancel))
//            builder.setOnAlertResultListener(object : OnAlertResultListener {
//
//                override fun onCancel() {
//
//                }
//
//                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//
//                    when (event_alert) {
//
//                        AlertBuilder.EVENT_ALERT.LIST -> {
////                            if(!PplusCommonUtil.loginCheck(this@LocationAroundGoodsActivity)){
////                                return
////                            }
//                            setGoodsInfo(list[event_alert.getValue() - 1])
//                        }
//                    }
//                }
//            }).builder().show(this)
//        }
//    }
//
//    override fun onMapViewDoubleTapped(p0: MapView?, p1: MapPoint?) {}
//    override fun onMapViewDragStarted(p0: MapView?, p1: MapPoint?) {}
//    override fun onMapViewDragEnded(p0: MapView?, p1: MapPoint?) {}
//    override fun onMapViewSingleTapped(p0: MapView?, p1: MapPoint?) {}
//    override fun onMapViewZoomLevelChanged(p0: MapView?, p1: Int) {}
//    override fun onMapViewLongPressed(p0: MapView?, p1: MapPoint?) {}
//    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {}
//    override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {}
//    override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {}
//
//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.msg_view_map), ToolbarOption.ToolbarMenu.RIGHT)
//        return toolbarOption
//    }
//
//    override fun getOnToolbarClickListener(): OnToolbarListener {
//
//        return OnToolbarListener { v, toolbarMenu, tag ->
//            when (toolbarMenu) {
//                ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
//                    onBackPressed()
//                }
//            }
//        }
//    }
//
//    override fun onResume() {
//        super.onResume()
//
//        beforeTop = null
//        beforeLeft = null
//        beforeBottom = null
//        beforeRight = null
//        initializeUI()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        mMapView.removeAllPOIItems()
//        layout_location_around_page.removeAllViews()
//    }
//
//    override fun onBackPressed() {
//        super.onBackPressed()
//        mMapView.removeAllPOIItems()
//        layout_location_around_page.removeAllViews()
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when(requestCode){
//            Const.REQ_SIGN_IN->{
//                resetData(true)
//            }
//        }
//    }
//}
