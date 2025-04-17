package com.pplus.prnumberbiz.apps.offer;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;

public class OfferDetailActivity extends BaseActivity{

    @Override
    public String getPID(){

        return null;
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_offer_detail;
    }

    @Override
    public void initializeView(Bundle savedInstanceState){

        ListView list = (ListView) findViewById(R.id.list_offer_detail);
        View headerView = getLayoutInflater().inflate(R.layout.item_offer, null);
        list.addHeaderView(headerView);
    }

}
