package com.pplus.prnumberbiz.apps.setting.ui;


import android.graphics.Typeface;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment;
import com.pplus.prnumberbiz.core.util.PplusCommonUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 회원 탈퇴 마지막 호출
 */
public class SecessionResultFragment extends BaseFragment{

    public static SecessionResultFragment newInstance(){

        SecessionResultFragment fragment = new SecessionResultFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
//        if(getArguments() != null) {
//            mTokenReturnTime = getArguments().getString(Const.TOKEN_RETURN_TIME);
//        }
    }

    @Override
    public String getPID(){

        return "";
    }

    @Override
    public int getLayoutResourceId(){

        return R.layout.fragment_secession_result;
    }

    @Override
    public void initializeView(View container){

        TextView textDate = (TextView) container.findViewById(R.id.text_secessionDate);
        Date todayDate = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        String date = format.format(todayDate);
        textDate.setText(getActivity().getString(R.string.word_secessionDate) + date);
        TextView textDescription = (TextView) container.findViewById(R.id.text_secession_description2);
        SpannableString ss = new SpannableString(getString(R.string.msg_secession_description2));

        ClickableSpan clickableSpan = new ClickableSpan(){

            @Override
            public void updateDrawState(TextPaint ds){

                ds.setUnderlineText(true);
                ds.setColor(ContextCompat.getColor(getActivity(), R.color.color_ad00cc));
                ds.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            }

            @Override
            public void onClick(View view){

                PplusCommonUtil.Companion.logOutAndRestart();
            }
        };
        ss.setSpan(clickableSpan, 17, 22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textDescription.setText(ss);
        textDescription.setMovementMethod(LinkMovementMethod.getInstance());
        container.findViewById(R.id.text_authConfirm).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                PplusCommonUtil.Companion.logOutAndRestart();
            }
        });
    }

    @Override
    public void init(){

    }

    public SecessionResultFragment(){
        // Required empty public constructor
    }


}
