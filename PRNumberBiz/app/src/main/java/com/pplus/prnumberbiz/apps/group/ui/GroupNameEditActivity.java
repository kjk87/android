package com.pplus.prnumberbiz.apps.group.ui;

import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager;
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.core.code.common.EnumData;
import com.pplus.prnumberbiz.core.network.ApiBuilder;
import com.pplus.prnumberbiz.core.network.model.dto.Group;
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import network.common.PplusCallback;
import retrofit2.Call;

public class GroupNameEditActivity extends BaseActivity implements ImplToolbar{

    @Override
    public String getPID(){

        return "";
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_group_name_edit;
    }

    private EnumData.CustomerType mType;
    private Group mGroup;
    private ArrayList<Group> mGroupList;
    private EditText mEditChangeName;

    @Override
    public void initializeView(Bundle savedInstanceState){

        mType = (EnumData.CustomerType)getIntent().getSerializableExtra(Const.KEY);

        TextView textGroupName = (TextView) findViewById(R.id.text_group_name_groupName);
        mEditChangeName = (EditText) findViewById(R.id.edit_group_name_changeName);

        mGroup = getIntent().getParcelableExtra(Const.DATA);
        mGroupList = getIntent().getParcelableArrayListExtra(Const.GROUP);
        textGroupName.setText(mGroup.getName());

    }

    @NonNull
    @Override
    public ToolbarOption getToolbarOption(){

        ToolbarOption toolbarOption = new ToolbarOption(this);
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_change_groupName), ToolbarOption.ToolbarMenu.LEFT);
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

                            String name = mEditChangeName.getText().toString().trim();
                            if(name.length() == 0) {
                                showAlert(R.string.msg_input_groupName);
                                return;
                            }

                            if(mGroupList != null && mGroupList.size() > 0){
                                for(Group group : mGroupList){
                                    if(group.getName().equals(name)){
                                        showAlert(R.string.msg_duplicated_group_name);
                                        return;
                                    }
                                }
                            }

                            Map<String, String> params = new HashMap<>();
                            params.put("no", ""+mGroup.getNo());
                            params.put("name", name);
                            switch (mType){

                                case customer:

                                    showProgress("");
                                    ApiBuilder.create().updateGroupName(params).setCallback(new PplusCallback<NewResultResponse<Object>>(){

                                        @Override
                                        public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){
                                            hideProgress();
                                            setResult(RESULT_OK);
                                            finish();
                                        }

                                        @Override
                                        public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response){
                                            hideProgress();
                                            if(response.getResultCode() == 504){
                                                showAlert(R.string.msg_duplicated_group_name);
                                            }else {
                                                showAlert(R.string.server_error_default);
                                            }
                                        }
                                    }).build().call();
                                    break;
                                case plus:
                                    params.put("page.no", ""+LoginInfoManager.getInstance().getUser().getPage().getNo());
                                    ApiBuilder.create().updateFanGroupName(params).setCallback(new PplusCallback<NewResultResponse<Object>>(){

                                        @Override
                                        public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){
                                            hideProgress();
                                            setResult(RESULT_OK);
                                            finish();
                                        }

                                        @Override
                                        public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response){
                                            hideProgress();
                                            if(response.getResultCode() == 504){
                                                showAlert(R.string.msg_duplicated_group_name);
                                            }else {
                                                showAlert(R.string.server_error_default);
                                            }
                                        }
                                    }).build().call();
                                    break;
                            }
                        }
                        break;
                }
            }
        };

    }
}
