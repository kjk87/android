package com.pplus.prnumberbiz.apps.common.mgmt;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pple.pplus.utils.part.pref.PreferenceUtil;
import com.pplus.prnumberbiz.PRNumberBizApplication;
import com.pplus.prnumberbiz.core.network.ApiController;
import com.pplus.prnumberbiz.core.network.model.dto.SystemBoard;

import java.lang.reflect.Type;

/**
 * Created by Administrator on 2016-09-08.
 */
public class SystemBoardManager{

    private SystemBoard board;

    public SystemBoard getBoard(){

        return board;
    }

    public void setBoard(SystemBoard board){

        this.board = board;
    }

    // Preference
    public static final String SYSTEM_BOARD = "system_board";

    private static SystemBoardManager mSystemBoardManager = null;

    public static SystemBoardManager getInstance(){

        if(mSystemBoardManager == null) {
            mSystemBoardManager = new SystemBoardManager();
            mSystemBoardManager.load();
        }

        return mSystemBoardManager;
    }

    public void load(){

        String data = PreferenceUtil.getDefaultPreference(PRNumberBizApplication.getContext()).getString(SYSTEM_BOARD);
        Type typeOfSrc = new TypeToken<SystemBoardManager>(){

        }.getType();
        SystemBoardManager manager = new Gson().fromJson(data, typeOfSrc);
        if(manager != null) {
            mSystemBoardManager = manager;
        }
    }

    public void save(){

        Type typeOfSrc = new TypeToken<SystemBoardManager>(){

        }.getType();
        String data = new Gson().toJson(mSystemBoardManager, typeOfSrc);

        PreferenceUtil.getDefaultPreference(PRNumberBizApplication.getContext()).put(SYSTEM_BOARD, data);

        ApiController.getPRNumberService().updateHeaders();
    }

    public void clear(){

        //        PreferenceUtil.getPreference(PPlusApplication.getContext(), SHARED_LOGIN_INFO).clear();
        mSystemBoardManager.setBoard(null);
        mSystemBoardManager.save();
        ApiController.getPRNumberService().updateHeaders();
    }
}
