package com.pplus.prnumberuser;

import android.app.Activity;
import android.content.Context;

import androidx.multidex.MultiDexApplication;

import com.igaworks.v2.core.application.AbxActivityHelper;
import com.igaworks.v2.core.application.AbxActivityLifecycleCallbacks;
import com.pplus.prnumberuser.apps.common.Foreground;
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager;
import com.pplus.prnumberuser.core.database.DBManager;
import com.pplus.prnumberuser.core.sns.facebook.FaceBookUtil;
import com.pplus.prnumberuser.core.sns.kakao.KakaoUtil;
import com.pplus.utils.part.info.DeviceUtil;
import com.pplus.utils.part.logs.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 김종경 on 2016-07-15.
 */

public class PRNumberApplication extends MultiDexApplication{

    private String LOG_TAG = this.getClass().getSimpleName();


    private static String SCHEMA_DATA = null;
    private static String HOMEPAGE_DATA = null;
    private static long PAGE_SEQ = -1;

    public PRNumberApplication(){

    }

    public static String getSchemaData(){

        return SCHEMA_DATA;
    }

    public static void setSchemaData(String schemaData){

        SCHEMA_DATA = schemaData;
    }

    private static Context mContext;

    public static Context getContext(){

        return mContext;
    }

    private static List<Activity> mActivityList = new ArrayList<Activity>();


    @Override
    public void onCreate(){

        super.onCreate();
        mContext = this;

                /* 시스템 커스텀 폰트 사용을 위하여 추가합니다.*/
//        Typekit.getInstance().addNormal(Typekit.createFromAsset(mContext, "fonts/NotoSans-Regular.otf")).addBold(Typekit.createFromAsset(mContext, "fonts/NotoSans-Bold.otf")).addCustom1(Typekit.createFromAsset(mContext, "fonts/NotoSans-Thin.otf"));

        Foreground.init(this);

        AbxActivityHelper.initializeSdk(this, getString(R.string.adbrix_app_key), getString(R.string.adbrix_secret_key));
        registerActivityLifecycleCallbacks(new AbxActivityLifecycleCallbacks());

        //fabricInit();
        LoginInfoManager.getInstance();
        KakaoUtil.Companion.init(getContext());
        FaceBookUtil.init(getContext());
        DeviceUtil.initialize(getContext());
        // adbrix
//        PplusCommonUtil.requestAdbrixApplication(this);

        DBManager.getInstance(this);

    }

    @Override
    protected void attachBaseContext(Context base){

        super.attachBaseContext(base);

    }

    @Override
    public void onTerminate(){

        super.onTerminate();
        mActivityList.clear();
    }

    public static List<Activity> getActivityList(){

        return mActivityList;
    }

    public static void setActivityList(List<Activity> mActivityList){

        PRNumberApplication.mActivityList = mActivityList;
    }

    public static Activity getCurrentActivity(){

        return mActivityList != null && mActivityList.size() > 0 ? mActivityList.get(mActivityList.size() - 1) : null;
    }



    public static <E> boolean containsInstance(Class<? extends E> clazz){

        for(Activity e : mActivityList) {
            LogUtil.d("", "e getName = " + e.getClass().getSimpleName());
            if(clazz.isInstance(e)) {
                return true;
            }
        }
        return false;
    }
}
