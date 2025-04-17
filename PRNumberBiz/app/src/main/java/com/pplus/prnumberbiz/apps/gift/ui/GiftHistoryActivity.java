//package com.pplus.prnumberbiz.apps.gift.ui;
//
//import android.app.DatePickerDialog;
//import android.content.Intent;
//import android.support.annotation.NonNull;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.AbsListView;
//import android.widget.AdapterView;
//import android.widget.DatePicker;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.pple.pplus.utils.part.logs.LogUtil;
//import com.pple.pplus.utils.part.utils.StringUtils;
//import com.pple.pplus.utils.part.utils.time.DateFormatUtils;
//import com.pplus.prnumberbiz.Const;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder;
//import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener;
//import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
//import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
//import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
//import com.pplus.prnumberbiz.apps.gift.data.GiftHistoryAdapter;
//import com.pplus.prnumberbiz.core.code.common.EnumData;
//import com.pplus.prnumberbiz.core.network.ApiBuilder;
//import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
//import com.pplus.prnumberbiz.core.util.PplusCommonUtil;
//import com.pplus.prnumberbiz.core.util.ToastUtil;
//
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.Map;
//
//import network.common.PplusCallback;
//import retrofit2.Call;
//
//public class GiftHistoryActivity extends BaseActivity implements ImplToolbar, View.OnClickListener{
//
//    @Override
//    public String getPID(){
//
//        return null;
//    }
//
//    @Override
//    public String getSID(){
//
//        return null;
//    }
//
//    @Override
//    public int getLayoutRes(){
//
//        return R.layout.activity_gift_history;
//    }
//
//    private View stickyHolder, layout_sticky;
//    private TextView text_total_count, textStartDay, textEndDay, text_sort, text_not_exist;
//    private GiftHistoryAdapter mAdapter;
//    private int mPage = 1, mTotalCount = 0;
//    private boolean mLockListView = true;
//    private Calendar startCalendar, endCalendar;
//    private boolean mInputStart, mInputEnd;
//    private String mSort, mStart, mEnd;
//
//    @Override
//    public void initializeView(Bundle savedInstanceState){
//
//        Calendar c = Calendar.getInstance();
//        startCalendar = Calendar.getInstance();
//        startCalendar.add(Calendar.DATE, -7);
//        endCalendar = Calendar.getInstance();
//
//        final ListView list = (ListView) findViewById(R.id.list_gift_history);
//        LayoutInflater layoutInflater = LayoutInflater.from(this);
//        View headerView = layoutInflater.inflate(R.layout.header_cash_history, null);
//        stickyHolder = headerView.findViewById(R.id.sticky_cash_history_holder);
//        list.addHeaderView(headerView);
//        layout_sticky = findViewById(R.id.layout_gift_history_sticky);
//        layout_sticky.setVisibility(View.GONE);
//        text_not_exist = (TextView) findViewById(R.id.text_gift_history_notExist);
//
//        text_total_count = (TextView) findViewById(R.id.text_gift_history_total_count);
//        text_total_count.setText(PplusCommonUtil.Companion.fromHtml(getString(R.string.html_total_count3, 0)));
//
//        text_sort = (TextView) findViewById(R.id.text_gift_history_sort);
//        text_sort.setOnClickListener(this);
//
//        textStartDay = (TextView) findViewById(R.id.text_cash_history_startDay);
//        textEndDay = (TextView) findViewById(R.id.text_cash_history_endDay);
//        textStartDay.setOnClickListener(this);
//        textEndDay.setOnClickListener(this);
//
//        findViewById(R.id.text_cash_history_search).setOnClickListener(this);
//
//        mAdapter = new GiftHistoryAdapter(this);
//        list.setAdapter(mAdapter);
//
//        list.setOnScrollListener(new AbsListView.OnScrollListener(){
//
//            @Override
//            public void onScrollStateChanged(AbsListView absListView, int i){
//
//            }
//
//            @Override
//            public void onScroll(AbsListView absListView, int i, int i1, int i2){
//
//                /* Check if the first item is already reached to top.*/
//                if(list.getFirstVisiblePosition() == 0) {
//                    View firstChild = list.getChildAt(0);
//                    int topY = 0;
//                    if(firstChild != null) {
//                        topY = firstChild.getTop();
//                    }
//
//                    int heroTopY = stickyHolder.getTop();
//                    layout_sticky.setY(Math.max(0, heroTopY + topY));
//                }
//            }
//        });
//
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
//
//                int pos = i - list.getHeaderViewsCount();
//                LogUtil.e(LOG_TAG, "pos : {}", pos);
//                Intent intent = new Intent(GiftHistoryActivity.this, GiftHistoryDetailActivity.class);
//                intent.putExtra(Const.DATA, mAdapter.getItem(pos));
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivity(intent);
//            }
//        });
//
//        list.setOnScrollListener(new AbsListView.OnScrollListener(){
//
//            @Override
//            public void onScrollStateChanged(AbsListView absListView, int i){
//
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount){
//
//                /* Check if the first item is already reached to top.*/
//                if(list.getFirstVisiblePosition() == 0) {
//                    View firstChild = list.getChildAt(0);
//                    int topY = 0;
//                    if(firstChild != null) {
//                        topY = firstChild.getTop();
//                    }
//
//                    int heroTopY = stickyHolder.getTop();
//                    layout_sticky.setY(Math.max(0, heroTopY + topY));
//                }
//
//                if(!mLockListView) {
//                    if((totalItemCount < mTotalCount) && (visibleItemCount + firstVisibleItem) >= (totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN)) {
//                        mPage++;
//                        listCall(mPage);
//                    }
//                }
//            }
//        });
//        existData(true);
//
//        mInputStart = true;
//        mInputEnd = true;
//        mSort = "new";
//        initDate();
//        getCount();
//    }
//
//    private void initDate(){
//        mEnd = endCalendar.get(Calendar.YEAR) + "-" + DateFormatUtils.formatTime(endCalendar.get(Calendar.MONTH) + 1) + "-" + DateFormatUtils.formatTime(endCalendar.get(Calendar.DAY_OF_MONTH)) + " 23:59:59";
//        textEndDay.setText(getString(R.string.format_date, String.valueOf(endCalendar.get(Calendar.YEAR)), DateFormatUtils.formatTime(endCalendar.get(Calendar.MONTH) + 1), DateFormatUtils.formatTime(endCalendar.get(Calendar.DAY_OF_MONTH))));
//
//        mStart = startCalendar.get(Calendar.YEAR) + "-" + DateFormatUtils.formatTime(startCalendar.get(Calendar.MONTH) + 1) + "-" + DateFormatUtils.formatTime(startCalendar.get(Calendar.DAY_OF_MONTH)) + " 00:00:00";
//        textStartDay.setText(getString(R.string.format_date, String.valueOf(startCalendar.get(Calendar.YEAR)), DateFormatUtils.formatTime(startCalendar.get(Calendar.MONTH) + 1), DateFormatUtils.formatTime(startCalendar.get(Calendar.DAY_OF_MONTH))));
//    }
//
//    private void existData(boolean isExist){
//
//        if(isExist) {
//            stickyHolder.setVisibility(View.VISIBLE);
//            layout_sticky.setVisibility(View.VISIBLE);
//            text_not_exist.setVisibility(View.GONE);
//        } else {
//            stickyHolder.setVisibility(View.GONE);
//            layout_sticky.setVisibility(View.GONE);
//            text_not_exist.setVisibility(View.VISIBLE);
//        }
//
//    }
//
//    @Override
//    public void onClick(View view){
//
//        switch (view.getId()) {
//            case R.id.text_cash_history_startDay:
//                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener(){
//
//                    @Override
//                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2){
//
//                        startCalendar.set(i, i1, i2);
//                        //                        if(startCalendar.compareTo(mTodayCalendar) > 0){
//                        //                            ToastUtil.show(getActivity(), R.string.msg_input_startday_before_endday);
//                        //                            return;
//                        //                        }
//
//                        if(mInputEnd) {
//                            if(startCalendar.compareTo(endCalendar) > 0) {
//                                ToastUtil.show(GiftHistoryActivity.this, R.string.msg_input_startday_before_endday);
//                                return;
//                            }
//                        }
//                        mInputStart = true;
//
//                        textStartDay.setText(getString(R.string.format_date, String.valueOf(i), DateFormatUtils.formatTime(i1+1), DateFormatUtils.formatTime(i2)));
//                        mStart = i + "-" + DateFormatUtils.formatTime(i1+1) + "-" + DateFormatUtils.formatTime(i2) + " 00:00:00";
//                    }
//                }, startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DAY_OF_MONTH)).show();
//                break;
//            case R.id.text_cash_history_endDay:
//                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener(){
//
//                    @Override
//                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2){
//
//                        endCalendar.set(i, i1, i2);
//                        if(mInputStart) {
//                            if(startCalendar.compareTo(endCalendar) > 0) {
//                                ToastUtil.show(GiftHistoryActivity.this, R.string.msg_input_endday_after_startday);
//                                return;
//                            }
//                        }
//
//                        mInputEnd = true;
//
//                        textEndDay.setText(getString(R.string.format_date, String.valueOf(i), DateFormatUtils.formatTime(i1+1), DateFormatUtils.formatTime(i2)));
//                        mEnd = i + "-" + DateFormatUtils.formatTime(i1+1) + "-" + DateFormatUtils.formatTime(i2) + " 23:59:59";
//                    }
//                }, endCalendar.get(Calendar.YEAR), endCalendar.get(Calendar.MONTH), endCalendar.get(Calendar.DAY_OF_MONTH)).show();
//                break;
//            case R.id.text_cash_history_search:
//                getCount();
//                break;
//            case R.id.text_gift_history_sort:
//                AlertBuilder.Builder builder = new AlertBuilder.Builder();
//                builder.setContents(getString(R.string.word_sort_recent), getString(R.string.word_sort_price), getString(R.string.word_sort_past));
//                builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(new OnAlertResultListener(){
//
//                    @Override
//                    public void onCancel(){
//
//                    }
//
//                    @Override
//                    public void onResult(AlertBuilder.EVENT_ALERT event_alert){
//
//                        switch (event_alert.getValue()) {
//                            case 1:
//                                text_sort.setText(getString(R.string.word_sort_recent));
//                                break;
//                            case 2:
//                                text_sort.setText(getString(R.string.word_sort_price));
//                                break;
//                            case 3:
//                                text_sort.setText(getString(R.string.word_sort_past));
//                                break;
//                        }
//                        getCount();
//                    }
//                }).builder().show(this);
//                break;
//        }
//    }
//
//    private void getCount(){
//        Map<String, String> params = new HashMap<>();
//        StringBuilder builder = new StringBuilder();
//        builder.append(EnumData.BolType.giftBol.name()).append(",");
//        builder.append(EnumData.BolType.giftBols.name());
//        params.put("filter", builder.toString());
//        if(StringUtils.isNotEmpty(mStart)) {
//            params.put("start", mStart);
//        }
//
//        if(StringUtils.isNotEmpty(mEnd)) {
//            params.put("end", mEnd);
//        }
//
//        ApiBuilder.create().getBolHistoryCount(params).setCallback(new PplusCallback<NewResultResponse<Integer>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<Integer>> call, NewResultResponse<Integer> response){
//                mTotalCount = response.getData();
//                text_total_count.setText(PplusCommonUtil.Companion.fromHtml(getString(R.string.html_total_count3, mTotalCount)));
//                mPage = 1;
//                mAdapter.clear();
//                listCall(mPage);
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<Integer>> call, Throwable t, NewResultResponse<Integer> response){
//
//            }
//        }).build().call();
//    }
//
//    private void listCall(int page){
//
//
//        Map<String, String> params = new HashMap<>();
//        StringBuilder builder = new StringBuilder();
//        builder.append(EnumData.BolType.giftBol.name()).append(",");
//        builder.append(EnumData.BolType.giftBols.name());
//        params.put("filter", builder.toString());
//        if(StringUtils.isNotEmpty(mStart)) {
//            params.put("start", mStart);
//        }
//
//        if(StringUtils.isNotEmpty(mEnd)) {
//            params.put("end", mEnd);
//        }
//
//        params.put("align", mSort);
//        params.put("pg", "" + page);
//        mLockListView = true;
//        showProgress("");
//        ApiBuilder.create().getBolHistoryListWithTargetList(params).setCallback(new PplusCallback<NewResultResponse<Bol>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<Bol>> call, NewResultResponse<Bol> response){
//
//                hideProgress();
//                mLockListView = false;
//
//                if(mPage == 1) {
//                    if(response.getDatas().size() > 0) {
//                        existData(true);
//                    } else {
//                        existData(false);
//                    }
//                }
//                mAdapter.addAll(response.getDatas());
//
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<Bol>> call, Throwable t, NewResultResponse<Bol> response){
//
//                hideProgress();
//            }
//        }).build().call();
//    }
//
//    @NonNull
//    @Override
//    public ToolbarOption getToolbarOption(){
//
//        ToolbarOption toolbarOption = new ToolbarOption(this);
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_bol_gift_history), ToolbarOption.ToolbarMenu.LEFT);
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
//                }
//            }
//        };
//    }
//}
