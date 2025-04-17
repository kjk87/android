package com.pplus.luckybol.core.location

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.builder.AlertBuilder
import com.pplus.luckybol.apps.common.builder.AlertBuilder.EVENT_ALERT
import com.pplus.luckybol.apps.common.builder.OnAlertResultListener
import com.pplus.luckybol.apps.common.builder.data.AlertData.MessageData
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.network.model.dto.LocationData
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.pref.PreferenceUtil
import com.pplus.utils.part.utils.StringUtils

/**
 * Created by 김종경 on 2016-08-26.
 */
object LocationUtil {
    // 값이 없는 경우 Default값 생성
//        if (mSpecifyLocationData == null) {
//            mSpecifyLocationData = getDefaultLocationData();
//        }
    var specifyLocationData: LocationData? = null

    @JvmField
    var DEFAULT_LATITUDE = 37.4876057

    @JvmField
    var DEFAULT_LONGITUDE = 127.0107591

    @JvmStatic
    fun getCurrentLocation(activity: Activity?): Location? { //위치값 가져오기
        // 퍼미션 확인 후 요청
        return if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.getFusedLocationProviderClient(activity).lastLocation.result
        } else {
            null
        }
    }

    fun getLastLocation(activity: BaseActivity, listener: LocationListener) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return
        }
        LocationServices.getFusedLocationProviderClient(activity).lastLocation.addOnSuccessListener {
            listener.onLocationChanged(it)
        }.addOnFailureListener {
            startLocationUpdates(activity, listener)
        }
    }

    var mLocationCallback: LocationCallback? = null

    interface LocationResultListener {

        fun result(location:Location?)
    }
    fun startLocationOnly(activity: BaseActivity, listener: LocationResultListener?) { //위치값 요청하기

        // TODO Null point exception이 발생합니다.
        try {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                // 위치 정보 요청후 30초간 응답이 없다면 사용자에게 네트워크 정보를 요청 하도록 합니다.
                val handler = Handler(Looper.myLooper()!!)
                val timeOutRunnable = Runnable {
//                    val builder = AlertBuilder.Builder().setAutoCancel(false).setBackgroundClickable(false)
//                    builder.addContents(MessageData(activity.getString(R.string.msg_location_error1), AlertBuilder.MESSAGE_TYPE.TEXT, 1))
//                    builder.addContents(MessageData(activity.getString(R.string.msg_location_error2), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
//                    builder.addContents(MessageData(activity.getString(R.string.msg_location_error3), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
//                    builder.setLeftText(activity.getString(R.string.word_cancel)).setRightText(activity.getString(R.string.msg_setting)).setOnAlertResultListener(object : OnAlertResultListener {
//                        override fun onCancel() {
//                            listener?.result(null)
//                        }
//
//                        override fun onResult(event_alert: EVENT_ALERT) {
//
//                            if(mLocationCallback != null){
//                                LocationServices.getFusedLocationProviderClient(activity).removeLocationUpdates(mLocationCallback)
//                            }
//
//                            when (event_alert) {
//                                EVENT_ALERT.RIGHT -> {
//                                    val locationIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//                                    activity.locationLauncher.launch(locationIntent)
//                                }
//                                EVENT_ALERT.LEFT -> {
//                                    listener?.result(null)
//                                }
//                            }
//                        }
//                    }).builder().show(activity)
                    listener?.result(null)
                }

                val locationRequest = LocationRequest.create()
                locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                locationRequest.numUpdates = 1
                locationRequest.interval = 10000
                locationRequest.fastestInterval = 5000
                handler.postDelayed(timeOutRunnable, 10000)
                val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
                val settingsClient = LocationServices.getSettingsClient(activity)
                val task = settingsClient.checkLocationSettings(builder.build())

                mLocationCallback = object : LocationCallback() {
                    private var isLocationUpdates = false
                    override fun onLocationResult(locationResult: LocationResult) {
                        isLocationUpdates = true
                        if(mLocationCallback != null){
                            LocationServices.getFusedLocationProviderClient(activity).removeLocationUpdates(mLocationCallback!!)
                        }
                        val lastLocation = locationResult.lastLocation
                        LogUtil.e("onLocationResult", "onLocationResult")
                        val h = Handler()
                        h.post {
                            handler.removeCallbacks(timeOutRunnable)
                            listener?.result(lastLocation)
                        }
                    }

                    override fun onLocationAvailability(locationAvailability: LocationAvailability) {
                        if (locationAvailability.isLocationAvailable) {
                            LogUtil.e("onLocationAvailability", "locationAvailability is true")
                            try {
                            } catch (e: Exception) {
                                LogUtil.e("onLocationAvailability", e.toString())
                            }
                        }
                    }
                }

                task.addOnSuccessListener(activity) { locationSettingsResponse ->
                    LogUtil.e("OnSuccessListener", "onSuccess {}", locationSettingsResponse.locationSettingsStates?.isLocationPresent)
                    LocationServices.getFusedLocationProviderClient(activity).requestLocationUpdates(locationRequest, mLocationCallback!!, Looper.myLooper()!!)
                }.addOnFailureListener(activity) { e ->
                    LogUtil.e("OnFailureListener", "onFailure {}", e.toString())
                    val statusCode = (e as ApiException).statusCode
                    when (statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                            handler.removeCallbacks(timeOutRunnable)
                            try {
                                //위치 설정이 만족스럽지 않을 때 감지.
                                // 그러나 사용자에게 대화상자를  보여줌으로써 고칠 수있습니다.
                                val rae = e as ResolvableApiException
                                //startResolutionForResult ()를 호출하여 대화 상자를 표시합니다.
                                rae.startResolutionForResult(
                                    activity,
                                    Const.REQ_LOCATION_CODE)
                            } catch (sie: SendIntentException) {
                                LogUtil.e("debug", "PendingIntent unable to execute request.")
                            }
                        }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> LogUtil.e("SETTINGS_CHANGE_UNAVAILABLE", "SETTINGS_CHANGE_UNAVAILABLE")
                    }
                }
            }
        } catch (e: NullPointerException) {
            LogUtil.e("LocationUtil", e.toString())
        }
    }

    fun startLocationUpdates(activity: BaseActivity, listener: LocationListener?) { //위치값 요청하기

        // TODO Null point exception이 발생합니다.
        try {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                // 위치 정보 요청후 30초간 응답이 없다면 사용자에게 네트워크 정보를 요청 하도록 합니다.
                val handler = Handler(Looper.myLooper()!!)
                val timeOutRunnable = Runnable {
                    val builder = AlertBuilder.Builder().setAutoCancel(false).setBackgroundClickable(false)
                    builder.addContents(MessageData(activity.getString(R.string.msg_location_error1), AlertBuilder.MESSAGE_TYPE.TEXT, 1))
                    builder.addContents(MessageData(activity.getString(R.string.msg_location_error2), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                    builder.addContents(MessageData(activity.getString(R.string.msg_location_error3), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                    builder.setLeftText(activity.getString(R.string.word_cancel)).setRightText(activity.getString(R.string.msg_setting)).setOnAlertResultListener(object : OnAlertResultListener {
                        override fun onCancel() {
                            val locationData = getLastLocation(activity)
                            var location = Location("location")
                            if (locationData != null) {
                                location.latitude = locationData.latitude
                                location.longitude = locationData.longitude
                            } else {
                                location = defaultData
                            }
                            listener!!.onLocationChanged(location)
                        }

                        override fun onResult(event_alert: EVENT_ALERT) {

                            if(mLocationCallback != null){
                                LocationServices.getFusedLocationProviderClient(activity).removeLocationUpdates(mLocationCallback!!)
                            }

                            when (event_alert) {
                                EVENT_ALERT.RIGHT -> {
                                    val locationIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                                    activity.locationLauncher.launch(locationIntent)
                                }
                                EVENT_ALERT.LEFT -> {
                                    val locationData = getLastLocation(activity)
                                    var location = Location("location")
                                    if (locationData != null) {
                                        location.latitude = locationData.latitude
                                        location.longitude = locationData.longitude
                                    } else {
                                        location = defaultData
                                    }
                                    listener!!.onLocationChanged(location)
                                }
                                else -> {}
                            }
                        }
                    }).builder().show(activity)
                }
                val locationRequest = LocationRequest.create()
                locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                locationRequest.numUpdates = 1
                locationRequest.interval = 10000
                locationRequest.fastestInterval = 5000
                handler.postDelayed(timeOutRunnable, 10000)
                val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
                val settingsClient = LocationServices.getSettingsClient(activity)
                val task = settingsClient.checkLocationSettings(builder.build())

                mLocationCallback = object : LocationCallback() {
                    private var isLocationUpdates = false
                    override fun onLocationResult(locationResult: LocationResult) {
                        isLocationUpdates = true
                        if(mLocationCallback != null){
                            LocationServices.getFusedLocationProviderClient(activity).removeLocationUpdates(mLocationCallback!!)
                        }
                        val lastLocation = locationResult.lastLocation
                        LogUtil.e("onLocationResult", "onLocationResult")
                        val h = Handler()
                        h.post {
                            handler.removeCallbacks(timeOutRunnable)
                            listener?.onLocationChanged(lastLocation)
                        }
                    }

                    override fun onLocationAvailability(locationAvailability: LocationAvailability) {
                        if (locationAvailability.isLocationAvailable) {
                            LogUtil.e("onLocationAvailability", "locationAvailability is true")
                            try {
                            } catch (e: Exception) {
                                LogUtil.e("onLocationAvailability", e.toString())
                            }
                        }
                    }
                }

                task.addOnSuccessListener(activity) { locationSettingsResponse ->
                    LogUtil.e("OnSuccessListener", "onSuccess {}", locationSettingsResponse.locationSettingsStates?.isLocationPresent)
                    LocationServices.getFusedLocationProviderClient(activity).requestLocationUpdates(locationRequest, mLocationCallback!!, Looper.myLooper()!!)
                }.addOnFailureListener(activity) { e ->
                    LogUtil.e("OnFailureListener", "onFailure {}", e.toString())
                    val statusCode = (e as ApiException).statusCode
                    when (statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                            handler.removeCallbacks(timeOutRunnable)
                            try {
                                //위치 설정이 만족스럽지 않을 때 감지.
                                // 그러나 사용자에게 대화상자를  보여줌으로써 고칠 수있습니다.
                                val rae = e as ResolvableApiException
                                //startResolutionForResult ()를 호출하여 대화 상자를 표시합니다.
                                rae.startResolutionForResult(
                                        activity,
                                        Const.REQ_LOCATION_CODE)
                            } catch (sie: SendIntentException) {
                                LogUtil.e("debug", "PendingIntent unable to execute request.")
                            }
                        }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> LogUtil.e("SETTINGS_CHANGE_UNAVAILABLE", "SETTINGS_CHANGE_UNAVAILABLE")
                    }
                }
            }
        } catch (e: NullPointerException) {
            LogUtil.e("LocationUtil", e.toString())
        }
    }

    @JvmStatic
    fun isLocationEnabled(context: Context): Boolean {
        return isGpsLocationEnabled(context) || isNetworkLocationEnabled(context)
    }

    fun isGpsLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun isNetworkLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) || locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    @JvmStatic
    fun setLastLocation(context: Context?, latitude: Double, longitude: Double) {
        PreferenceUtil.getDefaultPreference(context).put(Const.LATITUDE, "" + latitude)
        PreferenceUtil.getDefaultPreference(context).put(Const.LONGITUDE, "" + longitude)
    }

    @JvmStatic
    fun getLastLocation(context: Context?): LocationData? {
        val data = LocationData()
        if (StringUtils.isEmpty(PreferenceUtil.getDefaultPreference(context).getString(Const.LATITUDE))) {
            return null
        }
        if (StringUtils.isEmpty(PreferenceUtil.getDefaultPreference(context).getString(Const.LONGITUDE))) {
            return null
        }
        data.latitude = PreferenceUtil.getDefaultPreference(context).getString(Const.LATITUDE).toDouble()
        data.longitude = PreferenceUtil.getDefaultPreference(context).getString(Const.LONGITUDE).toDouble()
        return data
    }

    val defaultData: Location
        get() {
            val location = Location("location")
            location.latitude = DEFAULT_LATITUDE
            location.longitude = DEFAULT_LONGITUDE
            return location
        }
    val defaultLocationData: LocationData
        get() {
            val locationData = LocationData()
            locationData.latitude = DEFAULT_LATITUDE
            locationData.longitude = DEFAULT_LONGITUDE
            return locationData
        }
}