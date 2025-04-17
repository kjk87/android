//package com.pplus.prnumberuser.apps.bol.ui;
//
//import android.content.Intent;
//import android.os.Bundle;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import android.view.View;
//
//import com.pplus.prnumberuser.Const;
//import com.pplus.prnumberuser.R;
//import com.pplus.prnumberuser.apps.bol.data.BolGiftAdapter;
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar;
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener;
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption;
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity;
//import com.pplus.prnumberuser.apps.common.ui.custom.BottomItemOffsetDecoration;
//import com.pplus.prnumberuser.core.network.ApiBuilder;
//import com.pplus.prnumberuser.core.network.model.dto.BolGift;
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import com.pplus.networks.common.PplusCallback;
//import retrofit2.Call;
//
//public class BolGiftContainerActivity extends BaseActivity implements ImplToolbar{
//
//    @Override
//    public String getPID(){
//
//        return null;
//    }
//
//
//    @Override
//    public int getLayoutRes(){
//
//        return R.layout.activity_bol_gift_container;
//    }
//
//    private BolGiftAdapter mAdapter;
//    private LinearLayoutManager mLayoutManager;
//    private boolean mLockListView = true;
//    private int mTotalCount = 0, mPage = 0;
//
//    @Override
//    public void initializeView(Bundle savedInstanceState){
//
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_gift_container);
//        mAdapter = new BolGiftAdapter(this);
//        mLayoutManager = new LinearLayoutManager(this);
//        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setAdapter(mAdapter);
//        recyclerView.addItemDecoration(new BottomItemOffsetDecoration(this, R.dimen.height_54));
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
//        mAdapter.setOnClickListener(new BolGiftAdapter.OnClickListener(){
//
//            @Override
//            public void onClick(int position){
////                Intent intent = new Intent(BolGiftContainerActivity.this, BolGiftDetailActivity.class);
////                intent.putExtra(Const.DATA, mAdapter.getItem(position));
////                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                startActivityForResult(intent, Const.REQ_CASH_CHANGE);
//            }
//        });
//
//        getCount();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data){
//
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode){
//            case Const.REQ_CASH_CHANGE:
//                if(resultCode == RESULT_OK){
//                    setResult(RESULT_OK);
//                    getCount();
//                }
//                break;
//        }
//    }
//
//    private void getCount(){
//
//        ApiBuilder.create().getGiftCount().setCallback(new PplusCallback<NewResultResponse<Integer>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<Integer>> call, NewResultResponse<Integer> response){
//                mTotalCount = response.getData();
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
//        mLockListView = true;
//        Map<String, String> params = new HashMap<>();
//        params.put("pg", ""+page);
//        showProgress("");
//        ApiBuilder.create().getGiftList(params).setCallback(new PplusCallback<NewResultResponse<BolGift>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<BolGift>> call, NewResultResponse<BolGift> response){
//                hideProgress();
//                mLockListView = false;
//                mAdapter.addAll(response.getDatas());
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<BolGift>> call, Throwable t, NewResultResponse<BolGift> response){
//                mLockListView = false;
//            }
//        }).build().call();
//    }
//
//    @NonNull
//    @Override
//    public ToolbarOption getToolbarOption(){
//
//        ToolbarOption toolbarOption = new ToolbarOption(this);
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_gift_container), ToolbarOption.ToolbarMenu.LEFT);
//        return toolbarOption;
//    }
//
//    @Override
//    public OnToolbarListener getOnToolbarClickListener(){
//
//        return new OnToolbarListener(){
//
//            @Override
//            public void onClick(View v, ToolbarOption.ToolbarMenu toolbarMenu, Object tag){
//
//                switch (toolbarMenu) {
//                    case LEFT:
//                        if(tag.equals(1)) {
//                            onBackPressed();
//                        }
//                        break;
//                }
//            }
//        };
//    }
//}
