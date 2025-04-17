package com.pplus.prnumberbiz.apps.customer.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.pple.pplus.utils.part.utils.StringUtils;
import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager;
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.apps.common.ui.custom.BottomItemOffsetDecoration;
import com.pplus.prnumberbiz.apps.customer.data.GroupAdapter;
import com.pplus.prnumberbiz.apps.customer.data.SelectPlusAdapter;
import com.pplus.prnumberbiz.core.network.ApiBuilder;
import com.pplus.prnumberbiz.core.network.model.dto.Fan;
import com.pplus.prnumberbiz.core.network.model.dto.Group;
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
import com.pplus.prnumberbiz.core.util.PplusCommonUtil;
import com.pplus.prnumberbiz.core.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import network.common.PplusCallback;
import retrofit2.Call;

public class SelectPlusActivity extends BaseActivity implements ImplToolbar, View.OnClickListener{

    @Override
    public String getPID(){

        return null;
    }


    @Override
    public int getLayoutRes(){

        return R.layout.activity_select_customer;
    }

    private TextView selectCount, text_description;
    private EditText edit_search;
    private View text_all, layout_notExist;
    private SelectPlusAdapter mAdapter;

    private int mPage = 1, mTotalCount = 0;
    private boolean mLockListView = true;
    private String mSearch;
    private View text_select_total;
    private LinearLayoutManager mLayoutManager;
    private GroupAdapter mGroupAdapter;
    private Group mAllGroup;

    @Override
    public void initializeView(Bundle savedInstanceState){

        findViewById(R.id.layout_select_customer_search).setVisibility(View.GONE);
        findViewById(R.id.layout_gift_search_my_customer_side).setVisibility(View.GONE);

        edit_search = (EditText) findViewById(R.id.edit_select_customer_search);
        edit_search.setSingleLine();
        edit_search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        edit_search.setOnEditorActionListener(new TextView.OnEditorActionListener(){

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){

                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        search();
                        break;
                }

                return true;
            }
        });

        findViewById(R.id.image_select_customer_search).setOnClickListener(this);

        text_select_total = findViewById(R.id.text_gift_search_customer_select_total);
        text_select_total.setOnClickListener(this);

        text_all = findViewById(R.id.text_select_customer_all);
        text_all.setOnClickListener(this);

        layout_notExist = findViewById(R.id.layout_select_customer_notExist);
        layout_notExist.setVisibility(View.GONE);

        selectCount = (TextView) findViewById(R.id.text_select_my_customer_select_count);


        text_description = (TextView)findViewById(R.id.text_select_my_customer_description);
        text_description.setVisibility(View.VISIBLE);
        text_description.setText(R.string.msg_select_plus_description);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_select_customer_search);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new BottomItemOffsetDecoration(this, R.dimen.height_54));
        mAdapter = new SelectPlusAdapter(this);
        ArrayList<Fan> userList = getIntent().getParcelableArrayListExtra(Const.CUSTOMER);
        if(userList != null && userList.size() > 0){
            mAdapter.setSelectList(userList);
        }
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){

            int pastVisibleItems, visibleItemCount, totalItemCount;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){

                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = mLayoutManager.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
                if(!mLockListView) {
                    if((totalItemCount < mTotalCount) && (visibleItemCount + pastVisibleItems) >= (totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN)) {
                        mPage++;
                        getPlusList(mGroupAdapter.getSelectGroup().getNo(), mPage);

                    }
                }
            }
        });

        RecyclerView recyclerGroup = (RecyclerView) findViewById(R.id.recycler_select_customer_group);
        recyclerGroup.setLayoutManager(new LinearLayoutManager(this));
        mGroupAdapter = new GroupAdapter(this);
        recyclerGroup.setAdapter(mGroupAdapter);

        mGroupAdapter.setOnItemClickListener(new GroupAdapter.OnItemClickListener(){

            @Override
            public void onItemClick(int position){

                mGroupAdapter.setSelectGroup(mGroupAdapter.getItem(position));
                mTotalCount = mGroupAdapter.getItem(position).getCount();
                mSearch = "";
                edit_search.setText("");
                mPage = 1;
                text_all.setSelected(false);
                getPlusList(mGroupAdapter.getSelectGroup().getNo(), mPage);
            }
        });

        TextView text_notExist = (TextView)findViewById(R.id.text_gift_search_customer_notExist);
        text_notExist.setText(getString(R.string.format_msg_gift_search_my_customer_notExist1, LoginInfoManager.getInstance().getUser().getPage().getName()));
        mSearch = "";
        edit_search.setText("");
        mPage = 1;
        getGroupAll();

    }

    private void setSelectCount(){
        selectCount.setText(PplusCommonUtil.Companion.fromHtml(getString(R.string.html_msg_gift_search_select_count, mAdapter.getSelectList().size())));
    }

    private void getPlusList(Long no, final int page){

        Map<String, String> params = new HashMap<>();
        if(no != null){
            params.put("no", "" + no);
        }

        params.put("pg", "" + page);
        if(StringUtils.isNotEmpty(mSearch)) {
            params.put("search", mSearch);
        }
        mLockListView = true;
        showProgress("");
        ApiBuilder.create().getFanList(params).setCallback(new PplusCallback<NewResultResponse<Fan>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Fan>> call, NewResultResponse<Fan> response){
                hideProgress();
                mLockListView = false;
                if(page == 1) {
                    mAdapter.clear();
                    if(response.getDatas() == null || response.getDatas().size() == 0) {
                        layout_notExist.setVisibility(View.VISIBLE);
                    } else {
                        layout_notExist.setVisibility(View.GONE);
                    }
                }
                mAdapter.addAll(response.getDatas());
                isTotal();
            }

            @Override
            public void onFailure(Call<NewResultResponse<Fan>> call, Throwable t, NewResultResponse<Fan> response){
                hideProgress();
                mLockListView = false;
            }
        }).build().call();

    }

    public void getGroupAll(){

        Map<String, String> params = new HashMap<>();
        params.put("no", "" + LoginInfoManager.getInstance().getUser().getPage().getNo());

        ApiBuilder.create().getFanGroupAll(params).setCallback(new PplusCallback<NewResultResponse<Group>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Group>> call, NewResultResponse<Group> response){

                List<Group> groupList = response.getDatas();
                if(groupList != null && groupList.size() > 0){

                    for(int i = 0; i < groupList.size(); i++){
                        if(groupList.get(i).isDefaultGroup()){
                            layout_notExist.setVisibility(View.GONE);
                            mAllGroup = groupList.get(i);
                            mTotalCount = mAllGroup.getCount();
                            text_all.setSelected(true);
                            mGroupAdapter.setSelectGroup(mAllGroup);
                            groupList.remove(i);
                            mSearch = "";
                            listCall();
                            break;
                        }
                    }

                    mGroupAdapter.setDataList(groupList);
                }else{
                    mGroupAdapter.clear();
                    mAdapter.clear();
                    layout_notExist.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<NewResultResponse<Group>> call, Throwable t, NewResultResponse<Group> response){

            }
        }).build().call();
    }

    private void getCount(Long no){

        Map<String, String> params = new HashMap<>();
        params.put("no", "" + no);
        if(StringUtils.isNotEmpty(mSearch)) {
            params.put("search", mSearch);
        }

        ApiBuilder.create().getFanCount(params).setCallback(new PplusCallback<NewResultResponse<Integer>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Integer>> call, NewResultResponse<Integer> response){

                mTotalCount = response.getData();
                listCall();
            }

            @Override
            public void onFailure(Call<NewResultResponse<Integer>> call, Throwable t, NewResultResponse<Integer> response){

            }
        }).build().call();
    }

    private void listCall(){
        mPage = 1;
        edit_search.setText("");
        mAdapter.clear();
        getPlusList(mGroupAdapter.getSelectGroup().getNo(), mPage);
    }

    private boolean checkSelect(){


        if(mAdapter.getItemCount() > 0){
            boolean allCheck = true;
            for(Fan fan : mAdapter.getDataList()) {
                if(!mAdapter.getSelectList().contains(fan)) {
                    allCheck = false;
                    break;
                }
            }
            return allCheck;
        }else{
            return false;
        }

    }

    public void isTotal(){

        if(checkSelect()) {
            text_select_total.setSelected(true);
        } else {
            text_select_total.setSelected(false);
        }
        setSelectCount();
    }

    private void search(){
        mSearch = edit_search.getText().toString().trim();
        if(StringUtils.isEmpty(mSearch)) {
            showAlert(R.string.msg_input_searchWord);
            return;
        }
        getCount(mGroupAdapter.getSelectGroup().getNo());
    }

    @Override
    public void onClick(View view){

        switch (view.getId()) {
            case R.id.image_select_customer_search:

                search();
                break;
            case R.id.text_gift_search_customer_select_total:
                if(checkSelect()) {
                    text_select_total.setSelected(false);
                    mAdapter.noneSelect();
                } else {
                    text_select_total.setSelected(true);
                    mAdapter.allSelect();
                }
                setSelectCount();
                break;
            case R.id.text_select_customer_all:
                if(mAllGroup != null){
                    mTotalCount = mGroupAdapter.getSelectGroup().getCount();
                    mGroupAdapter.setSelectGroup(mAllGroup);
                    mSearch = "";
                    listCall();
                    text_all.setSelected(true);
                }
                break;
        }
    }

    @NonNull
    @Override
    public ToolbarOption getToolbarOption(){

        ToolbarOption toolbarOption = new ToolbarOption(this);
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_select_customer), ToolbarOption.ToolbarMenu.LEFT);
        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, getString(R.string.word_complete));
        return toolbarOption;
    }

    @Override
    public OnToolbarListener getOnToolbarClickListener(){

        return new OnToolbarListener(){

            @Override
            public void onClick(View v, ToolbarOption.ToolbarMenu toolbarMenu, Object tag){

                switch (toolbarMenu) {
                    case LEFT:
                        if(tag.equals(1)) {
                            onBackPressed();
                        }
                        break;
                    case RIGHT:
                        if(tag.equals(1)) {
                            ArrayList<Fan> selectList = mAdapter.getSelectList();
                            if(selectList == null || selectList.size() == 0) {
                                ToastUtil.show(SelectPlusActivity.this, R.string.msg_select_customer);
                                return;
                            }

                            Intent intent = new Intent();
                            intent.putParcelableArrayListExtra(Const.CUSTOMER, selectList);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                        break;
                }
            }
        };

    }
}
