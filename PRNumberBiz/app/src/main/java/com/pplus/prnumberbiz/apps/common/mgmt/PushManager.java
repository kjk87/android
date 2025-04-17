package com.pplus.prnumberbiz.apps.common.mgmt;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pple.pplus.utils.part.info.OsUtil;
import com.pple.pplus.utils.part.logs.LogUtil;
import com.pple.pplus.utils.part.pref.PreferenceUtil;
import com.pple.pplus.utils.part.utils.StringUtils;
import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.PRNumberBizApplication;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.LauncherScreenActivity;
import com.pplus.prnumberbiz.apps.goods.ui.GoodsUseAlertActivity;
import com.pplus.prnumberbiz.apps.main.ui.BizMainActivity;
import com.pplus.prnumberbiz.apps.nfc.ui.NFCPayWaitActivity;
import com.pplus.prnumberbiz.core.code.common.MoveType2Code;
import com.pplus.prnumberbiz.core.network.model.dto.BuyGoods;
import com.pplus.prnumberbiz.push.NotiManager;
import com.pplus.prnumberbiz.push.PushReceiveData;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by ksh on 2016-10-20.
 */

public class PushManager {

    private static final String TAG = PushManager.class.getSimpleName();
    public static PushManager mPushManager;
    private static NotificationManager mNotificationManager;
    private Context mContext;

    public static final int NOTI_SYSTEM = 0; // 시스템 푸쉬(이벤트 등)
    public static final int NOTI_TALK = 1000; // 톡으로 온 경우
    public static final int NOTI_PLUS = 2000; // 플러스 친구를 신청한 경우
    public static final int NOTI_REPLAY = 3000; // 댓글이 달린 경우
    private int mUnReTotalCount = 0;

    public static PushManager getInstance(Context context) {

        if (mPushManager == null) {
            mPushManager = new PushManager();
        }
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        return mPushManager;
    }

    public int getUnReTotalCount() {

        return mUnReTotalCount;
    }

    public void setUnReTotalCount(int unReTotalCount) {

        this.mUnReTotalCount = unReTotalCount;
    }

    public void sendNotification(Context context, PushReceiveData data) {

        if (data == null) {
            return;
        }

        if (StringUtils.isNotEmpty(data.getMoveType2()) && data.getMoveType2().equals(MoveType2Code.buyGoodsUse.name()) && PRNumberBizApplication.containsInstance(BizMainActivity.class) && LoginInfoManager.getInstance().isMember()) {
            Intent intent = new Intent(context, GoodsUseAlertActivity.class);
            BuyGoods buyGoods = new BuyGoods();
            buyGoods.setSeqNo(Long.valueOf(data.getMoveTarget()));
            intent.putExtra(Const.DATA, buyGoods);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return;
        }

        if (StringUtils.isNotEmpty(data.getMoveType2()) && data.getMoveType2().equals(MoveType2Code.payComplete.name()) && PRNumberBizApplication.containsInstance(NFCPayWaitActivity.class)){
            Intent intent = new Intent(context, NFCPayWaitActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Const.ORDER_ID, data.getMove_target_string());
            context.startActivity(intent);
            return;
        }

        int pushId = PreferenceUtil.getDefaultPreference(context).get(Const.PUSH_ID, 0);
        LogUtil.e(TAG, "data = " + data.toString());

        // 현재는 시스템 푸쉬만 존재
        int notifyId = NOTI_SYSTEM;

        NotiManager notiManager = new NotiManager(context);
        if (OsUtil.isOreo()) {
            notiManager.createMainNotificationChannel();
        }

        NotificationCompat.Builder notiBuilder = notiManager.createNotificationCompatBuilder();

        if (StringUtils.isNotEmpty(data.getImage_path())) {
            try {

                Bitmap theBitmap = Glide.
                        with(context).asBitmap().
                        load(data.getImage_path()).apply(new RequestOptions().centerCrop().override(context.getResources().getDimensionPixelSize(R.dimen.width_144), context.getResources().getDimensionPixelSize(R.dimen.width_144))).submit().get();

                notiBuilder.setLargeIcon(theBitmap);
            } catch (Exception e) {

            }
        } else {
            Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
            notiBuilder.setLargeIcon(bm);
        }

        notiBuilder.setSmallIcon(R.mipmap.ic_noti);
        notiBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
        notiBuilder.setContentTitle(data.getTitle());
        notiBuilder.setContentText(data.getContents());
        notiBuilder.setAutoCancel(true);
        notiBuilder.setTicker(data.getTitle());

        notiBuilder.setLights(0xff00bff6, 1000, 5000);
        notiBuilder.setVibrate(new long[]{0, 500, 100, 500});

        notiBuilder.setPriority(NotificationCompat.PRIORITY_MAX);


        Intent resultIntent = null;
        if (PRNumberBizApplication.containsInstance(BizMainActivity.class) && LoginInfoManager.getInstance().isMember()) {
            resultIntent = new Intent(context, BizMainActivity.class);
        } else {
            resultIntent = new Intent(context, LauncherScreenActivity.class);
        }

        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        resultIntent.putExtra(Const.PUSH_DATA, data);

        PendingIntent pendingIntent = PendingIntent.getActivity(PRNumberBizApplication.getContext(), pushId, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        notiBuilder.setContentIntent(pendingIntent);

        mNotificationManager.notify(pushId, notiBuilder.build());
        PreferenceUtil.getDefaultPreference(context).put(Const.PUSH_ID, ++pushId);

    }

    public Bitmap getBitmapFromURL(String src) {

        try {
            Bitmap myBitmap = BitmapFactory.decodeStream((InputStream) new URL(src).getContent());
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 모든 Push의 Noti를 제거
     */
    public void cancelAllNotification() {
        mNotificationManager.cancelAll();
    }

}
