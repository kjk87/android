package com.pplus.prnumberbiz.apps.push.ui;

import android.content.Context;
import android.graphics.Rect;
import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.apps.push.data.PushReceiverAdapter;
import com.pplus.prnumberbiz.core.network.ApiBuilder;
import com.pplus.prnumberbiz.core.network.model.dto.Msg;
import com.pplus.prnumberbiz.core.network.model.dto.Target;
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
import com.pplus.prnumberbiz.core.util.PplusCommonUtil;

import java.util.HashMap;
import java.util.Map;

import network.common.PplusCallback;
import retrofit2.Call;

public class PushReceiverActivity extends BaseActivity implements ImplToolbar{

    @Override
    public String getPID(){

        return null;
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_push_receiver;
    }

    private TextView text_total_count;
    private int mPage = 1, mTotalCount = 0;
    private boolean mLockListView = true;
    private LinearLayoutManager mLayoutManager;
    private PushReceiverAdapter mAdapter;
    private Msg msg;

    @Override
    public void initializeView(Bundle savedInstanceState){

        msg = getIntent().getParcelableExtra(Const.DATA);
        text_total_count = (TextView) findViewById(R.id.text_push_receiver_total_count);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_push_receiver);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new CustomDecoration(this, R.dimen.height_50, R.dimen.height_60));
        mAdapter = new PushReceiverAdapter(this, msg.getType());
        recyclerView.setAdapter(mAdapter);

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
                        getPushList(mPage);
                    }
                }
            }
        });

        if(msg.getType().equals("push")) {
            getPushCount();
        } else {
            getSmsCount();
        }

    }

    private class CustomDecoration extends RecyclerView.ItemDecoration{

        private int mItemOffset, mTopOffset;

        public CustomDecoration(int itemOffset, int topOffset){

            mItemOffset = itemOffset;
            mTopOffset = topOffset;
        }

        public CustomDecoration(@NonNull Context context, @DimenRes int itemOffsetId, @DimenRes int topOffsetId){

            this(context.getResources().getDimensionPixelSize(itemOffsetId), context.getResources().getDimensionPixelSize(topOffsetId));
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state){

            super.getItemOffsets(outRect, view, parent, state);

            int position = parent.getChildAdapterPosition(view);
            if(position == 0) {
                outRect.set(0, mTopOffset, 0, mItemOffset);
            } else {
                outRect.set(0, 0, 0, mItemOffset);
            }
        }
    }

    private void getSmsCount(){

        Map<String, String> params = new HashMap<>();
        params.put("no", "" + msg.getNo());
        ApiBuilder.create().getSmsTargetCount(params).setCallback(new PplusCallback<NewResultResponse<Integer>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Integer>> call, NewResultResponse<Integer> response){

                mTotalCount = response.getData();
                text_total_count.setText(PplusCommonUtil.Companion.fromHtml(getString(R.string.html_receiver_sms_total_count, mTotalCount)));
                mPage = 1;
                mAdapter.clear();
                getSmsList(mPage);
            }

            @Override
            public void onFailure(Call<NewResultResponse<Integer>> call, Throwable t, NewResultResponse<Integer> response){

            }
        }).build().call();
    }

    private void getSmsList(int page){

        Map<String, String> params = new HashMap<>();
        params.put("no", "" + msg.getNo());
        params.put("pg", "" + page);
        ApiBuilder.create().getSmsTargetList(params).setCallback(new PplusCallback<NewResultResponse<Target>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Target>> call, NewResultResponse<Target> response){

                mAdapter.addAll(response.getDatas());
            }

            @Override
            public void onFailure(Call<NewResultResponse<Target>> call, Throwable t, NewResultResponse<Target> response){

            }
        }).build().call();
    }

    private void getPushCount(){

        Map<String, String> params = new HashMap<>();
        params.put("no", "" + msg.getNo());
        ApiBuilder.create().getPushTargetCount(params).setCallback(new PplusCallback<NewResultResponse<Integer>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Integer>> call, NewResultResponse<Integer> response){

                mTotalCount = response.getData();
                text_total_count.setText(PplusCommonUtil.Companion.fromHtml(getString(R.string.html_receiver_total_count, mTotalCount)));
                mPage = 1;
                mAdapter.clear();
                getPushList(mPage);
            }

            @Override
            public void onFailure(Call<NewResultResponse<Integer>> call, Throwable t, NewResultResponse<Integer> response){

            }
        }).build().call();
    }

    private void getPushList(int page){

        Map<String, String> params = new HashMap<>();
        params.put("no", "" + msg.getNo());
        params.put("pg", "" + page);
        ApiBuilder.create().getPushTargetList(params).setCallback(new PplusCallback<NewResultResponse<Target>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Target>> call, NewResultResponse<Target> response){

                mAdapter.addAll(response.getDatas());
            }

            @Override
            public void onFailure(Call<NewResultResponse<Target>> call, Throwable t, NewResultResponse<Target> response){

            }
        }).build().call();
    }

    @NonNull
    @Override
    public ToolbarOption getToolbarOption(){

        ToolbarOption toolbarOption = new ToolbarOption(this);
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_receiver), ToolbarOption.ToolbarMenu.RIGHT);
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
