package com.pplus.luckybol.push.firebase

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import com.pplus.luckybol.apps.common.mgmt.AppInfoManager
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.mgmt.PushManager
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.push.PushReceiveData
import com.pplus.networks.common.PplusCallback
import retrofit2.Call

/**
 * Created by j2n on 2016. 7. 29..
 */
class MessageService : FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        if (StringUtils.isNotEmpty(p0)) {
            LogUtil.e(TAG, "onNewToken : {}", p0)

            if(LoginInfoManager.getInstance().isMember){
                val params = HashMap<String, String>()
                params["pushKey"] = p0
                params["sessionCheck"] = "false"
                ApiBuilder.create().updatePushKey(params).setCallback(object : PplusCallback<NewResultResponse<Any>>{
                    override fun onResponse(call: Call<NewResultResponse<Any>>?,
                                            response: NewResultResponse<Any>?) {

                    }

                    override fun onFailure(call: Call<NewResultResponse<Any>>?,
                                           t: Throwable?,
                                           response: NewResultResponse<Any>?) {

                    }
                }).build().call()
            }
            AppInfoManager.getInstance().pushToken = p0
        }
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        LogUtil.e(TAG, "From: {}", remoteMessage.from)
        //        eBqWMg6FkUY:APA91bF0zbYpXpwaPDP7AbgOa7UUCau3Cf-xVKNd8BC3DuY6GP5I6lK64NwlBEs2NwbMtJcljaNCYjHgmLn20_aJq_toeSCo4Il52f3d8dUUVwremeJHU7NXJ7f1Ho3YFj_ajGh7tDLi
// fSglEG5qf_g:APA91bF7KOIcRLaUVCjNZwPq4nVBcHunfgjNX2ldjJ-TBr75LNFhuVX2SpUsrMkJjwg29PDYUw8NltQjTUPpkBLKkBSWt39vTbxtGV13Ao_OXR8cpRIKrKq8NSP8o1nS-M_RQzu4vc-J
//move_type1=inner, move_type2=eventWin, msg_id=001:1001291, contents=멀티샵 플레이어 이벤트, msgNo=1001291, title=멀티샵 플레이어 이벤트, move_target=1000177
// Check if message contains a data payload.
        if (remoteMessage != null && remoteMessage.data != null && !remoteMessage.data.isEmpty()) {
            LogUtil.e(TAG, "Message data payload: " + remoteMessage.data)
            val data = PushReceiveData()
            data.msgNo = remoteMessage.data["msgNo"]
            data.title = remoteMessage.data["title"]
            data.contents = remoteMessage.data["contents"]
            if (StringUtils.isNotEmpty(remoteMessage.data["image_path"])) {
                data.image_path = remoteMessage.data["image_path"]
            }
            if (StringUtils.isNotEmpty(remoteMessage.data["image_path1"])) {
                data.image_path1 = remoteMessage.data["image_path1"]
            }
            if (StringUtils.isNotEmpty(remoteMessage.data["move_type1"])) {
                data.moveType1 = remoteMessage.data["move_type1"]
            }
            if (StringUtils.isNotEmpty(remoteMessage.data["move_type2"])) {
                data.moveType2 = remoteMessage.data["move_type2"]
            }
            if (StringUtils.isNotEmpty(remoteMessage.data["move_target"])) {
                data.moveTarget = remoteMessage.data["move_target"]
            }
            if (StringUtils.isNotEmpty(remoteMessage.data["move_target_string"])) {
                data.move_target_string = remoteMessage.data["move_target_string"]
            }
            PushManager.getInstance(this).sendNotification(this, data)
            //            sendNotification(data.getTitle(), data.getContents());
        }
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }
}