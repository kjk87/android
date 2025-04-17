package com.pplus.prnumberuser.apps.common.builder;

import android.content.Context;
import android.content.Intent;

import com.pplus.utils.BusProvider;
import com.pplus.prnumberuser.apps.common.builder.data.AlertData;
import com.pplus.prnumberuser.apps.common.ui.common.AlertActivity;
import com.squareup.otto.Subscribe;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by 김종경 on 2016-07-20.
 */
public class AlertBuilder implements Serializable{

    private String LOG_TAG = this.getClass().getSimpleName();

    public static String ALERT_KEYS = "alert.builder.keys";

    public enum MESSAGE_TYPE{
        HTML, TEXT
    }

    public enum STYLE_ALERT{
        MESSAGE(0), LIST_CENTER(0), LIST_BOTTOM(0), LIST_RADIO(0), LIST_RADIO_BOTTOM(0);

        public int value;

        public int getValue(){

            return value;
        }

        public void setValue(int value){

            this.value = value;
        }

        STYLE_ALERT(int value){

            this.value = value;
        }


    }

    public enum EVENT_ALERT{
        LEFT(-1), RIGHT(-2), CANCEL(-3), SINGLE(-4), LIST(0), RADIO(0);

        public int value;

        public int getValue(){

            return value;
        }

        public void setValue(int value){

            this.value = value;
        }

        EVENT_ALERT(int value){

            this.value = value;
        }
    }

    /**
     * alert data object..
     */
    private AlertData alertData;

    /**
     * result listener..
     */
    private OnAlertResultListener onAlertResultListener;

    private AlertBuilder(String title, AlertData.MessageData[] contents, String leftText, String rightText, boolean autoCancel, boolean backgroundClickable, STYLE_ALERT style_alert, int maxLine, OnAlertResultListener onAlertResultListener){

        alertData = new AlertData(title, contents, leftText, rightText, autoCancel, backgroundClickable, style_alert, maxLine);

        this.onAlertResultListener = onAlertResultListener;
    }

    @Subscribe
    public void result(AlertResult alertResult){

        BusProvider.getInstance().unregister(this);

        if(alertResult.getAlertData().getIdentityHashCode() == (alertData.getIdentityHashCode())) {

            if(onAlertResultListener != null) {

                switch (alertResult.getEvent_alert()) {
                    case CANCEL:
                        onAlertResultListener.onCancel();
                        break;
                    default:
                        onAlertResultListener.onResult(alertResult.getEvent_alert());
                        break;
                }
                return;
            }
        } else {
            BusProvider.getInstance().register(this);
        }
    }

    public void show(Context context){

        Intent intent = new Intent(context, AlertActivity.class);
        intent.putExtra(ALERT_KEYS, alertData);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        BusProvider.getInstance().register(this);
    }

    /**
     * @param context
     * @param isNewTask 외부에서 호출한 경우
     */
    public void show(Context context, boolean isNewTask){

        Intent intent = new Intent(context, AlertActivity.class);
        intent.putExtra(ALERT_KEYS, alertData);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        BusProvider.getInstance().register(this);
    }

    /**
     * alert 빌더
     */
    public static class Builder{

        public Builder(){

            this.messageData = new AlertData.MessageData[]{};
        }

        private AlertData.MessageData[] messageData;
        /**
         * main title
         */
        private String title;
        /**
         * contents array
         */
        private String[] contents;
        /**
         * left button text
         */
        private String leftText;
        /**
         * right button text
         */
        private String rightText;
        /**
         * auto cancel
         */
        private boolean autoCancel = true;

        /**
         * backgroundClickable
         */
        private boolean backgroundClickable = true;
        /**
         * result listener..
         */
        private OnAlertResultListener onAlertResultListener;
        /**
         * alert style..
         * <pre>
         *     기본 스타일은 Message type
         * </pre>
         */
        private STYLE_ALERT style_alert = STYLE_ALERT.MESSAGE;

        /**
         * 기본적으로 표시되는 라인의 수 default 2line..
         */
        private int maxLine = 2;

        public Builder setTitle(String title){

            this.title = title;
            return this;
        }

        public Builder setContents(String... contents){

            String[] result = contents;

            messageData = new AlertData.MessageData[result.length];
            for(int i = 0; i < result.length; i++) {
                AlertData.MessageData message = new AlertData.MessageData(result[i], MESSAGE_TYPE.TEXT, maxLine);
                messageData[i] = message;
            }
            return this;
        }

        public Builder addContents(AlertData.MessageData contents){

            messageData = Arrays.copyOf(messageData, messageData.length + 1);
            messageData[messageData.length - 1] = contents;
            return this;
        }

        public Builder setLeftText(String leftText){

            this.leftText = leftText;
            return this;
        }

        public Builder setRightText(String rightText){

            this.rightText = rightText;
            return this;
        }

        public Builder setOnAlertResultListener(OnAlertResultListener onAlertResultListener){

            this.onAlertResultListener = onAlertResultListener;
            return this;
        }

        public Builder setAutoCancel(boolean autoCancel){

            this.autoCancel = autoCancel;
            return this;
        }

        public Builder setBackgroundClickable(boolean backgroundClickable){

            this.backgroundClickable = backgroundClickable;
            return this;
        }

        public Builder setStyle_alert(STYLE_ALERT style_alert){

            this.style_alert = style_alert;
            return this;
        }

        public Builder setDefaultMaxLine(int maxLine){

            this.maxLine = maxLine;
            return this;
        }

        public AlertBuilder builder(){

            return new AlertBuilder(title, messageData, leftText, rightText, autoCancel, backgroundClickable, style_alert, maxLine, onAlertResultListener);
        }

    }

    public static class AlertResult{

        private AlertData alertData;
        private EVENT_ALERT event_alert;

        public AlertData getAlertData(){

            return alertData;
        }

        public void setAlertData(AlertData alertData){

            this.alertData = alertData;
        }

        public EVENT_ALERT getEvent_alert(){

            return event_alert;
        }

        public void setEvent_alert(EVENT_ALERT event_alert){

            this.event_alert = event_alert;
        }

        @Override
        public String toString(){

            return "AlertResult{" +
                    "alertData=" + alertData +
                    ", event_alert=" + event_alert +
                    '}';
        }

        @Override
        public boolean equals(Object o){

            if(this == o) return true;
            if(o == null || getClass() != o.getClass()) return false;

            AlertResult that = (AlertResult) o;

            return alertData != null ? alertData.equals(that.alertData) : that.alertData == null;

        }

        @Override
        public int hashCode(){

            return alertData != null ? alertData.hashCode() : 0;
        }
    }

}
