//package com.pplus.prnumberbiz.apps.cash.ui;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.widget.TextView;
//
//import com.pplus.prnumberbiz.Const;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.cash.data.PayPendingAdapter;
//import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager;
//import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
//import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
//import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
//import com.pplus.prnumberbiz.apps.common.ui.custom.BottomItemOffsetDecoration;
//import com.pplus.prnumberbiz.core.network.ApiBuilder;
//import com.pplus.prnumberbiz.core.network.model.dto.Payment;
//import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
//
//import network.common.PplusCallback;
//import retrofit2.Call;
//
//public class PayPendingListActivity extends BaseActivity implements ImplToolbar{
//
//    @Override
//    public String getPID(){
//
//        return null;
//    }
//
//    @Override
//    public int getLayoutRes(){
//
//        return R.layout.activity_pay_pending_list;
//    }
//
//    private PayPendingAdapter mAdapter;
//
//    @Override
//    public void initializeView(Bundle savedInstanceState){
//
//        TextView text_description = (TextView)findViewById(R.id.text_pay_pending_description);
//        text_description.setText(getString(R.string.format_msg_cash_pending_description, LoginInfoManager.getInstance().getUser().getPage().getName()));
//
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_pay_pending);
//        mAdapter = new PayPendingAdapter(this);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(mAdapter);
//        recyclerView.addItemDecoration(new BottomItemOffsetDecoration(this, R.dimen.height_54));
//
//        mAdapter.setOnClickListener(new PayPendingAdapter.OnClickListener(){
//
//            @Override
//            public void onClick(int position){
//
//                Intent intent = new Intent(PayPendingListActivity.this, PayPendingDetailActivity.class);
//                intent.putExtra(Const.DATA, mAdapter.getItem(position));
//                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//            }
//        });
//
//        getPayPendingList();
//    }
//
//    private void getPayPendingList(){
//
//        ApiBuilder.create().getPendingApprovalAll().setCallback(new PplusCallback<NewResultResponse<Payment>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<Payment>> call, NewResultResponse<Payment> response){
//
//                mAdapter.setDataList(response.getDatas());
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<Payment>> call, Throwable t, NewResultResponse<Payment> response){
//
//            }
//        }).build().call();
//    }
//
//    @NonNull
//    @Override
//    public ToolbarOption getToolbarOption(){
//
//        ToolbarOption toolbarOption = new ToolbarOption(this);
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_cash_pending_history), ToolbarOption.ToolbarMenu.LEFT);
//
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
