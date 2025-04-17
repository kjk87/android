package com.pplus.prnumberbiz.apps.offer;

import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

import com.pple.pplus.utils.part.apps.resource.ResourceUtil;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder;
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener;
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.apps.offer.data.OfferAdapter;
import com.pplus.prnumberbiz.core.util.PplusCommonUtil;

public class OfferActivity extends BaseActivity implements ImplToolbar, View.OnClickListener{

    @Override
    public String getPID(){

        return null;
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_offer;
    }

//    private View stickyHolder, layout_history_sticky;
    private TextView textTotalCount, text_sort;//text1month, text3month, text6month, textStartDay, textEndDay
    private View text_not_exsit;

    private OfferAdapter mAdapter;

    @Override
    public void initializeView(Bundle savedInstanceState){

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_offer);
        mAdapter = new OfferAdapter(this);
        recyclerView.setAdapter(mAdapter);

//        LayoutInflater layoutInflater = LayoutInflater.from(this);
//        View headerView = layoutInflater.inflate(R.layout.header_history, null);
//        stickyHolder = headerView.findViewById(R.id.sticky_history_holder);
//        stickyHolder.getLayoutParams().height = getResources().getDimensionPixelSize(R.dimen.height_150);
//        list.addHeaderView(headerView);
//        layout_history_sticky = findViewById(R.id.layout_offer_history_sticky);

        textTotalCount = (TextView) findViewById(R.id.text_offer_history_totalCount);
        textTotalCount.setText(PplusCommonUtil.Companion.fromHtml(getString(R.string.html_offer_total_count, 18)));

        text_sort = (TextView)findViewById(R.id.text_offer_history_sort);
        text_sort.setOnClickListener(this);

//        text1month = (TextView) findViewById(R.id.text_history_1month);
//        text3month = (TextView) findViewById(R.id.text_history_3month);
//        text6month = (TextView) findViewById(R.id.text_history_6month);
//        text1month.setOnClickListener(this);
//        text3month.setOnClickListener(this);
//        text6month.setOnClickListener(this);
//        selectedTextView(text1month, text3month, text6month);

//        textStartDay = (TextView) findViewById(R.id.text_history_startDay);
//        textEndDay = (TextView) findViewById(R.id.text_history_endDay);
//        textStartDay.setOnClickListener(this);
//        textEndDay.setOnClickListener(this);

//        findViewById(R.id.text_history_search).setOnClickListener(this);

        text_not_exsit = findViewById(R.id.text_offer_not_exist);
        text_not_exsit.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view){

        switch (view.getId()) {
            case R.id.text_offer_history_sort:
                AlertBuilder.Builder builder = new AlertBuilder.Builder();
                builder.setContents(getString(R.string.word_sort_recent), getString(R.string.word_sort_price), getString(R.string.word_sort_past));
                builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(new OnAlertResultListener(){

                    @Override
                    public void onCancel(){

                    }

                    @Override
                    public void onResult(AlertBuilder.EVENT_ALERT event_alert){
                        switch (event_alert.getValue()) {
                            case 1:
                                text_sort.setText(getString(R.string.word_sort_recent));
                                break;
                            case 2:
                                text_sort.setText(getString(R.string.word_sort_price));
                                break;
                            case 3:
                                text_sort.setText(getString(R.string.word_sort_past));
                                break;
                        }
                    }
                }).builder().show(this);
                break;
        }
    }

    private void selectedTextView(TextView textView1, TextView textView2, TextView textView3){

        textView1.setSelected(true);
        textView2.setSelected(false);
        textView3.setSelected(false);
        textView1.setTypeface(Typeface.DEFAULT_BOLD);
        textView2.setTypeface(Typeface.DEFAULT);
        textView3.setTypeface(Typeface.DEFAULT);
    }

    @NonNull
    @Override
    public ToolbarOption getToolbarOption(){

        ToolbarOption toolbarOption = new ToolbarOption(this);
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_offer_en), ToolbarOption.ToolbarMenu.LEFT);

        View view = getLayoutInflater().inflate(R.layout.item_top_right, null);
        TextView textView = (TextView) view.findViewById(R.id.text_top_right);
        textView.setText(R.string.word_cash_payment);
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        textView.setTextColor(ResourceUtil.getColor(this, R.color.color_ff4646));
        textView.setBackgroundResource(R.drawable.border_color_ff4646_1px);
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
                }
            }
        };
    }
}
