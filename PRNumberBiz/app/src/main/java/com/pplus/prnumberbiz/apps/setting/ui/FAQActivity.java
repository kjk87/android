package com.pplus.prnumberbiz.apps.setting.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.apps.setting.data.FaqAdapter;
import com.pplus.prnumberbiz.core.network.ApiBuilder;
import com.pplus.prnumberbiz.core.network.model.dto.Faq;
import com.pplus.prnumberbiz.core.network.model.dto.FaqGroup;
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import network.common.PplusCallback;
import retrofit2.Call;

public class FAQActivity extends BaseActivity implements ImplToolbar{

    @Override
    public String getPID(){

        return null;
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_faq;
    }

    private List<FaqGroup> groupList;
    private LinearLayout layout_group;
    private ImageView image_faq_down;
    private boolean mIsGroupClick = false;
    private FaqGroup mSelectGroup;
    private TextView text_group;

    private LinearLayoutManager mLayoutManager;
    private FaqAdapter mAdapter;
    private boolean mLockListView = true;
    private int mTotalCount = 0, mPage = 0;

    @Override
    public void initializeView(Bundle savedInstanceState){
        findViewById(R.id.fv_faq).setOnClickListener(categoryLayoutClickListener);
        findViewById(R.id.fv_faq).setEnabled(false);
        layout_group = (LinearLayout)findViewById(R.id.layout_faq_group);
        layout_group.setVisibility(View.GONE);
        image_faq_down =(ImageView)findViewById(R.id.image_faq_down);
        text_group = (TextView)findViewById(R.id.text_faq_group);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_faq);
        mAdapter = new FaqAdapter(this);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new FaqAdapter.OnItemClickListener(){

            @Override
            public void onItemClick(int position){

                if(mAdapter.getItem(position) != null) {
                    // Webview 상세로 이동
                    Intent intent = new Intent(FAQActivity.this, NoticeDetailActivity.class);
                    intent.putExtra(Const.FAQ, mAdapter.getItem(position));
                    startActivity(intent);
                }
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

        groupList = new ArrayList<FaqGroup>();
        FaqGroup totalGroup = new FaqGroup();
        totalGroup.setNo(0l);
        totalGroup.setName(getString(R.string.word_total));
        mSelectGroup = totalGroup;
        groupList.add(totalGroup);
        getCount();
        callGroup();
    }

    public View.OnClickListener categoryLayoutClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view){

            mIsGroupClick = !mIsGroupClick;

            if(mIsGroupClick) {
                image_faq_down.setImageResource(R.drawable.ic_setting_up);
                layout_group.removeAllViews();
                layout_group.setVisibility(View.VISIBLE);

                for(int i = 0; i < groupList.size(); i++) {
                    if(!groupList.get(i).getNo().equals(mSelectGroup.getNo())){
                        View v = getLayoutInflater().inflate(R.layout.item_toolbar_list_row, null);
                        TextView tv = (TextView) v.findViewById(R.id.tv_item_list);
                        tv.setText(groupList.get(i).getName());
                        tv.setTag(tv.getId(), groupList.get(i));
                        tv.setOnClickListener(categoryClickListener);
                        layout_group.addView(v);
                    }

                }
            } else {
                image_faq_down.setImageResource(R.drawable.ic_setting_down);
                layout_group.setVisibility(View.GONE);
            }
        }
    };

    public View.OnClickListener categoryClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view){

            mIsGroupClick = false;
            image_faq_down.setImageResource(R.drawable.ic_setting_down);
            layout_group.setVisibility(View.GONE);
            mSelectGroup = (FaqGroup) view.getTag(view.getId());
            text_group.setText(mSelectGroup.getName());
            getCount();
        }
    };

    private void callGroup(){

        Map<String, String> params = new HashMap<>();
        params.put("appKey", getPackageName());
        showProgress("");
        ApiBuilder.create().getFaqGroupAll(params).setCallback(new PplusCallback<NewResultResponse<FaqGroup>>(){

            @Override
            public void onResponse(Call<NewResultResponse<FaqGroup>> call, NewResultResponse<FaqGroup> response){
                hideProgress();
                groupList.addAll(response.getDatas());
                findViewById(R.id.fv_faq).setEnabled(true);
            }

            @Override
            public void onFailure(Call<NewResultResponse<FaqGroup>> call, Throwable t, NewResultResponse<FaqGroup> response){
                hideProgress();
            }
        }).build().call();
    }

    private void getCount(){

        Map<String, String> params = new HashMap<>();
        params.put("appKey", getPackageName());
        if(!mSelectGroup.getNo().equals(0l)){
            params.put("no", "" + mSelectGroup.getNo());
        }
        ApiBuilder.create().getFaqCount(params).setCallback(new PplusCallback<NewResultResponse<Integer>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Integer>> call, NewResultResponse<Integer> response){

                mTotalCount = response.getData();
                mPage = 1;
                mAdapter.clear();
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
        if(!mSelectGroup.getNo().equals(0l)){
            params.put("no", "" + mSelectGroup.getNo());
        }
        mLockListView = false;
        showProgress("");
        ApiBuilder.create().getFaqList(params).setCallback(new PplusCallback<NewResultResponse<Faq>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Faq>> call, NewResultResponse<Faq> response){
                hideProgress();
                mLockListView = true;
                mAdapter.addAll(response.getDatas());
            }

            @Override
            public void onFailure(Call<NewResultResponse<Faq>> call, Throwable t, NewResultResponse<Faq> response){
                hideProgress();
            }
        }).build().call();
    }

    @NonNull
    @Override
    public ToolbarOption getToolbarOption(){

        ToolbarOption toolbarOption = new ToolbarOption(this);
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_faq_en), ToolbarOption.ToolbarMenu.LEFT);
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
