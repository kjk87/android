package com.pplus.prnumberbiz.apps.post.ui;


import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment;
import com.pplus.prnumberbiz.apps.common.ui.custom.BottomItemOffsetDecoration;
import com.pplus.prnumberbiz.apps.post.data.ReviewAdapter;
import com.pplus.prnumberbiz.core.network.ApiBuilder;
import com.pplus.prnumberbiz.core.network.model.dto.Post;
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;

import java.util.HashMap;
import java.util.Map;

import network.common.PplusCallback;
import retrofit2.Call;

public class ReviewFragment extends BaseFragment<ReviewActivity>{


    public ReviewFragment(){
        // Required empty public constructor
    }

    public static ReviewFragment newInstance(){

        ReviewFragment fragment = new ReviewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutResourceId(){

        return R.layout.fragment_review;
    }

    private ReviewAdapter mAdapter;
    private View layout_not_exist;
    private TextView text_total_count;

    private LinearLayoutManager mLayoutManager;
    private int mPage, mTotalCount = 0;
    private boolean mLockListView = true;

    @Override
    public void initializeView(View container){

        //        GradeBar gradeBar = (GradeBar) container.findViewById(R.id.gradebar_review);
        //        Page page = LoginInfoManager.getInstance().getUser().getPage();
        layout_not_exist = container.findViewById(R.id.layout_review_list_notExist);

        text_total_count = (TextView) container.findViewById(R.id.text_review_total_count);

        RecyclerView recyclerView = (RecyclerView) container.findViewById(R.id.recycler_review);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ReviewAdapter(getActivity());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addItemDecoration(new BottomItemOffsetDecoration(getActivity(), R.dimen.height_60));

        mAdapter.setOnItemClickListener(new ReviewAdapter.OnItemClickListener(){

            @Override
            public void onItemClick(int position){

                Intent intent = new Intent(getActivity(), ReviewDetailActivity.class);
                intent.putExtra(Const.POST_NO, mAdapter.getItem(position).getNo());
                startActivity(intent);
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
                        getPostList(mPage);
                    }
                }
            }
        });

        getCount();
    }

    @Override
    public void init(){

    }

    //    private class CustomItemOffsetDecoration extends RecyclerView.ItemDecoration{
    //
    //        private int mItemOffset, mTopOffset, mLastOffset;
    //
    //        public CustomItemOffsetDecoration(int itemOffset, int topOffset, int lastOffset){
    //
    //            mItemOffset = itemOffset;
    //            mTopOffset = topOffset;
    //            mLastOffset = lastOffset;
    //        }
    //
    //        public CustomItemOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId, @DimenRes int topOffsetId, @DimenRes int lastOffsetId){
    //
    //            this(context.getResources().getDimensionPixelSize(itemOffsetId), context.getResources().getDimensionPixelSize(topOffsetId), context.getResources().getDimensionPixelSize(lastOffsetId));
    //        }
    //
    //        @Override
    //        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state){
    //
    //            super.getItemOffsets(outRect, view, parent, state);
    //
    //            int position = parent.getChildAdapterPosition(view);
    //            if(position == 0) {
    //                outRect.set(0, mTopOffset, 0, mItemOffset);
    //            } else {
    //                outRect.set(0, 0, 0, mItemOffset);
    //            }
    //            if(position == mTotalCount-1){
    //                outRect.bottom = mLastOffset;
    //            }
    //
    //        }
    //    }

    private void getCount(){

        Map<String, String> params = new HashMap<>();
        params.put("boardNo", "" + LoginInfoManager.getInstance().getUser().getPage().getReviewBoard().getNo());
        ApiBuilder.create().getBoardPostCount(params).setCallback(new PplusCallback<NewResultResponse<Integer>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Integer>> call, NewResultResponse<Integer> response){

                if(!isAdded()) {
                    return;
                }

                mTotalCount = response.getData();
                text_total_count.setText(getString(R.string.format_word_review_post_with_count, mTotalCount));

                mPage = 1;
                getPostList(mPage);
            }

            @Override
            public void onFailure(Call<NewResultResponse<Integer>> call, Throwable t, NewResultResponse<Integer> response){

            }
        }).build().call();
    }

    private void getPostList(int page){

        mLockListView = true;
        Map<String, String> params = new HashMap<>();
        params.put("boardNo", "" + LoginInfoManager.getInstance().getUser().getPage().getReviewBoard().getNo());
        params.put("pg", "" + page);

        showProgress("");
        ApiBuilder.create().getBoardPostList(params).setCallback(new PplusCallback<NewResultResponse<Post>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Post>> call, NewResultResponse<Post> response){

                mLockListView = false;
                hideProgress();
                if(!isAdded()) {
                    return;
                }

                if(mPage == 1) {
                    mAdapter.clear();
                    if(response.getDatas().size() == 0) {
                        layout_not_exist.setVisibility(View.VISIBLE);
                    } else {
                        layout_not_exist.setVisibility(View.GONE);
                    }
                }

                mAdapter.addAll(response.getDatas());
            }

            @Override
            public void onFailure(Call<NewResultResponse<Post>> call, Throwable t, NewResultResponse<Post> response){

                mLockListView = false;
                hideProgress();
            }
        }).build().call();
    }

    @Override
    public void onClick(View v){

        switch (v.getId()) {

        }
    }

    @Override
    public String getPID(){

        return null;
    }

}
