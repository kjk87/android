package com.pplus.prnumberbiz.core.location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.pple.pplus.utils.part.logs.LogUtil;
import com.pple.pplus.utils.part.pref.PreferenceUtil;
import com.pple.pplus.utils.part.utils.StringUtils;
import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder;
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener;
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData;
import com.pplus.prnumberbiz.core.network.model.dto.LocationData;

/**
 * Created by 김종경 on 2016-08-26.
 */
public class LocationUtil{

    private static LocationData mSpecifyLocationData;
    private static GoogleApiClient mGoogleApiClient;

    public static double DEFAULT_LATITUDE = 37.4899469;
    public static double DEFAULT_LONGITUDE = 127.0318931;

    public static void init(Context context, GoogleApiClient.ConnectionCallbacks callbacks){

        if(mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context).addApi(LocationServices.API).addConnectionCallbacks(callbacks).build();
        } else {
            mGoogleApiClient.registerConnectionCallbacks(callbacks);
        }
    }

    public static boolean isConnected(){

        return mGoogleApiClient.isConnected();
    }

    public static void connect(){

        if(!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    public static void disConnect(){

        if(mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    public static Location getCurrentLocation(Activity activity){//위치값 가져오기
        // 퍼미션 확인 후 요청
        if(mGoogleApiClient.isConnected()) {
            LogUtil.e("mGoogleApiClient", "isConnected");
            if(ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                return currentLocation;
            }
            return null;
        } else {
            return null;
        }
    }

    public static void startLocationUpdates(final Activity activity, final LocationListener listener){//위치값 요청하기

        // TODO Null point exception이 발생합니다.
        try {
            //            Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            //            context.startActivity(myIntent);
            if(mGoogleApiClient.isConnected()) {
                if(ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    LocationRequest locationRequest = new LocationRequest();
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                    // 위치 정보 요청후 30초간 응답이 없다면 사용자에게 네트워크 정보를 요청 하도록 합니다.
                    final Handler handler = new Handler(Looper.myLooper());

                    final Runnable timeOutRunnable = new Runnable(){

                        @Override
                        public void run(){


                            AlertBuilder.Builder builder = new AlertBuilder.Builder().setAutoCancel(false).setBackgroundClickable(false);
                            builder.addContents(new AlertData.MessageData(activity.getString(R.string.msg_location_error1), AlertBuilder.MESSAGE_TYPE.TEXT, 1));
                            builder.addContents(new AlertData.MessageData(activity.getString(R.string.msg_location_error2), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
                            builder.addContents(new AlertData.MessageData(activity.getString(R.string.msg_location_error3), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
                            builder.setLeftText(activity.getString(R.string.word_cancel)).setRightText(activity.getString(R.string.msg_setting)).setOnAlertResultListener(new OnAlertResultListener(){

                                @Override
                                public void onCancel(){

                                    LocationData locationData = getLastLocation(activity);

                                    Location location = new Location("location");

                                    if(locationData != null) {
                                        location.setLatitude(locationData.getLatitude());
                                        location.setLongitude(locationData.getLongitude());

                                    } else {
                                        location = getDefaultData();
                                    }

                                    listener.onLocationChanged(location);

                                }

                                @Override
                                public void onResult(AlertBuilder.EVENT_ALERT event_alert){

                                    switch (event_alert) {
                                        case RIGHT:

                                            Intent locationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                            activity.startActivityForResult(locationIntent, Const.REQ_LOCATION_CODE);

                                            break;
                                    }

                                    LocationData locationData = getLastLocation(activity);

                                    Location location = new Location("location");

                                    if(locationData != null) {
                                        location.setLatitude(locationData.getLatitude());
                                        location.setLongitude(locationData.getLongitude());

                                    } else {
                                        location = getDefaultData();
                                    }

                                    listener.onLocationChanged(location);


                                }
                            }).builder().show(activity);
                        }

                    };

                    handler.postDelayed(timeOutRunnable, 5 * 1000);

                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, new LocationListener(){

                        @Override
                        public void onLocationChanged(Location location){

                            listener.onLocationChanged(location);
                            handler.removeCallbacks(timeOutRunnable);

//                            disConnect();
//                            stopLocationUpdates();
                        }
                    });
                }
            }

        } catch (NullPointerException e) {

        }

    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    public static void stopLocationUpdates(){
//        disConnect();
//        if(mGoogleApiClient.isConnected()){
//            disConnect();
//
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, new LocationListener(){

                @Override
                public void onLocationChanged(Location location){

                }
            });
//        }



    }

    public static boolean isLocationEnabled(Context context){

        return (isGpsLocationEnabled(context) || isNetworkLocationEnabled(context));
    }

    public static boolean isGpsLocationEnabled(Context context){

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static boolean isNetworkLocationEnabled(Context context){

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) || locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
    }

    public static void setLastLocation(Context context, double latitude, double longitude){

        PreferenceUtil.getDefaultPreference(context).put(Const.LATITUDE, "" + latitude);
        PreferenceUtil.getDefaultPreference(context).put(Const.LONGITUDE, "" + longitude);
    }

    public static LocationData getLastLocation(Context context){

        LocationData data = new LocationData();
        if(StringUtils.isEmpty(PreferenceUtil.getDefaultPreference(context).getString(Const.LATITUDE))) {
            return null;
        }
        if(StringUtils.isEmpty(PreferenceUtil.getDefaultPreference(context).getString(Const.LONGITUDE))) {
            return null;
        }
        data.setLatitude(Double.parseDouble(PreferenceUtil.getDefaultPreference(context).getString(Const.LATITUDE)));
        data.setLongitude(Double.parseDouble(PreferenceUtil.getDefaultPreference(context).getString(Const.LONGITUDE)));
        return data;
    }

    public static Location getDefaultData() {
        Location location = new Location("location");
        location.setLatitude(DEFAULT_LATITUDE);
        location.setLongitude(DEFAULT_LONGITUDE);

        return location;
    }

    public static LocationData getDefaultLocationData() {
        LocationData locationData = new LocationData();
        locationData.setLatitude(DEFAULT_LATITUDE);
        locationData.setLongitude(DEFAULT_LONGITUDE);
        return locationData;
    }

    public static void setSpecifyLocationData(LocationData specifyLocationData){

        LocationUtil.mSpecifyLocationData = specifyLocationData;
    }

    public static LocationData getSpecifyLocationData(){

        // 값이 없는 경우 Default값 생성
        if(mSpecifyLocationData == null) {
            mSpecifyLocationData = getDefaultLocationData();
        }
        return mSpecifyLocationData;
    }
}
