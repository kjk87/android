package com.pplus.prnumberbiz.apps.pages.ui;

import android.content.Intent;
import androidx.annotation.NonNull;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.pple.pplus.utils.part.logs.LogUtil;
import com.pple.pplus.utils.part.utils.StringUtils;
import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.apps.pages.data.SearchAddressAdapter;
import com.pplus.prnumberbiz.core.network.ApiController;
import com.pplus.prnumberbiz.core.network.model.dto.Juso;
import com.pplus.prnumberbiz.core.network.model.dto.ResultAddress;
import com.pplus.prnumberbiz.core.network.model.dto.SearchAddress;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchAddressActivity extends BaseActivity implements ImplToolbar, View.OnClickListener{

    @Override
    public String getPID(){

        return null;
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_search_address;
    }

    private boolean mLockListView = true;
    private int mTotalCount = 0, mPage = 0;
    private String mSearch;
    private LinearLayoutManager mLayoutManager;
    private SearchAddressAdapter mAdapter;
    private EditText edit_search_address;
    private View text_search_not_exist;

    @Override
    public void initializeView(Bundle savedInstanceState){

//        if(!PreferenceUtil.getDefaultPreference(this).get(Const.GUIDE2, false)){
//            PreferenceUtil.getDefaultPreference(this).put(Const.GUIDE2, true);
//            Intent intent = new Intent(this, SignUpGuideActivity.class);
//            if(type != null && type.equals(EnumData.PageTypeCode.person)){
//                intent.putExtra(Const.KEY, Const.GUIDE_PERSON_ADDRESS);
//            }else{
//                intent.putExtra(Const.KEY, Const.GUIDE_STORE_ADDRESS);
//            }
//
//            startActivity(intent);
//        }

        edit_search_address = (EditText) findViewById(R.id.edit_search_address);
        edit_search_address.setSingleLine();
        edit_search_address.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        edit_search_address.setOnEditorActionListener(new TextView.OnEditorActionListener(){

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){

                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        mSearch = edit_search_address.getText().toString().trim();
                        if(StringUtils.isNotEmpty(mSearch)) {
                            mPage = 1;
                            search(mPage);
                        } else {
                            showAlert(R.string.msg_input_searchWord);
                        }

                        break;
                }

                return true;
            }
        });
        findViewById(R.id.image_search_address).setOnClickListener(this);
        text_search_not_exist = findViewById(R.id.text_search_not_exist);

        RecyclerView recycler = (RecyclerView) findViewById(R.id.recycler_search_address);
        mLayoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(mLayoutManager);
        mAdapter = new SearchAddressAdapter(this);
        recycler.setAdapter(mAdapter);

        recycler.addOnScrollListener(new RecyclerView.OnScrollListener(){

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
                        search(mPage);
                    }
                }
            }
        });

        mAdapter.setOnItemClickListener(new SearchAddressAdapter.OnItemClickListener(){

            @Override
            public void onItemClick(int position){

                Juso juso = mAdapter.getItem(position);
                Intent data = new Intent();
                data.putExtra(Const.ADDRESS, juso);
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

    @Override
    public void onClick(View view){

        switch (view.getId()) {
            case R.id.image_search_address:
                mSearch = edit_search_address.getText().toString().trim();
                if(StringUtils.isEmpty(mSearch)) {
                    showAlert(R.string.msg_input_searchWord);
                    return;
                }
                mPage = 1;
                search(mPage);
                break;
        }
    }

    private void search(int page){

        if(StringUtils.isEmpty(mSearch)) {
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put("pg", "" + page);
        params.put("search", mSearch);
        showProgress("");
        mLockListView = true;
        ApiController.getPRNumberApi().requestSearchAddress(params).enqueue(new Callback<ResultAddress>(){

            @Override
            public void onResponse(Call<ResultAddress> call, Response<ResultAddress> response){
                mLockListView = false;
                hideProgress();
                LogUtil.e(LOG_TAG, "result : {}", response.body().toString());
                SearchAddress address = response.body().getResults();
                mTotalCount = address.getCommon().getTotalCount();
                if(mTotalCount == 0){
                    text_search_not_exist.setVisibility(View.VISIBLE);
                }else{
                    text_search_not_exist.setVisibility(View.GONE);
                }
                if(mPage == 1) {
                    mAdapter.clear();
                }

                mAdapter.addAll(address.getJuso());
            }

            @Override
            public void onFailure(Call<ResultAddress> call, Throwable t){
                mLockListView = false;
                hideProgress();
            }
        });
    }

    @NonNull
    @Override
    public ToolbarOption getToolbarOption(){

        ToolbarOption toolbarOption = new ToolbarOption(this);
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_find_address), ToolbarOption.ToolbarMenu.RIGHT);
        return toolbarOption;
    }

    @Override
    public OnToolbarListener getOnToolbarClickListener(){

        return new OnToolbarListener(){

            @Override
            public void onClick(View v, ToolbarOption.ToolbarMenu toolbarMenu, Object tag){

                switch (toolbarMenu) {
                    case RIGHT:
                        if(tag.equals(1)) {
                            onBackPressed();
                        }
                        break;
                }
            }
        };

    }
}
