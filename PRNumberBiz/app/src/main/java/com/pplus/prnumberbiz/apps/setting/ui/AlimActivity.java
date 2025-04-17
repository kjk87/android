package com.pplus.prnumberbiz.apps.setting.ui;

import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.CompoundButton;

import com.pple.pplus.utils.part.utils.ArrayUtils;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager;
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.apps.common.ui.custom.SafeSwitchCompat;
import com.pplus.prnumberbiz.core.network.ApiBuilder;
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;

import java.util.HashMap;
import java.util.Map;

import network.common.PplusCallback;
import retrofit2.Call;

/**
 * 알림설정
 */
public class AlimActivity extends BaseActivity implements ImplToolbar, SafeSwitchCompat.OnSafeCheckedListener{

    private SafeSwitchCompat switch_push;
    private SafeSwitchCompat switch_review, switch_reply, switch_plus, switch_note, switch_cash;

    @Override
    public String getPID(){

        return "";
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_alim;
    }

    private static final String ON = "1";
    private static final String OFF = "0";

    public static final int REVIEW = 1;
    public static final int REPLY = 2;
    public static final int PLUS = 5;
    public static final int CASH = 6;
    public static final int NOTE = 7;

    Character[] pushChars;
    private boolean pushActivate;

    @Override
    public void initializeView(Bundle savedInstanceState){

        pushActivate = LoginInfoManager.getInstance().getUser().getDevice().getInstalledApp().isPushActivate();
        String pushMask = LoginInfoManager.getInstance().getUser().getDevice().getInstalledApp().getPushMask();
        pushChars = ArrayUtils.toObject(pushMask.toCharArray());

        switch_push = (SafeSwitchCompat) findViewById(R.id.switch_alim_push);

        switch_review = (SafeSwitchCompat) findViewById(R.id.switch_alim_review);
        switch_reply = (SafeSwitchCompat) findViewById(R.id.switch_alim_reply);
        switch_plus = (SafeSwitchCompat) findViewById(R.id.switch_alim_plus);
        switch_note = (SafeSwitchCompat) findViewById(R.id.switch_alim_note);
        switch_cash = (SafeSwitchCompat) findViewById(R.id.switch_alim_cash);

        switch_push.setSafeCheck(pushActivate, SafeSwitchCompat.IGNORE);
        switchEnable(pushActivate);
        if(pushActivate) {
            switch_review.setSafeCheck(pushChars[REVIEW].toString().equals(ON), SafeSwitchCompat.IGNORE);
            switch_reply.setSafeCheck(pushChars[REPLY].toString().equals(ON), SafeSwitchCompat.IGNORE);
            switch_plus.setSafeCheck(pushChars[PLUS].toString().equals(ON), SafeSwitchCompat.IGNORE);
            switch_note.setSafeCheck(pushChars[NOTE].toString().equals(ON), SafeSwitchCompat.IGNORE);
            switch_cash.setSafeCheck(pushChars[CASH].toString().equals(ON), SafeSwitchCompat.IGNORE);
        } else {
            switch_review.setSafeCheck(false, SafeSwitchCompat.IGNORE);
            switch_reply.setSafeCheck(false, SafeSwitchCompat.IGNORE);
            switch_plus.setSafeCheck(false, SafeSwitchCompat.IGNORE);
            switch_note.setSafeCheck(false, SafeSwitchCompat.IGNORE);
            switch_cash.setSafeCheck(false, SafeSwitchCompat.IGNORE);
        }

        switch_push.setOnSafeCheckedListener(this);
        switch_review.setOnSafeCheckedListener(this);
        switch_reply.setOnSafeCheckedListener(this);
        switch_plus.setOnSafeCheckedListener(this);
        switch_note.setOnSafeCheckedListener(this);
        switch_cash.setOnSafeCheckedListener(this);

    }

    private void switchEnable(boolean enable){

        switch_review.setClickable(enable);
        switch_reply.setClickable(enable);
        switch_plus.setClickable(enable);
        switch_note.setClickable(enable);
        switch_cash.setClickable(enable);
    }

    private void update(){

        Map<String, String> params = new HashMap<>();
        params.put("pushActivate", String.valueOf(pushActivate));

        StringBuilder sb = new StringBuilder(pushChars.length);
        for(Character c : pushChars)
            sb.append(c.charValue());

        final String pushMask = sb.toString();

        params.put("pushMask", pushMask);

        showProgress("");
        ApiBuilder.create().updatePushConfig(params).setCallback(new PplusCallback<NewResultResponse<Object>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){

                hideProgress();
                LoginInfoManager.getInstance().getUser().getDevice().getInstalledApp().setPushActivate(pushActivate);
                LoginInfoManager.getInstance().getUser().getDevice().getInstalledApp().setPushMask(pushMask);
            }

            @Override
            public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response){

                hideProgress();
            }
        }).build().call();
    }

    @NonNull
    @Override
    public ToolbarOption getToolbarOption(){

        ToolbarOption toolbarOption = new ToolbarOption(this);
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_alarm_set), ToolbarOption.ToolbarMenu.LEFT);
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

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck){

        switch (compoundButton.getId()) {
            case R.id.switch_alim_push:

                switch_review.setOnSafeCheckedListener(null);
                switch_reply.setOnSafeCheckedListener(null);
                switch_plus.setOnSafeCheckedListener(null);
                switch_note.setOnSafeCheckedListener(null);
                switch_cash.setOnSafeCheckedListener(null);

                pushActivate = isCheck;
                switchEnable(pushActivate);

                if(pushActivate) {
                    switch_review.setSafeCheck(pushChars[REVIEW].toString().equals(ON), SafeSwitchCompat.IGNORE);
                    switch_reply.setSafeCheck(pushChars[REPLY].toString().equals(ON), SafeSwitchCompat.IGNORE);
                    switch_plus.setSafeCheck(pushChars[PLUS].toString().equals(ON), SafeSwitchCompat.IGNORE);
                    switch_note.setSafeCheck(pushChars[NOTE].toString().equals(ON), SafeSwitchCompat.IGNORE);
                    switch_cash.setSafeCheck(pushChars[CASH].toString().equals(ON), SafeSwitchCompat.IGNORE);
                } else {
                    switch_review.setSafeCheck(false, SafeSwitchCompat.IGNORE);
                    switch_reply.setSafeCheck(false, SafeSwitchCompat.IGNORE);
                    switch_plus.setSafeCheck(false, SafeSwitchCompat.IGNORE);
                    switch_note.setSafeCheck(false, SafeSwitchCompat.IGNORE);
                    switch_cash.setSafeCheck(false, SafeSwitchCompat.IGNORE);
                }

                switch_review.setOnSafeCheckedListener(this);
                switch_reply.setOnSafeCheckedListener(this);
                switch_plus.setOnSafeCheckedListener(this);
                switch_note.setOnSafeCheckedListener(this);
                switch_cash.setOnSafeCheckedListener(this);

                update();
                break;
            case R.id.switch_alim_review:
                if(pushActivate) {
                    check(REVIEW, isCheck);
                    update();
                }
                break;
            case R.id.switch_alim_reply:
                if(pushActivate) {
                    check(REPLY, isCheck);
                    update();
                }
                break;
            case R.id.switch_alim_plus:
                if(pushActivate) {
                    check(PLUS, isCheck);
                    update();
                }
                break;
            case R.id.switch_alim_note:
                if(pushActivate) {
                    check(NOTE, isCheck);
                    update();
                }
                break;
            case R.id.switch_alim_cash:
                if(pushActivate) {
                    check(CASH, isCheck);
                    update();
                }
                break;
        }
    }

    @Override
    public void onAlwaysCalledListener(CompoundButton buttonView, boolean isChecked){

    }

    private void check(int pos, boolean isCheck){

        if(isCheck) {
            pushChars[pos] = ON.charAt(0);
        } else {
            pushChars[pos] = OFF.charAt(0);
        }
    }
}
