package com.pplus.luckybol.apps.common.mgmt;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pplus.luckybol.Const;
import com.pplus.luckybol.LuckyBolApplication;
import com.pplus.luckybol.R;
import com.pplus.luckybol.apps.LauncherScreenActivity;
import com.pplus.luckybol.apps.main.ui.AppMainActivity;
import com.pplus.luckybol.push.NotiManager;
import com.pplus.luckybol.push.PushReceiveData;
import com.pplus.utils.part.logs.LogUtil;
import com.pplus.utils.part.pref.PreferenceUtil;
import com.pplus.utils.part.utils.StringUtils;

import java.util.concurrent.ExecutionException;

/**
 * Created by ksh on 2016-10-20.
 */

public class PushManager{

    private static final String TAG = PushManager.class.getSimpleName();
    private static PushManager mPushManager;
    private static NotificationManager mNotificationManager;

    public static PushManager getInstance(Context context){

        if(mPushManager == null) {
            mPushManager = new PushManager();
        }
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        return mPushManager;
    }

    public void sendNotification(Context context, PushReceiveData data){

        if(data == null) {
            return;
        }

        int pushId = PreferenceUtil.getDefaultPreference(context).get(Const.PUSH_ID, 0);

        NotiManager notiManager = new NotiManager(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notiManager.createMainNotificationChannel();
        }

        NotificationCompat.Builder notiBuilder = notiManager.createNotificationCompatBuilder();

        notiBuilder.setSmallIcon(R.mipmap.ic_noti);
        notiBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
        notiBuilder.setContentTitle(data.getTitle());
        notiBuilder.setContentText(data.getContents());
        notiBuilder.setAutoCancel(true);
        notiBuilder.setTicker(data.getTitle());

        if(StringUtils.isNotEmpty(data.getImage_path())) {
            try {
                Bitmap theBitmap = Glide.
                        with(context).asBitmap().
                        load(data.getImage_path()).apply(new RequestOptions().centerCrop().override(context.getResources().getDimensionPixelSize(R.dimen.width_144), context.getResources().getDimensionPixelSize(R.dimen.width_144))).submit().get();

                notiBuilder.setLargeIcon(theBitmap);

            } catch (InterruptedException | ExecutionException e) {
                LogUtil.e(TAG, e.toString());
            }
        } else {
            Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
            notiBuilder.setLargeIcon(bm);
        }

        if(StringUtils.isNotEmpty(data.getImage_path1())) {
            try {
                Bitmap bigBitmap = Glide.
                        with(context).asBitmap().
                        load(data.getImage_path1()).apply(new RequestOptions().centerCrop().override(context.getResources().getDimensionPixelSize(R.dimen.width_1024), context.getResources().getDimensionPixelSize(R.dimen.height_512))).submit().get();

                NotificationCompat.BigPictureStyle s = new NotificationCompat.BigPictureStyle().bigPicture(bigBitmap);
                s.setBigContentTitle(data.getTitle()).setSummaryText(data.getContents());
                notiBuilder.setStyle(s);
            } catch (InterruptedException | ExecutionException e) {
                LogUtil.e(TAG, e.toString());
            }
        } else {
            NotificationCompat.BigTextStyle s = new NotificationCompat.BigTextStyle().bigText(data.getContents());
            s.setBigContentTitle(data.getTitle());
            notiBuilder.setStyle(s);
        }

        notiBuilder.setLights(0xff00bff6, 1000, 5000);
        notiBuilder.setVibrate(new long[]{0, 500, 100, 500});

        notiBuilder.setPriority(NotificationCompat.PRIORITY_MAX);

        Intent resultIntent = null;
//        if(LuckyBolApplication.containsInstance(MainActivity.class) && LoginInfoManager.getInstance().isMember()) {
        if(LuckyBolApplication.containsInstance(AppMainActivity.class) && LoginInfoManager.getInstance().isMember()) {
//            resultIntent = new Intent(context, MainActivity.class);
            resultIntent = new Intent(context, AppMainActivity.class);
        } else {
            resultIntent = new Intent(context, LauncherScreenActivity.class);
        }

        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        resultIntent.putExtra(Const.PUSH_DATA, data);

        PendingIntent pendingIntent = PendingIntent.getActivity(LuckyBolApplication.getContext(), pushId, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        notiBuilder.setContentIntent(pendingIntent);

        mNotificationManager.notify(pushId, notiBuilder.build());
        PreferenceUtil.getDefaultPreference(context).put(Const.PUSH_ID, ++pushId);
    }

}
