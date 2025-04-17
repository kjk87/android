//package com.pplus.prnumberbiz.apps.shop.ui;
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
//import com.pple.pplus.utils.part.utils.StringUtils;
//import com.pplus.prnumberbiz.Const;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder;
//import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener;
//import com.pplus.prnumberbiz.apps.common.component.RecyclerScaleScrollListener;
//import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment;
//import com.pplus.prnumberbiz.apps.coupon.data.CouponAdapter;
//import com.pplus.prnumberbiz.apps.coupon.ui.CouponDetailActivity;
//import com.pplus.prnumberbiz.apps.coupon.ui.MakeCouponActivity;
//import com.pplus.prnumberbiz.core.code.common.EnumData;
//import com.pplus.prnumberbiz.core.network.ApiBuilder;
//import com.pplus.prnumberbiz.core.network.model.dto.CouponTemplate;
//import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
//import com.pplus.prnumberbiz.core.util.PplusCommonUtil;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import network.common.PplusCallback;
//import retrofit2.Call;
//
//public class ShopCouponFragment extends BaseFragment{
//
//
//    public ShopCouponFragment(){
//        // Required empty public constructor
//    }
//
//    public static ShopCouponFragment newInstance(){
//
//        ShopCouponFragment fragment = new ShopCouponFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public int getLayoutResourceId(){
//
//        return R.layout.fragment_shop_coupon;
//    }
//
//    private TextView text_total_count, text_sort;
//    private CouponAdapter mAdapter;
//    private View layout_notExist, layout_write;
//    private LinearLayoutManager mLayoutManager;
//
//    private boolean mLockListView = true;
//    private int mTotalCount = 0, mPage = 0;
//    private String mStatus;
//
//    @Override
//    public void initializeView(View container){
//
//        text_total_count = (TextView) container.findViewById(R.id.text_shop_coupon_totalCount);
//
//        RecyclerView recyclerView = (RecyclerView) container.findViewById(R.id.recycler_shop_coupon);
//        recyclerView.addItemDecoration(new CustomItemOffsetDecoration(getActivity(), R.dimen.height_50, R.dimen.height_60));
//        mLayoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(mLayoutManager);
//        mAdapter = new CouponAdapter(getActivity());
//        recyclerView.setAdapter(mAdapter);
//        mAdapter.setOnItemClickListener(new CouponAdapter.OnItemClickListener(){
//
//            @Override
//            public void onItemClick(int position){
//
//                Intent intent = new Intent(getActivity(), CouponDetailActivity.class);
//                intent.putExtra(Const.COUPON, mAdapter.getItem(position));
//                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                getActivity().startActivityForResult(intent, Const.REQ_COUPON_CHANGE);
//            }
//        });
//        layout_notExist = container.findViewById(R.id.layout_shop_coupon_notExist);
//        layout_notExist.setVisibility(View.GONE);
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
//        text_sort = (TextView) container.findViewById(R.id.text_shop_coupon_sort);
//        text_sort.setOnClickListener(this);
//
//        layout_write = container.findViewById(R.id.layout_shop_coupon_write);
//        layout_write.setOnClickListener(this);
//        recyclerView.addOnScrollListener(new RecyclerScaleScrollListener(layout_write));
//        mStatus = "";
//        getCount();
//    }
//
//    @Override
//    public void init(){
//
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
//
//        Map<String, String> params = new HashMap<>();
//        params.put("pageNo", "" + LoginInfoManager.getInstance().getUser().getPage().getNo());
//        if(StringUtils.isNotEmpty(mStatus)){
//            params.put("status", mStatus);
//        }
//        showProgress("");
//        mLockListView = true;
//        ApiBuilder.create().getCouponTemplateCount(params).setCallback(new PplusCallback<NewResultResponse<Integer>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<Integer>> call, NewResultResponse<Integer> response){
//
//                hideProgress();
//                mLockListView = false;
//                mTotalCount = response.getData();
//                text_total_count.setText(PplusCommonUtil.fromHtml(getString(R.string.html_coupon_total_count, response.getData())));
//                if(mTotalCount == 0) {
//                    layout_notExist.setVisibility(View.VISIBLE);
//                } else {
//                    layout_notExist.setVisibility(View.GONE);
//                }
//                mPage = 1;
//                mAdapter.clear();
//                listCall(mPage);
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<Integer>> call, Throwable t, NewResultResponse<Integer> response){
//
//                hideProgress();
//            }
//        }).build().call();
//    }
//
//
//    private void listCall(final int page){
//
//        mLockListView = true;
//        showProgress("");
//        Map<String, String> params = new HashMap<>();
//        params.put("pageNo", "" + LoginInfoManager.getInstance().getUser().getPage().getNo());
//        params.put("pg", "" + page);
//        if(StringUtils.isNotEmpty(mStatus)){
//            params.put("status", mStatus);
//        }
//        params.put("sz", "" + Const.LOAD_DATA_LIMIT_CNT);
//
//        ApiBuilder.create().getCouponTemplateList(params).setCallback(new PplusCallback<NewResultResponse<CouponTemplate>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<CouponTemplate>> call, NewResultResponse<CouponTemplate> response){
//
//                hideProgress();
//                mLockListView = false;
//                if(page == 1) {
//                    mAdapter.clear();
//                }
//
//                mAdapter.addAll(response.getDatas());
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<CouponTemplate>> call, Throwable t, NewResultResponse<CouponTemplate> response){
//
//                hideProgress();
//                mLockListView = false;
//            }
//        }).build().call();
//
//    }
//
//    @Override
//    public void onClick(View v){
//
//        switch (v.getId()) {
//
//            case R.id.layout_shop_coupon_write:
//                Intent intent = new Intent(getActivity(), MakeCouponActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                getActivity().startActivityForResult(intent, Const.REQ_COUPON_CHANGE);
//                break;
//            case R.id.text_shop_coupon_sort:
//                AlertBuilder.Builder builder = new AlertBuilder.Builder();
//                builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER);
//                builder.setContents(new String[]{getString(R.string.word_total), getString(R.string.word_use_enable), getString(R.string.word_term_expire)});
//                builder.setLeftText(getString(R.string.word_cancel));
//                builder.setOnAlertResultListener(new OnAlertResultListener(){
//
//                    @Override
//                    public void onCancel(){
//
//                    }
//
//                    @Override
//                    public void onResult(AlertBuilder.EVENT_ALERT event_alert){
//
//                        switch (event_alert) {
//                            case LIST:
//                                switch (event_alert.getValue()){
//                                    case 1:
//                                        mStatus = "";
//                                        text_sort.setText(R.string.word_total);
//                                        break;
//                                    case 2:
//                                        mStatus = EnumData.CouponStatus.active.name();
//                                        text_sort.setText(R.string.word_use_enable);
//                                        break;
//                                    case 3:
//                                        mStatus = EnumData.CouponStatus.expired.name();
//                                        text_sort.setText(R.string.word_term_expire);
//                                        break;
//                                }
//
//                                getCount();
//                                break;
//                        }
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
//                case Const.REQ_COUPON_CHANGE:
//                    getCount();
//                    break;
//            }
//        }
//    }
//}
