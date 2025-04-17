//package com.pplus.prnumberbiz.apps.bol.ui;
//
//import android.content.Intent;
//import android.support.annotation.NonNull;
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.inputmethod.EditorInfo;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import com.pple.pplus.utils.part.utils.StringUtils;
//import com.pplus.prnumberbiz.Const;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.bol.data.SelectUserAdapter;
//import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
//import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
//import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
//import com.pplus.prnumberbiz.apps.common.ui.custom.BottomItemOffsetDecoration;
//import com.pplus.prnumberbiz.core.network.ApiBuilder;
//import com.pplus.prnumberbiz.core.network.model.dto.User;
//import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
//import com.pplus.prnumberbiz.core.util.PplusCommonUtil;
//import com.pplus.prnumberbiz.core.util.ToastUtil;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//import network.common.PplusCallback;
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
//    public int getLayoutRes(){
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
//    private ArrayList<User> mUserList;
//
//    @Override
//    public void initializeView(Bundle savedInstanceState){
//
//        mUserList = getIntent().getParcelableArrayListExtra(Const.DATA);
//
//        edit_search = (EditText) findViewById(R.id.edit_gift_search_search);
//        edit_search.setSingleLine();
//        edit_search.setHint(R.string.hint_input_nickname);
//        edit_search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
//        edit_search.setOnEditorActionListener(new TextView.OnEditorActionListener(){
//
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
//
//                switch (actionId) {
//                    case EditorInfo.IME_ACTION_SEARCH:
//                        search();
//                        break;
//                }
//
//                return true;
//            }
//        });
//
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
//        text_total_count.setText(PplusCommonUtil.Companion.fromHtml(getString(R.string.html_msg_gift_search_total_count, mTotalCount)));
//    }
//
//    @Override
//    public void onClick(View view){
//
//        switch (view.getId()) {
//            case R.id.image_gift_search_search:
//                search();
//                break;
//        }
//    }
//
//    private void search(){
//        String search = edit_search.getText().toString().trim();
//        if(StringUtils.isEmpty(search)) {
//            showAlert(R.string.msg_input_searchWord);
//            return;
//        }
//
//        mSearch = search;
//        getCount();
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
