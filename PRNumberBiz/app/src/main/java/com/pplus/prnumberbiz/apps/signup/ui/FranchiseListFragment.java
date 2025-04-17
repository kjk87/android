//package com.pplus.prnumberbiz.apps.signup.ui;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Rect;
//import android.os.Bundle;
//import android.support.annotation.DimenRes;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//
//import com.pplus.prnumberbiz.Const;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment;
//import com.pplus.prnumberbiz.apps.signup.data.FranchiseAdapter;
//import com.pplus.prnumberbiz.apps.signup.data.FranchiseGroupAdapter;
//import com.pplus.prnumberbiz.apps.signup.data.ImplFranchiseFragment;
//import com.pplus.prnumberbiz.core.network.ApiBuilder;
//import com.pplus.prnumberbiz.core.network.model.dto.Franchise;
//import com.pplus.prnumberbiz.core.network.model.dto.FranchiseGroup;
//import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import network.common.PplusCallback;
//import retrofit2.Call;
//
///**
// * 회원가입 완료
// */
//public class FranchiseListFragment extends BaseFragment<SearchFranchiseActivity> implements ImplFranchiseFragment{
//
//    public static FranchiseListFragment newInstance(){
//
//        FranchiseListFragment fragment = new FranchiseListFragment();
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState){
//
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public String getPID(){
//
//        return "";
//    }
//
//    public FranchiseListFragment(){
//        // Required empty public constructor
//    }
//
//    @Override
//    public int getLayoutResourceId(){
//
//        return R.layout.fragment_franchise_list;
//    }
//
//    private FranchiseGroupAdapter mGroupAdapter;
//    private int mPage = 1, mTotalCount = 0;
//    private boolean mLockListView = true;
//    private LinearLayoutManager mLayoutManager;
//    private FranchiseAdapter mAdapter;
//
//    @Override
//    public void initializeView(View container){
//
//        RecyclerView recyclerView_group = (RecyclerView) container.findViewById(R.id.recycler_franchise_group);
//        recyclerView_group.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
//        recyclerView_group.addItemDecoration(new CustomDecoration(getActivity(), R.dimen.width_72));
//        mGroupAdapter = new FranchiseGroupAdapter(getActivity());
//        recyclerView_group.setAdapter(mGroupAdapter);
//
//        RecyclerView recyclerView = (RecyclerView) container.findViewById(R.id.recycler_franchise_list);
//        mLayoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.addItemDecoration(new LastItemOffsetDecoration(getActivity(), R.dimen.width_250));
//        mAdapter = new FranchiseAdapter(getActivity());
//        recyclerView.setAdapter(mAdapter);
//
//
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
//
//            int pastVisibleItems, visibleItemCount, totalItemCount;
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
//
//                super.onScrolled(recyclerView, dx, dy);
//                visibleItemCount = mLayoutManager.getChildCount();
//                totalItemCount = mLayoutManager.getItemCount();
//                pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
//                if(!mLockListView) {
//                    if((totalItemCount < mTotalCount) && (visibleItemCount + pastVisibleItems) >= (totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN)) {
//                        mPage++;
//                        getList(mGroupAdapter.getSelectGroup().getNo(), mPage);
//                    }
//                }
//            }
//        });
//
//        mGroupAdapter.setOnItemClickListener(new FranchiseGroupAdapter.OnItemClickListener(){
//
//            @Override
//            public void onItemClick(int position){
//                mGroupAdapter.setSelectGroup(position);
//                getCount(mGroupAdapter.getSelectGroup().getNo());
//            }
//        });
//
//        mAdapter.setOnItemClickListener(new FranchiseAdapter.OnItemClickListener(){
//
//            @Override
//            public void onItemClick(int position){
//                mAdapter.setSelectData(position);
//            }
//        });
//
//        ApiBuilder.create().getFranchiseGroupAll().setCallback(new PplusCallback<NewResultResponse<FranchiseGroup>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<FranchiseGroup>> call, NewResultResponse<FranchiseGroup> response){
//
//                mGroupAdapter.addAll(response.getDatas());
//                mGroupAdapter.setSelectGroup(0);
//                getCount(mGroupAdapter.getSelectGroup().getNo());
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<FranchiseGroup>> call, Throwable t, NewResultResponse<FranchiseGroup> response){
//
//            }
//        }).build().call();
//    }
//
//    @Override
//    public void init(){
//
//    }
//
//    private void getCount(final Long no){
//
//        Map<String, String> params = new HashMap<>();
//        params.put("groupNo", "" + no);
//        ApiBuilder.create().getFranchiseCount(params).setCallback(new PplusCallback<NewResultResponse<Integer>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<Integer>> call, NewResultResponse<Integer> response){
//
//                mTotalCount = response.getData();
//                mPage = 1;
//                mAdapter.clear();
//                getList(no, mPage);
//
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<Integer>> call, Throwable t, NewResultResponse<Integer> response){
//
//            }
//        }).build().call();
//    }
//
//    private void getList(Long no, int page){
//        Map<String, String> params = new HashMap<>();
//        params.put("groupNo", "" + no);
//        params.put("pg", "" + page);
//        ApiBuilder.create().getFranchiseList(params).setCallback(new PplusCallback<NewResultResponse<Franchise>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<Franchise>> call, NewResultResponse<Franchise> response){
//                mAdapter.addAll(response.getDatas());
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<Franchise>> call, Throwable t, NewResultResponse<Franchise> response){
//
//            }
//        }).build().call();
//    }
//
//    @Override
//    public void onClick(View v){
//
//        Intent intent;
//        switch (v.getId()) {
//        }
//    }
//
//    private class LastItemOffsetDecoration extends RecyclerView.ItemDecoration{
//
//        private int mLastOffset;
//
//        public LastItemOffsetDecoration(int lastOffset){
//
//            mLastOffset = lastOffset;
//        }
//
//        public LastItemOffsetDecoration(@NonNull Context context, @DimenRes int lastOffsetId){
//
//            this(context.getResources().getDimensionPixelSize(lastOffsetId));
//        }
//
//        @Override
//        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state){
//
//            super.getItemOffsets(outRect, view, parent, state);
//
//            int position = parent.getChildAdapterPosition(view);
//            if(position != 0 && position == (mTotalCount - 1)) {
//                outRect.set(0, 0, 0, mLastOffset);
//            }
//
//        }
//    }
//
//    private class CustomDecoration extends RecyclerView.ItemDecoration{
//
//        private int mItemOffset;
//
//        public CustomDecoration(int itemOffset){
//
//            mItemOffset = itemOffset;
//        }
//
//        public CustomDecoration(@NonNull Context context, @DimenRes int itemOffsetId){
//
//            this(context.getResources().getDimensionPixelSize(itemOffsetId));
//        }
//
//        @Override
//        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state){
//
//            super.getItemOffsets(outRect, view, parent, state);
//
//            int position = parent.getChildAdapterPosition(view);
//            if(position == 0) {
//                outRect.set(mItemOffset, 0, 0, 0);
//            }
//
//        }
//    }
//
//    @Override
//    public Franchise getSelectData(){
//
//        return mAdapter.getSelectData();
//    }
//}
