package com.pplus.prnumberbiz.apps.customer.ui;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;

public class CustomerAddActivity extends BaseActivity implements View.OnClickListener{

    @Override
    public String getPID(){

        return null;
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_customer_add;
    }

    @Override
    public void initializeView(Bundle savedInstanceState){

        findViewById(R.id.text_customer_add_load_address).setOnClickListener(this);
        findViewById(R.id.text_customer_add_directly_reg).setOnClickListener(this);
        findViewById(R.id.text_customer_add_cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View view){

        Intent intent = null;
        switch (view.getId()) {
            case R.id.text_customer_add_load_address:
                intent = new Intent(this, SelectContactActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, Const.REQ_CUSTOMER);
                break;
            case R.id.text_customer_add_directly_reg:
                intent = new Intent(this, CustomerDirectRegActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, Const.REQ_CUSTOMER);
                break;
            case R.id.text_customer_add_cancel:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case Const.REQ_CUSTOMER:
                setResult(resultCode);
                finish();
                break;
        }
    }
}
