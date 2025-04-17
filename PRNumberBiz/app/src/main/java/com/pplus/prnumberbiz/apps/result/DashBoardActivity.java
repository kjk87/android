//package com.pplus.prnumberbiz.apps.result;
//
//import android.graphics.Typeface;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.text.Html;
//import android.view.View;
//import android.widget.TextView;
//
//import com.github.mikephil.charting.charts.BarChart;
//import com.github.mikephil.charting.components.AxisBase;
//import com.github.mikephil.charting.components.XAxis;
//import com.github.mikephil.charting.data.BarData;
//import com.github.mikephil.charting.data.BarDataSet;
//import com.github.mikephil.charting.data.BarEntry;
//import com.github.mikephil.charting.data.Entry;
//import com.github.mikephil.charting.formatter.IAxisValueFormatter;
//import com.github.mikephil.charting.formatter.IValueFormatter;
//import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
//import com.github.mikephil.charting.utils.ViewPortHandler;
//import com.pple.pplus.utils.part.apps.resource.ResourceUtil;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
//import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
//import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
//import com.pplus.prnumberbiz.core.util.PplusCommonUtil;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//
//public class DashBoardActivity extends BaseActivity implements ImplToolbar, View.OnClickListener{
//
//    @Override
//    public String getPID(){
//
//        return null;
//    }
//
//    @Override
//    public int getLayoutRes(){
//
//        return R.layout.activity_dash_board;
//    }
//
//    private BarChart mChartDaily;
//    private TextView text_visitor, text_plus, text_share, text_review, text_daily_average, text_daily_average_count, text_weekly_average, text_weekly_average_count;
//    private View layout_dailyTab, layout_weeklyTab, layout_sales_completeTab, layout_order_countTab;
//
//    private IAxisValueFormatter xAxisDailyFormatter = new IAxisValueFormatter(){
//
//        @Override
//        public String getFormattedValue(float value, AxisBase axis){
//
//            Calendar cal = Calendar.getInstance();
//            cal.add(Calendar.DATE, -7);
//            return (int) (cal.get(Calendar.DATE) + value) + getString(R.string.word_day);
//        }
//    };
//
//    private IAxisValueFormatter xAxisWeeklyFormatter = new IAxisValueFormatter(){
//
//        @Override
//        public String getFormattedValue(float value, AxisBase axis){
//
//            Calendar cal = Calendar.getInstance();
//            cal.add(Calendar.WEEK_OF_MONTH, -7);
//            cal.add(Calendar.WEEK_OF_MONTH, (int) value);
//            int month = cal.get(Calendar.MONTH) + 1;
//            int week = cal.get(Calendar.WEEK_OF_MONTH);
//            return month + getString(R.string.word_month) + week + getString(R.string.word_week);
//        }
//    };
//
//    @Override
//    public void initializeView(Bundle savedInstanceState){
//
//        layout_dailyTab = findViewById(R.id.layout_dash_board_daily);
//        layout_weeklyTab = findViewById(R.id.layout_dash_board_weekly);
//        layout_dailyTab.setOnClickListener(this);
//        layout_weeklyTab.setOnClickListener(this);
//        setSelected(layout_dailyTab, layout_weeklyTab);
//
//        text_visitor = (TextView) findViewById(R.id.text_dash_board_visitor);
//        text_plus = (TextView) findViewById(R.id.text_dash_board_plus);
//        text_share = (TextView) findViewById(R.id.text_dash_board_share);
//        text_review = (TextView) findViewById(R.id.text_dash_board_review);
//        text_visitor.setOnClickListener(this);
//        text_plus.setOnClickListener(this);
//        text_share.setOnClickListener(this);
//        text_review.setOnClickListener(this);
//        selectedTextView(text_visitor, text_plus, text_share, text_review);
//
//        text_daily_average = (TextView) findViewById(R.id.text_dash_board_daily_average);
//        text_weekly_average = (TextView) findViewById(R.id.text_dash_board_weekly_average);
//        text_daily_average_count = (TextView) findViewById(R.id.text_dash_board_daily_average_count);
//        text_weekly_average_count = (TextView) findViewById(R.id.text_dash_board_weekly_average_count);
//        text_daily_average.setText(getString(R.string.format_daily_average, getString(R.string.word_visitor)));
//        text_weekly_average.setText(getString(R.string.format_weekly_average, getString(R.string.word_visitor)));
//
//        mChartDaily = (BarChart) findViewById(R.id.chart_dash_board_daily);
//        mChartDaily.setDrawBarShadow(false);
//        mChartDaily.setDrawValueAboveBar(true);
//        mChartDaily.setDrawGridBackground(false);
//        mChartDaily.setDescription(null);
//        mChartDaily.setScaleEnabled(false);
//
//        XAxis xAxisDaily = mChartDaily.getXAxis();
//        xAxisDaily.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxisDaily.setDrawGridLines(false);
//        xAxisDaily.setGranularity(1f); // only intervals of 1 day
//        xAxisDaily.setLabelCount(7);
//        xAxisDaily.setValueFormatter(xAxisDailyFormatter);
//
//        mChartDaily.getAxisLeft().setDrawAxisLine(false);
//        mChartDaily.getAxisLeft().setDrawLabels(false);
//        mChartDaily.getAxisRight().setDrawAxisLine(false);
//        mChartDaily.getAxisRight().setDrawLabels(false);
//        mChartDaily.getLegend().setEnabled(false);
//
//        mChartDaily.setVisibility(View.VISIBLE);
//
//
//        setData(6, 6);
//
//        layout_sales_completeTab = findViewById(R.id.layout_dash_board_sales_complete);
//        layout_order_countTab = findViewById(R.id.layout_dash_board_order_count);
//        layout_sales_completeTab.setOnClickListener(this);
//        layout_order_countTab.setOnClickListener(this);
//        setSelected(layout_sales_completeTab, layout_order_countTab);
//
//        setYesterdayToday();
//
//        TextView text_coupon_back = (TextView) findViewById(R.id.text_dash_board_coupon_back);
//        text_coupon_back.setText(PplusCommonUtil.Companion.fromHtml(getString(R.string.html_back_coupon)));
//
//    }
//
//    private void setYesterdayToday(){
//
//        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.DATE, -1);
//        int yesterdayMonth = cal.get(Calendar.MONTH) + 1;
//        int yesterdayDay = cal.get(Calendar.DATE);
//
//        TextView text_yesterday = (TextView) findViewById(R.id.text_dash_board_yesterday);
//        text_yesterday.setText(getString(R.string.format_yesterday, yesterdayMonth + "." + yesterdayDay));
//
//        cal.add(Calendar.DATE, 1);
//        int month = cal.get(Calendar.MONTH) + 1;
//        int day = cal.get(Calendar.DATE);
//
//        TextView text_today = (TextView) findViewById(R.id.text_dash_board_today);
//        text_today.setText(getString(R.string.format_today, month + "." + day));
//    }
//
//    @Override
//    public void onClick(View view){
//
//        switch (view.getId()) {
//            case R.id.layout_dash_board_daily:
//                setSelected(layout_dailyTab, layout_weeklyTab);
//                mChartDaily.getXAxis().setValueFormatter(xAxisDailyFormatter);
//                mChartDaily.setVisibility(View.GONE);
//                mChartDaily.setVisibility(View.VISIBLE);
//
//                break;
//            case R.id.layout_dash_board_weekly:
//                setSelected(layout_weeklyTab, layout_dailyTab);
//                mChartDaily.getXAxis().setValueFormatter(xAxisWeeklyFormatter);
//                mChartDaily.setVisibility(View.GONE);
//                mChartDaily.setVisibility(View.VISIBLE);
//                break;
//            case R.id.text_dash_board_visitor:
//                selectedTextView(text_visitor, text_plus, text_share, text_review);
//                text_daily_average.setText(getString(R.string.format_daily_average, getString(R.string.word_visitor)));
//                text_weekly_average.setText(getString(R.string.format_weekly_average, getString(R.string.word_visitor)));
//                break;
//            case R.id.text_dash_board_plus:
//                selectedTextView(text_plus, text_visitor, text_share, text_review);
//                text_daily_average.setText(getString(R.string.format_daily_average, getString(R.string.html_follower_total_count)));
//                text_weekly_average.setText(getString(R.string.format_weekly_average, getString(R.string.word_plus)));
//                break;
//            case R.id.text_dash_board_share:
//                selectedTextView(text_share, text_visitor, text_plus, text_review);
//                text_daily_average.setText(getString(R.string.format_daily_average, getString(R.string.word_share)));
//                text_weekly_average.setText(getString(R.string.format_weekly_average, getString(R.string.word_share)));
//                break;
//            case R.id.text_dash_board_review:
//                selectedTextView(text_review, text_visitor, text_plus, text_share);
//                text_daily_average.setText(getString(R.string.format_daily_average, getString(R.string.word_review)));
//                text_weekly_average.setText(getString(R.string.format_weekly_average, getString(R.string.word_review)));
//                break;
//        }
//    }
//
//    private void selectedTextView(TextView textView1, TextView textView2, TextView textView3, TextView textView4){
//
//        textView1.setSelected(true);
//        textView2.setSelected(false);
//        textView3.setSelected(false);
//        textView4.setSelected(false);
//        textView1.setTypeface(Typeface.DEFAULT_BOLD);
//        textView2.setTypeface(Typeface.DEFAULT);
//        textView3.setTypeface(Typeface.DEFAULT);
//        textView4.setTypeface(Typeface.DEFAULT);
//    }
//
//    private void setSelected(View view1, View view2){
//
//        view1.setSelected(true);
//        view2.setSelected(false);
//    }
//
//    private void setData(int count, float range){
//
//        float start = 1f;
//
//        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
//
//        for(int i = (int) start; i < start + count + 1; i++) {
//            float mult = (range + 1);
//            int val = (int) (Math.random() * mult) + 1;
//
//            yVals1.add(new BarEntry(i, val));
//        }
//
//        BarDataSet set1;
//
//        if(mChartDaily.getData() != null && mChartDaily.getData().getDataSetCount() > 0) {
//            set1 = (BarDataSet) mChartDaily.getData().getDataSetByIndex(0);
//            set1.setValues(yVals1);
//            mChartDaily.getData().notifyDataChanged();
//            mChartDaily.notifyDataSetChanged();
//        } else {
//            set1 = new BarDataSet(yVals1, null);
//
//            set1.setDrawIcons(false);
//            set1.setColors(ResourceUtil.getColor(this, R.color.color_444444));
//            set1.setValueFormatter(new IValueFormatter(){
//
//                @Override
//                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler){
//
//                    return (int) value + "";
//                }
//            });
//
//            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
//            dataSets.add(set1);
//
//            BarData data = new BarData(dataSets);
//            data.setValueTextSize(10f);
//            data.setBarWidth(0.7f);
//
//            mChartDaily.setData(data);
//        }
//    }
//
//    @NonNull
//    @Override
//    public ToolbarOption getToolbarOption(){
//
//        ToolbarOption toolbarOption = new ToolbarOption(this);
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_statistics), ToolbarOption.ToolbarMenu.LEFT);
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
