package com.pplus.prnumberbiz.apps.post.ui;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.pple.pplus.utils.part.utils.StringUtils;
import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;

public class SetLuckyBolActivity extends BaseActivity implements ImplToolbar{

    @Override
    public String getPID(){

        return null;
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_set_lucky_bol;
    }

    private EditText edit_set_lucky_bol_point;

    @Override
    public void initializeView(Bundle savedInstanceState){

        edit_set_lucky_bol_point = (EditText)findViewById(R.id.edit_set_lucky_bol_point);
        edit_set_lucky_bol_point.setSingleLine();

        findViewById(R.id.text_set_lucky_bol_complete).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                String bol = edit_set_lucky_bol_point.getText().toString().trim();
                if(StringUtils.isEmpty(bol)){
                    showAlert(R.string.msg_input_post_lucky_bol);
                    return;
                }

                Intent data = new Intent();
                data.putExtra(Const.BOL, bol);
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

    @NonNull
    @Override
    public ToolbarOption getToolbarOption(){

        ToolbarOption toolbarOption = new ToolbarOption(this);
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_setting_luckybol), ToolbarOption.ToolbarMenu.RIGHT);
        return toolbarOption;
    }

    @Override
    public OnToolbarListener getOnToolbarClickListener(){

        return new OnToolbarListener(){

            @Override
            public void onClick(View v, ToolbarOption.ToolbarMenu toolbarMenu, Object tag){

                switch (toolbarMenu) {
                    case RIGHT:
                        if(tag.equals(1)) {
                            onBackPressed();
                        }
                        break;
                }
            }
        };

    }
}
