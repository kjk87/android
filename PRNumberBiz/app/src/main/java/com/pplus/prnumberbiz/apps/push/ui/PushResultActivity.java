package com.pplus.prnumberbiz.apps.push.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

import com.pple.pplus.utils.part.format.FormatUtil;
import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.apps.push.data.PushListAdapter;
import com.pplus.prnumberbiz.core.code.common.EnumData;
import com.pplus.prnumberbiz.core.network.ApiBuilder;
import com.pplus.prnumberbiz.core.network.model.dto.Msg;
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
import com.pplus.prnumberbiz.core.util.PplusCommonUtil;

import java.util.HashMap;
import java.util.Map;

import network.common.PplusCallback;
import retrofit2.Call;

public class PushResultActivity extends BaseActivity implements ImplToolbar, View.OnClickListener{

    @Override
    public String getPID(){

        return "Main_menu_push_list";
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_push_result;
    }

    private View layout_completeTab, layout_reservationTab, layout_cancelTab;
    private TextView text_totalCount;
    private PushListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    EnumData.MsgStatus mStatus;
    private int mPage = 1, mTotalCount = 0;
    private boolean mLockListView = true;

    @Override
    public void initializeView(Bundle savedInstanceState){

        layout_completeTab = findViewById(R.id.layout_push_result_completeTab);
        layout_reservationTab = findViewById(R.id.layout_push_result_reservationTab);
        layout_cancelTab = findViewById(R.id.layout_push_result_cancelTab);
        layout_completeTab.setOnClickListener(this);
        layout_reservationTab.setOnClickListener(this);
        layout_cancelTab.setOnClickListener(this);


        text_totalCount = (TextView) findViewById(R.id.text_push_result_totalCount);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_push_result);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        mStatus = EnumData.MsgStatus.finish;
        mAdapter = new PushListAdapter(this, mStatus);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new PushListAdapter.OnItemClickListener(){

            @Override
            public void onItemClick(int position){

                Intent intent = new Intent(PushResultActivity.this, PushResultDetailActivity.class);
                intent.putExtra(Const.DATA, mAdapter.getItem(position));
                intent.putExtra(Const.TYPE, mStatus);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        setSelected(layout_completeTab, layout_reservationTab, layout_cancelTab);
        recyclerView.addItemDecoration(new CustomItemOffsetDecoration(this, R.dimen.height_40));
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
                        getMsgList(mPage);
                    }
                }
            }
        });

        getMsgCount();
    }

    @Override
    public void onClick(View view){

        switch (view.getId()) {
            case R.id.layout_push_result_completeTab:
                setSelected(layout_completeTab, layout_reservationTab, layout_cancelTab);
                mStatus = EnumData.MsgStatus.finish;
                mAdapter.setStatus(mStatus);
                getMsgCount();
                break;
            case R.id.layout_push_result_reservationTab:
                setSelected(layout_reservationTab, layout_completeTab, layout_cancelTab);
                mStatus = EnumData.MsgStatus.reserved;
                mAdapter.setStatus(mStatus);
                getMsgCount();
                break;
            case R.id.layout_push_result_cancelTab:
                setSelected(layout_cancelTab, layout_reservationTab, layout_completeTab);
                mStatus = EnumData.MsgStatus.cancelSend;
                mAdapter.setStatus(mStatus);
                getMsgCount();
                break;
        }
    }

    private void getMsgCount(){

        Map<String, String> params = new HashMap<>();
        params.put("status", mStatus.name());
        params.put("type", "push");
        params.put("input", "user");
        showProgress("");
        ApiBuilder.create().getMsgCount(params).setCallback(new PplusCallback<NewResultResponse<Integer>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Integer>> call, NewResultResponse<Integer> response){
                hideProgress();
                mTotalCount = response.getData();

                if(mTotalCount == 0) {
                    if(mStatus.equals(EnumData.MsgStatus.finish)) {
                        text_totalCount.setText(R.string.msg_not_exist_send_list);
                    } else {
                        text_totalCount.setText(R.string.msg_not_exist_reservation_push);
                    }
                } else {
                    text_totalCount.setText(PplusCommonUtil.Companion.fromHtml(getString(R.string.html_total_count3, FormatUtil.getMoneyType(""+mTotalCount))));
                }

                mAdapter.clear();
                mPage = 1;
                getMsgList(mPage);
            }

            @Override
            public void onFailure(Call<NewResultResponse<Integer>> call, Throwable t, NewResultResponse<Integer> response){
                hideProgress();
            }
        }).build().call();
    }

    private void getMsgList(int page){

        Map<String, String> params = new HashMap<>();
        params.put("status", mStatus.name());
        params.put("type", "push");
        params.put("input", "user");
        params.put("pg", "" + page);
        mLockListView = true;
        showProgress("");
        ApiBuilder.create().getMsgList(params).setCallback(new PplusCallback<NewResultResponse<Msg>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Msg>> call, NewResultResponse<Msg> response){

                hideProgress();
                mLockListView = false;
                mAdapter.addAll(response.getDatas());
            }

            @Override
            public void onFailure(Call<NewResultResponse<Msg>> call, Throwable t, NewResultResponse<Msg> response){
                hideProgress();
            }
        }).build().call();
    }

    private class CustomItemOffsetDecoration extends RecyclerView.ItemDecoration{

        private int mTopOffset;

        public CustomItemOffsetDecoration(int topOffset){

            mTopOffset = topOffset;
        }

        public CustomItemOffsetDecoration(@NonNull Context context, @DimenRes int topOffsetId){

            this(context.getResources().getDimensionPixelSize(topOffsetId));
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state){

            super.getItemOffsets(outRect, view, parent, state);

            int position = parent.getChildAdapterPosition(view);
            if(position == 0) {
                outRect.set(0, mTopOffset, 0, 0);
            }

        }
    }

    private void setSelected(View view1, View view2, View view3){

        view1.setSelected(true);
        view2.setSelected(false);
        view3.setSelected(false);
    }

    @NonNull
    @Override
    public ToolbarOption getToolbarOption(){

        ToolbarOption toolbarOption = new ToolbarOption(this);
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_view_result), ToolbarOption.ToolbarMenu.LEFT);
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
