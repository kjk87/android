package com.pplus.prnumberbiz.apps.customer.ui;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import androidx.annotation.NonNull;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.pple.pplus.utils.part.apps.resource.ResourceUtil;
import com.pple.pplus.utils.part.format.FormatUtil;
import com.pple.pplus.utils.part.utils.StringUtils;
import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager;
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.core.code.common.EnumData;
import com.pplus.prnumberbiz.core.database.entity.Contact;
import com.pplus.prnumberbiz.core.network.ApiBuilder;
import com.pplus.prnumberbiz.core.network.model.dto.Customer;
import com.pplus.prnumberbiz.core.network.model.dto.No;
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;

import java.util.HashMap;
import java.util.Map;

import network.common.PplusCallback;
import retrofit2.Call;

public class CustomerDirectRegActivity extends BaseActivity implements ImplToolbar, View.OnClickListener{

    @Override
    public String getPID(){

        return null;
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_customer_direct_reg;
    }

    private EditText edit_customer_name, edit_mobile_number;
    private boolean check = false;
    private String mobileNumber;

    private Customer mCustomer;
    private EnumData.MODE mode;

    @Override
    public void initializeView(Bundle savedInstanceState){

        mCustomer = getIntent().getParcelableExtra(Const.DATA);

        edit_customer_name = (EditText) findViewById(R.id.edit_customer_direct_reg_customer_name);
        edit_customer_name.setSingleLine();
        edit_mobile_number = (EditText) findViewById(R.id.edit_customer_direct_reg_mobile_number);
        edit_mobile_number.setSingleLine();
        findViewById(R.id.text_customer_direct_reg_load_contact).setOnClickListener(this);
        findViewById(R.id.text_customer_direct_reg_duplicate).setOnClickListener(this);

        if(mCustomer != null) {
            mode = EnumData.MODE.UPDATE;
            edit_customer_name.setText(mCustomer.getName());
            edit_mobile_number.setText(mCustomer.getMobile());
            mobileNumber = mCustomer.getMobile();
            setTitle(getString(R.string.msg_modify));
        } else {
            mode = EnumData.MODE.WRITE;
            setTitle(getString(R.string.msg_directly_reg));
        }

    }

    @Override
    public void onClick(View view){

        switch (view.getId()) {
            case R.id.text_customer_direct_reg_duplicate:
                checkMobile();
                break;
            case R.id.text_customer_direct_reg_load_contact:
                Intent i=new Intent(Intent.ACTION_PICK);
                i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(i, Const.REQ_CONTACT);
//                loadContact();
                break;
        }
    }

    private void loadContact(){
        Intent intent = new Intent(this, SelectOneContactActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, Const.REQ_CUSTOMER);
    }

    private void checkMobile(){

        final String number = edit_mobile_number.getText().toString().trim();

        if(mode.equals(EnumData.MODE.UPDATE) && mCustomer.getMobile().equals(number)){
            showAlert(R.string.msg_same_saved_number);
            return;
        }

        if(StringUtils.isEmpty(number)) {
            showAlert(R.string.msg_input_mobile_number);
            return;
        }

        if(!FormatUtil.isCellPhoneNumber(number)){
            showAlert(R.string.msg_invalid_mobile_number);
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put("page.no", "" + LoginInfoManager.getInstance().getUser().getPage().getNo());
        params.put("mobile", number);
        showProgress("");
        ApiBuilder.create().getCustomerByMobile(params).setCallback(new PplusCallback<NewResultResponse<Customer>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Customer>> call, NewResultResponse<Customer> response){

                hideProgress();
                if(response.getData() != null) {
                    check = false;
                    showAlert(R.string.msg_registered_customer);
                } else {
                    check = true;
                    mobileNumber = number;
                    showAlert(R.string.msg_enable_use);
                }
            }

            @Override
            public void onFailure(Call<NewResultResponse<Customer>> call, Throwable t, NewResultResponse<Customer> response){

                hideProgress();
                check = false;
            }
        }).build().call();

    }

    private void updateCustomer(){

        Customer params = new Customer();
        String name = edit_customer_name.getText().toString().trim();
        if(StringUtils.isEmpty(name)) {
            showAlert(R.string.msg_input_customer_name);
            return;
        }
        if(StringUtils.isEmpty(mobileNumber)) {
            showAlert(R.string.msg_input_mobile_number);
            return;
        }

        if(!mCustomer.getMobile().equals(mobileNumber)){
            if(!check) {
                showAlert(R.string.msg_check_duplication_number);
                return;
            }
        }

        params.setName(name);
        params.setPage(new No(LoginInfoManager.getInstance().getUser().getPage().getNo()));
        params.setMobile(mobileNumber);
        params.setInputType("direct");
        params.setNo(mCustomer.getNo());
        showProgress("");
        ApiBuilder.create().updateCustomer(params).setCallback(new PplusCallback<NewResultResponse<Customer>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Customer>> call, NewResultResponse<Customer> response){

                hideProgress();

                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(Call<NewResultResponse<Customer>> call, Throwable t, NewResultResponse<Customer> response){

                hideProgress();
            }
        }).build().call();
    }

    private void insertCustomer(){

        Customer params = new Customer();
        String name = edit_customer_name.getText().toString().trim();
        if(StringUtils.isEmpty(name)) {
            showAlert(R.string.msg_input_customer_name);
            return;
        }

        if(!check) {
            showAlert(R.string.msg_check_duplication_number);
            return;
        }

        if(StringUtils.isEmpty(mobileNumber)) {
            showAlert(R.string.msg_input_mobile_number);
            return;
        }
        params.setName(name);
        params.setPage(new No(LoginInfoManager.getInstance().getUser().getPage().getNo()));
        params.setMobile(mobileNumber);
        params.setInputType("direct");
        showProgress("");
        ApiBuilder.create().insertCustomer(params).setCallback(new PplusCallback<NewResultResponse<Customer>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Customer>> call, NewResultResponse<Customer> response){

                hideProgress();

                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(Call<NewResultResponse<Customer>> call, Throwable t, NewResultResponse<Customer> response){

                hideProgress();
            }
        }).build().call();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case Const.REQ_CUSTOMER:
                if(resultCode == RESULT_OK){
                    if(data != null){
                        Contact contact = data.getParcelableExtra(Const.DATA);
                        edit_customer_name.setText(contact.getMemberName());
                        edit_mobile_number.setText(contact.getMobileNumber());
                        check = false;
                    }
                }
                break;
            case Const.REQ_CONTACT:
                if(resultCode == RESULT_OK){
                    if(data != null){
                        Uri uri = data.getData();
                        if(uri != null){
                            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                            if(cursor != null && cursor.moveToFirst()){
                                int  phoneIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                                int  nameIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                                edit_customer_name.setText(cursor.getString(nameIndex));
                                edit_mobile_number.setText(cursor.getString(phoneIndex).replace("-", ""));
                                cursor.close();
                            }
                        }
                    }
                }

                break;
        }
    }

    @NonNull
    @Override
    public ToolbarOption getToolbarOption(){

        ToolbarOption toolbarOption = new ToolbarOption(this);
        toolbarOption.initializeDefaultToolbar(getString(R.string.msg_directly_reg), ToolbarOption.ToolbarMenu.LEFT);
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
                            switch (mode) {

                                case WRITE:
                                    insertCustomer();
                                    break;
                                case UPDATE:
                                    updateCustomer();
                                    break;
                            }

                        }
                        break;
                }
            }
        };

    }


}
