package com.pplus.luckybol.apps.common.service;//package com.pplus.luckybol.apps.common.service;
//
//import android.Manifest;
//import android.app.PendingIntent;
//import android.app.job.JobParameters;
//import android.app.job.JobService;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.os.Looper;
//import android.support.annotation.Nullable;
//import android.support.v4.content.ContextCompat;
//
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationServices;
//import com.pplus.utils.part.logs.LogUtil;
//import com.pplus.luckybol.core.location.LocationUtil;
//
///**
// * Created by imac on 2017. 11. 29..
// */
//
//public class LocationJobService extends JobService{
//
//    private static final String LOG_TAG = "LocationJobService";
//    private GoogleApiClient mGoogleApiClient;
//
//    @Override
//    public boolean onStartJob(JobParameters params){
//
//        if(LocationUtil.isLocationEnabled(this)) {
//
//            mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks(){
//
//                @Override
//                public void onConnected(@Nullable Bundle bundle){
//                    if(ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//
//                        LocationRequest locationRequest = new LocationRequest();
//                        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//                        locationRequest.setInterval(600000);
//                        locationRequest.setFastestInterval(600000);
//                        locationRequest.setNumUpdates(1);
//
//                        LogUtil.e(LOG_TAG, "locationRequest");
//
//                        Intent intent = new Intent(LocationJobService.this, LocationIntentService.class);
//                        PendingIntent pendingIntent = PendingIntent.getService(LocationJobService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                        LocationServices.getFusedLocationProviderClient(LocationJobService.this).requestLocationUpdates(locationRequest, pendingIntent);
//                        mGoogleApiClient.disconnect();
//                    }
//                }
//
//                @Override
//                public void onConnectionSuspended(int i){
//                    mGoogleApiClient.disconnect();
//                }
//            }).build();
//            mGoogleApiClient.connect();
//        }
//
//        return true;
//    }
//
//    @Override
//    public boolean onStopJob(JobParameters params){
//
//        return false;
//    }
//}
