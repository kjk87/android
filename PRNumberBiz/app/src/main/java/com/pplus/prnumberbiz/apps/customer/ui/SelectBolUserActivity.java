package com.pplus.prnumberbiz.apps.customer.ui;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

import com.pple.pplus.utils.part.logs.LogUtil;
import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.core.network.model.dto.User;

import java.util.ArrayList;

public class SelectBolUserActivity extends BaseActivity implements ImplToolbar, View.OnClickListener{

    @Override
    public String getPID(){

        return null;
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_select_bol_user;
    }

    private ArrayList<User> mUserList;
    private View layout_tab1, layout_tab2;

    @Override
    public void initializeView(Bundle savedInstanceState){

        mUserList = getIntent().getParcelableArrayListExtra(Const.DATA);

        layout_tab1 = findViewById(R.id.layout_select_bol_user_tab1);
        layout_tab2 = findViewById(R.id.layout_select_bol_user_tab2);
        layout_tab1.setOnClickListener(this);
        layout_tab2.setOnClickListener(this);
        layout_tab1.setSelected(true);
        layout_tab2.setSelected(false);
        setCustomerFragment();
    }

    @Override
    public void onClick(View view){

        switch (view.getId()) {
            case R.id.layout_select_bol_user_tab1:
                layout_tab1.setSelected(true);
                layout_tab2.setSelected(false);
                setCustomerFragment();
                break;
            case R.id.layout_select_bol_user_tab2:
                layout_tab1.setSelected(false);
                layout_tab2.setSelected(true);
                setPlusFragment();
                break;
        }
    }

    public void setUser(ArrayList<User> selectList){

        mUserList = new ArrayList<>();
        mUserList.addAll(selectList);
        LogUtil.e(LOG_TAG, "setUser {}", mUserList.size());
    }

    private void setCustomerFragment(){

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.select_bol_user_container, SelectCustomerFragment.newInstance(mUserList), SelectCustomerFragment.class.getSimpleName());
        ft.commit();
    }

    private void setPlusFragment(){

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.select_bol_user_container, SelectPlusFragment.newInstance(mUserList), SelectPlusFragment.class.getSimpleName());
        ft.commit();
    }

    @NonNull
    @Override
    public ToolbarOption getToolbarOption(){

        ToolbarOption toolbarOption = new ToolbarOption(this);
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_select_customer), ToolbarOption.ToolbarMenu.LEFT);
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
                            if(mUserList == null || mUserList.size() == 0) {
                                showAlert(R.string.msg_select_add_customer);
                            }

                            Intent data = new Intent();
                            data.putParcelableArrayListExtra(Const.DATA, mUserList);
                            setResult(RESULT_OK, data);
                            finish();
                        }
                        break;
                }
            }
        };

    }
}
