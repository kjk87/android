package com.pplus.prnumberbiz.apps.setting.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.apps.setting.data.NoticeAdapter;
import com.pplus.prnumberbiz.core.network.ApiBuilder;
import com.pplus.prnumberbiz.core.network.model.dto.Notice;
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;

import java.util.HashMap;
import java.util.Map;

import network.common.PplusCallback;
import retrofit2.Call;

/**
 * 공지사항
 */
public class NoticeActivity extends BaseActivity implements ImplToolbar{

    private LinearLayoutManager mLayoutManager;
    private NoticeAdapter mAdapter;
    private boolean mLockListView = true;
    private int mTotalCount = 0, mPage = 0;

    @Override
    public String getPID(){

        return "Main_menu_notice";
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_notice;
    }

    @Override
    public void initializeView(Bundle savedInstanceState){

        RecyclerView recyclerview = (RecyclerView) findViewById(R.id.recycler_notice);
        mAdapter = new NoticeAdapter(this);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new NoticeAdapter.OnItemClickListener(){

            @Override
            public void onItemClick(int position){

                if(mAdapter.getItem(position) != null) {
                    // Webview 상세로 이동
                    Intent intent = new Intent(NoticeActivity.this, NoticeDetailActivity.class);
                    intent.putExtra(Const.NOTICE, mAdapter.getItem(position));
                    startActivity(intent);
                }
            }
        });

        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener(){

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
                        listCall(mPage);
                    }
                }
            }
        });

        getCount();

    }

    private void getCount(){

        Map<String, String> params = new HashMap<>();
        params.put("appKey", getPackageName());
        ApiBuilder.create().getNoticeCount(params).setCallback(new PplusCallback<NewResultResponse<Integer>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Integer>> call, NewResultResponse<Integer> response){

                mTotalCount = response.getData();
                mPage = 1;
                listCall(mPage);
            }

            @Override
            public void onFailure(Call<NewResultResponse<Integer>> call, Throwable t, NewResultResponse<Integer> response){

            }
        }).build().call();
    }

    private void listCall(int page){

        Map<String, String> params = new HashMap<>();
        params.put("appKey", getPackageName());
        params.put("pg", "" + page);
        mLockListView = true;
        ApiBuilder.create().getNoticeList(params).setCallback(new PplusCallback<NewResultResponse<Notice>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Notice>> call, NewResultResponse<Notice> response){
                mLockListView = false;
                mAdapter.addAll(response.getDatas());
            }

            @Override
            public void onFailure(Call<NewResultResponse<Notice>> call, Throwable t, NewResultResponse<Notice> response){
                mLockListView = false;
            }
        }).build().call();
    }

    @NonNull
    @Override
    public ToolbarOption getToolbarOption(){

        ToolbarOption toolbarOption = new ToolbarOption(this);
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_notice), ToolbarOption.ToolbarMenu.LEFT);
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
