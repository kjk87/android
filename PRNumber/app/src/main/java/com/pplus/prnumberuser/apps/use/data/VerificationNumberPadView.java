package com.pplus.prnumberuser.apps.use.data;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.pplus.prnumberuser.R;
import com.pplus.prnumberuser.apps.common.ui.custom.dial.DialKey;

import java.util.ArrayList;

/**
 * Created by 김종경 on 2016-08-08.
 */
public class VerificationNumberPadView extends LinearLayout{

    public interface OnKeyClickListener{

        public void onClick(DialKey key);
    }

    private OnKeyClickListener mOnKeyClickListener;

    public VerificationNumberPadView(Context context){

        super(context);
        initialize();
    }

    public VerificationNumberPadView(Context context, AttributeSet attrs){

        super(context, attrs);
        initialize();
    }

    public VerificationNumberPadView(Context context, AttributeSet attrs, int defStyleAttr){

        super(context, attrs, defStyleAttr);
        initialize();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VerificationNumberPadView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){

        super(context, attrs, defStyleAttr, defStyleRes);
        initialize();
    }

    public void setOnKeyClickListener(OnKeyClickListener onKeyClickListener){

        this.mOnKeyClickListener = onKeyClickListener;
    }

    private int[] numberImage = {R.drawable.btn_passcode_number_1, R.drawable.btn_passcode_number_2, R.drawable.btn_passcode_number_3, R.drawable.btn_passcode_number_4, R.drawable.btn_passcode_number_5, R.drawable.btn_passcode_number_6, R.drawable.btn_passcode_number_7, R.drawable.btn_passcode_number_8, R.drawable.btn_passcode_number_9, R.drawable.btn_passcode_number_10, R.drawable.btn_passcode_number_0, R.drawable.btn_passcode_number_delete};

    public void initialize(){

        setOrientation(LinearLayout.VERTICAL);
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));

        ArrayList<DialKey> dialList = new ArrayList<>();

        String[] numberList = getResources().getStringArray(R.array.dial_number);

        DialKey key;
        for(int i = 0; i < numberList.length; i++) {
            key = new DialKey();
            key.setNumber(numberList[i]);

            dialList.add(key);
        }

        View divider;

        LinearLayout line = new LinearLayout(getContext());
        line.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1));
        addView(line);

        View dialKeyView;

        for(int i = 0; i < dialList.size(); i++) {

            if(i != 0 && i % 3 == 0) {
                line = new LinearLayout(getContext());
                line.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1));
                addView(line);
            }

            dialKeyView = LayoutInflater.from(getContext()).inflate(R.layout.item_dial_key, new RelativeLayout(getContext()), false);
            dialKeyView.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));

            ImageView imageDial = (ImageView) dialKeyView.findViewById(R.id.image_dial_number);
            dialKeyView.setTag(dialList.get(i));

            imageDial.setImageResource(numberImage[i]);

            dialKeyView.setOnClickListener(new OnClickListener(){

                @Override
                public void onClick(View v){

                    if(mOnKeyClickListener != null) {
                        mOnKeyClickListener.onClick((DialKey) v.getTag());
                    }
                }
            });

            line.addView(dialKeyView);
        }

    }
}
