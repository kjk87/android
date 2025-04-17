//package com.pplus.prnumberbiz.apps.post.ui;
//
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Rect;
//import android.os.Bundle;
//import android.support.annotation.DimenRes;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.Html;
//import android.view.View;
//import android.widget.TextView;
//
//import com.pplus.prnumberbiz.Const;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.common.component.RecyclerScaleScrollListener;
//import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment;
//import com.pplus.prnumberbiz.apps.post.data.PostAdapter;
//import com.pplus.prnumberbiz.core.code.common.EnumData;
//import com.pplus.prnumberbiz.core.network.ApiBuilder;
//import com.pplus.prnumberbiz.core.network.model.dto.Post;
//import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import network.common.PplusCallback;
//import retrofit2.Call;
//
//public class PostListFragment extends BaseFragment{
//
//
//    public PostListFragment(){
//        // Required empty public constructor
//    }
//
//    public static PostListFragment newInstance(EnumData.AdsStatus sortType){
//
//        PostListFragment fragment = new PostListFragment();
//        Bundle args = new Bundle();
//        args.putSerializable(Const.SORT, sortType);
////        args.putBoolean(Const.WRITE, isWrite);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public int getLayoutResourceId(){
//
//        return R.layout.fragment_post_list;
//    }
//
//    private PostAdapter mAdapter;
//
//    private int mPage, mTotalCount = 0;
//    private LinearLayoutManager mLayoutManager;
//    private boolean mLockListView = true;
//    private View layout_post_write, layout_not_exist;
//    private TextView text_total_count, text_sort;
//    private EnumData.PostSortType mSortType;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState){
//
//        super.onCreate(savedInstanceState);
//        if(getArguments() != null){
//            mSortType = (EnumData.PostSortType)getArguments().getSerializable(Const.SORT);
////            mIsWrite = getArguments().getBoolean(Const.WRITE);
//        }
//    }
//
//    @Override
//    public void initializeView(View container){
//
//        text_total_count = (TextView)container.findViewById(R.id.text_post_total_count);
//        RecyclerView recycler = (RecyclerView) container.findViewById(R.id.recycler_post);
//        mLayoutManager = new LinearLayoutManager(getActivity());
//        recycler.setLayoutManager(mLayoutManager);
//        mAdapter = new PostAdapter(getActivity(), EnumData.PostType.pr);
//        recycler.setAdapter(mAdapter);
//        recycler.addItemDecoration(new CustomItemOffsetDecoration(getActivity(), R.dimen.height_120, R.dimen.height_100));
//        layout_not_exist = container.findViewById(R.id.layout_post_list_notExist);
//        mAdapter.setOnItemClickListener(new PostAdapter.OnItemClickListener(){
//
//            @Override
//            public void onItemClick(int position){
//
//                Intent intent = new Intent(getActivity(), PostDetailActivity.class);
//                intent.putExtra(Const.KEY, EnumData.PostType.pr);
//                intent.putExtra(Const.POST_NO, mAdapter.getItem(position).getNo());
//                getActivity().startActivityForResult(intent, Const.REQ_POST);
//            }
//        });
//
//        recycler.addOnScrollListener(new RecyclerView.OnScrollListener(){
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
//                        getPostList(mPage);
//                    }
//                }
//            }
//        });
//
//        text_sort = (TextView)container.findViewById(R.id.text_post_sort);
//        layout_post_write = container.findViewById(R.id.layout_post_write);
//
//        layout_post_write.setVisibility(View.VISIBLE);
//        layout_post_write.setOnClickListener(this);
//        recycler.addOnScrollListener(new RecyclerScaleScrollListener(layout_post_write));
//
//        setSort();
//        getCount();
//    }
//
//    @Override
//    public void init(){
//
//    }
//
//    private void setSort(){
//        switch (mSortType){
//            case total:
//                text_sort.setText(R.string.word_total);
//                break;
//            case ing:
//                text_sort.setText(R.string.word_ads_ing);
//                break;
//            case waiting:
//                text_sort.setText(R.string.word_ads_waiting);
//                break;
//            case stop:
//                text_sort.setText(R.string.word_ads_stop);
//                break;
//            case close:
//                text_sort.setText(R.string.word_ads_close);
//                break;
//            case normal:
//                text_sort.setText(R.string.word_ads_normal);
//                break;
//        }
//    }
//
//    private class CustomItemOffsetDecoration extends RecyclerView.ItemDecoration{
//
//        private int mItemOffset, mTopOffset;
//
//        public CustomItemOffsetDecoration(int itemOffset, int topOffset){
//
//            mItemOffset = itemOffset;
//            mTopOffset = topOffset;
//        }
//
//        public CustomItemOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId, @DimenRes int topOffsetId){
//
//            this(context.getResources().getDimensionPixelSize(itemOffsetId), context.getResources().getDimensionPixelSize(topOffsetId));
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
//
//        }
//    }
//
//    private void getCount(){
//        Map<String, String> params = new HashMap<>();
//        params.put("boardNo", "" + LoginInfoManager.getInstance().getUser().getPage().getPrBoard().getNo());
//        ApiBuilder.create().getPostCount(params).setCallback(new PplusCallback<NewResultResponse<Integer>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<Integer>> call, NewResultResponse<Integer> response){
//                mTotalCount = response.getData();
//                text_total_count.setText(Html.fromHtml(getString(R.string.html_post_total_count, mTotalCount)));
//                mPage = 1;
//                getPostList(mPage);
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<Integer>> call, Throwable t, NewResultResponse<Integer> response){
//
//            }
//        }).build().call();
//    }
//
//    private void getPostList(int page){
//
//        mLockListView = true;
//        Map<String, String> params = new HashMap<>();
//        params.put("boardNo", "" + LoginInfoManager.getInstance().getUser().getPage().getPrBoard().getNo());
//        params.put("pg", "" + page);
//        params.put("sz", "" + Const.LOAD_DATA_LIMIT_CNT);
//
//        showProgress("");
//        ApiBuilder.create().getPostList(params).setCallback(new PplusCallback<NewResultResponse<Post>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<Post>> call, NewResultResponse<Post> response){
//
//                mLockListView = false;
//                hideProgress();
//                if(mPage == 1) {
//                    mAdapter.clear();
//                    if(response.getDatas().size() == 0){
//                        layout_not_exist.setVisibility(View.VISIBLE);
//                    }else{
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
//        switch (v.getId()) {
//            case R.id.layout_post_write:
//                Intent intent = new Intent(getActivity(), PostWriteActivity.class);
//                intent.putExtra(Const.MODE, EnumData.MODE.WRITE);
//                getActivity().startActivityForResult(intent, Const.REQ_POST);
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
//    @Override
//    public String getSID(){
//
//        return null;
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data){
//
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode == Activity.RESULT_OK) {
//            switch (requestCode) {
//                case Const.REQ_POST:
//                    getCount();
//                    break;
//            }
//        }
//    }
//}
