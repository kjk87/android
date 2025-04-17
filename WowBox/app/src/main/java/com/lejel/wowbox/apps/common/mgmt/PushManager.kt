package com.lejel.wowbox.apps.common.mgmt

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.pref.PreferenceUtil
import com.pplus.utils.part.utils.StringUtils
import com.lejel.wowbox.WowBoxApplication
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.LauncherScreenActivity
import com.lejel.wowbox.apps.main.ui.MainActivity
import com.lejel.wowbox.push.NotiManager
import com.lejel.wowbox.push.PushReceiveData
import java.util.concurrent.ExecutionException

/**
 * Created by ksh on 2016-10-20.
 */
class PushManager {
    fun sendNotification(context: Context, data: PushReceiveData) {

        var pushId: Int = PreferenceUtil.getDefaultPreference(context).get(Const.PUSH_ID, 0)
        val notiManager = NotiManager(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notiManager.createMainNotificationChannel()
        }
        val notiBuilder: NotificationCompat.Builder = notiManager.createNotificationCompatBuilder()
        notiBuilder.setSmallIcon(R.mipmap.ic_noti)
        notiBuilder.setDefaults(Notification.DEFAULT_VIBRATE)
        notiBuilder.setContentTitle(data.title)
        notiBuilder.setContentText(data.contents)
        notiBuilder.setAutoCancel(true)
        notiBuilder.setTicker(data.title)
        if (StringUtils.isNotEmpty(data.image_path)) {
            try {
                val theBitmap: Bitmap = Glide.with(context).asBitmap().load(data.image_path).apply(RequestOptions().centerCrop().override(context.resources.getDimensionPixelSize(R.dimen.width_144), context.resources.getDimensionPixelSize(R.dimen.width_144))).submit().get()
                notiBuilder.setLargeIcon(theBitmap)
            } catch (e: InterruptedException) {
                LogUtil.e(TAG, e.toString())
            } catch (e: ExecutionException) {
                LogUtil.e(TAG, e.toString())
            }
        } else {
            val bm: Bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher)
            notiBuilder.setLargeIcon(bm)
        }
        if (StringUtils.isNotEmpty(data.image_path1)) {
            try {
                val bigBitmap: Bitmap = Glide.with(context).asBitmap().load(data.image_path1).apply(RequestOptions().centerCrop().override(context.resources.getDimensionPixelSize(R.dimen.width_1024), context.resources.getDimensionPixelSize(R.dimen.height_512))).submit().get()
                val s = NotificationCompat.BigPictureStyle().bigPicture(bigBitmap)
                s.setBigContentTitle(data.title).setSummaryText(data.contents)
                notiBuilder.setStyle(s)
            } catch (e: InterruptedException) {
                LogUtil.e(TAG, e.toString())
            } catch (e: ExecutionException) {
                LogUtil.e(TAG, e.toString())
            }
        } else {
            val s = NotificationCompat.BigTextStyle().bigText(data.contents)
            s.setBigContentTitle(data.title)
            notiBuilder.setStyle(s)
        }
        notiBuilder.setLights(-0xff400a, 1000, 5000)
        notiBuilder.setVibrate(longArrayOf(0, 500, 100, 500))
        notiBuilder.priority = NotificationCompat.PRIORITY_MAX
        var resultIntent: Intent?
        if (WowBoxApplication.containsInstance(MainActivity::class.java) && LoginInfoManager.getInstance().isMember()) {
            resultIntent = Intent(context, MainActivity::class.java)
        } else {
            resultIntent = Intent(context, LauncherScreenActivity::class.java)
        }
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        resultIntent.putExtra(Const.PUSH_DATA, data)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(WowBoxApplication.context, pushId, resultIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT)
        notiBuilder.setContentIntent(pendingIntent)
        mNotificationManager!!.notify(pushId, notiBuilder.build())
        PreferenceUtil.getDefaultPreference(context).put(Const.PUSH_ID, ++pushId)
    }

    companion object {
        private val TAG = PushManager::class.java.simpleName
        private var mPushManager: PushManager? = null
        private var mNotificationManager: NotificationManager? = null

        @JvmStatic
        fun getInstance(context: Context): PushManager {
            if (mPushManager == null) {
                mPushManager = PushManager()
            }
            mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            return mPushManager!!
        }
    }
}