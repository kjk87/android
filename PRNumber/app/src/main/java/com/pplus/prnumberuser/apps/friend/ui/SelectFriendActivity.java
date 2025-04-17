//package com.pplus.prnumberuser.apps.friend.ui;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Rect;
//import android.os.Bundle;
//import androidx.annotation.DimenRes;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import android.text.Html;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import com.pplus.utils.part.utils.StringUtils;
//import com.pplus.prnumberuser.Const;
//import com.pplus.prnumberuser.R;
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar;
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener;
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption;
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity;
//import com.pplus.prnumberuser.apps.friend.data.SelectFriendAdapter;
//import com.pplus.prnumberuser.core.network.ApiBuilder;
//import com.pplus.prnumberuser.core.network.model.dto.Friend;
//import com.pplus.prnumberuser.core.network.model.dto.User;
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse;
//import com.pplus.prnumberuser.core.util.ToastUtil;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//import com.pplus.networks.common.PplusCallback;
//import retrofit2.Call;
//
//public class SelectFriendActivity extends BaseActivity implements ImplToolbar, View.OnClickListener{
//
//    @Override
//    public String getPID(){
//
//        return null;
//    }
//
//    @Override
//    public int getLayoutView(){
//
//        return R.layout.activity_select_friend;
//    }
//
//    private TextView text_select_count, text_total_count;
//    private EditText edit_search;
//    private View layout_notExist;
//    private SelectFriendAdapter mAdapter;
//
//    private int mPage = 1, mTotalCount = 0;
//    private boolean mLockListView = true;
//    private String mSearch;
//    private LinearLayoutManager mLayoutManager;
//
//    private ArrayList<User> mUserList;
//
//    @Override
//    public void initializeView(Bundle savedInstanceState){
//
//        mUserList = getIntent().getParcelableArrayListExtra(Const.DATA);
//
//        text_total_count = (TextView)findViewById(R.id.text_select_friend_total);
//        text_select_count = (TextView)findViewById(R.id.text_select_friend_count);
//
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_select_friend);
//        mAdapter = new SelectFriendAdapter(this);
//        mLayoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setAdapter(mAdapter);
//        recyclerView.addItemDecoration(new CustomItemOffsetDecoration(this, R.dimen.height_54, R.dimen.height_60));
//
//        if(mUserList != null && mUserList.size() > 0){
//            mAdapter.setSelectList(mUserList);
//        }
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
//        mAdapter.setOnItemClickListener(new SelectFriendAdapter.OnItemClickListener(){
//
//            @Override
//            public void onItemClick(int position){
//                setSelectCount();
//            }
//        });
//
//        getCount();
//    }
//
//    private void setSelectCount(){
//        text_select_count.setText(Html.fromHtml(getString(R.string.html_msg_receiver_select_count, mAdapter.getSelectList().size())));
//    }
//
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
//        ApiBuilder.create().getExistsNicknameFriendCount().setCallback(new PplusCallback<NewResultResponse<Integer>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<Integer>> call, NewResultResponse<Integer> response){
//
//                mTotalCount = response.getData();
//                text_total_count.setText(getString(R.string.format_select_friend_total_count, mTotalCount));
//                mAdapter.clear();
//                mPage = 1;
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
//        Map<String, String> params = new HashMap<>();
//        params.put("pg", "" + page);
//        showProgress("");
//        mLockListView = true;
//        ApiBuilder.create().getExistsNicknameFriendList(params).setCallback(new PplusCallback<NewResultResponse<Friend>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<Friend>> call, NewResultResponse<Friend> response){
//
//                hideProgress();
//                mLockListView = false;
//                mAdapter.addAll(response.getDatas());
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<Friend>> call, Throwable t, NewResultResponse<Friend> response){
//                hideProgress();
//                mLockListView = false;
//            }
//        }).build().call();
//    }
//
//    @Override
//    public void onClick(View view){
//
//        switch (view.getId()) {
//            case R.id.image_select_customer_search:
//
//                mSearch = edit_search.getText().toString().trim();
//                if(StringUtils.isEmpty(mSearch)) {
//                    showAlert(R.string.msg_input_searchWord);
//                    return;
//                }
//                getCount();
//                break;
//        }
//    }
//
//    @NonNull
//    @Override
//    public ToolbarOption getToolbarOption(){
//
//        ToolbarOption toolbarOption = new ToolbarOption(this);
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_my_1freind), ToolbarOption.ToolbarMenu.LEFT);
//        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, getString(R.string.word_complete));
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
//                    case RIGHT:
//                        if(tag.equals(1)) {
//                            ArrayList<User> selectList = mAdapter.getSelectList();
//                            if(selectList == null || selectList.size() == 0) {
//                                ToastUtil.show(SelectFriendActivity.this, R.string.msg_select_customer);
//                                return;
//                            }
//
//                            Intent intent = new Intent();
//                            intent.putParcelableArrayListExtra(Const.DATA, selectList);
//                            setResult(RESULT_OK, intent);
//                            finish();
//                        }
//                        break;
//                }
//            }
//        };
//
//    }
//}
