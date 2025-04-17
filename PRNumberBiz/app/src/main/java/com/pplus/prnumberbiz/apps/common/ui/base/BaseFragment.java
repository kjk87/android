package com.pplus.prnumberbiz.apps.common.ui.base;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.igaworks.adbrix.IgawAdbrix;
import com.pple.pplus.utils.part.logs.LogUtil;
import com.pple.pplus.utils.part.utils.StringUtils;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder;
import com.pplus.prnumberbiz.apps.common.impl.ImplFragment;
import com.pplus.prnumberbiz.apps.common.mgmt.DialogManager;

import java.io.Serializable;


public abstract class BaseFragment<T extends BaseActivity> extends Fragment implements ImplFragment, View.OnClickListener, Serializable{

    public String LOG_TAG = this.getClass().getSimpleName();

    protected View mContainer;

    public abstract String getPID();

    private T activity;

    @Override
    public void onClick(View v){

    }

    @Override
    public void onSaveInstanceState(Bundle state){

        super.onSaveInstanceState(state);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        if(StringUtils.isNotEmpty(getPID())) {
            IgawAdbrix.retention(getPID());
        }


        if(mContainer != null) {
            if(mContainer.getParent() != null)
                ((ViewGroup) mContainer.getParent()).removeView(mContainer);
            return mContainer;
        }

        mContainer = inflater.inflate(getLayoutResourceId(), container, false);

        initializeView(mContainer);

        return mContainer;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){

        super.onActivityCreated(savedInstanceState);

        init();
    }

    public T getParentActivity(){

        if(super.getActivity() != null) {
            if(getActivity() instanceof BaseActivity) {
                activity = (T) getActivity();
            }
        }
        return activity;
    }

    public void showAlert(String message){

        AlertBuilder.Builder builder = new AlertBuilder.Builder();
        builder.setTitle(getString(R.string.word_notice_alert));
        builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE);
        builder.setContents(message);
        builder.builder().show(getActivity());
    }

    public void showAlert(@StringRes int messageId){

        showAlert(getString(messageId));
    }

    public void showProgress(String msg){

        LogUtil.e(LOG_TAG, "showProgress : {}", msg);

        DialogManager.getInstance().showLoadingDialog(getActivity(), msg, false);

    }

    public void hideProgress(){

        LogUtil.e(LOG_TAG, "hideProgress");
        DialogManager.getInstance().hideLoadingDialog(getActivity());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);
        if(getChildFragmentManager().getFragments() != null) {
            for(Fragment fragment : getChildFragmentManager().getFragments()) {
                if(fragment != null) {
                    fragment.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
    }
}
