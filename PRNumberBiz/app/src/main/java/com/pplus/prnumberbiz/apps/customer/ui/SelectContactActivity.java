package com.pplus.prnumberbiz.apps.customer.ui;

import android.os.Bundle;
import androidx.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.pple.pplus.utils.part.utils.StringUtils;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager;
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.apps.customer.data.SelectContactAdapter;
import com.pplus.prnumberbiz.core.database.DBManager;
import com.pplus.prnumberbiz.core.database.dao.ContactDao;
import com.pplus.prnumberbiz.core.database.entity.Contact;
import com.pplus.prnumberbiz.core.network.ApiBuilder;
import com.pplus.prnumberbiz.core.network.model.dto.Customer;
import com.pplus.prnumberbiz.core.network.model.dto.No;
import com.pplus.prnumberbiz.core.network.model.request.params.ParamsCustomerList;
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
import com.pplus.prnumberbiz.core.util.PplusCommonUtil;
import com.pplus.prnumberbiz.core.util.SoundSearcher;

import java.util.ArrayList;
import java.util.List;

import network.common.PplusCallback;
import retrofit2.Call;

public class SelectContactActivity extends BaseActivity implements ImplToolbar, View.OnClickListener{

    @Override
    public String getPID(){

        return null;
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_select_contact;
    }

    private TextView selectCount;
    private EditText edit_search;
    private View layout_notExist, text_select_total;
    private LinearLayout layout_group;
    private SelectContactAdapter mAdapter;
    private List<Contact> mContactList;
    private String mSearchWord;

    @Override
    public void initializeView(Bundle savedInstanceState){

        edit_search = (EditText) findViewById(R.id.edit_select_contact_search);
        findViewById(R.id.image_select_contact_search).setOnClickListener(this);

        selectCount = (TextView) findViewById(R.id.text_select_contact_select_count);

        text_select_total = findViewById(R.id.text_select_contact_total);
        text_select_total.setOnClickListener(this);

        ListView list = (ListView) findViewById(R.id.list_select_contact);
        mAdapter = new SelectContactAdapter(this);
        list.setAdapter(mAdapter);

        ContactDao contactDao = DBManager.getInstance(this).getSession().getContactDao();
        mContactList = contactDao.queryBuilder().where(ContactDao.Properties.Delete.eq(false)).orderAsc(ContactDao.Properties.MemberName).list();

        mAdapter.addAll(mContactList);

        edit_search.setSingleLine();
        edit_search.setImeOptions(EditorInfo.IME_ACTION_DONE);
        edit_search.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){

            }

            @Override
            public void afterTextChanged(Editable editable){

                if(StringUtils.isNotEmpty(mSearchWord) && mSearchWord.equals(editable.toString())) {
                    return;
                }

                mSearchWord = editable.toString();
                setData();
            }
        });
    }

    private void setData(){
        mAdapter.clear();
        for(Contact contact : mContactList){
            if(StringUtils.isNotEmpty(mSearchWord) && !SoundSearcher.matchString(contact.getMemberName(), mSearchWord)) {
                continue;
            }
            mAdapter.add(contact);
        }
    }

    @Override
    public void onClick(View view){

        switch (view.getId()) {
            case R.id.image_select_contact_search:
                break;
            case R.id.text_select_contact_total:

                if(isTotal()){
                    text_select_total.setSelected(false);
                    mAdapter.setSelectList(new ArrayList<Contact>());
                }else{
                    text_select_total.setSelected(true);
                    mAdapter.setSelectList(mContactList);
                }
                selectCount.setText(PplusCommonUtil.Companion.fromHtml(getString(R.string.html_msg_gift_search_select_count, mAdapter.getSelectList().size())));
                break;
        }
    }

    public void checkTotal(){
        text_select_total.setSelected(isTotal());
        selectCount.setText(PplusCommonUtil.Companion.fromHtml(getString(R.string.html_msg_gift_search_select_count, mAdapter.getSelectList().size())));
    }

    private boolean isTotal(){

        if(mContactList.size() == mAdapter.getSelectList().size()) {
            for(Contact contact : mContactList) {
                if(!mAdapter.getSelectList().contains(contact)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @NonNull
    @Override
    public ToolbarOption getToolbarOption(){

        ToolbarOption toolbarOption = new ToolbarOption(this);
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_select_contact_customer), ToolbarOption.ToolbarMenu.LEFT);
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
                            if(mAdapter.getSelectList() == null || mAdapter.getSelectList().size() == 0 ){
                                showAlert(R.string.msg_select_add_customer);
                                return;
                            }
                            insertCustomer();
                        }
                        break;
                }
            }
        };

    }

    private void insertCustomer(){
        ParamsCustomerList params = new ParamsCustomerList();
        params.setPage(new No(LoginInfoManager.getInstance().getUser().getPage().getNo()));

        List<Customer> customerList = new ArrayList<>();
        for(int i = 0; i < mAdapter.getSelectList().size(); i++) {
            Customer customer = new Customer();
            customer.setMobile(mAdapter.getSelectList().get(i).getMobileNumber());
            customer.setName(mAdapter.getSelectList().get(i).getMemberName());
            customer.setInputType("contact");
            customerList.add(customer);
        }
        params.setCustomerList(customerList);
        showProgress("");
        ApiBuilder.create().insertCustomerList(params).setCallback(new PplusCallback<NewResultResponse<Customer>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Customer>> call, NewResultResponse<Customer> response){
                hideProgress();
                showAlert(R.string.msg_added_select_customer);
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(Call<NewResultResponse<Customer>> call, Throwable t, NewResultResponse<Customer> response){
                hideProgress();

            }
        }).build().call();
    }
}
