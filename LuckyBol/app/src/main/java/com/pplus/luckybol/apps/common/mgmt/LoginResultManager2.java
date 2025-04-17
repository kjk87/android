package com.pplus.luckybol.apps.common.mgmt;

import android.content.Intent;

import com.pplus.utils.part.utils.StringUtils;
import com.pplus.luckybol.LuckyBolApplication;
import com.pplus.luckybol.R;
import com.pplus.luckybol.apps.common.builder.AlertBuilder;
import com.pplus.luckybol.apps.common.builder.OnAlertResultListener;
import com.pplus.luckybol.apps.common.builder.data.AlertData;
import com.pplus.luckybol.core.code.common.LoginStatus;
import com.pplus.luckybol.core.code.common.RestrictionStatusCode;
import com.pplus.luckybol.core.code.common.SnsTypeCode;
import com.pplus.luckybol.core.network.model.dto.User;
import com.pplus.luckybol.core.util.PplusCommonUtil;

/**
 * Created by ksh on 2016-10-13.
 */

public class LoginResultManager2{

    private static LoginResultManager2 mLoginManager;

    private SnsTypeCode tag;

    public static LoginResultManager2 getInstance(){

        if(mLoginManager == null) {
            mLoginManager = new LoginResultManager2();
        }
        return mLoginManager;
    }

    public interface LoginResultListener{

        public void loginResult(loginState state);
    }

    public enum loginState{
        AuthActivity, // AuthActivity 호출
        SignUpActivity, // 회원가입 호출
        SecessionCancelActivity, // 회원탈퇴 취소 Activity 호출
        UnVerifiedUserActivity, // SMS 인증 Activity 호출
        Success, // 로그인 성공
        Id_pass_fail, // ID, PASS 실패
        Cancel_btn, // Cancel Btn를 누른 경우
        FAQActivity, // FAQ Activity 실행
        Exile, // 영구정지
    }

    public LoginResultManager2 setTag(SnsTypeCode tag){

        this.tag = tag;

        return mLoginManager;
    }

    public void fail(final User user, final LoginResultListener listener){

        AlertBuilder.Builder builder = new AlertBuilder.Builder();
        builder.setTitle(getString(R.string.word_notice_alert));
        builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE);
        builder.addContents(new AlertData.MessageData(getString(R.string.msg_failed_login), AlertBuilder.MESSAGE_TYPE.TEXT, 5));
        builder.setRightText(getString(R.string.word_confirm));
        builder.setOnAlertResultListener(new OnAlertResultListener(){

            @Override
            public void onCancel(){

            }

            @Override
            public void onResult(AlertBuilder.EVENT_ALERT event_alert){

                switch (event_alert) {
                    case SINGLE:
                        if(listener != null) {
                            listener.loginResult(loginState.Id_pass_fail);
                        }
                        break;
                }
            }
        }).builder().show(LuckyBolApplication.getContext(), true);

//        if(user.getLoginFailCount() != 3 && user.getLoginFailCount() < 5) {
//
//            AlertBuilder.Builder builder = new AlertBuilder.Builder();
//            builder.setTitle(getString(R.string.word_notice));
//            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE);
//            builder.addContents(new AlertData.MessageData(getString(R.string.msg_failed_login), AlertBuilder.MESSAGE_TYPE.TEXT, 5));
//            builder.setRightText(getString(R.string.word_confirm));
//            builder.setOnAlertResultListener(new OnAlertResultListener(){
//
//                @Override
//                public void onCancel(){
//
//                }
//
//                @Override
//                public void onResult(AlertBuilder.EVENT_ALERT event_alert){
//
//                    switch (event_alert) {
//                        case SINGLE:
//                            if(listener != null) {
//                                listener.loginResult(loginState.Id_pass_fail);
//                            }
//                            break;
//                    }
//                }
//            }).builder().show(LuckyBolApplication.getContext(), true);
//        } else {
//            if(user.getLoginFailCount() == 3) {
//                AlertBuilder.Builder builder = new AlertBuilder.Builder();
//                builder.setTitle(getString(R.string.word_notice);
//                builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE);
//                builder.addContents(new AlertData.MessageData(getString(R.string.msg_failed_login_3times), AlertBuilder.MESSAGE_TYPE.TEXT, 5));
//                builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.msg_auth));
//                builder.setOnAlertResultListener(new OnAlertResultListener(){
//
//                    @Override
//                    public void onCancel(){
//
//                    }
//
//                    @Override
//                    public void onResult(AlertBuilder.EVENT_ALERT event_alert){
//
//                        switch (event_alert) {
//                            case LEFT:
//                                if(listener != null) {
//                                    listener.loginResult(loginState.Cancel_btn);
//                                }
//                                break;
//                            case RIGHT:
//                                if(listener != null) {
//                                    listener.loginResult(loginState.AuthActivity);
//                                }
//                                break;
//                        }
//                    }
//                }).builder().show(LuckyBolApplication.getContext(), true);
//            } else {
//                if(user.getLoginFailCount() >= 5) {
//                    AlertBuilder.Builder builder = new AlertBuilder.Builder();
//                    builder.setTitle(getString(R.string.word_notice));
//                    builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE);
//                    builder.addContents(new AlertData.MessageData(getString(R.string.msg_failed_login_5times1), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
//                    builder.addContents(new AlertData.MessageData(getString(R.string.msg_failed_login_5times2), AlertBuilder.MESSAGE_TYPE.TEXT, 1));
//                    builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.msg_find_password));
//                    builder.setOnAlertResultListener(new OnAlertResultListener(){
//
//                        @Override
//                        public void onCancel(){
//
//                        }
//
//                        @Override
//                        public void onResult(AlertBuilder.EVENT_ALERT event_alert){
//
//                            switch (event_alert) {
//
//                                case LEFT:
//                                    if(listener != null) {
//                                        listener.loginResult(loginState.Cancel_btn);
//                                    }
//                                    break;
//                                case RIGHT:
//                                    if(listener != null) {
//                                        listener.loginResult(loginState.AuthActivity);
//                                    }
//                                    break;
//                            }
//                        }
//                    }).builder().show(LuckyBolApplication.getContext(), true);
//                }
//            }
//        }
    }

    /**
     * 결과에 따른 처리
     */
    public void success(final User user, final LoginResultListener listener){

        AppInfoManager.getInstance().setIsGustLocation(true);
        // 인증이 true이고, 제재는 none, status는 정상으로 된 경우 로그인 허용
        if(!StringUtils.isEmpty(user.getMobile()) && user.getRestrictionStatus().equals(RestrictionStatusCode.none.name()) && user.getUseStatus() != null &&
                (user.getUseStatus().equals(LoginStatus.normal.name())) || user.getUseStatus().equals(LoginStatus.duplication.name())) {

//            saveLogin(resultLogin);
            if(listener != null) {
                listener.loginResult(loginState.Success);
            }

        }
        // 인증이 true이고, status가 탈퇴대기인 경우 대기상태로 보냄
        else if(user.getUseStatus() != null && user.getUseStatus().equals(LoginStatus.waitingToLeave.name())) {

            AlertBuilder.Builder builder = new AlertBuilder.Builder();
            builder.setTitle(getString(R.string.word_notice_alert));
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE);
            builder.addContents(new AlertData.MessageData(getString(R.string.msg_leave_cancel_description1), AlertBuilder.MESSAGE_TYPE.TEXT, 3));
            builder.addContents(new AlertData.MessageData(getString(R.string.msg_leave_cancel_description2), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm));
            builder.setBackgroundClickable(false).setAutoCancel(false);
            builder.setOnAlertResultListener(new OnAlertResultListener(){

                @Override
                public void onCancel(){
                }

                @Override
                public void onResult(AlertBuilder.EVENT_ALERT event_alert){

                    switch (event_alert) {
                        case RIGHT:
                            if(listener != null) {
                                listener.loginResult(loginState.SecessionCancelActivity);
                            }
                            break;
                        case LEFT:
                            PplusCommonUtil.Companion.logOutAndRestart();
                            break;
                    }
                }
            }).builder().show(LuckyBolApplication.getContext());

        } // 제제 1~6단계인 경우
        else if(user.getRestrictionStatus().equals(RestrictionStatusCode.restriction1.name()) || user.getRestrictionStatus().equals(RestrictionStatusCode.restriction2.name()) ||
                user.getRestrictionStatus().equals(RestrictionStatusCode.restriction3.name()) || user.getRestrictionStatus().equals(RestrictionStatusCode.restriction4.name()) ||
                user.getRestrictionStatus().equals(RestrictionStatusCode.restriction5.name()) || user.getRestrictionStatus().equals(RestrictionStatusCode.restriction6.name())) {

//            saveLogin(resultLogin);

            AlertBuilder.Builder builder = new AlertBuilder.Builder();
            builder.setTitle(getString(R.string.word_notice_alert));
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE);
            String contents = null;

            switch (RestrictionStatusCode.valueOf(user.getRestrictionStatus())) {
                case restriction1:
                    contents = getString(R.string.msg_restriction1);
                    break;
                case restriction2:
                    contents = getString(R.string.msg_restriction2);
                    break;
                case restriction3:
                    contents = getString(R.string.msg_restriction3);
                    break;
                case restriction4:
                    contents = getString(R.string.msg_restriction4);
                    break;
                case restriction5:
                    contents = getString(R.string.msg_restriction5);
                    break;
                case restriction6:
                    contents = getString(R.string.msg_restriction6);
                    break;
            }
            builder.addContents(new AlertData.MessageData(contents, AlertBuilder.MESSAGE_TYPE.TEXT, 5));
            builder.setLeftText(getString(R.string.word_detail)).setRightText(getString(R.string.word_confirm));
            builder.setAutoCancel(false);
            builder.setOnAlertResultListener(new OnAlertResultListener(){

                @Override
                public void onCancel(){

                }

                @Override
                public void onResult(AlertBuilder.EVENT_ALERT event_alert){

                    switch (event_alert) {
                        case LEFT:
                            if(listener != null) {
                                listener.loginResult(loginState.FAQActivity); // FAQ Activity로 보냄
                            }
                            break;
                        case RIGHT:
                            if(listener != null) {
                                listener.loginResult(loginState.Success); // Main으로 보냄
                            }
                            break;
                    }
                }
            }).builder().show(LuckyBolApplication.getContext(), true);
        }
        // 영구정지인 경우
        else if(RestrictionStatusCode.valueOf(user.getRestrictionStatus()) == RestrictionStatusCode.exile) {
            AlertBuilder.Builder builder = new AlertBuilder.Builder();
            builder.setTitle(getString(R.string.word_notice_alert));
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE);
            builder.addContents(new AlertData.MessageData(getString(R.string.msg_restriction_exit), AlertBuilder.MESSAGE_TYPE.TEXT, 5));
            builder.setLeftText(getString(R.string.word_confirm)).setRightText(getString(R.string.msg_inquiry));
            builder.setAutoCancel(false);
            builder.setBackgroundClickable(false);
            builder.setOnAlertResultListener(new OnAlertResultListener(){

                @Override
                public void onCancel(){

                }

                @Override
                public void onResult(AlertBuilder.EVENT_ALERT event_alert){

                    switch (event_alert) {

                        case LEFT:
                            listener.loginResult(loginState.Cancel_btn);
                            break;
                        case RIGHT:
                            if(listener != null) {
                                listener.loginResult(loginState.Exile);
                            }
                            break;
                    }
                }
            }).builder().show(LuckyBolApplication.getContext(), true);
        } else if(StringUtils.isEmpty(user.getMobile())) {
            AlertBuilder.Builder builder = new AlertBuilder.Builder();
            builder.setTitle(getString(R.string.word_notice_alert));
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE);
            builder.addContents(new AlertData.MessageData(String.format(getString(R.string.format_msg_not_verified1), user.getLoginId()), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
            builder.addContents(new AlertData.MessageData(getString(R.string.msg_not_verified2), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_phone_auth));
            builder.setOnAlertResultListener(new OnAlertResultListener(){

                @Override
                public void onCancel(){

                }

                @Override
                public void onResult(AlertBuilder.EVENT_ALERT event_alert){

                    Intent intent = null;
                    switch (event_alert) {

                        case LEFT:
                            break;
                        case RIGHT:
                            saveLogin(user);
                            if(listener != null) {
                                listener.loginResult(loginState.UnVerifiedUserActivity);
                            }
                            break;
                    }
                }
            }).builder().show(LuckyBolApplication.getContext(), true);
        }
    }

    private String getString(int resId){

        return LuckyBolApplication.getContext().getString(resId);
    }

    private void saveLogin(User resultLogin){

        LoginInfoManager.getInstance().setUser(resultLogin);
        LoginInfoManager.getInstance().save();
    }
}
