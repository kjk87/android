package com.pplus.prnumberbiz.apps.pages.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.pple.pplus.utils.part.utils.StringUtils;
import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;

public class ApplyCategoryActivity extends BaseActivity implements View.OnClickListener{

    @Override
    public String getPID(){

        return null;
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_apply_category;
    }

    private EditText edit_name;

    @Override
    public void initializeView(Bundle savedInstanceState){
        String name = getIntent().getStringExtra(Const.DATA);
        edit_name = (EditText) findViewById(R.id.edit_apply_category_name);
        if(StringUtils.isNotEmpty(name)){
            edit_name.setText(name);
        }
        findViewById(R.id.text_apply_category_cancel).setOnClickListener(this);
        findViewById(R.id.text_apply_category_confirm).setOnClickListener(this);
    }

    @Override
    public void onClick(View view){

        switch (view.getId()) {
            case R.id.text_apply_category_cancel:
                finish();
                break;
            case R.id.text_apply_category_confirm:
                String name = edit_name.getText().toString().trim();
                if(StringUtils.isEmpty(name)){
                    showAlert(R.string.msg_input_category_name);
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra(Const.DATA, name);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }
}
