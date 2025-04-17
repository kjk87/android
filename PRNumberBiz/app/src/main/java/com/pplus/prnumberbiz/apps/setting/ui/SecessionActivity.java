package com.pplus.prnumberbiz.apps.setting.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import android.view.View;

import com.pple.pplus.utils.BusProvider;
import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment;
import com.pplus.prnumberbiz.core.util.PplusCommonUtil;

/**
 * 회원탈퇴
 */
public class SecessionActivity extends BaseActivity implements ImplToolbar{

    protected ToolbarOption toolbarOption;
    private boolean leave = false;

    @Override
    public String getPID(){

        return "";
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_secession;
    }

    public boolean isLeave(){

        return leave;
    }

    public void setLeave(boolean leave){

        this.leave = leave;
    }

    @Override
    public void initializeView(Bundle savedInstanceState){

        BusProvider.getInstance().register(this);
        BaseFragment fragment = new SecessionPreCautionFragment();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.secession_container, fragment, fragment.getClass().getSimpleName());
        ft.commit();
    }

    /**
     * 회원 탈퇴 Second 호출 - 인증 수단 선택
     */
    public void secessionAuth(){

    }

    public void secessionResult(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        SecessionResultFragment fragment = SecessionResultFragment.newInstance();
        ft.replace(R.id.secession_container, fragment, SecessionResultFragment.class.getSimpleName());
        ft.commitAllowingStateLoss();
    }

    @Override
    protected void onDestroy(){

        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Const.REQ_SIGN_IN:
                setResult(RESULT_OK);
                finish();
                break;
        }
    }

    @NonNull
    @Override
    public ToolbarOption getToolbarOption(){

        ToolbarOption toolbarOption = new ToolbarOption(this);
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_member_leave), ToolbarOption.ToolbarMenu.LEFT);
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

    @Override
    public void onBackPressed(){
        if(leave){
            PplusCommonUtil.Companion.logOutAndRestart();
        }else{
            super.onBackPressed();
        }
    }
}
