package com.pplus.prnumberuser.push

import android.app.NotificationChannel
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.pplus.prnumberuser.PRNumberApplication
import com.pplus.prnumberuser.R

/**
 * Created by imac on 2018. 4. 9..
 */
class NotiManager(private val context: Context) {

    companion object {
        private val CHANNEL_ID = "com.pplus.prnumberuser.NOTI"
        private val CHANNEL_NAME = PRNumberApplication.getContext().getString(R.string.app_name)
        private val CHANNEL_DESCRIPTION = PRNumberApplication.getContext().getString(R.string.app_name)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getMainNotificationId(): String {
        return CHANNEL_ID
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createMainNotificationChannel() {
        val id = CHANNEL_ID
        val name = CHANNEL_NAME
        val description = CHANNEL_DESCRIPTION
        val importance = android.app.NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel(id, name, importance)
        mChannel.description = description
        mChannel.enableLights(true)
        mChannel.lightColor = Color.RED
        mChannel.enableVibration(true)
        val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
        mNotificationManager.createNotificationChannel(mChannel)
    }

    fun createNotificationCompatBuilder(): NotificationCompat.Builder {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return NotificationCompat.Builder(context, getMainNotificationId())
        } else {
            return NotificationCompat.Builder(context)
        }
    }
}