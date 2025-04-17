package com.pplus.prnumberbiz.apps.shop.ui;

import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pple.pplus.utils.part.logs.LogUtil;
import com.pple.pplus.utils.part.utils.StringUtils;
import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.core.network.model.dto.Page;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

public class LocationShopActivity extends BaseActivity implements ImplToolbar, MapView.MapViewEventListener{

    @Override
    public String getPID(){

        return "";
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_location_shop;
    }

    private MapView mMapView;
    private Page mPage;

    private boolean isZoomOnce = false;

    @Override
    public void initializeView(Bundle savedInstanceState){

        mMapView = new MapView(this);
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.layout_location_pageMap);
        mapViewContainer.addView(mMapView);
        mMapView.setDaumMapApiKey(getString(R.string.kakao_app_key));
        mMapView.setMapViewEventListener(this);
        mMapView.removeAllPOIItems();
        //        mMapView.setPOIItemEventListener(this);
        mPage = getIntent().getParcelableExtra(Const.PAGE);

        if(mPage == null) {
            return;
        }

        String address = mPage.getAddress().getRoadBase();

        if(StringUtils.isNotEmpty(mPage.getAddress().getRoadDetail())){
            address = address + " " + mPage.getAddress().getRoadDetail();
        }

        TextView textName = (TextView) findViewById(R.id.text_location_page_name);
        textName.setText(address);
        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(mPage.getLatitude(), mPage.getLongitude());

        //내위치 마커만들기
//        MapPOIItem myMarker = new MapPOIItem();
//        if (StringUtils.isNotEmpty(LocationUtil.getSpecifyLocationData().getAddress())) {
//            myMarker.setItemName(LocationUtil.getSpecifyLocationData().getAddress());
//        } else {
//            myMarker.setItemName(getString(R.string.word_my_location));
//        }
//        myMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
//        myMarker.setCustomImageResourceId(R.drawable.ic_my_location);
//        myMarker.setCustomImageAutoscale(false);
//        myMarker.setCustomImageAnchor(0.5f, 0.5f);
//        mMapView.setCalloutBalloonAdapter(new CustomBalloonAdapter(this));
//        MapPoint myPoint = MapPoint.mapPointWithGeoCoord(LocationUtil.getSpecifyLocationData().getLatitude(), LocationUtil.getSpecifyLocationData().getLongitude());
//        myMarker.setMapPoint(myPoint);

        MapPOIItem customMarker = new MapPOIItem();
        customMarker.setItemName(mPage.getName());
        customMarker.setTag(0);
        customMarker.setMapPoint(mapPoint);
        customMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
        customMarker.setCustomImageResourceId(R.drawable.ic_location); // 마커 이미지.
        customMarker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
        customMarker.setCustomImageAnchor(0.5f, 0.5f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.

        MapPOIItem[] items = new MapPOIItem[1];
//        items[0] = myMarker;
        items[0] = customMarker;
        mMapView.addPOIItems(items);
//        mMapView.setMapCenterPoint(mapPoint, false);
//        mMapView.setZoomLevel(1, false);
        mMapView.fitMapViewAreaToShowAllPOIItems();

    }

    @Override
    public void onMapViewInitialized(MapView mapView){
        MapView.setMapTilePersistentCacheEnabled(true);
        LogUtil.i("yklee", "onMapViewInitialized");
    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint){
        LogUtil.i("yklee", "onMapViewCenterPointMoved");
    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i){
        LogUtil.i("yklee", "onMapViewZoomLevelChanged = {}" , i);
        if (!isZoomOnce) {
            isZoomOnce = true;
            mapView.setZoomLevelFloat(mapView.getZoomLevel()+0.5f, true);
        }
    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint){
        LogUtil.i("yklee", "onMapViewSingleTapped");
    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint){
        LogUtil.i("yklee", "onMapViewDoubleTapped");
    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint){
        LogUtil.i("yklee", "onMapViewLongPressed");
    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint){
        LogUtil.i("yklee", "onMapViewDragStarted");
    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint){
        LogUtil.i("yklee", "onMapViewDragEnded");
    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint){
        LogUtil.i("yklee", "onMapViewMoveFinished");
    }

    @NonNull
    @Override
    public ToolbarOption getToolbarOption(){

        ToolbarOption toolbarOption = new ToolbarOption(this);
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_location), ToolbarOption.ToolbarMenu.RIGHT);
        return toolbarOption;
    }

    @Override
    public OnToolbarListener getOnToolbarClickListener(){

        return new OnToolbarListener(){

            @Override
            public void onClick(View v, ToolbarOption.ToolbarMenu toolbarMenu, Object tag){

                switch (toolbarMenu) {
                    case RIGHT:
                        if(tag.equals(1)) {
                            onBackPressed();
                        }
                        break;
                }

            }
        };

    }

    @Override
    public void onBackPressed(){

        super.onBackPressed();
        mMapView.removeAllPOIItems();
        ((ViewGroup) findViewById(R.id.layout_location_pageMap)).removeAllViews();
    }
}
