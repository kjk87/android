package com.root37.buflexz.apps.common.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CompoundButton;

import androidx.annotation.IntDef;
import androidx.appcompat.widget.SwitchCompat;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by ksh on 2016-09-30.
 */

public class SafeSwitchCompat extends SwitchCompat implements CompoundButton.OnCheckedChangeListener{

    private OnSafeCheckedListener onSafeCheckedListener;

    private int mIgnoreListener = CALL_LISTENER;

    public static final int IGNORE = 0;
    public static final int CALL_LISTENER = 1;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({IGNORE, CALL_LISTENER})
    public @interface ListenerMode {
    }

    public SafeSwitchCompat(Context context){
        super(context);
        init(context);
    }

    public SafeSwitchCompat(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context);
    }

    public SafeSwitchCompat(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * @param checkState     change state of the checkbox to
     * @param mIgnoreListener true to ignore the listener else listener will be  notified
     */
    public void setSafeCheck(boolean checkState, @ListenerMode int mIgnoreListener) {
        if (isChecked() == checkState) return; //already in the same state no need to fire listener.

        if (onSafeCheckedListener != null) { // this to avoid a bug if the user listens for the event after using this method and in that case he will miss first check
            this.mIgnoreListener = mIgnoreListener;
        } else {
            this.mIgnoreListener = CALL_LISTENER;
        }
        setChecked(checkState);
    }

    private void init(Context context) {
        setOnCheckedChangeListener(this);
    }

    public OnSafeCheckedListener getOnSafeCheckedListener() {
        return onSafeCheckedListener;
    }

    public void setOnSafeCheckedListener(OnSafeCheckedListener onSafeCheckedListener) {
        this.onSafeCheckedListener = onSafeCheckedListener;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (onSafeCheckedListener != null)
            onSafeCheckedListener.onAlwaysCalledListener(buttonView, isChecked);// this has to be called before onCheckedChange
        if (onSafeCheckedListener != null && (mIgnoreListener == CALL_LISTENER)) {
            onSafeCheckedListener.onCheckedChanged(buttonView, isChecked);
        }
        mIgnoreListener = CALL_LISTENER;
    }

    /**
     * Listener that will be called when you want it to be called.
     * On checked change listeners are called even when the setElementChecked is called from code. :(
     */
    public interface OnSafeCheckedListener extends OnCheckedChangeListener {
        void onAlwaysCalledListener(CompoundButton buttonView, boolean isChecked);
    }
}
