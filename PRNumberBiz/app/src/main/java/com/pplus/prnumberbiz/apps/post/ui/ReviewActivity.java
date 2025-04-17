package com.pplus.prnumberbiz.apps.post.ui;

import androidx.annotation.NonNull;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import android.view.View;

import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;

public class ReviewActivity extends BaseActivity implements ImplToolbar{

    @Override
    public String getPID(){

        return null;
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_review;
    }

    @Override
    public void initializeView(Bundle savedInstanceState){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.review_container, ReviewFragment.newInstance(), ReviewFragment.class.getSimpleName());
        ft.commit();
    }

    @NonNull
    @Override
    public ToolbarOption getToolbarOption(){

        ToolbarOption toolbarOption = new ToolbarOption(this);
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_customer_review), ToolbarOption.ToolbarMenu.LEFT);

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
                }
            }
        };
    }
}
