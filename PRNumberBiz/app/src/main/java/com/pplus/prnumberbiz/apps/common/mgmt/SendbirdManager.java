//package com.pplus.prnumberbiz.apps.common.mgmt;
//
//import android.content.Context;
//import android.content.Intent;
//
//import com.pple.pplus.utils.part.logs.LogUtil;
//import com.pple.pplus.utils.part.utils.StringUtils;
//import com.pplus.prnumberbiz.Const;
//import com.pplus.prnumberbiz.PRNumberBizApplication;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.core.network.model.response.result.TalkChannelData;
//import com.sendbird.android.GroupChannelListQuery;
//import com.sendbird.android.SendBird;
//import com.sendbird.android.SendBirdException;
//import com.sendbird.android.User;
//
///**
// * Created by ksh on 2016-10-06.
// */
//
//public class SendbirdManager{
//
//    private static final String TAG = SendbirdManager.class.getSimpleName();
//    private static SendbirdManager mSendbirdManager;
//    private GroupChannelListQuery mQuery;
//
//    public static SendbirdManager getInstance(){
//
//        if(mSendbirdManager == null) {
//            mSendbirdManager = new SendbirdManager();
//        }
//        return mSendbirdManager;
//    }
//
//    public SendUnReadCountListener mSendUnReadCountListener;
//
//    public interface SendUnReadCountListener{
//
//        void sendUnReadCount(int count);
//    }
//
//    public void setSendUnReadCountListener(SendUnReadCountListener listener){
//
//        mSendUnReadCountListener = listener;
//    }
//
//    public interface SendbirdConnectListener{
//
//        void onConnect(boolean isSuccess);
//    }
//
//    public SendbirdConnectListener mSendbirdConnectListener;
//
//    public void setSendbirdConnectListener(SendbirdConnectListener listener){
//
//        mSendbirdConnectListener = listener;
//    }
//
//    public enum TargetType{
//        member,
//        page;
//    }
//
//    /**
//     * SendBird용을 위한 ID값
//     *
//     * @return
//     */
//    public String getSendBirdId(){
//
//        return "Biz"+String.valueOf(LoginInfoManager.getInstance().getUser().getPage().getNo());
//    }
//
//    public void sendbirdDisconnect(){
//
//        if(SendBird.getConnectionState() != SendBird.ConnectionState.OPEN) {
//            sendbirdInit();
//        }
//
//        SendBird.disconnect(new SendBird.DisconnectHandler(){
//
//            @Override
//            public void onDisconnected(){
//
//                LogUtil.d(TAG, "정상적으로 Talk 서비스에서 로그아웃되었습니다.");
//            }
//        });
//
//    }
//
//    /**
//     * SendBird 초기화
//     */
//    public void sendbirdInit(){
//
//        Context context = PRNumberBizApplication.getContext();
//
//        String sendbird_id = null;
//        // 스테이징 서버인 경우
//        if(DebugUtilManager.getInstance().getDebugValue().isStagingServer()) {
//            sendbird_id = context.getString(R.string.send_bird_id_staging);
//        }
//        // 개발서버인 경우
//        else if(DebugUtilManager.getInstance().getDebugValue().isDebugServer()) {
//            sendbird_id = context.getString(R.string.send_bird_id_dev);
//        }
//        // 상용인 경우
//        else {
//            sendbird_id = context.getString(R.string.send_bird_id);
//        }
//
//        SendBird.init(sendbird_id, context);
//    }
//
//    public void sendbirdInitConnect(final SendbirdConnectListener listener){
//
//        if(SendBird.getConnectionState() != SendBird.ConnectionState.OPEN) {
//            sendbirdInit();
//
//            SendBird.connect(SendbirdManager.getInstance().getSendBirdId(), new SendBird.ConnectHandler(){
//
//                @Override
//                public void onConnected(User user, SendBirdException e){
//
//                    if(e != null) {
//                        TalkUtil.setSendbirdError(e);
//                        LogUtil.entry_log(SendbirdManager.class.getSimpleName());
//                        if(listener != null) {
//                            listener.onConnect(false);
//                        }
//                        return;
//                    }
//
//                    if(listener != null) {
//                        listener.onConnect(true);
//                    }
//                }
//            });
//        } else {
//            if(listener != null) {
//                listener.onConnect(true);
//            }
//        }
//    }
//
//    public void getUnReadCount(final SendUnReadCountListener listener){
////        if(LoginInfoManager.getInstance().isMember()) {
////            ApiBuilder.create().requestUnReadCount().setCallback(new PplusforAlertCallback<ResultResponse<ResultUnReadCount>>(){
////
////                @Override
////                public void onErrorAlert(ResultResponse<ResultUnReadCount> response){
////
////                }
////
////                @Override
////                public void onResponse(Call<ResultResponse<ResultUnReadCount>> call, ResultResponse<ResultUnReadCount> response){
////
////                    if(response != null && response.getData() != null) {
////                        // Sendbird 회원인 경우
////                        if(response.getData().getSendbirdUser() != null && YnCode.Y == response.getData().getSendbirdUser()) {
////                            if(listener != null) {
////                                listener.sendUnReadCount((int)response.getData().getUnread_count());
////                            }
////                            else {
////                                BusProvider.getInstance().post(new BusProviderData().setType(BusProviderData.BUS_MAIN_UNREAD_COUNT).setData((response.getData().getUnread_count())));
////                            }
////                        }
////                    }
////                }
////
////                @Override
////                public void onFailure(Call<ResultResponse<ResultUnReadCount>> call, Throwable t, ResultResponse<ResultUnReadCount> response){
////
////                }
////            }).build().call();
////        }
//
//    }
//
//    public Intent setPushIntentData(Intent resultIntent, TalkChannelData data) {
//
//        // 메시지
//        if(StringUtils.isNotEmpty(data.getMessage())) {
//            resultIntent.putExtra(Const.SB_MESSAGE, data.getMessage());
//        }
//        // 안읽은 메시지 카운트
//        if(StringUtils.isNotEmpty(data.getUnread_message_count())) {
//            resultIntent.putExtra(Const.SB_UNREAD_MESSAGE_COUNT, data.getUnread_message_count());
//        }
//        // channel_url
//        if(StringUtils.isNotEmpty(data.getChannel_url())) {
//            resultIntent.putExtra(Const.SB_CHANNEL_URL, data.getChannel_url());
//        }
//        // 보낸시간
//        if(StringUtils.isNotEmpty(data.getCreated_at())) {
//            resultIntent.putExtra(Const.SB_CREATE_AT, data.getCreated_at());
//        }
//        /**
//         * 보낸사람
//         */
//        // target, member or page
//        if(StringUtils.isNotEmpty(data.getSender_target_type())) {
//            resultIntent.putExtra(Const.SB_SENDER_TARGET_TYPE, data.getSender_target_type());
//        }
//        // userSeqNo or PageSeqNo
//        if(StringUtils.isNotEmpty(data.getSender_target_seq_no())) {
//            resultIntent.putExtra(Const.SB_SENDER_TARGET_SEQ_NO, data.getSender_target_seq_no());
//        }
//        // nickname
//        if(StringUtils.isNotEmpty(data.getSender_nickname())) {
//            resultIntent.putExtra(Const.SB_SENDER_NICKNAME, data.getSender_nickname());
//        }
//        // profile url
//        if(StringUtils.isNotEmpty(data.getSender_profile_url())) {
//            resultIntent.putExtra(Const.SB_SENDER_PROFILE_URL, data.getSender_profile_url());
//        }
//        // PageSeqNo
//        if(StringUtils.isNotEmpty(data.getSender_page_seq_no())) {
//            resultIntent.putExtra(Const.SB_SENDER_PAGE_SEQ_NO, data.getSender_page_seq_no());
//        }
//        // StoreName
//        if(StringUtils.isNotEmpty(data.getSender_store_name())) {
//            resultIntent.putExtra(Const.SB_SENDER_STORE_NAME, data.getSender_store_name());
//        }
//        return  resultIntent;
//    }
//
//    public String subStringBytes(String str, int byteLength){
//        // String 을 byte 길이 만큼 자르기.
//
//        int retLength = 0;
//        int tempSize = 0;
//        int asc;
//        if(str == null || "".equals(str) || "null".equals(str)) {
//            str = "";
//        }
//
//        int length = str.length();
//
//        for(int i = 1; i <= length; i++) {
//            asc = (int) str.charAt(i - 1);
//            if(asc > 127) {
//                if(byteLength >= tempSize + 2) {
//                    tempSize += 2;
//                    retLength++;
//                } else {
//                    return str.substring(0, retLength) + "...";
//                }
//            } else {
//                if(byteLength > tempSize) {
//                    tempSize++;
//                    retLength++;
//                }
//            }
//        }
//
//        return str.substring(0, retLength);
//    }
//
//
//}
