package com.pplus.prnumberbiz.apps.customer.ui;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.pple.pplus.utils.part.apps.resource.ResourceUtil;
import com.pple.pplus.utils.part.utils.StringUtils;
import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder;
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener;
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData;
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager;
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.apps.common.ui.custom.BottomItemOffsetDecoration;
import com.pplus.prnumberbiz.apps.customer.data.SelectCustomerAdapter;
import com.pplus.prnumberbiz.apps.customer.data.SelectPlusAdapter;
import com.pplus.prnumberbiz.core.network.ApiBuilder;
import com.pplus.prnumberbiz.core.network.model.dto.Customer;
import com.pplus.prnumberbiz.core.network.model.dto.Fan;
import com.pplus.prnumberbiz.core.network.model.dto.Group;
import com.pplus.prnumberbiz.core.network.model.dto.No;
import com.pplus.prnumberbiz.core.network.model.request.params.ParamsCustomerGroup;
import com.pplus.prnumberbiz.core.network.model.request.params.ParamsFanGroup;
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
import com.pplus.prnumberbiz.core.util.PplusCommonUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import network.common.PplusCallback;
import retrofit2.Call;

public class SelectCustomerConfigActivity extends BaseActivity implements ImplToolbar, View.OnClickListener{

    @Override
    public String getPID(){

        return null;
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_select_customer;
    }

    private TextView selectCount;
    private EditText edit_search;
    private View text_all, layout_notExist, text_description;
    private SelectCustomerAdapter mCustomerAdapter;
    private SelectPlusAdapter mPlusAdapter;

    private String mKey;
    private Group group;
    private Long pageNo;
    private int mPage = 1, mTotalCount = 0;
    private boolean mLockListView = true;
    private String mSearch;
    private View text_select_total;
    private LinearLayoutManager mLayoutManager;

    @Override
    public void initializeView(Bundle savedInstanceState){

        mKey = getIntent().getStringExtra(Const.KEY);

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


        text_description = findViewById(R.id.text_select_my_customer_description);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_select_customer_search);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new BottomItemOffsetDecoration(this, R.dimen.height_54));

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
                        if(mKey.equals(Const.CUSTOMER_GROUP_ADD)) {
                            getExcludeCustomerList(mPage);
                        } else if(mKey.equals(Const.CUSTOMER_GROUP_DEL)) {
                            getCustomerList(mPage);
                        } else if(mKey.equals(Const.PLUS_GROUP_ADD)) {
                            getExcludeFanList(mPage);
                        } else if(mKey.equals(Const.PLUS_GROUP_DEL)) {
                            getFanList(mPage);
                        }

                    }
                }
            }
        });

        TextView text_notExist = (TextView) findViewById(R.id.text_gift_search_customer_notExist);
        text_notExist.setText(getString(R.string.format_msg_gift_search_my_customer_notExist1, LoginInfoManager.getInstance().getUser().getPage().getName()));
        pageNo = LoginInfoManager.getInstance().getUser().getPage().getNo();
        group = getIntent().getParcelableExtra(Const.GROUP);
        findViewById(R.id.layout_gift_search_my_customer_side).setVisibility(View.GONE);

        if(mKey.equals(Const.CUSTOMER_GROUP_ADD)) {
            mCustomerAdapter = new SelectCustomerAdapter(this);
            recyclerView.setAdapter(mCustomerAdapter);
            mSearch = "";
            edit_search.setText("");
            edit_search.setHint(R.string.hint_search_customer);
            getExcludeCustomerCount();
        } else if(mKey.equals(Const.CUSTOMER_GROUP_DEL)) {
            mCustomerAdapter = new SelectCustomerAdapter(this);
            recyclerView.setAdapter(mCustomerAdapter);
            mSearch = "";
            edit_search.setText("");
            mPage = 1;
            edit_search.setHint(R.string.hint_search_customer);
            getCustomerCount();
        } else if(mKey.equals(Const.PLUS_GROUP_ADD)) {
            mPlusAdapter = new SelectPlusAdapter(this);
            recyclerView.setAdapter(mPlusAdapter);
            mSearch = "";
            edit_search.setText("");
            mPage = 1;
            edit_search.setHint(R.string.hint_input_nickname);
            getExcludeFanCount();
        } else if(mKey.equals(Const.PLUS_GROUP_DEL)) {
            mPlusAdapter = new SelectPlusAdapter(this);
            recyclerView.setAdapter(mPlusAdapter);
            mSearch = "";
            edit_search.setText("");
            mPage = 1;
            edit_search.setHint(R.string.hint_input_nickname);
            getFanCount();
        }

    }

    private void setSelectCount(){

        if(mKey.equals(Const.CUSTOMER_GROUP_ADD) || mKey.equals(Const.CUSTOMER_GROUP_DEL)) {
            selectCount.setText(PplusCommonUtil.Companion.fromHtml(getString(R.string.html_msg_gift_search_select_count, mCustomerAdapter.getSelectList().size())));
        } else {
            selectCount.setText(PplusCommonUtil.Companion.fromHtml(getString(R.string.html_msg_gift_search_select_count, mPlusAdapter.getSelectList().size())));
        }
    }

    private void getExcludeFanCount(){

        Map<String, String> params = new HashMap<>();
        params.put("groupNo", "" + group.getNo());
        params.put("pageNo", "" + pageNo);
        if(StringUtils.isNotEmpty(mSearch)) {
            params.put("search", mSearch);
        }
        showProgress("");
        ApiBuilder.create().getExcludeFanCount(params).setCallback(new PplusCallback<NewResultResponse<Integer>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Integer>> call, NewResultResponse<Integer> response){

                hideProgress();
                mTotalCount = response.getData();
                mPage = 1;
                getExcludeFanList(mPage);
            }

            @Override
            public void onFailure(Call<NewResultResponse<Integer>> call, Throwable t, NewResultResponse<Integer> response){

                hideProgress();
            }
        }).build().call();
    }

    private void getExcludeFanList(int page){

        mLockListView = true;
        Map<String, String> params = new HashMap<>();
        params.put("groupNo", "" + group.getNo());
        params.put("pageNo", "" + pageNo);
        params.put("pg", "" + page);
        if(StringUtils.isNotEmpty(mSearch)) {
            params.put("search", mSearch);
        }
        showProgress("");
        ApiBuilder.create().getExcludeFanList(params).setCallback(new PplusCallback<NewResultResponse<Fan>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Fan>> call, NewResultResponse<Fan> response){

                hideProgress();
                mLockListView = false;
                if(mPage == 1) {
                    mPlusAdapter.clear();
                    if(response.getDatas() == null || response.getDatas().size() == 0) {
                        layout_notExist.setVisibility(View.VISIBLE);
                    } else {
                        layout_notExist.setVisibility(View.GONE);
                    }
                }
                mPlusAdapter.addAll(response.getDatas());
                isTotal();
            }

            @Override
            public void onFailure(Call<NewResultResponse<Fan>> call, Throwable t, NewResultResponse<Fan> response){

                hideProgress();
            }
        }).build().call();
    }

    private void getExcludeCustomerCount(){

        Map<String, String> params = new HashMap<>();
        params.put("groupNo", "" + group.getNo());
        params.put("pageNo", "" + pageNo);
        if(StringUtils.isNotEmpty(mSearch)) {
            params.put("search", mSearch);
        }
        showProgress("");
        ApiBuilder.create().getExcludeCustomerCount(params).setCallback(new PplusCallback<NewResultResponse<Integer>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Integer>> call, NewResultResponse<Integer> response){

                hideProgress();
                mTotalCount = response.getData();
                mPage = 1;
                getExcludeCustomerList(mPage);
            }

            @Override
            public void onFailure(Call<NewResultResponse<Integer>> call, Throwable t, NewResultResponse<Integer> response){

                hideProgress();
            }
        }).build().call();
    }

    private void getExcludeCustomerList(int page){

        mLockListView = true;
        Map<String, String> params = new HashMap<>();
        params.put("groupNo", "" + group.getNo());
        params.put("pageNo", "" + pageNo);
        params.put("pg", "" + page);
        if(StringUtils.isNotEmpty(mSearch)) {
            params.put("search", mSearch);
        }
        showProgress("");
        ApiBuilder.create().getExcludeCustomerList(params).setCallback(new PplusCallback<NewResultResponse<Customer>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Customer>> call, NewResultResponse<Customer> response){

                hideProgress();
                mLockListView = false;
                if(mPage == 1) {
                    mCustomerAdapter.clear();
                    if(response.getDatas() == null || response.getDatas().size() == 0) {
                        layout_notExist.setVisibility(View.VISIBLE);
                    } else {
                        layout_notExist.setVisibility(View.GONE);
                    }
                }
                mCustomerAdapter.addAll(response.getDatas());
                isTotal();
            }

            @Override
            public void onFailure(Call<NewResultResponse<Customer>> call, Throwable t, NewResultResponse<Customer> response){

                hideProgress();
            }
        }).build().call();
    }

    private void getCustomerCount(){

        Map<String, String> params = new HashMap<>();
        params.put("no", "" + group.getNo());
        if(StringUtils.isNotEmpty(mSearch)) {
            params.put("search", mSearch);
        }
        showProgress("");
        ApiBuilder.create().getCustomerCount(params).setCallback(new PplusCallback<NewResultResponse<Integer>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Integer>> call, NewResultResponse<Integer> response){

                hideProgress();
                mTotalCount = response.getData();
                mPage = 1;
                getCustomerList(mPage);
            }

            @Override
            public void onFailure(Call<NewResultResponse<Integer>> call, Throwable t, NewResultResponse<Integer> response){

                hideProgress();
            }
        }).build().call();
    }

    private void getCustomerList(final int page){

        Map<String, String> params = new HashMap<>();
        params.put("no", "" + group.getNo());
        params.put("pg", "" + page);
        if(StringUtils.isNotEmpty(mSearch)) {
            params.put("search", mSearch);
        }
        showProgress("");
        ApiBuilder.create().getCustomerList(params).setCallback(new PplusCallback<NewResultResponse<Customer>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Customer>> call, NewResultResponse<Customer> response){

                hideProgress();
                if(page == 1) {
                    mCustomerAdapter.clear();
                    if(response.getDatas() == null || response.getDatas().size() == 0) {
                        layout_notExist.setVisibility(View.VISIBLE);
                    } else {
                        layout_notExist.setVisibility(View.GONE);
                    }
                }
                mCustomerAdapter.addAll(response.getDatas());
                isTotal();
            }

            @Override
            public void onFailure(Call<NewResultResponse<Customer>> call, Throwable t, NewResultResponse<Customer> response){

                hideProgress();
            }
        }).build().call();

    }

    private void getFanCount(){

        Map<String, String> params = new HashMap<>();
        params.put("no", "" + group.getNo());
        if(StringUtils.isNotEmpty(mSearch)) {
            params.put("search", mSearch);
        }
        showProgress("");
        ApiBuilder.create().getFanCount(params).setCallback(new PplusCallback<NewResultResponse<Integer>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Integer>> call, NewResultResponse<Integer> response){

                hideProgress();
                mTotalCount = response.getData();
                mPage = 1;
                getFanList(mPage);
            }

            @Override
            public void onFailure(Call<NewResultResponse<Integer>> call, Throwable t, NewResultResponse<Integer> response){

                hideProgress();
            }
        }).build().call();
    }

    private void getFanList(final int page){

        Map<String, String> params = new HashMap<>();
        params.put("no", "" + group.getNo());
        params.put("pg", "" + page);
        if(StringUtils.isNotEmpty(mSearch)) {
            params.put("search", mSearch);
        }
        showProgress("");
        ApiBuilder.create().getFanList(params).setCallback(new PplusCallback<NewResultResponse<Fan>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Fan>> call, NewResultResponse<Fan> response){

                hideProgress();
                if(page == 1) {
                    mPlusAdapter.clear();
                    if(response.getDatas() == null || response.getDatas().size() == 0) {
                        layout_notExist.setVisibility(View.VISIBLE);
                    } else {
                        layout_notExist.setVisibility(View.GONE);
                    }
                }
                mPlusAdapter.addAll(response.getDatas());
            }

            @Override
            public void onFailure(Call<NewResultResponse<Fan>> call, Throwable t, NewResultResponse<Fan> response){

                hideProgress();
            }
        }).build().call();

    }

    private boolean checkSelect(){

        if(mKey.equals(Const.CUSTOMER_GROUP_ADD) || mKey.equals(Const.CUSTOMER_GROUP_DEL)) {
            if(mCustomerAdapter.getItemCount() > 0) {
                boolean allCheck = true;
                for(Customer customer : mCustomerAdapter.getDataList()) {
                    if(!mCustomerAdapter.getSelectList().contains(customer)) {
                        allCheck = false;
                        break;
                    }
                }
                return allCheck;
            } else {
                return false;
            }
        } else {
            if(mPlusAdapter.getItemCount() > 0) {
                boolean allCheck = true;
                for(Fan fan : mPlusAdapter.getDataList()) {
                    if(!mPlusAdapter.getSelectList().contains(fan)) {
                        allCheck = false;
                        break;
                    }
                }
                return allCheck;
            } else {
                return false;
            }
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
        if(mKey.equals(Const.CUSTOMER_GROUP_ADD)) {
            getExcludeCustomerCount();
        } else if(mKey.equals(Const.CUSTOMER_GROUP_DEL)) {
            getCustomerCount();
        } else if(mKey.equals(Const.PLUS_GROUP_ADD)) {
            getExcludeFanCount();
        } else if(mKey.equals(Const.PLUS_GROUP_DEL)) {
            getFanCount();
        }
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
                    if(mKey.equals(Const.CUSTOMER_GROUP_ADD) || mKey.equals(Const.CUSTOMER_GROUP_DEL)) {
                        mCustomerAdapter.noneSelect();
                    } else {
                        mPlusAdapter.noneSelect();
                    }

                } else {
                    text_select_total.setSelected(true);
                    if(mKey.equals(Const.CUSTOMER_GROUP_ADD) || mKey.equals(Const.CUSTOMER_GROUP_DEL)) {
                        mCustomerAdapter.allSelect();
                    } else {
                        mPlusAdapter.allSelect();
                    }

                }
                setSelectCount();
                break;
        }
    }

    @NonNull
    @Override
    public ToolbarOption getToolbarOption(){

        ToolbarOption toolbarOption = new ToolbarOption(this);

        String key = getIntent().getStringExtra(Const.KEY);
        if(key.equals(Const.CUSTOMER_GROUP_ADD) || key.equals(Const.PLUS_GROUP_ADD)) {
            toolbarOption.initializeDefaultToolbar(getString(R.string.word_add_group_member), ToolbarOption.ToolbarMenu.LEFT);
        } else if(key.equals(Const.CUSTOMER_GROUP_DEL) || key.equals(Const.PLUS_GROUP_DEL)) {
            toolbarOption.initializeDefaultToolbar(getString(R.string.word_delete_group_member), ToolbarOption.ToolbarMenu.LEFT);
        }else{
            toolbarOption.initializeDefaultToolbar(getString(R.string.word_config_group_member), ToolbarOption.ToolbarMenu.LEFT);
        }

        TextView textRightTop = new TextView(new ContextThemeWrapper(this, R.style.buttonStyle));
        textRightTop.setText(R.string.word_complete);
        textRightTop.setClickable(true);
        textRightTop.setGravity(Gravity.CENTER);
        textRightTop.setPadding(0, 0, getResources().getDimensionPixelSize(R.dimen.width_66), 0);
        textRightTop.setTextColor(ResourceUtil.getColorStateList(this, R.color.color_579ffb));
        textRightTop.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.textSize_45pt));
        textRightTop.setSingleLine();
        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, textRightTop, 0);
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

                            if(mKey.equals(Const.CUSTOMER_GROUP_ADD) || mKey.equals(Const.CUSTOMER_GROUP_DEL)) {
                                final List<Customer> selectList = mCustomerAdapter.getSelectList();
                                if(selectList == null || selectList.size() == 0) {
                                    if(mKey.equals(Const.CUSTOMER_GROUP_ADD)) {
                                        showAlert(R.string.msg_select_add_customer);
                                    } else if(mKey.equals(Const.CUSTOMER_GROUP_DEL)) {
                                        showAlert(R.string.msg_select_delete_customer);
                                    }

                                    return;
                                }

                                String contents = null;
                                if(mKey.equals(Const.CUSTOMER_GROUP_ADD)) {
                                    contents = getString(R.string.msg_question_add_select_customer);
                                } else if(mKey.equals(Const.CUSTOMER_GROUP_DEL)) {
                                    contents = getString(R.string.msg_question_delete_select_customer);
                                }

                                AlertBuilder.Builder builder = new AlertBuilder.Builder();
                                builder.setTitle(getString(R.string.word_notice_alert));
                                builder.addContents(new AlertData.MessageData(contents, AlertBuilder.MESSAGE_TYPE.TEXT, 1));
                                builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm));
                                builder.setOnAlertResultListener(new OnAlertResultListener(){

                                    @Override
                                    public void onCancel(){

                                    }

                                    @Override
                                    public void onResult(AlertBuilder.EVENT_ALERT event_alert){
                                        switch (event_alert){
                                            case RIGHT:
                                                ParamsCustomerGroup params = new ParamsCustomerGroup();
                                                params.setGroup(new No(group.getNo()));
                                                params.setCustomerList(selectList);

                                                if(mKey.equals(Const.CUSTOMER_GROUP_ADD)) {

                                                    showProgress("");
                                                    ApiBuilder.create().addCustomerListToGroup(params).setCallback(new PplusCallback<NewResultResponse<Object>>(){

                                                        @Override
                                                        public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){

                                                            hideProgress();
                                                            showAlert(R.string.msg_added_select_customer);
                                                            setResult(RESULT_OK);
                                                            finish();
                                                        }

                                                        @Override
                                                        public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response){

                                                            hideProgress();
                                                        }
                                                    }).build().call();
                                                } else if(mKey.equals(Const.CUSTOMER_GROUP_DEL)) {

                                                    showProgress("");
                                                    ApiBuilder.create().removeCustomerListToGroup(params).setCallback(new PplusCallback<NewResultResponse<Object>>(){

                                                        @Override
                                                        public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){

                                                            hideProgress();
                                                            showAlert(R.string.msg_deleted_select_customer);
                                                            setResult(RESULT_OK);
                                                            finish();
                                                        }

                                                        @Override
                                                        public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response){

                                                            hideProgress();
                                                        }
                                                    }).build().call();
                                                }
                                                break;
                                        }
                                    }
                                }).builder().show(SelectCustomerConfigActivity.this);

                            } else {
                                final List<Fan> selectList = mPlusAdapter.getSelectList();
                                if(selectList == null || selectList.size() == 0) {
                                    if(mKey.equals(Const.PLUS_GROUP_ADD)) {
                                        showAlert(R.string.msg_select_add_customer);
                                    } else if(mKey.equals(Const.PLUS_GROUP_DEL)) {
                                        showAlert(R.string.msg_select_delete_customer);
                                    }

                                    return;
                                }

                                String contents = null;
                                if(mKey.equals(Const.PLUS_GROUP_ADD)) {
                                    contents = getString(R.string.msg_question_add_select_customer);
                                } else if(mKey.equals(Const.PLUS_GROUP_DEL)) {
                                    contents = getString(R.string.msg_question_delete_select_customer);
                                }

                                AlertBuilder.Builder builder = new AlertBuilder.Builder();
                                builder.setTitle(getString(R.string.word_notice_alert));
                                builder.addContents(new AlertData.MessageData(contents, AlertBuilder.MESSAGE_TYPE.TEXT, 1));
                                builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm));
                                builder.setOnAlertResultListener(new OnAlertResultListener(){

                                    @Override
                                    public void onCancel(){

                                    }

                                    @Override
                                    public void onResult(AlertBuilder.EVENT_ALERT event_alert){
                                        switch (event_alert){
                                            case RIGHT:

                                                ParamsFanGroup params = new ParamsFanGroup();
                                                params.setGroup(new No(group.getNo()));
                                                params.setFanList(selectList);
                                                if(mKey.equals(Const.PLUS_GROUP_ADD)) {
                                                    showProgress("");
                                                    ApiBuilder.create().addFanListToGroup(params).setCallback(new PplusCallback<NewResultResponse<Object>>(){

                                                        @Override
                                                        public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){

                                                            hideProgress();
                                                            showAlert(R.string.msg_added_select_customer);
                                                            setResult(RESULT_OK);
                                                            finish();
                                                        }

                                                        @Override
                                                        public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response){

                                                            hideProgress();
                                                        }
                                                    }).build().call();
                                                } else if(mKey.equals(Const.PLUS_GROUP_DEL)) {
                                                    showProgress("");
                                                    ApiBuilder.create().removeFanListToGroup(params).setCallback(new PplusCallback<NewResultResponse<Object>>(){

                                                        @Override
                                                        public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){

                                                            hideProgress();
                                                            showAlert(R.string.msg_deleted_select_customer);
                                                            setResult(RESULT_OK);
                                                            finish();
                                                        }

                                                        @Override
                                                        public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response){

                                                            hideProgress();
                                                        }
                                                    }).build().call();
                                                }
                                                break;
                                        }
                                    }
                                }).builder().show(SelectCustomerConfigActivity.this);
                            }

                        }
                        break;
                }
            }
        };

    }
}
