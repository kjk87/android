package com.pplus.prnumberbiz.apps.common.ui.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.ColorInt;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;
import com.google.android.material.appbar.AppBarLayout;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.igaworks.adbrix.IgawAdbrix;
import com.pple.pplus.utils.part.apps.resource.ResourceUtil;
import com.pple.pplus.utils.part.info.OsUtil;
import com.pple.pplus.utils.part.logs.LogUtil;
import com.pple.pplus.utils.part.utils.StringUtils;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder;
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData;
import com.pplus.prnumberbiz.apps.common.mgmt.DialogManager;
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager;
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
import com.pplus.prnumberbiz.apps.common.ui.common.AlertActivity;

import java.util.ArrayList;

/**
 * <pre>
 *     Application base Activity..
 *     <re>
 *     <b>해당 activity 에서 처리 할 수 있는 작업</b>
 *     <t>
 *        1. 타이틀을 처리 할 수 있도록 구현c
 *        2. 스크린 로그 기록 할 수 있도록
 *     </t>
 *     </re>
 * </pre>
*/
public abstract class BaseActivity extends AppCompatActivity{

    public String LOG_TAG = this.getClass().getSimpleName();

    public abstract String getPID();

    private ToolbarOption toolbarOption;

    private ImplToolbar implToolbar;

    private LayerDrawable oldBackground;

    private Toolbar toolbar = null;

    private FirebaseAnalytics mFirebaseAnalytics;


//    @Override
//    protected void attachBaseContext(Context newBase){
//
//        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState){

        //initActivityOption();

        super.onCreate(savedInstanceState);

        if(!(this instanceof AlertActivity)){
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
            if(LoginInfoManager.getInstance().isMember()){
                mFirebaseAnalytics.setUserId(LoginInfoManager.getInstance().getUser().getLoginId());
            }

            Bundle bundle = new Bundle();
            bundle.putString("screen_name", LOG_TAG);
            mFirebaseAnalytics.logEvent("screen_enter", bundle);
        }



        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        if(OsUtil.isLollipop()){
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        if(this instanceof ImplToolbar) {

            initializeToolbar();

        } else {
            setContentView(getLayoutRes());
            setActionbarColor(Color.BLACK);
        }

        if(StringUtils.isNotEmpty(getPID())) {
            IgawAdbrix.retention(getPID());
        }


        initializeView(savedInstanceState);
    }

    public FirebaseAnalytics getFirebaseAnalytics(){

        return mFirebaseAnalytics;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);

    }


    public <T> T getView(@IdRes int id, Class<T> tClass){

        return (T) findViewById(id);
    }

    @LayoutRes
    public abstract int getLayoutRes();

    public abstract void initializeView(Bundle savedInstanceState);

    public void initializeToolbar(){

        implToolbar = (ImplToolbar) this;

        setContentView(R.layout.activity_base);

        findViewById(R.id.app_main_coordinator_layout).setFitsSystemWindows(true);

        AppBarLayout appBarLayout = getView(R.id.appbar, AppBarLayout.class);

        toolbar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.toolbar, null, false);
        toolbar.setContentInsetsAbsolute(0, 0);

        appBarLayout.addView(toolbar);
        setSupportActionBar(toolbar);

        toolbarOption = implToolbar.getToolbarOption();

        if(toolbarOption != null) {


            if(!toolbarOption.isScrollFlags()) {
                AppBarLayout.LayoutParams toolbarLayoutParams = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
                toolbarLayoutParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
            }

            TextView textTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            textTitle.setSingleLine();

            if(StringUtils.isEmpty(toolbarOption.getTitle())) {
                textTitle.setText("");
            } else {
                textTitle.setText(toolbarOption.getTitle());
            }

            RelativeLayout.LayoutParams titleParams = (RelativeLayout.LayoutParams) textTitle.getLayoutParams();

            switch (toolbarOption.getTitleGravity()) {
                case CENTER:
                    titleParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                    break;
                case LEFT:
                    titleParams.addRule(RelativeLayout.RIGHT_OF, R.id.left_btn_area);
                    break;
                case RIGHT:
                    titleParams.addRule(RelativeLayout.LEFT_OF, R.id.right_btn_area);
                    break;
            }

            textTitle.setLayoutParams(titleParams);

            RelativeLayout leftLinearLayout = (RelativeLayout) toolbar.findViewById(R.id.left_btn_area);

            RelativeLayout rightLinearLayout = (RelativeLayout) toolbar.findViewById(R.id.right_btn_area);

            ToolbarOption.ToolbarMenu[] toolbarMenus = {ToolbarOption.ToolbarMenu.LEFT, ToolbarOption.ToolbarMenu.RIGHT};

            RelativeLayout.LayoutParams params = null;

            for(ToolbarOption.ToolbarMenu menu : toolbarMenus) {

                ArrayList<View> viewArrayList = toolbarOption.getToolbarMenuArrayList(menu);

                switch (menu) {
                    case LEFT:
                        params = (RelativeLayout.LayoutParams) leftLinearLayout.getLayoutParams();
                        leftLinearLayout.setLayoutParams(params);

                        if(viewArrayList != null && !viewArrayList.isEmpty()) {
                            View previousView = null;
                            for(View v : viewArrayList) {
                                v.setOnClickListener(toolbarLeftClickListener);

                                ResourceUtil.setGenerateViewId(v);

                                RelativeLayout.LayoutParams layoutParams = toolbarOption.getViewParams();

                                if(previousView != null) {
                                    layoutParams.addRule(RelativeLayout.RIGHT_OF, previousView.getId());
                                }

                                leftLinearLayout.addView(v, layoutParams);

                                previousView = v;
                            }
                            textTitle.setPadding(0, 0, 0, 0);
                        } else {
                            // 좌측 메뉴가 없다면 패딩을 주도록함!
//                            textTitle.setPadding(getResources().getDimensionPixelSize(R.dimen.width_72), 0, 0, 0);
                        }
                        break;
                    case RIGHT:
                        params = (RelativeLayout.LayoutParams) rightLinearLayout.getLayoutParams();
                        rightLinearLayout.setLayoutParams(params);


                        if(viewArrayList != null && !viewArrayList.isEmpty()) {
                            View previousView = null;
                            for(View v : viewArrayList) {
                                v.setOnClickListener(toolbarRightClickListener);
                                ResourceUtil.setGenerateViewId(v);
                                RelativeLayout.LayoutParams layoutParams = toolbarOption.getViewParams();

                                if(previousView != null) {
                                    layoutParams.addRule(RelativeLayout.RIGHT_OF, previousView.getId());
                                }

                                rightLinearLayout.addView(v, layoutParams);

                                previousView = v;
                            }
                        }
                        break;
                }
            }

            setActionbarColor(toolbarOption.getToolbarBackgroundColor());
        }
        View v = LayoutInflater.from(this).inflate(getLayoutRes(), null, false);
        getView(R.id.main_content, FrameLayout.class).addView(v);
    }



    public void setActionbarColor(@ColorInt int color){

        int orginColor = color;

        if(color == Color.WHITE) {
            color = Color.BLACK;
        }

        Drawable colorDrawable = new ColorDrawable(orginColor);
        //        Drawable bottomDrawable = new ColorDrawable(ResourceUtil.getColor(this, android.R.color.transparent));
        LayerDrawable ld = new LayerDrawable(new Drawable[]{colorDrawable});

        if(oldBackground == null) {
            if(getSupportActionBar() != null) getSupportActionBar().setBackgroundDrawable(ld);
        } else {
            if(getSupportActionBar() != null) {
                TransitionDrawable td = new TransitionDrawable(new Drawable[]{oldBackground, ld});
                getSupportActionBar().setBackgroundDrawable(td);
                td.startTransition(200);
            }
        }
        oldBackground = ld;
    }

    public OnClickListener toolbarLeftClickListener = new OnClickListener(){

        @Override
        public void onClick(View view){

            if(implToolbar.getOnToolbarClickListener() != null) {
                implToolbar.getOnToolbarClickListener().onClick(view, ToolbarOption.ToolbarMenu.LEFT, view.getTag());
            }
        }
    };

    public OnClickListener toolbarRightClickListener = new OnClickListener(){

        @Override
        public void onClick(View view){

            if(implToolbar.getOnToolbarClickListener() != null) {
                implToolbar.getOnToolbarClickListener().onClick(view, ToolbarOption.ToolbarMenu.RIGHT, view.getTag());
            }

        }
    };

    public void setTitle(String title){

        ((TextView) toolbar.findViewById(R.id.toolbar_title)).setText(title);
    }

    public String getToolBarTitle(){

        return ((TextView) toolbar.findViewById(R.id.toolbar_title)).getText().toString();
    }

    public TextView getToolBarTextView(){

        return ((TextView) toolbar.findViewById(R.id.toolbar_title));
    }

    public void showAlert(String message, int line){

        AlertBuilder.Builder builder = new AlertBuilder.Builder();
        builder.setTitle(getString(R.string.word_notice_alert));
        builder.addContents(new AlertData.MessageData(message, AlertBuilder.MESSAGE_TYPE.TEXT, line));
        builder.setRightText(getString(R.string.word_confirm)).builder().show(this);
    }

    public void showAlert(String message){

        showAlert(message, 2);
    }

    public void showAlert(@StringRes int messageId){

        showAlert(getString(messageId), 2);
    }

    public void showAlert(@StringRes int messageId, int line){

        showAlert(getString(messageId), line);
    }

    public Toolbar getToolbar(){

        return toolbar;
    }

    public void showProgress(String msg){

        LogUtil.e(LOG_TAG, "showProgress : {}", msg);
        DialogManager.getInstance().showLoadingDialog(this, msg, false);
    }

    public void hideProgress(){

        LogUtil.e(LOG_TAG, "hideProgress");
        DialogManager.getInstance().hideLoadingDialog(this);
    }

    @Override
    protected void onDestroy(){

        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);
        if(getSupportFragmentManager().getFragments() != null) {
            for(Fragment fragment : getSupportFragmentManager().getFragments()) {
                if(fragment != null) {
                    fragment.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
    }

    @Override
    public void onBackPressed(){

        super.onBackPressed();
    }
}
