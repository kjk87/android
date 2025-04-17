package com.pplus.luckybol.core.util

import android.app.Activity
import android.app.ActivityManager
import android.content.ComponentName
import android.content.ContentUris
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.Html
import android.text.InputFilter
import android.text.Spanned
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.fragment.app.FragmentActivity
import com.buzzvil.buzzad.benefit.BuzzAdBenefit
import com.buzzvil.buzzad.benefit.core.models.UserProfile
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.gson.Gson
import com.pplus.luckybol.Const
import com.pplus.luckybol.LuckyBolApplication
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.LauncherScreenActivity
import com.pplus.luckybol.apps.common.builder.AlertBuilder
import com.pplus.luckybol.apps.common.builder.OnAlertResultListener
import com.pplus.luckybol.apps.common.builder.data.AlertData
import com.pplus.luckybol.apps.common.mgmt.*
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.event.ui.EventAlertActivity
import com.pplus.luckybol.apps.location.ui.LocationSetActivity
import com.pplus.luckybol.apps.signin.ui.SnsLoginActivity
import com.pplus.luckybol.core.AESCrypt
import com.pplus.luckybol.core.chrome.customtab.CustomTabUtil
import com.pplus.luckybol.core.chrome.shared.CustomTabsHelper
import com.pplus.luckybol.core.code.common.EnumData
import com.pplus.luckybol.core.code.common.SnsTypeCode
import com.pplus.luckybol.core.database.DBManager
import com.pplus.luckybol.core.debug.DebugConstant
import com.pplus.luckybol.core.location.GetAddressTask
import com.pplus.luckybol.core.location.LocationUtil
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.*
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.info.DeviceUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.pref.PreferenceUtil
import com.pplus.utils.part.utils.StringUtils
import com.pplus.utils.part.utils.time.DateFormatUtils
import retrofit2.Call
import java.io.File
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

/**
 * Created by imac on 2018. 2. 13..
 */
class PplusCommonUtil {
    companion object {

        private val TAG = PplusCommonUtil::class.java.simpleName
        private val LOG_TAG = PplusCommonUtil::class.java.simpleName

        fun getDeviceID(): String {

            return DeviceUtil.PRIVACY.getDeviceUUID(LuckyBolApplication.getContext())
        }

        fun formatDistance(distance: Double): String {

            if (distance > 1) {
                return String.format("%.1f", distance) + "km"
            } else {
                val meter = (distance * 1000).toInt()
                return meter.toString() + "m"
            }
        }

        fun hideKeyboard(activity: Activity?) {

            if (activity == null || activity.currentFocus == null) {
                return
            }

            val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
        }

        fun showKeyboard(activity: Activity?, editText: EditText) {

            if (activity == null || activity.currentFocus == null) {
                return
            }

            val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(editText, 0)
        }

        fun toggleKeyboard(activity: Activity, view: View) {

            view.requestFocus()
            val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS)
        }

        fun report(context: Context, report_type: EnumData.REPORT_TYPE, targetNo: Long?) {

            val contents = context.resources.getStringArray(R.array.report_radio)

            AlertBuilder.Builder().setContents(*contents).setTitle(context.getString(R.string.word_notice_alert)).setLeftText(context.getString(R.string.word_cancel)).setRightText(context.getString(R.string.word_confirm)).setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_RADIO).setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                    if (event_alert == AlertBuilder.EVENT_ALERT.LEFT) {
                        return
                    }
                    when (event_alert) {
                        AlertBuilder.EVENT_ALERT.RADIO -> {
                            val params = Report()
                            params.target = No(targetNo)
                            params.targetType = report_type.name
                            params.reason = contents[event_alert.getValue() - 1]

                            ApiBuilder.create().reporting(params).setCallback(object : PplusCallback<NewResultResponse<Report>> {

                                override fun onResponse(call: Call<NewResultResponse<Report>>, response: NewResultResponse<Report>) {

                                    ToastUtil.showAlert(context, R.string.msg_reported)
                                }

                                override fun onFailure(call: Call<NewResultResponse<Report>>, t: Throwable, response: NewResultResponse<Report>) {

                                    if (response.resultCode == 504) {
                                        when (report_type) {

                                            EnumData.REPORT_TYPE.page -> ToastUtil.showAlert(context, R.string.msg_already_reported_page)
                                            EnumData.REPORT_TYPE.article -> ToastUtil.showAlert(context, R.string.msg_already_reported_post)
                                            EnumData.REPORT_TYPE.comment -> ToastUtil.showAlert(context, R.string.msg_already_reported_reply)
                                            EnumData.REPORT_TYPE.goods -> ToastUtil.showAlert(context, R.string.msg_already_reported_goods)
                                        }

                                    } else {
                                        ToastUtil.showAlert(context, R.string.server_error_default)
                                    }
                                }
                            }).build().call()
                        }
                        else -> {}
                    }


                }
            }).builder().show(context)
        }

        fun getAlbumDir(): File? {

            var storageDir = File(LuckyBolApplication.getContext().filesDir, LuckyBolApplication.getContext().getString(R.string.word_album_name))

            if (!storageDir.mkdirs()) {
                if (!storageDir.exists()) {
                    LogUtil.e("getAlbumDir", "failed to create directory")
                    return null
                }
            }

            return storageDir
        }

        //sns intent
        fun getOpenSnsIntent(context: Context, snsTypeCode: SnsTypeCode, url: String, linkage: Boolean): Intent? {
            var url = url

            var intent: Intent? = null

            if (linkage) {
                return Intent(Intent.ACTION_VIEW, Uri.parse(url))
            }
            when (snsTypeCode) {

                SnsTypeCode.twitter -> {
                    try {
                        context.packageManager.getPackageInfo("com.twitter.android", 0)

                        val uri = Uri.parse("twitter://user?screen_name=$url")
                        intent = Intent(Intent.ACTION_VIEW, uri)
                    } catch (e: PackageManager.NameNotFoundException) {
                        intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/$url"))
                    }
                }

                SnsTypeCode.facebook -> {
                    try {
                        val versionCode = context.packageManager.getPackageInfo("com.facebook.katana", 0).versionCode
                        if (versionCode >= 3002850) {
                            //                        LogUtil.e("URL", "url : {}", "fb://facewebmodal/f?href=https://www.facebook.com/" + url);
                            var uri: Uri? = null
                            if (url.startsWith("page://")) {
                                url = url.replace("page://", "")
                                uri = Uri.parse("fb://page/$url")
                            } else if (url.startsWith("user://")) {
                                url = url.replace("user://", "")
                                uri = Uri.parse("fb://facewebmodal/f?href=https://www.facebook.com/$url")
                            } else {
                                uri = Uri.parse("fb://facewebmodal/f?href=https://www.facebook.com/$url")
                            }
                            intent = Intent(Intent.ACTION_VIEW, uri)
                        } else {
                            if (url.startsWith("page://")) {
                                url = url.replace("page://", "")
                            } else if (url.startsWith("user://")) {
                                url = url.replace("user://", "")
                            }
                            val uri = Uri.parse("fb://page/$url")
                            intent = Intent(Intent.ACTION_VIEW, uri)
                        }
                    } catch (e: PackageManager.NameNotFoundException) {
                        if (url.startsWith("page://")) {
                            url = url.replace("page://", "")
                        } else if (url.startsWith("user://")) {
                            url = url.replace("user://", "")
                        }
                        return Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/$url"))
                    }
                }

                SnsTypeCode.instagram -> {
                    try {
                        context.packageManager.getPackageInfo("com.instagram.android", 0)
                        LogUtil.e("instagram", "url : {}", "http://instagram.com/_u/$url")
                        val uri = Uri.parse("http://instagram.com/_u/$url")
                        intent = Intent(Intent.ACTION_VIEW, uri)
                        //                    intent.setData(uri);
                        intent.`package` = "com.instagram.android"
                    } catch (e: PackageManager.NameNotFoundException) {
                        intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com/$url"))
                    }
                }

                SnsTypeCode.kakao -> intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                SnsTypeCode.blog, SnsTypeCode.homepage -> {
                    var urlString = ""
                    if (!url.startsWith("http://") && !url.startsWith("https://")) {
                        urlString = "http://$url"
                    } else {
                        urlString = url
                    }

                    intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlString))
                }
                else -> {}
            }
            return intent
        }

        fun hasNetworkConnection(): Boolean {

            var hasConnectedWifi = false
            var hasConnectedMobile = false

            val cm = LuckyBolApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            if (activeNetwork != null) { // connected to the internet
                if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    hasConnectedWifi = true
                } else if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) {
                    // connected to the mobile provider's data plan
                    hasConnectedMobile = true
                }
            }

            return hasConnectedWifi || hasConnectedMobile
        }

        fun logOutAndRestart() {

            val contactDao = DBManager.getInstance(LuckyBolApplication.getContext()).session.contactDao
            contactDao.deleteAll()
            LoginInfoManager.getInstance().clear() // 초기화
            AppInfoManager.getInstance().setAutoSignIn(false)

            for (activity in LuckyBolApplication.getActivityList()) {
                if (!(activity is LauncherScreenActivity)) {
                    activity.finish()
                }
            }
            val intent = Intent(LuckyBolApplication.getContext(), LauncherScreenActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            LuckyBolApplication.getContext().startActivity(intent)
        }

        fun getVideoId(videoUrl: String): String? {

            val reg = "(?:youtube(?:-nocookie)?\\.com\\/(?:[^\\/\\n\\s]+\\/\\S+\\/|(?:v|e(?:mbed)?)\\/|\\S*?[?&]v=)|youtu\\.be\\/)([a-zA-Z0-9_-]{11})"
            val pattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE)
            val matcher = pattern.matcher(videoUrl)

            return if (matcher.find()) matcher.group(1) else null
        }

        /**
         * 이모티콘이 있을경우 "" 리턴, 그렇지 않을 경우 null 리턴
         */
        var specialCharacterFilter: InputFilter = InputFilter { source, start, end, dest, dstart, dend ->
            for (i in start until end) {

                val type = Character.getType(source[i])

                LogUtil.d(LOG_TAG, "type = {}", type)

                if (type == Character.SURROGATE.toInt() || type == Character.OTHER_SYMBOL.toInt()) {
                    ToastUtil.show(LuckyBolApplication.getContext(), LuckyBolApplication.getContext().getString(R.string.msg_disable_emoticon))
                    return@InputFilter ""
                }
            }
            null
        }

        fun isInstalledPackage(context: Context, packagename: String): Boolean {

            val pm = context.packageManager
            try {
                pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES)
                return true
            } catch (e: PackageManager.NameNotFoundException) {
                return false
            }

        }

        fun getWidth(context: Context): Int {

            try {
                val displayMetrics = DisplayMetrics()
                val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

                windowManager.defaultDisplay.getMetrics(displayMetrics)

                return displayMetrics.widthPixels
            } catch (e: Exception) {

                return 0
            }

        }

        fun getHeight(context: Context): Int {

            try {
                val displayMetrics = DisplayMetrics()
                val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

                windowManager.defaultDisplay.getMetrics(displayMetrics)

                return displayMetrics.heightPixels
            } catch (e: Exception) {

                return 0
            }

        }

        fun getDPI(size: Int, context: Context): Int {

            try {
                val ac = context as Activity
                val metrics = DisplayMetrics()
                ac.windowManager.defaultDisplay.getMetrics(metrics)
                return size * metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT
            } catch (e: Exception) {
                // TODO: handle exception
                return 0

            }

        }

        fun showDebugData(activity: Activity, mDebugState: TextView) {
            // Debug Util이 깔려 있는지?
            if (isInstalledPackage(activity, "kr.co.j2n.pplusutil")) {
                mDebugState.visibility = View.VISIBLE

                val data = PreferenceUtil.getDefaultPreference(LuckyBolApplication.getContext()).getString(DebugConstant.DEBUG_SHARED_NAME)
                if (data != null) {
                    val mDebugValue = Gson().fromJson(data, DebugUtilManager.DebugValue::class.java)
                    DebugUtilManager.getInstance().debugValue = mDebugValue
                }

                var debugTxt: String? = null

                if (DebugUtilManager.getInstance().debugValue.isDebugServer) {
                    debugTxt = "개발 "
                } else if (DebugUtilManager.getInstance().debugValue.isStagingServer) {
                    debugTxt = "스테이징 "
                } else {
                    debugTxt = "상용 "
                }

                if (DebugUtilManager.getInstance().debugValue.isDebugMode) {
                    debugTxt += "D " + AppInfoManager.getInstance().appVersionCode
                } else {
                    debugTxt += AppInfoManager.getInstance().appVersionCode
                }
                mDebugState.text = debugTxt
            } else {
                mDebugState.visibility = View.GONE
            }

            if (DebugUtilManager.getInstance().debugValue.isDebugMode) {
                val str = "width = " + getWidth(activity) + " height = " + getHeight(activity) + " dpi = " + getDPI(160, activity) + " Density =  " + activity.resources.displayMetrics.density
                Toast.makeText(activity, str, Toast.LENGTH_SHORT).show()
            }
        }

        interface SuccessLocationListener {

            fun onSuccess()
        }

        fun alertGpsOff(activity: BaseActivity) {
            val builder = AlertBuilder.Builder()
            builder.setTitle(activity.getString(R.string.word_notice_alert))
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
            builder.addContents(AlertData.MessageData(activity.getString(R.string.msg_on_locationService), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
            builder.setBackgroundClickable(false)
            builder.setLeftText(activity.getString(R.string.word_cancel)).setRightText(activity.getString(R.string.word_setting))
            builder.setOnAlertResultListener(object : OnAlertResultListener {


                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                    var intent: Intent? = null
                    when (event_alert) {
                        AlertBuilder.EVENT_ALERT.LEFT -> {
//                            intent = Intent(activity, LocationSetActivity::class.java)
//                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                            activity.startActivityForResult(intent, Const.REQ_LOCATION_CODE)
                        }
                        AlertBuilder.EVENT_ALERT.RIGHT -> {
                            intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                            activity.startActivityForResult(intent, Const.REQ_LOCATION_CODE)
                        }
                        else -> {}
                    }
                }
            }).builder().show(activity)
        }

        fun alertLocation(activity: BaseActivity, isRefresh: Boolean, listener: SuccessLocationListener?) {


            //위치값도 못가져오고, 마지막 위치값도 없을때
            if (LocationUtil.specifyLocationData == null && !LocationUtil.isLocationEnabled(activity) && LocationUtil.getLastLocation(activity) == null) {
                val builder = AlertBuilder.Builder()
                builder.setTitle(activity.getString(R.string.word_notice_alert))
                builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
                builder.addContents(AlertData.MessageData(activity.getString(R.string.msg_on_locationService), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
                builder.setBackgroundClickable(false)
                builder.setLeftText(activity.getString(R.string.word_search_location_map)).setRightText(activity.getString(R.string.word_setting))
                builder.setOnAlertResultListener(object : OnAlertResultListener {


                    override fun onCancel() {

                    }

                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                        var intent: Intent? = null
                        when (event_alert) {
                            AlertBuilder.EVENT_ALERT.LEFT -> {
                                intent = Intent(activity, LocationSetActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                activity.startActivityForResult(intent, Const.REQ_LOCATION_CODE)
                            }
                            AlertBuilder.EVENT_ALERT.RIGHT -> {
                                intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                                activity.startActivityForResult(intent, Const.REQ_LOCATION_CODE)
                            }
                            else -> {}
                        }
                    }
                }).builder().show(activity)
            } else {
                if (!isRefresh && LocationUtil.specifyLocationData != null) {
                    LocationUtil.setLastLocation(activity, LocationUtil.specifyLocationData!!.latitude!!, LocationUtil.specifyLocationData!!.longitude!!)
                    listener?.onSuccess()

                } else if (LocationUtil.isLocationEnabled(activity)) {

                    LocationUtil.startLocationUpdates(activity) { location ->
                        LogUtil.e(LOG_TAG, "onLocationChanged")
                        activity.hideProgress()

                        //                                        LocationUtil.stopLocationUpdates();
                        if (location != null) {
                            LogUtil.e("startLocationUpdates", "lat : {}, lon : {}", location.latitude, location.longitude)
                            val locationData = LocationData()
                            locationData.latitude = location.latitude
                            locationData.longitude = location.longitude
                            LocationUtil.specifyLocationData = locationData
                            LocationUtil.setLastLocation(activity, location.latitude, location.longitude)
                            listener?.onSuccess()
                        } else {
                            LocationUtil.specifyLocationData = LocationUtil.defaultLocationData
                            LogUtil.e(LOG_TAG, "location null")
                        }
//                        LocationUtil.disConnect()
                    }

//                    activity.showProgress(activity.getString(R.string.msg_getting_current_location))
//                    LocationUtil.init(activity, object : GoogleApiClient.ConnectionCallbacks {
//
//                        override fun onConnected(bundle: Bundle?) {
//
//
//                        }
//
//                        override fun onConnectionSuspended(i: Int) {
//
//                            activity.hideProgress()
//                            LogUtil.e(LOG_TAG, "onConnectionSuspended")
//                        }
//
//                    })
//                    LocationUtil.connect()
                } else {
                    activity.hideProgress()
                    LogUtil.e(LOG_TAG, "LocationDisabled")
                    if (LocationUtil.getLastLocation(activity) != null) {
                        val builder = AlertBuilder.Builder()
                        builder.setTitle(activity.getString(R.string.word_notice_alert))
                        builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
                        builder.addContents(AlertData.MessageData(activity.getString(R.string.msg_alert_set_last_location), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
                        builder.setBackgroundClickable(false)
                        builder.setLeftText(activity.getString(R.string.word_search_location)).setRightText(activity.getString(R.string.word_confirm))
                        builder.setOnAlertResultListener(object : OnAlertResultListener {

                            override fun onCancel() {

                            }

                            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                                when (event_alert) {
                                    AlertBuilder.EVENT_ALERT.RIGHT -> {
                                        LocationUtil.specifyLocationData = LocationUtil.getLastLocation(activity)
                                        listener?.onSuccess()
                                    }
                                    AlertBuilder.EVENT_ALERT.LEFT -> {
                                        val intent = Intent(activity, LocationSetActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                        activity.startActivityForResult(intent, Const.REQ_LOCATION_CODE)
                                    }
                                    else -> {}
                                }
                            }
                        }).builder().show(activity)
                    } else {
                        val builder = AlertBuilder.Builder()
                        builder.setTitle(activity.getString(R.string.word_notice_alert))
                        builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
                        builder.addContents(AlertData.MessageData(activity.getString(R.string.msg_alert_set_location), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
                        builder.setBackgroundClickable(false)
                        builder.setLeftText(activity.getString(R.string.word_cancel)).setRightText(activity.getString(R.string.word_confirm))
                        builder.setOnAlertResultListener(object : OnAlertResultListener {

                            override fun onCancel() {

                            }

                            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                                when (event_alert) {
                                    AlertBuilder.EVENT_ALERT.LEFT -> {
                                        LocationUtil.specifyLocationData = LocationUtil.defaultLocationData
                                        LocationUtil.setLastLocation(activity, LocationUtil.specifyLocationData!!.latitude!!, LocationUtil.specifyLocationData!!.longitude!!)
                                        listener?.onSuccess()
                                    }
                                    AlertBuilder.EVENT_ALERT.RIGHT -> {
                                        val intent = Intent(activity, LocationSetActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                        activity.startActivityForResult(intent, Const.REQ_LOCATION_CODE)
                                    }
                                    else -> {}
                                }
                            }
                        }).builder().show(activity)
                    }
                }
            }
        }

        interface OnAddressCallListener {
            fun onResult(address: String)
        }

        fun callAddress(locationData: LocationData?, listener: OnAddressCallListener?) {

            if (locationData != null) {
                val addressSync = GetAddressTask(LuckyBolApplication.getContext(), listener)
                addressSync.execute(locationData)


//                LogUtil.e(LOG_TAG, "lon : {}, lat : {}", locationData.longitude!!, locationData.latitude!!)

//                ApiController.getMapApi().requestAddressByGeo(locationData.longitude.toString(), locationData.latitude.toString()).enqueue(object : Callback<ResultDaumAddress> {
//
//                    override fun onResponse(call: Call<ResultDaumAddress>, response: Response<ResultDaumAddress>) {
//
//                        LogUtil.e(LOG_TAG, "requestAddressByGeo")
//
//                        var address: String? = null
//                        if (response.body() != null && response.body().documents != null && response.body().documents.size > 0) {
//
//                            if (response.body().documents[0].road_address != null && StringUtils.isNotEmpty(response.body().documents[0].road_address.address_name)) {
//                                address = response.body().documents[0].road_address.address_name
//                            } else {
//                                address = response.body().documents[0].address.address_name
//                            }
//                        }
//
//                        if (StringUtils.isNotEmpty(address)) {
//                            LocationUtil.specifyLocationData.address = address
//                            if (listener != null) {
//                                listener.onResult(address!!)
//                            }
//                        }
//                    }
//
//                    override fun onFailure(call: Call<ResultDaumAddress>, t: Throwable) {
//
//                    }
//                })
            }

        }

        fun getAvailMemoryInfo(): MemoryInfo {

            val memoryInfo = MemoryInfo()

            val mi = ActivityManager.MemoryInfo()
            val activityManager = LuckyBolApplication.getContext().getSystemService(ACTIVITY_SERVICE) as ActivityManager
            activityManager.getMemoryInfo(mi)
            val availableMegs = (mi.availMem / 1048576L).toDouble()

            LogUtil.d(TAG, "mi.availMem = {} mi.totalMem = {}", mi.availMem, mi.totalMem)

            val percentAvail = (mi.availMem.toDouble() / mi.totalMem.toDouble() * 100).toInt()

            LogUtil.d(TAG, "percentAvail {}", percentAvail)

            memoryInfo.availMem = mi.availMem
            memoryInfo.totalMem = mi.totalMem
            memoryInfo.availableMegs = availableMegs
            memoryInfo.availablePercent = percentAvail

            return memoryInfo
        }

//    public static void requestAdbrixApplication(Application application){
//        // 상용에서만 동작하도록 수정
//        if(DebugUtilManager.getInstance().getDebugValue() != null && !DebugUtilManager.getInstance().getDebugValue().isDebugServer() && !DebugUtilManager.getInstance().getDebugValue().isStagingServer()) {
//            IgawCommon.autoSessionTracking(application);
//
//        }
//    }
//
//    public static void requestAdbrixRetention(String retention){
//        // 상용에서만 동작하도록 수정
//        if(DebugUtilManager.getInstance().getDebugValue() != null && !DebugUtilManager.getInstance().getDebugValue().isDebugServer() && !DebugUtilManager.getInstance().getDebugValue().isStagingServer()) {
//            IgawAdbrix.retention(retention);
//        }
//    }
//
//    public static void requestAdbirxFirstTimeExperience(String firstTimeExperience){
//        // 상용에서만 동작하도록 수정
//        if(DebugUtilManager.getInstance().getDebugValue() != null && !DebugUtilManager.getInstance().getDebugValue().isDebugServer() && !DebugUtilManager.getInstance().getDebugValue().isStagingServer()) {
//            IgawAdbrix.firstTimeExperience(firstTimeExperience);
//        }
//    }

        fun getFile(name: String): File {

            val output = File(name)
            if (!output.exists()) {
                try {
                    output.createNewFile()
                } catch (e: IOException) {
                    LogUtil.e(LOG_TAG, e)
                }

            }
            return output
        }

        fun deleteFromMediaScanner(filePath: String) {
            val file = File(filePath)
            LogUtil.e("ExternalStorage", "file path : {}", filePath)
            if (file.exists()) {
                try {
                    LogUtil.e(LOG_TAG, "filename : "+file.name)
                    val fileUri = Uri.fromFile(File(filePath))
                    file.delete()
                    val c = LuckyBolApplication.getContext().contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, "_data='${fileUri.path}'", null, null)
                    if (c != null && c.moveToFirst()) {
                        val id = c.getInt(0)
                        val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id.toLong())
                        LogUtil.e("delete", "uri {}", uri.path)
                        LuckyBolApplication.getContext().contentResolver.delete(uri, null, null)
                        c.close()
                    }
                } catch (e: Exception) {
                    LogUtil.e(LOG_TAG, e)
                }
            }
        }


        fun updateIconBadge(context: Context, notiCnt: Int) {

            val badgeIntent = Intent("android.intent.action.BADGE_COUNT_UPDATE")
            badgeIntent.putExtra("badge_count", notiCnt)
            badgeIntent.putExtra("badge_count_package_name", context.packageName)
            badgeIntent.putExtra("badge_count_class_name", getLauncherClassName(context))
            context.sendBroadcast(badgeIntent)
        }

        fun getLauncherClassName(context: Context): String? {

            val pm = context.packageManager

            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)

            val resolveInfos = pm.queryIntentActivities(intent, 0)
            for (resolveInfo in resolveInfos) {
                val pkgName = resolveInfo.activityInfo.applicationInfo.packageName
                if (pkgName.equals(context.packageName, ignoreCase = true)) {
                    return resolveInfo.activityInfo.name
                }
            }
            return null
        }

        fun getCountryCode(): String {

            val tm = LuckyBolApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

            var code = tm.simCountryIso

            if (StringUtils.isNotEmpty(code)) {
                return code
            }

            code = tm.networkCountryIso

            if (StringUtils.isNotEmpty(code)) {
                return code
            }

            code = Locale.getDefault().country

            return code
        }

        fun getYoutubeVideoId(videoId: String): String? {

            val pattern = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"
            val p = Pattern.compile(pattern)
            val m = p.matcher(videoId)
            val youtu = videoId.indexOf("youtu")
            if (m.matches() && youtu != -1) {
                val ytu = videoId.indexOf("https://youtu.be/")
                if (ytu != -1) {
                    val split = videoId.split(".be/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    return split[1]
                }
                try {
                    val youtube = URL(videoId)
                    val split = youtube.query.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val query = split[1].indexOf("&")
                    if (query != -1) {
                        val nSplit = split[1].split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        return nSplit[0]
                    } else
                        return split[1]
                } catch (e: MalformedURLException) {
                    return null
                }

            }
            return null //throw something or return what you want
        }

        interface ReloadListener {

            fun reload()
        }

        fun getSession(listener: ReloadListener?) {

            if (LoginInfoManager.getInstance().isMember) {
                ApiBuilder.create().session.setCallback(object : PplusCallback<NewResultResponse<User>> {

                    override fun onResponse(call: Call<NewResultResponse<User>>, response: NewResultResponse<User>) {
                        if (LoginInfoManager.getInstance().isMember) {
                            val password = LoginInfoManager.getInstance().user.password
                            val page = LoginInfoManager.getInstance().user.page
                            LoginInfoManager.getInstance().user = response.data
                            LoginInfoManager.getInstance().user.password = password
                            if (page != null) {
                                LoginInfoManager.getInstance().user.page = page
                            }
                            LoginInfoManager.getInstance().save()

                            listener?.reload()
                        }

                    }

                    override fun onFailure(call: Call<NewResultResponse<User>>, t: Throwable, response: NewResultResponse<User>) {

                    }
                }).build().call()
            }
        }

        fun reloadSession(listener: ReloadListener?) {
            if (LoginInfoManager.getInstance().isMember) {
                ApiBuilder.create().reloadSession().setCallback(object : PplusCallback<NewResultResponse<User>> {

                    override fun onResponse(call: Call<NewResultResponse<User>>, response: NewResultResponse<User>) {
                        if (LoginInfoManager.getInstance().isMember) {
                            val password = LoginInfoManager.getInstance().user.password
                            val page = LoginInfoManager.getInstance().user.page

                            LoginInfoManager.getInstance().user = response.data
                            LoginInfoManager.getInstance().user.password = password
                            if (page != null) {
                                LoginInfoManager.getInstance().user.page = page
                            }
                            LoginInfoManager.getInstance().save()

                            listener?.reload()
                        }

                    }

                    override fun onFailure(call: Call<NewResultResponse<User>>, t: Throwable, response: NewResultResponse<User>) {

                    }
                }).build().call()
            }

        }

        fun encryption(strNormalText: String): String {

            val seedValue = Const.SECRET_KEY
            var normalTextEnc = ""
            try {
                normalTextEnc = AESCrypt.encrypt(seedValue, strNormalText)
            } catch (e: Exception) {
                LogUtil.e(LOG_TAG, e)
            }

            return normalTextEnc
        }

        fun decryption(strEncryptedText: String): String {

            val seedValue = Const.SECRET_KEY
            var strDecryptedText = ""
            try {
                strDecryptedText = AESCrypt.decrypt(seedValue, strEncryptedText)
            } catch (e: Exception) {
                LogUtil.e(LOG_TAG, e)
            }

            return strDecryptedText
        }

        interface AlarmAgreeListener {

            fun result(pushActivate: Boolean, pushMask: String)
        }

        fun alertAlarmAgree(listener: AlarmAgreeListener?) {

            val builder = AlertBuilder.Builder()
            builder.setTitle(LuckyBolApplication.getContext().getString(R.string.word_notice_alert))
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
            builder.addContents(AlertData.MessageData(LuckyBolApplication.getContext().getString(R.string.msg_alarm_popup), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
            builder.setAutoCancel(false)
            builder.setBackgroundClickable(false)
            builder.setLeftText(LuckyBolApplication.getContext().getString(R.string.word_cancel)).setRightText(LuckyBolApplication.getContext().getString(R.string.word_approve))
            builder.setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                    var pushMask = ""
                    var pushActivate = true
                    when (event_alert) {
                        AlertBuilder.EVENT_ALERT.LEFT -> {
                            pushMask = "0000000000000000"
                            pushActivate = false
                        }
                        AlertBuilder.EVENT_ALERT.RIGHT -> {
                            pushMask = "1111111111111111"
                            pushActivate = true
                        }
                        else -> {}
                    }

                    listener?.result(pushActivate, pushMask)
                }
            }).builder().show(LuckyBolApplication.getContext())
        }

        fun fromHtml(html: String): Spanned {

            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
            } else {
                Html.fromHtml(html)
            }
        }


        fun openPageChromeWebView(context: Context, url: String, page: Page) {
            var urlString = ""
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                urlString = "http://$url"
            } else {
                urlString = url
            }

            val memoryInfo = PplusCommonUtil.getAvailMemoryInfo()

            if (!isChromeCustomTabsSupported(context)) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(urlString)
                try {
                    context.startActivity(intent)
                }catch (e:Exception){

                }

                return
            }

            val session = CustomTabUtil.getSession()
            val builder = CustomTabsIntent.Builder(session)
            builder.setToolbarColor(Color.parseColor("#ffffff")).setShowTitle(true)

            if(session == null){
                LogUtil.e(LOG_TAG, "session null")
            }

            if (session != null) CustomTabUtil.prepareBottombar(context, builder, page)
            builder.setStartAnimations(context, R.anim.slide_in_right, R.anim.slide_out_left)
            builder.setExitAnimations(context, R.anim.slide_in_left, R.anim.slide_out_right)
            builder.setCloseButtonIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_navbar_back))

            val customTabsIntent = builder.build()
            if (session != null) {
                CustomTabsHelper.addKeepAliveExtra(context, customTabsIntent.intent)
            } else {
                if (!TextUtils.isEmpty(CustomTabUtil.mPackageNameToBind)) {
                    customTabsIntent.intent.setPackage(CustomTabUtil.mPackageNameToBind)
                }
            }
            customTabsIntent.intent.data = Uri.parse(urlString)
            (context as BaseActivity).startActivityForResult(customTabsIntent.intent, Const.REQ_CHROME)
//            customTabsIntent.launchUrl(context, Uri.parse(urlString))

        }


        fun showEventAlert(context: Context, code: Int, event: Event?, launcher: ActivityResultLauncher<Intent>?) {
            val intent = Intent(context, EventAlertActivity::class.java)
            intent.putExtra(Const.KEY, code)
            if (event != null) {
                intent.putExtra(Const.DATA, event)
            }
            if(launcher != null){
                launcher.launch(intent)
            }else{
                context.startActivity(intent)
            }

        }

        fun showEventAlert(context: Context, code: Int, event: Event?, joinDate: String?, joinTerm: Int?, remainSecond: Int?, launcher: ActivityResultLauncher<Intent>?) {
            val intent = Intent(context, EventAlertActivity::class.java)
            intent.putExtra(Const.KEY, code)
            if (event != null) {
                intent.putExtra(Const.DATA, event)
            }
            if (StringUtils.isNotEmpty(joinDate)) {
                intent.putExtra(Const.JOIN_DATE, joinDate)
                intent.putExtra(Const.JOIN_TERM, joinTerm)
                intent.putExtra(Const.REMAIN_SECOND, remainSecond)
            }
            if(launcher != null){
                launcher.launch(intent)
            }else{
                context.startActivity(intent)
            }
        }

        private var mAdmobInterstitialAd: InterstitialAd? = null

        fun initAdMob(context:Context) {

            MobileAds.initialize(context)

            val adRequest = AdRequest.Builder().build()

            InterstitialAd.load(context,Const.ADMOB_INTERSTITIAL_ID, adRequest, object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    LogUtil.e(LOG_TAG, "onAdFailedToLoad : {}", adError?.message)
                    (context as BaseActivity).hideProgress()
                    mAdmobInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    LogUtil.e(LOG_TAG, "onAdLoaded")
                    (context as BaseActivity).hideProgress()
                    mAdmobInterstitialAd = interstitialAd
                    showAdmob(context)
                }
            })
        }

        fun showAdmob(context: Context) {
            mAdmobInterstitialAd?.show((context as BaseActivity))
        }

//        fun scheduleJob(context: Context) {
//            if (LoginInfoManager.getInstance().user.loginId.equals("ceo")) {
//                if (OsUtil.isLollipop()) {
//                    val tm = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
//                    tm.cancelAll()
//                    val mServiceComponent = ComponentName(context, LocationJobService::class.java)
//                    val builder = JobInfo.Builder(0, mServiceComponent)
//
//                    builder.setPeriodic(900000)
//                    builder.setRequiresCharging(false)
//                    builder.setPersisted(true)
//
//                    // Schedule job
//                    tm.schedule(builder.build())
//                } else {
//                    val myTask = OneoffTask.Builder()
//                            .setService(LocationTaskService::class.java)
//                            .setExecutionWindow(
//                                    600000, 900000)
//                            .setTag("")
//                            .build();
//                    GcmNetworkManager.getInstance(context).schedule(myTask);
//
//                }
//            }
//
//
//        }

        private fun isChromeCustomTabsSupported(context: Context): Boolean {
            val serviceIntent = Intent("android.support.customtabs.action.CustomTabsService")
            serviceIntent.`package` = "com.android.chrome"

            val service = object : CustomTabsServiceConnection() {
                override fun onCustomTabsServiceConnected(name: ComponentName, client: CustomTabsClient) {}


                override fun onServiceDisconnected(name: ComponentName?) {}
            }

            val customTabsSupported =
                    context.bindService(serviceIntent, service, Context.BIND_AUTO_CREATE or Context.BIND_WAIVE_PRIORITY)
            context.unbindService(service)

            return customTabsSupported
        }

        fun openChromeWebView(context: Context, url: String) {
            var urlString = ""
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                urlString = "http://$url"
            } else {
                urlString = url
            }

            if (!isChromeCustomTabsSupported(context)) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(urlString)
                context.startActivity(intent)
            } else {
                val builder = CustomTabsIntent.Builder()

//                val icon = BitmapFactory.decodeResource(context.resources, R.drawable.btn_main_plus)
//                val pendingIntent = createPendingIntent(context)
//                builder.setActionButton(icon, "", pendingIntent)

                val customTabsIntent = builder.build()
                LogUtil.e(LOG_TAG, urlString)
                customTabsIntent.launchUrl(context, Uri.parse(urlString))

            }
        }

        fun calculateBol(price: Long): Long {
            val bolRatio = CountryConfigManager.getInstance().config.properties.bolRatio
            val taxRatio = CountryConfigManager.getInstance().config.properties.taxRatio

            val bolPrice = (price / bolRatio!!.toDouble()) * ((taxRatio!!.plus(100)).toDouble() / 100.0)
            return Math.ceil(bolPrice).toLong()
        }

        fun runOtherApp(packageName: String) {

            var intent = LuckyBolApplication.getContext().packageManager.getLaunchIntentForPackage(packageName)
            if (intent != null) {
                // We found the activity now start the activity
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                LuckyBolApplication.getContext().startActivity(intent)
            } else {
                // Bring user to the market or let them choose an app?
                intent = Intent(Intent.ACTION_VIEW)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.data = Uri.parse("market://details?id=$packageName")
                LuckyBolApplication.getContext().startActivity(intent)
            }
        }

        fun loginCheck(activity: FragmentActivity, launcher: ActivityResultLauncher<Intent>?):Boolean{
            if (!LoginInfoManager.getInstance().isMember) {

                val builder = AlertBuilder.Builder()
                builder.setTitle(activity.getString(R.string.word_notice_alert))
                builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
                builder.addContents(AlertData.MessageData(activity.getString(R.string.msg_alert_login_join), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                builder.setLeftText(activity.getString(R.string.word_cancel)).setRightText(activity.getString(R.string.word_login_join))
                builder.setBackgroundClickable(false).setAutoCancel(false)
                builder.setOnAlertResultListener(object : OnAlertResultListener {

                    override fun onCancel() {}

                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                        when (event_alert) {
                            AlertBuilder.EVENT_ALERT.RIGHT -> {
                                val intent = Intent(activity, SnsLoginActivity::class.java)
                                if(launcher == null){
                                    activity.startActivity(intent)
                                }else{
                                    launcher.launch(intent)
                                }
                            }
                            else -> {}
                        }
                    }
                }).builder().show(activity)
                return false
            }else{
                return true
            }
        }

        fun getDateFormat(date:String):String{
            val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(date)
            val c = Calendar.getInstance()
            val todayYear = c.get(Calendar.YEAR)
            val todayMonth = c.get(Calendar.MONTH)
            val todayDay = c.get(Calendar.DAY_OF_MONTH)
            c.time = d

            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            if (todayYear == year && todayMonth == month && todayDay == day) {
                val output = SimpleDateFormat("a HH:mm", Locale.getDefault())
                return output.format(d)
            } else {
                val output = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
                return output.format(d)
            }
        }

        fun share(context: Context) {
            //        val recommendPoint = FormatUtil.getMoneyType(CountryConfigManager.getInstance().config.properties.recommendBol.toString())
            val recommendKey = LoginInfoManager.getInstance().user.recommendKey
            val text = "${context.getString(R.string.format_invite_description, recommendKey)}\n${context.getString(R.string.format_msg_invite_url, LoginInfoManager.getInstance().user.recommendKey)}"
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, text)
            val chooserIntent = Intent.createChooser(intent, context.getString(R.string.word_share))
            context.startActivity(chooserIntent)
        }

        fun setBuzvilProfileData(){
            val builder = UserProfile.Builder(BuzzAdBenefit.getUserProfile())
            builder.userId(LoginInfoManager.getInstance().user.no.toString())
            if (LoginInfoManager.getInstance().user.gender.equals(EnumData.GenderType.male.name)) {
                builder.gender(UserProfile.Gender.MALE)
            } else {
                builder.gender(UserProfile.Gender.MALE)
            }

            if (StringUtils.isNotEmpty(LoginInfoManager.getInstance().user.birthday)) {
                val birth = LoginInfoManager.getInstance().user.birthday!!.substring(0, 4)
                builder.birthYear(birth.toInt())
            }

            val userProfile = builder.build()
            BuzzAdBenefit.setUserProfile(userProfile)
        }
    }


}


