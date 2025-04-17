package com.root37.buflexz.core.util

import android.app.Activity
import android.content.ComponentName
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.telephony.TelephonyManager
import android.text.Html
import android.text.Spanned
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.info.DeviceUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import com.pplus.utils.part.utils.time.DateFormatUtils
import com.root37.buflexz.BuflexzApplication
import com.root37.buflexz.Const
import com.root37.buflexz.R
import com.root37.buflexz.apps.LauncherScreenActivity
import com.root37.buflexz.apps.common.builder.AlertBuilder
import com.root37.buflexz.apps.common.builder.OnAlertResultListener
import com.root37.buflexz.apps.common.builder.data.AlertData
import com.root37.buflexz.apps.common.mgmt.LoginInfoManager
import com.root37.buflexz.apps.login.LoginActivity
import com.root37.buflexz.core.AESCrypt
import com.root37.buflexz.core.network.ApiBuilder
import com.root37.buflexz.core.network.model.dto.Member
import com.root37.buflexz.core.network.model.response.NewResultResponse
import retrofit2.Call
import java.io.File
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.regex.Pattern

/**
 * Created by imac on 2018. 2. 13..
 */
class PplusCommonUtil {
    companion object {

        private val TAG = PplusCommonUtil::class.java.simpleName
        private val LOG_TAG = PplusCommonUtil::class.java.simpleName

        fun getDeviceID(): String {

            return DeviceUtil.PRIVACY.getDeviceUUID(BuflexzApplication.context)
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

        fun getAlbumDir(): File? {

            var storageDir = File(BuflexzApplication.context.filesDir, BuflexzApplication.context.getString(R.string.word_album_name))

            if (!storageDir.mkdirs()) {
                if (!storageDir.exists()) {
                    LogUtil.e("getAlbumDir", "failed to create directory")
                    return null
                }
            }

            return storageDir
        }

        fun hasNetworkConnection(): Boolean {

            var result = false
            val connectivityManager = BuflexzApplication.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }

            return result
        }

        fun logOutAndRestart() {

            LoginInfoManager.getInstance().clear() // 초기화

            for (activity in BuflexzApplication.getActivityList()) {
                if (!(activity is LauncherScreenActivity)) {
                    activity.finish()
                }
            }
            val intent = Intent(BuflexzApplication.context, LauncherScreenActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            BuflexzApplication.context.startActivity(intent)
        }

        fun getVideoId(videoUrl: String): String? {

            val reg = "(?:youtube(?:-nocookie)?\\.com\\/(?:[^\\/\\n\\s]+\\/\\S+\\/|(?:v|e(?:mbed)?)\\/|\\S*?[?&]v=)|youtu\\.be\\/)([a-zA-Z0-9_-]{11})"
            val pattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE)
            val matcher = pattern.matcher(videoUrl)

            return if (matcher.find()) matcher.group(1) else null
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

        fun getAppVersion(context: Context): String {
            return context.packageManager.getPackageInfo(context.packageName, 0).versionName
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
                    LogUtil.e(LOG_TAG, "filename : " + file.name)
                    val fileUri = Uri.fromFile(File(filePath))
                    file.delete()
                    val c = BuflexzApplication.context.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, "_data='${fileUri.path}'", null, null)
                    if (c != null && c.moveToFirst()) {
                        val id = c.getInt(0)
                        val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id.toLong())
                        LogUtil.e("delete", "uri {}", uri.path)
                        BuflexzApplication.context.contentResolver.delete(uri, null, null)
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

            val tm = BuflexzApplication.context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

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
                    } else return split[1]
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

            if (LoginInfoManager.getInstance().isMember()) {
                ApiBuilder.create().getSession().setCallback(object : PplusCallback<NewResultResponse<Member>> {

                    override fun onResponse(call: Call<NewResultResponse<Member>>, response: NewResultResponse<Member>) {
                        if (LoginInfoManager.getInstance().isMember()) {
                            LoginInfoManager.getInstance().member = response.result
                            LoginInfoManager.getInstance().save()

                            listener?.reload()
                        }

                    }

                    override fun onFailure(call: Call<NewResultResponse<Member>>, t: Throwable, response: NewResultResponse<Member>) {

                    }
                }).build().call()
            }
        }

        fun reloadSession(listener: ReloadListener?) {
            if (LoginInfoManager.getInstance().isMember()) {
                ApiBuilder.create().reloadSession().setCallback(object : PplusCallback<NewResultResponse<Member>> {

                    override fun onResponse(call: Call<NewResultResponse<Member>>, response: NewResultResponse<Member>) {
                        if (LoginInfoManager.getInstance().isMember()) {

                            if (StringUtils.isNotEmpty(response.result!!.refreshToken)) {
                                response.result!!.refreshToken = encryption(response.result!!.refreshToken!!)
                            }

                            LoginInfoManager.getInstance().member = response.result
                            LoginInfoManager.getInstance().save()

                            listener?.reload()
                        }

                    }

                    override fun onFailure(call: Call<NewResultResponse<Member>>, t: Throwable, response: NewResultResponse<Member>) {

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

        fun fromHtml(html: String): Spanned {

            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
            } else {
                Html.fromHtml(html)
            }
        }


        private var mAdmobInterstitialAd: InterstitialAd? = null

        private fun isChromeCustomTabsSupported(context: Context): Boolean {
            val serviceIntent = Intent("android.support.customtabs.action.CustomTabsService")
            serviceIntent.`package` = "com.android.chrome"

            val service = object : CustomTabsServiceConnection() {
                override fun onCustomTabsServiceConnected(name: ComponentName, client: CustomTabsClient) {
                }


                override fun onServiceDisconnected(name: ComponentName?) {}
            }

            val customTabsSupported = context.bindService(serviceIntent, service, Context.BIND_AUTO_CREATE or Context.BIND_WAIVE_PRIORITY)
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

        fun runOtherApp(packageName: String) {

            var intent = BuflexzApplication.context.packageManager.getLaunchIntentForPackage(packageName)
            if (intent != null) { // We found the activity now start the activity
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                BuflexzApplication.context.startActivity(intent)
            } else { // Bring user to the market or let them choose an app?
                intent = Intent(Intent.ACTION_VIEW)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.data = Uri.parse("market://details?id=$packageName")
                BuflexzApplication.context.startActivity(intent)
            }
        }

        fun loginCheck(activity: FragmentActivity, launcher: ActivityResultLauncher<Intent>?): Boolean {
            if (!LoginInfoManager.getInstance().isMember()) {

                val builder = AlertBuilder.Builder()
                builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
                builder.addContents(AlertData.MessageData(activity.getString(R.string.msg_alert_login_join), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                builder.setLeftText(activity.getString(R.string.word_cancel)).setRightText(activity.getString(R.string.word_login_join))
                builder.setBackgroundClickable(false).setAutoCancel(false)
                builder.setOnAlertResultListener(object : OnAlertResultListener {

                    override fun onCancel() {}

                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                        when (event_alert) {
                            AlertBuilder.EVENT_ALERT.RIGHT -> {
                                val intent = Intent(activity, LoginActivity::class.java)
                                if (launcher == null) {
                                    activity.startActivity(intent)
                                } else {
                                    try {
                                        launcher.launch(intent)
                                    } catch (e: Exception) {
                                        LogUtil.e(LOG_TAG, e.toString())
                                    }

                                }
                            }

                            else -> {}
                        }
                    }
                }).builder().show(activity)
                return false
            } else {
                return true
            }
        }

        fun getDateFormat(date: String): String {
            val d = setTimeZoneOffset(DateFormatUtils.PPLUS_DATE_FORMAT.parse(date))
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


        fun <T> getParcelable(bundle: Bundle, name: String?, clazz: Class<T>): T? {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable(name, clazz)
            } else {
                bundle.getParcelable(name)
            }
        }

        fun <T> getParcelableExtra(intent: Intent, name: String?, clazz: Class<T>): T? {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(name, clazz)
            } else {
                intent.getParcelableExtra(name)
            }
        }

        fun <T : Parcelable?> getParcelableArrayListExtra(intent: Intent, name: String?, clazz: Class<T>): ArrayList<T>? {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableArrayListExtra(name, clazz)
            } else {
                intent.getParcelableArrayListExtra(name)
            }
        }

        val defaultProfile = arrayOf(R.drawable.ic_profile_default_1, R.drawable.ic_profile_default_2, R.drawable.ic_profile_default_3, R.drawable.ic_profile_default_4, R.drawable.ic_profile_default_5)
        fun getDefaultProfile(position: Int): Int {
            return defaultProfile[position % defaultProfile.size]
        }

        fun isVersionUpdate(context: Context, newVersion: String?): Int {
            var serverVer = IntArray(3) // 서버에서 받아온 버전값
            var curVer = IntArray(3) // 현재 앱의 버전값
            var isUpdate = -1
            serverVer = getIntegerVersion(newVersion)
            curVer = getIntegerVersion(getAppVersion(context))
            val curVer_int = Integer.valueOf(curVer[0] + curVer[1] + curVer[2])
            val minVer_int = Integer.valueOf(serverVer[0] + serverVer[1] + serverVer[2])

            // 첫번재 자리를 체크해서 버전이 높으면 강제 업데이트
            if (serverVer[0] > curVer[0]) {
                isUpdate = 1
            } else { // 첫번째 자리가 같고, 두번째 자리도 높으면 강제 업데이트
                if (serverVer[0] == curVer[0] && serverVer[1] > curVer[1]) {
                    isUpdate = 1
                } else if (serverVer[0] == curVer[0] && serverVer[1] == curVer[1] && serverVer[2] > curVer[2]) {
                    isUpdate = 0
                }
            }
            return isUpdate
        }

        private fun getIntegerVersion(verInfo: String?): IntArray {
            val ver = IntArray(3)
            if (verInfo != null && verInfo.length > 0 && verInfo.contains(".")) {
                val data: Array<String?> = verInfo.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (i in data.indices) {
                    if (data[i] != null && data[i] != "") {
                        if (data[i]!!.contains("(")) {
                            val position = data[i]!!.indexOf("(")
                            data[i] = data[i]!!.substring(0, position)
                        }
                        try {
                            ver[i] = Integer.valueOf(data[i])
                        } catch (e: NumberFormatException) {
                            ver[i] = 0
                        }
                    }
                }
            }
            return ver
        }

        fun setTimeZoneOffset(date: Date): Date {
            val calendar = Calendar.getInstance()
            val offset = calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET)
            date.time = date.time + offset - 32400000
            return date
        }

        fun setServerTimeZone(date: Date): Date{
            val returnDate = Date()
            val calendar = Calendar.getInstance()
            val offset = calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET)
            returnDate.time = date.time - offset + 32400000
            return returnDate
        }
    }

}


