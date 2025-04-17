//package com.pplus.prnumberuser.apps.plus.ui;
//
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
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager;
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar;
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener;
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption;
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity;
//import com.pplus.prnumberuser.apps.common.ui.custom.BottomItemOffsetDecoration;
//import com.pplus.prnumberuser.apps.plus.data.SelectPlusAdapter;
//import com.pplus.prnumberuser.core.network.ApiBuilder;
//import com.pplus.prnumberuser.core.network.model.dto.Group;
//import com.pplus.prnumberuser.core.network.model.dto.No;
//import com.pplus.prnumberuser.core.network.model.dto.Plus;
//import com.pplus.prnumberuser.core.network.model.request.params.ParamsPlusGroup;
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import com.pplus.networks.common.PplusCallback;
//import retrofit2.Call;
//
//public class SelectPlusConfigActivity extends BaseActivity implements ImplToolbar, View.OnClickListener{
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
//        return R.layout.activity_select_customer;
//    }
//
//    private TextView selectCount;
//    private EditText edit_search;
//    private View text_all, layout_notExist, text_description;
//    private SelectPlusAdapter mPlusAdapter;
//
//    private String mKey;
//    private Group group;
//    private Long pageNo;
//    private int mPage = 1, mTotalCount = 0;
//    private boolean mLockListView = true;
//    private String mSearch;
//    private View text_select_total;
//    private LinearLayoutManager mLayoutManager;
//
//    @Override
//    public void initializeView(Bundle savedInstanceState){
//
//        mKey = getIntent().getStringExtra(Const.KEY);
//
//        edit_search = (EditText) findViewById(R.id.edit_select_customer_search);
//        findViewById(R.id.image_select_customer_search).setOnClickListener(this);
//        text_select_total = findViewById(R.id.text_gift_search_customer_select_total);
//        text_select_total.setOnClickListener(this);
//
//        text_all = findViewById(R.id.text_select_customer_all);
//        text_all.setOnClickListener(this);
//
//        layout_notExist = findViewById(R.id.layout_select_customer_notExist);
//        layout_notExist.setVisibility(View.GONE);
//
//        selectCount = (TextView) findViewById(R.id.text_select_my_customer_select_count);
//
//
//        text_description = findViewById(R.id.text_select_my_customer_description);
//
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_select_customer_search);
//        mLayoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(mLayoutManager);
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
//                        if(mKey.equals(Const.PLUS_GROUP_ADD)) {
//                            getExcludePlusList(mPage);
//                        } else if(mKey.equals(Const.PLUS_GROUP_DEL)) {
//                            getPlusList(mPage);
//                        }
//
//                    }
//                }
//            }
//        });
//
//        TextView text_notExist = (TextView) findViewById(R.id.text_gift_search_customer_notExist);
//        text_notExist.setText(getString(R.string.format_msg_gift_search_my_customer_notExist1, LoginInfoManager.getInstance().getUser().getPage().getName()));
//        pageNo = LoginInfoManager.getInstance().getUser().getPage().getNo();
//        group = getIntent().getParcelableExtra(Const.GROUP);
//        findViewById(R.id.layout_gift_search_my_customer_side).setVisibility(View.GONE);
//
//        mPlusAdapter = new SelectPlusAdapter(this);
//        recyclerView.setAdapter(mPlusAdapter);
//        mSearch = "";
//        edit_search.setText("");
//        mPage = 1;
//
//        if(mKey.equals(Const.PLUS_GROUP_ADD)) {
//            getExcludePlusCount();
//        } else if(mKey.equals(Const.PLUS_GROUP_DEL)) {
//            getPlusCount();
//        }
//
//    }
//
//    private void setSelectCount(){
//
//        selectCount.setText(Html.fromHtml(getString(R.string.html_msg_gift_search_select_count, mPlusAdapter.getSelectList().size())));
//    }
//
//    private void getExcludePlusCount(){
//
//        Map<String, String> params = new HashMap<>();
//        params.put("groupNo", "" + group.getNo());
//        params.put("pageNo", "" + pageNo);
//        if(StringUtils.isNotEmpty(mSearch)) {
//            params.put("search", mSearch);
//        }
//        showProgress("");
//        ApiBuilder.create().getExcludePlusCount(params).setCallback(new PplusCallback<NewResultResponse<Integer>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<Integer>> call, NewResultResponse<Integer> response){
//
//                hideProgress();
//                mTotalCount = response.getData();
//                mPage = 1;
//                getExcludePlusList(mPage);
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
//    private void getExcludePlusList(int page){
//
//        mLockListView = true;
//        Map<String, String> params = new HashMap<>();
//        params.put("groupNo", "" + group.getNo());
//        params.put("pageNo", "" + pageNo);
//        params.put("pg", "" + page);
//        if(StringUtils.isNotEmpty(mSearch)) {
//            params.put("search", mSearch);
//        }
//        showProgress("");
//        ApiBuilder.create().getExcludePlusList(params).setCallback(new PplusCallback<NewResultResponse<Plus>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<Plus>> call, NewResultResponse<Plus> response){
//                hideProgress();
//                mLockListView = false;
//                if(mPage == 1) {
//                    mPlusAdapter.clear();
//                    if(response.getDatas() == null || response.getDatas().size() == 0) {
//                        layout_notExist.setVisibility(View.VISIBLE);
//                    } else {
//                        layout_notExist.setVisibility(View.GONE);
//                    }
//                }
//                mPlusAdapter.addAll(response.getDatas());
//                isTotal();
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<Plus>> call, Throwable t, NewResultResponse<Plus> response){
//                hideProgress();
//            }
//        }).build().call();
//    }
//
//    private void getPlusCount(){
//
//        Map<String, String> params = new HashMap<>();
//        params.put("no", "" + group.getNo());
//        if(StringUtils.isNotEmpty(mSearch)) {
//            params.put("search", mSearch);
//        }
//        showProgress("");
//        ApiBuilder.create().getPlusCount(params).setCallback(new PplusCallback<NewResultResponse<Integer>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<Integer>> call, NewResultResponse<Integer> response){
//
//                hideProgress();
//                mTotalCount = response.getData();
//                mPage = 1;
//                getPlusList(mPage);
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
//    private void getPlusList(final int page){
//
//        Map<String, String> params = new HashMap<>();
//        params.put("no", "" + group.getNo());
//        params.put("pg", "" + page);
//        if(StringUtils.isNotEmpty(mSearch)) {
//            params.put("search", mSearch);
//        }
//        showProgress("");
//        ApiBuilder.create().getPlusList(params).setCallback(new PplusCallback<NewResultResponse<Plus>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<Plus>> call, NewResultResponse<Plus> response){
//                hideProgress();
//                if(page == 1) {
//                    mPlusAdapter.clear();
//                    if(response.getDatas() == null || response.getDatas().size() == 0) {
//                        layout_notExist.setVisibility(View.VISIBLE);
//                    } else {
//                        layout_notExist.setVisibility(View.GONE);
//                    }
//                }
//                mPlusAdapter.addAll(response.getDatas());
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<Plus>> call, Throwable t, NewResultResponse<Plus> response){
//                hideProgress();
//            }
//        }).build().call();
//
//    }
//
//    private boolean checkSelect(){
//
//        if(mPlusAdapter.getItemCount() > 0) {
//            boolean allCheck = true;
//            for(Plus fan : mPlusAdapter.getDataList()) {
//                if(!mPlusAdapter.getSelectList().contains(fan)) {
//                    allCheck = false;
//                    break;
//                }
//            }
//            return allCheck;
//        } else {
//            return false;
//        }
//
//    }
//
//    public void isTotal(){
//
//        if(checkSelect()) {
//            text_select_total.setSelected(true);
//        } else {
//            text_select_total.setSelected(false);
//        }
//        setSelectCount();
//    }
//
//    @Override
//    public void onClick(View view){
//
//        switch (view.getId()) {
//            case R.id.image_select_customer_search:
//                mSearch = edit_search.getText().toString().trim();
//                if(mKey.equals(Const.PLUS_GROUP_ADD)) {
//                    getExcludePlusCount();
//                } else if(mKey.equals(Const.PLUS_GROUP_DEL)) {
//                    getPlusCount();
//                }
//
//                break;
//            case R.id.text_gift_search_customer_select_total:
//                if(checkSelect()) {
//                    text_select_total.setSelected(false);
//                    mPlusAdapter.noneSelect();
//                } else {
//                    text_select_total.setSelected(true);
//                    mPlusAdapter.allSelect();
//
//                }
//                setSelectCount();
//                break;
//        }
//    }
//
//    @NonNull
//    @Override
//    public ToolbarOption getToolbarOption(){
//
//        ToolbarOption toolbarOption = new ToolbarOption(this);
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_select_pr_shop), ToolbarOption.ToolbarMenu.LEFT);
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
//
//                            List<Plus> selectList = mPlusAdapter.getSelectList();
//                            if(selectList == null || selectList.size() == 0) {
//                                if(mKey.equals(Const.PLUS_GROUP_ADD)) {
//                                    showAlert(R.string.msg_select_add_customer);
//                                } else if(mKey.equals(Const.PLUS_GROUP_DEL)) {
//                                    showAlert(R.string.msg_select_delete_customer);
//                                }
//
//                                return;
//                            }
//
//                            ParamsPlusGroup params = new ParamsPlusGroup();
//                            params.setGroup(new No(group.getNo()));
//                            params.setPlusList(selectList);
//                            if(mKey.equals(Const.PLUS_GROUP_ADD)) {
//                                showProgress("");
//                                ApiBuilder.create().addPlusListToGroup(params).setCallback(new PplusCallback<NewResultResponse<Object>>(){
//
//                                    @Override
//                                    public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){
//
//                                        hideProgress();
//                                        showAlert(R.string.msg_added_select_follow);
//                                        setResult(RESULT_OK);
//                                        finish();
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response){
//
//                                        hideProgress();
//                                    }
//                                }).build().call();
//                            } else if(mKey.equals(Const.PLUS_GROUP_DEL)) {
//                                showProgress("");
//                                ApiBuilder.create().removePlusListToGroup(params).setCallback(new PplusCallback<NewResultResponse<Object>>(){
//
//                                    @Override
//                                    public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){
//
//                                        hideProgress();
//                                        showAlert(R.string.msg_deleted_select_follow);
//                                        setResult(RESULT_OK);
//                                        finish();
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response){
//
//                                        hideProgress();
//                                    }
//                                }).build().call();
//                            }
//
//
//                        }
//                        break;
//                }
//            }
//        };
//
//    }
//}
