package com.pplus.prnumberbiz.core.util

import android.app.Activity
import android.app.ActivityManager
import android.content.ComponentName
import android.content.ContentUris
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import android.telephony.TelephonyManager
import android.text.Html
import android.text.InputFilter
import android.text.Spanned
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.pple.pplus.utils.part.info.DeviceUtil
import com.pple.pplus.utils.part.info.OsUtil
import com.pple.pplus.utils.part.logs.LogUtil
import com.pple.pplus.utils.part.pref.PreferenceUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pple.pplus.utils.part.utils.time.DateFormatUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.PRNumberBizApplication
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.LauncherScreenActivity
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData
import com.pplus.prnumberbiz.apps.common.mgmt.AppInfoManager
import com.pplus.prnumberbiz.apps.common.mgmt.DebugUtilManager
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.mgmt.MemoryInfo
import com.pplus.prnumberbiz.core.AESCrypt
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.code.common.SnsTypeCode
import com.pplus.prnumberbiz.core.database.DBManager
import com.pplus.prnumberbiz.core.debug.DebugConstant
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.No
import com.pplus.prnumberbiz.core.network.model.dto.Report
import com.pplus.prnumberbiz.core.network.model.dto.User
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import network.common.PplusCallback
import retrofit2.Call
import java.io.File
import java.io.IOException
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

            return DeviceUtil.PRIVACY.getDeviceUUID(PRNumberBizApplication.getContext())
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
                                        }

                                    } else {
                                        ToastUtil.showAlert(context, R.string.server_error_default)
                                    }
                                }
                            }).build().call()
                        }
                    }


                }
            }).builder().show(context)
        }

        fun getAlbumDir(): File? {

            var storageDir: File? = null

            if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {

                storageDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), PRNumberBizApplication.getContext().getString(R.string.word_album_name))

                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()) {
                        LogUtil.e("getAlbumDir", "failed to create directory")
                        return null
                    }
                }

            } else {
                LogUtil.e("getAlbumDir", "External storage is not mounted READ/WRITE.")
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

                SnsTypeCode.twitter -> try {
                    context.packageManager.getPackageInfo("com.twitter.android", 0)

                    val uri = Uri.parse("twitter://user?screen_name=" + url)
                    intent = Intent(Intent.ACTION_VIEW, uri)
                } catch (e: PackageManager.NameNotFoundException) {
                    intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + url))
                }

                SnsTypeCode.facebook -> try {
                    val versionCode = context.packageManager.getPackageInfo("com.facebook.katana", 0).versionCode
                    if (versionCode >= 3002850) {
                        //                        LogUtil.e("URL", "url : {}", "fb://facewebmodal/f?href=https://www.facebook.com/" + url);
                        var uri: Uri? = null
                        if (url.startsWith("page://")) {
                            url = url.replace("page://", "")
                            uri = Uri.parse("fb://page/" + url)
                        } else if (url.startsWith("user://")) {
                            url = url.replace("user://", "")
                            uri = Uri.parse("fb://facewebmodal/f?href=https://www.facebook.com/" + url)
                        } else {
                            uri = Uri.parse("fb://facewebmodal/f?href=https://www.facebook.com/" + url)
                        }
                        intent = Intent(Intent.ACTION_VIEW, uri)
                    } else {
                        if (url.startsWith("page://")) {
                            url = url.replace("page://", "")
                        } else if (url.startsWith("user://")) {
                            url = url.replace("user://", "")
                        }
                        val uri = Uri.parse("fb://page/" + url)
                        intent = Intent(Intent.ACTION_VIEW, uri)
                    }
                } catch (e: PackageManager.NameNotFoundException) {
                    if (url.startsWith("page://")) {
                        url = url.replace("page://", "")
                    } else if (url.startsWith("user://")) {
                        url = url.replace("user://", "")
                    }
                    return Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + url))
                }

                SnsTypeCode.instagram -> try {
                    context.packageManager.getPackageInfo("com.instagram.android", 0)
                    LogUtil.e("instagram", "url : {}", "http://instagram.com/_u/" + url)
                    val uri = Uri.parse("http://instagram.com/_u/" + url)
                    intent = Intent(Intent.ACTION_VIEW, uri)
                    //                    intent.setData(uri);
                    intent.`package` = "com.instagram.android"
                } catch (e: PackageManager.NameNotFoundException) {
                    intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com/" + url))
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
            }
            return intent
        }

        fun hasNetworkConnection(): Boolean {

            var hasConnectedWifi = false
            var hasConnectedMobile = false

            val cm = PRNumberBizApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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

            val contactDao = DBManager.getInstance(PRNumberBizApplication.getContext()).session.contactDao
            contactDao.deleteAll()
            LoginInfoManager.getInstance().clear() // 초기화
            AppInfoManager.getInstance().setAutoSignIn(false)

            for (activity in PRNumberBizApplication.getActivityList()) {
                activity.finish()
            }
            val intent = Intent(PRNumberBizApplication.getContext(), LauncherScreenActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            PRNumberBizApplication.getContext().startActivity(intent)

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

                LogUtil.d(LOG_TAG, "type = " + type)

                if (type == Character.SURROGATE.toInt() || type == Character.OTHER_SYMBOL.toInt()) {
                    ToastUtil.show(PRNumberBizApplication.getContext(), PRNumberBizApplication.getContext().getString(R.string.msg_disable_emoticon))
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

                val data = PreferenceUtil.getDefaultPreference(PRNumberBizApplication.getContext()).getString(DebugConstant.DEBUG_SHARED_NAME)
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

//        fun callAddress(activity: Activity, locationData: LocationData?) {
//
//            if (locationData != null) {
//
//                ApiController.getMapApi().requestAddressByGeo(locationData.longitude!!, locationData.latitude!!).enqueue(object : Callback<ResultDaumAddress> {
//
//                    override fun onResponse(call: Call<ResultDaumAddress>, response: Response<ResultDaumAddress>) {
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
//                            LocationUtil.getSpecifyLocationData().address = address
//                        }
//                    }
//
//                    override fun onFailure(call: Call<ResultDaumAddress>, t: Throwable) {
//
//                    }
//                })
//            }
//
//        }

        fun getAvailMemoryInfo(): MemoryInfo {

            val memoryInfo = MemoryInfo()

            val mi = ActivityManager.MemoryInfo()
            val activityManager = PRNumberBizApplication.getContext().getSystemService(ACTIVITY_SERVICE) as ActivityManager
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
                    e.printStackTrace()
                }

            }
            return output
        }

        fun deleteFromMediaScanner(filePath: String) {
            val file = File(filePath)
            LogUtil.e("ExternalStorage", "file path : {}", filePath)
            if (file.exists()) {
                try {
                    val fileUri = Uri.fromFile(File(filePath))
                    file.delete()
                    val c = PRNumberBizApplication.getContext().contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, "_data='${fileUri.path}'", null, null)
                    if (c != null && c.moveToFirst()) {
                        val id = c.getInt(0)
                        val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id.toLong())
                        LogUtil.e("delete", "uri {}", uri.path)
                        val delete = PRNumberBizApplication.getContext().contentResolver.delete(uri, null, null)
                        LogUtil.e("delete", "value : {}", delete)
                        c.close()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
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

            val tm = PRNumberBizApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

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
                } catch (e: Exception) {
                    // TODO Auto-generated catch block
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
                e.printStackTrace()
            }

            return normalTextEnc
        }

        fun decryption(strEncryptedText: String): String {

            val seedValue = Const.SECRET_KEY
            var strDecryptedText = ""
            try {
                strDecryptedText = AESCrypt.decrypt(seedValue, strEncryptedText)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return strDecryptedText
        }

        interface AlarmAgreeListener {

            fun result(pushActivate: Boolean, pushMask: String)
        }

        fun alertAlarmAgree(listener: AlarmAgreeListener?) {

            val builder = AlertBuilder.Builder()
            builder.setTitle(PRNumberBizApplication.getContext().getString(R.string.word_notice_alert))
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
            builder.addContents(AlertData.MessageData(PRNumberBizApplication.getContext().getString(R.string.msg_alim_popup), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
            builder.setAutoCancel(false)
            builder.setBackgroundClickable(false)
            builder.setLeftText(PRNumberBizApplication.getContext().getString(R.string.word_cancel)).setRightText(PRNumberBizApplication.getContext().getString(R.string.word_approve))
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
                    }

                    listener?.result(pushActivate, pushMask)
                }
            }).builder().show(PRNumberBizApplication.getContext())
        }

        fun fromHtml(html: String): Spanned {

            return if (OsUtil.isNougat()) {
                Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
            } else {
                Html.fromHtml(html)
            }

        }

        private fun isChromeCustomTabsSupported(context: Context): Boolean {
            val serviceIntent = Intent("android.support.customtabs.action.CustomTabsService");
            serviceIntent.`package` = "com.android.chrome"

            val service = object : CustomTabsServiceConnection() {
                override fun onCustomTabsServiceConnected(name: ComponentName?, client: CustomTabsClient?) {}

                override fun onServiceDisconnected(name: ComponentName?) {}
            }

            val customTabsSupported =
                    context.bindService(serviceIntent, service, Context.BIND_AUTO_CREATE or Context.BIND_WAIVE_PRIORITY)
            context.unbindService(service);

            return customTabsSupported;
        }

        fun openChromeWebView(context: Context, url: String) {
            var urlString = ""
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                urlString = "http://$url"
            } else {
                urlString = url
            }

            val memoryInfo = PplusCommonUtil.getAvailMemoryInfo()

            if (!isChromeCustomTabsSupported(context) || Const.CHROME_WEB_MEMORY_MIN > memoryInfo.availableMegs || Const.CHROME_WEB_MEMORY_MAX > memoryInfo.totalMem / 1048576L) {
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

        fun isToday(date:String):Boolean{
            val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(date)
            val c = Calendar.getInstance()
            val todayYear = c.get(Calendar.YEAR)
            val todayMonth = c.get(Calendar.MONTH)
            val todayDay = c.get(Calendar.DAY_OF_MONTH)
            c.time = d

            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            return (todayYear == year && todayMonth == month && todayDay == day)
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

    }
}


