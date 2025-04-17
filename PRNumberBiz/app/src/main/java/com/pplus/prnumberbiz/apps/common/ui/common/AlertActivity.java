package com.pplus.prnumberbiz.apps.common.ui.common;

import android.os.Bundle;
import androidx.annotation.ColorRes;

import android.text.Spanned;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.pple.pplus.utils.BusProvider;
import com.pple.pplus.utils.part.apps.resource.ResourceUtil;
import com.pple.pplus.utils.part.utils.NumberUtils;
import com.pple.pplus.utils.part.utils.StringUtils;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder;
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData;
import com.pplus.prnumberbiz.apps.common.component.autofit.AutofitTextView;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.core.util.PplusCommonUtil;

import static com.pplus.prnumberbiz.apps.common.builder.AlertBuilder.STYLE_ALERT.LIST_BOTTOM;
import static com.pplus.prnumberbiz.apps.common.builder.AlertBuilder.STYLE_ALERT.LIST_CENTER;
import static com.pplus.prnumberbiz.apps.common.builder.AlertBuilder.STYLE_ALERT.MESSAGE;

public class AlertActivity extends BaseActivity implements View.OnClickListener{

    //    private AlertData alertData;

    private int fontSize;

    private AlertBuilder.AlertResult alertResult;

    private
    @ColorRes
    int alertMessageColor = R.color.color_343434;

    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        fontSize = getResources().getDimensionPixelSize((R.dimen.textSize_42pt));

        super.onCreate(savedInstanceState);


    }

    @Override
    protected void onPause(){

        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    public String getPID(){

        return null;
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_alert;
    }

    @Override
    public void initializeView(Bundle savedInstanceState){

        alertResult = new AlertBuilder.AlertResult();
        alertResult.setAlertData((AlertData) getIntent().getExtras().getSerializable(AlertBuilder.ALERT_KEYS));

        if(alertResult.getAlertData() != null) {

            this.setFinishOnTouchOutside(alertResult.getAlertData().isBackgroundClickable());

            if(alertResult.getAlertData().isBackgroundClickable())
                findViewById(R.id.top_contents_layout).setOnClickListener(this);

            //@tile empty message hide view..
            TextView textTitle = (TextView) findViewById(R.id.text_alertTitle);
            if(StringUtils.isEmpty(alertResult.getAlertData().getTitle())) {
                findViewById(R.id.text_alertTitle).setVisibility(View.GONE);
                findViewById(R.id.line_alert_title).setVisibility(View.GONE);
            } else {
                textTitle.setText(alertResult.getAlertData().getTitle());
                textTitle.setTextColor(ResourceUtil.getColor(this, alertMessageColor));
            }


            RelativeLayout contentsLayout = (RelativeLayout) findViewById(R.id.text_alertContents_layout);

            AlertData.MessageData[] messageDatas = alertResult.getAlertData().getContents();

            AutofitTextView textContents = null;

            if(alertResult.getAlertData().getStyle_alert() == MESSAGE) {
                for(AlertData.MessageData data : messageDatas) {

                    AutofitTextView previous = textContents;

                    textContents = (AutofitTextView) LayoutInflater.from(AlertActivity.this).inflate(R.layout.item_alert_text, null, false);

                    ResourceUtil.setGenerateViewId(textContents);

                    RelativeLayout.LayoutParams params = null;
                    params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    textContents.setGravity(Gravity.CENTER);
                    textContents.setTextColor(ResourceUtil.getColor(this, alertMessageColor));

                    if(previous != null) {
                        params.addRule(RelativeLayout.BELOW, previous.getId());
                        params.setMargins(0, getResources().getDimensionPixelSize(R.dimen.width_36), 0, 0);
                    }

                    //FIXME Hoon! AUTO FIT TextView size enable..
                    //                    textContents.setSizeToFit();
                    textContents.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
                    //                    textContents.setMinTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimensionPixelSize(R.dimen.textSize_11pt));
                    textContents.setMaxLines(data.getMaxLine());

                    textContents.setLayoutParams(params);
                    switch (data.getMessage_type()) {
                        case HTML:
                            textContents.setText(PplusCommonUtil.Companion.fromHtml(data.getContents()));
                            break;
                        case TEXT:
                            textContents.setText(data.getContents());
                            break;
                    }

                    contentsLayout.addView(textContents, params);
                }
            } else if(alertResult.getAlertData().getStyle_alert() == LIST_CENTER || alertResult.getAlertData().getStyle_alert() == LIST_BOTTOM) {

                if(alertResult.getAlertData().getStyle_alert() == LIST_BOTTOM) {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) findViewById(R.id.layout_alert).getLayoutParams();
                    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    params.setMargins(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.height_50));
                }

                ListView listView = new ListView(this);
                listView.setDivider(null);
                listView.setDividerHeight(0);

                int visibleCnt = 6;

                if(alertResult.getAlertData().getContents().length > visibleCnt) {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) ((visibleCnt + 0.5) * getResources().getDimensionPixelSize(R.dimen.height_152)));
                    listView.setLayoutParams(params);
                }

                ListAdapter listAdapter = new ListAdapter();
                listView.setAdapter(new ListAdapter());
                listView.setOnItemClickListener(listAdapter);

                contentsLayout.setPadding(0, 0, 0, 0);
                contentsLayout.addView(listView);
            } else if(alertResult.getAlertData().getStyle_alert() == AlertBuilder.STYLE_ALERT.LIST_RADIO || alertResult.getAlertData().getStyle_alert() == AlertBuilder.STYLE_ALERT.LIST_RADIO_BOTTOM) {

                // 라디오 그룹 스크롤 기능 추가를 위해 레이아웃 구성합니다.
                if(alertResult.getAlertData().getStyle_alert() == AlertBuilder.STYLE_ALERT.LIST_RADIO_BOTTOM) {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) findViewById(R.id.layout_alert).getLayoutParams();
                    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    params.setMargins(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.height_50));
                }

                LinearLayout linearLayout = new LinearLayout(AlertActivity.this);
                ScrollView scrollView = new ScrollView(AlertActivity.this);

                scrollView.addView(linearLayout);

                radioGroup = new RadioGroup(AlertActivity.this);

                linearLayout.addView(radioGroup);
                //                scrollView.addView(radioGroup);

                int visibleCnt = 6;

                LinearLayout.LayoutParams viewGroupParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                if(alertResult.getAlertData().getContents().length > visibleCnt) {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) ((visibleCnt + 0.5) * getResources().getDimensionPixelSize(R.dimen.height_152)));
                    scrollView.setLayoutParams(params);
                }

                //(RadioGroup) LayoutInflater.from(AlertActivity.this).inflate(R.layout.item_alert_radio, null, false);

                viewGroupParams.setMargins(0, getResources().getDimensionPixelSize(R.dimen.height_67), 0, getResources().getDimensionPixelSize(R.dimen.height_79));
                radioGroup.setLayoutParams(viewGroupParams);

                boolean isSelcted = false;
                int firstId = 0;
                for(int i = 0; i < alertResult.getAlertData().getContents().length; i++) {

                    AlertData.MessageData messageData = alertResult.getAlertData().getContents()[i];

                    RadioButton radioButton = new RadioButton(AlertActivity.this);
                    radioButton.setPadding(getResources().getDimensionPixelSize(R.dimen.width_18), 0, 0, 0);
                    radioButton.setButtonDrawable(R.drawable.radio_alert);
                    radioButton.setCompoundDrawablePadding(getResources().getDimensionPixelSize(R.dimen.width_50));
                    RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.height_67));

                    int marginTop = 0;

                    if(i != 0) {
                        marginTop = getResources().getDimensionPixelSize(R.dimen.width_54);
                    }

                    layoutParams.setMargins(getResources().getDimensionPixelSize(R.dimen.width_77), marginTop, 0, 0);
                    radioButton.setLayoutParams(layoutParams);

                    radioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
                    radioButton.setSingleLine();
                    radioButton.setTextColor(ResourceUtil.getColor(AlertActivity.this, R.color.color_585858));
                    radioButton.setText(messageData.getContents());

                    //                    if(NumberUtils.compare(alertData.getContents().length, i + 1) == 0) {
                    //                        radioButton.setBackgroundResource(R.drawable.btn_alert_list);
                    //                    } else {
                    //                        radioButton.setBackgroundResource(R.drawable.btn_alert_list_underbar);
                    //                    }

                    //                    radioButton.setBackgroundResource(R.drawable.btn_alert_list);

                    radioButton.setId(i);

                    radioGroup.addView(radioButton);

                }

                radioGroup.check(Math.max(alertResult.getAlertData().getStyle_alert().getValue() - 1, 0));


                contentsLayout.setPadding(0, 0, 0, 0);
                contentsLayout.addView(scrollView);
            }

            TextView textLeftBtn = (TextView) findViewById(R.id.text_alertLeftBtn);
            TextView textRightBtn = (TextView) findViewById(R.id.text_alertRightBtn);

            if(StringUtils.isEmpty(alertResult.getAlertData().getLeftText()) && StringUtils.isEmpty(alertResult.getAlertData().getRightText())) {
                switch (alertResult.getAlertData().getStyle_alert()) {
                    case LIST_CENTER:
                    case LIST_BOTTOM:
                        break;
                    case MESSAGE:
                        alertResult.getAlertData().setRightText(getString(R.string.word_confirm));
                        break;
                    case LIST_RADIO:
                    case LIST_RADIO_BOTTOM:
                        alertResult.getAlertData().setRightText(getString(R.string.word_confirm));
                        break;
                }
            }

            if(StringUtils.isEmpty(alertResult.getAlertData().getLeftText()) && StringUtils.isEmpty(alertResult.getAlertData().getRightText())) {
                findViewById(R.id.text_alertLine).setVisibility(View.GONE);
                findViewById(R.id.line_alert_bottom).setVisibility(View.GONE);
                textLeftBtn.setVisibility(View.GONE);
                textRightBtn.setVisibility(View.GONE);
            } else if(StringUtils.isEmpty(alertResult.getAlertData().getLeftText()) || StringUtils.isEmpty(alertResult.getAlertData().getRightText())) {
                findViewById(R.id.text_alertLine).setVisibility(View.GONE);
                textLeftBtn.setVisibility(View.GONE);

                if(StringUtils.isEmpty(alertResult.getAlertData().getLeftText())) {
                    textRightBtn.setText(alertResult.getAlertData().getRightText());
                } else if(StringUtils.isEmpty(alertResult.getAlertData().getRightText())) {
                    textRightBtn.setText(alertResult.getAlertData().getLeftText());
                }
            } else {
                textLeftBtn.setText(alertResult.getAlertData().getLeftText());
                textRightBtn.setText(alertResult.getAlertData().getRightText());
            }

            textLeftBtn.setOnClickListener(this);
            textRightBtn.setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View view){

        switch (view.getId()) {
            case R.id.text_alertLeftBtn:
                alertResult.setEvent_alert(AlertBuilder.EVENT_ALERT.LEFT);
                BusProvider.getInstance().post(alertResult);
                break;
            case R.id.text_alertRightBtn:
                if(alertResult.getAlertData().getStyle_alert() == AlertBuilder.STYLE_ALERT.LIST_RADIO || alertResult.getAlertData().getStyle_alert() == AlertBuilder.STYLE_ALERT.LIST_RADIO_BOTTOM) {

                    RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.text_alertContents_layout);

                    int number = -1;

                    if(radioGroup != null) {
                        number = radioGroup.getCheckedRadioButtonId();
                    } else {
                        number = 0;
                    }

                    AlertBuilder.EVENT_ALERT eventAlert = AlertBuilder.EVENT_ALERT.RADIO;

                    if(number != -1) {
                        eventAlert.setValue(number + 1);
                        alertResult.setEvent_alert(eventAlert);
                        BusProvider.getInstance().post(alertResult);
                        break;
                    } else {
                        return;
                    }
                } else {
                    if(StringUtils.isEmpty(alertResult.getAlertData().getLeftText()) || StringUtils.isEmpty(alertResult.getAlertData().getRightText())) {
                        alertResult.setEvent_alert(AlertBuilder.EVENT_ALERT.SINGLE);
                        BusProvider.getInstance().post(alertResult);
                    } else {
                        alertResult.setEvent_alert(AlertBuilder.EVENT_ALERT.RIGHT);
                        BusProvider.getInstance().post(alertResult);
                    }

                }

                break;
        }
        finish();
    }


    @Override
    public void finish(){

        alertResult.setEvent_alert(AlertBuilder.EVENT_ALERT.CANCEL);
        BusProvider.getInstance().post(alertResult);
        super.finish();
    }

    public class ListAdapter extends BaseAdapter implements AdapterView.OnItemClickListener{

        public ListAdapter(){

        }

        @Override
        public int getCount(){

            return alertResult.getAlertData().getContents().length;
        }

        @Override
        public AlertData.MessageData getItem(int i){

            return alertResult.getAlertData().getContents()[i];
        }

        @Override
        public long getItemId(int i){

            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup){

            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_alert_text_layout, null, false);
            AutofitTextView textContents = (AutofitTextView) v.findViewById(R.id.tv_autofit);

            if(NumberUtils.compare(alertResult.getAlertData().getContents().length, i + 1) == 0) {
                textContents.setBackgroundResource(R.drawable.btn_alert_list);
            } else {
                textContents.setBackgroundResource(R.drawable.btn_alert_list_underbar);
            }

            textContents.setGravity(Gravity.CENTER);
            textContents.setTextColor(ResourceUtil.getColor(viewGroup.getContext(), alertMessageColor));

            //@TODO AUTO FIT TextView size enable..
            //            textContents.setSizeToFit();
            textContents.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
            //            textContents.setMinTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimensionPixelSize(R.dimen.textSize_11pt));
            textContents.setMaxLines(2);

            AlertData.MessageData data = getItem(i);

            switch (data.getMessage_type()) {
                case HTML:
                    Spanned result = null;
                    //                    if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    //                        result = Html.fromHtml(data.getContents(), Html.FROM_HTML_MODE_LEGACY);
                    //                    } else {
                    result = PplusCommonUtil.Companion.fromHtml(data.getContents());
                    //                    }
                    textContents.setText(result);
                    break;
                case TEXT:
                    textContents.setText(data.getContents());
                    break;
            }

            return v;
        }


        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){

            AlertBuilder.EVENT_ALERT event_alert = AlertBuilder.EVENT_ALERT.LIST;
            event_alert.setValue(i + 1);
            alertResult.setEvent_alert(event_alert);
            BusProvider.getInstance().post(alertResult);
            finish();
        }
    }

    @Override
    public void onBackPressed(){

        if(alertResult.getAlertData() != null) {
            if(!alertResult.getAlertData().isAutoCancel()) return;
        }
        super.onBackPressed();
    }
}
