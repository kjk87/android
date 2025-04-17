package com.pplus.prnumberbiz.apps.signup.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

import com.pple.pplus.utils.part.utils.StringUtils;
import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment;
import com.pplus.prnumberbiz.apps.signup.data.FranchiseAdapter;
import com.pplus.prnumberbiz.apps.signup.data.ImplFranchiseFragment;
import com.pplus.prnumberbiz.core.network.ApiBuilder;
import com.pplus.prnumberbiz.core.network.model.dto.Franchise;
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
import com.pplus.prnumberbiz.core.util.PplusCommonUtil;

import java.util.HashMap;
import java.util.Map;

import network.common.PplusCallback;
import retrofit2.Call;

/**
 * 회원가입 완료
 */
public class FranchiseResultFragment extends BaseFragment implements ImplFranchiseFragment{

    public static FranchiseResultFragment newInstance(String search){

        FranchiseResultFragment fragment = new FranchiseResultFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Const.DATA, search);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            mSearch = getArguments().getString(Const.DATA);
        }
    }

    @Override
    public String getPID(){

        return "";
    }

    public FranchiseResultFragment(){
        // Required empty public constructor
    }

    @Override
    public int getLayoutResourceId(){

        return R.layout.fragment_franchise_result;
    }

    private int mPage = 1, mTotalCount = 0;
    private boolean mLockListView = true;
    private LinearLayoutManager mLayoutManager;
    private FranchiseAdapter mAdapter;
    private String mSearch;
    private TextView text_total_count;

    @Override
    public void initializeView(View container){

        RecyclerView recyclerView = (RecyclerView) container.findViewById(R.id.recycler_franchise_result);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new LastItemOffsetDecoration(getActivity(), R.dimen.width_250));
        mAdapter = new FranchiseAdapter(getActivity());
        recyclerView.setAdapter(mAdapter);
        text_total_count = (TextView)container.findViewById(R.id.text_franchise_total_count);

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

        mAdapter.setOnItemClickListener(new FranchiseAdapter.OnItemClickListener(){

            @Override
            public void onItemClick(int position){
                mAdapter.setSelectData(position);
            }
        });

        getCount();
    }

    @Override
    public void init(){

    }

    public void reSearch(String search){
        mSearch = search;
        mAdapter.clear();
        getCount();
    }

    private void getCount(){

        Map<String, String> params = new HashMap<>();
        if(StringUtils.isNotEmpty(mSearch)) {
            params.put("search", mSearch);
        }
        ApiBuilder.create().getFranchiseCount(params).setCallback(new PplusCallback<NewResultResponse<Integer>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Integer>> call, NewResultResponse<Integer> response){

                mTotalCount = response.getData();
                text_total_count.setText(PplusCommonUtil.Companion.fromHtml(getString(R.string.html_franchise_search_result_count, mTotalCount)));
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
        Map<String, String> params = new HashMap<>();
        params.put("pg", "" + page);
        if(StringUtils.isNotEmpty(mSearch)) {
            params.put("search", mSearch);
        }
        ApiBuilder.create().getFranchiseList(params).setCallback(new PplusCallback<NewResultResponse<Franchise>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Franchise>> call, NewResultResponse<Franchise> response){
                mAdapter.addAll(response.getDatas());
            }

            @Override
            public void onFailure(Call<NewResultResponse<Franchise>> call, Throwable t, NewResultResponse<Franchise> response){

            }
        }).build().call();
    }

    @Override
    public void onClick(View v){

        Intent intent;
        switch (v.getId()) {
        }
    }

    private class LastItemOffsetDecoration extends RecyclerView.ItemDecoration{

        private int mLastOffset;

        public LastItemOffsetDecoration(int lastOffset){

            mLastOffset = lastOffset;
        }

        public LastItemOffsetDecoration(@NonNull Context context, @DimenRes int lastOffsetId){

            this(context.getResources().getDimensionPixelSize(lastOffsetId));
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state){

            super.getItemOffsets(outRect, view, parent, state);

            int position = parent.getChildAdapterPosition(view);
            if(position != 0 && position == (mTotalCount - 1)) {
                outRect.set(0, 0, 0, mLastOffset);
            }

        }
    }

    @Override
    public Franchise getSelectData(){

        return mAdapter.getSelectData();
    }
}
