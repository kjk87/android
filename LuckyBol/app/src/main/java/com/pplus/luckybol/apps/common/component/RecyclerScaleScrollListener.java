package com.pplus.luckybol.apps.common.component;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;

import androidx.recyclerview.widget.RecyclerView;

import com.pplus.utils.part.logs.LogUtil;
import com.pplus.luckybol.R;

/**
 * Created by j2n on 2017. 1. 6..
 *
 * recyclerView 에서 scale animation 전용 리스너
 *
 * 타이머에 따른 노출 기능 추가
 *
 * autoVisible = true 1.5초 후 노출되도록 변경
 */
public class RecyclerScaleScrollListener extends RecyclerView.OnScrollListener{

    private View scaleView;

    private boolean animationRun = false;

    private boolean autoVisible = true;

    public RecyclerScaleScrollListener(View scaleView){

        this.scaleView = scaleView;
    }

    public RecyclerScaleScrollListener(View scaleView , boolean autoVisible){

        this.scaleView = scaleView;
        this.autoVisible = autoVisible;
    }

    public void autoVisible(boolean autoVisible){

        this.autoVisible = autoVisible;
    }

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg){

            super.handleMessage(msg);
        }
    };

    private Runnable scaleRunnable = new Runnable(){

        @Override
        public void run(){

            if(autoVisible && !animationRun && !scaleView.isShown()) show();
            else {
                handler.sendEmptyMessageDelayed(0, 1 * 1000);
            }
        }
    };

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState){

        super.onScrollStateChanged(recyclerView, newState);

        switch (newState) {
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE: // 스크롤이 정지되어 있는 상태

//                handler.postDelayed(scaleRunnable , 1500);

                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL: // 스크롤이 터치되어 있을 때

                handler.removeCallbacks(scaleRunnable);

                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_FLING: // 이건 스크롤이 움직이고 있을때

                break;
        }

    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy){

        super.onScrolled(recyclerView, dx, dy);

        int scrolly = recyclerView.computeVerticalScrollOffset();

        int oldScrolly = recyclerView.computeVerticalScrollOffset() - dy;

        if(scrolly > oldScrolly && !animationRun && scaleView.isShown()) {

            hide();

        } else if(scrolly < oldScrolly && !animationRun && !scaleView.isShown()) {

            show();

        }
    }

    private void hide(){

        animationRun = true;

        Animation hideAni = AnimationUtils.loadAnimation(scaleView.getContext(), R.anim.scale_xy_hide);
        hideAni.setAnimationListener(new Animation.AnimationListener(){

            @Override
            public void onAnimationStart(Animation animation){

            }

            @Override
            public void onAnimationEnd(Animation animation){

                scaleView.setVisibility(View.GONE);
                animationRun = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation){

            }
        });
        scaleView.startAnimation(hideAni);
    }

    private void show(){

        if (!scaleView.isEnabled()) {
            return;
        }

        animationRun = true;

        Animation showAni = AnimationUtils.loadAnimation(scaleView.getContext(), R.anim.scale_xy_show);
        showAni.setAnimationListener(new Animation.AnimationListener(){

            @Override
            public void onAnimationStart(Animation animation){
                LogUtil.e("SHOW", "onAnimationStart");
                scaleView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation){

                animationRun = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation){

            }
        });
        scaleView.startAnimation(showAni);
    }
}
