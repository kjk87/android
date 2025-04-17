//package com.pplus.prnumberuser.apps.post.ui;
//
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Rect;
//import android.os.Bundle;
//import androidx.annotation.DimenRes;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import android.view.View;
//import android.widget.TextView;
//
//import com.pplus.utils.part.format.FormatUtil;
//import com.pplus.prnumberuser.Const;
//import com.pplus.prnumberuser.R;
//import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment;
//import com.pplus.prnumberuser.apps.post.data.ReviewAdapter;
//import com.pplus.prnumberuser.core.network.ApiBuilder;
//import com.pplus.prnumberuser.core.network.model.dto.Page;
//import com.pplus.prnumberuser.core.network.model.dto.Post;
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import com.pplus.networks.common.PplusCallback;
//import retrofit2.Call;
//
//public class ReviewListFragment extends BaseFragment{
//
//
//    public ReviewListFragment(){
//        // Required empty public constructor
//    }
//
//    public static ReviewListFragment newInstance(Page page){
//
//        ReviewListFragment fragment = new ReviewListFragment();
//        Bundle args = new Bundle();
//        args.putParcelable(Const.PAGE, page);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public int getLayoutResourceId(){
//
//        return R.layout.fragment_review_list;
//    }
//
//    private ReviewAdapter mAdapter;
//    private View layout_not_exist;
//    private TextView text_total_count;
//
//    private LinearLayoutManager mLayoutManager;
//    private int mPaging, mTotalCount = 0;
//    private boolean mLockListView = true;
//    private Page mPage;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState){
//
//        super.onCreate(savedInstanceState);
//        if(getArguments() != null) {
//            mPage = getArguments().getParcelable(Const.PAGE);
//        }
//    }
//
//    @Override
//    public void init(){
//
//    }
//
//    @Override
//    public void initializeView(View container){
//
//        layout_not_exist = container.findViewById(R.id.layout_review_list_not_exist);
//        text_total_count = (TextView) container.findViewById(R.id.text_review_total_count);
//
//        layout_not_exist = container.findViewById(R.id.layout_review_list_not_exist);
//
//        text_total_count = (TextView) container.findViewById(R.id.text_review_total_count);
//
//        RecyclerView recyclerView = (RecyclerView) container.findViewById(R.id.recycler_review);
//        mLayoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(mLayoutManager);
//        mAdapter = new ReviewAdapter(getActivity());
//        recyclerView.setAdapter(mAdapter);
//
//        recyclerView.addItemDecoration(new CustomItemOffsetDecoration(getActivity(), R.dimen.height_60, R.dimen.height_60, R.dimen.height_200));
//
//        mAdapter.setOnItemClickListener(new ReviewAdapter.OnItemClickListener(){
//
//            @Override
//            public void onItemClick(int position){
//
//                Intent intent = new Intent(getActivity(), ReviewDetailActivity.class);
//                intent.putExtra(Const.DATA, mAdapter.getItem(position));
//                getActivity().startActivityForResult(intent, Const.REQ_DETAIL);
//            }
//        });
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
//                        mPaging++;
//                        listCall(mPaging);
//                    }
//                }
//            }
//        });
//
//        getCount();
//    }
//
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
//            if(position == mTotalCount - 1) {
//                outRect.bottom = mLastOffset;
//            }
//
//        }
//    }
//
//    private void getCount(){
//
//        Map<String, String> params = new HashMap<>();
//        params.put("boardNo", "" + mPage.getReviewBoard().getNo());
//        ApiBuilder.create().getBoardPostCount(params).setCallback(new PplusCallback<NewResultResponse<Integer>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<Integer>> call, NewResultResponse<Integer> response){
//
//                mTotalCount = response.getData();
//                text_total_count.setText(getString(R.string.format_review_count, FormatUtil.getMoneyType(String.valueOf(mTotalCount))));
//                mPaging = 1;
//                listCall(mPaging);
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<Integer>> call, Throwable t, NewResultResponse<Integer> response){
//
//            }
//        }).build().call();
//    }
//
//    private void listCall(int page){
//
//        mLockListView = true;
//        Map<String, String> params = new HashMap<>();
//        params.put("boardNo", "" + mPage.getReviewBoard().getNo());
//        params.put("pg", "" + page);
//        params.put("sz", "" + Const.LOAD_DATA_LIMIT_CNT);
//
//        showProgress("");
//        ApiBuilder.create().getBoardPostList(params).setCallback(new PplusCallback<NewResultResponse<Post>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<Post>> call, NewResultResponse<Post> response){
//
//                mLockListView = false;
//                hideProgress();
//                if(mPaging == 1) {
//                    mAdapter.clear();
//                    if(response.getDatas().size() == 0) {
//                        layout_not_exist.setVisibility(View.VISIBLE);
//                    } else {
//                        layout_not_exist.setVisibility(View.GONE);
//                    }
//                }
//
//                mAdapter.addAll(response.getDatas());
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<Post>> call, Throwable t, NewResultResponse<Post> response){
//
//                mLockListView = false;
//                hideProgress();
//            }
//        }).build().call();
//    }
//
//    @Override
//    public void onClick(View v){
//
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data){
//
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case Const.REQ_DETAIL:
//            case Const.REQ_MODIFY:
//            case Const.REQ_POST:
//                if(resultCode == Activity.RESULT_OK) {
//                    getCount();
//                }
//                break;
//        }
//    }
//
//    @Override
//    public String getPID(){
//
//        return null;
//    }
//
//
//}
