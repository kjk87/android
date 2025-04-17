package com.pplus.prnumberbiz.apps.customer.ui;

import android.content.Intent;
import androidx.annotation.NonNull;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder;
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener;
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData;
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.apps.common.ui.custom.BottomItemOffsetDecoration;
import com.pplus.prnumberbiz.apps.customer.data.SelectSmsLockerAdapter;
import com.pplus.prnumberbiz.core.network.ApiBuilder;
import com.pplus.prnumberbiz.core.network.model.dto.SavedMsg;
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
import com.pplus.prnumberbiz.core.util.PplusCommonUtil;

import java.util.HashMap;
import java.util.Map;

import network.common.PplusCallback;
import retrofit2.Call;

public class SmsLockerActivity extends BaseActivity implements ImplToolbar{

    @Override
    public String getPID(){

        return null;
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_sms_locker;
    }

    private SelectSmsLockerAdapter mAdapter;
    private TextView textTotalCount;
    private LinearLayoutManager mLayoutManager;
    private int mPage = 1, mTotalCount = 0;
    private boolean mLockListView = true;
    private View layout_not_exist;

    @Override
    public void initializeView(Bundle savedInstanceState){

        layout_not_exist = findViewById(R.id.layout_sms_locker_not_exist);
        mAdapter = new SelectSmsLockerAdapter(this);

        TextView text_description = (TextView) findViewById(R.id.text_sms_locker_description);
        text_description.setText(R.string.msg_select_sms_locker_description);

        textTotalCount = (TextView) findViewById(R.id.text_select_sms_locker_totalCount);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_select_sms_locker);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new SelectSmsLockerAdapter(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new BottomItemOffsetDecoration(this, R.dimen.height_50));
        mAdapter.setOnItemClickListener(new SelectSmsLockerAdapter.OnItemClickListener(){

            @Override
            public void onItemClick(int position){

                mAdapter.setSelectData(mAdapter.getItem(position));
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){

            int pastVisibleItems, visibleItemCount, totalItemCount;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){

                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = mLayoutManager.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
                if(!mLockListView) {
                    if((totalItemCount < mTotalCount) && (visibleItemCount + pastVisibleItems) >= (totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN)) {
                        mPage++;
                        getList(mPage);
                    }
                }
            }
        });

        findViewById(R.id.text_select_sms_locker_delete).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                final SavedMsg msg = mAdapter.getSelectData();
                if(msg == null) {
                    showAlert(R.string.msg_select_delete_msg);
                    return;
                }

                AlertBuilder.Builder builder = new AlertBuilder.Builder();
                builder.setTitle(getString(R.string.word_notice_alert));
                builder.addContents(new AlertData.MessageData(getString(R.string.msg_question_delete_saved_msg), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
                builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm));
                builder.setOnAlertResultListener(new OnAlertResultListener(){

                    @Override
                    public void onCancel(){

                    }

                    @Override
                    public void onResult(AlertBuilder.EVENT_ALERT event_alert){

                        switch (event_alert) {
                            case RIGHT:
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("no", "" + msg.getNo());

                                showProgress("");
                                ApiBuilder.create().deleteSavedMsg(params).setCallback(new PplusCallback<NewResultResponse<Object>>(){

                                    @Override
                                    public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){

                                        hideProgress();
                                        mAdapter.setSelectData(null);
                                        getCount();
                                    }

                                    @Override
                                    public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response){

                                        hideProgress();
                                    }
                                }).build().call();
                                break;
                        }
                    }
                }).builder().show(SmsLockerActivity.this);


            }
        });
        getCount();

    }

    public void getCount(){

        ApiBuilder.create().getSavedMsgCount().setCallback(new PplusCallback<NewResultResponse<Integer>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Integer>> call, NewResultResponse<Integer> response){

                mTotalCount = response.getData();
                if(mTotalCount == 0){
                    layout_not_exist.setVisibility(View.VISIBLE);
                }else{
                    layout_not_exist.setVisibility(View.GONE);
                }
                setTotalCount();
                mPage = 1;
                mAdapter.clear();
                getList(mPage);
            }

            @Override
            public void onFailure(Call<NewResultResponse<Integer>> call, Throwable t, NewResultResponse<Integer> response){

            }
        }).build().call();
    }

    private void getList(int page){

        mLockListView = true;
        Map<String, String> params = new HashMap<>();
        params.put("pg", "" + page);
        showProgress("");
        ApiBuilder.create().getSavedMsgList(params).setCallback(new PplusCallback<NewResultResponse<SavedMsg>>(){

            @Override
            public void onResponse(Call<NewResultResponse<SavedMsg>> call, NewResultResponse<SavedMsg> response){

                hideProgress();

                mAdapter.addAll(response.getDatas());
            }

            @Override
            public void onFailure(Call<NewResultResponse<SavedMsg>> call, Throwable t, NewResultResponse<SavedMsg> response){

            }
        }).build().call();
    }

    private void setTotalCount(){

        textTotalCount.setText(PplusCommonUtil.Companion.fromHtml(getString(R.string.html_total_count, mTotalCount)));
    }

    @NonNull
    @Override
    public ToolbarOption getToolbarOption(){

        ToolbarOption toolbarOption = new ToolbarOption(this);
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_locker), ToolbarOption.ToolbarMenu.LEFT);
        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, getString(R.string.word_confirm));
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

                            SavedMsg savedMsg = mAdapter.getSelectData();

                            if(savedMsg == null) {
                                showAlert(R.string.msg_select_use_msg);
                                return;
                            }

                            Intent intent = new Intent();
                            intent.putExtra(Const.MSG, savedMsg);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                        break;
                }
            }
        };

    }
}
