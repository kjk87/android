//package com.pplus.prnumberbiz.apps.shop.ui;
//
//
//import android.app.Activity;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.v4.view.ViewPager;
//import android.support.v4.widget.NestedScrollView;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.pple.pplus.utils.part.logs.LogUtil;
//import com.pple.pplus.utils.part.utils.StringUtils;
//import com.pplus.prnumberbiz.Const;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment;
//import com.pplus.prnumberbiz.apps.common.ui.custom.DirectionIndicator;
//import com.pplus.prnumberbiz.apps.pages.data.IntroduceImagePagerAdapter;
//import com.pplus.prnumberbiz.apps.pages.ui.IntroduceImageDetailActivity;
//import com.pplus.prnumberbiz.apps.pages.ui.PageConfigActivity2;
//import com.pplus.prnumberbiz.core.code.common.SnsTypeCode;
//import com.pplus.prnumberbiz.core.network.ApiBuilder;
//import com.pplus.prnumberbiz.core.network.model.dto.Attachment;
//import com.pplus.prnumberbiz.core.network.model.dto.ImgUrl;
//import com.pplus.prnumberbiz.core.network.model.dto.Page;
//import com.pplus.prnumberbiz.core.network.model.dto.Sns;
//import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
//import com.pplus.prnumberbiz.core.util.PplusCommonUtil;
//import com.pplus.prnumberbiz.core.util.PplusNumberUtil;
//
//import net.daum.mf.map.api.MapPOIItem;
//import net.daum.mf.map.api.MapPoint;
//import net.daum.mf.map.api.MapView;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import network.common.PplusCallback;
//import retrofit2.Call;
//
//public class ShopIntroduceFragment extends BaseFragment implements MapView.MapViewEventListener, MapView.POIItemEventListener{
//
//
//    public ShopIntroduceFragment(){
//        // Required empty public constructor
//    }
//
//    public static ShopIntroduceFragment newInstance(){
//
//        ShopIntroduceFragment fragment = new ShopIntroduceFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public int getLayoutResourceId(){
//
//        return R.layout.fragment_shop_introduce;
//    }
//
//    private Page mPage;
//    private IntroduceImagePagerAdapter mIntroPagerAdapter;
//    private DirectionIndicator mDirectionIndicator;
//    private MapView mMapView;
//    private View layout_sns, image_facebook, image_kakao, image_twitter, image_instagram, layout_config, layout_opening_hours, layout_convenience_info, layout_parking, layout_address, text_homepage, text_blog;
//    private NestedScrollView scroll;
////    private ImageView image_profile;
//    private TextView text_name, text_prnumber, text_introduce, text_address, text_opening_hours, text_convenience_info, text_parking;
//    private ViewGroup mapViewContainer;
//
//    @Override
//    public void initializeView(View container){
//
////        image_profile = (ImageView) container.findViewById(R.id.image_shop_introduce_profile);
//        text_name = (TextView) container.findViewById(R.id.tex_shop_introduce_name);
//        text_prnumber = (TextView) container.findViewById(R.id.text_shop_intorduce_prnumber);
//        text_introduce = (TextView) container.findViewById(R.id.text_shop_introduce);
//
//        layout_opening_hours = container.findViewById(R.id.layout_shop_introduce_opening_hours);
//        layout_convenience_info = container.findViewById(R.id.layout_shop_introduce_convenience_info);
//        layout_parking = container.findViewById(R.id.layout_shop_introduce_parking);
//        layout_address = container.findViewById(R.id.layout_shop_introduce_address);
//        text_address = (TextView) container.findViewById(R.id.text_shop_introduce_address);
//        text_homepage = container.findViewById(R.id.image_shop_introduce_homepage);
//        text_blog = container.findViewById(R.id.image_shop_introduce_blog);
//        text_homepage.setOnClickListener(this);
//        text_blog.setOnClickListener(this);
//
//        text_opening_hours = (TextView) container.findViewById(R.id.text_shop_introduce_opening_hours);
//        text_convenience_info = (TextView) container.findViewById(R.id.text_shop_introduce_convenience_info);
//        text_parking = (TextView) container.findViewById(R.id.text_shop_introduce_parking);
//        mapViewContainer = (ViewGroup) container.findViewById(R.id.text_shop_introduce_map);
//
//        ViewPager pager = (ViewPager) container.findViewById(R.id.pager_shop_introduce);
//        mIntroPagerAdapter = new IntroduceImagePagerAdapter(getActivity());
//        pager.setAdapter(mIntroPagerAdapter);
//
//        mIntroPagerAdapter.setListener(new IntroduceImagePagerAdapter.OnItemClickListener(){
//
//            @Override
//            public void onItemClick(int position){
//
//                if(StringUtils.isNotEmpty(mIntroPagerAdapter.getData(position).getTargetType()) && mIntroPagerAdapter.getData(position).getTargetType().equals("youtube")){
//
//                }else{
//                    Intent intent = new Intent(getActivity(), IntroduceImageDetailActivity.class);
//                    intent.putExtra(Const.DATA, (ArrayList<Attachment>) mIntroPagerAdapter.getDataList());
//                    intent.putExtra(Const.POSITION, position);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                    startActivityForResult(intent, Const.REQ_INTRODUCE_IMAGE_DETAIL);
//
//                }
//
//            }
//        });
//        mDirectionIndicator = (DirectionIndicator) container.findViewById(R.id.indicator_shop_introduce);
//        mDirectionIndicator.setVisibility(View.GONE);
//        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
//
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels){
//
//            }
//
//            @Override
//            public void onPageSelected(int position){
//
//                mDirectionIndicator.setCurrentItem(position);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state){
//
//            }
//        });
//
//        layout_sns = container.findViewById(R.id.layout_shop_introduce_sns);
//        image_facebook = container.findViewById(R.id.image_shop_introduce_facebook);
//        image_kakao = container.findViewById(R.id.image_shop_introduce_kakao);
//        image_twitter = container.findViewById(R.id.image_shop_introduce_twitter);
//        image_instagram = container.findViewById(R.id.image_shop_introduce_instagram);
//        image_facebook.setOnClickListener(onSnsClickListener);
//        image_kakao.setOnClickListener(onSnsClickListener);
//        image_twitter.setOnClickListener(onSnsClickListener);
//        image_instagram.setOnClickListener(onSnsClickListener);
//
//        scroll = (NestedScrollView) container.findViewById(R.id.scroll_shop_introduce);
//
//        layout_config = container.findViewById(R.id.layout_shop_introduce_config);
//        layout_config.setOnClickListener(this);
//        scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener(){
//
//            @Override
//            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY){
//
//                LogUtil.e(LOG_TAG, "scrollY : {}, oldScrollY : {}", scrollY, oldScrollY);
//                if(scrollY > oldScrollY) {//down
//                    hide();
//                } else {//up
//                    show();
//                }
//            }
//        });
//
//    }
//
//    private boolean animationRun;
//
//    private void hide(){
//
//        animationRun = true;
//
//        Animation hideAni = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_xy_hide);
//        hideAni.setAnimationListener(new Animation.AnimationListener(){
//
//            @Override
//            public void onAnimationStart(Animation animation){
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation){
//
//                layout_config.setVisibility(View.GONE);
//                animationRun = false;
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation){
//
//            }
//        });
//        layout_config.startAnimation(hideAni);
//    }
//
//    private void show(){
//
//        if(!layout_config.isEnabled()) {
//            return;
//        }
//
//        animationRun = true;
//
//        Animation showAni = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_xy_show);
//        showAni.setAnimationListener(new Animation.AnimationListener(){
//
//            @Override
//            public void onAnimationStart(Animation animation){
//
//                layout_config.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation){
//
//                animationRun = false;
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation){
//
//            }
//        });
//        layout_config.startAnimation(showAni);
//    }
//
//    @Override
//    public void init(){
//
//        mPage = LoginInfoManager.getInstance().getUser().getPage();
//
////        if(mPage.getProfileImage() != null && StringUtils.isNotEmpty(mPage.getProfileImage().getUrl())) {
////            Glide.with(this).load(mPage.getProfileImage().getUrl()).centerCrop().placeholder(R.drawable.img_post_profile_default).error(R.drawable.img_post_profile_default).into(image_profile);
////        }
//
//        if(StringUtils.isNotEmpty(mPage.getName())){
//            text_name.setText("" + mPage.getName());
//        }
//
//        if(mPage.getNumberList().size() > 0){
//            text_prnumber.setText("" + PplusNumberUtil.getPrNumberFormat(mPage.getNumberList().get(0).getNumber()));
//        }
//
//
//        String introduction = null;
//        if(StringUtils.isNotEmpty(mPage.getCatchphrase())){
//            introduction = mPage.getCatchphrase() + "\n\n";
//        }
//
//        if(StringUtils.isNotEmpty(mPage.getIntroduction())){
//            introduction = introduction + mPage.getIntroduction();
//        }
//
//        text_introduce.setText(introduction);
//
//        if(mPage.getProperties() == null) {
//            layout_opening_hours.setVisibility(View.GONE);
//            layout_convenience_info.setVisibility(View.GONE);
//            layout_parking.setVisibility(View.GONE);
//            text_homepage.setVisibility(View.GONE);
//            text_blog.setVisibility(View.GONE);
//
//        } else {
//
//            if(StringUtils.isNotEmpty(mPage.getProperties().getHomepageUrl())) {
//                text_homepage.setVisibility(View.VISIBLE);
//            } else {
//                text_homepage.setVisibility(View.GONE);
//            }
//
//            if(StringUtils.isNotEmpty(mPage.getProperties().getBlogUrl())) {
//                text_blog.setVisibility(View.VISIBLE);
//            } else {
//                text_blog.setVisibility(View.GONE);
//            }
//
//            if(StringUtils.isNotEmpty(mPage.getProperties().getOpeningHours())) {
//                layout_opening_hours.setVisibility(View.VISIBLE);
//                text_opening_hours.setText(mPage.getProperties().getOpeningHours());
//            } else {
//                layout_opening_hours.setVisibility(View.GONE);
//            }
//
//            if(StringUtils.isNotEmpty(mPage.getProperties().getConvenienceInfo())) {
//                layout_convenience_info.setVisibility(View.VISIBLE);
//
//                text_convenience_info.setText(mPage.getProperties().getConvenienceInfo());
//            } else {
//                layout_convenience_info.setVisibility(View.GONE);
//            }
//
//            if(StringUtils.isNotEmpty(mPage.getProperties().getParkingInfo())) {
//                layout_parking.setVisibility(View.VISIBLE);
//
//                text_parking.setText(mPage.getProperties().getParkingInfo());
//            } else {
//                layout_parking.setVisibility(View.GONE);
//            }
//        }
//
//        if(mPage.getAddress() == null || StringUtils.isEmpty(mPage.getAddress().getRoadBase())) {
//            layout_address.setVisibility(View.GONE);
//        } else {
//            layout_address.setVisibility(View.VISIBLE);
//            layout_address.setOnClickListener(this);
//            String detailAddress = "";
//            if(StringUtils.isNotEmpty(mPage.getAddress().getRoadDetail())){
//                detailAddress = mPage.getAddress().getRoadDetail();
//            }
//            text_address.setText(mPage.getAddress().getRoadBase() + " " + detailAddress);
//
//            if(mPage.getLatitude() != null && mPage.getLongitude() != null) {
//                mapViewContainer.setVisibility(View.VISIBLE);
//                mMapView = new MapView(getActivity());
//
//                mMapView.setOnTouchListener(new View.OnTouchListener(){
//
//                    @Override
//                    public boolean onTouch(View view, MotionEvent motionEvent){
//
//                        view.getParent().requestDisallowInterceptTouchEvent(true);
//                        return false;
//                    }
//                });
//
//                MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(mPage.getLatitude(), mPage.getLongitude());
//
//                mMapView.setMapCenterPoint(mapPoint, false);
//                mapViewContainer.addView(mMapView);
//                mMapView.setDaumMapApiKey(getString(R.string.kakao_app_key));
//                mMapView.setMapViewEventListener(this);
//                mMapView.setPOIItemEventListener(this);
//                mMapView.removeAllPOIItems();
//                mMapView.setDefaultCurrentLocationMarker();
//
//                MapPOIItem customMarker = new MapPOIItem();
//                customMarker.setItemName(mPage.getName());
//                customMarker.setTag(0);
//                customMarker.setMapPoint(mapPoint);
//                customMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
//                customMarker.setCustomImageResourceId(R.drawable.ic_location); // 마커 이미지.
//                customMarker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
//                customMarker.setCustomImageAnchor(0.5f, 0.5f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.
//                mMapView.addPOIItem(customMarker);
//
//            } else {
//                mapViewContainer.setVisibility(View.GONE);
//            }
//        }
//
//        callIntroduceImage();
//        getSnsLink();
//    }
//
//    private void getSnsLink(){
//
//        Map<String, String> params = new HashMap<>();
//        params.put("no", "" + mPage.getNo());
//
//        ApiBuilder.create().getSnsLinkAll(params).setCallback(new PplusCallback<NewResultResponse<Sns>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<Sns>> call, NewResultResponse<Sns> response){
//
//                if(response.getDatas() == null || response.getDatas().size() == 0) {
//                    layout_sns.setVisibility(View.GONE);
//                } else {
//                    layout_sns.setVisibility(View.VISIBLE);
//                    initSNS(response.getDatas());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<Sns>> call, Throwable t, NewResultResponse<Sns> response){
//
//            }
//        }).build().call();
//    }
//
//    private void initSNS(List<Sns> snsList){
//
//        if(snsList != null && !snsList.isEmpty()) {
//            for(Sns sns : snsList) {
//                setSns(sns);
//            }
//        }
//    }
//
//    private void setSns(Sns sns){
//
//        if(sns != null) {
//            if(StringUtils.isEmpty(sns.getUrl())) {
//                switch (SnsTypeCode.valueOf(sns.getType())) {
//
//                    case twitter:
//                        image_twitter.setVisibility(View.GONE);
//                        break;
//                    case facebook:
//                        image_facebook.setVisibility(View.GONE);
//                        break;
//                    case instagram:
//                        image_instagram.setVisibility(View.GONE);
//                        break;
//                    case kakao:
//                        image_kakao.setVisibility(View.GONE);
//                        break;
//
//                }
//            } else {
//                switch (SnsTypeCode.valueOf(sns.getType())) {
//
//                    case twitter:
//                        image_twitter.setVisibility(View.VISIBLE);
//                        image_twitter.setTag(sns);
//                        break;
//                    case facebook:
//                        image_facebook.setVisibility(View.VISIBLE);
//                        image_facebook.setTag(sns);
//                        break;
//                    case instagram:
//                        image_instagram.setVisibility(View.VISIBLE);
//                        image_instagram.setTag(sns);
//                        break;
//                    case kakao:
//                        image_kakao.setVisibility(View.VISIBLE);
//                        image_kakao.setTag(sns);
//                        break;
//                }
//            }
//        }
//    }
//
//    private View.OnClickListener onSnsClickListener = new View.OnClickListener(){
//
//        @Override
//        public void onClick(View v){
//
//            Sns sns = (Sns) v.getTag();
//            snsEvent(sns);
//        }
//    };
//
//    private void snsEvent(Sns sns){
//
//        // SNS 페이지 이동
//        if(StringUtils.isNotEmpty(sns.getUrl())) {
//            // 계정으로 이동
//            startActivity(PplusCommonUtil.Companion.getOpenSnsIntent(getActivity(), SnsTypeCode.valueOf(sns.getType()), sns.getUrl(), sns.isLinkage()));
//        }
//
//    }
//
//    private List<Attachment> attachmentList;
//
//    private void callIntroduceImage(){
//
//        Map<String, String> params = new HashMap<>();
//        params.put("no", "" + mPage.getNo());
//        ApiBuilder.create().getIntroImageAll(params).setCallback(new PplusCallback<NewResultResponse<Attachment>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<Attachment>> call, NewResultResponse<Attachment> response){
//                attachmentList = new ArrayList<Attachment>();
//                attachmentList.addAll(response.getDatas());
//                if(StringUtils.isNotEmpty(mPage.getMainMovieUrl())){
//                    Attachment mainYoutube = new Attachment();
//                    mainYoutube.setTargetType("youtube");
//                    mainYoutube.setUrl(mPage.getMainMovieUrl());
//                    attachmentList.add(0, mainYoutube);
//                }
//
//                callMovieList();
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<Attachment>> call, Throwable t, NewResultResponse<Attachment> response){
//
//            }
//        }).build().call();
//    }
//
//    private void callMovieList(){
//        Map<String, String> params = new HashMap<>();
//        params.put("no", "" + mPage.getNo());
//        ApiBuilder.create().getIntroMovieAll(params).setCallback(new PplusCallback<NewResultResponse<ImgUrl>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<ImgUrl>> call, NewResultResponse<ImgUrl> response){
//                List<ImgUrl> youtubeList = response.getDatas();
//                for(int i = 0; i < youtubeList.size(); i++){
//                    Attachment addMovieList = new Attachment();
//                    addMovieList.setTargetType("youtube");
//                    addMovieList.setUrl(youtubeList.get(i).getUrl());
//                    attachmentList.add(addMovieList);
//                }
//
//                if(attachmentList.size() > 0) {
//
//                    mDirectionIndicator.removeAllViews();
//                    mDirectionIndicator.setVisibility(View.VISIBLE);
//                    mDirectionIndicator.build(LinearLayout.HORIZONTAL, attachmentList.size());
//                } else {
//                    mDirectionIndicator.setVisibility(View.GONE);
//                }
//
//                mIntroPagerAdapter.setDataList(attachmentList);
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<ImgUrl>> call, Throwable t, NewResultResponse<ImgUrl> response){
//
//            }
//        }).build().call();
//    }
//
//    @Override
//    public void onClick(View v){
//
//        Intent intent;
//        switch (v.getId()) {
//            case R.id.layout_shop_introduce_address:
//                mapViewContainer.removeAllViews();
//                intent = new Intent(getActivity(), LocationShopActivity.class);
//                intent.putExtra(Const.PAGE, mPage);
//                startActivityForResult(intent, Const.REQ_LOCATION_CODE);
//                break;
//            case R.id.layout_shop_introduce_config:
//                intent = new Intent(getActivity(), PageConfigActivity2.class);
//                intent.putExtra(Const.FIRST, false);
//                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                getActivity().startActivityForResult(intent, Const.REQ_SET_PROFILE);
//                break;
//            case R.id.image_shop_introduce_homepage:
//                try {
//                    String url = mPage.getProperties().getHomepageUrl();
//                    if(!url.startsWith("http://") && !url.startsWith("https://")) {
//                        url = "http://" + url;
//                    }
//                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                    startActivity(intent);
//                } catch (Exception e) {
//
//                }
//
//
//                break;
//            case R.id.image_shop_introduce_blog:
//                try {
//                    String url = mPage.getProperties().getBlogUrl();
//                    if(!url.startsWith("http://") && !url.startsWith("https://")) {
//                        url = "http://" + url;
//                    }
//                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                    startActivity(intent);
//                } catch (Exception e) {
//
//                }
//
//                break;
//        }
//    }
//
//    @Override
//    public String getPID(){
//
//        return null;
//    }
//
//    @Override
//    public String getSID(){
//
//        return null;
//    }
//
//    @Override
//    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem){
//
//    }
//
//    @Override
//    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem){
//
//        mapViewContainer.removeAllViews();
//        Intent intent = new Intent(getActivity(), LocationShopActivity.class);
//        intent.putExtra(Const.PAGE, mPage);
//        startActivityForResult(intent, Const.REQ_LOCATION_CODE);
//    }
//
//    @Override
//    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType){
//
//    }
//
//    @Override
//    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint){
//
//    }
//
//    @Override
//    public void onMapViewInitialized(MapView mapView){
//
//        scroll.scrollTo(0, 0);
//    }
//
//    @Override
//    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint){
//
//    }
//
//    @Override
//    public void onMapViewZoomLevelChanged(MapView mapView, int i){
//
//    }
//
//    @Override
//    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint){
//
//    }
//
//    @Override
//    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint){
//
//    }
//
//    @Override
//    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint){
//
//    }
//
//    @Override
//    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint){
//
//    }
//
//    @Override
//    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint){
//
//    }
//
//    @Override
//    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint){
//
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data){
//
//        super.onActivityResult(requestCode, resultCode, data);
//
//        switch (requestCode) {
//            case Const.REQ_LOCATION_CODE:
//            case Const.REQ_SET_PROFILE:
//                init();
//                break;
//        }
//
//        if(resultCode == Activity.RESULT_OK) {
//            switch (requestCode) {
//
//            }
//        }
//    }
//}
