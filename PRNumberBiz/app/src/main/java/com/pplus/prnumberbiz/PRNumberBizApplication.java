package com.pplus.prnumberbiz;

import android.app.Activity;
import android.content.Context;
import androidx.multidex.MultiDexApplication;

import com.igaworks.IgawCommon;
import com.pple.pplus.utils.part.info.DeviceUtil;
import com.pple.pplus.utils.part.logs.LogUtil;
import com.pplus.prnumberbiz.apps.common.Foreground;
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager;
import com.pplus.prnumberbiz.core.sns.facebook.FaceBookUtil;
import com.pplus.prnumberbiz.core.sns.kakao.KakaoUtil;
import com.pplus.prnumberbiz.core.sns.twitter.TwitterUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 김종경 on 2016-07-15.
 */

public class PRNumberBizApplication extends MultiDexApplication{

    private String LOG_TAG = this.getClass().getSimpleName();


    private static String SCHEMA_DATA = null;

    public PRNumberBizApplication(){

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
        LoginInfoManager.getInstance();
        KakaoUtil.init(this);
        TwitterUtil.init(this);
        DeviceUtil.initialize(this);
        FaceBookUtil.init(this);

        IgawCommon.autoSessionTracking(this);
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

        PRNumberBizApplication.mActivityList = mActivityList;
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
