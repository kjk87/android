package com.pplus.prnumberbiz.apps.signin.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.apps.common.ui.common.WebViewActivity;
import com.pplus.prnumberbiz.core.network.ApiBuilder;
import com.pplus.prnumberbiz.core.network.model.dto.No;
import com.pplus.prnumberbiz.core.network.model.dto.Terms;
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import network.common.PplusCallback;
import retrofit2.Call;

public class NotSignedTermsActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, ImplToolbar{

    @Override
    public String getPID(){

        return null;
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_not_signed_terms;
    }

    private LinearLayout layout_terms;
    private List<CheckBox> mCheckBoxList;
    private CheckBox check_all;
    private ArrayList<Terms> mTermsList;
    private Map<Long, Boolean> mTermsAgreeMap;
    private ScrollView scroll_add_info;

    @Override
    public void initializeView(Bundle savedInstanceState){

        scroll_add_info = (ScrollView)findViewById(R.id.scroll_add_info);

        layout_terms = (LinearLayout)findViewById(R.id.layout_add_info_terms);

        check_all = (CheckBox) findViewById(R.id.check_add_info_totalAgree);
        check_all.setOnCheckedChangeListener(this);

        findViewById(R.id.text_add_info_complete).setOnClickListener(this);

        ApiBuilder.create().getNotSignedActiveTermsAll(getPackageName()).setCallback(new PplusCallback<NewResultResponse<Terms>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Terms>> call, NewResultResponse<Terms> response){

                mTermsList = (ArrayList<Terms>) response.getDatas();
                if(mTermsList != null && mTermsList.size() > 0) {
                    mTermsAgreeMap = new HashMap<>();
                    layout_terms.removeAllViews();
                    mCheckBoxList = new ArrayList<CheckBox>();
                    for(int i = 0; i < mTermsList.size(); i++) {
                        final Terms terms = mTermsList.get(i);
                        mTermsAgreeMap.put(terms.getNo(), false);
                        View viewTerms = LayoutInflater.from(NotSignedTermsActivity.this).inflate(R.layout.item_terms, new LinearLayout(NotSignedTermsActivity.this));
                        if(i != 0) {
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            layoutParams.setMargins(0, getResources().getDimensionPixelSize(R.dimen.height_50), 0, 0);
                            viewTerms.setLayoutParams(layoutParams);
                        }
                        CheckBox checkTerms = (CheckBox) viewTerms.findViewById(R.id.check_terms_agree);
                        checkTerms.setText(terms.getSubject());
                        checkTerms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){

                                boolean isAll = true;
                                for(CheckBox checkBox : mCheckBoxList) {
                                    if(!checkBox.isChecked()) {
                                        isAll = false;
                                        break;
                                    }
                                }
                                check_all.setOnCheckedChangeListener(null);
                                check_all.setChecked(isAll);
                                check_all.setOnCheckedChangeListener(NotSignedTermsActivity.this);

                                mTermsAgreeMap.put(terms.getNo(), isChecked);
                            }
                        });
                        mCheckBoxList.add(checkTerms);

                        viewTerms.findViewById(R.id.image_terms_story).setOnClickListener(new View.OnClickListener(){

                            @Override
                            public void onClick(View view){

                                Intent intent = new Intent(NotSignedTermsActivity.this, WebViewActivity.class);
                                intent.putExtra(Const.TITLE, terms.getSubject());
                                intent.putExtra(Const.TOOLBAR_RIGHT_ARROW, true);
                                intent.putExtra(Const.WEBVIEW_URL, terms.getUrl());
                                intent.putExtra(Const.TERMS_LIST, mTermsList);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                            }
                        });
                        layout_terms.addView(viewTerms);
                    }
                }else{
                    check_all.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<NewResultResponse<Terms>> call, Throwable t, NewResultResponse<Terms> response){

            }
        }).build().call();

    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.text_add_info_complete:
                updateTerms();

                break;
        }
    }


    private void updateTerms(){
        if(mTermsList != null && mTermsList.size() > 0){
            for(int i = 0; i < mTermsList.size(); i++) {
                if(mTermsList.get(i).isCompulsory() && !mTermsAgreeMap.get(mTermsList.get(i).getNo())) {
                    showAlert(R.string.msg_agree_terms);
                    return;
                }
            }

            List<No> termsList = new ArrayList<>();
            StringBuilder builder = new StringBuilder();
            for(Map.Entry<Long, Boolean> entry : mTermsAgreeMap.entrySet()) {
                if(entry.getValue()) {
                    builder.append(entry.getKey());
                    builder.append(",");
                }
            }

            builder.setLength(builder.length()-1);
            if(termsList.size() > 0){
                Map<String, String> params = new HashMap<>();
                params.put("termsNo", builder.toString());
                showProgress("");
                ApiBuilder.create().agreeTermsList(params).setCallback(new PplusCallback<NewResultResponse<Object>>(){

                    @Override
                    public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){
                        hideProgress();
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response){
                        hideProgress();
                        setResult(RESULT_OK);
                        finish();
                    }
                }).build().call();

            }else{
                setResult(RESULT_OK);
                finish();
            }
        }else{
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b){
        if(mCheckBoxList != null) {
            for(CheckBox checkBox : mCheckBoxList) {
                checkBox.setChecked(b);
            }
        } else {
            check_all.setChecked(!b);
        }
    }

    @NonNull
    @Override
    public ToolbarOption getToolbarOption(){

        ToolbarOption toolbarOption = new ToolbarOption(this);
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_agree_terms), ToolbarOption.ToolbarMenu.LEFT);

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
