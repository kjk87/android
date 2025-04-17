package com.pplus.luckybol;

import android.app.Activity;
import android.content.Context;

import androidx.multidex.MultiDexApplication;

import com.buzzvil.buzzad.benefit.BuzzAdBenefit;
import com.buzzvil.buzzad.benefit.BuzzAdBenefitConfig;
import com.buzzvil.buzzad.benefit.presentation.feed.FeedConfig;
import com.igaworks.v2.core.application.AbxActivityHelper;
import com.igaworks.v2.core.application.AbxActivityLifecycleCallbacks;
import com.pplus.luckybol.apps.common.Foreground;
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager;
import com.pplus.luckybol.core.sns.kakao.KakaoUtil;
import com.pplus.utils.part.info.DeviceUtil;
import com.pplus.utils.part.logs.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 김종경 on 2016-07-15.
 */

public class LuckyBolApplication extends MultiDexApplication {

    private String LOG_TAG = this.getClass().getSimpleName();


    private static String SCHEMA_DATA = null;
    private static String HOMEPAGE_DATA = null;
    private static long PAGE_SEQ = -1;

    public LuckyBolApplication() {

    }

    public static String getSchemaData() {

        return SCHEMA_DATA;
    }

    public static void setSchemaData(String schemaData) {

        SCHEMA_DATA = schemaData;
    }

    private static Context mContext;

    public static Context getContext() {

        return mContext;
    }

    private static List<Activity> mActivityList = new ArrayList<Activity>();

    @Override
    public void onCreate() {

        super.onCreate();
        mContext = this;

        AbxActivityHelper.initializeSdk(this, getString(R.string.adbrix_app_key), getString(R.string.adbrix_secret_key));
        registerActivityLifecycleCallbacks(new AbxActivityLifecycleCallbacks());

        Foreground.init(this);

        final FeedConfig feedConfig = new FeedConfig.Builder(getString(R.string.buzvil_feed_id))
                .build();

        final BuzzAdBenefitConfig buzzAdBenefitConfig = new BuzzAdBenefitConfig.Builder(getApplicationContext())
                .setDefaultFeedConfig(feedConfig)
                .build();

        BuzzAdBenefit.init(getApplicationContext(), buzzAdBenefitConfig);

        //fabricInit();
//        AudienceNetworkAds.initialize(this);
        LoginInfoManager.getInstance();
        KakaoUtil.Companion.init(getContext());
//        FaceBookUtil.init(getContext());
        DeviceUtil.initialize(getContext());

    }

    @Override
    protected void attachBaseContext(Context base) {

        super.attachBaseContext(base);

    }

    @Override
    public void onTerminate() {

        super.onTerminate();
        mActivityList.clear();
    }

    public static List<Activity> getActivityList() {

        return mActivityList;
    }

    public static void setActivityList(List<Activity> mActivityList) {

        LuckyBolApplication.mActivityList = mActivityList;
    }

    public static Activity getCurrentActivity() {

        return mActivityList != null && mActivityList.size() > 0 ? mActivityList.get(mActivityList.size() - 1) : null;
    }


    public static <E> boolean containsInstance(Class<? extends E> clazz) {

        for (Activity e : mActivityList) {
            LogUtil.d("", "e getName = " + e.getClass().getSimpleName());
            if (clazz.isInstance(e)) {
                return true;
            }
        }
        return false;
    }
}
