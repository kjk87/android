package com.pplus.prnumberbiz.apps.post.ui;

import androidx.annotation.NonNull;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pple.pplus.utils.part.apps.resource.ResourceUtil;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.core.code.common.EnumData;

import java.util.ArrayList;

public class PostOptionSettingActivity extends BaseActivity implements ImplToolbar{

    @Override
    public String getPID(){

        return null;
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_post_option_setting;
    }

    private LinearLayout layout_post_option_dayOfWeek;



    private ArrayList<TextView> mTextDayOfWeekList;

    @Override
    public void initializeView(Bundle savedInstanceState){
        String[] dayOfWeek = getResources().getStringArray(R.array.day_of_the_week);
        layout_post_option_dayOfWeek = (LinearLayout)findViewById(R.id.layout_post_option_dayOfWeek);
        mTextDayOfWeekList = new ArrayList<>();
        for(int i = 0; i < dayOfWeek.length; i++){
            TextView textView = new TextView(this);
            textView.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
            textView.setTextSize(getResources().getDimensionPixelSize(R.dimen.textSize_40pt));
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(ResourceUtil.getColor(this, R.color.color_545454_000000));
            textView.setId(i);
            textView.setOnClickListener(mDayOfWeekClickListener);
            mTextDayOfWeekList.add(textView);
            layout_post_option_dayOfWeek.addView(textView);
        }
    }

    View.OnClickListener mDayOfWeekClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view){

            view.setSelected(!view.isSelected());

            switch (EnumData.DayOfWeek.values()[view.getId()]){

                case mon:
                    break;
                case tue:
                    break;
                case wed:
                    break;
                case thu:
                    break;
                case fri:
                    break;
                case sat:
                    break;
                case sun:
                    break;
            }
        }
    };

    @NonNull
    @Override
    public ToolbarOption getToolbarOption(){

        ToolbarOption toolbarOption = new ToolbarOption(this);
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_setting_event_option), ToolbarOption.ToolbarMenu.LEFT);
        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, getString(R.string.word_complete));
        return toolbarOption;
    }

    @Override
    public OnToolbarListener getOnToolbarClickListener(){

        return new OnToolbarListener(){

            @Override
            public void onClick(View v, ToolbarOption.ToolbarMenu toolbarMenu, Object tag){

                switch (toolbarMenu) {
                    case LEFT:
                        if(tag.equals(1)) {
                            onBackPressed();
                        }
                        break;
                    case RIGHT:
                        if(tag.equals(1)) {

                        }
                        break;
                }
            }
        };

    }
}
