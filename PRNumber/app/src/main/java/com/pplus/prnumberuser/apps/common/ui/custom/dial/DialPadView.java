package com.pplus.prnumberuser.apps.common.ui.custom.dial;

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

import java.util.ArrayList;

/**
 * Created by 김종경 on 2016-08-08.
 */
public class DialPadView extends LinearLayout {

    public interface OnKeyClickListener {

        void onClick(DialKey key, View view);
        void onLongClick(DialKey key, View view);
    }

    private OnKeyClickListener mOnKeyClickListener;

    public DialPadView(Context context) {

        super(context);
        initialize();
    }

    public DialPadView(Context context, AttributeSet attrs) {

        super(context, attrs);
        initialize();
    }

    public DialPadView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        initialize();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DialPadView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        super(context, attrs, defStyleAttr, defStyleRes);
        initialize();
    }

    public void setOnKeyClickListener(OnKeyClickListener onKeyClickListener) {

        this.mOnKeyClickListener = onKeyClickListener;
    }

    private int[] numberImage = {R.drawable.btn_number_1,
            R.drawable.btn_number_2,
            R.drawable.btn_number_3,
            R.drawable.btn_number_4,
            R.drawable.btn_number_5,
            R.drawable.btn_number_6,
            R.drawable.btn_number_7,
            R.drawable.btn_number_8,
            R.drawable.btn_number_9,
            R.drawable.btn_number_10,
            R.drawable.btn_number_0,
            R.drawable.btn_number_11};

    public void initialize() {

        setOrientation(LinearLayout.VERTICAL);
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));

        ArrayList<DialKey> dialList = new ArrayList<>();

        String[] numberList = getResources().getStringArray(R.array.dial_number);
//        String[] koList = getResources().getStringArray(R.array.dial_ko);
//        String[] enList = getResources().getStringArray(R.array.dial_en);

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
        line.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 1));
        addView(line);

        View dialKeyView;

        for (int i = 0; i < dialList.size(); i++) {

            if (i != 0 && i % 3 == 0) {
                line = new LinearLayout(getContext());
                line.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 1));
                addView(line);
            }

            dialKeyView = LayoutInflater.from(getContext()).inflate(R.layout.item_dial_key, new RelativeLayout(getContext()), false);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
//            layoutParams.setMarginEnd(getResources().getDimensionPixelSize(R.dimen.width_119));
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
            dialKeyView.setOnLongClickListener(new OnLongClickListener(){
                @Override
                public boolean onLongClick(View v) {
                    if (mOnKeyClickListener != null) {
                        mOnKeyClickListener.onLongClick((DialKey) v.getTag(), v);
                    }
                    return false;
                }
            });

            line.addView(dialKeyView);
        }

    }
}
