//package com.pplus.prnumberbiz.apps.mobilegift.ui;
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
//import android.widget.TextView;
//
//import com.pplus.prnumberbiz.Const;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder;
//import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment;
//import com.pplus.prnumberbiz.apps.mobilegift.data.MobileGiftAdapter;
//import com.pplus.prnumberbiz.core.code.common.EnumData;
//import com.pplus.prnumberbiz.core.network.ApiBuilder;
//import com.pplus.prnumberbiz.core.network.model.dto.MobileGift;
//import com.pplus.prnumberbiz.core.network.model.dto.MobileGiftCategory;
//import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import network.common.PplusCallback;
//import retrofit2.Call;
//
//public class MobileGiftListFragment extends BaseFragment<MobileGiftActivity>{
//
//
//    public MobileGiftListFragment(){
//        // Required empty public constructor
//    }
//
//    public static MobileGiftListFragment newInstance(MobileGiftCategory category){
//
//        MobileGiftListFragment fragment = new MobileGiftListFragment();
//        Bundle args = new Bundle();
//        args.putParcelable(Const.CATEGORY, category);
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
//    public int getLayoutResourceId(){
//
//        return R.layout.fragment_mobile_gift_list;
//    }
//
//    private MobileGiftAdapter mAdapter;
//    GridLayoutManager mLayoutManager;
//
//    private int mPage = 1, mTotalCount = 0;
//    private boolean mLockListView = true;
//
//    private TextView text_total_count, text_sort;
//    private MobileGiftCategory mCategory;
//    private EnumData.MobileGiftSortType mSort;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState){
//
//        super.onCreate(savedInstanceState);
//        if(getArguments() != null) {
//            mCategory = getArguments().getParcelable(Const.CATEGORY);
//        }
//    }
//
//    @Override
//    public void initializeView(View container){
//
//        RecyclerView recyclerView = (RecyclerView) container.findViewById(R.id.recycler_mobile_gift_list);
//        mLayoutManager = new GridLayoutManager(getActivity(), 2);
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.addItemDecoration(new ItemOffsetDecoration(getActivity(), R.dimen.height_16));
//        mAdapter = new MobileGiftAdapter(getActivity());
//        recyclerView.setAdapter(mAdapter);
//
//        text_total_count = (TextView) container.findViewById(R.id.text_mobile_gift_total_count);
//        text_total_count.setText(getString(R.string.format_total_with_count, 0));
//
//        text_sort = (TextView)container.findViewById(R.id.text_mobile_gift_sort);
//        text_sort.setOnClickListener(this);
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
//                        listCall(mPage);
//                    }
//                }
//            }
//        });
//
//        mAdapter.setOnItemClickListener(new MobileGiftAdapter.OnItemClickListener(){
//
//            @Override
//            public void onItemClick(int position){
//
//                Intent intent = new Intent(getActivity(), MobileGiftDetailActivity.class);
//                intent.putExtra(Const.DATA, mAdapter.getItem(position));
//                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//            }
//        });
//
//        mSort = EnumData.MobileGiftSortType.recent;
//        getCount();
//    }
//
//    @Override
//    public void init(){
//
//    }
//
//    @Override
//    public void onClick(View v){
//
//        super.onClick(v);
//        switch (v.getId()){
//            case R.id.text_mobile_gift_sort:
//                AlertBuilder.Builder builder = new AlertBuilder.Builder();
//                builder.setContents(getString(R.string.word_sort_recent), getString(R.string.word_sort_high_price), getString(R.string.word_sort_low_price));
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
//                                text_sort.setText(getString(R.string.word_sort_recent));
//                                mSort = EnumData.MobileGiftSortType.recent;
//                                break;
//                            case 2:
//                                text_sort.setText(getString(R.string.word_sort_high_price));
//                                mSort = EnumData.MobileGiftSortType.highPrice;
//                                break;
//                            case 3:
//                                text_sort.setText(getString(R.string.word_sort_low_price));
//                                mSort = EnumData.MobileGiftSortType.lowPrice;
//                                break;
//                        }
//                        getCount();
//                    }
//                }).builder().show(getActivity());
//                break;
//        }
//    }
//
//    private void getCount(){
//
//        Map<String, String> params = new HashMap<>();
//        if(mCategory.getNo() != null){
//            params.put("no", "" + mCategory.getNo());
//        }
//
//        ApiBuilder.create().getMobileGiftCount(params).setCallback(new PplusCallback<NewResultResponse<Integer>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<Integer>> call, NewResultResponse<Integer> response){
//                if(!isAdded()){
//                    return;
//                }
//                mTotalCount = response.getData();
//                text_total_count.setText(getString(R.string.format_total_with_count, mTotalCount));
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
//
//        Map<String, String> params = new HashMap<>();
//        if(mCategory.getNo() != null){
//            params.put("no", "" + mCategory.getNo());
//        }
//        params.put("align", mSort.name());
//        params.put("pg", "" + page);
//        mLockListView = true;
//        showProgress("");
//        ApiBuilder.create().getMobileGiftList(params).setCallback(new PplusCallback<NewResultResponse<MobileGift>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<MobileGift>> call, NewResultResponse<MobileGift> response){
//                if(!isAdded()){
//                    return;
//                }
//                mLockListView = false;
//                hideProgress();
//                mAdapter.addAll(response.getDatas());
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<MobileGift>> call, Throwable t, NewResultResponse<MobileGift> response){
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
//            this(context.getResources().getDimensionPixelSize(itemOffsetId), context.getResources().getDimensionPixelSize(R.dimen.width_8));
//        }
//
//        @Override
//        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state){
//
//            super.getItemOffsets(outRect, view, parent, state);
//
//            int position = parent.getChildAdapterPosition(view);
//            if(position < 2){
//                outRect.set(mRightOffset, mItemOffset, mRightOffset , mRightOffset);
//            }else{
//                outRect.set(mRightOffset, mRightOffset, mRightOffset , mRightOffset);
//            }
////            if(position == 0){
////
////            }else if(position == 1){
////                outRect.set(mRightOffset, mItemOffset, 0, mRightOffset);
////            }else{
////                if(position % 2 == 1) {
////                    outRect.set(mRightOffset, mRightOffset, 0, mRightOffset);
////                } else {
////                    outRect.set(0, mRightOffset, mRightOffset, mRightOffset);
////                }
////            }
//
//        }
//    }
//}
