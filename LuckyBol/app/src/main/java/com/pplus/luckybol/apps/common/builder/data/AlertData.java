package com.pplus.luckybol.apps.common.builder.data;

import com.pplus.utils.part.utils.StringUtils;
import com.pplus.luckybol.apps.common.builder.AlertBuilder;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by j2n on 2016. 8. 22..
 */
public class AlertData implements Serializable{

    private static final long serialVersionUID = 5076033928181479319L;

    private String LOG_TAG = this.getClass().getSimpleName();

    private int identityHashCode;

    public int getIdentityHashCode(){

        return identityHashCode;
    }

    /**
     * main title
     */
    private String title;
    /**
     * contents array
     */
    private MessageData[] contents;
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
    private boolean autoCancel;

    /**
     * auto cancel
     */
    private boolean backgroundClickable;

    /**
     * 기본적으로 표시되는 라인의 수 default 2line..
     */
    private int maxLine;
    /**
     * alert style..
     * <pre>
     *     기본 스타일은 Message type
     * </pre>
     */
    private AlertBuilder.STYLE_ALERT style_alert;

    public AlertData(String title, MessageData[] contents, String leftText, String rightText, boolean autoCancel, boolean backgroundClickable, AlertBuilder.STYLE_ALERT style_alert, int maxLine){

        this.title = title;
        this.contents = contents;
        this.leftText = leftText;
        this.rightText = rightText;
        this.autoCancel = autoCancel;
        this.style_alert = style_alert;
        this.backgroundClickable = backgroundClickable;
        this.maxLine = maxLine;

        this.identityHashCode = System.identityHashCode(this);
    }

    public AlertData(){

        this.identityHashCode = System.identityHashCode(this);
    }

    public String getTitle(){

        return title;
    }

    public MessageData[] getContents(){

        return contents;
    }

    public String getLeftText(){

        return leftText;
    }

    public String getRightText(){

        return rightText;
    }

    public boolean isAutoCancel(){

        return autoCancel;
    }

    public int getMaxLine(){

        return maxLine;
    }

    public boolean isSingle(){

        if(StringUtils.isEmpty(leftText) || StringUtils.isEmpty(rightText)) {
            return true;
        }
        return false;
    }

    public void setTitle(String title){

        this.title = title;
    }

    public void setContents(MessageData[] contents){

        this.contents = contents;
    }

    public void setLeftText(String leftText){

        this.leftText = leftText;
    }

    public void setRightText(String rightText){

        this.rightText = rightText;
    }

    public void setAutoCancel(boolean autoCancel){

        this.autoCancel = autoCancel;
    }

    public boolean isBackgroundClickable(){

        return backgroundClickable;
    }

    public void setBackgroundClickable(boolean backgroundClickable){

        this.backgroundClickable = backgroundClickable;
    }

    public AlertBuilder.STYLE_ALERT getStyle_alert(){

        return style_alert;
    }

    public void setStyle_alert(AlertBuilder.STYLE_ALERT style_alert){

        this.style_alert = style_alert;
    }

    @Override
    public String toString(){

        return "AlertData{" +
                "title='" + title + '\'' +
                ", contents=" + Arrays.toString(contents) +
                ", leftText='" + leftText + '\'' +
                ", rightText='" + rightText + '\'' +
                ", autoCancel=" + autoCancel +
                ", maxLine=" + maxLine +
                ", style_alert=" + style_alert +
                '}';
    }

    public static class MessageData implements Serializable{

        private static final long serialVersionUID = 6943399599090986268L;

        private String LOG_TAG = this.getClass().getSimpleName();

        public MessageData(){

        }

        private String contents;

        private AlertBuilder.MESSAGE_TYPE message_type = AlertBuilder.MESSAGE_TYPE.TEXT;

        private int maxLine = 3;

        public MessageData(String contents){

            this.contents = contents;
        }

        public MessageData(String contents, AlertBuilder.MESSAGE_TYPE message_type){

            this.contents = contents;
            this.message_type = message_type;
        }

        public MessageData(String contents, AlertBuilder.MESSAGE_TYPE message_type, int maxLine){

            this.contents = contents;
            this.message_type = message_type;
            this.maxLine = maxLine;
        }

        public void setContents(String contents){

            this.contents = contents;
        }

        public void setMessage_type(AlertBuilder.MESSAGE_TYPE message_type){

            this.message_type = message_type;
        }

        public void setMaxLine(int maxLine){

            this.maxLine = maxLine;
        }

        public String getContents(){

            return contents;
        }

        public AlertBuilder.MESSAGE_TYPE getMessage_type(){

            return message_type;
        }

        public int getMaxLine(){

            return maxLine;
        }

        @Override
        public String toString(){

            return "MessageData{" +
                    "contents='" + contents + '\'' +
                    ", message_type=" + message_type +
                    ", maxLine=" + maxLine +
                    '}';
        }
    }

    @Override
    public boolean equals(Object o){

        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        AlertData alertData = (AlertData) o;

        if(autoCancel != alertData.autoCancel) return false;
        if(maxLine != alertData.maxLine) return false;
        if(title != null ? !title.equals(alertData.title) : alertData.title != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if(!Arrays.equals(contents, alertData.contents)) return false;
        return style_alert == alertData.style_alert;

    }

    @Override
    public int hashCode(){

        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(contents);
        result = 31 * result + (autoCancel ? 1 : 0);
        result = 31 * result + maxLine;
        result = 31 * result + (style_alert != null ? style_alert.hashCode() : 0);
        return result;
    }
}
