package com.pplus.prnumberbiz.apps.group.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.core.code.common.EnumData;
import com.pplus.prnumberbiz.core.network.ApiBuilder;
import com.pplus.prnumberbiz.core.network.model.dto.Group;
import com.pplus.prnumberbiz.core.network.model.dto.No;
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;

import java.util.ArrayList;

import network.common.PplusCallback;
import retrofit2.Call;

public class GroupAddActivity extends BaseActivity{

    @Override
    public String getPID(){

        return "";
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_group_add;
    }

    private EditText mEditName;
    private EnumData.CustomerType mType;
    private ArrayList<Group> mGroupList;

    @Override
    public void initializeView(Bundle savedInstanceState){
        mType = (EnumData.CustomerType)getIntent().getSerializableExtra(Const.KEY);

        mGroupList = getIntent().getParcelableArrayListExtra(Const.GROUP);

        mEditName = (EditText) findViewById(R.id.edit_group_add_name);
        mEditName.setSingleLine();
//        mEditName.addTextChangedListener(new EmojiTextWatcher(mEditName));

        findViewById(R.id.text_group_add_cancelBtn).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                setResult(RESULT_CANCELED);
                finish();
            }
        });

        findViewById(R.id.text_group_add_confirmBtn).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                String name = mEditName.getText().toString().trim();
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
                switch (mType){

                    case customer:
                        insertGroup(name);
                        break;
                    case plus:
                        insertPlusGroup(name);
                        break;
                }


            }
        });

    }

    private void insertPlusGroup(String name){
        Group params = new Group();
        params.setName(name);
        params.setDefaultGroup(false);

        int priority = getIntent().getIntExtra(Const.PRIORITY, 0);

        params.setPriority(priority + 1);
        params.setPage(new No(LoginInfoManager.getInstance().getUser().getPage().getNo()));
        showProgress("");
        ApiBuilder.create().insertFanGroup(params).setCallback(new PplusCallback<NewResultResponse<Object>>(){

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
    }

    private void insertGroup(String name){
        Group params = new Group();
        params.setName(name);
        params.setDefaultGroup(false);

        int priority = getIntent().getIntExtra(Const.PRIORITY, 0);

        params.setPriority(priority + 1);
        params.setPage(new No(LoginInfoManager.getInstance().getUser().getPage().getNo()));
        showProgress("");
        ApiBuilder.create().insertGroup(params).setCallback(new PplusCallback<NewResultResponse<Object>>(){

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
    }
}
