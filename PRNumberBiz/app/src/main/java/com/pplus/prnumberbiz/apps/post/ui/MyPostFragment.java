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
//import android.view.View;
//import android.widget.TextView;
//
//import com.pple.pplus.utils.part.utils.StringUtils;
//import com.pplus.prnumberbiz.Const;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder;
//import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener;
//import com.pplus.prnumberbiz.apps.common.component.RecyclerScaleScrollListener;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment;
//import com.pplus.prnumberbiz.apps.post.data.MyPostAdapter;
//import com.pplus.prnumberbiz.core.code.common.EnumData;
//import com.pplus.prnumberbiz.core.network.ApiBuilder;
//import com.pplus.prnumberbiz.core.network.model.dto.Post;
//import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
//import com.pplus.prnumberbiz.core.util.PplusCommonUtil;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import network.common.PplusCallback;
//import retrofit2.Call;
//
//public class MyPostFragment extends BaseFragment{
//
//
//    public MyPostFragment(){
//        // Required empty public constructor
//    }
//
//    public static MyPostFragment newInstance(EnumData.AdsStatus sortType){
//
//        MyPostFragment fragment = new MyPostFragment();
//        Bundle args = new Bundle();
//        args.putSerializable(Const.SORT, sortType);
//        //        args.putBoolean(Const.WRITE, isWrite);
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
//    private MyPostAdapter mAdapter;
//
//    private int mPage, mTotalCount = 0;
//    private LinearLayoutManager mLayoutManager;
//    private boolean mLockListView = true;
//    private View layout_post_write, layout_not_exist;
//    private TextView text_total_count, text_sort;
//    private EnumData.AdsStatus mSortType;
//    private String mFilter;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState){
//
//        super.onCreate(savedInstanceState);
//        if(getArguments() != null) {
//            mSortType = (EnumData.AdsStatus) getArguments().getSerializable(Const.SORT);
//        }
//    }
//
//    @Override
//    public void initializeView(View container){
//
//        text_total_count = (TextView) container.findViewById(R.id.text_post_total_count);
//        RecyclerView recycler = (RecyclerView) container.findViewById(R.id.recycler_post);
//        mLayoutManager = new LinearLayoutManager(getActivity());
//        recycler.setLayoutManager(mLayoutManager);
//        mAdapter = new MyPostAdapter(getActivity(), this);
//        recycler.setAdapter(mAdapter);
//        recycler.addItemDecoration(new CustomItemOffsetDecoration(getActivity(), R.dimen.height_60, R.dimen.height_60, R.dimen.height_200));
//        layout_not_exist = container.findViewById(R.id.layout_post_list_not_exist);
//        mAdapter.setOnItemClickListener(new MyPostAdapter.OnItemClickListener(){
//
//            @Override
//            public void onItemClick(int position){
//
//                Intent intent = new Intent(getActivity(), PostWithAdsDetailActivity.class);
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
//                        listCall(mPage);
//                    }
//                }
//            }
//        });
//
//        text_sort = (TextView) container.findViewById(R.id.text_post_sort);
//        text_sort.setOnClickListener(this);
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
//    public void deleteCount(){
//        mTotalCount--;
//        text_total_count.setText(PplusCommonUtil.Companion.fromHtml(getString(R.string.html_post_total_count, mTotalCount)));
//    }
//
//    @Override
//    public void init(){
//
//    }
//
//    private void setSort(){
//
//        mFilter = mSortType.name();
//        switch (mSortType) {
//            case total:
//                text_sort.setText(R.string.word_total);
//                mFilter = "";
//                break;
//            case ing:
//                text_sort.setText(R.string.word_ads_ing);
//                break;
//            case ready:
//                text_sort.setText(R.string.word_ads_waiting);
//                break;
//            case stop:
//                text_sort.setText(R.string.word_ads_stop);
//                break;
//            case finish:
//            case complete:
//                text_sort.setText(R.string.word_ads_close);
//                mFilter = EnumData.AdsStatus.finish + "," + EnumData.AdsStatus.complete;
//                break;
//        }
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
//        if(StringUtils.isNotEmpty(mFilter)) {
//            params.put("filter", mFilter);
//        }
//
//        ApiBuilder.create().getAdvertiseMyPostCount(params).setCallback(new PplusCallback<NewResultResponse<Integer>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<Integer>> call, NewResultResponse<Integer> response){
//
//                if(!isAdded()) {
//                    return;
//                }
//
//                mTotalCount = response.getData();
//                text_total_count.setText(PplusCommonUtil.Companion.fromHtml(getString(R.string.html_post_total_count, mTotalCount)));
//
//                if(mTotalCount == 0) {
//                    layout_not_exist.setVisibility(View.VISIBLE);
//                } else {
//                    layout_not_exist.setVisibility(View.GONE);
//                }
//
//                mPage = 1;
//                mAdapter.clear();
//                listCall(mPage);
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
//
//        if(StringUtils.isNotEmpty(mFilter)) {
//            params.put("filter", mFilter);
//        }
//        params.put("pg", "" + page);
//
//        showProgress("");
//        ApiBuilder.create().getAdvertiseMyPostList(params).setCallback(new PplusCallback<NewResultResponse<Post>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<Post>> call, NewResultResponse<Post> response){
//
//                if(!isAdded()) {
//                    return;
//                }
//                mLockListView = false;
//                hideProgress();
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
//            case R.id.text_post_sort:
//                AlertBuilder.Builder builder = new AlertBuilder.Builder();
//                builder.setContents(getResources().getStringArray(R.array.ads_sort));
//                builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(new OnAlertResultListener(){
//
//                    @Override
//                    public void onCancel(){
//
//                    }
//
//                    @Override
//                    public void onResult(AlertBuilder.EVENT_ALERT event_alert){
//
//                        switch (event_alert.getValue()) {
//                            case 1:
//                                mSortType = EnumData.AdsStatus.total;
//                                break;
//                            case 2:
//                                mSortType = EnumData.AdsStatus.ing;
//                                break;
//                            case 3:
//                                mSortType = EnumData.AdsStatus.ready;
//                                break;
//                            case 4:
//                                mSortType = EnumData.AdsStatus.stop;
//                                break;
//                            case 5:
//                                mSortType = EnumData.AdsStatus.complete;
//                                break;
//                        }
//                        setSort();
//                        getCount();
//                    }
//                }).builder().show(getActivity());
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
//                case Const.REQ_ADS_SETTING:
//                    getCount();
//                    break;
//            }
//        }
//    }
//}
