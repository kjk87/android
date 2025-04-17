package com.pplus.luckybol.apps.event.data;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import androidx.fragment.app.DialogFragment;

import com.pplus.luckybol.R;
import com.pplus.luckybol.apps.common.component.GraduallyTextView;

/**
 * Created by Administrator on 2016/3/30.
 */
public class EventLoadingView extends DialogFragment {

    public EventLoadingView(){

    }

//    Animation operatingAnim;

    Dialog mDialog;

    GraduallyTextView mGraduallyTextView;

    String text;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        if(mDialog == null) {
            mDialog = new Dialog(getActivity(), R.style.event_loading_dialog);
            mDialog.setContentView(R.layout.dialog_event);
            mDialog.setCanceledOnTouchOutside(true);
            mDialog.getWindow().setGravity(Gravity.CENTER);

//            operatingAnim = new RotateAnimation(360f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//            operatingAnim.setRepeatCount(Animation.INFINITE);
//            operatingAnim.setDuration(2000);

//            LinearInterpolator lin = new LinearInterpolator();
//            operatingAnim.setInterpolator(lin);

            View view = mDialog.getWindow().getDecorView();

            mGraduallyTextView = (GraduallyTextView) view.findViewById(R.id.graduallyTextView);

            mGraduallyTextView.setText(text);

//            operatingAnim.setAnimationListener(new Animation.AnimationListener(){
//
//                @Override
//                public void onAnimationStart(Animation animation){
//
//                }
//
//                @Override
//                public void onAnimationEnd(Animation animation){
//
//                }
//
//                @Override
//                public void onAnimationRepeat(Animation animation){
//                    //          eyelid_left.resetAnimator();
//                    //          eyelid_right.resetAnimator();
//                }
//            });
        }
        return mDialog;
    }

    @Override
    public void onResume(){

        super.onResume();
        mGraduallyTextView.startLoading();
    }

    @Override
    public void onPause(){

        super.onPause();

//        operatingAnim.reset();
        mGraduallyTextView.stopLoading();
    }

    public void setText(String str){

        text = str;
    }

    @Override
    public void onDismiss(DialogInterface dialog){

        super.onDismiss(dialog);
        mDialog = null;
        System.gc();
    }
}
