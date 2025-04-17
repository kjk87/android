package com.pplus.luckybol.core.util;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.StringRes;

import com.pplus.luckybol.R;
import com.pplus.luckybol.apps.common.builder.AlertBuilder;
import com.pplus.luckybol.apps.common.builder.data.AlertData;

/**
 * Created by j2n on 2016. 10. 12..
 */

public class ToastUtil{

    public static void show(Context context, @StringRes int message){

        show(context, context.getString(message));
    }

    public static void show(Context context, String message){

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showAlert(Context context, String message){

        AlertBuilder.Builder builder = new AlertBuilder.Builder();
        builder.setTitle(context.getString(R.string.word_notice_alert));
        builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE);
        builder.addContents(new AlertData.MessageData(message, AlertBuilder.MESSAGE_TYPE.TEXT, 3));
        builder.builder().show(context);
    }

    public static void showAlert(Context context, String message, boolean isNewTask){

        AlertBuilder.Builder builder = new AlertBuilder.Builder();
        builder.setTitle(context.getString(R.string.word_notice_alert));
        builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE);
        builder.addContents(new AlertData.MessageData(message, AlertBuilder.MESSAGE_TYPE.TEXT, 3));
        builder.builder().show(context, isNewTask);
    }

    public static void showAlert(Context context, @StringRes int messageId){

        showAlert(context, context.getString(messageId));
    }

    public static void showAlert(Context context, @StringRes int messageId, boolean isNewTask){

        showAlert(context, context.getString(messageId), isNewTask);
    }

    public static void showNotPageToast(Context context) {
        String message = context.getString(R.string.msg_not_exist_prnumber);
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
