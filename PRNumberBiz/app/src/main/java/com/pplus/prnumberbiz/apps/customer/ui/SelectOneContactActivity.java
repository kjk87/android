package com.pplus.prnumberbiz.apps.customer.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.pple.pplus.utils.part.apps.resource.ResourceUtil;
import com.pple.pplus.utils.part.utils.NumberUtils;
import com.pple.pplus.utils.part.utils.StringUtils;
import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.apps.customer.data.SelectOneContactAdapter;
import com.pplus.prnumberbiz.core.database.DBManager;
import com.pplus.prnumberbiz.core.database.dao.ContactDao;
import com.pplus.prnumberbiz.core.database.entity.Contact;
import com.pplus.prnumberbiz.core.util.PplusCommonUtil;
import com.pplus.prnumberbiz.core.util.SoundSearcher;

import java.util.List;

public class SelectOneContactActivity extends BaseActivity implements ImplToolbar, View.OnClickListener{

    @Override
    public String getPID(){

        return null;
    }


    @Override
    public int getLayoutRes(){

        return R.layout.activity_select_one_contact;
    }

    private TextView selectCount;
    private EditText edit_search;
    private SelectOneContactAdapter mAdapter;
    private List<Contact> mContactList;
    private String mSearchWord;

    @Override
    public void initializeView(Bundle savedInstanceState){

        edit_search = (EditText) findViewById(R.id.edit_select_one_contact_search);
        selectCount = (TextView) findViewById(R.id.text_select_one_contact_count);


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_select_one_contact);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SelectOneContactAdapter(this);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new SelectOneContactAdapter.OnItemClickListener(){

            @Override
            public void onItemClick(int position){

                mAdapter.setSelectData(position);
                text_top_right.setBackgroundResource(R.drawable.btn_contact_list_sel);

            }
        });

        ContactDao contactDao = DBManager.getInstance(this).getSession().getContactDao();
        mContactList = contactDao.queryBuilder().where(ContactDao.Properties.Delete.eq(false)).orderAsc(ContactDao.Properties.MemberName).list();

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

        setData();
    }

    private void setData(){

        mAdapter.clear();
        for(Contact contact : mContactList) {
            if(NumberUtils.isNumber(mSearchWord)) {
                if(StringUtils.isNotEmpty(mSearchWord) && !SoundSearcher.matchString(contact.getMobileNumber(), mSearchWord)) {
                    continue;
                }
            } else {
                if(StringUtils.isNotEmpty(mSearchWord) && !SoundSearcher.matchString(contact.getMemberName(), mSearchWord)) {
                    continue;
                }
            }

            mAdapter.add(contact);
        }

        selectCount.setText(PplusCommonUtil.Companion.fromHtml(getString(R.string.html_total_count2, mAdapter.getItemCount())));
    }

    @Override
    public void onClick(View view){

        switch (view.getId()) {
            case R.id.image_select_one_contact_search:
                break;
        }
    }

    private TextView text_top_right;

    @NonNull
    @Override
    public ToolbarOption getToolbarOption(){

        ToolbarOption toolbarOption = new ToolbarOption(this);
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_select_contact_customer), ToolbarOption.ToolbarMenu.LEFT);

        View view = getLayoutInflater().inflate(R.layout.item_top_right, null);
        text_top_right = (TextView) view.findViewById(R.id.text_top_right);
        text_top_right.setText(R.string.word_complete);
        text_top_right.setTypeface(text_top_right.getTypeface(), Typeface.BOLD);
        text_top_right.setTextColor(ResourceUtil.getColor(this, R.color.white));
        text_top_right.setBackgroundResource(R.drawable.btn_contact_list_nor);
        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, view, 0);
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
                            if(mAdapter.getSelectData() == null) {
                                showAlert(R.string.msg_select_add_customer);
                                return;
                            }
                            Intent data = new Intent();
                            data.putExtra(Const.DATA, mAdapter.getSelectData());
                            setResult(RESULT_OK, data);
                            finish();
                        }
                        break;
                }
            }
        };

    }
}
