//package com.pplus.prnumberbiz.apps.gift.ui;
//
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.pple.pplus.utils.part.format.FormatUtil;
//import com.pple.pplus.utils.part.utils.StringUtils;
//import com.pple.pplus.utils.part.utils.time.DateFormatUtils;
//import com.pplus.prnumberbiz.Const;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
//import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
//import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
//import com.pplus.prnumberbiz.apps.gift.data.GiftHistoryDetailAdapter;
//import com.pplus.prnumberbiz.core.network.model.dto.Bol;
//import com.pplus.prnumberbiz.core.network.model.dto.User;
//import com.pplus.prnumberbiz.core.util.PplusCommonUtil;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//
//public class GiftHistoryDetailActivity extends BaseActivity implements ImplToolbar{
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
//        return R.layout.activity_gift_history_detail;
//    }
//
//    private GiftHistoryDetailAdapter mAdapter;
//    private Bol mBol;
//
//    @Override
//    public void initializeView(Bundle savedInstanceState){
//
//        mBol = getIntent().getParcelableExtra(Const.DATA);
//
//        final ListView list = (ListView) findViewById(R.id.list_gift_history_detail);
//        LayoutInflater layoutInflater = LayoutInflater.from(this);
//        View headerView = layoutInflater.inflate(R.layout.header_gift_history_detail, null);
//        TextView text_total_point = (TextView) headerView.findViewById(R.id.text_header_gift_history_detail_giftPoint);
//        TextView text_receiver = (TextView)headerView.findViewById(R.id.text_header_gift_history_detail_receiver);
//        TextView text_message_title = (TextView)headerView.findViewById(R.id.text_header_gift_history_detail_message_title);
//        TextView text_message = (TextView)headerView.findViewById(R.id.text_header_gift_history_detail_message);
//        TextView text_date = (TextView)headerView.findViewById(R.id.text_header_gift_history_detail_date);
//        TextView text_receiver_title = (TextView)headerView.findViewById(R.id.text_header_gift_history_detail_receiver_title);
//        list.addHeaderView(headerView);
//
//
//        text_total_point.setText(PplusCommonUtil.Companion.fromHtml(getString(R.string.html_bol, FormatUtil.getMoneyType("" + mBol.getAmount()))));
//        List<User> targetList = mBol.getTargetList();
//        if(targetList.size() == 1) {
//            text_receiver.setText("" + targetList.get(0).getNickname());
//        } else {
//            text_receiver.setText(getString(R.string.format_other, "" + targetList.get(0).getNickname(), targetList.size() - 1));
//        }
//
//        if(StringUtils.isNotEmpty(mBol.getProperties().getMessage())){
//            text_message.setText(mBol.getProperties().getMessage());
//            text_message_title.setVisibility(View.VISIBLE);
//            text_message.setVisibility(View.VISIBLE);
//        }else{
//            text_message_title.setVisibility(View.GONE);
//            text_message.setVisibility(View.GONE);
//        }
//        try {
//            Date d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mBol.getRegDate());
//
//            SimpleDateFormat output = new SimpleDateFormat("yyyy.MM.dd");
//            text_date.setText(output.format(d));
//
//        } catch (Exception e) {
//
//        }
//
//        text_receiver_title.setText(getString(R.string.format_recipient, targetList.size()));
//
//        mAdapter = new GiftHistoryDetailAdapter(this);
//        list.setAdapter(mAdapter);
//        mAdapter.addAll(mBol.getTargetList());
//    }
//
//    @NonNull
//    @Override
//    public ToolbarOption getToolbarOption(){
//
//        ToolbarOption toolbarOption = new ToolbarOption(this);
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_gift_history_detail), ToolbarOption.ToolbarMenu.LEFT);
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
