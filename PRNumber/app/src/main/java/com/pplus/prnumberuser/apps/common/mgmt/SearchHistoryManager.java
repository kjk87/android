package com.pplus.prnumberuser.apps.common.mgmt;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pplus.utils.part.pref.PreferenceUtil;
import com.pplus.prnumberuser.PRNumberApplication;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by 김종경 on 2016-10-10.
 */

public class SearchHistoryManager{

    // Preference
    public static final String SHARED_SEARCH_HISTORY = "search_history";

    private static final int MAX_COUNT = 10;

    private ArrayList<String> mSearchHistoryList = null;
    private static SearchHistoryManager mSearchHistoryManager = null;

    public static SearchHistoryManager getInstance(){

        if(mSearchHistoryManager == null) {
            mSearchHistoryManager = new SearchHistoryManager();
            mSearchHistoryManager.load();
        }

        return mSearchHistoryManager;
    }

    public ArrayList<String> getSearchHistoryList(){

        return mSearchHistoryList;
    }

    public void add(String word){

        if(mSearchHistoryList == null) {
            mSearchHistoryList = new ArrayList<>();
        }

        if(mSearchHistoryList.contains(word)) {
            mSearchHistoryList.remove(word);
        }
        mSearchHistoryList.add(0, word);
        if(mSearchHistoryList.size() > MAX_COUNT) {
            for(int i = MAX_COUNT; i < mSearchHistoryList.size(); i++) {
                mSearchHistoryList.remove(i);
            }
        }

        save();
    }

    public void delete(String word){

        if(mSearchHistoryList.contains(word)) {
            mSearchHistoryList.remove(word);
        }
        save();
    }

    public void allDelete(){

        mSearchHistoryList = new ArrayList<>();
        save();
    }

    public void save(){

        Type type = new TypeToken<ArrayList<String>>(){

        }.getType();
        String data = new Gson().toJson(mSearchHistoryList, type);

        PreferenceUtil.getDefaultPreference(PRNumberApplication.getContext()).put(SHARED_SEARCH_HISTORY, data);
    }

    public void load(){

        String data = PreferenceUtil.getDefaultPreference(PRNumberApplication.getContext()).getString(SHARED_SEARCH_HISTORY);
        Type type = new TypeToken<ArrayList<String>>(){

        }.getType();

        ArrayList<String> searchHistoryList = new Gson().fromJson(data, type);

        if(searchHistoryList != null) {
            mSearchHistoryList = searchHistoryList;
        } else {
            if(mSearchHistoryList != null) {
                mSearchHistoryList.clear();
            }
        }
    }

}
