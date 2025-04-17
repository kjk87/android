//package com.pplus.luckybol.apps.common.service
//
//import android.Manifest
//import android.app.PendingIntent
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.os.Bundle
//import android.support.v4.content.ContextCompat
//import com.google.android.gms.common.api.GoogleApiClient
//import com.google.android.gms.gcm.GcmNetworkManager
//import com.google.android.gms.gcm.GcmTaskService
//import com.google.android.gms.gcm.TaskParams
//import com.google.android.gms.location.LocationRequest
//import com.google.android.gms.location.LocationServices
//import com.pplus.utils.part.logs.LogUtil
//import com.pplus.luckybol.core.location.LocationUtil
//
///**
// * Created by imac on 2018. 2. 26..
// */
//class LocationTaskService : GcmTaskService() {
//
//    private val LOG_TAG = "LocationJobService"
//    private var mGoogleApiClient: GoogleApiClient? = null
//
//    override fun onRunTask(p0: TaskParams?): Int {
//
//        if (LocationUtil.isLocationEnabled(this)) {
//
//            mGoogleApiClient = GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
//
//                override fun onConnected(bundle: Bundle?) {
//                    if (ContextCompat.checkSelfPermission(baseContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//
//                        val locationRequest = LocationRequest()
//                        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//                        locationRequest.interval = 600000
//                        locationRequest.fastestInterval = 600000
//                        locationRequest.numUpdates = 1
//
//                        LogUtil.e(LOG_TAG, "locationRequest")
//
//                        val intent = Intent(this@LocationTaskService, LocationIntentService::class.java)
//                        val pendingIntent = PendingIntent.getService(this@LocationTaskService, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//                        LocationServices.getFusedLocationProviderClient(this@LocationTaskService).requestLocationUpdates(locationRequest, pendingIntent)
//                        mGoogleApiClient!!.disconnect()
//                    }
//                }
//
//                override fun onConnectionSuspended(i: Int) {
//                    mGoogleApiClient!!.disconnect()
//                }
//            }).build()
//            mGoogleApiClient!!.connect()
//        }
//
//        return GcmNetworkManager.RESULT_SUCCESS
//    }
//}