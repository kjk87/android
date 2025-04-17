//package com.pplus.prnumberuser.apps.bol.ui;
//
//import android.content.Intent;
//import android.os.Bundle;
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
//import com.pplus.prnumberuser.apps.bol.data.SelectUserAdapter;
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar;
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener;
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption;
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity;
//import com.pplus.prnumberuser.apps.common.ui.custom.BottomItemOffsetDecoration;
//import com.pplus.prnumberuser.core.network.ApiBuilder;
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
//public class GiftSearchActivity extends BaseActivity implements ImplToolbar, View.OnClickListener{
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
//        return R.layout.activity_gift_search;
//    }
//
//    private TextView text_total_count;
//    private EditText edit_search;
//    SelectUserAdapter mAdapter;
//
//    private int mPage = 1, mTotalCount = 0;
//    private boolean mLockListView = true;
//    private String mSearch;
//    private LinearLayoutManager mLayoutManager;
//
//    @Override
//    public void initializeView(Bundle savedInstanceState){
//
//        edit_search = (EditText) findViewById(R.id.edit_gift_search_search);
//        edit_search.setSingleLine();
//        findViewById(R.id.image_gift_search_search).setOnClickListener(this);
//
//        text_total_count = (TextView) findViewById(R.id.text_gift_search_total_count);
//        text_total_count.setVisibility(View.INVISIBLE);
//
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_gift_search);
//        mAdapter = new SelectUserAdapter(this);
//        recyclerView.setAdapter(mAdapter);
//        mLayoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.addItemDecoration(new BottomItemOffsetDecoration(this, R.dimen.height_50));
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
//    }
//
//    private void getCount(){
//
//        Map<String, String> params = new HashMap<>();
//        params.put("search", mSearch);
//        ApiBuilder.create().getExistsNicknameUserCount(params).setCallback(new PplusCallback<NewResultResponse<Integer>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<Integer>> call, NewResultResponse<Integer> response){
//
//                mTotalCount = response.getData();
//                setTotalCount();
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
//        params.put("search", mSearch);
//        showProgress("");
//        mLockListView = true;
//        ApiBuilder.create().getExistsNicknameUserList(params).setCallback(new PplusCallback<NewResultResponse<User>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<User>> call, NewResultResponse<User> response){
//
//                hideProgress();
//                mLockListView = false;
//                mAdapter.addAll(response.getDatas());
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<User>> call, Throwable t, NewResultResponse<User> response){
//
//                hideProgress();
//                mLockListView = false;
//            }
//        }).build().call();
//    }
//
//    private void setTotalCount(){
//
//        text_total_count.setVisibility(View.VISIBLE);
//        text_total_count.setText(Html.fromHtml(getString(R.string.html_msg_gift_search_total_count, mTotalCount)));
//    }
//
//    @Override
//    public void onClick(View view){
//
//        switch (view.getId()) {
//            case R.id.image_gift_search_search:
//                String search = edit_search.getText().toString().trim();
//                if(StringUtils.isEmpty(search)) {
//                    showAlert(R.string.msg_input_searchWord);
//                    return;
//                }
//
//                mSearch = search;
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
//        toolbarOption.initializeDefaultToolbar(getString(R.string.msg_search), ToolbarOption.ToolbarMenu.LEFT);
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
//                                ToastUtil.show(GiftSearchActivity.this, R.string.msg_select_customer);
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
