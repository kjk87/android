package com.pple.pplus.utils.part.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

/**
 * Created by ksh on 2016-08-30.
 */
public class ClipUtils{

    private Context mContext;
    private ClipboardManager clipboard;
    private String pasteData = "";
    
    public ClipUtils(Context context) {
        super();
        mContext = context;
        
        init();
    }

    private void init(){
        // Gets a handle to the clipboard service.
        clipboard = (ClipboardManager)
                mContext.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    public void copyText(String label, String text) {
        // Creates a new text clip to put on the clipboard
        ClipData clip = ClipData.newPlainText(label,text);

        // Set the clipboard's primary clip.
        clipboard.setPrimaryClip(clip);
    }

    public String pasteText(){

        if(clipboard.hasPrimaryClip()) {
            // Examines the item on the clipboard. If getText() does not return null, the clip item contains the
            // text. Assumes that this application can only handle one item at a time.
            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);

            // Gets the clipboard as text.
            pasteData = item.getText().toString();
        }

        return pasteData;
    }
}
