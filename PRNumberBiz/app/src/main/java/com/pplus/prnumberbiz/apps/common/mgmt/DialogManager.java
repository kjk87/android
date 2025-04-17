package com.pplus.prnumberbiz.apps.common.mgmt;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.pplus.prnumberbiz.R;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by ksh on 2016-09-19.
 */

public class DialogManager{

    private Map<String, Dialog> dialogMap;

    public static DialogManager mDialogManager = null;

    public static DialogManager getInstance(){

        if(mDialogManager == null) {
            mDialogManager = new DialogManager();
        }
        return mDialogManager;
    }

    private DialogManager(){
        // 다이얼로그 맵을 관리합니다.
        dialogMap = new HashMap<>();
    }

    public void showLoadingDialog(Activity activity){

        showLoadingDialog(activity, null, false);
    }

    public void showLoadingDialog(Activity activity, String message, boolean cancelable){

        if(activity == null) {
            return;
        }

        String tag = String.valueOf(activity.hashCode());

        Dialog dialog;

        if(dialogMap.containsKey(tag)) {

            dialog = dialogMap.get(tag);

            if(dialog != null) {
                TextView textView = (TextView) dialog.findViewById(R.id.tv_pb);
                if(!TextUtils.isEmpty(message)) {
                    textView.setVisibility(View.VISIBLE);
                    textView.setText(message);
                }
                dialog.show();
            }

        } else {

            dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_layout);
            TextView textView = (TextView) dialog.findViewById(R.id.tv_pb);
            if(!TextUtils.isEmpty(message)) {
                textView.setVisibility(View.VISIBLE);
                textView.setText(message);
            }
            dialog.setCancelable(cancelable);
            if(!dialog.isShowing() && activity != null && !activity.isFinishing()) {
                try {
                    dialog.show();
                    dialogMap.put(tag, dialog);
                } catch (Exception e) {

                }
            }
        }



    }

    public void hideLoadingDialog(Activity activity){
        try{
            if(activity == null) {
                hideAll();
                return;
            }

            String tag = String.valueOf(activity.hashCode());

            if(dialogMap.containsKey(tag)) {

                Dialog dialog = dialogMap.get(tag);

                if(dialog != null) {
                    if(dialog.isShowing() && activity != null && !activity.isFinishing()) {
                        dialog.dismiss();
                    }
                }

                dialogMap.remove(tag);
            }

            hideAll();
        }catch (Exception e){

        }

    }

    public void hideAll(){

        try{
            Iterator<String> iterator = dialogMap.keySet().iterator();
            while (iterator.hasNext()) {
                String tag = iterator.next();
                Dialog dialog = dialogMap.get(tag);
                if(dialog != null && dialog.isShowing()) {
                    try {
                        dialog.dismiss();
                    } catch (Exception e) {

                    }
                }
                dialogMap.remove(tag);
            }
        }catch (Exception e){

        }


    }

}
