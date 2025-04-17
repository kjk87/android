package com.lejel.wowbox.apps.common.ui.custom;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

import com.pplus.utils.part.logs.LogUtil;

/**
 * Created by Windows7-00 on 2016-11-09.
 */

public class ClipEditText extends AppCompatEditText {

    private ClipboardManager mClipboardManager;
    private OnPasteListener mListener;

    public ClipEditText(Context context) {
        super(context);
        mClipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    public ClipEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mClipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    public ClipEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mClipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    @Override
    public boolean onTextContextMenuItem(int id) {
        switch (id){
            case android.R.id.cut:
                break;
            case android.R.id.paste:
                if (mClipboardManager.getPrimaryClip() == null) {
                    return true;
                }
                ClipData.Item item = mClipboardManager.getPrimaryClip().getItemAt(0);
                LogUtil.i("test", "Paste : " + item.getText().toString());
                if (mListener != null) {
                    mListener.onPaste(item.getText().toString());
                }
                break;
            case android.R.id.copy:
                break;
        }
        return super.onTextContextMenuItem(id);
    }

    public void setListener(OnPasteListener listener) {
        this.mListener = listener;
    }

    public interface OnPasteListener {
        void onPaste(String pastText);
    }
}
