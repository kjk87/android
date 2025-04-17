package com.pplus.prnumberbiz.apps.mobilegift.ui;

import android.content.Intent;
import androidx.annotation.NonNull;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.apps.mobilegift.data.MobileGiftHistoryAdapter;
import com.pplus.prnumberbiz.core.network.ApiBuilder;
import com.pplus.prnumberbiz.core.network.model.dto.MobileGiftHistory;
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;

import java.util.HashMap;
import java.util.Map;

import network.common.PplusCallback;
import retrofit2.Call;

public class MobileGiftHistoryActivity extends BaseActivity implements ImplToolbar{

    @Override
    public String getPID(){

        return null;
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_mobile_gift_history;
    }

    private int mPage = 1, mTotalCount = 0;
    private boolean mLockListView = true;
    private MobileGiftHistoryAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    @Override
    public void initializeView(Bundle savedInstanceState){

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_mobile_gift_history);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MobileGiftHistoryAdapter(this);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new MobileGiftHistoryAdapter.OnItemClickListener(){

            @Override
            public void onItemClick(int position){

                Intent intent = new Intent(MobileGiftHistoryActivity.this, MobileGiftHistoryDetailActivity.class);
                intent.putExtra(Const.DATA, mAdapter.getItem(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intent, Const.REQ_DETAIL);
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
                        listCall(mPage);
                    }
                }
            }
        });

        getCount();
    }

    private void getCount(){
        ApiBuilder.create().getMobileGiftPurchaseCount().setCallback(new PplusCallback<NewResultResponse<Integer>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Integer>> call, NewResultResponse<Integer> response){
                mTotalCount = response.getData();
                if(mTotalCount == 0){
                    findViewById(R.id.text_mobile_gift_history_not_exist).setVisibility(View.VISIBLE);
                }else{
                    findViewById(R.id.text_mobile_gift_history_not_exist).setVisibility(View.GONE);
                }
                mAdapter.clear();
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
        params.put("pg", ""+page);

        showProgress("");
        ApiBuilder.create().getMobileGiftPurchaseList(params).setCallback(new PplusCallback<NewResultResponse<MobileGiftHistory>>(){

            @Override
            public void onResponse(Call<NewResultResponse<MobileGiftHistory>> call, NewResultResponse<MobileGiftHistory> response){
                hideProgress();
                mAdapter.addAll(response.getDatas());
            }

            @Override
            public void onFailure(Call<NewResultResponse<MobileGiftHistory>> call, Throwable t, NewResultResponse<MobileGiftHistory> response){
                hideProgress();
            }
        }).build().call();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case Const.REQ_DETAIL:
                if(resultCode == RESULT_OK){
                    getCount();
                }
                break;
        }
    }

    @NonNull
    @Override
    public ToolbarOption getToolbarOption(){

        ToolbarOption toolbarOption = new ToolbarOption(this);
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_gift_history), ToolbarOption.ToolbarMenu.LEFT);
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
