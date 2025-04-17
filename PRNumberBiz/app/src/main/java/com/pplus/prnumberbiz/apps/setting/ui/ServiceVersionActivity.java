package com.pplus.prnumberbiz.apps.setting.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.pple.pplus.utils.part.logs.LogUtil;
import com.pple.pplus.utils.part.utils.StringUtils;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.mgmt.AppInfoManager;
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.core.network.ApiBuilder;
import com.pplus.prnumberbiz.core.network.model.dto.AppVersion;
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;

import java.util.HashMap;
import java.util.Map;

import network.common.PplusCallback;
import retrofit2.Call;

/**
 * 버전 정보
 */
public class ServiceVersionActivity extends BaseActivity implements ImplToolbar{

    private static final String TAG = ServiceVersionActivity.class.getSimpleName();

    @Override
    public String getPID(){

        return "";
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_service_version;
    }

    private TextView mTextUpdate;

    @Override
    public void initializeView(Bundle savedInstanceState){

        mTextUpdate = (TextView) findViewById(R.id.text_version_update);

        TextView textCurrentVersion = (TextView) findViewById(R.id.text_version_currentVersion);
        final TextView textLatestVersion = (TextView) findViewById(R.id.text_version_latestVersion);

        textCurrentVersion.setText(AppInfoManager.getInstance().getAppVersion());

        Map<String, String> params = new HashMap<>();
        params.put("appKey", getPackageName());
        params.put("version", AppInfoManager.getInstance().getAppVersion());


        ApiBuilder.create().appVersion(params).setCallback(new PplusCallback<NewResultResponse<AppVersion>>(){

            @Override
            public void onResponse(Call<NewResultResponse<AppVersion>> call, NewResultResponse<AppVersion> response){

                LogUtil.e(LOG_TAG, response.getData().toString());

                AppVersion data = response.getData();

                if(data != null && StringUtils.isNotEmpty(data.getVersion())) {

                    String newVersion = data.getVersion();

                    textLatestVersion.setText(newVersion);

                    int isUpdate = AppInfoManager.getInstance().isVersionUpdate(newVersion);

                    // 여기서는 메이저업데이트와는 상관없이 처리
                    if(isUpdate != -1) {
                        mTextUpdate.setText(R.string.word_update);
                        mTextUpdate.setSelected(true);
                        mTextUpdate.setClickable(true);
                        mTextUpdate.setOnClickListener(new View.OnClickListener(){

                            @Override
                            public void onClick(View view){

                                Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName()));
                                try {
                                    startActivity(it);
                                } catch (ActivityNotFoundException e) {
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<NewResultResponse<AppVersion>> call, Throwable t, NewResultResponse<AppVersion> response){

            }

        }).build().call();
    }

    @NonNull
    @Override
    public ToolbarOption getToolbarOption(){

        ToolbarOption toolbarOption = new ToolbarOption(this);
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_version_info), ToolbarOption.ToolbarMenu.LEFT);
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
