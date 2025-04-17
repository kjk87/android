package com.pplus.luckybol.apps.common.ui.custom.dial;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;

import com.pplus.luckybol.R;

import java.util.ArrayList;

/**
 * Created by 김종경 on 2016-08-08.
 */
public class NumberEventPadView extends LinearLayout {

    public interface OnKeyClickListener {

        public void onClick(DialKey key, View view);
    }

    private OnKeyClickListener mOnKeyClickListener;

    public NumberEventPadView(Context context) {

        super(context);
        initialize();
    }

    public NumberEventPadView(Context context, AttributeSet attrs) {

        super(context, attrs);
        initialize();
    }

    public NumberEventPadView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        initialize();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NumberEventPadView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        super(context, attrs, defStyleAttr, defStyleRes);
        initialize();
    }

    public void setOnKeyClickListener(OnKeyClickListener onKeyClickListener) {

        this.mOnKeyClickListener = onKeyClickListener;
    }

    private int[] numberImage = {R.drawable.btn_number_event_dial_1,
            R.drawable.btn_number_event_dial_2,
            R.drawable.btn_number_event_dial_3,
            R.drawable.btn_number_event_dial_4,
            R.drawable.btn_number_event_dial_5,
            R.drawable.btn_number_event_dial_6,
            R.drawable.btn_number_event_dial_7,
            R.drawable.btn_number_event_dial_8,
            R.drawable.btn_number_event_dial_9,
            R.drawable.btn_number_event_dial_10,
            R.drawable.btn_number_event_dial_0,
            R.drawable.btn_number_event_delete};


    public void initialize() {

        setOrientation(LinearLayout.VERTICAL);
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));

        ArrayList<DialKey> dialList = new ArrayList<>();

        String[] numberList = getResources().getStringArray(R.array.dial_number);

        DialKey key;
        for (int i = 0; i < numberList.length; i++) {
            key = new DialKey();
            key.setNumber(numberList[i]);
//            key.setKo(koList[i]);
//            key.setEn(enList[i]);

            dialList.add(key);
        }

        View divider;

        LinearLayout line = new LinearLayout(getContext());
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1);
//        layoutParams.bottomMargin = getResources().getDimensionPixelSize(R.dimen.height_24);
        line.setLayoutParams(layoutParams);
        addView(line);

        View dialKeyView;

        for (int i = 0; i < dialList.size(); i++) {

            if (i != 0 && i % 3 == 0) {
                line = new LinearLayout(getContext());
                layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1);

//                if(i < 9){
//                    layoutParams.bottomMargin = getResources().getDimensionPixelSize(R.dimen.height_24);
//                }

                line.setLayoutParams(layoutParams);
                addView(line);
            }

            dialKeyView = LayoutInflater.from(getContext()).inflate(R.layout.item_event_dial_key, new RelativeLayout(getContext()), false);
            layoutParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
//            if(i % 3 < 2){
//                layoutParams.setMarginEnd(getResources().getDimensionPixelSize(R.dimen.width_119));
//            }

            dialKeyView.setLayoutParams(layoutParams);

            ImageView imageDial = (ImageView) dialKeyView.findViewById(R.id.image_dial_number);
            dialKeyView.setTag(dialList.get(i));

            imageDial.setImageResource(numberImage[i]);

            dialKeyView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (mOnKeyClickListener != null) {
                        mOnKeyClickListener.onClick((DialKey) v.getTag(), v);
                    }
                }
            });

            line.addView(dialKeyView);
        }

    }
}
