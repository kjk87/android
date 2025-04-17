package com.pplus.prnumberbiz.push.firebase;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pple.pplus.utils.part.logs.LogUtil;
import com.pple.pplus.utils.part.utils.StringUtils;
import com.pplus.prnumberbiz.apps.common.mgmt.AppInfoManager;
import com.pplus.prnumberbiz.apps.common.mgmt.PushManager;
import com.pplus.prnumberbiz.push.PushReceiveData;

/**
 * Created by j2n on 2016. 7. 29..
 */
public class PRNumberBizMessageService extends FirebaseMessagingService{

    private static final String TAG = "MyFirebaseMsgService";


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        if(StringUtils.isNotEmpty(s)) {
            LogUtil.e(TAG, "onNewToken : {}", s);
            AppInfoManager.getInstance().setPushToken(s);
        }
    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        LogUtil.e(TAG, "From: {}", remoteMessage.getFrom());

        //        eBqWMg6FkUY:APA91bF0zbYpXpwaPDP7AbgOa7UUCau3Cf-xVKNd8BC3DuY6GP5I6lK64NwlBEs2NwbMtJcljaNCYjHgmLn20_aJq_toeSCo4Il52f3d8dUUVwremeJHU7NXJ7f1Ho3YFj_ajGh7tDLi

        // Check if message contains a data payload.
        if(remoteMessage.getData().size() > 0) {

            LogUtil.e(TAG, "NoteSend data payload: " + remoteMessage.getData());

            if(remoteMessage.getData().size() > 0) {

                LogUtil.e(TAG, "Message data payload: " + remoteMessage.getData());

                PushReceiveData data = new PushReceiveData();
                data.setMsgNo(remoteMessage.getData().get("msgNo"));
                data.setTitle(remoteMessage.getData().get("title"));
                data.setContents(remoteMessage.getData().get("contents"));
                if(StringUtils.isNotEmpty(remoteMessage.getData().get("image_path"))) {
                    data.setImage_path(remoteMessage.getData().get("image_path"));
                }

                if(StringUtils.isNotEmpty(remoteMessage.getData().get("image_path1"))) {
                    data.setImage_path1(remoteMessage.getData().get("image_path1"));
                }

                if(StringUtils.isNotEmpty(remoteMessage.getData().get("move_type1"))) {
                    data.setMoveType1(remoteMessage.getData().get("move_type1"));
                }
                if(StringUtils.isNotEmpty(remoteMessage.getData().get("move_type2"))) {
                    data.setMoveType2(remoteMessage.getData().get("move_type2"));
                }
                if(StringUtils.isNotEmpty(remoteMessage.getData().get("move_target"))) {
                    data.setMoveTarget(remoteMessage.getData().get("move_target"));
                }
                if(StringUtils.isNotEmpty(remoteMessage.getData().get("move_target_string"))) {
                    data.setMove_target_string(remoteMessage.getData().get("move_target_string"));
                }

                PushManager.getInstance(this).sendNotification(this, data);
            }

            // Also if you intend on generating your own notifications as a result of a received FCM
            // message, here is where that should be initiated. See sendNotification method below.
        }
        // [END receive_message]
    }
}
