package com.pplus.prnumberbiz.apps.common.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.pplus.prnumberbiz.R;


/**
 * Created by Windows7-00 on 2016-10-21.
 */

public class GradeBar extends LinearLayout {

    private int mYellowStar = R.drawable.ic_post_score_y;
    private int mGrayStar = R.drawable.ic_post_score_g;
    private int mHalfowStar = R.drawable.ic_post_score_y_half;
    private int mMargin = 0;

    public GradeBar(Context context) {
        super(context);
    }

    public GradeBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout(context, attrs);
    }

    public GradeBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout(context, attrs);
    }

    private void initLayout(Context context, AttributeSet attrs) {

        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.GradeBar, 0, 0);

        mYellowStar = attr.getResourceId(R.styleable.GradeBar_grade_y, mYellowStar);
        mGrayStar = attr.getResourceId(R.styleable.GradeBar_grade_g, mGrayStar);
        mHalfowStar = attr.getResourceId(R.styleable.GradeBar_grade_h, mHalfowStar);
        mMargin = attr.getDimensionPixelSize(R.styleable.GradeBar_grade_margin, context.getResources().getDimensionPixelSize(R.dimen.width_6));
        //do nothing
        setOrientation(LinearLayout.HORIZONTAL);
        this.setGravity(Gravity.CENTER);
    }

    public void build(String grade) {
        removeAllViews();
        String[] grades = grade.split("\\.");
        int front = Integer.valueOf(grades[0]);
        if(grades.length == 2){
            int decimal = Integer.valueOf(grades[1]);

            ImageView[] imageStar = new ImageView[5];
            for (int i = 0; i < 5; i++) {
                imageStar[i] = new ImageView(getContext());
                LayoutParams lp = null;
                lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                if(i < 4){
                    lp.rightMargin = mMargin;
                }

                if(i < front){
                    imageStar[i].setImageResource(mYellowStar);
                }else{
                    imageStar[i].setImageResource(mGrayStar);
                }

                if(decimal >= 5 && i == front){
                    imageStar[front].setImageResource(mHalfowStar);
                }

                this.addView(imageStar[i], lp);
            }
        }else{
            ImageView[] imageStar = new ImageView[5];
            for (int i = 0; i < 5; i++) {
                imageStar[i] = new ImageView(getContext());
                LayoutParams lp = null;
                lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                if(i < 4){
                    lp.rightMargin = mMargin;
                }

                if(i < front){
                    imageStar[i].setImageResource(mYellowStar);
                }else{
                    imageStar[i].setImageResource(mGrayStar);
                }

                this.addView(imageStar[i], lp);
            }
        }

    }

}
