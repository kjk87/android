//package com.pplus.prnumberbiz.apps.product;
//
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Rect;
//import android.os.Bundle;
//import android.support.annotation.DimenRes;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//
//import com.pplus.prnumberbiz.Const;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment;
//import com.pplus.prnumberbiz.apps.post.ui.PostActivity;
//import com.pplus.prnumberbiz.apps.product.data.ProductAdapter;
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
//public class ProductListFragment extends BaseFragment<PostActivity>{
//
//
//    public ProductListFragment(){
//        // Required empty public constructor
//    }
//
//    public static ProductListFragment newInstance(Long no){
//
//        ProductListFragment fragment = new ProductListFragment();
//        Bundle args = new Bundle();
//        args.putLong(Const.NO, no);
//        fragment.setArguments(args);
//        return fragment;
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
//    public int getLayoutResourceId(){
//
//        return R.layout.fragment_product_list;
//    }
//
//    private View layout_empty;
//    private ProductAdapter mAdapter;
//    GridLayoutManager mLayoutManager;
//
//    private Long mPageNo;
//    private int mPage;
//    private boolean mLockListView = true, mIsLast = false;
//
//    @Override
//    public void initializeView(View container){
//
//        layout_empty = container.findViewById(R.id.layout_product_config_empty);
//        layout_empty.setVisibility(View.GONE);
//
//        mPageNo = LoginInfoManager.getInstance().getUser().getPage().getNo();
//        RecyclerView recyclerView = (RecyclerView)container.findViewById(R.id.recycler_product_config);
//        mLayoutManager = new GridLayoutManager(getActivity(), 2);
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.addItemDecoration(new ItemOffsetDecoration(getActivity(), R.dimen.height_150));
//        mAdapter = new ProductAdapter(getActivity());
//        recyclerView.setAdapter(mAdapter);
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
//                    if(!mIsLast && (visibleItemCount + pastVisibleItems) >= (totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN)) {
//                        mPage++;
//                        getPostList(mPage);
//                    }
//                }
//            }
//        });
//
//        mAdapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener(){
//
//            @Override
//            public void onItemClick(int position){
//                Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
//                intent.putExtra(Const.KEY, EnumData.PostType.pr);
//                intent.putExtra(Const.POST_NO, mAdapter.getItem(position).getNo());
//                startActivityForResult(intent, Const.REQ_POST);
//            }
//        });
//
//        mPage = 1;
//        mIsLast = false;
//        getPostList(mPage);
//    }
//
//    @Override
//    public void init(){
//
//    }
//
//    private void getPostList(int page){
//
//        mLockListView = true;
//        Map<String, String> params = new HashMap<>();
//        params.put("no", "" + mPageNo);
//        params.put("filter", EnumData.PostType.pr.name());
//        params.put("pg", "" + page);
//        params.put("sz", "" + Const.LOAD_DATA_LIMIT_CNT);
//
//        showProgress("");
//        ApiBuilder.create().getBoardPostList(params).setCallback(new PplusCallback<NewResultResponse<Post>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<Post>> call, NewResultResponse<Post> getPostList){
//
//                mLockListView = false;
//                hideProgress();
//                if(mPage == 1) {
//                    mAdapter.clear();
//                    if(getPostList.getDatas().size() == 0){
//                        layout_empty.setVisibility(View.VISIBLE);
//                    }else{
//                        layout_empty.setVisibility(View.GONE);
//                    }
//                }
//
//                mAdapter.addAll(getPostList.getDatas());
//                if(getPostList.getDatas() == null || getPostList.getDatas().size() == 0) {
//                    mIsLast = true;
//                    if(mPage > 1){
//                        mPage--;
//                    }
//                }
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
//    private class ItemOffsetDecoration extends RecyclerView.ItemDecoration{
//
//        private int mItemOffset, mRightOffset;
//
//        public ItemOffsetDecoration(int itemOffset, int rightOffset){
//
//            mItemOffset = itemOffset;
//            mRightOffset = rightOffset;
//        }
//
//        public ItemOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId){
//
//            this(context.getResources().getDimensionPixelSize(itemOffsetId), context.getResources().getDimensionPixelSize(R.dimen.width_36));
//        }
//
//        @Override
//        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state){
//
//            super.getItemOffsets(outRect, view, parent, state);
//
//            int position = parent.getChildAdapterPosition(view);
//
//            if(position % 2 == 1){
//                outRect.set(0, 0, 0, mItemOffset);
//            }else{
//                outRect.set(0, 0, mRightOffset, mItemOffset);
//            }
//
//        }
//    }
//}
