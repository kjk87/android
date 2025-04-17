package com.pplus.prnumberbiz.core.sns.kakao;

import android.app.Activity;

/**
 * @author leoshin, created at 15. 7. 20..
 */
public class KakaoBaseActivity extends Activity{

    protected static Activity self;

    @Override
    protected void onResume(){

        super.onResume();
        KakaoUtil.setCurrentActivity(this);
        self = KakaoBaseActivity.this;
    }

    @Override
    protected void onPause(){

        clearReferences();
        super.onPause();
    }

    @Override
    protected void onDestroy(){

        clearReferences();
        super.onDestroy();
    }

    private void clearReferences(){

        Activity currActivity = KakaoUtil.getCurrentActivity();
        if(currActivity != null && currActivity.equals(this)) {
            KakaoUtil.setCurrentActivity(null);
        }
    }

}
